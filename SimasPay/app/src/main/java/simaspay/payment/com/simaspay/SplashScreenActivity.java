package simaspay.payment.com.simaspay;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.JSONParser;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;


public class SplashScreenActivity extends Activity {

    Handler handler = new Handler();

    TextView textView;

    RSAEncryption rsaEncryption;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = SplashScreenActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        textView = (TextView) findViewById(R.id.text);
        textView.setTypeface(Utility.LightTextFormat(this));

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            Log.e("--------","========================Connected");
        } else {
            // display error
            Log.e("--------","====================== Not==Connected");
        }

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        checkPermission();



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }


    class RSAEncryption extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_GETPUBLICKEY);
            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    SplashScreenActivity.this);

            Log.e("--------","----------"+checkWriteExternalPermission());


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
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (response != null) {
                Log.e("-------","====="+response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseDataContainer = null;
                try {
                    responseDataContainer = obj.parse(response);
                } catch (Exception e) {
                }
                try {
                    if (responseDataContainer != null) {
                        if (responseDataContainer.getSuccess().equals("true")) {
                            sharedPreferences
                                    .edit()
                                    .putString("MODULE",
                                            responseDataContainer.getPublicKeyModulus())
                                    .commit();
                            sharedPreferences
                                    .edit()
                                    .putString("EXPONENT",
                                            responseDataContainer.getPublicKeyExponet())
                                    .commit();
                            Intent intent = new Intent(SplashScreenActivity.this, LoginScreenActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if (responseDataContainer.getMsg() != null) {
                                Utility.ShowDialog(responseDataContainer.getMsg(), SplashScreenActivity.this);
                            } else {
                                Utility.ShowDialog(sharedPreferences.getString(
                                        "ErrorMessage",
                                        getResources().getString(
                                                R.string.bahasa_serverNotRespond)), SplashScreenActivity.this);
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
                Utility.ShowDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), SplashScreenActivity.this);
            }
        }
    }

    private boolean checkWriteExternalPermission()
    {

        String permission = "java.io.Writer";
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
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
                        109);
            }
        }else{
            rsaEncryption = new RSAEncryption();
            rsaEncryption.execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 109: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    rsaEncryption = new RSAEncryption();
                    rsaEncryption.execute();
                } else {
                }
                return;
            }

        }
    }
}
