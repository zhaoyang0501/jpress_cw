package io.jpress.jp.client.controller;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

import io.jpress.core.JBaseController;
import io.jpress.jp.Consts;
import io.jpress.jp.interceptor.UrlIntereptor;
import io.jpress.jp.interceptor.UserStatusInterceptor;
import io.jpress.jp.interceptor.WechatGetOpenIdInterceptor;
import io.jpress.jp.model.Cash;
import io.jpress.jp.model.Lastbill;
import io.jpress.jp.model.Myinfo;
import io.jpress.jp.model.Statistics;
import io.jpress.jp.utils.DateUtils;
import io.jpress.jp.utils.JpAttachmentUtils;
import io.jpress.jp.vo.DetailTax;
import io.jpress.jp.vo.IncomeTax;
import io.jpress.model.Attachment;
import io.jpress.model.User;
import io.jpress.model.query.AttachmentQuery;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.CookieUtils;
import io.jpress.utils.StringUtils;

@Before({UrlIntereptor.class,WechatGetOpenIdInterceptor.class,UserStatusInterceptor.class})
@RouterMapping(url = "/account", viewPath = "/templates/jp/client")
public class AccountController extends JBaseController {
	
	private static final Log log = Log.getLog(AccountController.class);
	@Clear({WechatGetOpenIdInterceptor.class,UserStatusInterceptor.class})
	public void client_accountData(){
		String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
		if(StringUtils.isNotBlank(openId)){
			User user = UserQuery.me().findFirstFromMetadata("openId", openId);
			if(user==null){return;}
			setAttr("role", user.getRole());
		}
	}
	
	//来往账务
	public void client_comAccount(){
		String mdate = getPara("mdate");
		String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
		String mdateStr="";
		User user = UserQuery.me().findFirstFromMetadata("openId", openId);
		
		if(user==null){return;}
		if(StringUtils.isNotBlank(mdate)){
			mdate=mdate.replace("-","年")+"月";
			mdateStr=mdate;
			List<Cash> chlist= Cash.DAO.findAccountByIdCashs(user.getId(), mdateStr);
			
			setAttr("chlist", chlist);
			setAttr("company", user.getCompany());
		}else{

//			List<Attachment> atth = AttachmentQuery.me().findList(null, contentId, Consts.FILE_EXCEL, null, null, null, null, null);
			List<Attachment> atth = AttachmentQuery.me().findList(user.getId(), null, Consts.FILE_EXCEL, null, null, null, null, null);
			if(atth.size()==0){return;}
			
				mdateStr=atth.get(0).getFlag();
				List<Cash> chlist= Cash.DAO.findAccountByIdCashs(user.getId(), mdateStr);
				setAttr("chlist", chlist);
				setAttr("company", user.getCompany());
			
		}
	}
	
