package com.example.mobilepizza.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilepizza.R;
import com.example.mobilepizza.classes.HistoryItem;

import java.util.ArrayList;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.MyViewHolder> {

    private final ArrayList<HistoryItem> historyItems;

    public HistoryRecyclerAdapter(ArrayList<HistoryItem> historyItems) {
        this.historyItems = historyItems;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date_text, address_text, sum_text;
        RecyclerView recyclerView;
        Button button;

        public MyViewHolder(View view) {
            super(view);

            date_text = view.findViewById(R.id.textView19);
            address_text = view.findViewById(R.id.textView21);
            sum_text = view.findViewById(R.id.textView233);
            recyclerView = view.findViewById(R.id.history_item_recycler);
            button = view.findViewById(R.id.button4);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.date_text.setText(historyItems.get(position).getTime());
        holder.address_text.setText(historyItems.get(position).getAddress());
        holder.sum_text.setText(historyItems.get(position).getSum() + "₸");

        HistoryImagesRecyclerAdapter adapter =
                new HistoryImagesRecyclerAdapter(historyItems.get(position).getImages());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }
}
