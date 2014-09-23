package edu.psu.rcy5017.publicspeakingassistant;

import java.util.ArrayList;
import java.util.List;

import edu.psu.rcy5017.publicspeakingassistant.model.Speech;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SpeechDataSource {

    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.SPEECH_TITLE };

    public SpeechDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Speech createSpeech(String title) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.SPEECH_TITLE, title);
        long insertId = database.insert(DatabaseHelper.SPEECH_TABLE_NAME, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.SPEECH_TABLE_NAME,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Speech newSpeech = cursorSpeech(cursor);
        cursor.close();
        return newSpeech;
    }

    public void deleteSpeech(Speech speech) {
        long id = speech.getId();
        System.out.println("Speech deleted with id: " + id);
        database.delete(DatabaseHelper.SPEECH_TABLE_NAME, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }
    
    public void renameSpeech(String title) {
     
    }

    public List<Speech> getAllSpeeches() {
        List<Speech> speeches = new ArrayList<Speech>();

        Cursor cursor = database.query(DatabaseHelper.SPEECH_TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Speech speech = cursorSpeech(cursor);
            speeches.add(speech);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return speeches;
    }

    private Speech cursorSpeech(Cursor cursor) {
        Speech speech = new Speech();
        speech.setId(cursor.getLong(0));
        speech.setTitle(cursor.getString(1));
        return speech;
    }
} 