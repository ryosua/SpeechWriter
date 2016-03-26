package edu.psu.rcy5017.speechwriter.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.ericharlow.DragNDrop.DragNDropAdapter;
import com.ericharlow.DragNDrop.DragNDropListView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.psu.rcy5017.speechwriter.R;
import edu.psu.rcy5017.speechwriter.constant.DefaultValues;
import edu.psu.rcy5017.speechwriter.constant.RequestCodes;
import edu.psu.rcy5017.speechwriter.controller.OptionsCntl;
import edu.psu.rcy5017.speechwriter.datasource.SpeechDataSource;
import edu.psu.rcy5017.speechwriter.listener.DragListenerImpl;
import edu.psu.rcy5017.speechwriter.listener.DropReorderListener;
import edu.psu.rcy5017.speechwriter.listener.RemoveListenerImpl;
import edu.psu.rcy5017.speechwriter.model.Speech;
import edu.psu.rcy5017.speechwriter.task.CreateSpeechTask;
import edu.psu.rcy5017.speechwriter.task.DeleteTask;
import edu.psu.rcy5017.speechwriter.task.GetAllTask;
import edu.psu.rcy5017.speechwriter.task.RenameSpeechTask;
import edu.psu.rcy5017.speechwriter.task.UpdateOrderTask;

public class SpeechListActivity extends ListActivity {
    
    private static final String TAG = "SpeechListActivity";
    
    private DragNDropAdapter<Speech> adapter;
    private SpeechDataSource datasource;
    private final OptionsCntl optionsCntl = OptionsCntl.INSTANCE;
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_list);
        
        datasource = new SpeechDataSource(this);
        
        try {
            final List<Speech> values = new GetAllTask<Speech>(datasource, 0).execute().get(); // pass a 0 for parent id because speech is at the top of the type hierarchy.
  
            adapter = new DragNDropAdapter<Speech>(this, new int[]{R.layout.dragitem}, new int[]{R.id.TextView01}, values);
            setListAdapter(adapter);
            
            final ListView listView = getListView();
            if (listView instanceof DragNDropListView) {
                ((DragNDropListView) listView).setDropListener(new DropReorderListener<Speech>(adapter, datasource, listView));
                ((DragNDropListView) listView).setRemoveListener(new RemoveListenerImpl<Speech>(adapter, listView));
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
        
            case R.id.add_speech:

                try {
                    final Speech speech = new CreateSpeechTask(datasource, this).execute().get();
                    // Save the new speech to the view.
                    adapter.add(speech);

                    // Update the order of the speech.
                    new UpdateOrderTask<Speech>(datasource, speech, adapter.getCount());

                    // Force user to overwrite the default name.
                    renameSpeech(speech, adapter.getCount() - 1);
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
        inflater.inflate(R.menu.speech_option_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        
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
                
            case R.id.speech_recordings:
                viewSpeechRecordings(speech);
                return true;
        
            case R.id.delete_speech:
                // Delete the speech, and update the adapter.
                new DeleteTask<Speech>(datasource, speech).execute();
                adapter.remove(speech);
                adapter.notifyDataSetChanged();
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
        if (requestCode == RequestCodes.RENAME_SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            final long newSpeechId = data.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
            final String newSpeechTitle = data.getStringExtra("text");
            final Speech speech = new Speech(newSpeechId, newSpeechTitle);
            final int position = data.getIntExtra("position", DefaultValues.DEFAULT_INT_VALUE);
                
            // Get the speech item to update.
            final Speech speechToUpdate = 
                    adapter.getItem(position);
            
            // Update the title.
            speechToUpdate.setTitle(speech.getTitle());
            adapter.notifyDataSetChanged();
                
            // Save the changes to the database.
            new RenameSpeechTask(datasource, speech).execute();
        }
    }
    
   /**
    * Opens the speech in the main activity view.
    * @param speech the speech to start
    */
    private void startSpeech(Speech speech) {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("id", speech.getId());
        startActivityForResult(intent, RequestCodes.START_SPEECH_REQUEST_CODE);
    }
    
    /**
     * Opens the note card list activity.
     * @param speech the speech to edit
     */
    private void editSpeech(Speech speech) {
        final Intent intent = new Intent(this, NoteCardListActivity.class);
        intent.putExtra("id", speech.getId());
        startActivityForResult(intent, RequestCodes.EDIT_SPEECH_REQUEST_CODE);
    }
    
    /**
     * Opens the speech recordings list activity.
     * @param speech the speech to edit
     */
    private void viewSpeechRecordings(Speech speech) {
        final Intent intent = new Intent(this, SpeechRecordingListActivity.class);
        intent.putExtra("id", speech.getId());
        startActivityForResult(intent, RequestCodes.VIEW_SPEECH_RECORDINGS_REQUEST_CODE);
    }
    
    /**
     * Opens an activity to edit the speech title.
     * @param speech the speech to rename
     */
    private void renameSpeech(Speech speech, int position) {
        final Intent intent = new Intent(this, EditTextActivity.class);
        intent.putExtra("position", position );
        intent.putExtra("id", speech.getId());
        intent.putExtra("text", speech.getTitle());
        startActivityForResult(intent, RequestCodes.RENAME_SPEECH_REQUEST_CODE);
    }  
} 