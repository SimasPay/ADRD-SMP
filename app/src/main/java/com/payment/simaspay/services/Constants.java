package com.payment.simaspay.services;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    // Language Selection
    public static String URL_PBQ = "https://sandbox.dimo.co.id";
    private static String serverNotResponding;
    public static final String LOG_TAG = "SimasPay";
    public static String getServerNotResponding() {
        return serverNotResponding;
    }

    public static void setServerNotResponding(String serverNotResponding) {
        Constants.serverNotResponding = serverNotResponding;
    }

	/* Login Screen */

    private static String loginTitle;
    private static String pinText;
    private static String canNotBeEmpty;
    private static String incorrectPinLenth;
    private static String loginBodyMsg;

    public static String getLoginBodyMsg() {
        return loginBodyMsg;
    }

    public static void setLoginBodyMsg(String loginBodyMsg) {
        Constants.loginBodyMsg = loginBodyMsg;
    }

    public static String getLoginTitle() {
        return loginTitle;
    }

    public static void setLoginTitle(String loginTitle) {
        Constants.loginTitle = loginTitle;
    }

    public static String getPin() {
        return pinText;
    }

    public static void setPin(String pinText) {
        Constants.pinText = pinText;
    }

    public static String getCanNotBeEmpty() {
        return canNotBeEmpty;
    }

    public static void setCanNotBeEmpty(String canNotBeEmpty) {
        Constants.canNotBeEmpty = canNotBeEmpty;
    }

    public static String getInCorrectPinLenth() {
        return incorrectPinLenth;
    }

    public static void setInCorrectPinLenth(String incorrectPinLenth) {
        Constants.incorrectPinLenth = incorrectPinLenth;
    }

	/* Login Screen */

	/* Settings Screens */

    private static String setting;
    private static String changePin;
    private static String forgetPin;
    private static String changeEmail;
    private static String changeNama;
    private static String termsAndCondition;
    private static String privacy;
    private static String takePhoto;
    public static String pin;
    private static String newPin;
    private static String confirmPin;
    private static String newEmail;
    private static String confirmEmail;
    private static String save;
    private static String nameChange;
    private static String nameChangeText;

    public static String getSetting() {
        return setting;
    }

    public static void setSetting(String setting) {
        Constants.setting = setting;
    }

    public static String getChangePin() {
        return changePin;
    }

    public static void setChangePin(String changePin) {
        Constants.changePin = changePin;
    }

    public static String getForgetPin() {
        return forgetPin;
    }

    public static void setForgetPin(String forgetPin) {
        Constants.forgetPin = forgetPin;
    }

    public static String getChangeEmail() {
        return changeEmail;
    }

    public static void setChangeEmail(String changeEmail) {
        Constants.changeEmail = changeEmail;
    }

    public static String getChangeNama() {
        return changeNama;
    }

    public static void setChangeNama(String changeNama) {
        Constants.changeNama = changeNama;
    }

    public static String getTermsAndCondition() {
        return termsAndCondition;
    }

    public static void setTermsAndCondition(String termsAndCondition) {
        Constants.termsAndCondition = termsAndCondition;
    }

    public static String getPrivacy() {
        return privacy;
    }

    public static void setPrivacy(String privacy) {
        Constants.privacy = privacy;
    }

    public static String getTakePhoto() {
        return takePhoto;
    }

    public static void setTakePhoto(String takePhoto) {
        Constants.takePhoto = takePhoto;
    }

    public static String getNewPin() {
        return newPin;
    }

    public static void setNewPin(String newPin) {
        Constants.newPin = newPin;
    }

    public static String getConfirmPin() {
        return confirmPin;
    }

    public static void setConfirmPin(String confirmPin) {
        Constants.confirmPin = confirmPin;
    }

    public static String getNewEmail() {
        return newEmail;
    }

    public static void setNewEmail(String newEmail) {
        Constants.newEmail = newEmail;
    }

    public static String getConfirmEmail() {
        return confirmEmail;
    }

    public static void setConfirmEmail(String confirmEmail) {
        Constants.confirmEmail = confirmEmail;
    }

    public static String getSave() {
        return save;
    }

    public static void setSave(String save) {
        Constants.save = save;
    }

    public static String getNameChange() {
        return nameChange;
    }

    public static void setNameChange(String nameChange) {
        Constants.nameChange = nameChange;
    }

    public static String getNameChangeText() {
        return nameChangeText;
    }

    public static void setNameChangeText(String nameChangeText) {
        Constants.nameChangeText = nameChangeText;
    }

    /* NFC Screens */
    public static String NFCTitle;
    public static String multiCardScreenTitle;
    public static String messageText;
    // check transaction
    public static String CheckTransactionTitle;
    public static String btndoneText;
    public static String btnDownload;
    // NFC card topup
    public static String cardTopupTitle;
    public static String cardNameText;
    public static String cardNumberText;
    public static String amountText;
    public static String refillText;
    public static String toNfcCard;
    public static String transactionFeeText;
    public static String btnConfirm;
    public static String btnCancel;
    public static String transactionStatusText;
    public static String amountAddedtoNFCText;
    public static String refText;
    // Unlink Card
    public static String unLikCardTitle;
    // Link Card
    public static String likCardTitle;
    public static String changeAliasTitle;

    // Favorites
    public static String favoriteTitle;
    public static String transfertoPhone;
    public static String transferToBank;
    public static String prepaid;
    public static String postPaid;
    public static String btneditName;
    public static String btndelete;
    public static String transfer;

    public static int wallet_number = 0, bank_number = 1,
            wallet_Bank_number = 2;
    public static int mobile_banking = 2, mobile_wallet = 1,
            mobile_source_pocket, mobile_wallet_bank = 3;

    public static int getMobile_source_pocket() {
        return mobile_source_pocket;
    }

    public static void setMobile_source_pocket(int mobile_source_pocket) {
        Constants.mobile_source_pocket = mobile_source_pocket;
    }

    /*
     * Application Error messages, Loading titles
     */
    public static String app_title;
    public static String server_error_message;

    /*
     * For LandingScreen Services visible/invisible. Set '0' for visible, '8' to
     * invisible.
     */
    public static int registrationService;

    /*
     * For LoginScreen
     */
    public static int pinLength;
    public static int minMDNLength;
    public static int maxMDNLength;

    /*
     * For AccountType selection Emoney == 1 || Banking == 2 || Both == 0
     */
    public static int defaultAccountType;

    /*
     * For HomeScreen Services visible/invisible. Set '0' for visible, '8' to
     * invisible.
     */
    public static int billPayment_Service;

    /*
     * For CashOutScreen Services visible/invisible. Set '0' for visible, '8' to
     * invisible.
     */
    public static int from_ATM_cashout;

    /*
     * Transfer visible/invisible. Set '0' for visible, '8' to invisible.
     */
    public static int transfer_nibss_Service;

    /*
     * TransferToOthers Services visible/invisible. Set '0' for visible, '8' to
     * invisible.
     */
    public static int transfer_WtoW_service;
    public static int transfer_WtoB_service;
    public static int transfer_BtoW_service;
    public static int transfer_BtoB_service;

    /*
     * Buy->Paymerchant visible/invisible. Set '0' for visible, '8' to
     * invisible.
     */
    public static int buy_paymarchant_service;

    /*
     * For Default cash selection. 0=wallet,1=bank,2=both(Ask user to select)
     */
    public static int billpayment_home;
    public static int buy_purchase_cash_service;
    public static int buy_airtime_cash_service;
    public static int accounts_checkBalence_walletservice;
    public static int accounts_checkHistory_walletservice;
    public static int transfer_nibss_cash_service;

    /*
     * Application Error messages, Loading titles
     */
    public static String build_date;
    public static String about_us;

    public static int getDefaultAccountType() {
        return defaultAccountType;
    }

    public static void setDefaultAccountType(int defaultAccountType) {
        Constants.defaultAccountType = defaultAccountType;
    }

    public static String getApp_title() {
        return app_title;
    }

    public static void setApp_title(String app_title) {
        Constants.app_title = app_title;
    }

    public static String getServer_error_message() {
        return server_error_message;
    }

    public static void setServer_error_message(String server_error_message) {
        Constants.server_error_message = server_error_message;
    }

    public static int getRegistrationService() {
        return registrationService;
    }

    public static void setRegistrationService(int registrationService) {
        Constants.registrationService = registrationService;
    }

    public static int getPinLength() {
        return pinLength;
    }

    public static void setPinLength(int pinLength) {
        Constants.pinLength = pinLength;
    }

    public static int getMinMDNLength() {
        return minMDNLength;
    }

    public static void setMinMDNLength(int minMDNLength) {
        Constants.minMDNLength = minMDNLength;
    }

    public static int getMaxMDNLength() {
        return maxMDNLength;
    }

    public static void setMaxMDNLength(int maxMDNLength) {
        Constants.maxMDNLength = maxMDNLength;
    }

    public static int getBillPayment_Service() {
        return billPayment_Service;
    }

    public static void setBillPayment_Service(int billPayment_Service) {
        Constants.billPayment_Service = billPayment_Service;
    }

    public static int getFrom_ATM_cashout() {
        return from_ATM_cashout;
    }

    public static void setFrom_ATM_cashout(int from_ATM_cashout) {
        Constants.from_ATM_cashout = from_ATM_cashout;
    }

    public static int getTransfer_nibss_Service() {
        return transfer_nibss_Service;
    }

    public static void setTransfer_nibss_Service(int transfer_nibss_Service) {
        Constants.transfer_nibss_Service = transfer_nibss_Service;
    }

    public static int getTransfer_WtoW_service() {
        return transfer_WtoW_service;
    }

    public static void setTransfer_WtoW_service(int transfer_WtoW_service) {
        Constants.transfer_WtoW_service = transfer_WtoW_service;
    }

    public static int getTransfer_WtoB_service() {
        return transfer_WtoB_service;
    }

    public static void setTransfer_WtoB_service(int transfer_WtoB_service) {
        Constants.transfer_WtoB_service = transfer_WtoB_service;
    }

    public static int getTransfer_BtoW_service() {
        return transfer_BtoW_service;
    }

    public static void setTransfer_BtoW_service(int transfer_BtoW_service) {
        Constants.transfer_BtoW_service = transfer_BtoW_service;
    }

    public static int getTransfer_BtoB_service() {
        return transfer_BtoB_service;
    }

    public static void setTransfer_BtoB_service(int transfer_BtoB_service) {
        Constants.transfer_BtoB_service = transfer_BtoB_service;
    }

    public static int getTransfer_nibss_cash_service() {
        return transfer_nibss_cash_service;
    }

    public static void setTransfer_nibss_cash_service(
            int transfer_nibss_cash_service) {
        Constants.transfer_nibss_cash_service = transfer_nibss_cash_service;
    }

    public static int getBuy_paymarchant_service() {
        return buy_paymarchant_service;
    }

    public static void setBuy_paymarchant_service(int buy_paymarchant_service) {
        Constants.buy_paymarchant_service = buy_paymarchant_service;
    }

    public static int getBillpayment_home() {
        return billpayment_home;
    }

    public static void setBillpayment_home(int billpayment_home) {
        Constants.billpayment_home = billpayment_home;
    }

    public static int getBuy_purchase_cash_service() {
        return buy_purchase_cash_service;
    }

    public static void setBuy_purchase_cash_service(
            int buy_purchase_cash_service) {
        Constants.buy_purchase_cash_service = buy_purchase_cash_service;
    }

    public static int getBuy_airtime_cash_service() {
        return buy_airtime_cash_service;
    }

    public static void setBuy_airtime_cash_service(int buy_airtime_cash_service) {
        Constants.buy_airtime_cash_service = buy_airtime_cash_service;
    }

    public static int getAccounts_checkBalence_walletservice() {
        return accounts_checkBalence_walletservice;
    }

    public static void setAccounts_checkBalence_walletservice(
            int accounts_checkBalence_walletservice) {
        Constants.accounts_checkBalence_walletservice = accounts_checkBalence_walletservice;
    }

    public static int getAccounts_checkHistory_walletservice() {
        return accounts_checkHistory_walletservice;
    }

    public static void setAccounts_checkHistory_walletservice(
            int accounts_checkHistory_walletservice) {
        Constants.accounts_checkHistory_walletservice = accounts_checkHistory_walletservice;
    }

    public static String getBuild_date() {
        return build_date;
    }

    public static void setBuild_date(String build_date) {
        Constants.build_date = build_date;
    }

    public static String getAbout_us() {
        return about_us;
    }

    public static void setAbout_us(String about_us) {
        Constants.about_us = about_us;
    }

    public static ArrayAdapter<String> dataAdapter;

    public static ArrayAdapter<String> getDataAdapter() {
        return dataAdapter;
    }

    public static void setDataAdapter(ArrayAdapter<String> dataAdapter) {
        Constants.dataAdapter = dataAdapter;
    }

    public static List<String> list = new ArrayList<String>();

    public static List<String> getList() {
        return list;
    }

    public static void setList(List<String> list) {
        Constants.list = list;
    }

    public static String IS_STATIC = "NO";
    public static String SOURCE_MDN_NAME = "";
    public static String SOURCE_NIC_NAME = "";
    public static String SOURCE_MDN_PIN = "1234";
    public static String SOURCE_INSTITUTION_ID = "HUB";
    //	public static String SOURCE_INSTITUTION_ID = "";
    // public static String DESTINATION_MDN_NAME = "";
    public static String RESPONSE_CODE = "00";
    public static int DIGITS_OTP = 4;

    public static int MFA_CONNECTION_TIMEOUT = 90000;
    public static int CONNECTION_TIMEOUT = 30000;
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
    public static final String SERVICE_BILLPAYMENT = "Payment";

    public static String TRANSACTION_CASHOUT_AT_ATM_INQUIRY = "CashOutAtATMInquiry";
    public static String TRANSACTION_CASHOUT_AT_ATM = "CashOutAtATM";
    // transactions
    public static String PARAMTER_MFA_TRANSACTION = "mfaTransaction";
    public static String TRANSACTION_MFA_TRANSACTION = "Inquiry";
    public static String TRANSACTION_MFA_TRANSACTION_CONFIRM = "Confirm";

    //JSON Data
    public static String TRANSACTION_GETTHIRDPARTYDATA = "GetThirdPartyData";
    public static String TRANSACTION_SMARTFREN_DATA = "category.smartfrenData";
    public static String TRANSACTION_PROVICE_DATA = "category.province";
    public static String TRANSACTION_WORK_DATA = "category.work";
    public static String TRANSACTION_REFERRAL_DATA = "category.product_referral";
    public static String TRANSACTION_GETTHIRDPARTYLCOATION = "GetThirdPartyLocation";
    public static String TRANSACTION_GETBANKCODELIST = "category.bankCodes";
    public static String TRANSACTION_PAYMENTS="category.payments";
    public static String TRANSACTION_PURCHASES="category.purchase";
    public static String TRANSACTION_CONTACTUS_FILE="category.ContactUsfile";
    public static String CONSTANT_GENERATE_FAVORITE_JSON="GenerateFavoriteJSON";
    public static String PARAMETER_FAVORITE_ID="favoriteCategoryID";
    /* UpdateProfile */
    public static final String TRANSACTION_UPDATE_PROFILE = "UpdateProfile";
    public static final String PARAMETER_PROFILE_IMAGE_STRING = "profileImageString";
    // QR Payment transactions
    public static final String TRANSACTION_QR_BILLPAYMENT_INQUIRY = "QRPaymentInquiry";
    public static final String TRANSACTION_QR_BILLPAYMENT = "QRPayment";
    public static final String TRANSACTION_QR_PAYMENT = "QRPayment";
    public static final String TRANSACTION_USER_APIKEY = "GetUserAPIKey";

    public static final String TRANSACTION_FLASHIZ_INQUIRY = "FlashizInquiry";
    public static final String TRANSACTION_FLASHIZ = "Flashiz";
    public static final String TRANSACTION_CASHOUT_AGENT_INQUIRY = "CashOutInquiry";
    public static final String TRANSACTION_CASHOUT_AGENT = "CashOut";
    public static final String TRANSACTION_GENERATE_OTP = "GenerateOTP";
    public static final String TRANSACTION_VALIDATE_OTP = "ValidateOTP";
    public static final String TRANSACTION_NFC_CARDLINK = "NFCCardLink";
    public static final String TRANSACTION_NFC_CARD_UNLINK = "NFCCardUnlink";
    public static final String TRANSACTION_NFC_MODIFY_ALIAS = "ModifyNFCCardAlias";
    public static final String TRANSACTION_ADD_FAVORITE = "AddFavorite";
    public static final String TRANSACTION_EDIT_FAVORITE = "EditFavorite";
    public static final String TRANSACTION_DELETE_FAVORITE = "DeleteFavorite";
    public static final String TRANSACTION_CHANGE_NICKNAME = "ChangeNickname";
    public static final String TRANSACTION_FORGOTPIN_INQUIRY = "ForgotPinInquiry";
    public static final String TRANSACTION_FORGOTPIN = "ForgotPin";
    public static final String TRANSACTION_CHANGE_EMAIL = "ChangeEmail";
    public static final String TRANSACTION_FORGOT_PASSWORD = "ChangeEmail";
    public static final String TRANSACTION_GET_FAVORITES = "GenerateFavoriteJSON";
    public static final String TRANSACTION_NFC_CARD_ALIAS_MODIFY = "ModifyNFCCardAlias";
    public static final String TRANSACTION_NFC_CHECKBALANCE = "NFCCardBalance";
    public static final String TRANSACTION_NFC_HISTORY = "History";
    public static final String TRANSACTION_REGISTRATION = "RegistrationWithActivationForHub";
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
    public static final String TRANSACTION_OTHER_MDN = "ChangeOtherMDN";
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
    public static final String UPDATE_PHOTO = "UpdateProfile";
    public static final String TRANSACTION_UANGKU = "TransferToUangku";
    public static final String TRANSACTION_CASHOUT = "CashOut";
    public static final String TRANSACTION_SUBSCRIBERREGISTRATION = "SubscriberRegistration";
    public static final String TRANSACTION_CASHIN = "CashIn";
    public static final String TRANSACTION_INTERBANK_TRANSFER_INQUIRY = "InterBankTransferInquiry";
    public static final String TRANSACTION_INTERBANK_TRANSFER = "InterBankTransfer";
    public static final String TRANSACTION_KYC_STATUS = "SubscriberStatus";
    public static final String TRANSACTION_PROMO_IMAGE = "GetPromoImage";
    public static final String CHECKBALANCE_HISTORY = "CheckBalanceWithHistory";
    public static final String GETSECRET_QUESTION = "GetSecretQuestion";
    public static final String RESET_PINBY_EMAIL = "ResetPinByEmail";
    public static final String VALIDATE_SQA = "ValidateSQA";
    public static final String RESET_PINBYSQ_INQUIRY = "ResetPinBySQInquiry";
    public static final String RESET_PINBYSQ = "ResetPinBySQ";
    public static final String RESET_PINBY_EMAIL_INQUIRY = "ResetPinByEmailInquiry";
    public static final String GETSECRET_PIN_AND_SECURITY_QUESTION_ANSWER = "ResetPinAndSQA";
    public static final String VIEW_PROFILE = "ViewProfile";
    public static final String TRANSACTION_RESENDMFAOTP="ResendMFAOTP";
    public static final String TRANSACTION_RESENDMFAOTPNO="ResendMFAOTPNoPIN";
    public static final String MDN_VALIDATION_FORGOTPIN = "MDNvalidationforForgotPIN";

    public static final String TRANSACTION_BUY_VIPMEMBERSHIP = "EnableMemberShipInquiry";
    public static final String TRANSACTION_BUY_VIPMEMBERSHIPCONFIRMATION = "EnableMemberShip";

    public static final String TRANSACTION_MSP_ID = "2";

    // donate
    public static final String TRANSACTION_DONATE_INQUIRY = "DonationInquiry";
    public static final String TRANSACTION_DONATE = "Donation";

    // webapi specific
    public static final String TRANSACTION_LOGIN = "Login";
    public static final String TRANSACTION_LOGOUT = "Logout";
    public static final String TRANSACTION_BANK_CONFIRMATION = "InterBankTransfer";
    public static final String TRANSACTION_BANK_INQUERY = "InterBankTransferInquiry";
    public static final String TRANSACTION_UANGKU_INQUERY = "TransferToUangkuInquiry";
    public static final String TRANSACTION_TRANSFER_INQUIRY = "TransferInquiry";
    public static final String TRANSACTION_CASHIN_INQUIRY = "CashInInquiry";
    public static final String TRANSACTION_CASHOUT_INQUIRY = "CashOutInquiry";
    public static final String TRANSACTION_NFC_POCKET_TOPUP_INQUIRY = "NFCPocketTopupInquiry";
    public static final String TRANSACTION_NFC_POCKET_TOPUP = "NFCPocketTopup";
    public static final String TRANSACTION_NFC_MULTICARD = "NFCCardBalance";
    public  static final String TRANSACTION_REFFERAL_INQUERY="ProductReferral";
    // Buy Section transations
    public static final String TRANSACTION_AIRTIME_PURCHASE_INQUIRY = "AirtimePurchaseInquiry";
    public static final String TRANSACTION_AIRTIME_PURCHASE = "AirtimePurchase";

    public static final String TRANSACTION_KTPVALIDATION = "SubscriberKTPValidation";
    public static final String TRANSACTION_CLOSEACCOUNTINQUERY = "SubscriberClosingInquiry";
    public static final String TRANSACTION_CLOSEACCOUNT = "SubscriberClosing";


    // Bill payment transactions

    public static final String TRANSACTION_BILLPAYMENT_INQUIRY = "BillPayInquiry";
    public static final String TRANSACTION_PREPAID_BILLPAYMENT_INQUIRY = "PrepaidBillPayInquiry";
    public static final String TRANSACTION_BILLPAYMENT = "BillPay";

    // parameters
    public static final String TRANSACTION_ISSIMASPAYACTIVITY = "isSimaspayActivity";
    public static final String TRANSACTION_INTIATED_MDN = "initiatedMDN";
    public static final String TRANSACTION_INTIATED_TENANTID = "initiatedTenantId";
    public static final String PARAMETER_PROFILE_ID = "profileID";
    public static final String PARAMETER_INSTITUTION_ID = "institutionID";
    public static final String PARAMETER_MSPID = "mspID";
    public static final String PARAMETER_AUTHENTICATION_KEY = "authenticationKey";
    public static final String CONSTANT_AUTHENTICATION_KEY = "f";
    public static final String CONSTANT_DENOMCODE = "2";
    public static final String PARAMETER_MPIN = "mpin";
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
    public static final String PARAMETER_FULLNAME = "fullName";
    public static final String PARAMETER_NON_FINANCIAL_ENQUIRY = "mfaTransaction";

    public static final String PARAMETER_MFA_OTP = "mfaOtp";
    public static final String PARAMETER_CHANNEL_ID = "channelID";
    public static final String PARAMETER_IMG_STRING = "profileImageString";
    public static final String PARAMETER_CATEGORY = "category";
    public static final String PARAMETER_STATE = "state";
    public static final String PARAMETER_REGIONNAME = "regionName";
    public static final String PARAMETER_CITY = "city";
    public static final String PARAMETER_CONFIRM_PIN = "confirmPIN";
    public static final String PARAMETER_SOURCE_PIN = "sourcePIN";
    public static final String PARAMETER_FAV_CAT_ID = "favoriteCategoryID";
    public static final String PARAMETER_FAV_LABEL = "favoriteLabel";
    public static final String PARAMETER_FAV_VALUE = "favoriteValue";
    public static final String PARAMETER_FAV_CODE = "favoriteCode";
    public static final String PARAMETER_NICKNAME = "nickname";
    public static final String PARAMETER_NEW_EMAIL = "newEmail";
    public static final String PARAMETER_CONFIRM_EMAIL = "confirmEmail";
    public static final String PARAMETER_TRANSACTIONID = "transactionId";
    public static final String PARAMETER_KTPDOCUMENT = "ktpDocument";
    public static final String PARAMETER_DOMESTIC_IDENTITY = "domesticIdentity";
    public static final String PARAMETER_DEST_MDN = "destMDN";
    public static final String PARAMETER_AMOUNT = "amount";
    public static final String PARAMETER_SCTLID="sctlId";
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
    public static final String PARAMETER_SCTL = "sctlId";
    public static final String PARAMETER_TO_DATE = "toDate";
    public static final String PARAMETER_FORGOTMPIN = "forgotMPIN";
    public static final String PARAMETER_CARDPAN_SUFFIX = "cardPANSuffix";
    public static final String PARAMETER_SRC_MESSAGE = "sourceMessage";
    public static final String PARAMETER_DEST_POCKET_CODE = "destPocketCode";
    public static final String PARAMETER_DESCRIPTION = "description";
    public static final String PARAMETER_TRANSFER_ID = "transferID";
    public static final String PARAMETER_E_MSP_ID = "eMspId";
    public static final String PARAMETER_E_MDN = "eMdn";
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
    public static final String PARAMETER_USERAPIKEY = "userApiKey";

    public static final String PARAMETER_DOB = "dob";
    public static final String PARAMETER_SALT = "salt";
    public static final String PARAMETER_AUTHENTICATION_STRING = "authenticationString";
    public static final String PARAMETER_ACTIVATION_NEWPIN = "activationNewPin";
    public static final String PARAMETER_ACTIVATION_CONFIRMPIN = "activationConfirmPin";
    public static final String PARAMETER_OTHER_NUMBER = "otherMDN";
    public static final String PARAMETER_COMPANY_ID = "companyID";

    public static final String PARAMETER_APPTYPE = "apptype";
    public static final String CONSTANTS_APPTYPE = "";
    public static final String PARAMETER_APPVERSION = "appversion";
    public static final String PARAMETER_APPOS = "appos";
    public static final String PARAMETER_DEVICE_MODEL = "deviceModel";
    public static final String PARAMETER_OS_VERSION = "osVersion";
    public static final String PARAMETER_SIMASPAYACTIVITY = "isSimaspayActivity";
    public static final String PARAMETER_VERSION = "version";
    public static final String PARAMETER_KTPID = "ktpId";
    public static final String PARAMETER_PRODUCT = "productDesired";
    public static final String PARAMETER_OTHERS="others";

    public static final String IS_SIMASPAYACTIVITY = "true";
    public static final String PARAMETER_CONFIRM_MPIN = "confirmPIN";

    //SourcePocketCode
    public static final String PARAMETER_TYPEUSER = "akun";
    public static final String PARAMETER_PROFPICSTRING = "profileImageString";
    public static final String PARAMETER_AGENTTYPE = "AgentUsing";
    public static final String PARAMETER_USERTYPE = "userType";
    public static final String PARAMETER_USES_AS = "useas";
    public static final String PARAMETER_ACCOUNTNUMBER = "accountnumber";
    public static final String PARAMETER_PHONENUMBER = "mobileNumber";
    public static final String CONSTANT_BANK_USER = "bank";
    public static final String CONSTANT_EMONEYNONKYC_USER = "nonkyc";
    public static final String CONSTANT_EMONEYKYC_USER = "kyc";
    public static final String CONSTANT_BOTH_USER = "both";
    public static final String CONSTANT_EMONEY_REGULER = "E-money Reguler";
    public static final String CONSTANT_EMONEY_PLUS = "E-money Plus";
    public static final int CONSTANT_EMONEY_INT = 3;
    public static final int CONSTANT_LAKUPANDAI_INT = 2;
    public static final int CONSTANT_BANKSINARMAS_INT = 1;
    public static final int CONSTANT_BANK_INT = 0;



    public static final String PARAMETER_KTPRW = "ktpRW";
    public static final String PARAMETER_KTPLINE1 = "ktpLine1";
    public static final String PARAMETER_KTPRT = "ktpRT";
    public static final String PARAMETER_KTPSTATE = "ktpState";
    public static final String PARAMETER_KTPSUBSTATE = "ktpSubState";
    public static final String PARAMETER_KTPREGIONNAME = "ktpRegionName";
    public static final String PARAMETER_KTPZIPCODE = "ktpZipCode";
    public static final String PARAMETER_KTPCITY = "ktpCity";
    public static final String PARAMETER_KTPLIFETIME = "ktpLifetime";
    public static final String PARAMETER_KTPCOUNTRY = "ktpCountry";
    public static final String PARAMETER_DIFF_COUNTRY = "country";
    public static final String PARAMETER_DIFF_STATE = "state";
    public static final String PARAMETER_DIFF_SUB_STATE = "subState";
    public static final String PARAMETER_DIFF_CITY = "city";
    public static final String PARAMETER_DIFF_ZIPCODE = "zipCode";
    public static final String PARAMETER_DIFF_REGIONNAME="regionName";
    public static final String PARAMETER_DIFF_RT="RT";
    public static final String PARAMETER_DIFF_RW="RW";
    public static final String PARAMETER_MOTHERSMAIDENNAME="subMothersMaidenName";
    public static final String PARAMETER_DIFF_LINE1="addressLine1";
    public static final String PARAMETER_KTPVALIDUNTIL="ktpValidUntil";
    public static final String PARAMETER_SOURCEOFFUNDS="sourceOfFunds";
    public static final String PARAMETER_SUPPORTDOCUMENT="supportingDocument";
    public static final String PARAMETER_SUBSCRIBER_DOCUMENT="subscriberFormDocument";
    public static final String PARAMETER_OPENINGACCOUNT="goalOfOpeningAccount";
    public static final String PARAMETER_INCOME="income";
    public static final String PARAMETER_WORK="work";
    public static final String PARAMETER_OTHER_WORK="otherwork";



    public static final String PARAMETER_DEST_BANK_CODE = "destBankCode";
    public static final String PARAMETER_DEST_ACCOUNT_NO = "destAccountNo";

    // Parameters for Bill payement
    public static final String PARAMETER_BILLER_CODE = "billerCode";
    public static final String PARAMETER_BILL_NO = "billNo";
    public static final String PARAMETER_DENOM_CODE = "denomCode";
    public static final String PARAMETER_NOMINAL_PRICE = "nominalAmount";
    // Bill payment transactions
    public static final String PARAMETER_PAYMENTMODE = "paymentMode";
    public static final String PARAMETER_PAYMENTMODE_POSTPAID_VALUE = "HubZeroAmount";
    public static final String PARAMETER_PAYMENTMODE_PREPAID_VALUE = "HubFullAmount";

    // Parameters for QRBill payement
    public static final String PARAMETER_PAYMENT_MODE = "paymentMode";
    public static final String PARAMETER_USER_API_KEY = "userAPIKey";
    public static final String PARAMETER_MERCHANT_NAME = "merchantData";
    public static final String PARAMETER_LOYALITYNAME = "loyalityName";
    public static final String PARAMETER_NUMBEROFCOUPUNS = "numberOfCoupons";
    public static final String PARAMETER_DISCOUNTED_AMOUNT = "discountAmount";
    public static final String PARAMETER_DISCOUNTED_TYPE = "discountType";
    public static final String PARAMETER_REDEEMAMOUNT = "amountRedeemed";
    public static final String PARAMETER_REDEEMPOINTS = "pointsRedeemed";
    public static final String PARAMETER_TIPAMOUNT = "tippingAmount";
    public static final String PARAMETER_ONBEHALFOFMDN = "onBehalfOfMDN";

    // LupaPIN
    public static final String PARAMETER_SECURITY_QUESTION = "securityQuestion";
    public static final String PARAMETER_SECURITY_ANSWER = "securityAnswer";

    // rajkumer added for DestBankAccount
    public static final String PARAMETER_DEST_BankAccount = "destBankAccount";

    public static final String MESSAGE_MOBILE_TRANSFER = "Mobile Transfer";
    public static final String DUMMY_BANK_ID = "Not Yet";
    public static final String CONSTANT_BANK_ID = "";
    public static final String CONSTANT_VALUE_ZERO = "0";
    public static final String CONSTANT_VALUE_TRUE = "true";
    public static final String SERVICE_PROVIDER_NAME = "serviceProviderName";
    public static final String CONSTANT_VALUE_FALSE = "false";
    public static final String CONSTANT_CHANNEL_ID = "7";
    public static final String CONSTANT_INSTITUTION_ID = "";
    public static final String CONSTANT_INSTITUTION_ID_SIMASPAY = "simaspay";
    public static final String CONSTANT_MSBID = "1";
    //	public static final String CONSTANT_INSTITUTION_ID = "el";
