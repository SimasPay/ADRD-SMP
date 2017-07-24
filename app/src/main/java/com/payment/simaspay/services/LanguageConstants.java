package com.payment.simaspay.services;

import java.util.ArrayList;
import java.util.List;

import android.widget.ArrayAdapter;



public class LanguageConstants {
	
	//Language Selection
	
	/*Login Screen*/
	public static String loginTitle;
	public static String pinText;
	public static String canNotBeEmpty;
	public static String correctPinLenth;
	public static String getLoginTitle() {
		return loginTitle;
	}
	public static void setLoginTitle(String loginTitle) {
		LanguageConstants.loginTitle = loginTitle;
	}
	public static String getPin() {
		return pinText;
	}
	public static void setPin(String pinText) {
		LanguageConstants.pinText = pinText;
	}
	public static String getCanNotBeEmpty() {
		return canNotBeEmpty;
	}
	public static void setCanNotBeEmpty(String canNotBeEmpty) {
		LanguageConstants.canNotBeEmpty = canNotBeEmpty;
	}
	public static String getCorrectPinLenth() {
		return correctPinLenth;
	}
	public static void setCorrectPinLenth(String correctPinLenth) {
		LanguageConstants.correctPinLenth = correctPinLenth;
	}



	
	public static int wallet_number = 0, bank_number =1, wallet_Bank_number=2;
	public static int mobile_banking = 2, mobile_wallet = 1, mobile_source_pocket, mobile_wallet_bank = 3;
	
	public static int getMobile_source_pocket() {
		return mobile_source_pocket;
	}
	public static void setMobile_source_pocket(int mobile_source_pocket) {
		LanguageConstants.mobile_source_pocket = mobile_source_pocket;
	}



	/*
	 *  Application connecting URL
	 */
	public static String webAPIUrl;
	
	/*
	 *  Application Error messages, Loading titles
	 */
	public static String app_title;
	public static String server_error_message;
	
	/*
	 *  For LandingScreen Services visible/invisible. Set '0' for visible, '8' to invisible.
	 */
	public static int registrationService;
	
	/*
	 *  For LoginScreen
	 */
	public static int pinLength;
	public static int minMDNLength;
	public static int maxMDNLength;	
	
	/*
	 *  For AccountType selection Emoney == 1 || Banking == 2 || Both == 0
	 * 
	 */
	public static int defaultAccountType;

	/*
	 *  For HomeScreen Services visible/invisible. Set '0' for visible, '8' to invisible.
	 */
	public static int billPayment_Service;
	
	/*
	 *  For CashOutScreen Services visible/invisible. Set '0' for visible, '8' to invisible.
	 */
	public static int from_ATM_cashout;
	
	/*
	 *  Transfer visible/invisible. Set '0' for visible, '8' to invisible.
	 */
	public static int transfer_nibss_Service;
	
	/*
	 *  TransferToOthers Services visible/invisible. Set '0' for visible, '8' to invisible.
	 */
	public static int transfer_WtoW_service;
	public static int transfer_WtoB_service;
	public static int transfer_BtoW_service;
	public static int transfer_BtoB_service;
	
	
	/*
	 *  Buy->Paymerchant visible/invisible. Set '0' for visible, '8' to invisible.
	 */
	public static int buy_paymarchant_service;
	
	/*
	 *  For Default cash selection. 0=wallet,1=bank,2=both(Ask user to select)
	 */
	public static int billpayment_home;
	public static int buy_purchase_cash_service;
	public static int buy_airtime_cash_service;
	public static int accounts_checkBalence_walletservice;
	public static int accounts_checkHistory_walletservice;
	public static int transfer_nibss_cash_service;
	
	/*
	 *  Application Error messages, Loading titles
	 */
	public static String build_date;
	public static String about_us;
	
	
	public static int getDefaultAccountType() {
		return defaultAccountType;
	}
	public static void setDefaultAccountType(int defaultAccountType) {
		LanguageConstants.defaultAccountType = defaultAccountType;
	}
	
