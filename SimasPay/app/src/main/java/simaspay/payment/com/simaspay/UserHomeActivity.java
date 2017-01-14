package simaspay.payment.com.simaspay;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.utils.OnSwipeTouchListener;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by widy on 1/13/17.
 * 13
 */

public class UserHomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SimasPay";
    SharedPreferences sharedPreferences;
    private static String mdn, mpin, nama;
    String rsaKey;
    ProgressBar progbar;
    TextView checkbalance, phone_lbl, name_lbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simaspay_home);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        mdn = sharedPreferences.getString("mobileNumber", "");
        nama = sharedPreferences.getString("userName", "");
        mpin = sharedPreferences.getString("mpin", "");
        Log.d("data", "mdn: "+mdn+", mpin: "+mpin);

        progbar = (ProgressBar)findViewById(R.id.progressbar);
        progbar.setVisibility(View.GONE);
        final View swipe_balance=(View)findViewById(R.id.swipe_balance);
        name_lbl=(TextView)findViewById(R.id.fullname_lbl);
        name_lbl.setText(nama);
        phone_lbl=(TextView)findViewById(R.id.mobilephone_lbl);
        phone_lbl.setText(mdn);
        checkbalance=(TextView)findViewById(R.id.check_balance_lbl);
        checkbalance.setVisibility(View.GONE);
        swipe_balance.setOnTouchListener(new OnSwipeTouchListener(UserHomeActivity.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();

            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                Animation RightSwipe = AnimationUtils.loadAnimation(UserHomeActivity.this, R.anim.right_to_left);
                RightSwipe.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        checkbalance.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        checkbalance.setVisibility(View.VISIBLE);
                        progbar.setVisibility(View.VISIBLE);
                        swipe_balance.setVisibility(View.GONE);
                        new reqCheckBalance().execute();
                        //linLay.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                swipe_balance.startAnimation(RightSwipe);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe_balance.setVisibility(View.VISIBLE);
                        checkbalance.setVisibility(View.GONE);
                        progbar.setVisibility(View.GONE);
                        swipe_balance.startAnimation(inFromRightAnimation());
                    }
                }, 3000);
                // Put your logic here for text visibility and for timer like progress bar for 5 second and setText
            }
    });
    }

    private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    class reqCheckBalance extends AsyncTask<Void, Void, Void> {
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            String module = sharedPreferences.getString("MODULE", "NONE");
            String exponent = sharedPreferences.getString("EXPONENT", "NONE");

            try {
                rsaKey = CryptoService.encryptWithPublicKey(module, exponent,
                        mpin.getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("service", "Wallet");
            mapContainer.put("txnName", "CheckBalance");
            mapContainer.put("institutionID", "simaspay");
            mapContainer.put("authenticationKey", "");
            mapContainer.put("sourceMDN", mdn);
            mapContainer.put("sourcePIN", CryptoService.encryptWithPublicKey(module, exponent, mpin.getBytes()));
            mapContainer.put("bankID", "");
            mapContainer.put("sourcePocketCode", "1");
            mapContainer.put("channelID", "7");
            Log.e("-----",""+mapContainer.toString());

            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    UserHomeActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progbar.setVisibility(View.GONE);
            if (response != null) {
                Log.e("-------", "=====" + response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseDataContainer = null;
                try {
                    responseDataContainer = obj.parse(response);
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.toString());
                }
                try {
                    if (responseDataContainer != null) {
                        Log.d("test", "not null");
                        String amount = responseDataContainer.getAmount();
                        checkbalance.setText("Rp "+ amount +"");
                    }
                }catch (Exception e) {
                    Log.d(LOG_TAG, "ERROR: " + e.toString());
                }
            }

        }

    }
}
