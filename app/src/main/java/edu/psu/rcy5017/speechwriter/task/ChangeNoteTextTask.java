package edu.psu.rcy5017.speechwriter.task;

import edu.psu.rcy5017.speechwriter.datasource.NoteDataSource;
import edu.psu.rcy5017.speechwriter.model.Note;

public class ChangeNoteTextTask extends NoteTask {
    
    private NoteDataSource datasource;
    
    public ChangeNoteTextTask(NoteDataSource datasource, Note note) {
        super(note);
        this.datasource = datasource;
    }

    @Override
    protected Void doInBackground(Void... params) {
        final Note note = getNote();
        datasource.open();
        datasource.changeNoteText(note, note.getText());
        datasource.close();
        return null;
    }
    
}