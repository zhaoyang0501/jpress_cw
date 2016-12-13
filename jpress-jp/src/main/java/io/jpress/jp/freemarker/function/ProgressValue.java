package io.jpress.jp.freemarker.function;

import java.math.BigInteger;
import java.util.List;

import io.jpress.core.render.freemarker.JFunction;
import io.jpress.model.Content;
import io.jpress.model.query.ContentQuery;
import io.jpress.utils.StringUtils;

public class ProgressValue extends JFunction{

	@Override
	public Object onExec() {
		BigInteger key =BigInteger.valueOf(Long.parseLong(getToString(0))); //获取当前的一个参数
		String key2 = getToString(1);
		if(StringUtils.isBlank(key2)||key==null){
			return "";
		}
	List<Content> clist=ContentQuery.me().findListByModuleAndObjectId(key2, key);
	StringBuffer sb = new StringBuffer();
	for (Content c : clist) {
		sb.append("<div>");
		sb.append("<p>"+c.getText()+"</p>");
		sb.append("<p>	收齐资料时间："+c.metadata("collectTime")+"</p>");
		sb.append("<p>	提交资料时间："+c.metadata("submitTime")+"</p>");
		sb.append("<p>	办结时间："+c.metadata("finishTime")+"</p>");
		sb.append("</div>");
	}
		return sb.toString();

	}

}
