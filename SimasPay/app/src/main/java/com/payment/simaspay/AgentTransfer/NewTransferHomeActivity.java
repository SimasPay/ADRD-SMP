package com.payment.simaspay.AgentTransfer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.payment.simaspay.UangkuTransfer.UangkuTransferDetailsActivity;
import com.payment.simaspay.lakupandai.LakupandaiTransferDetailsActivity;

import java.util.ArrayList;

import simaspay.payment.com.simaspay.R;
import simaspay.payment.com.simaspay.UserHomeActivity;

/**
 * Created by widy on 1/16/17.
 * 16
 */

public class NewTransferHomeActivity extends AppCompatActivity implements OnItemClickListener {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferhome);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        listView = (ListView) findViewById(R.id.transfer_list);
        listView.setOnItemClickListener(NewTransferHomeActivity.this);
        LinearLayout backLin=(LinearLayout)findViewById(R.id.back_layout);
        backLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewTransferHomeActivity.this, UserHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
        String teks=((TextView) view).getText().toString();
        if(teks.equals("Bank Sinarmas")){
            Intent intent = new Intent(NewTransferHomeActivity.this, TransferDetailsActivity.class);
            startActivity(intent);
        }else if(teks.equals("Bank Lainnya")){
            Intent intent = new Intent(NewTransferHomeActivity.this, BankDetailsActivity.class);
            startActivity(intent);
        }else if(teks.equals("Laku Pandai")){
            Intent intent = new Intent(NewTransferHomeActivity.this, LakupandaiTransferDetailsActivity.class);
            startActivity(intent);
        }else if(teks.equals("Uangku")){
            Intent intent = new Intent(NewTransferHomeActivity.this, UangkuTransferDetailsActivity.class);
            startActivity(intent);
        }else if(teks.equals("E-Money Lainnya")){
            //
        }
    }
}
