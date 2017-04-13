package com.payment.simaspay.AgentTransfer;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import simaspay.payment.com.simaspay.FavouriteInputActivity;
import simaspay.payment.com.simaspay.R;
import simaspay.payment.com.simaspay.UserHomeActivity;

import static com.payment.simaspay.services.Constants.LOG_TAG;

/**
 * Created by widy on 1/26/17.
 * 26
 */

public class TransferBankToEmoneyNotificationActivity extends AppCompatActivity {
    String destName, destMDN, destAmount, transactionID;
    CheckBox favBtn;
    Boolean isSetFav=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfernotification);
        if (android.os.Build.VERSION.SDK_INT > 14) {
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

        TextView label_fav=(TextView)findViewById(R.id.label_fav);
        //label_fav.setVisibility(View.GONE);
        favBtn=(CheckBox)findViewById(R.id.checkfav);
        //favBtn.setVisibility(View.GONE);
        favBtn.setOnClickListener(arg0 -> {
            if(!favBtn.isChecked()){
                isSetFav=false;
            } else{
                isSetFav=true;
            }
        });

        Button okbutton=(Button)findViewById(R.id.ok_btn);
        okbutton.setOnClickListener(view -> {
            if(favBtn.isChecked()){
                Log.d(LOG_TAG, "checked");
                if(getIntent().getExtras()!=null){
                    Intent intent = new Intent(TransferBankToEmoneyNotificationActivity.this, FavouriteInputActivity.class);
                    intent.putExtra("DestMDN",destMDN);
                    intent.putExtra("favCat",getIntent().getExtras().getString("favCat"));
                    intent.putExtra("idFavCat",getIntent().getExtras().getInt("idFavCat"));
                    startActivityForResult(intent, 10);
                }
            }else{
                Intent i = new Intent(TransferBankToEmoneyNotificationActivity.this, UserHomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }
}
