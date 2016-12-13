package io.jpress.jp.client.controller;

import io.jpress.core.JBaseCRUDController;
import io.jpress.jp.Consts;
import io.jpress.jp.interceptor.UrlIntereptor;
import io.jpress.jp.interceptor.WechatGetOpenIdInterceptor;
import io.jpress.model.Content;
import io.jpress.model.User;
import io.jpress.model.query.ContentQuery;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.CookieUtils;
import io.jpress.utils.StringUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import com.jfinal.aop.Before;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

@Before(UrlIntereptor.class)
@RouterMapping(url = "/register", viewPath = "/templates/jp/client")
public class RegisterController extends JBaseCRUDController<Content> {
	private static final Log log = Log.getLog(RegisterController.class);
	/**
	 * 用户注册
	 */
	public void yzm(){
		renderCaptcha();
	}
	public void client_user_stepOne(){
		
		User user = getModel(User.class);
		if (StringUtils.isBlank(user.getUsername())&&StringUtils.isBlank(user.getPassword())&&StringUtils.isBlank(user.getEmail())&&StringUtils.isBlank(user.getRole())) {return;}
		if (StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())||StringUtils.isBlank(user.getEmail())||StringUtils.isBlank(user.getRole())) {renderAjaxResultForError("验证码错误");}
		 //去掉用户类型设置
//		if(StringUtils.isBlank(user.getRole())){
//			List<SetUsertype> suList=SetUsertype.DAO.findAllSetUserType();
//			setAttr("suList", suList);
//		}else {}
			if(!validateCaptcha("yzm")){
				renderAjaxResultForError("验证码错误");
				return;
			}
//			User u=UserQuery.me().findUserByUsername(user.getUsername());
//			if(u!=null){
//				renderAjaxResultForError("用户已存在");
//				return;
//			}
			
			if(StringUtils.areNotBlank(user.getUsername(),user.getPassword(),user.getEmail(),user.getRole())){
				final String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
				User tu=UserQuery.me().findUserByMobile(user.getUsername());
				User u=UserQuery.me().findUserByUsername(user.getUsername());
				if (tu!=null && user.getUsername().equals(tu.getMobile())){
					tu.setUsername(user.getUsername());
					tu.setPassword(user.getPassword());
					tu.setEmail(user.getEmail());
					tu.setRole(user.getRole());
					
					tu.setMobile(user.getMobile());//去掉号码
					tu.saveOrUpdate();
					tu.saveOrUpdateMetadta("openId",openId);
					renderAjaxResult("注册成功", 11, null);
					return;
				}else if(u!=null){
					renderAjaxResultForError("用户已存在");
					return;
				}else{
					setSessionAttr("user", user);
					renderAjaxResultForSuccess("下一步");
				}
			}else{
				renderAjaxResultForError("请填写完整！");
				return;
			}
			renderAjaxResultForSuccess();
	}
	
	
	public void client_user_stepTwo(){
//		User user = getSessionAttr("user");
	//获取openId ， username是nickname 。	
		
		final String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
		final String company = getPara("company");
		final String corporate =getPara("corporate");
		final String platformReq =getPara("platformReq");
		
		final String contact = getPara("contact");
		final String pcontact =getPara("pcontact");
		final String addrcontact =getPara("addrcontact");
		
		final String city = getPara("city");
		final String district = getPara("district");
		final String address = getPara("address");
		final String bscope = getPara("bscope");
		
		final String stockholder = getPara("stockholder");
		final String stockholderId = getPara("stockholderId");
		final String stockRate = getPara("stockRate");
		final String stockNum = getPara("stockNum");
		final String flag=getPara("flag");
		
		if(StringUtils.areNotBlank(company,corporate,platformReq)){
			Db.tx(new IAtom() {
				
				@Override
				public boolean run() throws SQLException {
					// TODO Auto-generated method stub
					User userinfo = getSessionAttr("user");
					if (userinfo == null) {
						renderAjaxResultForError("注册失败，请重新注册");
						return false;
					}
					
					
					User userQuery =UserQuery.me().findUserByMobile(userinfo.getUsername());
					if(userQuery!=null){
						userQuery.setCompany(company);
						userQuery.setRole(userinfo.getRole());
						userQuery.setUsername(userinfo.getUsername());
						userQuery.setPassword(userinfo.getPassword());
						userQuery.setEmail(userinfo.getEmail());
						userQuery.saveOrUpdate();
						
						userQuery.saveOrUpdateMetadta("openId",openId);//模拟微信_openId
						userQuery.saveOrUpdateMetadta("corporate", corporate);
						userQuery.saveOrUpdateMetadta("platformReq", platformReq);
						userQuery.saveOrUpdateMetadta("user_status", Consts.USER_STATUS_STOP);
						
						userQuery.saveOrUpdateMetadta("city", city);
						userQuery.saveOrUpdateMetadta("district", district);
						userQuery.saveOrUpdateMetadta("address", address);
						userQuery.saveOrUpdateMetadta("bscope", bscope);
						
						userQuery.saveOrUpdateMetadta("contact", contact);
						userQuery.saveOrUpdateMetadta("pcontact", pcontact);
						userQuery.saveOrUpdateMetadta("addrcontact", addrcontact);
						
						userQuery.saveOrUpdateMetadta("stockholder",stockholder);
						userQuery.saveOrUpdateMetadta("stockholderId",stockholderId);
						userQuery.saveOrUpdateMetadta("flag",flag);
						userQuery.saveOrUpdateMetadta("stockRate",stockRate);
						userQuery.saveOrUpdateMetadta("stockNum",stockNum);
					}else{
						User user = new User();
						user.setCompany(company);
						user.setRole(userinfo.getRole());
						user.setUsername(userinfo.getUsername());
						user.setPassword(userinfo.getPassword());
						user.setEmail(userinfo.getEmail());
						
						user.saveOrUpdate();
						
						user.saveOrUpdateMetadta("openId",openId);//模拟微信_openId
						user.saveOrUpdateMetadta("corporate", corporate);
						user.saveOrUpdateMetadta("platformReq", platformReq);
						user.saveOrUpdateMetadta("user_status", Consts.USER_STATUS_STOP);
						
						user.saveOrUpdateMetadta("city", city);
						user.saveOrUpdateMetadta("district", district);
						user.saveOrUpdateMetadta("address", address);
						user.saveOrUpdateMetadta("bscope", bscope);
						
						user.saveOrUpdateMetadta("contact", contact);
						user.saveOrUpdateMetadta("pcontact", pcontact);
						user.saveOrUpdateMetadta("addrcontact", addrcontact);
						
						user.saveOrUpdateMetadta("stockholder",stockholder);
						user.saveOrUpdateMetadta("stockholderId",stockholderId);
						user.saveOrUpdateMetadta("flag",flag);
						user.saveOrUpdateMetadta("stockRate",stockRate);
						user.saveOrUpdateMetadta("stockNum",stockNum);
					}
					removeSessionAttr("user");
					renderAjaxResultForSuccess();
					return true;
				}
			});
		}
	}
	
	public void client_user_regsuccess(){}
	
	
