package edu.psu.rcy5017.publicspeakingassistant.task;

import edu.psu.rcy5017.publicspeakingassistant.datasource.DataSource;
import android.os.AsyncTask;

/**
 * A task that updates the order of the elements from a particular table corresponding to a Datasource.
 * @author Ryan Yosua
 *
 * @param <E> The type of the element that your task will update. This will be one of the types from the model package.
 */
public class UpdateOrderTask<E> extends AsyncTask<Void, Void, Integer> {
    
    private final DataSource<E> datasource;
    private final E elementToUpdate;
    private final int newOrder;
    
    public UpdateOrderTask(DataSource<E> datasource, E elementToUpdate, int newOrder) {
        this.datasource = datasource;
        this.elementToUpdate = elementToUpdate;
        this.newOrder = newOrder;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        datasource.open();
        final Integer rowsAffected = datasource.ubdateOrder(elementToUpdate, newOrder);
        datasource.close();
        return rowsAffected;
    }

}