package io.jpress.jp.model;


public class StockholderInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String stockholder;
	private String stockholderId;
	private String flag;
	private String stockRate;
	private String stockNum;
	public String getStockholder() {
		return stockholder;
	}
	public void setStockholder(String stockholder) {
		this.stockholder = stockholder;
	}
	public String getStockholderId() {
		return stockholderId;
	}
	public void setStockholderId(String stockholderId) {
		this.stockholderId = stockholderId;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getStockRate() {
		return stockRate;
	}
	public void setStockRate(String stockRate) {
		this.stockRate = stockRate;
	}
	public String getStockNum() {
		return stockNum;
	}
	public void setStockNum(String stockNum) {
		this.stockNum = stockNum;
	}
	@Override
	public String toString() {
		return "StockholderInfo [stockholder=" + stockholder
				+ ", stockholderId=" + stockholderId + ", flag=" + flag
				+ ", stockRate=" + stockRate + ", stockNum=" + stockNum + "]";
	}
	
	
}
