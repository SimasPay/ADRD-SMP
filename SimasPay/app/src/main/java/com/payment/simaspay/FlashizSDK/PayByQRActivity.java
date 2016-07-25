package com.payment.simaspay.FlashizSDK;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.dimo.PayByQR.PayByQRProperties;
import com.dimo.PayByQR.PayByQRSDK;
import com.dimo.PayByQR.PayByQRSDKListener;
import com.dimo.PayByQR.UserAPIKeyListener;
import com.dimo.PayByQR.data.Constant;
import com.dimo.PayByQR.model.InvoiceModel;
import com.dimo.PayByQR.model.LoyaltyModel;
import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.TimerCount;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SessionTimeOutActivity;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 3/21/2016.
 */
public class PayByQRActivity extends AppCompatActivity implements PayByQRSDKListener {

    PayByQRSDK payByQRSDK;

    String userApiKey;

    String idnumber;
    SharedPreferences sharedPreferences;

    String otpValue, sctl;

    UserAPIKeyListener userAPIKeyListener;
    String QRBillerCode = "QRFLASHIZ";

    String pin;

    InvoiceModel invoiceModel;
    String invoiceID;

    boolean value=false;

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("========onpause========", "------Nagendra Palepu");
    }

    @Override
    public void callbackShowDialog(Context context, int i, String s, LoyaltyModel loyaltyModel) {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String body = intent.getExtras().getString("message");

                if (body.contains("Kode OTP Simaspay anda")
                        && body.contains(idnumber)) {
                    otpValue = body
                            .substring(
                                    body.indexOf("Kode OTP Simaspay anda : ")
                                            + new String(
                                            "Kode OTP Simaspay anda : ")
                                            .length(),
                                    body.indexOf(". ")).trim();
                    otp = otpValue;
                    handler.removeCallbacks(runnable);
                    handler1.postDelayed(runnable1, 1000);
                    new ConfirmationAsynTask().execute();


                } else if (body.contains(getResources().getString(R.string.EnglishSmsData))
                        && body.contains(idnumber)) {
                    otpValue = body
                            .substring(
                                    body.indexOf(getResources().getString(R.string.EnglishSmsData))
                                            + new String(
                                            getResources().getString(R.string.EnglishSmsData))
                                            .length(),
                                    body.indexOf(". ")).trim();

                    otp = otpValue;
                    handler.removeCallbacks(runnable);
                    handler1.postDelayed(runnable1, 1000);
                    new ConfirmationAsynTask().execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };*/
    void Cancel() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        callbackSDKClosed();
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getExtras().getString("value").equalsIgnoreCase("0")) {
                    Log.e("=======", "======Nagendra Palepu-------");
                    value=true;
                    Cancel();
                } else if (intent.getExtras().getString("value").equalsIgnoreCase("1")) {
                    otpValue = intent.getExtras().getString("otpValue");
                    new ConfirmationAsynTask().execute();
                } else if (intent.getExtras().getString("value").equalsIgnoreCase("2")) {
                    Utility.TransactionsdisplayDialog("Silakan masukkan kode OTP sebelum batas waktu yang ditentukan.", PayByQRProperties.getSDKContext());
                } else if (intent.getExtras().getString("value").equalsIgnoreCase("3")) {
                    Utility.TransactionsdisplayDialog(intent.getExtras().getString("otpValue"), PayByQRProperties.getSDKContext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Handler handler1 = new Handler();

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Handler handler = new Handler();


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!payByQRSDK.isPolling())
                payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, getResources().getString(R.string.bahasa_serverNotRespond), true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_by_qr);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        userApiKey = sharedPreferences.getString("userApiKey", "");


        payByQRSDK = new
                PayByQRSDK(PayByQRActivity.this, PayByQRActivity.this);
        payByQRSDK.setIsPolling(true);
        payByQRSDK.setServerURL(PayByQRSDK.ServerURL.SERVER_URL_DEV);
        payByQRSDK.setMinimumTransaction(500);
        payByQRSDK.setIsUsingCustomDialog(false);
        payByQRSDK.setIsPolling(false);

        payByQRSDK.startSDK(PayByQRSDK.MODULE_PAYMENT);
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
        if (userApiKey != null) {

            if (userApiKey.equalsIgnoreCase("")) {
                userAPIKeyListener = listener;
                new UserApiKeyAsynTask().execute();
            } else {
                listener.setUserAPIKey(userApiKey);

            }
        } else {
            listener.setUserAPIKey(userApiKey);
        }
    }

    @Override
    public boolean callbackInvalidQRCode() {

        return false;

    }

    @Override
    public void callbackLostConnection() {
        finish();
    }


    @Override
    public void callbackPayInvoice(InvoiceModel invoiceModel1) {

        invoiceID = invoiceModel1.invoiceID;
        invoiceModel = invoiceModel1;

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP) {
            if ((checkCallingOrSelfPermission(android.Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED) && checkCallingOrSelfPermission(Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_SMS, android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.SEND_SMS},
                        109);
            } else {

                pinDialogBox();

            }
        } else {
            pinDialogBox();
        }

    }

    @Override
    public void callbackSDKClosed() {

        if(value){
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            payByQRSDK.closeSDK();
            Intent intent1 = getIntent();
            setResult(10, intent1);
            finish();

        }else {
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

        }
    }


    String UserApikeyresponse;
    int msgCode;
    WebServiceHttp webServiceHttp;

    class UserApiKeyAsynTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_USER_APIKEY);
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN,
                    sharedPreferences.getString("mobileNumber", ""));

            webServiceHttp = new WebServiceHttp(mapContainer, PayByQRActivity.this);

            UserApikeyresponse = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (UserApikeyresponse != null) {
                XMLParser obj = new XMLParser();

                EncryptedResponseDataContainer responseContainer = null;
                try {
                    responseContainer = obj.parse(UserApikeyresponse);
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());

                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 2103) {
                    userApiKey = responseContainer.getUserApiKey();
                    sharedPreferences.edit()
                            .putString("userApiKey",
                                    responseContainer.getUserApiKey())
                            .commit();
                    userApiKey = sharedPreferences.getString("userApiKey", "");
                } else {
                    userApiKey = "";
                }
                userAPIKeyListener.setUserAPIKey(userApiKey);
            } else {
                userAPIKeyListener.setUserAPIKey("");
            }
        }
    }


    String inqueryResponse, mfa, otp, parentTxnId, txnId;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 109) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pinDialogBox();
            } else {

            }
        }
    }

    String rsaKey;

    class PaymentInquiryAsyn extends AsyncTask<Object, Void, Void> {

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
            InvoiceModel invoiceModel = (InvoiceModel) params[0];
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_QR_BILLPAYMENT_INQUIRY);
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN,
                    sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN,
                    rsaKey);
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
            mapContainer.put(Constants.PARAMETER_LOYALITYNAME,
                    invoiceModel.loyaltyProgramName.replace(" ", "+"));
            mapContainer.put(Constants.PARAMETER_NUMBEROFCOUPUNS, invoiceModel.numberOfCoupons + "");
            mapContainer.put(Constants.PARAMETER_DISCOUNTED_AMOUNT, invoiceModel.amountOfDiscount + "");
            mapContainer.put(Constants.PARAMETER_DISCOUNTED_TYPE, invoiceModel.discountType);
            mapContainer.put(Constants.PARAMETER_REDEEMAMOUNT, invoiceModel.amountRedeemed + "");
            mapContainer.put(Constants.PARAMETER_REDEEMPOINTS, invoiceModel.pointsRedeemed + "");
            mapContainer.put(Constants.PARAMETER_TIPAMOUNT, invoiceModel.tipAmount + "");

            webServiceHttp = new WebServiceHttp(mapContainer, PayByQRActivity.this);

            inqueryResponse = webServiceHttp.getResponseSSLCertificatation();

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
                    Intent intent = new Intent(PayByQRActivity.this, SessionTimeOutActivity.class);
                    startActivityForResult(intent, 40);
                } else if (!((msgCode == 72) || (msgCode == 2109) || (msgCode == 713))) {
                    if (msgCode == 29) {
                        if (!payByQRSDK.isPolling())
                            payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, responseContainer.getMsg(), true);
                    } else {
                        if (!payByQRSDK.isPolling())
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
                        registerReceiver(broadcastReceiver, new IntentFilter("com.send"));
                        parentTxnId = responseContainer
                                .getEncryptedParentTxnId();
                        idnumber = responseContainer.getSctl();
                        txnId = responseContainer
                                .getEncryptedTransferId();
//                        handler.postDelayed(runnable, Constants.MFA_CONNECTION_TIMEOUT);
                        TimerCount timerCount = new TimerCount(PayByQRProperties.getSDKContext(), responseContainer.getSctl());
                        timerCount.SMSAlert("");
                    } else {
                        otp = otpValue;
                        parentTxnId = responseContainer
                                .getEncryptedParentTxnId();
                        txnId = responseContainer.getEncryptedTransferId();
                        new ConfirmationAsynTask().execute();
                        sctl = responseContainer.getSctl();

                    }

                }
            } else {
                if (!payByQRSDK.isPolling())
                    payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, getResources().getString(R.string.bahasa_serverNotRespond), true);
            }
        }
    }


    String confirmationResponse;

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
                    if (!payByQRSDK.isPolling())
                        payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, responseContainer.getMsg(), true);
                } else {
                    if (!payByQRSDK.isPolling())
                        payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.STATUS_CODE_PAYMENT_SUCCESS, getString(com.dimo.PayByQR.R.string.text_payment_success), true);
                }
            } else {
                if (!payByQRSDK.isPolling())
                    payByQRSDK.notifyTransaction(com.dimo.PayByQR.data.Constant.ERROR_CODE_PAYMENT_FAILED, getResources().getString(R.string.bahasa_serverNotRespond), true);
            }
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

    Dialog dialogCustomWish;
    TextView txt_1, txt_2, txt_3;
    EditText pinlayEditText;

    void pinDialogBox() {
        Log.e("----------", "----------Nagendra palepu----");
        dialogCustomWish = new Dialog(PayByQRProperties.getSDKContext());
        dialogCustomWish.setCancelable(false);
        dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        dialogCustomWish.setContentView(R.layout.pinlayout);
        txt_1 = (TextView) dialogCustomWish.findViewById(R.id.txt_1);
        txt_2 = (TextView) dialogCustomWish.findViewById(R.id.txt_2);
        txt_3 = (TextView) dialogCustomWish.findViewById(R.id.txt_3);

        txt_3.setText(sharedPreferences.getString("mobileNumber", ""));

        txt_1.setTypeface(Utility.Robot_Regular(PayByQRActivity.this));
        txt_2.setTypeface(Utility.Robot_Regular(PayByQRActivity.this));
        txt_3.setTypeface(Utility.Robot_Regular(PayByQRActivity.this));

        pinlayEditText = (EditText) dialogCustomWish.findViewById(R.id.mpin);
        pinlayEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    dialogCustomWish.dismiss();
                    pin = pinlayEditText.getText().toString();
                    new PaymentInquiryAsyn().execute(invoiceModel);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialogCustomWish.show();
    }
}
