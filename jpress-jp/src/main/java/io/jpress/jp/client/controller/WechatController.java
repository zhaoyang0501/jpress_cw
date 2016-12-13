package io.jpress.jp.client.controller;


import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.api.ApiResult;

import io.jpress.core.JBaseController;
import io.jpress.jp.Consts;
import io.jpress.jp.interceptor.UrlIntereptor;
import io.jpress.jp.interceptor.WechatGetOpenIdInterceptor;
import io.jpress.jp.interceptor.WechatUserGetInterceptor;
import io.jpress.jp.model.UserContract;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.CookieUtils;
import io.jpress.utils.StringUtils;
import io.jpress.wechat.WechatUserInterceptor;

@Before(UrlIntereptor.class)
@RouterMapping(url = "/clientWechat", viewPath = "/templates/jp/client")
public class WechatController extends JBaseController {
	private static final Log log = Log.getLog(WechatController.class);

	public void index(){
//		String openId = "c3b36939-9814-4304-ba50-73401e997add";
		String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
		if(StringUtils.isNotBlank(openId)){
			final User user = UserQuery.me().findFirstFromMetadata("openId", openId);
			if(user==null){return;}
			UserContract uc = UserContract.DAO.findByUserId(user.getId());
			setAttr("uc", uc);
			
		}
	}
	public void agent(){
		
	}
	@Before(WechatUserInterceptor.class)
//	@Before(WechatUserGetInterceptor.class)
	public void getCookieByWechat(){
		 String json = getSessionAttr(io.jpress.Consts.SESSION_WECHAT_USER);
		 ApiResult apiResult = ApiResult.create(json);
//		 String openId = result.getStr("openid");
//			CookieUtils.put(this, Consts.ATTR_WECHAT_OPENID, openId);
		 String openId =  apiResult.getStr("openid"); 
		 CookieUtils.put(this, Consts.ATTR_WECHAT_OPENID, openId);
		 
		 redirect("/clientWechat/index");
		 
	}
	
}
