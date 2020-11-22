package com.note11.easy_calling.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.note11.easy_calling.R;
import com.note11.easy_calling.data.NumberCache;
import com.note11.easy_calling.data.NumberModel;
import com.note11.easy_calling.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    static final int R_CALL = 1;
    static final int R_PHONE_NUMBERS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //App Screen Brightness Maximum Setting
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = 1.0f;
        getWindow().setAttributes(params);

        //checkPermission & getPermission
        if (!checkPermission()) getPermission();
        checkCallingPermission();
        checkGetPhonesArrayPermission();

        binding.setPhone("");
        binding.btnCall.setOnClickListener(v->{
            if(binding.getPhone().isEmpty()) return;

            if(NumberCache.getNumber(this)!=null)
                NumberCache.clear(this);
            NumberCache.setNumber(this, new NumberModel(binding.getPhone()));
            Toast.makeText(this, "설정완료!", Toast.LENGTH_SHORT).show();
        });

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

    public boolean checkCallingPermission(){
        int pCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (pCheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, R_CALL);
            return false;
        }
    }

    public boolean checkGetPhonesArrayPermission() {
        int pCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (pCheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, R_PHONE_NUMBERS);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case R_CALL: {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    finish();
                break;
            }
            case R_PHONE_NUMBERS: {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                    finish();
                break;
            }
        }
    }

    public void getPermission() {
        //TODO 시간 있으면 GetPermission 액티비티로 기능 옮기기
        AlertDialog.Builder D = new AlertDialog.Builder(this);
        D.setTitle("접근성 권한이 필요합니다.");
        D.setMessage("음량 버튼의 접근을 위해 접근성 권한이 필요합니다.");
        D.setPositiveButton("설정하러가기", (dialog, which) -> {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            Toast.makeText(this, "설치된 서비스에서 쉬운 전화걸기를 선택하여 설정을 켜주세요.", Toast.LENGTH_LONG).show();
        }).create().show();
    }

}