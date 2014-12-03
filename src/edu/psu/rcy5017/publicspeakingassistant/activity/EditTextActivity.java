package edu.psu.rcy5017.publicspeakingassistant.activity;

import edu.psu.rcy5017.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.constant.DefaultValues;
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

/**
 * An Activity used to edit the text of some database column which is presented in a ListActivity.
 * Ex: A speech title, or a note's text.
 * @author Ryan Yosua
 *
 */
public class EditTextActivity extends Activity {
    
    private static final String TAG = "EditTextActivity";
    
    private int position;
    private long id;
    private String retrievedText;
    
    private EditText textField;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_text_activity);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        textField = (EditText) findViewById(R.id.edit_text_speech_title);
        
        // Create a speech object from data passed from list activity.
        final Intent intent = this.getIntent();
        
        // Get the variables passed as extras.
        position = intent.getIntExtra("position", DefaultValues.DEFAULT_INT_VALUE);
        id = intent.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
        retrievedText = intent.getStringExtra("text");
        
        // Populate the text field with the speech data.
        textField.setText(retrievedText);
        textField.setSelection(retrievedText.length());
        
        // Add the done button listener.
        textField.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || 
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    saveAndFinish(textField, position);
                }    
                return false;
            }
        });
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndFinish(textField, position);
        }
        
        return false;
    }
    
    @Override
    public void onBackPressed() {
        saveAndFinish(textField, position);
    }
    
    private void saveAndFinish(EditText textField, int position) {
        final String editedText = textField.getText().toString();
        
        final Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("id", id);
        intent.putExtra("text", editedText);
                
        setResult(RESULT_OK, intent);
        finish();
    }
    
}