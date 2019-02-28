package com.payment.simaspay.PaymentPurchaseAccount;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.R;
import com.payment.simaspay.receivers.IncomingSMS;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.utils.Functions;
import com.payment.simaspay.constants.EncryptedResponseDataContainer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class PurchaseConfirmationActivity extends AppCompatActivity implements IncomingSMS.AutoReadSMSListener {
    private static final String LOG_TAG = "SimasPay";
    TextView title, heading, name, name_field, number, number_field, amount, amount_field, charges, charges_field, total, total_field;
    Button cancel, confirmation;
    LinearLayout back;
    View line;
    SharedPreferences sharedPreferences, languageSettings;
    ProgressDialog progressDialog;
    String OTP;
    private EditText edt;
    private static AlertDialog dialogBuilder;
    LinearLayout otplay, otp2lay;
    Context context;
    String otpValue;
    String selectedLanguage;
    String sourceMDN, stMPIN, stSctl, stTransferID, stParentTxnID, stAmount, stCharges, stName;
    Functions func;
    int idTransferCat;
    String typeTransferCat="", AdditionalInfo="";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.commonconfirmation);
        func = new Functions(this);
        func.initiatedToolbar(this);
        IncomingSMS.setListener(PurchaseConfirmationActivity.this);

        title = findViewById(R.id.title);
        heading = findViewById(R.id.textview);
        name = findViewById(R.id.name);
        name_field = findViewById(R.id.name_field);
        number = findViewById(R.id.number);
        number_field = findViewById(R.id.number_field);
        amount = findViewById(R.id.amount);
        amount_field = findViewById(R.id.amount_field);
        charges = findViewById(R.id.products);
        charges_field = findViewById(R.id.other_products);
        total = findViewById(R.id.total);
        total_field = findViewById(R.id.total_field);

        line = findViewById(R.id.line);
        cancel = findViewById(R.id.cancel);
        confirmation = findViewById(R.id.next);

        back = findViewById(R.id.back_layout);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");

        progressDialog = new ProgressDialog(PurchaseConfirmationActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
        Drawable drawable = new ProgressBar(PurchaseConfirmationActivity.this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(PurchaseConfirmationActivity.this, R.color.red_sinarmas),
                PorterDuff.Mode.SRC_IN);
        progressDialog.setIndeterminateDrawable(drawable);

        String module = sharedPreferences.getString("MODULE", "NONE");
        String exponent = sharedPreferences.getString("EXPONENT", "NONE");
        sourceMDN = sharedPreferences.getString("mobileNumber", "");
        String pinValue = "";
        try {
            pinValue = CryptoService.encryptWithPublicKey(module, exponent,
                    sharedPreferences.getString("mpin", "").getBytes());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        stMPIN = pinValue;
        stSctl = getIntent().getExtras().getString("sctlID");

        stTransferID = getIntent().getExtras().getString("transferID");
        stParentTxnID = getIntent().getExtras().getString("parentTxnID");
        stAmount = getIntent().getExtras().getString("originalAmount");
        stCharges = getIntent().getExtras().getString("charges");
        stName = getIntent().getExtras().getString("Name");

        title.setTypeface(Utility.Robot_Regular(PurchaseConfirmationActivity.this));
        heading.setTypeface(Utility.Robot_Regular(PurchaseConfirmationActivity.this));
        name.setTypeface(Utility.Robot_Regular(PurchaseConfirmationActivity.this));
        name_field.setTypeface(Utility.Robot_Light(PurchaseConfirmationActivity.this));
        number.setTypeface(Utility.Robot_Regular(PurchaseConfirmationActivity.this));
        number_field.setTypeface(Utility.Robot_Light(PurchaseConfirmationActivity.this));
        amount.setTypeface(Utility.Robot_Regular(PurchaseConfirmationActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(PurchaseConfirmationActivity.this));
        charges.setTypeface(Utility.Robot_Regular(PurchaseConfirmationActivity.this));
        charges_field.setTypeface(Utility.Robot_Light(PurchaseConfirmationActivity.this));
        total.setTypeface(Utility.Robot_Regular(PurchaseConfirmationActivity.this));
        total_field.setTypeface(Utility.Robot_Light(PurchaseConfirmationActivity.this));
        cancel.setTypeface(Utility.Robot_Regular(PurchaseConfirmationActivity.this));
        confirmation.setTypeface(Utility.Robot_Regular(PurchaseConfirmationActivity.this));

        cancel.setOnClickListener(view -> finish());

        name.setText("Nama Produk");
        name_field.setText(getIntent().getExtras().getString("billerDetails"));
        number.setText("Nominal Pulsa");
        number_field.setText("Rp. " + getIntent().getExtras().getString("originalAmount"));
        amount.setText("Nomor Handphone");
        amount_field.setText(getIntent().getExtras().getString("invoiceNo"));
        charges.setText("Biaya Administrasi");
        charges_field.setText("Rp. " + getIntent().getExtras().getString("charges"));
        total.setText("Total Pendebitan");
        total_field.setText("Rp. " + getIntent().getExtras().getString("debitamt"));
        AdditionalInfo=getIntent().getExtras().getString("additionalInfo");
        if(AdditionalInfo!=null&&!AdditionalInfo.equals("")){
            Log.d(LOG_TAG, "test AdditInfo: "+AdditionalInfo);
        }

        charges_field.setVisibility(View.VISIBLE);
        charges.setVisibility(View.VISIBLE);
        total.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);
        total_field.setVisibility(View.VISIBLE);


        amount_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        number_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        name_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        charges_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        total_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));


        confirmation.setOnClickListener(view -> {

            if (Objects.requireNonNull(getIntent().getExtras().getString("mfaMode")).equalsIgnoreCase("OTP")) {
                new requestOTPAsyncTask().execute();
            } else {
                new PurchaseConfirmationAsynTask().execute();
            }


        });

        back.setOnClickListener(view -> finish());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 109) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(LOG_TAG, "permission granted");
            }
        }
    }

    @Override
    public void onReadSMS(String otp) {
        Log.d(LOG_TAG, "otp from SMS: " + otp);
        if(otp==null){
            edt.setText("");
        }else{
            edt.setText(otp);
        }
        otpValue = otp;
    }


    private class PurchaseConfirmationAsynTask extends AsyncTask<Void, Void, Void> {
        String response;

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_AIRTIME_PURCHASE);
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
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BUY);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                typeTransferCat="B2Purchase";
                idTransferCat=2;
            } else if (sharedPreferences.getInt("userType", -1) == 1) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BUY);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
                typeTransferCat="B2Purchase";
                idTransferCat=2;
            } else if (sharedPreferences.getInt("userType", -1) == 2) {
                if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_AGENT);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                    typeTransferCat="E2Purchase";
                    idTransferCat=8;
                } else {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_AGENT);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    typeTransferCat="B2Purchase";
                    idTransferCat=2;
                }
            } else if (sharedPreferences.getInt("userType", -1) == 3) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BUY);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                typeTransferCat="E2Purchase";
                idTransferCat=8;
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
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, PurchaseConfirmationActivity.this);

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
                int msgCode;
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
                } else if (msgCode == 653 || msgCode == 2030) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(PurchaseConfirmationActivity.this, PurchaseSuccessActivity.class);
                    if (responseContainer.getInvoiceNo() != null) {
                        intent.putExtra("invoiceNo", responseContainer.getInvoiceNo());
                    } else {
                        intent.putExtra("invoiceNo", getIntent().getExtras().getString("invoiceNo"));
                    }
                    intent.putExtra("billerDetails", getIntent().getExtras().getString("billerDetails"));
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("amount", responseContainer.getEncryptedDebitAmount());
                    intent.putExtra("originalAmount", responseContainer.getAmount());
                    intent.putExtra("additionalInfo", responseContainer.getAditionalInfo());
                    intent.putExtra("charges", responseContainer.getEncryptedTransactionCharges());
                    intent.putExtra("favCode", getIntent().getExtras().getString("ProductCode"));
                    intent.putExtra("favCat", typeTransferCat);
                    intent.putExtra("idFavCat", idTransferCat);
                    intent.putExtra("selectedItem", getIntent().getExtras().getString("selectedItem"));
                    startActivityForResult(intent, 10);
                } else if (msgCode == 703) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(PurchaseConfirmationActivity.this, PaymentSuccessActivity.class);
                    intent.putExtra("amount", getIntent().getExtras().getString("amount"));
                    intent.putExtra("originalAmount", responseContainer.getAmount());
                    intent.putExtra("DestMDN", getIntent().getExtras().getString("DestMDN"));
                    intent.putExtra("transferID", responseContainer.getEncryptedTransferId());
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("charges", responseContainer.getEncryptedTransactionCharges());
                    intent.putExtra("Name", getIntent().getExtras().getString("Name"));
                    intent.putExtra("additionalInfo", responseContainer.getAditionalInfo());
                    intent.putExtra("favCode", getIntent().getExtras().getString("ProductCode"));
                    intent.putExtra("favCat", typeTransferCat);
                    intent.putExtra("idFavCat", idTransferCat);
                    intent.putExtra("selectedItem", getIntent().getExtras().getString("selectedItem"));
                    startActivityForResult(intent, 10);
                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (responseContainer.getMsg() == null) {
                        func.errorNullResponseConfirmation();
                        dialogBuilder.dismiss();
                    } else {
                        func.errorElseResponseConfirmation(responseContainer.getMsg());
                        dialogBuilder.dismiss();
                    }
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                func.errorNullResponseConfirmation();
                dialogBuilder.dismiss();
            }
        }
    }

    private void showOTPRequiredDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullParent = null;
        View dialoglayout = inflater.inflate(R.layout.new_otp_dialog, nullParent, false);
        dialogBuilder = new AlertDialog.Builder(PurchaseConfirmationActivity.this, R.style.MyAlertDialogStyle).create();
        dialogBuilder.setCanceledOnTouchOutside(false);
        dialogBuilder.setTitle("");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(dialoglayout);

        // EditText OTP
        otplay = dialoglayout.findViewById(R.id.halaman1);
        otp2lay = dialoglayout.findViewById(R.id.halaman2);
        otp2lay.setVisibility(View.GONE);
        TextView manualotp = dialoglayout.findViewById(R.id.manualsms_lbl);
        Button cancel_otp = dialoglayout.findViewById(R.id.cancel_otp);
        manualotp.setOnClickListener(arg0 -> {
            otplay.setVisibility(View.GONE);
            otp2lay.setVisibility(View.VISIBLE);
        });
        edt = dialoglayout.findViewById(R.id.otp_value);

        Log.d(LOG_TAG, "otpValue : " + edt.getText().toString());

        // Timer
        final TextView timer = dialoglayout.findViewById(R.id.otp_timer);
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
        final Button ok_otp = dialoglayout.findViewById(R.id.ok_otp);
        ok_otp.setEnabled(false);
        ok_otp.setTextColor(getResources().getColor(R.color.dark_red));
        ok_otp.setOnClickListener(v -> {
            if (edt.getText() == null || edt.getText().toString().equals("")) {
                func.errorEmptyOTP();
            } else {
                myTimer.cancel();
                if (otpValue == null || otpValue.equals("")) {
                    otpValue = edt.getText().toString();
                }
                new PurchaseConfirmationAsynTask().execute();
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
                ok_otp.setEnabled(true);
                ok_otp.setTextColor(getResources().getColor(R.color.red));
                if (edt.getText().length() >= Constants.DIGITS_OTP) {
                    Log.d(LOG_TAG, "otp dialog length: " + edt.getText().length());
                    myTimer.cancel();
                    if (otpValue == null || otpValue.equals("")) {
                        otpValue = edt.getText().toString();
                    }
                    new PurchaseConfirmationAsynTask().execute();

                }

            }
        });
        dialogBuilder.show();
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

            Log.e("-----", "" + mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    PurchaseConfirmationActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PurchaseConfirmationActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            Drawable drawable = new ProgressBar(PurchaseConfirmationActivity.this).getIndeterminateDrawable().mutate();
            drawable.setColorFilter(ContextCompat.getColor(PurchaseConfirmationActivity.this, R.color.red_sinarmas),
                    PorterDuff.Mode.SRC_IN);
            progressDialog.setIndeterminateDrawable(drawable);
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
                            case "631":
                                func.errorTimeoutResponseConfirmation(responseDataContainer.getMsg());
                                break;
                            case "2171":
                                String message = responseDataContainer.getMsg();
                                Log.d(LOG_TAG, "message" + message);
                                String transactionTime = responseDataContainer.getTransactionTime();
                                Log.d(LOG_TAG, "transactionTime" + transactionTime);
                                String responseCode = responseDataContainer.getResponseCode();
                                Log.d(LOG_TAG, "responseCode" + responseCode);
                                Log.d("test", "not null");
                                showOTPRequiredDialog();
                                break;
                            default:
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(PurchaseConfirmationActivity.this, R.style.MyAlertDialogStyle);
                                alertbox.setMessage(responseDataContainer.getMsg());
                                alertbox.setNeutralButton("OK", (arg0, arg1) -> arg0.dismiss());
                                alertbox.show();
                                break;
                        }
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }else{
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), PurchaseConfirmationActivity.this);
            }
        }
    }
}