	public static String getApp_title() {
		return app_title;
	}
	public static void setApp_title(String app_title) {
		LanguageConstants.app_title = app_title;
	}
	public static String getServer_error_message() {
		return server_error_message;
	}
	public static void setServer_error_message(String server_error_message) {
		LanguageConstants.server_error_message = server_error_message;
	}
	public static int getRegistrationService() {
		return registrationService;
	}
	public static void setRegistrationService(int registrationService) {
		LanguageConstants.registrationService = registrationService;
	}
	public static String getWebAPIUrl() {
		return webAPIUrl;
	}
	public static void setWebAPIUrl(String webAPIUrl) {
		LanguageConstants.webAPIUrl = webAPIUrl;
	}
	public static int getPinLength() {
		return pinLength;
	}
	public static void setPinLength(int pinLength) {
		LanguageConstants.pinLength = pinLength;
	}
	public static int getMinMDNLength() {
		return minMDNLength;
	}
	public static void setMinMDNLength(int minMDNLength) {
		LanguageConstants.minMDNLength = minMDNLength;
	}
	public static int getMaxMDNLength() {
		return maxMDNLength;
	}
	public static void setMaxMDNLength(int maxMDNLength) {
		LanguageConstants.maxMDNLength = maxMDNLength;
	}

	public static int getBillPayment_Service() {
		return billPayment_Service;
	}
	public static void setBillPayment_Service(int billPayment_Service) {
		LanguageConstants.billPayment_Service = billPayment_Service;
	}
	
	public static int getFrom_ATM_cashout() {
		return from_ATM_cashout;
	}
	public static void setFrom_ATM_cashout(int from_ATM_cashout) {
		LanguageConstants.from_ATM_cashout = from_ATM_cashout;
	}
	public static int getTransfer_nibss_Service() {
		return transfer_nibss_Service;
	}
	public static void setTransfer_nibss_Service(int transfer_nibss_Service) {
		LanguageConstants.transfer_nibss_Service = transfer_nibss_Service;
	}
	public static int getTransfer_WtoW_service() {
		return transfer_WtoW_service;
	}
	public static void setTransfer_WtoW_service(int transfer_WtoW_service) {
		LanguageConstants.transfer_WtoW_service = transfer_WtoW_service;
	}
	public static int getTransfer_WtoB_service() {
		return transfer_WtoB_service;
	}
	public static void setTransfer_WtoB_service(int transfer_WtoB_service) {
		LanguageConstants.transfer_WtoB_service = transfer_WtoB_service;
	}
	public static int getTransfer_BtoW_service() {
		return transfer_BtoW_service;
	}
	public static void setTransfer_BtoW_service(int transfer_BtoW_service) {
		LanguageConstants.transfer_BtoW_service = transfer_BtoW_service;
	}
	public static int getTransfer_BtoB_service() {
		return transfer_BtoB_service;
	}
	public static void setTransfer_BtoB_service(int transfer_BtoB_service) {
		LanguageConstants.transfer_BtoB_service = transfer_BtoB_service;
	}
	public static int getTransfer_nibss_cash_service() {
		return transfer_nibss_cash_service;
	}
	public static void setTransfer_nibss_cash_service(
			int transfer_nibss_cash_service) {
		LanguageConstants.transfer_nibss_cash_service = transfer_nibss_cash_service;
	}
	public static int getBuy_paymarchant_service() {
		return buy_paymarchant_service;
	}
	public static void setBuy_paymarchant_service(int buy_paymarchant_service) {
		LanguageConstants.buy_paymarchant_service = buy_paymarchant_service;
	}
	public static int getBillpayment_home() {
		return billpayment_home;
	}
	public static void setBillpayment_home(int billpayment_home) {
		LanguageConstants.billpayment_home = billpayment_home;
	}
	public static int getBuy_purchase_cash_service() {
		return buy_purchase_cash_service;
	}
	public static void setBuy_purchase_cash_service(int buy_purchase_cash_service) {
		LanguageConstants.buy_purchase_cash_service = buy_purchase_cash_service;
	}
	public static int getBuy_airtime_cash_service() {
		return buy_airtime_cash_service;
	}
	public static void setBuy_airtime_cash_service(int buy_airtime_cash_service) {
		LanguageConstants.buy_airtime_cash_service = buy_airtime_cash_service;
	}
	public static int getAccounts_checkBalence_walletservice() {
		return accounts_checkBalence_walletservice;
	}
	public static void setAccounts_checkBalence_walletservice(
			int accounts_checkBalence_walletservice) {
		LanguageConstants.accounts_checkBalence_walletservice = accounts_checkBalence_walletservice;
	}
	public static int getAccounts_checkHistory_walletservice() {
		return accounts_checkHistory_walletservice;
	}
	public static void setAccounts_checkHistory_walletservice(
			int accounts_checkHistory_walletservice) {
		LanguageConstants.accounts_checkHistory_walletservice = accounts_checkHistory_walletservice;
	}
	public static String getBuild_date() {
		return build_date;
	}
	public static void setBuild_date(String build_date) {
		LanguageConstants.build_date = build_date;
	}
	public static String getAbout_us() {
		return about_us;
	}
	public static void setAbout_us(String about_us) {
		LanguageConstants.about_us = about_us;
	}
	
