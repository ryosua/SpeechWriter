package edu.psu.rcy5017.speechwriter.datasource;

import java.util.ArrayList;
import java.util.List;

import edu.psu.rcy5017.speechwriter.DatabaseHelper;
import edu.psu.rcy5017.speechwriter.model.Note;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class NoteDataSource extends DataSource<Note> {
    
    private static final String TAG = "NoteDataSource";
    
    private String[] allColumns = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.NOTE_TEXT, DatabaseHelper.NOTECARD_ID };

    public NoteDataSource(Context context) {
        super(context);
    }
    
    /**
     * Creates a new note in the database.
     * @param text the text of the note
     * @param noteCardID the note card that the note belongs to
     * @return the note created
     */
    public Note createNote(String text, long noteCardID) {
        final int DEFAULT_ORDER = 0;
        final ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NOTE_TEXT, text);
        values.put(DatabaseHelper.NOTE_ORDER, DEFAULT_ORDER);
        values.put(DatabaseHelper.NOTECARD_ID, noteCardID);
        long insertId = getDatabase().insert(DatabaseHelper.NOTE_TABLE_NAME, null,
                values);
        Cursor cursor = getDatabase().query(DatabaseHelper.NOTE_TABLE_NAME,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Note note = cursorToNote(cursor);
        cursor.close();
        return note;
    }
     
    /**
     * Changes the note text in the database.
     * @param note the note to change text
     * @param newText the new text to change for this note
     * @return the number of rows affected
     */
    public int changeNoteText(Note note, String newText) {
        final ContentValues args = new ContentValues();
        args.put(DatabaseHelper.NOTE_TEXT, newText);
        return getDatabase().update(
                DatabaseHelper.NOTE_TABLE_NAME, args, DatabaseHelper.COLUMN_ID + "=" + note.getId(), null);
    }
       
    /**
     * Converts a cursor to a note.
     * @param cursor the cursor to convert
     * @return the note
     */
    private Note cursorToNote(Cursor cursor) {
        final long newNoteId = cursor.getLong(0);
        final String newNoteText = cursor.getString(1);
        final long newNoteNoteCardId = cursor.getLong(2);
        final Note note = new Note(newNoteId, newNoteNoteCardId, newNoteText);
        
        return note;
    }

    @Override
    public List<Note> getAll(long parentID) {
        final List<Note> notes = new ArrayList<Note>();
        
        final String selection = DatabaseHelper.NOTECARD_ID + "=" + parentID;
        Cursor cursor = getDatabase().query(DatabaseHelper.NOTE_TABLE_NAME,
                allColumns, selection, null, null, null, DatabaseHelper.NOTE_ORDER);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = cursorToNote(cursor);
            notes.add(note);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return notes;
    }

    @Override
    public void deleteObject(Note noteToDelete) {
        long id = noteToDelete.getId();
        Log.d(TAG, "Note deleted with id: " + id);
        getDatabase().delete(DatabaseHelper.NOTE_TABLE_NAME, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    @Override
    public int ubdateOrder(Note noteToUpdate, int newOrder) {
        final ContentValues args = new ContentValues();
        args.put(DatabaseHelper.NOTE_ORDER, newOrder);
        return getDatabase().update(
                DatabaseHelper.NOTE_TABLE_NAME, args, DatabaseHelper.COLUMN_ID + "=" + noteToUpdate.getId(), null);
    }

}