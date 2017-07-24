package com.payment.simaspay.AgentTransfer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.FavouriteInputActivity;
import simaspay.payment.com.simaspay.R;
import simaspay.payment.com.simaspay.UserHomeActivity;

import static com.payment.simaspay.services.Constants.LOG_TAG;


public class TransferOtherBankSuccessActivity extends AppCompatActivity {

    TextView title, label_fav, heading, id_field, id_value, name, name_field, bank, bank_field, number, number_field, amount, amount_field, charge, charge_field, total, total_field;
    LinearLayout backlayout;
    Button confirm;
    CheckBox favBtn;
    Boolean isSetFav=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transferotherbanksuccess);

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
        bank = (TextView) findViewById(R.id.bank);
        bank_field = (TextView) findViewById(R.id.bank_field);
        number = (TextView) findViewById(R.id.number);
        number_field = (TextView) findViewById(R.id.number_field);
        amount = (TextView) findViewById(R.id.amount);
        amount_field = (TextView) findViewById(R.id.amount_field);
        charge = (TextView) findViewById(R.id.charge);
        charge_field = (TextView) findViewById(R.id.charge_field);
        total = (TextView) findViewById(R.id.total);
        total_field = (TextView) findViewById(R.id.total_field);

        id_field = (TextView) findViewById(R.id.transfer_field);
        id_value = (TextView) findViewById(R.id.transferID);

        backlayout = (LinearLayout) findViewById(R.id.back_layout);

        backlayout.setVisibility(View.GONE);

        name_field.setText(getIntent().getExtras().getString("Name"));
        bank_field.setText(getIntent().getExtras().getString("BankName"));
        number_field.setText(getIntent().getExtras().getString("DestMDN"));
        amount_field.setText("Rp. "+getIntent().getExtras().getString("amount"));
        id_value.setText(getIntent().getExtras().getString("sctlID"));


        backlayout.setOnClickListener(view -> finish());

        title.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        heading.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        name.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        name_field.setTypeface(Utility.Robot_Light(TransferOtherBankSuccessActivity.this));
        bank.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        bank_field.setTypeface(Utility.Robot_Light(TransferOtherBankSuccessActivity.this));
        number.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        number_field.setTypeface(Utility.Robot_Light(TransferOtherBankSuccessActivity.this));
        amount.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        amount_field.setTypeface(Utility.Robot_Light(TransferOtherBankSuccessActivity.this));
        charge.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        charge_field.setTypeface(Utility.Robot_Light(TransferOtherBankSuccessActivity.this));
        total.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
        total_field.setTypeface(Utility.Robot_Light(TransferOtherBankSuccessActivity.this));

        id_value.setTypeface(Utility.Robot_Light(TransferOtherBankSuccessActivity.this));
        id_field.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));
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


        confirm = (Button) findViewById(R.id.next);

        confirm.setTypeface(Utility.Robot_Regular(TransferOtherBankSuccessActivity.this));

        confirm.setOnClickListener(view -> {
            if(favBtn.isChecked()){
                Log.d(LOG_TAG, "checked");
                if(getIntent().getExtras()!=null){
                    Intent intent = new Intent(TransferOtherBankSuccessActivity.this, FavouriteInputActivity.class);
                    intent.putExtra("DestMDN",getIntent().getExtras().getString("DestMDN"));
                    intent.putExtra("favCat",getIntent().getExtras().getString("favCat"));
                    intent.putExtra("idFavCat",getIntent().getExtras().getInt("idFavCat"));
                    startActivity(intent);
                }
            }else{
                Intent i = new Intent(TransferOtherBankSuccessActivity.this, UserHomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });




    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(TransferOtherBankSuccessActivity.this, UserHomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return super.onKeyDown(keyCode, event);
    }
}
