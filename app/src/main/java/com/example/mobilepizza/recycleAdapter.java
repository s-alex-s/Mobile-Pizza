package com.example.mobilepizza;

import com.example.mobilepizza.Food;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class recycleAdapter extends RecyclerView.Adapter<recycleAdapter.MyViewHolder> {
    private final ArrayList<Food> foodList;

    public recycleAdapter(ArrayList<Food> foodList) {
        this.foodList = foodList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name1Txt;

        Button name;
        public MyViewHolder(final View view){
            super(view);

            name1Txt = view.findViewById(R.id.textView10);
            name = view.findViewById(R.id.button);
        }
    }


    @NonNull
    @Override
    public recycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodlist_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull recycleAdapter.MyViewHolder holder, int position) {
        String name = "";
        if (Locale.getDefault().getLanguage().equals("ru")) {
            name = foodList.get(position).getName_ru();
        } else if (Locale.getDefault().getLanguage().equals("en")) {
            name = foodList.get(position).getName_en();
        }

        holder.name1Txt.setText(name);
        if (Locale.getDefault().getLanguage().equals("ru")) {
            holder.name.setText(foodList.get(position).getPrice() + " тг");
        } else if (Locale.getDefault().getLanguage().equals("en")) {
            holder.name.setText(foodList.get(position).getPrice() + " tg");
        }

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
