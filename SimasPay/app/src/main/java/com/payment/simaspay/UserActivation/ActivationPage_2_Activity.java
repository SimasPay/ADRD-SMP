package com.payment.simaspay.UserActivation;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.receivers.IncomingSMS;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.utils.Functions;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;


public class ActivationPage_2_Activity extends AppCompatActivity implements IncomingSMS.AutoReadSMSListener {
    TextView text_1, text_2, text_3, text_4, text_5, text_6, text_7;
    EditText e_Mdn, e_mPin;
    ProgressDialog progressDialog;
    Button lanjut;
    Dialog dialogCustomWish;
    Context context;
    String idnumber, otpValue;
    String rsaKey, mobileNumber, otp, name;
    SharedPreferences sharedPreferences;
    private static final String LOG_TAG = "SimasPay";
    private EditText edt;
    private static AlertDialog dialogBuilder;
    LinearLayout otplay, otp2lay;
    String response;
    Functions func;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_2);
        context = this;

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        IncomingSMS.setListener(this);

        func=new Functions(this);
        func.initiatedToolbar(this);

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

        e_mPin.setHint("6 digit kode aktivasi");

        text_1.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_2.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_3.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_4.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_5.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_6.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_7.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));


        String htmlString1 = "<u>Kirim Ulang</u>";
        text_5.setText(Html.fromHtml(htmlString1));

        String htmlString = "<u>Lo</u>" + "g" + "<u>in</u>";
        text_7.setText(Html.fromHtml(htmlString));

        progressDialog = new ProgressDialog(ActivationPage_2_Activity.this);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
        progressDialog.setCancelable(false);

        lanjut = (Button) findViewById(R.id.next);


        text_5.setOnClickListener(view -> {
            boolean networkCheck = Utility.isConnectingToInternet(context);
            if (!networkCheck) {
                Utility.networkDisplayDialog(
                        getResources().getString(
                                R.string.bahasa_serverNotRespond), context);

            } else if (e_Mdn.getText().toString().equals("")) {
                Utility.networkDisplayDialog("SimasPay Harap masukkan nomor handphone Anda", ActivationPage_2_Activity.this);
            } else if (e_Mdn.getText().toString().replace(" ", "")
                    .length() < 10) {
                Utility.networkDisplayDialog("SimasPay Nomor handphone yang Anda masukkan harus 10-14 angka.",
                        ActivationPage_2_Activity.this);
            } else {
                mobileNumber = e_Mdn.getText().toString();
                new ResendOtpAsyn().execute();
            }

        });

        lanjut.setTypeface(Utility.Robot_Regular(ActivationPage_2_Activity.this));

        lanjut.setOnClickListener(view -> {
            boolean networkCheck = Utility.isConnectingToInternet(context);
            if (!networkCheck) {
                Utility.networkDisplayDialog(
                        getResources().getString(
                                R.string.bahasa_serverNotRespond), context);

            } else if (e_Mdn.getText().toString().equals("")) {
                Utility.networkDisplayDialog("Masukkan Nomor Handphone", ActivationPage_2_Activity.this);
            } else if (e_Mdn.getText().toString().replace(" ", "")
                    .length() < 10) {
                Utility.networkDisplayDialog("Nomor Handphone harus lebih dari 10 angka",
                        ActivationPage_2_Activity.this);
            } else if (e_mPin.getText().toString().equals("")) {
                Utility.networkDisplayDialog("'SimasPay Harap masukkan kode aktivasi Anda.", ActivationPage_2_Activity.this);
            }  else if (e_mPin.getText().toString().replace(" ", "")
                    .length() < 6) {
                Utility.networkDisplayDialog("Kode aktivasi yang Anda masukkan harus 6 angka.",
                        ActivationPage_2_Activity.this);
            }else {

                int currentapiVersion = Build.VERSION.SDK_INT;
                if (currentapiVersion > Build.VERSION_CODES.LOLLIPOP) {
                    if ((checkCallingOrSelfPermission(Manifest.permission.READ_SMS)
                            != PackageManager.PERMISSION_GRANTED) && checkCallingOrSelfPermission(Manifest.permission.RECEIVE_SMS)
                            != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS},
                                109);
                    } else {
                        mobileNumber = e_Mdn.getText().toString();
                        otp = e_mPin.getText().toString();
                        new ActivationAsyn().execute();
                    }
                } else {
                    mobileNumber = e_Mdn.getText().toString();
                    otp = e_mPin.getText().toString();
                    new ActivationAsyn().execute();
                }
            }
        });


        text_7.setOnClickListener(view -> {
            Intent i = getIntent();
            setResult(13, i);
            finish();
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


        button.setOnClickListener(v -> dialogCustomWish.dismiss());
        dialogCustomWish.show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 109) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mobileNumber = e_Mdn.getText().toString();
                otp = e_mPin.getText().toString();
                //new ActivationAsyn().execute();
            }
        }
    }

    int msgCode;

    @Override
    public void onReadSMS(String otp) {
        Log.d(LOG_TAG, "otp from SMS: " + otp);
        edt.setText(otp);
        otpValue=otp;
    }

    private class ActivationAsyn extends AsyncTask<Void, Void, Void> {

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
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, mobileNumber);
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
                    msgCode = Integer.parseInt(responseContainer.getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 2040) {
                    if (responseContainer.getMfaMode().equalsIgnoreCase("OTP")) {
                        idnumber = responseContainer.getSctl();
                        name = responseContainer.getName();
                        showOTPRequiredDialog();
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Intent intent1 = new Intent(ActivationPage_2_Activity.this, ActivationPage_3_Activity.class);
                        intent1.putExtra("SctlID", responseContainer.getSctl());
                        intent1.putExtra("mailedOtp", otp);
                        intent1.putExtra("name", responseContainer.getName());
                        intent1.putExtra("mobileNumber", mobileNumber);
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

    private class ResendOtpAsyn extends AsyncTask<Void, Void, Void> {
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, mobileNumber);
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

    class MFAResendOTPAsyn extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_RESENDMFAOTP);
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN,mobileNumber);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
            mapContainer.put(Constants.PARAMETER_SCTLID, idnumber);

            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, context);

            response = webServiceHttp.getResponseSSLCertificatation();
            Log.e("-------------","------------"+mapContainer.toString());
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
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
                try {
                    msgCode = Integer.parseInt(responseContainer.getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }

                if (msgCode == 2171) {
                    dialogCustomWish.dismiss();
                    progressDialog.dismiss();
                    showOTPRequiredDialog();
                } else if (msgCode == 2172) {
                    dialogCustomWish.dismiss();
                    progressDialog.dismiss();
                    Utility.displayDialog(responseContainer.getMsg(), ActivationPage_2_Activity.this);

                } else if (msgCode == 2173) {
                    dialogCustomWish.dismiss();
                    progressDialog.dismiss();
                    Utility.displayDialog(responseContainer.getMsg(), ActivationPage_2_Activity.this);
                }else{
                    dialogCustomWish.dismiss();
                    progressDialog.dismiss();
                    Utility.displayDialog(responseContainer.getMsg(), ActivationPage_2_Activity.this);
                }
            } else {
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        context.getResources().getString(
                                R.string.bahasa_serverNotRespond)), context);
                dialogCustomWish.dismiss();
                progressDialog.dismiss();
            }
        }
    }

    private void showOTPRequiredDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullParent = null;
        View dialoglayout = inflater.inflate(R.layout.new_otp_dialog, nullParent, false);
        dialogBuilder = new AlertDialog.Builder(ActivationPage_2_Activity.this, R.style.MyAlertDialogStyle).create();
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
        ok_otp.setOnClickListener(v -> {
            if (edt.getText() == null || edt.getText().toString().equals("")) {
                func.errorEmptyOTP();
            } else {
                myTimer.cancel();
                otpValue=edt.getText().toString();
                Intent intent1 = new Intent(ActivationPage_2_Activity.this, ActivationPage_3_Activity.class);
                intent1.putExtra("SctlID", idnumber);
                intent1.putExtra("mailedOtp", otp);
                intent1.putExtra("mobileNumber", mobileNumber);
                intent1.putExtra("otpValue", otpValue);
                intent1.putExtra("mfaMode", "OTP");
                intent1.putExtra("name", name);
                startActivityForResult(intent1, 20);
            }
        });
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    ok_otp.setEnabled(false);
                } else {
                    ok_otp.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt.getText().length() > 5) {
                    Log.d(LOG_TAG, "otp dialog length: " + edt.getText().length());
                    myTimer.cancel();
                    otpValue=edt.getText().toString();
                    Intent intent1 = new Intent(ActivationPage_2_Activity.this, ActivationPage_3_Activity.class);
                    intent1.putExtra("SctlID", idnumber);
                    intent1.putExtra("mailedOtp", otp);
                    intent1.putExtra("mobileNumber", mobileNumber);
                    intent1.putExtra("otpValue", otpValue);
                    intent1.putExtra("mfaMode", "OTP");
                    intent1.putExtra("name", name);
                    startActivityForResult(intent1, 20);
                }

            }
        });
        dialogBuilder.show();
    }
}
