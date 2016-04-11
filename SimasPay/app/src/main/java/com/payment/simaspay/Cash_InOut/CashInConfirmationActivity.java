package com.payment.simaspay.Cash_InOut;

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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
public class CashInConfirmationActivity extends Activity {

    TextView title, heading, name, name_field, number, number_field, amount, amount_field;

    Button cancel, confirmation;


    LinearLayout back;

    boolean Timervalueout;
    String otpValue="", sctl;
    boolean nextpressedornot;
    SharedPreferences sharedPreferences;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String body = intent.getExtras().getString("message");
                if (body.contains("Kode Simobi Anda")
                        && body.contains(getIntent().getExtras().getString("sctlID"))) {

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
                    if(nextpressedornot){
                        new CashInAsynTask().execute();
                        handlerforTimer.removeCallbacks(runnableforExit);
                    }

                   

                } else if (body.contains("Your Simobi Code is ")
                        && body.contains(getIntent().getExtras().getString("sctlID"))) {
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
                    if(nextpressedornot){
                        new CashInAsynTask().execute();
                        handlerforTimer.removeCallbacks(runnableforExit);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Handler handler = new Handler();

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
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
            Timervalueout = true;
            Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), CashInConfirmationActivity.this);

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("com.msg.simaspay"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        cancel = (Button) findViewById(R.id.cancel);
        confirmation = (Button) findViewById(R.id.next);

        back = (LinearLayout) findViewById(R.id.back_layout);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        title.setTypeface(Utility.Robot_Regular(CashInConfirmationActivity.this));
        heading.setTypeface(Utility.Robot_Regular(CashInConfirmationActivity.this));
        name.setTypeface(Utility.Robot_Regular(CashInConfirmationActivity.this));
        name_field.setTypeface(Utility.Robot_Light(CashInConfirmationActivity.this));
        number.setTypeface(Utility.Robot_Regular(CashInConfirmationActivity.this));
        number_field.setTypeface(Utility.Robot_Light(CashInConfirmationActivity.this));
        amount.setTypeface(Utility.Robot_Regular(CashInConfirmationActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(CashInConfirmationActivity.this));
        cancel.setTypeface(Utility.Robot_Regular(CashInConfirmationActivity.this));
        confirmation.setTypeface(Utility.Robot_Regular(CashInConfirmationActivity.this));


        name_field.setText(getIntent().getExtras().getString("Name"));
        number_field.setText(getIntent().getExtras().getString("DestMDN"));
        amount_field.setText("Rp. "+getIntent().getExtras().getString("amount"));

        amount_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        number_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        name_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));


        progressDialog = new ProgressDialog(CashInConfirmationActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    unregisterReceiver(broadcastReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = getIntent();
                setResult(11, intent);
                finish();
            }
        });

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {

                    nextpressedornot = true;
                    if (Timervalueout) {
                        Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), CashInConfirmationActivity.this);
                    } else {
                        if (otpValue.equals("")) {
                            progressDialog.show();
                        } else {
                            handlerforTimer.removeCallbacks(runnableforExit);
                            new CashInAsynTask().execute();
                        }
                    }
                }else{
                    if (Timervalueout) {
                        Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), CashInConfirmationActivity.this);
                    } else {
                        handlerforTimer.removeCallbacks(runnableforExit);
                        new CashInAsynTask().execute();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    unregisterReceiver(broadcastReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = getIntent();
                setResult(11, intent);
                finish();
            }
        });

        handlerforTimer.postDelayed(runnableforExit, 90000);

    }

    ProgressDialog progressDialog;
    int msgCode;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
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

    String OTP;

    class CashInAsynTask extends AsyncTask<Void, Void, Void> {


        String response;

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_AGENT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_CASHIN);
            mapContainer.put(Constants.PARAMETER_BANK_ID, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_TRANSFER_ID, getIntent().getExtras().getString("transferID"));
            mapContainer.put(Constants.PARAMETER_PARENTTXN_ID,getIntent().getExtras().getString("ParentId"));
            mapContainer.put(Constants.PARAMETER_DEST_MDN, getIntent().getExtras().getString("DestMDN"));
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION,Constants.TRANSACTION_MFA_TRANSACTION_CONFIRM);
            mapContainer.put(Constants.PARAMETER_CONFIRMED,Constants.CONSTANT_VALUE_TRUE);
            if(getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                String module = sharedPreferences.getString("MODULE", "NONE");
                String exponent = sharedPreferences.getString("EXPONENT", "NONE");
                try {
                    OTP = CryptoService.encryptWithPublicKey(module, exponent,
                            otpValue.getBytes());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                mapContainer.put(Constants.PARAMETER_MFA_OTP, OTP);
            }else{
                mapContainer.put(Constants.PARAMETER_MFA_OTP, "");
            }
            mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE,Constants.POCKET_CODE_BANK_SINARMAS);
            mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE,Constants.POCKET_CODE_EMONEY);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, CashInConfirmationActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {

            if(!progressDialog.isShowing()) {
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
                try {
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 631) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(CashInConfirmationActivity.this, SessionTimeOutActivity.class);
                    startActivityForResult(intent, 40);
                } else if (msgCode == 296) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(CashInConfirmationActivity.this, CashInSuccessActivity.class);
                    intent.putExtra("amount",getIntent().getExtras().getString("amount"));
                    intent.putExtra("DestMDN",getIntent().getExtras().getString("DestMDN"));
                    intent.putExtra("transferID",responseContainer.getEncryptedTransferId());
                    intent.putExtra("sctlID",responseContainer.getSctl());
                    intent.putExtra("Name",getIntent().getExtras().getString("Name"));
                    startActivityForResult(intent, 10);
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
                                CashInConfirmationActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), CashInConfirmationActivity.this);
                    }
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), CashInConfirmationActivity.this);
            }
        }
    }
}
