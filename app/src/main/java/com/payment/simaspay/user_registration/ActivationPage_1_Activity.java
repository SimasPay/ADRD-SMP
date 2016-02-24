package com.payment.simaspay.user_registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 12/21/2015.
 */
public class ActivationPage_1_Activity extends Activity {

    TextView terms_conditions;

    Button cancel, accept;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(resultCode==Activity.RESULT_OK){
                Intent intent=getIntent();
                setResult(Activity.RESULT_OK,intent);
                finish();
            }else if(resultCode==12){
                Intent intent=getIntent();
                setResult(12,intent);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page_1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        terms_conditions = (TextView) findViewById(R.id.terms_conditions);

        cancel = (Button) findViewById(R.id.cancel);
        accept = (Button) findViewById(R.id.accept);

        terms_conditions.setTypeface(Utility.Robot_Regular(ActivationPage_1_Activity.this));
        cancel.setTypeface(Utility.Robot_Regular(ActivationPage_1_Activity.this));
        accept.setTypeface(Utility.Robot_Regular(ActivationPage_1_Activity.this));


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivationPage_1_Activity.this,ActivationPage_2_Activity.class);
                startActivityForResult(intent,10);
            }
        });
    }
}
