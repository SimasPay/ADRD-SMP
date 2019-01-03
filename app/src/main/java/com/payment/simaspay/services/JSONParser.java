package com.payment.simaspay.services;

import com.payment.simaspay.utils.CustomSSLSocketFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;


//import android.util.Log;

public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	// constructor
	public JSONParser() {

	}

	public JSONObject getJSONFromUrl(String url) {
//		Log.e("-----", "====" + url);

		// Making HTTP request
		try {
			// defaultHttpClient
			HttpClient httpClient;
			// if (url.startsWith("http")) {
			httpClient = new DefaultHttpClient();
			// } else {
			// Log.e("----", "----" + url);
			// httpClient = getStagingHttpClient();
			// }
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
//			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
//			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
//		Log.e("file", "JSON Data " + jObj);
		// return JSON String
		return jObj;

	}
	public String getDataFromUrl(String url) {
//		Log.e("-----", "====" + url);

		// Making HTTP request
		try {
			// defaultHttpClient
			HttpClient httpClient;
			// if (url.startsWith("http")) {
			httpClient = new DefaultHttpClient();
			// } else {
			// Log.e("----", "----" + url);
			// httpClient = getStagingHttpClient();
			// }
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
//			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		
		// return JSON String
		
//		Log.e("---------", "========"+json);
		return json;

	}

	public String getJsonData(String url) {

//		Log.e("response_profile", "==" + url);
		HttpClient httpClient1 = getStagingHttpClient();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Content-Type", "application/json");
		BasicHttpResponse httpResponse1 = null;
		try {
			httpResponse1 = (BasicHttpResponse) httpClient1.execute(httpGet);
		} catch (Exception e) {
			e.printStackTrace();
//			Log.e("--------------", "hello");
			return "1";
		}
		BasicResponseHandler handler1 = new BasicResponseHandler();
		String response_profile = "";
		try {
			response_profile = handler1.handleResponse(httpResponse1);
		} catch (HttpResponseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Log.e("response_profile", "==" + response_profile);
		return response_profile;

	}

	public String postHttpsData(String vehicleJSONStringer, String url) {
//		Log.e("------------------", "" + url);
//		Log.e("------------vehicleJSONStringer------", "" + vehicleJSONStringer);
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Content-type", "application/json");

		StringEntity entity = null;
		try {

			entity = new StringEntity(vehicleJSONStringer);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		httppost.setEntity(entity);

		HttpClient httpClient = getStagingHttpClient();

		// httpClient=new DefaultHttpClient();
		HttpResponse response = null;
		try {
			response = httpClient.execute(httppost);
		} catch (ClientProtocolException e1) {

			e1.printStackTrace();
			return "1";
		} catch (IOException e1) {
			e1.printStackTrace();
			return "-1";
		}
		try {
		} catch (Exception e1) {
			e1.printStackTrace();
			return "1";
		}
		InputStream inputStream = null;
		try {
			inputStream = response.getEntity().getContent();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuffer stringBuilder = new StringBuffer();
		String bufferedStrChunk = null;
		try {
			while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
				stringBuilder.append(bufferedStrChunk);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "1";
		}
		return stringBuilder.toString();
	}

	public HttpClient getStagingHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			org.apache.http.conn.ssl.SSLSocketFactory sf = new CustomSSLSocketFactory(
					trustStore);
			sf.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);
			HttpClient httpClient = new DefaultHttpClient(ccm, params);
			HttpConnectionParams.setSoTimeout(httpClient.getParams(), 60000);
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
					60000);
			return httpClient;
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

}
