package edu.psu.rcy5017.speechwriter.listener;

import android.widget.ListView;

import com.ericharlow.DragNDrop.DragNDropAdapter;
import com.ericharlow.DragNDrop.DropListener;

public class DropListenerImpl<E> implements DropListener {
    
    private final DragNDropAdapter<E> adapter;
    private final ListView listView;
    
    public DropListenerImpl(DragNDropAdapter<E> adapter, ListView listView) {
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
    
    public DragNDropAdapter<E> getAdapter() {
        return adapter;
    }

}