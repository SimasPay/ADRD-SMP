package com.payment.simaspay.agentdetails;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import org.w3c.dom.Text;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 12/31/2015.
 */
public class Register_to_SimaspayUserActivity extends Activity {


    TextView textView;

    public static Button reg_1, reg_2, reg_3, next,back;

    Fragment fr;
    public static Context context;

    public static int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.registertosimaspayuser);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.rem_bal));
        }
        textView = (TextView) findViewById(R.id.titled);

        reg_1 = (Button) findViewById(R.id.reg_1);
        reg_2 = (Button) findViewById(R.id.reg_2);
        reg_3 = (Button) findViewById(R.id.reg_3);

        textView.setTypeface(Utility.LightTextFormat(Register_to_SimaspayUserActivity.this));
        reg_1.setTypeface(Utility.LightTextFormat(Register_to_SimaspayUserActivity.this));
        reg_2.setTypeface(Utility.LightTextFormat(Register_to_SimaspayUserActivity.this));
        reg_3.setTypeface(Utility.LightTextFormat(Register_to_SimaspayUserActivity.this));


        next = (Button) findViewById(R.id.next);
        next.setTypeface(Utility.LightTextFormat(Register_to_SimaspayUserActivity.this));

        back=(Button)findViewById(R.id.btnBacke);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        fr = new Registration_1_Fragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if (i == 1) {
                    reg_2.setClickable(true);
                    reg_2.setFocusable(true);

                    fr = new Registration_2_Fragment();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else if (i == 2) {
                    reg_2.setClickable(true);
                    reg_2.setFocusable(true);
                    reg_3.setClickable(true);
                    reg_3.setFocusable(true);

                    fr = new Registration_3_Fragment();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    Intent intent = new Intent(Register_to_SimaspayUserActivity.this, Register_To_SimaspayUserConfirmationActivity.class);
                    startActivityForResult(intent,10);
                }
            }
        });

        reg_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fr = new Registration_2_Fragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        reg_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fr = new Registration_3_Fragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        reg_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fr = new Registration_1_Fragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(resultCode==Activity.RESULT_OK){
                finish();
            }
        }
    }
}