	public static ArrayAdapter<String> dataAdapter;


	public static ArrayAdapter<String> getDataAdapter() {
		return dataAdapter;
	}
	public static void setDataAdapter(ArrayAdapter<String> dataAdapter) {
		LanguageConstants.dataAdapter = dataAdapter;
	}


	public static List<String> list = new ArrayList<String>();
	

	public static List<String> getList() {
		return list;
	}
	public static void setList(List<String> list) {
		LanguageConstants.list = list;
	}

	
	public static String SOURCE_MDN_NAME = "";
	public static String SOURCE_NIC_NAME = "";
	public static String SOURCE_MDN_PIN = "1234";
	public static String SOURCE_INSTITUTION_ID = "HUB";
	//public static String DESTINATION_MDN_NAME = "";
	
	public static int MFA_CONNECTION_TIMEOUT =20000 ;
	public static int CONNECTION_TIMEOUT =30000 ;
    public static final String ZEROES_STRING = "000";
    public static final String UTF_8 = "UTF-8";
    public static final String US_ASCII = "US-ASCII";
    public static final int PBE_ITERATION_COUNT = 20;
    public static final byte[] ZEROES_STRING_ENCODED = null;
    public static final String EMPTY_STRING = "";
    // services
    public static final String SERVICE_ACCOUNT = "Account";
    public static final String SERVICE_NFC = "NFCService";
    public static final String SERVICE_WALLET = "Wallet";
    public static final String SERVICE_BANK = "Bank";
    public static final String SERVICE_AGENT = "AgentServices";
    public static final String SERVICE_SHOPPING = "Shopping";
    public static final String SERVICE_BUY = "Buy";
    public static final String SERVICE_BILLPAYMENT="Payment";
    
