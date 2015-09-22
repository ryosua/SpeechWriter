package edu.psu.rcy5017.speechwriter.activity;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.ericharlow.DragNDrop.DragNDropAdapter;
import com.ericharlow.DragNDrop.DragNDropListView;

import edu.psu.rcy5017.speechwriter.R;
import edu.psu.rcy5017.speechwriter.constant.DefaultValues;
import edu.psu.rcy5017.speechwriter.constant.RequestCodes;
import edu.psu.rcy5017.speechwriter.controller.OptionsCntl;
import edu.psu.rcy5017.speechwriter.datasource.NoteDataSource;
import edu.psu.rcy5017.speechwriter.listener.DragListenerImpl;
import edu.psu.rcy5017.speechwriter.listener.DropReorderListener;
import edu.psu.rcy5017.speechwriter.listener.RemoveListenerImpl;
import edu.psu.rcy5017.speechwriter.model.Note;
import edu.psu.rcy5017.speechwriter.task.ChangeNoteTextTask;
import edu.psu.rcy5017.speechwriter.task.CreateNoteTask;
import edu.psu.rcy5017.speechwriter.task.DeleteTask;
import edu.psu.rcy5017.speechwriter.task.GetAllTask;
import edu.psu.rcy5017.speechwriter.task.UpdateOrderTask;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class NoteListActivity extends ListActivity {
    
    private static final String TAG = "NoteListActivity";
    
    private DragNDropAdapter<Note> adapter;
    private NoteDataSource datasource;
    private long noteCardID;
    private final OptionsCntl optionsCntl = OptionsCntl.INSTANCE;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        
        datasource = new NoteDataSource(this);
        
        // Get the note card id passed from list activity.
        final Intent intent = this.getIntent();
        noteCardID = intent.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
       
        try {
            final List<Note> values = new GetAllTask<Note>(datasource, noteCardID).execute().get();
            
            adapter = new DragNDropAdapter<Note>(this, new int[]{R.layout.dragitem}, new int[]{R.id.TextView01}, values);
            
            setListAdapter(adapter);
            
            final ListView listView = getListView();
            if (listView instanceof DragNDropListView) {
                ((DragNDropListView) listView).setDropListener(new DropReorderListener<Note>(adapter, datasource, listView));
                ((DragNDropListView) listView).setRemoveListener(new RemoveListenerImpl<Note>(adapter, listView));
                ((DragNDropListView) listView).setDragListener(new DragListenerImpl());
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
      
        
        // Register the ListView  for Context menu  
        registerForContextMenu(getListView());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * Called via the onClick attribute of the buttons in xml file.
     * @param view the calling view
     */
    public void onClick(View view) {
        switch (view.getId()) {
        
        case R.id.add_note:
            try {
                // Create and save the new note to the database.
                final Note note = new CreateNoteTask(datasource, noteCardID).execute().get();
                
                // Update the order of the note.
                new UpdateOrderTask<Note>(datasource, note, adapter.getCount());
                
                // Save the new note to the database.
                adapter.add(note);
                // Force user to overwrite the default text.
                editNote(note, adapter.getCount() - 1);
                adapter.notifyDataSetChanged();
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
                new DeleteTask<Note>(datasource, note).execute();
                adapter.remove(note);
                adapter.notifyDataSetChanged();
                return true;
                
            case R.id.action_settings:
                Log.d(TAG, "options selected");
                return true;
        }
     
        return false;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Log.d(TAG, "options selected");
            optionsCntl.openOptionsPage(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            new ChangeNoteTextTask(datasource, noteToUpdate).execute();
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