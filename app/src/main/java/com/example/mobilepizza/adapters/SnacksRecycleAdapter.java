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
import com.example.mobilepizza.classes.FoodClasses.Snacks;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Locale;

public class SnacksRecycleAdapter extends RecyclerView.Adapter<SnacksRecycleAdapter.MyViewHolder> {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    private final ArrayList<Snacks> snacksList;

    public SnacksRecycleAdapter(ArrayList<Snacks> snacksList) {
        this.snacksList = snacksList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView snackName;
        ImageView imageView;
        Button button;
        ProgressBar progressBar;

        public MyViewHolder(final View view) {
            super(view);

            snackName = view.findViewById(R.id.foodactivity_name);
            imageView = view.findViewById(R.id.food_img);
            button = view.findViewById(R.id.add_cart_button);
            progressBar = view.findViewById(R.id.progressBar4);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.foodlist_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FoodActivity.class);
                intent.putExtra("food", snacksList.get(holder.getAdapterPosition()));
                intent.putExtra("type", "snack");
                holder.itemView.getContext().startActivity(intent);
            }
        });

        String name = "";
        if (Locale.getDefault().getLanguage().equals("ru")) {
            name = snacksList.get(position).getName_ru();
        } else if (Locale.getDefault().getLanguage().equals("en")) {
            name = snacksList.get(position).getName_en();
        }

        holder.snackName.setText(name);
        if (Locale.getDefault().getLanguage().equals("ru")) {
            holder.button.setText(holder.itemView.getContext().getString(R.string.from) + " " +
                    snacksList.get(position).getPrice() + "₸");
        } else if (Locale.getDefault().getLanguage().equals("en")) {
            holder.button.setText(holder.itemView.getContext().getString(R.string.from) + " " +
                    snacksList.get(position).getPrice() + "₸");
        }

        storageReference.child(snacksList.get(position).getImg()).getDownloadUrl()
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
        return snacksList.size();
    }
}
