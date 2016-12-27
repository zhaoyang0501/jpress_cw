package io.jpress.jp.admin.controller;



import java.io.File;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

import io.jpress.core.JBaseController;
import io.jpress.jp.interceptor.JaddContractInterceptor;
import io.jpress.jp.interceptor.JprogressInterceptor;
import io.jpress.jp.interceptor.JstatisticsInterceptor;
import io.jpress.jp.model.SetEnterpriseChange;
import io.jpress.jp.model.SetOrders;
import io.jpress.jp.model.SetProgress;
import io.jpress.jp.model.Statistics;
import io.jpress.jp.model.Tuser;
import io.jpress.jp.model.UserContract;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.AttachmentUtils;
import io.jpress.utils.StringUtils;

@RouterMapping(url = "/admin/data", viewPath = "/templates/jp")
public class _SetDataController extends JBaseController {
	/**
	 * 红线设置
	 */
	@Before(JstatisticsInterceptor.class)
	public void set_warn_statistics(){
		Statistics statistics = getModel(Statistics.class);
		Statistics sa=Statistics.DAO.findByStatistics();
		if(StringUtils.isNotBlank(statistics.getFlag())){
			if(sa==null){
				statistics.saveOrUpdate();
				setAttr("sa", statistics);
				renderAjaxResultForSuccess();
			}else{
				sa.setLprocess(statistics.getLprocess());
				sa.setLservice(statistics.getLservice());
				sa.setLstuff(statistics.getLstuff());
	//			sa.setFlag(Consts.STATISTICS_SETTING);
				sa.setWprocess(statistics.getWprocess());
				sa.setWservice(statistics.getWservice());
				sa.setWstuff(statistics.getWstuff());
				sa.saveOrUpdate();
				setAttr("sa", sa);
				renderAjaxResultForSuccess();
			}
		}
		setAttr("sa", sa);
	}
	
	/**
	 * 用户类型
	 */
//	public void set_userType(){
//		
//		List<SetUsertype> suList=SetUsertype.DAO.findAllSetUserType();
//		setAttr("suList", suList);
//		
//	}
//	public void set_userType_am(){
//		SetUsertype sut = getModel(SetUsertype.class);
//		BigInteger id=getParaToBigInteger("id");
//		if(id!=null){
//			SetUsertype su = SetUsertype.DAO.findById(id);
//			if(StringUtils.isNotBlank(sut.getUserType())){
//				
//				su.setUserType(sut.getUserType());
//				su.saveOrUpdate();
//				renderAjaxResultForSuccess();
//				
//			}else {
//				setAttr("id", id);
//				setAttr("su", su);
//			}
//			
//		}else {
//			if(StringUtils.isNotBlank(sut.getUserType())){
//				SetUsertype newsut= new SetUsertype();
//				newsut.setCreated(new Date());
//				newsut.setUserType(sut.getUserType());
//				newsut.save();
//				renderAjaxResultForSuccess();
//			}
//		}
//	}
//	
//	public void set_userType_delete(){
//		BigInteger id=getParaToBigInteger("id");
//		SetUsertype su = SetUsertype.DAO.findById(id);
//		su.delete();
//		redirect("/admin/data/set_userType");
//	}
//	
	
	
	/**
	 * 进度办理事项
	 */
	
	public void set_progress(){
		List<SetProgress> spList=SetProgress.DAO.findAllSetProgress();
		setAttr("spList", spList);
	}
	
