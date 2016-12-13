package io.jpress.jp;

import java.util.List;

import io.jpress.jp.model.Tuser;
import io.jpress.jp.utils.SmsUtils;
import io.jpress.model.User;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

public class CollectMessage extends Task {

	@Override
	public void execute(TaskExecutionContext context) throws RuntimeException {
//		System.err.println("*************CollectMessage发短信了哈哈哈！！！！");
		List<User> users = Tuser.TUSER.findRoleById("小规模纳税人");
		for(User u:users){
			String phone=u.getUsername();
			SmsUtils.sendName("SMS_21305112",null, phone);
//			System.err.println("亲，您好！将至月底，请尽快把本月的各类发票（银行单据、发票、发票汇总表、费用单据等）准备齐全，将有外勤联系收取，如有疑问，请联系您的专属会计或致电咱单位电话：18198281363。感谢您的配合，祝生活愉快！（若无发票的公司可忽略本条信息）");
		}
	}

}
