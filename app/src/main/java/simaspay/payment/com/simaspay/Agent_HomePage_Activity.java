package simaspay.payment.com.simaspay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.AgentTransfer.AgentTransferHomeActivity;
import com.payment.simaspay.Referral_and_CloseAccount.CloseAccountDetailsActivity;
import com.payment.simaspay.Referral_and_CloseAccount.ReferralDetailsActivity;
import com.payment.simaspay.agentdetails.AgentRegistrationActivity;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.Cash_InOut.CashInDetailsActivity;
import com.payment.simaspay.userdetails.BalanceSheetActivity;

/**
 * Created by Nagendra P on 12/24/2015.
 */
public class Agent_HomePage_Activity extends Activity {


    TextView agent_tariktunai_text, agent_buka_text, agent_tutap_text, agent_transaksi_text, agent_rekening_text, agent_referrel_text, userName, userNumber, agent_akun, logut_text;


    TextView simas, pay;

    LinearLayout logout_layout, numberswitching_layout, agent_buka_layout, number_switching, agent_setortunai_layout, agent_tutap_layout, agent_transaksi_layout, agent_referral_layout, agent_rekening_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agent_home);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.splashscreen));
        }

        simas = (TextView) findViewById(R.id.simas);


        String s = "simaspay";
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1f), 0, 5, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);// set color

        simas.setText(ss1);


        agent_tariktunai_text = (TextView) findViewById(R.id.agent_tariktunai_text);
        agent_buka_text = (TextView) findViewById(R.id.agent_buka_text);
        agent_tutap_text = (TextView) findViewById(R.id.agent_tutap_text);
        agent_transaksi_text = (TextView) findViewById(R.id.agent_transaksi_text);
        agent_rekening_text = (TextView) findViewById(R.id.agent_rekening_text);
        agent_referrel_text = (TextView) findViewById(R.id.agent_referrel_text);

        userName = (TextView) findViewById(R.id.userName);
        userNumber = (TextView) findViewById(R.id.userNumber);

        agent_akun = (TextView) findViewById(R.id.ganti_akun);
        logut_text = (TextView) findViewById(R.id.laoout_text);

        agent_tariktunai_text.setTypeface(Utility.LightTextFormat(Agent_HomePage_Activity.this));
        agent_buka_text.setTypeface(Utility.LightTextFormat(Agent_HomePage_Activity.this));
        agent_tutap_text.setTypeface(Utility.LightTextFormat(Agent_HomePage_Activity.this));
        agent_transaksi_text.setTypeface(Utility.LightTextFormat(Agent_HomePage_Activity.this));
        agent_rekening_text.setTypeface(Utility.LightTextFormat(Agent_HomePage_Activity.this));
        agent_referrel_text.setTypeface(Utility.LightTextFormat(Agent_HomePage_Activity.this));
        userName.setTypeface(Utility.LightTextFormat(Agent_HomePage_Activity.this));
        userNumber.setTypeface(Utility.OpemSans_Light(Agent_HomePage_Activity.this));
        agent_akun.setTypeface(Utility.LightTextFormat(Agent_HomePage_Activity.this));
        logut_text.setTypeface(Utility.LightTextFormat(Agent_HomePage_Activity.this));

        logout_layout = (LinearLayout) findViewById(R.id.logOut);
        numberswitching_layout = (LinearLayout) findViewById(R.id.gantiAkun_layout);
        agent_tutap_layout = (LinearLayout) findViewById(R.id.agent_tutap_layout);
        agent_transaksi_layout = (LinearLayout) findViewById(R.id.agent_transaksi_layout);
        agent_referral_layout = (LinearLayout) findViewById(R.id.agent_referral_layout);
        agent_rekening_layout = (LinearLayout) findViewById(R.id.agent_rekening_layout);

        agent_rekening_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Agent_HomePage_Activity.this, BalanceSheetActivity.class);
                intent.putExtra("simaspayuser", true);
                intent.putExtra("transfer_or_mutasi", false);
                intent.putExtra("red_or_white", true);
                intent.putExtra("lakupandai", false);
                startActivityForResult(intent, 4);
            }
        });

        agent_referral_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Agent_HomePage_Activity.this, ReferralDetailsActivity.class);
                startActivity(intent);
            }
        });


        agent_buka_layout = (LinearLayout) findViewById(R.id.agent_buka_layout);
        agent_setortunai_layout = (LinearLayout) findViewById(R.id.agent_tariktunai_layout);

        agent_transaksi_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Agent_HomePage_Activity.this, AgentTransferHomeActivity.class);
                startActivityForResult(intent, 4);
            }
        });

        agent_buka_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Agent_HomePage_Activity.this, AgentRegistrationActivity.class);
                startActivity(intent);
            }
        });
        agent_tutap_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Agent_HomePage_Activity.this, CloseAccountDetailsActivity.class);
                startActivity(intent);
            }
        });

        agent_setortunai_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Agent_HomePage_Activity.this, CashInDetailsActivity.class);
                startActivity(intent);
            }
        });


        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });


        numberswitching_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4) {
            if (resultCode == RESULT_OK) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else if (resultCode == RESULT_CANCELED) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        }
    }
}
