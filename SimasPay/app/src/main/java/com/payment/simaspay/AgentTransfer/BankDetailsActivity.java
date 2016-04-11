package com.payment.simaspay.AgentTransfer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.payment.simaspay.PojoClasses.BanksData;
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

/**
 * Created by Nagendra P on 1/30/2016.
 */
public class BankDetailsActivity extends Activity {

    TextView title;

    EditText search;

    ListView bankList;

    LinearLayout backlayLayout;

    BanksAdapter banksAdapter;

    ArrayList<BanksData> arrayList = new ArrayList<>();


    ArrayList<BanksData> filteredList = new ArrayList<>();


    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bankdetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }
        title = (TextView) findViewById(R.id.title);
        search = (EditText) findViewById(R.id.search);
        bankList = (ListView) findViewById(R.id.bankList);

        progressDialog = new ProgressDialog(BankDetailsActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayList.clear();
                for (int i = 0; i < filteredList.size(); i++) {
                    final String text = filteredList.get(i).getBankName();
                    if (text.contains(s.toString().toLowerCase()) || text.contains(s.toString().toUpperCase())) {
                        arrayList.add(filteredList.get(i));
                    }
                }
                banksAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new BanksListAsyn().execute();

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        backlayLayout = (LinearLayout) findViewById(R.id.back_layout);

        backlayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title.setTypeface(Utility.Robot_Regular(BankDetailsActivity.this));


        bankList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(BankDetailsActivity.this, TransferOtherBankDetailsActivity.class);
                intent.putExtra("BankCode", arrayList.get(i).getBankId());
                intent.putExtra("BankName", arrayList.get(i).getBankName());
                startActivityForResult(intent, 10);

            }
        });


    }

    class BanksAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = LayoutInflater.from(BankDetailsActivity.this).inflate(R.layout.textviewdata, null);
            TextView textView = (TextView) view1.findViewById(R.id.textviewdata_text);

            textView.setText(arrayList.get(i).getBankName());
            textView.setTypeface(Utility.Robot_Regular(BankDetailsActivity.this));
            return view1;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }


    String response;
    ProgressDialog progressDialog;

    class BanksListAsyn extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_BILLPAYMENT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_GETTHIRDPARTYDATA);
            mapContainer.put(Constants.PARAMETER_CATEGORY, Constants.TRANSACTION_GETBANKCODELIST);
            mapContainer.put(Constants.PARAMETER_VERSION, "0");
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, BankDetailsActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            Log.e("-----", "" + mapContainer.toString());
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (response != null) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Log.e("------", "Response====" + response);
                if (response != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (jsonObject != null) {
                        try {
                            if (jsonObject.getString("success").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("bankData");
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        BanksData banksData = new BanksData();
                                        try {
                                            banksData.setBankId(jsonArray.getJSONObject(i).getString("code"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            banksData.setBankName(jsonArray.getJSONObject(i).getString("name"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        arrayList.add(banksData);
                                        filteredList.add(banksData);
                                    }

                                    if (arrayList.size() > 0) {
                                        banksAdapter = new BanksAdapter();
                                        bankList.setAdapter(banksAdapter);
                                    }

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), BankDetailsActivity.this);
            }
            super.onPostExecute(aVoid);
        }
    }
}
