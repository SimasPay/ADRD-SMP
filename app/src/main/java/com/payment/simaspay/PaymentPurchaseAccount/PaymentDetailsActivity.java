package com.payment.simaspay.PaymentPurchaseAccount;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import android.widget.Toast;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.Cash_InOut.CashOutDetailsActivity;
import com.payment.simaspay.UangkuTransfer.UangkuTransferDetailsActivity;
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


public class PaymentDetailsActivity extends AppCompatActivity {
    TextView title, product, number, pin, amount_Text, rp;
    EditText product_field, number_field, pin_field, amountField;
    Button submit;
    LinearLayout back;
    String[] strings;
    String billNumber, encryptedpinValue, amountValue, rangealert, noEntryAlert;
    SharedPreferences sharedPreferences;
    int minLimitValue=0, maxLimitValue = 0;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 11;
    static final int PICK_CONTACT = 1;
    static final int EXIT = 10;
    Functions func;
    ArrayList<FavoriteData> favList2 = new ArrayList<FavoriteData>();
    SharedPreferences settings, languageSettings;
    String selectedLanguage;
    int stCatID;
    Spinner spinner_fav;
    String sourceMDN, stMPIN, selectedItem = "man";
    String selectedValue;
    RelativeLayout spinner_layout;
    int spinnerLength=0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentdetails);

        func = new Functions(this);
        func.initiatedToolbar(this);
        getPermissionToReadUserContacts();

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");
        settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString("mobileNumber", "");
        stMPIN = func.generateRSA(sharedPreferences.getString(Constants.PARAMETER_MPIN, ""));

        title = findViewById(R.id.titled);
        product = findViewById(R.id.name_product);
        product_field = findViewById(R.id.product_field);
        number = findViewById(R.id.number);
        number_field = findViewById(R.id.number_field);
        number_field.setFocusable(true);
        number_field.setFocusableInTouchMode(true);
        number_field.requestFocus();
        if(getIntent().getExtras().getBoolean("isMDN")){
            number_field.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_contact_phone_black_24dp, 0);
            number_field.setOnTouchListener((v, event) -> {
                //final int DRAWABLE_LEFT = 0;
                //final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                //final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (number_field.getRight() - number_field.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent, PICK_CONTACT);
                        return true;
                    }
                }
                return false;
            });
        }else{
            number_field.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        pin = findViewById(R.id.mPin);
        pin_field = findViewById(R.id.pin);
        amount_Text = findViewById(R.id.mAMount);
        rp = findViewById(R.id.Rp);
        amountField = findViewById(R.id.Payment_amountentry_field);

        title.setTypeface(Utility.Robot_Regular(PaymentDetailsActivity.this));
        product.setTypeface(Utility.Robot_Regular(PaymentDetailsActivity.this));
        product_field.setTypeface(Utility.Robot_Light(PaymentDetailsActivity.this));
        number.setTypeface(Utility.Robot_Regular(PaymentDetailsActivity.this));

        number_field.setTypeface(Utility.Robot_Light(PaymentDetailsActivity.this));

        pin.setTypeface(Utility.Robot_Regular(PaymentDetailsActivity.this));
        pin_field.setTypeface(Utility.Robot_Light(PaymentDetailsActivity.this));

        amount_Text.setTypeface(Utility.Robot_Regular(PaymentDetailsActivity.this));
        rp.setTypeface(Utility.Robot_Light(PaymentDetailsActivity.this));
        amountField.setTypeface(Utility.Robot_Light(PaymentDetailsActivity.this));

        if (getIntent().getExtras().getString("PaymentMode").equalsIgnoreCase("FullAmount")) {

            amountField.setVisibility(View.VISIBLE);
            rp.setVisibility(View.VISIBLE);
            amount_Text.setVisibility(View.VISIBLE);

        } else {
            amountField.setVisibility(View.GONE);
            rp.setVisibility(View.GONE);
            amount_Text.setVisibility(View.GONE);
        }


        try {
            strings = getIntent().getExtras().getString("invoiceType").split("\\|");

            number.setText("" + strings[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        product_field.setText(getIntent().getExtras().getString("CategoryType") + " - " + getIntent().getExtras().getString("ProductName"));
        product_field.setEnabled(false);
        product_field.setClickable(false);

        back = findViewById(R.id.back_layout);

        back.setOnClickListener(view -> finish());

        spinner_layout = findViewById(R.id.spinner_layout);
        spinner_layout.setVisibility(View.GONE);
        spinner_fav = findViewById(R.id.spinner_fav);

        RadioGroup radioTujuanGroup = findViewById(R.id.rad_tujuan);
        radioTujuanGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                //Log.d("chk", "id " + checkedId);
                if (checkedId == R.id.favlist_option) {
                    selectedItem = "fav";
                    spinner_layout.setVisibility(View.VISIBLE);
                    Log.d(LOG_TAG, "spinner length: "+ spinnerLength);
                    if(spinnerLength==0){
                        spinner_fav.setEnabled(false);
                        spinner_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background_disabled));
                    }else{
                        spinner_fav.setEnabled(true);
                        spinner_layout.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                        try {
                            Field popup = Spinner.class.getDeclaredField("mPopup");
                            popup.setAccessible(true);
                            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner_fav);
                            if(spinnerLength>2){
                                popupWindow.setHeight(500);
                            }
                        }
                        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
                            Log.d(LOG_TAG, "error: "+e.toString());
                        }
                    }
                    number_field.setVisibility(View.GONE);
                    amountField.setFocusable(true);
                    amountField.requestFocus();
                } else if (checkedId == R.id.manualinput_option) {
                    selectedItem = "man";
                    spinner_layout.setVisibility(View.GONE);
                    number_field.setVisibility(View.VISIBLE);
                    number_field.setFocusable(true);
                    number_field.requestFocus();
                }
            }
        });

        //getFavoriteList
        new getFavList().execute();

        spinner_fav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                selectedValue = ((TextView) v.findViewById(R.id.value_fav)).getText().toString();
                Log.d(LOG_TAG, "selected value: "+selectedValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        submit = findViewById(R.id.submit);
        submit.setTypeface(Utility.Robot_Regular(PaymentDetailsActivity.this));
        minLimitValue = getIntent().getExtras().getInt("minLength");
        maxLimitValue = getIntent().getExtras().getInt("maxLength");

        Log.d(LOG_TAG, "minValue: "+minLimitValue+", maxValue: "+maxLimitValue);


        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLimitValue);
        number_field.setFilters(FilterArray);

        try {
            rangealert = getIntent().getExtras().getString("errormessage1");
        } catch (Exception e) {
            e.printStackTrace();
            rangealert = strings[1] + " yang Anda masukkan harus 10-16 angka.";
        }


        try {
            noEntryAlert = getIntent().getExtras().getString("errormessage");
        } catch (Exception e) {
            e.printStackTrace();
            noEntryAlert = "Harap masukkan " + strings[1] + " Anda.";
        }


        InputFilter[] FilterArray1 = new InputFilter[1];
        FilterArray1[0] = new InputFilter.LengthFilter(getResources().getInteger(R.integer.pinSize));
        pin_field.setFilters(FilterArray1);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean ada = false;
                for (FavoriteData string : favList2) {
                    ada = string.getCategoryName().equals(number_field.getText().toString());
                    Log.d(LOG_TAG, "ada : "+ada);
                    if(ada){
                        break;
                    }
                }
                if (selectedItem.equals("man")) {
                    Log.d(LOG_TAG, "value numberfield: "+number_field.getText().toString());
                    if (number_field.getText().toString().length() <= 0) {
                        Utility.displayDialog(noEntryAlert, PaymentDetailsActivity.this);
                    } else if (number_field.getText().toString().length() < minLimitValue) {
                        Utility.displayDialog(rangealert, PaymentDetailsActivity.this);
                    } else if (number_field.getText().toString().length() > maxLimitValue) {
                        Utility.displayDialog(rangealert, PaymentDetailsActivity.this);
                    } else if (ada){
                        Utility.displayDialog(getResources().getString(R.string.same_favorit), PaymentDetailsActivity.this);
                    } else {
                        billNumber = number_field.getText().toString().replace(" ", "");
                        if (getIntent().getExtras().getString("PaymentMode").equalsIgnoreCase("FullAmount")) {

                            if (amountField.getText().toString().replace(" ", "").length() == 0) {
                                Utility.displayDialog(getResources().getString(R.string.id_empty_billpaymentamount), PaymentDetailsActivity.this);
                            } else if (pin_field.getText().toString().length() <= 0) {
                                Utility.displayDialog(getResources().getString(R.string.id_empty_mpin), PaymentDetailsActivity.this);
                            } else if (pin_field.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                                Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), PaymentDetailsActivity.this);
                            } else {
                                amountValue = amountField.getText().toString().replace(" ", "");
                                String module = sharedPreferences.getString("MODULE", "NONE");
                                String exponent = sharedPreferences.getString("EXPONENT", "NONE");

                                try {
                                    encryptedpinValue = CryptoService.encryptWithPublicKey(module, exponent,
                                            pin_field.getText().toString().getBytes());
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                new BillpaymentAsynTask().execute();
                            }
                        } else {

                            if (pin_field.getText().toString().length() <= 0) {
                                Utility.displayDialog(getResources().getString(R.string.id_empty_mpin), PaymentDetailsActivity.this);
                            } else if (pin_field.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                                Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), PaymentDetailsActivity.this);
                            } else {
                                amountValue = "";
                                String module = sharedPreferences.getString("MODULE", "NONE");
                                String exponent = sharedPreferences.getString("EXPONENT", "NONE");

                                try {
                                    encryptedpinValue = CryptoService.encryptWithPublicKey(module, exponent,
                                            pin_field.getText().toString().getBytes());
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                new BillpaymentAsynTask().execute();
                            }
                        }

                    }
                } else if (selectedItem.equals("fav")) {
                    billNumber = selectedValue;
                    if (getIntent().getExtras().getString("PaymentMode").equalsIgnoreCase("FullAmount")) {
                        if(spinnerLength<=0) {
                            Utility.displayDialog(getResources().getString(R.string.input_manual_error_custom) + " " + strings[1] + " Anda", PaymentDetailsActivity.this);
                        }else if (amountField.getText().toString().replace(" ", "").length() == 0) {
                            Utility.displayDialog(getResources().getString(R.string.id_empty_billpaymentamount), PaymentDetailsActivity.this);
                        } else if (pin_field.getText().toString().length() <= 0) {
                            Utility.displayDialog(getResources().getString(R.string.id_empty_mpin), PaymentDetailsActivity.this);
                        } else if (pin_field.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                            Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), PaymentDetailsActivity.this);
                        } else {
                            amountValue = amountField.getText().toString().replace(" ", "");
                            String module = sharedPreferences.getString("MODULE", "NONE");
                            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

                            try {
                                encryptedpinValue = CryptoService.encryptWithPublicKey(module, exponent,
                                        pin_field.getText().toString().getBytes());
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            new BillpaymentAsynTask().execute();
                        }
                    } else {
                        if(spinnerLength<=0){
                            Utility.displayDialog(getResources().getString(R.string.input_manual_error_custom)+" "+strings[1]+" Anda", PaymentDetailsActivity.this);
                        }else if (pin_field.getText().toString().length() <= 0) {
                            Utility.displayDialog("Harap masukkan mPIN Anda.", PaymentDetailsActivity.this);
                        } else if (pin_field.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                            Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), PaymentDetailsActivity.this);
                        } else {
                            amountValue = "";
                            String module = sharedPreferences.getString("MODULE", "NONE");
                            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

                            try {
                                encryptedpinValue = CryptoService.encryptWithPublicKey(module, exponent,
                                        pin_field.getText().toString().getBytes());
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            new BillpaymentAsynTask().execute();
                        }
                    }
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
                            phn_no = phn_no.replace("-", "");
                            phn_no = phn_no.replace("+", "");
                            phn_no = phn_no.replace(" ", "");
                            number_field.setText(phn_no);
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

    private class BillpaymentAsynTask extends AsyncTask<Void, Void, Void> {


        String response;

        @Override
        protected Void doInBackground(Void... params) {
            /*billerCode=013001&denomCode=&amount=4000
                    */

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);

            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_BILLPAYMENT_INQUIRY);
            if (sharedPreferences.getInt("userType", -1) == 0) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BILLPAYMENT);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);

            } else if (sharedPreferences.getInt("userType", -1) == 1) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BILLPAYMENT);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
            } else if (sharedPreferences.getInt("userType", -1) == 2) {
                if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_AGENT);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                } else {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_AGENT);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                }
            } else if (sharedPreferences.getInt("userType", -1) == 3) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BILLPAYMENT);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
            }


            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, encryptedpinValue);
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION);
            mapContainer.put(Constants.PARAMETER_BILL_NO, billNumber);
            mapContainer.put(Constants.PARAMETER_PAYMENT_MODE, getIntent().getExtras().getString("PaymentMode"));
            mapContainer.put(Constants.PARAMETER_BILLER_CODE, getIntent().getExtras().getString("ProductCode"));
            mapContainer.put(Constants.PARAMETER_DENOM_CODE, "");

            mapContainer.put(Constants.PARAMETER_AMOUNT, amountValue);

            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, PaymentDetailsActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }


        ProgressDialog progressDialog;
        int msgCode;


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PaymentDetailsActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            Drawable drawable = new ProgressBar(PaymentDetailsActivity.this).getIndeterminateDrawable().mutate();
            drawable.setColorFilter(ContextCompat.getColor(PaymentDetailsActivity.this, R.color.red_sinarmas),
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
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 631) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(PaymentDetailsActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(responseContainer.getMsg());
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(PaymentDetailsActivity.this, SecondLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    alertbox.show();
                } else if (msgCode == 713) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    String responseAdditional="";
                    if(responseContainer.getAditionalInfo()==null){
                        responseAdditional="";
                    }else{
                        responseAdditional=responseContainer.getAditionalInfo();
                    }
                    Intent intent = new Intent(PaymentDetailsActivity.this, PaymentConfirmationActivity.class);
                    intent.putExtra("creditamt", responseContainer.getEncryptedDebitAmount());
                    intent.putExtra("originalAmount", responseContainer.getAmount());
                    intent.putExtra("charges", responseContainer.getEncryptedTransactionCharges());
                    intent.putExtra("transferID", responseContainer.getEncryptedTransferId());
                    intent.putExtra("parentTxnID", responseContainer.getEncryptedParentTxnId());
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("nominalamt", responseContainer.getNominalAmount());
                    intent.putExtra("additionalInfo", responseAdditional);
                    intent.putExtra("invoiceNo", responseContainer.getInvoiceNo());
                    intent.putExtra("PaymentMode", getIntent().getExtras().getString("PaymentMode"));
                    intent.putExtra("ProductCode", getIntent().getExtras().getString("ProductCode"));
                    intent.putExtra("billerDetails", getIntent().getExtras().getString("CategoryType") + " - " + getIntent().getExtras().getString("ProductName"));
                    intent.putExtra("Name", responseContainer.getCustName());
                    intent.putExtra("mfaMode", responseContainer.getMfaMode());
                    intent.putExtra("selectedItem", selectedItem);
                    try {
                        intent.putExtra("numberTitle", strings[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        intent.putExtra("numberTitle", "");
                    }


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
                                PaymentDetailsActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), PaymentDetailsActivity.this);
                    }
                }
            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), PaymentDetailsActivity.this);
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
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, stMPIN);
            if (sharedPreferences.getInt("userType", -1) == 0) {
                stCatID = 3;
            } else if (sharedPreferences.getInt("userType", -1) == 1) {
                stCatID = 3;
            } else if (sharedPreferences.getInt("userType", -1) == 2) {
                if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                    stCatID = 9;
                } else {
                    stCatID = 3;
                }
            } else if (sharedPreferences.getInt("userType", -1) == 3) {
                stCatID = 9;
            }
            mapContainer.put(Constants.PARAMETER_FAVORITE_ID, String.valueOf(stCatID));
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, "7");

            Log.e("-----", "" + mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    PaymentDetailsActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PaymentDetailsActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            Drawable drawable = new ProgressBar(PaymentDetailsActivity.this).getIndeterminateDrawable().mutate();
            drawable.setColorFilter(ContextCompat.getColor(PaymentDetailsActivity.this, R.color.red_sinarmas),
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
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(PaymentDetailsActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage("Please login again");
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(PaymentDetailsActivity.this, SecondLoginActivity.class);
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
                                    if(jsonarra.getJSONObject(i).getString("favoriteCode").equals(getIntent().getExtras().getString("ProductCode"))){
                                        favData.setCategoryID(jsonarra.getJSONObject(i).getString("subscriberFavoriteID"));
                                        favData.setCategoryName(jsonarra.getJSONObject(i).getString("favoriteValue"));
                                        favData.setFavoriteLabel(jsonarra.getJSONObject(i).getString("favoriteLabel"));
                                        favList2.add(favData);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            CustomSpinnerAdapter customAdapter = new CustomSpinnerAdapter(getApplicationContext(), favList2);
                            spinner_fav.setAdapter(customAdapter);
                            spinnerLength=spinner_fav.getAdapter().getCount();
                            Log.d(LOG_TAG, "spinner length: "+spinner_fav.getAdapter().getCount());
                        }
                    }else{
                        Log.d(LOG_TAG, "fav list: NULL");
                    }
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(number_field.getWindowToken(), 0);
    }

    @Override
    public void onStop() {
        super.onStop();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(number_field.getWindowToken(), 0);
    }
}
