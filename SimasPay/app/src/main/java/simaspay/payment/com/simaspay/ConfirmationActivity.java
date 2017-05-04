package simaspay.payment.com.simaspay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.receivers.IncomingSMS;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.utils.Functions;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by widy on 1/11/17.
 * 11
 */

public class ConfirmationActivity extends AppCompatActivity implements IncomingSMS.AutoReadSMSListener {
    String stFullname, stEmail, stMPIN, stConfMPIN, stQuestion, stAnswer, sourceMDN;
    TextView lbl_name, lbl_email, lbl_mdn;
    Button benar_btn, salah_btn;
    private static final String LOG_TAG = "SimasPay";
    private EditText edt;
    private static AlertDialog dialogBuilder;
    LinearLayout otplay, otp2lay;
    Context context;
    SharedPreferences languageSettings;
    String rsaKey;
    String pin, otpValue;
    String selectedLanguage;
    Functions func;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        func=new Functions(this);
        func.initiatedToolbar(this);
        if (Build.VERSION.SDK_INT > 14) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        context=ConfirmationActivity.this;
        IncomingSMS.setListener(ConfirmationActivity.this);

        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");

        lbl_name=(TextView)findViewById(R.id.lbl_name);
        lbl_email=(TextView)findViewById(R.id.lbl_email);
        lbl_mdn=(TextView)findViewById(R.id.lbl_phone);
        benar_btn=(Button)findViewById(R.id.benar_btn);
        salah_btn=(Button)findViewById(R.id.salah_btn);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            stFullname = (String) extras.get("fullname");
            stEmail = (String) extras.get("email");
            sourceMDN = (String) extras.get("mdn");
            stMPIN = (String) extras.get("mpin");
            stConfMPIN = (String) extras.get("mpin2");
            stQuestion = (String) extras.get("question");
            stAnswer=(String) extras.get("answer");
            lbl_name.setText(stFullname);
            if(stEmail.equals("")||stEmail==null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    lbl_email.setText(Html.fromHtml("<i>("+getResources().getString(R.string.kosong)+")</i>",0));
                }else{
                    lbl_email.setText(Html.fromHtml("<i>("+getResources().getString(R.string.kosong)+")</i>"));
                }
            }else{
                lbl_email.setText(stEmail);
            }
            lbl_mdn.setText(sourceMDN);
        }
        benar_btn.setOnClickListener(view -> new reqSMSAsyncTask().execute());
        salah_btn.setOnClickListener(view -> {
            finish();
        });

    }

    @Override
    public void onReadSMS(String otp) {
        Log.d(LOG_TAG, "otp from SMS: " + otp);
        edt.setText(otp);
        otpValue=otp;
    }

    private class reqSMSAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("service",
                    "Account");
            mapContainer.put("txnName",
                    "GenerateOTP");
            mapContainer.put("sourceMDN",
                    sourceMDN);
            mapContainer.put("channelID",
                    "7");
            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    ConfirmationActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ConfirmationActivity.this);
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
                    Log.e(LOG_TAG, e.toString());
                }
                try {
                    if (responseDataContainer != null) {
                        Log.d("test", "not null");
                        showOTPRequiredDialog();
                    }
                }catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }

        }

    }

    private class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
            sharedPreferences.edit().putString("profileId", "").apply();
            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

            try {
                rsaKey = CryptoService.encryptWithPublicKey(module, exponent,
                        pin.getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Log.d(LOG_TAG,"cek! : fullname : " + stFullname + ", email: "+ stEmail + ", mpin: " + stMPIN + ", confmpin: " + stConfMPIN + ", question: "+ stQuestion + ", answer: " + stAnswer + ", otp: " + otpValue);
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put("service", "Account");
            mapContainer.put("txnName", "SubscriberRegistration");
            mapContainer.put("institutionID", Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put("authenticationKey", "");
            mapContainer.put("sourceMDN", sourceMDN);
            mapContainer.put("subFirstName", stFullname);
            mapContainer.put("channelID", "7");
            mapContainer.put("email", stEmail);
            mapContainer.put("activationNewPin", CryptoService.encryptWithPublicKey(module, exponent,
                    stMPIN.getBytes()));
            mapContainer.put("activationConfirmPin", CryptoService.encryptWithPublicKey(module, exponent,
                    stConfMPIN.getBytes()));
            mapContainer.put("securityQuestion", stQuestion);
            mapContainer.put("securityAnswer", stAnswer);
            mapContainer.put("otp", CryptoService.encryptWithPublicKey(module, exponent,
                    otpValue.getBytes()));
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    ConfirmationActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ConfirmationActivity.this);
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
                    Log.e("responseContainer", "responseContainer" + responseDataContainer + "");

                } catch (Exception e) {
                    Log.e(LOG_TAG, e.toString());
                }
                try {
                    if (responseDataContainer != null) {
                        Log.d("test", "not null");
                        int msgCode = 0;

                        try {
                            msgCode = Integer.parseInt(responseDataContainer.getMsgCode());
                        } catch (Exception e) {
                            msgCode = 0;
                        }

                        if(msgCode==2180){
                            Intent intent = new Intent(ConfirmationActivity.this, NotificationActivity.class);
                            startActivity(intent);
                            ConfirmationActivity.this.finish();
                        }else {
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(ConfirmationActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", (arg0, arg1) -> {
                                dialogBuilder.dismiss();
                                ConfirmationActivity.this.finish();
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

    private void showOTPRequiredDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullParent = null;
        View dialoglayout = inflater.inflate(R.layout.new_otp_dialog, nullParent, false);
        dialogBuilder = new AlertDialog.Builder(ConfirmationActivity.this, R.style.MyAlertDialogStyle).create();
        dialogBuilder.setCanceledOnTouchOutside(false);
        dialogBuilder.setTitle("");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(dialoglayout);

        // EditText OTP
        otplay = (LinearLayout) dialoglayout.findViewById(R.id.halaman1);
        otp2lay = (LinearLayout) dialoglayout.findViewById(R.id.halaman2);
        otp2lay.setVisibility(View.GONE);
        TextView manualotp = (TextView) dialoglayout.findViewById(R.id.manualsms_lbl);
        Button cancel_otp = (Button) dialoglayout.findViewById(R.id.cancel_otp);
        manualotp.setOnClickListener(arg0 -> {
            otplay.setVisibility(View.GONE);
            otp2lay.setVisibility(View.VISIBLE);
        });
        edt = (EditText) dialoglayout.findViewById(R.id.otp_value);

        Log.d(LOG_TAG, "otpValue : " + edt.getText().toString());

        // Timer
        final TextView timer = (TextView) dialoglayout.findViewById(R.id.otp_timer);
        // 120detik
        final CountDownTimer myTimer = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                timer.setText(
                        f.format(millisUntilFinished / 60000) + ":" + f.format(millisUntilFinished % 60000 / 1000));
            }

            @Override
            public void onFinish() {
                dialogBuilder.dismiss();
                func.errorOTP();
                timer.setText("00:00");
            }
        };
        myTimer.start();
        cancel_otp.setOnClickListener(v -> {
            dialogBuilder.dismiss();
            myTimer.cancel();
        });
        final Button ok_otp = (Button) dialoglayout.findViewById(R.id.ok_otp);
        ok_otp.setEnabled(false);
        ok_otp.setTextColor(getResources().getColor(R.color.dark_red));
        ok_otp.setOnClickListener(v -> {
            if (edt.getText() == null || edt.getText().toString().equals("")) {
                func.errorEmptyOTP();
            } else {
                myTimer.cancel();
                if(otpValue==null||otpValue.equals("")){
                    otpValue=edt.getText().toString();
                }
                new RegisterAsyncTask().execute();
            }
        });
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ok_otp.setEnabled(true);
                ok_otp.setTextColor(getResources().getColor(R.color.red));
                if (edt.getText().length() >= Constants.DIGITS_OTP) {
                    Log.d(LOG_TAG, "otp dialog length: " + edt.getText().length());
                    myTimer.cancel();
                    otpValue=edt.getText().toString();
                    dialogBuilder.dismiss();
                    new ConfirmationActivity.RegisterAsyncTask().execute();
                }

            }
        });
        dialogBuilder.show();
    }

}
