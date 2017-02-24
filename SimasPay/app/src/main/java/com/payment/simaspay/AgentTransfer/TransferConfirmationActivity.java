package com.payment.simaspay.AgentTransfer;

import android.Manifest;
import android.app.Activity;
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
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
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

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.UangkuTransfer.UangkuTransferConfirmationActivity;
import com.payment.simaspay.receivers.IncomingSMS;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.userdetails.SessionTimeOutActivity;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.InputNumberScreenActivity;
import simaspay.payment.com.simaspay.R;
import simaspay.payment.com.simaspay.UserHomeActivity;

public class TransferConfirmationActivity extends AppCompatActivity implements IncomingSMS.AutoReadSMSListener{

    TextView title, heading, name, name_field, number, number_field, amount, amount_field, products, product_field;
    private static AlertDialog dialogBuilder, alertError;
    Button cancel, confirmation;
    LinearLayout back;
    boolean Timervalueout;
    boolean nextpressedornot;
    SharedPreferences sharedPreferences;
    static boolean isExitActivity = false;
    LinearLayout otplay, otp2lay;
    private static final String LOG_TAG = "SimasPay";
    String otpValue = "", sctl;
    private EditText edt;
    SharedPreferences settings, settings2, languageSettings;
    String selectedLanguage;
    String OTP;
    ProgressDialog progressDialog;
    int msgCode;
    Context context;
    private AlertDialog.Builder alertbox;
    String sourceMDN, stMPIN, stFullname, stAmount, stMDN, stTransferID, stParentTxnID, stSctl, message, transactionTime, responseCode;

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

        IncomingSMS.setListener(TransferConfirmationActivity.this);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");
        settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString("mobileNumber","");
        stMPIN = getIntent().getExtras().getString("mpin");
        stFullname = getIntent().getExtras().getString("Name");
        stAmount = getIntent().getExtras().getString("amount");
        stMDN = getIntent().getExtras().getString("DestMDN");
        stTransferID = getIntent().getExtras().getString("transferID");
        stParentTxnID = getIntent().getExtras().getString("ParentId");
        stSctl = getIntent().getExtras().getString("sctlID");

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

        name.setText("Nama Pemilik Rekening");
        name_field.setText(getIntent().getExtras().getString("Name"));
        number.setText("Bank Tujuan");
        number_field.setText("Bank Sinarmas");
        amount.setText("Nomor Rekening Tujuan");
        amount_field.setText(getIntent().getExtras().getString("DestMDN"));
        products.setText("Jumlah");
        product_field.setText("Rp. " + getIntent().getExtras().getString("amount"));


