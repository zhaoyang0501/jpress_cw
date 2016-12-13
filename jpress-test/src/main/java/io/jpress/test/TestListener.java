package io.jpress.test;

import io.jpress.core.Jpress;
import io.jpress.menu.MenuManager;
import io.jpress.message.Actions;
import io.jpress.message.Message;
import io.jpress.message.MessageListener;
import io.jpress.message.annotation.Listener;
//  运行顺序  weight=Listener.DEFAULT_WEIGHT+100
// 注解 配置监听那些事件
@Listener(action={Actions.JPRESS_STARTED,MenuManager.ACTION_INIT_MENU},weight=Listener.DEFAULT_WEIGHT+100)
public class TestListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		
		System.out.println(">>>>>>"+message.getAction());
		
		if(message.getAction().equals(MenuManager.ACTION_INIT_MENU)){   
			MenuManager manager = message.getData(); //获取监听该整体对象
			
			manager.removeMenuGroupById("wechat");  // 删除微信
		}
	//接听事件添加 freemarke 自定义函数	
//		Jpress.addFunction("", function);
		
	}

}
