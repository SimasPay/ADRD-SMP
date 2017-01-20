package com.payment.simaspay.userdetails;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
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
import simaspay.payment.com.simaspay.UserHomeActivity;

/**
 * Created by Nagendra P on 4/27/2016.
 * Widy Agung Priasmoro on 12/20/2016
 */
public class SecondLoginActivity extends Activity {
    private static final String LOG_TAG = "SimasPay";
    TextView txt_1, txt_2, number, contact_us;
    Context context;
    SharedPreferences sharedPreferences;
    EditText e_mPin;
    int msgCode;

    String pin;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (resultCode != Activity.RESULT_OK) {
                SecondLoginActivity.this.finish();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_login);
        context = SecondLoginActivity.this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            final int version = Build.VERSION.SDK_INT;
            if (version >= 23) {
                window.setStatusBarColor(ContextCompat.getColor(context, R.color.splashscreen));
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
            }
            //window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        e_mPin = (EditText) findViewById(R.id.mpin);

        contact_us = (TextView) findViewById(R.id.contact_us);
        txt_1 = (TextView) findViewById(R.id.txt_1);
        txt_2 = (TextView) findViewById(R.id.txt_2);
        number = (TextView) findViewById(R.id.txt_3);


        contact_us.setTypeface(Utility.Robot_Light(SecondLoginActivity.this));
        e_mPin.setTypeface(Utility.Robot_Light(SecondLoginActivity.this));
        txt_1.setTypeface(Utility.Robot_Light(SecondLoginActivity.this));
        txt_2.setTypeface(Utility.Robot_Light(SecondLoginActivity.this));
        number.setTypeface(Utility.Robot_Regular(SecondLoginActivity.this));

        number.setText(sharedPreferences.getString("mobileNumber",""));

