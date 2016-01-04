package edu.psu.rcy5017.speechwriter.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import edu.psu.rcy5017.speechwriter.R;
import edu.psu.rcy5017.speechwriter.constant.MixPanelCodes;

public class SplashScreenActivity extends Activity {
    
    private static final String TAG = "SplashScreenActivity";

    private MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Remove title bar.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.activity_splash);
        
        Log.d(TAG, "Splash activity started");

        // Record an event if this was the first use of the app.
        final String PREFS_NAME = "speech_writer_settings";

        mixpanel = MixpanelAPI.getInstance(this.getApplicationContext(), MixPanelCodes.MIXPANEL_TOKEN);

        // Record that the app has been installed for the first time.
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("first_time", true)) {
            Log.d(TAG, "App is being used for the first time.");

            mixpanel.track("App Installed");

            settings.edit().putBoolean("first_time", false).commit();
        } else {
            Log.d(TAG, "The app has been used before.");
        }

        mixpanel.track("Splash Screen Started");

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Send mixpanel data when the app is closed.
        mixpanel.flush();
        Log.d(TAG, "Flushing data .............");
    }
    
}