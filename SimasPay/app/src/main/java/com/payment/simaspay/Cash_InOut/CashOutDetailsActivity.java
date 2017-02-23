package com.payment.simaspay.Cash_InOut;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.AgentTransfer.TransferEmoneyActivity;
import com.payment.simaspay.AgentTransfer.TransferEmoneyConfirmationActivity;
import com.payment.simaspay.AgentTransfer.TransferEmoneyToEmoneyConfirmationActivity;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.userdetails.SessionTimeOutActivity;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/29/2016.
 */
public class CashOutDetailsActivity extends AppCompatActivity {
    TextView title, handphone, jumlah, mPin, Rp;
    Button submit;
    EditText number, amount, pin;
    LinearLayout btnBacke;
    String pinValue, mdn, amountValue;
    SharedPreferences sharedPreferences;
    String message, transactionTime, debitamt, creditamt, charges, transferID, parentTxnID, sctlID, mfaMode;
    private AlertDialog.Builder alertbox;
    private static final String LOG_TAG = "SimasPay";

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
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        title = (TextView) findViewById(R.id.titled);
        handphone = (TextView) findViewById(R.id.handphone);
        number = (EditText) findViewById(R.id.number);

        String untuk = getIntent().getExtras().getString("untuk");
        title.setText("Tarik Tunai - "+untuk);
        String account = sharedPreferences.getString("useas", "");
        if (account.equals("Bank")) {
            handphone.setVisibility(View.VISIBLE);
            number.setVisibility(View.VISIBLE);
        } else {
            if(untuk.equals("Untuk Saya")){
                handphone.setVisibility(View.GONE);
                number.setVisibility(View.GONE);
            }else if(untuk.equals("Untuk Orang Lain")){
                handphone.setVisibility(View.VISIBLE);
                number.setVisibility(View.VISIBLE);
            }
        }

        jumlah = (TextView) findViewById(R.id.jumlah);
        mPin = (TextView) findViewById(R.id.mPin);
        Rp = (TextView) findViewById(R.id.Rp);

        submit = (Button) findViewById(R.id.submit);

        amount = (EditText) findViewById(R.id.amount);
        pin = (EditText) findViewById(R.id.pin);


        btnBacke = (LinearLayout) findViewById(R.id.back_layout);

        title.setTypeface(Utility.Robot_Regular(CashOutDetailsActivity.this));
        handphone.setTypeface(Utility.HelveticaNeue_Medium(CashOutDetailsActivity.this));
        jumlah.setTypeface(Utility.HelveticaNeue_Medium(CashOutDetailsActivity.this));
        mPin.setTypeface(Utility.HelveticaNeue_Medium(CashOutDetailsActivity.this));
        submit.setTypeface(Utility.Robot_Regular(CashOutDetailsActivity.this));
        number.setTypeface(Utility.Robot_Light(CashOutDetailsActivity.this));
        amount.setTypeface(Utility.Robot_Light(CashOutDetailsActivity.this));
        pin.setTypeface(Utility.Robot_Light(CashOutDetailsActivity.this));
        Rp.setTypeface(Utility.Robot_Light(CashOutDetailsActivity.this));


        handphone.setText("Nomor Handphone");

