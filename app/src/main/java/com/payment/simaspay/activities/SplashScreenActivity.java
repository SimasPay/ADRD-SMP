package com.payment.simaspay.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.payment.simaspay.R;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.constants.EncryptedResponseDataContainer;
import com.testfairy.TestFairy;

import java.util.HashMap;
import java.util.Map;

import static com.payment.simaspay.services.Constants.LOG_TAG;


public class SplashScreenActivity extends Activity {

    public SharedPreferences settings;
    public SharedPreferences.Editor editor;
    private static final String TAG = "SimasPay";
    TextView textView;
    RSAEncryption rsaEncryption;
    SharedPreferences sharedPreferences;
    private Context context;
    String appID;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int MY_PERMISSIONS_REQUEST_SMS = 10;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 11;
    private static final int READ_PHONE_STATE_REQUEST = 109;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TestFairy.begin(this, "9a9d8ac3647160ccec0cbc90360b1e94ccd87258");
        setContentView(R.layout.activity_splash_screen);
        settings = getSharedPreferences(TAG, 0);
        context = SplashScreenActivity.this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = SplashScreenActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        textView = findViewById(R.id.text);
        textView.setTypeface(Utility.LightTextFormat(this));
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        Log.d(LOG_TAG, "proxy: " + getProxyDetails(this));
        if (getProxyDetails(this).equals("0") || getProxyDetails(this) == null || getProxyDetails(this).equals("null:null") || getProxyDetails(this).contains("0")) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connMgr != null) {
                networkInfo = connMgr.getActiveNetworkInfo();
            }
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.e("--------", "========================Connected");
            } else {
                //display error
                Log.e("--------", "====================== Not==Connected");
            }
            checkPermission();
        } else {
            Log.e(LOG_TAG, "====================== Proxy detected!");
            Utility.ShowDialog2(sharedPreferences.getString(
                    "ErrorMessage",
                    getResources().getString(
                            R.string.untrusted_connection)), SplashScreenActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("StaticFieldLeak")
    private class RSAEncryption extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String response;
        PackageInfo info = null;
        String version = "";

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            //Log.d(TAG, Constants.PARAMETER_CHANNEL_ID+ ", "+Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_ACCOUNT);
            //Log.d(TAG, Constants.PARAMETER_SERVICE_NAME+ ", "+Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_GETPUBLICKEY);
            //Log.d(TAG, Constants.PARAMETER_TRANSACTIONNAME+ ", "+Constants.TRANSACTION_GETPUBLICKEY);
            mapContainer.put(Constants.PARAMETER_APPOS, Constants.OS_ANDROID);
            mapContainer.put(Constants.PARAMETER_SIMASPAYACTIVITY, Constants.IS_SIMASPAYACTIVITY);
            mapContainer.put(Constants.PARAMETER_PROFILE_ID, "");
            //Log.d(TAG, Constants.PARAMETER_APPOS+", "+Constants.OS_ANDROID);
            PackageManager manager = context.getPackageManager();

            try {
                info = manager.getPackageInfo(context.getPackageName(), 0);
                version = info.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            mapContainer.put(Constants.PARAMETER_APPVERSION, version);
            //Log.d(TAG, Constants.PARAMETER_APPVERSION+", "+version);
            //Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    SplashScreenActivity.this);

            //Log.e("--------","----------"+checkWriteExternalPermission());


           /* JSONParser jsonParser=new JSONParser();

            response=jsonParser.getDataFromUrl("http://54.255.194.95:8080/webapi/dynamic?txnName=GetPublicKey&service=Account&channelID=7&institutionID=&mspID=1&accountType=");*/

            response = webServiceHttp.getResponseSSLCertificatation();


            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SplashScreenActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            Drawable drawable = new ProgressBar(SplashScreenActivity.this).getIndeterminateDrawable().mutate();
            drawable.setColorFilter(ContextCompat.getColor(SplashScreenActivity.this, R.color.red_sinarmas),
                    PorterDuff.Mode.SRC_IN);
            progressDialog.setIndeterminateDrawable(drawable);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (response != null) {
                Log.e("-------", "=====SplashScreen====" + response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseDataContainer = null;
                try {
                    responseDataContainer = obj.parse(response);
                } catch (Exception e) {
                    Log.d(LOG_TAG, "error:" + e.getMessage());
                }
                try {
                    if (responseDataContainer != null) {
                        //Log.d("test", "not null");
                        if (responseDataContainer.getSuccess().equals("true")) {
                            //Log.d("test", "success:true");
                            sharedPreferences
                                    .edit()
                                    .putString("MODULE",
                                            responseDataContainer.getPublicKeyModulus())
                                    .apply();
                            sharedPreferences
                                    .edit()
                                    .putString("EXPONENT",
                                            responseDataContainer.getPublicKeyExponet())
                                    .apply();

                            String firsttime = settings.getString("firsttime", "yes");
                            //Log.d("test", "firsttime: "+ firsttime);
                            //Log.d("test", "not null");
                            int msgCode;
                            try {
                                msgCode = Integer.parseInt(responseDataContainer.getMsgCode());
                            } catch (Exception e) {
                                msgCode = 0;
                            }
                            if (msgCode == 2310) {
                                final String appURL = responseDataContainer.getAppURL();
                                //Log.d(TAG, "appURL:"+appURL);
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(SplashScreenActivity.this, R.style.MyAlertDialogStyle);
                                alertbox.setMessage(responseDataContainer.getMsg());
                                alertbox.setNeutralButton("OK", (arg0, arg1) -> {
                                    String[] parts = appURL.split("=");
                                    if (parts.length > 0) {
                                        appID = parts[1];
                                    } else {
                                        appID = getPackageName();
                                    }
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appID)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appID)));
                                    }
                                    SplashScreenActivity.this.finish();
                                });
                                alertbox.show();
                            } else if (msgCode == 2309) {
                                if (firsttime.equals("yes")) {
                                    Intent intent = new Intent(SplashScreenActivity.this, TermsNConditionsActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    String mdn = settings.getString("mobileNumber", "");
                                    if (!mdn.equals("")) {
                                        Intent intent = new Intent(SplashScreenActivity.this, SecondLoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(SplashScreenActivity.this, LandingScreenActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            } else if (responseDataContainer.getMsgCode().equals("null")) {
                                Utility.ShowDialog(sharedPreferences.getString(
                                        "ErrorMessage",
                                        getResources().getString(
                                                R.string.bahasa_serverNotRespond)), SplashScreenActivity.this);
                            }
                        } else {
                            if (responseDataContainer.getMsg() != null) {
                                Utility.ShowDialog(responseDataContainer.getMsg(), SplashScreenActivity.this);
                            } else {
                                String errorDialog = sharedPreferences.getString("ErrorMessage", getResources().getString(R.string.bahasa_serverNotRespond));
                                Log.d(LOG_TAG, "error message: " + errorDialog);
                                if (errorDialog.equals(getResources().getString(R.string.untrusted_connection))) {
                                    Log.d(LOG_TAG, "untrusted");
                                    Utility.ShowDialog2(sharedPreferences.getString(
                                            "ErrorMessage",
                                            getResources().getString(
                                                    R.string.bahasa_serverNotRespond)), SplashScreenActivity.this);
                                } else {
                                    Log.d(LOG_TAG, "else");
                                    Utility.ShowDialog(sharedPreferences.getString(
                                            "ErrorMessage",
                                            getResources().getString(
                                                    R.string.bahasa_serverNotRespond)), SplashScreenActivity.this);
                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    Utility.ShowDialog(sharedPreferences.getString(
                            "ErrorMessage",
                            getResources().getString(
                                    R.string.bahasa_serverNotRespond)), SplashScreenActivity.this);
                }
            } else {
                /*
                Utility.ShowDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), SplashScreenActivity.this);
                                */
                String errorDialog = sharedPreferences.getString("ErrorMessage", getResources().getString(R.string.bahasa_serverNotRespond));
                if (errorDialog.equals(getResources().getString(R.string.untrusted_connection))) {
                    Log.d(LOG_TAG, "untrusted");
                    Utility.ShowDialog2(sharedPreferences.getString(
                            "ErrorMessage",
                            getResources().getString(
                                    R.string.untrusted_connection)), SplashScreenActivity.this);
                } else {
                    Log.d(LOG_TAG, "else");
                    Utility.ShowDialog(sharedPreferences.getString(
                            "ErrorMessage",
                            getResources().getString(
                                    R.string.bahasa_serverNotRespond)), SplashScreenActivity.this);
                }
            }
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(SplashScreenActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {
                rsaEncryption = new RSAEncryption();
                rsaEncryption.execute();
            } else {
                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        READ_PHONE_STATE_REQUEST);
            }
        } else {
            rsaEncryption = new RSAEncryption();
            rsaEncryption.execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.d(LOG_TAG, "grant result: " + grantResults.length);
        switch (requestCode) {
            case READ_PHONE_STATE_REQUEST:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    rsaEncryption = new RSAEncryption();
                    rsaEncryption.execute();
                }
                break;
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "camera permission granted");
                }
                break;
            case MY_PERMISSIONS_REQUEST_SMS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(LOG_TAG, "------sms granted()");
                }
                break;
            case READ_CONTACTS_PERMISSIONS_REQUEST:
                if (grantResults.length == 1 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(LOG_TAG, "------read contact granted()");
                    rsaEncryption = new RSAEncryption();
                    rsaEncryption.execute();
                }
                break;
        }
    }

    private static String getProxyDetails(Context context) {
        String proxyAddress = "";
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                proxyAddress = android.net.Proxy.getHost(context);
                if (proxyAddress == null || proxyAddress.equals("")) {
                    return proxyAddress;
                }
                proxyAddress += ":" + android.net.Proxy.getPort(context);
            } else {
                proxyAddress = System.getProperty("http.proxyHost");
                proxyAddress += ":" + System.getProperty("http.proxyPort");
            }
        } catch (Exception ex) {
            //ignore
        }
        return proxyAddress;
    }


}
