package io.jpress.jp.client.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.sun.mail.imap.protocol.ID;

import io.jpress.core.JBaseController;
import io.jpress.jp.Consts;
import io.jpress.jp.interceptor.UrlIntereptor;
import io.jpress.jp.interceptor.UserStatusInterceptor;
import io.jpress.jp.interceptor.WechatGetOpenIdInterceptor;
import io.jpress.jp.model.Balance;
import io.jpress.jp.model.CompanyContract;
import io.jpress.jp.model.CompanyInfo;
import io.jpress.jp.model.CompanyStock;
import io.jpress.jp.model.Contract;
import io.jpress.jp.model.EnterpriseChangeContent;
import io.jpress.jp.model.OrdersEnterprise;
import io.jpress.jp.model.PaymentRecords;
import io.jpress.jp.model.SetEnterpriseChange;
import io.jpress.jp.model.SetOrders;
import io.jpress.model.Content;
import io.jpress.model.User;
import io.jpress.model.query.ContentQuery;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.AttachmentUtils;
import io.jpress.utils.CookieUtils;
import io.jpress.utils.StringUtils;

@Before({UrlIntereptor.class,WechatGetOpenIdInterceptor.class,UserStatusInterceptor.class})
@RouterMapping(url = "/orders", viewPath = "/templates/jp/client")
public class OrderController extends JBaseController{
	/**
	 * 展现列表
	 */
	public void client_orders(){
		List<SetOrders> spList=SetOrders.DAO.findAllSetOrders();
		setAttr("spList", spList);
	}
	

	/**
	 * 排断下单是否需要添加详情信息
	 */
	public void client_orders_option(){
		keepPara();
		SetOrders sd =getModel(SetOrders.class);
		if ("1".equals(sd.getFlag())) {
			redirect("/orders/client_orders_detail?flag="+sd.getFlag()+"&setOrdersId="+sd.getId());
		}else if("2".equals(sd.getFlag())){
			redirect("/orders/client_enterprise_register?flag="+sd.getFlag()+"&setOrdersId="+sd.getId());
		}else{
			redirect("/orders/client_orders_contact?flag="+sd.getFlag()+"&setOrdersId="+sd.getId());
		}
	}
	
	/**
	 *  下单添加详情信息
	 */
	public void client_orders_detail(){
		String flag = getPara("flag");
		String[] title = getParaValues("title");
		String[] content = getParaValues("content");
		BigInteger setOrdersId = getParaToBigInteger("setOrdersId");
		//是否提交数据
		if(content==null){
			List<SetEnterpriseChange> secList=SetEnterpriseChange.DAO.findAllBySetOrdersId(setOrdersId);
			setAttr("secList", secList);
			setAttr("setOrdersId", setOrdersId);
			setAttr("flag", flag);
		}else{
			List<EnterpriseChangeContent> etpList = new ArrayList<EnterpriseChangeContent>();
			for(int i=0;i<content.length;i++){
				EnterpriseChangeContent etpcct=new EnterpriseChangeContent();
				etpcct.setContent(content[i]);
				etpcct.setTitle(title[i]);
				etpList.add(etpcct);
			}
			setSessionAttr("etpList", etpList);
			redirect("/orders/client_orders_contact?flag="+flag+"&setOrdersId="+setOrdersId);
		}
		
	}
	
	/**
	 * 工商注册
	 */
	public void client_enterprise_register() {
		if(!isMultipartRequest()){
			String flag = getPara("flag");
			BigInteger setOrdersId = getParaToBigInteger("setOrdersId");
		  setAttr("flag", flag);
		  setAttr("setOrdersId", setOrdersId);
		}else{
//			List<String> pic= new ArrayList<String>();
//			List<UploadFile> file =getFiles();
			
			String house = AttachmentUtils.moveFile(getFile("house"));
			String identity = AttachmentUtils.moveFile(getFile("identity"));
			String property = AttachmentUtils.moveFile(getFile("property"));
			//上次合同是可选项
//			if (StringUtils.isBlank(house)) {
//				renderAjaxResultForError("请上传上传房屋合同图片");
//				return;
//			}
//			if (StringUtils.isBlank(identity)) {
//				renderAjaxResultForError("请上传上传省份证图片");
//				return;
//			}
//			if (StringUtils.isBlank(property)) {
//				renderAjaxResultForError("请上传上产权证图片");
//				return;
//			}
			setSessionAttr("house", house);
			setSessionAttr("identity", identity);
			setSessionAttr("property", property);
			

			String flag = getPara("flag");
			BigInteger setOrdersId = getParaToBigInteger("setOrdersId");
			String[] tempetn = getParaValues("enterpriseName");
			if(tempetn==null||tempetn.length==0||"".equals(tempetn[0])){
				renderAjaxResultForError("公司名为空");
				return;
			}
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < tempetn.length; i++) {
				sb.append(tempetn[i]).append(",");
			}
		String enterpriseName=sb.toString().substring(0, sb.length()-1);
		
		 String city = getPara("city");
		 String district = getPara("district");
		 String address = getPara("address");
			if(StringUtils.isBlank(district)){
				renderAjaxResultForError("地区名为空");
				return;
			}
			if(StringUtils.isBlank(city)){
				renderAjaxResultForError("城市名为空");
				return;
			}
			if(StringUtils.isBlank(address)){
				renderAjaxResultForError("详情地址为空");
				return;
			}

		 String bscope = getPara("bscope"); //经验范围

		 String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);

