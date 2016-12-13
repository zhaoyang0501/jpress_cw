package io.jpress.jp.admin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import io.jpress.core.JBaseController;
import io.jpress.jp.Consts;
import io.jpress.jp.interceptor.JstatisticsInterceptor;
import io.jpress.jp.model.Cash;
import io.jpress.jp.model.Lastbill;
import io.jpress.jp.model.Myinfo;
import io.jpress.jp.model.Statistics;
import io.jpress.jp.utils.DateUtils;
import io.jpress.jp.utils.JpAttachmentUtils;
import io.jpress.jp.utils.SmsUtils;
import io.jpress.model.Attachment;
import io.jpress.model.User;
import io.jpress.model.query.AttachmentQuery;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.upload.UploadFile;

@RouterMapping(url="/admin/uploadData", viewPath="/templates/jp")
@Before(JstatisticsInterceptor.class)
public class _UploadDataController extends JBaseController {
	/**
	 * 上传文件
	 * 
	 */
	public void content_data(){
		
		//哪些月份没有生成数据
			
//			String etpId=getPara("etpId");
			BigInteger userId =getParaToBigInteger("userId");
			String mdate=getPara("mdate");
			String jpflag = getPara("jpflag");
			String type = Consts.FILE_BILL;
			if("0".equals(jpflag)){
				List<Cash> chList= new Cash().DAO.findCashByMdateWithContentId(userId,mdate);
				setAttr("chList", chList);
			}else if("1".equals(jpflag)){
				String mdate2=mdate.replace("年","-").replace("月","");
//				List<Attachment> attlist =AttachmentQuery.me().findList(null, contentId, type, null, null, mdate2, null, null);
				List<Attachment> attlist =AttachmentQuery.me().findList(userId, null, type, null, null, mdate2, mdate2, mdate2);
				setAttr("attlist", attlist);
			}
			
			setAttr("jpflag", jpflag);
			setAttr("userId", userId);
		
	}
	
	public void uploadExcel(){

		
		Db.tx(new IAtom() {
			
			@Override
			public boolean run() throws SQLException {
				String dataFile="";
				if(!isMultipartRequest()){
					renderAjaxResultForError();
					return false;
				}
				UploadFile file = getFile();

				BigInteger userId =getParaToBigInteger("userId");
				String flag = getPara("flag");
				if (file != null) {
					dataFile = JpAttachmentUtils.moveFile(file,userId.toString(),flag);
				}
				
				
				String type = Consts.FILE_EXCEL;
//				Attachment ath = AttachmentQuery.me().findFirst(null, contentId, type, flag, null, null, null, null);
				Attachment ath = AttachmentQuery.me().findFirst(userId, null, type, flag, null, null, null, null);
				if(ath==null){
					Attachment atht=new Attachment();
				
					atht.setUserId(userId);
					atht.setPath(dataFile.replace("\\", "/"));
					atht.setType(type);
					atht.setCreated(new Date());
					atht.setFlag(flag);
					atht.saveOrUpdate();
				}else{
					String deleteFile = ath.getPath();
					ath.setUserId(userId);
					ath.setType(type);
					ath.setPath(dataFile.replace("\\", "/"));
					ath.setCreated(new Date());
					ath.setFlag(flag);
					ath.saveOrUpdate();
					File delFile= new File(PathKit.getWebRootPath() +deleteFile);
					if(delFile.exists()){delFile.delete();}
					
				}
				//上期留底
				String lastmon =getPara("lastmon");
				Lastbill lb=Lastbill.DAO.findbillById(userId,flag);			
				if(lb==null){
					Lastbill lab=new Lastbill();
					lab.setUserId(userId);
					lab.setMdate(flag);
					lab.setCreated(new Date());
					lab.setLastmon(lastmon);
					lab.saveOrUpdate();
				}else{
					lb.setUserId(userId);
					lb.setMdate(flag);
					lb.setCreated(new Date());
					lb.setLastmon(lastmon);
					lb.saveOrUpdate();
				}
				
				setAttr("userId", userId);
				renderAjaxResultForSuccess();
				
				return true;
			}
		});
		
			
	}
	
