package edu.psu.rcy5017.speechwriter.listener;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.ericharlow.DragNDrop.DragListener;

import edu.psu.rcy5017.speechwriter.R;

public class DragListenerImpl implements DragListener {

    int backgroundColor = 0xe0103010;
    int defaultBackgroundColor;
    
    public void onDrag(int x, int y, ListView listView) {
        
    }
    
    public void onStartDrag(View itemView) {
        itemView.setVisibility(View.INVISIBLE);
        defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
        itemView.setBackgroundColor(backgroundColor);
        ImageView iv = (ImageView)itemView.findViewById(R.id.ImageView01);
        if (iv != null) iv.setVisibility(View.INVISIBLE);
    }
    
    public void onStopDrag(View itemView) {
        itemView.setVisibility(View.VISIBLE);
        itemView.setBackgroundColor(defaultBackgroundColor);
        ImageView iv = (ImageView)itemView.findViewById(R.id.ImageView01);
        if (iv != null) iv.setVisibility(View.VISIBLE);
    }

}
