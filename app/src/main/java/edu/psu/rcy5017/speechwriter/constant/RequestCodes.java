package edu.psu.rcy5017.speechwriter.constant;

/**
 * A class containing request code constants.
 * @author Ryan Yosua
 *
 */
public final class RequestCodes {
    
    public static final int RENAME_SPEECH_REQUEST_CODE = 1001;
    public static final int EDIT_SPEECH_REQUEST_CODE = 1002;
    public static final int RENAME_NOTECARD_REQUEST_CODE = 1003;
    public static final int EDIT_NOTECARD_REQUEST_CODE = 1004;
    public static final int EDIT_NOTE_REQUEST_CODE = 1005;
    public static final int START_SPEECH_REQUEST_CODE = 1006;
    public static final int VIEW_SPEECH_RECORDINGS_REQUEST_CODE = 1007;
    public static final int RENAME_SPEECH_RECORDING_REQUEST_CODE = 1008;
    public static final int OPEN_OPTIONS_REQUEST = 1009;

    private RequestCodes() {
        // Prevent the instantiation of this class.
    }

}