    public static String TRANSACTION_CASHOUT_AT_ATM_INQUIRY= "CashOutAtATMInquiry";
    public static String TRANSACTION_CASHOUT_AT_ATM= "CashOutAtATM";
    // transactions  
    public static final String TRANSACTION_GENERATE_OTP = "GenerateOTP";
    public static final String TRANSACTION_VALIDATE_OTP = "ValidateOTP";
    public static final String TRANSACTION_NFC_CARDLINK = "NFCCardLink";
    public static final String TRANSACTION_NFC_CARD_UNLINK = "NFCCardUnlink";
    public static final String TRANSACTION_NFC_MODIFY_ALIAS = "ModifyNFCCardAlias";
    public static final String TRANSACTION_ADD_FAVORITE = "AddFavorite";
    public static final String TRANSACTION_EDIT_FAVORITE = "EditFavorite";
    public static final String TRANSACTION_DELETE_FAVORITE = "DeleteFavorite";
    public static final String TRANSACTION_CHANGE_NICKNAME = "ChangeNickname";
    public static final String TRANSACTION_CHANGE_EMAIL = "ChangeEmail";
    public static final String TRANSACTION_FORGOT_PASSWORD = "ChangeEmail";
    public static final String TRANSACTION_GET_FAVORITES = "GenerateFavoriteJSON";
    public static final String TRANSACTION_NFC_CARD_ALIAS_MODIFY = "ModifyNFCCardAlias";
    public static final String TRANSACTION_NFC_CHECKBALANCE = "NFCCardBalance";
    public static final String TRANSACTION_NFC_HISTORY = "History";
    public static final String TRANSACTION_REGISTRATION= "RegistrationWithActivationForHub";
    public static final String TRANSACTION_GETPUBLICKEY = "GetPublicKey";
    public static final String TRANSACTION_PURCHASE_INQUIRY = "PurchaseInquiry";
    public static final String TRANSACTION_PURCHASE = "Purchase";
    public static final String TRANSACTION_ACTIVATION = "Activation";
    public static final String TRANSACTION_RESEND_OTP = "ResendOtp";
    public static final String TRANSACTION_REACTIVATION = "Reactivation";
    public static final String TRANSACTION_RESET_PIN = "ResetPinByOTP";
    public static final String TRANSACTION_REGISTRATION_MEDIUM = "GetRegistrationMedium";
    public static final String TRANSACTION_TRANSACTIONSTATUS = "TransactionStatus";
    public static final String TRANSACTION_CHANGEPIN = "ChangePIN";
    public static final String TRANSACTION_CHANGEPIN_CONFIRM = "ChangePinConfirm";
    public static final String TRANSACTION_ACTIVATION_CONFIRM = "ActivationConfirm";
    public static final String TRANSACTION_REACTIVATION_CONFIRM = "ReactivationConfirm";
    public static final String TRANSACTION_RESETPIN = "ResetPIN";
    public static final String TRANSACTION_AGENTACTIVATION = "AgentActivation";
    public static final String TRANSACTION_CHECKBALANCE = "CheckBalance";
    public static final String TRANSACTION_PDF_DOWNLOAD = "DownloadHistoryAsPDF";
    public static final String TRANSACTION_EMAIL_SEND = "EmailHistoryAsPDF";
    public static final String TRANSACTION_HISTORY = "History";
    public static final String TRANSACTION_TRANSFER = "Transfer";
    public static final String TRANSACTION_CASHOUT = "CashOut";
    public static final String TRANSACTION_SUBSCRIBERREGISTRATION = "SubscriberRegistration";
    public static final String TRANSACTION_CASHIN = "CashIn";
    public static final String TRANSACTION_INTERBANK_TRANSFER_INQUIRY = "InterBankTransferInquiry";
    public static final String TRANSACTION_INTERBANK_TRANSFER = "InterBankTransfer";
    //webapi specific 
    public static final String TRANSACTION_LOGIN = "Login";
    public static final String TRANSACTION_LOGOUT = "Logout";
    public static final String TRANSACTION_TRANSFER_INQUIRY = "TransferInquiry";
    public static final String TRANSACTION_CASHIN_INQUIRY = "CashInInquiry";
    public static final String TRANSACTION_CASHOUT_INQUIRY = "CashOutInquiry";
    public static final String TRANSACTION_NFC_POCKET_TOPUP_INQUIRY = "NFCPocketTopupInquiry";
    public static final String TRANSACTION_NFC_POCKET_TOPUP = "NFCPocketTopup";
    public static final String TRANSACTION_NFC_MULTICARD = "NFCCardBalance";
    //Buy Section transations
    public static final String TRANSACTION_AIRTIME_PURCHASE_INQUIRY = "AirtimePurchaseInquiry";
    public static final String TRANSACTION_AIRTIME_PURCHASE = "AirtimePurchase";
    
    //Bill payment transactions
    
    public static final String TRANSACTION_BILLPAYMENT_INQUIRY = "BillPayInquiry";
    public static final String TRANSACTION_BILLPAYMENT = "BillPay";
    
    //parameters 
    public static final String PARAMETER_INSTITUTION_ID = "institutionID";
    public static final String PARAMETER_AUTHENTICATION_KEY = "authenticationKey";
    public static final String PARAMETER_SERVICE_NAME = "service";
    public static final String PARAMETER_TRANSACTIONNAME = "txnName";
    public static final String PARAMETER_CARD_PAN = "cardPan";
    public static final String PARAMETER_CARD_ALIAS_NAME = "cardAlias";
    public static final String PARAMETER_SOURCE_MDN = "sourceMDN";
    public static final String PARAMETER_ID_NUMBER = "idNumber";
    public static final String PARAMETER_NICNAME = "nickname";
    public static final String PARAMETER_NEW_PIN = "newPIN";
    public static final String PARAMETER_CONFIRM_NEW_PIN = "newPIN";
    public static final String PARAMETER_OTP = "otp";
    public static final String PARAMETER_NON_FINANCIAL_ENQUIRY = "mfaTransaction";
    
