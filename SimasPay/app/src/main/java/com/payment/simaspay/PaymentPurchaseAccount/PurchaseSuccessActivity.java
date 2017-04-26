package com.payment.simaspay.PaymentPurchaseAccount;

import android.content.Intent;
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

import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.FavouriteInputActivity;
import simaspay.payment.com.simaspay.R;
import simaspay.payment.com.simaspay.UserHomeActivity;

import static com.payment.simaspay.services.Constants.LOG_TAG;


public class PurchaseSuccessActivity extends AppCompatActivity {
    CheckBox favBtn;
    Boolean isSetFav=false;
    TextView title, heading, name, name_field, number, number_field, amount, amount_field,transfer_field,transferID,charges,charges_field,total,total_field;
    View view;
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

        charges = (TextView) findViewById(R.id.products);
        charges_field = (TextView) findViewById(R.id.other_products);
        total = (TextView) findViewById(R.id.total);
        total_field = (TextView) findViewById(R.id.total_field);

        view=(View)findViewById(R.id.line);

        charges_field.setVisibility(View.VISIBLE);
        charges.setVisibility(View.VISIBLE);
        total_field.setVisibility(View.VISIBLE);
        total.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);


        transferID=(TextView)findViewById(R.id.transferID);
        transfer_field=(TextView)findViewById(R.id.transfer_field);

        ok = (Button) findViewById(R.id.ok);

        transferID.setText(getIntent().getExtras().getString("sctlID"));

        heading.setText("Pembelian Berhasil!");
        name.setText("Nama Produk");
        name_field.setText(getIntent().getExtras().getString("billerDetails"));
        number.setText("Nominal Pulsa");
        number_field.setText("Rp.  "+getIntent().getExtras().getString("originalAmount"));
        amount.setText("Nomor Handphone");
        amount_field.setText(getIntent().getExtras().getString("invoiceNo"));
        charges.setText("Biaya Administrasi");
        charges_field.setText("Rp. "+getIntent().getExtras().getString("charges"));
        total.setText("Total Pendebitan");
        total_field.setText("Rp. "+getIntent().getExtras().getString("amount"));

        amount_field.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize));
        number_field.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize));
        name_field.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize));
        charges_field.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize));
        total_field.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.textSize));

        favBtn=(CheckBox)findViewById(R.id.checkfav);
        favBtn.setOnClickListener(arg0 -> {
            if(!favBtn.isChecked()){
                isSetFav=false;
            } else{
                isSetFav=true;
            }
        });

        findViewById(R.id.ID_layout).setVisibility(View.VISIBLE);

        title.setTypeface(Utility.Robot_Regular(PurchaseSuccessActivity.this));
        heading.setTypeface(Utility.Robot_Regular(PurchaseSuccessActivity.this));
        name.setTypeface(Utility.Robot_Regular(PurchaseSuccessActivity.this));
        name_field.setTypeface(Utility.Robot_Light(PurchaseSuccessActivity.this));
        number.setTypeface(Utility.Robot_Regular(PurchaseSuccessActivity.this));
        number_field.setTypeface(Utility.Robot_Light(PurchaseSuccessActivity.this));
        amount.setTypeface(Utility.Robot_Regular(PurchaseSuccessActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(PurchaseSuccessActivity.this));
        transfer_field.setTypeface(Utility.Robot_Regular(PurchaseSuccessActivity.this));
        transferID.setTypeface(Utility.Robot_Light(PurchaseSuccessActivity.this));
        ok.setTypeface(Utility.Robot_Regular(PurchaseSuccessActivity.this));
        charges.setTypeface(Utility.Robot_Regular(PurchaseSuccessActivity.this));
        charges_field.setTypeface(Utility.Robot_Light(PurchaseSuccessActivity.this));
        total.setTypeface(Utility.Robot_Regular(PurchaseSuccessActivity.this));
        total_field.setTypeface(Utility.Robot_Light(PurchaseSuccessActivity.this));

        Log.d(LOG_TAG, "favCat:"+getIntent().getExtras().getString("favCat")+", idFavCat: "+getIntent().getExtras().getInt("idFavCat"));
        if(getIntent().getExtras().getString("favCode")!=null){
            Log.d(LOG_TAG, "favCode:"+getIntent().getExtras().getString("favCode"));
        }
        ok.setOnClickListener(view1 -> {
            if(favBtn.isChecked()){
                Log.d(LOG_TAG, "checked");
                if(getIntent().getExtras()!=null){
                    Intent intent = new Intent(PurchaseSuccessActivity.this, FavouriteInputActivity.class);
                    intent.putExtra("DestMDN",getIntent().getExtras().getString("invoiceNo"));
                    intent.putExtra("favCat",getIntent().getExtras().getString("favCat"));
                    intent.putExtra("idFavCat",getIntent().getExtras().getInt("idFavCat"));
                    if(getIntent().getExtras().getString("favCode")!=null){
                        intent.putExtra("favCode", getIntent().getExtras().getString("favCode"));
                    }
                    startActivity(intent);
                }
            }else{
                Intent i = new Intent(PurchaseSuccessActivity.this, UserHomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PurchaseSuccessActivity.this, UserHomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent i = new Intent(PurchaseSuccessActivity.this, UserHomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return super.onKeyDown(keyCode, event);
    }
}
