package com.note11.easy_calling.screen.first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.note11.easy_calling.R;
import com.note11.easy_calling.databinding.ActivityGetPermissionBinding;

import java.util.ArrayList;
import java.util.List;

public class GetPermission extends AppCompatActivity {

    private ActivityGetPermissionBinding binding;
    private static final int MULTIPLE_PERMISSIONS = 101;
    String[] permissions = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CALL_LOG
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_permission);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_permission);
        binding.btnPermissionNext.setOnClickListener(v -> {
            getPermission1();
            getPermission2();
        });
    }

    private void getPermission1() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED)
                permissionList.add(pm);
        }
        if (!permissionList.isEmpty())
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
        else
            goToNext();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[i])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showToast_PermissionDeny();
                                return;
                            }
                        }
                    }
                } else {
                    showToast_PermissionDeny();
                    return;
                }
                goToNext();
            }
        }

    }

    private void getPermission2() {
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        Toast.makeText(this, "설치된 서비스에서 쉬운 전화걸기를 선택하여 설정을 켜주세요.", Toast.LENGTH_LONG).show();
    }

    private void goToNext(){
        startActivity(new Intent(this, SetShortCutActivity.class));
        finish();
    }

    private void showToast_PermissionDeny() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 서비스 이용이 가능합니다. 설정에서 권한 허용해주시기 바랍니다.", Toast.LENGTH_LONG).show();
        ActivityCompat.finishAffinity(this);
    }
}