package io.jpress.jp.interceptor;

import io.jpress.interceptor.InterUtils;
import io.jpress.menu.MenuManager;
import io.jpress.model.User;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class JonlyAdminInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		User user = InterUtils.tryToGetUser(inv);
		
		
		if (user != null && user.isAdministrator()) { //这个为什么直接getRole()就得到了数据
			controller.setAttr("_menu_html", MenuManager.me().generateHtml());
			controller.setAttr("trole", user.getRole());
			inv.invoke();
			return;
		}
		controller.redirect("/admin/tuser/limits");

	}

}
