package com.payment.simaspay.AgentTransfer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.receivers.IncomingSMS;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.utils.Functions;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;
import simaspay.payment.com.simaspay.UserHomeActivity;

public class TransferOtherbankConfirmationActivity extends AppCompatActivity implements IncomingSMS.AutoReadSMSListener{

    TextView title, heading, name, name_field, bank, bank_field, number, number_field, amount, amount_field, charge, charge_field, total, total_field;
    LinearLayout backlayout;
    Button confirm, Cancel;
    LinearLayout otplay, otp2lay;
    private static final String LOG_TAG = "SimasPay";
    private EditText edt;
    SharedPreferences languageSettings;
    String selectedLanguage;
    String sourceMDN, stMPIN, stFullname, stAmount, stMDN, stTransferID, stParentTxnID, stSctl, message, transactionTime, responseCode;
    Context context;
    ProgressDialog progressDialog;
    String OTP;
    int msgCode;
    boolean nextpressedornot;
    SharedPreferences sharedPreferences;
    String otpValue="";
    private static AlertDialog dialogBuilder;
    Functions func;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transferotherbankconfirmation);
        context = this;

        IncomingSMS.setListener(TransferOtherbankConfirmationActivity.this);

        func = new Functions(this);
        func.initiatedToolbar(this);

        sharedPreferences=getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");

        sourceMDN=sharedPreferences.getString("mobileNumber", "");
        stMPIN = getIntent().getExtras().getString("mpin");
        stSctl = getIntent().getExtras().getString("sctlID");
        stTransferID = getIntent().getExtras().getString("transferID");
        stParentTxnID = getIntent().getExtras().getString("ParentId");
        stAmount = getIntent().getExtras().getString("amount");
        stFullname = getIntent().getExtras().getString("destname");
        stMDN = getIntent().getExtras().getString("DestMDN");

        title = (TextView) findViewById(R.id.title);
        heading = (TextView) findViewById(R.id.textview);
        name = (TextView) findViewById(R.id.name);
        name_field = (TextView) findViewById(R.id.name_field);
        bank = (TextView) findViewById(R.id.bank);
        bank_field = (TextView) findViewById(R.id.bank_field);
        number = (TextView) findViewById(R.id.number);
        number_field = (TextView) findViewById(R.id.number_field);
        amount = (TextView) findViewById(R.id.amount);
        amount_field = (TextView) findViewById(R.id.amount_field);
        charge = (TextView) findViewById(R.id.charge);
        charge_field = (TextView) findViewById(R.id.charge_field);
        total = (TextView) findViewById(R.id.total);
        total_field = (TextView) findViewById(R.id.total_field);
        backlayout = (LinearLayout) findViewById(R.id.back_layout);

        backlayout.setOnClickListener(view -> finish());

        progressDialog = new ProgressDialog(TransferOtherbankConfirmationActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.waitingSms));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));

        title.setTypeface(Utility.Robot_Regular(TransferOtherbankConfirmationActivity.this));
        heading.setTypeface(Utility.Robot_Regular(TransferOtherbankConfirmationActivity.this));
        name.setTypeface(Utility.Robot_Regular(TransferOtherbankConfirmationActivity.this));
        name_field.setTypeface(Utility.Robot_Light(TransferOtherbankConfirmationActivity.this));
        bank.setTypeface(Utility.Robot_Regular(TransferOtherbankConfirmationActivity.this));
        bank_field.setTypeface(Utility.Robot_Light(TransferOtherbankConfirmationActivity.this));
        number.setTypeface(Utility.Robot_Regular(TransferOtherbankConfirmationActivity.this));
        number_field.setTypeface(Utility.Robot_Light(TransferOtherbankConfirmationActivity.this));
        amount.setTypeface(Utility.Robot_Regular(TransferOtherbankConfirmationActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(TransferOtherbankConfirmationActivity.this));
        charge.setTypeface(Utility.Robot_Regular(TransferOtherbankConfirmationActivity.this));
        charge_field.setTypeface(Utility.Robot_Light(TransferOtherbankConfirmationActivity.this));
        total.setTypeface(Utility.Robot_Regular(TransferOtherbankConfirmationActivity.this));
        total_field.setTypeface(Utility.Robot_Light(TransferOtherbankConfirmationActivity.this));

        name_field.setText(getIntent().getExtras().getString("Name"));
        bank_field.setText(getIntent().getExtras().getString("BankName"));
        number_field.setText(getIntent().getExtras().getString("DestMDN"));
        amount_field.setText("Rp. "+getIntent().getExtras().getString("originalamount"));
        charge_field.setText("Rp. "+getIntent().getExtras().getString("charges"));
        total_field.setText("Rp. "+getIntent().getExtras().getString("amount"));


        confirm = (Button) findViewById(R.id.next);
        Cancel = (Button) findViewById(R.id.cancel);

        confirm.setTypeface(Utility.Robot_Regular(TransferOtherbankConfirmationActivity.this));
        Cancel.setTypeface(Utility.Robot_Regular(TransferOtherbankConfirmationActivity.this));

        confirm.setOnClickListener(view -> {

            if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                nextpressedornot=true;
                    int currentapiVersion = Build.VERSION.SDK_INT;
                    if (currentapiVersion > Build.VERSION_CODES.LOLLIPOP) {
                        if ((checkCallingOrSelfPermission(Manifest.permission.READ_SMS)
                                != PackageManager.PERMISSION_GRANTED) && checkCallingOrSelfPermission(Manifest.permission.RECEIVE_SMS)
                                != PackageManager.PERMISSION_GRANTED) {

                            requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS},
                                    109);
                        } else {
                            new requestOTPAsyncTask().execute();
                        }
                    } else {
                        new requestOTPAsyncTask().execute();
                    }
            } else {
                new OtherBankConfirmAsyncTask().execute();
            }

        });

        Cancel.setOnClickListener(view -> finish());
    }

    private class OtherBankConfirmAsyncTask extends AsyncTask<Void, Void, Void> {
        String response;

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_INTERBANK_TRANSFER);
            mapContainer.put(Constants.PARAMETER_BANK_ID, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString(Constants.PARAMETER_PHONENUMBER, ""));
            mapContainer.put(Constants.PARAMETER_TRANSFER_ID, getIntent().getExtras().getString("transferID"));
            mapContainer.put(Constants.PARAMETER_PARENTTXN_ID,getIntent().getExtras().getString("ParentId"));
            mapContainer.put(Constants.PARAMETER_DEST_ACCOUNT_NO, getIntent().getExtras().getString("DestMDN"));
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION,Constants.TRANSACTION_MFA_TRANSACTION_CONFIRM);
            mapContainer.put(Constants.PARAMETER_CONFIRMED,Constants.CONSTANT_VALUE_TRUE);
            if(sharedPreferences.getInt(Constants.PARAMETER_USERTYPE,-1)==Constants.CONSTANT_BANK_INT){
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, getIntent().getExtras().getString("BankCode"));
            }else if(sharedPreferences.getInt(Constants.PARAMETER_USERTYPE,-1)==Constants.CONSTANT_BANKSINARMAS_INT){
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
                mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE,Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, getIntent().getExtras().getString("BankCode"));
            }else if(sharedPreferences.getInt(Constants.PARAMETER_USERTYPE,-1)==Constants.CONSTANT_LAKUPANDAI_INT) {
                if(sharedPreferences.getInt(Constants.PARAMETER_AGENTTYPE,-1)==Constants.CONSTANT_EMONEY_INT){
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                    mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE,Constants.POCKET_CODE_BANK);
                    mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, getIntent().getExtras().getString("BankCode"));
                }else{
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, getIntent().getExtras().getString("BankCode"));
                }
            }else if(sharedPreferences.getInt(Constants.PARAMETER_USERTYPE,-1)==Constants.CONSTANT_EMONEY_INT) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE,Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, getIntent().getExtras().getString("BankCode"));
            }
            if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                OTP = func.generateRSA(otpValue);
                mapContainer.put(Constants.PARAMETER_MFA_OTP, OTP);
            } else {
                mapContainer.put(Constants.PARAMETER_MFA_OTP, "");
            }
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, TransferOtherbankConfirmationActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {

            if(!progressDialog.isShowing()) {
                progressDialog.show();
                progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
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
                    msgCode = Integer.parseInt(responseContainer.getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 631) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    func.errorTimeoutResponseConfirmation(responseContainer.getMsg());
                } else if (msgCode == 293 || msgCode==81) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(TransferOtherbankConfirmationActivity.this, TransferOtherBankSuccessActivity.class);
                    intent.putExtra("amount",getIntent().getExtras().getString("amount"));
                    intent.putExtra("originalamount",getIntent().getExtras().getString("originalamount"));
                    intent.putExtra("DestMDN",getIntent().getExtras().getString("DestMDN"));
                    intent.putExtra("transferID",responseContainer.getEncryptedTransferId());
                    intent.putExtra("sctlID",responseContainer.getSctl());
                    intent.putExtra("Name",getIntent().getExtras().getString("Name"));
                    intent.putExtra("BankName",getIntent().getExtras().getString("BankName"));
                    intent.putExtra("BankCode",getIntent().getExtras().getString("BankCode"));
                    startActivityForResult(intent, 10);
                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (responseContainer.getMsg() == null) {
                        func.errorNullResponseConfirmation();
                    } else {
                        func.errorElseResponseConfirmation(responseContainer.getMsg());
                    }
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                func.errorNullResponseConfirmation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 109) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(LOG_TAG, "permission granted");
            }
        }
    }

    class requestOTPAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("txnName", "ResendMFAOTP");
            mapContainer.put("service", "Wallet");
            mapContainer.put("institutionID", Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put("authenticationKey", "");
            mapContainer.put("sourceMDN", sourceMDN);
            mapContainer.put("sourcePIN", stMPIN);
            mapContainer.put("sctlId", stSctl);
            mapContainer.put("channelID", "7");

            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    TransferOtherbankConfirmationActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(TransferOtherbankConfirmationActivity.this);
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
                        AlertDialog.Builder alertbox;
                        if (msgCode == 631) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            alertbox = new AlertDialog.Builder(TransferOtherbankConfirmationActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setCancelable(false);
                            alertbox.setNeutralButton("OK", (arg0, arg1) -> {
                                Intent intent = new Intent(TransferOtherbankConfirmationActivity.this, SecondLoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            });
                            alertbox.show();
                        }else if(responseDataContainer.getMsgCode().equals("2171")){
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
                            alertbox = new AlertDialog.Builder(TransferOtherbankConfirmationActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", (arg0, arg1) -> {
                                Intent intent = new Intent(TransferOtherbankConfirmationActivity.this, UserHomeActivity.class);
                                startActivity(intent);
                                TransferOtherbankConfirmationActivity.this.finish();
                            });
                            alertbox.show();
                        }
                    }
                }catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }else{
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), TransferOtherbankConfirmationActivity.this);
            }
        }
    }

    private void showOTPRequiredDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullParent = null;
        View dialoglayout = inflater.inflate(R.layout.new_otp_dialog, nullParent, false);
        dialogBuilder = new AlertDialog.Builder(TransferOtherbankConfirmationActivity.this, R.style.MyAlertDialogStyle).create();
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
        manualotp.setOnClickListener(arg0 -> {
            otplay.setVisibility(View.GONE);
            otp2lay.setVisibility(View.VISIBLE);
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
                dialogBuilder.dismiss();
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
                new OtherBankConfirmAsyncTask().execute();
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
                    Log.d(LOG_TAG, "otp dialog : " + edt.getText());
                    Log.d(LOG_TAG, "otp dialog length: " + edt.getText().length());
                    otpValue=edt.getText().toString();
                    myTimer.cancel();
                    if(otpValue==null||otpValue.equals("")){
                        otpValue=edt.getText().toString();
                    }
                    new OtherBankConfirmAsyncTask().execute();

                }

            }
        });
        dialogBuilder.show();
    }

    @Override
    public void onReadSMS(String otp) {
        Log.d(LOG_TAG, "otp from SMS: " + otp);
        edt.setText(otp);
        otpValue=otp;
    }
}
