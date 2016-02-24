package com.payment.simaspay.Referral_and_CloseAccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/28/2016.
 */
public class ReferralDetailsActivity extends Activity {

    TextView Name, number, email, others_text, title;

    EditText name_field, number_field, mail_field, others_field;

    Button others, submit;

    LinearLayout back_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referralaccountdetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        Name = (TextView) findViewById(R.id.name);
        number = (TextView) findViewById(R.id.number);
        email = (TextView) findViewById(R.id.mail);
        others_text = (TextView) findViewById(R.id.produk);

        back_layout=(LinearLayout)findViewById(R.id.back_layout);

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title = (TextView) findViewById(R.id.titled);

        name_field = (EditText) findViewById(R.id.name_Editfield);
        number_field = (EditText) findViewById(R.id.number_Editfield);
        mail_field = (EditText) findViewById(R.id.mail_Editfield);
        others_field = (EditText) findViewById(R.id.lainnya_editText1);

        submit = (Button) findViewById(R.id.submit);
        others = (Button) findViewById(R.id.lainnya);

        Name.setTypeface(Utility.Robot_Regular(ReferralDetailsActivity.this));
        number.setTypeface(Utility.Robot_Regular(ReferralDetailsActivity.this));
        email.setTypeface(Utility.Robot_Regular(ReferralDetailsActivity.this));
        others_text.setTypeface(Utility.Robot_Regular(ReferralDetailsActivity.this));
        title.setTypeface(Utility.Robot_Regular(ReferralDetailsActivity.this));

        submit.setTypeface(Utility.Robot_Regular(ReferralDetailsActivity.this));

        name_field.setTypeface(Utility.Robot_Light(ReferralDetailsActivity.this));
        number_field.setTypeface(Utility.Robot_Light(ReferralDetailsActivity.this));
        mail_field.setTypeface(Utility.Robot_Light(ReferralDetailsActivity.this));
        others_field.setTypeface(Utility.Robot_Light(ReferralDetailsActivity.this));
        others.setTypeface(Utility.Robot_Light(ReferralDetailsActivity.this));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReferralDetailsActivity.this,ReferralAccountConfirmationActivity.class);
                startActivityForResult(intent,10);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(resultCode==10){
                finish();
            }
        }
    }
}
