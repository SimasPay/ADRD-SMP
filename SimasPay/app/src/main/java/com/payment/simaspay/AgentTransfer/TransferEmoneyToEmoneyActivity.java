package com.payment.simaspay.AgentTransfer;

import android.Manifest;
import android.annotation.TargetApi;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.utils.CustomSpinnerAdapter;
import com.payment.simaspay.utils.FavoriteData;
import com.payment.simaspay.utils.Functions;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

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
    String pinValue, destmdn, amountValue, sourceMDN, stMPIN;
    String message, transactionTime, receiverAccountName, destinationBank, destinationName, destinationAccountNumber,destinationMDN,transferID,parentTxnID,sctlID,mfaMode;
    private Functions func;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 11;
    static final int PICK_CONTACT=1;
    static final int EXIT=10;
    ArrayList<FavoriteData> favList2 = new ArrayList<FavoriteData>();
    int msgCode, stCatID;
    Spinner spinner_fav;
    String selectedItem="man";
    String selectedValue;
    SharedPreferences settings, languageSettings;
    String selectedLanguage;
    RelativeLayout spinner_layout;
    int spinnerLength=0;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoney_to_emoney);
        func=new Functions(this);
        func.initiatedToolbar(this);
        getPermissionToReadUserContacts();

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");
        settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString("mobileNumber", "");
        stMPIN = func.generateRSA(sharedPreferences.getString(Constants.PARAMETER_MPIN, ""));

        btnBacke = (LinearLayout) findViewById(R.id.back_layout);
        btnBacke.setOnClickListener(view -> finish());

        title = (TextView) findViewById(R.id.titled);
        spinner_layout = (RelativeLayout) findViewById(R.id.spinner_layout);
        spinner_layout.setVisibility(View.GONE);
        spinner_fav = (Spinner) findViewById(R.id.spinner_fav);

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

        RadioGroup radioTujuanGroup = (RadioGroup) findViewById(R.id.rad_tujuan);
        radioTujuanGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d("chk", "id " + checkedId);
            if (checkedId == R.id.favlist_option) {
                selectedItem = "fav";
                spinner_layout.setVisibility(View.VISIBLE);
                if(spinnerLength==0){
                    spinner_fav.setEnabled(false);
                    spinner_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background_disabled));
                }else{
                    spinner_fav.setEnabled(true);
                    spinner_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                }
                tujuan.setVisibility(View.GONE);
            } else if (checkedId == R.id.manualinput_option) {
                selectedItem = "man";
                spinner_layout.setVisibility(View.GONE);
                tujuan.setVisibility(View.VISIBLE);
            }
        });

        amount = (EditText) findViewById(R.id.amount);
        pin = (EditText) findViewById(R.id.pin);

        SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString(Constants.PARAMETER_PHONENUMBER,"");

        title.setTypeface(Utility.Robot_Regular(TransferEmoneyToEmoneyActivity.this));
        handphone.setTypeface(Utility.HelveticaNeue_Medium(TransferEmoneyToEmoneyActivity.this));
        mPin.setTypeface(Utility.HelveticaNeue_Medium(TransferEmoneyToEmoneyActivity.this));
        jumlah.setTypeface(Utility.HelveticaNeue_Medium(TransferEmoneyToEmoneyActivity.this));
        Rp.setTypeface(Utility.HelveticaNeue_Medium(TransferEmoneyToEmoneyActivity.this));
        tujuan.setTypeface(Utility.Robot_Light(TransferEmoneyToEmoneyActivity.this));
        pin.setTypeface(Utility.Robot_Light(TransferEmoneyToEmoneyActivity.this));
        amount.setTypeface(Utility.Robot_Light(TransferEmoneyToEmoneyActivity.this));

        new getFavList().execute();

        spinner_fav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                selectedValue = ((TextView) v.findViewById(R.id.value_fav)).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        submit.setTypeface(Utility.Robot_Regular(TransferEmoneyToEmoneyActivity.this));
        submit.setOnClickListener(view -> {
            if (selectedItem.equals("man")) {
                if(tujuan.getText().toString().replace(" ", "").length()==0) {
                    tujuan.setError(getResources().getString(R.string.id_masukkan_no_hp));
                }else if(tujuan.getText().toString().replace(" ", "").length()<10) {
                    tujuan.setError(getResources().getString(R.string.id_no_hp_validation_msg));
                }else if(tujuan.getText().toString().replace(" ", "").length()>14) {
                    tujuan.setError(getResources().getString(R.string.id_no_hp_validation_msg));
                }else if(amount.getText().toString().replace(" ", "").length()==0) {
                    amount.setError(getResources().getString(R.string.id_jumlah_transfer_validation));
                }else if(pin.getText().toString().length()==0){
                    pin.setError(getResources().getString(R.string.id_masukkan_mpin));
                }else{
                    pinValue=func.generateRSA(pin.getText().toString());
                    destmdn = (tujuan.getText().toString().replace(" ", ""));
                    amountValue = amount.getText().toString().replace("Rp ", "");
                    new inquiryAsyncTask().execute();
                }
            } else if (selectedItem.equals("fav")) {
                if(amount.getText().toString().replace(" ", "").length()==0) {
                    amount.setError(getResources().getString(R.string.id_jumlah_transfer_validation));
                }else if(pin.getText().toString().length()==0){
                    pin.setError(getResources().getString(R.string.id_masukkan_mpin));
                }else{
                    pinValue=func.generateRSA(pin.getText().toString());
                    destmdn = selectedValue;
                    amountValue = amount.getText().toString().replace("Rp ", "");
                    new inquiryAsyncTask().execute();
                }
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

    class inquiryAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, "TransferInquiry");
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID, Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_KEY, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sourceMDN);
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, pinValue);
            mapContainer.put(Constants.PARAMETER_DEST_MDN, destmdn);
            mapContainer.put(Constants.PARAMETER_DEST_BankAccount, "");
            mapContainer.put(Constants.PARAMETER_AMOUNT, amountValue);
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_BANK_ID, "");
            mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
            mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_EMONEY);

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
                        AlertDialog.Builder alertbox;
                        switch (responseDataContainer.getMsgCode()) {
                            case "631":
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
                                break;
                            case "72":
                            case "676":
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
                                    Intent intent = new Intent(TransferEmoneyToEmoneyActivity.this, TransferEmoneyToEmoneyConfirmationActivity.class);
                                    intent.putExtra("destmdn", destmdn);
                                    intent.putExtra("transferID", transferID);
                                    intent.putExtra("sctlID", sctlID);
                                    intent.putExtra("amount", amountValue);
                                    intent.putExtra("destname", receiverAccountName);
                                    intent.putExtra("mpin", pinValue);
                                    intent.putExtra("parentTxnID", parentTxnID);
                                    intent.putExtra("mfaMode", mfaMode);
                                    intent.putExtra("selectedItem", selectedItem);
                                    startActivity(intent);
                                } else {
                                    //tanpa OTP
                                }
                                break;
                            default:
                                alertbox = new AlertDialog.Builder(TransferEmoneyToEmoneyActivity.this, R.style.MyAlertDialogStyle);
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
                                R.string.bahasa_serverNotRespond)), TransferEmoneyToEmoneyActivity.this);
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

    private class getFavList extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, Constants.CONSTANT_GENERATE_FAVORITE_JSON);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID, Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_KEY, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sourceMDN);
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, stMPIN);
            if (sharedPreferences.getInt("userType", -1) == 0) {
                stCatID = 5;
            } else if (sharedPreferences.getInt("userType", -1) == 1) {
                stCatID = 5;
            } else if (sharedPreferences.getInt("userType", -1) == 2) {
                if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                    stCatID = 7;
                } else {
                    stCatID = 5;
                }
            } else if (sharedPreferences.getInt("userType", -1) == 3) {
                stCatID = 7;
            }
            mapContainer.put(Constants.PARAMETER_FAVORITE_ID, String.valueOf(stCatID));
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, "7");

            Log.e("-----", "" + mapContainer.toString());
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
                Log.d(LOG_TAG, "response: " + response);
                JSONArray jsonarra = null;
                try {
                    jsonarra = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonarra != null) {
                    if (jsonarra.length() > 0) {
                        for (int i = 0; i < jsonarra.length(); i++) {
                            FavoriteData favData = new FavoriteData();
                            try {
                                favData.setCategoryID(jsonarra.getJSONObject(i).getString("subscriberFavoriteID"));
                                favData.setCategoryName(jsonarra.getJSONObject(i).getString("favoriteValue"));
                                favData.setFavoriteLabel(jsonarra.getJSONObject(i).getString("favoriteLabel"));
                                favList2.add(favData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        CustomSpinnerAdapter customAdapter = new CustomSpinnerAdapter(getApplicationContext(), favList2);
                        spinner_fav.setAdapter(customAdapter);
                        spinnerLength=spinner_fav.getAdapter().getCount();
                        Log.d(LOG_TAG, "spinner length: "+spinner_fav.getAdapter().getCount());
                    }
                }
            }
        }
    }
}
