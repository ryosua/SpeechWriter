package edu.psu.rcy5017.speechwriter.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import edu.psu.rcy5017.speechwriter.constant.MiscConstants;
import edu.psu.rcy5017.speechwriter.listener.SpeechDeclineListener;
import edu.psu.rcy5017.speechwriter.listener.SpeecherAcceptListener;

/**
 * A controller to manage the Speecher ad.
 */
public class SpeecherAdController {

    private static final String TAG = "SpeecherAdController";
    private static final int MAX_IGNORES = 3;
    private static final int DEFAULT_AD_COUNT = 0;
    private static final int DEFAULT_IGNORE_COUNT = 0;
    private final boolean DEFAULT_VISITED_PAGE = false;

    private final Activity activity;
    private final SharedPreferences settings;

    // Set from shared preferences.
    private int adCount;
    private int ignoreCount;
    private boolean visitedWebpage;

    public SpeecherAdController(Activity activity) {
        this.activity = activity;
        settings = activity.getSharedPreferences(MiscConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);

        // Load how many times the ad was shown.
        adCount = settings.getInt("adCount", DEFAULT_AD_COUNT);

        // Load if visited Webpage.
        visitedWebpage = settings.getBoolean("visitedWebpage", DEFAULT_VISITED_PAGE);

        // Load how many times the ad was ignored.
        ignoreCount = settings.getInt("ignoreCount", DEFAULT_IGNORE_COUNT);
    }

    /**
     * Shows the Speecher ad dialog. The user can go to the Speecher launch page or ignore the ad.
     */
    public void showSpeecherDialog() {
        Log.d(TAG, "adCount: " + adCount);
        Log.d(TAG, "ignoreCount: " + ignoreCount);
        Log.d(TAG, "visitedWebpage: " + visitedWebpage);

        if (visitedWebpage == false) {
            if (adCount == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                String title = "Coming Soon";

                if (title != null) builder.setTitle(title);

                CharSequence message = "A new app, Speecher, is coming soon!";

                builder.setMessage(message);
                builder.setPositiveButton("Get Updates", new SpeecherAcceptListener(this));

                builder.setNegativeButton("Not now", new SpeechDeclineListener(this));
                builder.show();

                setAdCount(adCount + 1);

            } else if (adCount == 5) {
                setAdCount(0);
            } else {
                setAdCount(adCount + 1);
            }

        } else {
            Log.d(TAG, "The ad has been shown, or has been ignored enough times");
        }
    }

    /**
     * Creates an intent to open the Speecher launch page in the browser.
     */
    public void goToAd() {
        Log.d(TAG, "Send to newsletter");
        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://getspeecher.com"));
        activity.startActivity(i);

        setVisitedWebPage(true);
    }

    /**
     * Ignores the ad. If this is called enough times, the ad will no longer be shown.
     */
    public void ignoreAd() {
        setIgnoreCount(ignoreCount + 1);

        // After the ad has been shown a few times, stop showing it.
        if (ignoreCount >= MAX_IGNORES) {
            setVisitedWebPage(true);
        }
    }

    private void setAdCount(int count) {
        adCount = count;

        final SharedPreferences.Editor editor = settings.edit();
        editor.putInt("adCount", count);
        editor.commit();

    }

    private void setIgnoreCount(int count) {
        ignoreCount = count;

        final SharedPreferences.Editor editor = settings.edit();
        editor.putInt("ignoreCount", count);
        editor.commit();
    }

    private void setVisitedWebPage(boolean visited) {
        visitedWebpage = visited;
        final SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("visitedWebpage", visitedWebpage);
        editor.commit();
    }
}
