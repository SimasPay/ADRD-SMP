package simaspay.payment.com.simaspay;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.AgentTransfer.NewTransferHomeActivity;
import com.payment.simaspay.FlashizSDK.PayByQRActivity;
import com.payment.simaspay.PaymentPerchaseAccount.PaymentAndPerchaseAccountTypeActivity;
import com.payment.simaspay.agentdetails.NumberSwitchingActivity;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.userdetails.Trans_DataSelectionActivity;
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
    private static String mdn, mpin, nama, label_home;
    String rsaKey;
    ProgressBar progbar;
    private ImageButton switch_account, history_transaction, transfer, pembelian, pembayaran, pbq, logout;
    TextView checkbalance, phone_lbl, name_lbl, home_lbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simaspay_home);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        home_lbl=(TextView)findViewById(R.id.home_label);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        mdn = sharedPreferences.getString("mobileNumber", "");
        nama = sharedPreferences.getString("userName", "");
        mpin = sharedPreferences.getString("mpin", "");
        label_home = sharedPreferences.getString("akun","");

        switch_account=(ImageButton)findViewById(R.id.switch_account);
        logout=(ImageButton)findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, SecondLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sharedPreferences.edit().putString("userApiKey", "NONE").apply();
                startActivity(intent);
            }
        });

        if(label_home.equals("bank")){
            home_lbl.setText("Bank");
            switch_account.setVisibility(View.GONE);
        }else if(label_home.equals("nonkyc")){
            home_lbl.setText("E-Money Reguler");
            switch_account.setVisibility(View.GONE);
        }else if(label_home.equals("both")){
            home_lbl.setText("E-Money Plus");
            switch_account.setVisibility(View.VISIBLE);
        }
        Log.d("data", "mdn: "+mdn+", mpin: "+mpin);

        switch_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //switch account
                Intent intent = new Intent(UserHomeActivity.this, NumberSwitchingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        history_transaction=(ImageButton)findViewById(R.id.transaksi_btn);
        history_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, Trans_DataSelectionActivity.class);
                startActivity(intent);
            }
        });
        transfer=(ImageButton)findViewById(R.id.transfer_btn);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, NewTransferHomeActivity.class);
                intent.putExtra("simaspayuser", false);
                intent.putExtra("agentornot", false);
                startActivityForResult(intent, 20);
            }
        });

        pembelian=(ImageButton)findViewById(R.id.pembelian_btn);
        pembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, PaymentAndPerchaseAccountTypeActivity.class);
                intent.putExtra("accounttype", false);
                startActivity(intent);
            }
        });

        pembayaran=(ImageButton)findViewById(R.id.pembayaran_btn);
        pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, PaymentAndPerchaseAccountTypeActivity.class);
                intent.putExtra("accounttype", true);
                startActivity(intent);
            }
        });

        pbq=(ImageButton)findViewById(R.id.pbq_btn);
        pbq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, PayByQRActivity.class);
                startActivity(intent);
            }
        });




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
                        if(checkbalance.getText().equals("")||checkbalance.getText()==null){
                            progbar.setVisibility(View.VISIBLE);
                        }else{
                            progbar.setVisibility(View.GONE);
                        }
                        swipe_balance.setVisibility(View.GONE);
                        if(label_home.equals("bank")){
                            new CheckBalanceAsynTask().execute();
                        }else{
                            new reqCheckBalance().execute();
                        }
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
                }, 5000);
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

    class CheckBalanceAsynTask extends AsyncTask<Void, Void, Void> {
        String response;

        @Override
        protected Void doInBackground(Void... params) {


            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_CHECKBALANCE);
            mapContainer.put(Constants.PARAMETER_BANK_ID, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, sharedPreferences.getString("password", ""));
            if (sharedPreferences.getInt("userType", -1) == 0) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
            } else if (sharedPreferences.getInt("userType", -1) == 1) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
            } else if (sharedPreferences.getInt("userType", -1) == 2) {
                if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                } else {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                }
            }
            mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, UserHomeActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(checkbalance.getText().equals("")||checkbalance.getText()==null){
                progbar.setVisibility(View.VISIBLE);
            }else{
                progbar.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progbar.setVisibility(View.GONE);
            int msgCode=0;
            if (response != null) {
                Log.e("-------", "---------" + response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseContainer = null;
                try {
                    responseContainer = obj.parse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 631) {
                    checkbalance.setText("Timeout");
                } else if (msgCode == 274 || msgCode == 4) {
                    checkbalance.setText("Rp "+ responseContainer.getAmount() +"");
                }
            }
        }
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
            if(checkbalance.getText().equals("")||checkbalance.getText()==null){
                progbar.setVisibility(View.VISIBLE);
            }else{
                progbar.setVisibility(View.GONE);
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = getIntent();
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
