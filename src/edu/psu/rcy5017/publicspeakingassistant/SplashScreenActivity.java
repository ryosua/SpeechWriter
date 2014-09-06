package edu.psu.rcy5017.publicspeakingassistant;

import edu.psu.rcy501.publicspeakingassistant.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity {
	// Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
 
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                try {
					startActivity(i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
