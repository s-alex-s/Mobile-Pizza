package com.example.mobilepizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.mobilepizza.adapters.HistoryRecyclerAdapter;
import com.example.mobilepizza.classes.HistoryItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersHistoryActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ValueEventListener valueEventListener;

    ArrayList<HistoryItem> historyItems;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_history);

        historyItems = new ArrayList<>();
        recyclerView = findViewById(R.id.history_activity_recycler);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<HistoryItem> to_reverse = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    to_reverse.add(data.getValue(HistoryItem.class));
                }

                for (int i = to_reverse.size() - 1; i != -1; i--) {
                    historyItems.add(to_reverse.get(i));
                }

                HistoryRecyclerAdapter adapter = new HistoryRecyclerAdapter(historyItems);
                recyclerView.setLayoutManager(new LinearLayoutManager(OrdersHistoryActivity.this));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrdersHistoryActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        };

        databaseReference.child("orders_history").child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        databaseReference.child("orders_history").child(mAuth.getCurrentUser().getUid())
                .removeEventListener(valueEventListener);
    }
}