	  /**红线提醒
	   * 收入统计
	   */
	public void client_statistics(){
		String mdate = getPara("mdate");
		
//		String openId = "c3b36939-9814-4304-ba50-73401e997add";
		String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
		User user = UserQuery.me().findFirstFromMetadata("openId", openId);
		if(user==null){return;}
		String mdateStr="";
		
		if(StringUtils.isNotBlank(mdate)){
			mdate=mdate.replace("-","年")+"月";
			mdateStr=mdate;
		}else{
			List<Attachment> atth = AttachmentQuery.me().findList(user.getId(), null, Consts.FILE_EXCEL, null, null, null, null, null);
			if(atth.size()==0){return;}
			mdateStr=atth.get(0).getFlag();
			
		}
		
		
		List<Cash> chlist = Cash.DAO.findStatisticByIdCashs(user.getId(), mdateStr);
		Statistics st = Statistics.DAO.findByStatistics();
		
		//判断红线提现，上限设置
		BigDecimal sellAmount = new BigDecimal("0");
		BigDecimal sellmon = new BigDecimal("0");
		for (int i = 0; i < chlist.size(); i++) {
			//加工修理修配提醒
			if(chlist.get(i).getSubName().equals("加工修理修配")){
				BigDecimal sublp = st.getLprocess().subtract(new BigDecimal(chlist.get(i).getThreec()));
				setAttr("lprocess", st.getLprocess());//修理修配  上线
				setAttr("sublp", sublp); //修理修配 可赠收
				setAttr("lpy", chlist.get(i).getThreec());
				setAttr("lpm", chlist.get(i).getTwoc());
			}
			//销售收入提醒
			else if(chlist.get(i).getSubName().equals("销售收入")){
				BigDecimal subls = st.getLstuff().subtract(new BigDecimal(chlist.get(i).getThreec()));
				setAttr("lstuff", st.getLstuff()); //销售收入上线
				setAttr("subls", subls);//销售收入可赠收
				setAttr("lsy", chlist.get(i).getThreec()); //本年累计收入
				setAttr("lsm", chlist.get(i).getTwoc());// 本月收入
			}
			else{
				sellAmount = sellAmount.add(new BigDecimal(chlist.get(i).getThreec()));
				sellmon = sellmon.add(new BigDecimal(chlist.get(i).getTwoc()));
			}
			
		}
		// 销售服务提醒
		
		BigDecimal subsell = st.getLservice().subtract(sellAmount);
		setAttr("lservice", st.getLservice());//上线
		setAttr("sellAmount", sellAmount);//服务本年累计收入
		setAttr("sellmon", sellmon);//服务本月收入
		setAttr("subsell", subsell);//服务可赠收
		
		setAttr("st", st);//上线设置全部内容
		setAttr("mdateStr", mdateStr);//月份
		System.err.println(chlist);
		setAttr("chlist", chlist);// 统计明细
	}
	
