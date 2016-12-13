package io.jpress.jp.model;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Page;

import io.jpress.jp.model.base.BaseContract;
import io.jpress.model.core.Table;

/**
 * Generated by JPress.
 */
@Table(tableName="contract",primaryKey="id")
public class Contract extends BaseContract<Contract> {
	private static final long serialVersionUID = 1L;

	public static final Contract DAO = new Contract();
	
	
	public static final Map<String, String> paystatusstr = new HashMap<String, String>();

	static {
		paystatusstr.put("0", "一次性付清");
		paystatusstr.put("1", "季付");
		paystatusstr.put("2", "半年付");
		paystatusstr.put("3", "年付 ");
	}
	
	public String getPaystatusstr(){
		return paystatusstr.get(getPayStatus());
	}
	
	
	public Contract findContractByOrdersEnterpriseId(BigInteger ordersEnterpriseId){
		String sql = "select * from `contract` where orders_enterprise_id = ?";
		return findFirst(sql, ordersEnterpriseId);
	}
	public Contract findContractByOIdwithSId(BigInteger ordersEnterpriseId){
		StringBuffer sql = new StringBuffer( "select c.*,so.orderType set_orders_orderType from `contract` c ");
		sql.append(" left join `orders_enterprise` oe on c.orders_enterprise_id = oe.id ");
		sql.append(" left join `set_orders` so on oe.set_orders_id = so.id ");
		sql.append(" where orders_enterprise_id = ? ");
		
		return findFirst(sql.toString(), ordersEnterpriseId);
	}
	
	public Page<Contract> findByOrderEnterpirseUserId(int pageNumber, int pageSize, BigInteger userId){
		String sql  = "select c.*,s.orderType set_orderType ";
		StringBuilder sqlBuilder = new StringBuilder(" from `contract` c left join `orders_enterprise` oe on c.orders_enterprise_id = oe.id ");
		sqlBuilder.append(" LEFT JOIN `set_orders` s ON oe.set_orders_id = s.id  ");
		sqlBuilder.append(" where oe.userId = ?  ORDER BY c.created DESC");
		return DAO.paginate(pageNumber, pageSize, sql, sqlBuilder.toString(),userId);
		
	}

	/**
	 *  change the status to be deadline
	 */
	public List<Contract> findContract (){
		
		String sql = "select * from `contract` where status = '1' ";
		return find(sql);
		
	}
	/**
	 *  change the status to be deadline
	 */
	public List<Contract> findContractWithPay(){
		String sql = "select * from `contract` WHERE status = '1' and pay_status !='0' ";
		return find(sql);
		
	}
	
}