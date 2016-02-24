package com.payment.simaspay.AgentTransfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.payment.simaspay.services.Utility;

import java.util.ArrayList;

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

    void AddData() {
        BanksData banksData = new BanksData();
        banksData.setBankName("Bank Bukopin");
        banksData.setBankId("1");
        arrayList.add(banksData);

        banksData = new BanksData();
        banksData.setBankName("BCA");
        banksData.setBankId("2");
        arrayList.add(banksData);

        banksData = new BanksData();
        banksData.setBankName("Bank CIMB Niaga");
        banksData.setBankId("3");
        arrayList.add(banksData);

        banksData = new BanksData();
        banksData.setBankName("Bank Danamon");
        banksData.setBankId("4");
        arrayList.add(banksData);

        banksData = new BanksData();
        banksData.setBankName("Bank Hana");
        banksData.setBankId("5");
        arrayList.add(banksData);
        banksData = new BanksData();

        banksData.setBankName("Bank ICBC Indonesia");
        banksData.setBankId("6");
        arrayList.add(banksData);
        banksData = new BanksData();

        banksData.setBankName("Bank Index Selindo");
        banksData.setBankId("7");
        arrayList.add(banksData);
        banksData = new BanksData();

        banksData.setBankName("Bank Maybank Indonesia");
        banksData.setBankId("8");
        arrayList.add(banksData);
        banksData = new BanksData();

        banksData.setBankName("Bank Maspion");
        banksData.setBankId("9");
        arrayList.add(banksData);

        banksData.setBankName("Bank Mayapada");
        banksData.setBankId("10");
        arrayList.add(banksData);


    }


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

        AddData();

        backlayLayout = (LinearLayout) findViewById(R.id.back_layout);

        backlayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title.setTypeface(Utility.Robot_Regular(BankDetailsActivity.this));

        banksAdapter = new BanksAdapter();

        bankList.setAdapter(banksAdapter);

        bankList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (arrayList.get(i).getBankId() == "2") {
                    Intent intent = new Intent(BankDetailsActivity.this, TransferOtherBankDetailsActivity.class);
                    startActivityForResult(intent, 10);
                }
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
}
