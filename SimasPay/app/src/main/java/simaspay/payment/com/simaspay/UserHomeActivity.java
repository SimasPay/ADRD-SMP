package simaspay.payment.com.simaspay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dimo.PayByQR.PayByQRSDK;
import com.payment.simaspay.AgentTransfer.NewTransferHomeActivity;
import com.payment.simaspay.AgentTransfer.NewWithdrawHomeActivity;
import com.payment.simaspay.FlashizSDK.PayByQRActivity;
import com.payment.simaspay.PaymentPerchaseAccount.PaymentAndPerchaseAccountTypeActivity;
import com.payment.simaspay.agentdetails.ChangePinActivity;
import com.payment.simaspay.agentdetails.NumberSwitchingActivity;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.userdetails.Trans_DataSelectionActivity;
import com.payment.simaspay.userdetails.TransactionsListActivity;
import com.payment.simaspay.utils.Functions;
import com.payment.simaspay.utils.OnSwipeTouchListener;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by widy on 1/13/17.
 * 13
 */

public class UserHomeActivity extends AppCompatActivity {
    private static final int EXIT = 20;
    private static final String LOG_TAG = "SimasPay";
    SharedPreferences sharedPreferences;
    private static String mdn, mpin, nama, label_home;
    String rsaKey;
    ProgressBar progbar;
    String accountSelected = "", sourceMDN, stMPIN;
    private ImageButton gantimpin, switch_account, history_transaction, transfer, pembelian, pembayaran, pbq, promopbq, tariktunai, logout, daftaremoney;
    TextView checkbalance, phone_lbl, name_lbl, home_lbl;
    LinearLayout daftaremoneylay;
    Functions func;
    public View swipe_balance;
    String fromDate, toDate;
    Calendar calendar;
    SimpleDateFormat sdf, sdf1;
    private ImageView photo;
    SharedPreferences languageSettings, settings;
    String selectedLanguage;
    protected static final int REQ_CODE_PICK_IMAGE = 1;
    ProgressDialog progressDialog;
    int msgCode;
    Context context;
    private AlertDialog.Builder alertbox;
    private static AlertDialog dialogBuilder;
    private String encodedImg, encodedImgFromAPI;
    final int CAMERA_CAPTURE = 2;
    final int CROP_PIC = 3;
    private Uri picUri;
    private Bitmap profpic;
    private TextView initname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simaspay_home);
        func = new Functions(this);
        func.initiatedToolbar(this);

        home_lbl = (TextView) findViewById(R.id.home_label);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString(Constants.PARAMETER_PHONENUMBER, "");
        String pin = settings.getString(Constants.PARAMETER_MPIN, "");
        stMPIN = func.generateRSA(pin);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");
        mdn = sharedPreferences.getString(Constants.PARAMETER_PHONENUMBER, "");
        nama = sharedPreferences.getString("userName", "");
        mpin = sharedPreferences.getString(Constants.PARAMETER_MPIN, "");
        label_home = sharedPreferences.getString(Constants.PARAMETER_TYPEUSER, "");
        switch_account = (ImageButton) findViewById(R.id.switch_account);
        logout = (ImageButton) findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, SecondLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sharedPreferences.edit().putString(Constants.PARAMETER_USERAPIKEY, "NONE").apply();
                startActivity(intent);
            }
        });

        //Profpic / Profile Picture
        photo = (ImageView) findViewById(R.id.profile_pic);
        initname = (TextView)findViewById(R.id.initname);
        encodedImgFromAPI = sharedPreferences.getString(Constants.PARAMETER_PROFPICSTRING, "");
        if(!encodedImgFromAPI.equals("")){
            profpic = decodeBase64(encodedImgFromAPI);
            if(profpic!=null||!profpic.equals("")){
                photo.setImageBitmap(profpic);
                initname.setVisibility(View.INVISIBLE);
            }
        }else{
            String fullname = sharedPreferences.getString("fullname", "");
            initname.setText(initialName(fullname));
            initname.setVisibility(View.VISIBLE);
            photo.setImageDrawable(getResources().getDrawable(R.drawable.circle_bg));
        }
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        phone_lbl = (TextView) findViewById(R.id.mobilephone_lbl);
        transfer = (ImageButton) findViewById(R.id.transfer_btn);
        tariktunai = (ImageButton) findViewById(R.id.tariktunai_btn);
        if (label_home.equals(Constants.CONSTANT_BANK_USER)) {
            home_lbl.setText(getResources().getString(R.string.bank));
            phone_lbl.setText(sharedPreferences.getString(Constants.PARAMETER_ACCOUNTNUMBER, ""));
            switch_account.setVisibility(View.GONE);
            tariktunai.setEnabled(false);
            tariktunai.setAlpha((float) 0.2);
            LinearLayout tariktunailay = (LinearLayout) findViewById(R.id.layout_tariktunai);
            tariktunailay.setVisibility(View.INVISIBLE);
            accountSelected = Constants.CONSTANT_BANK_USER;
        } else if (label_home.equals(Constants.CONSTANT_EMONEYNONKYC_USER)) {
            home_lbl.setText(getResources().getString(R.string.id_emoney_reguler));
            phone_lbl.setText(mdn);
            switch_account.setVisibility(View.GONE);
            transfer.setEnabled(false);
            tariktunai.setEnabled(false);
            transfer.setAlpha((float) 0.2);
            tariktunai.setAlpha((float) 0.2);
            LinearLayout tariktunailay = (LinearLayout) findViewById(R.id.layout_tariktunai);
            tariktunailay.setBackgroundResource(R.drawable.borderwhitetrans);
            LinearLayout transferlay = (LinearLayout) findViewById(R.id.transferlay);
            transferlay.setBackgroundResource(R.drawable.borderwhitetrans);
            accountSelected = Constants.CONSTANT_EMONEY_REGULER;
        } else if (label_home.equals(Constants.CONSTANT_EMONEYKYC_USER)) {
            home_lbl.setText(getResources().getString(R.string.id_emoney_plus));
            phone_lbl.setText(sharedPreferences.getString(Constants.PARAMETER_PHONENUMBER, ""));
            switch_account.setVisibility(View.GONE);
            accountSelected = Constants.CONSTANT_EMONEY_PLUS;
        } else if (label_home.equals(Constants.CONSTANT_BOTH_USER)) {
            String account = sharedPreferences.getString(Constants.PARAMETER_USES_AS, "");
            accountSelected = account;
            if (account.equals(Constants.CONSTANT_BANK_USER)) {
                home_lbl.setText(getResources().getString(R.string.bank));
                phone_lbl.setText(sharedPreferences.getString(Constants.PARAMETER_ACCOUNTNUMBER, ""));
                LinearLayout tariktunailay = (LinearLayout) findViewById(R.id.layout_tariktunai);
                tariktunailay.setVisibility(View.INVISIBLE);
            } else {
                home_lbl.setText(getResources().getString(R.string.id_emoney_plus));
                phone_lbl.setText(sharedPreferences.getString(Constants.PARAMETER_PHONENUMBER, ""));
            }
            switch_account.setVisibility(View.VISIBLE);
        }
        Log.d("data", "mdn: " + mdn + ", mpin: " + mpin);
        sharedPreferences.edit().putString(Constants.PARAMETER_USES_AS, accountSelected).apply();

        switch_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //switch account
                Intent intent = new Intent(UserHomeActivity.this, NumberSwitchingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        history_transaction = (ImageButton) findViewById(R.id.transaksi_btn);
        history_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (label_home.equals(Constants.CONSTANT_BANK_USER) || (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_BANK_INT)) {
                    Calendar calendar1 = Calendar.getInstance();
                    sdf = new SimpleDateFormat("ddMMyyyy");
                    sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                    toDate = sdf.format(calendar1.getTime());
                    calendar1.set(Calendar.DAY_OF_MONTH, 1);
                    fromDate = sdf.format(calendar1.getTime());
//                    new DwnLoadAsynTask().execute();
                    Intent intent = new Intent(UserHomeActivity.this, TransactionsListActivity.class);
                    intent.putExtra("fromDate", fromDate);
                    intent.putExtra("toDate", toDate);
                    startActivityForResult(intent, 10);
                } else {
                    Intent intent = new Intent(UserHomeActivity.this, Trans_DataSelectionActivity.class);
                    startActivity(intent);
                }
            }
        });

        pembelian = (ImageButton) findViewById(R.id.pembelian_btn);
        pembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, PaymentAndPerchaseAccountTypeActivity.class);
                intent.putExtra("accounttype", false);
                startActivity(intent);
            }
        });

        pembayaran = (ImageButton) findViewById(R.id.pembayaran_btn);
        pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, PaymentAndPerchaseAccountTypeActivity.class);
                intent.putExtra("accounttype", true);
                startActivity(intent);
            }
        });

        pbq = (ImageButton) findViewById(R.id.pbq_btn);
        pbq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, PayByQRActivity.class);
                intent.putExtra(PayByQRActivity.INTENT_EXTRA_MODULE, PayByQRSDK.MODULE_PAYMENT);
                startActivity(intent);
            }
        });

        promopbq = (ImageButton) findViewById(R.id.promo_pbq_btn);
        promopbq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, PayByQRActivity.class);
                intent.putExtra(PayByQRActivity.INTENT_EXTRA_MODULE, PayByQRSDK.MODULE_LOYALTY);
                startActivity(intent);
            }
        });

        daftaremoneylay = (LinearLayout) findViewById(R.id.daftar_emoney);
        daftaremoney = (ImageButton) findViewById(R.id.daftar_emoney_btn);
        if (sharedPreferences.getString(Constants.PARAMETER_TYPEUSER, "").equals(Constants.CONSTANT_BANK_USER)) {
            daftaremoneylay.setVisibility(View.VISIBLE);
            daftaremoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UserHomeActivity.this, DaftarEmoneyActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            daftaremoneylay.setVisibility(View.INVISIBLE);
        }

        daftaremoney = (ImageButton) findViewById(R.id.daftar_emoney_btn);
        daftaremoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, DaftarEmoneyActivity.class);
                startActivity(intent);
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, NewTransferHomeActivity.class);
                intent.putExtra("simaspayuser", false);
                intent.putExtra("agentornot", false);
                sharedPreferences.edit().putString(Constants.PARAMETER_USES_AS, accountSelected).apply();
                startActivityForResult(intent, 20);
            }
        });

        tariktunai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, NewWithdrawHomeActivity.class);
                sharedPreferences.edit().putString(Constants.PARAMETER_USES_AS, accountSelected).apply();
                startActivity(intent);
            }
        });

        gantimpin = (ImageButton) findViewById(R.id.gantimpin_btn);
        gantimpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomeActivity.this, ChangePinActivity.class);
                intent.putExtra("simaspayuser", false);
                intent.putExtra("agentornot", false);
                sharedPreferences.edit().putString(Constants.PARAMETER_USES_AS, accountSelected).apply();
                startActivityForResult(intent, 20);
            }
        });


        progbar = (ProgressBar) findViewById(R.id.progressbar);
        progbar.setVisibility(View.GONE);
        swipe_balance = (View) findViewById(R.id.swipe_balance);
        name_lbl = (TextView) findViewById(R.id.fullname_lbl);
        name_lbl.setText(nama);
        checkbalance = (TextView) findViewById(R.id.check_balance_lbl);
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
                        if (checkbalance.getText().equals("") || checkbalance.getText() == null) {
                            progbar.setVisibility(View.VISIBLE);
                        } else {
                            progbar.setVisibility(View.GONE);
                        }
                        swipe_balance.setVisibility(View.GONE);
                        if (isNetworkAvailable() == true) {
                            checkbalance.setText("");
                            progbar.setVisibility(View.VISIBLE);
                            new CheckBalanceAsynTask().execute();
                        } else {
                            checkbalance.setText(getResources().getString(R.string.id_no_inetconnectivity));
                            progbar.setVisibility(View.GONE);
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
                        }

                        //linLay.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                swipe_balance.startAnimation(RightSwipe);

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
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, mdn);
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_KEY, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, sharedPreferences.getString("password", ""));
            if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_BANK_INT) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
            } else if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_BANKSINARMAS_INT) {
                mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK_SINARMAS);
            } else if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_LAKUPANDAI_INT) {
                if (sharedPreferences.getInt(Constants.PARAMETER_AGENTTYPE, -1) == 1) {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                } else {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                }
            } else if (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_EMONEY_INT) {
                if (sharedPreferences.getInt(Constants.PARAMETER_AGENTTYPE, -1) == Constants.CONSTANT_EMONEY_INT) {
                    Log.d(LOG_TAG, "emoney");
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                } else if (sharedPreferences.getInt(Constants.PARAMETER_AGENTTYPE, -1) == Constants.CONSTANT_BANK_INT) {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                }
            }
            //mapContainer.put(Constants.PARAMTER_MFA_TRANSACTION, Constants.TRANSACTION_MFA_TRANSACTION);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, UserHomeActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (checkbalance.getText().equals("") || checkbalance.getText() == null) {
                progbar.setVisibility(View.VISIBLE);
            } else {
                progbar.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progbar.setVisibility(View.GONE);
            int msgCode = 0;
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
                } else if (msgCode == 571) {
                    checkbalance.setText("Requested  account with the given cardpan is not available");
                } else if (msgCode == 274 || msgCode == 4) {
                    //checkbalance.setText("Rp "+ responseContainer.getAmount() +"");
                    checkbalance.setText(Html.fromHtml("<small>Rp </small><big>" + responseContainer.getAmount() + "</big>"));
                }
            } else {
                checkbalance.setText(getResources().getString(R.string.id_no_inetconnectivity));
            }
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
        }
    }

    /**
     * class reqCheckBalance extends AsyncTask<Void, Void, Void> {
     * String response;
     *
     * @Override protected Void doInBackground(Void... params) {
     * rsaKey=func.generateRSA(mpin);
     * Map<String, String> mapContainer = new HashMap<>();
     * String account = sharedPreferences.getString(Constants.PARAMETER_USES_AS,"");
     * accountSelected=account;
     * if(account.equals(Constants.CONSTANT_BANK_USER)){
     * Log.d(LOG_TAG, "bank");
     * mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
     * mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
     * }else{
     * mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
     * mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
     * }
     * mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, Constants.TRANSACTION_CHECKBALANCE);
     * mapContainer.put(Constants.PARAMETER_INSTITUTION_ID, Constants.CONSTANT_INSTITUTION_ID);
     * mapContainer.put("authenticationKey", "");
     * mapContainer.put("sourceMDN", mdn);
     * mapContainer.put("sourcePIN", rsaKey);
     * mapContainer.put("bankID", "");
     * mapContainer.put("channelID", "7");
     * Log.e("-----",""+mapContainer.toString());
     * <p>
     * WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
     * UserHomeActivity.this);
     * response = webServiceHttp.getResponseSSLCertificatation();
     * return null;
     * }
     * @Override protected void onPreExecute() {
     * super.onPreExecute();
     * if(checkbalance.getText().equals("")||checkbalance.getText()==null){
     * progbar.setVisibility(View.VISIBLE);
     * }else{
     * progbar.setVisibility(View.GONE);
     * }
     * }
     * @Override protected void onPostExecute(Void aVoid) {
     * super.onPostExecute(aVoid);
     * progbar.setVisibility(View.GONE);
     * if (response != null) {
     * Log.e("-------", "=====" + response);
     * XMLParser obj = new XMLParser();
     * EncryptedResponseDataContainer responseDataContainer = null;
     * try {
     * responseDataContainer = obj.parse(response);
     * } catch (Exception e) {
     * Log.e(LOG_TAG, e.toString());
     * }
     * try {
     * if (responseDataContainer != null) {
     * if (responseDataContainer.getMsgCode().equals("631")) {
     * checkbalance.setText("Timeout");
     * } else if (responseDataContainer.getMsgCode().equals("274") || responseDataContainer.getMsgCode().equals("4")) {
     * checkbalance.setText("Rp "+ responseDataContainer.getAmount() +"");
     * }else{
     * Log.d("test", "not null");
     * String amount = responseDataContainer.getAmount();
     * checkbalance.setText("Rp "+ amount +"");
     * }
     * }
     * }catch (Exception e) {
     * Log.d(LOG_TAG, "ERROR: " + e.toString());
     * }
     * }else{
     * checkbalance.setText(getResources().getString(R.string.id_no_inetconnectivity));
     * }
     * }
     * }
     **/

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EXIT:
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case REQ_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        Bitmap selectedBitmap = extras.getParcelable("data");
                        photo.setImageBitmap(selectedBitmap);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        encodedImg = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        Log.d(LOG_TAG, "encoded image:" + encodedImg);
                        new PhotoUpload().execute();
                    }
                }
                break;
            case CAMERA_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    // get the Uri for the captured image
                    picUri = data.getData();
                    performCrop();
                }
                break;
            case CROP_PIC:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    // get the cropped bitmap
                    Bitmap thePic = extras.getParcelable("data");
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    thePic.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    encodedImg = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Log.d(LOG_TAG, "encoded image:" + encodedImg);
                    new PhotoUpload().execute();
                    photo.setImageBitmap(thePic);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), LandingScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void pickImage() {
        //Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        final CharSequence[] items = {getResources().getString(R.string.id_camera),getResources().getString(R.string.id_galeri), getResources().getString(R.string.id_batal)};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UserHomeActivity.this);
        builder.setTitle(getResources().getString(R.string.id_pilih_foto));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getResources().getString(R.string.id_camera))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_CAPTURE);
                } else if (items[item].equals(getResources().getString(R.string.id_galeri))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.putExtra("crop", "true");
                    intent.putExtra("return-data", true);
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
                } else if (items[item].equals(getResources().getString(R.string.id_batal))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    class PhotoUpload extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.UPDATE_PHOTO);
            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID, Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_KEY, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sourceMDN);
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, stMPIN);
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_IMG_STRING, encodedImg);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, UserHomeActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(UserHomeActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (response != null) {
                Log.e("-------", "=====" + response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseDataContainer = null;
                try {
                    responseDataContainer = obj.parse(response);
                    Log.e("responseContainer", "responseContainer" + responseDataContainer + "");

                } catch (Exception e) {
                    Log.e(LOG_TAG, e.toString());
                }
                try {
                    if (responseDataContainer != null) {
                        Log.d("test", "not null");
                        int msgCode = 0;

                        try {
                            msgCode = Integer.parseInt(responseDataContainer.getMsgCode());
                        } catch (Exception e) {
                            msgCode = 0;
                        }

                        if (msgCode == 631) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            alertbox = new AlertDialog.Builder(UserHomeActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setCancelable(false);
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(UserHomeActivity.this, SecondLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                            alertbox.show();
                            dialogBuilder.dismiss();
                        } else if (msgCode == 2143) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            alertbox = new AlertDialog.Builder(UserHomeActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                    initname.setVisibility(View.INVISIBLE);
                                }
                            });
                            alertbox.show();
                            dialogBuilder.dismiss();
                        } else {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            alertbox = new AlertDialog.Builder(UserHomeActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });
                            alertbox.show();
                            dialogBuilder.dismiss();
                        }
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), UserHomeActivity.this);
            }
        }
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private String initialName(String full_name){
        Pattern p = Pattern.compile("((^| )[A-Za-z])");
        Matcher m = p.matcher(full_name);
        String inititals="";
        while(m.find()){
            inititals+=m.group().trim();
        }
        System.out.println(inititals.toUpperCase());
        return inititals;
    }


}
