package com.eyeself.controller;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

import io.jpress.core.BaseFrontController;
import io.jpress.model.Attachment;
import io.jpress.model.User;
import io.jpress.model.query.AttachmentQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.AttachmentUtils;
import io.jpress.utils.FileUtils;

@RouterMapping(url = "/attachment")
public class AttachmentController extends BaseFrontController {

	public void list() {
		User user = getLoginedUser();
		if (user == null) {
			renderError(404);
		}

		Page<Attachment> page = AttachmentQuery.me().paginate(getPageNumber(), getPageSize(), user.getId(), null, null,
				null, null, null, null, null);

		setAttr("page", page);

		render("component_attachment_list.html");
	}

	public void upload() {
		User user = getLoginedUser();
		if (user == null) {
			renderError(404);
		}

		UploadFile uploadFile = getFile();
		if (null != uploadFile) {
			String newPath = AttachmentUtils.moveFile(uploadFile);

			Attachment attachment = new Attachment();
			attachment.setUserId(user.getId());
			attachment.setCreated(new Date());
			attachment.setTitle(uploadFile.getOriginalFileName());
			attachment.setPath(newPath.replace("\\", "/"));
			attachment.setSuffix(FileUtils.getSuffix(uploadFile.getFileName()));
			attachment.setMimeType(uploadFile.getContentType());
			attachment.save();

			JSONObject json = new JSONObject();
			json.put("success", true);
			json.put("src", getRequest().getContextPath() + newPath);
			renderJson(json.toString());
		} else {
			renderJson("success", false);
		}

	}

}
