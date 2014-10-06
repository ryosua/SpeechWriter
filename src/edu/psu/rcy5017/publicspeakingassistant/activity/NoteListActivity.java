package edu.psu.rcy5017.publicspeakingassistant.activity;

import java.util.List;

import edu.psu.rcy501.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.constant.DefaultValues;
import edu.psu.rcy5017.publicspeakingassistant.datasource.NoteDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.Note;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

public class NoteListActivity extends ListActivity {
	
	private static final String TAG = "NoteListActivity";
	
	private NoteDataSource datasource;
    private long noteCardID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        
        datasource = new NoteDataSource(this);
        datasource.open();
        
        // Get the note card id passed from list activity.
        final Intent intent = this.getIntent();
        noteCardID = intent.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
       
        final List<Note> values = datasource.getAllNotes(noteCardID);
        
    	// Use the SimpleCursorAdapter to show the elements in a ListView.
        final ArrayAdapter<Note> adapter = 
        		new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1, values);
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
		final ArrayAdapter<Note> adapter = (ArrayAdapter<Note>) getListAdapter();
    	
    	// Get the speechID passed from list activity.
        final Intent intent = this.getIntent();
       
        switch (view.getId()) {
        
        case R.id.add_note:
        	Log.d(TAG, "TODO: Add note.");
        	final long noteCardID = intent.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
        	final Note note = datasource.createNote("New Note", noteCardID);
            // Save the new notecard to the database.
            adapter.add(note);
            //TODO: Force user to overwrite the default name.
            //renameNoteCard(note, adapter.getCount() - 1);
            break;    
        }
        adapter.notifyDataSetChanged();
    }
	
}