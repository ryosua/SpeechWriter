package edu.psu.rcy5017.speechwriter.task;

import android.os.AsyncTask;
import edu.psu.rcy5017.speechwriter.datasource.NoteCardDataSource;
import edu.psu.rcy5017.speechwriter.model.NoteCard;

public class CreateNoteCardTask extends AsyncTask<Void, Void, NoteCard> {
    
    private final NoteCardDataSource datasource;
    private final long speechID;
    
    public CreateNoteCardTask(NoteCardDataSource datasource, long speechID) {
        this.datasource = datasource;
        this.speechID = speechID;
    }

    @Override
    protected NoteCard doInBackground(Void... params) {
        datasource.open();
        final NoteCard noteCard = datasource.createNoteCard("New Note Card", speechID);
        datasource.close();
        return noteCard;
    }
   
}