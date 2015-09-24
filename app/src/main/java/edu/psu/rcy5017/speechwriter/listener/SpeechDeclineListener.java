package edu.psu.rcy5017.speechwriter.listener;

import android.content.DialogInterface;

import edu.psu.rcy5017.speechwriter.controller.SpeecherAdController;

public class SpeechDeclineListener implements DialogInterface.OnClickListener {

    private static final String TAG = "SpeecherAcceptListener";

    private final SpeecherAdController adController;

    public SpeechDeclineListener(SpeecherAdController adController) {
        this.adController = adController;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        adController.ignoreAd();
    }
}
