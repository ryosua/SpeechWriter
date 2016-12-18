package edu.psu.rcy5017.speechwriter.activity;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.ericharlow.DragNDrop.DragNDropAdapter;
import com.ericharlow.DragNDrop.DragNDropListView;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
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
import edu.psu.rcy5017.speechwriter.R;
import edu.psu.rcy5017.speechwriter.constant.DefaultValues;
import edu.psu.rcy5017.speechwriter.constant.MiscConstants;
//import edu.psu.rcy5017.speechwriter.constant.MixPanelCodes;
import edu.psu.rcy5017.speechwriter.constant.RequestCodes;
import edu.psu.rcy5017.speechwriter.controller.AudioCntl;
import edu.psu.rcy5017.speechwriter.controller.OptionsCntl;
import edu.psu.rcy5017.speechwriter.datasource.SpeechRecordingDataSource;
import edu.psu.rcy5017.speechwriter.listener.DragListenerImpl;
import edu.psu.rcy5017.speechwriter.listener.DropReorderListener;
import edu.psu.rcy5017.speechwriter.listener.RemoveListenerImpl;
import edu.psu.rcy5017.speechwriter.model.SpeechRecording;
import edu.psu.rcy5017.speechwriter.task.DeleteTask;
import edu.psu.rcy5017.speechwriter.task.GetAllTask;
import edu.psu.rcy5017.speechwriter.task.RenameSpeechRecordingTask;

public class SpeechRecordingListActivity extends ListActivity {
    
    private static final String TAG = "SpeechRecordingListA...";
    
    private DragNDropAdapter<SpeechRecording> adapter;
    private SpeechRecordingDataSource datasource;
    private final OptionsCntl optionsCntl = OptionsCntl.INSTANCE;
    private long speechID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recording_list);
        
        datasource = new SpeechRecordingDataSource(this);
        
        // Get the speechID passed from list activity.
        final Intent intent = this.getIntent();
        speechID = intent.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
       
        try {
            final List<SpeechRecording> values = new GetAllTask<SpeechRecording>(datasource, speechID).execute().get();
            
            adapter = new DragNDropAdapter<SpeechRecording>(this, new int[]{R.layout.dragitem}, new int[]{R.id.TextView01}, values);
            setListAdapter(adapter);
            
            final ListView listView = getListView();
            if (listView instanceof DragNDropListView) {
                ((DragNDropListView) listView).setDropListener(new DropReorderListener<SpeechRecording>(adapter, datasource, listView));
                ((DragNDropListView) listView).setRemoveListener(new RemoveListenerImpl<SpeechRecording>(adapter, listView));
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.RENAME_SPEECH_RECORDING_REQUEST_CODE && resultCode == RESULT_OK) {
            final long newSpeechRecordingId = data.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
            final String newSpeechRecordingTitle = data.getStringExtra("text");
            final SpeechRecording speechRecording = new SpeechRecording(newSpeechRecordingId, newSpeechRecordingTitle, speechID);
            final int position = data.getIntExtra("position", DefaultValues.DEFAULT_INT_VALUE);
                 
            // Get the speech item to update.
            final SpeechRecording speechRecordingToUpdate = 
                    adapter.getItem(position);
            
            // Update the title.
            speechRecordingToUpdate.setTitle(speechRecording.getTitle());
            adapter.notifyDataSetChanged();
            
            // Save the changes to the database.
            new RenameSpeechRecordingTask(datasource, speechRecordingToUpdate).execute();
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
        inflater.inflate(R.menu.speech_recording_option_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
       
        final SpeechRecording speechRecording = (SpeechRecording) getListAdapter().getItem(info.position);
        
        switch (item.getItemId()) {
            case R.id.play_speech_recording:
                playSpeechRecording(speechRecording);
                return true;
                         
            case R.id.rename_speech_recording:
                renameSpeechRecording(speechRecording, info.position);
                return true;
                
            case R.id.share_speech_recording:
                emailSpeech(speechRecording);
                Log.d(TAG, "Speech Recording shared");
                return true;
       
            case R.id.delete_speech_recording:
                new DeleteTask<SpeechRecording>(datasource, speechRecording).execute();
                adapter.remove(speechRecording);
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
      
    private void playSpeechRecording(SpeechRecording speechRecording) {
        final AudioCntl audioCntl = AudioCntl.INSTANCE;
        audioCntl.startPlaying(speechRecording.getFile());

        // Record activity in mixpanel.
        //final MixpanelAPI mixpanel = MixpanelAPI.getInstance(this.getApplicationContext(), MixPanelCodes.MIXPANEL_TOKEN);
        //mixpanel.track("Recording Played");
    }
    
    /**
     * Opens an activity to edit the speech recording title.
     * @param speechRecording the speech recording to rename
     */
    private void renameSpeechRecording(SpeechRecording speechRecording, int position) {
        final Intent intent = new Intent(this, EditTextActivity.class);
        intent.putExtra("position", position );
        intent.putExtra("id", speechRecording.getId());
        intent.putExtra("text", speechRecording.getTitle());
        startActivityForResult(intent, RequestCodes.RENAME_SPEECH_RECORDING_REQUEST_CODE);
    }
    
    private void emailSpeech(SpeechRecording speechRecording) {
        final String file = MiscConstants.FILE_DIRECTORY + speechRecording.getFile() + MiscConstants.AUDIO_EXTENSION;
        Log.d(TAG, "File: " + file);
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        // Set the type to 'email'.
        shareIntent.setType("vnd.android.cursor.dir/email");
        // The attachment.
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file));
        // The mail subject.
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, file);
        startActivity(Intent.createChooser(shareIntent , ""));
    }

}