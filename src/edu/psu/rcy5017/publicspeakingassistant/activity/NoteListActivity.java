package edu.psu.rcy5017.publicspeakingassistant.activity;

import java.util.List;

import edu.psu.rcy501.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.constant.DefaultValues;
import edu.psu.rcy5017.publicspeakingassistant.constant.RequestCodes;
import edu.psu.rcy5017.publicspeakingassistant.datasource.NoteDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.Note;
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
     * Called via the onClick attribute of the buttons in xml file.
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
            editNote(note, adapter.getCount() - 1);
            break;    
        }
        adapter.notifyDataSetChanged();
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
        inflater.inflate(R.menu.note_option_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        
        @SuppressWarnings("unchecked")
        final ArrayAdapter<Note> adapter = (ArrayAdapter<Note>) getListAdapter();
        final Note note = (Note) getListAdapter().getItem(info.position);
        
        switch (item.getItemId()) {
                
            case R.id.edit_note:
                editNote(note, info.position);
                return true;
            
            case R.id.delete_note:
                datasource.deleteNote(note);
                adapter.remove(note);
                adapter.notifyDataSetChanged();
                return true;
        }
     
        return false;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.EDIT_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {
            final long newNoteId = data.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
            final String newNoteText = data.getStringExtra("text");
            final Note note = new Note(newNoteId, noteCardID, newNoteText);
            final int position = data.getIntExtra("position", DefaultValues.DEFAULT_INT_VALUE);
            
            @SuppressWarnings("unchecked")
            final ArrayAdapter<Note> adapter = (ArrayAdapter<Note>) getListAdapter();
            
            // Get the note card item to update.
            final Note noteToUpdate = 
                    adapter.getItem(position);
            
            // Update the title.
            noteToUpdate.setText(note.getText());
            adapter.notifyDataSetChanged();
            
            // Save the changes to the database.
            datasource.open();
            datasource.changeNoteText(note, note.getText());
        }
    }
    
    private void editNote(Note note, int position) {
        final Intent intent = new Intent(this, EditTextActivity.class);
        intent.putExtra("position", position );
        intent.putExtra("id", note.getId());
        intent.putExtra("text", note.getText());
        startActivityForResult(intent, RequestCodes.EDIT_NOTE_REQUEST_CODE);
    }
    
}