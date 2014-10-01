package edu.psu.rcy5017.publicspeakingassistant.activity;
import java.util.List;

import edu.psu.rcy501.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.datasource.SpeechDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.Speech;

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
	private static final int EDIT_SPEECH_REQUEST_CODE = 1001;
	
    private SpeechDataSource datasource;
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_list);
        
        datasource = new SpeechDataSource(this);
        datasource.open();

        final List<Speech> values = datasource.getAllSpeeches();
        
        // Use the SimpleCursorAdapter to show the elements in a ListView.
        final ArrayAdapter<Speech> adapter = 
        		new ArrayAdapter<Speech>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
       
        // Register the ListView  for Context menu  
        registerForContextMenu(getListView());
    }

    /**
     * Called via the onClick attribute of the buttons in main.xml.
     * @param view the calling view
     */
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        final ArrayAdapter<Speech> adapter = (ArrayAdapter<Speech>) getListAdapter();
    
        switch (view.getId()) {
        
        case R.id.add_speech:
        	final Speech speech = datasource.createSpeech("New Speech");
            // Save the new speech to the database.
            adapter.add(speech);
            // Force user to overwrite the default name.
            renameSpeech(speech, adapter.getCount() - 1);
            
            break;    
        }
        adapter.notifyDataSetChanged();
    }
       
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	final Speech speech = (Speech) getListAdapter().getItem(position);
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
        
        final ArrayAdapter<Speech> adapter = (ArrayAdapter<Speech>) getListAdapter();
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
        
	        case R.id.delete_speech:
	            datasource.deleteSpeech(speech);
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
    		final long newSpeechId = data.getLongExtra("id", RenameSpeechActivity.DEFAULT_LONG_VALUE);
    		final String newSpeechTitle = data.getStringExtra("title");
    		final Speech speech = new Speech(newSpeechId, newSpeechTitle);
    		final int position = data.getIntExtra("position", RenameSpeechActivity.DEFAULT_INT_VALUE);
    		
    		// DEBUG
    		Log.d(TAG, "id: " + speech.getId());
    		Log.d(TAG, "position: " + position);
    		Log.d(TAG, "new title: " + speech.getTitle());
    		
    		final ArrayAdapter<Speech> adapter = (ArrayAdapter<Speech>) getListAdapter();
    		
    		// Get the speech item to update.
    		final Speech speechToUpdate = 
    				adapter.getItem(position);
    		
    		// Update the title.
    		speechToUpdate.setTitle(speech.getTitle());
    		adapter.notifyDataSetChanged();
		}
    }
    
   /**
    * Opens the speech in the main activity view.
    * @param speech the speech to start
    */
    private void startSpeech(Speech speech) {
    	final Intent intent = new Intent(this, MainActivity.class);   
        startActivity(intent);
    }
    
    private void editSpeech(Speech speech) {
    	final Intent intent = new Intent(this, NoteCardListActivity.class);
    	intent.putExtra("id", speech.getId());
    	startActivityForResult(intent, EDIT_SPEECH_REQUEST_CODE);
    }
    
    /**
     * Opens an activity to edit the speech title.
     * @param speech the speech to rename
     */
    private void renameSpeech(Speech speech, int position) {
    	final Intent intent = new Intent(this, RenameSpeechActivity.class);
    	intent.putExtra("position", position );
    	intent.putExtra("id", speech.getId());
    	intent.putExtra("title", speech.getTitle());
    	startActivityForResult(intent, RENAME_SPEECH_REQUEST_CODE);
    }

} 