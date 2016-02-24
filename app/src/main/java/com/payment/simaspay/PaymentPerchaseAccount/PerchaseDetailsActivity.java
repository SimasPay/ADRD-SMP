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
 * Created by Nagendra P on 1/29/2016.
 */
public class PerchaseDetailsActivity extends Activity {

    TextView title,pulsa_field, product, number, pin, product_field, number_field, pin_field;

    Button submit,nominal_pulsa;

    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchasedetails);

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

        pulsa_field=(TextView)findViewById(R.id.pulsa);

        nominal_pulsa=(Button)findViewById(R.id.pulsa_field);

        title.setTypeface(Utility.Robot_Regular(PerchaseDetailsActivity.this));
        product.setTypeface(Utility.Robot_Regular(PerchaseDetailsActivity.this));
        product_field.setTypeface(Utility.Robot_Light(PerchaseDetailsActivity.this));
        number.setTypeface(Utility.Robot_Regular(PerchaseDetailsActivity.this));
        number_field.setTypeface(Utility.Robot_Light(PerchaseDetailsActivity.this));
        pin.setTypeface(Utility.Robot_Regular(PerchaseDetailsActivity.this));
        pin_field.setTypeface(Utility.Robot_Light(PerchaseDetailsActivity.this));
        pulsa_field.setTypeface(Utility.Robot_Regular(PerchaseDetailsActivity.this));
        nominal_pulsa.setTypeface(Utility.Robot_Light(PerchaseDetailsActivity.this));

        product_field.setText("Isi Ulang Pulsa - Smartfren");
        product_field.setEnabled(false);
        product_field.setClickable(false);

        title.setText("Pembelian");

        back=(LinearLayout)findViewById(R.id.back_layout);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit=(Button)findViewById(R.id.submit);

        submit.setTypeface(Utility.Robot_Regular(PerchaseDetailsActivity.this));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PerchaseDetailsActivity.this,PerchaseConfirmationActivity.class);
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
