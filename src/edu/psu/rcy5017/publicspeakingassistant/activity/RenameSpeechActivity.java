package edu.psu.rcy5017.publicspeakingassistant.activity;

import edu.psu.rcy501.publicspeakingassistant.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class RenameSpeechActivity extends Activity {
	
    private static final String TAG = "RenameSpeechActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_speech);
    }
	 
}