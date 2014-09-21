package edu.psu.rcy5017.publicspeakingassistant.activity;
import java.util.List;

import edu.psu.rcy501.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.SpeechListDataSource;
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
	
    private SpeechListDataSource datasource;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_list);

        datasource = new SpeechListDataSource(this);
        datasource.open();

        List<NoteCardListDBTest> values = datasource.getAllNoteCardListDBTests();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<NoteCardListDBTest> adapter = new ArrayAdapter<NoteCardListDBTest>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
        
        // Register the ListView  for Context menu  
        registerForContextMenu(getListView());
        
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<NoteCardListDBTest> adapter = (ArrayAdapter<NoteCardListDBTest>) getListAdapter();
        NoteCardListDBTest speech = null;
        
        switch (view.getId()) {
        
        case R.id.add:
            // save the new speech to the database
        	
            speech = datasource.createNoteCardListDBTest("New Speech" + adapter.getCount());
            adapter.add(speech);
            
            break;    
        }
        adapter.notifyDataSetChanged();
    }
       
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	NoteCardListDBTest speech = (NoteCardListDBTest) getListAdapter().getItem(position);
    	startSpeech(speech);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.speech_option_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        
        ArrayAdapter<NoteCardListDBTest> adapter = (ArrayAdapter<NoteCardListDBTest>) getListAdapter();
        NoteCardListDBTest speech = null;
     
        switch (item.getItemId()) {
        case R.id.delete_speech:
        	speech = (NoteCardListDBTest) getListAdapter().getItem(info.position);
            datasource.deleteNoteCardListDBTest(speech);
            adapter.remove(speech);
            adapter.notifyDataSetChanged();
          
            return true;
        
        case R.id.start_speech:
	    	speech = (NoteCardListDBTest) getListAdapter().getItem(info.position);
	    	
	    	startSpeech(speech);
	      
	        return true;
	        
        case R.id.rename_speech:
	    	speech = (NoteCardListDBTest) getListAdapter().getItem(info.position);
	    	
	    	renameSpeech(speech);
	      
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
    
   /**
    * Opens the speech in the main activity view.
    * @param speech the speech to start
    */
    private void startSpeech(NoteCardListDBTest speech) {
    	Intent intent = new Intent(this, MainActivity.class);
        //tmpIntent.putExtra(SHOWITEMINTENT_EXTRA_FETCHROWID, position);
        //startActivityForResult(tmpIntent, ACTIVITY_SHOWITEM);
        
        startActivity(intent);
        // close this activity
        finish();
    }
    
    /**
     * Opens an activity to edit the speech title.
     * @param speech the speech to rename
     */
    private void renameSpeech(NoteCardListDBTest speech) {
    	Intent intent = new Intent(this, RenameSpeechActivity.class);
    	intent.putExtra("key", speech.getId());
    	intent.putExtra("title", speech.getTitle());
    	startActivityForResult(intent, RENAME_SPEECH_REQUEST_CODE);
    }

} 