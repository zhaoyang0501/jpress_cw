package io.jpress.jp.model;


import java.math.BigInteger;
import java.util.List;

import io.jpress.jp.model.base.BaseCompanyInfo;
import io.jpress.model.core.Table;

/**
 * Generated by JPress.
 */
@Table(tableName="company_info",primaryKey="id")
public class CompanyInfo extends BaseCompanyInfo<CompanyInfo> {
	private static final long serialVersionUID = 1L;

	public static final CompanyInfo DAO = new CompanyInfo();
	
	public CompanyInfo findByCompanyInfos(BigInteger ordersEnterpriseId){
		
//		String sql="SELECT * FROM `company_info` ci LEFT JOIN `company_stock` cs ON ci.id=cs.company_inof_id WHERE ci.orders_enterprise_id = ?";
		String sql="SELECT * FROM `company_info` WHERE orders_enterprise_id = ?";
		return findFirst(sql,ordersEnterpriseId);
	}
	
	
}