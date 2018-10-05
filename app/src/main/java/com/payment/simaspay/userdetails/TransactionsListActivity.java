package com.payment.simaspay.userdetails;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.payment.simaspay.PojoClasses.TransactionsData;
import com.payment.simaspay.services.AppConfigFile;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.JSONParser;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;


public class TransactionsListActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SimasPay";
    TextView title, period, terms_conditions_1;
    Button ok;
    TransactionsAdapter transactionsAdapter;
    LinearLayout back_layout, dwn_layout;
    int totaloCountValue, pageNumber = 0;
    ArrayList<TransactionsData> transationsListarray = new ArrayList<>();
    ListView listView;
    SharedPreferences sharedPreferences;
    boolean isLoading;
    String response;
    ProgressDialog progressDialog;
    WebServiceHttp webServiceHttp;
    int msgCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactionslist);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        isLoading = false;

        title = findViewById(R.id.title);
        period = findViewById(R.id.terms_conditions);
        terms_conditions_1 = findViewById(R.id.terms_conditions_1);


        progressDialog = new ProgressDialog(TransactionsListActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
        Drawable drawable = new ProgressBar(TransactionsListActivity.this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(TransactionsListActivity.this, R.color.red_sinarmas),
                PorterDuff.Mode.SRC_IN);
        progressDialog.setIndeterminateDrawable(drawable);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        listView = findViewById(R.id.listView);
        listView.setSmoothScrollbarEnabled(true);
        listView.setFastScrollEnabled(true);
        ok = findViewById(R.id.accept);

        back_layout = findViewById(R.id.back_layout);
        dwn_layout = findViewById(R.id.download_layout);

        String label_home = sharedPreferences.getString(Constants.PARAMETER_TYPEUSER, "");

        if (label_home.equals(Constants.CONSTANT_EMONEYNONKYC_USER)) {
            dwn_layout.setVisibility(View.VISIBLE);
            title.setText(getResources().getString(R.string.transaksi));
            findViewById(R.id.period).setVisibility(View.VISIBLE);
            String fromDate = getIntent().getExtras().getString("fromDate");
            String toDate = getIntent().getExtras().getString("toDate");
            terms_conditions_1.setText(fromDate.substring(0, 2) + " " + Utility.getMonth(fromDate.substring(2, 4)) + " '" + fromDate.substring(4) + " - " + toDate.substring(0, 2) + " " + Utility.getMonth(toDate.substring(2, 4)) + " '" + toDate.substring(4));
        } else if (label_home.equals(Constants.CONSTANT_EMONEYKYC_USER)){
            dwn_layout.setVisibility(View.VISIBLE);
            title.setText(getResources().getString(R.string.transaksi));
            findViewById(R.id.period).setVisibility(View.VISIBLE);
            String fromDate = getIntent().getExtras().getString("fromDate");
            String toDate = getIntent().getExtras().getString("toDate");
            terms_conditions_1.setText(fromDate.substring(0, 2) + " " + Utility.getMonth(fromDate.substring(2, 4)) + " '" + fromDate.substring(4) + " - " + toDate.substring(0, 2) + " " + Utility.getMonth(toDate.substring(2, 4)) + " '" + toDate.substring(4));
        } else if(label_home.equals(Constants.CONSTANT_BOTH_USER)){
            String account = sharedPreferences.getString(Constants.PARAMETER_USES_AS, "");
            if (account.equals(Constants.CONSTANT_BANK_USER)) {
                dwn_layout.setVisibility(View.INVISIBLE);
                title.setText(getResources().getString(R.string.transaksi));
                findViewById(R.id.period).setVisibility(View.GONE);
            } else {
                dwn_layout.setVisibility(View.VISIBLE);
                title.setText(getResources().getString(R.string.transaksi));
                findViewById(R.id.period).setVisibility(View.VISIBLE);
                String fromDate = getIntent().getExtras().getString("fromDate");
                String toDate = getIntent().getExtras().getString("toDate");
                terms_conditions_1.setText(fromDate.substring(0, 2) + " " + Utility.getMonth(fromDate.substring(2, 4)) + " '" + fromDate.substring(4) + " - " + toDate.substring(0, 2) + " " + Utility.getMonth(toDate.substring(2, 4)) + " '" + toDate.substring(4));
            }
        } else if(label_home.equals(Constants.CONSTANT_BANK_USER)) {
            dwn_layout.setVisibility(View.INVISIBLE);
            title.setText(getResources().getString(R.string.transaksi));
            findViewById(R.id.period).setVisibility(View.GONE);
        } else {
            dwn_layout.setVisibility(View.VISIBLE);
            title.setText(getResources().getString(R.string.transaksi));
            findViewById(R.id.period).setVisibility(View.GONE);
        }

        back_layout.setOnClickListener(view -> {
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });

        dwn_layout.setOnClickListener(v -> new DownLoadAsyncTask().execute());

        title.setTypeface(Utility.Robot_Regular(TransactionsListActivity.this));
        period.setTypeface(Utility.Robot_Regular(TransactionsListActivity.this));
        terms_conditions_1.setTypeface(Utility.Robot_Light(TransactionsListActivity.this));

        ok.setTypeface(Utility.Robot_Regular(TransactionsListActivity.this));


        ok.setOnClickListener(view -> {
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });
        new TransactionsListAsync().execute();



        /**
        if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_BANK_INT) {
            new TransactionsListAsync().execute();
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int lastIndexInScreen = visibleItemCount + firstVisibleItem;
                    if (totalItemCount == totaloCountValue) {
                        Log.d(LOG_TAG, "totalItemCount :"+totalItemCount);
                    } else {
                        if (lastIndexInScreen >= totalItemCount && !isLoading) {
                            isLoading = true;
                            new TransactionsListAsync().execute();
                        }
                    }
                }
            });
        } else {
            new TransactionsListAsyncNonBank().execute();
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int lastIndexInScreen = visibleItemCount + firstVisibleItem;
                    if (totalItemCount == totaloCountValue) {
                    } else {
                        Log.d(LOG_TAG, "lastIndexInScreen: " + lastIndexInScreen + "\nTotalItemCount" + totalItemCount + "\ntotalCountValue:" + totaloCountValue);
                        if (lastIndexInScreen >= totalItemCount && !isLoading) {
                            isLoading = true;
                            new TransactionsListAsyncNonBank().execute();
                        }
                    }
                }
            });
        }
         **/


    }

    private class TransactionsAdapter extends BaseAdapter {
        @Override
        public Object getItem(int i) {
            return 0;
        }

        @Override
        public int getCount() {
            return transationsListarray.size();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View view1 = LayoutInflater.from(TransactionsListActivity.this).inflate(R.layout.transacton, null);
            TextView tv_date = view1.findViewById(R.id.date);
            TextView tv_type = view1.findViewById(R.id.type);
            TextView tv_debitcredit = view1.findViewById(R.id.credit_or_debit);
            TextView tv_amount = view1.findViewById(R.id.amount);
            TextView tv_refID = view1.findViewById(R.id.refID);

            tv_date.setTypeface(Utility.Robot_Light(TransactionsListActivity.this));
            tv_type.setTypeface(Utility.Robot_Regular(TransactionsListActivity.this));
            tv_debitcredit.setTypeface(Utility.Robot_Light(TransactionsListActivity.this));
            tv_amount.setTypeface(Utility.Robot_Regular(TransactionsListActivity.this));
            tv_refID.setTypeface(Utility.Robot_Regular(TransactionsListActivity.this));

            Log.d(LOG_TAG, "transaction data: " + transationsListarray.get(i).getTransactionData());
            Log.d(LOG_TAG, "transaction time: " + transationsListarray.get(i).getDate_time());
            Log.d(LOG_TAG, "transaction amount: " + transationsListarray.get(i).getAmount());
            Log.d(LOG_TAG, "transaction isCredit: " + transationsListarray.get(i).getType());
            Log.d(LOG_TAG, "transaction ref_id: " + transationsListarray.get(i).getRef_id());

            if (transationsListarray.get(i).getType().equals("false")) {
                tv_debitcredit.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_blue_color));
                tv_debitcredit.setText(getResources().getString(R.string.debit));
            } else {
                tv_debitcredit.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_color));
                tv_debitcredit.setText(getResources().getString(R.string.credit));
            }

            tv_date.setText(transationsListarray.get(i).getDate_time());
            tv_type.setText(transationsListarray.get(i).getTransactionData());
            tv_amount.setText("Rp. " + transationsListarray.get(i).getAmount());
            tv_refID.setText("Ref ID: "+ transationsListarray.get(i).getRef_id());
            return view1;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private class TransactionsListAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            Log.d(LOG_TAG, "channelID, " + "7");
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_HISTORY);
            Log.d(LOG_TAG, "txtName, " + "History");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN,
                    sharedPreferences.getString("mobileNumber", ""));
            Log.d(LOG_TAG, "sourceMDN, " + sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN,
                    sharedPreferences.getString("password", ""));
            Log.d(LOG_TAG, "sourcePIN, " + sharedPreferences.getString("password", ""));
            if (sharedPreferences.getInt("userType", -1) == 1) {
                mapContainer.put(Constants.PARAMETER_FROM_DATE, getIntent().getExtras().getString("fromDate"));
                Log.d(LOG_TAG, "fromDate, " + getIntent().getExtras().getString("fromDate"));
                mapContainer.put(Constants.PARAMETER_TO_DATE, getIntent().getExtras().getString("toDate"));
                Log.d(LOG_TAG, "toDate, " + getIntent().getExtras().getString("toDate"));
            } else if (sharedPreferences.getInt("userType", -1) == 0) {

            } else if (sharedPreferences.getInt("userType", -1) == 2) {
                if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                    mapContainer.put(Constants.PARAMETER_FROM_DATE, getIntent().getExtras().getString("fromDate"));
                    Log.d(LOG_TAG, "fromDate, " + getIntent().getExtras().getString("fromDate"));
                    mapContainer.put(Constants.PARAMETER_TO_DATE, getIntent().getExtras().getString("toDate"));
                    Log.d(LOG_TAG, "toDate, " + getIntent().getExtras().getString("toDate"));
                }
            } else if(sharedPreferences.getInt("userType", -1) == 3) {
                mapContainer.put(Constants.PARAMETER_FROM_DATE, getIntent().getExtras().getString("fromDate"));
                Log.d(LOG_TAG, "fromDate, " + getIntent().getExtras().getString("fromDate"));
                mapContainer.put(Constants.PARAMETER_TO_DATE, getIntent().getExtras().getString("toDate"));
                Log.d(LOG_TAG, "toDate, " + getIntent().getExtras().getString("toDate"));
            }

            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID, Constants.CONSTANT_INSTITUTION_ID);
            Log.d(LOG_TAG, "institutionID, " + Constants.CONSTANT_INSTITUTION_ID);
            if (sharedPreferences.getInt("userType", -1) == 1) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                Log.d(LOG_TAG, "service, " + "Wallet");
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
                Log.d(LOG_TAG, "sourcePocketCode, " + Constants.POCKET_CODE_BANK_SINARMAS);
            } else if (sharedPreferences.getInt("userType", -1) == 0) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                Log.d(LOG_TAG, "service, " + Constants.SERVICE_BANK);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                Log.d(LOG_TAG, Constants.PARAMETER_SRC_POCKET_CODE + ", " + Constants.POCKET_CODE_BANK);
            } else if (sharedPreferences.getInt("userType", -1) == 2) {
                if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                    Log.d(LOG_TAG, Constants.PARAMETER_SERVICE_NAME + ", " + Constants.SERVICE_WALLET);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                    Log.d(LOG_TAG, Constants.PARAMETER_SRC_POCKET_CODE + ", " + Constants.POCKET_CODE_EMONEY);
                } else {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                    Log.d(LOG_TAG, Constants.PARAMETER_SERVICE_NAME + ", " + Constants.SERVICE_BANK);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                    Log.d(LOG_TAG, Constants.PARAMETER_SRC_POCKET_CODE + ", " + Constants.POCKET_CODE_BANK);
                }
            } else if (sharedPreferences.getInt("userType", -1) == 3) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                Log.d(LOG_TAG, Constants.PARAMETER_SERVICE_NAME + ", " + Constants.SERVICE_WALLET);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                Log.d(LOG_TAG, Constants.PARAMETER_SRC_POCKET_CODE + ", " + Constants.POCKET_CODE_EMONEY);
            }

            webServiceHttp = new WebServiceHttp(mapContainer,
                    TransactionsListActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isLoading == false) {
                if (progressDialog != null) {
                    progressDialog.show();
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null) {
                Log.e(LOG_TAG, "Response: " + response);
                if (isLoading == false) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
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
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(TransactionsListActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(responseContainer.getMsg());
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(TransactionsListActivity.this, SecondLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    alertbox.show();
                } else if (msgCode == 39) {
                    totaloCountValue = responseContainer.getTotalRecords();
                    pageNumber = pageNumber + 1;
                    isLoading = false;
                    HistroyParser parser = new HistroyParser();
                    Document doc = parser.getDomElement(response);

                    NodeList nl = doc
                            .getElementsByTagName("transactionDetail");
                    if (nl.getLength() > 0) {
                        for (int i = 0; i < nl.getLength(); i++) {
                            TransactionsData transationsList = new TransactionsData();
                            Element e = (Element) nl.item(i);
                            transationsList.setTransactionData(parser.getValue(e,
                                    "transactionDescription"));
                            transationsList.setAmount(parser
                                    .getValue(e, "amount"));
                            transationsList.setType(parser.getValue(e,
                                    "isCredit"));
                            transationsList.setDate_time(parser
                                    .getValue(e, "transactionTime"));
                            transationsList.setRef_id(parser
                                    .getValue(e, "refID"));
                            transationsListarray.add(transationsList);
                            //Log.d(LOG_TAG,"transactionListArray:"+transationsListarray);
                        }

                    }
                    if (transationsListarray.size() > 0) {
                        if (pageNumber == 1) {
                            transactionsAdapter = new TransactionsAdapter();
                            listView.setAdapter(transactionsAdapter);
                        } else {
                            transactionsAdapter.notifyDataSetChanged();
                        }
                    }
                } else if(msgCode == 67) {
                    totaloCountValue = responseContainer.getTotalRecords();
                    isLoading = false;
                    HistroyParser parser = new HistroyParser();
                    Document doc = parser.getDomElement(response);

                    NodeList nl = doc
                            .getElementsByTagName("transactionDetail");
                    if (nl.getLength() > 0) {
                        Log.d(LOG_TAG, "jumlah transaksi:"+nl.getLength());
                        for (int i = 0; i < nl.getLength(); i++) {
                            TransactionsData transationsList = new TransactionsData();
                            Element e = (Element) nl.item(i);
                            transationsList.setTransactionData(parser.getValue(e,
                                    "transactionType"));
                            transationsList.setAmount(parser
                                    .getValue(e, "amount"));
                            transationsList.setType(parser.getValue(e,
                                    "isCredit"));
                            transationsList.setDate_time(parser
                                    .getValue(e, "transactionTime"));
                            transationsList.setRef_id(parser
                                    .getValue(e, "refID"));
                            transationsListarray.add(transationsList);
                        }
                    }
                    transactionsAdapter = new TransactionsAdapter();
                    listView.setAdapter(transactionsAdapter);
                } else if (msgCode == 38) {
                    progressDialog.dismiss();
                    //Utility.networkDisplayDialog(responseContainer.getMsg(), TransactionsListActivity.this);
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(TransactionsListActivity.this, R.style.MyAlertDialogStyle);
                    alertbox.setMessage(responseContainer.getMsg());
                    alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    });
                    alertbox.show();
                }
            } else {
                 if (progressDialog != null) {
                    progressDialog.dismiss();
                 }
                 Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), TransactionsListActivity.this);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class TransactionsListAsyncNonBank extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, Constants.TRANSACTION_HISTORY);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID, Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put("authenticationKey", "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, sharedPreferences.getString("password", ""));
            mapContainer.put(Constants.PARAMETER_FROM_DATE, getIntent().getExtras().getString("fromDate"));
            mapContainer.put(Constants.PARAMETER_TO_DATE, getIntent().getExtras().getString("toDate"));
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);
            if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_BANK_INT) {
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
            } else {
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
            }

            webServiceHttp = new WebServiceHttp(mapContainer, TransactionsListActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isLoading == false) {
                if (progressDialog != null) {
                    progressDialog.show();
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null) {
                Log.e("=======", "======" + response);
                if (isLoading == false) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
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

                if (msgCode == 39) {
                    totaloCountValue = responseContainer.getTotalRecords();
                    pageNumber = pageNumber + 1;
                    isLoading = false;
                    HistroyParser parser = new HistroyParser();
                    Document doc = parser.getDomElement(response);

                    NodeList nl = doc
                            .getElementsByTagName("transactionDetail");
                    if (nl.getLength() > 0) {
                        for (int i = 0; i < nl.getLength(); i++) {
                            TransactionsData transationsList = new TransactionsData();
                            Element e = (Element) nl.item(i);
                            transationsList.setTransactionData(parser.getValue(e,
                                    "transactionDescription"));
                            transationsList.setAmount(parser
                                    .getValue(e, "amount"));
                            transationsList.setType(parser.getValue(e,
                                    "isCredit"));
                            transationsList.setDate_time(parser
                                    .getValue(e, "transactionTime"));
                            transationsList.setRef_id(parser
                                    .getValue(e, "refID"));
                            transationsListarray.add(transationsList);
                        }

                    }
                    if (transationsListarray.size() > 0) {
                        if (pageNumber == 1) {
                            transactionsAdapter = new TransactionsAdapter();
                            listView.setAdapter(transactionsAdapter);
                        } else {
                            transactionsAdapter.notifyDataSetChanged();
                        }
                    }
                } else if (msgCode == 38) {
                    Utility.networkDisplayDialog(responseContainer.getMsg(), TransactionsListActivity.this);
                }


            } else {
                 if (progressDialog != null) {
                 progressDialog.dismiss();
                 }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), TransactionsListActivity.this);
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class DownLoadAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            if (progressDialog != null) {
                progressDialog.show();
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null) {
                Log.e("=======", "======" + response);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
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

                if (msgCode == 2051) {
                    Log.d("test", "download url:"+ AppConfigFile.pfdDownLoadURL + responseContainer.getDownLoadUrl());
                    new DownloadPDFTask()
                            .execute(AppConfigFile.pfdDownLoadURL
                                    + responseContainer.getDownLoadUrl());
                } else if (msgCode == 38) {
                    Utility.networkDisplayDialog(responseContainer.getMsg(), TransactionsListActivity.this);
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), TransactionsListActivity.this);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();

            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, "DownloadHistoryAsPDF");
            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID, Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put("authenticationKey", "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, sharedPreferences.getString("password", ""));
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, Constants.CONSTANT_CHANNEL_ID);
            if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_BANK_INT) {
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
            } else {
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
            }
            mapContainer.put(Constants.PARAMETER_FROM_DATE, getIntent().getExtras().getString("fromDate"));
            mapContainer.put(Constants.PARAMETER_TO_DATE, getIntent().getExtras().getString("toDate"));
            /**
             if (sharedPreferences.getInt("userType", -1) == 1) {
             mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
             } else if (sharedPreferences.getInt("userType", -1) == 0) {
             mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
             mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
             } else if (sharedPreferences.getInt("userType", -1) == 2) {
             if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
             mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
             mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
             } else {
             mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
             mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
             }
             }
             Log.e("======","======"+mapContainer.toString());
             **/
