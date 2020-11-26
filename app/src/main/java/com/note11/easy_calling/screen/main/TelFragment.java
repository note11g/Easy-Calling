package com.note11.easy_calling.screen.main;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.note11.easy_calling.R;
import com.note11.easy_calling.data.AccModel;
import com.note11.easy_calling.data.TelModel;
import com.note11.easy_calling.data.UriCache;
import com.note11.easy_calling.databinding.FragmentTelBinding;
import com.note11.easy_calling.util.AccAdapter;


public class TelFragment extends Fragment {

    public static TelFragment newInstance() {
        return new TelFragment();
    }

    private Context mContext;

    private FragmentTelBinding binding;

    private ObservableArrayList<AccModel> it = new ObservableArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tel, container, false);

        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(mContext, 2);
        binding.recyclerSetTelAcc.setLayoutManager(gridLayoutManager);

        AccAdapter a = new AccAdapter();
        binding.recyclerSetTelAcc.setAdapter(a);

        it = getItems();
        binding.setItem(it);

        a.setOnItemLongClickListener((view, item) -> {
            Toast.makeText(mContext, "연락처 수정화면으로 이동합니다", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(mContext, TelAddActivity.class);
            i.putExtra("getPhone", item.getTm().getPhone());
            i.putExtra("getName", item.getTm().getName());
            startActivity(i);
            return true;
        });
        a.setOnItemClickListener((view, item) -> {
            AlertDialog.Builder oD = new AlertDialog.Builder(mContext, R.style.pDialogStyle);
            oD.setTitle("전화 걸기")
                    .setMessage(item.getTm().getName() + "님에게 전화를 거시겠습니까?")
                    .setNegativeButton("아니오", (dialog, which) -> {
                        return;
                    })
                    .setPositiveButton("예", (dialog, which) -> startActivity(
                            new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.getTm().getPhone()))
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))).show();
        });


        binding.edtPermission1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.setItem(searchItems(it, editable.toString()));
            }
        });

        binding.addTelBtn.setOnClickListener(v -> goToTelAddActivity());

        return binding.getRoot();
    }

    public ObservableArrayList<AccModel> searchItems(ObservableArrayList<AccModel> items, String search) {
        if (search == null || search.replaceAll(" ", "").equals("")) return items;

        ObservableArrayList<AccModel> results = new ObservableArrayList<>();

        for (AccModel accModel : items) {
            if (accModel.getTm().getName().toUpperCase().contains(search.toUpperCase()) ||
                    accModel.getTm().getName().toUpperCase().contains(search.replaceAll(" ", "").toUpperCase()) ||
                    accModel.getTm().getPhone().replaceAll("-", "").contains(search.replaceAll(" ", "").replaceAll("-", ""))
            )
                results.add(accModel);
        }

        return results;
    }

    public ObservableArrayList<AccModel> getItems() {
        ObservableArrayList<AccModel> datas = new ObservableArrayList<>();

        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] tableC = {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        Cursor c = mContext.getContentResolver().query(phoneUri, tableC, null, null, null);

        if (c != null) {
            //find index
            int idIndex = c.getColumnIndex(tableC[0]);
            int nameIndex = c.getColumnIndex(tableC[1]);
            int numberIndex = c.getColumnIndex(tableC[2]);

            while (c.moveToNext()) {
                //using index, get Phone Data
                TelModel phoneBook = new TelModel();
                String p = inputHypun(c.getString(numberIndex).replaceAll("-", ""));
                phoneBook.setId(c.getString(idIndex));
                phoneBook.setName(c.getString(nameIndex));
                phoneBook.setPhone(p);

                AccModel a = new AccModel();
                a.setTm(phoneBook);

                if (UriCache.getUri(mContext) != null && UriCache.getUri(mContext).getMap().get(p) != null)
                    a.setIu(UriCache.getUri(mContext).getMap().get(p));
                else
                    a.setIu(null);

                datas.add(a);
            }
            c.close();//Data close
        }
        return datas;
    }

    public String inputHypun(String number) {
        if (number.length() > 11 || number.length() <= 5) return number;
        if (number.length() == 8) number = "010" + number;

        try {
            if (number.startsWith("02")) {
                number = number.substring(0, 2) + "-" + number.substring(2, 2 + (number.length() - 2) / 2) + "-" + number.substring(2 + (number.length() - 2) / 2, number.length());
            } else if (number.startsWith("031") || number.startsWith("032") || number.startsWith("097")) {
                number = number.substring(0, 3) + "-" + number.substring(3, 3 + (number.length() - 3) / 2) + "-" + number.substring(3 + (number.length() - 3) / 2, number.length());
            } else {
                number = number.substring(0, number.length() - 8) + "-" + number.substring(number.length() - 8, number.length() - 4) + "-" + number.substring(number.length() - 4, number.length());
            }
        } catch (Exception e) {
            Log.d("ASDF", "ERROR Phone[SetShortCut]: " + number);
        }

        return number;
    }

    private void goToTelAddActivity() {
        Intent i = new Intent(mContext.getApplicationContext(), TelAddActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


    @Override
    public void onResume() {
        super.onResume();
        it = getItems();
        binding.setItem(it);
    }
}