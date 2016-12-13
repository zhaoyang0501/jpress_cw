package io.jpress.jp.utils;



import io.jpress.core.JBaseController;
import io.jpress.utils.StringUtils;
import ip.jpress.jp.notify.sms.ISmsSender;
import ip.jpress.jp.notify.sms.SmsMessage;
import ip.jpress.jp.notify.sms.SmsSenderFactory;

import com.jfinal.kit.PropKit;


public class SmsUtils {

	/**
	 * 生成一个验证码
	 * 
	 * @return
	 */
	public static String randomCode() {
		return String.valueOf(Math.random()).substring(2, 6);
	}

	/**
	 * 生成一个验证码 <br />
	 * 同一个用户，在一次session内，两次生成的验证码应该相同。
	 * 
	 * @param controller
	 * @return
	 */
	public static String randomCode(JBaseController controller) {
		String code = controller.getSessionAttr("_random_code");
		if (StringUtils.isNotBlank(code)) {
			return code;
		}
		code = randomCode();
		controller.setSessionAttr("_random_code", code);
		return code;
	}

	
	/**
	 * 发送验证码，同一个手机号，60秒内只能发送一次，否则发送失败。
	 * @param code 验证码
	 * @param toPhone 接收验证码的手机号
	 * @return 是否发送成功
	 */
	public static boolean sendCode(String code, String toPhone) {

		ISmsSender sender = SmsSenderFactory.createSender();
		SmsMessage sms = new SmsMessage();

		sms.setRec_num(toPhone);
		sms.setTemplate("SMS_11775009");
		sms.setParam("{\"code\":\" : [ " + code + " ] \",\"product\":\"【家具博士】\"}");
		sms.setSign_name("身份验证");
		
		String resultStr = sender.send(sms);
//		System.err.println(resultStr+"aaaaa");
		// {"error_response":{"code":15,"msg":"Remote serviceerror",
		// "sub_code":"isv.BUSINESS_LIMIT_CONTROL","sub_msg":"触发业务流控","request_id":"z26ff3dm7zpy"}}

		// {"alibaba_aliqin_fc_sms_num_send_response":{"result":{"err_code":"0",
		// "model":"101723718071^1102276275273","success":true},"request_id":"z29pwgj6nvy3"}}

		if (resultStr != null && resultStr.contains("alibaba_aliqin_fc_sms_num_send_response")
				&& resultStr.contains("success") && resultStr.contains("true")) {
			return true;
		}

		return false;
	}
	/**
	 * 发送验证码，同一个手机号，60秒内只能发送一次，否则发送失败。
	 * @param code 验证码
	 * @param toPhone 接收验证码的手机号
	 * @return 是否发送成功
	 */
	public static boolean sendName(String SMS,String name, String toPhone) {
		
		ISmsSender sender = SmsSenderFactory.createSender();
		SmsMessage sms = new SmsMessage();
		
		sms.setRec_num(toPhone);
		sms.setTemplate(SMS);
		if(StringUtils.isNotBlank(name)){
			sms.setParam("{\"name\":\"  " + name + "  \"}");
		}
		sms.setSign_name("活动验证");
		
		String resultStr = sender.send(sms);
//		System.err.println(resultStr+"aaaaa");
		// {"error_response":{"code":15,"msg":"Remote serviceerror",
		// "sub_code":"isv.BUSINESS_LIMIT_CONTROL","sub_msg":"触发业务流控","request_id":"z26ff3dm7zpy"}}
		
		// {"alibaba_aliqin_fc_sms_num_send_response":{"result":{"err_code":"0",
		// "model":"101723718071^1102276275273","success":true},"request_id":"z29pwgj6nvy3"}}
		
		if (resultStr != null && resultStr.contains("alibaba_aliqin_fc_sms_num_send_response")
				&& resultStr.contains("success") && resultStr.contains("true")) {
			return true;
		}
		
		return false;
	}

	public static void main(String[] args) {
		PropKit.use("main.properties");
		System.out.println("=======" + sendCode(randomCode(), "18611220550"));
	}

}
