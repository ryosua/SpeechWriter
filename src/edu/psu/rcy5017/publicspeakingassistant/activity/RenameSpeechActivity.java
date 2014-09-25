package edu.psu.rcy5017.publicspeakingassistant.activity;

import edu.psu.rcy501.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.SpeechDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.Speech;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class RenameSpeechActivity extends Activity {
	
    private static final String TAG = "RenameSpeechActivity";
    
    public static final int DEFAULT_INT_VALUE = -1;
    public static final long DEFAULT_LONG_VALUE = 0;
    
    private int position;
    private Speech speech;
    private EditText textField;
    
    private SpeechDataSource datasource;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_speech);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        datasource = new SpeechDataSource(this);
        datasource.open();
        
        textField = (EditText) findViewById(R.id.edit_text_speech_title);
        
        // Create a speech object from data passed from list activity.
        final Intent intent = this.getIntent();
        final long newSpeechId = intent.getLongExtra("id", RenameSpeechActivity.DEFAULT_LONG_VALUE);
		final String newSpeechTitle = intent.getStringExtra("title");
		speech = new Speech(newSpeechId, newSpeechTitle);
        position = intent.getIntExtra("position", DEFAULT_INT_VALUE);
        
        // Populate the text field with the speech data.
        textField.setText(speech.getTitle());
        textField.setSelection(speech.getTitle().length());
        
        // Add the done button listener.
        textField.setOnEditorActionListener(new OnEditorActionListener() {
        	@Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || 
                		(actionId == EditorInfo.IME_ACTION_DONE)) {
                	saveAndFinish(textField, speech, position);
                }    
                return false;
            }
        });
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == android.R.id.home) {
    		saveAndFinish(textField, speech, position);
    	}
    	
    	return false;
    }
    
    @Override
    public void onBackPressed() {
    	saveAndFinish(textField, speech, position);
    }
    
    private void saveAndFinish(EditText textField, Speech speech, int position) {
    	final String speechTitle = textField.getText().toString();
    	
    	final Intent intent = new Intent();
    	intent.putExtra("position", position);
    	intent.putExtra("id", speech.getId());
    	intent.putExtra("title", speechTitle);
    	
    	// Save the changes to the database.
    	datasource.renameSpeech(speech, speechTitle);
    	datasource.close();
    	    	
    	setResult(RESULT_OK, intent);
    	finish();
    }
    
}