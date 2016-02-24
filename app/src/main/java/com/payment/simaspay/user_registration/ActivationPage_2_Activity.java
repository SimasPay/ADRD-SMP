package com.payment.simaspay.user_registration;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 12/22/2015.
 */
public class ActivationPage_2_Activity extends Activity {

    TextView text_1, text_2, text_3, text_4, text_5, text_6, text_7;


    EditText e_Mdn, e_mPin;

    Button lanjut;
    Dialog dialogCustomWish;
    Context context;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_2);
        context=this;

        sharedPreferences=getSharedPreferences("SimasPay",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }
        e_Mdn = (EditText) findViewById(R.id.hand_phno);
        e_mPin = (EditText) findViewById(R.id.otp);
        text_1 = (TextView) findViewById(R.id.text_1);
        text_2 = (TextView) findViewById(R.id.text_2);
        text_3 = (TextView) findViewById(R.id.text_3);
        text_4 = (TextView) findViewById(R.id.text_4);
        text_5 = (TextView) findViewById(R.id.text_5);
        text_6 = (TextView) findViewById(R.id.text_6);
        text_7 = (TextView) findViewById(R.id.text_7);

        e_Mdn.setTypeface(Utility.Robot_Light(ActivationPage_2_Activity.this));
        e_mPin.setTypeface(Utility.Robot_Light(ActivationPage_2_Activity.this));

        text_1.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_2.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_3.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_4.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_5.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_6.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));
        text_7.setTypeface(Utility.LightTextFormat(ActivationPage_2_Activity.this));


        SpannableString content = new SpannableString("Kirim Ulang");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        text_5.setText(content);
        SpannableString content1 = new SpannableString("Login");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        text_7.setText(content1);

        lanjut=(Button)findViewById(R.id.next);


        text_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResendOtp("");
            }
        });

        lanjut.setTypeface(Utility.Robot_Regular(ActivationPage_2_Activity.this));

        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivationPage_2_Activity.this,ActivationPage_3_Activity.class);
                startActivityForResult(intent,20);
            }
        });


        text_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                setResult(12, i);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==20){
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

    public void ResendOtp(final String string){

        dialogCustomWish = new Dialog(context);
        dialogCustomWish.setCancelable(false);

        dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        View view = LayoutInflater.from(context).inflate(R.layout.resend_otp_dialog, null);
        dialogCustomWish.setContentView(R.layout.resend_otp_dialog);

        Button button = (Button) dialogCustomWish.findViewById(R.id.OK);
        TextView textView=(TextView)dialogCustomWish.findViewById(R.id.number);
        TextView textView1=(TextView)dialogCustomWish.findViewById(R.id.number_1);
        button.setTypeface(Utility.HelveticaNeue_Medium(context));
        textView.setTypeface(Utility.HelveticaNeue_Medium(context));
        textView1.setTypeface(Utility.LightTextFormat(context));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCustomWish.dismiss();
            }
        });
        dialogCustomWish.show();




    }
}
