package com.payment.simaspay.PaymentPerchaseAccount;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
 * Created by Nagendra P on 1/28/2016.
 */
public class PaymentConfirmationActivity extends Activity {

    TextView title, heading, name, name_field, number, number_field, amount, amount_field, charges, charges_field, total, total_field;

    Button cancel, confirmation;

    LinearLayout back;

    View line;


    boolean Timervalueout;

    SharedPreferences sharedPreferences;

    String otpValue = "", sctl;

    void Cancel() {
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
                if (intent.getExtras().getString("value").equalsIgnoreCase("0")) {
                    Cancel();
                } else if (intent.getExtras().getString("value").equalsIgnoreCase("1")) {
                    otpValue = intent.getExtras().getString("otpValue");
                    new BillPayConfirmationAsynTask().execute();
                } else if (intent.getExtras().getString("value").equalsIgnoreCase("2")) {
                    Utility.TransactionsdisplayDialog("Silakan masukkan kode OTP sebelum batas waktu yang ditentukan.", PaymentConfirmationActivity.this);
                } else if (intent.getExtras().getString("value").equalsIgnoreCase("3")) {
                    Utility.TransactionsdisplayDialog(intent.getExtras().getString("otpValue"), PaymentConfirmationActivity.this);
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
            if (dialogCustomWish.isShowing()) {
                dialogCustomWish.dismiss();
            }
            Timervalueout = true;

//            Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), PaymentConfirmationActivity.this);

        }
    };


    ProgressDialog progressDialog;
    int msgCode;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("com.send"));
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

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        title = (TextView) findViewById(R.id.title);
        heading = (TextView) findViewById(R.id.textview);
        name = (TextView) findViewById(R.id.name);
        name_field = (TextView) findViewById(R.id.name_field);
        number = (TextView) findViewById(R.id.number);
        number_field = (TextView) findViewById(R.id.number_field);
        amount = (TextView) findViewById(R.id.amount);
        amount_field = (TextView) findViewById(R.id.amount_field);
        charges = (TextView) findViewById(R.id.products);
        charges_field = (TextView) findViewById(R.id.other_products);
        total = (TextView) findViewById(R.id.total);
        total_field = (TextView) findViewById(R.id.total_field);

        line = (View) findViewById(R.id.line);

        cancel = (Button) findViewById(R.id.cancel);
        confirmation = (Button) findViewById(R.id.next);

        back = (LinearLayout) findViewById(R.id.back_layout);

        title.setTypeface(Utility.Robot_Regular(PaymentConfirmationActivity.this));
        heading.setTypeface(Utility.Robot_Regular(PaymentConfirmationActivity.this));
        name.setTypeface(Utility.Robot_Regular(PaymentConfirmationActivity.this));
        name_field.setTypeface(Utility.Robot_Light(PaymentConfirmationActivity.this));
        number.setTypeface(Utility.Robot_Regular(PaymentConfirmationActivity.this));
        number_field.setTypeface(Utility.Robot_Light(PaymentConfirmationActivity.this));
        amount.setTypeface(Utility.Robot_Regular(PaymentConfirmationActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(PaymentConfirmationActivity.this));
        charges.setTypeface(Utility.Robot_Regular(PaymentConfirmationActivity.this));
        charges_field.setTypeface(Utility.Robot_Light(PaymentConfirmationActivity.this));
        total.setTypeface(Utility.Robot_Regular(PaymentConfirmationActivity.this));
        total_field.setTypeface(Utility.Robot_Light(PaymentConfirmationActivity.this));
        cancel.setTypeface(Utility.Robot_Regular(PaymentConfirmationActivity.this));
        confirmation.setTypeface(Utility.Robot_Regular(PaymentConfirmationActivity.this));


        charges.setVisibility(View.VISIBLE);
        charges_field.setVisibility(View.VISIBLE);
        total.setVisibility(View.VISIBLE);
        total_field.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);


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

        name.setText("Nama Produk");
        name_field.setText(getIntent().getExtras().getString("billerDetails"));
        try {

            if(getIntent().getExtras().getString("numberTitle").equalsIgnoreCase("")){
                number.setText("Nomor Handphone");
            }else{
                number.setText(getIntent().getExtras().getString("numberTitle"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            number.setText("Nomor Handphone");
        }
        number_field.setText(getIntent().getExtras().getString("invoiceNo"));
        amount.setText("Jumlah");
        amount_field.setText("Rp. " + getIntent().getExtras().getString("originalAmount"));
        charges.setText("Biaya Administrasi");
        charges_field.setText("Rp. "+getIntent().getExtras().getString("charges"));
        total.setText("Total Pendebitan");
        total_field.setText("Rp. "+getIntent().getExtras().getString("creditamt"));

        amount_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        number_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        name_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        charges_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        total_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));

        progressDialog = new ProgressDialog(PaymentConfirmationActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));

        dialogCustomWish = new Dialog(context);
        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {

//                    if (Timervalueout) {
//                        Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), PaymentConfirmationActivity.this);
//                    } else {

                        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                        if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP) {
                            if ((checkCallingOrSelfPermission(android.Manifest.permission.READ_SMS)
                                    != PackageManager.PERMISSION_GRANTED) && checkCallingOrSelfPermission(Manifest.permission.RECEIVE_SMS)
                                    != PackageManager.PERMISSION_GRANTED) {

                                requestPermissions(new String[]{Manifest.permission.READ_SMS, android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.SEND_SMS},
                                        109);
                            } else {
                                handlerforTimer.removeCallbacks(runnableforExit);
                                TimerCount timerCount = new TimerCount(PaymentConfirmationActivity.this, getIntent().getExtras().getString("sctlID"));
                                timerCount.SMSAlert("");
                            }
                        } else {
                            handlerforTimer.removeCallbacks(runnableforExit);
                            TimerCount timerCount = new TimerCount(PaymentConfirmationActivity.this, getIntent().getExtras().getString("sctlID"));
                            timerCount.SMSAlert("");
                        }
