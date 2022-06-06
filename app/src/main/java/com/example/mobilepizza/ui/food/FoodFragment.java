package com.example.mobilepizza.ui.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilepizza.R;
import com.example.mobilepizza.adapters.PizzaRecycleAdapter;
import com.example.mobilepizza.classes.Food;
import com.example.mobilepizza.classes.FoodClasses.Pizza;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    ArrayList<Food> pizzaList;
    RecyclerView pizzaRecyclerView;

    RecyclerView.LayoutManager layoutManager;

    ProgressBar progressBar;
    ScrollView root_food;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_food, container, false);

        progressBar = view.findViewById(R.id.progressBar3);
        root_food = view.findViewById(R.id.root_food);

        layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL, false);

        pizzaRecyclerView = view.findViewById(R.id.pizzaRecycleView);
        pizzaList = new ArrayList<>();

        setLists();

        return view;
    }

    private void setLists() {
        PizzaRecycleAdapter adapter = new PizzaRecycleAdapter(pizzaList);
        pizzaRecyclerView.setLayoutManager(layoutManager);
        pizzaRecyclerView.setAdapter(adapter);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pizzaList.clear();

                for (DataSnapshot data : snapshot.child("pizza").getChildren()) {
                    pizzaList.add(data.getValue(Pizza.class));
                }

                progressBar.setVisibility(View.GONE);
                root_food.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}