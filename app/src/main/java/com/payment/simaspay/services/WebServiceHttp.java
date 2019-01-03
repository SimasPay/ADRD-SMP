package com.payment.simaspay.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.payment.simaspay.utils.MyTrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.payment.simaspay.R;

import static com.payment.simaspay.services.Constants.LOG_TAG;


public class WebServiceHttp  {

    private Context context;
    private String params;
    private Map<String, String> mapContainer = new HashMap<>();
    private StringBuilder requestUrlConstruct;
    private SharedPreferences subscriberKYCStatus;
    private SSLContext sslContext = null;
    public WebServiceHttp(Map<String, String> mapContainer, Context context) {
        this.context = context;
        this.mapContainer = mapContainer;
        this.context = context;
        requestUrlConstruct = new StringBuilder();
        subscriberKYCStatus = context.getSharedPreferences(context.getResources().getString(R.string.shared_prefvalue),
                Context.MODE_PRIVATE);
    }

    public WebServiceHttp(HashMap<String, String> mapContainer, Context context) {
        this.mapContainer = mapContainer;
        this.context = context;
        requestUrlConstruct = new StringBuilder();
        subscriberKYCStatus = context.getSharedPreferences("LOGIN_PREFERECES",
                Context.MODE_PRIVATE);
    }

    public String getUrl() {

        for (Map.Entry<String, String> entry : mapContainer.entrySet()) {
            String key = entry.getKey();
            String value;
            if(entry.getValue()==null){
                value = encodeString(entry.getValue());
            }else{
                value = encodeString(entry.getValue().trim());
            }
            requestUrlConstruct.append(key).append("=").append(value)
                    .append("&");
        }


        if (subscriberKYCStatus.getString("profileId", "0").equals("0")) {

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
        Log.e("-----",""+ params);
        return AppConfigFile.requestUrl;
    }


    @SuppressLint({"ParserError", "ParserError"})
    public String getResponseSSLCertificatation() {

        String contents = null;

        char[] passphrase = "DDTCert".toCharArray();
        KeyStore ksTrust = null;

        try {
            ksTrust = KeyStore.getInstance("BKS");
        } catch (KeyStoreException e1) {

            e1.printStackTrace();
        }

        try {
            assert ksTrust != null;
            ksTrust.load(context.getResources().openRawResource(R.raw.ddtcert),
                    passphrase);
        } catch (NoSuchAlgorithmException | java.security.cert.CertificateException | NotFoundException | IOException e1) {
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
            assert tmf != null;
            tmf.init(ksTrust);
        } catch (KeyStoreException e1) {
            e1.printStackTrace();
        }

        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }

        try {
            if (sslContext != null) {
                sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
            }
        } catch (KeyManagementException e1) {
            e1.printStackTrace();
        }

        URL url = null;

        try {
            try {
                sslContext = SSLContext.getInstance("TLS");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            X509TrustManager nullTrustManager = new NullTrustManager();
            TrustManager[] nullTrustManagers = {nullTrustManager};
            try {
                assert sslContext != null;
                sslContext.init(null, nullTrustManagers, new SecureRandom());
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }

            url = new URL(getUrl());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpsURLConnection conn = null;
        SSLSocketFactory sf;
        try {
            assert url != null;
            Log.d(LOG_TAG,"proxy settings: "+ getProxyDetails(context));
            conn = (HttpsURLConnection) url.openConnection(Proxy.NO_PROXY);
            try {
                sf = new SSLSocketFactory(ksTrust);
                sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            } catch (NoSuchAlgorithmException e) {
                Log.d(LOG_TAG, "problem no such Algorithm");
                e.printStackTrace();
            } catch (KeyManagementException e) {
                Log.d(LOG_TAG, "problem key management");
                e.printStackTrace();
            } catch (KeyStoreException e) {
                Log.d(LOG_TAG, "problem keystore");
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                Log.d(LOG_TAG, "problem unrecoverable key");
                e.printStackTrace();
            }
            //conn.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            try {
                sslContext = SSLContext.getInstance("SSL");
                TrustManager[] tmlist = {new MyTrustManager()};
                sslContext.init(null, tmlist, new java.security.SecureRandom());
                //context.init(null, tmlist, null);
                conn.setSSLSocketFactory(sslContext.getSocketFactory());
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            }
            //conn.setSSLSocketFactory(sslContext.getSocketFactory());
            //conn.setSSLSocketFactory(buildSslSocketFactory(context));
            conn.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
            conn.setReadTimeout(Constants.CONNECTION_TIMEOUT);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(params.getBytes().length);
            OutputStreamWriter wr = new OutputStreamWriter(
                    conn.getOutputStream());
            wr.write(params);
            wr.flush();

            int rc;

            rc = conn.getResponseCode();
            if (rc == 0) {
                int SPLASH_DISPLAY_LENGHT = 5000;
                new Handler().postDelayed(() -> {

                }, SPLASH_DISPLAY_LENGHT);
            } else {

                InputStreamReader resultInputStream = new InputStreamReader(
                        conn.getInputStream());
                BufferedReader rd = new BufferedReader(resultInputStream);
                String line;
                StringBuilder sb = new StringBuilder("");

                while ((line = rd.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                contents = sb.toString();
                if (contents.contains("Your request is queued.")) {
                    contents = null;
                }
                rd.close();
                resultInputStream.close();

            }


        } catch (SocketTimeoutException | java.net.ProtocolException e) {
            contents = null;
            subscriberKYCStatus.edit().putString("ErrorMessage", context.getResources().getString(R.string.bahasa_serverNotRespond)).apply();
            e.printStackTrace();
            Log.d(LOG_TAG, "socket timeout or protocol problem");
        } catch (ConnectException e) {
            subscriberKYCStatus.edit().putString("ErrorMessage", context.getResources().getString(R.string.bahasa_serverNotRespond)).apply();
            contents = null;
            e.printStackTrace();
            Log.d(LOG_TAG, "connection problem");
        } catch (IOException e) {
            contents = null;
            e.printStackTrace();
            Log.d(LOG_TAG, "io problem : "+ e.toString());
            if(e.toString().contains("not verified:")){
                subscriberKYCStatus.edit().putString("ErrorMessage", context.getResources().getString(R.string.untrusted_connection)).apply();
            }else{
                subscriberKYCStatus.edit().putString("ErrorMessage", context.getResources().getString(R.string.bahasa_serverNotRespond)).apply();

            }
        }finally{
            if (conn != null) {
                conn.disconnect();
            }
        }
        return contents;
    }


    private String encodeString(String parameter) {
        String parameter1 = null;
        try {
            if (parameter.contains("@")) {
                parameter1 = parameter;
            } else {
                parameter1 = URLEncoder.encode(parameter, "utf-8");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return parameter1;

    }

    private static String getProxyDetails(Context context) {
        String proxyAddress = "";
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                proxyAddress = android.net.Proxy.getHost(context);
                if (proxyAddress == null || proxyAddress.equals("")) {
                    return proxyAddress;
                }
                proxyAddress += ":" + android.net.Proxy.getPort(context);
            } else {
                proxyAddress = System.getProperty("http.proxyHost");
                proxyAddress += ":" + System.getProperty("http.proxyPort");
            }
        } catch (Exception ex) {
            Log.d(LOG_TAG, "error: "+ex.toString());
        }
        return proxyAddress;
    }


}
