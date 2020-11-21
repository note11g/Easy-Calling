package com.note11.easy_calling.util;

import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class AccessibilityService extends android.accessibilityservice.AccessibilityService {

    private static final String T = "Calling";

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        //using event.getKeyCode (this is Integer type)
        //and compare with KeyEvent.KEYCODE_VOLUME_UP or KEYCODE_VOLUME_DOWN
        if(KeyEvent.KEYCODE_VOLUME_UP == event.getKeyCode()){
            if(event.getEventTime() - event.getDownTime() > 0){
                Log.e(T, "Real detected Time : "+ (event.getEventTime() - event.getDownTime()));
                if(event.getEventTime() - event.getDownTime() >= 1000)
                    Toast.makeText(this, "길게 눌러짐", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent e) {
        //Event 가 detect 되면 called
        Log.d(T, e.toString());
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
