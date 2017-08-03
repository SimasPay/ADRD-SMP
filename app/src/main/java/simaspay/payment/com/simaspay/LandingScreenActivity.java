package simaspay.payment.com.simaspay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.payment.simaspay.UserActivation.ActivationPage_2_Activity;
import com.payment.simaspay.contactus.ContactUs_Activity;
import com.payment.simaspay.utils.Functions;

/**
 * Created by widy on 1/9/17.
 * 09
 */

public class LandingScreenActivity extends AppCompatActivity {
    Context context;
    public SharedPreferences settings;
    private static final String TAG = "SimasPay";
    Functions functions;
    TextView contact_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingpage);
        context = LandingScreenActivity.this;
        functions = new Functions(this);
        functions.initiatedToolbar(this);
        settings = getSharedPreferences(TAG, 0);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        Button login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(LandingScreenActivity.this, InputNumberScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button activation=(Button)findViewById(R.id.activation);
        activation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingScreenActivity.this, ActivationPage_2_Activity.class);
                startActivityForResult(intent, 10);
            }
        });

        contact_us=(TextView)findViewById(R.id.contact_us);
        contact_us.setText(Html.fromHtml("<u>Hubungi Kami</u>"));
        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingScreenActivity.this, ContactUs_Activity.class);
                startActivity(intent);
            }
        });


    }
}
