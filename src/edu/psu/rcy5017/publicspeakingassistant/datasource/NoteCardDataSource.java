package edu.psu.rcy5017.publicspeakingassistant.datasource;

import java.util.ArrayList;
import java.util.List;

import edu.psu.rcy5017.publicspeakingassistant.DatabaseHelper;
import edu.psu.rcy5017.publicspeakingassistant.model.NoteCard;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class NoteCardDataSource extends DataSource {
    
    private static final String TAG = "NoteCardDataSource";
    
    private String[] allColumns = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.NOTECARD_TITLE, DatabaseHelper.SPEECH_ID };

    public NoteCardDataSource(Context context) {
        super(context);
    }
    
    /**
     * Creates a new note card in the database.
     * @param title the title of the note card
     * @param speech the speech that the note card belongs to
     * @return the note card created
     */
    public NoteCard createNoteCard(String title, long speechID) {
        final ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NOTECARD_TITLE, title);
        values.put(DatabaseHelper.SPEECH_ID, speechID);
        long insertId = getDatabase().insert(DatabaseHelper.NOTECARD_TABLE_NAME, null,
                values);
        Cursor cursor = getDatabase().query(DatabaseHelper.NOTECARD_TABLE_NAME,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        NoteCard noteCard = cursorToNoteCard(cursor);
        cursor.close();
        return noteCard;
    }
    
    /**
     * Deletes the note card in the database.
     * @param noteCard the note card to delete
     */
    public void deleteNoteCard(NoteCard noteCard) {
        long id = noteCard.getId();
        Log.d(TAG, "Note card deleted with id: " + id);
        getDatabase().delete(DatabaseHelper.NOTECARD_TABLE_NAME, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }
    
    /**
     * Renames the note card in the database.
     * @param noteCard the note card to rename
     * @param newTitle the new title to rename this notecard
     * @return the number of rows affected
     */
    public int renameNotecard(NoteCard noteCard, String newTitle) {
        final ContentValues args = new ContentValues();
        args.put(DatabaseHelper.NOTECARD_TITLE, newTitle);
        return getDatabase().update(
                DatabaseHelper.NOTECARD_TABLE_NAME, args, DatabaseHelper.COLUMN_ID + "=" + noteCard.getId(), null);
    }
     
    /**
     * Get a list of note cards associated with a speech.
     * @param speechID the speech to get note cards from
     * @return the note card list
     */
    public List<NoteCard> getAllNoteCards(long speechID) {
        List<NoteCard> noteCards = new ArrayList<NoteCard>();
        
        final String selection = DatabaseHelper.SPEECH_ID + "=" + speechID;
        Cursor cursor = getDatabase().query(DatabaseHelper.NOTECARD_TABLE_NAME,
                allColumns, selection, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            NoteCard noteCard = cursorToNoteCard(cursor);
            noteCards.add(noteCard);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return noteCards;
    }
    
    /**
     * Converts a cursor to a note card.
     * @param cursor the cursor to convert
     * @return the note card
     */
    private NoteCard cursorToNoteCard(Cursor cursor) {
        final long newNoteCardId = cursor.getLong(0);
        final String newNoteCardTitle = cursor.getString(1);
        final long newNoteCardSpeechId = cursor.getLong(2);
        final NoteCard noteCard = new NoteCard(newNoteCardId, newNoteCardTitle, newNoteCardSpeechId);
        
        return noteCard;
    }

}