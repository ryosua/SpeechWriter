package edu.psu.rcy5017.publicspeakingassistant.listener;

import android.widget.ListAdapter;
import android.widget.ListView;

import com.ericharlow.DragNDrop.DragNDropAdapter;
import com.ericharlow.DragNDrop.RemoveListener;

public class RemoveListenerImpl<E> implements RemoveListener {
    
    private final ListAdapter adapter;
    private final ListView listView;
    
    public RemoveListenerImpl(ListAdapter adapter, ListView listView) {
        this.adapter = adapter;
        this.listView = listView;
    }
    
    @Override
    public void onRemove(int which) {
        if (adapter instanceof DragNDropAdapter) {
            ((DragNDropAdapter<E>)adapter).onRemove(which);
            listView.invalidateViews();
        }
    }

}