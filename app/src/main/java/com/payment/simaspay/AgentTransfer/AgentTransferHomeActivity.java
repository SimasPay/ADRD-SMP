package com.payment.simaspay.AgentTransfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.PaymentPerchaseAccount.PaymentAndPerchaseAccountTypeActivity;
import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/27/2016.
 */
public class AgentTransferHomeActivity extends Activity {

    LinearLayout transfer_layout, pembelian_layout, pembayaran_layout, flashiz_layout, number_switching_layout, Logout_layout;

    TextView transfer_text, pembelian_text, pembayaran_text, flashiz_text, number_switching, logout, name, number,agent;

    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transferhome);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }


        transfer_text = (TextView) findViewById(R.id.Transfer_layout_text);
        pembelian_text = (TextView) findViewById(R.id.Pembelian_layout_text);
        pembayaran_text = (TextView) findViewById(R.id.Pembayaran_layout_text);
        flashiz_text = (TextView) findViewById(R.id.Flashiz_layout_text);
        number_switching = (TextView) findViewById(R.id.ganti_akun);
        logout = (TextView) findViewById(R.id.laoout_text);
        name = (TextView) findViewById(R.id.userName);
        number = (TextView) findViewById(R.id.userNumber);
        agent = (TextView) findViewById(R.id.agent);

        backbutton=(ImageView)findViewById(R.id.menu_back);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                setResult(RESULT_FIRST_USER,intent);
                finish();
            }
        });

        transfer_text.setTypeface(Utility.Robot_Light(AgentTransferHomeActivity.this));
        pembelian_text.setTypeface(Utility.Robot_Light(AgentTransferHomeActivity.this));
        pembayaran_text.setTypeface(Utility.Robot_Light(AgentTransferHomeActivity.this));
        flashiz_text.setTypeface(Utility.Robot_Light(AgentTransferHomeActivity.this));
        number_switching.setTypeface(Utility.LightTextFormat(AgentTransferHomeActivity.this));
        logout.setTypeface(Utility.LightTextFormat(AgentTransferHomeActivity.this));
        name.setTypeface(Utility.LightTextFormat(AgentTransferHomeActivity.this));
        number.setTypeface(Utility.OpemSans_Light(AgentTransferHomeActivity.this));
        agent.setTypeface(Utility.LightTextFormat(AgentTransferHomeActivity.this));


        transfer_layout = (LinearLayout) findViewById(R.id.Transfer_layout);
        pembelian_layout = (LinearLayout) findViewById(R.id.Pembelian_layout);
        pembayaran_layout = (LinearLayout) findViewById(R.id.Pembayaran_layout);
        flashiz_layout = (LinearLayout) findViewById(R.id.Flashiz_layout);
        number_switching_layout = (LinearLayout) findViewById(R.id.gantiAkun_layout);
        Logout_layout = (LinearLayout) findViewById(R.id.logOut);


        Logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        number_switching_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });

        transfer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AgentTransferHomeActivity.this , TransferHomeActivity.class);
                intent.putExtra("simaspayuser",false);
                intent.putExtra("agentornot",true);
                startActivityForResult(intent,20);
            }
        });

        pembayaran_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AgentTransferHomeActivity.this, PaymentAndPerchaseAccountTypeActivity.class);
                intent.putExtra("accounttype",true);
                startActivity(intent);
            }
        });

        pembelian_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AgentTransferHomeActivity.this, PaymentAndPerchaseAccountTypeActivity.class);
                intent.putExtra("accounttype",false);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=getIntent();
        setResult(RESULT_FIRST_USER,intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=getIntent();
            setResult(RESULT_FIRST_USER,intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }
}
