package com.payment.simaspay.AgentTransfer;

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

import com.payment.simaspay.lakupandai.LakupandaiTransferDetailsActivity;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 12/29/2015.
 */
public class TransferHomeActivity extends Activity {

    LinearLayout layout1, layout2, layout3, logOut, numberSwitching, gantiAkun_layout, whitelogOut;

    TextView textView1, textView2, textView3, simas, name, number, ganti_akun, logout_text, whiteName, white_number;

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

        whiteName = (TextView) findViewById(R.id.userName);
        white_number = (TextView) findViewById(R.id.userNumber);


        ganti_akun = (TextView) findViewById(R.id.com_ganti_akun_text);
        logout_text = (TextView) findViewById(R.id.com_logout_text);

        textView1.setTypeface(Utility.LightTextFormat(TransferHomeActivity.this));
        textView2.setTypeface(Utility.LightTextFormat(TransferHomeActivity.this));
        textView3.setTypeface(Utility.LightTextFormat(TransferHomeActivity.this));


        name.setTypeface(Utility.LightTextFormat(TransferHomeActivity.this));
        number.setTypeface(Utility.LightTextFormat(TransferHomeActivity.this));
        logout_text.setTypeface(Utility.LightTextFormat(TransferHomeActivity.this));
        ganti_akun.setTypeface(Utility.LightTextFormat(TransferHomeActivity.this));

//        name.setText(sharedPreferences.getString("userName", ""));

        String user;
        if (sharedPreferences.getString("userName","").length() >= 13) {
            user = sharedPreferences.getString("userName","").substring(0, 13)+ "...";
        } else {
            user = sharedPreferences.getString("userName","");
        }
        name.setText(user);


//        whiteName.setText(sharedPreferences.getString("userName", ""));
        if (sharedPreferences.getString("userName","").length() >= 13) {
            user = sharedPreferences.getString("userName","").substring(0, 13)+ "...";
        } else {
            user = sharedPreferences.getString("userName","");
        }
        whiteName.setText(user);
        if(sharedPreferences.getInt("userType",-1)==0){
            white_number.setText(sharedPreferences.getString("accountnumber", ""));
            number.setText(sharedPreferences.getString("accountnumber", ""));
        }else if(sharedPreferences.getInt("userType",-1)==1){
            white_number.setText(sharedPreferences.getString("mobileNumber", ""));
            number.setText(sharedPreferences.getString("mobileNumber", ""));
        }else if(sharedPreferences.getInt("userType",-1)==2) {
            if(sharedPreferences.getInt("AgentUsing",-1)==1){
                white_number.setText(sharedPreferences.getString("mobileNumber", ""));
                number.setText(sharedPreferences.getString("mobileNumber", ""));
            }else{
                white_number.setText(sharedPreferences.getString("accountnumber", ""));
                number.setText(sharedPreferences.getString("accountnumber", ""));
            }
        }

        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.btn_bsim));
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.btn_banklain));
        imageView3.setImageDrawable(getResources().getDrawable(R.drawable.btn_lakupandai));

        textView1.setText("Bank Sinarmas");
        textView2.setText("Bank Lain");
        textView3.setText("Laku Pandai");

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransferHomeActivity.this, TransferDetailsActivity.class);
                startActivity(intent);
            }
        });

        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransferHomeActivity.this, LakupandaiTransferDetailsActivity.class);
                startActivity(intent);
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransferHomeActivity.this, BankDetailsActivity.class);
                startActivity(intent);
            }
        });

        if (getIntent().getExtras().getBoolean("agentornot")) {
            findViewById(R.id.whitecolor_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.redcolor_layout).setVisibility(View.GONE);
            findViewById(R.id.agent).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.whitecolor_layout).setVisibility(View.GONE);
            findViewById(R.id.redcolor_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.agent).setVisibility(View.GONE);
        }

        menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_FIRST_USER, intent);
                finish();
            }
        });
        logOut = (LinearLayout) findViewById(R.id.logOut);
        numberSwitching = (LinearLayout) findViewById(R.id.Number_switching);


        whitelogOut = (LinearLayout) findViewById(R.id.whitelogOut);
        gantiAkun_layout = (LinearLayout) findViewById(R.id.gantiAkun_layout);

        if (getIntent().getExtras().getBoolean("simaspayuser")) {
            numberSwitching.setVisibility(View.VISIBLE);
        } else {
            numberSwitching.setVisibility(View.GONE);
        }


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*  editor.putBoolean("Logout",true).commit();
                finish();*/
                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        numberSwitching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });


        whitelogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        gantiAkun_layout.setOnClickListener(new View.OnClickListener() {
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
