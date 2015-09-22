package edu.psu.rcy5017.speechwriter.listener;

import com.ericharlow.DragNDrop.DragNDropAdapter;

import edu.psu.rcy5017.speechwriter.datasource.DataSource;
import edu.psu.rcy5017.speechwriter.task.UpdateOrderTask;
import android.util.Log;
import android.widget.ListView;

public class DropReorderListener<E> extends DropListenerImpl<E>  {
    
    private static final String TAG = "DropReorderListener";
    
    private final DataSource<E> datasource;

    public DropReorderListener(DragNDropAdapter<E> adapter, DataSource<E> datasource, ListView listView) {
        super(adapter, listView);
        this.datasource = datasource;
    }
    
    @Override
    public void onDrop(int from, int to) {
        super.onDrop(from, to);
        
        final DragNDropAdapter<E> adapter = getAdapter();
        
        // Reorder every item in the list.
        // Note: For each item in the list, or task, the datasource is opened and close.
        // The performance of this operation could be improved if needed, creating a new task that reorders the whole list.
        for(int i = 0; i < adapter.getCount(); i++) {
            new UpdateOrderTask<E>(datasource, (E)adapter.getItem(i), i).execute();
        }
        
        adapter.notifyDataSetChanged();
    }

}
