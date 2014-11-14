package edu.psu.rcy5017.publicspeakingassistant.datasource;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import edu.psu.rcy5017.publicspeakingassistant.DatabaseHelper;

public abstract class DataSource<E> {
    
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
    
    /**
     * Gets a list of all all of the elements in a table.
     * @param parentID the id of the parent that you are fetching from, use 0 if none. Example: if you are fetching note cards, the parentID would be the id of the speech that they belong to.
     * @return
     */
    public abstract List<E> getAll(long parentID);
 
}