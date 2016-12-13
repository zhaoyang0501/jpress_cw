package io.jpress.jp.freemarker.function;

import java.math.BigInteger;

import io.jpress.core.render.freemarker.JFunction;
import io.jpress.model.Content;
import io.jpress.model.query.ContentQuery;
import io.jpress.utils.StringUtils;

public class EstockValue extends JFunction {

	@Override
	public Object onExec() {
		// TODO Auto-generated method stub
		BigInteger key =BigInteger.valueOf(Long.parseLong(getToString(0))); //获取当前的一个参数
		String key2 = getToString(1);
		if(StringUtils.isBlank(key2)||key==null){
			return "";
		}
		return ContentQuery.me().findFirstByModuleAndObjectId(key2, key).metadata("stockholder").toString();

	}

}
