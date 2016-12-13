package io.jpress.jp.model;


import java.util.List;

import io.jpress.jp.model.base.BaseSetProgress;
import io.jpress.model.core.Table;

/**
 * Generated by JPress.
 */
@Table(tableName="set_progress",primaryKey="id")
public class SetProgress extends BaseSetProgress<SetProgress> {
	private static final long serialVersionUID = 1L;

	public static final SetProgress DAO = new SetProgress();
	
	public List<SetProgress> findAllSetProgress(){
		String sql = "select * from `set_progress`";
		return find(sql);
	}
}
