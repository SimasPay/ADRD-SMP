package com.payment.simaspay.UserActivation;

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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 12/22/2015.
 */
public class ActivationPage_2_Activity extends Activity {

    TextView text_1, text_2, text_3, text_4, text_5, text_6, text_7;


    EditText e_Mdn, e_mPin;
    ProgressDialog progressDialog;

    Button lanjut;
    Dialog dialogCustomWish;
    Context context;

    String idnumber, otpValue, sctl;

    String rsaKey, mobileNumber, otp, name;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String body = intent.getExtras().getString("message");
                if (body.contains("Kode Simobi Anda")
                        && body.contains(idnumber)) {

                    otpValue = body
                            .substring(
                                    body.indexOf("Kode Simobi Anda ")
                                            + new String(
                                            "Kode Simobi Anda ")
                                            .length(),
                                    body.indexOf(" (no ref")).trim();
                    sctl = body.substring(
                            body.indexOf("no ref: ")
                                    + new String("no ref: ").length(),
                            body.indexOf(")")).trim();
                    handler.removeCallbacks(runnable);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent1 = new Intent(ActivationPage_2_Activity.this, ActivationPage_3_Activity.class);
                    intent1.putExtra("SctlID", idnumber);
                    intent1.putExtra("mailedOtp", otp);
                    if (mobileNumber.startsWith("62")) {
                        intent1.putExtra("mobileNumber", mobileNumber);
                    } else if (mobileNumber.startsWith("0")) {
                        intent1.putExtra("mobileNumber", "62" + mobileNumber.substring(1));
                    } else {
                        intent1.putExtra("mobileNumber", "62" + mobileNumber);
                    }
                    intent1.putExtra("otpValue", otpValue);
                    intent1.putExtra("mfaMode", "OTP");
                    intent1.putExtra("name", name);
                    startActivityForResult(intent1, 20);

                } else if (body.contains("Your Simobi Code is ")
                        && body.contains(idnumber)) {
                    otpValue = body
                            .substring(
                                    body.indexOf("Your Simobi Code is ")
                                            + new String(
                                            "Your Simobi Code is ")
                                            .length(),
                                    body.indexOf("(ref")).trim();
                    sctl = body.substring(
                            body.indexOf("(ref no: ")
                                    + new String("(ref no: ").length(),
                            body.indexOf(")")).trim();
                    handler.removeCallbacks(runnable);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent1 = new Intent(ActivationPage_2_Activity.this, ActivationPage_3_Activity.class);
                    intent1.putExtra("SctlID", idnumber);
                    intent1.putExtra("mailedOtp", otp);
                    intent1.putExtra("name", name);
                    if (mobileNumber.startsWith("62")) {
                        intent1.putExtra("mobileNumber", mobileNumber);
                    } else if (mobileNumber.startsWith("0")) {
                        intent1.putExtra("mobileNumber", "62" + mobileNumber.substring(1));
                    } else {
                        intent1.putExtra("mobileNumber", "62" + mobileNumber);
                    }
                    intent1.putExtra("mfaMode", "OTP");
                    intent1.putExtra("otpValue", otpValue);
                    startActivityForResult(intent1, 20);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Handler handler = new Handler();

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("com.msg.simaspay"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_2);
        context = this;

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }
        e_Mdn = (EditText) findViewById(R.id.hand_phno);
        e_mPin = (EditText) findViewById(R.id.otp);
        text_1 = (TextView) findViewById(R.id.text_1);
        text_2 = (TextView) findViewById(R.id.text_2);
        text_3 = (TextView) findViewById(R.id.text_3);
        text_4 = (TextView) findViewById(R.id.text_4);
        text_5 = (TextView) findViewById(R.id.text_5);
        text_6 = (TextView) findViewById(R.id.text_6);
        text_7 = (TextView) findViewById(R.id.text_7);

        e_Mdn.setTypeface(Utility.Robot_Light(ActivationPage_2_Activity.this));
        e_mPin.setTypeface(Utility.Robot_Light(ActivationPage_2_Activity.this));

