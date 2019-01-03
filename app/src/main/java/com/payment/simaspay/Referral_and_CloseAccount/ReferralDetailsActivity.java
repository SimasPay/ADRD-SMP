package com.payment.simaspay.Referral_and_CloseAccount;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.payment.simaspay.PojoClasses.ArealData;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.constants.EncryptedResponseDataContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.payment.simaspay.R;

/**
 * Created by Nagendra P on 1/28/2016.
 */
public class ReferralDetailsActivity extends Activity {

    TextView Name, number, email, others_text, title;

    EditText name_field, number_field, mail_field, others_field;

    Button others, submit;

    LinearLayout back_layout;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referralaccountdetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        Name = (TextView) findViewById(R.id.name);
        number = (TextView) findViewById(R.id.number);
        email = (TextView) findViewById(R.id.mail);
        others_text = (TextView) findViewById(R.id.produk);

        progressDialog = new ProgressDialog(ReferralDetailsActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));

        back_layout = (LinearLayout) findViewById(R.id.back_layout);

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title = (TextView) findViewById(R.id.titled);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        name_field = (EditText) findViewById(R.id.name_Editfield);
        number_field = (EditText) findViewById(R.id.number_Editfield);
        mail_field = (EditText) findViewById(R.id.mail_Editfield);
        others_field = (EditText) findViewById(R.id.lainnya_editText1);

        submit = (Button) findViewById(R.id.submit);
        others = (Button) findViewById(R.id.lainnya);

        Name.setTypeface(Utility.Robot_Regular(ReferralDetailsActivity.this));
        number.setTypeface(Utility.Robot_Regular(ReferralDetailsActivity.this));
        email.setTypeface(Utility.Robot_Regular(ReferralDetailsActivity.this));
        others_text.setTypeface(Utility.Robot_Regular(ReferralDetailsActivity.this));
        title.setTypeface(Utility.Robot_Regular(ReferralDetailsActivity.this));

        submit.setTypeface(Utility.Robot_Regular(ReferralDetailsActivity.this));

        name_field.setTypeface(Utility.Robot_Light(ReferralDetailsActivity.this));
        number_field.setTypeface(Utility.Robot_Light(ReferralDetailsActivity.this));
        mail_field.setTypeface(Utility.Robot_Light(ReferralDetailsActivity.this));
        others_field.setTypeface(Utility.Robot_Light(ReferralDetailsActivity.this));
        others.setTypeface(Utility.Robot_Light(ReferralDetailsActivity.this));

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(14);
        number_field.setFilters(FilterArray);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name_field.getText().toString().length() <= 0) {
                    Utility.displayDialog("Masukkan Nama Lengkap", ReferralDetailsActivity.this);
                } else if (number_field.getText().toString().length() <= 0) {
                    Utility.displayDialog("Masukkan Nomor Handphone", ReferralDetailsActivity.this);
                } else if (number_field.getText().toString().length() < 10) {
                    Utility.displayDialog(getResources().getString(R.string.number_less7), ReferralDetailsActivity.this);
                } else if (number_field.getText().toString().length() > 14) {
                    Utility.displayDialog(getResources().getString(R.string.number_grater14), ReferralDetailsActivity.this);
                } else if (!(mail_field.getText().toString().length() <= 0 || Utility.emailValidator(mail_field.getText().toString()))) {
                    Utility.displayDialog("Enter Valid Email ID", ReferralDetailsActivity.this);
                } else if (others.getText().toString().equals("")) {
                    Utility.displayDialog("Masukkan Produk yang Diinginkan", ReferralDetailsActivity.this);
                } else if (others.getText().toString().equals("Lainnya")) {
                    if (others_field.getText().toString().length() <= 0) {
                        Utility.displayDialog("Masukkan Sebutkan", ReferralDetailsActivity.this);
                    } else {
                        mdn = (number_field.getText().toString().replace(" ", ""));
                        NameField = name_field.getText().toString();
                        email_field = mail_field.getText().toString();
                        productDesired = others.getText().toString();
                        otherfield = others_field.getText().toString();
                        Intent intent = new Intent(ReferralDetailsActivity.this, ReferralAccountConfirmationActivity.class);
                        intent.putExtra("mdn", mdn);
                        intent.putExtra("NameField", NameField);
                        intent.putExtra("email_field", email_field);
                        intent.putExtra("productDesired", productDesired);
                        intent.putExtra("otherfield", otherfield);
                        startActivityForResult(intent, 10);
                    }
                } else {
                    mdn = (number_field.getText().toString().replace(" ", ""));
                    NameField = name_field.getText().toString();
                    email_field = mail_field.getText().toString();
                    productDesired = others.getText().toString();
                    otherfield = others_field.getText().toString();
                    Intent intent = new Intent(ReferralDetailsActivity.this, ReferralAccountConfirmationActivity.class);
                    intent.putExtra("mdn", mdn);
                    intent.putExtra("NameField", NameField);
                    intent.putExtra("email_field", email_field);
                    intent.putExtra("productDesired", productDesired);
                    intent.putExtra("otherfield", "");
                    startActivityForResult(intent, 10);
                }

            }
        });


        new UserReferralAsynTask().execute();
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.size() > 0) {
                    WorkDisplay();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 10) {
                finish();
            }
        }
    }


    String workString;


    ArrayList<ArealData> arrayList = new ArrayList<>();

    class UserReferralAsynTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_BILLPAYMENT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_GETTHIRDPARTYDATA);
            mapContainer.put(Constants.PARAMETER_CATEGORY, Constants.TRANSACTION_REFERRAL_DATA);
            mapContainer.put(Constants.PARAMETER_VERSION, Constants.CONSTANT_VALUE_ZERO);

            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, ReferralDetailsActivity.this);

            workString = webServiceHttp.getResponseSSLCertificatation();
           /* JSONParser jParser = new JSONParser();
             workString =  jParser.getJsonData("https://dl.dropboxusercontent.com/u/93708740/b.json");*/

            Log.e("=====", "=====" + workString);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (workString != null) {
                if (workString.startsWith("<")) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Log.e("-------", "---------" + workString);
                    XMLParser obj = new XMLParser();
                    EncryptedResponseDataContainer responseContainer = null;
                    try {
                        responseContainer = obj.parse(workString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        msgCode = Integer.parseInt(responseContainer
                                .getMsgCode());
                    } catch (Exception e) {
                        msgCode = 0;
                    }
                    Utility.displayDialog(responseContainer.getMsg(), ReferralDetailsActivity.this);
                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(workString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (jsonObject != null) {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = jsonObject.getJSONArray("referreal_list");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        arrayList.clear();
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ArealData arealData = new ArealData();
                                try {
                                    arealData.setWork(jsonArray.getJSONObject(i).getString("referrealName"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                arrayList.add(arealData);
                            }

                            if (arrayList.size() > 0) {
                                others.setText(arrayList.get(0).getWork());
                            }
                        }


                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Utility.networkDisplayDialog(sharedPreferences.getString(
                                "ErrorMessage",
                                getResources().getString(
                                        R.string.bahasa_serverNotRespond)), ReferralDetailsActivity.this);
                    }
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), ReferralDetailsActivity.this);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    Dialog dialogCustomWish;
    int selected_region = 0;

    ProductsAdapter productsAdapter;

    public void WorkDisplay() {

        dialogCustomWish = new Dialog(ReferralDetailsActivity.this);
        dialogCustomWish.setCancelable(false);

        dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        dialogCustomWish.setContentView(R.layout.province_list);

        Button button = (Button) dialogCustomWish.findViewById(R.id.ok);
        Button button1 = (Button) dialogCustomWish.findViewById(R.id.Cancel);
        TextView textView = (TextView) dialogCustomWish.findViewById(R.id.title);
        ListView listView = (ListView) dialogCustomWish.findViewById(R.id.locationsList);
        button.setTypeface(Utility.RegularTextFormat(ReferralDetailsActivity.this));
        button1.setTypeface(Utility.RegularTextFormat(ReferralDetailsActivity.this));
        textView.setTypeface(Utility.RegularTextFormat(ReferralDetailsActivity.this));
        textView.setText("Produk yang Diinginkan");

        productsAdapter = new ProductsAdapter();
        listView.setAdapter(productsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selected_region = position;
                productsAdapter.notifyDataSetChanged();
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_region = 0;
                dialogCustomWish.dismiss();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCustomWish.dismiss();

                if (selected_region != -1) {
                    others.setText(arrayList.get(selected_region).getWork());
                    if (arrayList.get(selected_region).getWork().equals("Lainnya")) {
                        others_field.setFocusableInTouchMode(true);
                        others_field.setClickable(true);
                        others_field.setFocusable(true);
                    } else {
                        others_field.setClickable(false);
                        others_field.setFocusable(false);
                    }
                }

            }
        });
        dialogCustomWish.show();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("=====", "======" + "Nagendra Palepu");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("=====", "=start=====" + "Nagendra Palepu");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("=====", "=Restart=====" + "Nagendra Palepu");
    }

    class ProductsAdapter extends BaseAdapter {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(ReferralDetailsActivity.this).inflate(R.layout.location_row, null);
            TextView textView = (TextView) view.findViewById(R.id.location_text);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_location);

            if (selected_region == position) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.selected));
            } else {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
            }

            textView.setText(arrayList.get(position).getWork());

            return view;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }


    int msgCode;
    ProgressDialog progressDialog;
    String mdn, NameField, email_field, otherfield, productDesired;


}
