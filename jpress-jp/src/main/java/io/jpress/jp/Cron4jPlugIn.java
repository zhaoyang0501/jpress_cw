package io.jpress.jp;

import com.jfinal.plugin.IPlugin;

import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;



public class Cron4jPlugIn implements  IPlugin {
	private final Scheduler scheduler = new Scheduler();

	@Override
	public boolean start() {
		scheduler.start();
		return true;
	}

	@Override
	public boolean stop() {
		scheduler.stop();
		return true;
	}

	public void addTask(String cronExpress, Runnable task) {
		scheduler.schedule(cronExpress, task);
	}
	
	public void addTask(String cronExpress,Task task){
		scheduler.schedule(cronExpress, task);
	}

	
}
