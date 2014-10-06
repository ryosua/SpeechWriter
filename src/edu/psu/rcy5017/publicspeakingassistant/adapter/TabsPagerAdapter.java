package edu.psu.rcy5017.publicspeakingassistant.adapter;

import java.util.List;

import edu.psu.rcy5017.publicspeakingassistant.fragment.NoteCardFragement;
import edu.psu.rcy5017.publicspeakingassistant.model.NoteCard;
import edu.psu.rcy5017.publicspeakingassistant.testmodel.NoteCardList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
	
	private final List<NoteCard> noteList;
 
    public TabsPagerAdapter(FragmentManager fm, List<NoteCard> noteList) {
        super(fm);
        
        this.noteList = noteList;
    }
 
    @Override
    public Fragment getItem(int index) {
    	return new NoteCardFragement(noteList.get(index).getId());
    }
 
    @Override
    public int getCount() {
        final int count = noteList.size();
        return count;
    }
 
}
