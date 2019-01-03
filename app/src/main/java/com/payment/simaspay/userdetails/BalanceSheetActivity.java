package com.payment.simaspay.userdetails;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.R;
import com.payment.simaspay.agentdetails.ChangePinActivity;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Nagendra P on 12/29/2015.
 */
public class BalanceSheetActivity extends Activity {

    LinearLayout layout1, layout2, layout3, logOut, numberSwitching, white_logOut, white_numberswitching;
    TextView textView1, textView2, textView3, simas, name, number, ganti_akun, logout_text, white_name, white_number, white_ganti_akun, white_logout_text;
    ImageView imageView1, imageView2, imageView3, menu_back;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    TextView agent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        sharedPreferences = getSharedPreferences("SimasPay", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        simas = (TextView) findViewById(R.id.simas);


        String s = "simaspay";
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1f), 0, 5, 0);
        ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);

        simas.setText(ss1);
        layout1 = (LinearLayout) findViewById(R.id.common_layout_1);
        layout2 = (LinearLayout) findViewById(R.id.common_layout_2);
        layout3 = (LinearLayout) findViewById(R.id.common_layout_3);

        imageView1 = (ImageView) findViewById(R.id.common_image_1);
        imageView2 = (ImageView) findViewById(R.id.common_image_2);
        imageView3 = (ImageView) findViewById(R.id.common_image_3);

        menu_back = (ImageView) findViewById(R.id.menu_back);

        textView1 = (TextView) findViewById(R.id.common_text_1);
        textView2 = (TextView) findViewById(R.id.common_text_2);
        textView3 = (TextView) findViewById(R.id.common_text_3);

        agent=(TextView)findViewById(R.id.agent);

        name = (TextView) findViewById(R.id.com_name);
        number = (TextView) findViewById(R.id.com_number);
        ganti_akun = (TextView) findViewById(R.id.com_ganti_akun_text);
        logout_text = (TextView) findViewById(R.id.com_logout_text);

        white_name = (TextView) findViewById(R.id.userName);
        white_number = (TextView) findViewById(R.id.userNumber);
        white_ganti_akun = (TextView) findViewById(R.id.ganti_akun);
        white_logout_text = (TextView) findViewById(R.id.laoout_text);

        textView1.setTypeface(Utility.LightTextFormat(BalanceSheetActivity.this));
        textView2.setTypeface(Utility.LightTextFormat(BalanceSheetActivity.this));
        textView3.setTypeface(Utility.LightTextFormat(BalanceSheetActivity.this));

        white_name.setTypeface(Utility.LightTextFormat(BalanceSheetActivity.this));
        white_number.setTypeface(Utility.OpemSans_Light(BalanceSheetActivity.this));
        white_logout_text.setTypeface(Utility.LightTextFormat(BalanceSheetActivity.this));
        white_ganti_akun.setTypeface(Utility.LightTextFormat(BalanceSheetActivity.this));

        name.setTypeface(Utility.LightTextFormat(BalanceSheetActivity.this));
        number.setTypeface(Utility.OpemSans_Light(BalanceSheetActivity.this));
        logout_text.setTypeface(Utility.LightTextFormat(BalanceSheetActivity.this));
        ganti_akun.setTypeface(Utility.LightTextFormat(BalanceSheetActivity.this));

//        name.setText(sharedPreferences.getString("userName", ""));

