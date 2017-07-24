package com.payment.simaspay.UangkuTransfer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.payment.simaspay.AgentTransfer.TransferDetailsActivity;
import com.payment.simaspay.AgentTransfer.TransferEmoneyToEmoneyActivity;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

import static com.payment.simaspay.services.Constants.LOG_TAG;

public class UangkuTransferDetailsActivity extends AppCompatActivity {
    TextView title, handphone, jumlah, mPin,Rp;
    Button submit;
    EditText number, amount, pin;
    LinearLayout btnBacke;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    String pinValue, amountValue, mdn;
    Functions func;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 11;
    static final int PICK_CONTACT=1;
    static final int EXIT=10;
    SharedPreferences settings, languageSettings;
    String selectedLanguage;
    int msgCode, stCatID;
    Spinner spinner_fav;
    String sourceMDN, stMPIN, selectedItem="man", response, selectedValue;
    ArrayList<FavoriteData> favList2 = new ArrayList<FavoriteData>();
    RelativeLayout spinner_layout;
    int spinnerLength=0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transferdetails);

        func = new Functions(this);
        func.initiatedToolbar(this);
        getPermissionToReadUserContacts();

        title = (TextView) findViewById(R.id.titled);
        handphone = (TextView) findViewById(R.id.handphone);
        jumlah = (TextView) findViewById(R.id.jumlah);
        mPin = (TextView) findViewById(R.id.mPin);
        Rp=(TextView) findViewById(R.id.Rp);

        title.setText("Transfer - Uangku");

        handphone.setText(getResources().getString(R.string.nohptujuan));

        submit = (Button) findViewById(R.id.submit);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");
        settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString("mobileNumber", "");
        stMPIN = func.generateRSA(sharedPreferences.getString(Constants.PARAMETER_MPIN, ""));

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

        spinner_layout = (RelativeLayout) findViewById(R.id.spinner_layout);
        spinner_layout.setVisibility(View.GONE);
        spinner_fav = (Spinner) findViewById(R.id.spinner_fav);
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner_fav);
            popupWindow.setHeight(500);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            Log.d(LOG_TAG, "error: "+e.toString());
        }

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
                number.setVisibility(View.GONE);
            } else if (checkedId == R.id.manualinput_option) {
                selectedItem = "man";
                spinner_layout.setVisibility(View.GONE);
                number.setVisibility(View.VISIBLE);
            }
        });

        //getFavoriteList
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


        submit.setOnClickListener(view -> {
            Boolean ada = false;
            for (FavoriteData string : favList2) {
                ada = string.getCategoryName().equals(number.getText().toString());
                Log.d(LOG_TAG, "ada : "+ada);
                if(ada){
                    break;
                }
            }
            if (selectedItem.equals("man")) {
                if (number.getText().toString().replace(" ", "").length() <= 0) {
                    Utility.displayDialog(getResources().getString(R.string.id_masukkan_no_hp), UangkuTransferDetailsActivity.this);
                } else if (number.getText().toString().replace(" ", "").length() < 10) {
                    Utility.displayDialog(getResources().getString(R.string.id_no_hp_validation_msg), UangkuTransferDetailsActivity.this);
                } else if (number.getText().toString().replace(" ", "").length() > 14) {
                    Utility.displayDialog(getResources().getString(R.string.id_no_hp_validation_msg), UangkuTransferDetailsActivity.this);
                } else if (ada){
                    Utility.displayDialog(getResources().getString(R.string.same_favorit), UangkuTransferDetailsActivity.this);
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
            } else if (selectedItem.equals("fav")) {
                if(spinnerLength<=0){
                    Utility.displayDialog(getResources().getString(R.string.input_manualhp_error), UangkuTransferDetailsActivity.this);
                }else if (amount.getText().toString().replace("Rp ", "").length() <= 0) {
                    Utility.displayDialog(getResources().getString(R.string.id_jumlah_transfer_validation), UangkuTransferDetailsActivity.this);
                } else if (pin.getText().toString().length() <= 0) {
                    Utility.displayDialog(getResources().getString(R.string.id_masukkan_mpin), UangkuTransferDetailsActivity.this);
                }else if (pin.getText().toString().length() <getResources().getInteger(R.integer.pinSize)) {
                    Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), UangkuTransferDetailsActivity.this);
                } else {
                    pinValue=func.generateRSA(pin.getText().toString());
                    mdn = selectedValue;
                    amountValue = amount.getText().toString().replace("Rp ", "");
                    new UangkuTransferAsynTask().execute();
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
            Drawable drawable = new ProgressBar(UangkuTransferDetailsActivity.this).getIndeterminateDrawable().mutate();
            drawable.setColorFilter(ContextCompat.getColor(UangkuTransferDetailsActivity.this, R.color.red_sinarmas),
                    PorterDuff.Mode.SRC_IN);
            progressDialog.setIndeterminateDrawable(drawable);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
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
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
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
                    intent.putExtra("selectedItem", selectedItem);
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
                stCatID = 6;
            } else if (sharedPreferences.getInt("userType", -1) == 1) {
                stCatID = 6;
            } else if (sharedPreferences.getInt("userType", -1) == 2) {
                if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                    stCatID = 12;
                } else {
                    stCatID = 6;
                }
            } else if (sharedPreferences.getInt("userType", -1) == 3) {
                stCatID = 12;
            }
            mapContainer.put(Constants.PARAMETER_FAVORITE_ID, String.valueOf(stCatID));
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, "7");

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
            Drawable drawable = new ProgressBar(UangkuTransferDetailsActivity.this).getIndeterminateDrawable().mutate();
            drawable.setColorFilter(ContextCompat.getColor(UangkuTransferDetailsActivity.this, R.color.red_sinarmas),
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
                Log.d(LOG_TAG, "response: " + response);
                if(response.contains("631")){
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(UangkuTransferDetailsActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage("Please login again");
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(UangkuTransferDetailsActivity.this, SecondLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    alertbox.show();
                }else{
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
}

