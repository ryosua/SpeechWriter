package edu.psu.rcy5017.publicspeakingassistant.adapter;

import edu.psu.rcy5017.publicspeakingassistant.NoteCardFragement;
import edu.psu.rcy5017.publicspeakingassistant.model.NoteCardList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
	
	private final NoteCardList noteList;
 
    public TabsPagerAdapter(FragmentManager fm, NoteCardList noteList) {
        super(fm);
        
        this.noteList = noteList;
    }
 
    @Override
    public Fragment getItem(int index) {
    	
    	return new NoteCardFragement(noteList.getList().get(index));
    	
    	//Example
    	/*
        switch (index) {
        case 0:
            // Top Rated fragment activity
            return new TopRatedFragment();
        case 1:
            // Games fragment activity
            return new GamesFragment();
        case 2:
            // Movies fragment activity
            return new MoviesFragment();
        }
 
        return null;
        */
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}
