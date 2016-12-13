package io.jpress.jp.interceptor;

import io.jpress.jp.Consts;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.utils.CookieUtils;
import io.jpress.utils.StringUtils;
import io.jpress.wechat.WechatUserInterceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;

public class WechatGetOpenIdInterceptor implements Interceptor {
	private static final Log log = Log.getLog(WechatGetOpenIdInterceptor.class);
	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		controller.setAttr("p", controller.getPara("p"));
		String openId = CookieUtils.get(controller, Consts.ATTR_WECHAT_OPENID);
		
		if(StringUtils.isBlank(openId)){
			inv.getController().redirect("/clientWechat/getCookieByWechat");
			return;
		}
		User user = UserQuery.me().findFirstFromMetadata("openId", openId);
		if (user != null) {
			inv.invoke();
			return;
		} 
		controller.redirect("/register/client_user_stepOne");
	}

}
