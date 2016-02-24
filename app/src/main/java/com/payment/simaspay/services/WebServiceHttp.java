package com.payment.simaspay.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
//import android.util.Log;
import android.widget.Toast;

import simaspay.payment.com.simaspay.R;


public class WebServiceHttp extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 5000;
	Context context;
	String params;
	Map<String, String> mapContainer = new HashMap<String, String>();
	StringBuilder requestUrlConstruct;
	String param;
	SharedPreferences subscriberKYCStatus;
	String zimplePay_value = "false";
	String intiatedMdn="";
	String intiatedId="";

	public WebServiceHttp(Map<String, String> mapContainer, Context context) {
		this.context = context;
		this.mapContainer = mapContainer;
		this.context = context;
		requestUrlConstruct = new StringBuilder();
		subscriberKYCStatus = context.getSharedPreferences("LOGIN_PREFERECES",
				Context.MODE_PRIVATE);
	}

	public String getUrl() {

		for (Map.Entry<String, String> entry : mapContainer.entrySet()) {

			String key = entry.getKey();
			String value = encodeString(entry.getValue().trim());
			requestUrlConstruct.append(key).append("=").append(value)
					.append("&");
		}

		try {
			String URL = "content://com.emoney.eljohn.MyProvider/friends";
			Uri friends = Uri.parse(URL);
			Cursor c = context.getContentResolver().query(friends, null, null,
					null, "name");
			String result = "Javacodegeeks Results:";
			if (!c.moveToFirst()) {
				zimplePay_value = "false";
				intiatedMdn="";
				intiatedId="";
			} else {
				do {
					result = result + "\n"
							+ c.getString(c.getColumnIndex("name"))
							+ " with id " + c.getString(c.getColumnIndex("id"))
							+ " has birthday: "
							+ c.getString(c.getColumnIndex("birthday"));
					zimplePay_value = c.getString(c.getColumnIndex("birthday"));
					intiatedMdn=c.getString(c.getColumnIndex("mdn"));
					intiatedId=c.getString(c.getColumnIndex("mid"));
				} while (c.moveToNext());

			}
		} catch (Exception e1) {
			zimplePay_value = "false";
			intiatedMdn="";
			intiatedId="";
//			Log.e("---------", "========Nagendra Palepu.=======");
			e1.printStackTrace();
		}
//		Log.e("----------", "-------Message Value----" + zimplePay_value);
		if (subscriberKYCStatus.getString("profileId", "0").equals("0")) {
			/*
			 * if (subscriberKYCStatus.getBoolean("flashiz", false) ||
			 * subscriberKYCStatus.getBoolean("isiulangPulsa", false) ||
			 * subscriberKYCStatus.getBoolean("BuyMemberShip", false) ||
			 * subscriberKYCStatus.getBoolean("Homepage", false))
			 */
			if ((subscriberKYCStatus.getBoolean("eljohn", false)
					&& zimplePay_value.equals("true"))||subscriberKYCStatus.getString("zimplePayValue", "false").equals("true")) {
					
				
				params = requestUrlConstruct.substring(0,
						requestUrlConstruct.length() - 1)
						+ "&"
						+ Constants.PARAMETER_INSTITUTION_ID
						+ "="
						+ Constants.CONSTANT_INSTITUTION_ID
						+ "&"
						+ Constants.PARAMETER_MSPID
						+ "="
						+ Constants.CONSTANT_MSBID
						+ "&"
						+ "accountType"
						+ "="
						+ "ZPAY";
			} else if (zimplePay_value.equals("true")) {
				params = requestUrlConstruct.substring(0,
						requestUrlConstruct.length() - 1)
						+ "&"
						+ Constants.PARAMETER_PROFILE_ID
						+ "="
						+ subscriberKYCStatus.getString("profileId", "0")
						+ "&"
						+ Constants.PARAMETER_INSTITUTION_ID
						+ "="
						+ Constants.CONSTANT_INSTITUTION_ID
						+ "&"
						+ Constants.PARAMETER_MSPID
						+ "="
						+ Constants.CONSTANT_MSBID
						+ "&"
						+ "accountType"
						+ "="
						+ "ZPAY";
			} else {

				params = requestUrlConstruct.substring(0,
						requestUrlConstruct.length() - 1)
						+ "&"
						+ Constants.PARAMETER_INSTITUTION_ID
						+ "="
						+ Constants.CONSTANT_INSTITUTION_ID
						+ "&"
						+ Constants.PARAMETER_MSPID
						+ "="
						+ Constants.CONSTANT_MSBID
						+ "&"
						+ "accountType"
						+ "="
						+ "";

			}
		} else {

			/*
			 * if (subscriberKYCStatus.getBoolean("flashiz", false) ||
			 * subscriberKYCStatus.getBoolean("isiulangPulsa", false) ||
			 * subscriberKYCStatus.getBoolean("BuyMemberShip", false) ||
			 * subscriberKYCStatus.getBoolean("Homepage", false)) {
			 */
			if ((subscriberKYCStatus.getBoolean("eljohn", false)
					&& zimplePay_value.equals("true"))||subscriberKYCStatus.getString("zimplePayValue", "false").equals("true")) {
				params = requestUrlConstruct.substring(0,
						requestUrlConstruct.length() - 1)
						+ "&"
						+ Constants.PARAMETER_PROFILE_ID
						+ "="
						+ subscriberKYCStatus.getString("profileId", "0")
						+ "&"
						+ Constants.PARAMETER_INSTITUTION_ID
						+ "="
						+ Constants.CONSTANT_INSTITUTION_ID
						+ "&"
						+ Constants.PARAMETER_MSPID
						+ "="
						+ Constants.CONSTANT_MSBID
						+ "&"
						+ "accountType"
						+ "="
						+ "ZPAY";
			} else if (zimplePay_value.equals("true")) {
				params = requestUrlConstruct.substring(0,
						requestUrlConstruct.length() - 1)
						+ "&"
						+ Constants.PARAMETER_PROFILE_ID
						+ "="
						+ subscriberKYCStatus.getString("profileId", "0")
						+ "&"
						+ Constants.PARAMETER_INSTITUTION_ID
						+ "="
						+ Constants.CONSTANT_INSTITUTION_ID
						+ "&"
						+ Constants.PARAMETER_MSPID
						+ "="
						+ Constants.CONSTANT_MSBID
						+ "&"
						+ "accountType"
						+ "="
						+ "ZPAY";
			} else {
				params = requestUrlConstruct.substring(0,
						requestUrlConstruct.length() - 1)
						+ "&"
						+ Constants.PARAMETER_PROFILE_ID
						+ "="
						+ subscriberKYCStatus.getString("profileId", "0")
						+ "&"
						+ Constants.PARAMETER_INSTITUTION_ID
						+ "="
						+ Constants.CONSTANT_INSTITUTION_ID
						+ "&"
						+ Constants.PARAMETER_MSPID
						+ "="
						+ Constants.CONSTANT_MSBID
						+ "&"
						+ "accountType"
						+ "="
						+ "";
			}

		}
//		Log.e("URL:Params ", "" + params);
//		Log.e("URL:Params ", "" + AppConfigFile.requestUrl);
		return AppConfigFile.requestUrl;
	}

	/**
	 * This method used for service call and handled the HTTPS response with SSL
	 * certification.
	 */

	@SuppressLint({ "ParserError", "ParserError" })
	public String getResponseSSLCertificatation() {

		String contents = null;

		// Load the self-signed server certificate
		char[] passphrase = "DDTCert".toCharArray();
		KeyStore ksTrust = null;

		try {
			ksTrust = KeyStore.getInstance("BKS");
		} catch (KeyStoreException e1) {

			e1.printStackTrace();
		}

		try {
			ksTrust.load(context.getResources().openRawResource(R.raw.ddtcert),
					passphrase);
		} catch (NoSuchAlgorithmException e1) {

			e1.printStackTrace();
		} catch (java.security.cert.CertificateException e1) {

			e1.printStackTrace();
		} catch (NotFoundException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		}

		TrustManagerFactory tmf = null;

		try {
			tmf = TrustManagerFactory.getInstance(KeyManagerFactory
					.getDefaultAlgorithm());
		} catch (NoSuchAlgorithmException e1) {

			e1.printStackTrace();
		}

		try {
			tmf.init(ksTrust);
		} catch (KeyStoreException e1) {

			e1.printStackTrace();
		}

		// Create a SSLContext with the certificate
		SSLContext sslContext = null;

		try {
			sslContext = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e1) {

			e1.printStackTrace();
		}

		try {
			sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
		} catch (KeyManagementException e1) {

			e1.printStackTrace();
		}

		URL url = null;
		// Create a HTTPS connection

		try {
			// Configure SSL Context
			try {
				sslContext = SSLContext.getInstance("TLS");
			} catch (NoSuchAlgorithmException e) {

				e.printStackTrace();
			}

			X509TrustManager nullTrustManager = new NullTrustManager();
			TrustManager[] nullTrustManagers = { nullTrustManager };
			try {
				sslContext.init(null, nullTrustManagers, new SecureRandom());
			} catch (KeyManagementException e) {

				e.printStackTrace();
			}

			url = new URL(getUrl());

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

		HttpsURLConnection conn = null;
		try {
//			 Log.e("", "Testing>>requestURL" + getUrl());
//			System.out.println("Testing>>Params" + params);

			conn = (HttpsURLConnection) url.openConnection();
			conn.setHostnameVerifier(new NullVerifier());
			conn.setSSLSocketFactory(sslContext.getSocketFactory());
			conn.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
			conn.setReadTimeout(Constants.CONNECTION_TIMEOUT);
			conn.setDoOutput(true);
			conn.setFixedLengthStreamingMode(params.getBytes().length);
			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(params);
			wr.flush();

			/*
			 * conn.setRequestMethod("POST");
			 * conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			 * conn.setHostnameVerifier(new NullVerifier());
			 * conn.setSSLSocketFactory(sslContext.getSocketFactory());
			 * conn.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
			 * conn.setReadTimeout(Constants.CONNECTION_TIMEOUT);
			 * conn.setDoOutput(true);
			 * conn.setFixedLengthStreamingMode(params.getBytes().length);
			 * DataOutputStream wr = new
			 * DataOutputStream(conn.getOutputStream()); wr.writeBytes(params);
			 * wr.flush(); wr.close();
			 */

			// conn.setAllowUserInteraction(true);
			int rc = 0;

			rc = conn.getResponseCode();

			if (rc == 0) {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {

					}
				}, SPLASH_DISPLAY_LENGHT);
			} else {

				InputStreamReader resultInputStream = new InputStreamReader(
						conn.getInputStream());
				BufferedReader rd = new BufferedReader(resultInputStream);
				String line;
				StringBuffer sb = new StringBuffer("");

				while ((line = rd.readLine()) != null) {
					sb.append(line + "\n");
				}
				contents = sb.toString();
				if (contents.contains("Your request is queued.")) {
					contents = null;
				}
				rd.close();
				resultInputStream.close();

			}
			

		} catch (SocketTimeoutException e) {
			// System.out.println("Time out " );
//			Log.e("----w---", "------Nagendra Socket TImeOut");
			contents = null;
			subscriberKYCStatus.edit().putString("ErrorMessage","Pelanggan Yth, saat ini sedang dilakukan pemeliharaan sistem untuk aplikasi Uangku, silahkan hubungi customer support untuk keterangan lebih lanjut.").commit();
			
		} catch (ConnectException e) {
			// System.out.println("connectionException ");
//			Log.e("-----w--", "------Connection Exception");
			subscriberKYCStatus.edit().putString("ErrorMessage","Pelanggan Yth, saat ini sedang dilakukan pemeliharaan sistem untuk aplikasi Uangku, silahkan hubungi customer support untuk keterangan lebih lanjut.").commit();
			contents = null;
		} catch (java.net.ProtocolException e) {
//			Log.e("---d----", "------Protocol Exception");
			subscriberKYCStatus.edit().putString("ErrorMessage","Pelanggan Yth, saat ini sedang dilakukan pemeliharaan sistem untuk aplikasi Uangku, silahkan hubungi customer support untuk keterangan lebih lanjut.").commit();
			e.printStackTrace();
		} catch (IOException e) {
//			Log.e("--d-----", "------ioException");
			// System.out.println("ioException ");
			subscriberKYCStatus.edit().putString("ErrorMessage","Tidak dapat terhubung dengan server Uangku. Harap periksa koneksi internet Anda dan coba kembali setelah beberapa saat.").commit();
			contents = null;
		} finally {
			conn.disconnect();
		}
//		Log.e("-----", "-----"+contents);
		return contents;
	}

	public String encodeString(String parameter) {
		String parameter1 = null;
		try {
			if (parameter.contains("@")) {
				parameter1 = parameter;
			} else {
				parameter1 = URLEncoder.encode(parameter, "utf-8");
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parameter1;

	}

}
