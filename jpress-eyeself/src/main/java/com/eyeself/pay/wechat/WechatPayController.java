/**
 * Copyright (c) 2015-2016, Michael Yang 杨福海 (fuhai999@gmail.com).
 *
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eyeself.pay.wechat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.jfinal.aop.Before;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.api.PaymentApi;
import com.jfinal.weixin.sdk.api.PaymentApi.TradeType;
import com.jfinal.weixin.sdk.kit.IpKit;
import com.jfinal.weixin.sdk.kit.PaymentKit;

import io.jpress.core.JBaseController;
import io.jpress.router.RouterMapping;
import io.jpress.utils.StringUtils;

@RouterMapping(url = "/wechat/pay")
public class WechatPayController extends JBaseController {
	
	static Log log = Log.getLog(WechatPayController.class);

	private static String notify_url = "http://www.xxx.com/wechat/pay/callback";

	public void index() {
		render("/WEB-INF/wechat/factory/recharge.html");
	}

	// 统一下单文档地址：https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
//	@Before({ WechatUserGetInterceptor.class, WechatFactoryContactInterceptor.class })
	public void prepay() {

		String openId = "";//CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
		String appid = PropKit.get("wechat_app_id").trim();
		String partner = PropKit.get("wechat_partner").trim();
		String paternerKey = PropKit.get("wechat_paterner_key").trim();

//		Factory factory = Factory.DAO.findByArator(openId);
//		if (factory == null) {
//			renderAjaxResultForError("factory is null,may user is not logined!");
//			return;
//		}

		String amountString = getPara("amount");
		if (!StringUtils.isNotBlank(amountString)) {
			renderAjaxResultForError("must input amount!");
			return;
		}

		BigDecimal amount = null;
		try {
			amount = new BigDecimal(amountString);
		} catch (Exception e) {
		}

		if (amount == null) {
			renderAjaxResultForError("amount is not BigDecimal data!");
			return;
		}

//		FactoryRecharge fr = new FactoryRecharge();
//		fr.setAmount(amount);
//		fr.setFactoryId(factory.getId());
//		fr.setCreated(new Date());
//		fr.setSource("wechat");
//		fr.setStatus(FactoryRecharge.STATUS_PREPAY);
		
//		fr.save();

		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("mch_id", partner);
		params.put("body", "家具博士充值支付");
//		params.put("out_trade_no", fr.getId().toString());
		params.put("total_fee", (Integer.parseInt(amountString) * 100)+"");

		String ip = IpKit.getRealIp(getRequest());
		ip = StrKit.isBlank(ip) ? "127.0.0.1" :ip;

		params.put("spbill_create_ip", ip);
		params.put("trade_type", TradeType.JSAPI.name());
		params.put("nonce_str", System.currentTimeMillis() / 1000 + "");
		params.put("notify_url", notify_url);
		params.put("openid", openId);

		String sign = PaymentKit.createSign(params, paternerKey);
		params.put("sign", sign);
		String xmlResult = PaymentApi.pushOrder(params);

		Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

		String return_code = result.get("return_code");
		String return_msg = result.get("return_msg");
		if (StrKit.isBlank(return_code) || !"SUCCESS".equals(return_code)) {
			renderAjaxResultForError(return_msg);
			return;
		}
		String result_code = result.get("result_code");
		if (StrKit.isBlank(result_code) || !"SUCCESS".equals(result_code)) {
			renderAjaxResultForError(return_msg);
			return;
		}
		
		String timeStamp = System.currentTimeMillis() / 1000 + "";
		String nonceStr = UUID.randomUUID().toString().replace("-", "") ;
		
		String prepay_id = result.get("prepay_id");
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appId", appid);
		packageParams.put("timeStamp", timeStamp);
		packageParams.put("nonceStr", nonceStr);
		packageParams.put("package", "prepay_id=" + prepay_id);
		packageParams.put("signType", "MD5");
		String packageSign = PaymentKit.createSign(packageParams, paternerKey);
		
		setAttr("message", "");
		setAttr("errorCode", "0");
		setAttr("appId", appid);
		setAttr("timeStamp", timeStamp );
		setAttr("nonceStr", nonceStr);
		setAttr("package", "prepay_id=" + prepay_id);
		setAttr("signType", "MD5");
		setAttr("paySign", packageSign);
//		setAttr("factoryRechargeId", fr.getId().toString());
		
		
		renderJson();
	}

	public void checkStatus() {
//		BigInteger id = getParaToBigInteger("id");
//		FactoryRecharge fr = FactoryRecharge.DAO.findById(id);
//
//		if (fr != null && FactoryRecharge.STATUS_SUCCESS.equals(fr.getStatus())) {
////			renderAjaxResultForSuccess();
//			setAttr("section", "factory");
//			setAttr("url", "/wechat/factory/index");
//			render("/WEB-INF/wechat/_inc/success.html");
//			
//		} else {
////			renderAjaxResultForError();
//			setAttr("msg","亲！充值不成功可能有以下原因</br>1、金额充值与输入的数值不一致；</br>2、网速慢导致网络中断；");
//			setAttr("section", "factory");
//			setAttr("url", "/wechat/factory/index");
//			setAttr("backurl", "/wechat/factory/recharge");
//			render("/WEB-INF/wechat/_inc/unsuccess.html");
//		}
	}

	public void callback() {
		// 支付结果通用通知文档:
		// https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
		String xmlMsg = HttpKit.readData(getRequest());
		System.out.println("支付通知=" + xmlMsg);
		Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);

		String result_code = params.get("result_code");
		// 总金额
		String totalFee = params.get("total_fee");
		// 商户订单号
		String orderId = params.get("out_trade_no");
		// 微信支付订单号
		String transId = params.get("transaction_id");
		// 支付完成时间，格式为yyyyMMddHHmmss
		String timeEnd = params.get("time_end");

		// 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
		// 避免已经成功、关闭、退款的订单被再次更新
		String paternerKey = PropKit.get("wechat_paterner_key").trim();
		if (PaymentKit.verifyNotify(params, paternerKey)) {
			if (("SUCCESS").equals(result_code)) {
//				FactoryRecharge fr = FactoryRecharge.DAO.findById(orderId);
//				if (fr != null) {
//					if (FactoryRecharge.STATUS_PREPAY.equals(fr.getStatus())) {
//						fr.setStatus(FactoryRecharge.STATUS_SUCCESS);
//						fr.saveOrUpdate();
//					}
					Map<String, String> xml = new HashMap<String, String>();
					xml.put("return_code", "SUCCESS");
					xml.put("return_msg", "OK");
					renderText(PaymentKit.toXml(xml));
					return;
//				}

//				Map<String, String> xml = new HashMap<String, String>();
//				xml.put("return_code", "FAIL");
//				xml.put("return_msg", "没有找到该订单");
//				renderText(PaymentKit.toXml(xml));
//				return;
			}
		}
		renderText("");
	}

}
