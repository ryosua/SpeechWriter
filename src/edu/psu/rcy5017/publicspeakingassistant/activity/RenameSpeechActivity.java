package edu.psu.rcy5017.publicspeakingassistant.activity;

import edu.psu.rcy501.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.model.NoteCardListDBTest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

public class RenameSpeechActivity extends Activity {
	
    private static final String TAG = "RenameSpeechActivity";
    
    public static final int DEFAULT_INT_VALUE = -1;
    public static final long DEFAULT_LONG_VALUE = 0;
    
    private int position;
    private NoteCardListDBTest speech;
    private EditText textField;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_speech);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        textField = (EditText) findViewById(R.id.edit_text_speech_title);
        
        // Create a speech object from data passed from list activity.
        final Intent intent = this.getIntent();
        speech = new NoteCardListDBTest();
        speech.setId(intent.getLongExtra("id", DEFAULT_LONG_VALUE));
        speech.setTitle(intent.getStringExtra("title"));
        position = intent.getIntExtra("position", DEFAULT_INT_VALUE);
        
        // Populate the text field with the speech data.
        textField.setText(speech.getTitle());
        textField.setSelection(speech.getTitle().length());
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
    
    private void saveAndFinish(EditText textField, NoteCardListDBTest speech, int position) {
    	final String speechTitle = textField.getText().toString();
    	
    	final Intent intent = new Intent();
    	intent.putExtra("position", position);
    	intent.putExtra("id", speech.getId());
    	intent.putExtra("title", speechTitle);
    	
    	setResult(RESULT_OK, intent);
    	finish();
    }
    
}