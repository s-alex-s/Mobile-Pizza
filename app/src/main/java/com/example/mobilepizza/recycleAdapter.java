package com.example.mobilepizza;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilepizza.Foodclasses.Pizza;

import java.util.ArrayList;

public class recycleAdapter extends RecyclerView.Adapter<recycleAdapter.MyViewHolder> {
    private ArrayList<Pizza> pizzaList;

    public recycleAdapter(ArrayList<Pizza> usersList) {
        this.pizzaList = usersList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name1Txt;

        public MyViewHolder(final View view){
            super(view);

            name1Txt = view.findViewById(R.id.textView10);
        }
    }


    @NonNull
    @Override
    public recycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodlist_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recycleAdapter.MyViewHolder holder, int position) {
        String name = pizzaList.get(position).getName();

        holder.name1Txt.setText(name);

    }

    @Override
    public int getItemCount() {
        return pizzaList.size();
    }
}
