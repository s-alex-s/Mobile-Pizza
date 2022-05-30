package com.example.mobilepizza.ui.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mobilepizza.AuthActivity;
import com.example.mobilepizza.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference db_ref;

    ProgressBar progressBar;

    // Google profile elements
    ScrollView google_profile;
    ImageView google_profile_photo;
    TextView google_name, google_phone, google_email, phone_title, google_address;
    Button google_logout_button;
    CardView add_phone_number, add_google_address;

    // Phone profile elements
    ScrollView phone_profile;
    TextView phone_name, phone_phone_number, phone_address;
    Button phone_logout_button;
    CardView add_name, add_phone_address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        db_ref = database.getReference();

        progressBar = view.findViewById(R.id.progressBar2);

        // Google profile elements
        google_profile = view.findViewById(R.id.google_profile);
        add_phone_number = view.findViewById(R.id.google_phone);
        add_google_address = view.findViewById(R.id.google_address);
        phone_title = view.findViewById(R.id.google_phone_title);
        google_profile_photo = view.findViewById(R.id.google_profile_photo);
        google_name = view.findViewById(R.id.google_name_text);
        google_address = view.findViewById(R.id.google_address_text);
        google_phone = view.findViewById(R.id.google_phone_text);
        google_logout_button = view.findViewById(R.id.google_logout_button);
        google_email = view.findViewById(R.id.google_email);

        // Phone profile elements
        phone_logout_button = view.findViewById(R.id.phone_logout_button);
        phone_phone_number = view.findViewById(R.id.phone_phone_number);
        phone_address = view.findViewById(R.id.phone_address_text);
        phone_name = view.findViewById(R.id.phone_name_text);
        phone_profile = view.findViewById(R.id.phone_profile);
        add_phone_address = view.findViewById(R.id.phone_address);
        add_name = view.findViewById(R.id.phone_name_card);

        getUserProfile(view);

        return view;
    }

    public void getPhoneNameDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.get_name);

        EditText editText = dialog.findViewById(R.id.name_edittext_dialog);
        editText.setText(phone_name.getText().toString());
        Button button = dialog.findViewById(R.id.name_dialog_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().replace(" ", "").equals("")) {
                    db_ref.child("users").child(currentUser.getUid()).child("user_name").setValue(editText.getText().toString().trim());
                    Toast.makeText(activity, getString(R.string.add_phone_name_success), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    phone_name.setText(editText.getText().toString().trim());
                } else {
                    editText.setError(getString(R.string.add_phone_name));
                }
            }
        });

        dialog.show();
    }

    public void getPhoneDeliveryAddressDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.get_delivery_address);

        EditText editText = dialog.findViewById(R.id.address_edittext_dialog);
        editText.setText(phone_address.getText().toString());
        Button button = dialog.findViewById(R.id.address_dialog_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().replace(" ", "").equals("")) {
                    db_ref.child("users").child(currentUser.getUid()).child("delivery_address").setValue(editText.getText().toString().trim());
                    Toast.makeText(activity, getString(R.string.add_address_success), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    phone_address.setText(editText.getText().toString().trim());
                } else {
                    editText.setError(getString(R.string.address_incorrect));
                }
            }
        });

        dialog.show();
    }

    public void getGoogleDeliveryAddressDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.get_delivery_address);

        EditText editText = dialog.findViewById(R.id.address_edittext_dialog);
        editText.setText(google_address.getText().toString());
        Button button = dialog.findViewById(R.id.address_dialog_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().replace(" ", "").equals("")) {
                    db_ref.child("users").child(currentUser.getUid()).child("delivery_address").setValue(editText.getText().toString().trim());
                    Toast.makeText(activity, getString(R.string.add_address_success), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    google_address.setText(editText.getText().toString().trim());
                } else {
                    editText.setError(getString(R.string.address_incorrect));
                }
            }
        });

        dialog.show();
    }

    public void getPhoneNumberDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.get_phone_number);

        EditText editText = dialog.findViewById(R.id.editTextPhone_dialog);
        CountryCodePicker ccp = dialog.findViewById(R.id.code_picker_dialog);
        Button button = dialog.findViewById(R.id.add_phone_number_dialog);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editText.getText().toString().length() == 10) {
                    String phone_input = ccp.getSelectedCountryCodeWithPlus() + editText.getText().toString();
                    db_ref.child("users").child(currentUser.getUid()).child("phone_number").setValue(phone_input);
                    Toast.makeText(activity, getString(R.string.add_phone_success), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    google_phone.setText(phone_input);
                } else {
                    editText.setError(getString(R.string.number_error));
                }
            }
        });

        dialog.show();
    }

    public void getUserProfile(View view) {
        if (currentUser.getProviderData().get(1).getProviderId().equals("google.com")) {

            UserInfo profile = currentUser.getProviderData().get(1);

            add_phone_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPhoneNumberDialog(getActivity());
                }
            });

            add_google_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getGoogleDeliveryAddressDialog(getActivity());
                }
            });

            Picasso.get().load(profile.getPhotoUrl()).into(google_profile_photo);

            google_name.setText(profile.getDisplayName());

            google_email.setText(profile.getEmail());

            google_logout_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(
                            GoogleSignInOptions.DEFAULT_SIGN_IN).build();
                    GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getContext(), signInOptions);
                    googleSignInClient.signOut();

                    mAuth.signOut();

                    startActivity(new Intent(getContext(), AuthActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            });

            db_ref.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!String.valueOf(snapshot.child("phone_number").getValue()).equals("null")) {
                        google_phone.setText(snapshot.child("phone_number").getValue().toString());
                    }

                    if (!String.valueOf(snapshot.child("delivery_address").getValue()).equals("null")) {
                        google_address.setText(snapshot.child("delivery_address").getValue().toString());
                    }
                    progressBar.setVisibility(View.GONE);
                    google_profile.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("firebase", error.getMessage());
                    Toast.makeText(view.getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (currentUser.getProviderData().get(1).getProviderId().equals("phone")) {

            phone_phone_number.setText(currentUser.getPhoneNumber());

            phone_logout_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();

                    startActivity(new Intent(getContext(), AuthActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            });

            add_phone_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPhoneDeliveryAddressDialog(getActivity());
                }
            });

            add_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPhoneNameDialog(getActivity());
                }
            });

            db_ref.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!String.valueOf(snapshot.child("user_name").getValue()).equals("null")) {
                        phone_name.setText(snapshot.child("user_name").getValue().toString());
                    }

                    if (!String.valueOf(snapshot.child("delivery_address").getValue()).equals("null")) {
                        phone_address.setText(snapshot.child("delivery_address").getValue().toString());
                    }
                    progressBar.setVisibility(View.GONE);
                    phone_profile.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("firebase", error.getMessage());
                    Toast.makeText(view.getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}