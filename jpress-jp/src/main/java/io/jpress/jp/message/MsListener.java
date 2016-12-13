package io.jpress.jp.message;

import io.jpress.core.Jpress;
import io.jpress.jp.freemarker.function.EstockValue;
import io.jpress.jp.freemarker.function.OrderEnterpriseValue;
import io.jpress.jp.freemarker.function.OrderValue;
import io.jpress.jp.freemarker.function.ProgressValue;
import io.jpress.jp.freemarker.function.StringIntValue;
import io.jpress.jp.freemarker.tag.StockTag;
import io.jpress.jp.freemarker.tag.UserTag;
import io.jpress.message.Actions;
import io.jpress.message.Message;
import io.jpress.message.MessageListener;
import io.jpress.message.annotation.Listener;

//运行顺序  weight=Listener.DEFAULT_WEIGHT+100
//注解 配置监听那些事件
@Listener(action={Actions.JPRESS_STARTED},weight=Listener.DEFAULT_WEIGHT+100)
public class MsListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		Jpress.addFunction("estock", new EstockValue());
		Jpress.addFunction("progress", new ProgressValue());
		Jpress.addFunction("StringInt", new StringIntValue());
		Jpress.addFunction("orderEnterprise", new OrderEnterpriseValue());
		Jpress.addFunction("order", new OrderValue());
		
		Jpress.addTag("stocks", new StockTag());
		Jpress.addTag("user", new UserTag());
		
	}

}
