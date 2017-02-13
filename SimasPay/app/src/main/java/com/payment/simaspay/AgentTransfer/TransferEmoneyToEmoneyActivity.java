package com.payment.simaspay.AgentTransfer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;
import simaspay.payment.com.simaspay.UserHomeActivity;

/**
 * Created by widy on 1/24/17.
 * 24
 */

public class TransferEmoneyToEmoneyActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SimasPay";
    SharedPreferences sharedPreferences;
    TextView title, handphone, jumlah, mPin, Rp;
    Button submit;
    EditText tujuan, amount, pin;
    LinearLayout btnBacke;
    ProgressDialog progressDialog;
    int msgCode;
    String pinValue, destmdn, amountValue, sourceMDN;
    String message, transactionTime, receiverAccountName, destinationBank, destinationName, destinationAccountNumber,destinationMDN,transferID,parentTxnID,sctlID,mfaMode;
    private AlertDialog.Builder alertbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoney_to_emoney);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        btnBacke = (LinearLayout) findViewById(R.id.back_layout);
        btnBacke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title = (TextView) findViewById(R.id.titled);

        handphone = (TextView) findViewById(R.id.noHPtujuan_lbl);
        jumlah = (TextView) findViewById(R.id.jumlah);
        mPin = (TextView) findViewById(R.id.mPin);
        Rp = (TextView) findViewById(R.id.Rp);

        submit = (Button) findViewById(R.id.submit);

        tujuan = (EditText) findViewById(R.id.noHPtujuan_edt);
        amount = (EditText) findViewById(R.id.amount);
        pin = (EditText) findViewById(R.id.pin);

        SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString("mobileNumber","");

        title.setTypeface(Utility.Robot_Regular(TransferEmoneyToEmoneyActivity.this));
        handphone.setTypeface(Utility.HelveticaNeue_Medium(TransferEmoneyToEmoneyActivity.this));
        mPin.setTypeface(Utility.HelveticaNeue_Medium(TransferEmoneyToEmoneyActivity.this));
        jumlah.setTypeface(Utility.HelveticaNeue_Medium(TransferEmoneyToEmoneyActivity.this));
        Rp.setTypeface(Utility.HelveticaNeue_Medium(TransferEmoneyToEmoneyActivity.this));
        tujuan.setTypeface(Utility.Robot_Light(TransferEmoneyToEmoneyActivity.this));
        pin.setTypeface(Utility.Robot_Light(TransferEmoneyToEmoneyActivity.this));
        amount.setTypeface(Utility.Robot_Light(TransferEmoneyToEmoneyActivity.this));

        submit.setTypeface(Utility.Robot_Regular(TransferEmoneyToEmoneyActivity.this));
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tujuan.getText().toString().replace(" ", "").length()==0) {
                    tujuan.setError("Harap masukkan Nomor Handphone Tujuan");
                    return;
                }else if(amount.getText().toString().replace(" ", "").length()==0) {
                    amount.setError("Harap masukkan jumlah yang ingin Anda transfer");
                    return;
                }else if(pin.getText().toString().length()==0){
                    pin.setError("Harap masukkan mPIN Anda");
                    return;
                }else{
                    String module = sharedPreferences.getString("MODULE", "NONE");
                    String exponent = sharedPreferences.getString("EXPONENT", "NONE");

                    try {
                        pinValue = CryptoService.encryptWithPublicKey(module, exponent,
                                pin.getText().toString().getBytes());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    //pinValue=pin.getText().toString();
                    destmdn = (tujuan.getText().toString().replace(" ", ""));
                    amountValue = amount.getText().toString().replace("Rp ", "");

                    new inquiryAsyncTask().execute();
                }
            }
        });
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

    class inquiryAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("txnName", "TransferInquiry");
            mapContainer.put("service", "Wallet");
            mapContainer.put("institutionID", "simaspay");
            mapContainer.put("authenticationKey", "");
            mapContainer.put("sourceMDN", sourceMDN);
            mapContainer.put("sourcePIN", pinValue);
            mapContainer.put("destMDN", destmdn);
            mapContainer.put("destBankAccount", "");
            mapContainer.put("amount", amountValue);
            mapContainer.put("channelID", "7");
            mapContainer.put("bankID", "");
            mapContainer.put("sourcePocketCode", "1");
            mapContainer.put("destPocketCode", "1");

            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    TransferEmoneyToEmoneyActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(TransferEmoneyToEmoneyActivity.this);
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
                            alertbox = new AlertDialog.Builder(TransferEmoneyToEmoneyActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                    Intent intent = new Intent(TransferEmoneyToEmoneyActivity.this, SecondLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                            alertbox.show();
                        } else if(responseDataContainer.getMsgCode().equals("72")||responseDataContainer.getMsgCode().equals("676")){
                            message = responseDataContainer.getMsg();
                            Log.d(LOG_TAG, "message"+message);
                            transactionTime = responseDataContainer.getTransactionTime();
                            Log.d(LOG_TAG, "transactionTime"+transactionTime);
                            receiverAccountName = responseDataContainer.getKycName();
                            Log.d(LOG_TAG, "receiverAccountName"+receiverAccountName);
                            destinationBank = responseDataContainer.getDestBank();
                            Log.d(LOG_TAG, "destinationBank"+destinationBank);
                            destinationName = responseDataContainer.getDestinationAccountName();
                            Log.d(LOG_TAG, "destinationName"+destinationName);
                            destinationAccountNumber = responseDataContainer.getDestinationAccountNumber();
                            Log.d(LOG_TAG, "destinationAccountNumber"+destinationAccountNumber);
                            destinationMDN = responseDataContainer.getDestMDN();
                            Log.d(LOG_TAG, "destinationMDN"+destinationMDN);
                            transferID = responseDataContainer.getEncryptedTransferId();
                            Log.d(LOG_TAG, "transferID"+transferID);
                            parentTxnID = responseDataContainer.getEncryptedParentTxnId();
                            Log.d(LOG_TAG, "parentTxnID"+parentTxnID);
                            sctlID = responseDataContainer.getSctl();
                            Log.d(LOG_TAG, "sctlID"+sctlID);
                            mfaMode = responseDataContainer.getMfaMode();
                            Log.d(LOG_TAG, "mfaMode"+mfaMode);
                            if(mfaMode.toString().equalsIgnoreCase("OTP")){
                                Intent intent = new Intent(TransferEmoneyToEmoneyActivity.this, TransferEmoneyToEmoneyConfirmationActivity.class);
                                intent.putExtra("destmdn", destmdn);
                                intent.putExtra("transferID", transferID);
                                intent.putExtra("sctlID", sctlID);
                                intent.putExtra("amount", amountValue);
                                intent.putExtra("destname", receiverAccountName);
                                intent.putExtra("mpin", pinValue);
                                intent.putExtra("parentTxnID",parentTxnID);
                                intent.putExtra("mfaMode",mfaMode);
                                startActivity(intent);
                            }else{
                                //tanpa OTP
                            }
                        }else{
                            alertbox = new AlertDialog.Builder(TransferEmoneyToEmoneyActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });
                            alertbox.show();
                        }
                    }
                }catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }
        }
    }
}
