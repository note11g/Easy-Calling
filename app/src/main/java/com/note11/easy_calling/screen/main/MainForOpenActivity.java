package com.note11.easy_calling.screen.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.accessibility.AccessibilityManager;

import com.note11.easy_calling.R;
import com.note11.easy_calling.data.NumberCache;
import com.note11.easy_calling.databinding.ActivityMainForOpenBinding;
import com.note11.easy_calling.screen.first.GetPermission;
import com.note11.easy_calling.screen.first.SetShortCutActivity;

import java.util.List;

public class MainForOpenActivity extends AppCompatActivity {

    private ActivityMainForOpenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_for_open);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_for_open);


        binding.bottomMain.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.dial:
                    switchFragment(DialFragment.newInstance());
                    break;
                case R.id.tel:
                    switchFragment(TelFragment.newInstance());
                    break;
                case R.id.call_log:
                    switchFragment(CallLogFragment.newInstance());
                    break;
            }
            return true;
        });
        switchFragment(DialFragment.newInstance());
        binding.bottomMain.setSelectedItemId(R.id.dial);

        if (!checkPermission()) startActivity(new Intent(this, GetPermission.class));
        else if (NumberCache.getNumber(this) == null)
            startActivity(new Intent(this, SetShortCutActivity.class));
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.commit();
    }

    private boolean checkPermission() {
        AccessibilityManager am = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> list = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.DEFAULT);

        for (int i = 0; i < list.size(); i++) {
            AccessibilityServiceInfo info = list.get(i);
            if (info.getResolveInfo().serviceInfo.packageName.equals(getApplication().getPackageName()))
                return true;
        }
        return false;
    }
}