			  	CompanyInfo companyInfo = new CompanyInfo();
			  	companyInfo.setEnterpriseName(enterpriseName);
			  	companyInfo.setCity(city);
			  	companyInfo.setDistrict(district);
			  	companyInfo.setAddress(address);
			  	companyInfo.setBscope(bscope);
			  	setSessionAttr("companyInfo", companyInfo);
					
				String[] stockholder = getParaValues("stockholder");
				String[] stockholderId = getParaValues("stockholderId");
				String[] stockRate = getParaValues("stockRate");
				String[] stockNum = getParaValues("stockNum");
				String[] status=getParaValues("status");
				List<CompanyStock> cs = new ArrayList<CompanyStock>();
				for(int i=0;i<stockholder.length;i++){
					if(StringUtils.isNotBlank(stockholder[i])){
						CompanyStock companyStock = new CompanyStock();
						
						companyStock.setStockholder(stockholder[i]);
						companyStock.setStockholderId(stockholderId[i]);
						companyStock.setStatus(status[i]);
						companyStock.setStockRate(stockRate[i]);
						companyStock.setStockNum(stockNum[i]);
						
						cs.add(companyStock);
					}
				}
				setSessionAttr("cs",cs);
				
//				String contracter = getPara("contracter");
//				String mobile = getPara("moblie");
//				String qq = getPara("qq");
//				CompanyContract cc = new CompanyContract();
//				cc.setContracter(contracter);
//				cc.setMobile(mobile);
//				cc.setQq(qq);
//				setSessionAttr("cc", cc);
//				redirect("/orders/client_orders_contact?flag="+flag+"&setOrdersId="+setOrdersId);
				renderAjaxResultForSuccess();
			
		  }	
	}
	
	/**
	 * 下单联系人
	 */
	public void client_orders_contact(){
		final String flag = getPara("flag");
//		String openId = "c3b36939-9814-4304-ba50-73401e997add";
		String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
		final BigInteger setOrdersId= getParaToBigInteger("setOrdersId"); 
		final User user = UserQuery.me().findFirstFromMetadata("openId", openId);
		if(user==null){return;}
		final OrdersEnterprise contacts = getModel(OrdersEnterprise.class);
		if(contacts==null){return;}
		 if (StringUtils.isNotBlank(contacts.getContactName())) {
			Db.tx(new IAtom() {
				
				@Override
				public boolean run() throws SQLException {
				
					if(user==null){
						renderAjaxResultForError("提交信息有误");
						return false;
					}
					
					OrdersEnterprise oe = new OrdersEnterprise();
					oe.setUserId(user.getId());
					
					oe.setStatus(Consts.ORDERS_STATUS_CONTACTED);
					
					oe.setContactName(contacts.getContactName());
					oe.setContent(contacts.getContent());
					oe.setEmail(contacts.getEmail());
					oe.setLinkTool(contacts.getLinkTool());
					oe.setMobile(contacts.getMobile());
					oe.setSetOrdersId(setOrdersId);
					oe.setCreated(new Date());
					oe.saveOrUpdate();
					
					if(flag.equals("1")){
						List<EnterpriseChangeContent> etpList = getSessionAttr("etpList");
						if(etpList==null){
							renderAjaxResultForError("请重新提交信息");
							return false;
						}
						for (EnterpriseChangeContent etpcc : etpList) {
							EnterpriseChangeContent etp = new EnterpriseChangeContent();
							etp.setContent(etpcc.getContent());
							etp.setOrdersEnterpriseId(oe.getId());
							etp.setTitle(etpcc.getTitle());
							etp.setCreated(new Date());
							etp.save();
						}
						removeSessionAttr("etpList");
					}else if(flag.equals("2")){
						
						String house=getSessionAttr("house");
						String identity=getSessionAttr("identity");
						String property=getSessionAttr("property");
						
						CompanyInfo tcf =getSessionAttr("companyInfo");
						CompanyInfo cf =new CompanyInfo();
						cf.setOrdersEnterpriseId(oe.getId());
						cf.setAddress(tcf.getAddress());
						cf.setBscope(tcf.getBscope());
						cf.setCity(tcf.getCity());
						cf.setDistrict(tcf.getDistrict());
						cf.setEnterpriseName(tcf.getEnterpriseName());
						
						if(StringUtils.isNotBlank(identity)){
							cf.setPic1(identity);
						}
						if(StringUtils.isNotBlank(house)){
							cf.setPic2(house);
						}
						if(StringUtils.isNotBlank(property)){
							cf.setPic3(property);
						}
						
						cf.saveOrUpdate();
						List<CompanyStock> cslist = getSessionAttr("cs");
						for(CompanyStock cs:cslist){
							CompanyStock cps = new CompanyStock();
							cps.setStatus(cs.getStatus());
							cps.setStockholder(cs.getStockholder());
							cps.setStockholderId(cs.getStockholderId());
							cps.setStockNum(cs.getStockNum());
							cps.setStockRate(cs.getStockRate());
							cps.setCompanyInofId(cf.getId());
							cps.saveOrUpdate();
						}
					}
					renderAjaxResultForSuccess();
					return true;
				}
			});
		}
		 setAttr("flag", flag);
		 setAttr("setOrdersId", setOrdersId);
		
	}
	
	/**
	 * 充值详情
	 */
	public void client_recharge(){
		
//		String openId = "c3b36939-9814-4304-ba50-73401e997add"; 
		String openId = CookieUtils.get(this, Consts.ATTR_WECHAT_OPENID);
		final User user = UserQuery.me().findFirstFromMetadata("openId", openId);
		if(user==null){return;}
//		String ordersEnterpriseStatus = getPara("ordersEnterpriseStatus"); //单子状态
//		String contractStatus = getPara("ordersEnterpriseStatus");//合同状态
		
		final String tempAmount =  getPara("amount");
		final String tempShouldPay =  getPara("shouldPay");
		final BigInteger setOrdersId = getParaToBigInteger("setOrdersId");
		final BigInteger ordersEnterpriseId = getParaToBigInteger("ordersEnterpriseId");
		final String remark = getPara("remark");
		
		if(StringUtils.isBlank(tempAmount)){//进入合同页
			OrdersEnterprise oe = OrdersEnterprise.DAO.findOrdersEnterpriseByStatus(user.getId(),Consts.ORDERS_STATUS_CONTACTED);//判断是否写合同
			if(oe==null){return;}
			Contract contract = Contract.DAO.findContractByOIdwithSId(oe.getId());
			if(contract!=null){
				BigDecimal integral=contract.getContractAmount().multiply(new BigDecimal("0.1"));
				Balance balance = Balance.DAO.findBalanceByUserId(user.getId());
				
				setAttr("contract", contract);
				setAttr("integral", integral);
				setAttr("balance", balance);
				
			}else{
				List<SetOrders> setOrders = SetOrders.DAO.findAllSetOrders();
				setAttr("setOrders", setOrders);
			}
			
		}else{
			Db.tx(new IAtom() {
				
				@Override
				public boolean run() throws SQLException {
					BigDecimal amount = new BigDecimal(tempAmount);
					
					
					if(ordersEnterpriseId==null){
						
						PaymentRecords pay = new PaymentRecords();
						pay.setAmount(amount);
						pay.setCreated(new Date());
						pay.setSetOrdersId(setOrdersId);
						pay.setRemark(remark);
						pay.setUserId(user.getId());
						pay.saveOrUpdate();
						
					}else{
						
						PaymentRecords pay = new PaymentRecords();
						pay.setAmount(amount);
						pay.setCreated(new Date());
//						pay.setSetOrdersId(setOrdersId);
						pay.setOrdersEnterpriseId(ordersEnterpriseId);
						pay.setUserId(user.getId());
						pay.setRemark(remark);
						pay.saveOrUpdate();
						// 单子状态
						
						OrdersEnterprise oet = OrdersEnterprise.DAO.findById(ordersEnterpriseId);
						oet.setStatus(Consts.ORDERS_STATUS_PAY);
						oet.saveOrUpdate();
						//合同状态
						
						Contract contract = Contract.DAO.findContractByOrdersEnterpriseId(ordersEnterpriseId);
						contract.setStatus(Consts.CONTRACT_STATUS_DOING);
						contract.saveOrUpdate();
					}
					
					//已缴费 余额
					Balance balance =Balance.DAO.findBalanceByUserId(user.getId());
					BigDecimal bamount = balance.getAmount().add(amount);
					
					if(StringUtils.isNotBlank(tempShouldPay)){
						BigDecimal shouldPay = new BigDecimal(tempShouldPay);
						bamount=bamount.subtract(shouldPay);
					}
					
					balance.setAmount(bamount);
					balance.saveOrUpdate();
					renderAjaxResultForSuccess();
					return true;
				}
			});
			
			
			
			
		}
		// 如果有合同展现合同， 如果没有展现类型和提交钱
		
		// 后台状态有点问题， 第一个是 新单子可以  提交 合同 ， 提交合同 status变为1 ，     
		// status 为1是，可以展现 和 修改 合同内容
		
		// 用户在前太提交 钱 不让交 
	}
	public void client_success(){}
	public void client_fail(){}
	
	/**
	 * 合同列表
	 */
	public void client_contractList(){
		BigInteger userId = getParaToBigInteger("userId");
		Page<Contract> page = Contract.DAO.findByOrderEnterpirseUserId(getPageNumber(), getPageSize(),userId);
		setAttr("page", page);
	}
	
	/**
	 * 支付列表
	 */
	public void client_payInfor(){
		BigInteger userId = getParaToBigInteger("userId");
		Page<PaymentRecords> page = PaymentRecords.DAO.findByPayRecordUserId(getPageNumber(), getPageSize(),userId);
		setAttr("page", page);
	}
}
