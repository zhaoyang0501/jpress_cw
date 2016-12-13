package io.jpress.jp;

import java.util.List;

import io.jpress.jp.model.Tuser;
import io.jpress.jp.utils.SmsUtils;
import io.jpress.model.User;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

public class ClearcardMessage extends Task {

	@Override
	public void execute(TaskExecutionContext context) throws RuntimeException {

//		System.err.println("*************ClearcardMessage发短信了哈哈哈！！！！");
		List<User> users = Tuser.TUSER.findList();
		for(User u:users){
			String phone=u.getUsername();
			SmsUtils.sendName("SMS_21310089",null, phone);
//			System.err.println("感谢亲的配合！本月的缴税已经全部申报完，若您有税控器，请您于15号以前清卡或反写，以免税控被锁死。为了避免给您造成不必要的麻烦，请一定要及时清卡！已经操作的客户，可忽略此条信息提醒。如有疑问，请联系您的专属会计或致电咱单位电话：18198281363。感谢您对我们的信任与支持！");
		}		
	}

}
