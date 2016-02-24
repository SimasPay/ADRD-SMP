package com.payment.simaspay.userdetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 12/30/2015.
 */
public class Trans_DataSelectionActivity extends Activity {

    LinearLayout back_layout;

    TextView title, heading, firstmonth, secondmonth, last2months, datefrom_text, dateto_text;

    Button datefrom_button, dateto_button, next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dwnloaddetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }
        back_layout = (LinearLayout) findViewById(R.id.back_layout);

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title = (TextView) findViewById(R.id.title);
        heading = (TextView) findViewById(R.id.sample);
        firstmonth = (TextView) findViewById(R.id.current_month_textView);
        secondmonth = (TextView) findViewById(R.id.onemonth_textView);
        last2months = (TextView) findViewById(R.id.twomonths_textView);
        datefrom_text = (TextView) findViewById(R.id.manual_enter_from);
        dateto_text = (TextView) findViewById(R.id.manual_enter_to);

        datefrom_button = (Button) findViewById(R.id.from);
        dateto_button = (Button) findViewById(R.id.to);
        next = (Button) findViewById(R.id.downLoad_ss);

        title.setTypeface(Utility.Robot_Regular(Trans_DataSelectionActivity.this));
        heading.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        firstmonth.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        secondmonth.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        last2months.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        datefrom_text.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        dateto_text.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        datefrom_button.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        dateto_button.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        next.setTypeface(Utility.Robot_Regular(Trans_DataSelectionActivity.this));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Trans_DataSelectionActivity.this, TransactionsListActivity.class);
                intent.putExtra("lakupandai",getIntent().getExtras().getBoolean("lakupandai"));
                startActivityForResult(intent,10);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(resultCode==RESULT_OK){
                finish();
            }
        }
    }
}
