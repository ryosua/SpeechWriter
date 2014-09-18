package edu.psu.rcy5017.publicspeakingassistant;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A helper class that is responsible for creating and updating the app database, and for creating all the tables.
 * @author ryosua
 *
 */
public final class DatabaseHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "PublicSpeakingAssistantDatabase";
	public static final String COLUMN_ID = "_id";
	
	// Note Card List Table
	public static final String NOTE_CARD_LIST_NAME = "NoteCardList";
	public static final String SPEECH_TITLE = "SpeechTitle"; // TODO: temp
	
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
     
    private void createNoteCardListTable(SQLiteDatabase db) {
    	final String NOTE_CARD_LIST_CREATE =
    	            "CREATE TABLE " + NOTE_CARD_LIST_NAME + " (" +
    	            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
    	            SPEECH_TITLE + " TEXT NOT NULL);"; //TODO: temporary, the title of the speech should be in Speech table.
    	
    	db.execSQL(NOTE_CARD_LIST_CREATE);
    }
      
    @Override
    public void onCreate(SQLiteDatabase db) {
    	//createSpeech(db);
    	createNoteCardListTable(db);
    	//createNoteCard(db);
    	//createSpeechRecording(db);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Do nothing.
	}
}