        String htmlString = "<u>Hubun</u>" + "g" + "<u>i Kami</u>";
        contact_us.setText(Html.fromHtml(htmlString));

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondLoginActivity.this, ContactUs_Activity.class);
                startActivity(intent);
            }
        });

        e_mPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    pin = e_mPin.getText().toString();
                    sharedPreferences.edit().putString("mpin", e_mPin.getText().toString()).apply();
                    nextProcess();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    void nextProcess() {
        new LoginAsynTask().execute();
    }


    @Override
    protected void onResume() {
        super.onResume();
       /* e_mPin.requestFocus();

        e_mPin.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(e_mPin, 0);
            }
        },200);*/
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
            sharedPreferences.edit().putString("profileId", "").apply();
            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

            try {
                rsaKey = CryptoService.encryptWithPublicKey(module, exponent,
                        pin.getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_LOGIN);
            mapContainer.put("institutionID","simaspay");
            mapContainer.put("authenticationKey","f");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_STRING, rsaKey);
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SIMASPAYACTIVITY, Constants.CONSTANT_VALUE_TRUE);
            mapContainer.put(Constants.PARAMETER_APPOS, AppConfigFile.appOS);
            mapContainer.put("apptype","");
            //mapContainer.put(Constants.PARAMETER_DEVICE_MODEL, deviceModel);
            mapContainer.put(Constants.PARAMETER_APPVERSION, version);
            //mapContainer.put(Constants.PARAMETER_OS_VERSION, osVersion);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, SecondLoginActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SecondLoginActivity.this);
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
                if (msgCode == 630) {
                    progressDialog.dismiss();

                    Log.e("------", "------" + responseContainer.getUserApiKey());
                    if (responseContainer.getUserApiKey() != null) {
                        sharedPreferences.edit()
                                .putString("userApiKey", responseContainer.getUserApiKey())
                                .apply();
                    } else {
                        sharedPreferences.edit()
                                .putString("userApiKey", "")
                                .apply();
                    }
                    Log.e("------", "------" + sharedPreferences.getString("userApiKey", ""));


                    sharedPreferences.edit().putString("password", rsaKey).apply();
                    sharedPreferences.edit().putString("userName", responseContainer.getName()).apply();
                    Log.d(LOG_TAG,"responseContainer.getIsBank():"+responseContainer.getIsBank());
                    Log.d(LOG_TAG,"responseContainer.getIsEmoney():"+responseContainer.getIsEmoney());
                    Log.d(LOG_TAG,"responseContainer.getIsKyc():"+responseContainer.getIsKyc());
                    if(responseContainer.getIsBank().equals("true")){
                        Log.d(LOG_TAG, "yes true");
                    }
                    if(responseContainer.getIsBank().equals("true") && responseContainer.getIsEmoney().equals("false") && responseContainer.getIsKyc().equals("true")){
                        Log.d(LOG_TAG, "bank account");
                        sharedPreferences.edit().putString("akun", "bank").apply();
                    }else if(responseContainer.getIsBank().equals("false")&&responseContainer.getIsEmoney().equals("true")&&responseContainer.getIsKyc().equals("false")){
                        Log.d(LOG_TAG, "emoney non KYC");
                        sharedPreferences.edit().putString("akun", "nonkyc").apply();
                    }else if(responseContainer.getIsBank().equals("true")&&responseContainer.getIsEmoney().equals("true")&&responseContainer.getIsKyc().equals("true")){
                        Log.d(LOG_TAG, "both");
                        sharedPreferences.edit().putString("akun", "both").apply();
                    }
                    e_mPin.setText("");




                    if (responseContainer.getCustomerType().equals("0")) {


                        if(responseContainer.getIsBank().equals("true")&&responseContainer.getIsEmoney().equals("true")&&responseContainer.getIsKyc().equals("true")) {
                            sharedPreferences.edit().putInt("userType", 1).apply();
                            sharedPreferences.edit().putInt("userType", 0).apply();
                            sharedPreferences.edit().putString("accountnumber", responseContainer.getBankAccountNumber()).apply();
                            Intent intent = new Intent(SecondLoginActivity.this, NumberSwitchingActivity.class);
                            startActivityForResult(intent, 20);
                        }else{
                            sharedPreferences.edit().putInt("userType", 0).apply();
                            sharedPreferences.edit().putString("accountnumber", responseContainer.getBankAccountNumber()).apply();
                            Intent intent = new Intent(SecondLoginActivity.this, UserHomeActivity.class);
                            startActivityForResult(intent, 20);
                        }

                        /**
                        if (responseContainer.getIsBank().equalsIgnoreCase("true")) {
                            sharedPreferences.edit().putInt("userType", 0).apply();
                            sharedPreferences.edit().putString("accountnumber", responseContainer.getBankAccountNumber()).apply();
                            //Intent intent = new Intent(SecondLoginActivity.this, SimaspayUserActivity.class);
                            Intent intent = new Intent(SecondLoginActivity.this, UserHomeActivity.class);
                            startActivityForResult(intent, 20);
                        } else {
                            sharedPreferences.edit().putInt("userType", 1).apply();
                            //Intent intent = new Intent(SecondLoginActivity.this, LakuPandaiActivity.class);

                        }
                         **/

                    } else if (responseContainer.getCustomerType().equals("2")) {
                        sharedPreferences.edit().putInt("userType", 2).apply();
                        Intent intent = new Intent(SecondLoginActivity.this, NumberSwitchingActivity.class);
                        sharedPreferences.edit().putString("accountnumber", responseContainer.getBankAccountNumber()).apply();
                        startActivityForResult(intent, 20);
                    }
                } else {
                    progressDialog.dismiss();
                    if (responseContainer.getMsg() == null) {
                        Utility.networkDisplayDialog(
                                sharedPreferences.getString(
                                        "ErrorMessage",
                                        getResources()
                                                .getString(
                                                        R.string.server_error_message)),
                                SecondLoginActivity.this);
                        e_mPin.setText("");
                    } else {
                        e_mPin.setText("");
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), SecondLoginActivity.this);
                    }
                }


            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), SecondLoginActivity.this);
            }
        }
    }
}
