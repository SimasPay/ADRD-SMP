package com.payment.simaspay.Cash_InOut;

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
 * Created by Nagendra P on 1/27/2016.
 */
public class CashInSuccessActivity extends Activity {

    TextView title, heading, name, name_field, number, number_field, amount, amount_field;

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

        ok = (Button) findViewById(R.id.ok);

        title.setTypeface(Utility.Robot_Regular(CashInSuccessActivity.this));
        heading.setTypeface(Utility.Robot_Regular(CashInSuccessActivity.this));
        name.setTypeface(Utility.Robot_Regular(CashInSuccessActivity.this));
        name_field.setTypeface(Utility.Robot_Light(CashInSuccessActivity.this));
        number.setTypeface(Utility.Robot_Regular(CashInSuccessActivity.this));
        number_field.setTypeface(Utility.Robot_Light(CashInSuccessActivity.this));
        amount.setTypeface(Utility.Robot_Regular(CashInSuccessActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(CashInSuccessActivity.this));
        ok.setTypeface(Utility.Robot_Regular(CashInSuccessActivity.this));

        amount_field.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize));
        number_field.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize));
        name_field.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize));

        name_field.setText(getIntent().getExtras().getString("Name"));
        number_field.setText(getIntent().getExtras().getString("DestMDN"));
        amount_field.setText("Rp. "+getIntent().getExtras().getString("amount"));


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
