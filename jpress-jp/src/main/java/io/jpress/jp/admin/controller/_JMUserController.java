package io.jpress.jp.admin.controller;

import java.math.BigInteger;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.admin.controller._UserController;
import io.jpress.interceptor.AdminInterceptor;
import io.jpress.jp.interceptor.JaddContractInterceptor;
import io.jpress.jp.interceptor.JonlyAdminInterceptor;
import io.jpress.jp.model.Tuser;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.template.TemplateManager;
@RouterMapping(url = "/admin/myuser", viewPath = "/WEB-INF/admin/myuser")
public class _JMUserController extends _UserController{
	
	public void user_list(){
		String delock = getPara("delock");
		setAttr("userCount", UserQuery.me().findCount());
		setAttr("adminCount", UserQuery.me().findAdminCount());
		User user  =this.getAttr("USER");
		
		Page<User> page = Tuser.TUSER.paginateMyuser(getPageNumber(), getPageSize(),delock,user.getId());
		setAttr("page", page);
		
		String templateHtml = "admin_user_index.html";
		if (TemplateManager.me().existsFile(templateHtml)) {
			setAttr("include", TemplateManager.me().currentTemplatePath() + "/" + templateHtml);
			return;
		}
		setAttr("include", "_index_include.html");
		setAttr("delock",delock);
		
	}
	
	//跳转限制页面
	@Clear(AdminInterceptor.class)
	public void limits(){
		
	}
	
	public void index() {
		setAttr("userCount", UserQuery.me().findCount());
		setAttr("adminCount", UserQuery.me().findAdminCount());
		User user  =this.getAttr("USER");
		Page<User> page =  Tuser.TUSER.paginateMyuser(getPageNumber(), getPageSize(),"",user.getId());
		setAttr("page", page);

		String templateHtml = "admin_user_index.html";
		if (TemplateManager.me().existsFile(templateHtml)) {
			setAttr("include", TemplateManager.me().currentTemplatePath() + "/" + templateHtml);
			return;
		}
		setAttr("include", "_index_include.html");
	}

	@Before(JaddContractInterceptor.class)
	public void edit() {
		super.edit();
	}
	@Before(JaddContractInterceptor.class)
	public void save() {
		super.save();
	}
	@Before(JonlyAdminInterceptor.class)
	public void delete() {
		super.delete();
	}
	
	
}
