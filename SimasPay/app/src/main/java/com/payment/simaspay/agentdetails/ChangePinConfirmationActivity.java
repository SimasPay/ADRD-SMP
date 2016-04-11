package com.payment.simaspay.agentdetails;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SessionTimeOutActivity;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 3/23/2016.
 */
public class ChangePinConfirmationActivity extends Activity {

    TextView textView, textView1, textView2, title;

    TextView editText, editText1, editText2;

    Button simpan,cancel;
    LinearLayout btnBacke;



    SharedPreferences sharedPreferences;

    ProgressDialog progressDialog;

    String otpValue="", sctl;

    boolean timeroutornot;


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("com.msg.simaspay"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String body = intent.getExtras().getString("message");

                if (body.contains(getResources().getString(R.string.BahasaSmsData))
                        && body.contains(getIntent().getExtras().getString("sctlID"))) {
                    otpValue = body
                            .substring(
                                    body.indexOf(getResources().getString(R.string.BahasaSmsData))
                                            + new String(
                                            getResources().getString(R.string.BahasaSmsData))
                                            .length(),
                                    body.indexOf(" (no ref")).trim();
                    sctl = body.substring(
                            body.indexOf("no ref: ")
                                    + new String("no ref: ").length(),
                            body.indexOf(")")).trim();
                    SMSAlert(otpValue);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    handlerforTimer.removeCallbacks(runnableforExit);

                } else if (body.contains(getResources().getString(R.string.EnglishSmsData))
                        && body.contains(getIntent().getExtras().getString("sctlID"))) {
                    otpValue = body
                            .substring(
                                    body.indexOf(getResources().getString(R.string.EnglishSmsData))
                                            + new String(
                                            getResources().getString(R.string.EnglishSmsData))
                                            .length(),
                                    body.indexOf("(ref")).trim();
                    sctl = body.substring(
                            body.indexOf("(ref no: ")
                                    + new String("(ref no: ").length(),
                            body.indexOf(")")).trim();
                    SMSAlert(otpValue);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    handlerforTimer.removeCallbacks(runnableforExit);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepinconfirmation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }
        textView = (TextView) findViewById(R.id.text1);
        textView1 = (TextView) findViewById(R.id.text2);
        textView2 = (TextView) findViewById(R.id.text3);

        context=this;

        progressDialog = new ProgressDialog(ChangePinConfirmationActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));

        title = (TextView) findViewById(R.id.titled);

        editText = (TextView) findViewById(R.id.editText1);
        editText1 = (TextView) findViewById(R.id.editText2);
        editText2 = (TextView) findViewById(R.id.editText3);

        editText.setText(getIntent().getExtras().getString("oldpinValue"));
        editText1.setText(getIntent().getExtras().getString("newpinValue"));
        editText2.setText(getIntent().getExtras().getString("confirmpinValue"));

        simpan = (Button) findViewById(R.id.changepin);
        btnBacke = (LinearLayout) findViewById(R.id.back_layout);

        cancel=(Button)findViewById(R.id.cancel);

