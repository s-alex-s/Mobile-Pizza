package com.example.mobilepizza;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mobilepizza.classes.FoodClasses.Drinks;
import com.example.mobilepizza.classes.FoodClasses.Pizza;
import com.example.mobilepizza.classes.FoodClasses.Snacks;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;

public class FoodActivity extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    Bundle arguments;

    ImageView imageView;
    TextView food_name, food_description, food_settings;
    Button button;
    RadioGroup dough, size;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        progressBar = findViewById(R.id.progressBar5);

        arguments = getIntent().getExtras();
        if (arguments.get("type").equals("pizza")) {
            setPizzaView((Pizza) arguments.get("food"));
        } else if (arguments.get("type").equals("snack")) {
            setSnacksView((Snacks) arguments.get("food"));
        } else if (arguments.get("type").equals("drink")) {
            setDrinksView((Drinks) arguments.get("food"));
        }
    }

    private void setSnacksView(Snacks food) {

    }

    private void setDrinksView(Drinks food) {

    }

    public void setPizzaView(Pizza food) {
        imageView = findViewById(R.id.foodactivity_image);
        storageReference.child(food.getImg()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(FoodActivity.this)
                                .load(uri)
                                .into(imageView);
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("fbstorage", e.getMessage());
                    }
                });

        dough = findViewById(R.id.pizzaactivity_dough);
        dough.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton dough_value = findViewById(dough.getCheckedRadioButtonId());
                if (dough_value.getText().toString().equals(getString(R.string.traditional_dough))) {
                    food.setDough_ru("Традиционное");
                    food.setDough_en("Traditional");
                } else if (dough_value.getText().toString().equals(getString(R.string.thin_dough))) {
                    food.setDough_ru("Тонкое");
                    food.setDough_en("Thin");
                }

                updatePizzaSettings(food);
            }
        });

        size = findViewById(R.id.pizzaactivity_size);
        size.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton size_value = findViewById(size.getCheckedRadioButtonId());
                if (size_value.getText().toString().equals(getString(R.string.small_pizza))) {
                    food.setSize("25");
                } else if (size_value.getText().toString().equals(getString(R.string.medium_pizza))) {
                    food.setSize("30");
                } else if (size_value.getText().toString().equals(getString(R.string.big_pizza))) {
                    food.setSize("35");
                }

                updatePizzaSettings(food);
            }
        });

        food_name = findViewById(R.id.foodactivity_name);
        food_description = findViewById(R.id.foodactivity_desc);
        food_settings = findViewById(R.id.foodactivity_settings);

        if (Locale.getDefault().getLanguage().equals("ru")) {
            food_name.setText(food.getName_ru());
            food_description.setText(food.getDescription_ru());
        } else if (Locale.getDefault().getLanguage().equals("en")) {
            food_name.setText(food.getName_en());
            food_description.setText(food.getDescription_en());
        }

        button = findViewById(R.id.add_cart_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        updatePizzaSettings(food);
    }

    public void updatePizzaSettings(Pizza food) {
        StringBuilder stringBuilder = new StringBuilder();

        switch (food.getSize()) {
            case "30":
                stringBuilder.append(getString(R.string.medium_pizza));
                break;
            case "25":
                stringBuilder.append(getString(R.string.small_pizza));
                break;
            case "35":
                stringBuilder.append(getString(R.string.big_pizza));
                break;
        }

        stringBuilder
                .append(" ")
                .append(food.getSize())
                .append(" ")
                .append(getString(R.string.size))
                .append(", ");

        if (Locale.getDefault().getLanguage().equals("ru")) {
            stringBuilder.append(food.getDough_ru().toLowerCase())
                    .append(" тесто");
        } else if (Locale.getDefault().getLanguage().equals("en")) {
            stringBuilder.append(food.getDough_en().toLowerCase())
                    .append(" dough");
        }

        food_settings.setText(stringBuilder.toString());
    }
}