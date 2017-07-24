package com.payment.simaspay.PojoClasses;

public class TransationsList {

	String refID;
	String nominalAmount;
	String transactionTime;
	String token;

	public String getIsCredit() {
		return isCredit;
	}

	public void setIsCredit(String isCredit) {
		this.isCredit = isCredit;
	}

	String isCredit;

	public String getRefID() {
		return refID;
	}

	public void setRefID(String refID) {
		this.refID = refID;
	}

	public String getNominalAmount() {
		return nominalAmount;
	}

	public void setNominalAmount(String nominalAmount) {
		this.nominalAmount = nominalAmount;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
