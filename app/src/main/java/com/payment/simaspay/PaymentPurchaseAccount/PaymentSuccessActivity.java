package com.payment.simaspay.PaymentPurchaseAccount;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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
import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.FavouriteInputActivity;
import simaspay.payment.com.simaspay.R;
import simaspay.payment.com.simaspay.UserHomeActivity;

import static com.payment.simaspay.services.Constants.LOG_TAG;

public class PaymentSuccessActivity extends AppCompatActivity {
    TextView title, heading, name, name_field, number, number_field, amount, amount_field, transfer_field, transferID, charges, charges_field, total, total_field;
    CheckBox favBtn;
    Boolean isSetFav=false;
    Button ok;
    View view;
    String AdditionalInfo="";

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

        title = findViewById(R.id.title);
        heading = findViewById(R.id.textview);
        name = findViewById(R.id.name);
        name_field = findViewById(R.id.name_field);
        number = findViewById(R.id.number);
        number_field = findViewById(R.id.number_field);
        amount = findViewById(R.id.amount);
        amount_field = findViewById(R.id.amount_field);

        charges = findViewById(R.id.products);
        charges_field = findViewById(R.id.other_products);

        total = findViewById(R.id.total);
        total_field = findViewById(R.id.total_field);

        transferID = findViewById(R.id.transferID);
        transfer_field = findViewById(R.id.transfer_field);

        TextView label_fav= findViewById(R.id.label_fav);
        favBtn= findViewById(R.id.checkfav);
        favBtn.setOnClickListener(arg0 -> {
            isSetFav = favBtn.isChecked();
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

        view = findViewById(R.id.line);
        transferID.setText(getIntent().getExtras().getString("sctlID"));

        ok = findViewById(R.id.ok);

        heading.setText("Pembayaran Berhasil!");
        name.setText("Nama Produk");
        name_field.setText(getIntent().getExtras().getString("billerDetails"));
        try {
            if(getIntent().getExtras().getString("numberTitle").equalsIgnoreCase("")){
                number.setText("Nomor Handphone");
            }else{
                number.setText(getIntent().getExtras().getString("numberTitle"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            number.setText("Nomor Handphone");
        }
        number_field.setText(getIntent().getExtras().getString("invoiceNo"));
        amount.setText("Jumlah");
        amount_field.setText("Rp. " + getIntent().getExtras().getString("originalAmount"));
        charges.setText("Biaya Administrasi");
        charges_field.setText("Rp. " + getIntent().getExtras().getString("charges"));
        total.setText("Total Pendebitan");
        total_field.setText("Rp. " + getIntent().getExtras().getString("totalAmount"));

        AdditionalInfo=getIntent().getExtras().getString("additionalInfo");
        if(!AdditionalInfo.equals("")){
            Log.d(LOG_TAG, "test AdditInfo: "+AdditionalInfo);
            charges.setVisibility(View.GONE);
            charges_field.setVisibility(View.VISIBLE);
            charges_field.setText(Html.fromHtml(AdditionalInfo));
            total.setVisibility(View.GONE);
            total_field.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            name_field.setVisibility(View.GONE);
            number.setVisibility(View.GONE);
            number_field.setVisibility(View.GONE);
            amount.setVisibility(View.GONE);
            amount_field.setVisibility(View.GONE);
        }else{
            charges.setVisibility(View.VISIBLE);
            charges.setText("Biaya Administrasi");
            charges_field.setText("Rp. " + getIntent().getExtras().getString("charges"));
            total.setText("Total Pendebitan");
            total_field.setText("Rp. " + getIntent().getExtras().getString("creditamt"));
        }

        amount_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        number_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        name_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        charges_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));
        total_field.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize));


        findViewById(R.id.ID_layout).setVisibility(View.VISIBLE);

        title.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));
        heading.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));
        name.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));
        name_field.setTypeface(Utility.Robot_Light(PaymentSuccessActivity.this));
        number.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));
        number_field.setTypeface(Utility.Robot_Light(PaymentSuccessActivity.this));
        amount.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(PaymentSuccessActivity.this));
        transfer_field.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));
        transferID.setTypeface(Utility.Robot_Light(PaymentSuccessActivity.this));
        ok.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));
        charges.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));
        charges_field.setTypeface(Utility.Robot_Light(PaymentSuccessActivity.this));
        total.setTypeface(Utility.Robot_Regular(PaymentSuccessActivity.this));
        total_field.setTypeface(Utility.Robot_Light(PaymentSuccessActivity.this));



        ok.setOnClickListener(view1 -> {
            if(favBtn.isChecked()){
                Log.d(LOG_TAG, "checked");
                if(getIntent().getExtras()!=null){
                    Intent intent = new Intent(PaymentSuccessActivity.this, FavouriteInputActivity.class);
                    intent.putExtra("DestMDN",getIntent().getExtras().getString("invoiceNo"));
                    intent.putExtra("favCat",getIntent().getExtras().getString("favCat"));
                    intent.putExtra("idFavCat",getIntent().getExtras().getInt("idFavCat"));
                    if(getIntent().getExtras().getString("favCode")!=null){
                        intent.putExtra("favCode", getIntent().getExtras().getString("favCode"));
                    }
                    startActivity(intent);
                }
            }else{
                Intent i = new Intent(PaymentSuccessActivity.this, UserHomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PaymentSuccessActivity.this, UserHomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(PaymentSuccessActivity.this, UserHomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return super.onKeyDown(keyCode, event);
    }
}
