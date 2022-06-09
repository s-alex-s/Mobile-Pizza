package com.example.mobilepizza.ui.food;

import android.annotation.SuppressLint;
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
import com.example.mobilepizza.adapters.DrinksRecycleAdapter;
import com.example.mobilepizza.adapters.PizzaRecycleAdapter;
import com.example.mobilepizza.adapters.SnacksRecycleAdapter;
import com.example.mobilepizza.classes.FoodClasses.Drinks;
import com.example.mobilepizza.classes.FoodClasses.Pizza;
import com.example.mobilepizza.classes.FoodClasses.Snacks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    ValueEventListener valueEventListener;

    ArrayList<Pizza> pizzaList;
    RecyclerView pizzaRecyclerView;

    ArrayList<Snacks> snacksList;
    RecyclerView snacksRecyclerView;

    ArrayList<Drinks> drinksList;
    RecyclerView drinksRecyclerView;

    ProgressBar progressBar;
    ScrollView root_food;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_food, container, false);

        progressBar = view.findViewById(R.id.progressBar3);
        root_food = view.findViewById(R.id.root_food);

        pizzaRecyclerView = view.findViewById(R.id.pizzaRecycleView);
        pizzaList = new ArrayList<>();

        snacksRecyclerView = view.findViewById(R.id.snacksRecycleView);
        snacksList = new ArrayList<>();

        drinksRecyclerView = view.findViewById(R.id.drinksRecycleView);
        drinksList = new ArrayList<>();

        PizzaRecycleAdapter adapter = new PizzaRecycleAdapter(pizzaList);
        pizzaRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        pizzaRecyclerView.setAdapter(adapter);

        SnacksRecycleAdapter snacksAdapter = new SnacksRecycleAdapter(snacksList);
        snacksRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        snacksRecyclerView.setAdapter(snacksAdapter);

        DrinksRecycleAdapter drinksAdapter = new DrinksRecycleAdapter(drinksList);
        drinksRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        drinksRecyclerView.setAdapter(drinksAdapter);

        valueEventListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pizzaList.clear();
                snacksList.clear();
                drinksList.clear();

                for (DataSnapshot data : snapshot.child("pizza").getChildren()) {
                    pizzaList.add(data.getValue(Pizza.class));
                }

                for (DataSnapshot data : snapshot.child("snacks").getChildren()) {
                    snacksList.add(data.getValue(Snacks.class));
                }

                for (DataSnapshot data : snapshot.child("drinks").getChildren()) {
                    drinksList.add(data.getValue(Drinks.class));
                }

                adapter.notifyDataSetChanged();
                snacksAdapter.notifyDataSetChanged();
                drinksAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
                root_food.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        };

        databaseReference.addValueEventListener(valueEventListener);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        databaseReference.removeEventListener(valueEventListener);
    }
}