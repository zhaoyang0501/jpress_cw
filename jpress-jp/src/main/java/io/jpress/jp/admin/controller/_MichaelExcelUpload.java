package io.jpress.jp.admin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

import io.jpress.code.generator.Excel2003Processer;
import io.jpress.code.generator.Excel2007Processer;
import io.jpress.code.generator.IPoiProcesser;
import io.jpress.core.JBaseController;
import io.jpress.jp.Consts;
import io.jpress.model.Attachment;
import io.jpress.model.Content;
import io.jpress.model.query.ContentQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.AttachmentUtils;
import io.jpress.utils.FileUtils;
import io.jpress.utils.StringUtils;

@RouterMapping(url="/admin/excelUpload", viewPath="/templates/jp")
public class _MichaelExcelUpload extends JBaseController{
	
	public void buildData(){
		if(!isMultipartRequest()){
			return;
		}
			UploadFile file = getFile();
			String strExcel =AttachmentUtils.moveFile(file);
			System.err.println(strExcel);
			strExcel = strExcel.replace("\\", "/");
			File newfile = new File(PathKit.getWebRootPath()+strExcel);
	    	String fname=newfile.getName();
	    	IPoiProcesser processer = null;
	    	if(fname.endsWith(".xls")){
	    		processer = new Excel2003Processer(newfile);
	    	}else{
	    		processer = new Excel2007Processer(newfile);
	    	}
	    	processer.init();
	    	List<String> imageList = processer.getImages();
	    	Content c = new Content();
	    	c.setCreated(new Date());
	    	c.setStatus(Content.STATUS_DRAFT);
	    	c.setModified(new Date());
	    	c.setTitle(processer.getTitle());
	    	c.setText(processer.getText());
	    	c.setThumbnail(imageList.get(0));
	    	c.save();
	    	c.saveOrUpdateMetadta("产品配置", processer.getProductAttachs());
	    	c.saveOrUpdateMetadta("产品属性", processer.getProductAttr());
	    	c.saveOrUpdateMetadta("产品参数", processer.getProductPara());
	    	processer.getImages();
	    	renderAjaxResultForSuccess();
	}
}
