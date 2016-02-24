package simaspay.payment.com.simaspay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.payment.simaspay.agentdetails.AgentSimaspayUserActivity;
import com.payment.simaspay.agentdetails.NumberSwitchingActivity;
import com.payment.simaspay.contactus.ContactUs_Activity;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.user_registration.ActivationPage_1_Activity;
import com.payment.simaspay.userdetails.SimaspayUserActivity;

/**
 * Created by Nagendra P on 12/14/2015.
 */
public class LoginScreenActivity extends Activity {

    EditText e_Mdn, e_mPin;
    Button login, register;

    String countryCode = "62", mobileNumber;

    TextView contact_us, simas, atau;

    Context context;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
               /* Intent intent = new Intent(LoginScreenActivity.this, SimaspayUserActivity.class);
                startActivity(intent);
                finish();*/
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        context = LoginScreenActivity.this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        e_Mdn = (EditText) findViewById(R.id.hand_phno);
        e_mPin = (EditText) findViewById(R.id.mpin);

        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.activation);
        atau = (TextView) findViewById(R.id.or);

        simas = (TextView) findViewById(R.id.simas);
//        simas.setTextSize(getResources().getDimensionPixelSize(R.dimen.txt));

        login.setTypeface(Utility.Robot_Regular(LoginScreenActivity.this));
        register.setTypeface(Utility.Robot_Regular(LoginScreenActivity.this));
        atau.setTypeface(Utility.Robot_Light(LoginScreenActivity.this));


        contact_us = (TextView) findViewById(R.id.contact_us);
        contact_us.setTypeface(Utility.Robot_Light(LoginScreenActivity.this));
        SpannableString content = new SpannableString("Hubungi Kami");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        contact_us.setText(content);
        e_Mdn.setTypeface(Utility.Robot_Light(LoginScreenActivity.this));
        e_mPin.setTypeface(Utility.Robot_Light(LoginScreenActivity.this));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginScreenActivity.this, ActivationPage_1_Activity.class);
                startActivityForResult(intent, 20);
            }
        });

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginScreenActivity.this, ContactUs_Activity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  if (e_Mdn.getText().toString().startsWith("0")) {
                    mobileNumber = e_Mdn.getText().toString()
                            .substring(1);
                } else {
                    mobileNumber = e_Mdn.getText().toString();
                }
                boolean networkCheck = Utility.isConnectingToInternet(context);
               *//* if (!networkCheck) {
                 *//**//*   Utility.networkDisplayDialog(
                            getResources().getString(
                                    R.string.bahasa_serverNotRespond), context);*//**//*

                } else*//*
                if (e_Mdn.getText().toString().equals("")) {
                    displayDialog("Masukkan Nomor Handphone", LoginScreenActivity.this);
                } else if (e_Mdn.getText().toString().replace(" ", "")
                        .length() < 7) {
                    displayDialog("Nomor Handphone harus lebih dari 6 angka",
                            LoginScreenActivity.this);
                } else if (e_mPin.getText().toString().equals("")) {
                    *//*
                     * pin.setError("Masukkan Pin Anda"); pin.requestFocus();
					 *//*
                    displayDialog("Masukkan Pin Anda", LoginScreenActivity.this);
                } else if (e_mPin.getText().length() < Constants.getPinLength()) {
                    displayDialog(Constants.getInCorrectPinLenth(), LoginScreenActivity.this);
                } else if (countryCode == null) {
                    Toast.makeText(LoginScreenActivity.this, "Masukkan Country Code ANda",
                            Toast.LENGTH_LONG).show();
                } else if (countryCode.equalsIgnoreCase("+62")) {
                    String mobile1 = countryCode + mobileNumber;

                    if (mobile1.length() > 14) {
                        displayDialog(" Nomor Handphone Lebih Dari 14 Angka",
                                LoginScreenActivity.this);
                    } else if (mobile1.length() <= 6) {
                        displayDialog(
                                " Nomor Handphone harus lebih dari 6 angka",
                                LoginScreenActivity.this);
                    } else {
                        mobileNumber = mobile1.substring(1);

                        nextProcess();
                    }
                } else if (!(countryCode.equalsIgnoreCase("+62"))) {
                    if (e_Mdn.getText().toString().startsWith("0")) {
                        mobileNumber = e_Mdn.getText().toString()
                                .substring(1);
                    } else {
                        mobileNumber = e_Mdn.getText().toString();
                    }
                    String mobleNumber = countryCode.replace("+", "")
                            + mobileNumber;

                    if (mobleNumber.length() > 14) {
                        displayDialog(" Nomor Handphone Lebih Dari 14 Angka",
                                LoginScreenActivity.this);
                    } else if (mobleNumber.length() <= 6) {
                        displayDialog(
                                " Nomor Handphone harus lebih dari 6 angka",
                                LoginScreenActivity.this);
                    } else {
                        mobileNumber = mobleNumber;
                        nextProcess();
                    }
                }*/
                nextProcess();
            }
        });

    }


    public static void displayDialog(String msg, Context ctx) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(ctx);
        alertbox.setMessage(msg);
        alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        alertbox.show();
    }


    void nextProcess() {


        Intent intent = new Intent(LoginScreenActivity.this, NumberSwitchingActivity.class);
        startActivity(intent);
        finish();
        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(e_mPin, InputMethodManager.SHOW_IMPLICIT);
        /*Intent intent = new Intent(LoginScreenActivity.this, SimaspayUserActivity.class);
        intent.putExtra("direct",true);
        startActivity(intent);
        finish();*/
    }
}
