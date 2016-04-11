package com.payment.simaspay.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

class GetPostpaidtpList extends AsyncTask<Void, Void, Void> {
	
	static Context context;
	ProgressDialog dialog;
	static long startTimeInMillis;
	@Override
	protected void onPreExecute() {
		startTimeInMillis=new java.util.Date().getTime();
		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setMessage("Loading ...");
		dialog.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		dialog.cancel();
		

	}
}