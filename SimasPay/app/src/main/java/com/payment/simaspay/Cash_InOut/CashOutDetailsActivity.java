package com.payment.simaspay.Cash_InOut;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
    Functions func;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 11;
    static final int PICK_CONTACT=1;
    static final int EXIT=10;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setortunaidetails);
        func=new Functions(this);
        func.initiatedToolbar(this);
        getPermissionToReadUserContacts();

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        title = (TextView) findViewById(R.id.titled);
        handphone = (TextView) findViewById(R.id.handphone);
        number = (EditText) findViewById(R.id.number);
        number.setOnTouchListener((v, event) -> {
            //final int DRAWABLE_LEFT = 0;
            //final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            //final int DRAWABLE_BOTTOM = 3;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (number.getRight() - number.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                    return true;
                }
            }
            return false;
        });

        String untuk = getIntent().getExtras().getString("untuk");
        title.setText("Tarik Tunai - "+untuk);
        if(sharedPreferences.getInt(Constants.PARAMETER_USERTYPE,-1)==Constants.CONSTANT_BANK_INT){
            if(untuk.equals("Untuk Saya")){
                handphone.setVisibility(View.GONE);
                number.setVisibility(View.GONE);
            }else if(untuk.equals("Untuk Orang Lain")){
                handphone.setVisibility(View.VISIBLE);
                number.setVisibility(View.VISIBLE);
            }
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
                int txtamount=0;
                if(!amount.getText().toString().equals("")){
                    txtamount = Integer.parseInt(amount.getText().toString().replace("Rp ", ""));
                }
                Boolean is50k = (txtamount % 50000) == 0;
                //Log.d("test","test kelipatan 50k: "+is50k);
                if(sharedPreferences.getInt(Constants.PARAMETER_USERTYPE,-1)==Constants.CONSTANT_BANK_INT){
                    if (number.getText().toString().replace(" ", "").length() <= 0) {
                        Utility.displayDialog(getResources().getString(R.string.empty_no_tujuan), CashOutDetailsActivity.this);
                    } else if (number.getText().toString().replace(" ", "").length() < 10) {
                        Utility.displayDialog(getResources().getString(R.string.empty_no_hp), CashOutDetailsActivity.this);
                    } else if (number.getText().toString().replace(" ", "").length() > 14) {
                        Utility.displayDialog(getResources().getString(R.string.empty_no_hp), CashOutDetailsActivity.this);
                    } else if (amount.getText().toString().replace("Rp ", "").length() <= 0) {
                        Utility.displayDialog("Silahkan masukkan jumlah yang ingin Anda Cashout", CashOutDetailsActivity.this);
                    } else if ((Integer.parseInt(amount.getText().toString().replace("Rp ", "")) < 100000)) {
                        Utility.displayDialog(getResources().getString(R.string.amount_invalid_cashout), CashOutDetailsActivity.this);
                    } else if (!is50k) {
                        Utility.displayDialog(getResources().getString(R.string.amount_invalid_cashout), CashOutDetailsActivity.this);
                    } else if (pin.getText().toString().length() <= 0) {
                        Utility.displayDialog(getResources().getString(R.string.empty_mpin), CashOutDetailsActivity.this);
                    } else if (pin.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                        Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), CashOutDetailsActivity.this);
                    } else {
                        pinValue=func.generateRSA(pin.getText().toString());
                        mdn = (number.getText().toString().replace(" ", ""));
                        amountValue = amount.getText().toString().replace("Rp ", "");
                        new CashOutAsynTask().execute();
                    }
                } else {
                    String untuk = getIntent().getExtras().getString("untuk");
                    if(untuk.equals("Untuk Saya")){
                        if (amount.getText().toString().replace("Rp ", "").length() <= 0) {
                            Utility.displayDialog("Silahkan masukkan jumlah yang ingin Anda Cashout.", CashOutDetailsActivity.this);
                        } else if (Integer.parseInt(amount.getText().toString().replace("Rp ", "")) < 100000){
                            Utility.displayDialog(getResources().getString(R.string.amount_invalid_cashout), CashOutDetailsActivity.this);
                        } else if (!is50k) {
                            Utility.displayDialog(getResources().getString(R.string.amount_invalid_cashout), CashOutDetailsActivity.this);
                        } else if (pin.getText().toString().length() <= 0) {
                            Utility.displayDialog(getResources().getString(R.string.empty_mpin), CashOutDetailsActivity.this);
                        } else if (pin.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                            Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), CashOutDetailsActivity.this);
                        } else {
                            pinValue=func.generateRSA(pin.getText().toString());
                            mdn = sharedPreferences.getString("mobileNumber","");
                            amountValue = amount.getText().toString().replace("Rp ", "");
                            new inquiryCashOutAsyncTask().execute();
                        }
                    }else if(untuk.equals("Untuk Orang Lain")){
                        if (number.getText().toString().replace(" ", "").length() <= 0) {
                            Utility.displayDialog(getResources().getString(R.string.empty_no_tujuan), CashOutDetailsActivity.this);
                        } else if (number.getText().toString().replace(" ", "").length() < 10) {
                            Utility.displayDialog(getResources().getString(R.string.empty_no_hp), CashOutDetailsActivity.this);
                        } else if (number.getText().toString().replace(" ", "").length() > 14) {
                            Utility.displayDialog(getResources().getString(R.string.empty_no_hp), CashOutDetailsActivity.this);
                        } else if (Integer.parseInt(amount.getText().toString().replace("Rp ", "")) < 100000) {
                            Utility.displayDialog(getResources().getString(R.string.amount_invalid_cashout), CashOutDetailsActivity.this);
                        } else if (!is50k) {
                            Utility.displayDialog(getResources().getString(R.string.amount_invalid_cashout), CashOutDetailsActivity.this);
                        } else if (amount.getText().toString().replace("Rp ", "").length() <= 0) {
                            Utility.displayDialog("Silahkan masukkan jumlah yang ingin Anda Cashout.", CashOutDetailsActivity.this);
                        } else if (pin.getText().toString().length() <= 0) {
                            Utility.displayDialog(getResources().getString(R.string.empty_mpin), CashOutDetailsActivity.this);
                        } else if (pin.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                            Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), CashOutDetailsActivity.this);
                        } else {
                            pinValue=func.generateRSA(pin.getText().toString());
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
                            number.setText(phn_no);
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

    ProgressDialog progressDialog;
    int msgCode;

    private class CashOutAsynTask extends AsyncTask<Void, Void, Void> {
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
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, Constants.TRANSACTION_CASHOUT_AT_ATM_INQUIRY);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID, Constants.CONSTANT_INSTITUTION_ID);
            //mapContainer.put("authenticationKey", "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString(Constants.PARAMETER_PHONENUMBER, ""));
            mapContainer.put(Constants.PARAMETER_ONBEHALFOFMDN, sharedPreferences.getString("userName", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, pinValue);
            //mapContainer.put("destMDN", destmdn);
            //mapContainer.put("destBankAccount", "");
            mapContainer.put(Constants.PARAMETER_AMOUNT, amountValue);
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);
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
            }else {
                    progressDialog.dismiss();
                    Utility.networkDisplayDialog(sharedPreferences.getString(
                            "ErrorMessage",
                            getResources().getString(
                                    R.string.bahasa_serverNotRespond)), CashOutDetailsActivity.this);
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
