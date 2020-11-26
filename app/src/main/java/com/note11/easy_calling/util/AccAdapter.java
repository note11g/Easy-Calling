package com.note11.easy_calling.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.note11.easy_calling.data.AccModel;
import com.note11.easy_calling.databinding.RowAccBinding;

import java.util.ArrayList;
import java.util.List;

public class AccAdapter extends RecyclerView.Adapter<AccAdapter.AccHolder> {

    private List<AccModel> list = new ArrayList<>();

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, AccModel item);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, AccModel item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setItem(List<AccModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AccHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccHolder(RowAccBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccHolder holder, int position) {
        AccModel model = list.get(position);
        holder.bind(model, onItemClickListener, onItemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class AccHolder extends RecyclerView.ViewHolder {

        private RowAccBinding binding;

        public AccHolder(RowAccBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(AccModel model, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
            binding.setAcc(model);
            itemView.setOnClickListener(view -> clickListener.onItemClick(view, model));
            itemView.setOnLongClickListener(view -> longClickListener.onItemLongClick(view, model));
        }

    }
}
