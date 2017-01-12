package simaspay.payment.com.simaspay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * Created by widy on 12/20/16.
 * 20
 */

public class TermsNConditionsActivity extends AppCompatActivity {
    Context context;
    SharedPreferences sharedPreferences;
    private WebView content_tnc;
    public SharedPreferences settings;
    public SharedPreferences.Editor editor;
    private Button batal, setuju;
    private static final String TAG = "Simaspay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsconditions);
        context = TermsNConditionsActivity.this;
        settings = getSharedPreferences(TAG, 0);
        editor = settings.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }
        batal = (Button)findViewById(R.id.cancel);
        batal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                TermsNConditionsActivity.this.finish();
            }
        });
        setuju = (Button)findViewById(R.id.submit);
        setuju.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                editor.putString("firsttime", "no").commit();
                Intent intent = new Intent(TermsNConditionsActivity.this, LandingScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });
        content_tnc = (WebView)findViewById(R.id.teks_tnc);
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
        content_tnc.loadUrl("http://banksinarmas.com/tabunganonline/simobi");
    }
}
