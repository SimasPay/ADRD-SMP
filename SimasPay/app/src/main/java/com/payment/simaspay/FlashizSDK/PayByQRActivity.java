package com.payment.simaspay.FlashizSDK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dimo.PayByQR.PayByQRProperties;
import com.dimo.PayByQR.PayByQRSDK;
import com.dimo.PayByQR.PayByQRSDK.SDKLocale;
import com.dimo.PayByQR.PayByQRSDKListener;
import com.dimo.PayByQR.UserAPIKeyListener;
import com.dimo.PayByQR.model.InvoiceModel;
import com.dimo.PayByQR.model.LoyaltyModel;
import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.receivers.IncomingSMS;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 3/21/2016.
 */
public class PayByQRActivity extends AppCompatActivity implements PayByQRSDKListener, IncomingSMS.AutoReadSMSListener {
    private static final String LOG_TAG = "SimasPay";
    PayByQRSDK payByQRSDK;
    String userApiKey,PayInAppInvoiceID, PayInAppURLCallback;
    String idnumber;
    String otpValue, sctl;
    UserAPIKeyListener userAPIKeyListener;
    String QRBillerCode = "QRFLASHIZ";
    String pin;
    InvoiceModel invoiceModel;
    String invoiceID;
    boolean value = false;
    String rsaKey;
    private EditText edt;
    SharedPreferences settings, settings2, languageSettings;
    String selectedLanguage;
    String confirmationResponse;
    private static AlertDialog dialogBuilder, alertError;
    private AlertDialog.Builder alertbox;
    boolean nextpressedornot;
    SharedPreferences sharedPreferences;
    static boolean isExitActivity = false;
    LinearLayout otplay, otp2lay;
    String inqueryResponse, mfa, otp, parentTxnId, txnId, sourceMDN, stMPIN, stSctl, pinValue;
    String UserApikeyresponse;
    int msgCode;
    WebServiceHttp webServiceHttp;
    String message, transactionTime, responseCode;
    public static final String INTENT_EXTRA_MODULE = "com.mfino.bsim.paybyqr.module";
    public static final String INTENT_EXTRA_INVOICE_ID = "com.mfino.bsim.paybyqr.invoiceID";
    public static final String INTENT_EXTRA_URL_CALLBACK = "com.mfino.bsim.paybyqr.URLCallback";
    private String DIMO_PREF = "com.mfino.bsim.paybyqr.Preference";
    private String DIMO_PREF_USERKEY = "com.mfino.bsim.paybyqr.UserKey";
    public static final String QR_STORE_DB = "com.mfino.bsim.QrStore.db";
    private int module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_by_qr);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        IncomingSMS.setListener(PayByQRActivity.this);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");
        sourceMDN = sharedPreferences.getString("mobileNumber", "");
        userApiKey = sharedPreferences.getString("userApiKey", "");
        if (userApiKey == null || userApiKey.equals("") || userApiKey.length() == 0) {
            new requestUserAPIKeyAsyncTask().execute();
            Log.d("userAPIKEY","userAPIKey is null!");
        } else {
            pin = sharedPreferences.getString("mpin", "");
            Log.d(LOG_TAG, "userApiKey from preferences: " + userApiKey);
            Log.d(LOG_TAG, "pin from preferences: " + pin);
            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

            try {
                pinValue = CryptoService.encryptWithPublicKey(module, exponent,
                        pin.toString().getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            stMPIN = pinValue;

            Log.d(LOG_TAG, "pinValue: " + pinValue);

            payByQRSDK = new PayByQRSDK(PayByQRActivity.this, PayByQRActivity.this);
            //payByQRSDK.setServerURL(PayByQRSDK.ServerURL.SERVER_URL_DEV);
            PayByQRProperties.setServerURLString(Constants.URL_PBQ);
            payByQRSDK.setMinimumTransaction(500);
            payByQRSDK.setIsUsingCustomDialog(false);
            //payByQRSDK.setIsPolling(false);

            if (selectedLanguage.equalsIgnoreCase("ENG")) {
                payByQRSDK.setSDKLocale(SDKLocale.ENGLISH);
            } else {
                payByQRSDK.setSDKLocale(SDKLocale.INDONESIAN);
            }

            int module2 = getIntent().getIntExtra(INTENT_EXTRA_MODULE, PayByQRSDK.MODULE_PAYMENT);
            if (module2 == PayByQRSDK.MODULE_IN_APP) {
                PayInAppInvoiceID = getIntent().getStringExtra(INTENT_EXTRA_INVOICE_ID);
                PayInAppURLCallback = getIntent().getStringExtra(INTENT_EXTRA_URL_CALLBACK);
                payByQRSDK.startSDK(module2, PayInAppInvoiceID, PayInAppURLCallback);
            } else {
                payByQRSDK.startSDK(module2);
            }

            /**
            Bundle extras = getIntent().getExtras();
            int getTypeSDK = extras.getInt(INTENT_EXTRA_MODULE, PayByQRSDK.MODULE_PAYMENT);
            Log.d(LOG_TAG, "getTypeSDK:" + getTypeSDK);
            if (getTypeSDK == PayByQRSDK.MODULE_LOYALTY) {
                PayInAppInvoiceID = getIntent().getStringExtra(INTENT_EXTRA_INVOICE_ID);
                PayInAppURLCallback = getIntent().getStringExtra(INTENT_EXTRA_URL_CALLBACK);
                payByQRSDK.startSDK(PayByQRSDK.MODULE_LOYALTY);
                Log.d(LOG_TAG, "startSDK:" + PayByQRSDK.MODULE_LOYALTY);
            } else {
                payByQRSDK.startSDK(PayByQRSDK.MODULE_PAYMENT);
                Log.d(LOG_TAG, "startSDK:" + PayByQRSDK.MODULE_PAYMENT);
            }
             **/
            Log.e(LOG_TAG, "------start: SDK-------");
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e(LOG_TAG, "------onPause-------");
    }

    @Override
    public void callbackShowDialog(Context context, int i, String s, LoyaltyModel loyaltyModel) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(LOG_TAG, "------onResume-------");
        Cancel();
    }

    void Cancel() {
        callbackSDKClosed();
        Log.e(LOG_TAG, "------cancel: callbackSDKClosed-------");
    }


    @Override
    public void callbackAuthenticationError() {
        finish();
    }


    @Override
    public void callbackEULAStateChanged(boolean arg0) {

    }

    @Override
    public void callbackGenerateUserAPIKey(UserAPIKeyListener listener) {
        listener.setUserAPIKey(userApiKey);
        Log.d(LOG_TAG, "setUSERAPIKey:" + userApiKey);
    }

    @Override
    public boolean callbackInvalidQRCode() {
        return false;
    }

    @Override
    public void callbackLostConnection() {
        finish();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void callbackPayInvoice(InvoiceModel invoiceModel1) {

        invoiceID = invoiceModel1.invoiceID;
        invoiceModel = invoiceModel1;
        Log.d(LOG_TAG, "invoiceID:" + invoiceID + ", invoiceModel:" + invoiceModel);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP) {
            if ((checkCallingOrSelfPermission(android.Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED) && checkCallingOrSelfPermission(Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_SMS, android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.SEND_SMS},
                        109);
            } else {
                new PaymentInquiryAsync().execute();
                Log.e(LOG_TAG, "------paymentInquiry-------");
            }
        } else {
            new PaymentInquiryAsync().execute();
            Log.e(LOG_TAG, "------paymentInquiry-------");
        }

    }

    @Override
    public void callbackSDKClosed() {
        if (value) {
            payByQRSDK.closeSDK();
            Log.e(LOG_TAG, "------payByQRSDK.closeSDK-------");
            Intent intent1 = getIntent();
            setResult(10, intent1);
            finish();
        } else {
            finish();
        }

    }


    @Override
    public Fragment callbackShowEULA() {
        return MyCustomEULA.newInstance();
    }

    @Override
    public boolean callbackTransactionStatus(int arg0, String arg1) {
        if (arg0 == com.dimo.PayByQR.data.Constant.STATUS_CODE_PAYMENT_SUCCESS) {
            finish();
            return true;
        } else {
            finish();
            return false;
        }

    }

    @Override
    public boolean callbackUnknowError() {
        return false;
    }

    @Override
    public void callbackUserHasCancelTransaction() {
        if (payByQRSDK.getModule() == PayByQRSDK.MODULE_IN_APP) {
            Cancel();
        }
    }

    @Override
    public void onReadSMS(String otp) {
        Log.d(LOG_TAG, "otp from SMS: " + otp);
        edt.setText(otp);
        otpValue = otp;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 109) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new PaymentInquiryAsync().execute();
                Log.e(LOG_TAG, "------PaymentInquiryAsync()");
            }
        }
    }

    class PaymentInquiryAsync extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

            try {
                rsaKey = CryptoService.encryptWithPublicKey(module, exponent,
                        pin.getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            //InvoiceModel invoiceModel = (InvoiceModel) params[0];
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            Log.d(LOG_TAG, Constants.PARAMETER_CHANNEL_ID + ": " + Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_QR_BILLPAYMENT_INQUIRY);
            Log.d(LOG_TAG, Constants.PARAMETER_TRANSACTIONNAME + ": " + Constants.TRANSACTION_QR_BILLPAYMENT_INQUIRY);
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN,
                    sharedPreferences.getString("mobileNumber", ""));
            Log.d(LOG_TAG, Constants.PARAMETER_SOURCE_MDN + ": " + sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN,
                    rsaKey);
            Log.d(LOG_TAG, Constants.PARAMETER_SOURCE_PIN + ": " + rsaKey);
            sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
            if (sharedPreferences.getInt("userType", -1) == 0) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BILLPAYMENT);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                Log.d(LOG_TAG, Constants.PARAMETER_SERVICE_NAME + ": " + Constants.SERVICE_BILLPAYMENT);
                Log.d(LOG_TAG, Constants.PARAMETER_SRC_POCKET_CODE + ": " + Constants.POCKET_CODE_BANK);
            } else if (sharedPreferences.getInt("userType", -1) == 1) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BILLPAYMENT);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
                Log.d(LOG_TAG, Constants.PARAMETER_SERVICE_NAME + ": " + Constants.SERVICE_BILLPAYMENT);
                Log.d(LOG_TAG, Constants.PARAMETER_SRC_POCKET_CODE + ": " + Constants.POCKET_CODE_BANK_SINARMAS);
            } else if (sharedPreferences.getInt("userType", -1) == 2) {
                if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_AGENT);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                    Log.d(LOG_TAG, Constants.PARAMETER_SERVICE_NAME + ": " + Constants.SERVICE_AGENT);
                    Log.d(LOG_TAG, Constants.PARAMETER_SRC_POCKET_CODE + ": " + Constants.POCKET_CODE_EMONEY);
                } else {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_AGENT);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    Log.d(LOG_TAG, Constants.PARAMETER_SERVICE_NAME + ": " + Constants.SERVICE_AGENT);
                    Log.d(LOG_TAG, Constants.PARAMETER_SRC_POCKET_CODE + ": " + Constants.POCKET_CODE_BANK);
                }
            } else if (sharedPreferences.getInt("userType", -1) == 3) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BILLPAYMENT);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                Log.d(LOG_TAG, Constants.PARAMETER_SERVICE_NAME + ": " + Constants.SERVICE_BILLPAYMENT);
                Log.d(LOG_TAG, Constants.PARAMETER_SRC_POCKET_CODE + ": " + Constants.POCKET_CODE_EMONEY);
            }

            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID,
                    Constants.CONSTANT_INSTITUTION_ID);
            Log.d(LOG_TAG, Constants.PARAMETER_INSTITUTION_ID + ": " + Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put(Constants.PARAMETER_AMOUNT, invoiceModel.paidAmount + "");
            Log.d(LOG_TAG, Constants.PARAMETER_AMOUNT + ": " + invoiceModel.paidAmount);
            mapContainer.put(Constants.PARAMETER_BILLER_CODE, QRBillerCode);
            Log.d(LOG_TAG, Constants.PARAMETER_BILLER_CODE + ": " + QRBillerCode);
            mapContainer.put(Constants.PARAMETER_PAYMENT_MODE,
                    Constants.TRANSACTION_QR_PAYMENT);
            Log.d(LOG_TAG, Constants.PARAMETER_PAYMENT_MODE + ": " + Constants.TRANSACTION_QR_PAYMENT);
            mapContainer.put(Constants.PARAMETER_MERCHANT_NAME, invoiceModel.merchantName);
            Log.d(LOG_TAG, Constants.PARAMETER_MERCHANT_NAME + ": " + invoiceModel.merchantName);
            mapContainer.put(Constants.PARAMETER_USER_API_KEY,
                    sharedPreferences.getString("userApiKey", "NONE"));
            Log.d(LOG_TAG, Constants.PARAMETER_USER_API_KEY + ": " + userApiKey);
            mapContainer.put(Constants.PARAMETER_BILL_NO, invoiceID);
            Log.d(LOG_TAG, Constants.PARAMETER_BILL_NO + ": " + invoiceID);
            mapContainer.put(Constants.PARAMETER_LOYALITYNAME,
                    invoiceModel.loyaltyProgramName.replace(" ", "+"));
            Log.d(LOG_TAG, Constants.PARAMETER_LOYALITYNAME + ": " + invoiceModel.loyaltyProgramName.replace(" ", "+"));
            mapContainer.put(Constants.PARAMETER_NUMBEROFCOUPUNS, invoiceModel.numberOfCoupons + "");
            Log.d(LOG_TAG, Constants.PARAMETER_NUMBEROFCOUPUNS + ": " + invoiceModel.numberOfCoupons);
            mapContainer.put(Constants.PARAMETER_DISCOUNTED_AMOUNT, invoiceModel.amountOfDiscount + "");
            Log.d(LOG_TAG, Constants.PARAMETER_DISCOUNTED_AMOUNT + ": " + invoiceModel.amountOfDiscount);
            mapContainer.put(Constants.PARAMETER_DISCOUNTED_TYPE, invoiceModel.discountType);
            Log.d(LOG_TAG, Constants.PARAMETER_DISCOUNTED_TYPE + ": " + invoiceModel.discountType);
            mapContainer.put(Constants.PARAMETER_REDEEMAMOUNT, invoiceModel.amountRedeemed + "");
            Log.d(LOG_TAG, Constants.PARAMETER_REDEEMAMOUNT + ": " + invoiceModel.amountRedeemed);
            mapContainer.put(Constants.PARAMETER_REDEEMPOINTS, invoiceModel.pointsRedeemed + "");
            Log.d(LOG_TAG, Constants.PARAMETER_REDEEMPOINTS + ": " + invoiceModel.pointsRedeemed);
            mapContainer.put(Constants.PARAMETER_TIPAMOUNT, invoiceModel.tipAmount + "");
            Log.d(LOG_TAG, Constants.PARAMETER_TIPAMOUNT + ": " + invoiceModel.tipAmount);


            webServiceHttp = new WebServiceHttp(mapContainer, PayByQRActivity.this);
            inqueryResponse = webServiceHttp.getResponseSSLCertificatation();
            /**
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(inqueryResponse==null||inqueryResponse.equals("")){
                        Log.d(LOG_TAG, "inqueryResponse: "+inqueryResponse);
                        payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, getResources().getString(R.string.bahasa_serverNotRespond), true);
                        Cancel();
                        payByQRSDK.closeSDK();
                    }
                }
            }, Constants.CONNECTION_TIMEOUT);
             **/
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (inqueryResponse != null) {

                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseContainer = null;
                try {
                    responseContainer = obj.parse(inqueryResponse);
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 631) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(PayByQRActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(responseContainer.getMsg());
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(PayByQRActivity.this, SecondLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    alertbox.show();
                } else if (!((msgCode == 72) || (msgCode == 2109) || (msgCode == 713))) {
                    if (msgCode == 29) {
                            payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, responseContainer.getMsg(), true);
                    } else {
                            payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, responseContainer.getMsg(), true);
                    }
                } else {
                    try {
                        if (responseContainer.getMfaMode()
                                .equalsIgnoreCase("OTP")) {
                            mfa = responseContainer.getMfaMode();
                        } else {
                            mfa = "None";
                        }

                    } catch (Exception e1) {

                        mfa = "None";
                    }
                    if (mfa.equalsIgnoreCase("OTP")) {
                        //registerReceiver(broadcastReceiver, new IntentFilter("com.send"));
                        parentTxnId = responseContainer
                                .getEncryptedParentTxnId();
                        idnumber = responseContainer.getSctl();
                        txnId = responseContainer
                                .getEncryptedTransferId();
                        stSctl = responseContainer.getSctl();
                        sctl = responseContainer.getSctl();
//                        handler.postDelayed(runnable, Constants.MFA_CONNECTION_TIMEOUT);
                        //TimerCount timerCount = new TimerCount(PayByQRProperties.getSDKContext(), responseContainer.getSctl());
                        //timerCount.SMSAlert("");
                        new requestOTPAsyncTask().execute();
                    } else {
                        otp = otpValue;
                        parentTxnId = responseContainer
                                .getEncryptedParentTxnId();
                        txnId = responseContainer.getEncryptedTransferId();
                        stSctl = responseContainer.getSctl();
                        sctl = responseContainer.getSctl();
                        new ConfirmationAsynTask().execute();
                        //showOTPRequiredDialog();


                    }

                }
            } else {
                    payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, getResources().getString(R.string.bahasa_serverNotRespond), true);
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(inqueryResponse==null||inqueryResponse.equals("")){
                        payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, getResources().getString(R.string.bahasa_serverNotRespond), true);
                        Cancel();
                        payByQRSDK.closeSDK();
                    }
                }
            }, Constants.CONNECTION_TIMEOUT);
        }
    }

    class ConfirmationAsynTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_QR_BILLPAYMENT);
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN,
                    sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN,
                    sharedPreferences.getString("password", ""));
            if (sharedPreferences.getInt("userType", -1) == 0) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BILLPAYMENT);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
            } else if (sharedPreferences.getInt("userType", -1) == 1) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BILLPAYMENT);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
            } else if (sharedPreferences.getInt("userType", -1) == 2) {
                if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_AGENT);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                } else {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_AGENT);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                }
            } else if (sharedPreferences.getInt("userType", -1) == 3) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BILLPAYMENT);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
            }

            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID,
                    Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put(Constants.PARAMETER_AMOUNT, invoiceModel.paidAmount + "");
            mapContainer.put(Constants.PARAMETER_BILLER_CODE, QRBillerCode);
            mapContainer.put(Constants.PARAMETER_PAYMENT_MODE,
                    Constants.TRANSACTION_QR_PAYMENT);
            mapContainer.put(Constants.PARAMETER_MERCHANT_NAME, invoiceModel.merchantName);
            mapContainer.put(Constants.PARAMETER_USER_API_KEY,
                    sharedPreferences.getString("userApiKey", "NONE"));
            mapContainer.put(Constants.PARAMETER_BILL_NO, invoiceID);
            mapContainer.put(Constants.PARAMETER_PARENTTXN_ID, parentTxnId);
            mapContainer.put(Constants.PARAMETER_TRANSFER_ID, txnId);
            mapContainer.put(Constants.PARAMETER_CONFIRMED, "true");
            try {
                if (mfa.equalsIgnoreCase("OTP")) {
                    String module = sharedPreferences.getString("MODULE", "NONE");
                    String exponent = sharedPreferences.getString("EXPONENT", "NONE");
                    try {
                        otp = CryptoService.encryptWithPublicKey(module, exponent,
                                otpValue.getBytes());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mapContainer.put(Constants.PARAMETER_MFA_OTP, otp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mapContainer.put(Constants.PARAMETER_LOYALITYNAME,
                    invoiceModel.loyaltyProgramName.replace(" ", "+"));
            mapContainer.put(Constants.PARAMETER_NUMBEROFCOUPUNS, invoiceModel.numberOfCoupons + "");
            mapContainer.put(Constants.PARAMETER_DISCOUNTED_AMOUNT, invoiceModel.amountOfDiscount + "");
            mapContainer.put(Constants.PARAMETER_DISCOUNTED_TYPE, invoiceModel.discountType);

            mapContainer.put(Constants.PARAMETER_REDEEMAMOUNT, invoiceModel.amountRedeemed + "");
            mapContainer.put(Constants.PARAMETER_REDEEMPOINTS, invoiceModel.pointsRedeemed + "");
            mapContainer.put(Constants.PARAMETER_TIPAMOUNT, invoiceModel.tipAmount + "");

            Log.e("========", "=-=========" + mapContainer.toString());

            webServiceHttp = new WebServiceHttp(mapContainer,
                    PayByQRActivity.this);

            confirmationResponse = webServiceHttp.getResponseSSLCertificatation();
            /**
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(confirmationResponse==null||confirmationResponse.equals("")){
                        Log.d(LOG_TAG, "confirmationResponse: "+confirmationResponse);
                        payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, getResources().getString(R.string.bahasa_serverNotRespond), true);
                        Cancel();
                        payByQRSDK.closeSDK();
                        dialogBuilder.dismiss();
                    }
                }
            }, Constants.CONNECTION_TIMEOUT);
             **/
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("========", "=-=========" + confirmationResponse);
            if (confirmationResponse != null) {
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseContainer = null;
                try {

                    responseContainer = obj.parse(confirmationResponse);
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {

                    e.printStackTrace();
                    msgCode = 0;
                }
                if (!((Integer.parseInt(responseContainer.getMsgCode()) == 2111) || (Integer
                        .parseInt(responseContainer.getMsgCode()) == 715))) {
                        payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, responseContainer.getMsg(), true);
                    dialogBuilder.dismiss();
                } else {
                        payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.STATUS_CODE_PAYMENT_SUCCESS, getString(com.dimo.PayByQR.R.string.text_payment_success), true);
                    dialogBuilder.dismiss();
                }
            } else {
                    payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, getResources().getString(R.string.bahasa_serverNotRespond), true);
                dialogBuilder.dismiss();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(confirmationResponse==null||confirmationResponse.equals("")){
                        payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, getResources().getString(R.string.bahasa_serverNotRespond), true);
                        Cancel();
                        payByQRSDK.closeSDK();
                        dialogBuilder.dismiss();
                    }
                }
            }, Constants.CONNECTION_TIMEOUT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 40) {
            if (resultCode == RESULT_OK) {
                Log.e("=======", "======" + "Nagendra Palepu");
            }
        }
    }

    private void showOTPRequiredDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullParent = null;
        View dialoglayout = inflater.inflate(R.layout.new_otp_dialog, nullParent, false);
        dialogBuilder = new AlertDialog.Builder(PayByQRProperties.getSDKContext(), R.style.MyAlertDialogStyle).create();
        dialogBuilder.setCanceledOnTouchOutside(false);
        dialogBuilder.setTitle("");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(dialoglayout);

        // EditText OTP
        otplay = (LinearLayout) dialoglayout.findViewById(R.id.halaman1);
        otp2lay = (LinearLayout) dialoglayout.findViewById(R.id.halaman2);
        otp2lay.setVisibility(View.GONE);
        TextView manualotp = (TextView) dialoglayout.findViewById(R.id.manualsms_lbl);
        //TextView waitingsms = (TextView) dialoglayout.findViewById(R.id.waitingsms_lbl);
        Button cancel_otp = (Button) dialoglayout.findViewById(R.id.cancel_otp);
        //waitingsms.setText("Menunggu SMS Kode Verifikasi di Nomor " + Html.fromHtml("<b>" + sharedPreferences.getString("mobileNumber", "") + "</b>") + "\n");
        manualotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                otplay.setVisibility(View.GONE);
                otp2lay.setVisibility(View.VISIBLE);
            }
        });
        edt = (EditText) dialoglayout.findViewById(R.id.otp_value);

        Log.d(LOG_TAG, "otpValue : " + edt.getText().toString());

        // Timer
        final TextView timer = (TextView) dialoglayout.findViewById(R.id.otp_timer);
        // 120detik
        final CountDownTimer myTimer = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                timer.setText(
                        f.format(millisUntilFinished / 60000) + ":" + f.format(millisUntilFinished % 60000 / 1000));
            }

            @Override
            public void onFinish() {
                errorOTP();
                timer.setText("00:00");
            }
        };
        myTimer.start();
        cancel_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                settings2 = getSharedPreferences(LOG_TAG, 0);
                settings2.edit().putString("ActivityName", "ExitConfirmationScreen").apply();
                if (myTimer != null) {
                    myTimer.cancel();
                }
                Cancel();
                payByQRSDK.closeSDK();
            }
        });
        final Button ok_otp = (Button) dialoglayout.findViewById(R.id.ok_otp);
        ok_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt.getText().toString() == null || edt.getText().toString().equals("")) {
                    errorOTP();
                } else {
                    if (myTimer != null) {
                        myTimer.cancel();
                    }
                    dialogBuilder.dismiss();
                    settings2 = getSharedPreferences(LOG_TAG, 0);
                    settings2.edit().putString("ActivityName", "ExitConfirmationScreen").apply();
                    isExitActivity = true;
                    otpValue = edt.getText().toString();
                    if (otpValue == null) {
                        otpValue = edt.getText().toString();
                    }

                    if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_BANK_INT) {
                        new ConfirmationAsynTask().execute();
                    } else {
                        new ConfirmationAsynTask().execute();
                    }
                }
            }
        });
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is empty
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    //ok_otp.setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    //ok_otp.setEnabled(true);
                }
                if (edt.getText().length() > 5) {
                    Log.d(LOG_TAG, "otp dialog : " + edt.getText());
                    Log.d(LOG_TAG, "otp dialog length: " + edt.getText().length());
                    otpValue = edt.getText().toString();
                    if (myTimer != null) {
                        myTimer.cancel();
                    }
                    if (otpValue == null) {
                        otpValue = edt.getText().toString();
                    }

                    if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_BANK_INT) {
                        new ConfirmationAsynTask().execute();
                    } else {
                        new ConfirmationAsynTask().execute();
                    }
                    dialogBuilder.dismiss();
                    //new TransferEmoneyConfirmationActivity.TransferConfirmationAsyncTask().execute();

                }

            }
        });
        dialogBuilder.show();
    }

    public void errorOTP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PayByQRProperties.getSDKContext(), R.style.MyAlertDialogStyle);
        builder.setCancelable(false);
        if (selectedLanguage.equalsIgnoreCase("ENG")) {
            builder.setTitle(getResources().getString(R.string.eng_otpfailed));
            builder.setMessage(getResources().getString(R.string.eng_desc_otpfailed)).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            settings2 = getSharedPreferences(LOG_TAG, 0);
                            settings2.edit().putString("ActivityName", "ExitConfirmationScreen").apply();
                            isExitActivity = true;
                            dialog.dismiss();
                            Cancel();
                            payByQRSDK.closeSDK();
                            dialogBuilder.dismiss();
                        }
                    });
        } else {
            builder.setTitle(getResources().getString(R.string.bahasa_otpfailed));
            builder.setMessage(getResources().getString(R.string.bahasa_desc_otpfailed)).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            settings2 = getSharedPreferences(LOG_TAG, 0);
                            settings2.edit().putString("ActivityName", "ExitConfirmationScreen").apply();
                            isExitActivity = true;
                            dialog.dismiss();
                            dialogBuilder.dismiss();
                            Cancel();
                            payByQRSDK.closeSDK();
                        }
                    });
        }
        alertError = builder.create();
        if (!isFinishing()) {
            alertError.show();
        }
    }

    class requestOTPAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("txnName", "ResendMFAOTP");
            mapContainer.put("service", Constants.SERVICE_WALLET);
            mapContainer.put("institutionID", Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put("authenticationKey", "");
            mapContainer.put("sourceMDN", sourceMDN);
            mapContainer.put("sourcePIN", stMPIN);
            mapContainer.put("sctlId", stSctl);
            mapContainer.put("channelID", "7");

            Log.e("-----", "" + mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    PayByQRActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PayByQRProperties.getSDKContext());
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (response != null) {
                Log.e("-------", "=====" + response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseDataContainer = null;
                try {
                    responseDataContainer = obj.parse(response);
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.toString());
                }
                try {
                    if (responseDataContainer != null) {
                        Log.d("test", "not null");
                        if (responseDataContainer.getMsgCode().equals("631")) {
                            alertbox = new AlertDialog.Builder(PayByQRActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    payByQRSDK.closeSDK();
                                    Intent intent = new Intent(PayByQRActivity.this, SecondLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                            alertbox.show();
                        } else if (responseDataContainer.getMsgCode().equals("2171")) {
                            message = responseDataContainer.getMsg();
                            Log.d(LOG_TAG, "message" + message);
                            transactionTime = responseDataContainer.getTransactionTime();
                            Log.d(LOG_TAG, "transactionTime" + transactionTime);
                            responseCode = responseDataContainer.getResponseCode();
                            Log.d(LOG_TAG, "responseCode" + responseCode);
                            Log.d("test", "not null");
                            int msgCode = 0;
                            showOTPRequiredDialog();
                        } else {
                            alertbox = new AlertDialog.Builder(PayByQRActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                    payByQRSDK.closeSDK();
                                }
                            });
                            alertbox.show();
                        }
                    }else{
                        alertbox = new AlertDialog.Builder(PayByQRActivity.this, R.style.MyAlertDialogStyle);
                        alertbox.setMessage(getResources().getString(R.string.bahasa_serverNotRespond));
                        alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, getResources().getString(R.string.bahasa_serverNotRespond), true);
                                Cancel();
                                payByQRSDK.closeSDK();
                            }
                        });
                        alertbox.show();
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }
        }
    }

    class requestUserAPIKeyAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("service", Constants.SERVICE_ACCOUNT);
            mapContainer.put("txnName", Constants.TRANSACTION_USER_APIKEY);
            mapContainer.put("institutionID", Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put("authenticationKey", "");
            mapContainer.put("sourceMDN", sourceMDN);
            mapContainer.put("channelID", "7");

            Log.e("-----", "" + mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    PayByQRActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PayByQRActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (response != null) {
                Log.e("-------", "=====" + response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseDataContainer = null;
                try {
                    responseDataContainer = obj.parse(response);
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.toString());
                }
                try {
                    if (responseDataContainer != null) {
                        Log.d("test", "not null");
                        if (responseDataContainer.getMsgCode().equals("631")) {
                            alertbox = new AlertDialog.Builder(PayByQRActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    payByQRSDK.closeSDK();
                                    Intent intent = new Intent(PayByQRActivity.this, SecondLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                            alertbox.show();
                        } else if (responseDataContainer.getMsgCode().equals("2103")) {
                            message = responseDataContainer.getMsg();
                            if (responseDataContainer.getUserApiKey() != null) {
                                UserApikeyresponse = responseDataContainer.getUserApiKey();
                                userApiKey = UserApikeyresponse;
                                sharedPreferences.edit()
                                        .putString("userApiKey", UserApikeyresponse)
                                        .apply();
                                pin = sharedPreferences.getString("mpin", "");
                                Log.d(LOG_TAG, "userApiKey from preferences: " + userApiKey);
                                Log.d(LOG_TAG, "pin from preferences: " + pin);
                                String module = sharedPreferences.getString("MODULE", "NONE");
                                String exponent = sharedPreferences.getString("EXPONENT", "NONE");

                                try {
                                    pinValue = CryptoService.encryptWithPublicKey(module, exponent,
                                            pin.toString().getBytes());
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                stMPIN = pinValue;

                                Log.d(LOG_TAG, "pinValue: " + pinValue);

                                payByQRSDK = new
                                        PayByQRSDK(PayByQRActivity.this, PayByQRActivity.this);
                                //payByQRSDK.setIsPolling(true);
                                //payByQRSDK.setServerURL(PayByQRSDK.ServerURL.SERVER_URL_DEV);
                                PayByQRProperties.setServerURLString(Constants.URL_PBQ);
                                payByQRSDK.setMinimumTransaction(500);
                                payByQRSDK.setIsUsingCustomDialog(false);
                                //payByQRSDK.setIsPolling(false);

                                if (selectedLanguage.equalsIgnoreCase("ENG")) {
                                    payByQRSDK.setSDKLocale(SDKLocale.ENGLISH);
                                } else {
                                    payByQRSDK.setSDKLocale(SDKLocale.INDONESIAN);
                                }

                                Bundle extras = getIntent().getExtras();
                                int getTypeSDK = extras.getInt(INTENT_EXTRA_MODULE, PayByQRSDK.MODULE_PAYMENT);
                                Log.d(LOG_TAG, "getTypeSDK:" + getTypeSDK);
                                if (getTypeSDK == PayByQRSDK.MODULE_LOYALTY) {
                                    payByQRSDK.startSDK(PayByQRSDK.MODULE_LOYALTY);
                                    Log.d(LOG_TAG, "startSDK:" + PayByQRSDK.MODULE_LOYALTY);
                                } else {
                                    payByQRSDK.startSDK(PayByQRSDK.MODULE_PAYMENT);
                                    Log.d(LOG_TAG, "startSDK:" + PayByQRSDK.MODULE_PAYMENT);
                                }
                                Log.e(LOG_TAG, "------start: SDK-------");
                            } else {
                                alertbox = new AlertDialog.Builder(PayByQRActivity.this, R.style.MyAlertDialogStyle);
                                alertbox.setMessage(responseDataContainer.getMsg());
                                alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        arg0.dismiss();
                                        finish();
                                    }
                                });
                                alertbox.show();
                            }
                        } else {
                            alertbox = new AlertDialog.Builder(PayByQRActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(getResources().getString(R.string.id_authentication_failed));
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                    finish();
                                }
                            });
                            alertbox.show();
                        }
                    }else {
                        alertbox = new AlertDialog.Builder(PayByQRActivity.this, R.style.MyAlertDialogStyle);
                        alertbox.setMessage(getResources().getString(R.string.bahasa_serverNotRespond));
                        alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, getResources().getString(R.string.bahasa_serverNotRespond), true);
                                Cancel();
                                payByQRSDK.closeSDK();
                            }
                        });
                        alertbox.show();
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }
        }
    }


}
