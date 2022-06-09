package com.example.mobilepizza.adapters;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Locale;

public class CartRecycleAdapter extends RecyclerView.Adapter<CartRecycleAdapter.MyViewHolder> {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference push;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    private String lastAction = "";

    private final ArrayList<CartItems> cartItems;

    public CartRecycleAdapter(ArrayList<CartItems> cartItems) {
        this.cartItems = cartItems;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, settings, price, quantity, plus, minus;
        ProgressBar progressBar;

        public MyViewHolder(final View view) {
            super(view);

            imageView = view.findViewById(R.id.imageView5);
            name = view.findViewById(R.id.textView);
            settings = view.findViewById(R.id.textView10);
            price = view.findViewById(R.id.textView11);
            quantity = view.findViewById(R.id.textView12);
            plus = view.findViewById(R.id.textView14);
            minus = view.findViewById(R.id.textView13);
            progressBar = view.findViewById(R.id.progressBar6);
        }
    }

    @NonNull
    @Override
    public CartRecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItems.get(holder.getAdapterPosition())
                        .setQuantity(cartItems.get(holder.getAdapterPosition()).getQuantity() + 1);

                holder.price.setText(cartItems.get(holder.getAdapterPosition()).getPrice() *
                        cartItems.get(holder.getAdapterPosition()).getQuantity() + "₸");
                holder.quantity.setText(cartItems.get(holder.getAdapterPosition()).getQuantity() + "");

                databaseReference.child("cart").child(mAuth.getCurrentUser().getUid())
                        .child(cartItems.get(holder.getAdapterPosition()).getKey())
                        .setValue(cartItems.get(holder.getAdapterPosition()));

                lastAction = "ed";
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cartItems.get(holder.getAdapterPosition())
                            .setQuantity(cartItems.get(holder.getAdapterPosition()).getQuantity() - 1);

                    holder.price.setText(cartItems.get(holder.getAdapterPosition()).getPrice() *
                            cartItems.get(holder.getAdapterPosition()).getQuantity() + "₸");
                    holder.quantity.setText(cartItems.get(holder.getAdapterPosition()).getQuantity() + "");

                    databaseReference.child("cart").child(mAuth.getCurrentUser().getUid())
                            .child(cartItems.get(holder.getAdapterPosition()).getKey())
                            .setValue(cartItems.get(holder.getAdapterPosition()));

                    lastAction = "ed";

                    if (cartItems.get(holder.getAdapterPosition()).getQuantity() == 0) {
                        databaseReference.child("cart").child(mAuth.getCurrentUser().getUid())
                                .child(cartItems.get(holder.getAdapterPosition()).getKey())
                                .removeValue();

                        cartItems.remove(holder.getAdapterPosition());
                        CartRecycleAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                        lastAction = "rm";
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.e("addCart", "too many requests");
                }
            }
        });

        if (Locale.getDefault().getLanguage().equals("ru")) {
            holder.name.setText(cartItems.get(position).getName_ru());
            holder.settings.setText(cartItems.get(position).getSettings_ru());
        } else if (Locale.getDefault().getLanguage().equals("en")) {
            holder.name.setText(cartItems.get(position).getName_en());
            holder.settings.setText(cartItems.get(position).getSettings_en());
        }

        holder.price.setText(cartItems.get(position).getPrice() * cartItems.get(position).getQuantity() + "₸");
        holder.quantity.setText(cartItems.get(position).getQuantity() + "");

        storageReference.child(cartItems.get(position).getImg()).getDownloadUrl()
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

    public String getLastAction() {
        return lastAction;
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
