package com.note11.easy_calling.util;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class AccessibilityService extends android.accessibilityservice.AccessibilityService {

    private static final String T = "Calling";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent ev) {
        //Event 가 detect 되면 called
        Log.d(T, ev.toString());
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        //서비스가 연결되었을때 called
        Log.d(T, "Service is connected!");
    }

    @Override
    public void onInterrupt() {
        Log.d(T, "onInterrupted");
    }
}
