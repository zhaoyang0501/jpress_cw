package io.jpress.jp.freemarker.function;

import java.math.BigInteger;

import io.jpress.core.render.freemarker.JFunction;
import io.jpress.jp.model.SetOrders;

public class OrderValue extends JFunction {

	@Override
	public Object onExec() {
		BigInteger key =BigInteger.valueOf(Long.parseLong(getToString(0)));
		SetOrders setOrders=SetOrders.DAO.findById(key);
		
		return setOrders.getOrderType();
	}

}
