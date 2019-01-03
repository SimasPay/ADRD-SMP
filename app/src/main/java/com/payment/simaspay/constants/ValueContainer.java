package com.payment.simaspay.constants;

import java.io.Serializable;


import android.content.Context;
import android.graphics.Bitmap;



/**
 * Response container
 * 
 * @author pramod
 */
public class ValueContainer implements Serializable {

	private String sourceMdn;
	private String destinationMdn;
	private String destBankAccount;
	private String sourcePin;
	private String amount;
	private String transferId;
	private String parentTxnId;
	private String secretAnswer;
	private String newPin;
	private String confirmPin;
	private String ActivationNewPin;
	private String ActivationConfirmPin;
	private String otp;
	private String TransactionID;
	private String Transactioncharges;
	private String creditAmount;
	private String debitAmount;
	private String authenticationString;
	private String salt;
	private String confirmed;
	private String channelId;
	private String sourcePocketCode;
	private String destinationPocketCode;
	private String serviceName;
	// private KeyParameter keyParameter;
	private String TransactionName;
	private String merchantCode;
	private String firstName;
	private String lastName;
	private String applicationID;
	private String SubscriberMDN;
	private String DateOfBirth;
	private String AccountType;
	private String ResponseMessage;
	private boolean loginResult;
	private String agentCode;
	private String billerCode;
	private String billNo;
	private String compId;
	private String appUpdateURL;
	private String appVersion;
	private String appOS;
	private String appType;
	private String transferType;
	private String BankCode;
	private String BankAccountNo;
	private boolean isUnregistered = false;
	private String cardPan;
	private String mfaMode;
	private String changePinConfirm;
	private String nonFinancialTransaction;
	private String sctl;
	private String activationOTP;
	public static String hashKey;
	public static String bankPin;
	public String nicName;
	public String otherNumber;
	public String institionID;
	public String pageNumber;
	public String numberofRecords;
	public String nfcCardAlias;
	public String favLabel;
	public String favValue;
	public String newEmail;
	public String confirmEmail;
	public String categoryId;
	public String fromDate;
	public String toDate;
	public String sendMail;
	public String favCode;
	public String screen;
	public String paymentMode;
	public String merchantName;
	public String userApiKey;
	public String demonCode;
	public String netPrice;
	Context context;
	public String description;
	public static Bitmap bitmap;
	public static Bitmap destbitmap;
	
	
	

	public static Bitmap getDestBitmap() {
		return destbitmap;
	}

	public static void setDestBitmap(Bitmap destbitmap) {
		ValueContainer.destbitmap = destbitmap;
	}

	public static Bitmap getBitmap() {
		return bitmap;
	}

