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

import com.eyeself.query.MyUserQuery;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.render.freemarker.BasePaginateTag;
import io.jpress.core.render.freemarker.JTag;
import io.jpress.model.User;
import io.jpress.utils.StringUtils;

public class CompanyPageTag extends JTag {

	int pageNumber;
	String orderby;
	String type;

	public CompanyPageTag(int pageNumber, String type, String orderby) {
		this.pageNumber = pageNumber;
		this.orderby = orderby;
		this.type = type;
	}

	@Override
	public void onRender() {

		int pagesize = getParamToInt("pageSize", 10);

		Page<User> page = MyUserQuery.me().paginateCompany(pageNumber, pagesize, type, orderby);
		setVariable("page", page);

		IndexPaginateTag pagination = new IndexPaginateTag(page, type, orderby);
		setVariable("pagination", pagination);

		renderBody();
	}

	public static class IndexPaginateTag extends BasePaginateTag {

		String type;
		String orderby;

		public IndexPaginateTag(Page<User> page, String type, String orderby) {
			super(page);
			this.type = type;
			this.orderby = orderby;
		}

		@Override
		protected String getUrl(int pageNumber) {
			String url = JFinal.me().getContextPath() + "/company";

			if (StringUtils.isNotBlank(type)) {
				url += "/" + type + "-" + pageNumber;
			} else {
				url += "/" + pageNumber;
			}

			if (enalbleFakeStatic()) {
				url += getFakeStaticSuffix();
			}

			if (StringUtils.isNotBlank(orderby)) {
				url += "?orderby=" + orderby;
			}

			if (StringUtils.isNotBlank(getAnchor())) {
				url += "#" + getAnchor();
			}

			return url;
		}

	}

}
