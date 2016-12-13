package com.eyeself.controller;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import com.eyeself.EyeSelf;
import com.eyeself.utils.UUIDUtils;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import io.jpress.core.BaseFrontController;
import io.jpress.interceptor.UserInterceptor;
import io.jpress.model.Content;
import io.jpress.model.Mapping;
import io.jpress.model.User;
import io.jpress.model.query.ContentQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.JsoupUtils;
import io.jpress.utils.StringUtils;

@RouterMapping(url = "/product")
@Before(UserInterceptor.class)
public class ProductController extends BaseFrontController {

	/**
	 * 发布产品
	 */
	public void publish() {

		final Content content = getModel(Content.class);
		
		content.setText(JsoupUtils.getBodyHtml(content.getText()));
		
		content.setModule(EyeSelf.MODULE_PRODUCT);
		content.setStatus(Content.STATUS_NORMAL);
		content.setSlug(UUIDUtils.uuid());
		content.setCreated(new Date());
		content.setModified(new Date());
		content.setUserIp(getIPAddress());
		content.setUserAgent(getUserAgent());

		User user = getLoginedUser();
		content.setUserId(user.getId());

		final BigInteger taxonomyId = getParaToBigInteger("product_taxonomy");

		boolean saved = Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				if (!content.save()) {
					return false;
				}

				Mapping mapping = new Mapping();
				mapping.setContentId(content.getId());
				mapping.setTaxonomyId(taxonomyId);
				if (!mapping.save()) {
					return false;
				}

				Map<String, String> metas = getMetas();
				if (metas != null) {
					for (Map.Entry<String, String> entry : metas.entrySet()) {
						content.saveOrUpdateMetadta(entry.getKey(), entry.getValue());
					}
				}
				return true;
			}
		});

		if (saved) {
			renderAjaxResultForSuccess("success", content.getUrl());
		} else {
			renderAjaxResultForError();
		}
	}

	/**
	 * 咨询跳转
	 */
	public void consult() {
		BigInteger id = getParaToBigInteger("id");
		if (id == null) {
			renderError(404);
		}

		Content product = ContentQuery.me().findById(id);
		if (product == null) {
			renderError(404);
		}

		User company = product.getUser();
		String qq = company.getQq();
		if (StringUtils.isBlank(qq)) {
			renderError(404);
		}

		String consult = product.metadata("consult_count");
		long consultCount = 0;
		if (consult != null) {
			consultCount = Long.parseLong(consult);
		}

		consultCount += 1;
		
		product.saveOrUpdateMetadta("consult_count", consultCount + "");
		redirect(String.format("http://wpa.qq.com/msgrd?v=3&uin=%s&site=www.eyeself.com&menu=yes", qq));

	}

}
