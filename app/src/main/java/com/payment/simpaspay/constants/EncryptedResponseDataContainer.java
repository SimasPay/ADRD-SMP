package com.payment.simpaspay.constants;

import java.util.Vector;

/**
 * Response container
 * 
 * @author pramod
 */
public class EncryptedResponseDataContainer {

	private String errorMsg;
	private Vector messagesList;
	private String msg;
	private String transactionTime;
	private String encryptedRefId;
	private String encryptedTransferId;
	private String encryptedParentTxnId;
	private String msgCode;
	private String responseData;
	private String encryptedAeskey;
	private String AuthenticationString;
	private String Salt;
	private String encryptedTransactionCharges;
	private String encryptedDebitAmount;
	private String encryptedCreditAmount;
	private String appUpdateURL;
	private String registrationMedium;
	private String aditionalInfo;
	private String custName;
	private String accountNumber;
	private String amount;
	private String destBank;
	private String destMDN;
	private String resetPinRequested;
	private String status;
	private String sctl;
	private String mfaMode;
	private String isActivated;
	private String publicKeyModulus;
	private String publicKeyExponet;
	private String success;
	private String idNumber;
	private String isUnRegistered;
	private String nfcCardAliasName;
	private String downLoadUrl;
	private String kycName;
	private String firstName;
	private String lastName;
	private String responseCode;
	private String nickName;
	private String kycStatus;
	private String subScriberStatus;
	private String promoImageUrl;
	private String userApiKey;
	private String NonKycAlert;
	private String billDate;
	private String invoiceNo;
	private String nominalAmount;
	private String responseMsg;
	private String profileId;
	private String currentBalance;
	private String name;
	private String sourceMDN;
	private String sqexist;
	private String remainbalance;
	private String maxbalance;

	private String securityQuestion;
	private String resetPinByEmail;
	private String resetPinByCSR;
	private String emailVerified;
	private String kycLevel;

	int totalRecords;

	boolean MoreRecordsAvailable;

	String bankId;
	private String profileImage;
	private String dateOfBirth;
	private String sessionTime;

	private String maxTrails;

	private String msisdn;

	private String vipMemberID;

	public String getVipMemberID() {
		return vipMemberID;
	}

