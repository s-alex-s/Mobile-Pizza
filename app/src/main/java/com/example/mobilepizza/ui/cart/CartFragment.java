package com.example.mobilepizza.ui.cart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.mobilepizza.classes.HistoryItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CartFragment extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    DatabaseReference push;

    ArrayList<CartItems> cartItems;
    RecyclerView cartRecyclerView;

    ProgressBar progressBar;
    FrameLayout root_cart;
    TextView title;
    Button button;
    RelativeLayout relativeLayout;
    ImageView imageView;

    ValueEventListener valueEventListener;

    int sum;
    int quantity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        progressBar = view.findViewById(R.id.progressBar7);
        root_cart = view.findViewById(R.id.root_cart);
        title = view.findViewById(R.id.cart_title_text);
        button = view.findViewById(R.id.button);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        imageView = view.findViewById(R.id.imageView8);

        cartRecyclerView = view.findViewById(R.id.cart_list);
        cartItems = new ArrayList<>();

        CartRecycleAdapter adapter = new CartRecycleAdapter(cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        cartRecyclerView.setAdapter(adapter);

        valueEventListener = new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sum = 0;
                quantity = 0;

                cartItems.clear();

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
                    if (quantity > 1) {
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
        
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.cart_clear));
                builder.setMessage(getString(R.string.cart_warn));

                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == DialogInterface.BUTTON_POSITIVE) {
                            databaseReference.child("cart").child(user.getUid()).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @SuppressLint("NotifyDataSetChanged")
                                        @Override
                                        public void onSuccess(Void unused) {
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(getContext(), getString(R.string.cart_cleared), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                };

                builder.setPositiveButton(getString(R.string.yes), listener);
                builder.setNegativeButton(getString(R.string.no), listener);

                builder.create().show();
            }
        });
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeOrderDialog(getActivity(), adapter);
            }
        });

        databaseReference.child("cart").child(user.getUid()).addValueEventListener(valueEventListener);

        return view;
    }

    public void getDeliveryAddressDialog(Activity activity, CartRecycleAdapter adapter) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.get_delivery_address);

        EditText editText = dialog.findViewById(R.id.address_edittext_dialog);
        Button button = dialog.findViewById(R.id.address_dialog_button);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar11);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().replace(" ", "").equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    databaseReference.child("users").child(user.getUid()).child("delivery_address").setValue(editText.getText().toString().trim())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(activity, getString(R.string.add_address_success), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    makeOrderDialog(activity, adapter);
                                    dialog.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(activity, getString(R.string.error), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                } else {
                    editText.setError(getString(R.string.address_incorrect));
                }
            }
        });

        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    public void makeOrderDialog(Activity activity, CartRecycleAdapter adapter) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.order_window);

        LinearLayout order_root = dialog.findViewById(R.id.order_root);
        ProgressBar progressBar1 = dialog.findViewById(R.id.progressBar8);
        TextView address = dialog.findViewById(R.id.textView16);
        databaseReference.child("users").child(user.getUid()).child("delivery_address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!String.valueOf(snapshot.getValue()).equals("null")) {
                    address.setText(getString(R.string.profile_address) + ": " + snapshot.getValue().toString());
                } else {
                    getDeliveryAddressDialog(activity, adapter);
                    dialog.dismiss();
                }

                progressBar1.setVisibility(View.GONE);
                order_root.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });

        TextView order_cost = dialog.findViewById(R.id.textView17);
        order_cost.setText(getString(R.string.cost_order) + ": " + sum + "₸");

        Button button2 = dialog.findViewById(R.id.button5);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeliveryAddressDialog(getActivity(), adapter);
                dialog.dismiss();
            }
        });

        Button button = dialog.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("users").child(user.getUid()).child("delivery_address").addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HistoryItem historyItem = new HistoryItem();
                        historyItem.setTime(new SimpleDateFormat("dd.MM.yyyy HH:mm")
                                .format(Calendar.getInstance().getTime()));
                        historyItem.setAddress(snapshot.getValue().toString());
                        ArrayList<String> images = new ArrayList<>();
                        for (int i = 0; i < cartItems.size(); i++) {
                            if (!images.contains(cartItems.get(i).getImg())) {
                                images.add(cartItems.get(i).getImg());
                            }
                        }
                        historyItem.setImages(images);
                        historyItem.setSum(sum);
                        historyItem.setCartItems(cartItems);

                        push = databaseReference.child("orders_history").child(user.getUid()).push();
                        historyItem.setKey(push.getKey());
                        push.setValue(historyItem);


                        int endIndex = cartItems.size();
                        databaseReference.child("cart").child(user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                adapter.notifyItemRangeRemoved(0, endIndex);
                                Toast.makeText(getContext(), getString(R.string.order_added), Toast.LENGTH_SHORT).show();
                            }
                        });

                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        databaseReference.child("cart").child(user.getUid()).removeEventListener(valueEventListener);
    }
}