package com.payment.simaspay.AgentTransfer;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import simaspay.payment.com.simaspay.R;
import simaspay.payment.com.simaspay.UserHomeActivity;

/**
 * Created by widy on 1/26/17.
 * 26
 */

public class TransferBankToEmoneyNotificationActivity extends AppCompatActivity {
    String destName, destMDN, destAmount, transactionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfernotification);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            destMDN = (String) extras.get("destmdn");
            destName = (String) extras.get("destName");
            destAmount = (String) extras.get("amount");
            transactionID = (String) extras.get("transactionID");
        }

        TextView transactionid_lbl=(TextView)findViewById(R.id.transactionid);
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.N) {
            transactionid_lbl.setText(Html.fromHtml("<b>ID Transaksi:</b>" + transactionID, Html.FROM_HTML_MODE_LEGACY));
        }else{
            transactionid_lbl.setText(Html.fromHtml("<b>ID Transaksi:</b>" + transactionID));
        }
        TextView destname_lbl=(TextView)findViewById(R.id.lbl_name);
        destname_lbl.setText(destName);
        TextView destmdn_lbl=(TextView)findViewById(R.id.lbl_mdn);
        destmdn_lbl.setText(destMDN);
        TextView destamount_lbl=(TextView)findViewById(R.id.lbl_amount);
        destamount_lbl.setText(TransferBankToEmoneyConfirmationActivity.formatRupiah(destAmount));

        Button okbutton=(Button)findViewById(R.id.ok_btn);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TransferBankToEmoneyNotificationActivity.this, UserHomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                TransferBankToEmoneyNotificationActivity.this.finish();
            }
        });

    }
}
