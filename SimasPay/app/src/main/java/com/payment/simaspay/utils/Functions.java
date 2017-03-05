package com.payment.simaspay.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.mfino.handset.security.CryptoService;

import simaspay.payment.com.simaspay.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by widy on 3/5/17.
 * 05
 */

public class Functions {
    SharedPreferences sharedPreferences;
    Context context;
    String rsaKey;

    public Functions(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
    }

    public String generateRSA(String pin){
        String module = sharedPreferences.getString("MODULE", "NONE");
        String exponent = sharedPreferences.getString("EXPONENT", "NONE");

        try {
            rsaKey = CryptoService.encryptWithPublicKey(module, exponent,
                    pin.getBytes());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return rsaKey;
    }

    public void initiatedToolbar(AppCompatActivity myActivityReference){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = myActivityReference.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(myActivityReference.getResources().getColor(R.color.dark_red));
        }
    }
}
