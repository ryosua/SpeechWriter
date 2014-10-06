package edu.psu.rcy5017.publicspeakingassistant.activity;

import java.util.List;

import edu.psu.rcy501.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.constant.DefaultValues;
import edu.psu.rcy5017.publicspeakingassistant.datasource.NoteCardDataSource;
import edu.psu.rcy5017.publicspeakingassistant.datasource.NoteDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.Note;
import edu.psu.rcy5017.publicspeakingassistant.model.NoteCard;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class NoteListActivity extends ListActivity {
	
	private static final String TAG = "NoteListActivity";
	
	private NoteDataSource datasource;
    private long noteCardID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notecard_list);
        
        datasource = new NoteDataSource(this);
        datasource.open();
        
        // Get the note card id passed from list activity.
        final Intent intent = this.getIntent();
        noteCardID = intent.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
        
        //TODO
        /*
        final List<Note> values = datasource.getAllNotes(noteCardID);
        
    	// Use the SimpleCursorAdapter to show the elements in a ListView.
        final ArrayAdapter<Note> adapter = 
        		new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
        */
        
        // Register the ListView  for Context menu  
        registerForContextMenu(getListView());
    }
	
}