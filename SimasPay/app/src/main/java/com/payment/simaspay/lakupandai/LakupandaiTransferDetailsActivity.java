package com.payment.simaspay.lakupandai;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import com.payment.simaspay.AgentTransfer.TransferConfirmationActivity;
import com.payment.simaspay.Cash_InOut.CashInConfirmationActivity;
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
public class LakupandaiTransferDetailsActivity extends Activity {

    TextView title, handphone, jumlah, mPin,Rp;

    Button submit;

    EditText number, amount, pin;

    LinearLayout btnBacke;

    SharedPreferences sharedPreferences;

    String pinValue, amountValue, mdn;

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

        title = (TextView) findViewById(R.id.titled);
        handphone = (TextView) findViewById(R.id.handphone);
        jumlah = (TextView) findViewById(R.id.jumlah);
        mPin = (TextView) findViewById(R.id.mPin);
        Rp=(TextView) findViewById(R.id.Rp);

        title.setText("Transfer - Laku Pandai");

        handphone.setText("Nomor Handphone Tujuan");

        submit = (Button) findViewById(R.id.submit);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        number = (EditText) findViewById(R.id.number);
        amount = (EditText) findViewById(R.id.amount);
        pin = (EditText) findViewById(R.id.pin);

        btnBacke = (LinearLayout) findViewById(R.id.back_layout);

        title.setTypeface(Utility.Robot_Regular(LakupandaiTransferDetailsActivity.this));
        handphone.setTypeface(Utility.HelveticaNeue_Medium(LakupandaiTransferDetailsActivity.this));
        jumlah.setTypeface(Utility.HelveticaNeue_Medium(LakupandaiTransferDetailsActivity.this));
        mPin.setTypeface(Utility.HelveticaNeue_Medium(LakupandaiTransferDetailsActivity.this));
        submit.setTypeface(Utility.Robot_Regular(LakupandaiTransferDetailsActivity.this));
        number.setTypeface(Utility.Robot_Light(LakupandaiTransferDetailsActivity.this));
        amount.setTypeface(Utility.Robot_Light(LakupandaiTransferDetailsActivity.this));
        pin.setTypeface(Utility.Robot_Light(LakupandaiTransferDetailsActivity.this));
        Rp.setTypeface(Utility.Robot_Light(LakupandaiTransferDetailsActivity.this));


        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(14);
        number.setFilters(FilterArray);

        InputFilter[] FilterArray1 = new InputFilter[1];
        FilterArray1[0] = new InputFilter.LengthFilter(getResources().getInteger(R.integer.pinSize));
        pin.setFilters(FilterArray1);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number.getText().toString().replace(" ", "").length() <= 0) {
                    Utility.displayDialog("Masukkan Nomor Handphone Tujuan.", LakupandaiTransferDetailsActivity.this);
                } else if (number.getText().toString().replace(" ", "").length() < 10) {
                    Utility.displayDialog("Nomor rekening yang Anda masukkan harus 10-14 angka.", LakupandaiTransferDetailsActivity.this);
                } else if (number.getText().toString().replace(" ", "").length() > 14) {
                    Utility.displayDialog("Nomor rekening yang Anda masukkan harus 10-14 angka.", LakupandaiTransferDetailsActivity.this);
                } else if (amount.getText().toString().replace("Rp ", "").length() <= 0) {
                    Utility.displayDialog("Harap masukkan jumlah yang ingin Anda transfer.", LakupandaiTransferDetailsActivity.this);
                } else if (pin.getText().toString().length() <= 0) {
                    Utility.displayDialog("Harap masukkan mPIN Anda.", LakupandaiTransferDetailsActivity.this);
                }else if (pin.getText().toString().length() <getResources().getInteger(R.integer.pinSize)) {
                    Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), LakupandaiTransferDetailsActivity.this);
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
                    new LakuPandaiInterBankAsynTask().execute();

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


    int msgCode;
    ProgressDialog progressDialog;


    class LakuPandaiInterBankAsynTask extends AsyncTask<Void, Void, Void> {


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
            mapContainer.put(Constants.PARAMETER_DEST_MDN, mdn);

            if(sharedPreferences.getInt("userType",-1)==0){
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, "");
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME,Constants.SERVICE_BANK);
            }else if(sharedPreferences.getInt("userType",-1)==1){
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
                mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, "");
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME,Constants.SERVICE_WALLET);
            }else if(sharedPreferences.getInt("userType",-1)==2) {
                if(sharedPreferences.getInt("AgentUsing",-1)==1){
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                    mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, "");
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME,Constants.SERVICE_AGENT);
                }else{
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE,  "");
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME,Constants.SERVICE_BANK);
                }
            }
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION,Constants.TRANSACTION_MFA_TRANSACTION);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, LakupandaiTransferDetailsActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LakupandaiTransferDetailsActivity.this);
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
                    Intent intent = new Intent(LakupandaiTransferDetailsActivity.this, SessionTimeOutActivity.class);
                    startActivityForResult(intent, 40);
                } else if (msgCode == 72) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    sharedPreferences.edit().putString("password",pinValue).commit();
                    Intent intent = new Intent(LakupandaiTransferDetailsActivity.this, LakuPandaiTransferConfirmationActivity.class);
                    intent.putExtra("amount",responseContainer.getEncryptedDebitAmount());
                    intent.putExtra("charges",responseContainer.getEncryptedTransactionCharges());
                    intent.putExtra("Acc_Number",responseContainer.getDestMDN());
                    intent.putExtra("transferID",responseContainer.getEncryptedTransferId());
                    intent.putExtra("ParentId",responseContainer.getEncryptedParentTxnId());
                    intent.putExtra("sctlID",responseContainer.getSctl());
                    intent.putExtra("Name",responseContainer.getKycName());
                    intent.putExtra("bank",responseContainer.getDestBank());
                    intent.putExtra("mfaMode",responseContainer.getMfaMode());
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
                                LakupandaiTransferDetailsActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), LakupandaiTransferDetailsActivity.this);
                    }
                }
            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), LakupandaiTransferDetailsActivity.this);
            }
        }
    }
}
