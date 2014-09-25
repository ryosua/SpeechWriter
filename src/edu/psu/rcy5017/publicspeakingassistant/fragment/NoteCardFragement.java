package edu.psu.rcy5017.publicspeakingassistant.fragment;

import java.util.ArrayList;

import edu.psu.rcy501.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.testmodel.NoteCard;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoteCardFragement extends Fragment {
	
	private final NoteCard noteCard;
	
	public NoteCardFragement(NoteCard noteCard) {
		this.noteCard = noteCard;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_note_card, container, false);
        
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.linearlayout_note_card_fragment);
        
        // Create text for each note on the note card.
        ArrayList<String> notes = noteCard.getNotes();
        for(int i = 0; i < notes.size(); i++)
        {
        	TextView text = new TextView(this.getActivity());
            text.setText(notes.get(i));
            layout.addView(text);
        }
       
        return rootView;
    }
}