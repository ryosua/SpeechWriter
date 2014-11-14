package edu.psu.rcy5017.publicspeakingassistant.task;

import java.util.List;

import edu.psu.rcy5017.publicspeakingassistant.datasource.DataSource;

import android.os.AsyncTask;

/**
 * A task that gets all of the elements from a particular table corresponding to a Datasource.
 * @author Ryan Yosua
 *
 * @param <E> The type of the element that your task will return. This will be one of the types from the model package.
 */
public class GetAllTask<E> extends AsyncTask<Void, Void, List<E>> {
    
    private final DataSource<E> datasource;
    private final long parentID;
    
    /**
     * 
     * @param datasource the data source to fetch from.
     * @param parentID the id of the parent that you are fetching from, use 0 if none. Example: if you are fetching note cards, the parentID would be the id of the speech that they belong to.
     */
    public GetAllTask(DataSource<E> datasource, long parentID) {
        this.datasource = datasource;
        this.parentID = parentID;
    }

    @Override
    protected List<E> doInBackground(Void... params) {
        datasource.open();
        final List<E> values = datasource.getAll(parentID);
        datasource.close();
        
        return values;
    }

}