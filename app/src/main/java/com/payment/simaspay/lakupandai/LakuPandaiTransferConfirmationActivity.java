package com.payment.simaspay.lakupandai;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.AgentTransfer.TranaferSuccessActivity;
import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 2/3/2016.
 */
public class LakuPandaiTransferConfirmationActivity extends Activity {

    TextView title, heading, name, name_field, number, number_field, amount, amount_field, products, product_field;

    Button cancel, confirmation;

    LinearLayout back;

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
        products = (TextView) findViewById(R.id.products);
        product_field = (TextView) findViewById(R.id.other_products);

        product_field.setVisibility(View.VISIBLE);
        products.setVisibility(View.VISIBLE);

        name.setText("Nama Pemilik Rekening");
        name_field.setText("BAYU SANTOSO");
        products.setText("Bank Tujuan");
        product_field.setText("Bank Sinarmas");
        number.setText("Nomor Rekening Tujuan");
        number_field.setText("08881234567");
        amount.setText("Jumlah");
        amount_field.setText("Rp 2.500.000");

        products.setVisibility(View.GONE);
        product_field.setVisibility(View.GONE);


        cancel = (Button) findViewById(R.id.cancel);
        confirmation = (Button) findViewById(R.id.next);

        back = (LinearLayout) findViewById(R.id.back_layout);

        title.setTypeface(Utility.Robot_Regular(LakuPandaiTransferConfirmationActivity.this));
        heading.setTypeface(Utility.Robot_Regular(LakuPandaiTransferConfirmationActivity.this));
        name.setTypeface(Utility.Robot_Regular(LakuPandaiTransferConfirmationActivity.this));
        name_field.setTypeface(Utility.Robot_Light(LakuPandaiTransferConfirmationActivity.this));
        number.setTypeface(Utility.Robot_Regular(LakuPandaiTransferConfirmationActivity.this));
        number_field.setTypeface(Utility.Robot_Light(LakuPandaiTransferConfirmationActivity.this));
        amount.setTypeface(Utility.Robot_Regular(LakuPandaiTransferConfirmationActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(LakuPandaiTransferConfirmationActivity.this));
        products.setTypeface(Utility.Robot_Regular(LakuPandaiTransferConfirmationActivity.this));
        product_field.setTypeface(Utility.Robot_Light(LakuPandaiTransferConfirmationActivity.this));
        cancel.setTypeface(Utility.Robot_Regular(LakuPandaiTransferConfirmationActivity.this));
        confirmation.setTypeface(Utility.Robot_Regular(LakuPandaiTransferConfirmationActivity.this));

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
                SMSAlert("");
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

    Context context;
    Dialog dialogCustomWish;

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

        EditText editText = (EditText) dialogCustomWish.findViewById(R.id.otpCode);
        editText.setHint("6 digit kode OTP");
        textView_1.setTypeface(Utility.Robot_Regular(context));


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogCustomWish.dismiss();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCustomWish.dismiss();
                Intent intent = new Intent(LakuPandaiTransferConfirmationActivity.this, LakuPandaiTransferSuccessActivity.class);
                startActivityForResult(intent, 10);
            }
        });
        dialogCustomWish.show();


    }
}
