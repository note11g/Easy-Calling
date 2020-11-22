package com.note11.easy_calling.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.note11.easy_calling.data.TelModel;
import com.note11.easy_calling.databinding.RowTelBinding;

import java.util.ArrayList;
import java.util.List;

public class TelAdapter extends RecyclerView.Adapter<TelAdapter.TelHolder> {

    private List<TelModel> list = new ArrayList<>();

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, TelModel item);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, TelModel item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setItem(List<TelModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TelHolder(RowTelBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TelHolder holder, int position) {
        TelModel model = list.get(position);
        holder.bind(model, onItemClickListener, onItemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class TelHolder extends RecyclerView.ViewHolder {

        private RowTelBinding binding;

        public TelHolder(RowTelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(TelModel model, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
            binding.setTel(model);
            itemView.setOnClickListener(view -> clickListener.onItemClick(view, model));
            itemView.setOnLongClickListener(view -> longClickListener.onItemLongClick(view, model));
        }

    }
}
