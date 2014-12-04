package edu.psu.rcy5017.speechwriter.datasource;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import edu.psu.rcy5017.speechwriter.DatabaseHelper;
import edu.psu.rcy5017.speechwriter.model.SpeechRecording;

public class SpeechRecordingDataSource extends DataSource<SpeechRecording> {
    
    private static final String TAG = "SpeechRecordingDataSource";
    
    private String[] allColumns = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.SPEECH_RECORDING_TITLE, DatabaseHelper.SPEECH_ID };

    public SpeechRecordingDataSource(Context context) {
        super(context);
    }
    
    /**
     * Creates a new speech recording in the database.
     * @param title the title of the speech recording
     * @param speech the speech that the speech recording belongs to
     * @return the speech recording created
     */
    public SpeechRecording createSpeechRecording(String title, long speechID) {
        final int DEFAULT_ORDER = 0;
        final ContentValues values = new ContentValues();
        values.put(DatabaseHelper.SPEECH_RECORDING_TITLE, title);
        values.put(DatabaseHelper.SPEECH_RECORDING_ORDER, DEFAULT_ORDER);
        values.put(DatabaseHelper.SPEECH_ID, speechID);
        long insertId = getDatabase().insert(DatabaseHelper.SPEECH_RECORDING_TABLE_NAME, null,
                values);
        final Cursor cursor = getDatabase().query(DatabaseHelper.SPEECH_RECORDING_TABLE_NAME,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        final SpeechRecording speechRecording = cursorToSpeechRecording(cursor);
        cursor.close();
        return speechRecording;
    }
       
    /**
     * Renames the speech recording in the database.
     * @param speechRecording the speech recording to rename
     * @param newTitle the new title to rename this speech recording
     * @return the number of rows affected
     */
    public int renameSpeechRecording(SpeechRecording speechRecording, String newTitle) {
        final ContentValues args = new ContentValues();
        args.put(DatabaseHelper.SPEECH_RECORDING_TITLE, newTitle);
        return getDatabase().update(
                DatabaseHelper.SPEECH_RECORDING_TABLE_NAME, args, DatabaseHelper.COLUMN_ID + "=" + speechRecording.getId(), null);
    }
    
    /**
     * Get a list of speech recordings associated with a speech.
     * @param speechID the speech to get recordings from
     * @return the speech recording list
     */
    public List<SpeechRecording> getAllSpeechRecordings(long speechID) {
        List<SpeechRecording> speechRecordings = new ArrayList<SpeechRecording>();
        
        final String selection = DatabaseHelper.SPEECH_ID + "=" + speechID;
        Cursor cursor = getDatabase().query(DatabaseHelper.SPEECH_RECORDING_TABLE_NAME,
                allColumns, selection, null, null, null, DatabaseHelper.SPEECH_RECORDING_ORDER);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SpeechRecording speechRecording = cursorToSpeechRecording(cursor);
            speechRecordings.add(speechRecording);
            cursor.moveToNext();
        }
        
        cursor.close();
        return speechRecordings;
    }
    
    /**
     * Converts a cursor to a speech recording.
     * @param cursor the cursor to convert
     * @return the speech recording
     */
    private SpeechRecording cursorToSpeechRecording(Cursor cursor) {
        final long newSpeechRecordingId = cursor.getLong(0);
        final String newSpeechRecordingTitle = cursor.getString(1);
        final long newSpeechRecordingSpeechId = cursor.getLong(2);
        final SpeechRecording speechRecording = new SpeechRecording(newSpeechRecordingId, newSpeechRecordingTitle, newSpeechRecordingSpeechId);
        
        return speechRecording;
    }

    @Override
    public List<SpeechRecording> getAll(long parentID) {
        final List<SpeechRecording> speechRecordings = new ArrayList<SpeechRecording>();
        
        final String selection = DatabaseHelper.SPEECH_ID + "=" + parentID;
        Cursor cursor = getDatabase().query(DatabaseHelper.SPEECH_RECORDING_TABLE_NAME,
                allColumns, selection, null, null, null, DatabaseHelper.SPEECH_RECORDING_ORDER);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SpeechRecording speechRecording = cursorToSpeechRecording(cursor);
            speechRecordings.add(speechRecording);
            cursor.moveToNext();
        }
        
        cursor.close();
        return speechRecordings;
    }

    @Override
    public void deleteObject(SpeechRecording speechRecordingToDelete) {
        long id = speechRecordingToDelete.getId();
        Log.d(TAG, "Speech recording deleted with id: " + id);
        getDatabase().delete(DatabaseHelper.SPEECH_RECORDING_TABLE_NAME, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    @Override
    public int ubdateOrder(SpeechRecording speechRecordingToUpdate, int newOrder) {
        final ContentValues args = new ContentValues();
        args.put(DatabaseHelper.SPEECH_RECORDING_ORDER, newOrder);
        return getDatabase().update(
                DatabaseHelper.SPEECH_RECORDING_TABLE_NAME, args, DatabaseHelper.COLUMN_ID + "=" + speechRecordingToUpdate.getId(), null);
    }

}