package edu.psu.rcy5017.publicspeakingassistant.constant;

import android.os.Environment;

/**
 * A class containing miscellaneous constants.
 * @author Ryan Yosua
 *
 */
public final class Misc {
    
    public static final String FILE_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String AUDIO_EXTENSION = ".3gp";
    
    private Misc() {
        // Prevent the instantiation of this class.
    }

}