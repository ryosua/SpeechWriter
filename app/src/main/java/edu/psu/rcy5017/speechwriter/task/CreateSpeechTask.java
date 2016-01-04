package edu.psu.rcy5017.speechwriter.task;

import android.app.Activity;
import android.os.AsyncTask;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import edu.psu.rcy5017.speechwriter.constant.MixPanelCodes;
import edu.psu.rcy5017.speechwriter.datasource.SpeechDataSource;
import edu.psu.rcy5017.speechwriter.model.Speech;

public class CreateSpeechTask extends AsyncTask<Void, Void, Speech> {
    
    private Speech speech;
    private final Activity activity;
    private final SpeechDataSource datasource;
    
    public CreateSpeechTask(SpeechDataSource datasource, Activity activity) {
        this.activity = activity;
        this.datasource = datasource;
    }

    @Override
    protected Speech doInBackground(Void... params) {
        datasource.open();
        speech = datasource.createSpeech("New Speech");
        datasource.close();

        // Record activity in mixpanel.
        final MixpanelAPI mixpanel = MixpanelAPI.getInstance(activity.getApplicationContext(), MixPanelCodes.MIXPANEL_TOKEN);
        mixpanel.track("Speech Created");

        return speech;
    }
    
}