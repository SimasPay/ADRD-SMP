package simaspay.payment.com.simaspay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.payment.simaspay.services.Constants;
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

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String device_id = tm.getDeviceId();
        Log.e("=======","====="+device_id);
        rsaEncryption = new RSAEncryption();
        rsaEncryption.execute();

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
}
