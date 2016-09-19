package com.payment.simaspay.Referral_and_CloseAccount;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
 * Created by Nagendra P on 1/27/2016.
 */
public class CloseAccountConfirmationActivity extends Activity {
    TextView title, heading, name, name_field, number, number_field, amount, amount_field;

    Button cancel, confirmation;

    LinearLayout back;

    Context context;
    Dialog dialogCustomWish;
    int i = 0;


    boolean Timervalueout;

    boolean nextpressedornot;

    String otpValue = "", sctl;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String body = intent.getExtras().getString("message");
                if (body.contains("Kode OTP Simaspay anda")
                        ) {

                    otpValue = body
                            .substring(
                                    body.indexOf("Kode OTP Simaspay anda ")
                                            + new String(
                                            "Kode OTP Simaspay anda ")
                                            .length(),
                                    body.indexOf(". ")).trim();

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (nextpressedornot) {
                        handlerforTimer.removeCallbacks(runnableforExit);
                    }


                } else if (body.contains("Your Simaspay code is ")
                        ) {
                    otpValue = body
                            .substring(
                                    body.indexOf("Your Simaspay code is ")
                                            + new String(
                                            "Your Simaspay code is ")
                                            .length(),
                                    body.indexOf(". ")).trim();
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (nextpressedornot) {
                        handlerforTimer.removeCallbacks(runnableforExit);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    Handler handlerforTimer = new Handler();

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
            Timervalueout = true;
            Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), CloseAccountConfirmationActivity.this);

        }
    };


    ProgressDialog progressDialog;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("com.msg.simaspay"));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.commonconfirmation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        title = (TextView) findViewById(R.id.title);
        heading = (TextView) findViewById(R.id.textview);
        name = (TextView) findViewById(R.id.name);
        name_field = (TextView) findViewById(R.id.name_field);
        number = (TextView) findViewById(R.id.number);
        number_field = (TextView) findViewById(R.id.number_field);
        amount = (TextView) findViewById(R.id.amount);
        amount_field = (TextView) findViewById(R.id.amount_field);

        number.setText("Nomor Rekening");

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        amount.setVisibility(View.GONE);
        amount_field.setVisibility(View.GONE);

        number_field.setText(getIntent().getExtras().getString("DestMDN"));
        name_field.setText(getIntent().getExtras().getString("Name"));

        cancel = (Button) findViewById(R.id.cancel);
        confirmation = (Button) findViewById(R.id.next);

        back = (LinearLayout) findViewById(R.id.back_layout);

        progressDialog = new ProgressDialog(CloseAccountConfirmationActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));

        title.setTypeface(Utility.Robot_Regular(CloseAccountConfirmationActivity.this));
        heading.setTypeface(Utility.Robot_Regular(CloseAccountConfirmationActivity.this));
        name.setTypeface(Utility.Robot_Regular(CloseAccountConfirmationActivity.this));
        name_field.setTypeface(Utility.Robot_Light(CloseAccountConfirmationActivity.this));
        number.setTypeface(Utility.Robot_Regular(CloseAccountConfirmationActivity.this));
        number_field.setTypeface(Utility.Robot_Light(CloseAccountConfirmationActivity.this));
        amount.setTypeface(Utility.Robot_Regular(CloseAccountConfirmationActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(CloseAccountConfirmationActivity.this));
        cancel.setTypeface(Utility.Robot_Regular(CloseAccountConfirmationActivity.this));
        confirmation.setTypeface(Utility.Robot_Regular(CloseAccountConfirmationActivity.this));

        if (!getIntent().getExtras().getBoolean("minBal")) {
            confirmation.setText("Kembali");
            cancel.setVisibility(View.GONE);
            heading.setText(getIntent().getExtras().getString("msg"));
            heading.setTextColor(getResources().getColor(R.color.reg));
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    unregisterReceiver(broadcastReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!Timervalueout) {
                    handlerforTimer.removeCallbacks(runnableforExit);
                }
                Intent intent = getIntent();
                setResult(11, intent);
                finish();
            }
        });

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!confirmation.getText().toString().equalsIgnoreCase("Kembali")) {
                    if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                        nextpressedornot = true;
                        if (Timervalueout) {
                            Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), CloseAccountConfirmationActivity.this);
                        } else {
                            if (otpValue.equals("")) {
                                progressDialog.show();
                            } else {
                                handlerforTimer.removeCallbacks(runnableforExit);
                                new CloseAccountConfirmationAsyn().execute();
                            }
                        }
                    } else {

                        SMSAlert("");
                    }
                } else {
                    Intent intent = getIntent();
                    setResult(11, intent);
                    finish();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i == 0) {
                    try {
                        unregisterReceiver(broadcastReceiver);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!Timervalueout) {
                        handlerforTimer.removeCallbacks(runnableforExit);
                    }
                    Intent intent = getIntent();
                    setResult(11, intent);
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!Timervalueout) {
            handlerforTimer.removeCallbacks(runnableforExit);
        }
        Intent intent = getIntent();
        setResult(11, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 10) {
                Intent intent = getIntent();
                setResult(10, intent);
                finish();
            } else {

            }
        }
    }

    TextView textView1;

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
        button.setTypeface(Utility.RegularTextFormat(context));
        button1.setTypeface(Utility.RegularTextFormat(context));
        textView.setTypeface(Utility.RegularTextFormat(context));
        textView_1.setTypeface(Utility.Robot_Regular(context));

        textView1 = (TextView) dialogCustomWish.findViewById(R.id.timer);
        textView1.setVisibility(View.GONE);

