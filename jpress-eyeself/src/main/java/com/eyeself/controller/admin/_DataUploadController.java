package com.eyeself.controller.admin;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.eyeself.EyeSelf;
import com.eyeself.utils.UUIDUtils;
import com.jfinal.aop.Before;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

import io.jpress.core.JBaseController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.model.Content;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import io.jpress.utils.AttachmentUtils;

@RouterMapping(url = "/admin/dataUpload", viewPath = "/WEB-INF/admin/dataUpload")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _DataUploadController extends JBaseController {

	public void index() {
		if (!isMultipartRequest()) {
			return;
		}

		UploadFile file = getFile();
		if (file == null) {
			return;
		}

		String strExcel = AttachmentUtils.moveFile(file);
		strExcel = strExcel.replace("\\", "/");
		File newfile = new File(PathKit.getWebRootPath() + strExcel);
		String fname = newfile.getName();
		IPoiProcesser processer = null;
		if (fname.endsWith(".xls")) {
			processer = new Excel2003Processer(newfile);
		} else {
			processer = new Excel2007Processer(newfile);
		}
		processer.init();
		List<String> imageList = processer.getImages();
		Content c = new Content();
		c.setCreated(new Date());
		c.setStatus(Content.STATUS_DRAFT);
		c.setModified(new Date());
		c.setTitle(processer.getTitle());
		c.setModule(EyeSelf.MODULE_PRODUCT);
		c.setModified(new Date());
		c.setSlug(UUIDUtils.uuid());
		c.setText(processer.getText());
		if (imageList != null && imageList.size() > 0) {
			c.setThumbnail(imageList.get(0));
			for (int i = 0; i < imageList.size(); i++) {
				c.saveOrUpdateMetadta("thumbnail" + i, JFinal.me().getContextPath() + imageList.get(i));
			}
		}

		c.save();

		c.saveOrUpdateMetadta("product_qd", processer.getProductAttachs());
		// c.saveOrUpdateMetadta("产品属性", processer.getProductAttr());
		c.saveOrUpdateMetadta("product_cs", processer.getProductPara());

		c.saveOrUpdateMetadta("product_name", processer.getProductName());
		c.saveOrUpdateMetadta("product_specification", processer.getProductSpecification());
		c.saveOrUpdateMetadta("factory", processer.getFactory());
		c.saveOrUpdateMetadta("approval_number", processer.getApprovalNumber());

		renderAjaxResultForSuccess();

	}

}
