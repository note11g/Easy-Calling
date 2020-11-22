package com.note11.easy_calling.screen.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.note11.easy_calling.R;
import com.note11.easy_calling.databinding.FragmentCallLogBinding;


public class CallLogFragment extends Fragment {

    public static CallLogFragment newInstance(){
        return new CallLogFragment();
    }

    private Context mContext;

    private FragmentCallLogBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_call_log, container,false);
        return binding.getRoot();
    }
}