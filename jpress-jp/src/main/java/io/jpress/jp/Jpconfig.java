package io.jpress.jp;

import com.jfinal.config.Constants;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.kit.PropKit;

import io.jpress.Config;
import io.jpress.interceptor.AdminInterceptor;
import io.jpress.jp.interceptor.JPonlyAdminInterceptor;

public class Jpconfig extends Config {
	
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub
//		PropKit.use("main.properties");//*********************
		super.configConstant(me); //调用父类的
		loadPropertyFile("cron4j.txt");
//		PropKit.use("main.properties");//*********************
		me.setDevMode(true);
		
	}
	
	
	public void configPlugin(Plugins plugins){
		super.configPlugin(plugins);
		Cron4jPlugIn cron4jPlugIn = new Cron4jPlugIn();
//		if (getPropertyToBoolean("SendStudentInfo.enable") == true) {
//			cron4jPlugIn.addTask(getProperty("SendStudentInfo.cron"), new SendMessage());
//		}
//		cron4jPlugIn.addTask(getProperty("SendStudentInfo.cron"), new SendMessage());
		//合同是否过期
			cron4jPlugIn.addTask(getProperty("contract.cron"), new ContractMessage());
		//进项票验票提醒内容	
			cron4jPlugIn.addTask(getProperty("input.cron"), new InputMessage());
		//收单提醒内容小规模
			cron4jPlugIn.addTask(getProperty("collect.cron"), new CollectMessage());
			//收单提醒内容一般纳税人
			cron4jPlugIn.addTask(getProperty("collectx.cron"), new CollectxMessage());
			//抄报税提醒内容
			cron4jPlugIn.addTask(getProperty("caobaotax.cron"), new CaobaotaxMessage());
			//清卡提醒内容
			cron4jPlugIn.addTask(getProperty("clearcard.cron"), new ClearcardMessage());
			//支付方式提醒
			cron4jPlugIn.addTask(getProperty("payment.cron"), new PaymentMessage());

		plugins.add(cron4jPlugIn);
	}
	
	
	@Override
	public void configInterceptor(Interceptors interceptors) {
		// TODO Auto-generated method stub
		super.configInterceptor(interceptors);
		interceptors.add(new JPonlyAdminInterceptor());
		
	}
}
