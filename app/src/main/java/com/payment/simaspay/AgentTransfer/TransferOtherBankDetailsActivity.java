package com.payment.simaspay.AgentTransfer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.utils.CustomSpinnerAdapter;
import com.payment.simaspay.utils.FavoriteData;
import com.payment.simaspay.utils.Functions;
import com.payment.simaspay.constants.EncryptedResponseDataContainer;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.payment.simaspay.R;


public class TransferOtherBankDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView title, handphone, jumlah, mPin, bankName,Rp;
    Button submit;
    EditText number, amount, pin, bankName_editfield;
    LinearLayout btnBacke;
    private static final String LOG_TAG = "SimasPay";
    Functions func;
    ProgressDialog progressDialog;
    int msgCode, stCatID;
    Spinner spinner_fav;
    String selectedItem="man";
    String selectedValue;
    ArrayList<FavoriteData> favList2 = new ArrayList<FavoriteData>();
    SharedPreferences sharedPreferences;
    String pinValue, mdn, amountValue;
    String sourceMDN, stMPIN;
    SharedPreferences settings, languageSettings;
    String selectedLanguage;
    RelativeLayout spinner_layout;
    int spinnerLength=0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otherbankdetails);
        func = new Functions(this);
        func.initiatedToolbar(this);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");
        settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString("mobileNumber", "");
        stMPIN = func.generateRSA(sharedPreferences.getString(Constants.PARAMETER_MPIN, ""));

        title = (TextView) findViewById(R.id.titled);
        handphone = (TextView) findViewById(R.id.handphone);
        jumlah = (TextView) findViewById(R.id.jumlah);
        mPin = (TextView) findViewById(R.id.mPin);
        bankName = (TextView) findViewById(R.id.bankName_textView);
        Rp = (TextView) findViewById(R.id.Rp);
        submit = (Button) findViewById(R.id.submit);

        number = (EditText) findViewById(R.id.number);
        amount = (EditText) findViewById(R.id.amount);
        pin = (EditText) findViewById(R.id.pin);
        bankName_editfield = (EditText) findViewById(R.id.BankName_fiels);

        btnBacke = (LinearLayout) findViewById(R.id.back_layout);

        title.setTypeface(Utility.Robot_Regular(TransferOtherBankDetailsActivity.this));
        handphone.setTypeface(Utility.HelveticaNeue_Medium(TransferOtherBankDetailsActivity.this));
        jumlah.setTypeface(Utility.HelveticaNeue_Medium(TransferOtherBankDetailsActivity.this));
        mPin.setTypeface(Utility.HelveticaNeue_Medium(TransferOtherBankDetailsActivity.this));
        bankName.setTypeface(Utility.HelveticaNeue_Medium(TransferOtherBankDetailsActivity.this));

        submit.setTypeface(Utility.Robot_Regular(TransferOtherBankDetailsActivity.this));
        number.setTypeface(Utility.Robot_Light(TransferOtherBankDetailsActivity.this));
        amount.setTypeface(Utility.Robot_Light(TransferOtherBankDetailsActivity.this));
        pin.setTypeface(Utility.Robot_Light(TransferOtherBankDetailsActivity.this));
        Rp.setTypeface(Utility.Robot_Light(TransferOtherBankDetailsActivity.this));
        bankName_editfield.setTypeface(Utility.Robot_Light(TransferOtherBankDetailsActivity.this));

        bankName_editfield.setText(getIntent().getExtras().getString("BankName"));

        bankName_editfield.setClickable(false);
        bankName_editfield.setFocusable(false);

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
        spinner_fav.setOnItemSelectedListener(this);

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
                amount.setFocusable(true);
                amount.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(amount, InputMethodManager.SHOW_IMPLICIT);
            } else if (checkedId == R.id.manualinput_option) {
                selectedItem = "man";
                spinner_layout.setVisibility(View.GONE);
                number.setVisibility(View.VISIBLE);
                number.setFocusable(true);
                number.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(number, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(25);
        number.setFilters(FilterArray);

        InputFilter[] FilterArray1 = new InputFilter[1];
        FilterArray1[0] = new InputFilter.LengthFilter(getResources().getInteger(R.integer.pinSize));
        pin.setFilters(FilterArray1);

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
                    Utility.displayDialog(getResources().getString(R.string.empty_no_rek), TransferOtherBankDetailsActivity.this);
                } else if (number.getText().toString().replace(" ", "").length() < 8) {
                    Utility.displayDialog(getResources().getString(R.string.digit_no_rek), TransferOtherBankDetailsActivity.this);
                } else if (number.getText().toString().replace(" ", "").length() > 25) {
                    Utility.displayDialog(getResources().getString(R.string.digit_no_rek), TransferOtherBankDetailsActivity.this);
                } else if (ada){
                    Utility.displayDialog(getResources().getString(R.string.same_favorit), TransferOtherBankDetailsActivity.this);
                } else if (amount.getText().toString().replace("Rp ", "").length() <= 0) {
                    Utility.displayDialog(getResources().getString(R.string.empty_amount), TransferOtherBankDetailsActivity.this);
                } else if (pin.getText().toString().length() <= 0) {
                    Utility.displayDialog(getResources().getString(R.string.id_empty_mpin), TransferOtherBankDetailsActivity.this);
                }else if (pin.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                    Utility.displayDialog(getResources().getString(R.string.id_invalid_mpin), TransferOtherBankDetailsActivity.this);
                } else {
                    pinValue = func.generateRSA(pin.getText().toString());
                    Log.d(LOG_TAG, "pinValue:"+pinValue);
                    mdn = (number.getText().toString().replace(" ", ""));
                    amountValue = amount.getText().toString().replace("Rp ", "");
                    new transferOtherBankAsynTask().execute();
                }
            } else if (selectedItem.equals("fav")) {
                if(spinnerLength<=0){
                    Utility.displayDialog(getResources().getString(R.string.input_manual_error), TransferOtherBankDetailsActivity.this);
                }else if (amount.getText().toString().replace("Rp ", "").length() <= 0) {
                    Utility.displayDialog(getResources().getString(R.string.empty_amount), TransferOtherBankDetailsActivity.this);
                } else if (pin.getText().toString().length() <= 0) {
                    Utility.displayDialog(getResources().getString(R.string.id_masukkan_mpin), TransferOtherBankDetailsActivity.this);
                }else if (pin.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                    Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), TransferOtherBankDetailsActivity.this);
                } else {
                    pinValue = func.generateRSA(pin.getText().toString());
                    Log.d(LOG_TAG, "pinValue:"+pinValue);
                    mdn = selectedValue;
                    amountValue = amount.getText().toString().replace("Rp ", "");
                    new transferOtherBankAsynTask().execute();
                }
            }
        });
        btnBacke.setOnClickListener(view -> finish());
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedValue = ((TextView) view.findViewById(R.id.value_fav)).getText().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class transferOtherBankAsynTask extends AsyncTask<Void, Void, Void> {
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_INTERBANK_TRANSFER_INQUIRY);
            mapContainer.put(Constants.PARAMETER_BANK_ID, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString(Constants.PARAMETER_PHONENUMBER, ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, pinValue);
            mapContainer.put(Constants.PARAMETER_AMOUNT, amountValue);
            mapContainer.put(Constants.PARAMETER_DEST_ACCOUNT_NO, mdn);
            if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_BANK_INT) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, getIntent().getExtras().getString("BankCode"));
            } else if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_BANKSINARMAS_INT) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
                mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, getIntent().getExtras().getString("BankCode"));
            } else if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_LAKUPANDAI_INT) {
                if (sharedPreferences.getInt(Constants.PARAMETER_AGENTTYPE, -1) == Constants.CONSTANT_EMONEY_INT) {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                    mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, getIntent().getExtras().getString("BankCode"));
                } else {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, getIntent().getExtras().getString("BankCode"));
                }
            }else if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_EMONEY_INT){
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                mapContainer.put(Constants.PARAMETER_DEST_POCKET_CODE, Constants.POCKET_CODE_BANK);
                mapContainer.put(Constants.PARAMETER_DEST_BANK_CODE, getIntent().getExtras().getString("BankCode"));
            }
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, TransferOtherBankDetailsActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(TransferOtherBankDetailsActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            Drawable drawable = new ProgressBar(TransferOtherBankDetailsActivity.this).getIndeterminateDrawable().mutate();
            drawable.setColorFilter(ContextCompat.getColor(TransferOtherBankDetailsActivity.this, R.color.red_sinarmas),
                    PorterDuff.Mode.SRC_IN);
            progressDialog.setIndeterminateDrawable(drawable);
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
                    msgCode = Integer.parseInt(responseContainer.getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 631) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(TransferOtherBankDetailsActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(responseContainer.getMsg());
                    alertbox.setNeutralButton("OK", (arg0, arg1) -> {
                        Intent intent = new Intent(TransferOtherBankDetailsActivity.this, SecondLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(intent, 40);
                    });
                    alertbox.show();
                } else if (msgCode == 72) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    sharedPreferences.edit().putString("password", pinValue).apply();
                    Intent intent = new Intent(TransferOtherBankDetailsActivity.this, TransferOtherbankConfirmationActivity.class);
                    intent.putExtra("amount", responseContainer.getEncryptedDebitAmount());
                    intent.putExtra("originalamount", responseContainer.getAmount());
                    intent.putExtra("charges", responseContainer.getEncryptedTransactionCharges());
                    intent.putExtra("DestMDN", responseContainer.getAccountNumber());
                    intent.putExtra("transferID", responseContainer.getEncryptedTransferId());
                    intent.putExtra("ParentId", responseContainer.getEncryptedParentTxnId());
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("Name", responseContainer.getCustName());
                    intent.putExtra("BankName", responseContainer.getDestBank());
                    intent.putExtra("BankCode", getIntent().getExtras().getString("BankCode"));
                    intent.putExtra("mfaMode", responseContainer.getMfaMode());
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
                                TransferOtherBankDetailsActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), TransferOtherBankDetailsActivity.this);
                    }
                }
            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), TransferOtherBankDetailsActivity.this);
            }
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
                stCatID = 4;
            } else if (sharedPreferences.getInt("userType", -1) == 1) {
                stCatID = 4;
            } else if (sharedPreferences.getInt("userType", -1) == 2) {
                if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                    stCatID = 10;
                } else {
                    stCatID = 4;
                }
            } else if (sharedPreferences.getInt("userType", -1) == 3) {
                stCatID = 10;
            }
            mapContainer.put(Constants.PARAMETER_FAVORITE_ID, String.valueOf(stCatID));
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, "7");

            Log.e("-----", "" + mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    TransferOtherBankDetailsActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(TransferOtherBankDetailsActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            Drawable drawable = new ProgressBar(TransferOtherBankDetailsActivity.this).getIndeterminateDrawable().mutate();
            drawable.setColorFilter(ContextCompat.getColor(TransferOtherBankDetailsActivity.this, R.color.red_sinarmas),
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
                if (response.contains("631")) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(TransferOtherBankDetailsActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage("Please login again");
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(TransferOtherBankDetailsActivity.this, SecondLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    alertbox.show();
                } else {
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
