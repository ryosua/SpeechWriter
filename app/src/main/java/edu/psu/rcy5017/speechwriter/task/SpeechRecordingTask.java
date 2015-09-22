package edu.psu.rcy5017.speechwriter.task;

import edu.psu.rcy5017.speechwriter.model.SpeechRecording;
import android.os.AsyncTask;

public class SpeechRecordingTask extends AsyncTask<Void, Void, Void> {
    
    private final SpeechRecording speechRecording;
    
    public SpeechRecordingTask(SpeechRecording speechRecording) {
        this.speechRecording = speechRecording;
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }
    
    public SpeechRecording getSpeechRecording() {
        return speechRecording;
    }
  
}