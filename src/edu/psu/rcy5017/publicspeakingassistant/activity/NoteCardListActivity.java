package edu.psu.rcy5017.publicspeakingassistant.activity;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.ericharlow.DragNDrop.DragNDropAdapter;
import com.ericharlow.DragNDrop.DragNDropListView;

import edu.psu.rcy5017.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.constant.DefaultValues;
import edu.psu.rcy5017.publicspeakingassistant.constant.RequestCodes;
import edu.psu.rcy5017.publicspeakingassistant.datasource.NoteCardDataSource;
import edu.psu.rcy5017.publicspeakingassistant.listener.DragListenerImpl;
import edu.psu.rcy5017.publicspeakingassistant.listener.DropReorderListener;
import edu.psu.rcy5017.publicspeakingassistant.listener.RemoveListenerImpl;
import edu.psu.rcy5017.publicspeakingassistant.model.NoteCard;
import edu.psu.rcy5017.publicspeakingassistant.task.CreateNoteCardTask;
import edu.psu.rcy5017.publicspeakingassistant.task.DeleteTask;
import edu.psu.rcy5017.publicspeakingassistant.task.GetAllTask;
import edu.psu.rcy5017.publicspeakingassistant.task.RenameNoteCardTask;
import edu.psu.rcy5017.publicspeakingassistant.task.UpdateOrderTask;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class NoteCardListActivity extends ListActivity {
    
    private static final String TAG = "NoteCardListActivity";
    
    private DragNDropAdapter<NoteCard> adapter;
    private NoteCardDataSource datasource;
    private long speechID;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notecard_list);
        
        datasource = new NoteCardDataSource(this);
        
        // Get the speechID passed from list activity.
        final Intent intent = this.getIntent();
        speechID = intent.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
       
        try {
            final List<NoteCard> values = new GetAllTask<NoteCard>(datasource, speechID).execute().get();
           
            adapter = new DragNDropAdapter<NoteCard>(this, new int[]{R.layout.dragitem}, new int[]{R.id.TextView01}, values);
            setListAdapter(adapter);
            
            final ListView listView = getListView();
            if (listView instanceof DragNDropListView) {
                ((DragNDropListView) listView).setDropListener(new DropReorderListener<NoteCard>(adapter, datasource, listView));
                ((DragNDropListView) listView).setRemoveListener(new RemoveListenerImpl<NoteCard>(adapter, listView));
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
    
    /**
     * Called via the onClick attribute of the buttons in xml file.
     * @param view the calling view
     */
    public void onClick(View view) {
        switch (view.getId()) {
        
        case R.id.add_note_card:
            
            try {
                final NoteCard noteCard  = new CreateNoteCardTask(datasource, speechID).execute().get();
                // Add the note card to the adapter.
                adapter.add(noteCard);
                
                // Update the order of the note card.
                new UpdateOrderTask<NoteCard>(datasource, noteCard, adapter.getCount());
                
                // Force user to overwrite the default name.
                renameNoteCard(noteCard, adapter.getCount() - 1);
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
        inflater.inflate(R.menu.notecard_option_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        
        final NoteCard noteCard = (NoteCard) getListAdapter().getItem(info.position);
        
        switch (item.getItemId()) {
                
            case R.id.edit_notecard:
                editNoteCard(noteCard);
                return true;
                
            case R.id.rename_notecard:
                renameNoteCard(noteCard, info.position);
                return true;
        
            case R.id.delete_notecard:
                new DeleteTask<NoteCard>(datasource, noteCard).execute();
                adapter.remove(noteCard);
                adapter.notifyDataSetChanged();
                return true;
        }
     
        return false;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.RENAME_NOTECARD_REQUEST_CODE && resultCode == RESULT_OK) {
            final long newNoteCardId = data.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
            final String newNoteCardTitle = data.getStringExtra("text");
            final NoteCard noteCard = new NoteCard(newNoteCardId, newNoteCardTitle, speechID);
            final int position = data.getIntExtra("position", DefaultValues.DEFAULT_INT_VALUE);
               
            // Get the note card item to update.
            final NoteCard noteCardToUpdate = 
                    adapter.getItem(position);
            
            // Update the title.
            noteCardToUpdate.setTitle(noteCard.getTitle());
            adapter.notifyDataSetChanged();
            
            // Save the changes to the database.
            new RenameNoteCardTask(noteCardToUpdate, datasource).execute();
        }
    }

    private void editNoteCard(NoteCard noteCard) {
        final Intent intent = new Intent(this, NoteListActivity.class);
        intent.putExtra("id", noteCard.getId());
        startActivityForResult(intent, RequestCodes.EDIT_NOTECARD_REQUEST_CODE);
    }

    private void renameNoteCard(NoteCard noteCard, int position) {
        final Intent intent = new Intent(this, EditTextActivity.class);
        intent.putExtra("position", position );
        intent.putExtra("id", noteCard.getId());
        intent.putExtra("text", noteCard.getTitle());
        startActivityForResult(intent, RequestCodes.RENAME_NOTECARD_REQUEST_CODE);
    }
     
}