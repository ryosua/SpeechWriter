package edu.psu.rcy5017.speechwriter.task;

import android.os.AsyncTask;
import edu.psu.rcy5017.speechwriter.datasource.SpeechDataSource;
import edu.psu.rcy5017.speechwriter.model.Speech;

public class CreateSpeechTask extends AsyncTask<Void, Void, Speech> {
    
    private Speech speech;
    private final SpeechDataSource datasource;
    
    public CreateSpeechTask(SpeechDataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    protected Speech doInBackground(Void... params) {
        datasource.open();
        speech = datasource.createSpeech("New Speech");
        datasource.close();
        return speech;
    }
    
}