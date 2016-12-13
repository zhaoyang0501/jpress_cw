package io.jpress.jp;

import java.util.List;

import io.jpress.jp.model.Tuser;
import io.jpress.jp.utils.SmsUtils;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

public class CaobaotaxMessage extends Task {

	@Override
	public void execute(TaskExecutionContext context) throws RuntimeException {
//		System.err.println("*************CaobaotaxMessage发短信了哈哈哈！！！！");
		// TODO Auto-generated method stub
		List<User> users = Tuser.TUSER.findList();
		for(User u:users){
			String phone=u.getUsername();
			SmsUtils.sendName("SMS_21445001",null, phone);
//			System.err.println("亲，您好！本月报税期已经开始了，需要贵公司协助税控进行抄报，抄报后代账会计将为您进行报税，如不抄报税控器的情况下将影响报税，会导致其他罚款等事件。如有疑问，请联系您的专属会计或致电咱单位电话：18198281363。");
		}
	}

}
