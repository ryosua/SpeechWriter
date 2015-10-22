package edu.psu.rcy5017.speechwriter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import edu.psu.rcy5017.speechwriter.R;
import edu.psu.rcy5017.speechwriter.constant.MixPanelCodes;

public class SplashScreenActivity extends Activity {
    
    private static final String TAG = "SplashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Remove title bar.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.activity_splash);
        
        Log.d(TAG, "Splash activity started");

        // Record activity in mixpanel.
        final MixpanelAPI mixpanel = MixpanelAPI.getInstance(this.getApplicationContext(), MixPanelCodes.MIXPANEL_TOKEN);
        mixpanel.track("Splash Screen Started");
        mixpanel.flush();

        final int SPLASH_TIME_OUT = 1500;
 
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start app main activity
                Intent i = new Intent(SplashScreenActivity.this, SpeechListActivity.class);
                startActivity(i);
        
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    
}