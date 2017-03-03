package simaspay.payment.com.simaspay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
        ok_btn = (Button)findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getExtras().getString("status").equals("daftaremoney")){
                    Intent intent = new Intent(NotificationActivity.this, SecondLoginActivity.class);
                    startActivity(intent);
                    NotificationActivity.this.finish();
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
        if(getIntent().getExtras().getString("status").equals("daftaremoney")){
            Intent intent = new Intent(NotificationActivity.this, SecondLoginActivity.class);
            startActivity(intent);
            NotificationActivity.this.finish();
        }else{
            super.onBackPressed();
        }
    }


}
