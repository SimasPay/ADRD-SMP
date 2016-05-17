package com.payment.simaspay.agentdetails;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
 * Created by Nagendra P on 1/12/2016.
 */
public class ChangePinActivity extends Activity {

    TextView textView, textView1, textView2, title;

    EditText editText, editText1, editText2;

    Button simpan;
    LinearLayout btnBacke;

    SharedPreferences sharedPreferences;

    ProgressDialog progressDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        textView = (TextView) findViewById(R.id.text1);
        textView1 = (TextView) findViewById(R.id.text2);
        textView2 = (TextView) findViewById(R.id.text3);

        progressDialog = new ProgressDialog(ChangePinActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));

        title = (TextView) findViewById(R.id.titled);

        editText = (EditText) findViewById(R.id.editText1);
        editText1 = (EditText) findViewById(R.id.editText2);
        editText2 = (EditText) findViewById(R.id.editText3);

        simpan = (Button) findViewById(R.id.changepin);
        btnBacke = (LinearLayout) findViewById(R.id.back_layout);

        btnBacke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);


        textView1.setTypeface(Utility.Robot_Regular(ChangePinActivity.this));
        textView2.setTypeface(Utility.Robot_Regular(ChangePinActivity.this));
        textView.setTypeface(Utility.Robot_Regular(ChangePinActivity.this));

        title.setTypeface(Utility.RegularTextFormat(ChangePinActivity.this));

        editText2.setTypeface(Utility.Robot_Light(ChangePinActivity.this));
        editText.setTypeface(Utility.Robot_Light(ChangePinActivity.this));
        editText1.setTypeface(Utility.Robot_Light(ChangePinActivity.this));

        simpan.setTypeface(Utility.Robot_Regular(ChangePinActivity.this));


        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().length() <= 0) {
                    Utility.displayDialog("Masukkan mPIN", ChangePinActivity.this);
                } else if (editText1.getText().toString().length() <= 0) {
                    Utility.displayDialog("Masukkan mPIN Baru", ChangePinActivity.this);
                } else if (editText2.getText().toString().length() <= 0) {
                    Utility.displayDialog("Masukkan Konfirmasi mPIN", ChangePinActivity.this);
                } else if (!editText1.getText().toString().equals(editText2.getText().toString())) {
                    Utility.displayDialog("mPIN and Konfirmasi mPIN need to be Same", ChangePinActivity.this);
                } else {

                    String module = sharedPreferences.getString("MODULE", "NONE");
                    String exponent = sharedPreferences.getString("EXPONENT", "NONE");
                    try {
                        oldPin = CryptoService.encryptWithPublicKey(module, exponent,
                                editText.getText().toString().getBytes());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    try {
                        newPin = CryptoService.encryptWithPublicKey(module, exponent,
                                editText1.getText().toString().getBytes());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    try {
                        ConfirmPin = CryptoService.encryptWithPublicKey(module, exponent,
                                editText2.getText().toString().getBytes());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    Log.e(editText1.getText().toString() + "--------" + editText2.getText().toString(), ConfirmPin + "======" + newPin);
                    new ChangePinAsyn().execute();
                }
            }
        });
    }

    String oldPin, newPin, ConfirmPin;

    WebServiceHttp webServiceHttp;
    String inqueryResponse;
    int msgCode;

    class ChangePinAsyn extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_CHANGEPIN);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, oldPin);
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_NEW_PIN, newPin);
            mapContainer.put(Constants.PARAMETER_CONFIRM_PIN, ConfirmPin);
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION);

            webServiceHttp = new WebServiceHttp(mapContainer, ChangePinActivity.this);

            inqueryResponse = webServiceHttp.getResponseSSLCertificatation();

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
            if (inqueryResponse != null) {
                Log.e("-------", "--------" + inqueryResponse);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseContainer = null;
                try {
                    responseContainer = obj.parse(inqueryResponse);
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
                if (msgCode == 2039) {

                    Intent intent = new Intent(ChangePinActivity.this, ChangePinConfirmationActivity.class);
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("oldpin", oldPin);
                    intent.putExtra("newPin", newPin);
                    intent.putExtra("ConfirmPin", ConfirmPin);
                    intent.putExtra("oldpinValue", editText.getText().toString());
                    intent.putExtra("newpinValue", editText1.getText().toString());
                    intent.putExtra("confirmpinValue", editText2.getText().toString());
                    intent.putExtra("mfaMode", responseContainer.getMfaMode());
                    startActivityForResult(intent, 10);

                } else if (msgCode == 631) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(ChangePinActivity.this, SessionTimeOutActivity.class);
                    startActivityForResult(intent, 40);
                }else if(msgCode==26){
                    sharedPreferences.edit().putString("password",newPin).commit();
                    Intent intent=new Intent(ChangePinActivity.this,ChangePinSuccessActivity.class);
                    startActivityForResult(intent,10);
                } else {
                    if (responseContainer.getMsg() == null) {
                        Utility.networkDisplayDialog(sharedPreferences.getString(
                                "ErrorMessage",
                                getResources().getString(
                                        R.string.bahasa_serverNotRespond)), ChangePinActivity.this);
                    } else {
                        Utility.networkDisplayDialog(responseContainer.getMsg(), ChangePinActivity.this);
                    }
                }

            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), ChangePinActivity.this);
            }
        }
    }


}
