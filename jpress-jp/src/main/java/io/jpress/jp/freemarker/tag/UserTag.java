package io.jpress.jp.freemarker.tag;

import java.math.BigInteger;
import java.util.List;

import io.jpress.core.render.freemarker.JTag;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;

public class UserTag extends JTag {

	@Override
	public void onRender() {
		String module = getParam("module");
		BigInteger id = getParamToBigInteger("id");
		
		if(id.compareTo(BigInteger.ZERO)==0){
			renderText("");
			//renderBody();
			return;
		}
		
		User data = UserQuery.me().findById(id);
		if (data == null ) {
			renderText("");
			return;
		}

		setVariable("user", data);
		renderBody();
	}

}
