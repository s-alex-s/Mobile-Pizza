package com.example.mobilepizza;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mobilepizza.classes.CartItems;
import com.example.mobilepizza.classes.Food;
import com.example.mobilepizza.classes.FoodClasses.Drinks;
import com.example.mobilepizza.classes.FoodClasses.Pizza;
import com.example.mobilepizza.classes.FoodClasses.Snacks;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;

public class FoodActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference(), push;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

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

        food_name = findViewById(R.id.foodactivity_name);
        food_description = findViewById(R.id.foodactivity_desc);
        food_settings = findViewById(R.id.foodactivity_settings);
        dough = findViewById(R.id.pizzaactivity_dough);
        size = findViewById(R.id.pizzaactivity_size);
        imageView = findViewById(R.id.foodactivity_image);
        button = findViewById(R.id.add_cart_button);

        arguments = getIntent().getExtras();
        if (arguments.get("type").equals("pizza")) {
            setPizzaView((Pizza) arguments.get("food"));
        } else if (arguments.get("type").equals("snack")) {
            setSnacksView((Snacks) arguments.get("food"));
        } else if (arguments.get("type").equals("drink")) {
            setDrinksView((Drinks) arguments.get("food"));
        }
    }

    @SuppressLint("SetTextI18n")
    private void setSnacksView(Snacks food) {
        setFoodInfo(food);

        food_settings.setText("1 " + getString(R.string.quantity));
        dough.setVisibility(View.GONE);
        size.setVisibility(View.GONE);

        button.setText(getString(R.string.add_to_cart) + " " + food.getPrice() + "₸");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartItems cartItem = new CartItems();

                cartItem.setImg(food.getImg());
                cartItem.setName_ru(food.getName_ru());
                cartItem.setName_en(food.getName_en());
                cartItem.setPrice(Integer.parseInt(food.getPrice()));

                cartItem.setSettings_ru(food.getDescription_ru());
                cartItem.setSettings_en(food.getDescription_en());

                addInCart(cartItem);

                Toast.makeText(FoodActivity.this, getString(R.string.add_snack_success), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setDrinksView(Drinks food) {
        setFoodInfo(food);

        food_settings.setVisibility(View.GONE);
        dough.setVisibility(View.GONE);
        size.setVisibility(View.GONE);

        button.setText(getString(R.string.add_to_cart) + " " + food.getPrice() + "₸");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartItems cartItem = new CartItems();

                cartItem.setImg(food.getImg());
                cartItem.setName_ru(food.getName_ru());
                cartItem.setName_en(food.getName_en());
                cartItem.setPrice(Integer.parseInt(food.getPrice()));

                cartItem.setSettings_ru(food.getDescription_ru());
                cartItem.setSettings_en(food.getDescription_en());

                addInCart(cartItem);

                Toast.makeText(FoodActivity.this, getString(R.string.add_drink_success), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void setPizzaView(Pizza food) {
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

        setFoodInfo(food);

        button.setText(getString(R.string.add_to_cart) + " " + food.getPrice_medium() + "₸");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartItems cartItem = new CartItems();

                cartItem.setImg(food.getImg());
                cartItem.setName_ru(food.getName_ru());
                cartItem.setName_en(food.getName_en());
                switch (food.getSize()) {
                    case "30":
                        cartItem.setPrice(Integer.parseInt(food.getPrice_medium()));
                        break;
                    case "25":
                        cartItem.setPrice(Integer.parseInt(food.getPrice_small()));
                        break;
                    case "35":
                        cartItem.setPrice(Integer.parseInt(food.getPrice_big()));
                        break;
                }

                cartItem.setSettings_ru(getRuPizzaSettings(food));
                cartItem.setSettings_en(getEnPizzaSettings(food));

                addInCart(cartItem);

                Toast.makeText(FoodActivity.this, getString(R.string.add_pizza_success), Toast.LENGTH_SHORT).show();
            }
        });

        updatePizzaSettings(food);
    }

    public void addInCart(CartItems cartItem) {
        databaseReference.child("cart").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() != 0) {
                    boolean add = true;

                    for (DataSnapshot data : snapshot.getChildren()) {
                        if (cartItem.equals(data.getValue(CartItems.class))) {
                            if (data.getValue(CartItems.class).getQuantity() != 50) {
                                add = false;
                                cartItem.setQuantity(data.getValue(CartItems.class).getQuantity() +
                                        cartItem.getQuantity());
                                cartItem.setKey(data.getValue(CartItems.class).getKey());

                                databaseReference
                                        .child("cart")
                                        .child(user.getUid())
                                        .child(cartItem.getKey())
                                        .setValue(cartItem);
                            } else {
                                add = false;
                            }
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setFoodInfo(Food food) {
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

        if (Locale.getDefault().getLanguage().equals("ru")) {
            food_name.setText(food.getName_ru());
            food_description.setText(food.getDescription_ru());
        } else {
            food_name.setText(food.getName_en());
            food_description.setText(food.getDescription_en());
        }
    }

    @SuppressLint("SetTextI18n")
    public void updatePizzaSettings(Pizza food) {
        switch (food.getSize()) {
            case "25":
                button.setText(getString(R.string.add_to_cart) + " " + food.getPrice_small() + "₸");
                break;
            case "30":
                button.setText(getString(R.string.add_to_cart) + " " + food.getPrice_medium() + "₸");
                break;
            case "35":
                button.setText(getString(R.string.add_to_cart) + " " + food.getPrice_big() + "₸");
                break;
        }

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
        } else {
            stringBuilder.append(food.getDough_en().toLowerCase())
                    .append(" dough");
        }

        food_settings.setText(stringBuilder.toString());
    }

    @SuppressLint("SetTextI18n")
    public String getRuPizzaSettings(Pizza food) {
        StringBuilder stringBuilder = new StringBuilder();

        switch (food.getSize()) {
            case "30":
                stringBuilder.append("Средняя");
                break;
            case "25":
                stringBuilder.append("Маленькая");
                break;
            case "35":
                stringBuilder.append("Большая");
                break;
        }

        stringBuilder
                .append(" ")
                .append(food.getSize())
                .append(" ")
                .append("см")
                .append(", ");

        stringBuilder.append(food.getDough_ru().toLowerCase()).append(" тесто");

        return stringBuilder.toString();
    }

    @SuppressLint("SetTextI18n")
    public String getEnPizzaSettings(Pizza food) {
        StringBuilder stringBuilder = new StringBuilder();

        switch (food.getSize()) {
            case "30":
                stringBuilder.append("Medium");
                break;
            case "25":
                stringBuilder.append("Small");
                break;
            case "35":
                stringBuilder.append("Big");
                break;
        }

        stringBuilder
                .append(" ")
                .append(food.getSize())
                .append(" ")
                .append("cm")
                .append(", ");

        stringBuilder.append(food.getDough_en().toLowerCase()).append(" dough");

        return stringBuilder.toString();
    }
}