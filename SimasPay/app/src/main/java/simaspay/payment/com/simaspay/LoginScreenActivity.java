package simaspay.payment.com.simaspay;

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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.agentdetails.NumberSwitchingActivity;
import com.payment.simaspay.lakupandai.LakuPandaiActivity;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.userdetails.SimaspayUserActivity;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Widy Agung P on 01/12/2017.
 * 12
 */
public class LoginScreenActivity extends AppCompatActivity {
    private static final String TAG = "SimasPay";
    EditText e_mPin;
    Button login;
    String countryCode = "62", mobileNumber;
    TextView simas;
    Context context;
    SharedPreferences sharedPreferences;
    String pin;
    int msgCode = 0;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent=new Intent(LoginScreenActivity.this, SecondLoginActivity.class);
                startActivity(intent);
                finish();
            }else if(resultCode==Activity.RESULT_FIRST_USER){

            } else {
                finish();
            }
        }else if (requestCode == 30) {
            if (resultCode == Activity.RESULT_OK) {

            }else if(resultCode==Activity.RESULT_FIRST_USER){

            } else {
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginScreenActivity.this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        ImageView back_btn = (ImageView)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(LoginScreenActivity.this, InputNumberScreenActivity.class);
                startActivity(intent);
            }
        });
        sharedPreferences = getSharedPreferences(TAG, MODE_PRIVATE);
        final String mdn = sharedPreferences.getString("phonenumber","");
        e_mPin = (EditText) findViewById(R.id.mpin);
        TextView phonenumber=(TextView)findViewById(R.id.label_phone);
        phonenumber.setText("Silakan masukkan MPIN untuk \\nnomor HP "+mdn);
        login = (Button) findViewById(R.id.login);

        simas = (TextView) findViewById(R.id.simas);
