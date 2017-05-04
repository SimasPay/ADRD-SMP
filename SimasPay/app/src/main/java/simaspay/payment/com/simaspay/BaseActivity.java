package simaspay.payment.com.simaspay;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.payment.simaspay.utils.Functions;

/**
 * Created by widy on 5/4/17.
 * 04
 */

public class BaseActivity extends AppCompatActivity {

    public static final long DISCONNECT_TIMEOUT = 600000; // 10 min = 10 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            Functions func = new Functions(getBaseContext());
            func.errorTimeoutResponseConfirmation("Please login again");
        }
    };

    public void resetDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction(){
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }
}