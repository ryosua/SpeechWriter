package edu.psu.rcy5017.publicspeakingassistant.activity;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
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
import edu.psu.rcy5017.publicspeakingassistant.task.SpeechRecordingTask;

public class SpeechRecordingListActivity extends ListActivity {
    
    private static final String TAG = "SpeechRecordingListActivity";
    
    private ArrayAdapter<SpeechRecording> adapter;
    private SpeechRecordingDataSource datasource;
    private long speechID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recording_list);
        
        datasource = new SpeechRecordingDataSource(this);
        
        // Get the speechID passed from list activity.
        final Intent intent = this.getIntent();
        speechID = intent.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
        
        List<SpeechRecording> values = null;
        try {
            values = new GetSpeechRecordingsTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        
        if(values != null) {
            // Use the SimpleCursorAdapter to show the elements in a ListView.
            adapter = new ArrayAdapter<SpeechRecording>(this, android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
        }
       
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
                 
            // Get the speech item to update.
            final SpeechRecording speechRecordingToUpdate = 
                    adapter.getItem(position);
            
            // Update the title.
            speechRecordingToUpdate.setTitle(speechRecording.getTitle());
            adapter.notifyDataSetChanged();
            
            // Save the changes to the database.
            new RenameSpeechRecordingTask(speechRecordingToUpdate).execute();
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
       
        final SpeechRecording speechRecording = (SpeechRecording) getListAdapter().getItem(info.position);
        
        switch (item.getItemId()) {
            case R.id.play_speech_recording:
                playSpeechRecording(speechRecording);
                return true;
                         
            case R.id.rename_speech_recording:
                renameSpeechRecording(speechRecording, info.position);
                return true;
       
            case R.id.delete_speech_recording:
                new DeleteSpeechRecordingTask(speechRecording).execute();
                return true;
        }
     
        return false;
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
    
    private class GetSpeechRecordingsTask extends AsyncTask<Void, Void, List<SpeechRecording>> {
        
        @Override
        protected List<SpeechRecording> doInBackground(Void... params) {
            datasource.open();
            final List<SpeechRecording> values = datasource.getAllSpeechRecordings(speechID);
            datasource.close();
            
            return values;
        }
        
    }
    
    private class DeleteSpeechRecordingTask extends SpeechRecordingTask {  
        /**
         * Creates a task that deletes a speech in the database
         * @param speech the speech to delete
         */
        public DeleteSpeechRecordingTask(SpeechRecording speechRecording) {
            super(speechRecording);
        }
        
        @Override
        protected Void doInBackground(Void... params) {
            datasource.open();
            datasource.deleteSpeechRecording(getSpeechRecording());
            datasource.close();
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            adapter.remove(getSpeechRecording());
            adapter.notifyDataSetChanged();
        }
        
    }
    
    private class RenameSpeechRecordingTask extends SpeechRecordingTask {
        
        public RenameSpeechRecordingTask(SpeechRecording speechRecording) {
            super(speechRecording);
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
    
}