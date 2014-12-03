package edu.psu.rcy5017.publicspeakingassistant.constant;

import android.os.Environment;

/**
 * A class containing miscellaneous constants.
 * @author Ryan Yosua
 *
 */
public final class MiscConstants {
    
    public static final String FILE_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String AUDIO_EXTENSION = ".3gp";
    public static final int MIN_FONT_SIZE = 25;
    public static final int MAX_FONT_SIZE = 75;
    public static final String PREFERENCES_NAME = "PublicSpeakingPreferences";
    
    private MiscConstants() {
        // Prevent the instantiation of this class.
    }

}