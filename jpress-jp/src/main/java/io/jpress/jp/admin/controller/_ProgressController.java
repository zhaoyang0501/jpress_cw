package io.jpress.jp.admin.controller;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import io.jpress.core.JBaseController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.jp.Consts;
import io.jpress.jp.interceptor.JAdminInterceptor;
import io.jpress.jp.interceptor.JprogressInterceptor;
import io.jpress.jp.model.Progress;
import io.jpress.jp.model.SetProgress;
import io.jpress.jp.utils.DateUtils;
import io.jpress.model.Content;
import io.jpress.model.query.ContentQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

@RouterMapping(url = "/admin/progress", viewPath = "/templates/jp")
public class _ProgressController extends JBaseController {
	
	public void show_progress_item(){
//		BigInteger contentId = getParaToBigInteger("etpId");
		keepPara();
		List<SetProgress> spList=SetProgress.DAO.findAllSetProgress();
		setAttr("spList", spList);
		
		BigInteger userId = getParaToBigInteger("userId");
		Page<Progress> page = Progress.DAO.findPageByUserId(getPageNumber(), getPageSize(),userId);
		setAttr("page", page);
	}
	/**
	 * 
	 */
	public void progressDetail(){
		BigInteger id = getParaToBigInteger("id");
		Progress progress = Progress.DAO.findById(id);
		setAttr("cp", progress);
	}
	
