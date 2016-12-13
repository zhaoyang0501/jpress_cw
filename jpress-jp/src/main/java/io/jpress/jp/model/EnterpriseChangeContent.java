package io.jpress.jp.model;


import java.math.BigInteger;
import java.util.List;

import io.jpress.jp.model.base.BaseEnterpriseChangeContent;
import io.jpress.model.core.Table;

/**
 * Generated by JPress.
 */
@Table(tableName="enterprise_change_content",primaryKey="id")
public class EnterpriseChangeContent extends BaseEnterpriseChangeContent<EnterpriseChangeContent> {
	private static final long serialVersionUID = 1L;

	public static final EnterpriseChangeContent DAO = new EnterpriseChangeContent();
	
	public  List<EnterpriseChangeContent>findListByOrdersEnterprise(BigInteger ordersEnterpriseId){
		
		String sql = "select * from `enterprise_change_content` where orders_enterprise_id = ? ";
		return find(sql, ordersEnterpriseId);
	}
}
