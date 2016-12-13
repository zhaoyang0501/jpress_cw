package com.eyeself.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.eyeself.utils.UUIDUtils;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import io.jpress.Consts;
import io.jpress.core.BaseFrontController;
import io.jpress.interceptor.UserInterceptor;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.notify.sms.ISmsSender;
import io.jpress.notify.sms.SmsMessage;
import io.jpress.notify.sms.SmsSenderFactory;
import io.jpress.router.RouterMapping;
import io.jpress.utils.CookieUtils;
import io.jpress.utils.EncryptUtils;
import io.jpress.utils.StringUtils;

@RouterMapping(url = "/user/center/update")
public class UserController extends BaseFrontController {

	public void user() {
		HashMap<String, String> map = getUploadFilesMap();
		final Map<String, String> metas = getMetas(map);

		final User user = getModel(User.class);

		String avatar = map.get("user.avatar");
		if (StringUtils.isNotBlank(avatar)) {
			user.setAvatar(avatar);
		}

		boolean saved = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {

				if (!user.saveOrUpdate()) {
					return false;
				}

				if (metas != null) {
					for (Map.Entry<String, String> entry : metas.entrySet()) {
						user.saveOrUpdateMetadta(entry.getKey(), entry.getValue());
					}
				}

				return true;
			}
		});

		if (saved) {
			renderAjaxResultForSuccess();
		} else {
			renderAjaxResultForError();
		}

	}

	public void detail() {
		render("component_company_detail.html");
	}

	@ActionKey("/user/register")
	@Clear(UserInterceptor.class)
	public void doRegister() {

		keepPara();

		String mobile = getPara("mobile");
		String mobile_code = getPara("mobile_code");

		String password = getPara("password");
		String confirm_password = getPara("confirm_password");

		if (StringUtils.isBlank(password)) {
			renderForRegister("密码不能为空！!", Consts.ERROR_CODE_PASSWORD_EMPTY);
			return;
		}

		if (StringUtils.isBlank(confirm_password)) {
			renderForRegister("确认密码不能为空！!", Consts.ERROR_CODE_PASSWORD_EMPTY);
			return;
		}

		if (!confirm_password.equals(password)) {
			renderForRegister("两次输入密码不一致!", Consts.ERROR_CODE_PASSWORD_EMPTY);
			return;
		}

		if (null != mobile && UserQuery.me().findUserByMobile(mobile) != null) {
			renderForRegister("该手机号码已经存在!", Consts.ERROR_CODE_PHONE_EXIST);
			return;
		}

		String sessionMobileCode = getSessionAttr("mobile_code");
		if (sessionMobileCode == null) {
			renderForRegister("请先获取验证码!", 1);
			return;
		}

		if (!sessionMobileCode.equals(mobile_code)) {
			renderForRegister("您输入的验证码不正确!", 1);
			return;
		}

		User user = new User();
		user.setUsername(mobile);
		user.setNickname(mobile);
		user.setMobile(mobile);

		String salt = EncryptUtils.salt();
		password = EncryptUtils.encryptPassword(password, salt);
		user.setPassword(password);
		user.setSalt(salt);
		user.setCreateSource("register");
		user.setCreated(new Date());

		if (user.save()) {
			
			//保存档案号
			user.saveOrUpdateMetadta("file_number", UUIDUtils.uuid());
			
			CookieUtils.put(this, Consts.COOKIE_LOGINED_USER, user.getId());

			if (isAjaxRequest()) {
				renderAjaxResultForSuccess();
			} else {
				String gotoUrl = getPara("goto");
				if (StringUtils.isNotEmpty(gotoUrl)) {
					gotoUrl = StringUtils.urlDecode(gotoUrl);
					gotoUrl = StringUtils.urlRedirect(gotoUrl);
					redirect(gotoUrl);
				} else {
					redirect(Consts.ROUTER_USER_CENTER);
				}
			}
		} else {
			renderAjaxResultForError();
		}
	}

	/**
	 * 发送验证码
	 */
	public void renderMobileCode() {
		String mobile = getPara("mobile");
		if (StringUtils.isBlank(mobile)) {
			renderAjaxResultForError();
			return;
		}

		String code = String.valueOf(Math.random()).substring(2, 6);
		ISmsSender sender = SmsSenderFactory.createSender();
		SmsMessage sms = new SmsMessage();

		sms.setRec_num(mobile);
		sms.setTemplate("SMS_14375140");
		sms.setParam("{\"code\":\" : [ " + code + " ] \",\"product\":\"【 EyeSelf云之眼 】\"}");
		sms.setSign_name("身份验证");

		boolean sendOK = sender.send(sms);
		if (sendOK) {
			setSessionAttr("mobile_code", code);
			renderAjaxResultForSuccess();
		} else {
			renderAjaxResultForError();
		}

	}

	private void renderForRegister(String message, int errorCode) {
		String referer = getRequest().getHeader("Referer");
		if (isAjaxRequest()) {
			renderAjaxResult(message, errorCode);
		} else {
			redirect(referer + "?errorcode=" + errorCode);
		}
	}

	public void getPwd() {
		String username = getPara("user");
		if (StringUtils.isBlank(username)) {
			renderAjaxResultForError("请输入用户名！");
			return;
		}

		User user = UserQuery.me().findUserByUsername(username);
		if (user == null) {
			renderAjaxResultForError("没有该用户名！");
			return;
		}

		if (StringUtils.isBlank(user.getMobile())) {
			renderAjaxResultForError("该用户没有配置手机号码！");
			return;
		}

		String code = String.valueOf(Math.random()).substring(2, 6);
		ISmsSender sender = SmsSenderFactory.createSender();
		SmsMessage sms = new SmsMessage();

		sms.setRec_num(user.getMobile());
		sms.setTemplate("SMS_14375138");
		sms.setParam("{\"code\":\" : [ " + code + " ] \",\"product\":\"【 EyeSelf云之眼 】\"}");
		sms.setSign_name("身份验证");

		boolean sendOK = sender.send(sms);
		if (sendOK) {
			setSessionAttr("mobile_code", code);
			setSessionAttr("getpwd_user", user);
			renderAjaxResultForSuccess("验证码以及发送到手机 ****" + user.getMobile().substring(7));
		} else {
			renderAjaxResultForError();
		}
	}

	public void checkCode() {
		String code = getPara("code");

		if (StringUtils.isBlank(code)) {
			renderAjaxResultForError();
			return;
		}

		if (code.equals(getSessionAttr("mobile_code"))) {
			renderAjaxResultForSuccess();
		} else {
			renderAjaxResultForError();
		}

	}

	public void pwdUpdate() {

		String mobile_code = getPara("mobile_code");

		String password = getPara("password");
		String confirm_password = getPara("confirm_password");


		User user = getSessionAttr("getpwd_user");
		if (user == null ) {
			renderForRegister("用户名信息错误!", Consts.ERROR_CODE_PASSWORD_EMPTY);
			return;
		}
		if (StringUtils.isBlank(password)) {
			renderForRegister("密码不能为空!", Consts.ERROR_CODE_PASSWORD_EMPTY);
			return;
		}
		if (StringUtils.isBlank(confirm_password)) {
			renderForRegister("确认密码不能为空!", Consts.ERROR_CODE_PASSWORD_EMPTY);
			return;
		}
		if (StringUtils.isBlank(mobile_code)) {
			renderForRegister("验证码不能为空！", Consts.ERROR_CODE_PASSWORD_EMPTY);
			return;
		}

		if (!confirm_password.equals(password)) {
			renderForRegister("两次输入密码不一致!", Consts.ERROR_CODE_PASSWORD_EMPTY);
			return;
		}

		String sessionMobileCode = getSessionAttr("mobile_code");

		// 验证码正确，可以修改密码
		if (mobile_code.equals(sessionMobileCode)) {
			String salt = EncryptUtils.salt();
			password = EncryptUtils.encryptPassword(password, salt);
			user.setSalt(salt);
			user.setPassword(password);

			if (user.update()) {
				renderForRegister("密码修改成功！", 0);
			} else {
				renderForRegister("密码修改失败！", 1);
			}
		} else {
			renderForRegister("验证码错误！", 1);
		}

	}

}
