package com.payment.simaspay.services;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.payment.simaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

import com.payment.simaspay.R;

/**
 * Created by Nagendra P on 4/19/2016.
 */
public class ChangePinTimerCount {
    Context context;
    TextView textView1;

    SharedPreferences sharedPreferences;

    CountDownTimer cTimer = null;
    String otpValue = "", sctl;
    Button button;

    ProgressBar progressBar;


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String body = intent.getExtras().getString("message");

                if (body.contains("Kode OTP Simaspay anda")) {

                    otpValue = body
                            .substring(
                                    body.indexOf("Kode OTP Simaspay anda : ")
                                            + new String(
                                            "Kode OTP Simaspay anda : ")
                                            .length(),
                                    body.indexOf(". ")).trim();
                    button.setEnabled(true);
                    editText.setText(otpValue);
                    cancelTimer();
                    progressBar.setVisibility(View.GONE);
                    textView1.setVisibility(View.GONE);
                    button.setTextColor(context.getResources().getColor(R.color.bg_color_h));

                } else if (body.contains("Your Simaspay code is ")) {
                    otpValue = body
                            .substring(
                                    body.indexOf("Your Simaspay code is ")
                                            + new String(
                                            "Your Simaspay code is ")
                                            .length(),
                                    body.indexOf(". ")).trim();

                    button.setEnabled(true);
                    Log.e("=========", "------after Button------");
                    editText.setText(otpValue);
                    Log.e("=========", "------after set OtpValue------");
                    cancelTimer();
                    Log.e("=========", "------after Cancel OtpValue------");
                    progressBar.setVisibility(View.GONE);
                    Log.e("=========", "------after set ProgressBar------");
                    button.setTextColor(context.getResources().getColor(R.color.bg_color_h));
                    textView1.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public ChangePinTimerCount(Context mContext, String string) {
        context = mContext;
        context.registerReceiver(broadcastReceiver, new IntentFilter("com.msg.simaspay"));
        sctl = string;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.shared_prefvalue), context.MODE_PRIVATE);
    }

    public void startTimer() {
        if (cTimer == null) {
            Log.e("=========", "------Nagendra in TImer Start------");
            cTimer = new CountDownTimer(60000, 1000) {
                public void onTick(long millisUntilFinished) {

                    int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)));
                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                    int seconds = (int) ((millisUntilFinished / 1000) % 60);
                    if (seconds < 10) {
                        textView1.setText(minutes + ":" + "0" + seconds);
                    } else {
                        textView1.setText(minutes + ":" + seconds);

                    }
                    progressBar.setVisibility(View.VISIBLE);
                    button.setTextColor(context.getResources().getColor(R.color.ok_disablecolor));
                    textView1.setTextColor(context.getResources().getColor(R.color.timer_color));
                }

                public void onFinish() {
                    textView1.setText("Kirim Ulang");
                    textView1.setTextColor(context.getResources().getColor(R.color.bg_color_h));
                    editText.setEnabled(true);
                    button.setEnabled(false);
                    progressBar.setVisibility(View.GONE);

                }
            };
            cTimer.start();
        }
    }

    public void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    Dialog dialogCustomWish;
    EditText editText;
    boolean value = false;

    public void SMSAlert(boolean string) {

        Log.e("=========", "------------");
        dialogCustomWish = new Dialog(context);
        dialogCustomWish.setCancelable(false);

        dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        View view = LayoutInflater.from(context).inflate(R.layout.sms_alert, null);
        dialogCustomWish.setContentView(R.layout.sms_alert);

        button = (Button) dialogCustomWish.findViewById(R.id.ok);
        Button button1 = (Button) dialogCustomWish.findViewById(R.id.Cancel);
        final TextView textView = (TextView) dialogCustomWish.findViewById(R.id.number);
        TextView textView_1 = (TextView) dialogCustomWish.findViewById(R.id.number_1);
        button.setTypeface(Utility.RegularTextFormat(context));
        button1.setTypeface(Utility.RegularTextFormat(context));
        textView.setTypeface(Utility.RegularTextFormat(context));

        textView1 = (TextView) dialogCustomWish.findViewById(R.id.timer);


        progressBar = (ProgressBar) dialogCustomWish.findViewById(R.id.progressbar);
        textView_1.setText("Kode OTP dan link telah dikirimkan ke nomor " + sharedPreferences.getString("mobileNumber", "") + ". Masukkan kode tersebut atau akses link yang tersedia.");


        editText = (EditText) dialogCustomWish.findViewById(R.id.otpCode);
        editText.setEnabled(false);
        editText.setHint("6 digit kode OTP");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    button.setEnabled(false);
                    progressBar.setVisibility(View.GONE);
                    button.setTextColor(context.getResources().getColor(R.color.ok_disablecolor));
                } else {
                    button.setEnabled(true);
                    button.setTextColor(context.getResources().getColor(R.color.bg_color_h));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textView_1.setTypeface(Utility.Robot_Regular(context));


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogCustomWish.dismiss();
                value = false;
                Intent i = new Intent("com.send");
                i.putExtra("value", "0");
                try {
                    context.unregisterReceiver(broadcastReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                context.sendBroadcast(i);

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
                dialogCustomWish.dismiss();
                if (button.getText().toString().equalsIgnoreCase("ok")) {
                    progressBar.setVisibility(View.GONE);
                    Intent i = new Intent("com.send");
                    i.putExtra("value", "1");
                    try {
                        context.unregisterReceiver(broadcastReceiver);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i.putExtra("otpValue", editText.getText().toString());
                    context.sendBroadcast(i);
                }

            }
        });

        if (otpValue.equalsIgnoreCase("")) {
            if (!string) {
                startTimer();
                editText.setText("");
                button.setEnabled(false);
            } else {
                textView1.setText("Kirim Ulang");
                textView1.setTextColor(context.getResources().getColor(R.color.bg_color_h));
                editText.setEnabled(true);
                button.setEnabled(false);
                progressBar.setVisibility(View.GONE);
            }

        } else {
            button.setEnabled(true);
            editText.setText(otpValue);
            cancelTimer();
            progressBar.setVisibility(View.GONE);
            button.setTextColor(context.getResources().getColor(R.color.bg_color_h));
            textView1.setVisibility(View.GONE);
        }


        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView1.getText().toString().equalsIgnoreCase("Kirim Ulang")) {
                    cTimer = null;
                    new MFAResendOTPAsyn().execute();
                }
            }
        });

        if (string) {
            if (otpValue.equalsIgnoreCase("")) {
                cTimer.cancel();
                cTimer = null;
                startTimer();
            } else {
                button.setEnabled(true);
                editText.setText(otpValue);
                cancelTimer();
                progressBar.setVisibility(View.GONE);
                button.setTextColor(context.getResources().getColor(R.color.bg_color_h));
                textView1.setVisibility(View.GONE);
            }
            if (!dialogCustomWish.isShowing()) {
                dialogCustomWish.show();
            }
        } else {

        }

    }


    String response;
    int msgCode;

    class MFAResendOTPAsyn extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_RESENDMFAOTP);
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, sharedPreferences.getString("password", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_WALLET);
            mapContainer.put(Constants.PARAMETER_SCTLID, sctl);

            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, context);

            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
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

                if (msgCode == 2171) {
                    cancelTimer();
                    startTimer();
                    progressBar.setVisibility(View.GONE);
                    editText.setEnabled(false);
                    button.setEnabled(false);
                } else if (msgCode == 2172) {
                    dialogCustomWish.dismiss();
                    try {
                        context.unregisterReceiver(broadcastReceiver);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent("com.send");
                    i.putExtra("value", "2");
                    i.putExtra("otpValue", responseContainer.getMsg());
                    context.sendBroadcast(i);
                } else if (msgCode == 2173) {
                    dialogCustomWish.dismiss();
                    try {
                        context.unregisterReceiver(broadcastReceiver);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent("com.send");
                    i.putExtra("value", "3");
                    i.putExtra("otpValue", responseContainer.getMsg());
                    context.sendBroadcast(i);
                }
            } else {
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        context.getResources().getString(
                                R.string.bahasa_serverNotRespond)), context);
            }
        }
    }


}
