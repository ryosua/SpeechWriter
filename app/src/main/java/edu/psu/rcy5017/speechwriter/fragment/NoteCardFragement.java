package edu.psu.rcy5017.speechwriter.fragment;

import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.psu.rcy5017.speechwriter.R;
import edu.psu.rcy5017.speechwriter.constant.MiscConstants;
import edu.psu.rcy5017.speechwriter.datasource.NoteDataSource;
import edu.psu.rcy5017.speechwriter.model.Note;
import edu.psu.rcy5017.speechwriter.task.GetAllTask;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoteCardFragement extends Fragment {
    
    private static final String TAG = "NoteCardFragement";
   
    private NoteDataSource datasource;
    private long noteCardID;
   
    public NoteCardFragement() {

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
            for(Note note: notes) {
                // Create text.
                final TextView text = new TextView(this.getActivity());
                text.setText(note.getText());
                
                // Load text size from preferences.
                final SharedPreferences settings = getActivity().getSharedPreferences(MiscConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
                final float defaultSize = (float) (MiscConstants.MAX_FONT_SIZE + MiscConstants.MIN_FONT_SIZE) / 2;
                final int textSize = settings.getInt("textSize", (int) defaultSize);
                
                // Change text size.
                text.setTextSize(textSize);
                                
                layout.addView(text);
            }
        }
       
        return rootView;
    }

    public void setNoteCardID(long noteCardID) {
        this.noteCardID = noteCardID;
    }
    
}