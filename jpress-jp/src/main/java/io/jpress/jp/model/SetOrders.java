package io.jpress.jp.model;


import java.util.List;

import io.jpress.jp.model.base.BaseSetOrders;
import io.jpress.model.core.Table;

/**
 * Generated by JPress.
 */
@Table(tableName="set_orders",primaryKey="id")
public class SetOrders extends BaseSetOrders<SetOrders> {
	private static final long serialVersionUID = 1L;

	public static final SetOrders DAO = new SetOrders();
	
	public List<SetOrders> findAllSetOrders(){
		String sql="select * from `set_orders`";
		return find(sql);
	}
	
	public List<SetOrders> findSetOrdersByFlag(){
		String sql="select * from `set_orders` where flag = '1' ";
		return find(sql);
	}
}
