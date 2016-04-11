package com.payment.simaspay.PaymentPerchaseAccount;

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

/**
 * Created by Nagendra P on 1/28/2016.
 */
public class ProviderNamesListActivity extends Activity {

    TextView title;

    LinearLayout back;

    ListView listView;

    String billerString;
    String CategoryType;

    AccountTypeAdapter accountTypeAdapter;

    ArrayList<Payments> providerNamesList = new ArrayList<>();

    ArrayList<AccountTypeClass> arrayList = new ArrayList<>();


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
        title.setTypeface(Utility.Robot_Regular(ProviderNamesListActivity.this));

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
            billerString = getIntent().getStringExtra("providerNames");
            try {
                JSONArray array = new JSONArray(billerString);
                Log.e("-------", "------" + array.toString());

                if (array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        Payments payments = new Payments();
                        try {
                            payments.setProviderName(array.getJSONObject(i).getString("providerName"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            payments.setProductsJsonArray(array.getJSONObject(i).getJSONArray("products"));
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
            title.setText("Pembelian");

            billerString = getIntent().getStringExtra("providerNames");
            try {
                JSONArray array = new JSONArray(billerString);
                Log.e("-------", "------" + array.toString());

                if (array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        Payments payments = new Payments();
                        try {
                            payments.setProviderName(array.getJSONObject(i).getString("providerName"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            payments.setProductsJsonArray(array.getJSONObject(i).getJSONArray("products"));
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
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (getIntent().getExtras().getBoolean("accounttype")) {
                    Intent intent = new Intent(ProviderNamesListActivity.this, BillersListActivity.class);
                    intent.putExtra("accounttype", getIntent().getExtras().getBoolean("accounttype"));
                    intent.putExtra("billerslist", providerNamesList.get(i).getProductsJsonArray().toString());
                    intent.putExtra("ProviderName", providerNamesList.get(i).getProviderName());
                    intent.putExtra("CategoryType", getIntent().getExtras().getString("CategoryType"));
                    startActivityForResult(intent, 10);
                } else {
                    Intent intent = new Intent(ProviderNamesListActivity.this, BillersListActivity.class);
                    intent.putExtra("accounttype", getIntent().getExtras().getBoolean("accounttype"));
                    intent.putExtra("billerslist", providerNamesList.get(i).getProductsJsonArray().toString());
                    intent.putExtra("ProviderName", providerNamesList.get(i).getProviderName());
                    intent.putExtra("CategoryType", getIntent().getExtras().getString("CategoryType"));
                    startActivityForResult(intent, 10);
                }
            }

        });


    }

    class AccountTypeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return providerNamesList.size();
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

            View view1 = LayoutInflater.from(ProviderNamesListActivity.this).inflate(R.layout.textviewdata, null);
            TextView textView = (TextView) view1.findViewById(R.id.textviewdata_text);

            if (getIntent().getExtras().getBoolean("accounttype")) {
                textView.setText(providerNamesList.get(i).getProviderName());
            } else {
                textView.setText(providerNamesList.get(i).getProviderName());
            }

            textView.setTypeface(Utility.Robot_Regular(ProviderNamesListActivity.this));
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
