package io.jpress.jp.freemarker.function;

import java.math.BigInteger;

import io.jpress.core.render.freemarker.JFunction;
import io.jpress.jp.model.OrdersEnterprise;

public class OrderEnterpriseValue extends JFunction {

	@Override
	public Object onExec() {
		BigInteger key =BigInteger.valueOf(Long.parseLong(getToString(0)));
		OrdersEnterprise oe=OrdersEnterprise.DAO.findOrdersEnterpriseById(key);
	
		return oe.get("orderType");
	}

}
