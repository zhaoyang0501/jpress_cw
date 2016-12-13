package io.jpress.jp.admin.controller;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Date;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import io.jpress.core.JBaseController;
import io.jpress.jp.Consts;
import io.jpress.model.Content;
import io.jpress.model.User;
import io.jpress.model.query.ContentQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.StringUtils;

@RouterMapping(url = "/admin/register", viewPath = "/templates/jp")
public class _RegisterController extends JBaseController {
	
	/**
	 * 工商注册
	 */
	public void enterprise_save() {
		final String mobile = getPara("mobile");
		
//		final String[] enterpriseName = getParaValues("enterpriseName");
		final String enterpriseName = getPara("enterpriseName");
		final String city = getPara("city");
		final String district = getPara("district");
		final String address = getPara("address");
		final String bscope = getPara("bscope");
	
		final Content cont = getModel(Content.class);
		System.err.println("########"+cont.getId());		
				Db.tx(new IAtom() {
				@Override
				public boolean run() throws SQLException {
					
					 if(cont.getId()==null){
						  User user = new User();
						  user.set("mobile", mobile);
						  user.save();
						  Content content = new Content();
							
							content.setUserId(user.getId());
							content.setModule(Consts.MODULE_ENTERPRISE);
							content.setStatus(Content.STATUS_NORMAL);
							content.setCreated(new Date());
							content.saveOrUpdate();
				
							
							content.saveOrUpdateMetadta("enterpriseName", enterpriseName);
							content.saveOrUpdateMetadta("city", city);
							content.saveOrUpdateMetadta("district", district);
							content.saveOrUpdateMetadta("address", address);
							content.saveOrUpdateMetadta("bscope", bscope);
				
							
							String[] stockholder = getParaValues("stockholder");
							String[] stockholderId = getParaValues("stockholderId");
							String[] stockRate = getParaValues("stockRate");
							String[] stockNum = getParaValues("stockNum");
							String[] flag=getParaValues("flag");
							
							for(int i=0;i<stockholder.length;i++){
								if(StringUtils.isNotBlank(stockholder[i])){
									Content stockContent = new Content();
									stockContent.setModule(Consts.MODULE_STOCK);
									stockContent.setStatus(Content.STATUS_NORMAL);
									stockContent.setObjectId(content.getId());						
									stockContent.setCreated(new Date());
									stockContent.saveOrUpdate();
									
									stockContent.saveOrUpdateMetadta("stockholder",stockholder[i]);
									stockContent.saveOrUpdateMetadta("stockholderId",stockholderId[i]);
									stockContent.saveOrUpdateMetadta("flag",flag[i]);
									stockContent.saveOrUpdateMetadta("stockRate",stockRate[i]);
									stockContent.saveOrUpdateMetadta("stockNum",stockNum[i]);
								}
							}
					  }else{
						  Content con=ContentQuery.me().findById(cont.getId());
						  con.saveOrUpdateMetadta("enterpriseName", enterpriseName);
						  con.saveOrUpdateMetadta("city", city);
						  con.saveOrUpdateMetadta("district", district);
						  con.saveOrUpdateMetadta("address", address);
						  con.saveOrUpdateMetadta("bscope", bscope);
						  
						  
						  BigInteger[] id =getParaValuesToBigInteger("stockId");
						  String[] stockholder = getParaValues("stockholder");
						  String[] stockholderId = getParaValues("stockholderId");
						  String[] stockRate = getParaValues("stockRate");
						  String[] stockNum = getParaValues("stockNum");
						  String[] flag=getParaValues("flag");
						  for(int i=0;i<stockholder.length;i++){
							if(StringUtils.isNotBlank(stockholder[i])){
								Content stockContent = ContentQuery.me().findById(id[i]);
								stockContent.setObjectId(con.getId());
								stockContent.setModule(Consts.MODULE_STOCK);
								stockContent.setStatus(Content.STATUS_NORMAL);
								stockContent.setModified(new Date());
								stockContent.saveOrUpdate();
								
								stockContent.saveOrUpdateMetadta("stockholder",stockholder[i]);
								stockContent.saveOrUpdateMetadta("stockholderId",stockholderId[i]);
								stockContent.saveOrUpdateMetadta("flag",flag[i]);
								stockContent.saveOrUpdateMetadta("stockRate",stockRate[i]);
								stockContent.saveOrUpdateMetadta("stockNum",stockNum[i]);
							}
						  }
					  }
		
					renderAjaxResultForSuccess();
					return true;
				}
			});
		  
	}

}
