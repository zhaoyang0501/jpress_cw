package io.jpress.jp;

import java.util.List;

import io.jpress.jp.model.Tuser;
import io.jpress.jp.utils.SmsUtils;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

public class InputMessage extends Task {

	public boolean canBePaused() {
		return true;
	}

	public boolean canBeStopped() {
		return true;
	}

	public boolean supportsCompletenessTracking() {
		return true;
	}

	public boolean supportsStatusTracking() {
		return true;
	}

	@Override
	public void execute(TaskExecutionContext context) throws RuntimeException {
//		System.err.println("*************InputMessage发短信了哈哈哈！！！！");
		List<User> users = Tuser.TUSER.findRoleById("一般纳税人");
		for(User u:users){
			String phone=u.getUsername();
			SmsUtils.sendName("SMS_21245169",null, phone);
//			System.err.println("亲，月底将至，属于一般纳税人企业的业主，请尽快根据本月收入情况进行进项税发票认证，若届时未认证或超期认证，将导致税款增加，请注意！如有疑问，请联系您的专属会计或致电咱单位电话：18198281363。");
		}
	}
}
