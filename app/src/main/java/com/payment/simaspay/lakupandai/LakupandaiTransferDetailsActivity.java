package com.payment.simaspay.lakupandai;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.AgentTransfer.TransferConfirmationActivity;
import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 2/3/2016.
 */
public class LakupandaiTransferDetailsActivity extends Activity {

    TextView title, handphone, jumlah, mPin;

    Button submit;

    EditText number, amount, pin;

    LinearLayout btnBacke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transferdetails);
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

        title.setText("Transfer - Laku Pandai");

        handphone.setText("Nomor Handphone Tujuan");

        submit = (Button) findViewById(R.id.submit);

        number = (EditText) findViewById(R.id.number);
        amount = (EditText) findViewById(R.id.amount);
        pin = (EditText) findViewById(R.id.pin);

        btnBacke = (LinearLayout) findViewById(R.id.back_layout);

        title.setTypeface(Utility.Robot_Regular(LakupandaiTransferDetailsActivity.this));
        handphone.setTypeface(Utility.HelveticaNeue_Medium(LakupandaiTransferDetailsActivity.this));
        jumlah.setTypeface(Utility.HelveticaNeue_Medium(LakupandaiTransferDetailsActivity.this));
        mPin.setTypeface(Utility.HelveticaNeue_Medium(LakupandaiTransferDetailsActivity.this));
        submit.setTypeface(Utility.Robot_Regular(LakupandaiTransferDetailsActivity.this));
        number.setTypeface(Utility.Robot_Light(LakupandaiTransferDetailsActivity.this));
        amount.setTypeface(Utility.Robot_Light(LakupandaiTransferDetailsActivity.this));
        pin.setTypeface(Utility.Robot_Light(LakupandaiTransferDetailsActivity.this));


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LakupandaiTransferDetailsActivity.this, LakuPandaiTransferConfirmationActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        btnBacke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        amount.setText("Rp ");
        Selection.setSelection(amount.getText(), amount.getText().length());


        amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().contains("Rp ")) {
                    amount.setText("Rp ");
                    Selection.setSelection(amount.getText(), amount.getText().length());

                }

            }
        });


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
