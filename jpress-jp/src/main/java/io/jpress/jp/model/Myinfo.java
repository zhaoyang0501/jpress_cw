package io.jpress.jp.model;


import java.math.BigInteger;

import com.jfinal.plugin.activerecord.Page;

import io.jpress.model.core.Table;
import io.jpress.jp.model.base.BaseMyinfo;

/**
 * Generated by JPress.
 */
@Table(tableName="myinfo",primaryKey="id")
public class Myinfo extends BaseMyinfo<Myinfo> {
	private static final long serialVersionUID = 1L;

	public static final Myinfo DAO = new Myinfo();
	
	public Page<Myinfo> pageByUserId(int pageNumber, int pageSize,BigInteger userId){
		String select ="select * ";
		StringBuilder from = new StringBuilder(" from `myinfo` ");
		from.append(" where userId = ? ");
		from.append(" order by created DESC ");
		return DAO.paginate(pageNumber, pageSize, select, from.toString(), userId);
	}
	
	public Myinfo findByMdate(String mdate){
		String sql="select * from `myinfo` where mdate = ?";
		return findFirst(sql);
	}
}