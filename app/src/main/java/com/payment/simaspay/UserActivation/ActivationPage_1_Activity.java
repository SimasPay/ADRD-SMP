package com.payment.simaspay.UserActivation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.R;
import com.payment.simaspay.services.Utility;

/**
 * Created by Nagendra P on 12/21/2015.
 */
public class ActivationPage_1_Activity extends Activity {

    TextView terms_conditions;

    Button cancel, accept;

    SharedPreferences sharedPreferences;

    ProgressDialog progressDialog;

    String rsaKey;

    WebView webView;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_FIRST_USER, intent);
                finish();
            } else if (resultCode == 12) {
                Intent intent = getIntent();
                setResult(12, intent);
                finish();
            } else if (resultCode == 13) {
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page_1);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        terms_conditions = (TextView) findViewById(R.id.terms_conditions);

        cancel = (Button) findViewById(R.id.cancel);
        accept = (Button) findViewById(R.id.accept);

        terms_conditions.setTypeface(Utility.Robot_Regular(ActivationPage_1_Activity.this));
        cancel.setTypeface(Utility.Robot_Regular(ActivationPage_1_Activity.this));
        accept.setTypeface(Utility.Robot_Regular(ActivationPage_1_Activity.this));


//        JustifiedTextView jtv= new JustifiedTextView(getApplicationContext(), "Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Nam liber te conscient to factor tum poen legum odioque civiuda. Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Nam liber te conscient to factor tum poen legum odioque civiuda.");

        progressDialog = new ProgressDialog(ActivationPage_1_Activity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
        progressDialog.show();

        webView = new WebView(ActivationPage_1_Activity.this);
        webView.setVisibility(View.GONE);

        webView.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {


                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                webView.setVisibility(View.VISIBLE);
            }


        });

        webView.loadUrl("http://banksinarmas.com/tabunganonline/simobi");

        LinearLayout place = (LinearLayout) findViewById(R.id.textlayout);
        place.addView(webView);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivationPage_1_Activity.this, ActivationPage_2_Activity.class);
                startActivityForResult(intent, 10);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = getIntent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
