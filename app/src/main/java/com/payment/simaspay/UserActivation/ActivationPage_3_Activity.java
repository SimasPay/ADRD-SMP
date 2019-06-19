package com.payment.simaspay.UserActivation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

import com.payment.simaspay.R;

public class ActivationPage_3_Activity extends AppCompatActivity {
    SharedPreferences sharedPreferences, languageSettings;
    TextView text_1, text_2, text_3, text_4;
    EditText e_pin, e_con_mPin;
    Button lanjut;
    String pin, ConfirmPin, otpValue, mailedOtp;
    String EncryptedPin, Encrypted_ConfirmPin, encryptedOtp, EncryptedMailedOtp;
    String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_confirmation);

        sharedPreferences = getSharedPreferences("SimasPay", MODE_PRIVATE);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        e_pin = (EditText) findViewById(R.id.mpin);
        e_con_mPin = (EditText) findViewById(R.id.confirm_mpin);
        text_1 = (TextView) findViewById(R.id.text_1);
        text_2 = (TextView) findViewById(R.id.text_2);
        text_3 = (TextView) findViewById(R.id.text_3);
        text_4 = (TextView) findViewById(R.id.text_8);


        e_pin.setTypeface(Utility.Robot_Light(ActivationPage_3_Activity.this));
        e_con_mPin.setTypeface(Utility.Robot_Light(ActivationPage_3_Activity.this));

        text_1.setTypeface(Utility.LightTextFormat(ActivationPage_3_Activity.this));
        text_2.setTypeface(Utility.Robot_Regular(ActivationPage_3_Activity.this));
        text_3.setTypeface(Utility.Robot_Light(ActivationPage_3_Activity.this));
        text_4.setTypeface(Utility.Robot_Light(ActivationPage_3_Activity.this));

        text_2.setText(getIntent().getExtras().getString("name"));


        lanjut = (Button) findViewById(R.id.next);

        lanjut.setTypeface(Utility.Robot_Regular(ActivationPage_3_Activity.this));

        lanjut.setOnClickListener(view -> {
            if (e_pin.getText().toString().replace(" ", "").length() == 0) {
                Utility.displayDialog("Masukkan mPIN", ActivationPage_3_Activity.this);
            } else if (e_pin.getText().toString().replace(" ", "").length() < 6) {
                Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), ActivationPage_3_Activity.this);
            } else if (e_con_mPin.getText().toString().replace(" ", "").length() == 0) {
                Utility.displayDialog("Masukkan Konfirmasi mPIN", ActivationPage_3_Activity.this);
            }else if (e_con_mPin.getText().toString().replace(" ", "").length() < 6) {
                Utility.displayDialog("Konfirmasi "+getResources().getString(R.string.mPinLegthMessage), ActivationPage_3_Activity.this);
            } else if (!e_pin.getText().toString().equals(e_con_mPin.getText().toString())) {
                Utility.displayDialog("mPIN dan konfirmasi mPIN yang Anda masukkan harus sama.", ActivationPage_3_Activity.this);
            } else {
                pin = e_pin.getText().toString();
                ConfirmPin = e_con_mPin.getText().toString();
                if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                    otpValue = getIntent().getExtras().getString("otpValue");
                } else {
                    otpValue = "";
                }
                mailedOtp = getIntent().getExtras().getString("mailedOtp");
                new ActivationAsyn().execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 30) {
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

    int msgCode;

    private class ActivationAsyn extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

            try {
                EncryptedPin = CryptoService.encryptWithPublicKey(module, exponent,
                        pin.getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                Encrypted_ConfirmPin = CryptoService.encryptWithPublicKey(module, exponent,
                        ConfirmPin.getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                try {
                    encryptedOtp = CryptoService.encryptWithPublicKey(module, exponent,
                            otpValue.getBytes());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                otpValue = "";
            }

            try {
                EncryptedMailedOtp = CryptoService.encryptWithPublicKey(module, exponent,
                        mailedOtp.getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Log.e(mailedOtp + "-----" + otpValue, "=====" + getIntent().getExtras().getString("otpValue"));

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_ACTIVATION);
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, getIntent().getExtras().getString("mobileNumber"));
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION_CONFIRM);
            mapContainer.put(Constants.PARAMETER_ACTIVATION_CONFIRMPIN, Encrypted_ConfirmPin);
            mapContainer.put(Constants.PARAMETER_ACTIVATION_NEWPIN, EncryptedPin);
            mapContainer.put(Constants.PARAMETER_PARENTTXN_ID, getIntent().getExtras().getString("SctlID"));
            if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                mapContainer.put(Constants.PARAMETER_MFA_OTP, encryptedOtp);
            } else {
                mapContainer.put(Constants.PARAMETER_MFA_OTP, "");
            }
            mapContainer.put(Constants.PARAMETER_OTP, EncryptedMailedOtp);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, ActivationPage_3_Activity.this);
            Log.e("========", "=======" + mapContainer.toString());
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivationPage_3_Activity.this);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null) {
                Log.e("-----", "=====" + response);
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
                if (msgCode == 52 || msgCode == 2032) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(ActivationPage_3_Activity.this, ActivationPage_4_Activity.class);
                    startActivityForResult(intent, 30);
                } else {
                    progressDialog.dismiss();
                    if (responseContainer.getMsg() == null) {
                        Utility.networkDisplayDialog(
                                sharedPreferences.getString(
                                        "ErrorMessage",
                                        getResources()
                                                .getString(
                                                        R.string.server_error_message)),
                                ActivationPage_3_Activity.this);

                    } else {

                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), ActivationPage_3_Activity.this);
                    }
                }

            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), ActivationPage_3_Activity.this);
            }
        }
    }


}
