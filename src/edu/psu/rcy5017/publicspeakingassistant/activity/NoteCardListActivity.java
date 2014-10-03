package edu.psu.rcy5017.publicspeakingassistant.activity;

import java.util.List;

import edu.psu.rcy501.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.datasource.NoteCardDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.NoteCard;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class NoteCardListActivity extends ListActivity {
	
	private static final String TAG = "NoteCardListActivity";
	
	// Request codes
	private static final int RENAME_NOTECARD_REQUEST_CODE = 1002;
	
    private NoteCardDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notecard_list);
        
        datasource = new NoteCardDataSource(this);
        datasource.open();
        
        // Get the speechID passed from list activity.
        final Intent intent = this.getIntent();
        final long speechID = intent.getLongExtra("id", EditTextActivity.DEFAULT_LONG_VALUE);
        
        final List<NoteCard> values = datasource.getAllNoteCards(speechID);
        
    	// Use the SimpleCursorAdapter to show the elements in a ListView.
        final ArrayAdapter<NoteCard> adapter = 
        		new ArrayAdapter<NoteCard>(this, android.R.layout.simple_list_item_1, values);
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
		final ArrayAdapter<NoteCard> adapter = (ArrayAdapter<NoteCard>) getListAdapter();
    	
    	// Get the speechID passed from list activity.
        final Intent intent = this.getIntent();
       
        switch (view.getId()) {
        
        case R.id.add_note_card:
        	
        	final long speechID = intent.getLongExtra("id", EditTextActivity.DEFAULT_LONG_VALUE);
        	final NoteCard noteCard = datasource.createNoteCard("New Note Card", speechID);
            // Save the new notecard to the database.
            adapter.add(noteCard);
            // Force user to overwrite the default name.
            //renameNoteCard(noteCard, adapter.getCount() - 1);
            
            break;    
        }
        adapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	final NoteCard noteCard = (NoteCard) getListAdapter().getItem(position);
    	//TODO: Edit Speech
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notecard_option_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        
        final ArrayAdapter<NoteCard> adapter = (ArrayAdapter<NoteCard>) getListAdapter();
        final NoteCard noteCard = (NoteCard) getListAdapter().getItem(info.position);
        
        switch (item.getItemId()) {
        		
        	case R.id.edit_notecard:
		    	editNoteCard(noteCard);
		    	return true;
		    	
        	case R.id.rename_notecard:
		    	renameNoteCard(noteCard, info.position);
		        return true;
        
	        case R.id.delete_notecard:
	            datasource.deleteNoteCard(noteCard);
	            adapter.remove(noteCard);
	            adapter.notifyDataSetChanged();
	            return true;
        }
     
      	return false;
    }

	private void editNoteCard(NoteCard noteCard) {
		Log.d(TAG, "TODO: Edit Note Card");
	}

	private void renameNoteCard(NoteCard noteCard, int position) {
		Log.d(TAG, "TODO: Rename Note Card");
	}
}