package com.note11.easy_calling.screen.first;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.note11.easy_calling.R;
import com.note11.easy_calling.data.NumberCache;
import com.note11.easy_calling.data.NumberModel;
import com.note11.easy_calling.data.TelModel;
import com.note11.easy_calling.databinding.ActivitySetShortCutBinding;
import com.note11.easy_calling.util.TelAdapter;

import java.util.ArrayList;

public class SetShortCutActivity extends AppCompatActivity {

    private ActivitySetShortCutBinding binding;
    private ObservableArrayList<TelModel> it = new ObservableArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_short_cut);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_short_cut);

        binding.recyclerSetTel.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        TelAdapter a = new TelAdapter();
        binding.recyclerSetTel.setAdapter(a);

        it = getItems();

        a.setOnItemClickListener((view, item) -> {
            AlertDialog.Builder oD = new AlertDialog.Builder(this,
                    android.R.style.Theme_DeviceDefault_Light_Dialog);

            oD.setMessage(item.getName()+"님으로 빠른 전화 걸기를 선택하시겠습니까?")
                    .setTitle("선택 확인 창")
                    .setPositiveButton("아니오", (dialog, which) -> { return; })
                    .setNeutralButton("예", (dialog, which) -> {
                        NumberCache.setNumber(this, new NumberModel(item.getPhone()));
                        Toast.makeText(this, "설정되었습니다.", Toast.LENGTH_SHORT).show();
                        this.finish();
                    }).show();
        });

        a.setOnItemLongClickListener((view, item) -> true);

        binding.setItems(it);
        //TODO 검색 기능 추가
    }

    public ObservableArrayList<TelModel> getItems() {
        ObservableArrayList<TelModel> datas = new ObservableArrayList<>();

        ContentResolver r = this.getContentResolver();

        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] tableC = {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };// get Column

        Cursor c = r.query(phoneUri, tableC, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                //find index
                int idIndex = c.getColumnIndex(tableC[0]);
                int nameIndex = c.getColumnIndex(tableC[1]);
                int numberIndex = c.getColumnIndex(tableC[2]);

                //using index, get Phone Data
                TelModel phoneBook = new TelModel();
                phoneBook.setId(c.getString(idIndex));
                phoneBook.setName(c.getString(nameIndex));
                phoneBook.setPhone(c.getString(numberIndex));

                datas.add(phoneBook);
            }
            c.close();//Data close
        }
        return datas;
    }

    @Override
    public void onBackPressed() { return; }
}