package com.payment.simaspay.PaymentPurchaseAccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.TextView;

import com.payment.simaspay.PojoClasses.AccountTypeClass;
import com.payment.simaspay.PojoClasses.Payments;
import com.payment.simaspay.services.Utility;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import simaspay.payment.com.simaspay.R;


public class BillersListActivity extends Activity {
    TextView title;
    LinearLayout back;
    ListView listView;
    String billerString;
    String CategoryType;

    AccountTypeAdapter accountTypeAdapter;

    ArrayList<Payments> providerNamesList = new ArrayList<>();

    ArrayList<AccountTypeClass> arrayList = new ArrayList<>();

    void AddAccountDetails() {
        AccountTypeClass accountTypeClass = new AccountTypeClass();
        accountTypeClass.setId("1");
        accountTypeClass.setName("Smartfren");
        arrayList.add(accountTypeClass);

        accountTypeClass = new AccountTypeClass();
        accountTypeClass.setId("2");
        accountTypeClass.setName("Kartu Halo");
        arrayList.add(accountTypeClass);

        accountTypeClass = new AccountTypeClass();
        accountTypeClass.setId("3");
        accountTypeClass.setName("XL/Axis");
        arrayList.add(accountTypeClass);

        accountTypeClass = new AccountTypeClass();
        accountTypeClass.setId("4");
        accountTypeClass.setName("Esia");
        arrayList.add(accountTypeClass);

        accountTypeClass = new AccountTypeClass();
        accountTypeClass.setId("5");
        accountTypeClass.setName("Telkom Flexi Classic");
        arrayList.add(accountTypeClass);

        accountTypeClass = new AccountTypeClass();
        accountTypeClass.setId("6");
        accountTypeClass.setName("Three");
        arrayList.add(accountTypeClass);


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


        title = (TextView) findViewById(R.id.titled);
        title.setTypeface(Utility.Robot_Regular(BillersListActivity.this));

        back = (LinearLayout) findViewById(R.id.back_layout);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.account_type);