        btnBacke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    unregisterReceiver(broadcastReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    handlerforTimer.removeCallbacks(runnableforExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent=getIntent();
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);


        textView1.setTypeface(Utility.Robot_Regular(ChangePinConfirmationActivity.this));
        textView2.setTypeface(Utility.Robot_Regular(ChangePinConfirmationActivity.this));
        textView.setTypeface(Utility.Robot_Regular(ChangePinConfirmationActivity.this));

        title.setTypeface(Utility.RegularTextFormat(ChangePinConfirmationActivity.this));

        editText2.setTypeface(Utility.Robot_Light(ChangePinConfirmationActivity.this));
        editText.setTypeface(Utility.Robot_Light(ChangePinConfirmationActivity.this));
        editText1.setTypeface(Utility.Robot_Light(ChangePinConfirmationActivity.this));

        simpan.setTypeface(Utility.Robot_Regular(ChangePinConfirmationActivity.this));
        cancel.setTypeface(Utility.Robot_Regular(ChangePinConfirmationActivity.this));

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                        if (otpValue.equalsIgnoreCase("")) {
                            if (!progressDialog.isShowing()) {
                                progressDialog.setMessage(getResources().getString(R.string.waitingSms));
                                progressDialog.show();
                            }
                        } else {
                            if (dialogCustomWish.isShowing()) {
                                new ChangePinConfirmationAsyn().execute();
                            } else {
                                dialogCustomWish.show();
                            }
                        }
                    }else{
                        if(timeroutornot){
                            Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), ChangePinConfirmationActivity.this);
                        }else{
                            handlerforTimer.removeCallbacks(runnableforExit);
                            new ChangePinConfirmationAsyn().execute();
                        }
                    }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    unregisterReceiver(broadcastReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    handlerforTimer.removeCallbacks(runnableforExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent=getIntent();
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });

        handlerforTimer.postDelayed(runnableforExit, 90000);
    }

    Handler handlerforTimer = new Handler();
    Runnable runnableforExit = new Runnable() {
        @Override
        public void run() {
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            timeroutornot = true;
            Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), ChangePinConfirmationActivity.this);

        }
    };

    Context context;
    Dialog dialogCustomWish;

    public void SMSAlert(final String string) {

        dialogCustomWish = new Dialog(context);
        dialogCustomWish.setCancelable(false);

        dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        View view = LayoutInflater.from(context).inflate(R.layout.sms_alert, null);
        dialogCustomWish.setContentView(R.layout.sms_alert);

        Button button = (Button) dialogCustomWish.findViewById(R.id.ok);
        Button button1 = (Button) dialogCustomWish.findViewById(R.id.Cancel);
        TextView textView = (TextView) dialogCustomWish.findViewById(R.id.number);
        TextView textView_1 = (TextView) dialogCustomWish.findViewById(R.id.number_1);

        textView_1.setText("Kode OTP dan link telah dikirimkan ke nomor " + sharedPreferences.getString("mobileNumber", "") + ". Masukkan kode tersebut atau akses link yang tersedia.");
        button.setTypeface(Utility.RegularTextFormat(context));
        button1.setTypeface(Utility.RegularTextFormat(context));
        textView.setTypeface(Utility.RegularTextFormat(context));
        textView_1.setTypeface(Utility.Robot_Regular(context));

        EditText editText = (EditText) dialogCustomWish.findViewById(R.id.otpCode);
        editText.setHint("6 digit kode OTP");
        editText.setText(string);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogCustomWish.dismiss();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCustomWish.dismiss();
                handler12.postDelayed(runnable12, 1000);
            }
        });
        dialogCustomWish.show();


    }

    Handler handler12 = new Handler();
    Runnable runnable12 = new Runnable() {
        @Override
        public void run() {
            handlerforTimer.removeCallbacks(runnableforExit);
            new ChangePinConfirmationAsyn().execute();
        }
    };

    WebServiceHttp webServiceHttp;
    String confirmationResponse;
    String encryptedOtp;
    int msgCode;

    class ChangePinConfirmationAsyn extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_CHANGEPIN);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, getIntent().getExtras().getString("oldpin"));
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_NEW_PIN, getIntent().getExtras().getString("newPin"));
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION_CONFIRM);
            mapContainer.put(Constants.PARAMETER_CONFIRM_PIN, getIntent().getExtras().getString("ConfirmPin"));
            mapContainer.put(Constants.PARAMETER_PARENTTXN_ID, getIntent().getExtras().getString("sctlID"));
            if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                String module = sharedPreferences.getString("MODULE", "NONE");
                String exponent = sharedPreferences.getString("EXPONENT", "NONE");
                try {
                    encryptedOtp = CryptoService.encryptWithPublicKey(module, exponent,
                            otpValue.getBytes());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                mapContainer.put(Constants.PARAMETER_MFA_OTP, encryptedOtp);
            } else {
                mapContainer.put(Constants.PARAMETER_MFA_OTP, "");
            }

            webServiceHttp = new WebServiceHttp(mapContainer, ChangePinConfirmationActivity.this);

            confirmationResponse = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog != null) {
                progressDialog.show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(confirmationResponse!=null){
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseContainer = null;
                try {
                    responseContainer = obj.parse(confirmationResponse);
                } catch (Exception e) {
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                try {
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if(msgCode==26){
                    sharedPreferences.edit().putString("password",getIntent().getExtras().getString("newPin")).commit();
                    Intent intent=new Intent(ChangePinConfirmationActivity.this,ChangePinSuccessActivity.class);
                    startActivityForResult(intent,10);
                }else if(msgCode==661){
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(ChangePinConfirmationActivity.this, SessionTimeOutActivity.class);
                    startActivityForResult(intent, 40);
                }else {
                    if (responseContainer.getMsg() == null) {
                        Utility.networkDisplayDialog(sharedPreferences.getString(
                                "ErrorMessage",
                                getResources().getString(
                                        R.string.bahasa_serverNotRespond)), ChangePinConfirmationActivity.this);
                    } else {
                        Utility.networkDisplayDialog(responseContainer.getMsg(), ChangePinConfirmationActivity.this);
                    }
                }
            }else{
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), ChangePinConfirmationActivity.this);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                handlerforTimer.removeCallbacks(runnableforExit);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent=getIntent();
            setResult(RESULT_CANCELED,intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(resultCode==RESULT_OK){
                Intent intent=getIntent();
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    }
}
