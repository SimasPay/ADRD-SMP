package com.payment.simaspay.services;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
//import android.util.Log;
import android.widget.EditText;

import com.payment.simaspay.constants.ValueContainer;

import com.payment.simaspay.R;

public class CommonServiceCall {

	public String responseXml;
	ValueContainer valueContainer;
	public static AlertDialog.Builder alertbox;
	int msgCode = 0;
	ProgressDialog dialog;
	EditText title, number, pin;
	Context ctx;
	String result;
	Thread checkUpdate;
	WebServiceHttp webServiceHttp;
	SharedPreferences sharedPreferences;
	Handler handler;
	static String emailreg = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

	public CommonServiceCall(Context context) {

		ctx = context;
		// checkUpdate = new Update();
		sharedPreferences = ctx.getSharedPreferences("LOGIN_PREFERECES",
				Context.MODE_PRIVATE);
		alertbox = new AlertDialog.Builder(ctx);
	}

	// Add Fav server calling


	// Email Validation
	public static boolean emailValidation(String email) {

		return email.matches(emailreg);

	}

	// Dialog Displaying

	public static void displayDialog(String msg, Context ctx) {
		alertbox = new AlertDialog.Builder(ctx);
		alertbox.setTitle(ctx.getResources().getString(R.string.bixTitle));
		alertbox.setMessage(msg);
		alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {

			}
		});
		alertbox.show();
	}



}
