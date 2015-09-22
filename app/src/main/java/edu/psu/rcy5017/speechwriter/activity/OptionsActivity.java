package edu.psu.rcy5017.speechwriter.activity;

import edu.psu.rcy5017.speechwriter.R;
import edu.psu.rcy5017.speechwriter.constant.MiscConstants;
import edu.psu.rcy5017.speechwriter.listener.ChangeFontSizeListener;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

public class OptionsActivity extends Activity {
    
    private static final String TAG = "OptionsActivity";
    
    private TextView exampleText;
    private SeekBar fontSizeControl;
    private ChangeFontSizeListener listener;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        
        fontSizeControl = (SeekBar) findViewById(R.id.font_size_seekbar);
        exampleText = (TextView) findViewById(R.id.example_text);
        
        // Set initial font size to saved size.
        final SharedPreferences settings = getSharedPreferences(MiscConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        final float defaultSize = (float) (MiscConstants.MAX_FONT_SIZE + MiscConstants.MIN_FONT_SIZE) / 2;
        final int textSize = settings.getInt("textSize", (int) defaultSize);
        exampleText.setTextSize(textSize);
        
        listener = new ChangeFontSizeListener(fontSizeControl, exampleText, MiscConstants.MIN_FONT_SIZE, MiscConstants.MAX_FONT_SIZE);
        
        // Set the progress relative to the size, and the min and max size.
        int progress = listener.getProgress(textSize);
        fontSizeControl.setProgress(progress);        
        fontSizeControl.setOnSeekBarChangeListener(listener);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndFinish();
        }
        
        return false;
    }
    
    @Override
    public void onBackPressed() {
        saveAndFinish();
    }
    
    private void saveAndFinish() {
        // Get the text size from the listener
        int textSize = listener.getTextSize(fontSizeControl.getProgress());
               
        // Save the text size to preferences.
        final SharedPreferences settings = getSharedPreferences(MiscConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("textSize", textSize);
        editor.commit();
        
        finish();
    }

}