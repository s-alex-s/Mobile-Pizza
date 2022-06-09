package com.example.mobilepizza.ui.cart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilepizza.R;
import com.example.mobilepizza.adapters.CartRecycleAdapter;
import com.example.mobilepizza.classes.CartItems;
import com.google.android.gms.tasks.OnSuccessListener;
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
    Button button;
    RelativeLayout relativeLayout;

    ValueEventListener valueEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        progressBar = view.findViewById(R.id.progressBar7);
        root_cart = view.findViewById(R.id.root_cart);
        title = view.findViewById(R.id.cart_title_text);
        button = view.findViewById(R.id.button);
        relativeLayout = view.findViewById(R.id.relativeLayout);

        cartRecyclerView = view.findViewById(R.id.cart_list);
        cartItems = new ArrayList<>();

        CartRecycleAdapter adapter = new CartRecycleAdapter(cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        cartRecyclerView.setAdapter(adapter);

        valueEventListener = new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();
                int sum = 0;
                int quantity = 0;

                for (DataSnapshot data : snapshot.getChildren()) {
                    cartItems.add(data.getValue(CartItems.class));

                    sum += data.getValue(CartItems.class).getPrice() *
                    data.getValue(CartItems.class).getQuantity();

                    quantity += data.getValue(CartItems.class).getQuantity();
                }

                if (cartItems.size() == 0) {
                    title.setText(getString(R.string.cart_empty));
                    relativeLayout.setVisibility(View.GONE);
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(quantity + " ");
                    if (cartItems.size() > 1) {
                        stringBuilder.append(getString(R.string.items));
                    } else {
                        stringBuilder.append(getString(R.string.item));
                    }
                    stringBuilder.append(" " + getString(R.string.amount) + " " + sum + "₸");

                    title.setText(stringBuilder.toString());
                    button.setText(getString(R.string.make_order) + " " + sum + "₸");
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
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int endIndex = cartItems.size();

                databaseReference.child("cart").child(user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        adapter.notifyItemRangeRemoved(0, endIndex);
                        Toast.makeText(getContext(), getString(R.string.order_added), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        databaseReference.child("cart").child(user.getUid()).addValueEventListener(valueEventListener);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        databaseReference.child("cart").child(user.getUid()).removeEventListener(valueEventListener);
    }
}