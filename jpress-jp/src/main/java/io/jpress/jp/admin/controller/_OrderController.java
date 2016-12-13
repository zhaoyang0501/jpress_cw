package io.jpress.jp.admin.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

import io.jpress.core.JBaseController;
import io.jpress.jp.Consts;
import io.jpress.jp.interceptor.JmodfyOrderStatusContractInterceptor;
import io.jpress.jp.model.Balance;
import io.jpress.jp.model.CompanyInfo;
import io.jpress.jp.model.CompanyStock;
import io.jpress.jp.model.Contract;
import io.jpress.jp.model.EnterpriseChangeContent;
import io.jpress.jp.model.Myinfo;
import io.jpress.jp.model.OrdersEnterprise;
import io.jpress.jp.model.PaymentRecords;
import io.jpress.jp.model.SetOrders;
import io.jpress.jp.utils.SmsUtils;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.StringUtils;
import io.jpress.wechat.processer.NewestContentsProcesser;

@RouterMapping(url="/admin/orders", viewPath="/templates/jp")
public class _OrderController extends JBaseController {
	/**
	 * 单子列表
	 */
	public void orders_list(){
		String status = getPara("status");
		String delock =getPara("delock");
		Page<OrdersEnterprise> page ;
		if(StringUtils.isNotBlank(delock)){
			 page = OrdersEnterprise.DAO.paginate(getPageNumber(), getPageSize(),status,delock);
		}else{
			 page = OrdersEnterprise.DAO.paginate(getPageNumber(), getPageSize(),status,null);
		}
		setAttr("page", page);
		setAttr("status", status);
		setAttr("delock", delock);
	}
	
	
	
	 /**
	  * 自行付费单子
	  */
	public void orders_vol_list(){
		String delock =getPara("delock");
		Page<PaymentRecords> page;
		if(StringUtils.isNotBlank(delock)){
			 page = PaymentRecords.DAO.paginate(getPageNumber(), getPageSize(),delock);
		}else{
			 page = PaymentRecords.DAO.paginate(getPageNumber(), getPageSize(),null);
		}
		
		setAttr("page", page);
		setAttr("delock", delock);
	}
	
//	public void orders_contacted(){
//		Page<OrdersEnterprise> page = OrdersEnterprise.DAO.paginate(getPageNumber(), getPageSize(),Consts.ORDERS_STATUS_BOOK);
//		setAttr("page", page);
//	}
//	
//	public void orders_pay(){
//		Page<OrdersEnterprise> page = OrdersEnterprise.DAO.paginate(getPageNumber(), getPageSize(),Consts.ORDERS_STATUS_BOOK);
//		setAttr("page", page);
//	}
	
//	public void orders_list_new_detail(){
//		BigInteger id = getParaToBigInteger("id");
//		
//		OrdersEnterprise ordersDetail = OrdersEnterprise.DAO.findDetailById(id);
//		setAttr("ordersDetail", ordersDetail);
//		
//		List<EnterpriseChangeContent> ecList = EnterpriseChangeContent.DAO.findListByOrdersEnterprise(id);
//		setAttr("ecList", ecList);
//		
//		System.err.println("newecList--->"+ecList);
//	}
	/**
	 * 新单子详情
	 */
	public void orders_list_contacted_detail(){
		BigInteger id = getParaToBigInteger("id");
		
		OrdersEnterprise ordersDetail = OrdersEnterprise.DAO.findDetailById(id);
		if(ordersDetail==null){
			return;
		}
		setAttr("ordersDetail", ordersDetail);
		SetOrders setOrders = SetOrders.DAO.findById(ordersDetail.getSetOrdersId());
		if(setOrders==null){
			return;
		}
		if(setOrders.getFlag().equals("1")){
		List<EnterpriseChangeContent> ecList = EnterpriseChangeContent.DAO.findListByOrdersEnterprise(id);
		setAttr("ecList", ecList);
		}else if(setOrders.getFlag().equals("2")){
			//List<CompanyInfo> cpi = CompanyInfo.DAO.findByCompanyInfos(id);
			CompanyInfo cpi = CompanyInfo.DAO.findByCompanyInfos(id);
			List<CompanyStock> stocks = CompanyStock.DAO.findByCompanyInfoId(cpi.getId());
			setAttr("cpi", cpi);
			setAttr("stocks", stocks);
		}
	}
	/**
	 * 付费子详情
	 */
	public void orders_list_pay_detail(){
		BigInteger id = getParaToBigInteger("id");
		
		OrdersEnterprise ordersDetail = OrdersEnterprise.DAO.findDetailById(id);
		setAttr("ordersDetail", ordersDetail);
		
		List<EnterpriseChangeContent> ecList = EnterpriseChangeContent.DAO.findListByOrdersEnterprise(id);
		setAttr("ecList", ecList);
	}
	
