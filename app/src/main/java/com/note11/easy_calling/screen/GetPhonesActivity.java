package com.note11.easy_calling.screen;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.note11.easy_calling.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class GetPhonesActivity extends AppCompatActivity {

    HashMap<String, String> phoneList;

    Button searchBtn;
    EditText searchInp;
    RecyclerView getPhonesRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_phones);

        phoneList = getPhoneList();

        init();

        drawPhoneItems(phoneList);
    }

    protected void init() {
        searchBtn = findViewById(R.id.searchBtn);
        searchInp = findViewById(R.id.searchInp);
        getPhonesRv = findViewById(R.id.getPhonesRv);

        searchBtn.setOnClickListener(btnSearchEvent);
    }

    View.OnClickListener btnSearchEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            drawPhoneItems(searchPhone(searchInp.getText().toString()));
        }
    };

    public HashMap<String, String> getPhoneList() {
        HashMap<String, String> phoneData = new HashMap<>();
        ContentResolver cr = getContentResolver();
        Cursor contacts = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " > 0", null, null);

        int nameFieldIndex = contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int numberFieldIndex = contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        String name, number;

        while(contacts.moveToNext()) {
            name = contacts.getString(nameFieldIndex);
            number = contacts.getString(numberFieldIndex).replaceAll("-", "");
            if(!phoneData.containsKey(number))
                phoneData.put(number, name);
        }

        contacts.close();

        return phoneData;
    }

    public HashMap<String, String> searchPhone(String q) {
        HashMap<String, String> result = new HashMap<>();
        for(String key : phoneList.keySet()) {
            String name = phoneList.get(key);
            if(key.contains(q) || name.contains(q))
                result.put(key, name);
        }

        return result;
    }

    public void drawPhoneItems(HashMap<String, String> items) {

    }
}