    public static final String PARAMETER_MFA_OTP = "mfaOtp";
    public static final String PARAMETER_CHANNEL_ID = "channelID";
    public static final String PARAMETER_CONFIRM_PIN = "confirmPIN";
    public static final String PARAMETER_SOURCE_PIN = "sourcePIN";
    public static final String PARAMETER_FAV_CAT_ID = "favoriteCategoryID";
    public static final String PARAMETER_FAV_LABEL = "favoriteLabel";
    public static final String PARAMETER_FAV_VALUE = "favoriteValue";
    public static final String PARAMETER_NICKNAME = "nickname";
    public static final String PARAMETER_NEW_EMAIL = "newEmail";
    public static final String PARAMETER_CONFIRM_EMAIL = "confirmEmail";
    public static final String PARAMETER_TRANSACTIONID = "transactionID";
    public static final String PARAMETER_DEST_MDN = "destMDN";
    public static final String PARAMETER_AMOUNT = "amount";
    public static final String PARAMETER_SECRET_ANSWER = "secretAnswer";
    public static final String PARAMETER_CONTACT_NO = "contactNumber";
    public static final String PARAMETER_EMAIL = "email";
    public static final String PARAMETER_LANG = "language";
    public static final String PARAMETER_NOTIFICATION_METHOD = "notificationMethod";
    public static final String PARAMETER_PARTNER_CODE = "partnerCode";
    public static final String PARAMETER_AGENT_CODE = "agentCode";
    public static final String PARAMETER_BUCKET_TYPE = "bucketType";
    public static final String PARAMETER_SRC_POCKET_CODE = "sourcePocketCode";
    public static final String PARAMETER_NO_OF_RECORDS = "numRecords";
    public static final String PARAMETER_PAGE_NUMBER = "pageNumber";
    public static final String PARAMETER_FROM_DATE = "fromDate";
    public static final String PARAMETER_TO_DATE= "toDate";
    public static final String PARAMETER_CARDPAN_SUFFIX = "cardPANSuffix";
    public static final String PARAMETER_SRC_MESSAGE = "sourceMessage";
    public static final String PARAMETER_DEST_POCKET_CODE = "destPocketCode";
    public static final String PARAMETER_TRANSFER_ID = "transferID";
    public static final String PARAMETER_CONFIRMED = "confirmed";
    public static final String PARAMETER_PARENTTXN_ID = "parentTxnID";
    public static final String PARAMETER_ISDEFAULT = "isDefault";
    public static final String PARAMETER_BANK_ID = "bankID";
    public static final String PARAMETER_BILLER_NAME = "billerName";
    public static final String PARAMETER_CUSTOMER_ID = "customerID";
    public static final String PARAMETER_BILL_DETAILS = "billDetails";
    public static final String PARAMETER_DATA = "data";
    public static final String PARAMETER_MFS_BILLER_CODE = "mfsBillerCode";
    public static final String PARAMETER_INVOICE_NO = "invoiceNo";
    public static final String PARAMETER_SUB_MDN = "subMDN";
    public static final String PARAMETER_SUB_FIRSTNAME = "subFirstName";
    public static final String PARAMETER_SUB_LASTNAME = "subLastName";
    public static final String PARAMETER_ACCOUNT_TYPE = "accountType";
    public static final String PARAMETER_APPLICATION_ID = "appId";
    public static final String PARAMETER_DOB = "dob";
    public static final String PARAMETER_SALT = "salt";
    public static final String PARAMETER_AUTHENTICATION_STRING = "authenticationString";
    public static final String PARAMETER_ACTIVATION_NEWPIN = "activationNewPin";
    public static final String PARAMETER_ACTIVATION_CONFIRMPIN = "activationConfirmPin";
    public static final String PARAMETER_OTHER_NUMBER = "otherMDN";
    public static final String PARAMETER_COMPANY_ID="companyID";
    
    public static final String PARAMETER_APPTYPE = "apptype";
    public static final String PARAMETER_APPVERSION = "appversion";
    public static final String PARAMETER_APPOS = "appos";
    
    public static final String PARAMETER_DEST_BANK_CODE = "destBankCode";
    public static final String PARAMETER_DEST_ACCOUNT_NO = "destAccountNo";
    
   // Parameters for Bill payement
    public static final String PARAMETER_BILLER_CODE = "billerCode";
    public static final String PARAMETER_BILL_NO="billNo";
    	
    //rajkumer added for DestBankAccount
    public static final String PARAMETER_DEST_BankAccount ="destBankAccount";
    
