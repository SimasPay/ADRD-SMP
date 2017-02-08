package com.payment.simaspay.agentdetails;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.payment.simaspay.lakupandai.LakuPandaiActivity;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.userdetails.SimaspayUserActivity;

import simaspay.payment.com.simaspay.Agent_HomePage_Activity;
import simaspay.payment.com.simaspay.LoginScreenActivity;
import simaspay.payment.com.simaspay.R;
import simaspay.payment.com.simaspay.UserHomeActivity;

/**
 * Created by Nagendra P on 12/30/2015.
 */
public class NumberSwitchingActivity extends Activity {

    TextView textView,agent_text,reg_text,agent_number,reg_number;

    LinearLayout agent_layout,reg_layout,logOut;

    Dialog dialogCustomWish;
    Context context;

    int value=0;

    TextView lagout_text;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.number_switching);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }
        textView=(TextView)findViewById(R.id.text_3);
        agent_text=(TextView)findViewById(R.id.agent_text);
        reg_text=(TextView)findViewById(R.id.reg_text);
        agent_number=(TextView)findViewById(R.id.agent_number);
        reg_number=(TextView)findViewById(R.id.reguler);

        sharedPreferences=getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        textView.setTypeface(Utility.Robot_Light(NumberSwitchingActivity.this));
        agent_text.setTypeface(Utility.Robot_Light(NumberSwitchingActivity.this));
        reg_text.setTypeface(Utility.Robot_Light(NumberSwitchingActivity.this));
        agent_number.setTypeface(Utility.Robot_Light(NumberSwitchingActivity.this));
        reg_number.setTypeface(Utility.Robot_Light(NumberSwitchingActivity.this));

        lagout_text = (TextView)findViewById(R.id.lagout_text);
        lagout_text.setTypeface(Utility.Robot_Light(NumberSwitchingActivity.this));

        agent_layout=(LinearLayout)findViewById(R.id.agent_layout);
        reg_layout=(LinearLayout)findViewById(R.id.reg_layout);
        logOut=(LinearLayout)findViewById(R.id.logOut);

        agent_number.setText(sharedPreferences.getString("mobileNumber",""));
        reg_number.setText(sharedPreferences.getString("accountnumber",""));


        agent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putInt("AgentUsing",1).commit();
                Intent intent=new Intent(NumberSwitchingActivity.this, UserHomeActivity.class);
                sharedPreferences.edit().putString("useas", "E-money Plus").apply();
                //intent.putExtra("useas","E-money Plus");
                startActivityForResult(intent,20);
                finish();
            }
        });

        reg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /*dialogCustomWish = new Dialog(context);
                dialogCustomWish.setCancelable(false);

                dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                View view1 = LayoutInflater.from(context).inflate(R.layout.select_account, null);
                dialogCustomWish.setContentView(R.layout.select_account);

                Button button = (Button) dialogCustomWish.findViewById(R.id.OK);
                Button button1 = (Button) dialogCustomWish.findViewById(R.id.Cancel);

                LinearLayout linearLayout = (LinearLayout) dialogCustomWish.findViewById(R.id.firststone);
                LinearLayout linearLayout1 = (LinearLayout) dialogCustomWish.findViewById(R.id.secondone);
                TextView textView=(TextView)dialogCustomWish.findViewById(R.id.title);

                TextView textView1=(TextView)dialogCustomWish.findViewById(R.id.number);
                TextView textView2=(TextView)dialogCustomWish.findViewById(R.id.number_1);

                button.setTypeface(Utility.RegularTextFormat(context));
                button1.setTypeface(Utility.RegularTextFormat(context));
                textView.setTypeface(Utility.LightTextFormat(context));

                textView1.setTypeface(Utility.LightTextFormat(context));
                textView2.setTypeface(Utility.LightTextFormat(context));

                final ImageView imageView=(ImageView)dialogCustomWish.findViewById(R.id.current_month_checkbox);
                final ImageView imageView1=(ImageView)dialogCustomWish.findViewById(R.id.current_month_checkbox_1);

                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.selected));
                        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
                        value=1;
                    }
                });

                linearLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
                        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.selected));
                        value=2;
                    }
                });

                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {




                        value=0;
                        dialogCustomWish.dismiss();
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(value==1){
                            dialogCustomWish.dismiss();
                            value=0;
                            Intent intent=new Intent(NumberSwitchingActivity.this, AgentSimaspayUserActivity.class);
                            intent.putExtra("direct",true);
                            startActivityForResult(intent,20);

                        }else if(value==2){
                            dialogCustomWish.dismiss();
                            value=0;
                            Intent intent=new Intent(NumberSwitchingActivity.this, LakuPandaiActivity.class);
                            intent.putExtra("direct",true);
                            startActivityForResult(intent,20);

                        }else{
                            value=0;
                            Toast.makeText(NumberSwitchingActivity.this,"Please Select Account Type",Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                dialogCustomWish.show();*/
                sharedPreferences.edit().putInt("AgentUsing",2).commit();
                Intent intent=new Intent(NumberSwitchingActivity.this, UserHomeActivity.class);
                intent.putExtra("direct",true);
                sharedPreferences.edit().putString("useas", "Bank").apply();
                startActivityForResult(intent,20);
                finish();

            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NumberSwitchingActivity.this, SecondLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sharedPreferences.edit().putString("userApiKey", "NONE").apply();
                startActivity(intent);
            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==20){
            if(resultCode==Activity.RESULT_OK){
                Intent intent=getIntent();
               setResult(RESULT_OK,intent);
                finish();
            }else if(resultCode==Activity.RESULT_CANCELED){

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