	/**
	 * 生成数据
	 */
	public void buildData(){
//		DECIMAL
//		String etpId=getPara("etpId");
		BigInteger userId =getParaToBigInteger("userId");
		String type=Consts.FILE_EXCEL;
		String flag=getPara("flag");
		Cash cash=Cash.DAO.findByBliudCashFlag(userId,flag);
		if(cash!=null){
			Db.update("delete from `cash` where userId ="+userId+" and mdate ='"+flag+"'");
		}
//		Attachment atth=AttachmentQuery.me().findFirst(null, contentId, type, flag, null, null, null, null);
		Attachment atth=AttachmentQuery.me().findFirst(userId, null, type, flag, null, null, null, null);
		File file = new File(PathKit.getWebRootPath()+atth.getPath());
		try {
			FileInputStream fis = new FileInputStream(file);
			HSSFWorkbook workbook= new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheetAt(0);
			int firstRowNum = 4;
			int lastRowNum = sheet.getLastRowNum();
			
			List<Cash> ls = new ArrayList<Cash>();
			
			for (int i = firstRowNum; i <= lastRowNum; i++) {
				Cash ch=new Cash();
				HSSFRow row1 = sheet.getRow(1);
				
				HSSFCell cell1 = row1.getCell(0);
				row1.getCell(0).setCellType(cell1.CELL_TYPE_STRING);
				ch.setComName(cell1.getStringCellValue());
				
				HSSFCell cell2 = row1.getCell(3);
				row1.getCell(3).setCellType(cell1.CELL_TYPE_STRING);
				ch.setMdate(cell2.getStringCellValue());
				
				ch.setUserId(userId);
				
				HSSFRow row = sheet.getRow(i);
				int lastCellNum = row.getLastCellNum();
				
				for (int j = 0; j < lastCellNum; j++) {
						HSSFCell cell = row.getCell(j);
						row.getCell(j).setCellType(cell.CELL_TYPE_STRING);
						String mon="";
						if("".equals(cell.getStringCellValue())){
							mon="0";
						}else{
							mon=cell.getStringCellValue();
						}
						switch (j) {
						case 0:
							ch.setSubCode(mon);
							break;
						case 1:
							ch.setSubName(mon);
							break;
						case 2:
							ch.setOned(mon);
							break;
						case 4:
							ch.setOnec(mon);
							break;
						case 5:
							ch.setTwod(mon);
							break;
						case 6:
							ch.setTwoc(mon);
							break;
						case 7:
							ch.setThreed(mon);
							break;
						case 8:
							ch.setThreec(mon);
							break;
						case 10:
							ch.setFourd(mon);
							break;
						case 11:
							ch.setFourc(mon);
							break;
						default:
							break;
						}
				}
				ls.add(ch);
			}
			Db.batchSave(ls, 1000);
			
			User user = UserQuery.me().findById(userId);
			if("小规模纳税人".equals(user.getRole())){
			//数据生成 红线提醒
				List<Cash> chlist = Cash.DAO.findStatisticByIdCashs(userId,flag);
				
				Statistics st = Statistics.DAO.findByStatistics();
				
				//判断红线提现，上限设置
				BigDecimal sellAmount = new BigDecimal("0");
				for (int i = 0; i < chlist.size(); i++) {
					//加工修理修配提醒
					if(chlist.get(i).getSubName().equals("加工修理修配")){
						if(new BigDecimal(chlist.get(i).getThreec()).compareTo(st.getLprocess())==1 || new BigDecimal(chlist.get(i).getTwoc()).compareTo(st.getLprocess())==0 ){
							// 发生一条消息得到我的信息里
//							System.err.println("您的加工修理修配达到了上线设置");
							SmsUtils.sendName("SMS_21360048",chlist.get(i).getThreec(), user.getUsername());
				//			存入数据库
							Myinfo info = new Myinfo();
							info.setType(Consts.MYINFO_PPROCESS);
							info.setUserId(userId);
							info.setCreated(new Date());
							info.setTitel(Consts.MYINFO_PPROCESS_TITEL);
							info.setContent("您好，截至本月账上收入已达"+chlist.get(i).getThreec()+"元，需按税法规定办理一般纳税人程序，超时限后期将无法抵扣进项税。如有疑问请联系您的会计");
							info.setFlag(Consts.MYINFO_UNFLAG);
							info.setMdate(flag);
							info.save();
						}else if(new BigDecimal(chlist.get(i).getThreec()).compareTo(st.getWprocess())==1 || new BigDecimal(chlist.get(i).getTwoc()).compareTo(st.getWprocess())==0 ){
							// 发生一条消息得到我的信息里
//							System.err.println("您的加工修理修配达到了红线提醒");
							SmsUtils.sendName("SMS_21420017",chlist.get(i).getThreec(), user.getUsername());
							Myinfo info = new Myinfo();
							info.setType(Consts.MYINFO_WPROCESS);
							info.setUserId(userId);
							info.setCreated(new Date());
							info.setTitel(Consts.MYINFO_WPROCESS_TITEL);
							info.setContent("您好，截至本月账上收入已达"+chlist.get(i).getThreec()+"元，请注意控制开票，否则税务将被强制认定为一般纳税人。如有疑问请联系您的会计。");
							info.setFlag(Consts.MYINFO_UNFLAG);
							info.setMdate(flag);
							info.save();
						}
					}
					//销售收入提醒
					else if(chlist.get(i).getSubName().equals("销售收入")){
						if(new BigDecimal(chlist.get(i).getThreec()).compareTo(st.getLstuff())==1 || new BigDecimal(chlist.get(i).getTwoc()).compareTo(st.getLprocess())==0 ){
							// 发生一条消息得到我的信息里
//							System.err.println("您的销售收入达到了上线设置");
							SmsUtils.sendName("SMS_21365078",chlist.get(i).getThreec(), user.getUsername());
							Myinfo info = new Myinfo();
							info.setType(Consts.MYINFO_PSTUFF);
							info.setUserId(userId);
							info.setCreated(new Date());
							info.setTitel(Consts.MYINFO_PSTUFF_TITEL);
							info.setContent("您好，截至本月账上收入已达"+chlist.get(i).getThreec()+"元，需按税法规定办理一般纳税人程序，超时限后期将无法抵扣进项税。如有疑问请联系您的会计");
							info.setFlag(Consts.MYINFO_UNFLAG);
							info.setMdate(flag);
							info.save();
							
						}else if(new BigDecimal(chlist.get(i).getThreec()).compareTo(st.getWstuff())==1 || new BigDecimal(chlist.get(i).getTwoc()).compareTo(st.getWprocess())==0 ){
							// 发生一条消息得到我的信息里
//							System.err.println("您的销售收入达到了红线提醒");
							SmsUtils.sendName("SMS_21375081",chlist.get(i).getThreec(), user.getUsername());
							
							Myinfo info = new Myinfo();
							info.setType(Consts.MYINFO_WSTUFF);
							info.setUserId(userId);
							info.setCreated(new Date());
							info.setTitel(Consts.MYINFO_WSTUFF_TITEL);
							info.setContent("您好，截至本月账上收入已达"+chlist.get(i).getThreec()+"元，请注意控制开票，否则税务将被强制认定为一般纳税人。如有疑问请联系您的会计。");
							info.setFlag(Consts.MYINFO_UNFLAG);
							info.setMdate(flag);
							info.save();
							
						}
					}
					else{
						if (StringUtils.isNotBlank(chlist.get(i).getThreec())) {
							sellAmount = sellAmount.add(new BigDecimal(chlist.get(i).getThreec()));
						}
					}
				}
				// 销售服务提醒
				if(sellAmount.compareTo(st.getLservice())==1 || sellAmount.compareTo(st.getLservice())==0){
					// 发生一条消息得到我的信息里
//					System.err.println("您的销售服务达到了上线设置");
					SmsUtils.sendName("SMS_21195190",sellAmount.toString(), user.getUsername());
					
					Myinfo info = new Myinfo();
					info.setType(Consts.MYINFO_PSERVICE);
					info.setUserId(userId);
					info.setCreated(new Date());
					info.setTitel(Consts.MYINFO_PSERVIC_TITELE);
					info.setContent("您好，截至本月账上收入已达"+sellAmount+"元，需按税法规定办理一般纳税人程序，超时限后期将无法抵扣进项税。如有疑问请联系您的会计");
					info.setFlag(Consts.MYINFO_UNFLAG);
					info.setMdate(flag);
					info.save();
				}
				else if(sellAmount.compareTo(st.getWservice())==1 || sellAmount.compareTo(st.getWservice())==0){
					// 发生一条消息得到我的信息里
//					System.err.println("您的销售服务达到了红线提醒");
					SmsUtils.sendName("SMS_21265153",sellAmount.toString(), user.getUsername());
					
					Myinfo info = new Myinfo();
					info.setType(Consts.MYINFO_WSERVICE);
					info.setUserId(userId);
					info.setCreated(new Date());
					info.setTitel(Consts.MYINFO_WSERVICE_TITEL);
					info.setContent("您好，截至本月账上收入已达"+sellAmount+"元，请注意控制开票，否则税务将被强制认定为一般纳税人。如有疑问请联系您的会计。");
					info.setFlag(Consts.MYINFO_UNFLAG);
					info.setMdate(flag);
					info.save();
				}
			}
			renderAjaxResultForSuccess("数据导入成功");
			
		} catch ( IOException e) {
			renderAjaxResultForError();
			e.printStackTrace();
		}
	}
}
