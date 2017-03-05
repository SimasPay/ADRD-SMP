package com.payment.simaspay.userdetails;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.payment.simaspay.agentdetails.NumberSwitchingActivity;
import com.payment.simaspay.services.AppConfigFile;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.utils.Functions;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;
import simaspay.payment.com.simaspay.UserHomeActivity;

/**
 * Created by Nagendra P on 4/27/2016.
 * Widy Agung Priasmoro on 12/20/2016
 */
public class SecondLoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SimasPay";
    TextView txt_1, txt_2, number, forgot_mpin;
    Context context;
    SharedPreferences sharedPreferences;
    EditText e_mPin;
    int msgCode=0;
    String pin, sourceMDN;
    String message, transactionTime, responseCode;
    String rsaKey;
    Functions functions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_login);
        context = SecondLoginActivity.this;
        functions = new Functions(this);
        functions.initiatedToolbar(this);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        e_mPin = (EditText) findViewById(R.id.mpin);

        forgot_mpin = (TextView) findViewById(R.id.forgot_mpin);
        txt_1 = (TextView) findViewById(R.id.txt_1);
        txt_2 = (TextView) findViewById(R.id.txt_2);
        number = (TextView) findViewById(R.id.txt_3);


        forgot_mpin.setTypeface(Utility.Robot_Light(SecondLoginActivity.this));
        forgot_mpin.setPaintFlags(forgot_mpin.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        e_mPin.setTypeface(Utility.Robot_Light(SecondLoginActivity.this));
        txt_1.setTypeface(Utility.Robot_Light(SecondLoginActivity.this));
        txt_2.setTypeface(Utility.Robot_Light(SecondLoginActivity.this));
        number.setTypeface(Utility.Robot_Regular(SecondLoginActivity.this));

        sourceMDN=sharedPreferences.getString(Constants.PARAMETER_PHONENUMBER,"");
        number.setText(sourceMDN);

        forgot_mpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new inquiryMDNValidationAsyncTask().execute();
            }
        });

        /**
        String htmlString = "<u>Hubun</u>" + "g" + "<u>i Kami</u>";
        contact_us.setText(Html.fromHtml(htmlString));

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondLoginActivity.this, ContactUs_Activity.class);
                startActivity(intent);
            }
        });
         **/

        e_mPin.requestFocus();
        e_mPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    pin = e_mPin.getText().toString();
                    sharedPreferences.edit().putString(Constants.PARAMETER_MPIN, e_mPin.getText().toString()).apply();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (resultCode != Activity.RESULT_OK) {
                SecondLoginActivity.this.finish();
            }
        }
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

    class LoginAsynTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String response;


        @Override
        protected Void doInBackground(Void... params) {

            /**
            String deviceModel = null, osVersion = null, deviceManufacture;
            try {
                osVersion = android.os.Build.VERSION.RELEASE;
                deviceModel = android.os.Build.MODEL;
                deviceManufacture = android.os.Build.MANUFACTURER;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
             **/

            PackageInfo pInfo = null;
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e1) {
                e1.printStackTrace();
            }
            String version = pInfo.versionName;

            int verCode = pInfo.versionCode;
            sharedPreferences.edit().putString("profileId", "").apply();

            rsaKey = functions.generateRSA(pin);
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_LOGIN);
            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID,Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_KEY, Constants.CONSTANT_AUTHENTICATION_KEY);
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString(Constants.PARAMETER_PHONENUMBER, ""));
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_STRING, rsaKey);
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SIMASPAYACTIVITY, Constants.CONSTANT_VALUE_TRUE);
            mapContainer.put(Constants.PARAMETER_APPOS, AppConfigFile.appOS);
            mapContainer.put(Constants.PARAMETER_APPTYPE, Constants.CONSTANTS_APPTYPE);
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
                    msgCode = Integer.parseInt(responseContainer.getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 630) {
                    progressDialog.dismiss();
                    e_mPin.setText("");

                    Log.e(LOG_TAG, "UserAPIKey: " + responseContainer.getUserApiKey());
                    if (responseContainer.getUserApiKey() != null) {
                        sharedPreferences.edit()
                                .putString("userApiKey", responseContainer.getUserApiKey())
                                .apply();
                    } else {
                        sharedPreferences.edit()
                                .putString("userApiKey", "")
                                .apply();
                    }
                    Log.e("------", "userAPIKey------" + sharedPreferences.getString("userApiKey", ""));


                    sharedPreferences.edit().putString("password", rsaKey).apply();
                    sharedPreferences.edit().putString("userName", responseContainer.getName()).apply();
                    Log.d(LOG_TAG,"responseContainer.getCustomerType():"+responseContainer.getCustomerType());
                    Log.d(LOG_TAG,"responseContainer.getIsBank():"+responseContainer.getIsBank());
                    Log.d(LOG_TAG,"responseContainer.getIsEmoney():"+responseContainer.getIsEmoney());
                    Log.d(LOG_TAG,"responseContainer.getIsKyc():"+responseContainer.getIsKyc());

                    if (responseContainer.getCustomerType().equals("0")) {
                        if(responseContainer.getIsBank().equals("true")&&responseContainer.getIsEmoney().equals("true")&&responseContainer.getIsKyc().equals("true")) {
                            sharedPreferences.edit().putInt(Constants.PARAMETER_USERTYPE, Constants.CONSTANT_EMONEY_INT).apply();
                            Log.d(LOG_TAG, "both");
                            sharedPreferences.edit().putString(Constants.PARAMETER_TYPEUSER, Constants.CONSTANT_BOTH_USER).apply();
                            sharedPreferences.edit().putString(Constants.PARAMETER_ACCOUNTNUMBER, responseContainer.getBankAccountNumber()).apply();
                            Intent intent = new Intent(SecondLoginActivity.this, NumberSwitchingActivity.class);
                            startActivityForResult(intent, 20);
                        }else if(responseContainer.getIsBank().equals("false")&&responseContainer.getIsEmoney().equals("true")&&responseContainer.getIsKyc().equals("true")){
                            sharedPreferences.edit().putInt(Constants.PARAMETER_USERTYPE, Constants.CONSTANT_EMONEY_INT).apply();
                            Log.d(LOG_TAG, "emoney KYC");
                            sharedPreferences.edit().putString(Constants.PARAMETER_TYPEUSER, Constants.CONSTANT_EMONEYKYC_USER).apply();
                            sharedPreferences.edit().putString(Constants.PARAMETER_ACCOUNTNUMBER, responseContainer.getBankAccountNumber()).apply();
                            sharedPreferences.edit().putInt(Constants.PARAMETER_AGENTTYPE, Constants.CONSTANT_EMONEY_INT).apply();
                            Intent intent = new Intent(SecondLoginActivity.this, UserHomeActivity.class);
                            startActivityForResult(intent, 20);
                        }else if(responseContainer.getIsBank().equals("false")&&responseContainer.getIsEmoney().equals("true")&&responseContainer.getIsKyc().equals("false")){
                            sharedPreferences.edit().putInt(Constants.PARAMETER_USERTYPE, Constants.CONSTANT_EMONEY_INT).apply();
                            Log.d(LOG_TAG, "emoney non KYC");
                            sharedPreferences.edit().putString(Constants.PARAMETER_TYPEUSER, Constants.CONSTANT_EMONEYNONKYC_USER).apply();
                            sharedPreferences.edit().putString(Constants.PARAMETER_ACCOUNTNUMBER, responseContainer.getBankAccountNumber()).apply();
                            sharedPreferences.edit().putInt(Constants.PARAMETER_AGENTTYPE, Constants.CONSTANT_EMONEY_INT).apply();
                            Intent intent = new Intent(SecondLoginActivity.this, UserHomeActivity.class);
                            startActivityForResult(intent, 20);
                        }else if(responseContainer.getIsBank().equals("true") && responseContainer.getIsEmoney().equals("false") && responseContainer.getIsKyc().equals("true")){
                            sharedPreferences.edit().putInt(Constants.PARAMETER_USERTYPE, Constants.CONSTANT_BANK_INT).apply();
                            Log.d(LOG_TAG, "bank account");
                            sharedPreferences.edit().putString(Constants.PARAMETER_TYPEUSER, Constants.CONSTANT_BANK_USER).apply();
                            sharedPreferences.edit().putString(Constants.PARAMETER_ACCOUNTNUMBER, responseContainer.getBankAccountNumber()).apply();
                            sharedPreferences.edit().putInt(Constants.PARAMETER_AGENTTYPE, Constants.CONSTANT_BANK_INT).apply();
                            Intent intent = new Intent(SecondLoginActivity.this, UserHomeActivity.class);
                            startActivityForResult(intent, 20);
                        }
                    } else if (responseContainer.getCustomerType().equals("2")) {
                        sharedPreferences.edit().putInt(Constants.PARAMETER_USERTYPE, Constants.CONSTANT_LAKUPANDAI_INT).apply();
                        Intent intent = new Intent(SecondLoginActivity.this, NumberSwitchingActivity.class);
                        sharedPreferences.edit().putString(Constants.PARAMETER_ACCOUNTNUMBER, responseContainer.getBankAccountNumber()).apply();
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

    class inquiryMDNValidationAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, Constants.MDN_VALIDATION_FORGOTPIN);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID, Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_KEY, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sourceMDN);
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    SecondLoginActivity.this);
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
            progressDialog.dismiss();
            if (response != null) {
                Log.e(LOG_TAG, "Response=====" + response);
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
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(SecondLoginActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });
                            alertbox.show();
                        } else if(responseDataContainer.getMsgCode().equals("2300")){
                            message = responseDataContainer.getMsg();
                            Log.d(LOG_TAG, "message "+message);
                            transactionTime = responseDataContainer.getTransactionTime();
                            Log.d(LOG_TAG, "transactionTime "+transactionTime);
                            responseCode = responseDataContainer.getResponseCode();
                            Log.d(LOG_TAG, "responseCode "+responseCode);
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(SecondLoginActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setTitle("Reset mPIN anda");
                            alertbox.setMessage(message);
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });
                            alertbox.show();
                        } else if(responseDataContainer.getMsgCode().equals("2301")){
                            message = responseDataContainer.getMsg();
                            Log.d(LOG_TAG, "message "+message);
                            transactionTime = responseDataContainer.getTransactionTime();
                            Log.d(LOG_TAG, "transactionTime "+transactionTime);
                            responseCode = responseDataContainer.getResponseCode();
                            Log.d(LOG_TAG, "responseCode "+responseCode);
                            //toSecurityQuestion

                        }else{
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(SecondLoginActivity.this, R.style.MyAlertDialogStyle);
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
}