        InputFilter[] FilterArray1 = new InputFilter[1];
        FilterArray1[0] = new InputFilter.LengthFilter(getResources().getInteger(R.integer.pinSize));
        pin.setFilters(FilterArray1);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = sharedPreferences.getString("useas", "");
                if (account.equals("Bank")) {
                    if (number.getText().toString().replace(" ", "").length() <= 0) {
                        Utility.displayDialog("Harap masukkan nomor Handphone Anda", CashOutDetailsActivity.this);
                    } else if (number.getText().toString().replace(" ", "").length() < 10) {
                        Utility.displayDialog("Nomor Handphone yang Anda masukkan harus 10-14 angka", CashOutDetailsActivity.this);
                    } else if (number.getText().toString().replace(" ", "").length() > 14) {
                        Utility.displayDialog("Nomor Handphone yang Anda masukkan harus 10-14 angka", CashOutDetailsActivity.this);
                    } else if (amount.getText().toString().replace("Rp ", "").length() <= 0) {
                        Utility.displayDialog("Silahkan masukkan jumlah yang ingin Anda Cashout.", CashOutDetailsActivity.this);
                    } else if (pin.getText().toString().length() <= 0) {
                        Utility.displayDialog("Harap masukkan mPIN Anda.", CashOutDetailsActivity.this);
                    } else if (pin.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                        Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), CashOutDetailsActivity.this);
                    } else {
                        String module = sharedPreferences.getString("MODULE", "NONE");
                        String exponent = sharedPreferences.getString("EXPONENT", "NONE");

                        try {
                            pinValue = CryptoService.encryptWithPublicKey(module, exponent,
                                    pin.getText().toString().getBytes());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        mdn = (number.getText().toString().replace(" ", ""));
                        amountValue = amount.getText().toString().replace("Rp ", "");
                        new CashOutAsynTask().execute();
                    }
                } else {
                    String untuk = getIntent().getExtras().getString("untuk");
                    if(untuk.equals("Untuk Saya")){
                        if (amount.getText().toString().replace("Rp ", "").length() <= 0) {
                            Utility.displayDialog("Silahkan masukkan jumlah yang ingin Anda Cashout.", CashOutDetailsActivity.this);
                        } else if (pin.getText().toString().length() <= 0) {
                            Utility.displayDialog("Harap masukkan mPIN Anda.", CashOutDetailsActivity.this);
                        } else if (pin.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                            Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), CashOutDetailsActivity.this);
                        } else {
                            String module = sharedPreferences.getString("MODULE", "NONE");
                            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

                            try {
                                pinValue = CryptoService.encryptWithPublicKey(module, exponent,
                                        pin.getText().toString().getBytes());
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            mdn = sharedPreferences.getString("mobileNumber","");
                            amountValue = amount.getText().toString().replace("Rp ", "");
                            new inquiryCashOutAsyncTask().execute();
                        }
                    }else if(untuk.equals("Untuk Orang Lain")){
                        if (number.getText().toString().replace(" ", "").length() <= 0) {
                            Utility.displayDialog("Harap masukkan nomor Handphone Anda", CashOutDetailsActivity.this);
                        } else if (number.getText().toString().replace(" ", "").length() < 10) {
                            Utility.displayDialog("Nomor Handphone yang Anda masukkan harus 10-14 angka", CashOutDetailsActivity.this);
                        } else if (number.getText().toString().replace(" ", "").length() > 14) {
                            Utility.displayDialog("Nomor Handphone yang Anda masukkan harus 10-14 angka", CashOutDetailsActivity.this);
                        } else if (amount.getText().toString().replace("Rp ", "").length() <= 0) {
                            Utility.displayDialog("Silahkan masukkan jumlah yang ingin Anda Cashout.", CashOutDetailsActivity.this);
                        } else if (pin.getText().toString().length() <= 0) {
                            Utility.displayDialog("Harap masukkan mPIN Anda.", CashOutDetailsActivity.this);
                        } else if (pin.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                            Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), CashOutDetailsActivity.this);
                        } else {
                            String module = sharedPreferences.getString("MODULE", "NONE");
                            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

                            try {
                                pinValue = CryptoService.encryptWithPublicKey(module, exponent,
                                        pin.getText().toString().getBytes());
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            mdn = (number.getText().toString().replace(" ", ""));
                            amountValue = amount.getText().toString().replace("Rp ", "");
                            new inquiryCashOutAsyncTask().execute();
                        }
                    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 10) {
                finish();
            }
        }
    }

    ProgressDialog progressDialog;
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
                    Constants.TRANSACTION_CASHOUT_INQUIRY);
            mapContainer.put(Constants.PARAMETER_BANK_ID, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, pinValue);
            mapContainer.put(Constants.PARAMETER_AMOUNT, amountValue);
            mapContainer.put(Constants.PARAMETER_DEST_MDN, mdn);
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION);
            mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
            mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, CashOutDetailsActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CashOutDetailsActivity.this);
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
                if (msgCode == 631) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(CashOutDetailsActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(responseContainer.getMsg());
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(CashOutDetailsActivity.this, SecondLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    alertbox.show();
                } else if (msgCode == 72) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    sharedPreferences.edit().putString("password", pinValue).apply();
                    Intent intent = new Intent(CashOutDetailsActivity.this, CashOutConfirmationActivity.class);
                    intent.putExtra("amount", responseContainer.getEncryptedDebitAmount());
                    intent.putExtra("charges", responseContainer.getEncryptedTransactionCharges());
                    intent.putExtra("DestMDN", mdn);
                    intent.putExtra("transferID", responseContainer.getEncryptedTransferId());
                    intent.putExtra("ParentId", responseContainer.getEncryptedParentTxnId());
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("Name", responseContainer.getName());
                    intent.putExtra("mfaMode", responseContainer.getMfaMode());
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
                                CashOutDetailsActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), CashOutDetailsActivity.this);
                    }
                }
            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), CashOutDetailsActivity.this);
            }
        }
    }

    class inquiryCashOutAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("txnName", "CashOutAtATMInquiry");
            mapContainer.put("service", "Wallet");
            mapContainer.put("institutionID", Constants.CONSTANT_INSTITUTION_ID);
            //mapContainer.put("authenticationKey", "");
            mapContainer.put("sourceMDN", sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put("onBehalfOfMDN", sharedPreferences.getString("userName", ""));
            mapContainer.put("sourcePIN", pinValue);
            //mapContainer.put("destMDN", destmdn);
            //mapContainer.put("destBankAccount", "");
            mapContainer.put("amount", amountValue);
            mapContainer.put("channelID", "7");
            //mapContainer.put("bankID", "");
            //mapContainer.put("sourcePocketCode", "2");
            //mapContainer.put("destPocketCode", "1");

            Log.e("-----", "" + mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    CashOutDetailsActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CashOutDetailsActivity.this);
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
                        if (responseDataContainer.getMsgCode().equals("631")) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            alertbox = new AlertDialog.Builder(CashOutDetailsActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(CashOutDetailsActivity.this, SecondLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                            alertbox.show();
                        } else if (responseDataContainer.getMsgCode().equals("708")) {
                            message = responseDataContainer.getMsg();
                            Log.d(LOG_TAG, "message" + message);
                            transactionTime = responseDataContainer.getTransactionTime();
                            Log.d(LOG_TAG, "transactionTime" + transactionTime);
                            debitamt = responseDataContainer.getEncryptedDebitAmount();
                            creditamt = responseDataContainer.getEncryptedCreditAmount();
                            charges = responseDataContainer.getEncryptedTransactionCharges();
                            transferID = responseDataContainer.getEncryptedTransferId();
                            Log.d(LOG_TAG, "transferID" + transferID);
                            parentTxnID = responseDataContainer.getEncryptedParentTxnId();
                            Log.d(LOG_TAG, "parentTxnID" + parentTxnID);
                            sctlID = responseDataContainer.getSctl();
                            Log.d(LOG_TAG, "sctlID" + sctlID);
                            mfaMode = responseDataContainer.getMfaMode();
                            Log.d(LOG_TAG, "mfaMode" + mfaMode);
                            if (mfaMode.toString().equalsIgnoreCase("OTP")) {
                                sharedPreferences.edit().putString("password", pinValue).apply();
                                Intent intent = new Intent(CashOutDetailsActivity.this, CashOutConfirmationActivity.class);
                                intent.putExtra("amount", responseDataContainer.getEncryptedDebitAmount());
                                intent.putExtra("charges", responseDataContainer.getEncryptedTransactionCharges());
                                intent.putExtra("DestMDN", mdn);
                                intent.putExtra("transferID", responseDataContainer.getEncryptedTransferId());
                                intent.putExtra("ParentId", responseDataContainer.getEncryptedParentTxnId());
                                intent.putExtra("sctlID", responseDataContainer.getSctl());
                                intent.putExtra("Name", responseDataContainer.getName());
                                intent.putExtra("mfaMode", responseDataContainer.getMfaMode());
                                intent.putExtra("untuk", getIntent().getExtras().getString("untuk"));
                                startActivityForResult(intent, 10);
                            } else {
                                //tanpa OTP
                            }
                        } else {
                            alertbox = new AlertDialog.Builder(CashOutDetailsActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });
                            alertbox.show();
                        }
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }
        }
    }
}
