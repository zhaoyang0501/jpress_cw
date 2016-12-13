package io.jpress.jp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.jpress.jp.freemarker.function.OrderEnterpriseValue;
import io.jpress.jp.model.Contract;
import io.jpress.jp.model.Myinfo;
import io.jpress.jp.model.OrdersEnterprise;
import io.jpress.jp.model.Tuser;
import io.jpress.jp.utils.DateUtils;
import io.jpress.jp.utils.SmsUtils;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

public class PaymentMessage extends Task {

	@Override
	public void execute(TaskExecutionContext context) throws RuntimeException {
		System.err.println("*************ContractMessage发短信了哈哈哈！！！！");
	
		String dc =DateUtils.date("yyyy-MM");
		int dcy=Integer.parseInt(dc.substring(0, 4));
		int dcm=Integer.parseInt(dc.substring(5, 7));
		List<Contract> contract = Contract.DAO.findContractWithPay();
		for(Contract c:contract){
			int dly=Integer.parseInt(c.getContractDeadline().toString().substring(0, 4));
			int dlm=Integer.parseInt(c.getContractDeadline().toString().substring(5, 7));
			int dy=Integer.parseInt(c.getContractDate().toString().substring(0, 4));
			int dm=Integer.parseInt(c.getContractDate().toString().substring(5, 7));
			int totalMonth=(dly-dy)*12+(dlm-dm);// 到期月份----- 总月份数
			int dcmonth = (dcy-dy)*12+(dcm-dm);//当前月份 的月份数
			int times = 0 ;//次数
			switch (c.getPayStatus()) {
			case "1":
				times =totalMonth/3;
				for(int i=1;i<=times;i++){
					if(dcmonth==(i*3)){
						OrdersEnterprise oe =OrdersEnterprise.DAO.findById(c.getOrdersEnterpriseId());
						Myinfo myinfo = new Myinfo();
						myinfo.setUserId(oe.getUserId());
						myinfo.setType(Consts.PUSH_PAY);
						myinfo.setTitel(Consts.PUSH_PAY);
						myinfo.setContent("您的季付账单已出，合同号为："+c.getId()+",请缴费");
						myinfo.setCreated(new Date());
						myinfo.saveOrUpdate();
						
						User user = UserQuery.me().findById(oe.getUserId());
						SmsUtils.sendName("SMS_21425006",c.getId().toString(), user.getUsername());
						
//						System.err.println("贵州泉源会计服务有限公司友情提醒：您的季付账单已出，合同号为："+c.getId()+",请及时缴费！");//短信
						
						break;
					}
				}
				break;
			case "2":
				times =totalMonth/6;
				for(int i=1;i<=times;i++){
					if(dcmonth==(i*6)){
						OrdersEnterprise oe =OrdersEnterprise.DAO.findById(c.getOrdersEnterpriseId());
						Myinfo myinfo = new Myinfo();
						myinfo.setUserId(oe.getUserId());
						myinfo.setType(Consts.PUSH_PAY);
						myinfo.setTitel(Consts.PUSH_PAY);
						myinfo.setContent("您的半年账单已出，合同号为："+c.getId()+",请缴费");
						myinfo.setCreated(new Date());
						myinfo.saveOrUpdate();
						User user = UserQuery.me().findById(oe.getUserId());
						SmsUtils.sendName("SMS_21330138",c.getId().toString(), user.getUsername());
//						System.err.println("贵州泉源会计服务有限公司友情提醒：您的半年付账单已出，合同号为："+c.getId()+",请及时缴费！");//短信
						break;
					}
				}
				break;
			case "3":
				times =totalMonth/12;
				for(int i=1;i<=times;i++){
					if(dcmonth==(i*12)){
						OrdersEnterprise oe =OrdersEnterprise.DAO.findById(c.getOrdersEnterpriseId());
						Myinfo myinfo = new Myinfo();
						myinfo.setUserId(oe.getUserId());
						myinfo.setType(Consts.PUSH_PAY);
						myinfo.setTitel(Consts.PUSH_PAY);
						myinfo.setContent("您的年账单已出，合同号为："+c.getId()+",请缴费");
						myinfo.setCreated(new Date());
						myinfo.saveOrUpdate();
						User user = UserQuery.me().findById(oe.getUserId());
						SmsUtils.sendName("SMS_21290138",c.getId().toString(), user.getUsername());
//						System.err.println("贵州泉源会计服务有限公司友情提醒：您的年付账单已出，合同号为："+c.getId()+",请及时缴费！");
						break;
					}
				}
				break;
			default:
				break;
			}
		}
	}

}
