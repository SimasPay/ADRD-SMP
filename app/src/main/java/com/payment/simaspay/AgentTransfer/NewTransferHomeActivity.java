package com.payment.simaspay.AgentTransfer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.payment.simaspay.services.Constants;

import simaspay.payment.com.simaspay.R;

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
        account=sharedPreferences.getString(Constants.PARAMETER_USES_AS,"");
        Log.d(LOG_TAG,"account as: " + account);
        listView = findViewById(R.id.transfer_list);
        String[] transferString=getResources().getStringArray(R.array.bahasa_transfer_array);
        final ArrayAdapter<String> listAdapter =
                new ArrayAdapter<String>(this, R.layout.textviewdata,R.id.textviewdata_text, transferString);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            TextView teksview= view.findViewById(R.id.textviewdata_text);
            String text = teksview.getText().toString();
            if(text.equals(getResources().getString(R.string.bank_sinarmas))){
                Intent intent = new Intent(NewTransferHomeActivity.this, TransferDetailsActivity.class);
                intent.putExtra("transfer", "sinarmas");
                startActivity(intent);
            }else if(text.equals(getResources().getString(R.string.bank_lainnya))){
                Intent intent = new Intent(NewTransferHomeActivity.this, BankDetailsActivity.class);
                intent.putExtra("transfer", "lainnya");
                startActivity(intent);
                /*
            }else if(text.equals("Laku Pandai")){
                Intent intent = new Intent(NewTransferHomeActivity.this, LakupandaiTransferDetailsActivity.class);
                startActivity(intent);
                 **/
            /*
            }else if(text.equals(getResources().getString(R.string.uangku))){
                Intent intent = new Intent(NewTransferHomeActivity.this, UangkuTransferDetailsActivity.class);
                intent.putExtra("transfer", "uangku");
                startActivity(intent);
             **/
            }else if(text.equals(getResources().getString(R.string.emoney))){
                if(account.equals(Constants.CONSTANT_EMONEY_PLUS)||account.equals(Constants.CONSTANT_EMONEY_REGULER)){
                    Intent intent = new Intent(NewTransferHomeActivity.this, TransferEmoneyToEmoneyActivity.class);
                    intent.putExtra("transfer", "emoney");
                    startActivity(intent);
                }else{
                    if(account.equals(Constants.CONSTANT_EMONEY_PLUS)||account.equals(Constants.CONSTANT_EMONEY_REGULER)){
                        Intent intent = new Intent(NewTransferHomeActivity.this, TransferEmoneyToEmoneyActivity.class);
                        intent.putExtra("transfer", "emoney");
                        startActivity(intent);
                    }else if(account.equals(Constants.CONSTANT_BANK_USER)){
                        Intent intent = new Intent(NewTransferHomeActivity.this, TransferBankToEmoneyActivity.class);
                        intent.putExtra("transfer", "emoney");
                        startActivity(intent);
                    }
                }
            }
        });

        LinearLayout backLin= findViewById(R.id.back_layout);
        backLin.setOnClickListener(view -> finish());
        listView.setAdapter(listAdapter);
    }

}
