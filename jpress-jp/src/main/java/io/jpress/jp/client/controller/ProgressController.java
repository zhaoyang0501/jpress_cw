package io.jpress.jp.client.controller;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import io.jpress.core.JBaseController;
import io.jpress.jp.Consts;
import io.jpress.jp.interceptor.UserStatusInterceptor;
import io.jpress.jp.interceptor.WechatGetOpenIdInterceptor;
import io.jpress.jp.model.Progress;
import io.jpress.jp.model.SetProgress;
import io.jpress.model.Content;
import io.jpress.model.User;
import io.jpress.model.query.ContentQuery;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.CookieUtils;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

@Before({WechatGetOpenIdInterceptor.class,UserStatusInterceptor.class})
@RouterMapping(url = "/progress", viewPath = "/templates/jp/client")
public class ProgressController extends JBaseController {
	
	//选择类型
	public void client_select_item(){
		
//		String openId = "c3b36939-9814-4304-ba50-73401e997add";
		String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
		User user = UserQuery.me().findFirstFromMetadata("openId", openId);
		if(user==null){return;}
//		Content con=ContentQuery.me().findFirstByModuleAndUserId(Consts.MODULE_ENTERPRISE, user.getId());

		List<SetProgress> spList=SetProgress.DAO.findAllSetProgress();
		setAttr("spList", spList);
		setAttr("userId", user.getId());
		
	}
	public void client_progress(){
//		String openId =getPara("openId");
//		String openId = "f169245c-cfc9-4be5-96a9-588524c90d4a";
//		User user = UserQuery.me().findFirstFromMetadata("openId", openId);
//		Content con=ContentQuery.me().findFirstByModuleAndUserId(Consts.MODULE_ENTERPRISE, user.getId());
		
//		String openId = "c3b36939-9814-4304-ba50-73401e997add";
		String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
		User user = UserQuery.me().findFirstFromMetadata("openId", openId);
		if(user==null){return;}
		BigInteger progressId = getParaToBigInteger("progressId"); 		
		SetProgress sp = SetProgress.DAO.findById(progressId);
		Progress progt=Progress.DAO.findFirstByContentIdAndProgressId(user.getId(),progressId);
//		BigInteger objectId = getParaToBigInteger("objectId");
//		Content etp =ContentQuery.me().findById(object_id);
//		Content cp=ContentQuery.me().findFirstByModuleAndObjectId(Consts.MODULE_PROGRESS, con.getId());
//		cp.getMetadatas(); // 获取元数据对象
		setAttr("cp", progt);
		setAttr("type", sp.getProgressType());
	}
}