//        simas.setTextSize(getResources().getDimensionPixelSize(R.dimen.txt));

        if(sharedPreferences.getBoolean("Login",false)){
            Intent intent=new Intent(LoginScreenActivity.this, SecondLoginActivity.class);
            startActivity(intent);
            finish();
        }
        login.setTypeface(Utility.Robot_Regular(LoginScreenActivity.this));

        e_mPin.setTypeface(Utility.Robot_Light(LoginScreenActivity.this));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean networkCheck = Utility.isConnectingToInternet(context);
                if (!networkCheck) {
                    Utility.networkDisplayDialog(
                            getResources().getString(
                                    R.string.bahasa_serverNotRespond), context);

                } else if (mdn.equals("")) {
                    Utility.displayDialog("Masukkan Nomor Handphone", LoginScreenActivity.this);
                } else if (mdn.replace(" ", "")
                        .length() < 10) {
                    Utility.displayDialog(getResources().getString(R.string.number_less7),
                            LoginScreenActivity.this);
                } else if (e_mPin.getText().toString().equals("")) {
                    Utility.displayDialog("Masukkan Pin Anda", LoginScreenActivity.this);
                }else if (e_mPin.getText().toString().replace(" ", "")
                        .length() < 6) {
                    Utility.displayDialog("mPIN you enter must be 6 digits.",
                            LoginScreenActivity.this);
                } else {
                    mobileNumber = mdn;
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
            sharedPreferences.edit().putString("profileId", "").apply();
            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

            try {
                rsaKey = CryptoService.encryptWithPublicKey(module, exponent, pin.getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Map<String, String> mapContainer = new HashMap<String, String>();
            /**
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_LOGIN);
            if (mobileNumber.startsWith("62")) {
                mapContainer.put(Constants.PARAMETER_SOURCE_MDN, mobileNumber);
            } else if (mobileNumber.startsWith("0")) {
                mapContainer.put(Constants.PARAMETER_SOURCE_MDN, "62" + mobileNumber.substring(1));
            } else {
                mapContainer.put(Constants.PARAMETER_SOURCE_MDN, "62" + mobileNumber);
            }
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_STRING, rsaKey);
            mapContainer.put(Constants.PARAMETER_APPVERSION, version);
            mapContainer.put(Constants.PARAMETER_APPOS, AppConfigFile.appOS);
            mapContainer.put(Constants.PARAMETER_DEVICE_MODEL, deviceModel);
            mapContainer.put(Constants.PARAMETER_OS_VERSION, osVersion);
            mapContainer.put(Constants.PARAMETER_SIMASPAYACTIVITY,Constants.CONSTANT_VALUE_TRUE);
            **/

            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, Constants.TRANSACTION_LOGIN);
            mapContainer.put("institutionID", Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put("authenticationKey", "f");
            if (mobileNumber.startsWith("62")) {
                mapContainer.put(Constants.PARAMETER_SOURCE_MDN, mobileNumber);
            } else if (mobileNumber.startsWith("0")) {
                mapContainer.put(Constants.PARAMETER_SOURCE_MDN, "62" + mobileNumber.substring(1));
            } else {
                mapContainer.put(Constants.PARAMETER_SOURCE_MDN, "62" + mobileNumber);
            }
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_STRING, rsaKey);
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SIMASPAYACTIVITY, Constants.CONSTANT_VALUE_TRUE);
            mapContainer.put(Constants.PARAMETER_APPOS, "2");
            mapContainer.put(Constants.PARAMETER_APPTYPE, "");
            mapContainer.put(Constants.PARAMETER_APPVERSION, version);

            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, LoginScreenActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginScreenActivity.this);
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
                    sharedPreferences.edit().putBoolean("Login",true).apply();
                    Log.e("------","------"+responseContainer.getUserApiKey());
                    if (responseContainer.getUserApiKey() != null) {
                        sharedPreferences.edit()
                                .putString("userApiKey", responseContainer.getUserApiKey())
                                .apply();
                    } else {
                        sharedPreferences.edit()
                                .putString("userApiKey", "")
                                .apply();
                    }
                    Log.e("------","------"+sharedPreferences.getString("userApiKey",""));

                    if (mobileNumber.startsWith("62")) {
                        sharedPreferences.edit().putString("mobileNumber", mobileNumber).apply();
                    } else if (mobileNumber.startsWith("0")) {
                        sharedPreferences.edit().putString("mobileNumber", "62" + mobileNumber.substring(1)).apply();
                    } else {
                        sharedPreferences.edit().putString("mobileNumber", "62" + mobileNumber).apply();
                    }

                    sharedPreferences.edit().putString("password", rsaKey).apply();
                    sharedPreferences.edit().putString("userName", responseContainer.getName()).apply();
                    e_mPin.setText("");
                    if (responseContainer.getCustomerType().equals("0")) {
                        if (responseContainer.getIsBank().equalsIgnoreCase("true")) {
                            sharedPreferences.edit().putInt("userType", 0).apply();
                            sharedPreferences.edit().putString("accountnumber",responseContainer.getBankAccountNumber()).apply();
                            Intent intent = new Intent(LoginScreenActivity.this, SimaspayUserActivity.class);
                            startActivityForResult(intent, 20);
                        } else {
                            sharedPreferences.edit().putInt("userType", 1).apply();
                            Intent intent = new Intent(LoginScreenActivity.this, LakuPandaiActivity.class);
                            startActivityForResult(intent, 20);
                        }

                    } else if (responseContainer.getCustomerType().equals("2")) {
                        sharedPreferences.edit().putInt("userType", 2).apply();
                        Intent intent = new Intent(LoginScreenActivity.this, NumberSwitchingActivity.class);
                        sharedPreferences.edit().putString("accountnumber",responseContainer.getBankAccountNumber()).apply();
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
                                LoginScreenActivity.this);
                        e_mPin.setText("");
                    } else {
                        e_mPin.setText("");
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), LoginScreenActivity.this);
                    }
                }


            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), LoginScreenActivity.this);
            }
        }
    }
}
