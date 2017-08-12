package com.payment.simaspay.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.os.Handler;

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

import simaspay.payment.com.simaspay.R;

//import android.util.Log;


public class WebServiceHttp  {

    private final int SPLASH_DISPLAY_LENGHT = 5000;
    private Context context;
    private String params;
    private Map<String, String> mapContainer = new HashMap<String, String>();
    private StringBuilder requestUrlConstruct;
    private String param;
    private SharedPreferences subscriberKYCStatus;

    public WebServiceHttp(Map<String, String> mapContainer, Context context) {
        this.context = context;
        this.mapContainer = mapContainer;
        this.context = context;
        requestUrlConstruct = new StringBuilder();
        subscriberKYCStatus = context.getSharedPreferences("LOGIN_PREFERECES",
                Context.MODE_PRIVATE);
    }

    public WebServiceHttp(HashMap<String, String> mapContainer, Context context) {
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
            String value="";
            //Log.d("Test","key:" + entry.getKey() + ", VALUE: "+ entry.getValue());
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
        //Log.e("-----",""+params.toString());
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
            tmf.init(ksTrust);
        } catch (KeyStoreException e1) {
            e1.printStackTrace();
        }

        SSLContext sslContext = null;

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


        } catch (SocketTimeoutException | java.net.ProtocolException e) {
            contents = null;
            subscriberKYCStatus.edit().putString("ErrorMessage", "Pelanggan Yth, saat ini sedang dilakukan pemeliharaan sistem untuk aplikasi Uangku, silahkan hubungi customer support untuk keterangan lebih lanjut.").apply();
            e.printStackTrace();
        } catch (ConnectException e) {
            subscriberKYCStatus.edit().putString("ErrorMessage", "Pelanggan Yth, saat ini sedang dilakukan pemeliharaan sistem untuk aplikasi Uangku, silahkan hubungi customer support untuk keterangan lebih lanjut.").apply();
            contents = null;
            e.printStackTrace();
        } catch (IOException e) {
            subscriberKYCStatus.edit().putString("ErrorMessage", "Tidak dapat terhubung dengan server Uangku. Harap periksa koneksi internet Anda dan coba kembali setelah beberapa saat.").apply();
            contents = null;
            e.printStackTrace();
        }finally{
            conn.disconnect();
        }
        return contents;
    }


    public String getPostResponseSSLCertificatation() {

        String contents = null;

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
        } catch (NoSuchAlgorithmException | java.security.cert.CertificateException | IOException | NotFoundException e1) {
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

        try {
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

            url = new URL(getUrl());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpsURLConnection conn = null;
        try {
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
            contents = null;
            subscriberKYCStatus.edit().putString("ErrorMessage", "Pelanggan Yth, saat ini sedang dilakukan pemeliharaan sistem untuk aplikasi Uangku, silahkan hubungi customer support untuk keterangan lebih lanjut.").apply();
        } catch (ConnectException e) {
            subscriberKYCStatus.edit().putString("ErrorMessage", "Pelanggan Yth, saat ini sedang dilakukan pemeliharaan sistem untuk aplikasi Uangku, silahkan hubungi customer support untuk keterangan lebih lanjut.").apply();
            contents = null;
        } catch (java.net.ProtocolException e) {
            subscriberKYCStatus.edit().putString("ErrorMessage", "Pelanggan Yth, saat ini sedang dilakukan pemeliharaan sistem untuk aplikasi Uangku, silahkan hubungi customer support untuk keterangan lebih lanjut.").apply();
            e.printStackTrace();
        } catch (IOException e) {
            subscriberKYCStatus.edit().putString("ErrorMessage", "Tidak dapat terhubung dengan server Uangku. Harap periksa koneksi internet Anda dan coba kembali setelah beberapa saat.").apply();
            contents = null;
        } finally {
            conn.disconnect();
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

}