        progressDialog = new ProgressDialog(TransferConfirmationActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.waitingSms));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));

        cancel = (Button) findViewById(R.id.cancel);
        confirmation = (Button) findViewById(R.id.next);

        back = (LinearLayout) findViewById(R.id.back_layout);

        title.setTypeface(Utility.Robot_Regular(TransferConfirmationActivity.this));
        heading.setTypeface(Utility.Robot_Regular(TransferConfirmationActivity.this));
        name.setTypeface(Utility.Robot_Regular(TransferConfirmationActivity.this));
        name_field.setTypeface(Utility.Robot_Light(TransferConfirmationActivity.this));
        number.setTypeface(Utility.Robot_Regular(TransferConfirmationActivity.this));
        number_field.setTypeface(Utility.Robot_Light(TransferConfirmationActivity.this));
        amount.setTypeface(Utility.Robot_Regular(TransferConfirmationActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(TransferConfirmationActivity.this));
        products.setTypeface(Utility.Robot_Regular(TransferConfirmationActivity.this));
        product_field.setTypeface(Utility.Robot_Light(TransferConfirmationActivity.this));
        cancel.setTypeface(Utility.Robot_Regular(TransferConfirmationActivity.this));
        confirmation.setTypeface(Utility.Robot_Regular(TransferConfirmationActivity.this));

        /**
         cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        try {
        unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
        e.printStackTrace();
        }
        try {
        handlerforTimer.removeCallbacks(runnableforExit);
        } catch (Exception e) {
        e.printStackTrace();
        }
        Intent intent = getIntent();
        setResult(11, intent);
        finish();
        }
        });
         **/

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransferConfirmationActivity.this.finish();
            }
        });

        confirmation.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                nextpressedornot = true;
                if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {

                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP) {
                        if ((checkCallingOrSelfPermission(android.Manifest.permission.READ_SMS)
                                != PackageManager.PERMISSION_GRANTED) && checkCallingOrSelfPermission(Manifest.permission.RECEIVE_SMS)
                                != PackageManager.PERMISSION_GRANTED) {

                            requestPermissions(new String[]{Manifest.permission.READ_SMS, android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.SEND_SMS},
                                    109);
                        } else {
                            //handlerforTimer.removeCallbacks(runnableforExit);
                            //TimerCount timerCount=new TimerCount(TransferConfirmationActivity.this,getIntent().getExtras().getString("sctlID"));
                            //timerCount.SMSAlert("");
                            new requestOTPAsyncTask().execute();
                        }
                    } else {
                        //handlerforTimer.removeCallbacks(runnableforExit);
                        //TimerCount timerCount=new TimerCount(TransferConfirmationActivity.this,getIntent().getExtras().getString("sctlID"));
                        //timerCount.SMSAlert("");
                        new requestOTPAsyncTask().execute();
                    }
                } else {
//                    if (Timervalueout) {
//                        Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), TransferConfirmationActivity.this);
//                    }else{
                    //handlerforTimer.removeCallbacks(runnableforExit);
                    //new InterBankBankSinarmasAsynTask().execute();
//                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /**
         back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        try {
        unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
        e.printStackTrace();
        }
        try {
        handlerforTimer.removeCallbacks(runnableforExit);
        } catch (Exception e) {
        e.printStackTrace();
        }
        Intent intent = getIntent();
        setResult(11, intent);
        finish();
        }
        });
         handlerforTimer.postDelayed(runnableforExit, 90000);
         **/
    }

    private void showOTPRequiredDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullParent = null;
        View dialoglayout = inflater.inflate(R.layout.new_otp_dialog, nullParent, false);
        dialogBuilder = new AlertDialog.Builder(TransferConfirmationActivity.this, R.style.MyAlertDialogStyle).create();
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
                    if(otpValue==null){
                        otpValue=edt.getText().toString();
                    }
                    String account = sharedPreferences.getString("useas","");
                    if(account.equals("Bank")) {
                        new InterBankBankSinarmasAsynTask().execute();
                    }else{
                        new TransferemoneyConfirmationAsyncTask().execute();
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
                    if(otpValue==null){
                        otpValue=edt.getText().toString();
                    }

                    String account = sharedPreferences.getString("useas","");
                    if(account.equals("Bank")) {
                        new InterBankBankSinarmasAsynTask().execute();
                    }else{
                        new TransferemoneyConfirmationAsyncTask().execute();
                    }
                    //new TransferEmoneyConfirmationActivity.TransferConfirmationAsyncTask().execute();

                }

            }
        });
        dialogBuilder.show();
    }

    public void errorOTP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TransferConfirmationActivity.this, R.style.MyAlertDialogStyle);
        builder.setCancelable(false);
        /**
        if (selectedLanguage.equalsIgnoreCase("ENG")) {
            builder.setTitle(getResources().getString(R.string.eng_otpfailed));
            builder.setMessage(getResources().getString(R.string.eng_desc_otpfailed)).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            settings2 = getSharedPreferences(LOG_TAG, 0);
                            settings2.edit().putString("ActivityName", "ExitConfirmationScreen").apply();
                            isExitActivity = true;
                            dialog.dismiss();
                        }
                    });
        } else {
         **/
            builder.setTitle(getResources().getString(R.string.bahasa_otpfailed));
            builder.setMessage(getResources().getString(R.string.bahasa_desc_otpfailed)).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            settings2 = getSharedPreferences(LOG_TAG, 0);
                            settings2.edit().putString("ActivityName", "ExitConfirmationScreen").apply();
                            isExitActivity = true;
                            dialog.dismiss();
                            dialogBuilder.dismiss();
                        }
                    });
        //}
        alertError = builder.create();
        if (!isFinishing()) {
            alertError.show();
        }
    }

    @Override
    public void onReadSMS(String otp) {
        Log.d(LOG_TAG, "otp from SMS: " + otp);
        edt.setText(otp);
        otpValue=otp;
    }

    /**
     Handler handler12 = new Handler();
     Runnable runnable12 = new Runnable() {
    @Override
    public void run() {
    handlerforTimer.removeCallbacks(runnableforExit);
    new InterBankBankSinarmasAsynTask().execute();
    }
    };
     **/

    class TransferemoneyConfirmationAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
            sharedPreferences.edit().putString("profileId", "").apply();
            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put("txnName", "Transfer");
            Log.d(LOG_TAG,"txnName Transfer");
            mapContainer.put("service", "Wallet");
            Log.d(LOG_TAG,"service Wallet");
            mapContainer.put("institutionID", Constants.CONSTANT_INSTITUTION_ID);
            Log.d(LOG_TAG,"institutionID ");
            mapContainer.put("authenticationKey", "");
            Log.d(LOG_TAG,"authenticationKey ");
            mapContainer.put("sourceMDN", sharedPreferences.getString("mobileNumber", ""));
            Log.d(LOG_TAG,"sourceMDN "+sharedPreferences.getString("mobileNumber", ""));
            //mapContainer.put("sourcePIN", sharedPreferences.getString("mobileNumber", ""));
            //Log.d(LOG_TAG,"sourcePIN "+sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put("destMDN", "");
            Log.d(LOG_TAG,"destMDN "+"");
            mapContainer.put("destBankAccount", stMDN);
            Log.d(LOG_TAG,"destBankAccount "+stMDN);
            mapContainer.put("transferID", stTransferID);
            Log.d(LOG_TAG,"transferID "+stTransferID);
            mapContainer.put("bankID", "");
            Log.d(LOG_TAG,"bankID ");
            mapContainer.put("parentTxnID", stParentTxnID);
            Log.d(LOG_TAG,"parentTxnID "+stParentTxnID);
            mapContainer.put(Constants.PARAMETER_CONFIRMED,Constants.CONSTANT_VALUE_TRUE);
            Log.d(LOG_TAG,"confirmed true");
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);
            Log.d(LOG_TAG,"channelID 7");
            mapContainer.put("sourcePocketCode", "1");
            Log.d(LOG_TAG,"sourcePocketCode 1");
            mapContainer.put("destPocketCode", "2");
            Log.d(LOG_TAG,"destPocketCode 2");
            if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                mapContainer.put(Constants.PARAMETER_MFA_OTP, CryptoService.encryptWithPublicKey(module, exponent, otpValue.getBytes()));
            }else{
                mapContainer.put(Constants.PARAMETER_MFA_OTP, "");
            }
            Log.d(LOG_TAG,"mfaOtp "+CryptoService.encryptWithPublicKey(module, exponent, otpValue.getBytes()));
            Log.d(LOG_TAG,"otp "+ otpValue);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    TransferConfirmationActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(TransferConfirmationActivity.this);
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
                            alertbox = new AlertDialog.Builder(TransferConfirmationActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(TransferConfirmationActivity.this, SecondLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                            alertbox.show();
                            dialogBuilder.dismiss();
                        }else if(msgCode==703){
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            Intent intent = new Intent(TransferConfirmationActivity.this, TransferEmoneyNotificationActivity.class);
                            intent.putExtra("destmdn", stMDN);
                            intent.putExtra("amount", stAmount);
                            intent.putExtra("destName", stFullname);
                            intent.putExtra("transactionID", responseDataContainer.getSctl());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            dialogBuilder.dismiss();
                        }else{
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            alertbox = new AlertDialog.Builder(TransferConfirmationActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });
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

    class requestOTPAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

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

            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    TransferConfirmationActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(TransferConfirmationActivity.this);
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
                            alertbox = new AlertDialog.Builder(TransferConfirmationActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(TransferConfirmationActivity.this, SecondLoginActivity.class);
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
                            alertbox = new AlertDialog.Builder(TransferConfirmationActivity.this, R.style.MyAlertDialogStyle);
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
            }
        }
    }

    class InterBankBankSinarmasAsynTask extends AsyncTask<Void, Void, Void> {
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_TRANSFER);
            mapContainer.put(Constants.PARAMETER_BANK_ID, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_TRANSFER_ID, getIntent().getExtras().getString("transferID"));
            mapContainer.put(Constants.PARAMETER_PARENTTXN_ID, getIntent().getExtras().getString("ParentId"));
            mapContainer.put(Constants.PARAMETER_DEST_BankAccount, getIntent().getExtras().getString("DestMDN"));
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION_CONFIRM);
            mapContainer.put(Constants.PARAMETER_CONFIRMED, Constants.CONSTANT_VALUE_TRUE);
            if (sharedPreferences.getInt("userType", -1) == 0) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, Constants.POCKET_CODE_BANK_SINARMAS_BANKCODE);
            } else if (sharedPreferences.getInt("userType", -1) == 1) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
                mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, Constants.POCKET_CODE_BANK_SINARMAS_BANKCODE);
            } else if (sharedPreferences.getInt("userType", -1) == 2) {
                if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_AGENT);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                    mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, Constants.POCKET_CODE_BANK_SINARMAS_BANKCODE);
                } else {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, Constants.POCKET_CODE_BANK_SINARMAS_BANKCODE);
                }
            }
            if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                String module = sharedPreferences.getString("MODULE", "NONE");
                String exponent = sharedPreferences.getString("EXPONENT", "NONE");
                try {
                    OTP = CryptoService.encryptWithPublicKey(module, exponent,
                            otpValue.getBytes());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                mapContainer.put(Constants.PARAMETER_MFA_OTP, OTP);
            } else {
                mapContainer.put(Constants.PARAMETER_MFA_OTP, "");
            }
            Log.e("=======","--------"+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, TransferConfirmationActivity.this);

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
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 631) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    //Intent intent = new Intent(TransferConfirmationActivity.this, SessionTimeOutActivity.class);
                    //startActivityForResult(intent, 40);
                    alertbox = new AlertDialog.Builder(TransferConfirmationActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(responseContainer.getMsg());
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                            Intent intent = new Intent(TransferConfirmationActivity.this, SecondLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    alertbox.show();
                    dialogBuilder.dismiss();
                } else if (msgCode == 293) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(TransferConfirmationActivity.this, TranaferSuccessActivity.class);
                    intent.putExtra("amount", getIntent().getExtras().getString("amount"));
                    intent.putExtra("DestMDN", getIntent().getExtras().getString("DestMDN"));
                    intent.putExtra("transferID", responseContainer.getEncryptedTransferId());
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("Name", getIntent().getExtras().getString("Name"));
                    startActivityForResult(intent, 10);
                    dialogBuilder.dismiss();
                } else if (msgCode == 703) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(TransferConfirmationActivity.this, TranaferSuccessActivity.class);
                    intent.putExtra("amount", getIntent().getExtras().getString("amount"));
                    intent.putExtra("DestMDN", getIntent().getExtras().getString("DestMDN"));
                    intent.putExtra("transferID", responseContainer.getEncryptedTransferId());
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("Name", getIntent().getExtras().getString("Name"));
                    startActivityForResult(intent, 10);
                    dialogBuilder.dismiss();
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
                                TransferConfirmationActivity.this);
                        dialogBuilder.dismiss();
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), TransferConfirmationActivity.this);
                        dialogBuilder.dismiss();
                    }
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), TransferConfirmationActivity.this);
                dialogBuilder.dismiss();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 109) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //handlerforTimer.removeCallbacks(runnableforExit);
                //TimerCount timerCount=new TimerCount(TransferConfirmationActivity.this,getIntent().getExtras().getString("sctlID"));
                //timerCount.SMSAlert("");
            } else {

            }
        }
    }

