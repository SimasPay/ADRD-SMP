package com.payment.simaspay.Referral_and_CloseAccount;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/27/2016.
 */
public class CloseAccountConfirmationActivity extends Activity {
    TextView title, heading, name, name_field, number, number_field, amount, amount_field;

    Button cancel, confirmation;

    LinearLayout back;

    Context context;
    Dialog dialogCustomWish;
    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
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

        number.setText("Nomor Rekening");

        amount.setVisibility(View.GONE);
        amount_field.setVisibility(View.GONE);

        cancel = (Button) findViewById(R.id.cancel);
        confirmation = (Button) findViewById(R.id.next);

        back = (LinearLayout) findViewById(R.id.back_layout);

        title.setTypeface(Utility.Robot_Regular(CloseAccountConfirmationActivity.this));
        heading.setTypeface(Utility.Robot_Regular(CloseAccountConfirmationActivity.this));
        name.setTypeface(Utility.Robot_Regular(CloseAccountConfirmationActivity.this));
        name_field.setTypeface(Utility.Robot_Light(CloseAccountConfirmationActivity.this));
        number.setTypeface(Utility.Robot_Regular(CloseAccountConfirmationActivity.this));
        number_field.setTypeface(Utility.Robot_Light(CloseAccountConfirmationActivity.this));
        amount.setTypeface(Utility.Robot_Regular(CloseAccountConfirmationActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(CloseAccountConfirmationActivity.this));
        cancel.setTypeface(Utility.Robot_Regular(CloseAccountConfirmationActivity.this));
        confirmation.setTypeface(Utility.Robot_Regular(CloseAccountConfirmationActivity.this));

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
                if (i == 0) {
                    SMSAlert("");
                }else if(i==1){

                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i == 0) {
                    Intent intent = getIntent();
                    setResult(11, intent);
                    finish();
                } else {
                    cancel.setVisibility(View.VISIBLE);
                    heading.setText("Pastikan data berikut sudah benar");
                    heading.setTextColor(getResources().getColor(R.color.black));
                    i = 0;

                }
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

    public void SMSAlert(final String string) {

        dialogCustomWish = new Dialog(context);
        dialogCustomWish.setCancelable(false);

        dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        View view = LayoutInflater.from(context).inflate(R.layout.sms_alert, null);
        dialogCustomWish.setContentView(R.layout.sms_alert);

        Button button = (Button) dialogCustomWish.findViewById(R.id.ok);
        Button button1 = (Button) dialogCustomWish.findViewById(R.id.Cancel);
        TextView textView = (TextView) dialogCustomWish.findViewById(R.id.number);
        TextView textView_1 = (TextView) dialogCustomWish.findViewById(R.id.number_1);
        button.setTypeface(Utility.RegularTextFormat(context));
        button1.setTypeface(Utility.RegularTextFormat(context));
        textView.setTypeface(Utility.RegularTextFormat(context));
        textView_1.setTypeface(Utility.Robot_Regular(context));


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogCustomWish.dismiss();
               /* dialogCustomWish.dismiss();
                confirmation.setText("Kembali");
                cancel.setVisibility(View.GONE);
                heading.setText("Rekening hanya dapat ditutup jika saldo sudah di bawah Rp 100.");
                heading.setTextColor(getResources().getColor(R.color.reg));
                i++;*/

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CloseAccountConfirmationActivity.this, CloseAccountSuccessActivity.class);
                startActivityForResult(intent, 10);
            }
        });
        dialogCustomWish.show();


    }

}
