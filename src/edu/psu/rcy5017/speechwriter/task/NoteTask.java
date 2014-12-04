package edu.psu.rcy5017.speechwriter.task;

import edu.psu.rcy5017.speechwriter.model.Note;
import android.os.AsyncTask;

public class NoteTask extends AsyncTask<Void, Void, Void> {
    
    private final Note note;
    
    public NoteTask(Note note) {
        this.note = note;
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }
    
    public Note getNote() {
        return note;
    }

}