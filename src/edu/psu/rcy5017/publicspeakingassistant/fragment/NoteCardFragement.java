package edu.psu.rcy5017.publicspeakingassistant.fragment;

import java.util.List;

import edu.psu.rcy5017.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.datasource.NoteDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.Note;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoteCardFragement extends Fragment {
    
    private final float FONT_SIZE = 40;
    private NoteDataSource datasource;
    private final long noteCardID;
   
    public NoteCardFragement(long noteCardID) {
        this.noteCardID = noteCardID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        final View rootView = inflater.inflate(R.layout.fragment_note_card, container, false);
        
        final LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.linearlayout_note_card_fragment);
        
        datasource = new NoteDataSource(getActivity());
        datasource.open();
        
        // Create text for each note on the note card.
        final List<Note> notes = datasource.getAllNotes(noteCardID);
        for(Note note: notes)
        {
            // Create text.
            final TextView text = new TextView(this.getActivity());
            text.setText(note.getText());
            
            // Change text size.
            text.setTextSize(FONT_SIZE);
             
            layout.addView(text);
        }
       
        return rootView;
    }
    
    @Override
    public void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        datasource.close();
        super.onPause();
    }
    
}