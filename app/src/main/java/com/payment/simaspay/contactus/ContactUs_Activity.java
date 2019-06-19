package com.payment.simaspay.contactus;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.constants.EncryptedResponseDataContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.payment.simaspay.R;

import static com.payment.simaspay.services.Constants.LOG_TAG;

public class ContactUs_Activity extends Activity {

    TextView mobile_number_1, mobile_number_2, e_mail, website, textView, textView1, textView2, heading;
    ImageView mobile_1, mobile_2;
    Dialog dialogCustomWish;
    Button btnBacke;
    Context context;
    LinearLayout layout_1, layout_2;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
        context = ContactUs_Activity.this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        mobile_number_1 = (TextView) findViewById(R.id.con_text_1);
        mobile_number_2 = (TextView) findViewById(R.id.con_text_2);
        e_mail = (TextView) findViewById(R.id.con_text_3);
        website = (TextView) findViewById(R.id.con_text_4);

        textView = (TextView) findViewById(R.id.textview);
        textView1 = (TextView) findViewById(R.id.textview2);
        textView2 = (TextView) findViewById(R.id.textview3);

        heading = (TextView) findViewById(R.id.titled);

        mobile_1 = (ImageView) findViewById(R.id.image_1);
        mobile_2 = (ImageView) findViewById(R.id.image_2);
        ImageView browser_4 = (ImageView) findViewById(R.id.image_4);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        layout_1 = (LinearLayout) findViewById(R.id.layout_3);
        layout_2 = (LinearLayout) findViewById(R.id.layout_4);

        btnBacke = (Button) findViewById(R.id.btnBacke);

        mobile_number_1.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));
        mobile_number_2.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));
        e_mail.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));
        website.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));

        textView.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));
        textView1.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));
        textView2.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));
        heading.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));

        new ContactUsAsyntask().execute();

        btnBacke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mobile_number_1.setText(getResources().getString(R.string.phone_contact_1));
        mobile_number_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CallToCustomerCare(mobile_number_1.getText().toString());
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mobile_number_1.getText().toString()));
                if (ActivityCompat.checkSelfPermission(ContactUs_Activity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });

        mobile_number_2.setText(getResources().getString(R.string.phone_contact_2));
        mobile_number_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*CallToCustomerCare(mobile_number_2.getText().toString());*/
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mobile_number_2.getText().toString()));
                if (ActivityCompat.checkSelfPermission(ContactUs_Activity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });

        mobile_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CallToCustomerCare(mobile_number_1.getText().toString());
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mobile_number_1.getText().toString()));
                if (ActivityCompat.checkSelfPermission(ContactUs_Activity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });

        mobile_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CallToCustomerCare(mobile_number_2.getText().toString());
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mobile_number_2.getText().toString()));
                if (ActivityCompat.checkSelfPermission(ContactUs_Activity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });

        e_mail.setText(getResources().getString(R.string.email_contact));
        e_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{e_mail.getText().toString()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Message Body");
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));


                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactUs_Activity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        website.setText(getResources().getString(R.string.website_contact));
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebPage("https://www.banksinarmas.com");
                /*
                try {
                    String url = "https://www.banksinarmas.com";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    String urlString="https://www.banksinarmas.com";
                    Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(urlString));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.android.chrome");
                    try {
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        context.startActivity(intent);
                    }
                }
                */
            }
        });

        browser_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebPage("https://www.banksinarmas.com");
                /*
                try {
                    String url = "http://www.banksinarmas.com";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    String urlString="http://www.banksinarmas.com";
                    Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(urlString));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.android.chrome");
                    try {
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        context.startActivity(intent);
                    }
                }
                */
            }
        });

        layout_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{e_mail.getText().toString()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Message Body");
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactUs_Activity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        /*layout_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = website.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        */
    }

    public void CallToCustomerCare(final String string) {

        dialogCustomWish = new Dialog(context);
        dialogCustomWish.setCancelable(false);

        dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        View view = LayoutInflater.from(context).inflate(R.layout.call_to_customercare, null);
        dialogCustomWish.setContentView(R.layout.call_to_customercare);

        Button button = (Button) dialogCustomWish.findViewById(R.id.OK);
        Button button1 = (Button) dialogCustomWish.findViewById(R.id.Cancel);
        TextView textView = (TextView) dialogCustomWish.findViewById(R.id.number);
        button.setTypeface(Utility.HelveticaNeue_Medium(context));
        button1.setTypeface(Utility.HelveticaNeue_Medium(context));
        textView.setTypeface(Utility.LightTextFormat(context));

        textView.setText(string);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogCustomWish.dismiss();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + string));
                if (ActivityCompat.checkSelfPermission(ContactUs_Activity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCustomWish.dismiss();
            }
        });
        dialogCustomWish.show();


    }

    ProgressDialog progressDialog;
    String response;
    int msgCode;

    private class ContactUsAsyntask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_BILLPAYMENT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_GETTHIRDPARTYDATA);
            mapContainer.put(Constants.PARAMETER_CATEGORY, Constants.TRANSACTION_CONTACTUS_FILE);
            mapContainer.put(Constants.PARAMETER_VERSION, Constants.CONSTANT_VALUE_ZERO);

            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, ContactUs_Activity.this);

            response = webServiceHttp.getResponseSSLCertificatation();
            /*JSONParser jParser = new JSONParser();
             response =  jParser.getJsonData("https://dl.dropboxusercontent.com/u/93708740/ContactUsfile.json");*/
            Log.e("=======", "-0-----" + response);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ContactUs_Activity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null) {
                if (response.startsWith("<")) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Log.e("-------", "---------" + response);
                    XMLParser obj = new XMLParser();
                    EncryptedResponseDataContainer responseContainer = null;
                    try {
                        responseContainer = obj.parse(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        msgCode = Integer.parseInt(responseContainer
                                .getMsgCode());
                    } catch (Exception e) {
                        msgCode = 0;
                    }
                    Utility.displayDialog(responseContainer.getMsg(), ContactUs_Activity.this);
                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (jsonObject != null) {
                        JSONObject jsonObject1 = null;

                        try {
                            jsonObject1 = jsonObject.getJSONObject("contactus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (jsonObject1 != null) {

                            try {
                                mobile_number_1.setText(jsonObject1.getString("mobilenumber_1"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mobile_number_2.setText(jsonObject1.getString("mobilenumber_2"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                e_mail.setText(jsonObject1.getString("emailid"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                website.setText(jsonObject1.getString("website"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Utility.networkDisplayDialog(sharedPreferences.getString(
                                "ErrorMessage",
                                getResources().getString(
                                        R.string.bahasa_serverNotRespond)), ContactUs_Activity.this);
                    }
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), ContactUs_Activity.this);
            }
        }
    }

    public void openWebPage(String url) {

        try {
            Uri webpage = Uri.parse(url);

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                webpage = Uri.parse("https://" + url);
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            Log.d(LOG_TAG, "Please try again later");
        }

    }

}
