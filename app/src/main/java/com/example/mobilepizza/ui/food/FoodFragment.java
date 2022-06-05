package com.example.mobilepizza.ui.food;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobilepizza.FoodClass;
import com.example.mobilepizza.R;
import com.example.mobilepizza.recycleAdapter;

import java.util.ArrayList;

public class FoodFragment extends Fragment {
    private ArrayList<FoodClass> foodList;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);
        recyclerView = view.findViewById(R.id.recycleView2);
        foodList = new ArrayList<>();

        setUserinfo();
        setAdapter(view);

        return view;
    }

    private void setAdapter(View view) {
        recycleAdapter adapter = new recycleAdapter(foodList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setUserinfo() {
        FoodClass pizza1 = new FoodClass();
        pizza1.setName_ru("Сырные палочки с песто");
        pizza1.setName_en("Pesto cheese sticks");
        pizza1.setPrice("2000");

        FoodClass pizza2 = new FoodClass();
        pizza2.setName_en("Pepperoni");
        pizza2.setName_ru("Пепперони");
        pizza2.setPrice("1000");

        foodList.add(pizza1);
        foodList.add(pizza2);
    }
}