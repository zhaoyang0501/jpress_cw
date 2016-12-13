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
package io.jpress.jp.wechat;

import com.jfinal.kit.JsonKit;
import io.jpress.core.JBaseController;
import io.jpress.jp.Consts;
import io.jpress.jp.interceptor.UrlIntereptor;
import io.jpress.jp.interceptor.UserStatusInterceptor;
import io.jpress.jp.interceptor.WechatGetOpenIdInterceptor;
import io.jpress.jp.model.Balance;
import io.jpress.jp.model.Contract;
import io.jpress.jp.model.OrdersEnterprise;
import io.jpress.jp.model.PaymentRecords;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.CookieUtils;
import io.jpress.utils.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.sf.ehcache.pool.impl.FromLargestCachePoolEvictor;

import com.alibaba.fastjson.parser.Feature;
import com.jfinal.aop.Before;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.weixin.sdk.api.PaymentApi;
import com.jfinal.weixin.sdk.api.PaymentApi.TradeType;
import com.jfinal.weixin.sdk.kit.IpKit;
import com.jfinal.weixin.sdk.kit.PaymentKit;


@RouterMapping(url = "/wechat/pay")
public class WechatPayController extends JBaseController {

	static Log log = Log.getLog(WechatPayController.class);
//腾讯回调地址
	private static String notify_url = "http://dh.jiajuboshi.com/wechat/pay/callback";

	public void index() {
	//	render("/templates/jp/client/client_recharge.html");
		redirect("/orders/client_recharge");
	}

	// 统一下单文档地址：https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
	@Before({UrlIntereptor.class,WechatGetOpenIdInterceptor.class,UserStatusInterceptor.class})
	public void prepay() {

		String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
		String appid = PropKit.get("wechat_app_id").trim();
		String partner = PropKit.get("wechat_partner").trim();
		String paternerKey = PropKit.get("wechat_paterner_key").trim();

		
		 String amountString = getPara("amount");
		 String tempShouldPay =  getPara("shouldPay");
		 BigInteger setOrdersId = getParaToBigInteger("setOrdersId");
		 BigInteger ordersEnterpriseId = getParaToBigInteger("ordersEnterpriseId");
		 String remark = getPara("remark");
		
		User user = UserQuery.me().findFirstFromMetadata("openId", openId);
		
		if (user == null) {
			renderAjaxResultForError("用户不存在");
			return;
		}
		
		BigDecimal amount = new BigDecimal(amountString);
		if (amount == null) {
			renderAjaxResultForError("请输入金额");
			return;
		}
		
		PaymentRecords pay = new PaymentRecords();
		if(ordersEnterpriseId==null){
			pay.setAmount(amount);
			pay.setCreated(new Date());
			pay.setSetOrdersId(setOrdersId);
			pay.setRemark(remark);
			pay.setUserId(user.getId());
			pay.setStatus(Consts.PAY_STATUS_PREPAY);
			pay.saveOrUpdate();
			
		}else{
			pay.setAmount(amount);
			pay.setCreated(new Date());
//			pay.setSetOrdersId(setOrdersId);
			pay.setOrdersEnterpriseId(ordersEnterpriseId);
			pay.setUserId(user.getId());
			pay.setRemark(remark);
			pay.setStatus(Consts.PAY_STATUS_PREPAY);
			pay.saveOrUpdate();
		}
	
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("mch_id", partner);
		params.put("body", "泉源会计服务支付");
//		params.put("attach", tempShouldPay);
		params.put("out_trade_no", pay.getId().toString());//订单id
//		params.put("total_fee", (Integer.parseInt(amountString) * 100) + "");
		params.put("total_fee", amountString + ""); //一分钱

		String ip = IpKit.getRealIp(getRequest());
		ip = StrKit.isBlank(ip) ? "127.0.0.1" : ip;

		params.put("spbill_create_ip", ip);
		params.put("trade_type", TradeType.JSAPI.name());
		params.put("nonce_str", System.currentTimeMillis() / 1000 + "");
		params.put("notify_url", notify_url);
		params.put("openid", openId);

		String sign = PaymentKit.createSign(params, paternerKey);
		params.put("sign", sign);







		String xmlResult = PaymentApi.pushOrder(params);//微信服务器返回值




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
		String nonceStr = UUID.randomUUID().toString().replace("-", "");

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
		setAttr("timeStamp", timeStamp);
		setAttr("nonceStr", nonceStr);
		setAttr("package", "prepay_id=" + prepay_id);
		setAttr("signType", "MD5");
		setAttr("paySign", packageSign);
		setAttr("payId", pay.getId().toString());


		Map<String,String> attrs = new HashMap<String, String>();
		attrs.put("message","");
		attrs.put("errorCode","0");
		attrs.put("appId",appid);
		attrs.put("timeStamp",timeStamp);
		attrs.put("nonceStr",nonceStr);
		attrs.put("package","prepay_id=" + prepay_id);
		attrs.put("signType","MD5");
		attrs.put("paySign",packageSign);
		attrs.put("payId",pay.getId().toString());


		String json = JsonKit.toJson(attrs);

		renderJson(json);

//		renderJson(new String[]{"message","errorCode","appId","timeStamp","nonceStr","package","signType","paySign","payId"});
	}

