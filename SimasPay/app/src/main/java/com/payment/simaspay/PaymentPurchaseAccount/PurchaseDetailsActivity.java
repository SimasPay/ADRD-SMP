package com.payment.simaspay.PaymentPurchaseAccount;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mfino.handset.security.CryptoService;
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

import static com.payment.simaspay.services.Constants.LOG_TAG;

public class PurchaseDetailsActivity extends AppCompatActivity {
    TextView title, pulsa_field, product, number, pin, Rp;
    EditText product_field, number_field, pin_field, plnamount_entryfield;
    Button submit, nominal_pulsa;
    private static final String LOG_TAG = "SimasPay";
    LinearLayout back;
    String encryptedpinValue, billNumber, amountString;
    SharedPreferences sharedPreferences;
    LinearLayout manualEnterLayout;
    String[] strings;
    String rangealert, noEntryAlert;
    int maxLimitValue = 0;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 11;
    static final int PICK_CONTACT = 1;
    static final int EXIT = 10;
    Functions func;
    ArrayList<FavoriteData> favList2 = new ArrayList<FavoriteData>();
    SharedPreferences settings, languageSettings;
    String selectedLanguage;
    int msgCode, stCatID;
    Spinner spinner_fav;
    String sourceMDN, stMPIN, selectedItem = "man";
    String selectedValue;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchasedetails);

        func = new Functions(this);
        func.initiatedToolbar(this);
        getPermissionToReadUserContacts();

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");
        settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString("mobileNumber", "");
        stMPIN = func.generateRSA(sharedPreferences.getString(Constants.PARAMETER_MPIN, ""));

        title = (TextView) findViewById(R.id.titled);
        Rp = (TextView) findViewById(R.id.Rp);
        product = (TextView) findViewById(R.id.name_product);
        product_field = (EditText) findViewById(R.id.product_field);
        number = (TextView) findViewById(R.id.number);
        number_field = (EditText) findViewById(R.id.number_field);
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
        pin = (TextView) findViewById(R.id.mPin);
        pin_field = (EditText) findViewById(R.id.pin);
        plnamount_entryfield = (EditText) findViewById(R.id.pln_amountentry_field);
        manualEnterLayout = (LinearLayout) findViewById(R.id.manualEnterLayout);
        strings = getIntent().getExtras().getString("invoiceType").split("\\|");
        number.setText("" + strings[1]);
        try {
            maxLimitValue = getIntent().getExtras().getInt("maxLength");
        } catch (Exception e) {
            e.printStackTrace();
            maxLimitValue = 0;
        }
        if (maxLimitValue == 0) {
            maxLimitValue = 16;
        }

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLimitValue);
        number_field.setFilters(FilterArray);
        InputFilter[] FilterArray1 = new InputFilter[1];
        FilterArray1[0] = new InputFilter.LengthFilter(getResources().getInteger(R.integer.pinSize));
        pin_field.setFilters(FilterArray1);

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

        pulsa_field = (TextView) findViewById(R.id.pulsa);

        nominal_pulsa = (Button) findViewById(R.id.pulsa_field);

        pulsa_field.setText(getIntent().getExtras().getString("NominalType"));

        product_field.setText(getIntent().getExtras().getString("CategoryType") + " - " + getIntent().getExtras().getString("ProductName"));

        if (!getIntent().getExtras().getString("DenomValues").equalsIgnoreCase("")) {
            manualEnterLayout.setVisibility(View.GONE);
            nominal_pulsa.setVisibility(View.VISIBLE);
            String sampleString = getIntent().getExtras().getString("DenomValues");
            String[] items = sampleString.split("\\|");
            for (int i = 0; i < items.length; i++) {
                Log.e("------", "=======" + items[i]);
                arrayList.add(items[i]);
            }
        } else {
            manualEnterLayout.setVisibility(View.VISIBLE);
            nominal_pulsa.setVisibility(View.GONE);
        }


        nominal_pulsa.setOnClickListener(v -> {
            if (arrayList.size() > 0) {
                WorkDisplay();
            }
        });


        title.setTypeface(Utility.Robot_Regular(PurchaseDetailsActivity.this));
        product.setTypeface(Utility.Robot_Regular(PurchaseDetailsActivity.this));
        product_field.setTypeface(Utility.Robot_Light(PurchaseDetailsActivity.this));
        number.setTypeface(Utility.Robot_Regular(PurchaseDetailsActivity.this));
        number_field.setTypeface(Utility.Robot_Light(PurchaseDetailsActivity.this));
        pin.setTypeface(Utility.Robot_Regular(PurchaseDetailsActivity.this));
        pin_field.setTypeface(Utility.Robot_Light(PurchaseDetailsActivity.this));
        pulsa_field.setTypeface(Utility.Robot_Regular(PurchaseDetailsActivity.this));
        nominal_pulsa.setTypeface(Utility.Robot_Light(PurchaseDetailsActivity.this));

        plnamount_entryfield.setTypeface(Utility.Robot_Light(PurchaseDetailsActivity.this));
        Rp.setTypeface(Utility.Robot_Light(PurchaseDetailsActivity.this));

        product_field.setText(getIntent().getExtras().getString("CategoryType") + " - " + getIntent().getExtras().getString("ProductName"));
        product_field.setEnabled(false);
        product_field.setClickable(false);
        title.setText("Pembelian");
        back = (LinearLayout) findViewById(R.id.back_layout);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        submit = (Button) findViewById(R.id.submit);
        submit.setTypeface(Utility.Robot_Regular(PurchaseDetailsActivity.this));

        RelativeLayout spinner_layout = (RelativeLayout) findViewById(R.id.spinner_layout);
        spinner_layout.setVisibility(View.GONE);
        spinner_fav = (Spinner) findViewById(R.id.spinner_fav);

        RadioGroup radioTujuanGroup = (RadioGroup) findViewById(R.id.rad_tujuan);
        radioTujuanGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d("chk", "id " + checkedId);
            if (checkedId == R.id.favlist_option) {
                selectedItem = "fav";
                spinner_layout.setVisibility(View.VISIBLE);
                number_field.setVisibility(View.GONE);
            } else if (checkedId == R.id.manualinput_option) {
                selectedItem = "man";
                spinner_layout.setVisibility(View.GONE);
                number_field.setVisibility(View.VISIBLE);
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
            if (selectedItem.equals("man")) {
                if (nominal_pulsa.getText().length() <= 0 && nominal_pulsa.isShown()) {
                    Utility.displayDialog("Masukkan " + getIntent().getExtras().getString("NominalType"), PurchaseDetailsActivity.this);
                } else if (plnamount_entryfield.getText().toString().length() <= 0 && plnamount_entryfield.isShown()) {
                    Utility.displayDialog("Masukkan Nominal PLN Pulsa", PurchaseDetailsActivity.this);
                } else if (number_field.getText().toString().length() <= 0) {
                    Utility.displayDialog(noEntryAlert, PurchaseDetailsActivity.this);
                } else if (number_field.getText().toString().length() < 10) {
                    Utility.displayDialog(rangealert, PurchaseDetailsActivity.this);
                } else if (number_field.getText().toString().length() > maxLimitValue) {
                    Utility.displayDialog(rangealert, PurchaseDetailsActivity.this);
                } else {
                    billNumber = number_field.getText().toString().replace(" ", "");
                    if (getIntent().getExtras().getString("isPlnprepaid").equalsIgnoreCase("true")) {
                        if (plnamount_entryfield.getText().toString().replace("Rp ", "").length() == 0) {
                            Utility.displayDialog("Silahkan masukkan nominal pembelian", PurchaseDetailsActivity.this);
                        } else if (pin_field.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                            Utility.displayDialog("Harap masukkan mPIN Anda.", PurchaseDetailsActivity.this);
                        } else if (pin_field.getText().toString().length() < 6) {
                            Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), PurchaseDetailsActivity.this);
                        } else {
                            amountString = plnamount_entryfield.getText().toString().replace("Rp ", "");
                            String module = sharedPreferences.getString("MODULE", "NONE");
                            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

                            try {
                                encryptedpinValue = CryptoService.encryptWithPublicKey(module, exponent,
                                        pin_field.getText().toString().getBytes());
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            new PurchaseAccountAsynTask().execute();
                        }
                    } else {
                        if (pin_field.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                            Utility.displayDialog("Harap masukkan mPIN Anda.", PurchaseDetailsActivity.this);
                        } else if (pin_field.getText().toString().length() < 6) {
                            Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), PurchaseDetailsActivity.this);
                        } else {
                            amountString = nominal_pulsa.getText().toString().replace("Rp. ", "");
                            String module = sharedPreferences.getString("MODULE", "NONE");
                            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

                            try {
                                encryptedpinValue = CryptoService.encryptWithPublicKey(module, exponent,
                                        pin_field.getText().toString().getBytes());
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            new PurchaseAccountAsynTask().execute();
                        }
                    }
                }
            } else if (selectedItem.equals("fav")) {
                billNumber = selectedValue;
                if (getIntent().getExtras().getString("isPlnprepaid").equalsIgnoreCase("true")) {
                    if (plnamount_entryfield.getText().toString().replace("Rp ", "").length() == 0) {
                        Utility.displayDialog("Silahkan masukkan nominal pembelian", PurchaseDetailsActivity.this);
                    } else if (pin_field.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                        Utility.displayDialog("Harap masukkan mPIN Anda.", PurchaseDetailsActivity.this);
                    } else if (pin_field.getText().toString().length() < 6) {
                        Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), PurchaseDetailsActivity.this);
                    } else {
                        amountString = plnamount_entryfield.getText().toString().replace("Rp ", "");
                        String module = sharedPreferences.getString("MODULE", "NONE");
                        String exponent = sharedPreferences.getString("EXPONENT", "NONE");

                        try {
                            encryptedpinValue = CryptoService.encryptWithPublicKey(module, exponent,
                                    pin_field.getText().toString().getBytes());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        new PurchaseAccountAsynTask().execute();
                    }
                } else {
                    if (pin_field.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                        Utility.displayDialog("Harap masukkan mPIN Anda.", PurchaseDetailsActivity.this);
                    } else if (pin_field.getText().toString().length() < 6) {
                        Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), PurchaseDetailsActivity.this);
                    } else {
                        amountString = nominal_pulsa.getText().toString().replace("Rp. ", "");
                        String module = sharedPreferences.getString("MODULE", "NONE");
                        String exponent = sharedPreferences.getString("EXPONENT", "NONE");

                        try {
                            encryptedpinValue = CryptoService.encryptWithPublicKey(module, exponent,
                                    pin_field.getText().toString().getBytes());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        new PurchaseAccountAsynTask().execute();
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


    private class PurchaseAccountAsynTask extends AsyncTask<Void, Void, Void> {
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);

            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_AIRTIME_PURCHASE_INQUIRY);
            if (sharedPreferences.getInt("userType", -1) == 0) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BUY);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
            } else if (sharedPreferences.getInt("userType", -1) == 1) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BUY);
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
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BUY);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
            }


            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, encryptedpinValue);

            mapContainer.put(Constants.PARAMETER_BILL_NO, billNumber);
            mapContainer.put(Constants.PARAMETER_PAYMENT_MODE, getIntent().getExtras().getString("PaymentMode"));
            mapContainer.put(Constants.PARAMETER_BILLER_CODE, getIntent().getExtras().getString("ProductCode"));
            mapContainer.put(Constants.PARAMETER_DENOM_CODE, Constants.CONSTANT_DENOMCODE);
            mapContainer.put(Constants.PARAMETER_AMOUNT, amountString);
            mapContainer.put("nominalAmount", amountString);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, PurchaseDetailsActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }


        ProgressDialog progressDialog;
        int msgCode;


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PurchaseDetailsActivity.this);
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
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(PurchaseDetailsActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(responseContainer.getMsg());
                    alertbox.setNeutralButton("OK", (arg0, arg1) -> {
                        Intent intent = new Intent(PurchaseDetailsActivity.this, SecondLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    });
                    alertbox.show();
                } else if (msgCode == 660) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(PurchaseDetailsActivity.this, PurchaseConfirmationActivity.class);
                    intent.putExtra("debitamt", responseContainer.getEncryptedDebitAmount());
                    intent.putExtra("originalAmount", responseContainer.getAmount());
                    intent.putExtra("charges", responseContainer.getEncryptedTransactionCharges());
                    intent.putExtra("transferID", responseContainer.getEncryptedTransferId());
                    intent.putExtra("parentTxnID", responseContainer.getEncryptedParentTxnId());
                    intent.putExtra("sctlID", responseContainer.getSctl());
                    intent.putExtra("nominalamt", responseContainer.getNominalAmount());
                    intent.putExtra("invoiceNo", responseContainer.getInvoiceNo());
                    intent.putExtra("PaymentMode", getIntent().getExtras().getString("PaymentMode"));
                    intent.putExtra("ProductCode", getIntent().getExtras().getString("ProductCode"));
                    intent.putExtra("billerDetails", getIntent().getExtras().getString("CategoryType") + " - " + getIntent().getExtras().getString("ProductName"));
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
                                PurchaseDetailsActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), PurchaseDetailsActivity.this);
                    }
                }
            } else {
                Log.d(LOG_TAG, "response: " + null + ", -----" + response);
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), PurchaseDetailsActivity.this);
            }
        }
    }

    Dialog dialogCustomWish;
    int selected_region = -1;
    ProductsAdapter productsAdapter;

    public void WorkDisplay() {
        dialogCustomWish = new Dialog(PurchaseDetailsActivity.this);
        dialogCustomWish.setCancelable(false);
        dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogCustomWish.setContentView(R.layout.province_list);

        Button button = (Button) dialogCustomWish.findViewById(R.id.ok);
        Button button1 = (Button) dialogCustomWish.findViewById(R.id.Cancel);
        TextView textView = (TextView) dialogCustomWish.findViewById(R.id.title);
        ListView listView = (ListView) dialogCustomWish.findViewById(R.id.locationsList);
        button.setTypeface(Utility.RegularTextFormat(PurchaseDetailsActivity.this));
        button1.setTypeface(Utility.RegularTextFormat(PurchaseDetailsActivity.this));
        textView.setTypeface(Utility.RegularTextFormat(PurchaseDetailsActivity.this));
        textView.setText("Pilih");

        productsAdapter = new ProductsAdapter();
        listView.setAdapter(productsAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {

            selected_region = position;
            productsAdapter.notifyDataSetChanged();
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_region = -1;
                dialogCustomWish.dismiss();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCustomWish.dismiss();

                if (selected_region != -1) {
                    nominal_pulsa.setText("Rp. " + arrayList.get(selected_region).toString());
                }

            }
        });
        dialogCustomWish.show();


    }

    ArrayList<String> arrayList = new ArrayList<>();

    class ProductsAdapter extends BaseAdapter {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(PurchaseDetailsActivity.this).inflate(R.layout.location_row, null);
            TextView textView = (TextView) view.findViewById(R.id.location_text);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_location);

            if (selected_region == position) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.selected));
            } else {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
            }

//            imageView.setVisibility(View.GONE);

            textView.setText("Rp. " + arrayList.get(position).toString());

            return view;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
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
                stCatID = 2;
            } else if (sharedPreferences.getInt("userType", -1) == 1) {
                stCatID = 2;
            } else if (sharedPreferences.getInt("userType", -1) == 2) {
                if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                    stCatID = 8;
                } else {
                    stCatID = 2;
                }
            } else if (sharedPreferences.getInt("userType", -1) == 3) {
                stCatID = 8;
            }
            mapContainer.put(Constants.PARAMETER_FAVORITE_ID, String.valueOf(stCatID));
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, "7");

            Log.e("-----", "" + mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    PurchaseDetailsActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PurchaseDetailsActivity.this);
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
                    }
                }
            }
        }
    }
}
