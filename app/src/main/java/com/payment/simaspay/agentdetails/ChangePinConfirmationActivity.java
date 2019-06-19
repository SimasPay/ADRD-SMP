package com.payment.simaspay.agentdetails;

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
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.R;
import com.payment.simaspay.receivers.IncomingSMS;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.utils.Functions;
import com.payment.simaspay.constants.EncryptedResponseDataContainer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ChangePinConfirmationActivity extends AppCompatActivity implements IncomingSMS.AutoReadSMSListener{
    TextView textView, title;
    Button simpan,cancel;
    LinearLayout btnBacke;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    String otpValue="";
    String sourceMDN, stMPIN, stSctl, stMDN;
    String message, transactionTime, responseCode;
    private static final String LOG_TAG = "SimasPay";
    private EditText edt;
    private static AlertDialog dialogBuilder;
    LinearLayout otplay, otp2lay;
    SharedPreferences settings, languageSettings;
    String selectedLanguage;
    Context context;
    WebServiceHttp webServiceHttp;
    String confirmationResponse;
    String encryptedOtp;
    int msgCode=0;
    Functions func;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changempinconfirmation);
        func=new Functions(this);
        func.initiatedToolbar(this);

        context=ChangePinConfirmationActivity.this;
        IncomingSMS.setListener(ChangePinConfirmationActivity.this);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString("mobileNumber","");
        stMDN=sourceMDN;
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");

        progressDialog = new ProgressDialog(ChangePinConfirmationActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));

        title = findViewById(R.id.titled);
        simpan = findViewById(R.id.benar_btn);
        cancel= findViewById(R.id.salah_btn);

        btnBacke = findViewById(R.id.back_layout);
        btnBacke.setOnClickListener(view -> finish());

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        String module = sharedPreferences.getString("MODULE", "NONE");
        String exponent = sharedPreferences.getString("EXPONENT", "NONE");
        String mpin = sharedPreferences.getString("mpin", "");

        try {
            stMPIN = CryptoService.encryptWithPublicKey(module, exponent,
                    mpin.getBytes());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        stSctl=getIntent().getExtras().getString("sctlID");

        title.setTypeface(Utility.RegularTextFormat(ChangePinConfirmationActivity.this));
        simpan.setTypeface(Utility.Robot_Regular(ChangePinConfirmationActivity.this));
        cancel.setTypeface(Utility.Robot_Regular(ChangePinConfirmationActivity.this));
        simpan.setOnClickListener(v -> {
                if(Objects.requireNonNull(getIntent().getExtras().getString("mfaMode")).equalsIgnoreCase("OTP")) {
                    new requestOTPAsyncTask().execute();
                }else{
                    new ChangePinConfirmationAsyncTask().execute();
                }

        });

        cancel.setOnClickListener(v -> finish());
    }

    @Override
    public void onReadSMS(String otp) {
        edt.setText(otp);
        otpValue=otp;
    }

    private class ChangePinConfirmationAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_CHANGEPIN);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, getIntent().getExtras().getString("oldpin"));
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_NEW_PIN, getIntent().getExtras().getString("newPin"));
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION_CONFIRM);
            mapContainer.put(Constants.PARAMETER_CONFIRM_PIN, getIntent().getExtras().getString("ConfirmPin"));
            mapContainer.put(Constants.PARAMETER_PARENTTXN_ID, getIntent().getExtras().getString("sctlID"));
            //Log.d(LOG_TAG, "otpValue:"+otpValue);
            if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                String module = sharedPreferences.getString("MODULE", "NONE");
                String exponent = sharedPreferences.getString("EXPONENT", "NONE");
                try {
                    encryptedOtp = CryptoService.encryptWithPublicKey(module, exponent,
                            otpValue.getBytes());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                mapContainer.put(Constants.PARAMETER_MFA_OTP, encryptedOtp);
            } else {
                mapContainer.put(Constants.PARAMETER_MFA_OTP, "");
            }

            webServiceHttp = new WebServiceHttp(mapContainer, ChangePinConfirmationActivity.this);

            confirmationResponse = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog != null) {
                progressDialog.show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(confirmationResponse!=null){
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseContainer = null;
                try {
                    responseContainer = obj.parse(confirmationResponse);
                } catch (Exception e) {
                    //Log.d(LOG_TAG, "error: "+ e.toString());
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                try {
                    if (responseContainer != null) {
                        msgCode = Integer.parseInt(responseContainer.getMsgCode());
                    }
                } catch (Exception e) {
                    msgCode = 0;
                }
                if(msgCode==26){
                    sharedPreferences.edit().putString("password",getIntent().getExtras().getString("newPin")).apply();
                    Intent intent=new Intent(ChangePinConfirmationActivity.this,ChangePinSuccessActivity.class);
                    startActivityForResult(intent,10);
                }else if(msgCode==631){
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    func.errorTimeoutResponseConfirmation(responseContainer.getMsg());
                }else {
                    if (responseContainer != null) {
                        if (responseContainer.getMsg() == null) {
                            func.errorNullResponseConfirmation();
                        } else {
                            func.errorElseResponseConfirmation(responseContainer.getMsg());
                        }
                    }
                }
            }else{
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                func.errorNullResponseConfirmation();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(resultCode==RESULT_OK){
                Intent intent=getIntent();
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    }

    private void showOTPRequiredDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullParent = null;
        View dialoglayout = inflater.inflate(R.layout.new_otp_dialog, nullParent, false);
        dialogBuilder = new AlertDialog.Builder(ChangePinConfirmationActivity.this, R.style.MyAlertDialogStyle).create();
        dialogBuilder.setCanceledOnTouchOutside(false);
        dialogBuilder.setTitle("");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(dialoglayout);

        // EditText OTP
        otplay = dialoglayout.findViewById(R.id.halaman1);
        otp2lay = dialoglayout.findViewById(R.id.halaman2);
        otp2lay.setVisibility(View.GONE);
        TextView manualotp = dialoglayout.findViewById(R.id.manualsms_lbl);
        Button cancel_otp = dialoglayout.findViewById(R.id.cancel_otp);
        manualotp.setOnClickListener(arg0 -> {
            otplay.setVisibility(View.GONE);
            otp2lay.setVisibility(View.VISIBLE);
        });
        edt = dialoglayout.findViewById(R.id.otp_value);

        // Timer
        final TextView timer = dialoglayout.findViewById(R.id.otp_timer);
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
        final Button ok_otp = dialoglayout.findViewById(R.id.ok_otp);
        ok_otp.setEnabled(false);
        ok_otp.setTextColor(getResources().getColor(R.color.dark_red));
        ok_otp.setOnClickListener(v -> {
            if (edt.getText() == null || edt.getText().toString().equals("")) {
                func.errorEmptyOTP();
            } else {
                myTimer.cancel();
                if(otpValue==null||otpValue.equals("")){
                    otpValue=edt.getText().toString();
                }
                new ChangePinConfirmationAsyncTask().execute();
                dialogBuilder.dismiss();
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
                if(s.toString().trim().length()==0){
                    ok_otp.setEnabled(false);
                } else {
                    ok_otp.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                ok_otp.setEnabled(true);
                ok_otp.setTextColor(getResources().getColor(R.color.red));
                if (edt.getText().length() >= Constants.DIGITS_OTP) {
                    myTimer.cancel();
                    if(otpValue==null||otpValue.equals("")){
                        otpValue=edt.getText().toString();
                    }
                    new ChangePinConfirmationAsyncTask().execute();
                    dialogBuilder.dismiss();
                }

            }
        });
        dialogBuilder.show();
    }

    class requestOTPAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;
        int msgCode;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("txnName", "ResendMFAOTP");
            mapContainer.put("service", "Wallet");
            mapContainer.put("institutionID", Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put("authenticationKey", "");
            mapContainer.put("sourceMDN", sourceMDN);
            mapContainer.put("sourcePIN", stMPIN);
            mapContainer.put("sctlId", stSctl);
            mapContainer.put("channelID", "7");
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    ChangePinConfirmationActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ChangePinConfirmationActivity.this);
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
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseDataContainer = null;
                try {
                    responseDataContainer = obj.parse(response);
                } catch (Exception e) {
                    //Log.e(LOG_TAG, e.toString());
                }
                try {
                    if (responseDataContainer != null) {
                        msgCode = Integer.parseInt(responseDataContainer.getMsgCode());
                    }
                } catch (Exception e) {
                    msgCode = 0;
                }
                try {
                    if (responseDataContainer != null) {
                        switch (responseDataContainer.getMsgCode()) {
                            case "631":
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(ChangePinConfirmationActivity.this, R.style.MyAlertDialogStyle);
                                alertbox.setMessage(responseDataContainer.getMsg());
                                alertbox.setNeutralButton("OK", (arg0, arg1) -> {
                                    arg0.dismiss();
                                    Intent intent = new Intent(ChangePinConfirmationActivity.this, SecondLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                });
                                alertbox.show();
                                break;
                            case "2171":
                                message = responseDataContainer.getMsg();
                                //Log.d(LOG_TAG, "message" + message);
                                transactionTime = responseDataContainer.getTransactionTime();
                                //Log.d(LOG_TAG, "transactionTime" + transactionTime);
                                responseCode = responseDataContainer.getResponseCode();
                                //Log.d(LOG_TAG, "responseCode" + responseCode);
                                //Log.d("test", "not null");
                                showOTPRequiredDialog();
                                break;
                            default:
                                func.errorElseResponseConfirmation(responseDataContainer.getMsg());
                                dialogBuilder.dismiss();
                                break;
                        }
                    }
                }catch (Exception e) {
                    //Log.e(LOG_TAG, "error: " + e.toString());
                }
            }
        }
    }
}
