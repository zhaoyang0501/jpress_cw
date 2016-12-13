package io.jpress.jp.vo;

import io.jpress.utils.StringUtils;

import java.math.BigDecimal;

public class DetailTax {
	
	private String month;
	private String shouru="0";
	private String output="0";
	private String inputTax="0";
	private String reliefs="0";
	private String citytax="0";
	private String eduTax="0";
	private String localEduTax="0";
	
	private String incomeTax="0";
	private String stampTax="0";
	
	
	public String getLocalEduTax() {
		return localEduTax;
	}

	public void setLocalEduTax(String localEduTax) {
		this.localEduTax = localEduTax;
	}

	@Override
	public String toString() {
		return "{month=" + month + ", shouru=" + shouru
				+ ", output=" + output + ", inputTax=" + inputTax
				+ ", reliefs=" + reliefs + ", citytax=" + citytax + ", eduTax="
				+ eduTax + ", incomeTax=" + incomeTax + ", StampTax="
				+ stampTax + "}";
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getShouru() {
		return shouru;
	}

	public void setShouru(String shouru) {
		this.shouru = shouru;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getInputTax() {
		return inputTax;
	}

	public void setInputTax(String inputTax) {
		this.inputTax = inputTax;
	}

	public String getReliefs() {
		return reliefs;
	}

	public void setReliefs(String reliefs) {
		this.reliefs = reliefs;
	}

	public String getCitytax() {
		return citytax;
	}

	public void setCitytax(String citytax) {
		this.citytax = citytax;
	}

	public String getEduTax() {
		return eduTax;
	}

	public void setEduTax(String eduTax) {
		this.eduTax = eduTax;
	}

	public String getIncomeTax() {
		return incomeTax;
	}

	public void setIncomeTax(String incomeTax) {
		this.incomeTax = incomeTax;
	}

	public String getStampTax() {
		return stampTax;
	}

	public void setStampTax(String stampTax) {
		this.stampTax = stampTax;
	}
	
	public String getVat(){
		BigDecimal vat = new BigDecimal("0");
		BigDecimal outputV = new BigDecimal("0");
		BigDecimal inputTaxV = new BigDecimal("0");
		BigDecimal reliefsV = new BigDecimal("0");
		if (StringUtils.isNotBlank(output)) {
			outputV=	outputV.add(new BigDecimal(output));
		}
		if (StringUtils.isNotBlank(inputTax)) {
			inputTaxV=inputTaxV.add(new BigDecimal(inputTax));
		}
		if (StringUtils.isNotBlank(reliefs)) {
			reliefsV=reliefsV.add(new BigDecimal(reliefs));
		}
//		Float vat=Float.parseFloat(output)-Float.parseFloat(inputTax)-Float.parseFloat(reliefs);
		 vat = vat.add(outputV.subtract(inputTaxV).subtract(reliefsV));
		System.err.println("vat-->"+vat);
		return vat.toString();
	}

	
	public String getSubjoin(){
		BigDecimal subjoin = new BigDecimal("0");
		BigDecimal citytaxV = new BigDecimal("0");
		BigDecimal eduTaxV = new BigDecimal("0");
		BigDecimal localEduTaxV = new BigDecimal("0");
		
		if (StringUtils.isNotBlank(citytax)) {
			citytaxV=citytaxV.add(new BigDecimal(citytax));
		}
		if (StringUtils.isNotBlank(eduTax)) {
			eduTaxV=eduTaxV.add(new BigDecimal(eduTax));
		}
		if (StringUtils.isNotBlank(localEduTax)) {
			localEduTaxV=localEduTaxV.add(new BigDecimal(localEduTax));
		}
		
		subjoin = subjoin.add(citytaxV.add(eduTaxV).add(localEduTaxV));
			System.err.println("subjoin-->"+subjoin);
		return subjoin.toString();
	}
	

}