	public static void setBitmap(Bitmap bmp) {
		ValueContainer.bitmap = bmp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getDemonCode() {
		return demonCode;
	}

	public void setDemonCode(String demonCode) {
		this.demonCode = demonCode;
	}

	public String getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(String netPrice) {
		this.netPrice = netPrice;
	}

	public String getUserApiKey() {
		return userApiKey;
	}

	public void setUserApiKey(String userApiKey) {
		this.userApiKey = userApiKey;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getSendMail() {
		return sendMail;
	}

	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}

	public String getFavLabel() {
		return favLabel;
	}

	public void setFavLabel(String favLabel) {
		this.favLabel = favLabel;
	}

	public String getFavValue() {
		return favValue;
	}

	public void setFavValue(String favValue) {
		this.favValue = favValue;
	}

	public void setFavCode(String favCode) {
		this.favCode = favCode;
	}

	public String getFavCode() {
		return favCode;
	}

	private String idNumber;

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getNfcCardAlias() {
		return nfcCardAlias;
	}

	public void setNfcCardAlias(String nfcCardAlias) {
		this.nfcCardAlias = nfcCardAlias;
	}

	public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getNumberofRecords() {
		return numberofRecords;
	}

	public void setNumberofRecords(String numberofRecords) {
		this.numberofRecords = numberofRecords;
	}

	public String getInstitionID() {
		return institionID;
	}

	public void setInstitionID(String institionID) {
		this.institionID = institionID;
	}

	public String getNicName() {
		return nicName;
	}

	public void setNicName(String nicName) {
		this.nicName = nicName;
	}

	public String getOtherNumber() {
		return otherNumber;
	}

	public void setOtherNumber(String otherNumber) {
		this.otherNumber = otherNumber;
	}

	public String getBankPin() {
		return bankPin;
	}

	public void setBankPin(String bankPin) {
		this.bankPin = bankPin;
	}

	private String mfaOTP;

	public String getMfaOTP() {
		return mfaOTP;
	}

	public void setMfaOTP(String mfaOTP) {
		this.mfaOTP = mfaOTP;
	}

	public String getActivationOTP() {
		return activationOTP;
	}

	public void setActivationOTP(String activationOTP) {
		this.activationOTP = activationOTP;
	}

	public String getSctl() {
		return sctl;
	}

	public void setSctl(String sctl) {
		this.sctl = sctl;
	}

	public String getNonFinancialTransaction() {
		return nonFinancialTransaction;
	}

	public void setNonFinancialTransaction(String nonFinancialTransaction) {
		this.nonFinancialTransaction = nonFinancialTransaction;
	}

	public String getChangePinConfirm() {
		return changePinConfirm;
	}

	public void setChangePinConfirm(String changePinConfirm) {

		// this.changePinConfirm=HSM(changePinConfirm);
		// Without HSM
		this.changePinConfirm = changePinConfirm;
	}

	public String getMfaMode() {
		return mfaMode;
	}

	public void setMfaMode(String mfaMode) {
		this.mfaMode = mfaMode;
	}

	public String getCardPan() {
		return cardPan;
	}

	public void setCardPan(String cardPan) {
		this.cardPan = cardPan;
	}

	public boolean getUnregistered() {
		return isUnregistered;
	}

	public void setUnregistered(boolean unregistered) {
		this.isUnregistered = unregistered;
	}

	public String getBankAccountNo() {
		return BankAccountNo;
	}

	public void setBankAccountNo(String BankAccountNo) {
		this.BankAccountNo = BankAccountNo;
	}

	public void setBankCode(String code) {
		this.BankCode = code;
	}

	public String getBankCode() {
		return this.BankCode;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public String getAppUpdateURL() {
		return appUpdateURL;
	}

	public void setAppUpdateURL(String appUpdateURL) {
		this.appUpdateURL = appUpdateURL;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppOS() {
		return appOS;
	}

	public void setAppOS(String appOS) {
		this.appOS = appOS;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	/**
	 * @return the agentCode
	 */
	public String getAgentCode() {
		return agentCode;
	}

	/**
	 * @param agentCode
	 *            the agentCode to set
	 */
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	/**
	 * @return the billerCode
	 */
	public String getBillerCode() {
		return billerCode;
	}

	/**
	 * @param billerCode
	 *            the billerCode to set
	 */
	public void setBillerCode(String billerCode) {
		this.billerCode = billerCode;
	}

	/**
	 * @return the billNo
	 */
	public String getBillNo() {
		return billNo;
	}

	/**
	 * @param billNo
	 *            the billNo to set
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/**
	 * @return the compId
	 */
	public String getCompanyId() {
		return compId;
	}

	/**
	 * @param compId
	 *            the compId to set
	 */
	public void setCompanyId(String compId) {
		this.compId = compId;
	}

	public String getSourceMdn() {
		return sourceMdn;
	}

	public void setSourceMdn(String mdn) {

		if (mdn.length() < 5) {
			try {

				/*
				 * SharedPreferences settings =
				 * getContext().getSharedPreferences
				 * ("LOGIN_PREFERECES",Context.MODE_WORLD_READABLE);
				 * mdn=settings.getString("MDN", "NONE");
				 */
//
//				mDbFHelper = new DataBase(getContext());
//				mDbFHelper.open();
//				Cursor c = mDbFHelper.getmdn();
//				c.moveToFirst();
//				mdn = c.getString(c.getColumnIndex("mdn"));
				// String nicName = c.getString(c.getColumnIndex("nicName"));
				// settings.edit().putString("MDN", mdn).commit();

//				Constants.SOURCE_MDN_NAME = mdn;
//				Constants.SOURCE_NIC_NAME = nicName;
//				System.out.println("Testing>>MDN>>" + mdn);
//				System.out.println("Testing>>NAME>>" + nicName);
//
//				c.close();
//				mDbFHelper.close();

			} catch (Exception e) {

			}
		}

		/*
		 * if((mdn.indexOf("0")==0)){ mdn=mdn.replaceFirst("0", "62"); }else
		 * if(!(mdn.indexOf("62")==0)){ mdn="62"+mdn; }
		 */
		this.sourceMdn = mdn.replace("+", "");
		// } else {
		// throw new Exception("SourceMDN can not be changed");
		// }
	}

	public String getDestinationBankAccount() {
		return destBankAccount;
	}

	public void setDestinationBankAccount(String destBankAccount) {
		this.destBankAccount = destBankAccount;
	}

	public String getDestinationMdn() {
		return destinationMdn;
	}

	public void setDestinationMdn(String destinationMdn) {
		this.destinationMdn = destinationMdn;
	}

	public String getSourcePin() {
		return sourcePin;
	}

	public void setSourcePin(String pin) {

		// this.sourcePin=HSM(pin);

		// Without HSM
		this.sourcePin = pin;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getSourcePocketCode() {
		return sourcePocketCode;
	}

	public void setSourcePocketCode(String sourcePocketCode) {
		this.sourcePocketCode = sourcePocketCode;
	}

	public String getDestinationPocketCode() {
		return destinationPocketCode;
	}

	public void setDestinationPocketCode(String destinationPocketCode) {
		this.destinationPocketCode = destinationPocketCode;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getParentTxnId() {
		return parentTxnId;
	}

	public void setParentTxnId(String parentTxnId) {
		this.parentTxnId = parentTxnId;
	}

	public String getSecretAnswer() {
		return secretAnswer;
	}

	public void setSecretAnswer(String secretAnswer) {
		this.secretAnswer = secretAnswer;
	}

	public String getNewPin() {
		return newPin;
	}

	public void setNewPin(String newPin) {

		// this.newPin=HSM(newPin);
		// without HSM
		this.newPin = newPin;

	}

	public String getActivationNewPin() {
		return this.ActivationNewPin;
	}

	public void setActivationNewPin(String ActivationNewPin) {
		// this.ActivationNewPin=HSM(ActivationNewPin);
		// without HSM
		this.ActivationNewPin = ActivationNewPin;
	}

	public String getActivationConfirmPin() {

		return this.ActivationConfirmPin;
	}

	public void setActivationConfirmPin(String ActivationConfirmPin) {

		// this.ActivationConfirmPin=HSM(ActivationConfirmPin);
		// without HSM
		this.ActivationConfirmPin = ActivationConfirmPin;

	}

	public String getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(String confirmed) {
		this.confirmed = confirmed;
	}

	public String getConfirmPin() {
		return confirmPin;
	}

	public void setConfirmPin(String confirmPin) {
		// this.confirmPin=HSM(confirmPin);
		// without HSM
		this.confirmPin = confirmPin;

	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getAuthenticationString() {
		return authenticationString;
	}

	public void setAuthenticationString(String authenticationString) {
		this.authenticationString = authenticationString;
	}

	/*
	 * public KeyParameter getAESKey() { return keyParameter; }
	 * 
	 * public void setAESKey(byte[] kps) {
	 * 
	 * if(kps==null) { this.keyParameter = null; return; }
	 * 
	 * this.keyParameter = new KeyParameter(kps); }
	 */

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the applicationID
	 */
	public String getApplicationID() {
		return applicationID;
	}

	/**
	 * @param applicationID
	 *            the applicationID to set
	 */
	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
	}

	/**
	 * @return the SubscriberMDN
	 */
	public String getSubscriberMDN() {
		return SubscriberMDN;
	}

	/**
	 * @param SubscriberMDN
	 *            the SubscriberMDN to set
	 */
	public void setSubscriberMDN(String SubscriberMDN) {
		this.SubscriberMDN = SubscriberMDN;
	}

	/**
	 * @return the DateOfBirth
	 */
	public String getDateOfBirth() {
		return DateOfBirth;
	}

	/**
	 * @param DateOfBirth
	 *            the DateOfBirth to set
	 */
	public void setDateOfBirth(String DateOfBirth) {
		this.DateOfBirth = DateOfBirth;
	}

	/**
	 * @return the AccountType
	 */
	public String getAccountType() {
		return AccountType;
	}

	/**
	 * @param AccountType
	 *            the AccountType to set
	 */
	public void setAccountType(String AccountType) {
		this.AccountType = AccountType;
	}

	/**
	 * @return the TransactionName
	 */
	public String getTransactionName() {
		return TransactionName;
	}

	/**
	 * @param TransactionName
	 *            the TransactionName to set
	 */
	public void setTransactionName(String TransactionName) {
		this.TransactionName = TransactionName;
	}

	/**
	 * @return the loginResult
	 */
	public boolean isLoginResult() {
		return loginResult;
	}

	/**
	 * @param loginResult
	 *            the loginResult to set
	 */
	public void setLoginResult(boolean loginResult) {
		this.loginResult = loginResult;
	}

	// /**
	// * @return the nextForm
	// */
	// public CustomizedForm getNextForm() {
	// return nextForm;
	// }
	//
	// /**
	// * @param nextForm the nextForm to set
	// */
	// public void setNextForm(Form nextForm) {
	// this.nextForm = nextForm;
	// }

	/**
	 * @return the ResponseMessage
	 */
	public String getResponseMessage() {
		return ResponseMessage;
	}

	/**
	 * @param ResponseMessage
	 *            the ResponseMessage to set
	 */
	public void setResponseMessage(String ResponseMessage) {
		this.ResponseMessage = ResponseMessage;
	}

	/**
	 * @return the merchantCode
	 */
	public String getPartnerCode() {
		return merchantCode;
	}

	/**
	 * @param merchantCode
	 *            the merchantCode to set
	 */
	public void setPartnerCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	/**
	 * @return the otp
	 */
	public String getOTP() {
		return otp;
	}

	/**
	 * @param otp
	 *            the otp to set
	 */
	public void setOTP(String otp) {
		this.otp = otp;
	}

	/**
	 * @return the Transactioncharges
	 */
	public String getTransactioncharges() {
		return Transactioncharges;
	}

	/**
	 * @param Transactioncharges
	 *            the Transactioncharges to set
	 */
	public void setTransactioncharges(String Transactioncharges) {
		this.Transactioncharges = Transactioncharges;
	}

	/**
	 * @return the TransactionID
	 */
	public String getTransactionID() {
		return TransactionID;
	}

	/**
	 * @param TransactionID
	 *            the TransactionID to set
	 */
	public void setTransactionID(String TransactionID) {
		this.TransactionID = TransactionID;
	}

	/**
	 * @return the netCahsinAmount
	 */
	public String getCreditAmount() {
		return creditAmount;
	}

	/**
	 * @param netCahsinAmount
	 *            the netCahsinAmount to set
	 */
	public void setCreditAmount(String netCahsinAmount) {
		this.creditAmount = netCahsinAmount;
	}

	/**
	 * @return the debitAmount
	 */
	public String getDebitAmount() {
		return debitAmount;
	}

	/**
	 * @param debitAmount
	 *            the debitAmount to set
	 */
	public void setDebitAmount(String debitAmount) {
		this.debitAmount = debitAmount;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMerchantCode() {
		return this.merchantCode;
	}


}
