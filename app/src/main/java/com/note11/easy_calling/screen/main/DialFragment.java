package com.note11.easy_calling.screen.main;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.note11.easy_calling.R;
import com.note11.easy_calling.data.TelModel;
import com.note11.easy_calling.databinding.FragmentDialBinding;

import java.util.ArrayList;


public class DialFragment extends Fragment {

    public static DialFragment newInstance() {
        return new DialFragment();
    }

    private Context mContext;
    private FragmentDialBinding binding;
    private boolean isSeoul = false;

    private ArrayList<TelModel> it = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dial, container, false);

        it = getItems();
        settingPad();

        return binding.getRoot();
    }

    private void settingPad() {
        binding.setPhone("");
        binding.btnDialNum0.setOnClickListener(v -> dial("0"));
        binding.btnDialNum1.setOnClickListener(v -> dial("1"));
        binding.btnDialNum2.setOnClickListener(v -> dial("2"));
        binding.btnDialNum3.setOnClickListener(v -> dial("3"));
        binding.btnDialNum4.setOnClickListener(v -> dial("4"));
        binding.btnDialNum5.setOnClickListener(v -> dial("5"));
        binding.btnDialNum6.setOnClickListener(v -> dial("6"));
        binding.btnDialNum7.setOnClickListener(v -> dial("7"));
        binding.btnDialNum8.setOnClickListener(v -> dial("8"));
        binding.btnDialNum9.setOnClickListener(v -> dial("9"));
        binding.btnDialNumStar.setOnClickListener(v -> dial("*"));
        binding.btnDialNumHash.setOnClickListener(v -> dial("#"));
        binding.btnDialDel.setOnClickListener(v -> delDial());
        binding.btnDialCall.setOnClickListener(v -> calling());
        binding.btnDialDel.setOnLongClickListener(v -> delAll());
    }

    private void dial(String d) {
        binding.setPhone(binding.getPhone() + d);
        if (binding.getPhone().length() == 2) {
            if (binding.getPhone().startsWith("02")) {
                binding.setPhone(binding.getPhone() + "-");
                isSeoul = true;
            }
        }
        if (!isSeoul) {
            if (binding.getPhone().length() == 3 || binding.getPhone().length() == 8)
                binding.setPhone(binding.getPhone() + "-");
        } else {
            if (binding.getPhone().length() == 6)
                binding.setPhone(binding.getPhone() + "-");
        }

        TelModel telModel = searchBestLike(it, binding.getPhone());
        if(telModel != null) {
            Log.d("ASDF", "searchBestLike: " + telModel.getName() + telModel.getPhone());
            //TODO 가장 비슷한 전화번호
        }
    }

    private void delDial() {
        if (binding.getPhone().length() == 1)
            isSeoul = false;
        if (!binding.getPhone().isEmpty()) {
            if (binding.getPhone().endsWith("-"))
                binding.setPhone(binding.getPhone().substring(0, binding.getPhone().length() - 1));
            binding.setPhone(binding.getPhone().substring(0, binding.getPhone().length() - 1));
        }

        TelModel telModel = searchBestLike(it, binding.getPhone());
        if(telModel != null) {
            Log.d("ASDF", "searchBestLike: " + telModel.getName() + telModel.getPhone());
            //TODO 가장 비슷한 전화번호
        }
    }

    private void calling() {
        if (!binding.getPhone().isEmpty())
            startActivity(
                    new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + binding.getPhone()))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            );
    }

    private boolean delAll() {
        binding.setPhone("");
        isSeoul = false;
        return true;
    }

    public ArrayList<TelModel> getItems() {
        ArrayList<TelModel> datas = new ArrayList<>();

        ContentResolver r = mContext.getContentResolver();

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
                phoneBook.setPhone(c.getString(numberIndex).replaceAll("-", ""));

                datas.add(phoneBook);
            }
            c.close();//Data close
        }
        return datas;
    }

    public TelModel searchBestLike(ArrayList<TelModel> datas, String phone) {
        phone = phone.replaceAll("-", "");
        if(phone.length() == 0) return null;
        int sublen = 9999;
        TelModel result = null;
        for(TelModel telModel : datas) {
            if(telModel.getPhone().contains(phone) && sublen > telModel.getPhone().length() - phone.length()) {
                sublen = telModel.getPhone().length() - phone.length();
                result = telModel;
            }
        }

        return result;
    }

}