package io.jpress.jp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.jpress.jp.model.Contract;
import io.jpress.jp.utils.DateUtils;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;



public class ContractMessage  extends Task {
	
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
//		System.err.println("*************ContractMessage发短信了哈哈哈！！！！");
		long dateTime = System.currentTimeMillis(); 
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Contract> contract = Contract.DAO.findContract();
		for(Contract c:contract){
			String dfStr =df.format(c.getContractDeadline());//截止时间
			Date dt1;
			try {
				dt1 = df.parse(dfStr);
				if(dateTime >= dt1.getTime()){
					c.setStatus("2");
					c.saveOrUpdate();
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
}
