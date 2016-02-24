package com.payment.simaspay.PaymentPerchaseAccount;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.payment.simaspay.PojoClasses.AccountTypeClass;
import com.payment.simaspay.services.Utility;

import java.util.ArrayList;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/28/2016.
 */
public class PaymentAndPerchaseAccountTypeActivity extends Activity {

    TextView title;

    LinearLayout back;

    ListView listView;

    AccountTypeAdapter accountTypeAdapter;

    ArrayList<AccountTypeClass> arrayList = new ArrayList<>();

    void AddAccountDetails(boolean value) {
        if(value) {
            AccountTypeClass accountTypeClass = new AccountTypeClass();
            accountTypeClass.setId("1");
            accountTypeClass.setName("Tagihan Handphone");
            arrayList.add(accountTypeClass);

            accountTypeClass = new AccountTypeClass();
            accountTypeClass.setId("2");
            accountTypeClass.setName("Telepon");
            arrayList.add(accountTypeClass);

            accountTypeClass = new AccountTypeClass();
            accountTypeClass.setId("3");
            accountTypeClass.setName("Asuransi");
            arrayList.add(accountTypeClass);

            accountTypeClass = new AccountTypeClass();
            accountTypeClass.setId("4");
            accountTypeClass.setName("Cicilan");
            arrayList.add(accountTypeClass);

            accountTypeClass = new AccountTypeClass();
            accountTypeClass.setId("5");
            accountTypeClass.setName("Tiket");
            arrayList.add(accountTypeClass);

            accountTypeClass = new AccountTypeClass();
            accountTypeClass.setId("6");
            accountTypeClass.setName("TV Kabel");
            arrayList.add(accountTypeClass);

            accountTypeClass = new AccountTypeClass();
            accountTypeClass.setId("7");
            accountTypeClass.setName("Internet");
            arrayList.add(accountTypeClass);

            accountTypeClass = new AccountTypeClass();
            accountTypeClass.setId("8");
            accountTypeClass.setName("Lainnya");
            arrayList.add(accountTypeClass);
        }else{
            AccountTypeClass accountTypeClass = new AccountTypeClass();
            accountTypeClass.setId("1");
            accountTypeClass.setName("Isi Ulang Pulsa");
            arrayList.add(accountTypeClass);

            accountTypeClass = new AccountTypeClass();
            accountTypeClass.setId("2");
            accountTypeClass.setName("Voucher TV Kabel");
            arrayList.add(accountTypeClass);

            accountTypeClass = new AccountTypeClass();
            accountTypeClass.setId("3");
            accountTypeClass.setName("Lainnya");
            arrayList.add(accountTypeClass);
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
        title = (TextView) findViewById(R.id.titled);
        title.setTypeface(Utility.Robot_Regular(PaymentAndPerchaseAccountTypeActivity.this));

        if(getIntent().getExtras().getBoolean("accounttype")){
            title.setText("Pembayaran");
        }else{
            title.setText("Pembelian");
        }

        back = (LinearLayout) findViewById(R.id.back_layout);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.account_type);

        accountTypeAdapter = new AccountTypeAdapter();
        listView.setAdapter(accountTypeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (arrayList.get(i).getId() == "1") {
                    Intent intent = new Intent(PaymentAndPerchaseAccountTypeActivity.this, BillersListActivity.class);
                    intent.putExtra("accounttype",getIntent().getExtras().getBoolean("accounttype"));
                    startActivityForResult(intent, 10);
                }
            }
        });


    }

    class AccountTypeAdapter extends BaseAdapter {

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
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View view1 = LayoutInflater.from(PaymentAndPerchaseAccountTypeActivity.this).inflate(R.layout.textviewdata, null);
            TextView textView = (TextView) view1.findViewById(R.id.textviewdata_text);

            textView.setText(arrayList.get(i).getName());
            textView.setTypeface(Utility.Robot_Regular(PaymentAndPerchaseAccountTypeActivity.this));
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
}
