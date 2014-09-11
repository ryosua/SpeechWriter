package edu.psu.rcy5017.publicspeakingassistant;

import edu.psu.rcy501.publicspeakingassistant.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NoteCardFragement extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_note_card, container, false);
         
        return rootView;
    }
}