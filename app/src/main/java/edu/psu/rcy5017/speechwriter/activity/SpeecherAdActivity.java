package edu.psu.rcy5017.speechwriter.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import edu.psu.rcy5017.speechwriter.R;
import edu.psu.rcy5017.speechwriter.constant.MixPanelCodes;

public class SpeecherAdActivity extends Activity {

    private static final String TAG = "SpeecherAdActivity";

    private MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speecher_ad);

        mixpanel = MixpanelAPI.getInstance(this.getApplicationContext(), MixPanelCodes.MIXPANEL_TOKEN);

        mixpanel.track("Speecher Ad Impression");

        final View.OnClickListener clickListener = new SpeecherAdButtonListener();

        final Button speecherButton = (Button) findViewById(R.id.go_to_speecher_button);
        speecherButton.setOnClickListener(clickListener);

        final Button speechWriterButton = (Button) findViewById(R.id.use_speech_writer_button);
        speechWriterButton.setOnClickListener(clickListener);
    }

    private class SpeecherAdButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick called.");

            switch (v.getId()) {
                case R.id.go_to_speecher_button:
                    Log.d(TAG, "Go to Speecher in the app store.");
                    final String speecherPackagage = "com.yosuatreegames.speecher";

                    mixpanel.track("Speecher Link Followed");

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + speecherPackagage)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + speecherPackagage)));
                    }

                    break;

                case R.id.use_speech_writer_button:
                    Log.d(TAG, "Just use Speech Writer.");

                    mixpanel.track("Speecher Link Declined");

                    Intent speechListIntent = new Intent(SpeecherAdActivity.this, SpeechListActivity.class);
                    startActivity(speechListIntent);
                    break;
            }
        }
    }
}
