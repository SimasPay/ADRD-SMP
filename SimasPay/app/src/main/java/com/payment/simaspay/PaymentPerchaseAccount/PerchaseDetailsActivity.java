package com.payment.simaspay.PaymentPerchaseAccount;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.AgentTransfer.TransferEmoneyToEmoneyConfirmationActivity;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.userdetails.SessionTimeOutActivity;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/29/2016.
 */
public class PerchaseDetailsActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchasedetails);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        title = (TextView) findViewById(R.id.titled);


        Rp = (TextView) findViewById(R.id.Rp);

        product = (TextView) findViewById(R.id.name_product);
        product_field = (EditText) findViewById(R.id.product_field);
        number = (TextView) findViewById(R.id.number);
        number_field = (EditText) findViewById(R.id.number_field);
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


        nominal_pulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.size() > 0) {
                    WorkDisplay();
                }
            }
        });


        title.setTypeface(Utility.Robot_Regular(PerchaseDetailsActivity.this));
        product.setTypeface(Utility.Robot_Regular(PerchaseDetailsActivity.this));
        product_field.setTypeface(Utility.Robot_Light(PerchaseDetailsActivity.this));
        number.setTypeface(Utility.Robot_Regular(PerchaseDetailsActivity.this));
        number_field.setTypeface(Utility.Robot_Light(PerchaseDetailsActivity.this));
        pin.setTypeface(Utility.Robot_Regular(PerchaseDetailsActivity.this));
        pin_field.setTypeface(Utility.Robot_Light(PerchaseDetailsActivity.this));
        pulsa_field.setTypeface(Utility.Robot_Regular(PerchaseDetailsActivity.this));
        nominal_pulsa.setTypeface(Utility.Robot_Light(PerchaseDetailsActivity.this));

        plnamount_entryfield.setTypeface(Utility.Robot_Light(PerchaseDetailsActivity.this));
        Rp.setTypeface(Utility.Robot_Light(PerchaseDetailsActivity.this));

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


        submit.setTypeface(Utility.Robot_Regular(PerchaseDetailsActivity.this));


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nominal_pulsa.getText().length() <= 0 && nominal_pulsa.isShown()) {
                    Utility.displayDialog("Masukkan Nominal Pulsa", PerchaseDetailsActivity.this);
                } else if (plnamount_entryfield.getText().toString().length() <= 0 && plnamount_entryfield.isShown()) {
                    Utility.displayDialog("Masukkan Nominal PLN Pulsa", PerchaseDetailsActivity.this);
                } else if (number_field.getText().toString().length() <= 0) {
                    Utility.displayDialog(noEntryAlert, PerchaseDetailsActivity.this);
                } else if (number_field.getText().toString().length() <= 10) {
                    Utility.displayDialog(rangealert, PerchaseDetailsActivity.this);
                } else if (number_field.getText().toString().length() > maxLimitValue) {
                    Utility.displayDialog(rangealert, PerchaseDetailsActivity.this);
                } else {
                    billNumber = number_field.getText().toString().replace(" ", "");
                    if (getIntent().getExtras().getString("isPlnprepaid").equalsIgnoreCase("true")) {

                        if (plnamount_entryfield.getText().toString().replace("Rp ", "").length() == 0) {
                            Utility.displayDialog("Silahkan masukkan jumlah yang ingin Pembelian.", PerchaseDetailsActivity.this);
                        } else if (pin_field.getText().toString().length() < getResources().getInteger(R.integer.pinSize)) {
                            Utility.displayDialog("Harap masukkan mPIN Anda.", PerchaseDetailsActivity.this);
                        } else if (pin_field.getText().toString().length() < 6) {
                            Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), PerchaseDetailsActivity.this);
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
                            Utility.displayDialog("Harap masukkan mPIN Anda.", PerchaseDetailsActivity.this);
                        } else if (pin_field.getText().toString().length() < 6) {
                            Utility.displayDialog(getResources().getString(R.string.mPinLegthMessage), PerchaseDetailsActivity.this);
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

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 10) {
                Intent intent = getIntent();
                setResult(10, intent);
                finish();
            }
        }
    }


    class PurchaseAccountAsynTask extends AsyncTask<Void, Void, Void> {


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
            mapContainer.put(Constants.PARAMETER_DENOM_CODE, "2");
            mapContainer.put(Constants.PARAMETER_AMOUNT, amountString);
            mapContainer.put("nominalAmount", amountString);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, PerchaseDetailsActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }


        ProgressDialog progressDialog;
        int msgCode;


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PerchaseDetailsActivity.this);
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
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(PerchaseDetailsActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(responseContainer.getMsg());
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(PerchaseDetailsActivity.this, SecondLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    alertbox.show();
                } else if (msgCode == 660) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(PerchaseDetailsActivity.this, PerchaseConfirmationActivity.class);
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
                                PerchaseDetailsActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), PerchaseDetailsActivity.this);
                    }
                }
            } else {
                Log.d(LOG_TAG, "response: " + null + ", -----" + response);
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), PerchaseDetailsActivity.this);
            }
        }
    }

    Dialog dialogCustomWish;
    int selected_region = -1;

    ProductsAdapter productsAdapter;

    public void WorkDisplay() {

        dialogCustomWish = new Dialog(PerchaseDetailsActivity.this);
        dialogCustomWish.setCancelable(false);

        dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        dialogCustomWish.setContentView(R.layout.province_list);

        Button button = (Button) dialogCustomWish.findViewById(R.id.ok);
        Button button1 = (Button) dialogCustomWish.findViewById(R.id.Cancel);
        TextView textView = (TextView) dialogCustomWish.findViewById(R.id.title);
        ListView listView = (ListView) dialogCustomWish.findViewById(R.id.locationsList);
        button.setTypeface(Utility.RegularTextFormat(PerchaseDetailsActivity.this));
        button1.setTypeface(Utility.RegularTextFormat(PerchaseDetailsActivity.this));
        textView.setTypeface(Utility.RegularTextFormat(PerchaseDetailsActivity.this));
        textView.setText("Pilih");

        productsAdapter = new ProductsAdapter();
        listView.setAdapter(productsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selected_region = position;
                productsAdapter.notifyDataSetChanged();
            }
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
            View view = LayoutInflater.from(PerchaseDetailsActivity.this).inflate(R.layout.location_row, null);
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
}
