package com.payment.simaspay.userdetails;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.AgentTransfer.TransferHomeActivity;
import com.payment.simaspay.FlashizSDK.PayByQRActivity;
import com.payment.simaspay.PaymentPerchaseAccount.PaymentAndPerchaseAccountTypeActivity;
import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.LoginScreenActivity;
import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 12/29/2015.
 */
public class SimaspayUserActivity extends Activity {

    TextView simas, pay, name, number, logout_text;


    TextView transfer_text, pembelian_text, pembayaran_text, traiktunai_text, flashiz_text, rekening_text;

    LinearLayout transfer, pembelian, pembayaran, tariktunai, flashiz, rekening, logOut;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = getIntent();
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simas_pay_home);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }


        simas = (TextView) findViewById(R.id.simas);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String s = "simaspay";
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1f), 0, 5, 0);
        ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);

        simas.setText(ss1);


        transfer_text = (TextView) findViewById(R.id.tranfer_text);
        pembelian_text = (TextView) findViewById(R.id.pembelian_text);
        pembayaran_text = (TextView) findViewById(R.id.pembayaran_text);
        traiktunai_text = (TextView) findViewById(R.id.tariktunai_text);
        flashiz_text = (TextView) findViewById(R.id.flashiz_text);
        rekening_text = (TextView) findViewById(R.id.rekening_text);

        name = (TextView) findViewById(R.id.name);
        number = (TextView) findViewById(R.id.number);
        logout_text = (TextView) findViewById(R.id.text_logout);

        transfer = (LinearLayout) findViewById(R.id.tranfer);
        pembelian = (LinearLayout) findViewById(R.id.pembelian);
        pembayaran = (LinearLayout) findViewById(R.id.pembayaran);
        tariktunai = (LinearLayout) findViewById(R.id.tariktunai);
        flashiz = (LinearLayout) findViewById(R.id.flashiz);
        rekening = (LinearLayout) findViewById(R.id.rekening);

        transfer_text.setTypeface(Utility.LightTextFormat(SimaspayUserActivity.this));
        pembelian_text.setTypeface(Utility.LightTextFormat(SimaspayUserActivity.this));
        pembayaran_text.setTypeface(Utility.LightTextFormat(SimaspayUserActivity.this));
        traiktunai_text.setTypeface(Utility.LightTextFormat(SimaspayUserActivity.this));
        rekening_text.setTypeface(Utility.LightTextFormat(SimaspayUserActivity.this));
        flashiz_text.setTypeface(Utility.LightTextFormat(SimaspayUserActivity.this));

        name.setTypeface(Utility.LightTextFormat(SimaspayUserActivity.this));
        number.setTypeface(Utility.OpemSans_Light(SimaspayUserActivity.this));
        logout_text.setTypeface(Utility.LightTextFormat(SimaspayUserActivity.this));

        number.setText(sharedPreferences.getString("accountnumber", ""));
        String user;
        if (sharedPreferences.getString("userName","").length() >= 13) {
            user = sharedPreferences.getString("userName","").substring(0, 13)+ "...";
        } else {
            user = sharedPreferences.getString("userName","");
        }
        name.setText(user);

        logOut = (LinearLayout) findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(RESULT_OK,intent);
                finish();

            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(SimaspayUserActivity.this, TransferHomeActivity.class);
                intent.putExtra("simaspayuser", false);
                intent.putExtra("agentornot", false);
                startActivityForResult(intent, 20);
            }
        });

        pembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SimaspayUserActivity.this, PaymentAndPerchaseAccountTypeActivity.class);
                intent.putExtra("accounttype", false);
                startActivity(intent);
            }
        });
        pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SimaspayUserActivity.this, PaymentAndPerchaseAccountTypeActivity.class);
                intent.putExtra("accounttype", true);
                startActivity(intent);
            }
        });
        tariktunai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        rekening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SimaspayUserActivity.this, BalanceSheetActivity.class);
                intent.putExtra("simaspayuser", false);
                intent.putExtra("transfer_or_mutasi", false);
                intent.putExtra("lakupandai", false);
                intent.putExtra("red_or_white", false);
                startActivityForResult(intent, 20);
            }
        });
        flashiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SimaspayUserActivity.this, PayByQRActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
