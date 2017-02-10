package com.payment.simaspay.AgentTransfer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.payment.simaspay.Cash_InOut.CashOutConfirmationActivity;
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
import simaspay.payment.com.simaspay.UserHomeActivity;

/**
 * Created by Nagendra P on 1/28/2016.
 */
public class TransferDetailsActivity extends Activity {


    TextView title, handphone, jumlah, mPin, Rp;
    private static final String LOG_TAG = "SimasPay";
    Button submit;
    EditText number, amount, pin;
    LinearLayout btnBacke;
    String message, transactionTime, receiverAccountName, destinationBank, destinationName, destinationAccountNumber, destinationMDN, transferID, parentTxnID, sctlID, mfaMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transferdetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        title = (TextView) findViewById(R.id.titled);
        handphone = (TextView) findViewById(R.id.handphone);
        jumlah = (TextView) findViewById(R.id.jumlah);
        mPin = (TextView) findViewById(R.id.mPin);
        Rp = (TextView) findViewById(R.id.Rp);

        submit = (Button) findViewById(R.id.submit);

        number = (EditText) findViewById(R.id.number);
        amount = (EditText) findViewById(R.id.amount);
        pin = (EditText) findViewById(R.id.pin);

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(15);
        number.setFilters(FilterArray);


        InputFilter[] FilterArray1 = new InputFilter[1];
        FilterArray1[0] = new InputFilter.LengthFilter(getResources().getInteger(R.integer.pinSize));
        pin.setFilters(FilterArray1);

        btnBacke = (LinearLayout) findViewById(R.id.back_layout);

