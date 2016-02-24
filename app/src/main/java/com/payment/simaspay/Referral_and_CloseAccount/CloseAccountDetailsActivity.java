package com.payment.simaspay.Referral_and_CloseAccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/27/2016.
 */
public class CloseAccountDetailsActivity extends Activity {

    TextView title, handphone, jumlah, mPin;

    Button submit;

    EditText number, amount, pin;

    LinearLayout btnBacke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setortunaidetails);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        title = (TextView) findViewById(R.id.titled);
        handphone = (TextView) findViewById(R.id.handphone);
        jumlah = (TextView) findViewById(R.id.jumlah);
        mPin = (TextView) findViewById(R.id.mPin);

        submit = (Button) findViewById(R.id.submit);

        title.setText("Tutup Rekening");
        handphone.setText("Nomor Rekening");


        number = (EditText) findViewById(R.id.number);
        amount = (EditText) findViewById(R.id.amount);
        pin = (EditText) findViewById(R.id.pin);

        jumlah.setVisibility(View.GONE);
        mPin.setVisibility(View.GONE);
        amount.setVisibility(View.GONE);
        pin.setVisibility(View.GONE);

        btnBacke = (LinearLayout) findViewById(R.id.back_layout);

        title.setTypeface(Utility.Robot_Regular(CloseAccountDetailsActivity.this));
        handphone.setTypeface(Utility.HelveticaNeue_Medium(CloseAccountDetailsActivity.this));
        jumlah.setTypeface(Utility.HelveticaNeue_Medium(CloseAccountDetailsActivity.this));
        mPin.setTypeface(Utility.HelveticaNeue_Medium(CloseAccountDetailsActivity.this));
        submit.setTypeface(Utility.Robot_Regular(CloseAccountDetailsActivity.this));
        number.setTypeface(Utility.Robot_Light(CloseAccountDetailsActivity.this));
        amount.setTypeface(Utility.Robot_Light(CloseAccountDetailsActivity.this));
        pin.setTypeface(Utility.Robot_Light(CloseAccountDetailsActivity.this));


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CloseAccountDetailsActivity.this, CloseAccountConfirmationActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        btnBacke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(resultCode==10){
                finish();
            }
        }
    }
}
