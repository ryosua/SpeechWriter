package edu.psu.rcy5017.publicspeakingassistant.activity;

import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.psu.rcy5017.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.constant.DefaultValues;
import edu.psu.rcy5017.publicspeakingassistant.constant.RequestCodes;
import edu.psu.rcy5017.publicspeakingassistant.datasource.SpeechDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.Speech;
import edu.psu.rcy5017.publicspeakingassistant.task.SpeechTask;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SpeechListActivity extends ListActivity {
    
    private static final String TAG = "SpeechListActivity";
    
    private ArrayAdapter<Speech> adapter;
    private SpeechDataSource datasource;
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_list);
        
        datasource = new SpeechDataSource(this);
        
        @SuppressWarnings("unchecked")
        List<Speech> values = null;
        try {
            values = new GetSpeechesTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        
        if(values != null) {
            // Use the SimpleCursorAdapter to show the elements in a ListView.
            adapter = new ArrayAdapter<Speech>(this, android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
        }
        
        // Register the ListView  for Context menu  
        registerForContextMenu(getListView());
        
    }

    /**
     * Called via the onClick attribute of the buttons in xml file.
     * @param view the calling view
     */
    public void onClick(View view) {
    
        switch (view.getId()) {
        
        case R.id.add_speech:
            
            Speech speech = null;
            try {
                speech = new CreateSpeechTask().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            
            break;    
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
        inflater.inflate(R.menu.speech_option_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        
        final Speech speech = (Speech) getListAdapter().getItem(info.position);
        
        switch (item.getItemId()) {
            case R.id.start_speech:
                startSpeech(speech);
                return true;
                
            case R.id.edit_speech:
                editSpeech(speech);
                return true;
                
            case R.id.rename_speech:
                renameSpeech(speech, info.position);
                return true;
                
            case R.id.speech_recordings:
                viewSpeechRecordings(speech);
                return true;
        
            case R.id.delete_speech:
                // Delete the speech, and update the adapter.
                new DeleteSpeechTask(speech).execute();
                return true;
        }
     
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.RENAME_SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            final long newSpeechId = data.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
            final String newSpeechTitle = data.getStringExtra("text");
            final Speech speech = new Speech(newSpeechId, newSpeechTitle);
            final int position = data.getIntExtra("position", DefaultValues.DEFAULT_INT_VALUE);
                
            // Get the speech item to update.
            final Speech speechToUpdate = 
                    adapter.getItem(position);
            
            // Update the title.
            speechToUpdate.setTitle(speech.getTitle());
            adapter.notifyDataSetChanged();
                
            // Save the changes to the database.
            new RenameSpeechTask(speech).execute();
        }
    }
    
   /**
    * Opens the speech in the main activity view.
    * @param speech the speech to start
    */
    private void startSpeech(Speech speech) {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("id", speech.getId());
        startActivityForResult(intent, RequestCodes.START_SPEECH_REQUEST_CODE);
    }
    
    /**
     * Opens the note card list activity.
     * @param speech the speech to edit
     */
    private void editSpeech(Speech speech) {
        final Intent intent = new Intent(this, NoteCardListActivity.class);
        intent.putExtra("id", speech.getId());
        startActivityForResult(intent, RequestCodes.EDIT_SPEECH_REQUEST_CODE);
    }
    
    /**
     * Opens the speech recordings list activity.
     * @param speech the speech to edit
     */
    private void viewSpeechRecordings(Speech speech) {
        final Intent intent = new Intent(this, SpeechRecordingListActivity.class);
        intent.putExtra("id", speech.getId());
        startActivityForResult(intent, RequestCodes.VIEW_SPEECH_RECORDINGS_REQUEST_CODE);
    }
    
    /**
     * Opens an activity to edit the speech title.
     * @param speech the speech to rename
     */
    private void renameSpeech(Speech speech, int position) {
        final Intent intent = new Intent(this, EditTextActivity.class);
        intent.putExtra("position", position );
        intent.putExtra("id", speech.getId());
        intent.putExtra("text", speech.getTitle());
        startActivityForResult(intent, RequestCodes.RENAME_SPEECH_REQUEST_CODE);
    }
    
    private class CreateSpeechTask extends AsyncTask<Void, Void, Speech> {
        
        private Speech speech;

        @Override
        protected Speech doInBackground(Void... params) {
            datasource.open();
            speech = datasource.createSpeech("New Speech");
            datasource.close();
            return speech;
        }
        
        @Override
        protected void onPostExecute(Speech result) {
            // Save the new speech to the database.
            adapter.add(speech);
            // Force user to overwrite the default name.
            renameSpeech(speech, adapter.getCount() - 1);
            adapter.notifyDataSetChanged();
        }
        
    }
    
    private class DeleteSpeechTask extends SpeechTask
    {  
        /**
         * Creates a task that deletes a speech in the database
         * @param speech the speech to delete
         */
        public DeleteSpeechTask(Speech speech) {
            super(speech);
        }
        
        @Override
        protected Void doInBackground(Void... params) {
            datasource.open();
            datasource.deleteSpeech(getSpeech());
            datasource.close();
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            adapter.remove(getSpeech());
            adapter.notifyDataSetChanged();
        }
    }
    
    private class RenameSpeechTask extends SpeechTask {
        
        public RenameSpeechTask(Speech speech) {
            super(speech);
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
    
    private class GetSpeechesTask extends AsyncTask<Void, Void, List<Speech>> {
        
        @Override
        protected List<Speech> doInBackground(Void... params) {
            datasource.open();
            final List<Speech> values = datasource.getAllSpeeches();
            datasource.close();
            
            return values;
        }
        
    }
   
} 