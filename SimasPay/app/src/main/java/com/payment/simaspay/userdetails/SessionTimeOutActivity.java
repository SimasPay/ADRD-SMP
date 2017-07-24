package com.payment.simaspay.userdetails;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.UserActivation.ActivationPage_1_Activity;
import com.payment.simaspay.agentdetails.NumberSwitchingActivity;
import com.payment.simaspay.contactus.ContactUs_Activity;
import com.payment.simaspay.lakupandai.LakuPandaiActivity;
import com.payment.simaspay.services.AppConfigFile;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 3/9/2016.
 */
public class SessionTimeOutActivity extends Activity {

    EditText e_Mdn, e_mPin;
    Button login, register;

    String countryCode = "62", mobileNumber;

    TextView contact_us, simas, atau;

    Context context;

    SharedPreferences sharedPreferences;

    String pin;

    int msgCode = 0;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        context = SessionTimeOutActivity.this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        e_Mdn = (EditText) findViewById(R.id.hand_phno);
        e_mPin = (EditText) findViewById(R.id.mpin);

        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.activation);
        atau = (TextView) findViewById(R.id.or);

        simas = (TextView) findViewById(R.id.simas);
//        simas.setTextSize(getResources().getDimensionPixelSize(R.dimen.txt));

        e_Mdn.setText(sharedPreferences.getString("mobileNumber",""));
        e_Mdn.setClickable(false);
        e_Mdn.setFocusable(false);

        login.setTypeface(Utility.Robot_Regular(SessionTimeOutActivity.this));
        register.setTypeface(Utility.Robot_Regular(SessionTimeOutActivity.this));
        atau.setTypeface(Utility.Robot_Light(SessionTimeOutActivity.this));


        contact_us = (TextView) findViewById(R.id.contact_us);
        contact_us.setTypeface(Utility.Robot_Light(SessionTimeOutActivity.this));
        SpannableString content = new SpannableString("Hubungi Kami");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        contact_us.setText(content);
        e_Mdn.setTypeface(Utility.Robot_Light(SessionTimeOutActivity.this));
        e_mPin.setTypeface(Utility.Robot_Light(SessionTimeOutActivity.this));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SessionTimeOutActivity.this, ActivationPage_1_Activity.class);
                startActivityForResult(intent, 20);
            }
        });

        register.setVisibility(View.GONE);
        atau.setVisibility(View.GONE);
        contact_us.setVisibility(View.GONE);
        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SessionTimeOutActivity.this, ContactUs_Activity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean networkCheck = Utility.isConnectingToInternet(context);
                if (!networkCheck) {
                    Utility.networkDisplayDialog(
                            getResources().getString(
                                    R.string.bahasa_serverNotRespond), context);

                } else if (e_Mdn.getText().toString().equals("")) {
                    Utility.displayDialog("Masukkan Nomor Handphone", SessionTimeOutActivity.this);
                } else if (e_Mdn.getText().toString().replace(" ", "")
                        .length() < 7) {
                    Utility.displayDialog("Nomor Handphone harus lebih dari 6 angka",
                            SessionTimeOutActivity.this);
                } else if (e_mPin.getText().toString().equals("")) {
                    Utility.displayDialog("Masukkan Pin Anda", SessionTimeOutActivity.this);
                } else {
                    mobileNumber = e_Mdn.getText().toString();
                    pin = e_mPin.getText().toString();
                    nextProcess();
                }
            }
        });

    }




    void nextProcess() {
        new LoginAsynTask().execute();
    }

    String rsaKey;

    class LoginAsynTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String response;


        @Override
        protected Void doInBackground(Void... params) {

            String deviceModel = null, osVersion = null, deviceManufacture;
            try {
                osVersion = android.os.Build.VERSION.RELEASE;
                deviceModel = android.os.Build.MODEL;
                deviceManufacture = android.os.Build.MANUFACTURER;
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            PackageInfo pInfo = null;
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e1) {
                e1.printStackTrace();
            }
            String version = pInfo.versionName;

            int verCode = pInfo.versionCode;
            sharedPreferences.edit().putString("profileId", "").commit();
            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

            try {
                rsaKey = CryptoService.encryptWithPublicKey(module, exponent,
                        pin.getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_LOGIN);
            if(mobileNumber.startsWith("62")) {
                mapContainer.put(Constants.PARAMETER_SOURCE_MDN, mobileNumber);
            }else if(mobileNumber.startsWith("0")){
                mapContainer.put(Constants.PARAMETER_SOURCE_MDN, "62"+mobileNumber.substring(1));
            }else{
                mapContainer.put(Constants.PARAMETER_SOURCE_MDN, "62"+mobileNumber);
            }
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_STRING, rsaKey);
            mapContainer.put(Constants.PARAMETER_APPVERSION, version);
            mapContainer.put(Constants.PARAMETER_APPOS, AppConfigFile.appOS);
            mapContainer.put(Constants.PARAMETER_DEVICE_MODEL, deviceModel);
            mapContainer.put(Constants.PARAMETER_OS_VERSION, osVersion);
            mapContainer.put(Constants.PARAMETER_SIMASPAYACTIVITY,Constants.CONSTANT_VALUE_TRUE);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, SessionTimeOutActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SessionTimeOutActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null) {
                Log.e("-------","---------"+response);
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
                if (msgCode == 630) {
                    progressDialog.dismiss();
                    try {
                        if (!responseContainer.getUserApiKey().equals(
                                "")) {
                        } else {
                            sharedPreferences.edit()
                                    .putString("userApiKey", "NONE")
                                    .commit();
                        }
                    } catch (Exception e) {

                        sharedPreferences.edit().putString("userApiKey", "NONE")
                                .commit();
                    }
                    if(mobileNumber.startsWith("62")) {
                        sharedPreferences.edit().putString("mobileNumber", mobileNumber).commit();
                    }else if(mobileNumber.startsWith("0")){
                        sharedPreferences.edit().putString("mobileNumber","62"+mobileNumber.substring(1)).commit();
                    }else{
                        sharedPreferences.edit().putString("mobileNumber","62"+mobileNumber).commit();
                    }

                    sharedPreferences.edit().putString("password", rsaKey).commit();
                    sharedPreferences.edit().putString("userName",responseContainer.getName()).commit();
                    e_mPin.setText("");
                    e_Mdn.setText("");
                    Intent intent=getIntent();
                    setResult(RESULT_OK,intent);
                    finish();
                } else {
                    progressDialog.dismiss();
                    if (responseContainer.getMsg() == null) {
                        Utility.networkDisplayDialog(
                                sharedPreferences.getString(
                                        "ErrorMessage",
                                        getResources()
                                                .getString(
                                                        R.string.server_error_message)),
                                SessionTimeOutActivity.this);
                        e_mPin.setText("");
                    } else {
                        e_mPin.setText("");
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), SessionTimeOutActivity.this);
                    }
                }


            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), SessionTimeOutActivity.this);
            }
        }
    }
}
