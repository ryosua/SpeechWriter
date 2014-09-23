package edu.psu.rcy5017.publicspeakingassistant.activity;
import java.util.List;

import edu.psu.rcy501.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.SpeechDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.NoteCardListDBTest;

import android.app.ListActivity;
import android.content.Intent;
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
	
	// Request codes
	private static final int RENAME_SPEECH_REQUEST_CODE = 1001;
	
    private SpeechDataSource datasource;
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_list);
        
        datasource = new SpeechDataSource(this);
        datasource.open();

        final List<NoteCardListDBTest> values = datasource.getAllNoteCardListDBTests();
        
    	// use the SimpleCursorAdapter to show the
        // elements in a ListView
        final ArrayAdapter<NoteCardListDBTest> adapter = 
        		new ArrayAdapter<NoteCardListDBTest>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
       
        // Register the ListView  for Context menu  
        registerForContextMenu(getListView());
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        final ArrayAdapter<NoteCardListDBTest> adapter = (ArrayAdapter<NoteCardListDBTest>) getListAdapter();
        final NoteCardListDBTest speech = datasource.createNoteCardListDBTest("New Speech" + adapter.getCount());
        
        switch (view.getId()) {
        
        case R.id.add:
            // Save the new speech to the database.
            adapter.add(speech);
            
            break;    
        }
        adapter.notifyDataSetChanged();
    }
       
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	final NoteCardListDBTest speech = (NoteCardListDBTest) getListAdapter().getItem(position);
    	startSpeech(speech);
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
        
        final ArrayAdapter<NoteCardListDBTest> adapter = (ArrayAdapter<NoteCardListDBTest>) getListAdapter();
        final NoteCardListDBTest speech = (NoteCardListDBTest) getListAdapter().getItem(info.position);
        
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
        
	        case R.id.delete_speech:
	            datasource.deleteNoteCardListDBTest(speech);
	            adapter.remove(speech);
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == RENAME_SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
    		final NoteCardListDBTest speech = new NoteCardListDBTest();
    		speech.setId(data.getLongExtra("id", RenameSpeechActivity.DEFAULT_LONG_VALUE));
    		speech.setTitle(data.getStringExtra("title"));
    		final int position = data.getIntExtra("position", RenameSpeechActivity.DEFAULT_INT_VALUE);
    		
    		// DEBUG
    		Log.d(TAG, "id: " + speech.getId());
    		Log.d(TAG, "position: " + position);
    		Log.d(TAG, "new title: " + speech.getTitle());
    		
    		final ArrayAdapter<NoteCardListDBTest> adapter = (ArrayAdapter<NoteCardListDBTest>) getListAdapter();
    		
    		// Get the speech item to update.
    		final NoteCardListDBTest speechToUpdate = 
    				adapter.getItem(position);
    		
    		// Update database record using adapter.
    		speechToUpdate.setTitle(speech.getTitle());
    		adapter.notifyDataSetChanged();
		}
    }
    
   /**
    * Opens the speech in the main activity view.
    * @param speech the speech to start
    */
    private void startSpeech(NoteCardListDBTest speech) {
    	final Intent intent = new Intent(this, MainActivity.class);   
        startActivity(intent);
    }
    
    private void editSpeech(NoteCardListDBTest speech) {
    	final Intent intent = new Intent(this, EditSpeechActivity.class);   
        startActivity(intent);
    }
    
    /**
     * Opens an activity to edit the speech title.
     * @param speech the speech to rename
     */
    private void renameSpeech(NoteCardListDBTest speech, int position) {
    	final Intent intent = new Intent(this, RenameSpeechActivity.class);
    	intent.putExtra("position", position );
    	intent.putExtra("id", speech.getId());
    	intent.putExtra("title", speech.getTitle());
    	startActivityForResult(intent, RENAME_SPEECH_REQUEST_CODE);
    }

} 