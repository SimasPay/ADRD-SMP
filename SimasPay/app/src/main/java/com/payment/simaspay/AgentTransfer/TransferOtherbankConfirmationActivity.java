package com.payment.simaspay.AgentTransfer;

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
 * Created by Nagendra P on 2/3/2016.
 */
public class TransferOtherbankConfirmationActivity extends Activity {

    TextView title, heading, name, name_field, bank, bank_field, number, number_field, amount, amount_field, charge, charge_field, total, total_field;

    LinearLayout backlayout;

    Button confirm, Cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transferotherbankconfirmation);
        context = this;

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
        sharedPreferences=getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        backlayout = (LinearLayout) findViewById(R.id.back_layout);

        backlayout.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = getIntent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

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
        amount_field.setText("Rp. "+getIntent().getExtras().getString("amount"));
        charge_field.setText("Rp. "+getIntent().getExtras().getString("charges"));
        total_field.setText("Rp. "+getIntent().getExtras().getString("amount"));


        confirm = (Button) findViewById(R.id.next);
        Cancel = (Button) findViewById(R.id.cancel);

        confirm.setTypeface(Utility.Robot_Regular(TransferOtherbankConfirmationActivity.this));
        Cancel.setTypeface(Utility.Robot_Regular(TransferOtherbankConfirmationActivity.this));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                    nextpressedornot=true;
                    if (Timervalueout) {
                        Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), TransferOtherbankConfirmationActivity.this);
                    } else {
                        if ( otpValue.equals("")) {
                            progressDialog.show();
                        }else {
                            if (dialogCustomWish.isShowing()) {
                                new OtherBankLakuPandaiAsynTask().execute();
                            } else {
                                dialogCustomWish.show();
                            }
                        }
                    }
                } else {
                    if (Timervalueout) {
                        Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), TransferOtherbankConfirmationActivity.this);
                    }else{
                        handlerforTimer.removeCallbacks(runnableforExit);
                        new OtherBankLakuPandaiAsynTask().execute();
                    }
                }

            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = getIntent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        handlerforTimer.postDelayed(runnableforExit, 90000);
        dialogCustomWish = new Dialog(context);
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
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
            if(dialogCustomWish.isShowing()){
                dialogCustomWish.dismiss();
            }
            Timervalueout = true;
            Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), TransferOtherbankConfirmationActivity.this);

        }
    };
    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("com.msg.simaspay"));
    }


    boolean Timervalueout;

    boolean nextpressedornot;
    SharedPreferences sharedPreferences;

    String otpValue="",sctl;

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
                    SMSAlert(otpValue);
                    if(progressDialog!=null){
                        progressDialog.dismiss();
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
                    SMSAlert(otpValue);
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    
    Context context;
    Dialog dialogCustomWish;
    ProgressDialog progressDialog;

    public void SMSAlert(final String string) {


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
        textView_1.setText("Kode OTP dan link telah dikirimkan ke nomor "+sharedPreferences.getString("mobileNumber","")+". Masukkan kode tersebut atau akses link yang tersedia.");


        EditText editText=(EditText)dialogCustomWish.findViewById(R.id.otpCode);
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

                handler12.postDelayed(runnable12,1000);
            }
        });
        dialogCustomWish.show();


    }
    Handler handler12=new Handler();
    Runnable runnable12=new Runnable() {
        @Override
        public void run() {
            handlerforTimer.removeCallbacks(runnableforExit);
            new OtherBankLakuPandaiAsynTask().execute();
        }
    };
    String OTP;
    int msgCode;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    class OtherBankLakuPandaiAsynTask extends AsyncTask<Void, Void, Void> {


        String response;

        @Override
        protected Void doInBackground(Void... params) {




            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");
            try {
                OTP = CryptoService.encryptWithPublicKey(module, exponent,
                        otpValue.getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_INTERBANK_TRANSFER);
            mapContainer.put(Constants.PARAMETER_BANK_ID, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_TRANSFER_ID, getIntent().getExtras().getString("transferID"));
            mapContainer.put(Constants.PARAMETER_PARENTTXN_ID,getIntent().getExtras().getString("ParentId"));
            mapContainer.put(Constants.PARAMETER_DEST_ACCOUNT_NO, getIntent().getExtras().getString("DestMDN"));
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION,Constants.TRANSACTION_MFA_TRANSACTION_CONFIRM);
            mapContainer.put(Constants.PARAMETER_CONFIRMED,Constants.CONSTANT_VALUE_TRUE);
            if(sharedPreferences.getInt("userType",-1)==0){
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, getIntent().getExtras().getString("BankCode"));
            }else if(sharedPreferences.getInt("userType",-1)==1){
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
                mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE,Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, getIntent().getExtras().getString("BankCode"));
            }else if(sharedPreferences.getInt("userType",-1)==2) {
                if(sharedPreferences.getInt("AgentUsing",-1)==1){
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
            }
            if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
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
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 631) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(TransferOtherbankConfirmationActivity.this, SessionTimeOutActivity.class);
                    startActivityForResult(intent, 40);
                } else if (msgCode == 293 || msgCode==81) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(TransferOtherbankConfirmationActivity.this, TransferOtherBankSuccessActivity.class);
                    intent.putExtra("amount",getIntent().getExtras().getString("amount"));
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
                        Utility.networkDisplayDialog(
                                sharedPreferences.getString(
                                        "ErrorMessage",
                                        getResources()
                                                .getString(
                                                        R.string.server_error_message)),
                                TransferOtherbankConfirmationActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), TransferOtherbankConfirmationActivity.this);
                    }
                }
            } else {
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
}
