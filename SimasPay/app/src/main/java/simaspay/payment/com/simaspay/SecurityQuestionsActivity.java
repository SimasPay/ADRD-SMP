package simaspay.payment.com.simaspay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by widy on 1/11/17.
 * 11
 */

public class SecurityQuestionsActivity extends AppCompatActivity{
    private static final String LOG_TAG = "SIMASPAY";
    private Spinner questions;
    private EditText answer;
    JSONObject jsonobject;
    JSONArray jsonarray;
    List<String> allQuestions = new ArrayList<String>();
    String stFullname, stEmail, stMPIN, stConfMPIN, stQuestion, stAnswer, stMDN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securityquestions);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        questions=(Spinner)findViewById(R.id.questions_spinner);
        answer=(EditText)findViewById(R.id.answer_ed);
        answer.setSingleLine(true);
        ImageView back_btn = (ImageView)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        new QuestionsLists().execute();
        Button daftar=(Button)findViewById(R.id.daftar);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(answer.getText().toString().length()==0) {
                    answer.setError("Harap isi Jawaban Pertanyaan di atas");
                    return;
                }else{

                    Bundle extras = getIntent().getExtras();
                    if (extras != null) {
                        stFullname = (String) extras.get("fullname");
                        stEmail = (String) extras.get("email");
                        stMDN = (String) extras.get("mdn");
                        stMPIN = (String) extras.get("mpin");
                        stConfMPIN = (String) extras.get("mpin2");
                        stAnswer=answer.getText().toString();
                        Intent intent = new Intent(SecurityQuestionsActivity.this, ConfirmationActivity.class);
                        intent.putExtra("fullname", stFullname);
                        intent.putExtra("email", stEmail);
                        intent.putExtra("mdn", stMDN);
                        intent.putExtra("question", stQuestion);
                        intent.putExtra("answer", stAnswer);
                        intent.putExtra("mpin", stMPIN);
                        intent.putExtra("mpin2", stAnswer);
                        startActivity(intent);
                    }

                }
            }
        });

    }

    class QuestionsLists extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String response;

        @Override
        protected Void doInBackground(Void... voids) {
            Map<String, String> mapContainer = new HashMap<>();
            mapContainer.put("service",
                    "Payment");
            mapContainer.put("txnName",
                    "GetThirdPartyData");
            mapContainer.put("category",
                    "category.securityQuestions");
            mapContainer.put("version",
                    "-1");
            mapContainer.put("channelID",
                    "7");
            Log.e("-----",""+mapContainer.toString());
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer,
                    SecurityQuestionsActivity.this);
            response = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SecurityQuestionsActivity.this);
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
                Log.e("-------","====="+response);
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
                        JSONObject jsonObj = new JSONObject(response);
                        jsonarray = jsonObj.getJSONArray("questionData");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            jsonobject = jsonarray.getJSONObject(i);
                            String question = jsonobject.getString("question");
                            allQuestions.add(question);
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (SecurityQuestionsActivity.this, android.R.layout.simple_spinner_item,allQuestions );

                        dataAdapter.setDropDownViewResource
                                (android.R.layout.simple_spinner_dropdown_item);

                        questions.setAdapter(dataAdapter);

                        questions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> arg0,
                                                       View arg1, int position, long arg3) {
                                stQuestion=allQuestions.get(position).toString();
                                //Toast.makeText(getApplicationContext(), "Pertanyaan yang dipilih: "+ allQuestions.get(position).toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.toString());
                }
            }
        }
    }


}