//        textView_1.setText("Kode OTP dan link telah dikirimkan ke nomor " + getIntent().getExtras().getString("DestMDN") + " Masukkan kode tersebut atau akses link yang tersedia.");
        textView.setText("Masukkan Kode Verifikasi");
        textView_1.setText("Kode Verifikasi telah dikirimkan ke nasabah dengan nomor HP " + getIntent().getExtras().getString("DestMDN") );
        final EditText otpCode = (EditText) dialogCustomWish.findViewById(R.id.otpCode);


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

                if (otpCode.getText().toString().length() <= 0) {

                } else {
                    otp = otpCode.getText().toString();

                    handler12.postDelayed(runnable12, 1000);
                }
            }
        });
        dialogCustomWish.show();


    }

    Handler handler12 = new Handler();
    Runnable runnable12 = new Runnable() {
        @Override
        public void run() {
            new CloseAccountConfirmationAsyn().execute();
        }
    };

    SharedPreferences sharedPreferences;
    String encryptedmfaOTP, otp, encryptedOTP;
    int msgCode;

    class CloseAccountConfirmationAsyn extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {

            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

            try {
                encryptedOTP = CryptoService.encryptWithPublicKey(module, exponent,
                        otp.getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_CLOSEACCOUNT);
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_KEY, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, sharedPreferences.getString("password", ""));
            mapContainer.put(Constants.PARAMETER_DEST_MDN, getIntent().getExtras().getString("DestMDN"));
            mapContainer.put(Constants.PARAMETER_PARENTTXN_ID, getIntent().getExtras().getString("SctlID"));
            if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                try {
                    encryptedmfaOTP = CryptoService.encryptWithPublicKey(module, exponent,
                            otpValue.getBytes());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                mapContainer.put(Constants.PARAMETER_MFA_OTP, encryptedmfaOTP);
            } else {
                mapContainer.put(Constants.PARAMETER_MFA_OTP, "");
            }
            mapContainer.put(Constants.PARAMETER_OTP, encryptedOTP);

            Log.e("----------", "--------" + mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, CloseAccountConfirmationActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            if (progressDialog != null) {
                progressDialog.show();
            } else {
                progressDialog = new ProgressDialog(CloseAccountConfirmationActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
                progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
                progressDialog.show();
            }
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
                progressDialog.dismiss();
                try {
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 2130) {
                    Intent intent = new Intent(CloseAccountConfirmationActivity.this, CloseAccountSuccessActivity.class);
                    intent.putExtra("Name", responseContainer.getName());
                    intent.putExtra("DestMDN", responseContainer.getDestMDN());
                    startActivityForResult(intent, 10);
                }  else if (msgCode == 631) {
                    Intent intent = new Intent(CloseAccountConfirmationActivity.this, SessionTimeOutActivity.class);
                    startActivityForResult(intent, 40);
                } else {
                    if (responseContainer.getMsg() != null) {
                        Utility.networkDisplayDialog(responseContainer.getMsg(), CloseAccountConfirmationActivity.this);
                    } else {
                        Utility.networkDisplayDialog(sharedPreferences.getString(
                                "ErrorMessage",
                                getResources().getString(
                                        R.string.bahasa_serverNotRespond)), CloseAccountConfirmationActivity.this);
                    }
                }
            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), CloseAccountConfirmationActivity.this);
            }
        }
    }

}
