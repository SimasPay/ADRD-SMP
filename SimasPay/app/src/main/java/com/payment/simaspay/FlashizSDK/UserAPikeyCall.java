package com.payment.simaspay.FlashizSDK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 3/30/2016.
 */
public class UserAPikeyCall {

    static Context context;
    static SharedPreferences sharedPreferences;

    public static  void UserAPikeyCall(Context context1) {
        context=context1;
        sharedPreferences=context1.getSharedPreferences(context1.getResources().getString(R.string.shared_prefvalue),context1.MODE_PRIVATE);
        new UserApiKeyAsynTask().execute();
    }

    static WebServiceHttp webServiceHttp;
    static String UserApikeyresponse, userApiKey;
    static int msgCode;

    static class UserApiKeyAsynTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_USER_APIKEY);
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN,
                    sharedPreferences.getString("mobileNumber", ""));

            webServiceHttp = new WebServiceHttp(mapContainer, context);

            UserApikeyresponse = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("========", "0========" + UserApikeyresponse);
            if (UserApikeyresponse != null) {
                XMLParser obj = new XMLParser();

                EncryptedResponseDataContainer responseContainer = null;
                try {
                    responseContainer = obj.parse(UserApikeyresponse);
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());

                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 2103) {
                    userApiKey = responseContainer.getUserApiKey();
                    sharedPreferences.edit()
                            .putString("userApiKey",
                                    responseContainer.getUserApiKey())
                            .commit();
                    Intent intent=new Intent(context, PayByQRActivity.class);
                    (context).startActivity(intent);

                } else {
                    sharedPreferences.edit()
                            .putString("userApiKey",
                                    "")
                            .commit();
                    if (responseContainer.getMsg() == null) {
                        Utility.networkDisplayDialog(
                                sharedPreferences.getString(
                                        "ErrorMessage",
                                        context.getResources()
                                                .getString(
                                                        R.string.server_error_message)),
                                context);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(),context);
                    }
                }

            } else {
                Utility.networkDisplayDialog(
                        sharedPreferences.getString(
                                "ErrorMessage",
                                context.getResources()
                                        .getString(
                                                R.string.server_error_message)),
                        context);
            }
        }
    }
}
