package io.jpress.jp.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;



public class UrlIntereptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		controller.setAttr("p", controller.getPara("p"));
		inv.invoke();
	}

}
