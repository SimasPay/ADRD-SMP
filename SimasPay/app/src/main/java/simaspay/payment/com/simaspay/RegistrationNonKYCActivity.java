package simaspay.payment.com.simaspay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.receivers.IncomingSMS;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.utils.JSONFunction;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.payment.simaspay.services.AppConfigFile.webAPIUrlFiles;

/**
 * Created by widy on 1/4/17.
 * 04
 */

public class RegistrationNonKYCActivity extends AppCompatActivity implements IncomingSMS.AutoReadSMSListener {
    private static final String LOG_TAG = "SimasPay";
    static EditText fullname, email, mpin, conf_mpin, edt, phonenumber;
    static Spinner questions;
    static AlertDialog dialogBuilder, alertError;
    static Button lanjut;
    static boolean isExitActivity = false;
    LinearLayout otplay, otp2lay;
    Context context;
    SharedPreferences settings2, languageSettings;
    JSONObject jsonobject;
    JSONArray jsonarray;
    ProgressDialog mProgressDialog;
    List<String> allQuestions = new ArrayList<String>();
    SharedPreferences sharedPreferences, settings;
    String rsaKey;
    String pin, otpValue, mobilenumber;
    String stFullname, stEmail, stMPIN, stConfMPIN, stQuestion, stAnswer;
    String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_nonkyc);
        IncomingSMS.setListener(RegistrationNonKYCActivity.this);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        settings = getSharedPreferences(LOG_TAG, MODE_PRIVATE);
        mobilenumber = settings.getString("phonenumber","");
        context=RegistrationNonKYCActivity.this;
        settings2 = getSharedPreferences(LOG_TAG, 0);
        settings2.edit().putString("ActivityName", "RegistrationNonKYC").apply();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ImageView back_btn = (ImageView)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");

        fullname=(EditText)findViewById(R.id.fullname_ed);
        email=(EditText)findViewById(R.id.email_ed);
        mpin=(EditText)findViewById(R.id.mpin_ed);
        conf_mpin=(EditText)findViewById(R.id.mpin2_ed);
        //answer=(EditText)findViewById(R.id.answer_ed);
        phonenumber=(EditText)findViewById(R.id.mdn_ed);
        phonenumber.setText(mobilenumber);
        phonenumber.setEnabled(false);
        //questions=(Spinner)findViewById(R.id.questions_spinner);
        lanjut=(Button)findViewById(R.id.lanjut);
        //new DownloadJSON().execute();
        //new QuestionsLists().execute();
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(fullname.getText().toString().replace(" ", "").length()==0) {
                    fullname.setError(getResources().getString(R.string.id_invalid_name));
                    return;
                }
                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError(getResources().getString(R.string.id_invalid_email));
                    return;
                }
                else if(mpin.getText().toString().replace(" ", "").length()<6) {
                    mpin.setError(getResources().getString(R.string.id_invalid_mpin));
                    return;
                }else if(conf_mpin.getText().toString().replace(" ", "").length()<6) {
                    conf_mpin.setError(getResources().getString(R.string.id_invalid_mpin));
                    return;
                }else if(mpin.getText().toString().matches("^(?=\\d{6}$)(?:(.)\\1*|0?1?2?3?4?5?6?7?8?9?|9?8?7?6?5?4?3?2?1?0?)$")){
                    mpin.setError(getResources().getString(R.string.id_validation_mpin));
                    return;
                }else if(conf_mpin.getText().toString().matches("^(?=\\d{6}$)(?:(.)\\1*|0?1?2?3?4?5?6?7?8?9?|9?8?7?6?5?4?3?2?1?0?)$")){
                    mpin.setError(getResources().getString(R.string.id_validation_confmpin));
                    return;
                }else if(!mpin.getText().toString().equals(conf_mpin.getText().toString())){
                    Log.d(LOG_TAG, "mpin: "+mpin.getText().toString() + ", conf mpin: " +conf_mpin.getText().toString());
                    conf_mpin.setError(getResources().getString(R.string.id_checksame_mpin));
                    return;
                    /**
                }else if(answer.getText().toString().length()==0){
                    answer.setError("Harap isi Jawaban Pertanyaan di atas");
                    return;
                     **/
                }else {
                    pin = mpin.getText().toString();
                    stFullname=fullname.getText().toString();
                    stEmail=email.getText().toString();
                    stMPIN=mpin.getText().toString();
                    stConfMPIN=conf_mpin.getText().toString();
                    //stAnswer=answer.getText().toString();
                    //new reqSMSAsyncTask().execute();
                    Intent intent = new Intent(RegistrationNonKYCActivity.this, SecurityQuestionsActivity.class);
                    intent.putExtra("fullname", fullname.getText().toString());
                    intent.putExtra("email", email.getText().toString());
                    intent.putExtra("mdn", mobilenumber);
                    intent.putExtra("mpin", mpin.getText().toString());
                    intent.putExtra("mpin2", conf_mpin.getText().toString());
                    startActivity(intent);
                    //finish();
                    //Toast.makeText(getApplicationContext(), "Validated Succesfully", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onReadSMS(String otp) {
        Log.d(LOG_TAG, "otp from SMS: " + otp);
        edt.setText(otp);
        otpValue=otp;
    }

    class reqSMSAsyncTask extends AsyncTask<Void, Void, Void> {
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
                    mobilenumber);
            mapContainer.put("channelID",
                    "7");
            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    RegistrationNonKYCActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(RegistrationNonKYCActivity.this);
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
                }
            }

        }

    }

    class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {
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
            sharedPreferences.edit().putString("profileId", "").commit();
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
            mapContainer.put("institutionID", "simaspay");
            mapContainer.put("authenticationKey", "");
            mapContainer.put("sourceMDN", mobilenumber);
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
                    RegistrationNonKYCActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(RegistrationNonKYCActivity.this);
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
                    }
                }catch (Exception e) {
                }
            }

        }

    }

    private void showOTPRequiredDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullParent = null;
        View dialoglayout = inflater.inflate(R.layout.new_otp_dialog, nullParent, false);
        dialogBuilder = new AlertDialog.Builder(RegistrationNonKYCActivity.this, R.style.MyAlertDialogStyle).create();
        dialogBuilder.setCanceledOnTouchOutside(false);
        dialogBuilder.setTitle("");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(dialoglayout);

        // EditText OTP
        otplay = (LinearLayout) dialoglayout.findViewById(R.id.halaman1);
        otp2lay = (LinearLayout) dialoglayout.findViewById(R.id.halaman2);
        otp2lay.setVisibility(View.GONE);
        TextView manualotp = (TextView) dialoglayout.findViewById(R.id.manualsms_lbl);
        TextView waitingsms = (TextView) dialoglayout.findViewById(R.id.waitingsms_lbl);
        Button cancel_otp = (Button) dialoglayout.findViewById(R.id.cancel_otp);
        waitingsms.setText(getResources().getString(R.string.id_checksame_mpin)+mobilenumber+ "\n");
        manualotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                otplay.setVisibility(View.GONE);
                otp2lay.setVisibility(View.VISIBLE);
            }
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
                errorOTP();
                timer.setText("00:00");
            }
        };
        myTimer.start();
        cancel_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                settings2 = getSharedPreferences(LOG_TAG, 0);
                settings2.edit().putString("ActivityName", "ExitRegisterNonKYC").commit();
                if (myTimer != null) {
                    myTimer.cancel();
                }
            }
        });
        final Button ok_otp = (Button) dialoglayout.findViewById(R.id.ok_otp);
        ok_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt.getText().toString() == null || edt.getText().toString().equals("")) {
                    errorOTP();
                } else {
                    if (myTimer != null) {
                        myTimer.cancel();
                    }
                    settings2 = getSharedPreferences(LOG_TAG, 0);
                    settings2.edit().putString("ActivityName", "ExitRegisterNonKYC").commit();
                    isExitActivity = true;
                    if(otpValue==null){
                        otpValue=edt.getText().toString();
                    }
                    new RegisterAsyncTask().execute();
                }
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
                // Check if edittext is empty
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    //ok_otp.setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    //ok_otp.setEnabled(true);
                }
                if (edt.getText().length() > 5) {
                    Log.d(LOG_TAG, "otp dialog length: " + edt.getText().length());
                    if (myTimer != null) {
                        myTimer.cancel();
                    }
                    if(otpValue==null){
                        otpValue=edt.getText().toString();
                    }
                    //dialogBuilder.dismiss();
                    new RegisterAsyncTask().execute();
                    /**
                    isExitActivity = true;
                    settings2 = getSharedPreferences(LOG_TAG, 0);
                    String activityName = settings2.getString("ActivityName", "");
                    Log.d(LOG_TAG, "ActivityName : " + activityName);
                    if (activityName.equals("RegistrationNonKYC")) {
                        new RegisterAsyncTask().execute();
                    }
                     **/
                }

            }
        });
        dialogBuilder.show();
    }

    public void errorOTP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationNonKYCActivity.this, R.style.MyAlertDialogStyle);
        builder.setCancelable(false);
        if (selectedLanguage.equalsIgnoreCase("ENG")) {
            builder.setTitle(getResources().getString(R.string.eng_otpfailed));
            builder.setMessage(getResources().getString(R.string.eng_desc_otpfailed)).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            settings2 = getSharedPreferences(LOG_TAG, 0);
                            settings2.edit().putString("ActivityName", "ExitRegisterNonKYC").commit();
                            isExitActivity = true;
                            Intent intent = new Intent(RegistrationNonKYCActivity.this, SecondLoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
        } else {
            builder.setTitle(getResources().getString(R.string.bahasa_otpfailed));
            builder.setMessage(getResources().getString(R.string.bahasa_desc_otpfailed)).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            settings2 = getSharedPreferences(LOG_TAG, 0);
                            settings2.edit().putString("ActivityName", "ExitRegisterNonKYC").commit();
                            isExitActivity = true;
                            Intent intent = new Intent(RegistrationNonKYCActivity.this, SecondLoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
        }
        alertError = builder.create();
        if (!isFinishing()) {
            alertError.show();
        }
    }

}
