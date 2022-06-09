package com.example.mobilepizza.ui.cart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilepizza.R;
import com.example.mobilepizza.adapters.CartRecycleAdapter;
import com.example.mobilepizza.classes.CartItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    ArrayList<CartItems> cartItems;
    RecyclerView cartRecyclerView;

    ProgressBar progressBar;
    FrameLayout root_cart;
    TextView title;

    ValueEventListener valueEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        progressBar = view.findViewById(R.id.progressBar7);
        root_cart = view.findViewById(R.id.root_cart);
        title = view.findViewById(R.id.cart_title_text);

        cartRecyclerView = view.findViewById(R.id.cart_list);
        cartItems = new ArrayList<>();

        CartRecycleAdapter adapter = new CartRecycleAdapter(cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        cartRecyclerView.setAdapter(adapter);

        valueEventListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    if (cartItems.contains(data.getValue(CartItems.class))) {
                        for (int i = 0; i < cartItems.size(); i++) {
                            if (cartItems.get(i).equals(data.getValue(CartItems.class))) {
                                cartItems.get(i).setQuantity(cartItems.get(i).getQuantity() + 1);
                            }
                        }
                    } else {
                        cartItems.add(data.getValue(CartItems.class));
                    }
                }

                if (cartItems.size() == 0) {
                    title.setText(getString(R.string.cart_empty));
                }

                if (adapter.getLastAction().equals("ed")) {
                    adapter.notifyDataSetChanged();
                }

                progressBar.setVisibility(View.GONE);
                root_cart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        };

        databaseReference.child("cart").child(user.getUid()).addValueEventListener(valueEventListener);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        databaseReference.child("cart").child(user.getUid()).removeEventListener(valueEventListener);
    }
}