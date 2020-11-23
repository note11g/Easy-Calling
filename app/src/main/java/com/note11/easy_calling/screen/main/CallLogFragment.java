package com.note11.easy_calling.screen.main;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.note11.easy_calling.R;
import com.note11.easy_calling.data.CallLogModel;
import com.note11.easy_calling.databinding.FragmentCallLogBinding;
import com.note11.easy_calling.util.CallLogAdapter;

import java.util.Calendar;


public class CallLogFragment extends Fragment {

    public static CallLogFragment newInstance(){
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_call_log, container,false);

        binding.recyclerGetLog.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        CallLogAdapter a = new CallLogAdapter();

        binding.recyclerGetLog.setAdapter(a);

        it = getItems();

        a.setOnItemLongClickListener((view, item) -> true);
        a.setOnItemClickListener((view, item) -> {
            //TODO: 전화 걸기
        });
        binding.setItems(it);

        return binding.getRoot();
    }

    public ObservableArrayList<CallLogModel> getItems() {
        ObservableArrayList<CallLogModel> datas = new ObservableArrayList<>();

        String[] projection = new String[] {
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DURATION,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE
        };
        Cursor contacts = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, null);

        if(contacts != null) {
            int nameFieldIndex = contacts.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int numberFieldIndex = contacts.getColumnIndex(CallLog.Calls.NUMBER);
            int durationFieldIndex = contacts.getColumnIndex(CallLog.Calls.DURATION);
            int dateFieldIndex = contacts.getColumnIndex(CallLog.Calls.DATE);
            int typeFieldIndex = contacts.getColumnIndex(CallLog.Calls.TYPE);

            while (contacts.moveToNext()) {
                CallLogModel callLogModel = new CallLogModel();
                callLogModel.setName(contacts.getString(nameFieldIndex));
                callLogModel.setPhone(inputHypun(contacts.getString(numberFieldIndex).replaceAll("-", "")));
                long dur = contacts.getLong(durationFieldIndex);
                callLogModel.setDuration(dur/60+"분 " + dur%60+"초");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(contacts.getLong(dateFieldIndex));
                callLogModel.setDate(calendar);
                callLogModel.setType(contacts.getInt(typeFieldIndex));

                datas.add(callLogModel);
            }
        }

        contacts.close();

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
            Log.d("ASDF","ERROR Phone[CallLogFragment]: " + number);
        }

        return number;
    }
}