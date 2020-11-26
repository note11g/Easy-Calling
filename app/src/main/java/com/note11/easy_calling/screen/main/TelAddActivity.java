package com.note11.easy_calling.screen.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ContentProviderOperation;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Toast;

import com.note11.easy_calling.R;
import com.note11.easy_calling.data.TelModel;
import com.note11.easy_calling.data.UriCache;
import com.note11.easy_calling.data.UriModel;
import com.note11.easy_calling.databinding.ActivityTelAddBinding;

import java.util.ArrayList;
import java.util.HashMap;

import gun0912.tedimagepicker.builder.TedImagePicker;

public class TelAddActivity extends AppCompatActivity {

    private ActivityTelAddBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel_add);

        binding.setPhoneEdt.setText(getIntent().getStringExtra("getPhone"));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_tel_add);

        binding.btnAddImg.setOnClickListener(v-> TedImagePicker.with(this).start(this::setImage));
        binding.setPhoneEdt.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        binding.addContentBtn.setOnClickListener(v -> {
            String name = binding.setNameEdt.getText().toString();
            String phone = binding.setPhoneEdt.getText().toString();
            if(!name.equals("") && phone.length() > 0) {
                if(addItem(phone, name)) {
                    saveProfile(phone);
                    Toast.makeText(this, "등록이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private boolean addItem(String number, String name) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name).build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number.replaceAll("-", ""))
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void saveProfile(String phone){
        if(binding.getProfile()!=null){
            HashMap<String,String> map;
            if(UriCache.getUri(this)==null){
                map = new HashMap<>();
            }else{
                map =  UriCache.getUri(this).getMap();
                UriCache.clear(this);
            }
            map.put(phone, binding.getProfile().toString());
            UriCache.setUri(this, new UriModel(map));
            Toast.makeText(this, binding.getProfile().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setImage(Uri uri){
        binding.circleImageView.setImageURI(uri);
        binding.setProfile(uri);
    }
}