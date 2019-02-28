package com.payment.simaspay.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.payment.simaspay.R;
import com.payment.simaspay.UserActivation.ActivationPage_2_Activity;
import com.payment.simaspay.contactus.ContactUs_Activity;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.utils.Functions;

import static com.payment.simaspay.services.Constants.LOG_TAG;

/**
 * Created by widy on 1/9/17.
 * 09
 */

public class LandingScreenActivity extends AppCompatActivity {
    Context context;
    public SharedPreferences settings;
    Functions functions;
    TextView contact_us;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int MY_PERMISSIONS_ALL = 10;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 11;
    private static final int READ_PHONE_STATE_REQUEST = 109;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingpage);
        context = LandingScreenActivity.this;
        functions = new Functions(this);
        functions.initiatedToolbar(this);

        AllPermissions();

        settings = getSharedPreferences(LOG_TAG, 0);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        String firsttime = settings.getString("firsttime", "yes");

        Button login= findViewById(R.id.login);
        login.setOnClickListener(view -> {
            finish();
            if (firsttime != null) {
                if(firsttime.equals("yes")){
                    Intent intent = new Intent(LandingScreenActivity.this, InputNumberScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    String mdn = settings.getString("mobileNumber", "");
                    if (mdn != null) {
                        if(!mdn.equals("")){
                            Intent intent = new Intent(LandingScreenActivity.this, SecondLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(LandingScreenActivity.this, InputNumberScreenActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                }
            }

        });
        Button activation= findViewById(R.id.activation);
        activation.setOnClickListener(view -> {
            Intent intent = new Intent(LandingScreenActivity.this, ActivationPage_2_Activity.class);
            startActivityForResult(intent, 10);
        });

        contact_us= findViewById(R.id.contact_us);
        contact_us.setText(Html.fromHtml("<u>Hubungi Kami</u>"));
        contact_us.setOnClickListener(view -> {
            Intent intent = new Intent(LandingScreenActivity.this, ContactUs_Activity.class);
            startActivity(intent);
        });


    }

    private void AllPermissions(){
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion > Build.VERSION_CODES.LOLLIPOP) {
            if ((checkCallingOrSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) && checkCallingOrSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE},
                            MY_PERMISSIONS_ALL);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_PHONE_STATE_REQUEST:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "phone state permission granted");
                }
                break;
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "camera permission granted");
                }
                break;
            case MY_PERMISSIONS_ALL:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(LOG_TAG, "------all granted()");
                }
                break;
            case READ_CONTACTS_PERMISSIONS_REQUEST:
                if (grantResults.length == 1 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(LOG_TAG, "------read contact granted()");
                }
                break;
        }
    }
}
