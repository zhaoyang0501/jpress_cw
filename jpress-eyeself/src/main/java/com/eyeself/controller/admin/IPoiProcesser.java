package com.eyeself.controller.admin;

import java.util.List;

public interface IPoiProcesser {
	public void init();
	/**
	 * 获取标题
	 * @return
	 */
	public String getTitle();
	 /**
	  * 获取产品特点
	  * @return
	  */
	public String getText();
	
	
	/**
	 * 获取产品配置
	 * @return
	 */
	public String getProductAttachs();
	
	/**
	 * 获取产品属性
	 * @return
	 */
	public String getProductAttr();
	
	/**
	 * 获取产品名称
	 * @return
	 */
	public String getProductName();
	/**
	 * 获取型号
	 * @return
	 */
	public String getProductSpecification();
	
	/**
	 * 获取工厂名称
	 * @return
	 */
	public String getFactory();
	/**
	 * 批准文号
	 * @return
	 */
	public String getApprovalNumber();
	
	/**
	 * 获取产品参数
	 */
	
	public String getProductPara();
	
	public List<String> getImages();
}
