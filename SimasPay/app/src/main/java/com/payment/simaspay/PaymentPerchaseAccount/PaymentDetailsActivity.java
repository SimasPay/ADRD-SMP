package com.payment.simaspay.PaymentPerchaseAccount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.userdetails.SessionTimeOutActivity;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/28/2016.
 */
public class PaymentDetailsActivity extends AppCompatActivity {

    TextView title, product, number, pin, amount_Text, rp;

    EditText product_field, number_field, pin_field, amountField;

    Button submit;

    LinearLayout back;
    String[] strings;

    String billNumber, pinValue, encryptedpinValue, amountValue, rangealert, noEntryAlert;

    SharedPreferences sharedPreferences;
    int maxLimitValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentdetails);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        title = (TextView) findViewById(R.id.titled);
        product = (TextView) findViewById(R.id.name_product);
        product_field = (EditText) findViewById(R.id.product_field);
        number = (TextView) findViewById(R.id.number);
        number_field = (EditText) findViewById(R.id.number_field);
        pin = (TextView) findViewById(R.id.mPin);
        pin_field = (EditText) findViewById(R.id.pin);
        amount_Text = (TextView) findViewById(R.id.mAMount);
        rp = (TextView) findViewById(R.id.Rp);
        amountField = (EditText) findViewById(R.id.Payment_amountentry_field);

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

        back = (LinearLayout) findViewById(R.id.back_layout);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        submit = (Button) findViewById(R.id.submit);

        submit.setTypeface(Utility.Robot_Regular(PaymentDetailsActivity.this));

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

                if (number_field.getText().toString().length() <= 0) {
                    Utility.displayDialog(noEntryAlert, PaymentDetailsActivity.this);
                } else if (number_field.getText().toString().length() < 10) {
                    Utility.displayDialog(rangealert, PaymentDetailsActivity.this);
                } else if (number_field.getText().toString().length() > maxLimitValue) {
                    Utility.displayDialog(rangealert, PaymentDetailsActivity.this);
                } else {
                    billNumber = number_field.getText().toString().replace(" ", "");
                    if (getIntent().getExtras().getString("PaymentMode").equalsIgnoreCase("FullAmount")) {

                        if (amountField.getText().toString().replace(" ", "").length() == 0) {
                            Utility.displayDialog("Silahkan masukkan jumlah yang ingin Pembayaran.", PaymentDetailsActivity.this);
                        } else if (pin_field.getText().toString().length() <= 0) {
                            Utility.displayDialog("Harap masukkan mPIN Anda.", PaymentDetailsActivity.this);
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
                            Utility.displayDialog("Harap masukkan mPIN Anda.", PaymentDetailsActivity.this);
                        } else if (pin_field.getText().toString().length() < 6) {
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
        if (requestCode == 10) {
            if (resultCode == 10) {
                Intent intent = getIntent();
                setResult(10, intent);
                finish();
            }
        }
    }

    class BillpaymentAsynTask extends AsyncTask<Void, Void, Void> {


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
                    Intent intent = new Intent(PaymentDetailsActivity.this, PaymentConfirmationActivity.class);
                    intent.putExtra("creditamt", responseContainer.getEncryptedDebitAmount());
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
}
