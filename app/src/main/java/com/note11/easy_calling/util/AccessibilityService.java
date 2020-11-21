package com.note11.easy_calling.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.note11.easy_calling.data.NumberCache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccessibilityService extends android.accessibilityservice.AccessibilityService {
    ExecutorService es = Executors.newFixedThreadPool(1);
    private static final String T = "Calling";
    private static boolean isRecord = false;
    private Context context = this;

    public static HashMap<Integer, Long> clickTimer = new HashMap<>();

    public static MacroCallClass macroCallClass;
    public static MacroUsersClass macroUsersClass;

    public AccessibilityService() {
        macroCallClass = new MacroCallClass();
        macroUsersClass = new MacroUsersClass();

        KeyData data = new KeyData();
        data.keyCode = KeyEvent.KEYCODE_VOLUME_UP;
        data.isLongClick = true;
        macroCallClass.keyCode.add((data));

        data = new KeyData();
        data.keyCode = KeyEvent.KEYCODE_VOLUME_DOWN;
        data.isLongClick = true;
        macroUsersClass.keyCode.add((data));
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.d(T, "Key detected : " + event);
        Log.d(T, "Key Code: " + event.getKeyCode());

        KeyData callMacro = macroCallClass.searchKeyCode(event.getKeyCode());
        KeyData callUser = macroUsersClass.searchKeyCode(event.getKeyCode());
        if (callMacro != null) {
            callMacro.isClickNow = event.getAction() == KeyEvent.ACTION_DOWN;
            if (callMacro.isClickNow) {
                callMacro.clickTime = new Date().getTime();
                if (callMacro.isLongClick) {
                    LongButtonEventRender(macroCallClass, callMacro);
                } else {
                    callMacro.isStatus = true;
                    macroCallClass.ifIsAllChecked();
                }
            }
        }

        if (callUser != null) {
            callUser.isClickNow = event.getAction() == KeyEvent.ACTION_DOWN;
            if (callUser.isClickNow) {
                callUser.clickTime = new Date().getTime();
                if (callUser.isLongClick) {
                    LongButtonEventRender(macroUsersClass, callUser);
                } else {
                    callUser.isStatus = true;
                    macroUsersClass.ifIsAllChecked();
                }
            }
        }
        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent e) {}

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public void onInterrupt() {}

    private void LongButtonEventRender(MacroSystem macroSystem, KeyData keyData) {
        es.submit(new ButtonLongClickWorker(macroSystem, keyData));
    }

    public class MacroSystem {
        public KeyData searchKeyCode(int key) {
            return null;
        }

        public void ifIsAllChecked() {
        }
    }

    public class MacroCallClass extends MacroSystem {
        public ArrayList<KeyData> keyCode = new ArrayList<>();

        public KeyData searchKeyCode(int key) {
            for (KeyData keyData : keyCode) {
                if (keyData.keyCode == key) return keyData;
            }
            return null;
        }

        public void ifIsAllChecked() {
            for (KeyData keyData : keyCode) {
                if (!(keyData.isStatus && keyData.isClickNow))
                    return;
            }
            //TODO 위 버튼 눌렀을때만 실행되도록
            Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + NumberCache.getNumber(context).getPhone()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    public class MacroUsersClass extends MacroSystem {
        public ArrayList<KeyData> keyCode = new ArrayList<>();

        public KeyData searchKeyCode(int key) {
            for (KeyData keyData : keyCode) {
                if (keyData.keyCode == key) return keyData;
            }
            return null;
        }

        public void ifIsAllChecked() {
            for (KeyData keyData : keyCode) {
                if (!(keyData.isStatus && keyData.isClickNow))
                    return;
            }
            Log.d(T, "ifIsAllChecked[MacroUsersClass] : EVENT");
        }
    }

    public class KeyData {
        public int keyCode;
        public boolean isLongClick = false;
        public long clickTime = -1;
        public boolean isClickNow = false;
        public boolean isStatus = false;
    }

    public class ButtonLongClickWorker implements Runnable {
        final MacroSystem macroSystem;
        final KeyData keyData;

        public ButtonLongClickWorker(MacroSystem macroSystem, KeyData keyData) {
            this.macroSystem = macroSystem;
            this.keyData = keyData;
        }

        @Override
        public void run() {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (new Date().getTime() - keyData.clickTime >= 1200 && keyData.isClickNow) {
                        keyData.isStatus = true;
                        macroSystem.ifIsAllChecked();
                    }
                }
            };

            timer.schedule(task, 1200);
        }
    }
}