package edu.psu.rcy5017.speechwriter.task;

import android.os.AsyncTask;
import edu.psu.rcy5017.speechwriter.datasource.NoteDataSource;
import edu.psu.rcy5017.speechwriter.model.Note;

public class CreateNoteTask extends AsyncTask<Void, Void, Note> {
    
    private final NoteDataSource datasource;
    private final long noteCardID;
    
    public CreateNoteTask(NoteDataSource datasource, long noteCardID) {
        this.datasource = datasource;
        this.noteCardID = noteCardID;
    }

    @Override
    protected Note doInBackground(Void... params) {
        datasource.open();
        final Note note = datasource.createNote("New Note", noteCardID);
        datasource.close();
        return note;
    }
        
}