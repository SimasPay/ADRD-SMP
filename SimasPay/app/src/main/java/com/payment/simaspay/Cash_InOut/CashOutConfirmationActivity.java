package com.payment.simaspay.Cash_InOut;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.receivers.IncomingSMS;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.TimerCount;
import com.payment.simaspay.services.Utility;
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
 * Created by Nagendra P on 1/29/2016.
 */
public class CashOutConfirmationActivity extends AppCompatActivity implements IncomingSMS.AutoReadSMSListener{

    TextView title, heading, name, name_field, number, number_field, amount, amount_field;
    Button cancel, confirmation;
    LinearLayout back;
    boolean Timervalueout;
    String otpValue = "", sctl;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    Context context;
    Dialog dialogCustomWish;
    String OTP;
    int msgCode;
    private static final String LOG_TAG = "SimasPay";
    static boolean isExitActivity = false;
    LinearLayout otplay, otp2lay;
    private EditText edt;
    SharedPreferences settings, settings2, languageSettings;
    String selectedLanguage;
    private AlertDialog.Builder alertbox;
    String sourceMDN, stMPIN, stCharges, stName, stAmount, stMDN, stTransferID, stParentTxnID, stSctl, message, transactionTime, responseCode;
    TimerCount timerCount;
    private static AlertDialog dialogBuilder, alertError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.commonconfirmation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        IncomingSMS.setListener(CashOutConfirmationActivity.this);

        sharedPreferences= getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        title = (TextView) findViewById(R.id.title);
        heading = (TextView) findViewById(R.id.textview);
        name = (TextView) findViewById(R.id.name);

        name_field = (TextView) findViewById(R.id.name_field);
        number = (TextView) findViewById(R.id.number);
        number_field = (TextView) findViewById(R.id.number_field);
        amount = (TextView) findViewById(R.id.amount);
        amount_field = (TextView) findViewById(R.id.amount_field);

        cancel = (Button) findViewById(R.id.cancel);
        confirmation = (Button) findViewById(R.id.next);

