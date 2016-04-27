package com.payment.simaspay.Cash_InOut;

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
import android.util.TypedValue;
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
import com.payment.simaspay.services.TimerCount;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SessionTimeOutActivity;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/29/2016.
 */
public class CashOutConfirmationActivity extends Activity {

    TextView title, heading, name, name_field, number, number_field, amount, amount_field;

    Button cancel, confirmation;

    LinearLayout back;

    boolean Timervalueout;
    String otpValue = "", sctl;
    SharedPreferences sharedPreferences;

    void Cancel(){
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
        Intent intent1 = getIntent();
        setResult(10, intent1);
        finish();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if(intent.getExtras().getString("value").equalsIgnoreCase("0")){
                    Cancel();
                }else if(intent.getExtras().getString("value").equalsIgnoreCase("1")) {
                    otpValue = intent.getExtras().getString("otpValue");
                    new CashOutAsynTask().execute();
                }else if(intent.getExtras().getString("value").equalsIgnoreCase("2")){
                    Utility.TransactionsdisplayDialog("Silakan masukkan kode OTP sebelum batas waktu yang ditentukan.",CashOutConfirmationActivity.this);
                }else if(intent.getExtras().getString("value").equalsIgnoreCase("3")){
                    Utility.TransactionsdisplayDialog(intent.getExtras().getString("otpValue"),CashOutConfirmationActivity.this);
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
            Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), CashOutConfirmationActivity.this);

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("com.send"));
    }

    ProgressDialog progressDialog;

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

        sharedPreferences= getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

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

        title.setTypeface(Utility.Robot_Regular(CashOutConfirmationActivity.this));
        heading.setTypeface(Utility.Robot_Regular(CashOutConfirmationActivity.this));
        name.setTypeface(Utility.Robot_Regular(CashOutConfirmationActivity.this));
        name_field.setTypeface(Utility.Robot_Light(CashOutConfirmationActivity.this));
        number.setTypeface(Utility.Robot_Regular(CashOutConfirmationActivity.this));
        number_field.setTypeface(Utility.Robot_Light(CashOutConfirmationActivity.this));
        amount.setTypeface(Utility.Robot_Regular(CashOutConfirmationActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(CashOutConfirmationActivity.this));
        cancel.setTypeface(Utility.Robot_Regular(CashOutConfirmationActivity.this));
        confirmation.setTypeface(Utility.Robot_Regular(CashOutConfirmationActivity.this));



        progressDialog = new ProgressDialog(CashOutConfirmationActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));



        name.setText("Nama Agen");
        number.setText("Nomor Handphone Agen");
        amount.setText("Jumlah");
        name_field.setText(getIntent().getExtras().getString("Name"));
        number_field.setText(getIntent().getExtras().getString("DestMDN"));
        amount_field.setText("Rp. " + getIntent().getExtras().getString("amount"));

        amount_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        number_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        name_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));

        handlerforTimer.postDelayed(runnableforExit, 90000);
        dialogCustomWish = new Dialog(context);
        cancel.setOnClickListener(new View.OnClickListener() {
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
                setResult(11, intent);
                finish();
            }
        });

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                    if (Timervalueout) {
                        Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), CashOutConfirmationActivity.this);
                    } else {
                        handlerforTimer.removeCallbacks(runnableforExit);
                        TimerCount timerCount=new TimerCount(CashOutConfirmationActivity.this,getIntent().getExtras().getString("sctlID"));
                        timerCount.SMSAlert("");
                    }
                }else{
                    if(Timervalueout){
                        Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), CashOutConfirmationActivity.this);
                    }else {
                        handlerforTimer.removeCallbacks(runnableforExit);
                        new CashOutAsynTask().execute();
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
                try {
                    handlerforTimer.removeCallbacks(runnableforExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = getIntent();
                setResult(11, intent);
                finish();
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
        try {
            handlerforTimer.removeCallbacks(runnableforExit);
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

    Context context;
    Dialog dialogCustomWish;

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
        textView_1.setText("Kode OTP dan link telah dikirimkan ke nomor "+getIntent().getExtras().getString("DestMDN")+". Masukkan kode tersebut atau akses link yang tersedia.");
        EditText editText = (EditText) dialogCustomWish.findViewById(R.id.otpCode);
        editText.setHint("6 digit kode OTP");

        editText.setText(otpValue);
        editText.setClickable(false);
        editText.setFocusable(false);
        textView_1.setTypeface(Utility.Robot_Regular(context));


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
                handler12.postDelayed(runnable12, 2000);
            }
        });
        dialogCustomWish.show();


    }

    Handler handler12 = new Handler();
    Runnable runnable12 = new Runnable() {
        @Override
        public void run() {
            handlerforTimer.removeCallbacks(runnableforExit);
            new CashOutAsynTask().execute();
        }
    };
    String OTP;
    int msgCode;

    class CashOutAsynTask extends AsyncTask<Void, Void, Void> {


        String response;

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_WALLET);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_CASHOUT);
            mapContainer.put(Constants.PARAMETER_BANK_ID, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_TRANSFER_ID, getIntent().getExtras().getString("transferID"));
            mapContainer.put(Constants.PARAMETER_PARENTTXN_ID, getIntent().getExtras().getString("ParentId"));
            mapContainer.put(Constants.PARAMETER_DEST_MDN, getIntent().getExtras().getString("DestMDN"));
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION_CONFIRM);
            mapContainer.put(Constants.PARAMETER_CONFIRMED, Constants.CONSTANT_VALUE_TRUE);
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
            mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE,Constants.POCKET_CODE_EMONEY);
            mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE,Constants.POCKET_CODE_BANK_SINARMAS);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, CashOutConfirmationActivity.this);

            Log.e("========","========="+mapContainer.toString());
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {

            if (!progressDialog.isShowing()) {
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
                    Intent intent = new Intent(CashOutConfirmationActivity.this, SessionTimeOutActivity.class);
                    startActivityForResult(intent, 40);
                } else if (msgCode == 298) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(CashOutConfirmationActivity.this, CashoutSuccessActivity.class);
                    intent.putExtra("amount", getIntent().getExtras().getString("amount"));
                    intent.putExtra("DestMDN", getIntent().getExtras().getString("DestMDN"));
                    intent.putExtra("transferID", responseContainer.getEncryptedTransferId());
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("Name", getIntent().getExtras().getString("Name"));
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
                                CashOutConfirmationActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), CashOutConfirmationActivity.this);
                    }
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), CashOutConfirmationActivity.this);
            }
        }
    }
}
