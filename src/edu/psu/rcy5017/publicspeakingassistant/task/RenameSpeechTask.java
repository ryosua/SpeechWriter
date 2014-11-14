package edu.psu.rcy5017.publicspeakingassistant.task;

import edu.psu.rcy5017.publicspeakingassistant.datasource.SpeechDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.Speech;

public class RenameSpeechTask extends SpeechTask {
    
    private final SpeechDataSource datasource;
    
    public RenameSpeechTask(SpeechDataSource datasource, Speech speech) {
        super(speech);
        this.datasource = datasource;
    }

    @Override
    protected Void doInBackground(Void... params) {
        final Speech speech = getSpeech();
        datasource.open();
        datasource.renameSpeech(speech, speech.getTitle());
        datasource.close();
        return null;
    }
}
