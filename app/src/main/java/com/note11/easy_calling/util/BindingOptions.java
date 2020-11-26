package com.note11.easy_calling.util;

import android.util.Log;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.note11.easy_calling.data.AccModel;
import com.note11.easy_calling.data.CallLogModel;
import com.note11.easy_calling.data.TelModel;


public class BindingOptions {

    @BindingAdapter("tel")
    public static void bindTelItem(RecyclerView recyclerView, ObservableArrayList<TelModel> items){
        TelAdapter adapter = (TelAdapter) recyclerView.getAdapter();
        if(adapter!=null) adapter.setItem(items);
    }

    @BindingAdapter("log")
    public static void bindLogItem(RecyclerView recyclerView, ObservableArrayList<CallLogModel> items){
        CallLogAdapter adapter = (CallLogAdapter) recyclerView.getAdapter();
        if(adapter!=null) adapter.setItem(items);
    }

    @BindingAdapter("acc")
    public static void bindAccItem(RecyclerView recyclerView, ObservableArrayList<AccModel> items){
        AccAdapter adapter = (AccAdapter) recyclerView.getAdapter();
        if(adapter!=null) adapter.setItem(items);
    }


}
