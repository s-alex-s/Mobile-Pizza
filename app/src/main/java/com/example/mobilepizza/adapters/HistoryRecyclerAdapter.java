package com.example.mobilepizza.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilepizza.OrderViewActivity;
import com.example.mobilepizza.R;
import com.example.mobilepizza.classes.CartItems;
import com.example.mobilepizza.classes.HistoryItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.MyViewHolder> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    DatabaseReference push;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private final ArrayList<HistoryItem> historyItems;

    public HistoryRecyclerAdapter(ArrayList<HistoryItem> historyItems) {
        this.historyItems = historyItems;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date_text, address_text, sum_text;
        RecyclerView recyclerView;
        Button repeat_button, viewList_button;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);

            date_text = view.findViewById(R.id.textView19);
            address_text = view.findViewById(R.id.textView21);
            sum_text = view.findViewById(R.id.textView233);
            recyclerView = view.findViewById(R.id.history_item_recycler);
            repeat_button = view.findViewById(R.id.button4);
            viewList_button = view.findViewById(R.id.button3);
            cardView = view.findViewById(R.id.history_card_view);
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

        holder.repeat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInCart(historyItems.get(holder.getAdapterPosition()).getCartItems(), holder.itemView);
            }
        });

        holder.viewList_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), OrderViewActivity.class);
                intent.putExtra("order", historyItems.get(holder.getAdapterPosition()));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    public void addInCart(ArrayList<CartItems> cartItems, View view) {
        databaseReference.child("cart").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (CartItems cartItem : cartItems) {
                    if (snapshot.getChildrenCount() != 0) {
                        boolean add = true;

                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (cartItem.equals(data.getValue(CartItems.class))) {
                                add = false;
                                cartItem.setQuantity(data.getValue(CartItems.class).getQuantity() +
                                        cartItem.getQuantity());
                                cartItem.setKey(data.getValue(CartItems.class).getKey());

                                databaseReference
                                        .child("cart")
                                        .child(user.getUid())
                                        .child(cartItem.getKey())
                                        .setValue(cartItem);
                            }
                        }

                        if (add) {
                            push = databaseReference.child("cart").child(user.getUid()).push();
                            cartItem.setKey(push.getKey());
                            push.setValue(cartItem);
                        }
                    } else {
                        push = databaseReference.child("cart").child(user.getUid()).push();
                        cartItem.setKey(push.getKey());
                        push.setValue(cartItem);
                    }
                }

                Toast.makeText(view.getContext(), view.getContext()
                        .getString(R.string.order_added_to_cart), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), view.getContext().getString(R.string.error),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }
}