	// 增值税负税
	public void client_burdentax(){
		String mdate = getPara("mdate");
//		String openId = "c3b36939-9814-4304-ba50-73401e997add";
		String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
		User user = UserQuery.me().findFirstFromMetadata("openId", openId);
		if(user==null){return;}
		//要排断
	String mdateStr="";
		
		if(StringUtils.isNotBlank(mdate)){
//			Attachment atth = AttachmentQuery.me().findFirst(null, contentId, Consts.FILE_EXCEL, mdate, null, null, null, null);
//			mdateStr=atth.getFlag();
			mdate=mdate.replace("-","年")+"月";
			mdateStr=mdate;
		}else{
			List<Attachment> atth = AttachmentQuery.me().findList(user.getId(), null, Consts.FILE_EXCEL, null, null, null, null, null);
			if(atth.size()==0){return;}
			mdateStr=atth.get(0).getFlag();
		}
		
		List<Cash> chlist = Cash.DAO.findburdenTaxByCashs(user.getId(), mdateStr);
		Lastbill lb=Lastbill.DAO.findbillById(user.getId(),mdateStr);
		
		BigDecimal income = new BigDecimal("0");
		BigDecimal output = new BigDecimal("0");
		BigDecimal subfree = new BigDecimal("0");
		BigDecimal mainbm = new BigDecimal("0");
		BigDecimal mainby = new BigDecimal("0");
		BigDecimal taxed = new BigDecimal("0");
		
		BigDecimal vat = new BigDecimal("0");
		BigDecimal taxff = new BigDecimal("0");
		BigDecimal taxffy = new BigDecimal("0");
		
		for (int i = 0; i < chlist.size(); i++) {
			switch (chlist.get(i).getSubCode()) {
			case "222101":
				if(StringUtils.isNotBlank(chlist.get(i).getTwod())){
					income = income.add(new BigDecimal(chlist.get(i).getTwod()));
				
				}
				 
				break;
			case "22210106":
				if(StringUtils.isNotBlank(chlist.get(i).getTwoc())){
					 output = output.add(new BigDecimal(chlist.get(i).getTwoc()));
					 
				}
				break;
			case "22210104":
				if(StringUtils.isNotBlank(chlist.get(i).getTwod())){
				 subfree = subfree.add(new BigDecimal(chlist.get(i).getTwod()));
				}
				break;
			case "6001":
				if(StringUtils.isNotBlank(chlist.get(i).getTwoc())){
					 mainbm =mainbm.add(new BigDecimal(chlist.get(i).getTwoc()));
				}
				if(StringUtils.isNotBlank(chlist.get(i).getThreec())){
					mainby =mainby.add(new BigDecimal(chlist.get(i).getThreec()));
				}
				
				break;
			case "22210102":
				if(StringUtils.isNotBlank(chlist.get(i).getThreed())){
					taxed =taxed.add(new BigDecimal(chlist.get(i).getThreed()));
				}
				break;

			default:
				break;
			}
		}
		BigDecimal lastmon=new BigDecimal(lb.getLastmon());
		
		
		//百分比格式
		DecimalFormat decFormat = new DecimalFormat("#.0000"); //几位小数
		NumberFormat percent = NumberFormat.getPercentInstance(); //百分比格式
		
		if(output.compareTo(new BigDecimal("0"))==1){
			 vat = vat.add(output.subtract(income).subtract(lastmon).subtract(subfree));//应交增值税
			
			if(mainbm.compareTo(new BigDecimal("0"))==1){
				 taxff =taxff.add(new BigDecimal(decFormat.format(vat)).divide(mainbm,4)); //税负
			}
			if(mainby.compareTo(new BigDecimal("0"))==1){
				 taxffy =taxffy.add(new BigDecimal(decFormat.format(taxed)).divide(mainby,4)); //累计税负
			}
		}
		
				
		setAttr("income", income);
		setAttr("output", output);
		setAttr("subfree", subfree);
		
		setAttr("vat", vat);
		setAttr("taxff", percent.format(taxff));
		setAttr("taxffy", percent.format(taxffy));
		
		setAttr("chlist", chlist);
		setAttr("lb", lb);
		setAttr("company", user.getCompany());
	}
	
	
	//所得税测算
	public void client_incometax(){
//		String openId = "c3b36939-9814-4304-ba50-73401e997add";
		String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
		User user = UserQuery.me().findFirstFromMetadata("openId", openId);
		if(user==null){return;}
		String ydate=DateUtils.date("yyyy");
		
		HashMap<String, IncomeTax> map =new LinkedHashMap<String, IncomeTax>();
		List<Cash> chList = Cash.DAO.findIncometax(user.getId(),ydate);
		if(chList.size()==0){return;}
		IncomeTax it =null;
//		List<IncomeTax>
		for(Cash cash : chList){
			String month = cash.getMdate();
			it = map.get(month);
			if(it==null){
				it = new IncomeTax();
			}
			if("222106".equals(cash.getSubCode())){
				it.setSeasonIncome(cash.getTwoc());
				it.setYearIncome(cash.getThreec());
			}
			if ("6001".equals(cash.getSubCode())) {
				it.setYearMajor(cash.getThreec());
			}
			
			it.setMonth(cash.getMdate());
			
			map.put(month, it);
		}
		
		
		setAttr("ch", chList);
		setAttr("map", map);
		
	}
	//纳税明细
	public void client_detailtax(){
		String mdate = getPara("mdate");
		String mdate1 = getPara("mdate1");
		String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
		User user = UserQuery.me().findFirstFromMetadata("openId", openId);
		if(user==null){return;}
		if(StringUtils.areNotBlank(mdate,mdate1)){
		mdate=mdate.replace("-","年")+"月";
		mdate1=mdate1.replace("-","年")+"月";
		
		
		List<Cash> list  = Cash.DAO.findDetailtax(user.getId(),mdate,mdate1);
		if(list.size()==0){return;}
//		List<Nasuimingxi> nasuiList = new ArrayList<Nasuimingxi>();
		HashMap<String, DetailTax> map =new LinkedHashMap<String, DetailTax>();
		DetailTax dt =null;
		for (Cash cash : list) {
			String month = cash.getMdate();
			dt = map.get(month);
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
			dt.setMonth(cash.getMdate());
			
			map.put(month, dt); //map 的key是唯一的
		}
		
		setAttr("map",map);
		setAttr("mdate", mdate);
		setAttr("mdate1", mdate1);
		}
	}
	/**
	 * 上传账单
	 */
	public void client_uploadBill(){
		String picBill = "";
		if(isMultipartRequest()){
			UploadFile file = getFile();
	//		String openId = "c3b36939-9814-4304-ba50-73401e997add";
			String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
			User user = UserQuery.me().findFirstFromMetadata("openId", openId);
			if(user==null){return;}
			if (file != null) {
				picBill = JpAttachmentUtils.movePic(file,user.getId());
			}
			
			String type = Consts.FILE_BILL;
//			Attachment ath = AttachmentQuery.me().findFirst(null, contentId, type, null, null, null, null, null);
//			if(ath==null){
				Attachment atht=new Attachment();
				atht.setUserId(user.getId());
				atht.setPath(picBill.replace("\\","/"));
				atht.setType(type);
				atht.setCreated(new Date());
				atht.setFlag(DateUtils.date("yyyy年MM月"));
				atht.saveOrUpdate();
				renderAjaxResultForSuccess(picBill, atht);
//			}else{
//				String deleteFile = ath.getPath();
//				ath.setContentId(contentId);
//				ath.setType(type);
//				ath.setPath(picBill);
//				ath.setCreated(new Date());
//				ath.setFlag(DateUtils.date("yyyy年MM月"));
//				ath.saveOrUpdate();
//				File delFile= new File(PathKit.getWebRootPath() +deleteFile);
//				if(delFile.exists()){delFile.delete();}
//				renderAjaxResultForSuccess(picBill,ath);
//			}
			
			setAttr("userId", user.getId());
//			redirect("/admin/uploadData/content_data?etpId="+etpId);
		}else{
	//		String openId = "c3b36939-9814-4304-ba50-73401e997add";
			String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
			User user = UserQuery.me().findFirstFromMetadata("openId", openId);
			if(user==null){return;}
			String type = Consts.FILE_BILL;
			Page<Attachment> page = AttachmentQuery.me().paginate(getPageNumber(), getPageSize(),user.getId(), null, type, null, null, null, null, null);
			setAttr("page", page);
			setAttr("userId", user.getId());
		}
		
	}
	
