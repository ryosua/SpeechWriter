package edu.psu.rcy5017.publicspeakingassistant.datasource;

import java.util.ArrayList;
import java.util.List;

import edu.psu.rcy5017.publicspeakingassistant.DatabaseHelper;
import edu.psu.rcy5017.publicspeakingassistant.model.NoteCard;
import edu.psu.rcy5017.publicspeakingassistant.model.Speech;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class NoteCardDataSource extends DataSource {
	
	private static final String TAG = "NoteCard";
	
	private String[] allColumns = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.NOTECARD_TABLE_NAME, DatabaseHelper.SPEECH_ID };

	public NoteCardDataSource(Context context) {
		super(context);
	}
	
	/**
	 * Creates a new note card in the database.
	 * @param title the title of the note card
	 * @param speech the speech that the note card belongs to
	 * @return the note card created
	 */
	public NoteCard createNotecard(String title, Speech speech) {
		final ContentValues values = new ContentValues();
		values.put(DatabaseHelper.NOTECARD_TABLE_NAME, title);
		values.put(DatabaseHelper.SPEECH_ID, speech.getId());
		long insertId = getDatabase().insert(DatabaseHelper.NOTE_TABLE_NAME, null,
		        values);
		Cursor cursor = getDatabase().query(DatabaseHelper.NOTE_TABLE_NAME,
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
        getDatabase().delete(DatabaseHelper.NOTE_TABLE_NAME, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }
    
    /**
     * Renames the note card in the database.
     * @param noteCard the note card to delete
     * @param newTitle the new title to rename this notecard
     * @return the number of rows affected
     */
    public int renameNotecard(NoteCard noteCard, String newTitle) {
    	final ContentValues args = new ContentValues();
        args.put(DatabaseHelper.NOTECARD_TABLE_NAME, newTitle);
        return getDatabase().update(
        		DatabaseHelper.NOTE_TABLE_NAME, args, DatabaseHelper.COLUMN_ID + "=" + noteCard.getId(), null);
    }
    
    /**
     * Gets a list of note cards.
     * @return the note card list
     */
    public List<NoteCard> getAllNotecards() {
    	List<NoteCard> noteCards = new ArrayList<NoteCard>();

        Cursor cursor = getDatabase().query(DatabaseHelper.NOTECARD_TABLE_NAME,
                allColumns, null, null, null, null, null);

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
		return null;
    }

}