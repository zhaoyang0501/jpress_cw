package com.eyeself.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import com.eyeself.EyeSelf;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import io.jpress.core.BaseFrontController;
import io.jpress.interceptor.UserInterceptor;
import io.jpress.model.Content;
import io.jpress.model.Mapping;
import io.jpress.model.Taxonomy;
import io.jpress.model.User;
import io.jpress.model.query.TaxonomyQuery;
import io.jpress.router.RouterMapping;

@RouterMapping(url = "/business")
@Before(UserInterceptor.class)
public class BusinessController extends BaseFrontController {

	/**
	 * 发布招商
	 */
	public void publishTender() {
		process("zhaobiao");
	}
	
	public void publishBuyOffer() {
		process("qiugou");
	}

	private void process(String slug) {
		final Content content = getModel(Content.class);
		content.setModule(EyeSelf.MODULE_BUSINESS);
		content.setStatus(Content.STATUS_NORMAL);
		content.setSlug(content.getTitle());
		content.setCreated(new Date());
		content.setModified(new Date());
		content.setUserIp(getIPAddress());
		content.setUserAgent(getUserAgent());

		User user = getAttr("user");
		content.setUserId(user.getId());

		final Taxonomy taxonomy = TaxonomyQuery.me().findBySlugAndModule(slug, EyeSelf.MODULE_BUSINESS);
		if (taxonomy == null) {
			renderAjaxResultForError();
			return;
		}

		boolean saved = Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				if (!content.save()) {
					return false;
				}

				Mapping mapping = new Mapping();
				mapping.setContentId(content.getId());
				mapping.setTaxonomyId(taxonomy.getId());
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

}
