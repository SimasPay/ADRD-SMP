package com.payment.simaspay.contactus;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.payment.simaspay.services.Utility;

import java.io.LineNumberReader;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 12/22/2015.
 */
public class ContactUs_Activity extends Activity {

    TextView mobile_number_1, mobile_number_2, e_mail, website,textView,textView1,textView2,title1,title2,heading;

    ImageView mobile_1, mobile_2;
    Dialog dialogCustomWish;
    Button btnBacke;
    Context context;

    LinearLayout layout_1,layout_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
        context=ContactUs_Activity.this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        mobile_number_1=(TextView)findViewById(R.id.con_text_1);
        mobile_number_2=(TextView)findViewById(R.id.con_text_2);
        e_mail=(TextView)findViewById(R.id.con_text_3);
        website=(TextView)findViewById(R.id.con_text_4);

        textView=(TextView)findViewById(R.id.textview);
        textView1=(TextView)findViewById(R.id.textview2);
        textView2=(TextView)findViewById(R.id.textview3);
        title1=(TextView)findViewById(R.id.call_text_1);
        title2=(TextView)findViewById(R.id.call_text_2);
        heading=(TextView)findViewById(R.id.titled);

        mobile_1=(ImageView)findViewById(R.id.image_1);
        mobile_2=(ImageView)findViewById(R.id.image_2);

        layout_1=(LinearLayout)findViewById(R.id.layout_3);
        layout_2=(LinearLayout)findViewById(R.id.layout_4);

        btnBacke=(Button) findViewById(R.id.btnBacke);

        mobile_number_1.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));
        mobile_number_2.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));
        e_mail.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));
        website.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));

        textView.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));
        textView1.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));
        textView2.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));
        title1.setTypeface(Utility.BoldTextFormat(ContactUs_Activity.this));
        title2.setTypeface(Utility.BoldTextFormat(ContactUs_Activity.this));
        heading.setTypeface(Utility.Robot_Regular(ContactUs_Activity.this));

        btnBacke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mobile_number_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallToCustomerCare("1500 153");
            }
        });

        mobile_number_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallToCustomerCare("021 508 88888");
            }
        });

        mobile_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallToCustomerCare("1500 153");
            }
        });

        mobile_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallToCustomerCare("021 508 88888");
            }
        });

        e_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"care@banksinarmas.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT   , "Message Body");
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));


                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactUs_Activity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "http://www.banksinarmas.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        layout_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"care@banksinarmas.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT   , "Message Body");
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactUs_Activity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        layout_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "http://www.banksinarmas.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    public void CallToCustomerCare(final String string){

        dialogCustomWish = new Dialog(context);
        dialogCustomWish.setCancelable(false);

        dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        View view = LayoutInflater.from(context).inflate(R.layout.call_to_customercare, null);
        dialogCustomWish.setContentView(R.layout.call_to_customercare);

        Button button = (Button) dialogCustomWish.findViewById(R.id.OK);
        Button button1 = (Button) dialogCustomWish.findViewById(R.id.Cancel);
        TextView textView=(TextView)dialogCustomWish.findViewById(R.id.number);
        button.setTypeface(Utility.HelveticaNeue_Medium(context));
        button1.setTypeface(Utility.HelveticaNeue_Medium(context));
        textView.setTypeface(Utility.LightTextFormat(context));

        textView.setText(string);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogCustomWish.dismiss();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+string));
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

}
