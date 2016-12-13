/**
 * Copyright (c) 2015-2016, Michael Yang 杨福海 (fuhai999@gmail.com).
 *
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jpress.jp.message;

import io.jpress.menu.MenuGroup;
import io.jpress.menu.MenuItem;
import io.jpress.menu.MenuManager;
import io.jpress.message.Message;
import io.jpress.message.MessageListener;
import io.jpress.message.annotation.Listener;

@Listener(action = MenuManager.ACTION_INIT_MENU, async = false,weight=51)
public class StatisticsMenuInitListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		Object temp = message.getData();
		if (temp == null && !(temp instanceof MenuManager)) {
			return;
		}

		MenuManager menuMnager = (MenuManager) temp;
	
//		menuMnager.addMenuGroup(createWechatMenuGroup());
		menuMnager.addMenuGroup(0, createWechatMenuGroup());
		menuMnager.addMenuGroup(1, createOrdersMenuGroup());
		menuMnager.addMenuGroup(3, createUserMenuGroup());
//		menuMnager.addMenuGroup(MenuGroup.createBlockGroup()); //加空白条
		
		
		menuMnager.removeMenuGroupById("user");//移除user
		menuMnager.removeMenuGroupById("addon");//移除user
		menuMnager.removeMenuGroupById("option");//移除user
		menuMnager.removeMenuGroupById("tools");//移除user
		menuMnager.removeMenuGroupById("attachment");//移除user
		menuMnager.removeMenuGroupById("template");//移除user
//		menuMnager.removeMenuGroupById("wechat");//移除

	}

	private MenuGroup createUserMenuGroup(){
		MenuGroup group = new MenuGroup("tuser", "fa fa-user", "用户");
		{
			group.addMenuItem(new MenuItem("tu", "/admin/tuser/alluser", "所有用户"));
			group.addMenuItem(new MenuItem("tp", "/admin/tuser/edit", "添加用户"));
		}
		return group;
	}
	private MenuGroup createOrdersMenuGroup(){
		MenuGroup group = new MenuGroup("orders", "fa fa-file-text-o", "订单状态详情");
		{
//			group.addMenuItem(new MenuItem("or", "/admin/orders/orders_list?status=0", "新单子"));
			group.addMenuItem(new MenuItem("oc", "/admin/orders/orders_list?status=1", "新单子"));
			group.addMenuItem(new MenuItem("op", "/admin/orders/orders_list?status=2", "付费单子"));
			group.addMenuItem(new MenuItem("oa", "/admin/orders/orders_vol_list", "自行付费单子"));
			
		}
		return group;
	}
	private MenuGroup createWechatMenuGroup() {
		MenuGroup group = new MenuGroup("data", "fa fa-file-text-o", "数据设置");

		{
//			group.addMenuItem(new MenuItem("ru", "/admin/data/set_userType", "用户类型"));
			group.addMenuItem(new MenuItem("rs", "/admin/data/set_warn_statistics", "上限设置，红线提醒"));
			
			group.addMenuItem(new MenuItem("rp", "/admin/data/set_progress", "进度办理事项"));
			group.addMenuItem(new MenuItem("ro", "/admin/data/set_orders", "下单事项"));
//			group.addMenuItem(new MenuItem("rec", "/admin/data/set_enterprise_change", "下单详情信息"));
			group.addMenuItem(new MenuItem("rec", "/admin/data/set_orders_detail", "公司更变信息"));
			
		}
		return group;
	}

	

}