	@Before(JprogressInterceptor.class)
	public void set_progress_am(){
		
		if(!isMultipartRequest()){
			BigInteger id=getParaToBigInteger("id");
			if(id!=null){
				SetProgress sp = SetProgress.DAO.findById(id);
				setAttr("id", id);
				setAttr("sp", sp);
			}
		}else{
			String icon = "";
			UploadFile file = getFile();
			BigInteger id=getParaToBigInteger("id");
			SetProgress spt = getModel(SetProgress.class);
			if (file != null) {
				icon = AttachmentUtils.moveFile(file);
			}
			if(id!=null){
				SetProgress sp = SetProgress.DAO.findById(id);
				String deleteIcon = sp.getIcon();
				sp.setProgressType(spt.getProgressType());
				sp.setIcon(icon.replace("\\", "/"));
				sp.saveOrUpdate();
				File delFile= new File(PathKit.getWebRootPath() +deleteIcon);
				if(delFile.exists()){delFile.delete();}
				renderAjaxResultForSuccess();
			}else{
				
				SetProgress newspt= new SetProgress();
				newspt.setCreated(new Date());
				newspt.setProgressType(spt.getProgressType());
				newspt.save();
				newspt.setIcon(icon.replace("\\", "/"));
				newspt.saveOrUpdate();
				renderAjaxResultForSuccess();
			}
		}
		
		
		
		
		
//		SetProgress spt = getModel(SetProgress.class);
//		BigInteger id=getParaToBigInteger("id");
//		if(id!=null){
//			SetProgress sp = SetProgress.DAO.findById(id);
//			if(StringUtils.isNotBlank(spt.getProgressType())){
//				
//				sp.setProgressType(spt.getProgressType());
//				sp.saveOrUpdate();
//				renderAjaxResultForSuccess();
//				
//			}else {
//				setAttr("id", id);
//				setAttr("sp", sp);
//			}
//			
//		}else {
//			if(StringUtils.isNotBlank(spt.getProgressType())){
//				SetProgress newspt= new SetProgress();
//				newspt.setCreated(new Date());
//				newspt.setProgressType(spt.getProgressType());
//				newspt.save();
//				renderAjaxResultForSuccess();
//			}
//		}
	}
	@Before(JprogressInterceptor.class)
	public void set_progress_delete(){
		BigInteger id=getParaToBigInteger("id");
		SetProgress sp = SetProgress.DAO.findById(id);
		sp.delete();
		redirect("/admin/data/set_progress");
	}
	
	
	/**
	 * 下单事项
	 */
	public void set_orders(){
		List<SetOrders> spList=SetOrders.DAO.findAllSetOrders();
		setAttr("spList", spList);
	}
	@Before(JprogressInterceptor.class)
	public void set_orders_am(){
		if(!isMultipartRequest()){
			BigInteger id=getParaToBigInteger("id");
			if(id!=null){
				SetOrders sp = SetOrders.DAO.findById(id);
				setAttr("id", id);
				setAttr("sp", sp);
			}
		}else{
			String icon = "";
			UploadFile file = getFile();
			BigInteger id=getParaToBigInteger("id");
			SetOrders spt = getModel(SetOrders.class);
			if (file != null) {
				icon = AttachmentUtils.moveFile(file);
			}
			if(id!=null){
				SetOrders sp = SetOrders.DAO.findById(id);
				String deleteIcon = sp.getIcon();
				sp.setOrderType(spt.getOrderType());
				sp.setFlag(spt.getFlag());
				sp.setIcon(icon.replace("\\", "/"));
				sp.saveOrUpdate();
				File delFile= new File(PathKit.getWebRootPath() +deleteIcon);
				if(delFile.exists()){delFile.delete();}
				renderAjaxResultForSuccess();
			}else{
				SetOrders newspt= new SetOrders();
				newspt.setCreated(new Date());
				newspt.setOrderType(spt.getOrderType());
				newspt.setFlag(spt.getFlag());
				newspt.setIcon(icon.replace("\\", "/"));
				newspt.save();
				renderAjaxResultForSuccess();
			}
		}
		
		
		
//		SetOrders spt = getModel(SetOrders.class);
//		BigInteger id=getParaToBigInteger("id");
//		if(id!=null){
//			SetOrders sp = SetOrders.DAO.findById(id);
//			if(StringUtils.isNotBlank(spt.getOrderType())){
//				
//				sp.setOrderType(spt.getOrderType());
//				sp.setFlag(spt.getFlag());
//				sp.saveOrUpdate();
//				renderAjaxResultForSuccess();
//				
//			}else {
//				
//				setAttr("id", id);
//				setAttr("sp", sp);
//			}
//			
//		}else {
//			if(StringUtils.isNotBlank(spt.getOrderType())){
//				SetOrders newspt= new SetOrders();
//				newspt.setCreated(new Date());
//				newspt.setOrderType(spt.getOrderType());
//				newspt.setFlag(spt.getFlag());
//				newspt.save();
//				renderAjaxResultForSuccess();
//			}
//		}
	}
	@Before(JprogressInterceptor.class)
	public void set_orders_delete(){
		
		BigInteger id=getParaToBigInteger("id");
		SetOrders sp = SetOrders.DAO.findById(id);
		sp.delete();
		redirect("/admin/data/set_orders");
		
	}
	
