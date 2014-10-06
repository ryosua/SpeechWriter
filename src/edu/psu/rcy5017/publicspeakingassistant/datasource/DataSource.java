package edu.psu.rcy5017.publicspeakingassistant.datasource;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import edu.psu.rcy5017.publicspeakingassistant.DatabaseHelper;

public abstract class DataSource {
    
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    
    public DataSource(Context context) {
         dbHelper = new DatabaseHelper(context);
    }
    
    public void open() throws SQLException {
        database = getDbHelper().getWritableDatabase();
    }

    public void close() {
        getDbHelper().close();
    }
    
    public SQLiteDatabase getDatabase() {
        return database;
    }

    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }
}