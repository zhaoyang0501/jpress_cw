package io.jpress.jp.interceptor;

import io.jpress.jp.Consts;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.utils.CookieUtils;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;


public class UserStatusInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		String openId = CookieUtils.get(controller, Consts.ATTR_WECHAT_OPENID);
		User user = UserQuery.me().findFirstFromMetadata("openId", openId);
		if (user != null && "1".equals(user.metadata("user_status"))) {
			inv.invoke();
			return;
		} else {
			inv.getController().redirect("/register/client_user_regsuccess");
		}
	}

}
