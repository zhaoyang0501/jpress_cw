package io.jpress.jp.model;


import io.jpress.model.core.Table;
import io.jpress.jp.model.base.BaseStatistics;

/**
 * Generated by JPress.
 */
@Table(tableName="statistics",primaryKey="id")
public class Statistics extends BaseStatistics<Statistics> {
	private static final long serialVersionUID = 1L;

	public static final Statistics DAO = new Statistics();
	
//	public Statistics findByStatistics(String flag){
//		String sql= "select * from `statistics` where flag = ?";
//		 return findFirst(sql,flag);
//	}
	public Statistics findByStatistics(){
		String sql= "select * from `statistics` ";
		return findFirst(sql);
	}
}
