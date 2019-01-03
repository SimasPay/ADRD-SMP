package com.payment.simaspay.userdetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.payment.simaspay.R;
import com.payment.simaspay.services.Constants;


/**
 * Created by widy on 4/12/18.
 * 12
 */
public class FAQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewController());
        webView.requestFocus();
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);
                }
            }
        });
        webView.loadUrl(Constants.URL_FAQ);
    }

    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
