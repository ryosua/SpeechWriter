package edu.psu.rcy5017.publicspeakingassistant.task;

import edu.psu.rcy5017.publicspeakingassistant.model.Speech;
import android.os.AsyncTask;

/**
 * A task that does something with a speech, but does not need to return any results, or update progress.
 * @author Ryan Yosua
 *
 */
public class SpeechTask extends AsyncTask<Void, Void, Void> {
    
    private final Speech speech;
    
    public SpeechTask(Speech speech) {
        this.speech = speech;
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }
    
    public Speech getSpeech() {
        return speech;
    }

}