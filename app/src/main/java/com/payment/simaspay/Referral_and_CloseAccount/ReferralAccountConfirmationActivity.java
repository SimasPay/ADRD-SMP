package com.payment.simaspay.Referral_and_CloseAccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/28/2016.
 */
public class ReferralAccountConfirmationActivity extends Activity {


    TextView title, heading, name, name_field, number, number_field, amount, amount_field, products, products_field;

    Button cancel, confirmation;

    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commonconfirmation);
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
        products_field = (TextView) findViewById(R.id.other_products);

        products.setVisibility(View.VISIBLE);
        products_field.setVisibility(View.VISIBLE);

        cancel = (Button) findViewById(R.id.cancel);
        confirmation = (Button) findViewById(R.id.next);

        back = (LinearLayout) findViewById(R.id.back_layout);

        name.setText("Nama Lengkap");
        name_field.setText("PUTRI SETYANINGRUM");

        number.setText("Nomor Handphone");
        number_field.setText("08881234567");

        amount.setText("E-mail");
        amount_field.setText("putrisetyaningrum@gmail.com");

        products.setText("Produk yang Diinginkan");
        products_field.setText("Lainnya - Deposito");


        title.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));
        heading.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));
        name.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));
        name_field.setTypeface(Utility.Robot_Light(ReferralAccountConfirmationActivity.this));
        number.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));
        number_field.setTypeface(Utility.Robot_Light(ReferralAccountConfirmationActivity.this));
        amount.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(ReferralAccountConfirmationActivity.this));
        products.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));
        products_field.setTypeface(Utility.Robot_Light(ReferralAccountConfirmationActivity.this));
        cancel.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));
        confirmation.setTypeface(Utility.Robot_Regular(ReferralAccountConfirmationActivity.this));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(11, intent);
                finish();
            }
        });

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReferralAccountConfirmationActivity.this, ReferralAccountSuccessActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(11, intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        setResult(11, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 10) {
                Intent intent = getIntent();
                setResult(10, intent);
                finish();
            } else {

            }
        }
    }
}
