package simaspay.payment.com.simaspay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.receivers.IncomingSMS;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.utils.Functions;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by widy on 1/11/17.
 * 11
 */

public class SecurityQuestionsActivity extends AppCompatActivity implements IncomingSMS.AutoReadSMSListener{
    private static final String LOG_TAG = "SimasPay";
    private Spinner questions;
    private EditText answer;
    JSONObject jsonobject;
    JSONArray jsonarray;
    List<String> allQuestions = new ArrayList<String>();
    String stFullname, stEmail, stMPIN, stConfMPIN, stQuestion, stAnswer, stMDN;
    private String sourceMDN;
    Functions func;
    SharedPreferences sharedPreferences;
    String question_selected="", message, responseCode, transactionTime, stSctlID="", mfaMode="";
    private EditText edt;
    private static AlertDialog dialogBuilder, alertError;
    private AlertDialog.Builder alertbox;
    static boolean isExitActivity = false;
    LinearLayout otplay, otp2lay;
    Context context;
    SharedPreferences settings, settings2, languageSettings;
    String otpValue, pinValue;
    String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securityquestions);
        func=new Functions(this);
        func.initiatedToolbar(this);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        context = SecurityQuestionsActivity.this;
        IncomingSMS.setListener(SecurityQuestionsActivity.this);

        settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString("mobileNumber", "");

        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");

        TextView label_toolbar=(TextView) findViewById(R.id.label_toolbar);
        TextView desc_security=(TextView) findViewById(R.id.desc_security_lbl);
        Button daftar=(Button)findViewById(R.id.daftar);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Boolean isForgot = (Boolean) extras.getBoolean(Constants.PARAMETER_FORGOTMPIN, false);
            if (isForgot == true) {
                label_toolbar.setText("Reset mPIN");
                daftar.setText("Konfirmasi");
            } else {
                label_toolbar.setText(getResources().getString(R.string.id_pertanyaan_keamanan));
                String mpin = extras.getString("mpin");
                pinValue = func.generateRSA(mpin);
                daftar.setText("Daftar");
            }
        }
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = sharedPreferences.getString(Constants.PARAMETER_PHONENUMBER, "");
        questions=(Spinner)findViewById(R.id.questions_spinner);
        answer=(EditText)findViewById(R.id.answer_ed);
        answer.setSingleLine(true);

        ImageView back_btn = (ImageView)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new QuestionsLists().execute();
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                question_selected = questions.getSelectedItem().toString();
                if(answer.getText().toString().length()==0) {
                    answer.setError(getResources().getString(R.string.id_required_jawaban));
                    return;
                }else{
                    Bundle extras = getIntent().getExtras();
                    if (extras != null) {
                        Boolean isForgot = (Boolean) extras.getBoolean(Constants.PARAMETER_FORGOTMPIN, false);
                        String securityQuestion = (String) extras.getString("securityQuestion");
                        if(isForgot==true){
                            stQuestion=questions.getSelectedItem().toString();
                            stAnswer=answer.getText().toString();
                            if(securityQuestion.equals(stQuestion)){
                                new inquiryForgotMPINAsyncTask().execute();
                            }else{
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(SecurityQuestionsActivity.this, R.style.MyAlertDialogStyle);
                                alertbox.setMessage("Pertanyaan yang anda masukkan keliru");
                                alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        dialog.dismiss();
                                    }
                                });
                                alertbox.show();
                            }

                        }else{
                            stFullname = (String) extras.get("fullname");
                            stEmail = (String) extras.get("email");
                            stMDN = (String) extras.get("mdn");
                            stMPIN = (String) extras.get("mpin");
                            stConfMPIN = (String) extras.get("mpin2");
                            stAnswer=answer.getText().toString();
                            Intent intent = new Intent(SecurityQuestionsActivity.this, ConfirmationActivity.class);
                            intent.putExtra("fullname", stFullname);
                            intent.putExtra("email", stEmail);
                            intent.putExtra("mdn", stMDN);
                            intent.putExtra("question", stQuestion);
                            intent.putExtra("answer", stAnswer);
                            intent.putExtra("mpin", stMPIN);
                            intent.putExtra("mpin2", stAnswer);
                            startActivity(intent);
                        }
                    }

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

    class QuestionsLists extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... voids) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("service",
                    "Payment");
            mapContainer.put("txnName",
                    "GetThirdPartyData");
            mapContainer.put("category",
                    "category.securityQuestions");
            mapContainer.put("version",
                    "-1");
            mapContainer.put("channelID",
                    "7");
            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    SecurityQuestionsActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SecurityQuestionsActivity.this);
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
                    Log.e(LOG_TAG, e.toString());
                }
                try {
                    if (responseDataContainer != null) {
                        Log.d("test", "not null");
                        JSONObject jsonObj = new JSONObject(response);
                        jsonarray = jsonObj.getJSONArray("questionData");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            jsonobject = jsonarray.getJSONObject(i);
                            String question = jsonobject.getString("question");
                            allQuestions.add(question);
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (SecurityQuestionsActivity.this, android.R.layout.simple_spinner_item,allQuestions );

                        dataAdapter.setDropDownViewResource
                                (android.R.layout.simple_spinner_dropdown_item);

                        questions.setAdapter(dataAdapter);

                        questions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> arg0,
                                                       View arg1, int position, long arg3) {
                                stQuestion=allQuestions.get(position).toString();
                                //Toast.makeText(getApplicationContext(), "Pertanyaan yang dipilih: "+ allQuestions.get(position).toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.toString());
                }
            }
        }
    }

    class inquiryForgotMPINAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, Constants.TRANSACTION_FORGOTPIN_INQUIRY);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID, Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_KEY, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sourceMDN);
            mapContainer.put(Constants.PARAMETER_SECURITY_QUESTION, question_selected);
            mapContainer.put(Constants.PARAMETER_SECURITY_ANSWER, stAnswer);
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    SecurityQuestionsActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SecurityQuestionsActivity.this);
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
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(SecurityQuestionsActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });
                            alertbox.show();
                        } else if(responseDataContainer.getMsgCode().equals("2045")){
                            message = responseDataContainer.getMsg();
                            Log.d(LOG_TAG, "message "+message);
                            transactionTime = responseDataContainer.getTransactionTime();
                            Log.d(LOG_TAG, "transactionTime "+transactionTime);
                            responseCode = responseDataContainer.getResponseCode();
                            Log.d(LOG_TAG, "responseCode "+responseCode);
                            stSctlID = responseDataContainer.getSctl();
                            Log.d(LOG_TAG, "sctlID "+stSctlID);
                            mfaMode = responseDataContainer.getMfaMode();
                            Log.d(LOG_TAG, "mfaMode "+mfaMode);
                            if(mfaMode.toString().equalsIgnoreCase("OTP")){
                                Intent intent = new Intent(SecurityQuestionsActivity.this, NewMpin_Activity.class);
                                intent.putExtra(Constants.PARAMETER_FORGOTMPIN, true );
                                intent.putExtra("question", stQuestion);
                                intent.putExtra("answer", stAnswer);
                                intent.putExtra("sctlID", stSctlID);
                                intent.putExtra("mfaMode", mfaMode);
                                startActivity(intent);
                            }else{
                                //tanpa OTP
                            }
                        }else{
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(SecurityQuestionsActivity.this, R.style.MyAlertDialogStyle);
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

    class requestOTPAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;
        int msgCode;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("txnName", "ResendMFAOTP");
            mapContainer.put("service", "Wallet");
            mapContainer.put("institutionID", Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put("authenticationKey", "");
            mapContainer.put("sourceMDN", sourceMDN);
            mapContainer.put("sourcePIN", pinValue);
            mapContainer.put("sctlId", stSctlID);
            mapContainer.put("channelID", "7");

            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    SecurityQuestionsActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SecurityQuestionsActivity.this);
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
                    msgCode = Integer.parseInt(responseDataContainer.getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                try {
                    if (responseDataContainer != null) {
                        Log.d("test", "not null");
                        if (responseDataContainer.getMsgCode().equals("631")) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            alertbox = new AlertDialog.Builder(SecurityQuestionsActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                    Intent intent = new Intent(SecurityQuestionsActivity.this, SecondLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                            alertbox.show();
                        } else if(responseDataContainer.getMsgCode().equals("2171")){
                            message = responseDataContainer.getMsg();
                            Log.d(LOG_TAG, "message"+message);
                            transactionTime = responseDataContainer.getTransactionTime();
                            Log.d(LOG_TAG, "transactionTime"+transactionTime);
                            responseCode = responseDataContainer.getResponseCode();
                            Log.d(LOG_TAG, "responseCode"+responseCode);
                            Log.d("test", "not null");
                            int msgCode = 0;

                            showOTPRequiredDialog();
                        }else{
                            alertbox = new AlertDialog.Builder(SecurityQuestionsActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });
                            alertbox.show();
                            dialogBuilder.dismiss();
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
        dialogBuilder = new AlertDialog.Builder(SecurityQuestionsActivity.this, R.style.MyAlertDialogStyle).create();
        dialogBuilder.setCanceledOnTouchOutside(false);
        dialogBuilder.setTitle("");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(dialoglayout);

        // EditText OTP
        otplay = (LinearLayout) dialoglayout.findViewById(R.id.halaman1);
        otp2lay = (LinearLayout) dialoglayout.findViewById(R.id.halaman2);
        otp2lay.setVisibility(View.GONE);
        TextView manualotp = (TextView) dialoglayout.findViewById(R.id.manualsms_lbl);
        //TextView waitingsms = (TextView) dialoglayout.findViewById(R.id.waitingsms_lbl);
        Button cancel_otp = (Button) dialoglayout.findViewById(R.id.cancel_otp);
        //waitingsms.setText("Menunggu SMS Kode Verifikasi di Nomor " + Html.fromHtml("<b>"+sourceMDN+"</b>") + "\n");
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
                settings2.edit().putString("ActivityName", "ExitConfirmationScreen").apply();
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
                    settings2.edit().putString("ActivityName", "ExitConfirmationScreen").apply();
                    isExitActivity = true;
                    if(otpValue==null||otpValue.equals("")){
                        otpValue=edt.getText().toString();
                    }
                    //new DaftarEmoneyActivity.DaftarConfirmationAsyncTask().execute();
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
                    if(otpValue==null||otpValue.equals("")){
                        otpValue=edt.getText().toString();
                    }
                    //new DaftarEmoneyActivity.DaftarConfirmationAsyncTask().execute();

                }

            }
        });
        dialogBuilder.show();
    }

    public void errorOTP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SecurityQuestionsActivity.this, R.style.MyAlertDialogStyle);
        builder.setCancelable(false);
        if (selectedLanguage.equalsIgnoreCase("ENG")) {
            builder.setTitle(getResources().getString(R.string.eng_otpfailed));
            builder.setMessage(getResources().getString(R.string.eng_desc_otpfailed)).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            settings2 = getSharedPreferences(LOG_TAG, 0);
                            settings2.edit().putString("ActivityName", "ExitConfirmationScreen").apply();
                            isExitActivity = true;
                            finish();
                            dialogBuilder.dismiss();
                        }
                    });
        } else {
            builder.setTitle(getResources().getString(R.string.bahasa_otpfailed));
            builder.setMessage(getResources().getString(R.string.bahasa_desc_otpfailed)).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            settings2 = getSharedPreferences(LOG_TAG, 0);
                            settings2.edit().putString("ActivityName", "ExitConfirmationScreen").apply();
                            isExitActivity = true;
                            finish();
                            dialogBuilder.dismiss();
                        }
                    });
        }
        alertError = builder.create();
        if (!isFinishing()) {
            alertError.show();
        }
    }

    class forgotMPINConfirmationAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
            sharedPreferences.edit().putString("profileId", "").apply();
            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, Constants.TRANSACTION_FORGOTPIN);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID, Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_KEY, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sourceMDN);
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SCTL, stSctlID);
            if(mfaMode.toString().equalsIgnoreCase("OTP")){
                mapContainer.put(Constants.PARAMETER_MFA_OTP, CryptoService.encryptWithPublicKey(module, exponent, otpValue.getBytes()));
            }else{
                mapContainer.put(Constants.PARAMETER_MFA_OTP, "");
            }
            Log.d(LOG_TAG,"mfaOtp "+CryptoService.encryptWithPublicKey(module, exponent, otpValue.getBytes()));
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    SecurityQuestionsActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SecurityQuestionsActivity.this);
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

                        if (msgCode == 631) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            alertbox = new AlertDialog.Builder(SecurityQuestionsActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(SecurityQuestionsActivity.this, SecondLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                            alertbox.show();
                        }else if(msgCode==2306){
                            Intent intent = new Intent(SecurityQuestionsActivity.this, NotificationActivity.class);
                            intent.putExtra("status", "daftaremoney");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            SecurityQuestionsActivity.this.finish();
                        }else{
                            alertbox = new AlertDialog.Builder(SecurityQuestionsActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });
                            alertbox.show();
                            dialogBuilder.dismiss();
                        }
                    }
                }catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }
        }
    }


}
