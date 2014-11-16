package edu.psu.rcy5017.publicspeakingassistant.task;

import java.util.Date;

import android.os.AsyncTask;
import edu.psu.rcy5017.publicspeakingassistant.datasource.SpeechRecordingDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.SpeechRecording;

public class CreateSpeechRecordingTask extends AsyncTask<Void, Void, SpeechRecording> {
    
    private final SpeechRecordingDataSource datasource;
    private final long speechID;
    
    public CreateSpeechRecordingTask(SpeechRecordingDataSource datasource, long speechID) {
        this.datasource = datasource;
        this.speechID = speechID;
    }

    @Override
    protected SpeechRecording doInBackground(Void... params) {
        datasource.open();
        final SpeechRecording speechRecording = datasource.createSpeechRecording(new Date().toString(), speechID);
        datasource.close();
        
        return speechRecording;
    }

}