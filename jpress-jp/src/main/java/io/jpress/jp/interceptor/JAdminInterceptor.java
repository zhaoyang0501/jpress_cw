package io.jpress.jp.interceptor;

import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import io.jpress.interceptor.AdminInterceptor;
import io.jpress.interceptor.InterUtils;
import io.jpress.menu.MenuManager;
import io.jpress.model.User;

public class JAdminInterceptor extends AdminInterceptor {
	public void intercept(Invocation inv) {
		System.err.println("AAAAAAAA"); //我只有这个，让其往下走怎么得不到参数
		
		
Controller controller = inv.getController();
		
		String target = controller.getRequest().getRequestURI();
		String cpath = controller.getRequest().getContextPath();

		if (!target.startsWith(cpath + "/admin")) {
			inv.invoke();
			return;
		}

		controller.setAttr("c", controller.getPara("c"));
		controller.setAttr("p", controller.getPara("p"));
		controller.setAttr("m", controller.getPara("m"));
		controller.setAttr("t", controller.getPara("t"));
		controller.setAttr("s", controller.getPara("s"));
		controller.setAttr("k", controller.getPara("k"));
		controller.setAttr("page", controller.getPara("page"));

		User user = InterUtils.tryToGetUser(inv);
		
		if (user != null && user.isAdministrator()) { //这个为什么直接getRole()就得到了数据
			controller.setAttr("_menu_html", MenuManager.me().generateHtml());
			controller.setAttr("trole", user.getRole());
			inv.invoke();
			return;
		}else if(user != null && "会计经理".equals(user.getRole())){
			controller.setAttr("_menu_html", MenuManager.me().generateHtml());
			controller.setAttr("trole", user.getRole());
			inv.invoke();
			return;
		}else if(user != null && "外勤经理".equals(user.getRole())){
			controller.setAttr("_menu_html", MenuManager.me().generateHtml());
			controller.setAttr("trole", user.getRole());
			inv.invoke();
			return;
		}else if(user != null && "内务经理".equals(user.getRole())){
			controller.setAttr("_menu_html", MenuManager.me().generateHtml());
			controller.setAttr("trole", user.getRole());
			inv.invoke();
			return;
		}else if(user != null && "代理部经理".equals(user.getRole())){
			controller.setAttr("_menu_html", MenuManager.me().generateHtml());
			controller.setAttr("trole", user.getRole());
			inv.invoke();
			return;
		}
		
		controller.redirect("/admin/login");
		
	}
}
