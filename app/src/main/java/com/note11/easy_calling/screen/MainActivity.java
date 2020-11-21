package com.note11.easy_calling.screen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.note11.easy_calling.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //App Screen Brightness Maximum Setting
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = 1.0f;
        getWindow().setAttributes(params);

        //checkPermission & getPermission
        if (!checkPermission()) getPermission();

    }

    public boolean checkPermission() {
        AccessibilityManager am = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> list = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.DEFAULT);

        for (int i = 0; i < list.size(); i++) {
            AccessibilityServiceInfo info = list.get(i);
            if (info.getResolveInfo().serviceInfo.packageName.equals(getApplication().getPackageName()))
                return true;
        }
        return false;
    }

    public void getPermission() {
        //TODO 시간 있으면 GetPermission 액티비티로 기능 옮기기
        AlertDialog.Builder D = new AlertDialog.Builder(this);
        D.setTitle("접근성 권한이 필요합니다.");
        D.setMessage("음량 버튼의 접근을 위해 접근성 권한이 필요합니다.");
        D.setPositiveButton("설정하러가기", (dialog, which) -> {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            Toast.makeText(this, "설치된 서비스에서 쉬운 전화걸기를 선택하여 설정을 켜주세요.", Toast.LENGTH_SHORT).show();
        }).create().show();
    }

}