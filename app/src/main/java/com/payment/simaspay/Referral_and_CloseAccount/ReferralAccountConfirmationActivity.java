package com.payment.simaspay.Referral_and_CloseAccount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.R;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SessionTimeOutActivity;
import com.payment.simaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nagendra P on 1/28/2016.
 */
public class ReferralAccountConfirmationActivity extends Activity {


    TextView title, heading, name, name_field, number, number_field, amount, amount_field, products, products_field;

    Button cancel, confirmation;

    LinearLayout back;

    SharedPreferences sharedPreferences;

    String mdn, NameField, email_field, otherfield, productDesired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commonconfirmation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        title = (TextView) findViewById(R.id.title);
        heading = (TextView) findViewById(R.id.textview);
        name = (TextView) findViewById(R.id.name);
        name_field = (TextView) findViewById(R.id.name_field);
        number = (TextView) findViewById(R.id.number);
        number_field = (TextView) findViewById(R.id.number_field);
        amount = (TextView) findViewById(R.id.amount);
        amount_field = (TextView) findViewById(R.id.amount_field);
        products = (TextView) findViewById(R.id.products);
        products_field = (TextView) findViewById(R.id.other_products);

        products.setVisibility(View.VISIBLE);
        products_field.setVisibility(View.VISIBLE);

        cancel = (Button) findViewById(R.id.cancel);
        confirmation = (Button) findViewById(R.id.next);

        back = (LinearLayout) findViewById(R.id.back_layout);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        mdn = getIntent().getExtras().getString("mdn");
        NameField = getIntent().getExtras().getString("NameField");
        email_field = getIntent().getExtras().getString("email_field");
        otherfield = getIntent().getExtras().getString("otherfield");
        productDesired = getIntent().getExtras().getString("productDesired");


        name.setText("Nama Lengkap");
        name_field.setText(NameField);

        number.setText("Nomor Handphone");
        number_field.setText(mdn);

        amount.setText("E-mail");
        amount_field.setText(email_field);

        products.setText("Produk yang Diinginkan");
        if(productDesired.equals("Lainnya")){
            products_field.setText(productDesired+" - "+ otherfield);
        }else{
            products_field.setText(productDesired);
        }



        title.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));
        heading.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));
        name.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));
        name_field.setTypeface(Utility.Robot_Light(ReferralAccountConfirmationActivity.this));
        number.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));
        number_field.setTypeface(Utility.Robot_Light(ReferralAccountConfirmationActivity.this));
        amount.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(ReferralAccountConfirmationActivity.this));
        products.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));
        products_field.setTypeface(Utility.Robot_Light(ReferralAccountConfirmationActivity.this));
        cancel.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));
        confirmation.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(11, intent);
                finish();
            }
        });

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReferralAsynTask().execute();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(11, intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        setResult(11, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 10) {
                Intent intent = getIntent();
                setResult(10, intent);
                finish();
            } else {

            }
        }
    }

    class ReferralAsynTask extends AsyncTask<Void, Void, Void> {


        String response;

        @Override
        protected Void doInBackground(Void... params) {


            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_AGENT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_REFFERAL_INQUERY);
            if (productDesired.equals("Lainnya")) {
                mapContainer.put(Constants.PARAMETER_PRODUCT,productDesired );
                mapContainer.put(Constants.PARAMETER_OTHERS, otherfield);
            } else {
                mapContainer.put(Constants.PARAMETER_PRODUCT, productDesired);
                mapContainer.put(Constants.PARAMETER_OTHERS, otherfield);
            }

            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, sharedPreferences.getString("password", ""));
            mapContainer.put(Constants.PARAMETER_EMAIL, email_field);
            mapContainer.put(Constants.PARAMETER_DEST_MDN, mdn);
            mapContainer.put(Constants.PARAMETER_FULLNAME, NameField);

            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, ReferralAccountConfirmationActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }


        ProgressDialog progressDialog;
        int msgCode;


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ReferralAccountConfirmationActivity.this);
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
                    Intent intent = new Intent(ReferralAccountConfirmationActivity.this, SessionTimeOutActivity.class);
                    startActivityForResult(intent, 40);
                } else if (msgCode == 2133) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(ReferralAccountConfirmationActivity.this, ReferralAccountSuccessActivity.class);
                    intent.putExtra("mdn",mdn);
                    intent.putExtra("NameField",NameField);
                    intent.putExtra("email_field",email_field);
                    intent.putExtra("productDesired",productDesired);
                    intent.putExtra("otherfield",otherfield);
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
                                ReferralAccountConfirmationActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), ReferralAccountConfirmationActivity.this);
                    }
                }
            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), ReferralAccountConfirmationActivity.this);
            }
        }
    }
}
