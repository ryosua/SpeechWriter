package edu.psu.rcy5017.publicspeakingassistant.activity;

import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.psu.rcy5017.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.constant.DefaultValues;
import edu.psu.rcy5017.publicspeakingassistant.constant.RequestCodes;
import edu.psu.rcy5017.publicspeakingassistant.datasource.NoteDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.Note;
import edu.psu.rcy5017.publicspeakingassistant.task.NoteTask;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
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
    
    private ArrayAdapter<Note> adapter;
    private NoteDataSource datasource;
    private long noteCardID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        
        datasource = new NoteDataSource(this);
        
        // Get the note card id passed from list activity.
        final Intent intent = this.getIntent();
        noteCardID = intent.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
       
        List<Note> values = null;
        try {
            values = new GetNotesTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        
        if (values != null) {
            adapter = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1, values);
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
        // Get the speechID passed from list activity.
        final Intent intent = this.getIntent();
       
        switch (view.getId()) {
        
        case R.id.add_note: 
            // Create and save the new notecard to the database.
            try {
                new CreateNoteTask().execute().get();
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
        inflater.inflate(R.menu.note_option_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
 
        final Note note = (Note) getListAdapter().getItem(info.position);
        
        switch (item.getItemId()) {
                
            case R.id.edit_note:
                editNote(note, info.position);
                return true;
            
            case R.id.delete_note:
                new DeleteNoteTask(note).execute();
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
              
            // Get the note card item to update.
            final Note noteToUpdate = 
                    adapter.getItem(position);
            
            // Update the title.
            noteToUpdate.setText(note.getText());
            adapter.notifyDataSetChanged();
            
            // Save the changes to the database.
            new ChangeNoteTextTask(noteToUpdate).execute();
        }
    }
    
    private void editNote(Note note, int position) {
        final Intent intent = new Intent(this, EditTextActivity.class);
        intent.putExtra("position", position );
        intent.putExtra("id", note.getId());
        intent.putExtra("text", note.getText());
        startActivityForResult(intent, RequestCodes.EDIT_NOTE_REQUEST_CODE);
    }
    
    private class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {
        
        @Override
        protected List<Note> doInBackground(Void... params) {
            datasource.open();
            final List<Note> values = datasource.getAllNotes(noteCardID);
            datasource.close();
            
            return values;
        }
        
    }
    
    private class CreateNoteTask extends AsyncTask<Void, Void, Note> {
        
        private Note note;

        @Override
        protected Note doInBackground(Void... params) {
            datasource.open();
            note = datasource.createNote("New Note", noteCardID);
            datasource.close();
            return note;
        }
        
        @Override
        protected void onPostExecute(Note result) {
            // Save the new note to the database.
            adapter.add(note);
            // Force user to overwrite the default text.
            editNote(note, adapter.getCount() - 1);
            adapter.notifyDataSetChanged();
        }
        
    }
    
    private class ChangeNoteTextTask extends NoteTask {
        
        public ChangeNoteTextTask(Note note) {
            super(note);
        }

        @Override
        protected Void doInBackground(Void... params) {
            final Note note = getNote();
            datasource.open();
            datasource.changeNoteText(note, note.getText());
            datasource.close();
            return null;
        }
        
    }
    
    private class DeleteNoteTask extends NoteTask {  
        
        public DeleteNoteTask(Note note) {
            super(note);
        }

        @Override
        protected Void doInBackground(Void... params) {
            datasource.open();
            datasource.deleteNote(getNote());
            datasource.close();
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            adapter.remove(getNote());
            adapter.notifyDataSetChanged();
        }
        
    }
    
}