        title.setTypeface(Utility.Robot_Regular(TransferDetailsActivity.this));
        handphone.setTypeface(Utility.HelveticaNeue_Medium(TransferDetailsActivity.this));
        jumlah.setTypeface(Utility.HelveticaNeue_Medium(TransferDetailsActivity.this));
        mPin.setTypeface(Utility.HelveticaNeue_Medium(TransferDetailsActivity.this));
        submit.setTypeface(Utility.Robot_Regular(TransferDetailsActivity.this));
        number.setTypeface(Utility.Robot_Light(TransferDetailsActivity.this));
        amount.setTypeface(Utility.Robot_Light(TransferDetailsActivity.this));
        pin.setTypeface(Utility.Robot_Light(TransferDetailsActivity.this));
        Rp.setTypeface(Utility.Robot_Light(TransferDetailsActivity.this));


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number.getText().toString().replace(" ", "").length() <= 0) {
                    Utility.displayDialog("Harap Masukkan Nomor Rekening Tujuan Anda.", TransferDetailsActivity.this);
                } else if (number.getText().toString().replace(" ", "").length() < 10) {
                    Utility.displayDialog("SimasPay Nomor rekening Bank Sinarmas yang Anda masukkan harus 10-15 angka.", TransferDetailsActivity.this);
                } else if (number.getText().toString().replace(" ", "").length() > 15) {
                    Utility.displayDialog("SimasPay Nomor rekening Bank Sinarmas yang Anda masukkan harus 10-15 angka.", TransferDetailsActivity.this);
                } else if (amount.getText().toString().replace("Rp ", "").length() <= 0) {
                    Utility.displayDialog("Harap masukkan jumlah yang ingin Anda transfer.", TransferDetailsActivity.this);
                } else if (pin.getText().toString().length() <= 0) {
                    Utility.displayDialog("Harap masukkan mPIN Anda.", TransferDetailsActivity.this);
                } else if (pin.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                    Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), TransferDetailsActivity.this);
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

                    String account = sharedPreferences.getString("useas","");
                    if(account.equals("Bank")){
                        new transferBankSinarmasAsynTask().execute();
                    }else{
                        new inquiryEmoneyAsyncTask().execute();
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

    SharedPreferences sharedPreferences;

    String pinValue, mdn, amountValue;

    class transferBankSinarmasAsynTask extends AsyncTask<Void, Void, Void> {


        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_TRANSFER_INQUIRY);
            mapContainer.put(Constants.PARAMETER_BANK_ID, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, pinValue);
            mapContainer.put(Constants.PARAMETER_AMOUNT, amountValue);
            mapContainer.put(Constants.PARAMETER_DEST_BankAccount, mdn);
            if (sharedPreferences.getInt("userType", -1) == 0) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, Constants.POCKET_CODE_BANK_SINARMAS_BANKCODE);
            } else if (sharedPreferences.getInt("userType", -1) == 1) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
                mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, Constants.POCKET_CODE_BANK_SINARMAS_BANKCODE);
            } else if (sharedPreferences.getInt("userType", -1) == 2) {
                if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_AGENT);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                    mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, Constants.POCKET_CODE_BANK_SINARMAS_BANKCODE);
                } else {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, Constants.POCKET_CODE_BANK_SINARMAS_BANKCODE);
                }
            }
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, TransferDetailsActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(TransferDetailsActivity.this);
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
                    Intent intent = new Intent(TransferDetailsActivity.this, SessionTimeOutActivity.class);
                    startActivityForResult(intent, 40);
                } else if (msgCode == 72) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    sharedPreferences.edit().putString("password", pinValue).commit();
                    Intent intent = new Intent(TransferDetailsActivity.this, TransferConfirmationActivity.class);
                    intent.putExtra("amount", responseContainer.getEncryptedDebitAmount());
                    intent.putExtra("charges", responseContainer.getEncryptedTransactionCharges());
                    intent.putExtra("DestMDN", responseContainer.getAccountNumber());
                    intent.putExtra("transferID", responseContainer.getEncryptedTransferId());
                    intent.putExtra("ParentId", responseContainer.getEncryptedParentTxnId());
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("Name", responseContainer.getCustName());
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
                                TransferDetailsActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), TransferDetailsActivity.this);
                    }
                }
            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), TransferDetailsActivity.this);
            }
        }
    }

    class inquiryEmoneyAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("txnName", "TransferInquiry");
            mapContainer.put("service", "Wallet");
            mapContainer.put("institutionID", "");
            mapContainer.put("authenticationKey", "");
            mapContainer.put("sourceMDN", sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put("sourcePIN", pinValue);
            mapContainer.put("destMDN", "");
            mapContainer.put("destBankAccount",mdn);
            mapContainer.put("amount", amountValue);
            mapContainer.put("channelID", "7");
            mapContainer.put("bankID", "");
            mapContainer.put("sourcePocketCode", "1");
            mapContainer.put("destPocketCode", "2");

            Log.e("-----", "" + mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    TransferDetailsActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(TransferDetailsActivity.this);
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
                        if (msgCode == 631) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            Intent intent = new Intent(TransferDetailsActivity.this, SecondLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else if (responseDataContainer.getMsgCode().equals("72") || responseDataContainer.getMsgCode().equals("676")) {
                            message = responseDataContainer.getMsg();
                            Log.d(LOG_TAG, "message" + message);
                            transactionTime = responseDataContainer.getTransactionTime();
                            Log.d(LOG_TAG, "transactionTime" + transactionTime);
                            receiverAccountName = responseDataContainer.getKycName();
                            Log.d(LOG_TAG, "receiverAccountName" + receiverAccountName);
                            destinationBank = responseDataContainer.getDestBank();
                            Log.d(LOG_TAG, "destinationBank" + destinationBank);
                            destinationName = responseDataContainer.getDestinationAccountName();
                            Log.d(LOG_TAG, "destinationName" + destinationName);
                            destinationAccountNumber = responseDataContainer.getDestinationAccountNumber();
                            Log.d(LOG_TAG, "destinationAccountNumber" + destinationAccountNumber);
                            destinationMDN = responseDataContainer.getDestMDN();
                            Log.d(LOG_TAG, "destinationMDN" + destinationMDN);
                            transferID = responseDataContainer.getEncryptedTransferId();
                            Log.d(LOG_TAG, "transferID" + transferID);
                            parentTxnID = responseDataContainer.getEncryptedParentTxnId();
                            Log.d(LOG_TAG, "parentTxnID" + parentTxnID);
                            sctlID = responseDataContainer.getSctl();
                            Log.d(LOG_TAG, "sctlID" + sctlID);
                            mfaMode = responseDataContainer.getMfaMode();
                            Log.d(LOG_TAG, "mfaMode" + mfaMode);
                            if (mfaMode.toString().equalsIgnoreCase("OTP")) {
                                Intent intent = new Intent(TransferDetailsActivity.this, TransferConfirmationActivity.class);
                                intent.putExtra("DestMDN", mdn);
                                intent.putExtra("transferID", transferID);
                                intent.putExtra("sctlID", sctlID);
                                intent.putExtra("amount", amountValue);
                                intent.putExtra("destname", receiverAccountName);
                                intent.putExtra("mpin", pinValue);
                                intent.putExtra("ParentId", parentTxnID);
                                intent.putExtra("mfaMode", mfaMode);
                                intent.putExtra("charges", responseDataContainer.getEncryptedTransactionCharges());
                                intent.putExtra("transferID", transferID);
                                intent.putExtra("Name", receiverAccountName);
                                startActivity(intent);
                            } else {
                                //tanpa OTP
                            }
                        } else {
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(TransferDetailsActivity.this, R.style.MyAlertDialogStyle);
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
