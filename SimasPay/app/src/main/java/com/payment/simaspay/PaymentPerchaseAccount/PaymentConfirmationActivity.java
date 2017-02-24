package com.payment.simaspay.PaymentPerchaseAccount;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.userdetails.SessionTimeOutActivity;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/28/2016.
 */
public class PaymentConfirmationActivity extends AppCompatActivity implements IncomingSMS.AutoReadSMSListener{

    TextView title, heading, name, name_field, number, number_field, amount, amount_field, charges, charges_field, total, total_field;
    Button cancel, confirmation;
    LinearLayout back;
    View line;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    int msgCode;
    Dialog dialogCustomWish;
    String OTP;
    private static final String LOG_TAG = "SimasPay";
    private EditText edt;
    private static AlertDialog dialogBuilder;
    static boolean isExitActivity = false;
    LinearLayout otplay, otp2lay;
    Context context;
    SharedPreferences settings, settings2, languageSettings;
    String pin, otpValue;
    String selectedLanguage;
    String sourceMDN, message, transactionTime, responseCode, stMPIN, stSctl, stTransferID, stParentTxnID, stAmount, stCharges, stName;

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

        IncomingSMS.setListener(PaymentConfirmationActivity.this);

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

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");

        sourceMDN = sharedPreferences.getString("mobileNumber","");
        String module = sharedPreferences.getString("MODULE", "NONE");
        String exponent = sharedPreferences.getString("EXPONENT", "NONE");
        String pinValue="";
        try {
            pinValue  = CryptoService.encryptWithPublicKey(module, exponent,
                    sharedPreferences.getString("mpin", "").getBytes());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        stMPIN = pinValue;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            stSctl = getIntent().getExtras().getString("sctlID");
            stTransferID = getIntent().getExtras().getString("transferID");
            stParentTxnID = getIntent().getExtras().getString("parentTxnID");
            stAmount = getIntent().getExtras().getString("originalAmount");
            stCharges = getIntent().getExtras().getString("charges");
            stName = getIntent().getExtras().getString("Name");
        }

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
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                if (getIntent().getExtras().getString("mfaMode").equalsIgnoreCase("OTP")) {
                        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                        if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP) {
                            if ((checkCallingOrSelfPermission(android.Manifest.permission.READ_SMS)
                                    != PackageManager.PERMISSION_GRANTED) && checkCallingOrSelfPermission(Manifest.permission.RECEIVE_SMS)
                                    != PackageManager.PERMISSION_GRANTED) {

                                requestPermissions(new String[]{Manifest.permission.READ_SMS, android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.SEND_SMS},
                                        109);
                            } else {
                                new requestOTPAsyncTask().execute();
                            }
                        } else {
                            new requestOTPAsyncTask().execute();
                        }
                } else {
                    new BillPayConfirmationAsynTask().execute();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            }
        }
    }


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
            String account = sharedPreferences.getString("useas","");
            if(account.equals("Bank")) {
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
            }else{
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BILLPAYMENT);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
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
                if (msgCode==631) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(PaymentConfirmationActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(responseContainer.getMsg());
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(PaymentConfirmationActivity.this, SecondLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    alertbox.show();
                    dialogBuilder.dismiss();
                }else if (msgCode == 653) {
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
                        dialogBuilder.dismiss();
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), PaymentConfirmationActivity.this);
                        dialogBuilder.dismiss();
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
                dialogBuilder.dismiss();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 109) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //
            }
        }
    }

    @Override
    public void onReadSMS(String otp) {
        Log.d(LOG_TAG, "otp from SMS: " + otp);
        edt.setText(otp);
        otpValue=otp;
    }

    private void showOTPRequiredDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullParent = null;
        View dialoglayout = inflater.inflate(R.layout.new_otp_dialog, nullParent, false);
        dialogBuilder = new AlertDialog.Builder(PaymentConfirmationActivity.this, R.style.MyAlertDialogStyle).create();
        dialogBuilder.setCanceledOnTouchOutside(false);
        dialogBuilder.setTitle("");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(dialoglayout);

        // EditText OTP
        otplay = (LinearLayout) dialoglayout.findViewById(R.id.halaman1);
        otp2lay = (LinearLayout) dialoglayout.findViewById(R.id.halaman2);
        otp2lay.setVisibility(View.GONE);
        TextView manualotp = (TextView) dialoglayout.findViewById(R.id.manualsms_lbl);
        //TextView waitingsms = (TextView) dialoglayout.findViewById(R.id.waitingsms_lbl);
        Button cancel_otp = (Button) dialoglayout.findViewById(R.id.cancel_otp);
        //waitingsms.setText("Menunggu SMS Kode Verifikasi di Nomor " + Html.fromHtml("<b>"+sourceMDN+"</b>") + "\n");
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
                errorOTP();
                timer.setText("00:00");
            }
        };
        myTimer.start();
        cancel_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                settings2 = getSharedPreferences(LOG_TAG, 0);
                settings2.edit().putString("ActivityName", "ExitConfirmationScreen").apply();
                if (myTimer != null) {
                    myTimer.cancel();
                }
            }
        });
        final Button ok_otp = (Button) dialoglayout.findViewById(R.id.ok_otp);
        ok_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt.getText().toString() == null || edt.getText().toString().equals("")) {
                    errorOTP();
                } else {
                    if (myTimer != null) {
                        myTimer.cancel();
                    }
                    settings2 = getSharedPreferences(LOG_TAG, 0);
                    settings2.edit().putString("ActivityName", "ExitConfirmationScreen").apply();
                    isExitActivity = true;
                    if(otpValue==null){
                        otpValue=edt.getText().toString();
                    }
                    new BillPayConfirmationAsynTask().execute();
                }
            }
        });
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt.getText().length() > 5) {
                    Log.d(LOG_TAG, "otp dialog length: " + edt.getText().length());
                    myTimer.cancel();
                    if(otpValue==null){
                        otpValue=edt.getText().toString();
                    }
                    new BillPayConfirmationAsynTask().execute();

                }

            }
        });
        dialogBuilder.show();
    }

    public void errorOTP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentConfirmationActivity.this, R.style.MyAlertDialogStyle);
        builder.setCancelable(false);
        if (selectedLanguage.equalsIgnoreCase("ENG")) {
            builder.setTitle(getResources().getString(R.string.eng_otpfailed));
            builder.setMessage(getResources().getString(R.string.eng_desc_otpfailed)).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            dialogBuilder.dismiss();
                        }
                    });
        } else {
            builder.setTitle(getResources().getString(R.string.bahasa_otpfailed));
            builder.setMessage(getResources().getString(R.string.bahasa_desc_otpfailed)).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            dialogBuilder.dismiss();
                        }
                    });
        }
        AlertDialog alertError = builder.create();
        if (!isFinishing()) {
            alertError.show();
        }
    }

    class requestOTPAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("txnName", "ResendMFAOTP");
            mapContainer.put("service", Constants.SERVICE_WALLET);
            mapContainer.put("institutionID", Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put("authenticationKey", "");
            mapContainer.put("sourceMDN", sourceMDN);
            mapContainer.put("sourcePIN", stMPIN);
            mapContainer.put("sctlId", stSctl);
            mapContainer.put("channelID", "7");

            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    PaymentConfirmationActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PaymentConfirmationActivity.this);
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
                        switch (responseDataContainer.getMsgCode()) {
                            case "631": {
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(PaymentConfirmationActivity.this, R.style.MyAlertDialogStyle);
                                alertbox.setMessage(responseDataContainer.getMsg());
                                alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(PaymentConfirmationActivity.this, SecondLoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                });
                                alertbox.show();
                                dialogBuilder.dismiss();
                                break;
                            }
                            case "2171":
                                message = responseDataContainer.getMsg();
                                Log.d(LOG_TAG, "message" + message);
                                transactionTime = responseDataContainer.getTransactionTime();
                                Log.d(LOG_TAG, "transactionTime" + transactionTime);
                                responseCode = responseDataContainer.getResponseCode();
                                Log.d(LOG_TAG, "responseCode" + responseCode);
                                Log.d("test", "not null");
                                showOTPRequiredDialog();
                                break;
                            default: {
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(PaymentConfirmationActivity.this, R.style.MyAlertDialogStyle);
                                alertbox.setMessage(responseDataContainer.getMsg());
                                alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        arg0.dismiss();
                                    }
                                });
                                alertbox.show();
                                break;
                            }
                        }
                    }
                }catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }
        }
    }

}
