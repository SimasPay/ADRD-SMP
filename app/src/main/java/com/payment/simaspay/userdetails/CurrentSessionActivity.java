package com.payment.simaspay.userdetails;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 12/29/2015.
 */
public class CurrentSessionActivity extends Activity {

    LinearLayout back;

    TextView title, tanggal, tanggal_field, jam, jam_field, bal_head, balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bal_screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        back = (LinearLayout) findViewById(R.id.back_layout);

        tanggal = (TextView) findViewById(R.id.tanggal);
        tanggal_field = (TextView) findViewById(R.id.tanggal_field);
        jam = (TextView) findViewById(R.id.jam);
        jam_field = (TextView) findViewById(R.id.jam_field);
        title = (TextView) findViewById(R.id.title);
        bal_head = (TextView) findViewById(R.id.bal_head);
        balance = (TextView) findViewById(R.id.balc);

        tanggal.setTypeface(Utility.Robot_Regular(CurrentSessionActivity.this));
        tanggal_field.setTypeface(Utility.Robot_Light(CurrentSessionActivity.this));
        jam.setTypeface(Utility.Robot_Regular(CurrentSessionActivity.this));
        jam_field.setTypeface(Utility.Robot_Light(CurrentSessionActivity.this));
        title.setTypeface(Utility.Robot_Regular(CurrentSessionActivity.this));
        balance.setTypeface(Utility.Robot_Light(CurrentSessionActivity.this));
        bal_head.setTypeface(Utility.Robot_Regular(CurrentSessionActivity.this));

        balance.setText("Rp. "+getIntent().getExtras().getString("amount"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        jam_field.setText(" "+getIntent().getExtras().getString("Time"));
        tanggal_field.setText(" "+getIntent().getExtras().getString("Date"));




    }
}