	public void setVipMemberID(String vipMemberID) {
		this.vipMemberID = vipMemberID;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getMaxTrails() {
		return maxTrails;
	}

	public void setMaxTrails(String maxTrails) {
		this.maxTrails = maxTrails;
	}

	public String getSessionTime() {
		return sessionTime;
	}

	public void setSessionTime(String sessionTime) {
		this.sessionTime = sessionTime;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public boolean isMoreRecordsAvailable() {
		return MoreRecordsAvailable;
	}

	public void setMoreRecordsAvailable(boolean moreRecordsAvailable) {
		MoreRecordsAvailable = moreRecordsAvailable;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public String getKycLevel() {
		return kycLevel;
	}

	public void setKycLevel(String kycLevel) {
		this.kycLevel = kycLevel;
	}

	public String getSecurityQuestion() {
		return securityQuestion;
	}

	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}

	public String getResetPinByEmail() {
		return resetPinByEmail;
	}

	public void setResetPinByEmail(String resetPinByEmail) {
		this.resetPinByEmail = resetPinByEmail;
	}

	public String getResetPinByCSR() {
		return resetPinByCSR;
	}

	public void setResetPinByCSR(String resetPinByCSR) {
		this.resetPinByCSR = resetPinByCSR;
	}

	public String getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(String emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getRemainbalance() {
		return remainbalance;
	}

	public void setRemainbalance(String remainbalance) {
		this.remainbalance = remainbalance;
	}

	public String getMaxbalance() {
		return maxbalance;
	}

	public void setMaxbalance(String maxbalance) {
		this.maxbalance = maxbalance;
	}

	public String getSqexist() {
		return sqexist;
	}

	public void setSqexist(String sqexist) {
		this.sqexist = sqexist;
	}

	public String getSourceMDN() {
		return sourceMDN;
	}

	public void setSourceMDN(String sourceMDN) {
		this.sourceMDN = sourceMDN;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	private String referenceNo;
	private String errorCode;

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getNominalAmount() {
		return nominalAmount;
	}

	public void setNominalAmount(String nominalAmount) {
		this.nominalAmount = nominalAmount;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getNonKycAlert() {
		return NonKycAlert;
	}

	public void setNonKycAlert(String nonKycAlert) {
		NonKycAlert = nonKycAlert;
	}

	public String getUserApiKey() {
		return userApiKey;
	}

	public void setUserApiKey(String userApiKey) {
		this.userApiKey = userApiKey;
	}

	public String getPromoImageUrl() {
		return promoImageUrl;
	}

	public void setPromoImageUrl(String promoImageUrl) {
		this.promoImageUrl = promoImageUrl;
	}

	public String getSubScriberStatus() {
		return subScriberStatus;
	}

	public void setSubScriberStatus(String subScriberStatus) {
		this.subScriberStatus = subScriberStatus;
	}

	public String getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}

	public String getNonKYCAllow() {
		return nonKYCAllow;
	}

	public void setNonKYCAllow(String nonKYCAllow) {
		this.nonKYCAllow = nonKYCAllow;
	}

	private String nonKYCAllow;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getKycName() {
		return kycName;
	}

	public void setKycName(String kycName) {
		this.kycName = kycName;
	}

	public String getDownLoadUrl() {
		return downLoadUrl;
	}

	public void setDownLoadUrl(String downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}

	public String getNfcCardAliasName() {
		return nfcCardAliasName;
	}

	public void setNfcCardAliasName(String nfcCardAliasName) {
		this.nfcCardAliasName = nfcCardAliasName;
	}

	public String getIsUnRegistered() {
		return isUnRegistered;
	}

	public void setIsUnRegistered(String isUnRegistered) {
		this.isUnRegistered = isUnRegistered;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getPublicKeyModulus() {
		return publicKeyModulus;
	}

	public void setPublicKeyModulus(String publicKeyModulus) {
		this.publicKeyModulus = publicKeyModulus;
	}

	public String getPublicKeyExponet() {
		return publicKeyExponet;
	}

	public void setPublicKeyExponet(String publicKeyExponet) {
		this.publicKeyExponet = publicKeyExponet;
	}

	public String getIsActivated() {
		return isActivated;
	}

	public void setIsActivated(String isActivated) {
		this.isActivated = isActivated;
	}

	public String getMfaMode() {
		return mfaMode;
	}

	public void setMfaMode(String mfaMode) {
		this.mfaMode = mfaMode;
	}

	public String getSctl() {
		// System.out.println("hieeeeeeeeeREF_ID_get"+sctl);
		return sctl;
	}

	public void setSctl(String sctl) {
		this.sctl = sctl;
		// System.out.println("hieeeeeeeeeREF_ID"+sctl);
	}

	public String getResetPinRequested() {
		return resetPinRequested;
	}

	public void setResetPinRequested(String resetPinRequested) {
		this.resetPinRequested = resetPinRequested;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDestMDN() {
		return destMDN;
	}

	public void setDestMDN(String destMDN) {
		this.destMDN = destMDN;
	}

	public String getDestBank() {
		return destBank;
	}

	public void setDestBank(String destBank) {
		this.destBank = destBank;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
		// System.out.println("Testing>>CUST NAME>>"+custName);
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		// System.out.println("Amount set>>>"+amount);
		this.amount = amount;
	}

	public String getAditionalInfo() {
		return aditionalInfo;
	}

	public void setAditionalInfo(String aditionalInfo) {
		this.aditionalInfo = aditionalInfo;
	}

	public String getRegistrationMedium() {
		return registrationMedium;
	}

	public void setRegistrationMedium(String registrationMedium) {
		this.registrationMedium = registrationMedium;
	}

	public String getAppUpdateURL() {
		return appUpdateURL;
	}

	public void setAppUpdateURL(String appUpdateURL) {
		this.appUpdateURL = appUpdateURL;
	}

	public void setMsg(String msg) {
		if (this.messagesList == null) {
			this.messagesList = new Vector();
		}
		this.messagesList.addElement(msg);
		/*
		 * if(msg.contains("NGN")){ msg=msg.replace("NGN", "$"); }else{ msg=msg;
		 * }
		 */
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	/**
	 * @return the encryptedMessagesList
	 */
	public Vector getMessagesList() {
		return messagesList;
	}

	/**
	 * @return the encryptedRefId
	 */
	public String getEncryptedRefId() {
		return encryptedRefId;
	}

	/**
	 * @param encryptedRefId
	 *            the encryptedRefId to set
	 */
	public void setEncryptedRefId(String encryptedRefId) {
		this.encryptedRefId = encryptedRefId;
	}

	/**
	 * @return the encryptedTransferId
	 */
	public String getEncryptedTransferId() {
		return encryptedTransferId;
	}

	/**
	 * @param encryptedTransferId
	 *            the encryptedTransferId to set
	 */
	public void setEncryptedTransferId(String encryptedTransferId) {
		this.encryptedTransferId = encryptedTransferId;
	}

	/**
	 * @return the encryptedParentTxnId
	 */
	public String getEncryptedParentTxnId() {
		return encryptedParentTxnId;
	}

	/**
	 * @param encryptedParentTxnId
	 *            the encryptedParentTxnId to set
	 */
	public void setEncryptedParentTxnId(String encryptedParentTxnId) {
		this.encryptedParentTxnId = encryptedParentTxnId;
	}

	/**
	 * @return the encryptedAeskey
	 */
	public String getEncryptedAESkey() {
		return encryptedAeskey;
	}

	/**
	 * @param encryptedAeskey
	 *            the encryptedAeskey to set
	 */
	public void setEncryptedAESkey(String encryptedAeskey) {
		this.encryptedAeskey = encryptedAeskey;
	}

	/**
	 * @return the encryptedAuthenticationString
	 */
	public String getAuthenticationString() {
		return AuthenticationString;
	}

	/**
	 * @param encryptedAuthenticationString
	 *            the encryptedAuthenticationString to set
	 */
	public void setAuthenticationString(String AuthenticationString) {
		this.AuthenticationString = AuthenticationString;
	}

	/**
	 * @return the encryptedSalt
	 */
	public String getSalt() {
		return Salt;
	}

	/**
	 * @param encryptedSalt
	 *            the encryptedSalt to set
	 */
	public void setSalt(String Salt) {
		this.Salt = Salt;
	}

	/**
	 * @return the encryptedTransactionCharges
	 */
	public String getEncryptedTransactionCharges() {
		return encryptedTransactionCharges;
	}

	/**
	 * @param encryptedTransactionCharges
	 *            the encryptedTransactionCharges to set
	 */
	public void setEncryptedTransactionCharges(
			String encryptedTransactionCharges) {
		this.encryptedTransactionCharges = encryptedTransactionCharges;
	}

	/**
	 * @return the encryptedDebitAmount
	 */
	public String getEncryptedDebitAmount() {
		return encryptedDebitAmount;
	}

	/**
	 * @param encryptedDebitAmount
	 *            the encryptedDebitAmount to set
	 */
	public void setEncryptedDebitAmount(String encryptedDebitAmount) {
		this.encryptedDebitAmount = encryptedDebitAmount;
	}

	/**
	 * @return the encryptedCreditAmount
	 */
	public String getEncryptedCreditAmount() {
		return encryptedCreditAmount;
	}

	/**
	 * @param encryptedCreditAmount
	 *            the encryptedCreditAmount to set
	 */
	public void setEncryptedCreditAmount(String encryptedCreditAmount) {
		this.encryptedCreditAmount = encryptedCreditAmount;
	}

	/**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @param errorMsg
	 *            the errorMsg to set
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
