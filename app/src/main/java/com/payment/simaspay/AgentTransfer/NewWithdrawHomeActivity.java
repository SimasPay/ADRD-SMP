package com.payment.simaspay.AgentTransfer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.payment.simaspay.Cash_InOut.CashOutDetailsActivity;
import com.payment.simaspay.R;
import com.payment.simaspay.UangkuTransfer.UangkuTransferDetailsActivity;

/**
 * Created by widy on 2/13/17.
 * 13
 */

public class NewWithdrawHomeActivity extends AppCompatActivity {
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

        TextView title=(TextView)findViewById(R.id.titled);
        title.setText("Tarik Tunai");
        sharedPreferences=getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        account=sharedPreferences.getString("useas","");
        Log.d(LOG_TAG,"account as: " + account);
        listView = (ListView) findViewById(R.id.transfer_list);
        String[] transferString=getResources().getStringArray(R.array.withdraw_array);
        final ArrayAdapter<String> listAdapter =
                new ArrayAdapter<String>(this, R.layout.textviewdata,R.id.textviewdata_text, transferString);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView teksview=(TextView)view.findViewById(R.id.textviewdata_text);
                String text = teksview.getText().toString();
                if(text.equals("Untuk Saya")){
                    String account = sharedPreferences.getString("useas", "");
                    Intent intent = new Intent(NewWithdrawHomeActivity.this, CashOutDetailsActivity.class);
                    intent.putExtra("simaspayuser", false);
                    intent.putExtra("agentornot", false);
                    intent.putExtra("untuk", "Untuk Saya");
                    sharedPreferences.edit().putString("useas", account).apply();
                    startActivityForResult(intent, 20);
                }else if(text.equals("Untuk Orang Lain")){
                    String account = sharedPreferences.getString("useas", "");
                    Intent intent = new Intent(NewWithdrawHomeActivity.this, CashOutDetailsActivity.class);
                    intent.putExtra("simaspayuser", false);
                    intent.putExtra("agentornot", false);
                    intent.putExtra("untuk", "Untuk Orang Lain");
                    sharedPreferences.edit().putString("useas", account).apply();
                    startActivityForResult(intent, 20);
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
