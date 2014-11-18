package edu.psu.rcy5017.publicspeakingassistant;

import java.io.IOException;

import edu.psu.rcy5017.publicspeakingassistant.constant.Misc;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * A controller for recording and playing audio files.
 * Used: https://developer.android.com/guide/topics/media/audio-capture.html as example.
 * @author Ryan Yosua
 *
 */
public enum AudioCntl {
    
    INSTANCE;
    
    private final String TAG = "AudioCntl";
    private  MediaRecorder recorder;
    private  MediaPlayer player;
    
       
    /**
     * Starts playing an audio recording.
     * @param fileName the name of the file to play ex: "/testRecording.3gp"
     */
    public void startPlaying(String fileName) {
        player = new MediaPlayer();
        try {
            player.setDataSource(Misc.FILE_DIRECTORY + fileName + Misc.AUDIO_EXTENSION);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.d(TAG, "prepare() failed");
        }
    }
    
    /**
     * Stops playing the audio recording.
     */
    public void stopPlaying() {
        player.release();
        player = null;
    }
    
    /**
     * Starts recording an audio recording.
     * @param fileName the name of the file to record ex: "/testRecording.3gp"
     */
    public void startRecording(String fileName) {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(Misc.FILE_DIRECTORY + fileName + Misc.AUDIO_EXTENSION);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        
        Log.d(TAG, Misc.FILE_DIRECTORY + fileName + Misc.AUDIO_EXTENSION);
        
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.d(TAG, "prepare() failed");
        }

        recorder.start();
    }
    
    /**
     * Stops recording the audio recording.
     */
    public void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}