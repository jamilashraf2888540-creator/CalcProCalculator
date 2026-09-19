package com.example.calcprocalculator.DataBase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calcprocalculator.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    List<HistoryModel> historyList;

    public HistoryAdapter(List<HistoryModel> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_row, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {

        HistoryModel item = historyList.get(position);
        holder.tv_item_expression.setText(item.expression);
        holder.tv_item_result.setText("= " + item.result);
        holder.tv_time.setText(item.time);

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView tv_item_expression, tv_item_result, tv_time;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_item_result = itemView.findViewById(R.id.tv_item_result);
            tv_item_expression = itemView.findViewById(R.id.tv_item_expression);
            tv_time = itemView.findViewById(R.id.tv_time);


        }
    }
}
