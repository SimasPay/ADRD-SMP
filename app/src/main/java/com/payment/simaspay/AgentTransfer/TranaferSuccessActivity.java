package com.payment.simaspay.AgentTransfer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import com.payment.simaspay.activities.FavouriteInputActivity;
import com.payment.simaspay.R;
import com.payment.simaspay.activities.UserHomeActivity;

import static com.payment.simaspay.services.Constants.LOG_TAG;


public class TranaferSuccessActivity extends AppCompatActivity {
    TextView title, label_fav, heading, name, name_field, number, number_field, amount, amount_field,products,other_products,transfer_field,transferID;
    Button ok;
    CheckBox favBtn;
    Boolean isSetFav=false;

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

        label_fav=(TextView)findViewById(R.id.label_fav);
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
        transferID=(TextView)findViewById(R.id.transferID);
        transfer_field=(TextView)findViewById(R.id.transfer_field);
        findViewById(R.id.ID_layout).setVisibility(View.VISIBLE);

        products.setVisibility(View.VISIBLE);
        other_products.setVisibility(View.VISIBLE);
        transferID.setVisibility(View.VISIBLE);


        transferID.setText(getIntent().getExtras().getString("sctlID"));
        name.setText("Nama Pemilik Rekening");
        name_field.setText(getIntent().getExtras().getString("Name"));
        number.setText("Bank Tujuan");
        number_field.setText("Bank Sinarmas");
        amount.setText("Nomor Rekening Tujuan");
        amount_field.setText(getIntent().getExtras().getString("DestMDN"));
        products.setText("Jumlah");
        other_products.setText("Rp. "+getIntent().getExtras().getString("amount"));

        heading.setText("Transfer Berhasil!");

        ok = (Button) findViewById(R.id.ok);
        title.setTypeface(Utility.Robot_Regular(TranaferSuccessActivity.this));
        heading.setTypeface(Utility.Robot_Regular(TranaferSuccessActivity.this));
        name.setTypeface(Utility.Robot_Regular(TranaferSuccessActivity.this));
        name_field.setTypeface(Utility.Robot_Light(TranaferSuccessActivity.this));
        number.setTypeface(Utility.Robot_Regular(TranaferSuccessActivity.this));
        number_field.setTypeface(Utility.Robot_Light(TranaferSuccessActivity.this));
        amount.setTypeface(Utility.Robot_Regular(TranaferSuccessActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(TranaferSuccessActivity.this));
        products.setTypeface(Utility.Robot_Regular(TranaferSuccessActivity.this));
        other_products.setTypeface(Utility.Robot_Light(TranaferSuccessActivity.this));
        transfer_field.setTypeface(Utility.Robot_Regular(TranaferSuccessActivity.this));
        transferID.setTypeface(Utility.Robot_Light(TranaferSuccessActivity.this));
        ok.setTypeface(Utility.Robot_Regular(TranaferSuccessActivity.this));

        ok.setOnClickListener(view -> {
            if(favBtn.isChecked()){
                Log.d(LOG_TAG, "checked");
                if(getIntent().getExtras()!=null){
                    Intent intent = new Intent(TranaferSuccessActivity.this, FavouriteInputActivity.class);
                    intent.putExtra("DestMDN",getIntent().getExtras().getString("DestMDN"));
                    intent.putExtra("favCat",getIntent().getExtras().getString("favCat"));
                    intent.putExtra("idFavCat",getIntent().getExtras().getInt("idFavCat"));
                    startActivityForResult(intent, 10);
                }
            }else{
                Intent i = new Intent(TranaferSuccessActivity.this, UserHomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(TranaferSuccessActivity.this, UserHomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent i = new Intent(TranaferSuccessActivity.this, UserHomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return super.onKeyDown(keyCode, event);
    }

}
