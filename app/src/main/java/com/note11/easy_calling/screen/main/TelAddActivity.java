package com.note11.easy_calling.screen.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ContentProviderOperation;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.note11.easy_calling.R;
import com.note11.easy_calling.databinding.ActivityTelAddBinding;

import java.util.ArrayList;

public class TelAddActivity extends AppCompatActivity {

    ActivityTelAddBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel_add);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_tel_add);
    }

    private void addItem(String number, String name) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number.replaceAll("-", ""))
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}