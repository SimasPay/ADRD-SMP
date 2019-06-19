package com.payment.simaspay.userdetails;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.payment.simaspay.R;
import com.payment.simaspay.activities.LandingScreenActivity;
import com.payment.simaspay.activities.SecurityQuestionsActivity;
import com.payment.simaspay.activities.UserHomeActivity;
import com.payment.simaspay.agentdetails.ChangePinActivity;
import com.payment.simaspay.agentdetails.NumberSwitchingActivity;
import com.payment.simaspay.contactus.ContactUs_Activity;
import com.payment.simaspay.services.AppConfigFile;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.utils.Functions;
import com.payment.simaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

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
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int MY_PERMISSIONS_REQUEST_SMS = 109;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 11;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_login);
        context = SecondLoginActivity.this;
        functions = new Functions(this);
        functions.initiatedToolbar(this);

        //all permissions request
        cameraPermission();
        SMSPermission();
        getPermissionToReadUserContacts();

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        e_mPin = findViewById(R.id.mpin);

        forgot_mpin = findViewById(R.id.forgot_mpin);
        txt_1 = findViewById(R.id.txt_1);
        txt_2 = findViewById(R.id.txt_2);
        number = findViewById(R.id.txt_3);

        Button backbutton = findViewById(R.id.btnBacke);
        backbutton.setOnClickListener(view -> {
            Intent intent = new Intent(SecondLoginActivity.this, LandingScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        forgot_mpin.setTypeface(Utility.Robot_Light(SecondLoginActivity.this));
        forgot_mpin.setPaintFlags(forgot_mpin.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        e_mPin.setTypeface(Utility.Robot_Light(SecondLoginActivity.this));
        txt_1.setTypeface(Utility.Robot_Light(SecondLoginActivity.this));
        txt_2.setTypeface(Utility.Robot_Light(SecondLoginActivity.this));
        number.setTypeface(Utility.Robot_Regular(SecondLoginActivity.this));

        sourceMDN=sharedPreferences.getString(Constants.PARAMETER_PHONENUMBER,"");
        number.setText(sourceMDN);

        forgot_mpin.setOnClickListener(view -> new inquiryMDNValidationAsyncTask().execute());

        TextView tv_faq= findViewById(R.id.tv_faq);
        tv_faq.setPaintFlags(tv_faq.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tv_faq.setOnClickListener(view -> {
            Intent intent = new Intent(SecondLoginActivity.this, FAQActivity.class);
            startActivity(intent);
        });

        TextView tv_tnc= findViewById(R.id.tv_tnc);
        tv_tnc.setPaintFlags(tv_tnc.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tv_tnc.setOnClickListener(view -> {
            Intent intent = new Intent(SecondLoginActivity.this, TNCActivity.class);
            startActivity(intent);
        });

        TextView contact_us= findViewById(R.id.contact_us);
        contact_us.setPaintFlags(contact_us.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        //contact_us.setText(Html.fromHtml("<u>Hubungi Kami</u>"));
        contact_us.setOnClickListener(view -> {
            Intent intent = new Intent(SecondLoginActivity.this, ContactUs_Activity.class);
            startActivity(intent);
        });

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
    }

    @SuppressLint("StaticFieldLeak")
    private class LoginAsynTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String response;


        @Override
        protected Void doInBackground(Void... params) {

            /*
            String deviceModel = null, osVersion = null, deviceManufacture;
            try {
                osVersion = android.os.Build.VERSION.RELEASE;
                deviceModel = android.os.Build.MODEL;
                deviceManufacture = android.os.Build.MANUFACTURER;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
             */

            PackageInfo pInfo = null;
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e1) {
                e1.printStackTrace();
            }
            String version = null;
            if (pInfo != null) {
                version = pInfo.versionName;
            }

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
                //Log.e("-------", "---------" + response);
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

                    //Log.e(LOG_TAG, "UserAPIKey: " + responseContainer.getUserApiKey());
                    if (responseContainer.getUserApiKey() != null) {
                        sharedPreferences.edit()
                                .putString("userApiKey", responseContainer.getUserApiKey())
                                .apply();
                    } else {
                        sharedPreferences.edit()
                                .putString("userApiKey", "")
                                .apply();
                    }
                    //Log.e("------", "userAPIKey------" + sharedPreferences.getString("userApiKey", ""));


                    sharedPreferences.edit().putString("password", rsaKey).apply();
                    sharedPreferences.edit().putString("userName", responseContainer.getName()).apply();
                    //Log.d(LOG_TAG, "responseContainer.getCustomerType():" + responseContainer.getCustomerType());
                    //Log.d(LOG_TAG, "responseContainer.getIsBank():" + responseContainer.getIsBank());
                    //Log.d(LOG_TAG, "responseContainer.getIsEmoney():" + responseContainer.getIsEmoney());
                    //Log.d(LOG_TAG, "responseContainer.getIsKyc():" + responseContainer.getIsKyc());

                    sharedPreferences.edit().putString(Constants.PARAMETER_PROFPICSTRING, responseContainer.getProfpicString()).apply();
                    //Log.d(LOG_TAG, "responseContainer.getProfpicString():" + responseContainer.getProfpicString());
                    if (responseContainer.getCustomerType().equals("0")) {
                        if (responseContainer.getIsBank().equals("true") && responseContainer.getIsEmoney().equals("true") && responseContainer.getIsKyc().equals("true")) {
                            sharedPreferences.edit().putInt(Constants.PARAMETER_USERTYPE, Constants.CONSTANT_EMONEY_INT).apply();
                            //Log.d(LOG_TAG, "both");
                            sharedPreferences.edit().putString(Constants.PARAMETER_TYPEUSER, Constants.CONSTANT_BOTH_USER).apply();
                            sharedPreferences.edit().putString(Constants.PARAMETER_ACCOUNTNUMBER, responseContainer.getBankAccountNumber()).apply();
                            Intent intent = new Intent(SecondLoginActivity.this, NumberSwitchingActivity.class);
                            startActivityForResult(intent, 20);
                        } else if (responseContainer.getIsBank().equals("false") && responseContainer.getIsEmoney().equals("true") && responseContainer.getIsKyc().equals("true")) {
                            sharedPreferences.edit().putInt(Constants.PARAMETER_USERTYPE, Constants.CONSTANT_EMONEY_INT).apply();
                            //Log.d(LOG_TAG, "emoney KYC");
                            sharedPreferences.edit().putString(Constants.PARAMETER_TYPEUSER, Constants.CONSTANT_EMONEYKYC_USER).apply();
                            sharedPreferences.edit().putString(Constants.PARAMETER_ACCOUNTNUMBER, responseContainer.getBankAccountNumber()).apply();
                            sharedPreferences.edit().putInt(Constants.PARAMETER_AGENTTYPE, Constants.CONSTANT_EMONEY_INT).apply();
                            Intent intent = new Intent(SecondLoginActivity.this, UserHomeActivity.class);
                            startActivityForResult(intent, 20);
                        } else if (responseContainer.getIsBank().equals("false") && responseContainer.getIsEmoney().equals("true") && responseContainer.getIsKyc().equals("false")) {
                            sharedPreferences.edit().putInt(Constants.PARAMETER_USERTYPE, Constants.CONSTANT_EMONEY_INT).apply();
                            //Log.d(LOG_TAG, "emoney non KYC");
                            sharedPreferences.edit().putString(Constants.PARAMETER_TYPEUSER, Constants.CONSTANT_EMONEYNONKYC_USER).apply();
                            sharedPreferences.edit().putString(Constants.PARAMETER_ACCOUNTNUMBER, responseContainer.getBankAccountNumber()).apply();
                            sharedPreferences.edit().putInt(Constants.PARAMETER_AGENTTYPE, Constants.CONSTANT_EMONEY_INT).apply();
                            Intent intent = new Intent(SecondLoginActivity.this, UserHomeActivity.class);
                            startActivityForResult(intent, 20);
                        } else if (responseContainer.getIsBank().equals("true") && responseContainer.getIsEmoney().equals("false") && responseContainer.getIsKyc().equals("true")) {
                            sharedPreferences.edit().putInt(Constants.PARAMETER_USERTYPE, Constants.CONSTANT_BANK_INT).apply();
                            //Log.d(LOG_TAG, "bank account");
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
                }else if(msgCode == 2315) {
                    progressDialog.dismiss();
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(SecondLoginActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(responseContainer.getMsg());
                    alertbox.setNeutralButton("OK", (arg0, arg1) -> {
                        Intent intent = new Intent(SecondLoginActivity.this, ChangePinActivity.class);
                        startActivity(intent);
                    });
                    alertbox.show();
                }else if(msgCode == 2182) {
                    progressDialog.dismiss();
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(SecondLoginActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(responseContainer.getMsg());
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(SecondLoginActivity.this, ChangePinActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertbox.show();
                } else {
                    progressDialog.dismiss();
                    if (responseContainer != null) {
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

    private class inquiryMDNValidationAsyncTask extends AsyncTask<Void, Void, Void> {
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
                //Log.e(LOG_TAG, "Response=====" + response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseDataContainer = null;
                try {
                    responseDataContainer = obj.parse(response);
                } catch (Exception e) {
                    //Log.e(LOG_TAG, e.toString());
                }
                try {
                    if (responseDataContainer != null) {
                        //Log.d("test", "not null");
                        switch (responseDataContainer.getMsgCode()) {
                            case "631": {
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
                                break;
                            }
                            case "2300": {
                                message = responseDataContainer.getMsg();
                                //Log.d(LOG_TAG, "message " + message);
                                transactionTime = responseDataContainer.getTransactionTime();
                                //Log.d(LOG_TAG, "transactionTime " + transactionTime);
                                responseCode = responseDataContainer.getResponseCode();
                                //Log.d(LOG_TAG, "responseCode " + responseCode);
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(SecondLoginActivity.this, R.style.MyAlertDialogStyle);
                                alertbox.setTitle("Reset mPIN anda");
                                alertbox.setMessage(message);
                                alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        arg0.dismiss();
                                    }
                                });
                                alertbox.show();
                                break;
                            }
                            case "2301":
                                message = responseDataContainer.getMsg();
                                //Log.d(LOG_TAG, "message " + message);
                                transactionTime = responseDataContainer.getTransactionTime();
                                //Log.d(LOG_TAG, "transactionTime " + transactionTime);
                                responseCode = responseDataContainer.getResponseCode();
                                //Log.d(LOG_TAG, "responseCode " + responseCode);
                                String securityQuestion = responseDataContainer.getSecurityQuestion();
                                //Log.d(LOG_TAG, "securityQuestion " + securityQuestion);
                                //toSecurityQuestion
                                Intent intent = new Intent(SecondLoginActivity.this, SecurityQuestionsActivity.class);
                                intent.putExtra(Constants.PARAMETER_FORGOTMPIN, true);
                                intent.putExtra("securityQuestion", securityQuestion);
                                startActivity(intent);
                                break;
                            default: {
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(SecondLoginActivity.this, R.style.MyAlertDialogStyle);
                                alertbox.setMessage(responseDataContainer.getMsg());
                                alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        arg0.dismiss();
                                    }
                                });
                                alertbox.show();
                                break;
                            }
                        }
                    }
                }catch (Exception e) {
                    //Log.e(LOG_TAG, "error: " + e.toString());
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToReadUserContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {
            }
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

    private void SMSPermission(){
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion > Build.VERSION_CODES.LOLLIPOP) {
            if ((checkCallingOrSelfPermission(Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED) && checkCallingOrSelfPermission(Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS},
                            109);
                }
            }
        }
    }

    private void cameraPermission() {
        if (ContextCompat.checkSelfPermission(SecondLoginActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SecondLoginActivity.this,
                    Manifest.permission.CAMERA)) {
                //Log.d(LOG_TAG, "check camera permission");
            } else {
                ActivityCompat.requestPermissions(SecondLoginActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Log.d(LOG_TAG, "permission granted");
                } else {
                    //Log.d(LOG_TAG, "permission denied");
                }
            break;
            case MY_PERMISSIONS_REQUEST_SMS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(LOG_TAG, "------granted()");
                }
            break;
            case READ_CONTACTS_PERMISSIONS_REQUEST:
                if (grantResults.length == 1 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
                }
            break;
        }
    }

}
