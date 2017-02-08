package com.payment.simaspay.AgentTransfer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class NewTransferHomeActivity extends AppCompatActivity{
    private static final String LOG_TAG = "SimasPay";
    ListView listView;
    String account="";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferhome);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        sharedPreferences=getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        account=sharedPreferences.getString("useas","");
        Log.d(LOG_TAG,"account as: " + account);
        listView = (ListView) findViewById(R.id.transfer_list);
        String[] transferString=getResources().getStringArray(R.array.transfer_array);
        final ArrayAdapter<String> listAdapter =
                new ArrayAdapter<String>(this, R.layout.textviewdata,R.id.textviewdata_text, transferString);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView teksview=(TextView)view.findViewById(R.id.textviewdata_text);
                String text = teksview.getText().toString();
                if(text.equals("Bank Sinarmas")){
                    Intent intent = new Intent(NewTransferHomeActivity.this, TransferDetailsActivity.class);
                    startActivity(intent);
                }else if(text.equals("Bank Lainnya")){
                    Intent intent = new Intent(NewTransferHomeActivity.this, BankDetailsActivity.class);
                    startActivity(intent);
                    /**
                }else if(text.equals("Laku Pandai")){
                    Intent intent = new Intent(NewTransferHomeActivity.this, LakupandaiTransferDetailsActivity.class);
                    startActivity(intent);
                     **/
                }else if(text.equals("Uangku")){
                    Intent intent = new Intent(NewTransferHomeActivity.this, UangkuTransferDetailsActivity.class);
                    startActivity(intent);
                }else if(text.equals("E-money")){
                    if(account.equals("E-money Plus")||account.equals("E-Money Reguler")){
                        Intent intent = new Intent(NewTransferHomeActivity.this, TransferEmoneyToEmoneyActivity.class);
                        startActivity(intent);
                    }else{
                        if(account.equals("E-money Plus")||account.equals("E-Money Reguler")){
                            Intent intent = new Intent(NewTransferHomeActivity.this, TransferEmoneyToEmoneyActivity.class);
                            startActivity(intent);
                        }else if(account.equals("Bank")){
                            Intent intent = new Intent(NewTransferHomeActivity.this, TransferEmoneyActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }
        });

        LinearLayout backLin=(LinearLayout)findViewById(R.id.back_layout);
        backLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listView.setAdapter(listAdapter);
    }

}
