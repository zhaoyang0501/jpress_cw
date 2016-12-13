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

import java.util.ArrayList;
import java.util.List;

import com.jfinal.core.JFinal;

import io.jpress.core.render.freemarker.JTag;
import io.jpress.model.query.OptionQuery;
import io.jpress.utils.StringUtils;

public class SliderTag extends JTag {

	@Override
	public void onRender() {
		List<imagePair> list = new ArrayList<imagePair>();

		for (int i = 1; i <= 5; i++) {
			String src = OptionQuery.me().findValue("eyeself_index_slide" + i);
			if (StringUtils.isNotBlank(src)) {
				String href = OptionQuery.me().findValue("eyeself_index_slide" + i + "_link");
				if (href == null)
					href = "";
				list.add(new imagePair(src, href));
			}
		}

		renderText(buildHTML(list));
	}

	private String buildHTML(List<imagePair> list) {
		if (list == null || list.isEmpty()) {
			return "";
		}

		String firstImg = list.get(0).src;
		if (StringUtils.isNotBlank(JFinal.me().getContextPath())) {
			firstImg = JFinal.me().getContextPath() + "/" + firstImg;
		}

		return String.format(
				"<img id=\"slider\" class=\"headerImg\" style=\"height: 400px\" src=\"%s\" data-slideshow=\"%s\" > <input id=\"slidehrefs\" type=\"hidden\" value=\"%s\">",
				firstImg, buildSrc(list), buildHref(list));
	}

	private String buildSrc(List<imagePair> list) {
		StringBuffer srcBuilder = new StringBuffer();
		if (list.size() > 1) {
			for (int i = 1; i < list.size(); i++) {
				srcBuilder.append(JFinal.me().getContextPath());
				srcBuilder.append(list.get(i).src);
				if (i != list.size() - 1) {
					srcBuilder.append("|");
				}
			}
		}
		return srcBuilder.toString();

	}

	private String buildHref(List<imagePair> list) {
		StringBuffer hrefBuilder = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			hrefBuilder.append(list.get(i).href);
			if (i != list.size() - 1) {
				hrefBuilder.append("|");
			}
		}
		return  hrefBuilder.toString();
	}

	public static class imagePair {
		String src;
		String href;

		public imagePair(String src, String href) {
			super();
			this.src = src;
			this.href = href;
		}
	}

}
