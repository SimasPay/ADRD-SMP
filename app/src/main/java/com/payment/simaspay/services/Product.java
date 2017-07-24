package com.payment.simaspay.services;

public class Product {

	String name;
	String code;
	String denom;
	String offline;
	String denomCode,netPrice;


	public String getOffline() {
		return offline;
	}

	public void setOffline(String offline) {
		this.offline = offline;
	}

	public Product(String name,String code,String denom,String offline,String denomCode,String netPrice) {
		this.name = name;
		this.code = code;
		this.denom = denom;
		this.offline = offline;
		this.denomCode=denomCode;
		this.netPrice=netPrice;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return this.code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDenom() {
		return this.denom;
	}
	public void setDenom(String denom) {
		this.denom = denom;
	}
	public String getDenomCode() {
		return denomCode;
	}

	public String getNetPrice() {
		return netPrice;
	}

	@Override
	public String toString() {
		return this.name + "-" +this.code +"-"+this.denom;
	}


}
