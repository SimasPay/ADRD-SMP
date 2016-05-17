package com.payment.simaspay.rec;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class IncomingSms extends BroadcastReceiver {

//	final SmsManager sms = SmsManager.getDefault();

	public void onReceive(Context context, Intent intent) {

		final Bundle bundle = intent.getExtras();

		try {

			if (bundle != null) {

				final Object[] pdusObj = (Object[]) bundle.get("pdus");

				for (int i = 0; i < pdusObj.length; i++) {

					SmsMessage currentMessage = SmsMessage
							.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage
							.getDisplayOriginatingAddress();

					String senderNum = phoneNumber;
					String message = currentMessage.getDisplayMessageBody();


					Intent intentSendData = new Intent();
					intentSendData.setAction("com.msg.simaspay");
					intentSendData.putExtra("message", message);
					Log.e("=-------","----------"+message);
					context.sendBroadcast(intentSendData);

				}
			}

		} catch (Exception e) {

		}
	}
}
