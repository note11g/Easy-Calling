package com.note11.easy_calling.screen.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.note11.easy_calling.R;
import com.note11.easy_calling.data.NumberCache;
import com.note11.easy_calling.data.NumberModel;
import com.note11.easy_calling.databinding.ActivityMainBinding;
import com.note11.easy_calling.screen.first.GetPermission;

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
        if (!checkPermission()) startActivity(new Intent(this, GetPermission.class));

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

}