//

            webServiceHttp = new WebServiceHttp(mapContainer,
                    TransactionsListActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }
    }

    public class DownloadPDFTask extends AsyncTask<String, Void, Integer> {
        protected ProgressDialog mWorkingDialog; // progress dialog
        protected String mFileName; // downloaded file
        protected String mError; // for errors
        String fileName = "uangku_" + Constants.SOURCE_MDN_NAME;
        String fileExtension = ".pdf";
        String PATH;

        @Override
        protected Integer doInBackground(String... urls) {

            try {

                String[] strURLParts = urls[0].split("/");
                if (strURLParts.length > 0)
                    mFileName = strURLParts[strURLParts.length - 1];
                else
                    mFileName = "REPORT.pdf";
                byte[] dataBuffer = new byte[4096];
                int nRead = 0;

                String url = urls[0];
                HttpClient httpclient = null;
                if (url.startsWith("https")) {
                    httpclient = new JSONParser().getStagingHttpClient();
                } else {
                    httpclient = getNewHttpClient();
                }
                HttpPost httppost = new HttpPost(url);
                httppost.setHeader("Content-Type", "text/xml");
                BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient
                        .execute(httppost);

                InputStream streamInput = httpResponse.getEntity().getContent();
                BufferedInputStream bufferedStreamInput = new BufferedInputStream(
                        streamInput);
                FileOutputStream outputStream = TransactionsListActivity.this
                        .openFileOutput(mFileName, Context.MODE_PRIVATE);

                while ((nRead = bufferedStreamInput.read(dataBuffer)) > 0)
                    outputStream.write(dataBuffer, 0, nRead);
                streamInput.close();
                outputStream.close();

            } catch (Exception e) {
                return (1);
            }

            return (0);
        }


        @Override
        protected void onPreExecute() {
            mWorkingDialog = ProgressDialog.show(
                    TransactionsListActivity.this, "",
                    "Downloading PDF Document, Please Wait...", true);
            return;
        }


        @Override
        protected void onPostExecute(Integer result) {
            if (mWorkingDialog != null) {
                mWorkingDialog.dismiss();
                mWorkingDialog = null;
            }

            switch (result) {
                case 0:
                    try {
                        Uri uri = Uri.fromFile(TransactionsListActivity.this
                                .getFileStreamPath(mFileName));
                        Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                        intentUrl.setDataAndType(uri, "application/pdf");
                        intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentUrl);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(TransactionsListActivity.this,
                                "No PDF Viewer Installed", Toast.LENGTH_LONG)
                                .show();
                    } catch (Exception e) {
                        Toast.makeText(TransactionsListActivity.this,
                                "Error while downloading", Toast.LENGTH_LONG)
                                .show();
                    }

                    break;

                case 1: // Error

                    Toast.makeText(TransactionsListActivity.this,
                            "Error while downloading", Toast.LENGTH_LONG).show();
                    break;

            }

        }

    }

    public static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);

            org.apache.http.conn.ssl.SSLSocketFactory sf = new com.payment.simaspay.services.MySSLSocketFactory(
                    trustStore);
            sf.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
}
