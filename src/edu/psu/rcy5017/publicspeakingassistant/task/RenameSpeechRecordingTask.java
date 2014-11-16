package edu.psu.rcy5017.publicspeakingassistant.task;

import edu.psu.rcy5017.publicspeakingassistant.datasource.SpeechRecordingDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.SpeechRecording;

public class RenameSpeechRecordingTask extends SpeechRecordingTask {
    
    private final SpeechRecordingDataSource datasource;
    
    public RenameSpeechRecordingTask(SpeechRecordingDataSource datasource, SpeechRecording speechRecording) {
        super(speechRecording);
        this.datasource = datasource;
    }

    @Override
    protected Void doInBackground(Void... params) {
        final SpeechRecording speechRecording = getSpeechRecording();
        datasource.open();
        datasource.renameSpeechRecording(speechRecording, speechRecording.getTitle());
        datasource.close();
        return null;
    }
}