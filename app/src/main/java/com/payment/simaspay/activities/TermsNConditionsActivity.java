package com.payment.simaspay.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.payment.simaspay.R;
import com.payment.simaspay.services.Constants;

import static com.payment.simaspay.services.Constants.LOG_TAG;

/**
 * Created by widy on 12/20/16.
 * 20
 */

public class TermsNConditionsActivity extends AppCompatActivity {
    Context context;
    SharedPreferences sharedPreferences;
    public SharedPreferences settings;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsconditions);
        context = TermsNConditionsActivity.this;
        settings = getSharedPreferences(LOG_TAG, 0);
        editor = settings.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }
        Button batal = findViewById(R.id.cancel);
        batal.setOnClickListener(view -> TermsNConditionsActivity.this.finish());
        Button setuju = findViewById(R.id.submit);
        setuju.setOnClickListener(view -> {
            editor.putString("firsttime", "no").apply();
            Intent intent = new Intent(TermsNConditionsActivity.this, LandingScreenActivity.class);
            startActivity(intent);
            finish();
        });
        WebView content_tnc = findViewById(R.id.teks_tnc);
        WebSettings settings = content_tnc.getSettings();
        content_tnc.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.startsWith("http://")) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    return false;
                }
            }
        });
        settings.setDefaultTextEncodingName("utf-8");
        content_tnc.loadUrl(Constants.URL_TC);
    }
}
