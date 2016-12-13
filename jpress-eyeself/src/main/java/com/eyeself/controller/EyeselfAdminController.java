package com.eyeself.controller;

import com.jfinal.aop.Before;

import io.jpress.core.JBaseController;
import io.jpress.interceptor.UserInterceptor;
import io.jpress.router.RouterMapping;

@RouterMapping(url = "/admin/eyeself")
@Before(UserInterceptor.class)
public class EyeselfAdminController extends JBaseController {
	
	
	
	public void indexSetting() {
		
		
		render("/WEB-INF/admin/option/eyeself_index.html");

	}

}
