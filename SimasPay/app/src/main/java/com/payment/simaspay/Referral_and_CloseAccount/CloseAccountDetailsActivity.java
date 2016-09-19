package com.payment.simaspay.Referral_and_CloseAccount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class CloseAccountDetailsActivity extends Activity {

    TextView title, handphone, jumlah, mPin;

    Button submit;

    EditText number, amount, pin;

    SharedPreferences sharedPreferences;
    LinearLayout btnBacke;

    String DestMDN;

    String otpValue, idnumber, sctl, Name;


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setortunaidetails);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        title = (TextView) findViewById(R.id.titled);
        handphone = (TextView) findViewById(R.id.handphone);
        jumlah = (TextView) findViewById(R.id.jumlah);
        mPin = (TextView) findViewById(R.id.mPin);

        submit = (Button) findViewById(R.id.submit);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        title.setText("Tutup Rekening");
        handphone.setText("Nomor Rekening");


        number = (EditText) findViewById(R.id.number);
        amount = (EditText) findViewById(R.id.amount);
        pin = (EditText) findViewById(R.id.pin);

        jumlah.setVisibility(View.GONE);
        mPin.setVisibility(View.GONE);
        amount.setVisibility(View.GONE);
        pin.setVisibility(View.GONE);
        findViewById(R.id.setortunailayout).setVisibility(View.GONE);

        btnBacke = (LinearLayout) findViewById(R.id.back_layout);

        title.setTypeface(Utility.Robot_Regular(CloseAccountDetailsActivity.this));
        handphone.setTypeface(Utility.HelveticaNeue_Medium(CloseAccountDetailsActivity.this));
        jumlah.setTypeface(Utility.HelveticaNeue_Medium(CloseAccountDetailsActivity.this));
        mPin.setTypeface(Utility.HelveticaNeue_Medium(CloseAccountDetailsActivity.this));
        submit.setTypeface(Utility.Robot_Regular(CloseAccountDetailsActivity.this));
        number.setTypeface(Utility.Robot_Light(CloseAccountDetailsActivity.this));
        amount.setTypeface(Utility.Robot_Light(CloseAccountDetailsActivity.this));
        pin.setTypeface(Utility.Robot_Light(CloseAccountDetailsActivity.this));

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(14);
        number.setFilters(FilterArray);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number.getText().toString().replace(" ", "").length() <= 0) {
                    Utility.displayDialog("Masukkan Nomor Rekening",CloseAccountDetailsActivity.this);
                } else if (number.getText().toString().replace(" ", "").length() < 7) {
                    Utility.displayDialog(getResources().getString(R.string.number_less7),CloseAccountDetailsActivity.this);
                } else if (number.getText().toString().replace(" ", "").length() > 14) {
                    Utility.displayDialog(getResources().getString(R.string.number_grater14),CloseAccountDetailsActivity.this);
                } else {
                   /* if(number.getText().toString().replace(" ", "").startsWith("0")){
                        DestMDN="62"+number.getText().toString().replace(" ", "").substring(1);
                    }else if(number.getText().toString().replace(" ", "").startsWith("62")){
                        DestMDN=number.getText().toString().replace(" ", "");
                    }else {
                        DestMDN="62"+number.getText().toString().replace(" ", "");
                    }*/

                    DestMDN=number.getText().toString().replace(" ","");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(number.getWindowToken(), 0);
                    new CloseAsynTask().execute();
                }
            }
        });

        btnBacke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    int msgCode;
    ProgressDialog progressDialog;


    class CloseAsynTask extends AsyncTask<Void, Void, Void> {


        String response;

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_CLOSEACCOUNTINQUERY);
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_KEY, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, sharedPreferences.getString("password", ""));
            mapContainer.put(Constants.PARAMETER_DEST_MDN, DestMDN);

            Log.e("======","---------------"+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, CloseAccountDetailsActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CloseAccountDetailsActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
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
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 2128) {
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    idnumber = responseContainer.getSctl();
                    Name = responseContainer.getName();
                    Intent intent1 = new Intent(CloseAccountDetailsActivity.this, CloseAccountConfirmationActivity.class);
                    intent1.putExtra("SctlID", responseContainer.getSctl());
                    intent1.putExtra("Name", Name);
                    intent1.putExtra("DestMDN", DestMDN);
                    intent1.putExtra("minBal", true);
                    intent1.putExtra("mfaMode",responseContainer.getMfaMode());
                    startActivityForResult(intent1, 10);
                } else if (msgCode == 631) {
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(CloseAccountDetailsActivity.this, SessionTimeOutActivity.class);
                    startActivityForResult(intent, 40);
                } else if (msgCode == 2132) {
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(CloseAccountDetailsActivity.this, CloseAccountConfirmationActivity.class);
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("Name", responseContainer.getName());
                    intent.putExtra("DestMDN", DestMDN);
                    intent.putExtra("minBal", false);
                    intent.putExtra("msg", responseContainer.getMsg());
                    startActivityForResult(intent, 10);
                } else {
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    if (responseContainer.getMsg() == null) {
                        Utility.networkDisplayDialog(
                                sharedPreferences.getString(
                                        "ErrorMessage",
                                        getResources()
                                                .getString(
                                                        R.string.server_error_message)),
                                CloseAccountDetailsActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), CloseAccountDetailsActivity.this);
                    }
                }
            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), CloseAccountDetailsActivity.this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 10) {
                finish();
            }
        } else if (requestCode == 20) {
            if (resultCode == RESULT_OK) {

            }
        }
    }
}
