package io.jpress.jp.model;


import java.math.BigInteger;

import io.jpress.jp.model.base.BaseUserContract;
import io.jpress.model.core.Table;

/**
 * Generated by JPress.
 */
@Table(tableName="user_contract",primaryKey="id")
public class UserContract extends BaseUserContract<UserContract> {
	private static final long serialVersionUID = 1L;

	public static final UserContract DAO = new UserContract();
	
	public UserContract findByUserId(BigInteger userId){
		String sql = "select * from `user_contract` where userId = ? ";
		return findFirst(sql,userId);
	}
}
