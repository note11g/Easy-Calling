package com.note11.easy_calling.screen.main;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.note11.easy_calling.R;
import com.note11.easy_calling.data.CallLogModel;
import com.note11.easy_calling.data.NumberCache;
import com.note11.easy_calling.data.NumberModel;
import com.note11.easy_calling.databinding.FragmentCallLogBinding;
import com.note11.easy_calling.util.CallLogAdapter;

import java.util.Calendar;


public class CallLogFragment extends Fragment {

    public static CallLogFragment newInstance() {
        return new CallLogFragment();
    }

    private Context mContext;

    private FragmentCallLogBinding binding;

    private ObservableArrayList<CallLogModel> it = new ObservableArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_call_log, container, false);

        binding.recyclerGetLog.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        CallLogAdapter a = new CallLogAdapter();

        binding.recyclerGetLog.setAdapter(a);

        it = getItems();

        a.setOnItemLongClickListener(this::longClick);
        a.setOnItemClickListener((view, item) -> {
            AlertDialog.Builder oD = new AlertDialog.Builder(mContext, R.style.pDialogStyle);
            oD.setTitle("전화 걸기")
                    .setMessage((item.getName()!=null ? item.getName():"등록정보 없음") + "님에게 전화를 거시겠습니까?")
                    .setNegativeButton("아니오", (dialog, which) -> {
                        return;
                    })
                    .setPositiveButton("예", (dialog, which) -> startActivity(
                            new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.getPhone()))
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))).show();
        });
        binding.setItems(it);



        return binding.getRoot();
    }

    public ObservableArrayList<CallLogModel> getItems() {
        ObservableArrayList<CallLogModel> datas = new ObservableArrayList<>();

        String[] projection = new String[]{
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DURATION,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE,
                CallLog.Calls.CACHED_PHOTO_URI
        };

        Cursor contacts = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, null);

        if (contacts != null) {
            int nameFieldIndex = contacts.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int numberFieldIndex = contacts.getColumnIndex(CallLog.Calls.NUMBER);
            int durationFieldIndex = contacts.getColumnIndex(CallLog.Calls.DURATION);
            int dateFieldIndex = contacts.getColumnIndex(CallLog.Calls.DATE);
            int typeFieldIndex = contacts.getColumnIndex(CallLog.Calls.TYPE);
            int photoFieldIndex = contacts.getColumnIndex(CallLog.Calls.CACHED_PHOTO_URI);

            while (contacts.moveToNext()) {
                CallLogModel callLogModel = new CallLogModel();
                callLogModel.setName(contacts.getString(nameFieldIndex));
                callLogModel.setPhone(inputHypun(contacts.getString(numberFieldIndex).replaceAll("-", "")));
                long dur = contacts.getLong(durationFieldIndex);
                callLogModel.setDuration(dur / 60 + "분 " + dur % 60 + "초");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(contacts.getLong(dateFieldIndex));
                callLogModel.setDate(calendar);
                callLogModel.setType(contacts.getInt(typeFieldIndex));
                String photoURI = contacts.getString(photoFieldIndex);
                if(photoURI != null) {
                    callLogModel.setPhoto(Uri.parse(photoURI));
                    Log.d("TAG", "HasPhoto: " + photoURI);
                }

                datas.add(callLogModel);
            }
        }

        contacts.close();

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
            Log.d("ASDF", "ERROR Phone[CallLogFragment]: " + number);
        }

        return number;
    }

    private boolean longClick(View v, CallLogModel m) {
        //TODO long click
        //TODO 대체 logModel에서 type이 뭐임? 우리 부재중/수신/발신은 표시해야 됨
        Snackbar.make(v, "mtype:"+m.getType(),Snackbar.LENGTH_SHORT).show();
        return true;
    }


}