	/**
	 *  注册进度
	 */
	@Before(JprogressInterceptor.class)
	public void content_add_progress(){
		keepPara();
		BigInteger progressId =getParaToBigInteger("progressId");
		BigInteger userId =getParaToBigInteger("userId");
		Progress prpg = getBean(Progress.class);
		Progress progt=Progress.DAO.findFirstByContentIdAndProgressId(userId,progressId);
//		Content cp=ContentQuery.me().findFirstByModuleAndObjectId(Consts.MODULE_PROGRESS, object_id);
		if(StringUtils.isNotBlank(prpg.getStepProgress())){
			
//		Progress tempg = Progress.DAO.findFirstByContentWithStep(contentId,progressId);
			if (progt==null || ("4".equals(progt.getStepProgress()) && "1".equals(progt.getStatusFhc()))) {
				Progress pg = new Progress();
				pg.setSetProgressId(progressId);
				pg.setUserId(userId);
				if(StringUtils.isNotBlank(prpg.getStepProgress()) && prpg.getStepProgress().equals("1")){
					
					pg.setDateMt(prpg.getDateMt());
					if(StringUtils.isBlank(prpg.getStatusMtc())){
						pg.setStatusMtc("0");
					}else {
						pg.setStatusMtc(prpg.getStatusMtc());
					}
					pg.setAddressMt(prpg.getAddressMt());
					pg.setLactMt(prpg.getLactMt());
					pg.setUploadMt(prpg.getUploadMt());
					pg.setOperatorMt(prpg.getOperatorMt());
					pg.setPhoneMt(prpg.getPhoneMt());
					pg.setStepProgress(prpg.getStepProgress());
					pg.setCreated(new Date());
					pg.saveOrUpdate();
					
				}
				if(StringUtils.isNotBlank(prpg.getStepProgress()) && prpg.getStepProgress().equals("2")){
					pg.setDatePd(prpg.getDatePd());
					if(StringUtils.isBlank(prpg.getStatusPdc())){
						pg.setStatusPdc("0");
					}else {
						pg.setStatusPdc(prpg.getStatusPdc());
					}
					pg.setAddressPd(prpg.getAddressPd());
					pg.setDonePd(prpg.getDatePd());
					pg.setWaitPd(prpg.getWaitPd());
					pg.setOperatorPd(prpg.getOperatorPd());
					pg.setPhonePd(prpg.getPhonePd());
					pg.setStepProgress(prpg.getStepProgress());
					pg.saveOrUpdate();
				}
				if(StringUtils.isNotBlank(prpg.getStepProgress()) && prpg.getStepProgress().equals("3")){
					pg.setDateTax(prpg.getDateTax());
					if(StringUtils.isBlank(prpg.getStatusTaxc())){
						pg.setStatusTaxc("0");
					}else {
						pg.setStatusTaxc(prpg.getStatusTaxc());
					}
					pg.setAddressTax(prpg.getAddressTax());
					pg.setDoneTax(prpg.getDateTax());
					pg.setWaitTax(prpg.getWaitTax());
					pg.setOperatorTax(prpg.getOperatorTax());
					pg.setPhoneTax(prpg.getPhoneTax());
					pg.setStepProgress(prpg.getStepProgress());
					pg.saveOrUpdate();
				}
				if(StringUtils.isNotBlank(prpg.getStepProgress()) && prpg.getStepProgress().equals("4")){
					pg.setDateFh(prpg.getDateFh());
					if(StringUtils.isBlank(prpg.getStatusFhc())){
						pg.setStatusFhc("0");
					}else {
						pg.setStatusFhc(prpg.getStatusFhc());
					}
					pg.setPreTimeFh(prpg.getPreTimeFh());
					pg.setOperatorFh(prpg.getOperatorFh());
					pg.setPhoneFh(prpg.getPhoneFh());
					pg.setStepProgress(prpg.getStepProgress());
					pg.saveOrUpdate();
				}
//				setAttr("cp", pg);
				renderAjaxResultForSuccess();
			}else {
				if(StringUtils.isNotBlank(prpg.getStepProgress()) && prpg.getStepProgress().equals("1")){
					progt.setDateMt(prpg.getDateMt());
					if(StringUtils.isBlank(prpg.getStatusMtc())){
						progt.setStatusMtc("0");
					}else {
						progt.setStatusMtc(prpg.getStatusMtc());
					}
					progt.setAddressMt(prpg.getAddressMt());
					progt.setLactMt(prpg.getLactMt());
					progt.setUploadMt(prpg.getUploadMt());
					progt.setOperatorMt(prpg.getOperatorMt());
					progt.setPhoneMt(prpg.getPhoneMt());
					progt.setStepProgress(prpg.getStepProgress());
					progt.saveOrUpdate();
					
				}
				if(StringUtils.isNotBlank(prpg.getStepProgress()) && prpg.getStepProgress().equals("2")){
					progt.setDatePd(prpg.getDatePd());
					if(StringUtils.isBlank(prpg.getStatusPdc())){
						progt.setStatusPdc("0");
					}else {
						progt.setStatusPdc(prpg.getStatusPdc());
					}
					progt.setAddressPd(prpg.getAddressPd());
					progt.setDonePd(prpg.getDatePd());
					progt.setWaitPd(prpg.getWaitPd());
					progt.setOperatorPd(prpg.getOperatorPd());
					progt.setPhonePd(prpg.getPhonePd());
					progt.setStepProgress(prpg.getStepProgress());
					progt.saveOrUpdate();
				}
				if(StringUtils.isNotBlank(prpg.getStepProgress()) && prpg.getStepProgress().equals("3")){
					progt.setDateTax(prpg.getDateTax());
					if(StringUtils.isBlank(prpg.getStatusTaxc())){
						progt.setStatusTaxc("0");
					}else {
						progt.setStatusTaxc(prpg.getStatusTaxc());
					}
					progt.setAddressTax(prpg.getAddressTax());
					progt.setDoneTax(prpg.getDoneTax());
					progt.setWaitTax(prpg.getWaitTax());
					progt.setOperatorTax(prpg.getOperatorTax());
					progt.setPhoneTax(prpg.getPhoneTax());
					progt.setStepProgress(prpg.getStepProgress());
					progt.saveOrUpdate();
				}
				
				if(StringUtils.isNotBlank(prpg.getStepProgress()) && prpg.getStepProgress().equals("4")){
					progt.setDateFh(prpg.getDateFh());
					if(StringUtils.isBlank(prpg.getStatusFhc())){
						progt.setStatusFhc("0");
					}else {
						progt.setStatusFhc(prpg.getStatusFhc());
					}
					progt.setPreTimeFh(prpg.getPreTimeFh());
					progt.setOperatorFh(prpg.getOperatorFh());
					progt.setPhoneFh(prpg.getPhoneFh());
					progt.setStepProgress(prpg.getStepProgress());
					progt.saveOrUpdate();
				}
//				setAttr("cp", progt);
				renderAjaxResultForSuccess();
			}
			
		}else {
			if(progt==null || ("4".equals(progt.getStepProgress()) && "1".equals(progt.getStatusFhc()))){
				
			}else{setAttr("cp", progt);}
		}	
	}
	

	
//	public void client_progress(){
//		String objectId = getPara("objectId");
//		BigInteger object_id = new BigInteger(objectId);
////		Content etp =ContentQuery.me().findById(object_id);
//		Content cp=ContentQuery.me().findFirstByModuleAndObjectId(Consts.MODULE_PROGRESS, object_id);
//		cp.getMetadatas();
//		setAttr("cp", cp);
//	}
}
