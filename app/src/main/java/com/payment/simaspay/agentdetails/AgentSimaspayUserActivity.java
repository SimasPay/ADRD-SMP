package com.payment.simaspay.agentdetails;

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

import com.payment.simaspay.FlashizSDK.PayByQRActivity;
import com.payment.simaspay.PaymentPurchaseAccount.PaymentAndPurchaseAccountTypeActivity;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.userdetails.BalanceSheetActivity;
import com.payment.simaspay.AgentTransfer.TransferHomeActivity;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 12/29/2015.
 */
public class AgentSimaspayUserActivity extends Activity {

    TextView simas, pay;


    TextView transfer_text, pembelian_text, pembayaran_text, traiktunai_text, flashiz_text, rekening_text, akun_textView, logout_textView, user_Name, User_Number;

    LinearLayout transfer, pembelian, pembayaran, tariktunai, flashiz, rekening, logOut, switchnumber_layout;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            } else {

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

       /* if(sharedPreferences.getBoolean("Logout",false)){
            editor.putBoolean("Logout",false).commit();
            Intent intent=new Intent(SimaspayUserActivity.this, LoginScreenActivity.class);
            startActivity(intent);
            finish();

        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agent_simaspay_user);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        simas = (TextView) findViewById(R.id.simas);


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

        user_Name = (TextView) findViewById(R.id.user_name);
        User_Number = (TextView) findViewById(R.id.user_number);

        akun_textView = (TextView) findViewById(R.id.ganti_akun_text);
        logout_textView = (TextView) findViewById(R.id.logout_text);

        transfer = (LinearLayout) findViewById(R.id.tranfer);
        pembelian = (LinearLayout) findViewById(R.id.pembelian);
        pembayaran = (LinearLayout) findViewById(R.id.pembayaran);
        tariktunai = (LinearLayout) findViewById(R.id.tariktunai);
        flashiz = (LinearLayout) findViewById(R.id.flashiz);
        rekening = (LinearLayout) findViewById(R.id.rekening);

        transfer_text.setTypeface(Utility.LightTextFormat(AgentSimaspayUserActivity.this));
        pembelian_text.setTypeface(Utility.LightTextFormat(AgentSimaspayUserActivity.this));
        pembayaran_text.setTypeface(Utility.LightTextFormat(AgentSimaspayUserActivity.this));
        traiktunai_text.setTypeface(Utility.LightTextFormat(AgentSimaspayUserActivity.this));
        rekening_text.setTypeface(Utility.LightTextFormat(AgentSimaspayUserActivity.this));
        flashiz_text.setTypeface(Utility.LightTextFormat(AgentSimaspayUserActivity.this));
        akun_textView.setTypeface(Utility.LightTextFormat(AgentSimaspayUserActivity.this));
        logout_textView.setTypeface(Utility.LightTextFormat(AgentSimaspayUserActivity.this));

        user_Name.setTypeface(Utility.LightTextFormat(AgentSimaspayUserActivity.this));
        User_Number.setTypeface(Utility.OpemSans_Light(AgentSimaspayUserActivity.this));

        logOut = (LinearLayout) findViewById(R.id.logOut);

        User_Number.setText(sharedPreferences.getString("accountnumber",""));
//        user_Name.setText(sharedPreferences.getString("userName",""));

        String user;
        if (sharedPreferences.getString("userName","").length() >= 15) {
            user = sharedPreferences.getString("userName","").substring(0, 15)+ "...";
        } else {
            user = sharedPreferences.getString("userName","");
        }
        user_Name.setText(user);

        switchnumber_layout = (LinearLayout) findViewById(R.id.Number_switching);

        switchnumber_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentSimaspayUserActivity.this, TransferHomeActivity.class);
                intent.putExtra("simaspayuser", true);
                intent.putExtra("agentornot", false);
                startActivityForResult(intent, 20);
            }
        });

        pembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentSimaspayUserActivity.this, PaymentAndPurchaseAccountTypeActivity.class);
                intent.putExtra("accounttype", false);
                startActivity(intent);
            }
        });
        pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentSimaspayUserActivity.this, PaymentAndPurchaseAccountTypeActivity.class);
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
                Intent intent = new Intent(AgentSimaspayUserActivity.this, BalanceSheetActivity.class);
                intent.putExtra("simaspayuser", true);
                intent.putExtra("transfer_or_mutasi", false);
                intent.putExtra("red_or_white", false);
                intent.putExtra("lakupandai",false);
                startActivityForResult(intent, 20);
            }
        });
        flashiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentSimaspayUserActivity.this, PayByQRActivity.class);
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
