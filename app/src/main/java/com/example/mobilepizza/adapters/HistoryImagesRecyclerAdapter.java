package com.example.mobilepizza.adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobilepizza.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HistoryImagesRecyclerAdapter extends RecyclerView.Adapter<HistoryImagesRecyclerAdapter.MyViewHolder> {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference reference = storage.getReference();

    private final ArrayList<String> images;

    public HistoryImagesRecyclerAdapter(ArrayList<String> images) {
        this.images = images;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.imageView6);
            progressBar = view.findViewById(R.id.progressBar9);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_image, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        reference.child(images.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    Glide.with(holder.itemView.getContext())
                            .load(uri)
                            .into(holder.imageView);
                } catch (IllegalArgumentException e) {
                    Log.e("HIRA", "IllegalArgumentException");
                }

                holder.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
