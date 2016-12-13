package io.jpress.jp.model;


import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import io.jpress.model.User;
import io.jpress.model.core.Table;
import io.jpress.model.query.UserQuery;

/**
 * Generated by JPress.
 */
@Table(tableName="user",primaryKey="id")
public class Tuser {
	protected static final User DAO = new User();
	public static final Tuser TUSER = new Tuser();
	
	public Page<User> paginate(int pageNumber, int pageSize,String delock){
		String sql=" select * ";
		StringBuilder fromBuilder=new StringBuilder(" from `user`");
		fromBuilder.append(" where username like ?  ORDER BY created DESC");
		return DAO.paginate(pageNumber, pageSize,sql,fromBuilder.toString(), "%"+delock+"%");
		
	}
	
	public List<User> findRoleById(String role){
		String sql="select * from `user` where role = ? ";
		return DAO.find(sql);
	}
	public List<User> findList(){
		String sql="select * from `user` ";
		return DAO.find(sql);
	}
	
	
}