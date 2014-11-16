package edu.psu.rcy5017.publicspeakingassistant.listener;

import android.widget.ListAdapter;
import android.widget.ListView;

import com.ericharlow.DragNDrop.DragNDropAdapter;
import com.ericharlow.DragNDrop.DropListener;

public class DropListenerImpl<E> implements DropListener {
    
    private final ListAdapter adapter;
    private final ListView listView;
    
    public DropListenerImpl(ListAdapter adapter, ListView listView) {
        this.adapter = adapter;
        this.listView = listView;
    }
    
    @Override
    public void onDrop(int from, int to) {
        if (adapter instanceof DragNDropAdapter) {
            ((DragNDropAdapter<E>)adapter).onDrop(from, to);
            listView.invalidateViews();
        }
    }

}