package edu.psu.rcy5017.publicspeakingassistant.activity;

import java.util.List;

import edu.psu.rcy501.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.adapter.TabsPagerAdapter;
import edu.psu.rcy5017.publicspeakingassistant.constant.DefaultValues;
import edu.psu.rcy5017.publicspeakingassistant.datasource.NoteCardDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.NoteCard;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * The activity that displays notes and speech time, while the speaker is giving a speech.
 * @author Ryan Yosua
 *
 */
public class MainActivity extends FragmentActivity implements
ActionBar.TabListener {
    private static final String TAG = "MainActivity";
    
    private NoteCardDataSource datasource;
    private ViewPager viewPager;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Get speechId from intent.
        final Intent intent = this.getIntent();
        long speechID = intent.getLongExtra("id", DefaultValues.DEFAULT_LONG_VALUE);
        
        datasource = new NoteCardDataSource(this);
        datasource.open();
        
        final List<NoteCard> notecards = datasource.getAllNoteCards(speechID);
       
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
        
        Log.d(TAG, "Main activity started");
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
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}