//    ChangePinTimerCount timerCount;

    /**
    public void displayDialog(String msg, Context ctx) {


        final Dialog dialog = new Dialog(TransferConfirmationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.resend_otp_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        TextView TextView1 = (TextView) dialog.findViewById(R.id.number_1);
        TextView1.setText(msg);

        TextView textView=(TextView)dialog.findViewById(R.id.number);

        textView.setTypeface(Utility.Robot_Regular(ctx));
        TextView1.setTypeface(Utility.Robot_Light(ctx));

        textView.setText(ctx.getResources().getString(R.string.dailog_heading));


        Button okay = (Button) dialog.findViewById(R.id.OK);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Cancel();
            }
        });
        dialog.show();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if(intent.getExtras().getString("value").equalsIgnoreCase("0")){
                    Cancel();
                }else if(intent.getExtras().getString("value").equalsIgnoreCase("1")) {
                    otpValue = intent.getExtras().getString("otpValue");
                    new InterBankBankSinarmasAsynTask().execute();
                }else if(intent.getExtras().getString("value").equalsIgnoreCase("2")){
                    Utility.TransactionsdisplayDialog("Silakan masukkan kode OTP sebelum batas waktu yang ditentukan.",TransferConfirmationActivity.this);
                }else if(intent.getExtras().getString("value").equalsIgnoreCase("3")){
                    Utility.TransactionsdisplayDialog(intent.getExtras().getString("otpValue"),TransferConfirmationActivity.this);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    void Cancel(){
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            handlerforTimer.removeCallbacks(runnableforExit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent1 = getIntent();
        setResult(10, intent1);
        finish();
    }
    Handler handlerforTimer = new Handler();

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Runnable runnableforExit = new Runnable() {
        @Override
        public void run() {
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            Timervalueout = true;
//            Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), TransferConfirmationActivity.this);

        }
    };
    **/

    @Override
    protected void onResume() {
        super.onResume();
        //registerReceiver(broadcastReceiver, new IntentFilter("com.send"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /**
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            handlerforTimer.removeCallbacks(runnableforExit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        setResult(11, intent);
        finish();
         **/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 10) {
                Intent intent = getIntent();
                setResult(10, intent);
                finish();
            } else if (requestCode == 40) {
                if (resultCode == RESULT_OK) {

                }
            }
        }

        /**
         public void SMSAlert(final String string) {

         dialogCustomWish = new Dialog(context);
         dialogCustomWish.setCancelable(false);

         dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
         dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


         View view = LayoutInflater.from(context).inflate(R.layout.sms_alert, null);
         dialogCustomWish.setContentView(R.layout.sms_alert);

         Button button = (Button) dialogCustomWish.findViewById(R.id.ok);
         Button button1 = (Button) dialogCustomWish.findViewById(R.id.Cancel);
         TextView textView = (TextView) dialogCustomWish.findViewById(R.id.number);
         TextView textView_1 = (TextView) dialogCustomWish.findViewById(R.id.number_1);
         button.setTypeface(Utility.RegularTextFormat(context));
         button1.setTypeface(Utility.RegularTextFormat(context));
         textView.setTypeface(Utility.RegularTextFormat(context));


         textView_1.setText("Kode OTP dan link telah dikirimkan ke nomor " + sharedPreferences.getString("mobileNumber", "") + ". Masukkan kode tersebut atau akses link yang tersedia.");


         EditText editText = (EditText) dialogCustomWish.findViewById(R.id.otpCode);
         editText.setHint("6 digit kode OTP");
         editText.setText(string);
         textView_1.setTypeface(Utility.Robot_Regular(context));


         button1.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {

        dialogCustomWish.dismiss();

        }
        });

         button.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        dialogCustomWish.dismiss();

        handler12.postDelayed(runnable12, 1000);

        }
        });
         dialogCustomWish.show();
         }**/
    }
}
