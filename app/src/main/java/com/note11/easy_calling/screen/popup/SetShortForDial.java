package com.note11.easy_calling.screen.popup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.note11.easy_calling.R;
import com.note11.easy_calling.data.ShortCache;
import com.note11.easy_calling.data.ShortModel;
import com.note11.easy_calling.data.TelModel;
import com.note11.easy_calling.databinding.ActivitySetShortForDialBinding;
import com.note11.easy_calling.util.TelAdapter;

public class SetShortForDial extends AppCompatActivity {

    private ActivitySetShortForDialBinding binding;
    private ObservableArrayList<TelModel> it = new ObservableArrayList<>();
    private String getKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_short_for_dial);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_short_for_dial);

        getKey = getIntent().getStringExtra("key");

        binding.recyclerSetTel.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        TelAdapter a = new TelAdapter();
        binding.recyclerSetTel.setAdapter(a);

        it = getItems();

        a.setOnItemClickListener((view, item) -> {
            AlertDialog.Builder oD = new AlertDialog.Builder(this, R.style.pDialogStyle);

            oD.setTitle("선택 확인")
                    .setMessage(item.getName()+"님으로 "+getKey+"의 단축번호를 선택하시겠습니까?")
                    .setNegativeButton("아니오", (dialog, which) -> { return; })
                    .setPositiveButton("예", (dialog, which) -> {
                        setShort(getKey, item);
                        Toast.makeText(this, "설정이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                        this.finish();
                    }).show();
        });

        a.setOnItemLongClickListener((view, item) -> true);

        binding.setItems(it);

        binding.edtPermission1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.setItems(searchItems(it, editable.toString()));
            }
        });
    }

    public ObservableArrayList<TelModel> searchItems(ObservableArrayList<TelModel> items, String search) {
        if(search == null || search.replaceAll(" ", "").equals("")) return items;

        ObservableArrayList<TelModel> results = new ObservableArrayList<>();

        for(TelModel telModel : items) {
            if(telModel.getName().toUpperCase().contains(search.toUpperCase()) ||
                    telModel.getName().toUpperCase().contains(search.replaceAll(" ", "").toUpperCase()) ||
                    telModel.getPhone().replaceAll("-", "").contains(search.replaceAll(" ", "").replaceAll("-", ""))
            )
                results.add(telModel);
        }

        return results;
    }

    public ObservableArrayList<TelModel> getItems() {
        ObservableArrayList<TelModel> datas = new ObservableArrayList<>();

        ContentResolver r = this.getContentResolver();

        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] tableC = {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        Cursor c = r.query(phoneUri, tableC, null, null, null);

        if (c != null) {
            //find index
            int idIndex = c.getColumnIndex(tableC[0]);
            int nameIndex = c.getColumnIndex(tableC[1]);
            int numberIndex = c.getColumnIndex(tableC[2]);

            while (c.moveToNext()) {
                //using index, get Phone Data
                TelModel phoneBook = new TelModel();
                phoneBook.setId(c.getString(idIndex));
                phoneBook.setName(c.getString(nameIndex));
                phoneBook.setPhone(inputHypun(c.getString(numberIndex).replaceAll("-", "")));

                datas.add(phoneBook);
            }
            c.close();//Data close
        }
        return datas;
    }

    public String inputHypun(String number) {
        if(number.length() > 11 || number.length() <= 5) return number;
        if(number.length() == 8) number = "010" + number;

        try {
            if (number.startsWith("02")) {
                number = number.substring(0, 2) + "-" + number.substring(2, 2 + (number.length() - 2) / 2) + "-" + number.substring(2 + (number.length() - 2) / 2, number.length());
            } else if (number.startsWith("031") || number.startsWith("032") || number.startsWith("097")) {
                number = number.substring(0, 3) + "-" + number.substring(3, 3 + (number.length() - 3) / 2) + "-" + number.substring(3 + (number.length() - 3) / 2, number.length());
            } else {
                number = number.substring(0, number.length() - 8) + "-" + number.substring(number.length() - 8, number.length() - 4) + "-" + number.substring(number.length() - 4, number.length());
            }
        }catch(Exception e) {
            Log.d("ASDF", "ERROR Phone[SetShortCut]: "+ number);
        }

        return number;
    }

    private void setShort(String padNum, TelModel t){

        ShortModel r;
        String[] i = {t.getName(),t.getPhone()};

        if(ShortCache.getShort(this)!=null){
            r = ShortCache.getShort(this);
            ShortCache.clear(this);
        }else{
            r = new ShortModel(
                    new String[]{"",""},
                    new String[]{"",""},
                    new String[]{"",""},
                    new String[]{"",""},
                    new String[]{"",""},
                    new String[]{"",""},
                    new String[]{"",""},
                    new String[]{"",""},
                    new String[]{"",""},
                    new String[]{"",""},
                    new String[]{"",""},
                    new String[]{"",""}
            );
        }
        switch (padNum){
            case "1":
                r.setN1(i);
                break;
            case "2":
                r.setN2(i);
                break;
            case "3":
                r.setN3(i);
                break;
            case "4":
                r.setN4(i);
                break;
            case "5":
                r.setN5(i);
                break;
            case "6":
                r.setN6(i);
                break;
            case "7":
                r.setN7(i);
                break;
            case "8":
                r.setN8(i);
                break;
            case "9":
                r.setN9(i);
                break;
            case "*":
                r.setNs(i);
                break;
            case "0":
                r.setN0(i);
                break;
            case "#":
                r.setNh(i);
                break;
        }

        ShortCache.setShort(this, r);
    }
}