        text_1.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_2.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_3.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_4.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_5.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_6.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_7.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));


        String htmlString1 = "<u>Kirim Ulan</u>" + "g";
        text_5.setText(Html.fromHtml(htmlString1));

        String htmlString = "<u>Lo</u>" + "g" + "<u>in</u>";
        text_7.setText(Html.fromHtml(htmlString));

        lanjut = (Button) findViewById(R.id.next);


        text_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean networkCheck = Utility.isConnectingToInternet(context);
                if (!networkCheck) {
                    Utility.networkDisplayDialog(
                            getResources().getString(
                                    R.string.bahasa_serverNotRespond), context);

                } else if (e_Mdn.getText().toString().equals("")) {
                    Utility.networkDisplayDialog("Masukkan Nomor Handphone", ActivationPage_2_Activity.this);
                } else if (e_Mdn.getText().toString().replace(" ", "")
                        .length() < 7) {
                    Utility.networkDisplayDialog("Nomor Handphone harus lebih dari 6 angka",
                            ActivationPage_2_Activity.this);
                } else {

                    mobileNumber = e_Mdn.getText().toString();
                    new ResendOtpAsyn().execute();
                }

            }
        });

        lanjut.setTypeface(Utility.Robot_Regular(ActivationPage_2_Activity.this));

        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean networkCheck = Utility.isConnectingToInternet(context);
                if (!networkCheck) {
                    Utility.networkDisplayDialog(
                            getResources().getString(
                                    R.string.bahasa_serverNotRespond), context);

                } else if (e_Mdn.getText().toString().equals("")) {
                    Utility.networkDisplayDialog("Masukkan Nomor Handphone", ActivationPage_2_Activity.this);
                } else if (e_Mdn.getText().toString().replace(" ", "")
                        .length() < 7) {
                    Utility.networkDisplayDialog("Nomor Handphone harus lebih dari 6 angka",
                            ActivationPage_2_Activity.this);
                } else if (e_mPin.getText().toString().equals("")) {
                    Utility.networkDisplayDialog("Masukkan OTP Anda", ActivationPage_2_Activity.this);
                } else {
                    mobileNumber = e_Mdn.getText().toString();
                    otp = e_mPin.getText().toString();
                    new ActivationAsyn().execute();
                }

            }
        });


        text_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                setResult(13, i);
                finish();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                Intent i = getIntent();
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = getIntent();
        setResult(Activity.RESULT_CANCELED, i);
        finish();
    }

    public void ResendOtp(final String string) {

        dialogCustomWish = new Dialog(context);
        dialogCustomWish.setCancelable(false);

        dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        View view = LayoutInflater.from(context).inflate(R.layout.resend_otp_dialog, null);
        dialogCustomWish.setContentView(R.layout.resend_otp_dialog);

        Button button = (Button) dialogCustomWish.findViewById(R.id.OK);
        TextView textView = (TextView) dialogCustomWish.findViewById(R.id.number);
        TextView textView1 = (TextView) dialogCustomWish.findViewById(R.id.number_1);
        button.setTypeface(Utility.HelveticaNeue_Medium(context));
        textView.setTypeface(Utility.HelveticaNeue_Medium(context));
        textView1.setTypeface(Utility.LightTextFormat(context));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCustomWish.dismiss();
            }
        });
        dialogCustomWish.show();


    }

    int msgCode;

    class ActivationAsyn extends AsyncTask<Void, Void, Void> {

        String response;

        @Override
        protected Void doInBackground(Void... params) {
            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

            try {
                rsaKey = CryptoService.encryptWithPublicKey(module, exponent,
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
                    Constants.TRANSACTION_ACTIVATION);
            if (mobileNumber.startsWith("62")) {
                mapContainer.put(Constants.PARAMETER_SOURCE_MDN, mobileNumber);
            } else if (mobileNumber.startsWith("0")) {
                mapContainer.put(Constants.PARAMETER_SOURCE_MDN, "62" + mobileNumber.substring(1));
            } else {
                mapContainer.put(Constants.PARAMETER_SOURCE_MDN, "62" + mobileNumber);
            }
            mapContainer.put(Constants.PARAMETER_OTP, rsaKey);
            mapContainer.put(Constants.TRANSACTION_ISSIMASPAYACTIVITY, Constants.CONSTANT_VALUE_TRUE);
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION);
            mapContainer.put(Constants.PARAMETER_ACTIVATION_CONFIRMPIN, "");
            mapContainer.put(Constants.PARAMETER_ACTIVATION_NEWPIN, "");

            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, ActivationPage_2_Activity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivationPage_2_Activity.this);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (response != null) {
                Log.e("------------", "------" + response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseContainer = null;
                try {
                    responseContainer = obj.parse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 2040) {
                    if (responseContainer.getMfaMode().equalsIgnoreCase("OTP")) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        idnumber = responseContainer.getSctl();
                        name = responseContainer.getName();
                        handler.postDelayed(runnable, 60000);
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Intent intent1 = new Intent(ActivationPage_2_Activity.this, ActivationPage_3_Activity.class);
                        intent1.putExtra("SctlID", responseContainer.getSctl());
                        intent1.putExtra("mailedOtp", otp);
                        intent1.putExtra("name", responseContainer.getName());
                        if (mobileNumber.startsWith("62")) {
                            intent1.putExtra("mobileNumber", mobileNumber);
                        } else if (mobileNumber.startsWith("0")) {
                            intent1.putExtra("mobileNumber", "62" + mobileNumber.substring(1));
                        } else {
                            intent1.putExtra("mobileNumber", "62" + mobileNumber);
                        }
                        intent1.putExtra("mfaMode", "None");
                        startActivityForResult(intent1, 20);
                    }

                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (responseContainer.getMsg() == null) {
                        Utility.networkDisplayDialog(
                                sharedPreferences.getString(
                                        "ErrorMessage",
                                        getResources()
                                                .getString(
                                                        R.string.server_error_message)),
                                ActivationPage_2_Activity.this);
                        e_mPin.setText("");
                    } else {
                        e_mPin.setText("");
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), ActivationPage_2_Activity.this);
                    }
                }

            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), ActivationPage_2_Activity.this);
            }
        }
    }

    class ResendOtpAsyn extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
//            if (mobileNumber.startsWith("62")) {
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, mobileNumber);
//            } else if (mobileNumber.startsWith("0")) {
//                mapContainer.put(Constants.PARAMETER_SOURCE_MDN, "62" + mobileNumber.substring(1));
//            } else {
//                mapContainer.put(Constants.PARAMETER_SOURCE_MDN, "62" + mobileNumber);
//            }
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_RESEND_OTP);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, ActivationPage_2_Activity.this);

            response = webServiceHttp.getResponseSSLCertificatation();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivationPage_2_Activity.this);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null) {
                Log.e("--------", "------" + response);
                progressDialog.dismiss();
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseContainer = null;
                try {
                    responseContainer = obj.parse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }

                if (msgCode == 0) {
                    ResendOtp("");
                } else {
                    Utility.networkDisplayDialog(responseContainer.getMsg(), ActivationPage_2_Activity.this);
                }

            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), ActivationPage_2_Activity.this);
            }
        }
    }
}
