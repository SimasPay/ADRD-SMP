package com.payment.simaspay.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.payment.simaspay.R;
import com.payment.simaspay.userdetails.SecondLoginActivity;

/**
 * Created by widy on 1/12/17.
 * 12
 */

public class NotificationActivity extends AppCompatActivity {
    private Button ok_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        TextView textView=(TextView)findViewById(R.id.textView);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String status = getIntent().getStringExtra("status");
            if(status.equals("forgotmpin")){
                textView.setText(getResources().getString(R.string.id_notif_success_mpin));
            }else{
                textView.setText(Html.fromHtml("<strong>Pendaftaran Berhasil!</strong><br>Selamat, Anda berhasil membuat akun e-money Simaspay"));
            }
        }else{
            textView.setText(Html.fromHtml("<strong>Pendaftaran Berhasil!</strong><br>Selamat, Anda berhasil membuat akun e-money Simaspay"));
        }
        ok_btn = (Button)findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    if(getIntent().getExtras().getString("status").equals("daftaremoney")||getIntent().getExtras().getString("status").equals("forgotmpin")){
                        Intent intent = new Intent(NotificationActivity.this, SecondLoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(NotificationActivity.this, LandingScreenActivity.class);
                        startActivity(intent);
                        NotificationActivity.this.finish();
                    }
                }else{
                    Intent intent = new Intent(NotificationActivity.this, LandingScreenActivity.class);
                    startActivity(intent);
                    NotificationActivity.this.finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(getIntent().getExtras()!=null){
            if(getIntent().getExtras().getString("status")!=null){
                if(getIntent().getExtras().getString("status").equals("daftaremoney")||getIntent().getExtras().getString("status").equals("forgotmpin")){
                    Intent intent = new Intent(NotificationActivity.this, SecondLoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(NotificationActivity.this, LandingScreenActivity.class);
                    startActivity(intent);
                }
            }else{
                Intent intent = new Intent(NotificationActivity.this, LandingScreenActivity.class);
                startActivity(intent);
            }
        }else{
            Intent intent = new Intent(NotificationActivity.this, LandingScreenActivity.class);
            startActivity(intent);
        }
    }


}
