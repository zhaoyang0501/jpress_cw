package io.jpress.jp.model;


import io.jpress.jp.model.base.BaseCompanyContract;
import io.jpress.model.core.Table;

/**
 * Generated by JPress.
 */
@Table(tableName="company_contract",primaryKey="id")
public class CompanyContract extends BaseCompanyContract<CompanyContract> {
	private static final long serialVersionUID = 1L;

	public static final CompanyContract DAO = new CompanyContract();
}
