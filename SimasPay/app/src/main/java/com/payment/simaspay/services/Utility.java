package com.payment.simaspay.services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.payment.simaspay.FlashizSDK.PayByQRActivity;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;
import com.payment.simpaspay.constants.ValueContainer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import simaspay.payment.com.simaspay.R;

//import android.util.Log;


public class Utility {
    static Activity a;
    static Context context;
    static String responseXml = null;
    static ProgressDialog dialog;
    private static AlertDialog.Builder alertbox;
    String photId, motherMaidneName, mobileNumber;
    static ValueContainer valueContainer;
    static SharedPreferences subscriberKYCStatus;

    static InputStream is = null;
    static String json = "";
    String refNo, errorCode, parameters;
    static String fromScreen;
    public static boolean kycStatus = true;
    public static boolean kycStatusHome;
    static String screenTitle;
    static SharedPreferences sharedPreferences;

    /******************************************************************
     * Image Crop & Resize
     *******************************************************************/

    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels,
                                              int width, int height) {
        Bitmap result = null;
        try {

            if (width == height) {
                result = Bitmap.createBitmap(height + 10, height + 10,
                        Bitmap.Config.ARGB_8888);
            } else {
                result = Bitmap.createBitmap(height, height,
                        Bitmap.Config.ARGB_8888);
            }
            result = Bitmap.createBitmap(height, height,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            int color = 0xff000000;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, result.getWidth(), result.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(result.getWidth() / 2 + 0.7f,
                    result.getHeight() / 2 + 0.7f,
                    result.getWidth() / 2 + 0.1f, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        } catch (NullPointerException e) {
        } catch (OutOfMemoryError o) {
        }
        return result;
    }

    /******************************************************************
     * Help List
     *******************************************************************/

    public static String getParseString(String str) {

        StringBuilder sb = new StringBuilder();
        String delimiter = "\\|";
        String temp[] = str.split(delimiter);
        for (int i = 0; i < temp.length; i++)
            sb.append(temp[i]).append("\n");
        return sb.toString();

    }


    public static String NormalizationMDN(String string) {
        String DestMDN;
        if (string.startsWith("0")) {
            DestMDN = "62" + string.substring(1);
        } else if (string.startsWith("62")) {
            DestMDN = string;
        } else {
            DestMDN = "62" + string;
        }
        return DestMDN;
    }

    public static String format(String paramString) {
        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; ; i++) {
            if (i >= paramString.length())
                return localStringBuilder.toString();
            if ((i > 0) && (i % 4 == 0))
                localStringBuilder.append(" ");
            localStringBuilder.append(paramString.charAt(i));
        }
    }


    public static void setHideSoftKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static String formatfor3(String paramString) {
        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; ; i++) {
            if (i >= (paramString.length()))
                return localStringBuilder.toString();
            if ((i > 0) && (i % 4 == 0))
                localStringBuilder.append(" ");
            localStringBuilder.append(paramString.charAt(i));
        }
    }

    /******************************************************************
     * NON-KYC LIST
     *******************************************************************/

    public static ArrayList<String> getNonKYCAllowList(String str) {

        StringTokenizer stringTokenizer = new StringTokenizer(str, "|");
        ArrayList<String> allowList = new ArrayList<String>();
        while (stringTokenizer.hasMoreElements()) {

            allowList.add(stringTokenizer.nextElement().toString());
        }
        return allowList;
    }

    // Get file from the assets
    public String readFileFromAssets(String fileName, Context context) {
        // TODO Auto-generated method stub
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getAssets()
                    .open(fileName)));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String getAlertMessage(String string, Context context) {
        @SuppressWarnings("unused")
        JSONObject jsonObject = null;

        sharedPreferences = context.getSharedPreferences("LOGIN_PREFERECES",
                Context.MODE_PRIVATE);
        String stringValue = "";
        try {
            jsonObject = new JSONObject(sharedPreferences.getString("jsonFormat", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            return jsonObject.getJSONObject("SmartfrenData").getString(
                    "PackageName");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stringValue;

    }

    public static int getFontForPhone() {
        DisplayMetrics metrics = new DisplayMetrics();
        a.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//		System.out.println("Testing>>" + metrics.densityDpi);

        if (metrics.densityDpi == 120) {
//			System.out.println("Testing>>low" + metrics.densityDpi);
            return 13;
        } else if (metrics.densityDpi == 160) {
//			System.out.println("Testing>>Medium" + metrics.densityDpi);
            return 14;

        } else if (metrics.densityDpi == 240) {
//			System.out.println("Testing>>High" + metrics.densityDpi);
            return 14;

        } else if (metrics.densityDpi == 320) {

//			System.out.println("Testing>>Xhigh" + metrics.densityDpi);
            return 17;

        } else {
            return 0;
        }
    }



    @SuppressLint({"ParserError", "ParserError"})
    public static String getResponseSSLCertificatation(String params) {

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
            TrustManager[] nullTrustManagers = {nullTrustManager};
            try {
                sslContext.init(null, nullTrustManagers, new SecureRandom());
            } catch (KeyManagementException e) {

                e.printStackTrace();
            }

            url = new URL(AppConfigFile.kycServerUrl);

        } catch (MalformedURLException e) {

            e.printStackTrace();
        }

        HttpsURLConnection conn = null;
        try {
            // System.out.println("Testing>>requestURL"+getUrl());
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

            InputStreamReader resultInputStream = new InputStreamReader(
                    conn.getInputStream());
            BufferedReader rd = new BufferedReader(resultInputStream);
            String line;
            StringBuffer sb = new StringBuffer("");

            while ((line = rd.readLine()) != null) {
                sb.append(line + "\n");
                // contents +=rd.readLine();

//				Log.i("Nav", "version res:: " + line);
            }
            // contents = line;
            contents = sb.toString();
            // System.out.println("-----------Check for content--------------------------------"+contents);
            rd.close();
            resultInputStream.close();

            // System.out.println("------------ resp  --------------------------------"+
            // rc);

        } catch (SocketTimeoutException e) {
            // System.out.println("Time out " );
            contents = null;
        } catch (ConnectException e) {
            // System.out.println("connectionException ");
            contents = null;
        } catch (java.net.ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // System.out.println("ioException ");
            contents = null;
        } finally {
            conn.disconnect();
        }

        return contents;
    }

    public static Typeface LightTextFormat(Context context) {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "HelveticaNeue_Light.otf");

        return typeface;

    }

    public static Typeface OpemSans_Light(Context context) {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "OpenSans_Light.ttf");

        return typeface;

    }

    public static Typeface BoldTextFormat(Context context) {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "proximanova_bold.otf");

        return typeface;

    }

    public static Typeface RegularTextFormat(Context context) {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "Roboto_Medium.ttf");

        return typeface;

    }

    public static Typeface Robot_Regular(Context context) {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "Roboto_Regular.ttf");

        return typeface;

    }

    public static Typeface Robot_Light(Context context) {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "Roboto_Light.ttf");

        return typeface;

    }

    public static Typeface Robot_Bold(Context context) {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "Roboto_Bold.ttf");

        return typeface;

    }

    public static Typeface Robot_Thin(Context context) {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "Roboto_Thin.ttf");

        return typeface;

    }


    public static Typeface HelveticaNeue_Medium(Context context) {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "HelveticaNeue_Medium.otf");

        return typeface;

    }

    public static void homenetworkDisplayDialog(String msg, final Context ctx) {

        Utility.context = ctx;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.error_custom_dialog);

        android.widget.TextView TextView = (TextView) dialog.findViewById(R.id.errorMsg);
        TextView.setText(msg);

        Button exclamationIcon = (Button) dialog
                .findViewById(R.id.exclamationIcon);
        exclamationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((Activity) ctx).finish();
            }
        });

        Button okay = (Button) dialog.findViewById(R.id.btnOk);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((Activity) ctx).finish();

            }
        });
        dialog.show();
    }

    /*public static void ForceUpdate(String msg, final Context ctx) {

        Utility.context = ctx;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fource_update);

        TextView TextView = (TextView) dialog.findViewById(R.id.errorMsg);
        TextView.setText(msg);

        Button exclamationIcon = (Button) dialog
                .findViewById(R.id.exclamationIcon);
        // if button is clicked, close the custom dialog
//		exclamationIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((Activity) ctx).finish();
            }
        });

        Button okay = (Button) dialog.findViewById(R.id.btnOk);

//		Button cancel=(Button)dialog.findViewById(R.id.cancelForceUpdate);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((Activity) ctx).finish();

            }
        });

        okay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                 try {
                        Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                        Uri.parse("https://play.google.com/store/apps/details?id=com.uangku.emoney"));
                        ((Activity) ctx).startActivity(viewIntent);
            }catch(Exception e) {
                Toast.makeText(ctx,"Unable to Connect Try Again...",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            }
        });
        dialog.show();
    }*/
    public static String getHttpresponse(String params) {
        String contents = null;

        try {


            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(AppConfigFile.kycServerUrl
                    + params);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {

                sb.append(line + "\n");

            }
            contents = sb.toString();
            reader.close();
            is.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // resultString = sb.toString();
        return contents;
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static void ShowDialog(String str, final Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(ctx.getResources().getString(R.string.dailog_heading));
        builder.setMessage(str);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                ((Activity) ctx).finish();
            }
        });
        builder.setCancelable(false);
        AlertDialog alertdialog = builder.create();
        alertdialog.show();
    }



    public static void networkDisplayDialog(String msg, Context ctx) {

        Utility.context = ctx;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.resend_otp_dialog);
        dialog.setCancelable(false);
        TextView TextView1 = (TextView) dialog.findViewById(R.id.number_1);
        TextView1.setText(msg);

        TextView textView=(TextView)dialog.findViewById(R.id.number);

        textView.setTypeface(Utility.Robot_Regular(ctx));
        TextView1.setTypeface(Utility.Robot_Light(ctx));

        textView.setText(ctx.getResources().getString(R.string.dailog_heading));


        Button okay = (Button) dialog.findViewById(R.id.OK);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static boolean emailValidator(String email) {
        if (email.length() <= 0) {
            return false;
        } else {
            boolean isValid = false;

            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            CharSequence inputStr = email;

            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(inputStr);
            if (matcher.matches()) {
                isValid = true;
            }
            return isValid;

        }
    }
	
	/*public static void BUyMemberShipInquery(String msg, Context ctx) {

		Utility.context = ctx;
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.error_custom_dialog);

		TextView TextView = (TextView) dialog.findViewById(R.id.errorMsg);
		TextView.setText(msg);

		Button exclamationIcon = (Button) dialog
				.findViewById(R.id.exclamationIcon);
		// if button is clicked, close the custom dialog
		exclamationIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		Button okay = (Button) dialog.findViewById(R.id.btnOk);
		// if button is clicked, close the custom dialog
		okay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();
	}*/

    public static void displayDialog(String msg, Context ctx) {

        Utility.context = ctx;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.resend_otp_dialog);
        dialog.setCancelable(false);
        TextView TextView1 = (TextView) dialog.findViewById(R.id.number_1);
        TextView1.setText(msg);

        TextView textView=(TextView)dialog.findViewById(R.id.number);

        textView.setTypeface(Utility.Robot_Regular(ctx));
        TextView1.setTypeface(Utility.Robot_Light(ctx));

        textView.setText(ctx.getResources().getString(R.string.dailog_heading));


        Button okay = (Button) dialog.findViewById(R.id.OK);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }



    public static String getMonth(String month) {
        if (month.equalsIgnoreCase("01")) {
            return "Jan";
        } else if (month.equalsIgnoreCase("02")) {
            return "Feb";
        } else if (month.equalsIgnoreCase("03")) {
            return "Mar";
        } else if (month.equalsIgnoreCase("04")) {
            return "Apr";
        } else if (month.equalsIgnoreCase("05")) {
            return "Mei";
        } else if (month.equalsIgnoreCase("06")) {
            return "Jun";
        } else if (month.equalsIgnoreCase("07")) {
            return "Jul";
        } else if (month.equalsIgnoreCase("08")) {
            return "Agu";
        } else if (month.equalsIgnoreCase("09")) {
            return "Sep";
        } else if (month.equalsIgnoreCase("10")) {
            return "Okt";
        } else if (month.equalsIgnoreCase("11")) {
            return "Nov";
        } else if (month.equalsIgnoreCase("12")) {
            return "Des";
        } else {
            return "Choose Month";
        }

    }
    /*public static String getMonth(String month) {
        if (month.equalsIgnoreCase("01")) {
            return "Januari";
        } else if (month.equalsIgnoreCase("02")) {
            return "Februari";
        } else if (month.equalsIgnoreCase("03")) {
            return "Maret";
        } else if (month.equalsIgnoreCase("04")) {
            return "April";
        } else if (month.equalsIgnoreCase("05")) {
            return "Mei";
        } else if (month.equalsIgnoreCase("06")) {
            return "Juni";
        } else if (month.equalsIgnoreCase("07")) {
            return "Juli";
        } else if (month.equalsIgnoreCase("08")) {
            return "Agustus";
        } else if (month.equalsIgnoreCase("09")) {
            return "September";
        } else if (month.equalsIgnoreCase("10")) {
            return "Oktober";
        } else if (month.equalsIgnoreCase("11")) {
            return "November";
        } else if (month.equalsIgnoreCase("12")) {
            return "Desember";
        } else {
            return "Choose Month";
        }

    }*/
	/*public static void KycLevelOrNot(String msg, Context ctx) {

		Utility.context = ctx;
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.kyclevel);

		// TextView TextView = (TextView) dialog.findViewById(R.id.errorMsg);
		// TextView.setText(msg);

		ImageView exclamationIcon = (ImageView) dialog
				.findViewById(R.id.exclamationIcon1);
		exclamationIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		Button okay = (Button) dialog.findViewById(R.id.btnOk1);
		okay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(context,
						CaraRegistrasiUangkuActivity.class);
				context.startActivity(intent);
			}
		});
		dialog.show();
	}*/

	/*private static void getRemainingBalance() {
		// sourceMDN=629989315314&sourcePIN=xxxxxx&institutionID=&authenticationKey=&
		// txnName=RemainingBalance
		sharedPreferences = context.getSharedPreferences("LOGIN_PREFERECES",
				Context.MODE_WORLD_READABLE);
		Editor editor = sharedPreferences.edit();
		Map<String, String> mapContainer = new HashMap<String, String>();
		mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
				Constants.CONSTANT_CHANNEL_ID);
		mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
				Constants.SERVICE_WALLET);
		mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
				"RemainingBalance");
		mapContainer.put(Constants.PARAMETER_SOURCE_MDN,
				sharedPreferences.getString("mobileNumber", ""));
		mapContainer.put(Constants.PARAMETER_INSTITUTION_ID,
				Constants.CONSTANT_INSTITUTION_ID);
		mapContainer.put(Constants.PARAMETER_AUTHENTICATION_KEY, "");
		mapContainer.put(Constants.PARAMETER_SOURCE_PIN,
				sharedPreferences.getString("password", ""));

//		Log.e("------", "------" + mapContainer.toString());

		final WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
				context);
		dialog = ProgressDialog
				.show(context, "  Uangku               ", context
						.getResources().getString(R.string.bahasa_loading),
						true);
		dialog.setCancelable(true);
		final Handler handler = new Handler() {

			public void handleMessage(Message msg) {
				if (responseXml != null) {
//					Log.e("------", "-----" + responseXml);

					XMLParser obj = new XMLParser();
					*//** Parsing of response. *//*
					EncryptedResponseDataContainer responseContainer = null;
					int msgCode = 0;
					try {
						responseContainer = obj.parse(responseXml);
						msgCode = Integer.parseInt(responseContainer
								.getMsgCode());
					} catch (Exception e) {
						msgCode = 0;
					}
					dialog.dismiss();
					if (msgCode == 2134) {

						Intent intent = new Intent(context,
								HomeCurrentBalance.class);
						intent.putExtra("MDN",
								sharedPreferences.getString("mobileNumber", ""));
						intent.putExtra("remainingBalance",
								responseContainer.getRemainbalance());
						context.startActivity(intent);
					} else if (msgCode == 631) {
						Intent intent = new Intent(context,
								SessionTimeOutActivity.class);
						context.startActivity(intent);
					} else {
						wrongPinDialog(responseContainer.getMsg(), context);
						// displayDialog(responseContainer.getMsg(), context);
					}

				} else {
					dialog.dismiss();
					wrongPinDialog(
							context.getResources().getString(
									R.string.server_error_message), context);
				}
			}
		};

		final Thread checkUpdate = new Thread() {
			*/

    /**
     * Service call in thread in and getting response as xml in string.
     *//*
			public void run() {

				try {
					responseXml = webServiceHttp
							.getResponseSSLCertificatation();
				} catch (Exception e) {
					responseXml = null;
				}
				handler.sendEmptyMessage(0);
			}
		};
		checkUpdate.start();

	}*/
    public static String getDateDiffString(String endDate) {
        try {
            Calendar cal = Calendar.getInstance();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dateTwo = dateFormat.parse(endDate);

            long timeOne = cal.getTimeInMillis();
            long timeTwo = dateTwo.getTime();
            long oneDay = 1000 * 60 * 60 * 24;
            long delta = (timeTwo - timeOne) / oneDay;

            if (delta > 0) {
                return "" + delta + "";
            } else {
                delta *= -1;
                return "" + delta + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
