package com.example.mobilepizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilepizza.adapters.OrderViewRecyclerAdapter;
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

public class OrderViewActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    DatabaseReference push;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    HistoryItem historyItem;

    RecyclerView recyclerView;
    TextView sum_text, address_text, order_time_text;
    Button button;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);

        historyItem = (HistoryItem) getIntent().getSerializableExtra("order");

        sum_text = findViewById(R.id.textView23);
        sum_text.setText(getString(R.string.sum) + " " + historyItem.getSum() + "₸");

        address_text = findViewById(R.id.textView25);
        address_text.setText(historyItem.getAddress());

        order_time_text = findViewById(R.id.textView27);
        order_time_text.setText(historyItem.getTime());

        button = findViewById(R.id.button94);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInCart(historyItem.getCartItems());
            }
        });

        recyclerView = findViewById(R.id.order_view_recycler);

        OrderViewRecyclerAdapter adapter = new OrderViewRecyclerAdapter(historyItem.getCartItems());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void addInCart(ArrayList<CartItems> cartItems) {
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

                Toast.makeText(OrderViewActivity.this, getString(R.string.order_added_to_cart), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderViewActivity.this, getString(R.string.error),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}