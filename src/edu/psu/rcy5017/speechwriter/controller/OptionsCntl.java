package edu.psu.rcy5017.speechwriter.controller;

import edu.psu.rcy5017.speechwriter.activity.OptionsActivity;
import edu.psu.rcy5017.speechwriter.constant.RequestCodes;
import android.app.Activity;
import android.content.Intent;

public enum OptionsCntl {
    
    INSTANCE;
    
    public void openOptionsPage(Activity activity) {
        final Intent intent = new Intent(activity, OptionsActivity.class);
        activity.startActivityForResult(intent, RequestCodes.OPEN_OPTIONS_REQUEST);
    }

}