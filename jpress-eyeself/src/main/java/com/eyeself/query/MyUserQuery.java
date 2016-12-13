package com.eyeself.query;

import com.jfinal.plugin.activerecord.Page;

import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.utils.StringUtils;

public class MyUserQuery extends UserQuery {

	private static final MyUserQuery QUERY = new MyUserQuery();

	public static MyUserQuery me() {
		return QUERY;
	}

	public Page<User> paginateCompany(int pageNumber, int pageSize, String type, String orderby) {
		String select = "select * ";
		StringBuilder fromBuilder = new StringBuilder(" from user u ");
		if (StringUtils.isNotBlank(type)) {
			fromBuilder.append(" LEFT JOIN metadata m ON u.id = m.object_id ");
			fromBuilder.append(" AND m.object_type = 'user' AND m.meta_key='company_type' ");
		}

		fromBuilder.append(" where u.company is not null ");

		if ("factory".equals(type)) {
			fromBuilder.append(" AND m.meta_value = 'factory'");
		} else if ("dealer".equals(type)) {
			fromBuilder.append(" AND m.meta_value = 'dealer'");
		} else if ("sp".equals(type)) {
			fromBuilder.append(" AND m.meta_value = 'sp'");
		}

		buildOrderBy(orderby, fromBuilder);
		return DAO.paginate(pageNumber, pageSize, select, fromBuilder.toString());
	}

}