//        white_name.setText(sharedPreferences.getString("userName", ""));
        String user;
        if (sharedPreferences.getString("userName","").length() >= 15) {
            user = sharedPreferences.getString("userName","").substring(0, 15)+ "...";
        } else {
            user = sharedPreferences.getString("userName","");
        }
        white_name.setText(user);
        name.setText(user);


        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.btn_infosaldo));
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.btn_mutasi));
        imageView3.setImageDrawable(getResources().getDrawable(R.drawable.btn_mpin));

        textView1.setText("Info Saldo");
        textView3.setText("Ganti mPIN");

        menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_FIRST_USER, intent);
                finish();
            }
        });

        if (sharedPreferences.getInt("userType", -1) == 2){
            if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                agent.setVisibility(View.VISIBLE);
                textView2.setText("Transaksi");
                white_number.setText(sharedPreferences.getString("mobileNumber", ""));
                number.setText(sharedPreferences.getString("mobileNumber", ""));
            }else{
                textView2.setText("Transaksi");
                agent.setVisibility(View.GONE);
                white_number.setText(sharedPreferences.getString("accountnumber", ""));
                number.setText(sharedPreferences.getString("accountnumber", ""));
            }
        }else{
            if(sharedPreferences.getInt("userType", -1) == 1){
                textView2.setText("Transaksi");
                white_number.setText(sharedPreferences.getString("mobileNumber", ""));
                number.setText(sharedPreferences.getString("mobileNumber", ""));
            }else{
                textView2.setText("Transaksi");
                white_number.setText(sharedPreferences.getString("accountnumber", ""));
                number.setText(sharedPreferences.getString("accountnumber", ""));
            }
            agent.setVisibility(View.GONE);
        }


        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CheckBalanceAsynTask().execute();
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sharedPreferences.getInt("userType", -1) == 1) {
                    Intent intent = new Intent(BalanceSheetActivity.this, Trans_DataSelectionActivity.class);
                    startActivity(intent);
                } else if (sharedPreferences.getInt("userType", -1) == 0) {
                    Intent intent = new Intent(BalanceSheetActivity.this, TransactionsListActivity.class);
                    startActivity(intent);
                } else if (sharedPreferences.getInt("userType", -1) == 2) {
                    if (sharedPreferences.getInt("AgentUsing", -1) == 1) {
                        Intent intent = new Intent(BalanceSheetActivity.this, Trans_DataSelectionActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(BalanceSheetActivity.this, TransactionsListActivity.class);
                        startActivity(intent);
                    }
                }

            }
        });

        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BalanceSheetActivity.this, ChangePinActivity.class);
                startActivity(intent);
            }
        });
        logOut = (LinearLayout) findViewById(R.id.logOut);
        numberSwitching = (LinearLayout) findViewById(R.id.Number_switching);

        white_logOut = (LinearLayout) findViewById(R.id.whitelogOut);
        white_numberswitching = (LinearLayout) findViewById(R.id.gantiAkun_layout);


        if (getIntent().getExtras().getBoolean("simaspayuser")) {
            numberSwitching.setVisibility(View.VISIBLE);
        } else {
            numberSwitching.setVisibility(View.GONE);
        }
        if (getIntent().getExtras().getBoolean("red_or_white")) {
            findViewById(R.id.whitecolor_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.redcolor_layout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.whitecolor_layout).setVisibility(View.GONE);
            findViewById(R.id.redcolor_layout).setVisibility(View.VISIBLE);
        }
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   editor.putBoolean("Logout",true).commit();
                finish();*/
                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        white_numberswitching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        white_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        numberSwitching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = getIntent();
            setResult(Activity.RESULT_FIRST_USER, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    ProgressDialog progressDialog;
    int msgCode;

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
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, BalanceSheetActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(BalanceSheetActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(BalanceSheetActivity.this, SessionTimeOutActivity.class);
                    startActivityForResult(intent, 40);
                } else if (msgCode == 274 || msgCode == 4) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(BalanceSheetActivity.this, CurrentSessionActivity.class);
                    intent.putExtra("amount", responseContainer.getAmount());
                    intent.putExtra("Date",responseContainer.getDate());
                    intent.putExtra("Time",responseContainer.getTime());
                    startActivity(intent);

                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (responseContainer.getMsg() == null) {
                        Utility.networkDisplayDialog(
                                sharedPreferences.getString(
                                        "ErrorMessage",
                                        getResources()
                                                .getString(
                                                        R.string.server_error_message)),
                                BalanceSheetActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), BalanceSheetActivity.this);
                    }
                }
            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), BalanceSheetActivity.this);
            }
        }
    }
}
