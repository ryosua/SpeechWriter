package edu.psu.rcy5017.speechwriter.adapter;

import java.util.List;

import edu.psu.rcy5017.speechwriter.fragment.NoteCardFragement;
import edu.psu.rcy5017.speechwriter.model.NoteCard;
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
        final long noteCardID = noteCardList.get(index).getId();
        final NoteCardFragement noteCardFragement = new NoteCardFragement();
        noteCardFragement.setNoteCardID(noteCardID);
        return noteCardFragement;
    }
 
    @Override
    public int getCount() {
        final int count = noteCardList.size();
        return count;
    }
 
}