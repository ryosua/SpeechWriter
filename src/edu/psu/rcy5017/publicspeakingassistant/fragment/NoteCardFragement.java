package edu.psu.rcy5017.publicspeakingassistant.fragment;

import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.psu.rcy5017.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.datasource.NoteDataSource;
import edu.psu.rcy5017.publicspeakingassistant.model.Note;
import edu.psu.rcy5017.publicspeakingassistant.task.GetAllTask;
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
        
        // Create text for each note on the note card.
        List<Note> notes = null;
        try {
            notes = new GetAllTask<Note>(datasource, noteCardID).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        
        if (notes != null) {
            for(Note note: notes)
            {
                // Create text.
                final TextView text = new TextView(this.getActivity());
                text.setText(note.getText());
                
                // Change text size.
                text.setTextSize(FONT_SIZE);
                 
                layout.addView(text);
            }
        }
       
        return rootView;
    }
    
}