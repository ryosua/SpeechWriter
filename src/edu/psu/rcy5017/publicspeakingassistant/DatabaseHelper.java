package edu.psu.rcy5017.publicspeakingassistant;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A helper class
 * @author ryosua
 *
 */
public final class DatabaseHelper extends SQLiteOpenHelper {
	
	private final static int DATABASE_VERSION = 2;
	private final static String DATABASE_NAME = "PublicSpeakingAssistantDatabase";
	
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    private void createSpeech(SQLiteDatabase db) {
    	//final String NOTE_CARD_LIST_NAME = "NoteCardList";
    	
    	final String NOTE_CARD_LIST_CREATE =
    	            "CREATE TABLE " + NOTE_CARD_LIST_NAME + " (" +
    	            KEY_WORD + " TEXT, " +
    	            KEY_DEFINITION + " TEXT);";
    	
    	db.execSQL(NOTE_CARD_LIST_CREATE);
    }
    
    private void createNoteCardListTable(SQLiteDatabase db) {
    	final String NOTE_CARD_LIST_NAME = "NoteCardList";
    	
    	final String NOTE_CARD_LIST_CREATE =
    	            "CREATE TABLE " + NOTE_CARD_LIST_NAME + " (" +
    	            KEY_WORD + " TEXT, " +
    	            KEY_DEFINITION + " TEXT);";
    	
    	db.execSQL(NOTE_CARD_LIST_CREATE);
    }
    
    private void createNoteCard(SQLiteDatabase db) {
    	final String NOTE_CARD_NAME = "NoteCard";
    	
    	final String NOTE_CARD_CREATE =
    	            "CREATE TABLE " + NOTE_CARD_NAME + " (" +
    	            KEY_WORD + " TEXT, " +
    	            KEY_DEFINITION + " TEXT);";
    	
    	db.execSQL(NOTE_CARD_CREATE);
    }
     
    private void createSpeechRecording(SQLiteDatabase db) {
    	//final String NOTE_CARD_LIST_NAME = "NoteCardList";
    	
    	final String NOTE_CARD_LIST_CREATE =
    	            "CREATE TABLE " + NOTE_CARD_LIST_NAME + " (" +
    	            KEY_WORD + " TEXT, " +
    	            KEY_DEFINITION + " TEXT);";
    	
    	db.execSQL(NOTE_CARD_LIST_CREATE);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    	createSpeech(db);
    	createNoteCardListTable(db);
    	createNoteCard(db);
    	createSpeechRecording(db);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Do nothing.
	}
}