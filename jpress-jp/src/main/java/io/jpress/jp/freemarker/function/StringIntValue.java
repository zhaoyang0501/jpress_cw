package io.jpress.jp.freemarker.function;

import io.jpress.core.render.freemarker.JFunction;
import io.jpress.utils.StringUtils;

public class StringIntValue extends JFunction {

	@Override
	public Object onExec() {
		// TODO Auto-generated method stub
		String key = getToString(0);
		if(StringUtils.isBlank(key)){
			return "";
		}
		float num=Float.parseFloat(key);
		return num;
	}

}
