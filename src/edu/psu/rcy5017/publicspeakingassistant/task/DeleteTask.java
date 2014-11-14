package edu.psu.rcy5017.publicspeakingassistant.task;

import edu.psu.rcy5017.publicspeakingassistant.datasource.DataSource;
import android.os.AsyncTask;
import android.widget.BaseAdapter;

/**
 * A task that deletes a model instance that corresponding to a Datasource.
 * @author Ryan Yosua
 *
 * @param <E> The type of the element that your task will delete. This will be one of the types from the model package.
 */
public class DeleteTask<E> extends AsyncTask<Void, Void, Void> {
    
    private final BaseAdapter adapter;
    private final DataSource<E> datasource;
    private final E objectToDelete;
    
    public DeleteTask(DataSource<E> datasource, BaseAdapter adapter, E objectToDelete) {
        this.adapter = adapter;
        this.datasource = datasource;
        this.objectToDelete = objectToDelete;
    }

    @Override
    protected Void doInBackground(Void... params) {
        datasource.open();
        datasource.deleteObject(objectToDelete);
        datasource.close();
        return null;
    }
    
}