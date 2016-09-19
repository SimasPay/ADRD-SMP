package com.payment.simaspay.UserActivation;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 12/22/2015.
 */
public class ActivationPage_2_Activity extends Activity {

    TextView text_1, text_2, text_3, text_4, text_5, text_6, text_7;


    EditText e_Mdn, e_mPin;
    ProgressDialog progressDialog;

    Button lanjut;
    Dialog dialogCustomWish;
    Context context;

    String idnumber, otpValue, sctl;

    String rsaKey, mobileNumber, otp, name;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String body = intent.getExtras().getString("message");
                if (body.contains("Kode OTP Simaspay anda")) {

                    otpValue = body
                            .substring(
                                    body.indexOf("Kode OTP Simaspay anda : ")
                                            + new String(
                                            "Kode OTP Simaspay anda : ")
                                            .length(),
                                    body.indexOf(". ")).trim();
                    handler.removeCallbacks(runnable);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent1 = new Intent(ActivationPage_2_Activity.this, ActivationPage_3_Activity.class);
                    intent1.putExtra("SctlID", idnumber);
                    intent1.putExtra("mailedOtp", otp);
                    intent1.putExtra("mobileNumber", mobileNumber);
                    intent1.putExtra("otpValue", otpValue);
                    intent1.putExtra("mfaMode", "OTP");
                    intent1.putExtra("name", name);
                    startActivityForResult(intent1, 20);

                } else if (body.contains("Your Simaspay code is ")) {
                    otpValue = body
                            .substring(
                                    body.indexOf("Your Simaspay code is ")
                                            + new String(
                                            "Your Simaspay code is ")
                                            .length(),
                                    body.indexOf(". ")).trim();

                    handler.removeCallbacks(runnable);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent1 = new Intent(ActivationPage_2_Activity.this, ActivationPage_3_Activity.class);
                    intent1.putExtra("SctlID", idnumber);
                    intent1.putExtra("mailedOtp", otp);
                    intent1.putExtra("name", name);
                    intent1.putExtra("mobileNumber", mobileNumber);
                    intent1.putExtra("mfaMode", "OTP");
                    intent1.putExtra("otpValue", otpValue);
                    startActivityForResult(intent1, 20);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SMSAlert("");
        }
    };
    Handler handler = new Handler();

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("com.msg.simaspay"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_2);
        context = this;

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }
        e_Mdn = (EditText) findViewById(R.id.hand_phno);
        e_mPin = (EditText) findViewById(R.id.otp);
        text_1 = (TextView) findViewById(R.id.text_1);
        text_2 = (TextView) findViewById(R.id.text_2);
        text_3 = (TextView) findViewById(R.id.text_3);
        text_4 = (TextView) findViewById(R.id.text_4);
        text_5 = (TextView) findViewById(R.id.text_5);
        text_6 = (TextView) findViewById(R.id.text_6);
        text_7 = (TextView) findViewById(R.id.text_7);

        e_Mdn.setTypeface(Utility.Robot_Light(ActivationPage_2_Activity.this));
        e_mPin.setTypeface(Utility.Robot_Light(ActivationPage_2_Activity.this));

        e_mPin.setHint("6 digit kode aktivasi");

        text_1.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_2.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_3.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_4.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_5.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_6.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_7.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));


        String htmlString1 = "<u>Kirim Ulan</u>" + "g";
        text_5.setText(Html.fromHtml(htmlString1));

        String htmlString = "<u>Lo</u>" + "g" + "<u>in</u>";
        text_7.setText(Html.fromHtml(htmlString));

        progressDialog = new ProgressDialog(ActivationPage_2_Activity.this);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
        progressDialog.setCancelable(false);

        lanjut = (Button) findViewById(R.id.next);


        text_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean networkCheck = Utility.isConnectingToInternet(context);
                if (!networkCheck) {
                    Utility.networkDisplayDialog(
                            getResources().getString(
                                    R.string.bahasa_serverNotRespond), context);

                } else if (e_Mdn.getText().toString().equals("")) {
                    Utility.networkDisplayDialog("SimasPay Harap masukkan nomor handphone Anda", ActivationPage_2_Activity.this);
                } else if (e_Mdn.getText().toString().replace(" ", "")
                        .length() < 10) {
                    Utility.networkDisplayDialog("SimasPay Nomor handphone yang Anda masukkan harus 10-14 angka.",
                            ActivationPage_2_Activity.this);
                } else {

                    mobileNumber = e_Mdn.getText().toString();
                    new ResendOtpAsyn().execute();
                }

            }
        });

        lanjut.setTypeface(Utility.Robot_Regular(ActivationPage_2_Activity.this));

        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean networkCheck = Utility.isConnectingToInternet(context);
                if (!networkCheck) {
                    Utility.networkDisplayDialog(
                            getResources().getString(
                                    R.string.bahasa_serverNotRespond), context);

                } else if (e_Mdn.getText().toString().equals("")) {
                    Utility.networkDisplayDialog("Masukkan Nomor Handphone", ActivationPage_2_Activity.this);
                } else if (e_Mdn.getText().toString().replace(" ", "")
                        .length() < 10) {
                    Utility.networkDisplayDialog("Nomor Handphone harus lebih dari 10 angka",
                            ActivationPage_2_Activity.this);
                } else if (e_mPin.getText().toString().equals("")) {
                    Utility.networkDisplayDialog("'SimasPay Harap masukkan kode aktivasi Anda.", ActivationPage_2_Activity.this);
                }  else if (e_mPin.getText().toString().replace(" ", "")
                        .length() < 6) {
                    Utility.networkDisplayDialog("Kode aktivasi yang Anda masukkan harus 6 angka.",
                            ActivationPage_2_Activity.this);
                }else {

                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP) {
                        if ((checkCallingOrSelfPermission(android.Manifest.permission.READ_SMS)
                                != PackageManager.PERMISSION_GRANTED) && checkCallingOrSelfPermission(Manifest.permission.RECEIVE_SMS)
                                != PackageManager.PERMISSION_GRANTED) {

                            requestPermissions(new String[]{Manifest.permission.READ_SMS, android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.SEND_SMS},
                                    109);
                        } else {
                            mobileNumber = e_Mdn.getText().toString();
                            otp = e_mPin.getText().toString();
                            new ActivationAsyn().execute();
                        }
                    } else {
                        mobileNumber = e_Mdn.getText().toString();
                        otp = e_mPin.getText().toString();
                        new ActivationAsyn().execute();
                    }
                }

            }
        });


        text_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                setResult(13, i);
                finish();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                Intent i = getIntent();
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = getIntent();
        setResult(Activity.RESULT_CANCELED, i);
        finish();
    }

    public void ResendOtp(final String string) {

        dialogCustomWish = new Dialog(context);
        dialogCustomWish.setCancelable(false);

        dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        View view = LayoutInflater.from(context).inflate(R.layout.resend_otp_dialog, null);
        dialogCustomWish.setContentView(R.layout.resend_otp_dialog);

        Button button = (Button) dialogCustomWish.findViewById(R.id.OK);
        TextView textView = (TextView) dialogCustomWish.findViewById(R.id.number);
        TextView textView1 = (TextView) dialogCustomWish.findViewById(R.id.number_1);
        button.setTypeface(Utility.HelveticaNeue_Medium(context));
        textView.setTypeface(Utility.HelveticaNeue_Medium(context));
        textView1.setTypeface(Utility.LightTextFormat(context));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCustomWish.dismiss();
            }
        });
        dialogCustomWish.show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 109) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mobileNumber = e_Mdn.getText().toString();
                otp = e_mPin.getText().toString();
                new ActivationAsyn().execute();
            } else {

            }
        }
    }

    int msgCode;

    class ActivationAsyn extends AsyncTask<Void, Void, Void> {

        String response;

        @Override
        protected Void doInBackground(Void... params) {
            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

            try {
                rsaKey = CryptoService.encryptWithPublicKey(module, exponent,
                        otp.getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_ACTIVATION);
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, mobileNumber);
            mapContainer.put(Constants.PARAMETER_OTP, rsaKey);
            mapContainer.put(Constants.TRANSACTION_ISSIMASPAYACTIVITY, Constants.CONSTANT_VALUE_TRUE);
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION);
            mapContainer.put(Constants.PARAMETER_ACTIVATION_CONFIRMPIN, "");
            mapContainer.put(Constants.PARAMETER_ACTIVATION_NEWPIN, "");

            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, ActivationPage_2_Activity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivationPage_2_Activity.this);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (response != null) {
                Log.e("------------", "------" + response);
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
                if (msgCode == 2040) {
                    if (responseContainer.getMfaMode().equalsIgnoreCase("OTP")) {
                        idnumber = responseContainer.getSctl();
                        name = responseContainer.getName();
                        handler.postDelayed(runnable, 60000);
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Intent intent1 = new Intent(ActivationPage_2_Activity.this, ActivationPage_3_Activity.class);
                        intent1.putExtra("SctlID", responseContainer.getSctl());
                        intent1.putExtra("mailedOtp", otp);
                        intent1.putExtra("name", responseContainer.getName());
                        intent1.putExtra("mobileNumber", mobileNumber);
                        intent1.putExtra("mfaMode", "None");
                        startActivityForResult(intent1, 20);
                    }

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
                                ActivationPage_2_Activity.this);
                        e_mPin.setText("");
                    } else {
                        e_mPin.setText("");
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), ActivationPage_2_Activity.this);
                    }
                }

            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), ActivationPage_2_Activity.this);
            }
        }
    }

    class ResendOtpAsyn extends AsyncTask<Void, Void, Void> {
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, mobileNumber);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_RESEND_OTP);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, ActivationPage_2_Activity.this);

            response = webServiceHttp.getResponseSSLCertificatation();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null) {
                Log.e("--------", "------" + response);
                progressDialog.dismiss();
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

                if (msgCode == 0) {
                    ResendOtp("");
                } else {
                    Utility.networkDisplayDialog(responseContainer.getMsg(), ActivationPage_2_Activity.this);
                }

            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), ActivationPage_2_Activity.this);
            }
        }
    }


    Button button;
    TextView textView1;
    ProgressBar progressBar;
    EditText editText;

    public void SMSAlert(final String string) {


        dialogCustomWish = new Dialog(context);
        dialogCustomWish.setCancelable(false);

        dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        View view = LayoutInflater.from(context).inflate(R.layout.sms_alert, null);
        dialogCustomWish.setContentView(R.layout.sms_alert);

        button = (Button) dialogCustomWish.findViewById(R.id.ok);
        Button button1 = (Button) dialogCustomWish.findViewById(R.id.Cancel);
        final TextView textView = (TextView) dialogCustomWish.findViewById(R.id.number);
        TextView textView_1 = (TextView) dialogCustomWish.findViewById(R.id.number_1);
        button.setTypeface(Utility.RegularTextFormat(context));
        button1.setTypeface(Utility.RegularTextFormat(context));
        textView.setTypeface(Utility.RegularTextFormat(context));

        textView1 = (TextView) dialogCustomWish.findViewById(R.id.timer);
        editText = (EditText) dialogCustomWish.findViewById(R.id.otpCode);

        progressBar = (ProgressBar) dialogCustomWish.findViewById(R.id.progressbar);
        textView_1.setText("Mohon menunggu selagi kami melakukan verifikasi OTP secara otomatis ke nomor " + mobileNumber + " atau silakan masukkan kode secara manual jika verifikasi otomatis gagal.");


        textView1.setText("Kirim Ulang");
        textView1.setTextColor(context.getResources().getColor(R.color.bg_color_h));
        editText.setEnabled(true);
        button.setEnabled(false);

        editText.setHint("6 digit kode OTP");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    button.setEnabled(false);
                    progressBar.setVisibility(View.GONE);
                    button.setTextColor(context.getResources().getColor(R.color.ok_disablecolor));
                } else {
                    button.setEnabled(true);
                    button.setTextColor(context.getResources().getColor(R.color.bg_color_h));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        textView_1.setTypeface(Utility.Robot_Regular(context));


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogCustomWish.dismiss();
                Intent i = getIntent();
                setResult(Activity.RESULT_CANCELED, i);
                finish();


            }
        });

        button.setEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText.getText().toString().length()<6){
                    Utility.displayDialog("Kode OTP yang Anda masukkan harus 6 angka.", context);
                }else {
                    dialogCustomWish.dismiss();
                    Intent intent1 = new Intent(ActivationPage_2_Activity.this, ActivationPage_3_Activity.class);
                    intent1.putExtra("SctlID", idnumber);
                    intent1.putExtra("mailedOtp", otp);
                    intent1.putExtra("mobileNumber", mobileNumber);
                    intent1.putExtra("otpValue", editText.getText().toString());
                    intent1.putExtra("mfaMode", "OTP");
                    intent1.putExtra("name", name);
                    startActivityForResult(intent1, 20);
                }
            }
        });


        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView1.getText().toString().equalsIgnoreCase("Kirim Ulang")) {
                    dialogCustomWish.dismiss();
                    new MFAResendOTPAsyn().execute();
                }
            }
        });
        dialogCustomWish.show();

    }


    String response;

    class MFAResendOTPAsyn extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_RESENDMFAOTP);
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN,mobileNumber);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
            mapContainer.put(Constants.PARAMETER_SCTLID, idnumber);

            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, context);

            response = webServiceHttp.getResponseSSLCertificatation();
            Log.e("-------------","------------"+mapContainer.toString());
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
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

                if (msgCode == 2171) {
                    dialogCustomWish.dismiss();
                    progressDialog.dismiss();
                    handler.postDelayed(runnable, 30000);
                } else if (msgCode == 2172) {
                    dialogCustomWish.dismiss();
                    progressDialog.dismiss();
                    Utility.displayDialog(responseContainer.getMsg(), ActivationPage_2_Activity.this);

                } else if (msgCode == 2173) {
                    dialogCustomWish.dismiss();
                    progressDialog.dismiss();
                    Utility.displayDialog(responseContainer.getMsg(), ActivationPage_2_Activity.this);
                }else{
                    dialogCustomWish.dismiss();
                    progressDialog.dismiss();
                    Utility.displayDialog(responseContainer.getMsg(), ActivationPage_2_Activity.this);
                }
            } else {
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        context.getResources().getString(
                                R.string.bahasa_serverNotRespond)), context);
                dialogCustomWish.dismiss();
                progressDialog.dismiss();
            }
        }
    }
}
