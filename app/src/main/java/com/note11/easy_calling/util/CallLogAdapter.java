package com.note11.easy_calling.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.note11.easy_calling.data.CallLogModel;
import com.note11.easy_calling.databinding.RowLogBinding;

import java.util.ArrayList;
import java.util.List;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.CallLogHolder> {

    private List<CallLogModel> list = new ArrayList<>();

    private CallLogAdapter.OnItemClickListener onItemClickListener;
    private CallLogAdapter.OnItemLongClickListener onItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, CallLogModel item);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, CallLogModel item);
    }

    public void setOnItemClickListener(CallLogAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(CallLogAdapter.OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setItem(List<CallLogModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CallLogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CallLogAdapter.CallLogHolder(RowLogBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CallLogHolder holder, int position) {
        CallLogModel model = list.get(position);
        holder.bind(model, onItemClickListener, onItemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class CallLogHolder extends RecyclerView.ViewHolder {

        private RowLogBinding binding;

        public CallLogHolder(RowLogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CallLogModel model, CallLogAdapter.OnItemClickListener clickListener, CallLogAdapter.OnItemLongClickListener longClickListener) {
            binding.setLog(model);
            itemView.setOnClickListener(view -> clickListener.onItemClick(view, model));
            itemView.setOnLongClickListener(view -> longClickListener.onItemLongClick(view, model));
        }

    }
}