	public void orders_status_new(){
		BigInteger id = getParaToBigInteger("id");
		String status = getPara("status");
		String flag = getPara("flag");
		if(StringUtils.isNotBlank(flag)){
			OrdersEnterprise orderStatus = OrdersEnterprise.DAO.findById(id);
			orderStatus.setStatus(status);
			orderStatus.saveOrUpdate();
			renderAjaxResultForSuccess();
		}
		setAttr("status", status);
		setAttr("id", id);
	}
	/**
	 * 拟定合同
	 */
	@Before(JmodfyOrderStatusContractInterceptor.class)
	public void orders_status_contacted(){
		final BigInteger ordersEnterpriseId = getParaToBigInteger("id");
		String status = getPara("status");
		final BigInteger userId = getParaToBigInteger("userId");
		String flag = getPara("flag");//标识符
//		final User user = UserQuery.me().findById(userId);
		if(StringUtils.isNotBlank(flag)){
			Db.tx(new IAtom() {
				@Override
				public boolean run() throws SQLException {
					Contract contract = getModel(Contract.class);
					Contract cont = Contract.DAO.findContractByOrdersEnterpriseId(contract.getOrdersEnterpriseId()); 
					if(cont==null){
						Balance ba = Balance.DAO.findBalanceByUserId(userId);
						if(ba==null){
							Balance newba = new Balance();
							newba.setUserId(userId);
							newba.setAmount(new BigDecimal("0"));
							newba.setCreated(new Date());
							newba.saveOrUpdate();
							
							Contract newcont = new Contract();
							newcont.setOrdersEnterpriseId(contract.getOrdersEnterpriseId());
							newcont.setBalanceId(newba.getId());
							newcont.setContractAmount(contract.getContractAmount());
							newcont.setContractDate(contract.getContractDate());
							newcont.setContractDeadline(contract.getContractDeadline());
							newcont.setMonthBalance(contract.getMonthBalance());
							newcont.setContractDate(contract.getContractDate());
							newcont.setShouldPay(new BigDecimal(contract.getShouldPay().toString()));
							newcont.setMonthShouldPay(contract.getMonthShouldPay());
							newcont.setRemark(contract.getRemark());
							newcont.setPayStatus(contract.getPayStatus());
							newcont.setStatus(Consts.CONTRACT_STATUS_START);
							newcont.setCreated(new Date());
							newcont.saveOrUpdate();
							
							Myinfo myinfo = new Myinfo();
							myinfo.setUserId(userId);
							myinfo.setType(Consts.PUSH_CONTRACT);
							myinfo.setTitel(Consts.PUSH_CONTRACT);
							myinfo.setContent("您的合同已经拟定，请到事务代办缴费！");
							myinfo.setCreated(new Date());
							myinfo.saveOrUpdate();
//							SmsUtils.sendName("SMS_21435003",null, user.getUsername());
							
						}else{
							BigInteger baId=ba.getId();
							Contract newcont = new Contract();
							newcont.setOrdersEnterpriseId(contract.getOrdersEnterpriseId());
							newcont.setBalanceId(baId);
							newcont.setContractAmount(contract.getContractAmount());
							newcont.setContractDate(contract.getContractDate());
							newcont.setContractDeadline(contract.getContractDeadline());
							newcont.setMonthBalance(contract.getMonthBalance());
							newcont.setContractDate(contract.getContractDate());
							newcont.setShouldPay(new BigDecimal(contract.getShouldPay().toString()));
							newcont.setMonthShouldPay(contract.getMonthShouldPay());
							newcont.setRemark(contract.getRemark());
							newcont.setPayStatus(contract.getPayStatus());
							newcont.setStatus(Consts.CONTRACT_STATUS_START);
							newcont.setCreated(new Date());
							newcont.saveOrUpdate();
							
							Myinfo myinfo = new Myinfo();
							myinfo.setUserId(userId);
							myinfo.setType(Consts.PUSH_CONTRACT);
							myinfo.setTitel(Consts.PUSH_CONTRACT);
							myinfo.setContent("您的合同已经拟定，请到事务代办缴费！");
							myinfo.setCreated(new Date());
							myinfo.saveOrUpdate();
						}
						
					}else{
						
						cont.setOrdersEnterpriseId(contract.getOrdersEnterpriseId());
						cont.setContractAmount(contract.getContractAmount());
						cont.setContractDate(contract.getContractDate());
						cont.setContractDeadline(contract.getContractDeadline());
						cont.setMonthBalance(contract.getMonthBalance());
						cont.setContractDate(contract.getContractDate());
						cont.setShouldPay(new BigDecimal(contract.getShouldPay().toString()));
						cont.setMonthShouldPay(contract.getMonthShouldPay());
						cont.setRemark(contract.getRemark());
						cont.setPayStatus(contract.getPayStatus());
						cont.saveOrUpdate();
						
						Myinfo myinfo = new Myinfo();
						myinfo.setUserId(userId);
						myinfo.setType(Consts.CHANGE_CONTRACT);
						myinfo.setTitel(Consts.CHANGE_CONTRACT);
						myinfo.setContent("您的合同已更改，请到事务代办缴费！");
						myinfo.setCreated(new Date());
						myinfo.saveOrUpdate();
						
//						SmsUtils.sendName("SMS_21425006",c.getId().toString(), user.getUsername());
					}
					
					renderAjaxResultForSuccess();
					
					return true;
				}
			});
			
		}
		Contract contract = Contract.DAO.findContractByOrdersEnterpriseId(ordersEnterpriseId); 
		
		Balance balance = Balance.DAO.findBalanceByUserId(userId);
		if(balance==null){
			Balance ba = new Balance();
			ba.setUserId(userId);
			ba.setAmount(new BigDecimal("0"));
			ba.setCreated(new Date());
			ba.saveOrUpdate();
			setAttr("balance", ba);
		}else{
			setAttr("balance", balance);
		}
		
		setAttr("contract", contract);
		setAttr("status", status);
		setAttr("id", ordersEnterpriseId);
		
	}
	/**
	 * 付费状态 合同查看
	 */
	public void orders_status_pay(){
		BigInteger ordersEnterpriseId = getParaToBigInteger("id");
		String status = getPara("status");
		final BigInteger userId = getParaToBigInteger("userId");
		
		
		Contract contract = Contract.DAO.findContractByOrdersEnterpriseId(ordersEnterpriseId);
		
		PaymentRecords pr =PaymentRecords.DAO.findPaymentRecordsByOrdersEnterpriseId(ordersEnterpriseId);
		
		Balance balance = Balance.DAO.findBalanceByUserId(userId);
		
		setAttr("pr", pr);
		
		setAttr("balance", balance);
		setAttr("contract", contract);
		setAttr("status", status);
		setAttr("id", ordersEnterpriseId);
	}
	
}
