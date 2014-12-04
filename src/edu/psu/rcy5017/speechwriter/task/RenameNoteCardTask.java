package edu.psu.rcy5017.speechwriter.task;

import edu.psu.rcy5017.speechwriter.datasource.NoteCardDataSource;
import edu.psu.rcy5017.speechwriter.model.NoteCard;

public class RenameNoteCardTask extends NoteCardTask {
    
    private final NoteCardDataSource datasource;
 
    public RenameNoteCardTask(NoteCard noteCard, NoteCardDataSource datasource) {
        super(noteCard);
        this.datasource = datasource;
    }

    @Override
    protected Void doInBackground(Void... params) {
        final NoteCard noteCard = getNoteCard();
        datasource.open();
        datasource.renameNotecard(noteCard, noteCard.getTitle());
        datasource.close();
        return null;
    }
    
}