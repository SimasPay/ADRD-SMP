package com.payment.simaspay.agentdetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.R;
import com.payment.simaspay.activities.UserHomeActivity;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.utils.Functions;

public class NumberSwitchingActivity extends AppCompatActivity {

    TextView textView, agent_text, reg_text, agent_number, reg_number, lagout_text;
    LinearLayout agent_layout, reg_layout, logOut;
    Context context;
    SharedPreferences sharedPreferences;
    Functions func;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.number_switching);

        func=new Functions(this);
        func.initiatedToolbar(this);

        textView = (TextView) findViewById(R.id.text_3);
        agent_text = (TextView) findViewById(R.id.agent_text);
        reg_text = (TextView) findViewById(R.id.reg_text);
        agent_number = (TextView) findViewById(R.id.agent_number);
        reg_number = (TextView) findViewById(R.id.reguler);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        textView.setTypeface(Utility.Robot_Light(NumberSwitchingActivity.this));
        agent_text.setTypeface(Utility.Robot_Light(NumberSwitchingActivity.this));
        reg_text.setTypeface(Utility.Robot_Light(NumberSwitchingActivity.this));
        agent_number.setTypeface(Utility.Robot_Light(NumberSwitchingActivity.this));
        reg_number.setTypeface(Utility.Robot_Light(NumberSwitchingActivity.this));

        lagout_text = (TextView) findViewById(R.id.lagout_text);
        lagout_text.setTypeface(Utility.Robot_Light(NumberSwitchingActivity.this));

        agent_layout = (LinearLayout) findViewById(R.id.agent_layout);
        reg_layout = (LinearLayout) findViewById(R.id.reg_layout);
        logOut = (LinearLayout) findViewById(R.id.logOut);

        agent_number.setText(sharedPreferences.getString(Constants.PARAMETER_PHONENUMBER, ""));
        reg_number.setText(sharedPreferences.getString(Constants.PARAMETER_ACCOUNTNUMBER, ""));


        agent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putInt(Constants.PARAMETER_AGENTTYPE, Constants.CONSTANT_EMONEY_INT).apply();
                Intent intent = new Intent(NumberSwitchingActivity.this, UserHomeActivity.class);
                sharedPreferences.edit().putString(Constants.PARAMETER_USES_AS, Constants.CONSTANT_EMONEY_PLUS).apply();
                sharedPreferences.edit().putInt(Constants.PARAMETER_USERTYPE, Constants.CONSTANT_EMONEY_INT).apply();
                startActivityForResult(intent, 20);
                finish();
            }
        });

        reg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putInt(Constants.PARAMETER_AGENTTYPE, Constants.CONSTANT_BANK_INT).apply();
                Intent intent = new Intent(NumberSwitchingActivity.this, UserHomeActivity.class);
                intent.putExtra("direct", true);
                sharedPreferences.edit().putString(Constants.PARAMETER_USES_AS, Constants.CONSTANT_BANK_USER).apply();
                sharedPreferences.edit().putInt(Constants.PARAMETER_USERTYPE, Constants.CONSTANT_BANK_INT).apply();
                startActivityForResult(intent, 20);
                finish();

            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NumberSwitchingActivity.this, SecondLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sharedPreferences.edit().putString(Constants.PARAMETER_USERAPIKEY, Constants.CONSTANTS_NONE).apply();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(Constants.LOG_TAG, "canceled");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
