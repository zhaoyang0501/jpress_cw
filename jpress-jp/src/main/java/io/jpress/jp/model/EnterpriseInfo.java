package io.jpress.jp.model;

//用到缓存需要序列化
public class EnterpriseInfo implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String city;
	private String district;
	private String address;
	private String bscope;
	@Override
	public String toString() {
		return "EnterpriseInfo [city=" + city + ", district=" + district
				+ ", address=" + address + ", bscope=" + bscope
				+ ", getCity()=" + getCity() + ", getDistrict()="
				+ getDistrict() + ", getAddress()=" + getAddress()
				+ ", getBscope()=" + getBscope() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBscope() {
		return bscope;
	}
	public void setBscope(String bscope) {
		this.bscope = bscope;
	}

}
