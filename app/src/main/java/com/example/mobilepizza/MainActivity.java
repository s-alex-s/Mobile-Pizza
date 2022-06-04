package com.example.mobilepizza;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilepizza.Foodclasses.Pizza;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Food> foodList;
    private RecyclerView recyclerView;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycleView2);
        foodList = new ArrayList<>();

        setUserinfo();
        setAdapter();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.profile_nav_item, R.id.food_nav_item, R.id.cart_nav_item,
                R.id.contacts_nav_item
        ).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void setAdapter() {
        recycleAdapter adapter = new recycleAdapter(foodList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setUserinfo() {
        Food pizza1 = new Food();
        pizza1.setName_ru("Сырные палочки с песто");
        pizza1.setName_en("Pesto cheese sticks");
        pizza1.setPrice("2000");

        Food pizza2 = new Food();
        pizza2.setName_en("Pepperoni");
        pizza2.setName_ru("Пепперони");
        pizza2.setPrice("1000");

        foodList.add(pizza1);
        foodList.add(pizza2);

    }
}