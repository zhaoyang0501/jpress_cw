package com.eyeself.controller;

import java.math.BigInteger;
import java.util.Date;

import com.eyeself.tag.CompanyPageTag;
import com.eyeself.tag.UserProductPageTag;

import io.jpress.core.BaseFrontController;
import io.jpress.core.cache.ActionCache;
import io.jpress.model.Content;
import io.jpress.model.User;
import io.jpress.model.query.ContentQuery;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.StringUtils;

@RouterMapping(url = "/company")
public class CompanyController extends BaseFrontController {

	/**
	 * 企业首页
	 */
	@ActionCache
	public void index() {

		int page = 0;
		String type = null;

		String para = getPara(0);
		if (!StringUtils.isNumeric(para)) {
			type = para;
			page = getParaToInt(1, 1);
		} else {
			page = getParaToInt(0);
		}

		if (page < 1)
			page = 1;

		String orderby = getPara("orderby");

		setAttr("jp.companyPage", new CompanyPageTag(page, type, orderby));
		render("component_company_list.html");
	}

	/**
	 * 企业详情
	 */
	public void detail() {
		String para = getPara(0);
		if (StringUtils.isBlank(para)) {
			renderError(404);
		}

		BigInteger id = StringUtils.toBigInteger(para, null);
		if (id == null) {
			renderError(404);
		}

		int pageNumber = getParaToInt(1, 1);

		User user = UserQuery.me().findById(id);
		setAttr("user", user);

		UserProductPageTag userProductPage = new UserProductPageTag(id, pageNumber);
		setAttr("jp.userProductPage", userProductPage);

		render("component_company_detail.html");
	}

	/**
	 * 进行搜藏
	 */
	public void collection() {

		User user = getLoginedUser();
		if (user == null) {
			renderAjaxResult("用户没有登陆", 2);
			return;
		}

		BigInteger id = getParaToBigInteger("id");
		if (id == null) {
			renderAjaxResultForError();
			return;
		}

		User collectionUser = UserQuery.me().findById(id);
		if (collectionUser == null) {
			renderAjaxResultForError();
			return;
		}
		
		if(user.getId().compareTo(collectionUser.getId()) == 0){
			renderAjaxResultForError("不能搜藏你自己的公司");
			return;
		}

		Content conllectionRecode = ContentQuery.me().findFirstByModuleAndObjectId("companyConlection",
				collectionUser.getId(), user.getId());
		if (conllectionRecode != null) {
			renderAjaxResult("您已经搜藏过了", 3);
			return;
		}

		conllectionRecode = new Content();
		conllectionRecode.setModule("companyConlection");
		conllectionRecode.setCreated(new Date());
		conllectionRecode.setStatus(Content.STATUS_NORMAL);
		conllectionRecode.setObjectId(collectionUser.getId());
		conllectionRecode.setUserId(user.getId());

		if (conllectionRecode.save()) {
			renderAjaxResultForSuccess();
		} else {
			renderAjaxResultForError();
		}

	}

}
