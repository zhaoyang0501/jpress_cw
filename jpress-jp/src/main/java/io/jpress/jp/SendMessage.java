package io.jpress.jp;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;



public class SendMessage  extends Task {
	
	


	@Override
	public void execute(TaskExecutionContext context) throws RuntimeException {
		// TODO Auto-generated method stub
		System.err.println("*************发短信了哈哈哈！！！！");
	}

	

}
