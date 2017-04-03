package com.payment.simaspay.AgentTransfer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by widy on 1/24/17.
 * 24
 */

public class TransferBankToEmoneyActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SimasPay";
    SharedPreferences sharedPreferences;
    TextView title, handphone, jumlah, mPin, Rp;
    Button submit;
    EditText tujuan, amount, pin;
    LinearLayout btnBacke;
    String pinValue, destmdn, amountValue, sourceMDN;
    String message, transactionTime, receiverAccountName, destinationBank, destinationName, destinationAccountNumber,destinationMDN,transferID,parentTxnID,sctlID,mfaMode;
    Functions func;
    static final int PICK_CONTACT=1;
    static final int EXIT=10;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 11;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bsim_to_emoney);
        func = new Functions(this);
        func.initiatedToolbar(this);
        getPermissionToReadUserContacts();
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        btnBacke = (LinearLayout) findViewById(R.id.back_layout);
        btnBacke.setOnClickListener(view -> finish());

        title = (TextView) findViewById(R.id.titled);

        handphone = (TextView) findViewById(R.id.noHPtujuan_lbl);
        jumlah = (TextView) findViewById(R.id.jumlah);
        mPin = (TextView) findViewById(R.id.mPin);
        Rp = (TextView) findViewById(R.id.Rp);

        submit = (Button) findViewById(R.id.submit);

        tujuan = (EditText) findViewById(R.id.noHPtujuan_edt);
        tujuan.setOnTouchListener((v, event) -> {
            //final int DRAWABLE_LEFT = 0;
            //final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            //final int DRAWABLE_BOTTOM = 3;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (tujuan.getRight() - tujuan.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                    return true;
                }
            }
            return false;
        });
        amount = (EditText) findViewById(R.id.amount);
        pin = (EditText) findViewById(R.id.pin);

        SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString(Constants.PARAMETER_PHONENUMBER,"");

        title.setTypeface(Utility.Robot_Regular(TransferBankToEmoneyActivity.this));
        handphone.setTypeface(Utility.HelveticaNeue_Medium(TransferBankToEmoneyActivity.this));
        mPin.setTypeface(Utility.HelveticaNeue_Medium(TransferBankToEmoneyActivity.this));
        jumlah.setTypeface(Utility.HelveticaNeue_Medium(TransferBankToEmoneyActivity.this));
        Rp.setTypeface(Utility.HelveticaNeue_Medium(TransferBankToEmoneyActivity.this));
        tujuan.setTypeface(Utility.Robot_Light(TransferBankToEmoneyActivity.this));
        pin.setTypeface(Utility.Robot_Light(TransferBankToEmoneyActivity.this));
        amount.setTypeface(Utility.Robot_Light(TransferBankToEmoneyActivity.this));

        submit.setTypeface(Utility.Robot_Regular(TransferBankToEmoneyActivity.this));
        submit.setOnClickListener(view -> {
            if(tujuan.getText().toString().replace(" ", "").length()==0) {
                tujuan.setError("Harap masukkan Nomor Handphone Tujuan");
                return;
            }else if(tujuan.getText().toString().replace(" ", "").length()<10) {
                tujuan.setError("Nomor Handphone yang Anda masukkan harus 10-14 angka");
                return;
            }else if(tujuan.getText().toString().replace(" ", "").length()>14) {
                tujuan.setError("Nomor Handphone yang Anda masukkan harus 10-14 angka");
                return;
            }else if(amount.getText().toString().replace(" ", "").length()==0) {
                amount.setError("Harap masukkan jumlah yang ingin Anda transfer");
                return;
            }else if(pin.getText().toString().length()==0){
                pin.setError("Harap masukkan mPIN Anda");
                return;
            }else{
                pinValue=func.generateRSA(pin.getText().toString());
                destmdn = (tujuan.getText().toString().replace(" ", ""));
                amountValue = amount.getText().toString().replace("Rp ", "");
                new inquiryAsyncTask().execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT):
                if (resultCode == RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id =
                                c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =
                                c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            String phn_no = phones.getString(phones.getColumnIndex("data1"));
                            phn_no = phn_no.replace("-","");
                            phn_no = phn_no.replace("+","");
                            phn_no = phn_no.replace(" ","");
                            tujuan.setText(phn_no);
                        }
                    }
                }
                break;
            case EXIT:
                if (resultCode == RESULT_OK) {
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    private class inquiryAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, Constants.TRANSACTION_TRANSFER_INQUIRY);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.CONSTANT_BANK);
            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID, Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_KEY, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sourceMDN);
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, pinValue);
            mapContainer.put(Constants.PARAMETER_DEST_MDN, destmdn);
            mapContainer.put(Constants.PARAMETER_DEST_BankAccount, "");
            mapContainer.put(Constants.PARAMETER_AMOUNT, amountValue);
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_BANK_ID, Constants.CONSTANT_BANK_ID);
            mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
            mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_EMONEY);

            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    TransferBankToEmoneyActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(TransferBankToEmoneyActivity.this);
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
                Log.e(LOG_TAG, "Response=====" + response);
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
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(TransferBankToEmoneyActivity.this, R.style.MyAlertDialogStyle);
                                alertbox.setMessage(responseDataContainer.getMsg());
                                alertbox.setNeutralButton("OK", (arg0, arg1) -> {
                                    arg0.dismiss();
                                    Intent intent = new Intent(TransferBankToEmoneyActivity.this, SecondLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                });
                                alertbox.show();
                                break;
                            }
                            case "72":
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
                                if (mfaMode.equalsIgnoreCase("OTP")) {
                                    Intent intent = new Intent(TransferBankToEmoneyActivity.this, TransferBankToEmoneyConfirmationActivity.class);
                                    intent.putExtra("destmdn", destmdn);
                                    intent.putExtra("transferID", transferID);
                                    intent.putExtra("sctlID", sctlID);
                                    intent.putExtra("amount", amountValue);
                                    intent.putExtra("destname", receiverAccountName);
                                    intent.putExtra("mpin", pinValue);
                                    intent.putExtra("parentTxnID", parentTxnID);
                                    intent.putExtra("mfaMode", mfaMode);
                                    startActivity(intent);
                                } else {
                                    //tanpa OTP
                                    Log.d(LOG_TAG, "bypass OTP");
                                }
                                break;
                            default: {
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(TransferBankToEmoneyActivity.this, R.style.MyAlertDialogStyle);
                                alertbox.setMessage(responseDataContainer.getMsg());
                                alertbox.setNeutralButton("OK", (arg0, arg1) -> arg0.dismiss());
                                alertbox.show();
                                break;
                            }
                        }
                    }
                }catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }else{
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), TransferBankToEmoneyActivity.this);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToReadUserContacts() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