	public void checkStatus() {
		BigInteger id = getParaToBigInteger("id");
		PaymentRecords fr = PaymentRecords.DAO.findById(id);
		if (fr != null && PaymentRecords.STATUS_SUCCESS.equals(fr.getStatus())) {
			renderAjaxResultForSuccess();
		} else {
			renderAjaxResultForError();
		}
	}

	public void fverify() {
		String verify = getPara("verify");
		if (verify.equals("yes")) {
			render("/templates/jp/client/clent_success.html");
		} else {
			
			render("/templates/jp/client/clent_fail.html");
		}
	}

	public void callback() {
		// 支付结果通用通知文档:
		// https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
		String xmlMsg = HttpKit.readData(getRequest());
		Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
		String result_code = params.get("result_code");
		// 总金额
		String totalFee = params.get("total_fee");
		// 商户订单号
		final String payId = params.get("out_trade_no");
		// 微信支付订单号
		String transId = params.get("transaction_id");
		// 支付完成时间，格式为yyyyMMddHHmmss
		String timeEnd = params.get("time_end");
		//应缴多少钱
		final String shouldpay = params.get("attach");

		// 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
		// 避免已经成功、关闭、退款的订单被再次更新
		String paternerKey = PropKit.get("wechat_paterner_key").trim();
		if (PaymentKit.verifyNotify(params, paternerKey)) {
			if ("SUCCESS".equals(result_code)) {
				
				Db.tx(new IAtom() {
					@Override
					public boolean run() throws SQLException {
						PaymentRecords  fr = PaymentRecords.DAO.findById(payId);
						
						if(fr == null){
							return false;
						}
						if(!PaymentRecords.STATUS_PREPAY.equals(fr.getStatus())){
							return false;
						}
						fr.setStatus(PaymentRecords.STATUS_SUCCESS);
						fr.setCreated(new Date());
						if(!fr.saveOrUpdate()){
							return false;
						}
						
						//############################################33
						OrdersEnterprise oet = OrdersEnterprise.DAO.findById(fr.getOrdersEnterpriseId());
						oet.setStatus(Consts.ORDERS_STATUS_PAY);
						oet.saveOrUpdate();
						//合同状态
						
						Contract contract = Contract.DAO.findContractByOrdersEnterpriseId(fr.getOrdersEnterpriseId());
						contract.setStatus(Consts.CONTRACT_STATUS_DOING);
						contract.saveOrUpdate();
						
						//已缴费 余额
						Balance balance =Balance.DAO.findBalanceByUserId(fr.getUserId());
						BigDecimal bamount = balance.getAmount().add(fr.getAmount());
						
						if(StringUtils.isNotBlank(shouldpay)){
							BigDecimal shouldPay = new BigDecimal(shouldpay);
							bamount=bamount.subtract(shouldPay);
						}
						
						balance.setAmount(bamount);
						balance.saveOrUpdate();
						renderAjaxResultForSuccess();
						
						
						//#######################################################
						
						Map<String, String> xml = new HashMap<String, String>();
						xml.put("return_code", "SUCCESS");
						xml.put("return_msg", "OK");
						renderText(PaymentKit.toXml(xml));
						return true;
					}
					});

				Map<String, String> xml = new HashMap<String, String>();
				xml.put("return_code", "FAIL");
				xml.put("return_msg", "没有找到该订单");
				renderText(PaymentKit.toXml(xml));
				return;
			}
		}
		renderText("");
	}
}
