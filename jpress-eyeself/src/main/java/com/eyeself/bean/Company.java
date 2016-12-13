package com.eyeself.bean;

import io.jpress.model.User;

public class Company {

	public static final String TYPE_FACTORY = "factory"; // 厂家
	public static final String TYPE_DEALER = "dealer"; // 经销商
	public static final String TYPE_SERVICE_PROVIDER = "service_provider";// 服务商

	private String name;// 企业名称
	private String type;// 企业类型
	private String phone; // 联系电话
	private String mainBusiness;// 主营业务
	private User user;

	public Company() {
	}

	public Company(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMainBusiness() {
		return mainBusiness;
	}

	public void setMainBusiness(String mainBusiness) {
		this.mainBusiness = mainBusiness;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