	/**
	 * 更多下单详情信息
	 */
	public void set_orders_detail(){
		List<SetOrders> spList=SetOrders.DAO.findSetOrdersByFlag();
		setAttr("spList", spList);
	}
	
	/**
	 * 公司更变
	 */
	
	public void set_enterprise_change(){
		keepPara();
		BigInteger setOrdersId= getParaToBigInteger("setOrdersId");
		List<SetEnterpriseChange> secList=SetEnterpriseChange.DAO.findAllSetEnterpriseChange(setOrdersId);
		setAttr("secList", secList);
	}
	@Before(JprogressInterceptor.class)
	public void set_enterprise_change_am(){
		BigInteger setOrdersId = getParaToBigInteger("setOrdersId");
		keepPara();
		SetEnterpriseChange secv = getModel(SetEnterpriseChange.class);
		BigInteger id=getParaToBigInteger("id");
		if(id!=null){
			SetEnterpriseChange sec = SetEnterpriseChange.DAO.findById(id);
			
			if(StringUtils.isNotBlank(secv.getChangeTitle())){
				sec.setSetOrdersId(setOrdersId);
				sec.setChangeTitle(secv.getChangeTitle());
				sec.saveOrUpdate();
				
				renderAjaxResultForSuccess();
				
			}else{
				setAttr("id", id);
				setAttr("sec", sec);
			}
			
		}else {
			if(StringUtils.isNotBlank(secv.getChangeTitle())){
				SetEnterpriseChange newsecv= new SetEnterpriseChange();
				newsecv.setCreated(new Date());
				newsecv.setChangeTitle(secv.getChangeTitle());
				newsecv.setSetOrdersId(setOrdersId);
				newsecv.save();
				renderAjaxResultForSuccess();
			}
		}
	}
	@Before(JprogressInterceptor.class)
	public void set_enterprise_change_delete(){
		BigInteger id=getParaToBigInteger("id");
		SetEnterpriseChange sec = SetEnterpriseChange.DAO.findById(id);
		sec.delete();
		redirect("/admin/data/set_enterprise_change");
	}
	
	
	/**
	 * 外勤人员 ,添加联系人员
	 */
	@Before(JaddContractInterceptor.class)
	public void user_contract(){
		BigInteger userId =getParaToBigInteger("id");
		List<User> lists1 = Tuser.TUSER.findRoleById("会计经理");
		List<User> lists2 = Tuser.TUSER.findRoleById("外勤经理");
		setAttr("users1", lists1);
		setAttr("users2", lists2);
		UserContract uct = getModel(UserContract.class);
		
		if(uct.getAid()!=null){
			UserContract uc = UserContract.DAO.findByUserId(userId);
			User auser= UserQuery.me().findById(BigInteger.valueOf(uct.getAid()));
			User suser= UserQuery.me().findById(BigInteger.valueOf(uct.getSid()));
			if(uc==null){
				UserContract userContract = new UserContract();
				userContract.setUserId(userId);
				userContract.setSid(uct.getSid());
				userContract.setAid(uct.getAid());
				userContract.setAccountant(auser.getCompany());
				userContract.setAccountantMobile(auser.getUsername());
				userContract.setService(suser.getCompany());
				userContract.setServiceMobile(suser.getUsername());
				
				userContract.setCreated(new Date());
				userContract.saveOrUpdate();
				renderAjaxResultForSuccess();
			}else{
				uc.setSid(uct.getSid());
				uc.setAid(uct.getAid());
				uc.setAccountant(auser.getCompany());
				uc.setAccountantMobile(auser.getUsername());
				uc.setService(suser.getCompany());
				uc.setServiceMobile(suser.getUsername());
				uc.saveOrUpdate();
				renderAjaxResultForSuccess();
			}
		}else{
			UserContract uc = UserContract.DAO.findByUserId(userId);
			setAttr("uc", uc);
			setAttr("userId", userId);
		}
 	}
}
