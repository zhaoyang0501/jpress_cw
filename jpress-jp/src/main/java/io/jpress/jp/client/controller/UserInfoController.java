package io.jpress.jp.client.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import io.jpress.core.JBaseController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.jp.Consts;
import io.jpress.jp.interceptor.UrlIntereptor;
import io.jpress.jp.interceptor.UserStatusInterceptor;
import io.jpress.jp.interceptor.WechatGetOpenIdInterceptor;
import io.jpress.jp.model.Cash;
import io.jpress.jp.model.Contract;
import io.jpress.jp.model.Myinfo;
import io.jpress.jp.model.PaymentRecords;
import io.jpress.jp.model.UserContract;
import io.jpress.jp.vo.DetailTax;
import io.jpress.model.Attachment;
import io.jpress.model.User;
import io.jpress.model.query.AttachmentQuery;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.CookieUtils;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;

@Before({WechatGetOpenIdInterceptor.class,UserStatusInterceptor.class})
@RouterMapping(url = "/Information", viewPath = "/templates/jp/client")
public class UserInfoController extends JBaseController {
	private static final Log log = Log.getLog(UserInfoController.class);
	public void client_myInfo(){
		
//		String openId = "c3b36939-9814-4304-ba50-73401e997add";
		String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
		User user = UserQuery.me().findFirstFromMetadata("openId", openId);
		
		if(user==null){return;}
		setAttr("user", user);
		UserContract uc = UserContract.DAO.findByUserId(user.getId());
		setAttr("uc", uc);
		
		
		BigDecimal integral=PaymentRecords.DAO.findCountAmount(user.getId());
		if(integral==null){return;}
		if(integral!=null && integral.compareTo(new BigDecimal("3000"))==1){
			integral=integral.multiply(new BigDecimal("0.1")); //积分
			setAttr("integral", integral);
		} 
		
//      公司当月收入，当月税收
		List<Attachment> atth = AttachmentQuery.me().findList(user.getId(), null, Consts.FILE_EXCEL, null, null, null, null, null);
//		Page<Attachment> atth = AttachmentQuery.me().paginate(pageNumber, pageSize, userId, contentId, type, flag, keyword, month, mime, orderBy)
		if(atth.size()==0){return;}
		String mdateStr=atth.get(0).getFlag();
		List<Cash> chlist= Cash.DAO.findCashByMdateWithContentId(user.getId(), mdateStr);
		DetailTax dt =null;
		for (Cash cash : chlist) {
			String month = cash.getMdate();
			if(dt == null){
				dt = new DetailTax();
			}
			switch (cash.getSubCode()) {
				case "6001":
					dt.setShouru(cash.getTwoc());
					break;
				case "22210106":
					dt.setOutput(cash.getTwoc());
					break;
				case "22210101":
					dt.setInputTax(cash.getTwod());
					break;
				case "22210104":
					dt.setReliefs(cash.getTwod());
					break;
				case "222108":
					dt.setCitytax(cash.getTwoc());
					break;
				case "222113":
					dt.setEduTax(cash.getTwoc());
					break;
				case "222117":
					dt.setLocalEduTax(cash.getTwoc());
					break;
				case "222106":
					dt.setIncomeTax(cash.getTwoc());
					break;
				default:
					break;
			}
			if(cash.getSubName().equals("印花税")){
				dt.setStampTax(cash.getTwod());
			}
		}
		setAttr("shouru", dt.getShouru());
		setAttr("dt", dt);
		
		
		
	}
	/**
	 * 我的信息
	 */
	public void client_myMessage(){
		BigInteger userId = getParaToBigInteger("userId");
		Page<Myinfo> page = Myinfo.DAO.pageByUserId(getPageNumber(), getPageSize(), userId);
		setAttr("page", page);
	}
	/**
	 * 我的信息
	 */
	public void client_comMessage(){
		BigInteger id = getParaToBigInteger("id");
		Myinfo myinfo  = Myinfo.DAO.findById(id);
		setAttr("myinfo", myinfo);
	}
	
}
