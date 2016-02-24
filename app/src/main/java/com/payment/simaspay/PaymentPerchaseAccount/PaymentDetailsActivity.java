package com.payment.simaspay.PaymentPerchaseAccount;

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
public class PaymentDetailsActivity extends Activity {

    TextView title, product, number, pin, product_field, number_field, pin_field;

    Button submit;

    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentdetails);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        title = (TextView) findViewById(R.id.titled);
        product = (TextView) findViewById(R.id.name_product);
        product_field = (TextView) findViewById(R.id.product_field);
        number = (TextView) findViewById(R.id.number);
        number_field = (TextView) findViewById(R.id.number_field);
        pin = (TextView) findViewById(R.id.mPin);
        pin_field = (TextView) findViewById(R.id.pin);

        title.setTypeface(Utility.Robot_Regular(PaymentDetailsActivity.this));
        product.setTypeface(Utility.Robot_Regular(PaymentDetailsActivity.this));
        product_field.setTypeface(Utility.Robot_Light(PaymentDetailsActivity.this));
        number.setTypeface(Utility.Robot_Regular(PaymentDetailsActivity.this));
        number_field.setTypeface(Utility.Robot_Light(PaymentDetailsActivity.this));
        pin.setTypeface(Utility.Robot_Regular(PaymentDetailsActivity.this));
        pin_field.setTypeface(Utility.Robot_Light(PaymentDetailsActivity.this));

        product_field.setText("Tagihan Handphone - Smartfren");
        product_field.setEnabled(false);
        product_field.setClickable(false);

        back=(LinearLayout)findViewById(R.id.back_layout);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit=(Button)findViewById(R.id.submit);

        submit.setTypeface(Utility.Robot_Regular(PaymentDetailsActivity.this));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PaymentDetailsActivity.this,PaymentConfirmationActivity.class);
                startActivityForResult(intent,10);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(resultCode==10){
                Intent intent=getIntent();
                setResult(10,intent);
                finish();
            }
        }
    }
}
