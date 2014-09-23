package edu.psu.rcy5017.publicspeakingassistant;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A helper class that is responsible for creating and updating the app database, 
 * and for creating all the tables in the database.
 * @author ryosua
 *
 */
public final class DatabaseHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "PublicSpeakingAssistantDatabase";
	public static final String COLUMN_ID = "_id";
	
	// Note Card List Table
	public static final String SPEECH_TABLE_NAME = "Speech";
	public static final String SPEECH_TITLE = "SpeechTitle";
	
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
     
    private void createSpeechTable(SQLiteDatabase db) {
    	final String speechCreateStatement =
    	            "CREATE TABLE " + SPEECH_TABLE_NAME + " (" +
    	            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
    	            SPEECH_TITLE + " TEXT NOT NULL);";
    	
    	db.execSQL(speechCreateStatement);
    }
      
    @Override
    public void onCreate(SQLiteDatabase db) {
    	createSpeechTable(db);
    	//createNoteCardTable(db);
    	//createSpeechRecording(db);
    	//createBulletPointTable(db);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Do nothing.
	}
}