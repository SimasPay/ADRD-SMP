package com.payment.simaspay.Referral_and_CloseAccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
public class ReferralAccountSuccessActivity extends Activity {

    TextView title, heading, name, name_field, number, number_field, amount, amount_field,products,other_products;

    Button ok;

    String mdn, NameField, email_field, otherfield, productDesired;

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
        products = (TextView) findViewById(R.id.products);
        other_products = (TextView) findViewById(R.id.other_products);

        products.setVisibility(View.VISIBLE);
        other_products.setVisibility(View.VISIBLE);


        mdn = getIntent().getExtras().getString("mdn");
        NameField = getIntent().getExtras().getString("NameField");
        email_field = getIntent().getExtras().getString("email_field");
        otherfield = getIntent().getExtras().getString("otherfield");
        productDesired = getIntent().getExtras().getString("productDesired");

        ok = (Button) findViewById(R.id.ok);

        heading.setText("Referral Berhasil");

        name.setText("Nama Lengkap");
        name_field.setText(NameField);

        number.setText("Nomor Handphone");
        number_field.setText(mdn);

        amount.setText("E-mail");
        amount_field.setText(email_field);

        products.setText("Produk yang Diinginkan");
        if(productDesired.equals("Lainnya")){
            other_products.setText(productDesired+" - "+ otherfield);
        }else{
            other_products.setText(productDesired);
        }

        title.setTypeface(Utility.Robot_Regular(ReferralAccountSuccessActivity.this));
        heading.setTypeface(Utility.Robot_Regular(ReferralAccountSuccessActivity.this));
        name.setTypeface(Utility.Robot_Regular(ReferralAccountSuccessActivity.this));
        name_field.setTypeface(Utility.Robot_Light(ReferralAccountSuccessActivity.this));
        number.setTypeface(Utility.Robot_Regular(ReferralAccountSuccessActivity.this));
        number_field.setTypeface(Utility.Robot_Light(ReferralAccountSuccessActivity.this));
        amount.setTypeface(Utility.Robot_Regular(ReferralAccountSuccessActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(ReferralAccountSuccessActivity.this));
        products.setTypeface(Utility.Robot_Regular(ReferralAccountSuccessActivity.this));
        other_products.setTypeface(Utility.Robot_Light(ReferralAccountSuccessActivity.this));
        ok.setTypeface(Utility.Robot_Regular(ReferralAccountSuccessActivity.this));

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
