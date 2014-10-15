package edu.psu.rcy5017.publicspeakingassistant.activity;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import edu.psu.rcy5017.publicspeakingassistant.AudioCntl;
import edu.psu.rcy5017.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.constant.DefaultValues;
import edu.psu.rcy5017.publicspeakingassistant.constant.RequestCodes;
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.RENAME_SPEECH_RECORDING_REQUEST_CODE && resultCode == RESULT_OK) {
            final long newSpeechRecordingId = data.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
            final String newSpeechRecordingTitle = data.getStringExtra("text");
            final SpeechRecording speechRecording = new SpeechRecording(newSpeechRecordingId, newSpeechRecordingTitle, speechID);
            final int position = data.getIntExtra("position", DefaultValues.DEFAULT_INT_VALUE);
            
            @SuppressWarnings("unchecked")
            final ArrayAdapter<SpeechRecording> adapter = (ArrayAdapter<SpeechRecording>) getListAdapter();
            
            // Get the speech item to update.
            final SpeechRecording speechRecordingToUpdate = 
                    adapter.getItem(position);
            
            // Update the title.
            speechRecordingToUpdate.setTitle(speechRecording.getTitle());
            adapter.notifyDataSetChanged();
            
            // Save the changes to the database.
            datasource.open();
            datasource.renameSpeechRecording(speechRecording, speechRecording.getTitle());
        }
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        v.showContextMenu();
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.speech_recording_option_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        
        @SuppressWarnings("unchecked")
        final ArrayAdapter<SpeechRecording> adapter = (ArrayAdapter<SpeechRecording>) getListAdapter();
        final SpeechRecording speechRecording = (SpeechRecording) getListAdapter().getItem(info.position);
        
        switch (item.getItemId()) {
            case R.id.play_speech_recording:
                playSpeechRecording(speechRecording);
                return true;
                         
            case R.id.rename_speech_recording:
                renameSpeechRecording(speechRecording, info.position);
                return true;
       
            case R.id.delete_speech_recording:
                datasource.deleteSpeechRecording(speechRecording);
                adapter.remove(speechRecording);
                adapter.notifyDataSetChanged();
                return true;
        }
     
        return false;
    }
   
    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
    
    private void playSpeechRecording(SpeechRecording speechRecording) {
        final AudioCntl audioCntl = AudioCntl.INSTANCE;
        audioCntl.startPlaying(speechRecording.getFile());
    }
    
    /**
     * Opens an activity to edit the speech recording title.
     * @param speechRecording the speech recording to rename
     */
    private void renameSpeechRecording(SpeechRecording speechRecording, int position) {
        final Intent intent = new Intent(this, EditTextActivity.class);
        intent.putExtra("position", position );
        intent.putExtra("id", speechRecording.getId());
        intent.putExtra("text", speechRecording.getTitle());
        startActivityForResult(intent, RequestCodes.RENAME_SPEECH_RECORDING_REQUEST_CODE);
    }

}