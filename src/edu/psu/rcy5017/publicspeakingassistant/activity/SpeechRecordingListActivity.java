package edu.psu.rcy5017.publicspeakingassistant.activity;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import edu.psu.rcy501.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.constant.DefaultValues;
import edu.psu.rcy5017.publicspeakingassistant.datasource.SpeechRecordingDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.SpeechRecording;

public class SpeechRecordingListActivity extends ListActivity {
    
    private static final String TAG = "SpeechRecordingListActivity";
    
    private SpeechRecordingDataSource datasource;
    private long speechID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recording_list);
        
        datasource = new SpeechRecordingDataSource(this);
        datasource.open();
        
        // Get the speechID passed from list activity.
        final Intent intent = this.getIntent();
        speechID = intent.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
        
        final List<SpeechRecording> values = datasource.getAllSpeechRecordings(speechID);
        
        // Use the SimpleCursorAdapter to show the elements in a ListView.
        final ArrayAdapter<SpeechRecording> adapter = 
                new ArrayAdapter<SpeechRecording>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
       
        // Register the ListView  for Context menu  
        registerForContextMenu(getListView());
    }

}