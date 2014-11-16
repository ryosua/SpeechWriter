package edu.psu.rcy5017.publicspeakingassistant.datasource;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import edu.psu.rcy5017.publicspeakingassistant.DatabaseHelper;
import edu.psu.rcy5017.publicspeakingassistant.model.Speech;

public class SpeechDataSource extends DataSource<Speech> {
    
    private static final String TAG = "SpeechDataSource";

    private String[] allColumns = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.SPEECH_TITLE };
    
    public SpeechDataSource(Context context) {
        super(context);
    }

    /**
     * Creates a new speech in the database. The speech has an order of 0.
     * @param title the title of the speech
     * @return the speech created
     */
    public Speech createSpeech(String title) {
        final int DEFAULT_ORDER = 0;
        final ContentValues values = new ContentValues();
        values.put(DatabaseHelper.SPEECH_TITLE, title);
        values.put(DatabaseHelper.SPEECH_ORDER, DEFAULT_ORDER);
        long insertId = getDatabase().insert(DatabaseHelper.SPEECH_TABLE_NAME, null,
                values);
        Cursor cursor = getDatabase().query(DatabaseHelper.SPEECH_TABLE_NAME,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Speech newSpeech = cursorToSpeech(cursor);
        cursor.close();
        return newSpeech;
    }
    
    /**
     * Deletes the speech in the database.
     * @param speech the speech to delete
     */
    public void deleteSpeech(Speech speech) {
        long id = speech.getId();
        Log.d(TAG, "Speech deleted with id: " + id);
        getDatabase().delete(DatabaseHelper.SPEECH_TABLE_NAME, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }
    
    /**
     * Renames a speech in the database.
     * @param speech the speech to rename
     * @param newTitle the new title
     * @return the number of rows affected
     */
    public int renameSpeech(Speech speech, String newTitle) {
        final ContentValues args = new ContentValues();
        args.put(DatabaseHelper.SPEECH_TITLE, newTitle);
        return getDatabase().update(
                DatabaseHelper.SPEECH_TABLE_NAME, args, DatabaseHelper.COLUMN_ID + "=" + speech.getId(), null);
    }
      
    /**
     * Converts a cursor to a speech.
     * @param cursor the cursor to convert
     * @return the speech
     */
    private Speech cursorToSpeech(Cursor cursor) {
        final long newSpeechId = cursor.getLong(0);
        final String newSpeechTitle = cursor.getString(1);
        final Speech speech = new Speech(newSpeechId, newSpeechTitle);
        
        return speech;
    }

    @Override
    public List<Speech> getAll(long parentID) {
        final List<Speech> speeches = new ArrayList<Speech>();

        Cursor cursor = getDatabase().query(DatabaseHelper.SPEECH_TABLE_NAME,
                allColumns, null, null, null, null, DatabaseHelper.SPEECH_ORDER);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Speech speech = cursorToSpeech(cursor);
            speeches.add(speech);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return speeches;
    }

    @Override
    public void deleteObject(Speech speechToDelete) {
        long id = speechToDelete.getId();
        Log.d(TAG, "Speech deleted with id: " + id);
        getDatabase().delete(DatabaseHelper.SPEECH_TABLE_NAME, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }
   
    @Override
    public int ubdateOrder(Speech speechToUpdate, int newOrder) {
        final ContentValues args = new ContentValues();
        args.put(DatabaseHelper.SPEECH_ORDER, newOrder);
        return getDatabase().update(
                DatabaseHelper.SPEECH_TABLE_NAME, args, DatabaseHelper.COLUMN_ID + "=" + speechToUpdate.getId(), null);
    }
} 