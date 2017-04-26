package com.payment.simaspay.UangkuTransfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.FavouriteInputActivity;
import simaspay.payment.com.simaspay.R;
import simaspay.payment.com.simaspay.UserHomeActivity;

import static com.payment.simaspay.services.Constants.LOG_TAG;

/**
 * Created by Nagendra P on 2/3/2016.
 */
public class UangkuTransferSuccessActivity extends Activity {

    TextView title, heading, name, name_field, number, number_field, amount, amount_field, products, other_products, transfer_field, transferID;
    CheckBox favBtn;
    Boolean isSetFav=false;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commonsuccess);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        title = (TextView) findViewById(R.id.title);
        heading = (TextView) findViewById(R.id.textview);
        name = (TextView) findViewById(R.id.name);
        name_field = (TextView) findViewById(R.id.name_field);
        number = (TextView) findViewById(R.id.number);
        number_field = (TextView) findViewById(R.id.number_field);
        amount = (TextView) findViewById(R.id.amount);
        amount_field = (TextView) findViewById(R.id.amount_field);
        products = (TextView) findViewById(R.id.products);
        other_products = (TextView) findViewById(R.id.other_products);
        transferID = (TextView) findViewById(R.id.transferID);
        transfer_field = (TextView) findViewById(R.id.transfer_field);
        findViewById(R.id.ID_layout).setVisibility(View.VISIBLE);

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

        products.setVisibility(View.VISIBLE);
        other_products.setVisibility(View.VISIBLE);
        transferID.setVisibility(View.VISIBLE);

        name.setText("Nama Pemilik Rekening");
        name_field.setText(getIntent().getExtras().getString("Name"));
        products.setText("Bank Tujuan");
        other_products.setText("Bank Sinarmas");
        number.setText("Nomor Rekening Tujuan");
        number_field.setText(getIntent().getExtras().getString("Acc_Number"));
        amount.setText("Jumlah");
        amount_field.setText("Rp. "+getIntent().getExtras().getString("amount"));

        products.setVisibility(View.GONE);
        other_products.setVisibility(View.GONE);

        heading.setText("Transfer Berhasil!");

        ok = (Button) findViewById(R.id.ok);

        title.setTypeface(Utility.Robot_Regular(UangkuTransferSuccessActivity.this));
        heading.setTypeface(Utility.Robot_Regular(UangkuTransferSuccessActivity.this));
        name.setTypeface(Utility.Robot_Regular(UangkuTransferSuccessActivity.this));
        name_field.setTypeface(Utility.Robot_Light(UangkuTransferSuccessActivity.this));
        number.setTypeface(Utility.Robot_Regular(UangkuTransferSuccessActivity.this));
        number_field.setTypeface(Utility.Robot_Light(UangkuTransferSuccessActivity.this));
        amount.setTypeface(Utility.Robot_Regular(UangkuTransferSuccessActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(UangkuTransferSuccessActivity.this));
        products.setTypeface(Utility.Robot_Regular(UangkuTransferSuccessActivity.this));
        other_products.setTypeface(Utility.Robot_Light(UangkuTransferSuccessActivity.this));
        transfer_field.setTypeface(Utility.Robot_Regular(UangkuTransferSuccessActivity.this));
        transferID.setTypeface(Utility.Robot_Light(UangkuTransferSuccessActivity.this));
        ok.setTypeface(Utility.Robot_Regular(UangkuTransferSuccessActivity.this));

        ok.setOnClickListener(view -> {
            if(favBtn.isChecked()){
                Log.d(LOG_TAG, "checked");
                if(getIntent().getExtras()!=null){
                    Intent intent = new Intent(UangkuTransferSuccessActivity.this, FavouriteInputActivity.class);
                    intent.putExtra("DestMDN",getIntent().getExtras().getString("Acc_Number"));
                    intent.putExtra("favCat",getIntent().getExtras().getString("favCat"));
                    intent.putExtra("idFavCat",getIntent().getExtras().getInt("idFavCat"));
                    startActivity(intent);
                }
            }else{
                Intent i = new Intent(UangkuTransferSuccessActivity.this, UserHomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        transferID.setText(getIntent().getExtras().getString("sctlID"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(UangkuTransferSuccessActivity.this, UserHomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(UangkuTransferSuccessActivity.this, UserHomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return super.onKeyDown(keyCode, event);
    }
}

