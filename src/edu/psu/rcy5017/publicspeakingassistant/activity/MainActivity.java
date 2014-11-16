package edu.psu.rcy5017.publicspeakingassistant.activity;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import edu.psu.rcy5017.publicspeakingassistant.AudioCntl;
import edu.psu.rcy5017.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.adapter.TabsPagerAdapter;
import edu.psu.rcy5017.publicspeakingassistant.constant.DefaultValues;
import edu.psu.rcy5017.publicspeakingassistant.datasource.NoteCardDataSource;
import edu.psu.rcy5017.publicspeakingassistant.datasource.SpeechRecordingDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.NoteCard;
import edu.psu.rcy5017.publicspeakingassistant.model.SpeechRecording;
import edu.psu.rcy5017.publicspeakingassistant.task.GetAllTask;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * The activity that displays notes and speech time, while the speaker is giving a speech.
 * @author Ryan Yosua
 *
 */
public class MainActivity extends FragmentActivity implements
ActionBar.TabListener {
    private static final String TAG = "MainActivity";
    
    private AudioCntl audioCntl = AudioCntl.INSTANCE;    
    private NoteCardDataSource datasource;
    private long speechID;
    private ViewPager viewPager;    
    private TextView timerText;
    final SpeechRecordingDataSource speechRecordingdatasource = new SpeechRecordingDataSource(this);
    
    private int seconds;
      
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.d(TAG, "Main activity started");
        
        // Get speechId from intent.
        final Intent intent = this.getIntent();
        speechID = intent.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
        
        datasource = new NoteCardDataSource(this);
        
        List<NoteCard> notecards = null;
        try {
            notecards = new GetAllTask<NoteCard>(datasource, speechID).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        
        if (notecards != null) {
            viewPager = (ViewPager) findViewById(R.id.pager);
            final ActionBar actionBar = getActionBar();
            final TabsPagerAdapter tabAdapter = new TabsPagerAdapter(getSupportFragmentManager(), notecards);
     
            viewPager.setAdapter(tabAdapter);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
             
            // Create a tab for each note card.
            for (NoteCard noteCard : notecards) {
                actionBar.addTab(actionBar.newTab().setText(noteCard.getTitle())
                        .setTabListener(this));
            }
            
            // On swiping the viewpager make respective tab selected.
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    // on changing the page
                    // make respected tab selected
                    actionBar.setSelectedNavigationItem(position);
                }
             
                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    // Do nothing.
                }
             
                @Override
                public void onPageScrollStateChanged(int arg0) {
                    // Do nothing.
                }
            });
            
            // Set initial time to 0.
            timerText = (TextView) findViewById(R.id.timer_text);
            seconds = 0;
            
            // Start the speech recording.
            startRecording();
            
            // Start the timer.
            Timer speechTimer = new Timer();
            UpdateSpeechTimerTask updateTask = new UpdateSpeechTimerTask();
            speechTimer.schedule(updateTask, 0, 1000);
        }
    }
       
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // Do nothing.
    }

     @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // Do nothing.
    }
        
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        audioCntl.stopRecording();
        super.onPause();
    }
    
    /** Converts seconds to a digital time - [minutes]:[seconds]. 
     * @param seconds time in seconds
     * @return the digital display
     */
    private String secondsToDigitalTime(int seconds) {
        String display = "";
                
        int remaingSeconds = 0; // The number of seconds left after converting seconds to minutes.
        
        // Convert minutes to seconds.
        if (seconds >= 60) {
            int minutes = seconds / 60;
            
            if (minutes >= 10) {
                display += minutes / 10;
                display += minutes % 10;
                display += ":";
            } else {
                display += 0;
                display += minutes;
                display += ":";
            }
            remaingSeconds = seconds % 60;
        } else {
            display += "00:";
            remaingSeconds = seconds;
        }
        
        // Display the remaining seconds.
        if (remaingSeconds >= 10) {
            display += remaingSeconds / 10;
            display += remaingSeconds % 10;
        } else {
            display += "0";
            display += remaingSeconds;
        }
        
        return display;
    }
    
    private void startRecording() {
        try {
            // Create the speech recording record in the database, name it with the date it was created.
            final SpeechRecording speechRecording = new CreateSpeechRecordingTask().execute().get();
            
            // Start the voice recording.
            audioCntl.startRecording(speechRecording.getFile());
        } catch(InterruptedException ie) {
            ie.printStackTrace();
        } catch(ExecutionException ee) { 
            ee.printStackTrace();
        }
    }
    
    /**
     * A task that updates a speech timer every second on the main activity.
     * @author Ryan Yosua
     *
     */
    private final class UpdateSpeechTimerTask extends TimerTask {
        
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    seconds++;
                    timerText.setText(secondsToDigitalTime(seconds));
                }
            });
        }
    }
    
    public class CreateSpeechRecordingTask extends AsyncTask<Void, Void, SpeechRecording> {

        @Override
        protected SpeechRecording doInBackground(Void... params) {
            speechRecordingdatasource.open();
            final SpeechRecording speechRecording = speechRecordingdatasource.createSpeechRecording(new Date().toString() , speechID);
            speechRecordingdatasource.close();
            
            return speechRecording;
        }

    }
}