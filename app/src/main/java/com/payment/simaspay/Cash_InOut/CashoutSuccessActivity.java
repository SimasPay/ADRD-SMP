package com.payment.simaspay.Cash_InOut;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.payment.simaspay.AgentTransfer.TranaferSuccessActivity;
import com.payment.simaspay.PaymentPurchaseAccount.PurchaseSuccessActivity;
import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.FavouriteInputActivity;
import simaspay.payment.com.simaspay.R;
import simaspay.payment.com.simaspay.UserHomeActivity;

import static com.payment.simaspay.services.Constants.LOG_TAG;

/**
 * Created by Nagendra P on 1/29/2016.
 */
public class CashoutSuccessActivity extends AppCompatActivity {

    TextView title, heading, name, name_field, number, number_field, amount, amount_field, transfer, transferID;
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
        transfer = (TextView) findViewById(R.id.transfer_field);
        transferID = (TextView) findViewById(R.id.transferID);

        findViewById(R.id.ID_layout).setVisibility(View.VISIBLE);

        heading.setText("Tarik Tunai Berhasil!");

        ok = (Button) findViewById(R.id.ok);

        transferID.setText(getIntent().getExtras().getString("transferID"));

        title.setTypeface(Utility.Robot_Regular(CashoutSuccessActivity.this));
        heading.setTypeface(Utility.Robot_Regular(CashoutSuccessActivity.this));
        name.setTypeface(Utility.Robot_Regular(CashoutSuccessActivity.this));
        name_field.setTypeface(Utility.Robot_Light(CashoutSuccessActivity.this));
        number.setTypeface(Utility.Robot_Regular(CashoutSuccessActivity.this));
        number_field.setTypeface(Utility.Robot_Light(CashoutSuccessActivity.this));
        amount.setTypeface(Utility.Robot_Regular(CashoutSuccessActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(CashoutSuccessActivity.this));

        transfer.setTypeface(Utility.Robot_Regular(CashoutSuccessActivity.this));
        transferID.setTypeface(Utility.Robot_Light(CashoutSuccessActivity.this));
        ok.setTypeface(Utility.Robot_Regular(CashoutSuccessActivity.this));

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
        if(getIntent().getExtras()!=null){
            String selectedItem = getIntent().getExtras().getString("selectedItem");
            if(selectedItem.equals("man")){
                label_fav.setVisibility(View.VISIBLE);
                favBtn.setVisibility(View.VISIBLE);
            }else{
                label_fav.setVisibility(View.GONE);
                favBtn.setVisibility(View.GONE);
            }
        }

        SharedPreferences sharedPreferences= getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        String account = sharedPreferences.getString("useas","");
        if(account.equals("Bank")) {
            name.setText("Nama Agen");
            number.setText("Nomor Handphone Agen");
            amount.setText("Jumlah");
            name_field.setText(getIntent().getExtras().getString("Name"));
            number_field.setText(getIntent().getExtras().getString("DestMDN"));
            amount_field.setText("Rp. "+getIntent().getExtras().getString("amount"));
        }else{
            String untuk = getIntent().getExtras().getString("untuk");
            if(untuk.equals("Untuk Saya")){
                name.setVisibility(View.GONE);
                name_field.setVisibility(View.GONE);
                number.setVisibility(View.VISIBLE);
                number.setText("Jenis Transaksi");
                number_field.setVisibility(View.VISIBLE);
                number_field.setText("Tarik Tunai - "+untuk);
                label_fav.setVisibility(View.GONE);
                favBtn.setVisibility(View.GONE);
            }else if(untuk.equals("Untuk Orang Lain")){
                name.setVisibility(View.GONE);
                name_field.setVisibility(View.GONE);
                number.setVisibility(View.VISIBLE);
                number.setText("Jenis Transaksi");
                number_field.setVisibility(View.VISIBLE);
                number_field.setText("Tarik Tunai - "+untuk);
            }
            amount.setText("Jumlah");
            amount_field.setText("Rp. "+getIntent().getExtras().getString("amount"));
        }


        amount_field.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize));
        number_field.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize));
        name_field.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize));

        ok.setOnClickListener(view -> {
            if(favBtn.isChecked()){
                Log.d(LOG_TAG, "checked");
                if(getIntent().getExtras()!=null){
                    Intent intent = new Intent(CashoutSuccessActivity.this, FavouriteInputActivity.class);
                    intent.putExtra("DestMDN",getIntent().getExtras().getString("DestMDN"));
                    intent.putExtra("favCat",getIntent().getExtras().getString("favCat"));
                    intent.putExtra("idFavCat",getIntent().getExtras().getInt("idFavCat"));
                    startActivity(intent);
                }
            }else{
                Intent i = new Intent(CashoutSuccessActivity.this, UserHomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(CashoutSuccessActivity.this, UserHomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent i = new Intent(CashoutSuccessActivity.this, UserHomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return super.onKeyDown(keyCode, event);
    }
}