//	@Before(UserStatusInterceptor.class)
	
	/**
	 *  工商注册
	 */
//	public void client_enterprise_register() {
//
//		final String[] enterpriseName = getParaValues("enterpriseName");
//		final String city = getPara("city");
//		final String district = getPara("district");
//		final String address = getPara("address");
//		final String bscope = getPara("bscope");
//
//	//	String openId = "c3b36939-9814-4304-ba50-73401e997add";
//
//		String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
//
//		  if(enterpriseName==null||enterpriseName.length==0){}else{
//			  final User user = UserQuery.me().findFirstFromMetadata("openId", openId);
//
////				Content con=ContentQuery.me().findFirstByModuleAndObjectId(Consts.MODULE_ENTERPRISE, null, user.getId());
//				Content con=ContentQuery.me().findFirstByModuleAndUserId(Consts.MODULE_ENTERPRISE, user.getId());
//
//				if(con!=null){
//					renderAjaxResultForError("公司已注册");
//					return;
//				}
//				Db.tx(new IAtom() {
//				@Override
//				public boolean run() throws SQLException {
//
//
//					StringBuffer sb = new StringBuffer();
//					for (int i = 0; i < enterpriseName.length; i++) {
//						sb.append(enterpriseName[i]).append(",");
//					}
//
//					Content content = new Content();
//
//					content.setUserId(user.getId());
//					content.setModule(Consts.MODULE_ENTERPRISE);
//					content.setStatus(Content.STATUS_NORMAL);
//					content.setCreated(new Date());
//					content.saveOrUpdate();
//
//
//					content.saveOrUpdateMetadta("enterpriseName", sb.substring(0, sb.length()-1));
//					content.saveOrUpdateMetadta("city", city);
//					content.saveOrUpdateMetadta("district", district);
//					content.saveOrUpdateMetadta("address", address);
//					content.saveOrUpdateMetadta("bscope", bscope);
//
//
//					String[] stockholder = getParaValues("stockholder");
//					String[] stockholderId = getParaValues("stockholderId");
//					String[] stockRate = getParaValues("stockRate");
//					String[] stockNum = getParaValues("stockNum");
//					String[] flag=getParaValues("flag");
//					for(int i=0;i<stockholder.length;i++){
//						if(StringUtils.isNotBlank(stockholder[i])){
//							Content stockContent = new Content();
//							stockContent.setModule(Consts.MODULE_STOCK);
//							stockContent.setStatus(Content.STATUS_NORMAL);
//							stockContent.setObjectId(content.getId());// objectId ==
//							stockContent.setCreated(new Date());
//							stockContent.saveOrUpdate();
//
//							stockContent.saveOrUpdateMetadta("stockholder",stockholder[i]);
//							stockContent.saveOrUpdateMetadta("stockholderId",stockholderId[i]);
//							stockContent.saveOrUpdateMetadta("flag",flag[i]);
//							stockContent.saveOrUpdateMetadta("stockRate",stockRate[i]);
//							stockContent.saveOrUpdateMetadta("stockNum",stockNum[i]);
//						}
//					}
//					renderAjaxResultForSuccess();
//					return true;
//				}
//			});
//		  }
//	}

}
