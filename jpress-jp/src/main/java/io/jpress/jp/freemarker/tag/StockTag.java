package io.jpress.jp.freemarker.tag;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import io.jpress.core.render.freemarker.JTag;
import io.jpress.model.Content;
import io.jpress.model.ModelSorter;
import io.jpress.model.Taxonomy;
import io.jpress.model.query.ContentQuery;
import io.jpress.model.query.TaxonomyQuery;
import io.jpress.utils.StringUtils;



public class StockTag extends JTag {

	public static final String TAG_NAME = "jp.stocks";

	@Override
	public void onRender() {
		
		String module = getParam("module");
		BigInteger objectId = getParamToBigInteger("objectId");

		if(objectId.compareTo(BigInteger.ZERO)==0){
			//renderText("");
			renderBody();
			return;
		}
		

		

		List<Content> data = ContentQuery.me().findListByModuleAndObjectId(module, objectId);
		if (data == null || data.isEmpty()) {
			renderText("");
			return;
		}

		setVariable("stocks", data);
		renderBody();
	}

}
