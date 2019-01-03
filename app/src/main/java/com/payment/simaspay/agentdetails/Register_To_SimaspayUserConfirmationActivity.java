package com.payment.simaspay.agentdetails;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SessionTimeOutActivity;
import com.payment.simaspay.constants.EncryptedResponseDataContainer;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.payment.simaspay.R;

/**
 * Created by Nagendra P on 1/5/2016.
 */
public class Register_To_SimaspayUserConfirmationActivity extends Activity {

    TextView title, textView, name, name_field, nomor_ktp, nomor_ktp_field, ktp_berlaku_hingga,
            ktp_berlaku_hingga_field, alamat_sesuai_ktp, alamat_sesuai_ktp_field, alamat_domisili,
            alamat_domisili_field, nama_lengkap, nama_lengkap_field, tanggal_lahir, tanggal_lahir_field,
            pekerjaan, pekerjaan_field, pendapatan_per_Bulan, pendapatan_per_Bulan_field, tujuan_pembukaan_rekening,
            tujuan_pembukaan_rekening_field, sumber_dana, sumber_dana_field, nomor_hp, nomor_hp_field, e_mail, e_mail_field, tempat_lahir, tempat_lahir_field;

    Button confirmation, cancel;
    LinearLayout btnBack;

    Map<String, String> hashMap;
    Bitmap ktpBitmap, subscriberBitmap, supportedBitmap;
    SharedPreferences pSharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_simaspay_confirmation);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }
        pSharedPref = getApplicationContext().getSharedPreferences("transferData", Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        title = (TextView) findViewById(R.id.title);
        textView = (TextView) findViewById(R.id.textview);
        name = (TextView) findViewById(R.id.name);
        name_field = (TextView) findViewById(R.id.name_field);
        nomor_ktp = (TextView) findViewById(R.id.nomor_ktp);
        nomor_ktp_field = (TextView) findViewById(R.id.nomor_ktp_field);
        ktp_berlaku_hingga = (TextView) findViewById(R.id.ktp_berlaku_hingga);
        ktp_berlaku_hingga_field = (TextView) findViewById(R.id.ktp_berlaku_hingga_field);
        alamat_sesuai_ktp = (TextView) findViewById(R.id.alamat_sesuai_ktp);
        alamat_sesuai_ktp_field = (TextView) findViewById(R.id.alamat_sesuai_ktp_field);
        alamat_domisili = (TextView) findViewById(R.id.alamat_domisili);
        alamat_domisili_field = (TextView) findViewById(R.id.alamat_domisili_field);
        nama_lengkap = (TextView) findViewById(R.id.nama_lengkap);
        nama_lengkap_field = (TextView) findViewById(R.id.nama_lengkap_field);
        tanggal_lahir = (TextView) findViewById(R.id.tanggal_lahir);
        tanggal_lahir_field = (TextView) findViewById(R.id.tanggal_lahir_field);
        pekerjaan = (TextView) findViewById(R.id.pekerjaan);
        pekerjaan_field = (TextView) findViewById(R.id.pekerjaan_field);
        pendapatan_per_Bulan = (TextView) findViewById(R.id.pendapatan_per_Bulan);
        pendapatan_per_Bulan_field = (TextView) findViewById(R.id.pendapatan_per_Bulan_field);
        tujuan_pembukaan_rekening = (TextView) findViewById(R.id.tujuan_pembukaan_rekening);
        tujuan_pembukaan_rekening_field = (TextView) findViewById(R.id.tujuan_pembukaan_rekening_field);
        sumber_dana = (TextView) findViewById(R.id.sumber_dana);
        sumber_dana_field = (TextView) findViewById(R.id.sumber_dana_field);
        nomor_hp = (TextView) findViewById(R.id.nomor_hp);
        nomor_hp_field = (TextView) findViewById(R.id.nomor_hp_field);
        e_mail = (TextView) findViewById(R.id.e_mail);
        e_mail_field = (TextView) findViewById(R.id.e_mail_field);
        tempat_lahir = (TextView) findViewById(R.id.tempat_lahir);
        tempat_lahir_field = (TextView) findViewById(R.id.tempat_lahir_field);


        hashMap = loadMap();


        Log.e("-----------","---Before data-----"+hashMap.toString());

        if (pSharedPref.getString("ktpBitmap", "").equals("")) {
            hashMap.put(Constants.PARAMETER_KTPDOCUMENT, "");
        } else {
            File imgFile = new File(pSharedPref.getString("ktpBitmap", ""));
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            hashMap.put(Constants.PARAMETER_KTPDOCUMENT, ConvertBase64Data(myBitmap));
        }

        if (pSharedPref.getString("subscriberBitmap", "").equals("")) {
            hashMap.put(Constants.PARAMETER_SUBSCRIBER_DOCUMENT, "");
        } else {
            File imgFile = new File(pSharedPref.getString("subscriberBitmap", ""));
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            hashMap.put(Constants.PARAMETER_SUBSCRIBER_DOCUMENT, ConvertBase64Data(myBitmap));
        }

        if (pSharedPref.getString("supportedBitmap", "").equals("")) {
            hashMap.put(Constants.PARAMETER_SUPPORTDOCUMENT, "");
        } else {
            File imgFile = new File(pSharedPref.getString("supportedBitmap", ""));
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            hashMap.put(Constants.PARAMETER_SUPPORTDOCUMENT, ConvertBase64Data(myBitmap));
        }



        confirmation = (Button) findViewById(R.id.next);
        cancel = (Button) findViewById(R.id.cancel);

        btnBack = (LinearLayout) findViewById(R.id.back_layout);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        confirmation.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        cancel.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));


        title.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        textView.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        name.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        name_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserConfirmationActivity.this));
        nomor_ktp.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        nomor_ktp_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserConfirmationActivity.this));
        ktp_berlaku_hingga.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        ktp_berlaku_hingga_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserConfirmationActivity.this));
        alamat_sesuai_ktp.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        alamat_sesuai_ktp_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserConfirmationActivity.this));
        alamat_domisili.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        alamat_domisili_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserConfirmationActivity.this));
        nama_lengkap.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        nama_lengkap_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserConfirmationActivity.this));
        tanggal_lahir.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        tanggal_lahir_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserConfirmationActivity.this));
        pekerjaan.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        pekerjaan_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserConfirmationActivity.this));
        pendapatan_per_Bulan.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        pendapatan_per_Bulan_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserConfirmationActivity.this));
        tujuan_pembukaan_rekening.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        tujuan_pembukaan_rekening_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserConfirmationActivity.this));
        sumber_dana.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        sumber_dana_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserConfirmationActivity.this));
        nomor_hp.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        nomor_hp_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserConfirmationActivity.this));
        e_mail.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        e_mail_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserConfirmationActivity.this));
        tempat_lahir.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserConfirmationActivity.this));
        tempat_lahir_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserConfirmationActivity.this));


        try {
            tanggal_lahir_field.setText(hashMap.get(Constants.PARAMETER_DOB).substring(0, 2) + "-" + hashMap.get(Constants.PARAMETER_DOB).substring(2, 4) + "-" + hashMap.get(Constants.PARAMETER_DOB).substring(4));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(hashMap.get(Constants.PARAMETER_WORK).equalsIgnoreCase("Lainnya")){
            pekerjaan_field.setText(hashMap.get(Constants.PARAMETER_WORK) +" - "+hashMap.get(Constants.PARAMETER_OTHER_WORK));
        }else {
            pekerjaan_field.setText(hashMap.get(Constants.PARAMETER_WORK));
        }

        if(hashMap.get(Constants.PARAMETER_INCOME)!=null){
            double amount =  Double.parseDouble(hashMap.get(Constants.PARAMETER_INCOME));
            String s=String.format("%,.2f", amount).replace(",",".");
            try {
                pendapatan_per_Bulan_field.setText("Rp. "+s.substring(0,s.length()-3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        tujuan_pembukaan_rekening_field.setText(hashMap.get(Constants.PARAMETER_OPENINGACCOUNT));
        sumber_dana_field.setText(hashMap.get(Constants.PARAMETER_SOURCEOFFUNDS));
        e_mail_field.setText(hashMap.get(Constants.PARAMETER_EMAIL));
        tempat_lahir_field.setText(getIntent().getExtras().getString("birthplace"));

        name_field.setText(getIntent().getExtras().getString("KTPName"));
        nomor_ktp_field.setText(hashMap.get(Constants.PARAMETER_KTPID));

        if (hashMap.get(Constants.PARAMETER_KTPLIFETIME).equals("false")) {

            try {
                ktp_berlaku_hingga_field.setText(hashMap.get(Constants.PARAMETER_KTPVALIDUNTIL).substring(0, 2) + "-" + hashMap.get(Constants.PARAMETER_KTPVALIDUNTIL).substring(2, 4) + "-" + hashMap.get(Constants.PARAMETER_KTPVALIDUNTIL).substring(4));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ktp_berlaku_hingga_field.setText("Seumur Hidup");
        }

        alamat_sesuai_ktp_field.setText(getIntent().getExtras().getString("ktp_addressline"));
        if (hashMap.get(Constants.PARAMETER_DOMESTIC_IDENTITY).equals("1")) {
            alamat_domisili_field.setText("Sesuai Identitas");
        } else {
            alamat_domisili_field.setText(hashMap.get(Constants.PARAMETER_DIFF_LINE1));
        }

        nama_lengkap_field.setText(hashMap.get(Constants.PARAMETER_MOTHERSMAIDENNAME));
        nomor_hp_field.setText(hashMap.get(Constants.PARAMETER_DEST_MDN));

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AgentRegistrationAsynTask().execute();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            } else {

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    ProgressDialog progressDialog;
    String agentRegistrationResponse;

    int msgCode;
    SharedPreferences sharedPreferences, transferData;

    class AgentRegistrationAsynTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {


            WebServiceHttp webServiceHttp = new WebServiceHttp(hashMap, Register_To_SimaspayUserConfirmationActivity.this);

            agentRegistrationResponse = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Register_To_SimaspayUserConfirmationActivity.this);
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
            if (agentRegistrationResponse != null) {
                Log.e("-------", "---------" + agentRegistrationResponse);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseContainer = null;
                try {
                    responseContainer = obj.parse(agentRegistrationResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 631) {
                    Intent intent = new Intent(Register_To_SimaspayUserConfirmationActivity.this, SessionTimeOutActivity.class);
                    startActivityForResult(intent, 40);

                } else if (msgCode == 624) {

                    SharedPreferences.Editor editor = pSharedPref.edit();
                    editor.remove("My_map").commit();
                    JSONObject jsonObject = new JSONObject(hashMap);
                    String jsonString = jsonObject.toString();
                    editor.putString("My_map", jsonString);
                    editor.commit();
                    Intent intent = new Intent(Register_To_SimaspayUserConfirmationActivity.this, Register_To_SimaspayUserSuccessActivity.class);
                    intent.putExtra("KTPName", name_field.getText().toString());
                    intent.putExtra("birthplace", tanggal_lahir_field.getText().toString());
                    intent.putExtra("ktp_addressline", alamat_sesuai_ktp_field.getText().toString());
                    startActivityForResult(intent, 10);
                }else{
                    Utility.networkDisplayDialog(responseContainer.getMsg(), Register_To_SimaspayUserConfirmationActivity.this);
                }
            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), Register_To_SimaspayUserConfirmationActivity.this);
            }
        }
    }

    String ConvertBase64Data(Bitmap bitmap) {
        String string;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        bitmap.recycle();
        bitmap = null;
        byte[] b = baos.toByteArray();
        try {
            string = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            string = "";
            e.printStackTrace();
        }
        return string;
    }

    private Map<String, String> loadMap() {
        Map<String, String> outputMap = new HashMap<String, String>();

        try {
            if (pSharedPref != null) {
                String jsonString = pSharedPref.getString("My_map", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = (String) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }


}
