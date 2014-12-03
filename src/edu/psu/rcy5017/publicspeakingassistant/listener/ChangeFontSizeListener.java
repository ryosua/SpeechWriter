package edu.psu.rcy5017.publicspeakingassistant.listener;

import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * A change listener that changes the size of text, based on user input.
 * @author Ryan Yosua
 *
 */
public class ChangeFontSizeListener implements OnSeekBarChangeListener {
    
    private static final String TAG = "TextSizeControlListener";
   
    private final int maximumSize;
    private final int minimumSize;
    private final TextView text;
    
    public ChangeFontSizeListener(SeekBar seekBar, TextView text, int minimumSize, int maximumSize) {
        this.text = text;
        this.minimumSize = minimumSize;
        this.maximumSize = maximumSize;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        final int textSize = getTextSize(progress);
        text.setTextSize(textSize);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Update the example text size.
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
       // Do nothing.
    }
    
    public int getTextSize(int progress) {
        final float textSize = ( (maximumSize - minimumSize) * ((float) progress / 100) ) + minimumSize;
        return (int) textSize;
    }
    
    public int getProgress(int textSize) {
        final float progress = (float)(textSize - minimumSize) / (maximumSize - minimumSize);
        return (int) (progress * 100);
    }
}
