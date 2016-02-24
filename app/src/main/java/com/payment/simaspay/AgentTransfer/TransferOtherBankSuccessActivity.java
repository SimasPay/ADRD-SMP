package com.payment.simaspay.AgentTransfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 2/3/2016.
 */
public class TransferOtherBankSuccessActivity extends Activity {

    TextView title, heading, id_field, id_value, name, name_field, bank, bank_field, number, number_field, amount, amount_field, charge, charge_field, total, total_field;

    LinearLayout backlayout;

    Button confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transferotherbanksuccess);

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
        bank = (TextView) findViewById(R.id.bank);
        bank_field = (TextView) findViewById(R.id.bank_field);
        number = (TextView) findViewById(R.id.number);
        number_field = (TextView) findViewById(R.id.number_field);
        amount = (TextView) findViewById(R.id.amount);
        amount_field = (TextView) findViewById(R.id.amount_field);
        charge = (TextView) findViewById(R.id.charge);
        charge_field = (TextView) findViewById(R.id.charge_field);
        total = (TextView) findViewById(R.id.total);
        total_field = (TextView) findViewById(R.id.total_field);

        id_field = (TextView) findViewById(R.id.transfer_field);
        id_value = (TextView) findViewById(R.id.transferID);

        backlayout = (LinearLayout) findViewById(R.id.back_layout);

        backlayout.setVisibility(View.GONE);

        backlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        heading.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        name.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        name_field.setTypeface(Utility.Robot_Light(TransferOtherBankSuccessActivity.this));
        bank.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        bank_field.setTypeface(Utility.Robot_Light(TransferOtherBankSuccessActivity.this));
        number.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        number_field.setTypeface(Utility.Robot_Light(TransferOtherBankSuccessActivity.this));
        amount.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(TransferOtherBankSuccessActivity.this));
        charge.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        charge_field.setTypeface(Utility.Robot_Light(TransferOtherBankSuccessActivity.this));
        total.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        total_field.setTypeface(Utility.Robot_Light(TransferOtherBankSuccessActivity.this));

        id_value.setTypeface(Utility.Robot_Light(TransferOtherBankSuccessActivity.this));
        id_field.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));

        confirm = (Button) findViewById(R.id.next);

        confirm.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
