package com.note11.easy_calling.screen;

import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.note11.easy_calling.R;

public class GetPhonesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_phones);

        getPhoneList();
    }

    public void getPhoneList() {
        Cursor contacts = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        int nameFieldIndex = contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        int numberFieldIndex = contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        while(contacts.moveToNext()) {
            Log.d("G", "GETUsersPhones: " + contacts.getString(nameFieldIndex));
        }
    }
}