package edu.psu.rcy5017.publicspeakingassistant;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * A helper class that is responsible for creating and updating the app database, 
 * and for creating all the tables in the database.
 * @author ryosua
 *
 */
public final class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String TAG = "DatabaseHelper";
    
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "PublicSpeakingAssistantDatabase";
    public static final String COLUMN_ID = "_id";

    // Speech Table
    public static final String SPEECH_TABLE_NAME = "Speech";
    public static final String SPEECH_TITLE = "Title";
    
    // NoteCard Table
    public static final String NOTECARD_TABLE_NAME = "Notecard";
    public static final String NOTECARD_TITLE = "Title";
    public static final String SPEECH_ID = "SpeechID";
    
    // Note Table
    public static final String NOTE_TABLE_NAME = "Note";
    public static final String NOTE_TEXT = "Text";
    public static final String NOTECARD_ID = "NoteCardID";
    
    // SpeechRecording Table
    public static final String SPEECH_RECORDING_TABLE_NAME = "SpeechRecording";
    public static final String SPEECH_RECORDING_TITLE = "Title";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        createSpeechTable(db);
        createNoteCardTable(db);
        createNoteTable(db);
        createSpeechRecording(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do nothing.
    }
    
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
    
    private void createSpeechTable(SQLiteDatabase db) {
        final String createStatement =
                    "CREATE TABLE " + SPEECH_TABLE_NAME + " (" +
                            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            SPEECH_TITLE + " TEXT NOT NULL" +
                    ");";
        
        db.execSQL(createStatement);
    }
    
    private void createNoteCardTable(SQLiteDatabase db) {
        final String createStatement =
                "CREATE TABLE " + NOTECARD_TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NOTECARD_TITLE + " TEXT NOT NULL," +
                        SPEECH_ID + " INTEGER NOT NULL," +
                        "FOREIGN KEY("+ SPEECH_ID +") REFERENCES " + SPEECH_TABLE_NAME + "("+ COLUMN_ID +") ON DELETE CASCADE" + 
                ");";
            
        db.execSQL(createStatement);
    }
    
    private void createNoteTable(SQLiteDatabase db) {
        final String createStatement =
                "CREATE TABLE " + NOTE_TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NOTE_TEXT + " TEXT NOT NULL," +
                        NOTECARD_ID + " INTEGER NOT NULL," +
                        "FOREIGN KEY("+ NOTECARD_ID +") REFERENCES " + NOTECARD_TABLE_NAME + "("+ COLUMN_ID +") ON DELETE CASCADE" + 
                ");";
            
        db.execSQL(createStatement);
    }
    
    private void createSpeechRecording(SQLiteDatabase db) {
        final String createStatement =
                "CREATE TABLE " + SPEECH_RECORDING_TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SPEECH_RECORDING_TITLE + " TEXT NOT NULL," +
                        SPEECH_ID + " INTEGER NOT NULL," +
                        "FOREIGN KEY("+ SPEECH_ID +") REFERENCES " + SPEECH_TABLE_NAME + "("+ COLUMN_ID +") ON DELETE CASCADE" +  
                ");";
            
        db.execSQL(createStatement);
    }
    
}