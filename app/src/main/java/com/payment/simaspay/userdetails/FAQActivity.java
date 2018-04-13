package com.payment.simaspay.userdetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.payment.simaspay.services.Constants;

import simaspay.payment.com.simaspay.R;

/**
 * Created by widy on 4/12/18.
 * 12
 */
public class FAQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewController());
        webView.loadUrl(Constants.URL_FAQ);
        webView.requestFocus();
    }

    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
