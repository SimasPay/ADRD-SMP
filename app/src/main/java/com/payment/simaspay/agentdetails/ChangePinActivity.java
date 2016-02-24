package com.payment.simaspay.agentdetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/12/2016.
 */
public class ChangePinActivity extends Activity {

    TextView textView, textView1, textView2, title;

    EditText editText, editText1, editText2;

    Button simpan;
    LinearLayout btnBacke;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        textView = (TextView) findViewById(R.id.text1);
        textView1 = (TextView) findViewById(R.id.text2);
        textView2 = (TextView) findViewById(R.id.text3);


        title = (TextView) findViewById(R.id.titled);

        editText = (EditText) findViewById(R.id.editText1);
        editText1 = (EditText) findViewById(R.id.editText2);
        editText2 = (EditText) findViewById(R.id.editText3);

        simpan = (Button) findViewById(R.id.changepin);
        btnBacke = (LinearLayout) findViewById(R.id.back_layout);

        btnBacke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);


        textView1.setTypeface(Utility.Robot_Regular(ChangePinActivity.this));
        textView2.setTypeface(Utility.Robot_Regular(ChangePinActivity.this));
        textView.setTypeface(Utility.Robot_Regular(ChangePinActivity.this));

        title.setTypeface(Utility.RegularTextFormat(ChangePinActivity.this));

        editText2.setTypeface(Utility.Robot_Light(ChangePinActivity.this));
        editText.setTypeface(Utility.Robot_Light(ChangePinActivity.this));
        editText1.setTypeface(Utility.Robot_Light(ChangePinActivity.this));

        simpan.setTypeface(Utility.Robot_Regular(ChangePinActivity.this));


        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (editText.getText().toString().length() <= 0) {

                } else if (editText.getText().toString().length() <= 0) {

                } else if (editText1.getText().toString().length() <= 0) {

                } else if (editText1.getText().toString().length() <= 0) {

                } else if (editText2.getText().toString().length() <= 0) {

                } else if (editText2.getText().toString().length() <= 0) {

                } else if (!editText1.getText().toString().equals(editText2.getText().toString())) {

                } else {*/
                    Intent intent = new Intent(ChangePinActivity.this, ChangePinSuccessActivity.class);
                    startActivityForResult(intent, 10);
//                }
            }
        });
    }
}
