package com.payment.simaspay.AgentTransfer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
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

import com.payment.simaspay.receivers.IncomingSMS;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.utils.Functions;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

/**
 * Created by widy on 1/25/17.
 * 25
 */

public class TransferEmoneyToEmoneyConfirmationActivity extends AppCompatActivity implements IncomingSMS.AutoReadSMSListener{
    String sourceMDN, stFullname, stAmount, stMPIN, stTransferID, stSctl, stParentTxnID, stMDN;
    String message, transactionTime, responseCode, mfaMode;
    TextView lbl_name, lbl_mdn, lbl_amount, notice;
    Button benar_btn, salah_btn;
    private static final String LOG_TAG = "SimasPay";
    private EditText edt;
    private static AlertDialog dialogBuilder;
    LinearLayout otplay, otp2lay;
    Context context;
    SharedPreferences settings, languageSettings;
    String otpValue;
    String selectedLanguage;
    Functions func;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoney_confirmation);
        func=new Functions(this);
        func.initiatedToolbar(this);
        if (Build.VERSION.SDK_INT >= 14) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        context=TransferEmoneyToEmoneyConfirmationActivity.this;

        IncomingSMS.setListener(TransferEmoneyToEmoneyConfirmationActivity.this);

        settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString("mobileNumber","");

        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        lbl_name=(TextView)findViewById(R.id.lbl_name);
        lbl_amount=(TextView)findViewById(R.id.lbl_amount);
        lbl_mdn=(TextView)findViewById(R.id.lbl_mdn);
        notice=(TextView)findViewById(R.id.notice);
        benar_btn=(Button)findViewById(R.id.benar_btn);
        salah_btn=(Button)findViewById(R.id.salah_btn);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            stFullname = (String) extras.get("destname");
            stMDN = (String) extras.get("destmdn");
            stAmount = (String) extras.get("amount");
            stMPIN = (String) extras.get("mpin");
            stTransferID = (String) extras.get("transferID");
            stParentTxnID = (String) extras.get("parentTxnID");
            stSctl = (String) extras.get("sctlID");
        }
        if(stFullname==null){
            lbl_name.setText("(belum terdaftar)");
            stFullname="(belum terdaftar)";
            lbl_mdn.setText(stMDN+"*");
            notice.setVisibility(View.VISIBLE);
        }else{
            lbl_name.setText(stFullname);
            lbl_mdn.setText(stMDN);
            notice.setVisibility(View.GONE);
        }

        lbl_amount.setText(Functions.formatRupiah(stAmount));
        benar_btn.setOnClickListener(view -> {
            new requestOTPAsyncTask().execute();
            //showOTPRequiredDialog();
        });
        salah_btn.setOnClickListener(view -> finish());
    }


    @Override
    public void onReadSMS(String otp) {
        Log.d(LOG_TAG, "otp from SMS: " + otp);
        edt.setText(otp);
        otpValue=otp;
    }

    private void showOTPRequiredDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullParent = null;
        View dialoglayout = inflater.inflate(R.layout.new_otp_dialog, nullParent, false);
        dialogBuilder = new AlertDialog.Builder(TransferEmoneyToEmoneyConfirmationActivity.this, R.style.MyAlertDialogStyle).create();
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
        ok_otp.setEnabled(false);
        ok_otp.setTextColor(getResources().getColor(R.color.dark_red));
        ok_otp.setOnClickListener(v -> {
            if (edt.getText() == null || edt.getText().toString().equals("")) {
                func.errorEmptyOTP();
                dialogBuilder.dismiss();
            } else {
                myTimer.cancel();
                if(otpValue==null||otpValue.equals("")){
                    otpValue=edt.getText().toString();
                }
                new TransferConfirmationAsyncTask().execute();
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
                ok_otp.setEnabled(true);
                ok_otp.setTextColor(getResources().getColor(R.color.red));
                if (edt.getText().length() >= Constants.DIGITS_OTP) {
                    Log.d(LOG_TAG, "otp dialog length: " + edt.getText().length());
                    myTimer.cancel();
                    if(otpValue==null||otpValue.equals("")){
                        otpValue=edt.getText().toString();
                    }
                    new TransferConfirmationAsyncTask().execute();

                }

            }
        });
        dialogBuilder.show();
    }

    private class TransferConfirmationAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, Constants.TRANSACTION_TRANSFER);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_KEY, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sourceMDN);
            mapContainer.put(Constants.PARAMETER_DEST_MDN, stMDN);
            mapContainer.put(Constants.PARAMETER_TRANSFER_ID, stTransferID);
            mapContainer.put(Constants.PARAMETER_BANK_ID, "");
            mapContainer.put(Constants.PARAMETER_PARENTTXN_ID, stParentTxnID);
            mapContainer.put(Constants.PARAMETER_CONFIRMED,Constants.CONSTANT_VALUE_TRUE);
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
            mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
            mapContainer.put(Constants.PARAMETER_DEST_BankAccount, "");

            if(getIntent().getExtras()!=null) {
                mfaMode = getIntent().getExtras().getString("mfaMode");
                if (mfaMode != null) {
                    if (mfaMode.equalsIgnoreCase("OTP")) {
                        mapContainer.put(Constants.PARAMETER_MFA_OTP, func.generateRSA(otpValue));
                    } else {
                        mapContainer.put(Constants.PARAMETER_MFA_OTP, "");
                    }
                }
            }
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    TransferEmoneyToEmoneyConfirmationActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(TransferEmoneyToEmoneyConfirmationActivity.this);
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
                        int msgCode;

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
                        }else if(msgCode==293||msgCode==678){
                            Intent intent = new Intent(TransferEmoneyToEmoneyConfirmationActivity.this, TransferBankToEmoneyNotificationActivity.class);
                            intent.putExtra("destmdn", stMDN);
                            intent.putExtra("amount", stAmount);
                            intent.putExtra("destName", stFullname);
                            intent.putExtra("transactionID", responseDataContainer.getSctl());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            TransferEmoneyToEmoneyConfirmationActivity.this.finish();
                        }else{
                            func.errorElseResponseConfirmation(responseDataContainer.getMsg());
                            dialogBuilder.dismiss();
                        }
                    }
                }catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }else{
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
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
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, Constants.TRANSACTION_RESENDMFAOTP);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID, Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_KEY, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sourceMDN);
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, stMPIN);
            mapContainer.put(Constants.PARAMETER_SCTL, stSctl);
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);

            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    TransferEmoneyToEmoneyConfirmationActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(TransferEmoneyToEmoneyConfirmationActivity.this);
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
                        AlertDialog.Builder alertbox;
                        switch (responseDataContainer.getMsgCode()) {
                            case "631":
                                alertbox = new AlertDialog.Builder(TransferEmoneyToEmoneyConfirmationActivity.this, R.style.MyAlertDialogStyle);
                                alertbox.setMessage(responseDataContainer.getMsg());
                                alertbox.setCancelable(false);
                                alertbox.setNeutralButton("OK", (arg0, arg1) -> {
                                    Intent intent = new Intent(TransferEmoneyToEmoneyConfirmationActivity.this, SecondLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                });
                                alertbox.show();
                                break;
                            case "2171":
                                message = responseDataContainer.getMsg();
                                Log.d(LOG_TAG, "message" + message);
                                transactionTime = responseDataContainer.getTransactionTime();
                                Log.d(LOG_TAG, "transactionTime" + transactionTime);
                                responseCode = responseDataContainer.getResponseCode();
                                Log.d(LOG_TAG, "responseCode" + responseCode);
                                Log.d("test", "not null");

                                showOTPRequiredDialog();
                                break;
                            default:
                                alertbox = new AlertDialog.Builder(TransferEmoneyToEmoneyConfirmationActivity.this, R.style.MyAlertDialogStyle);
                                alertbox.setMessage(responseDataContainer.getMsg());
                                alertbox.setNeutralButton("OK", (arg0, arg1) -> arg0.dismiss());
                                alertbox.show();
                                dialogBuilder.dismiss();
                                break;
                        }
                    }
                }catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }else{
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), TransferEmoneyToEmoneyConfirmationActivity.this);
            }
        }
    }


}
