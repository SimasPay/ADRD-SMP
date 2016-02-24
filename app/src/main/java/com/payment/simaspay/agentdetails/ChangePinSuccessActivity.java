package com.payment.simaspay.agentdetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/12/2016.
 */
public class ChangePinSuccessActivity extends Activity {

    TextView title,textView;

    Button ok;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=getIntent();
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepinsuccess);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        title=(TextView)findViewById(R.id.titled);
        textView=(TextView)findViewById(R.id.success);

        ok=(Button)findViewById(R.id.changepinsuccess);

        title.setTypeface(Utility.Robot_Regular(ChangePinSuccessActivity.this));
        textView.setTypeface(Utility.Robot_Regular(ChangePinSuccessActivity.this));

        ok.setTypeface(Utility.Robot_Regular(ChangePinSuccessActivity.this));

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

    }
}
