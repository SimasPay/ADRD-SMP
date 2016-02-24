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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.agentdetails.ChangePinActivity;
import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 12/29/2015.
 */
public class BalanceSheetActivity extends Activity {

    LinearLayout layout1, layout2, layout3, logOut, numberSwitching, white_logOut, white_numberswitching;

    TextView textView1, textView2, textView3, simas, name, number, ganti_akun, logout_text, white_name, white_number, white_ganti_akun, white_logout_text;

    ImageView imageView1, imageView2, imageView3, menu_back;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        sharedPreferences = getSharedPreferences("SimasPay", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        simas = (TextView) findViewById(R.id.simas);


        String s = "simaspay";
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1f), 0, 5, 0);
        ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);

        simas.setText(ss1);
        layout1 = (LinearLayout) findViewById(R.id.common_layout_1);
        layout2 = (LinearLayout) findViewById(R.id.common_layout_2);
        layout3 = (LinearLayout) findViewById(R.id.common_layout_3);

        imageView1 = (ImageView) findViewById(R.id.common_image_1);
        imageView2 = (ImageView) findViewById(R.id.common_image_2);
        imageView3 = (ImageView) findViewById(R.id.common_image_3);

        menu_back = (ImageView) findViewById(R.id.menu_back);

        textView1 = (TextView) findViewById(R.id.common_text_1);
        textView2 = (TextView) findViewById(R.id.common_text_2);
        textView3 = (TextView) findViewById(R.id.common_text_3);

        name = (TextView) findViewById(R.id.com_name);
        number = (TextView) findViewById(R.id.com_number);
        ganti_akun = (TextView) findViewById(R.id.com_ganti_akun_text);
        logout_text = (TextView) findViewById(R.id.com_logout_text);

        white_name = (TextView) findViewById(R.id.userName);
        white_number = (TextView) findViewById(R.id.userNumber);
        white_ganti_akun = (TextView) findViewById(R.id.ganti_akun);
        white_logout_text = (TextView) findViewById(R.id.laoout_text);

        textView1.setTypeface(Utility.LightTextFormat(BalanceSheetActivity.this));
        textView2.setTypeface(Utility.LightTextFormat(BalanceSheetActivity.this));
        textView3.setTypeface(Utility.LightTextFormat(BalanceSheetActivity.this));

        white_name.setTypeface(Utility.LightTextFormat(BalanceSheetActivity.this));
        white_number.setTypeface(Utility.OpemSans_Light(BalanceSheetActivity.this));
        white_logout_text.setTypeface(Utility.LightTextFormat(BalanceSheetActivity.this));
        white_ganti_akun.setTypeface(Utility.LightTextFormat(BalanceSheetActivity.this));

        name.setTypeface(Utility.Robot_Light(BalanceSheetActivity.this));
        number.setTypeface(Utility.OpemSans_Light(BalanceSheetActivity.this));
        logout_text.setTypeface(Utility.Robot_Light(BalanceSheetActivity.this));
        ganti_akun.setTypeface(Utility.Robot_Light(BalanceSheetActivity.this));


        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.btn_infosaldo));
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.btn_mutasi));
        imageView3.setImageDrawable(getResources().getDrawable(R.drawable.btn_mpin));

        textView1.setText("Info Saldo");

        if (getIntent().getExtras().getBoolean("transfer_or_mutasi")) {
            textView2.setText("Mutasi");
        } else {
            textView2.setText("Transaksi");
        }
        textView3.setText("Ganti mPIN");

        menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_FIRST_USER, intent);
                finish();
            }
        });


        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BalanceSheetActivity.this, CurrentSessionActivity.class);
                startActivity(intent);
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BalanceSheetActivity.this, Trans_DataSelectionActivity.class);
                intent.putExtra("lakupandai", getIntent().getExtras().getBoolean("lakupandai"));
                startActivity(intent);
            }
        });

        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BalanceSheetActivity.this, ChangePinActivity.class);
                startActivity(intent);
            }
        });
        logOut = (LinearLayout) findViewById(R.id.logOut);
        numberSwitching = (LinearLayout) findViewById(R.id.Number_switching);

        white_logOut = (LinearLayout) findViewById(R.id.whitelogOut);
        white_numberswitching = (LinearLayout) findViewById(R.id.gantiAkun_layout);


        if (getIntent().getExtras().getBoolean("simaspayuser")) {
            numberSwitching.setVisibility(View.VISIBLE);
        } else {
            numberSwitching.setVisibility(View.GONE);
        }
        if (getIntent().getExtras().getBoolean("red_or_white")) {
            findViewById(R.id.whitecolor_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.redcolor_layout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.whitecolor_layout).setVisibility(View.GONE);
            findViewById(R.id.redcolor_layout).setVisibility(View.VISIBLE);
        }
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   editor.putBoolean("Logout",true).commit();
                finish();*/
                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        white_numberswitching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        white_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        numberSwitching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = getIntent();
            setResult(Activity.RESULT_FIRST_USER, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
