package com.eyeself.listener;

import io.jpress.menu.MenuGroup;
import io.jpress.menu.MenuItem;
import io.jpress.menu.MenuManager;
import io.jpress.message.Message;
import io.jpress.message.MessageListener;
import io.jpress.message.annotation.Listener;

@Listener(action = MenuManager.ACTION_INIT_MENU, async = false, weight = Listener.DEFAULT_WEIGHT + 1)
public class MenuInitListener implements MessageListener {

	@Override
	public void onMessage(Message message) {

		MenuManager manager = message.getData();

		manager.removeMenuGroupById("wechat");
		manager.removeMenuGroupById("tools");
		manager.removeMenuGroupById("addon");

		MenuGroup templateMenu = manager.getMenuGroupById("template");
		templateMenu.removeMenuItemById("list");
		templateMenu.removeMenuItemById("install");
		templateMenu.addMenuItem(0,new MenuItem("index", "/admin/eyeself/indexSetting", "首页设置"));

	}

}
