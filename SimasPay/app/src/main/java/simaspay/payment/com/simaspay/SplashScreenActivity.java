package simaspay.payment.com.simaspay;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.payment.simaspay.AgentTransfer.TransferConfirmationActivity;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.JSONParser;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;


public class SplashScreenActivity extends Activity {

    Handler handler = new Handler();
    public SharedPreferences settings;
    public SharedPreferences.Editor editor;
    private static final String TAG = "SimasPay";
    TextView textView;
    RSAEncryption rsaEncryption;
    SharedPreferences sharedPreferences;
    static Context context;
    String appID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        settings = getSharedPreferences(TAG, 0);
        context=SplashScreenActivity.this;
        editor = settings.edit();
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
        PackageInfo info = null;
        String version= "";

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            Log.d(TAG, Constants.PARAMETER_CHANNEL_ID+ ", "+Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_ACCOUNT);
            Log.d(TAG, Constants.PARAMETER_SERVICE_NAME+ ", "+Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_GETPUBLICKEY);
            Log.d(TAG, Constants.PARAMETER_TRANSACTIONNAME+ ", "+Constants.TRANSACTION_GETPUBLICKEY);
            mapContainer.put("appos", "2");
            mapContainer.put("isSimaspayActivity", "true");
            Log.d(TAG, "appos, 2");
            PackageManager manager = context.getPackageManager();

            try {
                info = manager.getPackageInfo(context.getPackageName(), 0);
                version = info.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            mapContainer.put("appversion", version);
            Log.d(TAG, "appversion, "+version);
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
                Log.e("-------","=====SplashScreen===="+response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseDataContainer = null;
                try {
                    responseDataContainer = obj.parse(response);
                } catch (Exception e) {
                }
                try {
                    if (responseDataContainer != null) {
                        Log.d("test", "not null");
                        if (responseDataContainer.getSuccess().equals("true")) {
                            Log.d("test", "success:true");
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
                            Log.d("test", "firsttime: "+ firsttime);
                            Log.d("test", "not null");
                            int msgCode = 0;
                            try {
                                msgCode = Integer.parseInt(responseDataContainer.getMsgCode());
                            } catch (Exception e) {
                                msgCode = 0;
                            }
                            if(msgCode==2310){
                                final String appURL = responseDataContainer.getAppURL();
                                Log.d(TAG, "appURL:"+appURL);
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(SplashScreenActivity.this, R.style.MyAlertDialogStyle);
                                alertbox.setMessage(responseDataContainer.getMsg());
                                alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        String[] parts = appURL.split("=");
                                        if(parts.length>0){
                                            appID = parts[1];
                                        }else{
                                            appID = getPackageName();
                                        }
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appID)));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appID)));
                                        }
                                        SplashScreenActivity.this.finish();
                                    }
                                });
                                alertbox.show();
                            }else if(msgCode==2309){
                                if(firsttime.equals("yes")){
                                    Intent intent = new Intent(SplashScreenActivity.this, TermsNConditionsActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Intent intent = new Intent(SplashScreenActivity.this, LandingScreenActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        } else {
                            Log.d("test", "success:false");
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
