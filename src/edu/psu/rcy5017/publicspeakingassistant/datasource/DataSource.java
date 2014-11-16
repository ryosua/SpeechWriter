package edu.psu.rcy5017.publicspeakingassistant.datasource;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import edu.psu.rcy5017.publicspeakingassistant.DatabaseHelper;

public abstract class DataSource<E> {
    
    private SQLiteDatabase database;
    private final DatabaseHelper dbHelper;
    
    public DataSource(Context context) {
         dbHelper = new DatabaseHelper(context);
    }
    
    public final void open() throws SQLException {
        database = getDbHelper().getWritableDatabase();
    }

    public final void close() {
        getDbHelper().close();
    }
    
    public final SQLiteDatabase getDatabase() {
        return database;
    }

    public final DatabaseHelper getDbHelper() {
        return dbHelper;
    }
    
    /**
     * Gets a list of all all of the elements in a table.
     * @param parentID the id of the parent that you are fetching from, use 0 if none. Example: if you are fetching note cards, the parentID would be the id of the speech that they belong to.
     * @return the list of elements
     */
    public abstract List<E> getAll(long parentID);
    
    /**
     * Deletes an object from the table.
     * @param elementToDelete the object to delete
     */
    public abstract void deleteObject(E elementToDelete);
    
    /**
     * Changes the order of the element in the database
     * @param elementToUpdate the element to change order
     * @param newOrder the new order of the element
     * @return the number of rows affected
     */
    public abstract int ubdateOrder(E elementToUpdate, int newOrder);
 
}