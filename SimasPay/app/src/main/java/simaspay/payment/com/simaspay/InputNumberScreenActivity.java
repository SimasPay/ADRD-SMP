package simaspay.payment.com.simaspay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by widy on 1/9/17.
 * 09
 */

public class InputNumberScreenActivity extends AppCompatActivity {
    Context context;
    public SharedPreferences settings;
    public SharedPreferences.Editor editor;
    private ImageView back_btn;
    public EditText phone_number;
    private TextView aktivasi_link;
    private static final String TAG = "SimasPay";
    private Button lanjut;
    private String phonenum, mdn;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entermobilenum);
        context = InputNumberScreenActivity.this;
        settings = getSharedPreferences(TAG, 0);
        mdn = settings.getString("mobileNumber", "");
        editor = settings.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        aktivasi_link = (TextView)findViewById(R.id.aktivasi_link);
        SpannableString content = new SpannableString("Aktivasi Akun");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        aktivasi_link.setText(content);
        aktivasi_link.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //link to ActivationScreen
            }
        });
        back_btn = (ImageView)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputNumberScreenActivity.this, LandingScreenActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        phone_number = (EditText)findViewById(R.id.hand_phno);
        if(!mdn.equals("")){
            phone_number.setText(mdn);
        }
        lanjut=(Button)findViewById(R.id.lanjut);
        lanjut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(phone_number.getText().length()>=10){
                    settings.edit().putString("phonenumber", phone_number.getText().toString()).apply();
                    settings.edit().putString("mobileNumber", phone_number.getText().toString()).apply();
                    phonenum=phone_number.getText().toString();
                    new mdnvalidation().execute();
                }else if(phone_number.getText().length()==0){
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(InputNumberScreenActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(getResources().getString(R.string.id_empty_mdn_input));
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
                    alertbox.show();
                }else if(phone_number.getText().length()<10&&phone_number.getText().length()>0){
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(InputNumberScreenActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(getResources().getString(R.string.id_invalid_mdn_input));
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
                    alertbox.show();
                }

            }
        });
    }

    class mdnvalidation extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("service", "Account");
            mapContainer.put("txnName", "SubscriberStatus");
            mapContainer.put("institutionID", Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put("authenticationKey", "");
            mapContainer.put("sourceMDN", phonenum);
            mapContainer.put("channelID", "7");
            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    InputNumberScreenActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(InputNumberScreenActivity.this);
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
                Log.e("-------", "=====" + response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseDataContainer = null;
                try {
                    responseDataContainer = obj.parse(response);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                try {
                    if (responseDataContainer != null) {
                        Log.d("test", "not null");
                        String mdn = responseDataContainer.getMdn();
                        String status = responseDataContainer.getStatus();
                        String firstName = responseDataContainer.getFirstName();
                        String lastName = responseDataContainer.getLastName();
                        Log.d("test", "mdn: "+ mdn + ", status: "+ status);
                        if(status.equals("0") || status.equals("27")){
                            settings.edit().putString("phonenumber", phone_number.getText().toString()).apply();
                            settings.edit().putString("mobileNumber", phone_number.getText().toString()).apply();
                            settings.edit().putString("firstName", firstName).apply();
                            settings.edit().putString("lastName", lastName).apply();
                            if(firstName==null){
                                settings.edit().putString("fullname", lastName).apply();
                            }else if(lastName==null){
                                settings.edit().putString("fullname", firstName).apply();
                            }else{
                                settings.edit().putString("fullname", firstName + " " + lastName).apply();
                            }

                            Intent intent = new Intent(InputNumberScreenActivity.this, SecondLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            //finish();
                        }else if(status.equals("11")){
                            Intent intent = new Intent(InputNumberScreenActivity.this, RegistrationNonKYCActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                }catch (Exception e) {
                }
            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), InputNumberScreenActivity.this);
            }

        }

    }
}
