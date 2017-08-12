package simaspay.payment.com.simaspay;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
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
import com.payment.simaspay.PaymentPurchaseAccount.PaymentAndPurchaseAccountTypeActivity;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by widy on 1/13/17.
 * 13
 */

public class UserHomeActivity extends BaseActivity {
    private static final int EXIT = 20;
    private static final String LOG_TAG = "SimasPay";
    SharedPreferences sharedPreferences;
    private static String mdn;
    private static String label_home;
    ProgressBar progbar;
    String accountSelected = "", sourceMDN, stMPIN;
    TextView checkbalance, phone_lbl, name_lbl, home_lbl;
    LinearLayout daftaremoneylay;
    Functions func;
    public View swipe_balance;
    String fromDate, toDate;
    SimpleDateFormat sdf, sdf1;
    private ImageView photo;
    SharedPreferences languageSettings, settings;
    String selectedLanguage;
    final int CROP_PIC = 3;
    final int CAMERA_CAPTURE = 2;
    protected static final int REQ_PICK_IMAGE = 4;
    private static final int PICK_FROM_CAMERA = 5;
    private static final int CROP_FROM_CAMERA = 6;
    private static final int PICK_FROM_FILE = 7;
    Context context;
    private String encodedImg;
    private Uri picUri;
    private TextView initname;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    boolean GallaryPhotoSelected = false;
    public static String Finalmedia = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simaspay_home);
        func = new Functions(this);
        func.initiatedToolbar(this);
        cameraPermission();
        home_lbl = (TextView) findViewById(R.id.home_label);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString(Constants.PARAMETER_PHONENUMBER, "");
        String pin = settings.getString(Constants.PARAMETER_MPIN, "");
        stMPIN = func.generateRSA(pin);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");
        mdn = sharedPreferences.getString(Constants.PARAMETER_PHONENUMBER, "");
        String nama = sharedPreferences.getString("userName", "");
        String mpin = sharedPreferences.getString(Constants.PARAMETER_MPIN, "");
        label_home = sharedPreferences.getString(Constants.PARAMETER_TYPEUSER, "");
        TextView switch_account = (TextView) findViewById(R.id.switch_account);
        TextView logout = (TextView) findViewById(R.id.logout_btn);
        logout.setOnClickListener(view -> {
            Intent intent = new Intent(UserHomeActivity.this, SecondLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            sharedPreferences.edit().putString(Constants.PARAMETER_USERAPIKEY, "NONE").apply();
            startActivity(intent);
        });

        //Profpic / Profile Picture
        photo = (ImageView) findViewById(R.id.profile_pic);
        initname = (TextView) findViewById(R.id.initname);
        String encodedImgFromAPI = sharedPreferences.getString(Constants.PARAMETER_PROFPICSTRING, "");
        if (!encodedImgFromAPI.equals("")) {
            Bitmap profpic = decodeBase64(encodedImgFromAPI);
            if (profpic != null) {
                photo.setImageBitmap(profpic);
                initname.setVisibility(View.INVISIBLE);
            }
        } else {
            String fullname = sharedPreferences.getString("fullname", "");
            initname.setText(initialName(fullname));
            initname.setVisibility(View.VISIBLE);
            photo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.circle_bg));

        }
        photo.setOnClickListener(view -> pickImage());

        phone_lbl = (TextView) findViewById(R.id.mobilephone_lbl);
        ImageButton transfer = (ImageButton) findViewById(R.id.transfer_btn);
        ImageButton tariktunai = (ImageButton) findViewById(R.id.tariktunai_btn);
        switch (label_home) {
            case Constants.CONSTANT_BANK_USER: {
                home_lbl.setText(getResources().getString(R.string.bank));
                phone_lbl.setText(sharedPreferences.getString(Constants.PARAMETER_ACCOUNTNUMBER, ""));
                switch_account.setVisibility(View.GONE);
                tariktunai.setEnabled(false);
                tariktunai.setAlpha((float) 0.2);
                LinearLayout tariktunailay = (LinearLayout) findViewById(R.id.layout_tariktunai);
                tariktunailay.setVisibility(View.INVISIBLE);
                accountSelected = Constants.CONSTANT_BANK_USER;
                break;
            }
            case Constants.CONSTANT_EMONEYNONKYC_USER: {
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
                break;
            }
            case Constants.CONSTANT_EMONEYKYC_USER:
                home_lbl.setText(getResources().getString(R.string.id_emoney_plus));
                phone_lbl.setText(sharedPreferences.getString(Constants.PARAMETER_PHONENUMBER, ""));
                switch_account.setVisibility(View.GONE);
                accountSelected = Constants.CONSTANT_EMONEY_PLUS;
                break;
            case Constants.CONSTANT_BOTH_USER:
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
                break;
        }
        sharedPreferences.edit().putString(Constants.PARAMETER_USES_AS, accountSelected).apply();

        switch_account.setOnClickListener(view -> {
            //switch account
            Intent intent = new Intent(UserHomeActivity.this, NumberSwitchingActivity.class);
            startActivity(intent);
            finish();
        });

        ImageButton history_transaction = (ImageButton) findViewById(R.id.transaksi_btn);
        history_transaction.setOnClickListener(view -> {
            if (label_home.equals(Constants.CONSTANT_BANK_USER) || (sharedPreferences.getInt(Constants.PARAMETER_USERTYPE, -1) == Constants.CONSTANT_BANK_INT)) {
                Calendar calendar1 = Calendar.getInstance();
                sdf = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
                sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
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
        });

        ImageButton pembelian = (ImageButton) findViewById(R.id.pembelian_btn);
        pembelian.setOnClickListener(view -> {
            Intent intent = new Intent(UserHomeActivity.this, PaymentAndPurchaseAccountTypeActivity.class);
            intent.putExtra("accounttype", false);
            startActivity(intent);
        });

        ImageButton pembayaran = (ImageButton) findViewById(R.id.pembayaran_btn);
        pembayaran.setOnClickListener(view -> {
            Intent intent = new Intent(UserHomeActivity.this, PaymentAndPurchaseAccountTypeActivity.class);
            intent.putExtra("accounttype", true);
            startActivity(intent);
        });

        ImageButton pbq = (ImageButton) findViewById(R.id.pbq_btn);
        pbq.setOnClickListener(view -> {
            Intent intent = new Intent(UserHomeActivity.this, PayByQRActivity.class);
            intent.putExtra(PayByQRActivity.INTENT_EXTRA_MODULE, PayByQRSDK.MODULE_PAYMENT);
            startActivity(intent);
        });

        ImageButton promopbq = (ImageButton) findViewById(R.id.promo_pbq_btn);
        promopbq.setOnClickListener(view -> {
            Intent intent = new Intent(UserHomeActivity.this, PayByQRActivity.class);
            intent.putExtra(PayByQRActivity.INTENT_EXTRA_MODULE, PayByQRSDK.MODULE_LOYALTY);
            startActivity(intent);
        });

        daftaremoneylay = (LinearLayout) findViewById(R.id.daftar_emoney);
        ImageButton daftaremoney = (ImageButton) findViewById(R.id.daftar_emoney_btn);
        if (sharedPreferences.getString(Constants.PARAMETER_TYPEUSER, "").equals(Constants.CONSTANT_BANK_USER)) {
            daftaremoneylay.setVisibility(View.VISIBLE);
            daftaremoney.setOnClickListener(view -> {
                Intent intent = new Intent(UserHomeActivity.this, DaftarEmoneyActivity.class);
                startActivity(intent);
            });
        } else {
            daftaremoneylay.setVisibility(View.INVISIBLE);
        }

        daftaremoney = (ImageButton) findViewById(R.id.daftar_emoney_btn);
        daftaremoney.setOnClickListener(view -> {
            Intent intent = new Intent(UserHomeActivity.this, DaftarEmoneyActivity.class);
            startActivity(intent);
        });

        transfer.setOnClickListener(view -> {
            Intent intent = new Intent(UserHomeActivity.this, NewTransferHomeActivity.class);
            intent.putExtra("simaspayuser", false);
            intent.putExtra("agentornot", false);
            sharedPreferences.edit().putString(Constants.PARAMETER_USES_AS, accountSelected).apply();
            startActivityForResult(intent, 20);
        });

        tariktunai.setOnClickListener(view -> {
            Intent intent = new Intent(UserHomeActivity.this, NewWithdrawHomeActivity.class);
            sharedPreferences.edit().putString(Constants.PARAMETER_USES_AS, accountSelected).apply();
            startActivity(intent);
        });

        ImageButton gantimpin = (ImageButton) findViewById(R.id.gantimpin_btn);
        gantimpin.setOnClickListener(view -> {
            Intent intent = new Intent(UserHomeActivity.this, ChangePinActivity.class);
            intent.putExtra("simaspayuser", false);
            intent.putExtra("agentornot", false);
            sharedPreferences.edit().putString(Constants.PARAMETER_USES_AS, accountSelected).apply();
            startActivityForResult(intent, 20);
        });


        progbar = (ProgressBar) findViewById(R.id.progressbar);
        progbar.setVisibility(View.GONE);
        swipe_balance = findViewById(R.id.swipe_balance);
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
                        if (isNetworkAvailable()) {
                            checkbalance.setText("");
                            progbar.setVisibility(View.VISIBLE);
                            new CheckBalanceAsynTask().execute();
                        } else {
                            checkbalance.setText(getResources().getString(R.string.id_no_inetconnectivity));
                            progbar.setVisibility(View.GONE);
                            final Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                swipe_balance.setVisibility(View.VISIBLE);
                                checkbalance.setVisibility(View.GONE);
                                progbar.setVisibility(View.GONE);
                                swipe_balance.startAnimation(inFromRightAnimation());
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

    private class CheckBalanceAsynTask extends AsyncTask<Void, Void, Void> {
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
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
                    //Log.d(LOG_TAG, "emoney");
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_EMONEY);
                } else if (sharedPreferences.getInt(Constants.PARAMETER_AGENTTYPE, -1) == Constants.CONSTANT_BANK_INT) {
                    mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_BANK);
                    mapContainer.put(Constants.PARAMETER_SRC_POCKET_CODE, Constants.POCKET_CODE_BANK);
                }
            }
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
                //Log.e("-------", "---------" + response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseContainer = null;
                try {
                    responseContainer = obj.parse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (responseContainer != null) {
                        msgCode = Integer.parseInt(responseContainer
                                .getMsgCode());
                    }
                    if (msgCode == 631) {
                        checkbalance.setText("");
                        func.errorTimeoutResponseConfirmation(responseContainer.getMsg());
                    } else if (msgCode == 571) {
                        checkbalance.setText("");
                        Utility.networkDisplayDialog(responseContainer.getMsg(), UserHomeActivity.this);
                    } else if (msgCode == 274 || msgCode == 4) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            checkbalance.setText(Html.fromHtml("<small>Rp </small><big>" + responseContainer.getAmount() + "</big>", Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            checkbalance.setText(Html.fromHtml("<small>Rp </small><big>" + responseContainer.getAmount() + "</big>"));
                        }
                    }
                } catch (Exception e) {
                   // Log.d(LOG_TAG, e.toString());
                }
            } else {
                checkbalance.setText("");
                Utility.networkDisplayDialog(getResources().getString(R.string.id_no_inetconnectivity), UserHomeActivity.this);
            }
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                swipe_balance.setVisibility(View.VISIBLE);
                checkbalance.setVisibility(View.GONE);
                progbar.setVisibility(View.GONE);
                swipe_balance.startAnimation(inFromRightAnimation());
            }, 5000);
        }
    }

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
            case PICK_FROM_CAMERA:
                //Log.i(LOG_TAG, "Inside PICK_FROM_CAMERA");
                String path = picUri.getPath();
                //Log.i(LOG_TAG, "After capture path " + path);
                doCrop();

                break;

            case PICK_FROM_FILE:
                picUri = data.getData();
                String path_photo = picUri.getPath();
                //Log.i(LOG_TAG,"picUri " + picUri);
                //Log.i(LOG_TAG,"After Crop mImageCaptureUri, path photo " + path_photo);
                GallaryPhotoSelected = true;
                doCrop();

                break;
            case REQ_PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    //Log.d(LOG_TAG, "processing image...");
                    picUri = data.getData();
                    performCrop();
                }
                break;
            case CAMERA_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    picUri = data.getData();
                    performCrop();
                }
                break;
            case CROP_PIC:
                if (resultCode == Activity.RESULT_OK) {
                    // get the cropped bitmap
                    Bundle extras = data.getExtras();
                    Bitmap thePic = extras.getParcelable("data");
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    if(thePic!=null){
                        thePic.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    }
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    encodedImg = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    //Log.d(LOG_TAG, "encoded image:" + encodedImg);
                    sharedPreferences.edit().putString(Constants.PARAMETER_PROFPICSTRING,encodedImg).apply();
                    new PhotoUpload().execute();
                    photo.setImageBitmap(thePic);
                }
                break;
            case CROP_FROM_CAMERA:
                if(data.getExtras()!=null){
                    Bundle extras = data.getExtras();
                    //Log.d(LOG_TAG, "extras: "+extras);
                    String selectedImagePath = picUri.getPath();
                    //Log.d(LOG_TAG, "CROP_FROM_CAMERA selectedImagePath: "+selectedImagePath);
                    //Log.i(LOG_TAG, "After Crop selectedImagePath " + selectedImagePath);
                    if (GallaryPhotoSelected) {
                        //Log.i(LOG_TAG, "Absolute Path " + selectedImagePath);
                        GallaryPhotoSelected = true;
                    }

                    Finalmedia = selectedImagePath;

                    if (extras != null) {
                        //Log.i(LOG_TAG, "Inside Extra " + selectedImagePath);
                        Bitmap thePic = decodeFile(selectedImagePath);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        if (thePic != null)
                            thePic.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        encodedImg = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        //Log.d(LOG_TAG, "encoded image:" + encodedImg);
                        sharedPreferences.edit().putString(Constants.PARAMETER_PROFPICSTRING,encodedImg).apply();
                        new PhotoUpload().execute();
                        photo.setImageBitmap(thePic);
                    }
                }else{
                    //Log.d(LOG_TAG, "extras null!");
                }
                break;
            default:
                break;
        }
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

    private class PhotoUpload extends AsyncTask<Void, Void, Void> {
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
            Drawable drawable = new ProgressBar(UserHomeActivity.this).getIndeterminateDrawable().mutate();
            drawable.setColorFilter(ContextCompat.getColor(UserHomeActivity.this, R.color.red_sinarmas),
                    PorterDuff.Mode.SRC_IN);
            progressDialog.setIndeterminateDrawable(drawable);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (response != null) {
                //Log.e("-------", "=====" + response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseDataContainer = null;
                try {
                    responseDataContainer = obj.parse(response);
                    //Log.e("responseContainer", "responseContainer" + responseDataContainer + "");

                } catch (Exception e) {
                    //Log.e(LOG_TAG, e.toString());
                }
                try {
                    if (responseDataContainer != null) {
                        //Log.d("test", "not null");
                        int msgCode;

                        try {
                            msgCode = Integer.parseInt(responseDataContainer.getMsgCode());
                        } catch (Exception e) {
                            msgCode = 0;
                        }

                        AlertDialog.Builder alertbox;
                        if (msgCode == 631) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            func.errorTimeoutResponseConfirmation(responseDataContainer.getMsg());
                        } else if (msgCode == 2143) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            alertbox = new AlertDialog.Builder(UserHomeActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", (arg0, arg1) -> {
                                arg0.dismiss();
                                initname.setVisibility(View.INVISIBLE);
                            });
                            alertbox.show();
                        } else {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            alertbox = new AlertDialog.Builder(UserHomeActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", (arg0, arg1) -> arg0.dismiss());
                            alertbox.show();
                        }
                    }
                } catch (Exception e) {
                    //Log.e(LOG_TAG, "error: " + e.toString());
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

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private String initialName(String full_name) {
        Pattern p = Pattern.compile("((^| )[A-Za-z])");
        Matcher m = p.matcher(full_name);
        String inititals = "";
        while (m.find()) {
            inititals += m.group().trim();
        }
        System.out.println(inititals.toUpperCase());
        return inititals;
    }

    private void cameraPermission() {
        if (ContextCompat.checkSelfPermission(UserHomeActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(UserHomeActivity.this,
                    Manifest.permission.CAMERA)) {
                //Log.d(LOG_TAG, "check camera permission");
            } else {
                ActivityCompat.requestPermissions(UserHomeActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Log.d(LOG_TAG, "permission granted");
                } else {
                    //Log.d(LOG_TAG, "permission denied");
                }
            }
        }
    }

    public static Bitmap decodeFile(String path) {
        int orientation;
        try {
            if (path == null) {
                return null;
            }
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            int scale = 1;
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bm = BitmapFactory.decodeFile(path, o2);
            Bitmap bitmap = bm;

            ExifInterface exif = new ExifInterface(path);

            orientation = exif
                    .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            //Log.e("ExifInteface .........", "rotation =" + orientation);
            //Log.e("orientation", "" + orientation);
            Matrix m = new Matrix();

            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                m.postRotate(180);
                //Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90);
                //Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270);
                //Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
                return bitmap;
            }
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }


    private void doCrop() {
        final ArrayList<Functions.CropOption> cropOptions = new ArrayList<Functions.CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(
                intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "Can not find image crop app",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(picUri);
            intent.setClassName("com.android.camera",
                    "com.android.camera.CropImage");
            intent.putExtra("outputX", 100);
            intent.putExtra("outputY", 100);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));
                startActivityForResult(i, PICK_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final Functions.CropOption co = new Functions.CropOption();

                    co.title = getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));

                    cropOptions.add(co);
                }

                Functions.CropOptionAdapter adapter = new Functions.CropOptionAdapter(
                        getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
                builder.setTitle(getResources().getString(R.string.id_crop_foto));
                builder.setAdapter(adapter,
                        (dialog, item) -> startActivityForResult(
                                cropOptions.get(item).appIntent,
                                CROP_FROM_CAMERA));

                builder.setOnCancelListener(dialog -> {

                    if (picUri != null) {
                        getContentResolver().delete(picUri, null,
                                null);
                        picUri = null;
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private void pickImage() {
        List<Intent> targets = new ArrayList<Intent>();
        Intent intent = new Intent(Intent.ACTION_PICK,
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        List<ResolveInfo> candidates = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo candidate : candidates) {
            String packageName = candidate.activityInfo.packageName;
            if (!packageName.equals("com.google.android.apps.photos") && !packageName.equals("com.google.android.apps.plus") && !packageName.equals("com.android.documentsui")) {
                Intent iWantThis = new Intent();
                iWantThis.setType("image/*");
                iWantThis.setAction(Intent.ACTION_GET_CONTENT);
                iWantThis.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                iWantThis.setPackage(packageName);
                targets.add(iWantThis);
            }
        }
        //intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(targets.remove(0), getResources().getString(R.string.id_pilih_foto)), REQ_PICK_IMAGE);
        //}
        /*
        final String[] items = new String[] { getResources().getString(R.string.id_galeri) };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);

        builder.setTitle(getResources().getString(R.string.id_pilih_foto));
        builder.setAdapter(adapter, (dialog, item) -> {
            if (item == 0) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_CAPTURE);
            } else { // pick from file
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.id_pilih_foto)), REQ_PICK_IMAGE);
            //}
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
        */
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

}