	/**
	 * 账单修改
	 */
	public void client_modifyBill(){
		
		
		//########################################
		String picBill = "";
		if(!isMultipartRequest()){
			renderAjaxResultForError("请选择账单！");
			return;
		}
			UploadFile file = getFile();
			String pid = getPara("pid");
			BigInteger id = new BigInteger(pid);
			
			Attachment att= AttachmentQuery.me().findById(id);
			
			if(att!=null){
				
				if (file != null) {
					picBill = JpAttachmentUtils.movePic(file,att.getUserId());
				}
				
				String deleteFile = att.getPath();
				att.setPath(picBill.replace("\\","/"));
				att.saveOrUpdate();
				File delFile= new File(PathKit.getWebRootPath() +deleteFile);
				if(delFile.exists()){delFile.delete();}
				
				renderAjaxResultForSuccess("修改成功",att);
				
			}else{
				renderAjaxResultForError("修改失败");
			}
			
	}
	
	
	public void client_deleteBill(){
		String pid = getPara("pid");
		BigInteger id=new BigInteger(pid);
		Attachment att= AttachmentQuery.me().findById(id);
		if(att!=null){
			String deleteFile = att.getPath();
			File delFile= new File(PathKit.getWebRootPath() +deleteFile);
			if(delFile.exists()){delFile.delete();}
			att.deleteById(id);
			renderAjaxResultForSuccess("删除成功");
			
		}else{
			renderAjaxResultForError("删除失败");
		}
	}
	
}
