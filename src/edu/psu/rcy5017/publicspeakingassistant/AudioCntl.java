package edu.psu.rcy5017.publicspeakingassistant;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
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
    private final String fileDirectory;
    private  MediaRecorder recorder;
    private  MediaPlayer player;
    
    private AudioCntl() {
        fileDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
    }
        
    /**
     * Starts playing an audio recording.
     * @param fileName the name of the file to play ex: "/testRecording.3gp"
     */
    public void startPlaying(String fileName) {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileDirectory + fileName);
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
        recorder.setOutputFile(fileDirectory + fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        
        Log.d(TAG, fileDirectory + fileName);
        
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