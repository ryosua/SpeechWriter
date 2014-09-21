package edu.psu.rcy5017.publicspeakingassistant;

import edu.psu.rcy501.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.adapter.TabsPagerAdapter;
import edu.psu.rcy5017.publicspeakingassistant.model.NoteCard;
import edu.psu.rcy5017.publicspeakingassistant.model.TestNoteList;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements
ActionBar.TabListener {
	private static final String TAG = "MainActivity";
	
	private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Create a list of test notecards.
        final TestNoteList notecards = new TestNoteList();
        notecards.populateList();
        
        //DEBUG
        notecards.logNotes();
 
        viewPager = (ViewPager) findViewById(R.id.pager);
        final ActionBar actionBar = getActionBar();
        final TabsPagerAdapter tabAdapter = new TabsPagerAdapter(getSupportFragmentManager(), notecards);
 
        viewPager.setAdapter(tabAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
         
        // Create a tab for each note card.
        for (NoteCard noteCard : notecards.getList()) {
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
}