        back = (LinearLayout) findViewById(R.id.back_layout);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");
        sharedPreferences=getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN=sharedPreferences.getString("mobileNumber", "");
        String module = sharedPreferences.getString("MODULE", "NONE");
        String exponent = sharedPreferences.getString("EXPONENT", "NONE");
        String pinValue="";
        try {
            pinValue  = CryptoService.encryptWithPublicKey(module, exponent,
                    sharedPreferences.getString("mpin", "").getBytes());
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        stMPIN = pinValue;
        stSctl = getIntent().getExtras().getString("sctlID");
        stTransferID = getIntent().getExtras().getString("transferID");
        stParentTxnID = getIntent().getExtras().getString("ParentId");
        stAmount = getIntent().getExtras().getString("amount");
        stCharges = getIntent().getExtras().getString("charges");
        stMDN = getIntent().getExtras().getString("DestMDN");
        stName = getIntent().getExtras().getString("Name");

        title.setTypeface(Utility.Robot_Regular(CashOutConfirmationActivity.this));
        heading.setTypeface(Utility.Robot_Regular(CashOutConfirmationActivity.this));
        name.setTypeface(Utility.Robot_Regular(CashOutConfirmationActivity.this));
        name_field.setTypeface(Utility.Robot_Light(CashOutConfirmationActivity.this));
        number.setTypeface(Utility.Robot_Regular(CashOutConfirmationActivity.this));
        number_field.setTypeface(Utility.Robot_Light(CashOutConfirmationActivity.this));
        amount.setTypeface(Utility.Robot_Regular(CashOutConfirmationActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(CashOutConfirmationActivity.this));
        cancel.setTypeface(Utility.Robot_Regular(CashOutConfirmationActivity.this));
        confirmation.setTypeface(Utility.Robot_Regular(CashOutConfirmationActivity.this));

        progressDialog = new ProgressDialog(CashOutConfirmationActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));

        if(sharedPreferences.getInt(Constants.PARAMETER_USERTYPE,-1)==Constants.CONSTANT_BANK_INT){
            name.setText("Nama Agen");
            number.setText("Nomor Handphone Agen");
            amount.setText("Jumlah");
            name_field.setText(getIntent().getExtras().getString("Name"));
            number_field.setText(getIntent().getExtras().getString("DestMDN"));
            amount_field.setText("Rp. " + getIntent().getExtras().getString("amount"));
        }else{
            String untuk = getIntent().getExtras().getString("untuk");
            if(untuk.equals("Untuk Saya")){
                name.setVisibility(View.GONE);
                name_field.setVisibility(View.GONE);
            }else if(untuk.equals("Untuk Orang Lain")){
                name.setVisibility(View.VISIBLE);
                name_field.setVisibility(View.VISIBLE);
            }
            number.setText("Jenis Transaksi");
            amount.setText("Jumlah");
            //name_field.setText(getIntent().getExtras().getString("Name"));
            number_field.setText("Tarik Tunai - "+untuk);
            amount_field.setText("Rp. " + getIntent().getExtras().getString("amount"));
        }


        amount_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        number_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        name_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new requestOTPAsyncTask().execute();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 10) {
                Intent intent = getIntent();
                setResult(10, intent);
                finish();
            } else {

            }
        }
    }


    class CashOutAsynTask extends AsyncTask<Void, Void, Void> {
        String response;

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_WALLET);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_CASHOUT);
            mapContainer.put(Constants.PARAMETER_BANK_ID, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_TRANSFER_ID, getIntent().getExtras().getString("transferID"));
            mapContainer.put(Constants.PARAMETER_PARENTTXN_ID, getIntent().getExtras().getString("ParentId"));
            mapContainer.put(Constants.PARAMETER_DEST_MDN, getIntent().getExtras().getString("DestMDN"));
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION_CONFIRM);
            mapContainer.put(Constants.PARAMETER_CONFIRMED, Constants.CONSTANT_VALUE_TRUE);
            if(getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                String module = sharedPreferences.getString("MODULE", "NONE");
                String exponent = sharedPreferences.getString("EXPONENT", "NONE");
                try {
                    OTP = CryptoService.encryptWithPublicKey(module, exponent,
                            otpValue.getBytes());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                mapContainer.put(Constants.PARAMETER_MFA_OTP, OTP);
            }else{
                mapContainer.put(Constants.PARAMETER_MFA_OTP, "");
            }
            mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE,Constants.POCKET_CODE_EMONEY);
            mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE,Constants.POCKET_CODE_BANK_SINARMAS);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, CashOutConfirmationActivity.this);

            Log.e("========","========="+mapContainer.toString());
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {

            if (!progressDialog.isShowing()) {
                progressDialog.show();
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
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 631) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    alertbox = new AlertDialog.Builder(CashOutConfirmationActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(responseContainer.getMsg());
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(CashOutConfirmationActivity.this, SecondLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    alertbox.show();
                } else if (msgCode == 298) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(CashOutConfirmationActivity.this, CashoutSuccessActivity.class);
                    intent.putExtra("amount", getIntent().getExtras().getString("amount"));
                    intent.putExtra("DestMDN", getIntent().getExtras().getString("DestMDN"));
                    intent.putExtra("transferID", responseContainer.getEncryptedTransferId());
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("Name", getIntent().getExtras().getString("Name"));
                    intent.putExtra("untuk", getIntent().getExtras().getString("untuk"));
                    startActivityForResult(intent, 10);
                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (responseContainer.getMsg() == null) {
                        Utility.networkDisplayDialog(
                                sharedPreferences.getString(
                                        "ErrorMessage",
                                        getResources()
                                                .getString(
                                                        R.string.server_error_message)),
                                CashOutConfirmationActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), CashOutConfirmationActivity.this);
                    }
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), CashOutConfirmationActivity.this);
            }
        }
    }

    class CashOutConfirmationAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
            sharedPreferences.edit().putString("profileId", "").apply();
            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put("txnName", "CashOutAtATM");
            Log.d(LOG_TAG,"txnName CashOutAtATM");
            mapContainer.put("service", "Wallet");
            Log.d(LOG_TAG,"service Wallet");
            mapContainer.put("institutionID", Constants.CONSTANT_INSTITUTION_ID);
            Log.d(LOG_TAG,"institutionID "+Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put("sourceMDN", sharedPreferences.getString("mobileNumber", ""));
            Log.d(LOG_TAG,"sourceMDN "+sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put("parentTxnID", stParentTxnID);
            Log.d(LOG_TAG,"parentTxnID "+stParentTxnID);
            mapContainer.put("transferID", stTransferID);
            Log.d(LOG_TAG,"transferID "+stTransferID);
            mapContainer.put(Constants.PARAMETER_CONFIRMED,Constants.CONSTANT_VALUE_TRUE);
            Log.d(LOG_TAG,"confirmed true");
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);
            Log.d(LOG_TAG,"channelID 7");
            if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                mapContainer.put(Constants.PARAMETER_MFA_OTP, CryptoService.encryptWithPublicKey(module, exponent, otpValue.getBytes()));
            }else{
                mapContainer.put(Constants.PARAMETER_MFA_OTP, "");
            }
            Log.d(LOG_TAG,"mfaOtp "+CryptoService.encryptWithPublicKey(module, exponent, otpValue.getBytes()));
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    CashOutConfirmationActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CashOutConfirmationActivity.this);
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
                            alertbox = new AlertDialog.Builder(CashOutConfirmationActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(CashOutConfirmationActivity.this, SecondLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                            alertbox.show();
                        }else if(msgCode==709){
                            Intent intent = new Intent(CashOutConfirmationActivity.this, CashoutSuccessActivity.class);
                            intent.putExtra("amount", getIntent().getExtras().getString("amount"));
                            intent.putExtra("DestMDN", getIntent().getExtras().getString("DestMDN"));
                            intent.putExtra("transferID", getIntent().getExtras().getString("transferID"));
                            intent.putExtra("sctlID", getIntent().getExtras().getString("sctlID"));
                            intent.putExtra("Name", getIntent().getExtras().getString("Name"));
                            intent.putExtra("untuk", getIntent().getExtras().getString("untuk"));
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivityForResult(intent, 10);
                        }else{
                            alertbox = new AlertDialog.Builder(CashOutConfirmationActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });
                            alertbox.show();
                        }
                    }
                }catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), CashOutConfirmationActivity.this);
            }
        }
    }

    @Override
    public void onReadSMS(String otp) {
        Log.d(LOG_TAG, "otp from SMS: " + otp);
        edt.setText(otp);
        otpValue=otp;
    }

    class requestOTPAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");
            String pinValue="";
            try {
                pinValue  = CryptoService.encryptWithPublicKey(module, exponent,
                        sharedPreferences.getString("mpin", "").getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("txnName", "ResendMFAOTP");
            mapContainer.put("service", "Wallet");
            mapContainer.put("institutionID", Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put("authenticationKey", "");
            mapContainer.put("sourceMDN", sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put("sourcePIN", pinValue);
            mapContainer.put("sctlId", getIntent().getExtras().getString("sctlID"));
            mapContainer.put("channelID", "7");

            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, CashOutConfirmationActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CashOutConfirmationActivity.this);
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
                            alertbox = new AlertDialog.Builder(CashOutConfirmationActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(CashOutConfirmationActivity.this, SecondLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                            alertbox.show();
                        }else if(responseDataContainer.getMsgCode().equals("2171")){
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
                            alertbox = new AlertDialog.Builder(CashOutConfirmationActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });
                            alertbox.show();
                        }
                    }
                }catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), CashOutConfirmationActivity.this);
            }
        }
    }

    private void showOTPRequiredDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullParent = null;
        View dialoglayout = inflater.inflate(R.layout.new_otp_dialog, nullParent, false);
        dialogBuilder = new AlertDialog.Builder(CashOutConfirmationActivity.this, R.style.MyAlertDialogStyle).create();
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
        //waitingsms.setText("Menunggu SMS Kode Verifikasi di Nomor " + Html.fromHtml("<b>"+sharedPreferences.getString("mobileNumber", "")+"</b>") + "\n");
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
                    settings2 = getSharedPreferences(LOG_TAG, 0);
                    settings2.edit().putString("ActivityName", "ExitConfirmationScreen").apply();
                    isExitActivity = true;
                    otpValue=edt.getText().toString();
                    if(otpValue==null||otpValue.equals("")){
                        otpValue=edt.getText().toString();
                    }
                    String account = sharedPreferences.getString("useas","");
                    if(sharedPreferences.getInt(Constants.PARAMETER_USERTYPE,-1)==Constants.CONSTANT_BANK_INT){
                        new CashOutAsynTask().execute();
                    }else{
                        new CashOutConfirmationAsyncTask().execute();
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
                    otpValue=edt.getText().toString();
                    if (myTimer != null) {
                        myTimer.cancel();
                    }
                    if(otpValue==null||otpValue.equals("")){
                        otpValue=edt.getText().toString();
                    }

                    if(sharedPreferences.getInt(Constants.PARAMETER_USERTYPE,-1)==Constants.CONSTANT_BANK_INT){
                        new CashOutAsynTask().execute();
                    }else{
                        new CashOutConfirmationAsyncTask().execute();
                    }
                    //new TransferEmoneyConfirmationActivity.TransferConfirmationAsyncTask().execute();

                }

            }
        });
        dialogBuilder.show();
    }

    public void errorOTP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CashOutConfirmationActivity.this, R.style.MyAlertDialogStyle);
        builder.setCancelable(false);
        if (selectedLanguage.equalsIgnoreCase("ENG")) {
            builder.setTitle(getResources().getString(R.string.eng_otpfailed));
            builder.setMessage(getResources().getString(R.string.eng_desc_otpfailed)).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            settings2 = getSharedPreferences(LOG_TAG, 0);
                            settings2.edit().putString("ActivityName", "ExitConfirmationScreen").apply();
                            dialog.dismiss();
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
                            dialog.dismiss();
                            dialogBuilder.dismiss();
                        }
                    });
        }
        alertError = builder.create();
        if (!isFinishing()) {
            alertError.show();
        }
    }
}
