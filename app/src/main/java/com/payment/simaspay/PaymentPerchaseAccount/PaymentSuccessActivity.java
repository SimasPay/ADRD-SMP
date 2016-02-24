package com.payment.simaspay.PaymentPerchaseAccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/28/2016.
 */
public class PaymentSuccessActivity extends Activity {
    TextView title, heading, name, name_field, number, number_field, amount, amount_field,transfer_field,transferID;

    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commonsuccess);
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

        transferID=(TextView)findViewById(R.id.transferID);
        transfer_field=(TextView)findViewById(R.id.transfer_field);

        ok = (Button) findViewById(R.id.ok);

        heading.setText("Pembayaran Berhasil!");
        name.setText("Nama Produk");
        name_field.setText("Tagihan Handphone - Smartfren");
        number.setText("Nomor Handphone");
        number_field.setText("08881234568");
        amount.setText("Jumlah");
        amount_field.setText("Rp 154.200");

        amount_field.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize));
        number_field.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize));
        name_field.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize));


        findViewById(R.id.ID_layout).setVisibility(View.VISIBLE);

        title.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));
        heading.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));
        name.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));
        name_field.setTypeface(Utility.Robot_Light(PaymentSuccessActivity.this));
        number.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));
        number_field.setTypeface(Utility.Robot_Light(PaymentSuccessActivity.this));
        amount.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(PaymentSuccessActivity.this));
        transfer_field.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));
        transferID.setTypeface(Utility.Robot_Light(PaymentSuccessActivity.this));
        ok.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(10, intent);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        setResult(10, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = getIntent();
            setResult(10, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
