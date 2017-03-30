package com.payment.simaspay.UangkuTransfer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.receivers.IncomingSMS;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.utils.Functions;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;


public class UangkuTransferConfirmationActivity extends AppCompatActivity implements IncomingSMS.AutoReadSMSListener{
    TextView title, heading, name, name_field, number, number_field, amount, amount_field, products, product_field;
    Button cancel, confirmation;
    LinearLayout back;
    SharedPreferences sharedPreferences;
    private EditText edt;
    String otpValue = "";
    private static final String LOG_TAG = "SimasPay";
    String OTP;
    private static AlertDialog dialogBuilder;
    LinearLayout otplay, otp2lay;
    SharedPreferences settings, languageSettings;
    String selectedLanguage;
    ProgressDialog progressDialog;
    int msgCode;
    Context context;
    private AlertDialog.Builder alertbox;
    String sourceMDN, stMPIN, stFullname, stAmount, stMDN, stTransferID, stParentTxnID, stSctl, message, transactionTime, responseCode;
    Functions func;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.commonconfirmation);
        func = new Functions(this);
        func.initiatedToolbar(this);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        IncomingSMS.setListener(UangkuTransferConfirmationActivity.this);

        progressDialog = new ProgressDialog(UangkuTransferConfirmationActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.waitingSms));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));

        title = (TextView) findViewById(R.id.title);
        heading = (TextView) findViewById(R.id.textview);
        name = (TextView) findViewById(R.id.name);
        name_field = (TextView) findViewById(R.id.name_field);
        number = (TextView) findViewById(R.id.number);
        number_field = (TextView) findViewById(R.id.number_field);
        amount = (TextView) findViewById(R.id.amount);
        amount_field = (TextView) findViewById(R.id.amount_field);
        products = (TextView) findViewById(R.id.products);
        product_field = (TextView) findViewById(R.id.other_products);

        product_field.setVisibility(View.VISIBLE);
        products.setVisibility(View.VISIBLE);

        name.setText(getResources().getString(R.string.nama_pemilik_rekening));
        name_field.setText(getIntent().getExtras().getString("Name"));
        products.setText(getResources().getString(R.string.id_bank_tujuan));
        product_field.setText(getResources().getString(R.string.id_banksinarmas));
        number.setText(getResources().getString(R.string.id_nomor_rekening_tujuan));
        number_field.setText(getIntent().getExtras().getString("Acc_Number"));
        amount.setText(getResources().getString(R.string.jumlah));
        amount_field.setText("Rp. " + getIntent().getExtras().getString("amount"));

        Log.e("---------", "---------" + getIntent().getExtras().getString("Acc_Number"));

        products.setVisibility(View.GONE);
        product_field.setVisibility(View.GONE);

        cancel = (Button) findViewById(R.id.cancel);
        confirmation = (Button) findViewById(R.id.next);

        back = (LinearLayout) findViewById(R.id.back_layout);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");
        settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString(Constants.PARAMETER_PHONENUMBER,"");
        stMPIN = getIntent().getExtras().getString(Constants.PARAMETER_MPIN);
        stFullname = getIntent().getExtras().getString("Name");
        stAmount = getIntent().getExtras().getString("amount");
        stMDN = getIntent().getExtras().getString("DestMDN");
        stTransferID = getIntent().getExtras().getString("transferID");
        stParentTxnID = getIntent().getExtras().getString("ParentId");
        stSctl = getIntent().getExtras().getString("sctlID");

        title.setTypeface(Utility.Robot_Regular(UangkuTransferConfirmationActivity.this));
        heading.setTypeface(Utility.Robot_Regular(UangkuTransferConfirmationActivity.this));
        name.setTypeface(Utility.Robot_Regular(UangkuTransferConfirmationActivity.this));
        name_field.setTypeface(Utility.Robot_Light(UangkuTransferConfirmationActivity.this));
        number.setTypeface(Utility.Robot_Regular(UangkuTransferConfirmationActivity.this));
        number_field.setTypeface(Utility.Robot_Light(UangkuTransferConfirmationActivity.this));
        amount.setTypeface(Utility.Robot_Regular(UangkuTransferConfirmationActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(UangkuTransferConfirmationActivity.this));
        products.setTypeface(Utility.Robot_Regular(UangkuTransferConfirmationActivity.this));
        product_field.setTypeface(Utility.Robot_Light(UangkuTransferConfirmationActivity.this));
        cancel.setTypeface(Utility.Robot_Regular(UangkuTransferConfirmationActivity.this));
        confirmation.setTypeface(Utility.Robot_Regular(UangkuTransferConfirmationActivity.this));

        cancel.setOnClickListener(view -> UangkuTransferConfirmationActivity.this.finish());

        confirmation.setOnClickListener(view -> {
            if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                int currentapiVersion = Build.VERSION.SDK_INT;
                if (currentapiVersion > Build.VERSION_CODES.LOLLIPOP) {
                    if ((checkCallingOrSelfPermission(Manifest.permission.READ_SMS)
                            != PackageManager.PERMISSION_GRANTED) && checkCallingOrSelfPermission(Manifest.permission.RECEIVE_SMS)
                            != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS},
                                109);
                    } else {
                        new requestOTPAsyncTask().execute();
                    }
                } else {
                    new requestOTPAsyncTask().execute();
                }
//                    }
            }

        });

        back.setOnClickListener(view -> UangkuTransferConfirmationActivity.this.finish());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 109) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(LOG_TAG, "permission granted");
            } else {
                Log.d(LOG_TAG, "permission rejected");
            }
        }
    }

    private void showOTPRequiredDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullParent = null;
        View dialoglayout = inflater.inflate(R.layout.new_otp_dialog, nullParent, false);
        dialogBuilder = new AlertDialog.Builder(UangkuTransferConfirmationActivity.this, R.style.MyAlertDialogStyle).create();
        dialogBuilder.setCanceledOnTouchOutside(false);
        dialogBuilder.setTitle("");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(dialoglayout);

        // EditText OTP
        otplay = (LinearLayout) dialoglayout.findViewById(R.id.halaman1);
        otp2lay = (LinearLayout) dialoglayout.findViewById(R.id.halaman2);
        otp2lay.setVisibility(View.GONE);
        TextView manualotp = (TextView) dialoglayout.findViewById(R.id.manualsms_lbl);
        Button cancel_otp = (Button) dialoglayout.findViewById(R.id.cancel_otp);
        manualotp.setOnClickListener(arg0 -> {
            otplay.setVisibility(View.GONE);
            otp2lay.setVisibility(View.VISIBLE);
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
                dialogBuilder.dismiss();
                func.errorOTP();
                timer.setText("00:00");
            }
        };
        myTimer.start();
        cancel_otp.setOnClickListener(v -> {
            dialogBuilder.dismiss();
            myTimer.cancel();
        });
        final Button ok_otp = (Button) dialoglayout.findViewById(R.id.ok_otp);
        ok_otp.setOnClickListener(v -> {
            if (edt.getText() == null || edt.getText().toString().equals("")) {
                func.errorEmptyOTP();
            } else {
                myTimer.cancel();
                otpValue=edt.getText().toString();
                if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_BANK_INT) {
                    new InterBankLakuPandaiAsynTask().execute();
                }else{
                    new TransferemoneyUangkuConfirmationAsyncTask().execute();
                }
            }
        });
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    ok_otp.setEnabled(false);
                } else {
                    ok_otp.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt.getText().length() > 5) {
                    Log.d(LOG_TAG, "otp dialog : " + edt.getText());
                    Log.d(LOG_TAG, "otp dialog length: " + edt.getText().length());
                    otpValue=edt.getText().toString();
                    myTimer.cancel();
                    if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_BANK_INT) {
                        new InterBankLakuPandaiAsynTask().execute();
                    }else{
                        new TransferemoneyUangkuConfirmationAsyncTask().execute();
                    }
                }
            }
        });
        dialogBuilder.show();
    }

    private class TransferemoneyUangkuConfirmationAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
            sharedPreferences.edit().putString("profileId", "").apply();
            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put("txnName", "TransferToUangku");
            Log.d(LOG_TAG,"txnName TransferToUangku");
            mapContainer.put("service", Constants.SERVICE_WALLET);
            Log.d(LOG_TAG,"service "+Constants.SERVICE_WALLET);
            mapContainer.put("institutionID", Constants.CONSTANT_INSTITUTION_ID);
            Log.d(LOG_TAG,"institutionID "+Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put("authenticationKey", "");
            Log.d(LOG_TAG,"authenticationKey ");
            mapContainer.put("sourceMDN", sharedPreferences.getString("mobileNumber", ""));
            Log.d(LOG_TAG,"sourceMDN "+sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put("transferID", stTransferID);
            Log.d(LOG_TAG,"transferID "+stTransferID);
            mapContainer.put("parentTxnID", stParentTxnID);
            Log.d(LOG_TAG,"parentTxnID "+stParentTxnID);
            mapContainer.put(Constants.PARAMETER_CONFIRMED,Constants.CONSTANT_VALUE_TRUE);
            Log.d(LOG_TAG,"confirmed true");
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);
            Log.d(LOG_TAG,"channelID 7");
            mapContainer.put("sourcePocketCode", "1");
            Log.d(LOG_TAG,"sourcePocketCode 1");
            if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                mapContainer.put(Constants.PARAMETER_MFA_OTP, CryptoService.encryptWithPublicKey(module, exponent, otpValue.getBytes()));
            }else{
                mapContainer.put(Constants.PARAMETER_MFA_OTP, "");
            }
            Log.d(LOG_TAG,"mfaOtp "+CryptoService.encryptWithPublicKey(module, exponent, otpValue.getBytes()));
            Log.d(LOG_TAG,"otp "+ otpValue);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    UangkuTransferConfirmationActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(UangkuTransferConfirmationActivity.this);
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
                    Log.e("responseContainer", "responseContainer" + responseDataContainer + "");

                } catch (Exception e) {
                    Log.e(LOG_TAG, e.toString());
                }
                try {
                    if (responseDataContainer != null) {
                        Log.d("test", "not null");
                        int msgCode = 0;

                        try {
                            msgCode = Integer.parseInt(responseDataContainer.getMsgCode());
                        } catch (Exception e) {
                            msgCode = 0;
                        }

                        if (msgCode == 631) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            func.errorTimeoutResponseConfirmation(responseDataContainer.getMsg());
                            dialogBuilder.dismiss();
                        }else if(msgCode==2176){
                            Intent intent = new Intent(UangkuTransferConfirmationActivity.this, UangkuTransferSuccessActivity.class);
                            intent.putExtra("amount", stAmount);
                            intent.putExtra("Acc_Number", getIntent().getExtras().getString("Acc_Number"));
                            intent.putExtra("transferID", responseDataContainer.getEncryptedTransferId());
                            intent.putExtra("sctlID", responseDataContainer.getSctl());
                            intent.putExtra("Name", getIntent().getExtras().getString("Name"));
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivityForResult(intent, 10);
                            finish();
                        }else{
                            func.errorElseResponseConfirmation(responseDataContainer.getMsg());
                            dialogBuilder.dismiss();
                        }
                    }
                }catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }else{
                func.errorNullResponseConfirmation();
            }
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

            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    UangkuTransferConfirmationActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(UangkuTransferConfirmationActivity.this);
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
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            func.errorTimeoutResponseConfirmation(responseDataContainer.getMsg());
                            dialogBuilder.dismiss();
                        } else if(responseDataContainer.getMsgCode().equals("2171")){
                            message = responseDataContainer.getMsg();
                            Log.d(LOG_TAG, "message"+message);
                            transactionTime = responseDataContainer.getTransactionTime();
                            Log.d(LOG_TAG, "transactionTime"+transactionTime);
                            responseCode = responseDataContainer.getResponseCode();
                            Log.d(LOG_TAG, "responseCode"+responseCode);
                            Log.d("test", "not null");
                            int msgCode = 0;

                            showOTPRequiredDialog();
                        }else{
                            alertbox = new AlertDialog.Builder(UangkuTransferConfirmationActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", (arg0, arg1) -> finish());
                            alertbox.show();
                            dialogBuilder.dismiss();
                        }
                    }
                }catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }
        }
    }

    @Override
    public void onReadSMS(String otp) {
        Log.d(LOG_TAG, "otp from SMS: " + otp);
        edt.setText(otp);
        otpValue=otp;
    }

    private class InterBankLakuPandaiAsynTask extends AsyncTask<Void, Void, Void> {

        String response;

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_UANGKU);
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_TRANSFER_ID, getIntent().getExtras().getString("transferID"));
            mapContainer.put(Constants.PARAMETER_PARENTTXN_ID, getIntent().getExtras().getString("ParentId"));
            mapContainer.put(Constants.PARAMETER_DEST_ACCOUNT_NO, getIntent().getExtras().getString("Acc_Number"));
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION_CONFIRM);
            mapContainer.put(Constants.PARAMETER_CONFIRMED, Constants.CONSTANT_VALUE_TRUE);
            if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                OTP=func.generateRSA(otpValue);
                mapContainer.put(Constants.PARAMETER_MFA_OTP, OTP);
            } else {
                mapContainer.put(Constants.PARAMETER_MFA_OTP, "");
            }
            if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_BANK_INT) {
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
            } else if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_BANKSINARMAS_INT) {
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
            } else if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_LAKUPANDAI_INT) {
                if (sharedPreferences.getInt(Constants.PARAMETER_AGENTTYPE, -1) == Constants.CONSTANT_EMONEY_INT) {
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_AGENT);
                } else {
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                }
            } else if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_EMONEY_INT) {
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
            }
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, UangkuTransferConfirmationActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {

            if (!progressDialog.isShowing()) {
                progressDialog.show();
                progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null) {
                Log.e("-------", "---------" + response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseContainer = null;
                try {
                    responseContainer = obj.parse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    msgCode = Integer.parseInt(responseContainer.getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 631) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    func.errorTimeoutResponseConfirmation(responseContainer.getMsg());
                    dialogBuilder.dismiss();
                } else if (msgCode == 293 || msgCode == 81 || msgCode == 305 || msgCode == 2176) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(UangkuTransferConfirmationActivity.this, UangkuTransferSuccessActivity.class);
                    intent.putExtra("amount", getIntent().getExtras().getString("amount"));
                    intent.putExtra("Acc_Number", getIntent().getExtras().getString("Acc_Number"));
                    intent.putExtra("transferID", responseContainer.getEncryptedTransferId());
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("Name", getIntent().getExtras().getString("Name"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, 10);
                    finish();
                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (responseContainer.getMsg() == null) {
                        func.errorNullResponseConfirmation();
                    } else {
                        func.errorElseResponseConfirmation(responseContainer.getMsg());
                    }
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                func.errorNullResponseConfirmation();
            }
        }
    }
}