//	public static final String CONSTANT_MSBID = "2";
    public static final String CONSTANT_EMONEY = "E Money";
    public static final String CONSTANT_BANK = "Bank";
    public static final String CONSTANT_AIRTIME = "Airtime";
    public static final int COLOUR_GRAY = 0xB5B5B5;
    public static final int COLOUR_SLATEGRAY = 0x9FB6CD;
    public static final String XML_MESSAGE = "message";
    public static final String XML_PROFILEIMAGE = "profileImageString";
    public static final String XML_ADITIONAL_INFO = "AdditionalInfo";
    public static final String XML_NICK_NAME = "nickname";
    public static final String XML_FIRST_NAME = "firstName";
    public static final String XML_RESPONSE_CODE = "responseCode";
    public static final String XML_RESPONSE_MESSAGE = "ResponseMessage";
    public static final String XML_LAST_NAME = "lastName";
    public static final String XML_REC_KYC_NAME = "ReceiverAccountName";
    public static final String XML_DOWNLOAD_URL = "downloadURL";
    public static final String XML_NFC_CARD_ALIAS_NAME = "CardAlias";
    public static final String XML_PUBLIC_MODULUS = "PublicKeyModulus";
    public static final String XML_PUBLIC_EXPONENT = "PublicKeyExponent";
    public static final String XML_SUCCESS = "Success";
    public static final String XML_DEST_CUST_NAME = "destinationName";
    public static final String XML_BILL_DATE = "billDate";
    public static final String XML_DEST_MDN = "destinationMDN";
    public static final String XML_DEST_BANK = "destinationBank";
    public static final String XML_BANK_ID = "bankID";
    public static final String XML_ACCOUNT_NUMBER = "destinationAccountNumber";
    public static final String XML_AMOUNT_TRANSFER = "creditamt";
    public static final String XML_TRANSACTION_TIME = "transactionTime";
    public static final String XML_REFID = "refID";
    public static final String XML_TRANSFERID = "transferID";
    public static final String XML_SCTL = "sctlID";
    public static final String XML_ID_NUMBER = "IDNumber";
    public static final String XML_MFAMODE = "mfaMode";
    public static final String XML_QAEXIST = "SQExist";
    public static final String XML_PARENT_TXNID = "parentTxnID";
    public static final String XML_INPUT = "input";
    public static final String XML_NAME = "Name";
    public static final String XML_VALUE = "value";
    public static final String XML_AMOUNT = "amount";
    public static final String XML_BILL_DETAILS = "billDetails";
    public static final String XML_KEY = "key";
    public static final String XML_SALT = "salt";
    public static final String XML_AUTHENTICATION = "authentication";
    public static final String XML_TRANSACTION_CHARGES = "charges";
    public static final String XML_DEBIT_AMOUNT = "debitamt";
    public static final String XML_CREDIT_AMOUNT = "creditamt";
    public static final String XML_APPUPDATEURL = "url";
    public static final String XML_APPURL = "AppURL";
    public static final String XML_REGISTRATION_MEDIUM = "RegistrationMedium";
    public static final String XML_RESET_PIN_REQUEST = "ResetPinRequested";
    public static final String XML_STATUS = "status";
    public static final String XML_IS_UNREGISTER = "IsUnregistered";
    public static final String XML_IS_ALREADY_ACTIVATED = "IsAlreadyActivated";
    public static final String XML_NON_KYC_ALLOW = "allowedTxns";
    public static final String XML_KYC_LEVEL = "kycLevel";
    public static final String XML_KYC_STATUS = "status";
    public static final String XML_PROFILE_PIC = "profileImage";
    public static final String XML_NONKYC_ALERT = "alertMessage";
    public static final String XML_PROMO_IMAGE_URL = "promoImageURL";
    public static final String XML_TOTAL_RECORDS = "totalTxnCount";
    public static final String XML_MORE_TRANSACTIONS_AVAILABLE = "MoreRecordsAvailable";
    public static final String XML_USER_API_KEY = "userAPIKey";
    public static final String XML_INVOICE_NO = "invoiceNo";
    public static final String XML_NOMINAL_AMOUNT = "nominalamt";
    public static final String XML_PROFILE_ID = "profileID";
    public static final String XML_CURRENTBALANCE = "currentBalance";
    public static final String XML_MAXBALANCE = "maximumBalance";
    public static final String XML_REMAINBALANCE = "remainingBalance";
    public static final String XML_SOURCE_MDN_NAME = "sourceMDN";
    public static final String XML_SQEXSIT = "SQExist";
    public static final String XML_BANKACCOUNT_NUMBER="bankAccountNumber";
    public static final String XML_RESET_PIN_BY_EMAIL = "resetPinByEmail";
    public static final String XML_RESET_PIN_BY_CSR = "resetPinByCSR";
    public static final String XML_EMAIL_VERIFIED = "emailVerified";
    public static final String XML_SECURITY_QUESTION = "securityQuestion";
    public static final String XML_SECURITY_ANSWER = "securityAnswer";
    public static final String XML_PROFILE_IMAGE = "profileImage";
    public static final String XML_DATE_OF_BIRTH = "dateOfBirth";
    public static final String XML_SESSION_TIMEOUT = "sessionTime";
    public static final String XML_MAX_TRAILS = "maxTrails";
    public static final String XML_MSISDN = "msisdn";
    public static final String XML_DATE = "date";
    public static final String XML_TIME = "time";
    // KyC Server
    public static final String XML_REFERENCE_NO = "referenceNo";
    public static final String XML_ERROR_CODE = "errorCode";
    public static final String XML_ISBANK = "isBank";
    public static final String XML_ISEMONEY = "isEmoney";
    public static final String XML_ISKYC = "isKyc";
    public static final String XML_ISLAKUPANDAI = "isLakupandia";
    public static final String XML_CUSTOMERTYPE = "type";
    public static final String XML_PROVINCE = "province";
    public static final String XML_DISTRICT = "district";
    public static final String XML_SUB_DISTRICT = "subDistrict";
    public static final String XML_CITY = "city";
    public static final String XML_MOTHERSMEIDENNAME = "mothersMaidenName";
    public static final String XML_TRANSACTIONID = "transactionId";
    public static final String XML_ADDRESSLINE = "addressLine";
    public static final String XML_RT = "rt";
    public static final String XML_RW = "rw";
    public static final String XML_POSTALCODE = "postalCode";
    public static final String XML_DOB = "dob";
    public static final String XML_BIRTHPLACE = "birthPlace";


    // NOTIFICATIONS
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
//    public static final String POCKET_CODE_EMONEY = "1";
    public static final String OS_ANDROID = "2";
    public static final String POCKET_CODE_BANK = "2";
    public static final String POCKET_CODE_EMONEY = "1";
    public static final int POCKET_INT_EMONEY = 1;
    public static final String POCKET_CODE_BANK_SINARMAS = "6";
    public static final String POCKET_CODE_BANK_SINARMAS_BANKCODE = "153";
    public static final String DISCLOSURE_LEGAL = "Legal Disclosure for Mobile Money Service "
            + "\n" + "I agree to the terms of this service";


    public static final String CONSTANTS_NONE = "NONE";
    public static final String URL_FAQ = "http://banksinarmas.com/id/simobiplus/simaspay-faq";
    public static final String URL_TC = "http://banksinarmas.com/id/simobiplus/simaspay-syarat-dan-ketentuan";
}
