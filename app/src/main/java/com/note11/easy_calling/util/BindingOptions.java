package com.note11.easy_calling.util;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.note11.easy_calling.data.TelModel;


public class BindingOptions {

    @BindingAdapter("tel")
    public static void bindTelItem(RecyclerView recyclerView, ObservableArrayList<TelModel> items){
        TelAdapter adapter = (TelAdapter) recyclerView.getAdapter();
        if(adapter!=null) adapter.setItem(items);

    }
}
