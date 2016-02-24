package com.payment.simaspay.user_registration;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 12/30/2015.
 */
public class ActivationPage_3_Activity extends Activity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    TextView text_1, text_2, text_3, text_4;


    EditText e_pin, e_con_mPin;

    Button lanjut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_confirmation);
        sharedPreferences = getSharedPreferences("SimasPay", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        e_pin = (EditText) findViewById(R.id.mpin);
        e_con_mPin = (EditText) findViewById(R.id.confirm_mpin);
        text_1 = (TextView) findViewById(R.id.text_1);
        text_2 = (TextView) findViewById(R.id.text_2);
        text_3 = (TextView) findViewById(R.id.text_3);
        text_4 = (TextView) findViewById(R.id.text_8);


        e_pin.setTypeface(Utility.Robot_Light(ActivationPage_3_Activity.this));
        e_con_mPin.setTypeface(Utility.Robot_Light(ActivationPage_3_Activity.this));

        text_1.setTypeface(Utility.LightTextFormat(ActivationPage_3_Activity.this));
        text_2.setTypeface(Utility.Robot_Regular(ActivationPage_3_Activity.this));
        text_3.setTypeface(Utility.Robot_Light(ActivationPage_3_Activity.this));
        text_4.setTypeface(Utility.Robot_Light(ActivationPage_3_Activity.this));


        lanjut = (Button) findViewById(R.id.next);

        lanjut.setTypeface(Utility.Robot_Regular(ActivationPage_3_Activity.this));

        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivationPage_3_Activity.this, ActivationPage_4_Activity.class);
                startActivityForResult(intent,30);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==30){
            if(resultCode==Activity.RESULT_OK){
                Intent i = getIntent();
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = getIntent();
        setResult(Activity.RESULT_CANCELED, i);
        finish();
    }
}
