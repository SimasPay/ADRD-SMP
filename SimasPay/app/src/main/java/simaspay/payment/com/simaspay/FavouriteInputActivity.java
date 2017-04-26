package simaspay.payment.com.simaspay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SecondLoginActivity;
import com.payment.simaspay.utils.Functions;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import java.util.HashMap;
import java.util.Map;

import static com.payment.simaspay.services.Constants.LOG_TAG;

/**
 * Created by widy on 4/6/17.
 * 06
 */

public class FavouriteInputActivity extends AppCompatActivity {
    Functions func;
    SharedPreferences sharedPreferences;
    int msgCode, idFavCat, stCatID;
    String sourceMDN, stMPIN, stFavNumber, message, transactionTime, responseCode;
    SharedPreferences settings, languageSettings;
    String selectedLanguage;
    String favCat="", FavCode="";
    EditText desc, number;
    String descript_fav;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_input);
        func=new Functions(this);
        func.initiatedToolbar(this);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        languageSettings = getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");
        settings = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        sourceMDN = settings.getString("mobileNumber","");
        stMPIN = func.generateRSA(sharedPreferences.getString(Constants.PARAMETER_MPIN, ""));

        //TextView title=(TextView)findViewById(R.id.titled);
        number=(EditText) findViewById(R.id.number);
        //number.setEnabled(false);
        desc=(EditText)findViewById(R.id.desc);

        if(getIntent().getExtras()!=null){
            number.setText(getIntent().getExtras().getString("DestMDN"));
            stFavNumber=getIntent().getExtras().getString("DestMDN");
            favCat=getIntent().getExtras().getString("favCat");
            idFavCat=getIntent().getExtras().getInt("idFavCat");
            stCatID=idFavCat;
            if(getIntent().getExtras().getString("favCode")!=null){
                FavCode=getIntent().getExtras().getString("favCode");
            }
        }


        ImageView back_btn=(ImageView)findViewById(R.id.btnBacke);
        back_btn.setOnClickListener(view -> {
            Intent i = new Intent(FavouriteInputActivity.this, UserHomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(view -> {
            descript_fav=desc.getText().toString();
            if(descript_fav.equals("")||descript_fav.length()<=2){
                Utility.displayDialog("Harap masukkan deskripsi", FavouriteInputActivity.this);
            }else{
                new submitFavAsyncTask().execute();
            }
        });

    }

    private class submitFavAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME, Constants.SERVICE_ACCOUNT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, "AddFavorite");
            mapContainer.put(Constants.PARAMETER_INSTITUTION_ID, Constants.CONSTANT_INSTITUTION_ID);
            mapContainer.put(Constants.PARAMETER_AUTHENTICATION_KEY, "");
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sourceMDN);
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, stMPIN);
            mapContainer.put("favoriteCategoryID", String.valueOf(stCatID));
            mapContainer.put("favoriteCode", FavCode.trim());
            mapContainer.put("favoriteLabel", descript_fav.trim());
            mapContainer.put("favoriteValue", stFavNumber.trim());
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID, "7");

            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    FavouriteInputActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(FavouriteInputActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (response != null) {
                Log.e("-------", "=====" + response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseDataContainer = null;
                try {
                    responseDataContainer = obj.parse(response);
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.toString());
                }
                try {
                    if (responseDataContainer != null) {
                        Log.d("test", "not null");
                        AlertDialog.Builder alertbox;
                        if (msgCode == 631) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            alertbox = new AlertDialog.Builder(FavouriteInputActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setCancelable(false);
                            alertbox.setNeutralButton("OK", (arg0, arg1) -> {
                                Intent intent = new Intent(FavouriteInputActivity.this, SecondLoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            });
                            alertbox.show();
                        }else if(responseDataContainer.getMsgCode().equals("2087")){
                            message = responseDataContainer.getMsg();
                            Log.d(LOG_TAG, "message"+message);
                            transactionTime = responseDataContainer.getTransactionTime();
                            Log.d(LOG_TAG, "transactionTime"+transactionTime);
                            responseCode = responseDataContainer.getResponseCode();
                            Log.d(LOG_TAG, "responseCode"+responseCode);
                            Log.d("test", "not null");
                            msgCode = 0;
                            alertbox = new AlertDialog.Builder(FavouriteInputActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", (arg0, arg1) -> {
                                Intent intent = new Intent(FavouriteInputActivity.this, UserHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            });
                            alertbox.show();
                        }else{
                            alertbox = new AlertDialog.Builder(FavouriteInputActivity.this, R.style.MyAlertDialogStyle);
                            alertbox.setMessage(responseDataContainer.getMsg());
                            alertbox.setNeutralButton("OK", (arg0, arg1) -> {
                                Intent intent = new Intent(FavouriteInputActivity.this, UserHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            });
                            alertbox.show();
                        }
                    }
                }catch (Exception e) {
                    Log.e(LOG_TAG, "error: " + e.toString());
                }
            }else{
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), FavouriteInputActivity.this);
            }
        }
    }

}
