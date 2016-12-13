/**
 * Copyright (c) 2015-2016, Michael Yang 杨福海 (fuhai999@gmail.com).
 *
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eyeself.tag;

import java.math.BigInteger;

import com.eyeself.EyeSelf;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.render.freemarker.BasePaginateTag;
import io.jpress.core.render.freemarker.JTag;
import io.jpress.model.Content;
import io.jpress.model.query.ContentQuery;
import io.jpress.utils.StringUtils;

public class UserProductPageTag extends JTag {

	BigInteger userId;
	int pageNumber;

	public UserProductPageTag(BigInteger id, int pageNumber) {
		this.userId = id;
		this.pageNumber = pageNumber;
	}

	@Override
	public void onRender() {

		int pageSize = getParamToInt("pagesize", 10);
		String orderby = getParam("orderby");
		String status = getParam("status", Content.STATUS_NORMAL);

		Page<Content> page = ContentQuery.me().paginate(pageNumber, pageSize, EyeSelf.MODULE_PRODUCT, null, status,
				null, userId, orderby);

		setVariable("page", page);

		MyPaginateTag tpt = new MyPaginateTag(page, userId);
		setVariable("pagination", tpt);

		renderBody();
	}

	public static class MyPaginateTag extends BasePaginateTag {

		BigInteger userId;

		public MyPaginateTag(Page<Content> page, BigInteger userid) {
			super(page);
			this.userId = userid;
		}

		@Override
		protected String getUrl(int pageNumber) {
			String url = JFinal.me().getContextPath() + "/company/detail/";
			url += userId + "-" + pageNumber;

			if (StringUtils.isNotBlank(getAnchor())) {
				url += "#" + getAnchor();
			}
			return url;
		}

	}

}
