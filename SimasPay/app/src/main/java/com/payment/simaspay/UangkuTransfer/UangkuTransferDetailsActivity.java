package com.payment.simaspay.UangkuTransfer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.utils.Functions;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;
import simaspay.payment.com.simaspay.R;

public class UangkuTransferDetailsActivity extends AppCompatActivity {
    TextView title, handphone, jumlah, mPin,Rp;
    Button submit;
    EditText number, amount, pin;
    LinearLayout btnBacke;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    String pinValue, amountValue, mdn;
    String response;
    int msgCode;
    Functions func;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transferdetails);

        func = new Functions(this);
        func.initiatedToolbar(this);

        title = (TextView) findViewById(R.id.titled);
        handphone = (TextView) findViewById(R.id.handphone);
        jumlah = (TextView) findViewById(R.id.jumlah);
        mPin = (TextView) findViewById(R.id.mPin);
        Rp=(TextView) findViewById(R.id.Rp);

        title.setText("Transfer - Uangku");

        handphone.setText(getResources().getString(R.string.nohptujuan));

        submit = (Button) findViewById(R.id.submit);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        number = (EditText) findViewById(R.id.number);
        amount = (EditText) findViewById(R.id.amount);
        pin = (EditText) findViewById(R.id.pin);

        btnBacke = (LinearLayout) findViewById(R.id.back_layout);

        title.setTypeface(Utility.Robot_Regular(UangkuTransferDetailsActivity.this));
        handphone.setTypeface(Utility.HelveticaNeue_Medium(UangkuTransferDetailsActivity.this));
        jumlah.setTypeface(Utility.HelveticaNeue_Medium(UangkuTransferDetailsActivity.this));
        mPin.setTypeface(Utility.HelveticaNeue_Medium(UangkuTransferDetailsActivity.this));
        submit.setTypeface(Utility.Robot_Regular(UangkuTransferDetailsActivity.this));
        number.setTypeface(Utility.Robot_Light(UangkuTransferDetailsActivity.this));
        amount.setTypeface(Utility.Robot_Light(UangkuTransferDetailsActivity.this));
        pin.setTypeface(Utility.Robot_Light(UangkuTransferDetailsActivity.this));
        Rp.setTypeface(Utility.Robot_Light(UangkuTransferDetailsActivity.this));

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(14);
        number.setFilters(FilterArray);

        InputFilter[] FilterArray1 = new InputFilter[1];
        FilterArray1[0] = new InputFilter.LengthFilter(getResources().getInteger(R.integer.pinSize));
        pin.setFilters(FilterArray1);


        submit.setOnClickListener(view -> {
            if (number.getText().toString().replace(" ", "").length() <= 0) {
                Utility.displayDialog(getResources().getString(R.string.id_masukkan_no_hp), UangkuTransferDetailsActivity.this);
            } else if (number.getText().toString().replace(" ", "").length() < 10) {
                Utility.displayDialog(getResources().getString(R.string.id_no_hp_validation_msg), UangkuTransferDetailsActivity.this);
            } else if (number.getText().toString().replace(" ", "").length() > 14) {
                Utility.displayDialog(getResources().getString(R.string.id_no_hp_validation_msg), UangkuTransferDetailsActivity.this);
            } else if (amount.getText().toString().replace("Rp ", "").length() <= 0) {
                Utility.displayDialog(getResources().getString(R.string.id_jumlah_transfer_validation), UangkuTransferDetailsActivity.this);
            } else if (pin.getText().toString().length() <= 0) {
                Utility.displayDialog(getResources().getString(R.string.id_masukkan_mpin), UangkuTransferDetailsActivity.this);
            }else if (pin.getText().toString().length() <getResources().getInteger(R.integer.pinSize)) {
                Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), UangkuTransferDetailsActivity.this);
            } else {
                pinValue=func.generateRSA(pin.getText().toString());
                mdn = (number.getText().toString().replace(" ", ""));
                amountValue = amount.getText().toString().replace("Rp ", "");
                new UangkuTransferAsynTask().execute();
            }
        });

        btnBacke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class UangkuTransferAsynTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_UANGKU_INQUERY);
            mapContainer.put(Constants.PARAMETER_BANK_ID, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString(Constants.PARAMETER_PHONENUMBER, ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, pinValue);
            mapContainer.put(Constants.PARAMETER_AMOUNT, amountValue);
            mapContainer.put(Constants.PARAMETER_DEST_ACCOUNT_NO, mdn);

            if(sharedPreferences.getInt(Constants.PARAMETER_USERTYPE,-1)==Constants.CONSTANT_BANK_INT){
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME,Constants.SERVICE_BANK);
            }else if(sharedPreferences.getInt(Constants.PARAMETER_USERTYPE,-1)==Constants.CONSTANT_BANKSINARMAS_INT){
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME,Constants.SERVICE_WALLET);
            }else if(sharedPreferences.getInt(Constants.PARAMETER_USERTYPE,-1)==Constants.CONSTANT_LAKUPANDAI_INT) {
                if(sharedPreferences.getInt(Constants.PARAMETER_AGENTTYPE,-1)==Constants.POCKET_INT_EMONEY){
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME,Constants.SERVICE_AGENT);
                }else{
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME,Constants.SERVICE_BANK);
                }
            }else if(sharedPreferences.getInt(Constants.PARAMETER_USERTYPE,-1)==Constants.CONSTANT_EMONEY_INT) {
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME,Constants.SERVICE_WALLET);
            }
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION,Constants.TRANSACTION_MFA_TRANSACTION);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, UangkuTransferDetailsActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            Log.e("-----------","--------"+response);
            return null;
        }


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(UangkuTransferDetailsActivity.this);
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
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(UangkuTransferDetailsActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(responseContainer.getMsg());
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(UangkuTransferDetailsActivity.this, SecondLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    alertbox.show();
                } else if (msgCode == 29) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(UangkuTransferDetailsActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(responseContainer.getMsg());
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            pin.setText("");
                        }
                    });
                    alertbox.show();
                } else if (msgCode == 72) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    sharedPreferences.edit().putString("password",pinValue).apply();
                    Intent intent = new Intent(UangkuTransferDetailsActivity.this, UangkuTransferConfirmationActivity.class);
                    intent.putExtra("amount",responseContainer.getEncryptedDebitAmount());
                    intent.putExtra("charges",responseContainer.getEncryptedTransactionCharges());
                    intent.putExtra("Acc_Number",responseContainer.getAccountNumber());
                    intent.putExtra("transferID",responseContainer.getEncryptedTransferId());
                    intent.putExtra("ParentId",responseContainer.getEncryptedParentTxnId());
                    intent.putExtra("sctlID",responseContainer.getSctl());
                    intent.putExtra("Name",responseContainer.getCustName());
                    intent.putExtra("bank",responseContainer.getDestBank());
                    intent.putExtra("mfaMode",responseContainer.getMfaMode());
                    intent.putExtra("mpin", pinValue);
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
                                UangkuTransferDetailsActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), UangkuTransferDetailsActivity.this);
                    }
                }
            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), UangkuTransferDetailsActivity.this);
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
        }
    }

    /**
    class inquiryEmoneyUangkuAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("txnName", "TransferToUangkuInquiry");
            mapContainer.put("service", Constants.SERVICE_WALLET);
            mapContainer.put("institutionID", Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put("authenticationKey", "");
            mapContainer.put("sourceMDN", sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put("sourcePIN", pinValue);
            mapContainer.put("destAccountNo", mdn);
            mapContainer.put("amount", amountValue);
            mapContainer.put("channelID", "7");
            //mapContainer.put("bankID", "");
            String account = sharedPreferences.getString("useas","");
            if(account.equals("Bank")) {
                mapContainer.put("sourcePocketCode", "2");
            }else{
                mapContainer.put("sourcePocketCode", "1");
            }

            Log.e("-----", "" + mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    UangkuTransferDetailsActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(UangkuTransferDetailsActivity.this);
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
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(UangkuTransferDetailsActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(UangkuTransferDetailsActivity.this, SecondLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                            alertbox.show();
                        }else if (responseDataContainer.getMsgCode().equals("72")) {
                            message = responseDataContainer.getMsg();
                            Log.d(LOG_TAG, "message" + message);
                            transactionTime = responseDataContainer.getTransactionTime();
                            Log.d(LOG_TAG, "transactionTime" + transactionTime);
                            //receiverAccountName = responseDataContainer.getCustName();
                            //Log.d(LOG_TAG, "receiverAccountName" + receiverAccountName);
                            destinationBank = responseDataContainer.getDestBank();
                            Log.d(LOG_TAG, "destinationBank" + destinationBank);
                            destinationName = responseDataContainer.getCustName();
                            Log.d(LOG_TAG, "destinationName" + destinationName);
                            destinationAccountNumber = responseDataContainer.getAccountNumber();
                            Log.d(LOG_TAG, "destinationAccountNumber" + destinationAccountNumber);
                            //destinationMDN = responseDataContainer.getDestMDN();
                            //Log.d(LOG_TAG, "destinationMDN" + destinationMDN);
                            transferID = responseDataContainer.getEncryptedTransferId();
                            Log.d(LOG_TAG, "transferID" + transferID);
                            parentTxnID = responseDataContainer.getEncryptedParentTxnId();
                            Log.d(LOG_TAG, "parentTxnID" + parentTxnID);
                            sctlID = responseDataContainer.getSctl();
                            Log.d(LOG_TAG, "sctlID" + sctlID);
                            mfaMode = responseDataContainer.getMfaMode();
                            amountValue = responseDataContainer.getEncryptedDebitAmount();
                            Log.d(LOG_TAG, "mfaMode" + mfaMode);
                            if (mfaMode.toString().equalsIgnoreCase("OTP")) {
                                Intent intent = new Intent(UangkuTransferDetailsActivity.this, UangkuTransferConfirmationActivity.class);
                                //intent.putExtra("DestMDN", mdn);
                                intent.putExtra("transferID", transferID);
                                intent.putExtra("sctlID", sctlID);
                                intent.putExtra("amount", amountValue);
                                intent.putExtra("destname", receiverAccountName);
                                intent.putExtra("mpin", pinValue);
                                intent.putExtra("ParentId", parentTxnID);
                                intent.putExtra("mfaMode", mfaMode);
                                intent.putExtra("charges", responseDataContainer.getEncryptedTransactionCharges());
                                intent.putExtra("transferID", transferID);
                                intent.putExtra("Acc_Number",destinationAccountNumber);
                                intent.putExtra("bank",destinationBank);
                                intent.putExtra("Name", destinationName);
                                startActivityForResult(intent, 10);
                            } else {
                                //tanpa OTP
                            }
                        } else {
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(UangkuTransferDetailsActivity.this, R.style.MyAlertDialogStyle);
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
     **/
}