//                    }
                } else {
//                    if (Timervalueout) {
//                        Utility.displayDialog(getResources().getString(R.string.SMS_notreceived_message), PaymentConfirmationActivity.this);
//                    } else {
                        handlerforTimer.removeCallbacks(runnableforExit);
                        new BillPayConfirmationAsynTask().execute();

//                    }
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

        handlerforTimer.postDelayed(runnableforExit, 90000);

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

        textView_1.setText("Kode OTP dan link telah dikirimkan ke nomor " + sharedPreferences.getString("mobileNumber", "") + ". Masukkan kode tersebut atau akses link yang tersedia.");
        button.setTypeface(Utility.RegularTextFormat(context));
        button1.setTypeface(Utility.RegularTextFormat(context));
        textView.setTypeface(Utility.RegularTextFormat(context));
        textView_1.setTypeface(Utility.Robot_Regular(context));

        EditText editText = (EditText) dialogCustomWish.findViewById(R.id.otpCode);
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
                handler12.postDelayed(runnable12, 1000);
            }
        });
        dialogCustomWish.show();


    }

    Handler handler12 = new Handler();
    Runnable runnable12 = new Runnable() {
        @Override
        public void run() {
            handlerforTimer.removeCallbacks(runnableforExit);
            new BillPayConfirmationAsynTask().execute();
        }
    };
    String OTP;


    class BillPayConfirmationAsynTask extends AsyncTask<Void, Void, Void> {


        String response;

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_BILLPAYMENT);
            mapContainer.put(Constants.PARAMETER_BANK_ID, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_TRANSFER_ID, getIntent().getExtras().getString("transferID"));
            mapContainer.put(Constants.PARAMETER_PARENTTXN_ID, getIntent().getExtras().getString("parentTxnID"));
            mapContainer.put(Constants.PARAMETER_BILL_NO, getIntent().getExtras().getString("invoiceNo"));
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION_CONFIRM);
            mapContainer.put(Constants.PARAMETER_PAYMENT_MODE, getIntent().getExtras().getString("PaymentMode"));
            mapContainer.put(Constants.PARAMETER_BILLER_CODE, getIntent().getExtras().getString("ProductCode"));
            mapContainer.put(Constants.PARAMETER_CONFIRMED, Constants.CONSTANT_VALUE_TRUE);
            if (sharedPreferences.getInt("userType", -1) == 0) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BILLPAYMENT);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
            } else if (sharedPreferences.getInt("userType", -1) == 1) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BILLPAYMENT);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
            } else if (sharedPreferences.getInt("userType", -1) == 2) {
                if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_AGENT);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                } else {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_AGENT);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                }
            }
            if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                String module = sharedPreferences.getString("MODULE", "NONE");
                String exponent = sharedPreferences.getString("EXPONENT", "NONE");
                try {
                    OTP = CryptoService.encryptWithPublicKey(module, exponent,
                            otpValue.getBytes());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                mapContainer.put(Constants.PARAMETER_MFA_OTP, OTP);
            } else {
                mapContainer.put(Constants.PARAMETER_MFA_OTP, "");
            }
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, PaymentConfirmationActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {

            if (!progressDialog.isShowing()) {
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
                    Intent intent = new Intent(PaymentConfirmationActivity.this, SessionTimeOutActivity.class);
                    startActivityForResult(intent, 40);
                } else if (msgCode == 653) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(PaymentConfirmationActivity.this, PaymentSuccessActivity.class);
                    if (responseContainer.getInvoiceNo() != null) {
                        intent.putExtra("invoiceNo", responseContainer.getInvoiceNo());
                    } else {
                        intent.putExtra("invoiceNo", getIntent().getExtras().getString("invoiceNo"));
                    }
                    intent.putExtra("billerDetails", getIntent().getExtras().getString("billerDetails"));
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("originalAmount",responseContainer.getAmount());
                    intent.putExtra("totalAmount",responseContainer.getEncryptedDebitAmount());
                    intent.putExtra("charges", responseContainer.getEncryptedTransactionCharges());
                    intent.putExtra("numberTitle",getIntent().getExtras().getString("numberTitle"));
                    startActivityForResult(intent, 10);
                } else if (msgCode == 703) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(PaymentConfirmationActivity.this, PaymentSuccessActivity.class);
                    intent.putExtra("originalAmount", responseContainer.getAmount());
                    intent.putExtra("DestMDN", getIntent().getExtras().getString("DestMDN"));
                    intent.putExtra("transferID", responseContainer.getEncryptedTransferId());
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("charges",responseContainer.getEncryptedTransactionCharges());
                    intent.putExtra("totalAmount",responseContainer.getEncryptedDebitAmount());
                    intent.putExtra("Name", getIntent().getExtras().getString("Name"));
                    intent.putExtra("numberTitle",getIntent().getExtras().getString("numberTitle"));
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
                                PaymentConfirmationActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), PaymentConfirmationActivity.this);
                    }
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), PaymentConfirmationActivity.this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 109) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handlerforTimer.removeCallbacks(runnableforExit);
                TimerCount timerCount = new TimerCount(PaymentConfirmationActivity.this, getIntent().getExtras().getString("sctlID"));
                timerCount.SMSAlert("");
            } else {

            }
        }
    }

}
