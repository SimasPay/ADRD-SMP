package com.payment.simaspay.PaymentPurchaseAccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.payment.simaspay.PojoClasses.AccountTypeClass;
import com.payment.simaspay.PojoClasses.Payments;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

public class PaymentAndPurchaseAccountTypeActivity extends AppCompatActivity {
    TextView title;
    LinearLayout back;
    ListView listView;
    AccountTypeAdapter accountTypeAdapter;
    SharedPreferences sharedPreferences;
    ArrayList<Payments> paymentsArrayList = new ArrayList<>();
    ArrayList<Payments> purchaseArrayList = new ArrayList<>();
    ArrayList<AccountTypeClass> arrayList = new ArrayList<>();

    void AddAccountDetails(boolean value) {
        if (value) {
            new PaymentCategoryAsynTask().execute();
        } else {
            new PurchaseCategoryAsynTask().execute();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentaccounttype);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        AddAccountDetails(getIntent().getExtras().getBoolean("accounttype"));
        title = findViewById(R.id.titled);
        title.setTypeface(Utility.Robot_Regular(PaymentAndPurchaseAccountTypeActivity.this));

        if (getIntent().getExtras().getBoolean("accounttype")) {
            title.setText("Pembayaran");
        } else {
            title.setText("Pembelian");
        }

        sharedPreferences=getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
        Drawable drawable = new ProgressBar(this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.red_sinarmas),
                PorterDuff.Mode.SRC_IN);
        progressDialog.setIndeterminateDrawable(drawable);
        progressDialog.show();

        back = findViewById(R.id.back_layout);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView = findViewById(R.id.account_type);


        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            if (getIntent().getExtras().getBoolean("accounttype")) {
                Intent intent = new Intent(PaymentAndPurchaseAccountTypeActivity.this, ProviderNamesListActivity.class);
                intent.putExtra("accounttype", getIntent().getExtras().getBoolean("accounttype"));
                intent.putExtra("providerNames", paymentsArrayList.get(i).getJsonArray().toString());
                intent.putExtra("CategoryType", paymentsArrayList.get(i).getProductCategory());
                startActivityForResult(intent, 10);
            }else{
                Intent intent = new Intent(PaymentAndPurchaseAccountTypeActivity.this, ProviderNamesListActivity.class);
                intent.putExtra("accounttype", getIntent().getExtras().getBoolean("accounttype"));
                intent.putExtra("providerNames",purchaseArrayList.get(i).getJsonArray().toString());
                intent.putExtra("CategoryType", purchaseArrayList.get(i).getProductCategory());
                startActivityForResult(intent, 10);
            }
        });


    }

    private class AccountTypeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (getIntent().getExtras().getBoolean("accounttype")) {
                return paymentsArrayList.size();
            } else {
                return purchaseArrayList.size();
            }

        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = LayoutInflater.from(PaymentAndPurchaseAccountTypeActivity.this).inflate(R.layout.textviewdata, null);
            TextView textView = view1.findViewById(R.id.textviewdata_text);
            if (getIntent().getExtras().getBoolean("accounttype")) {
                textView.setText(paymentsArrayList.get(i).getProductCategory());
            } else {
                textView.setText(purchaseArrayList.get(i).getProductCategory());
            }

            textView.setTypeface(Utility.Robot_Regular(PaymentAndPurchaseAccountTypeActivity.this));
            return view1;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 10) {
                finish();
            }
        }
    }


    String response;
    ProgressDialog progressDialog;


    private class PaymentCategoryAsynTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_BILLPAYMENT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_GETTHIRDPARTYDATA);
            mapContainer.put(Constants.PARAMETER_CATEGORY, Constants.TRANSACTION_PAYMENTS);
            mapContainer.put(Constants.PARAMETER_VERSION, Constants.CONSTANT_VALUE_ZERO);

            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, PaymentAndPurchaseAccountTypeActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog != null) {
                progressDialog.show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("-------","=-==-==="+response);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (response != null) {
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonObject != null) {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = jsonObject.getJSONArray("paymentData");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (jsonArray != null) {
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Payments payments = new Payments();
                                try {
                                    payments.setProductCategory(jsonArray.getJSONObject(i).getString("productCategory"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    payments.setJsonArray(jsonArray.getJSONObject(i).getJSONArray("providers"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                paymentsArrayList.add(payments);
                            }

                            if (paymentsArrayList.size() > 0) {
                                accountTypeAdapter = new AccountTypeAdapter();
                                listView.setAdapter(accountTypeAdapter);
                            }
                        }
                    }
                }
            }else{
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), PaymentAndPurchaseAccountTypeActivity.this);
            }
        }
    }



    private class PurchaseCategoryAsynTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_BILLPAYMENT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_GETTHIRDPARTYDATA);
            mapContainer.put(Constants.PARAMETER_CATEGORY, Constants.TRANSACTION_PURCHASES);
            mapContainer.put(Constants.PARAMETER_VERSION, Constants.CONSTANT_VALUE_ZERO);

            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, PaymentAndPurchaseAccountTypeActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog != null) {
                progressDialog.show();
            }


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("-------","=-==-==="+response);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (response != null) {
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonObject != null) {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = jsonObject.getJSONArray("purchaseData");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (jsonArray != null) {
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Payments payments = new Payments();
                                try {
                                    payments.setProductCategory(jsonArray.getJSONObject(i).getString("productCategory"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    payments.setJsonArray(jsonArray.getJSONObject(i).getJSONArray("providers"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                purchaseArrayList.add(payments);
                            }

                            if (purchaseArrayList.size() > 0) {
                                accountTypeAdapter = new AccountTypeAdapter();
                                listView.setAdapter(accountTypeAdapter);
                            }
                        }
                    }
                }
            }else{
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), PaymentAndPurchaseAccountTypeActivity.this);
            }
        }
    }
}
