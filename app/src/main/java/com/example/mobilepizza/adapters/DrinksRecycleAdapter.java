package com.example.mobilepizza.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobilepizza.FoodActivity;
import com.example.mobilepizza.R;
import com.example.mobilepizza.classes.FoodClasses.Drinks;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Locale;

public class DrinksRecycleAdapter extends RecyclerView.Adapter<DrinksRecycleAdapter.MyViewHolder> {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    private final ArrayList<Drinks> drinksList;

    public DrinksRecycleAdapter(ArrayList<Drinks> drinksList) {
        this.drinksList = drinksList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView drinkName;
        ImageView imageView;
        Button button;
        ProgressBar progressBar;

        public MyViewHolder(final View view) {
            super(view);

            drinkName = view.findViewById(R.id.foodactivity_name);
            imageView = view.findViewById(R.id.food_img);
            button = view.findViewById(R.id.add_cart_button);
            progressBar = view.findViewById(R.id.progressBar4);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodlist_items, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FoodActivity.class);
                intent.putExtra("food", drinksList.get(holder.getAdapterPosition()));
                intent.putExtra("type", "drink");
                holder.itemView.getContext().startActivity(intent);
            }
        });

        String name = "";
        if (Locale.getDefault().getLanguage().equals("ru")) {
            name = drinksList.get(position).getName_ru();
        } else {
            name = drinksList.get(position).getName_en();
        }

        holder.drinkName.setText(name);
        holder.button.setText(drinksList.get(position).getPrice() + "₸");

        storageReference.child(drinksList.get(position).getImg()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(holder.itemView.getContext())
                                .load(uri)
                                .into(holder.imageView);
                        holder.progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("fbstorage", e.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return drinksList.size();
    }
}
