package io.jpress.jp;

import io.jpress.jp.model.EnterpriseInfo;

/**
 * Hello world!
 *
 */
public class Consts {
	public static final String STOCK_HOLDER = "0";//股东
	public static final String STOCK_CORPORATE = "1";//法人
	
	public static final String MODULE_ENTERPRISE = "enterpriseInfo";
	public static final String MODULE_STOCK = "stock";
	public static final String MODULE_PROGRESS = "progress";
	
//	public static final String REGISTER_AUDIT = "0";//注册 审核中  
//	public static final String REGISTER_SUCCESS = "1";//注册成功
	
	public static final String FILE_EXCEL = "excel";//余额表文件
	public static final String FILE_BILL = "bill";//表单
	
//	public static final String STATISTICS_SETTING = "1234"; //收入统计设置
	
	
	public static final String MYINFO_UNFLAG="0";//未读取显示通知
	public static final String MYINFO_FLAG="1";//以读取显示通知
	
	public static final String MYINFO_WPROCESS="加工修理修配红线提醒";
	public static final String MYINFO_WSERVICE="销售服务红线提醒";
	public static final String MYINFO_WSTUFF="销售收入红线提醒";
	public static final String MYINFO_PPROCESS="加工修理修配上限设置";
	public static final String MYINFO_PSERVICE="销售服务上限设置";
	public static final String MYINFO_PSTUFF="销售收入上限设置";
	
	public static final String MYINFO_WPROCESS_TITEL="加工修理修配红线提醒";//标题
	public static final String MYINFO_WSERVICE_TITEL="销售服务红线提醒";
	public static final String MYINFO_WSTUFF_TITEL="销售收入红线提醒";
	public static final String MYINFO_PPROCESS_TITEL="加工修理修配上限设置";
	public static final String MYINFO_PSERVIC_TITELE="销售服务上限设置";
	public static final String MYINFO_PSTUFF_TITEL="销售收入上限设置";
	
//	public static final String MYINFO_WARN="";
//	public static final String MYINFO_WPROCESS_CONTENT="CONTENT加工修理修配红线提醒";//内容
//	public static final String MYINFO_WSERVICE_CONTENT="CONTENT销售服务红线提醒";
//	public static final String MYINFO_WSTUFF_CONTENT="CONTENT销售货物提醒";
//	public static final String MYINFO_PPROCESS_CONTENT="CONTENT加工修理修配上限设置";
//	public static final String MYINFO_PSERVICE_CONTENT="CONTENT销售服务上限设置";
//	public static final String MYINFO_PSTUFF_CONTENT="CONTENT销售货物上限设置";
	
	public static final String USER_STATUS_PASS="1";//用户状态 审核通过
	public static final String USER_STATUS_STOP="0"; // 等待审核
	
//	public static final String ORDERS_STATUS_BOOK="0";//用户下单
	public static final String ORDERS_STATUS_CONTACTED="1";//新单子
	public static final String ORDERS_STATUS_PAY="2";//付费订单
	public static final String ORDERS_STATUS_="3";//用户自行付费
	
	
	public static final String PAY_STATUS_ONCE="0";//付费一次
	public static final String PAY_STATUS_SEASON="1";//付费季
	public static final String PAY_STATUS_HALFYEAR="2";//付费半年
	public static final String PAY_STATUS_YEAR="3";//付费年
	
	
	public static final String CONTRACT_STATUS_START="0";//合同待开始
	public static final String CONTRACT_STATUS_DOING="1";//合同进行中
	public static final String CONTRACT_STATUS_DONE="2";//合同过期
	
	
	public static final String PUSH_PAY="缴费推送";//缴费推送
	public static final String PUSH_CONTRACT="拟定合同";//拟定合同
	public static final String CHANGE_CONTRACT="更改合同";//拟定合同
	
	
	
	public static final String ATTR_WECHAT_OPENID = "_openid";
	public static final String ATTR_WECHAT_NICKNAME = "_nickname";
	public static final String ATTR_WECHAT_AVATAR = "_avatar";
	
	public static final String PAY_STATUS_PREPAY = "prepay";//微信支付准备
	public static final String PAY_STATUS_SUCCESS = "success";//微信支付成功
	
}
