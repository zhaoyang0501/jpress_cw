package io.jpress.jp.vo;

public class IncomeTax {
	private String seasonIncome;
	private String yearIncome;
	private String yearMajor;
	private String month;
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getSeasonIncome() {
		return seasonIncome;
	}
	public void setSeasonIncome(String seasonIncome) {
		this.seasonIncome = seasonIncome;
	}
	public String getYearIncome() {
		return yearIncome;
	}
	public void setYearIncome(String yearIncome) {
		this.yearIncome = yearIncome;
	}
	public String getYearMajor() {
		return yearMajor;
	}
	public void setYearMajor(String yearMajor) {
		this.yearMajor = yearMajor;
	}
	@Override
	public String toString() {
		return "{seasonIncome=" + seasonIncome + ", yearIncome="
				+ yearIncome + ", yearMajor=" + yearMajor + ", month=" + month + "}";
	}
	
	
	
}
