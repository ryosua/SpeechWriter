package edu.psu.rcy5017.publicspeakingassistant.datasource;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import edu.psu.rcy5017.publicspeakingassistant.DatabaseHelper;
import edu.psu.rcy5017.publicspeakingassistant.model.Speech;

public class SpeechDataSource extends DataSource {

    private String[] allColumns = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.SPEECH_TITLE };

    public SpeechDataSource(Context context) {
        super(context);
    }

    public Speech createSpeech(String title) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.SPEECH_TITLE, title);
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

    public void deleteSpeech(Speech speech) {
        long id = speech.getId();
        System.out.println("Speech deleted with id: " + id);
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

    public List<Speech> getAllSpeeches() {
        List<Speech> speeches = new ArrayList<Speech>();

        Cursor cursor = getDatabase().query(DatabaseHelper.SPEECH_TABLE_NAME,
                allColumns, null, null, null, null, null);

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

    private Speech cursorToSpeech(Cursor cursor) {
    	final long newSpeechId = cursor.getLong(0);
		final String newSpeechTitle = cursor.getString(1);
		final Speech speech = new Speech(newSpeechId, newSpeechTitle);
        
        return speech;
    }
} 