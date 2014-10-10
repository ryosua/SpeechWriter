package edu.psu.rcy5017.publicspeakingassistant.adapter;

import java.util.List;

import edu.psu.rcy5017.publicspeakingassistant.fragment.NoteCardFragement;
import edu.psu.rcy5017.publicspeakingassistant.model.NoteCard;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
    
    private final List<NoteCard> noteCardList;
 
    public TabsPagerAdapter(FragmentManager fm, List<NoteCard> noteCardList) {
        super(fm);
        
        this.noteCardList = noteCardList;
    }
 
    @Override
    public Fragment getItem(int index) {
        return new NoteCardFragement(noteCardList.get(index).getId());
    }
 
    @Override
    public int getCount() {
        final int count = noteCardList.size();
        return count;
    }
 
}