        if (getIntent().getExtras().getBoolean("accounttype")) {
            title.setText("Pembayaran");
            billerString = getIntent().getStringExtra("billerslist");
            try {
                JSONArray array = new JSONArray(billerString);
                Log.e("-------", "------" + array.toString());

                if (array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        Payments payments = new Payments();
                        try {
                            payments.setProductName(array.getJSONObject(i).getString("productName"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            payments.setProductCode(array.getJSONObject(i).getString("productCode"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            payments.setPaymentMode(array.getJSONObject(i).getString("paymentMode"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            payments.setInvoiceType(array.getJSONObject(i).getString("invoiceType"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            payments.setErrormessage(array.getJSONObject(i).getString("errormessage"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            payments.setErrormessage1(array.getJSONObject(i).getString("errormessage1"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            payments.setMaxLength(array.getJSONObject(i).getInt("maxlength"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        providerNamesList.add(payments);
                    }

                    if (providerNamesList.size() > 0) {
                        accountTypeAdapter = new AccountTypeAdapter();
                        listView.setAdapter(accountTypeAdapter);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            billerString = getIntent().getStringExtra("billerslist");
            try {
                JSONArray array = new JSONArray(billerString);
                Log.e("-------", "------" + array.toString());

                if (array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        Payments payments = new Payments();
                        try {
                            payments.setProductName(array.getJSONObject(i).getString("productName"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            payments.setProductCode(array.getJSONObject(i).getString("productCode"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            payments.setDenomValues(array.getJSONObject(i).getString("Denom"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            payments.setPaymentMode(array.getJSONObject(i).getString("paymentMode"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            payments.setInvoiceType(array.getJSONObject(i).getString("invoiceType"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            payments.setNominaltype(array.getJSONObject(i).getString("Nominaltype"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            payments.setIsPLNPrepaid(array.getJSONObject(i).getString("isPLNPrepaid"));
                        } catch (JSONException e) {
                            payments.setIsPLNPrepaid("false");
                            e.printStackTrace();
                        }
                        try {
                            payments.setErrormessage(array.getJSONObject(i).getString("errormessage"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            payments.setErrormessage1(array.getJSONObject(i).getString("errormessage1"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            payments.setMaxLength(array.getJSONObject(i).getInt("maxlength"));
                            payments.setMinLength(array.getJSONObject(i).getInt("minlength"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        providerNamesList.add(payments);
                    }

                    if (providerNamesList.size() > 0) {
                        accountTypeAdapter = new AccountTypeAdapter();
                        listView.setAdapter(accountTypeAdapter);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            title.setText("Pembelian");
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (getIntent().getExtras().getBoolean("accounttype")) {
                    Intent intent = new Intent(BillersListActivity.this, PaymentDetailsActivity.class);
                    intent.putExtra("productCategory", providerNamesList.get(i).getProductCategory());
                    intent.putExtra("accounttype", getIntent().getExtras().getBoolean("accounttype"));
                    intent.putExtra("ProviderName", getIntent().getExtras().getString("ProviderName"));
                    intent.putExtra("CategoryType", getIntent().getExtras().getString("CategoryType"));
                    intent.putExtra("ProductName", providerNamesList.get(i).getProductName());
                    intent.putExtra("ProductCode", providerNamesList.get(i).getProductCode());
                    intent.putExtra("PaymentMode", providerNamesList.get(i).getPaymentMode());
                    intent.putExtra("invoiceType", providerNamesList.get(i).getInvoiceType());
                    intent.putExtra("errormessage", providerNamesList.get(i).getErrormessage());
                    intent.putExtra("errormessage1", providerNamesList.get(i).getErrormessage1());
                    intent.putExtra("maxLength", providerNamesList.get(i).getMaxLength());
                    intent.putExtra("minLength", providerNamesList.get(i).getMinLength());
                    startActivityForResult(intent, 10);
                } else {
                    Intent intent = new Intent(BillersListActivity.this, PurchaseDetailsActivity.class);
                    intent.putExtra("productCategory", providerNamesList.get(i).getProductCategory());
                    intent.putExtra("accounttype", getIntent().getExtras().getBoolean("accounttype"));
                    intent.putExtra("ProviderName", getIntent().getExtras().getString("ProviderName"));
                    intent.putExtra("CategoryType", getIntent().getExtras().getString("CategoryType"));
                    intent.putExtra("ProductName", providerNamesList.get(i).getProductName());
                    intent.putExtra("ProductCode", providerNamesList.get(i).getProductCode());
                    intent.putExtra("PaymentMode", providerNamesList.get(i).getPaymentMode());
                    intent.putExtra("invoiceType", providerNamesList.get(i).getInvoiceType());
                    intent.putExtra("NominalType", providerNamesList.get(i).getNominaltype());
                    intent.putExtra("errormessage", providerNamesList.get(i).getErrormessage());
                    intent.putExtra("errormessage1", providerNamesList.get(i).getErrormessage1());
                    intent.putExtra("maxLength", providerNamesList.get(i).getMaxLength());
                    intent.putExtra("minLength", providerNamesList.get(i).getMinLength());
                    if(providerNamesList.get(i).getIsPLNPrepaid().equalsIgnoreCase("true")){
                        intent.putExtra("DenomValues","");
                    }else {
                        intent.putExtra("DenomValues", providerNamesList.get(i).getDenomValues());
                    }
                    intent.putExtra("isPlnprepaid",providerNamesList.get(i).getIsPLNPrepaid());
                    startActivityForResult(intent, 10);
                }
            }

        });


    }

    class AccountTypeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (getIntent().getExtras().getBoolean("accounttype")) {
                return providerNamesList.size();
            } else {
                return providerNamesList.size();
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

            View view1 = LayoutInflater.from(BillersListActivity.this).inflate(R.layout.textviewdata, null);
            TextView textView = (TextView) view1.findViewById(R.id.textviewdata_text);

            if (getIntent().getExtras().getBoolean("accounttype")) {
                textView.setText(providerNamesList.get(i).getProductName());
            } else {
                textView.setText(providerNamesList.get(i).getProductName());
            }

            textView.setTypeface(Utility.Robot_Regular(BillersListActivity.this));
            return view1;
        }
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
}
