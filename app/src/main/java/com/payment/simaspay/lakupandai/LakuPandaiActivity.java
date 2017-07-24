package com.payment.simaspay.lakupandai;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.AgentTransfer.TransferHomeActivity;
import com.payment.simaspay.Cash_InOut.CashOutDetailsActivity;
import com.payment.simaspay.FlashizSDK.PayByQRActivity;
import com.payment.simaspay.FlashizSDK.UserAPikeyCall;
import com.payment.simaspay.PaymentPurchaseAccount.PaymentAndPurchaseAccountTypeActivity;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.userdetails.BalanceSheetActivity;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/13/2016.
 */

public class LakuPandaiActivity extends Activity {

    TextView simas, pay, name, number, logout_text;


    TextView transfer_text, pembelian_text, pembayaran_text, traiktunai_text, flashiz_text, rekening_text;

    LinearLayout transfer, pembelian, pembayaran, tariktunai, flashiz, rekening, logOut, value_layout;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
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

        value_layout = (LinearLayout) findViewById(R.id.value_layout);

        tariktunai.setVisibility(View.VISIBLE);

        transfer_text.setTypeface(Utility.LightTextFormat(LakuPandaiActivity.this));
        pembelian_text.setTypeface(Utility.LightTextFormat(LakuPandaiActivity.this));
        pembayaran_text.setTypeface(Utility.LightTextFormat(LakuPandaiActivity.this));
        traiktunai_text.setTypeface(Utility.LightTextFormat(LakuPandaiActivity.this));
        rekening_text.setTypeface(Utility.LightTextFormat(LakuPandaiActivity.this));
        flashiz_text.setTypeface(Utility.LightTextFormat(LakuPandaiActivity.this));

        name.setTypeface(Utility.LightTextFormat(LakuPandaiActivity.this));
        number.setTypeface(Utility.OpemSans_Light(LakuPandaiActivity.this));
        logout_text.setTypeface(Utility.LightTextFormat(LakuPandaiActivity.this));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(15, 15, 15, 15);

        number.setText(sharedPreferences.getString("mobileNumber", ""));
//        name.setText(sharedPreferences.getString("userName", ""));
        String user;
        if (sharedPreferences.getString("userName","").length() >= 15) {
            user = sharedPreferences.getString("userName","").substring(0, 15)+ "...";
        } else {
            user = sharedPreferences.getString("userName","");
        }
        name.setText(user);

        value_layout.setLayoutParams(layoutParams);


        logOut = (LinearLayout) findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LakuPandaiActivity.this, TransferHomeActivity.class);
                intent.putExtra("simaspayuser", false);
                intent.putExtra("agentornot", false);
                startActivityForResult(intent, 20);
            }
        });

        pembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LakuPandaiActivity.this, PaymentAndPurchaseAccountTypeActivity.class);
                intent.putExtra("accounttype", false);
                startActivity(intent);
            }
        });
        pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LakuPandaiActivity.this, PaymentAndPurchaseAccountTypeActivity.class);
                intent.putExtra("accounttype", true);
                startActivity(intent);
            }
        });
        tariktunai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LakuPandaiActivity.this, CashOutDetailsActivity.class);
                startActivity(intent);

            }
        });
        rekening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LakuPandaiActivity.this, BalanceSheetActivity.class);
                intent.putExtra("simaspayuser", false);
                intent.putExtra("transfer_or_mutasi", true);
                intent.putExtra("red_or_white", false);
                intent.putExtra("lakupandai", true);
                startActivityForResult(intent, 20);
            }
        });
        flashiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("------","------"+sharedPreferences.getString("userApiKey",""));
                if (sharedPreferences.getString("userApiKey", "").equalsIgnoreCase("")) {
                    UserAPikeyCall.UserAPikeyCall(LakuPandaiActivity.this);
                } else {
                    Intent intent = new Intent(LakuPandaiActivity.this, PayByQRActivity.class);
                    startActivity(intent);
                }
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

