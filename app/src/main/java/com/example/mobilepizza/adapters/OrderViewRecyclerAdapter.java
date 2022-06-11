package com.example.mobilepizza.adapters;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobilepizza.R;
import com.example.mobilepizza.classes.CartItems;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Locale;

public class OrderViewRecyclerAdapter extends RecyclerView.Adapter<OrderViewRecyclerAdapter.MyViewHolder> {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    private final ArrayList<CartItems> cartItems;

    public OrderViewRecyclerAdapter(ArrayList<CartItems> cartItems) {
        this.cartItems = cartItems;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, setting, cost;
        ImageView imageView;
        ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.textView28);
            setting = view.findViewById(R.id.textView29);
            cost = view.findViewById(R.id.textView30);
            imageView = view.findViewById(R.id.imageView7);
            progressBar = view.findViewById(R.id.progressBar10);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_view_item, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (Locale.getDefault().getLanguage().equals("ru")) {
            holder.name.setText(cartItems.get(position).getName_ru() + " (" +
                    cartItems.get(position).getQuantity() + " " +
                    holder.imageView.getContext().getString(R.string.quantity) + ")");
            holder.setting.setText(cartItems.get(position).getSettings_ru());
        } else {
            holder.name.setText(cartItems.get(position).getName_en() + " (" +
                    cartItems.get(position).getQuantity() + " " +
                    holder.imageView.getContext().getString(R.string.quantity) + ")");
            holder.setting.setText(cartItems.get(position).getSettings_en());
        }

        holder.cost.setText(cartItems.get(position).getPrice() * cartItems.get(position).getQuantity() + "₸");

        storageReference.child(cartItems.get(position).getImg()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.itemView.getContext())
                        .load(uri)
                        .into(holder.imageView);

                holder.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