    public static final String MESSAGE_MOBILE_TRANSFER = "Mobile Transfer";
    public static final String DUMMY_BANK_ID = "Not Yet";
    public static final String CONSTANT_VALUE_ZERO = "0";
    public static final String CONSTANT_VALUE_TRUE = "true";
    public static final String SERVICE_PROVIDER_NAME = "serviceProviderName";
    public static final String CONSTANT_VALUE_FALSE = "false";
    public static final String CONSTANT_CHANNEL_ID = "7";
    public static final String CONSTANT_INSTITUTION_ID = "HUB";
    public static final String CONSTANT_EMONEY = "E Money";
    public static final String CONSTANT_BANK = "Bank";
    public static final String CONSTANT_AIRTIME = "Airtime";
    public static final int COLOUR_GRAY = 0xB5B5B5;
    public static final int COLOUR_SLATEGRAY = 0x9FB6CD;
    public static final String XML_MESSAGE = "message";
    public static final String XML_ADITIONAL_INFO = "AdditionalInfo";
    public static final String XML_FIRST_NAME = "FirstName";
    public static final String XML_LAST_NAME = "LastName";
    public static final String XML_REC_KYC_NAME = "ReceiverAccountName";
    public static final String XML_DOWNLOAD_URL = "downloadURL";
    public static final String XML_NFC_CARD_ALIAS_NAME = "CardAlias";
    public static final String XML_PUBLIC_MODULUS = "PublicKeyModulus";
    public static final String XML_PUBLIC_EXPONENT = "PublicKeyExponent";
    public static final String XML_SUCCESS= "Success";
    public static final String XML_DEST_CUST_NAME = "destinationName";
    public static final String XML_DEST_MDN = "destinationMDN";
    public static final String XML_DEST_BANK= "destinationBank";
    public static final String XML_ACCOUNT_NUMBER = "destinationAccountNumber";
    public static final String XML_AMOUNT_TRANSFER= "creditamt";
    public static final String XML_TRANSACTION_TIME = "transactionTime";
    public static final String XML_REFID = "refID";
    public static final String XML_TRANSFERID = "transferID";
    public static final String XML_SCTL = "sctlID";
    public static final String XML_ID_NUMBER = "IDNumber";
    public static final String XML_MFAMODE = "mfaMode";
    public static final String XML_PARENT_TXNID = "parentTxnID";
    public static final String XML_INPUT = "input";
    public static final String XML_NAME = "name";
    public static final String XML_VALUE = "value";
    public static final String XML_AMOUNT = "amount";
    public static final String XML_BILL_DETAILS = "billDetails";
    public static final String XML_KEY = "key";
    public static final String XML_SALT = "salt";
    public static final String XML_AUTHENTICATION = "authentication";
    public static final String XML_TRANSACTION_CHARGES = "charges";
    public static final String XML_DEBIT_AMOUNT = "debitamt";
    public static final String XML_CREDIT_AMOUNT = "creditamt";
    public static final String XML_APPUPDATEURL="url";
    public static final String XML_REGISTRATION_MEDIUM="RegistrationMedium";
    public static final String XML_RESET_PIN_REQUEST="ResetPinRequested";
    public static final String XML_STATUS="Status";
    public static final String XML_IS_UNREGISTER="IsUnregistered";
    public static final String XML_IS_ALREADY_ACTIVATED="IsAlreadyActivated";
    //NOTIFICATIONS
    public static final String NOTIFICATIONCODE_WRONGPINSPECIFIED = "29";
    public static final String NOTIFICATIONCODE_TRANSFERINQUIRY_SUCCESS = "72";
    public static final String FEATURE_TRANSFER_SELF = "Transfer to Self";
    public static final String FEATURE_TRANSFER_OTHERS = "Transfer to Others";
    public static final String FEATURE_TRANSFER_EAZYTOBANK = "eaZyMoney To Bank";
    public static final String FEATURE_TRANSFER_BANKTOEAZY = "Bank to eaZyMoney";
    public static final String FEATURE_TRANSFER = "Transfer";
    public static final String FEATURE_BUY = "Buy";
    public static final String FEATURE_PAY = "Pay";
    public static final String FEATURE_MYACCOUNT = "Account";
    public static final String FEATURE_TO_SELF = "To Self";
    public static final String FEATURE_TO_FRIENDS = "To Friends";
    public static final String LABEL_BANK = "Bank";
    public static final String LABEL_EMONEY = "eaZyMoney";
    public static final String LABEL_BANK_TO_EMONEY = "Bank to eaZyMoney";
    public static final String LABEL_EMONEY_TO_BANK = "eaZyMoney to Bank";
    public static final String POCKET_CODE_EMONEY = "1";
    public static final String POCKET_CODE_BANK = "2";
    public static final String DISCLOSURE_LEGAL = "Legal Disclosure for Mobile Money Service " + "\n" + "I agree to the terms of this service";
    
}
