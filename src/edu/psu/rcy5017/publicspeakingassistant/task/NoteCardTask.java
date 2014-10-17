package edu.psu.rcy5017.publicspeakingassistant.task;

import edu.psu.rcy5017.publicspeakingassistant.model.NoteCard;
import android.os.AsyncTask;

/**
 * A task that does something with a note card, but does not need to return any results, or update progress.
 * @author Ryan Yosua
 *
 */
public class NoteCardTask extends AsyncTask<Void, Void, Void> {
    
    private final NoteCard noteCard;
    
    public NoteCardTask(NoteCard noteCard) {
        this.noteCard = noteCard;
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }
    
    public NoteCard getNoteCard() {
        return noteCard;
    }

}