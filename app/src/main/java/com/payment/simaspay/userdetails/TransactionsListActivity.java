package com.payment.simaspay.userdetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.TextView;

import com.payment.simaspay.PojoClasses.TransactionsData;
import com.payment.simaspay.services.Utility;

import java.util.ArrayList;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/29/2016.
 */
public class TransactionsListActivity extends Activity {

    TextView title, period, terms_conditions_1;

    Button ok;

    TransactionsAdapter transactionsAdapter;

    LinearLayout back_layout, dwn_layout;
    ArrayList<TransactionsData> arrayList = new ArrayList<>();

    ListView listView;

    void AddData() {

        TransactionsData transactionsData = new TransactionsData();
        transactionsData.setDate_time("06/04/2015");
        transactionsData.setAmount("Rp 500.000");
        transactionsData.setTransactionData("Transfer");
        transactionsData.setType("credit");
        arrayList.add(transactionsData);

        transactionsData = new TransactionsData();
        transactionsData.setDate_time("06/04/2015");
        transactionsData.setAmount("Rp 500.000");
        transactionsData.setTransactionData("Bill Payme");
        transactionsData.setType("debit");
        arrayList.add(transactionsData);

        transactionsData = new TransactionsData();
        transactionsData.setDate_time("06/04/2015");
        transactionsData.setAmount("Rp 500.000");
        transactionsData.setTransactionData("ATM Withdr");
        transactionsData.setType("debit");
        arrayList.add(transactionsData);

        transactionsData = new TransactionsData();
        transactionsData.setDate_time("06/04/2015");
        transactionsData.setAmount("Rp 500.000");
        transactionsData.setTransactionData("Transfer");
        transactionsData.setType("credit");
        arrayList.add(transactionsData);

        transactionsData = new TransactionsData();
        transactionsData.setDate_time("06/04/2015");
        transactionsData.setAmount("Rp 500.000");
        transactionsData.setTransactionData("ATM Withdr");
        transactionsData.setType("debit");
        arrayList.add(transactionsData);

        transactionsData = new TransactionsData();
        transactionsData.setDate_time("06/04/2015");
        transactionsData.setAmount("Rp 500.000");
        transactionsData.setTransactionData("ATM Withdr");
        transactionsData.setType("debit");
        arrayList.add(transactionsData);


    }

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
        AddData();
        title = (TextView) findViewById(R.id.title);
        period = (TextView) findViewById(R.id.terms_conditions);
        terms_conditions_1 = (TextView) findViewById(R.id.terms_conditions_1);


        listView = (ListView) findViewById(R.id.listView);

        ok = (Button) findViewById(R.id.accept);

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        dwn_layout = (LinearLayout) findViewById(R.id.download_layout);

        if (getIntent().getExtras().getBoolean("lakupandai")) {
            dwn_layout.setVisibility(View.VISIBLE);
            title.setText("Mutasi");
            findViewById(R.id.period).setVisibility(View.VISIBLE);
        } else {
            dwn_layout.setVisibility(View.GONE);
            title.setText("Transaksi");
            findViewById(R.id.period).setVisibility(View.GONE);
        }

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        title.setTypeface(Utility.Robot_Regular(TransactionsListActivity.this));
        period.setTypeface(Utility.Robot_Regular(TransactionsListActivity.this));
        terms_conditions_1.setTypeface(Utility.Robot_Light(TransactionsListActivity.this));

        ok.setTypeface(Utility.Robot_Regular(TransactionsListActivity.this));

        transactionsAdapter = new TransactionsAdapter();
        listView.setAdapter(transactionsAdapter);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    class TransactionsAdapter extends BaseAdapter {
        @Override
        public Object getItem(int i) {
            return 0;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View view1 = LayoutInflater.from(TransactionsListActivity.this).inflate(R.layout.transacton, null);
            TextView textView = (TextView) view1.findViewById(R.id.date);
            TextView textView1 = (TextView) view1.findViewById(R.id.type);
            TextView textView2 = (TextView) view1.findViewById(R.id.credit_or_debit);
            TextView textView3 = (TextView) view1.findViewById(R.id.amount);

            textView.setTypeface(Utility.Robot_Light(TransactionsListActivity.this));
            textView1.setTypeface(Utility.Robot_Regular(TransactionsListActivity.this));
            textView2.setTypeface(Utility.Robot_Light(TransactionsListActivity.this));
            textView3.setTypeface(Utility.Robot_Regular(TransactionsListActivity.this));

            if (arrayList.get(i).getType().equals("credit")) {
                textView2.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_color));
            } else {
                textView2.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_blue_color));
            }

            textView.setText(arrayList.get(i).getDate_time());
            textView1.setText(arrayList.get(i).getTransactionData());
            textView2.setText(arrayList.get(i).getType());
            textView3.setText(arrayList.get(i).getAmount());

            return view1;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
