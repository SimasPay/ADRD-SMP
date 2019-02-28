package com.payment.simaspay.receivers;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class IncomingSMS extends BroadcastReceiver {

	private static final String LOG_TAG = "SimasPay";
	public static AutoReadSMSListener listener;

	public static void setListener(AutoReadSMSListener listener) {
    	IncomingSMS.listener = listener;
    }

	@SuppressLint("UnsafeProtectedBroadcastReceiver")
	@Override
	public void onReceive(Context context, Intent intent) {
		//Log.d(LOG_TAG, "onReceive");
		SharedPreferences settings = context.getSharedPreferences(LOG_TAG,	0);
		//String sctl = settings.getString("Sctl", "");
		final Bundle bundle = intent.getExtras();
		try {
			if (bundle != null) {
				final Object[] pdusObj = (Object[]) bundle.get("pdus");
				Object [] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] messages = new SmsMessage[0];
				if (pdus != null) {
					messages = new SmsMessage[pdus.length];
				}
				if (pdusObj != null) {
					for (int i = 0; i < pdusObj.length; i++) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
							String format = bundle.getString("format");
							messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i], format);
						}
						else {
							messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
						}
						String message = messages[i].getMessageBody();
						//Log.d(LOG_TAG, "msg : " + message);
						try {
							//Log.d(LOG_TAG, "msg to lowercase:"+message.toLowerCase(Locale.getDefault()));
							if (message.toLowerCase(Locale.getDefault()).contains("kode otp simaspay anda ")
									|| message.toLowerCase(Locale.getDefault()).contains("your simaspay code is ")) {
								settings.edit().putBoolean("isAutoSubmit", true).apply();
								String otpValue = message.substring(message.substring(0, message.indexOf(".")).lastIndexOf(" "), message.indexOf(".")).trim();
								//Log.d(LOG_TAG, "OPT code : " + otpValue + "");
								if (listener != null) {
									listener.onReadSMS(otpValue);
								}

							}else{
								settings.edit().putBoolean("isAutoSubmit", false).apply();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}

		} catch (Exception e) {
			//Log.d(LOG_TAG, "Error: " + e);
		}
	}
	
	public interface AutoReadSMSListener {
        void onReadSMS(String otp);
    }


}