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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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

    View view;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference db_ref;

    ProgressBar progressBar;

    // Google profile elements
    ConstraintLayout google_profile;
    ImageView google_profile_photo;
    TextView google_name, google_phone, google_email, phone_title;
    Button google_logout_button, add_phone_number;
    CardView phone_card;

    // Phone profile elements
    ConstraintLayout phone_profile;
    ImageView phone_profile_photo;
    TextView phone_name, phone_phone_number;
    Button phone_logout_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        db_ref = database.getReference();

        progressBar = view.findViewById(R.id.progressBar2);

        // Google profile elements
        google_profile = view.findViewById(R.id.google_profile);
        add_phone_number = view.findViewById(R.id.add_phone_number_button);
        phone_card = view.findViewById(R.id.card_google_phone);
        phone_title = view.findViewById(R.id.google_phone_title);
        google_profile_photo = view.findViewById(R.id.google_profile_photo);
        google_name = view.findViewById(R.id.google_name);
        google_phone = view.findViewById(R.id.google_phone);
        google_logout_button = view.findViewById(R.id.google_logout_button);
        google_email = view.findViewById(R.id.google_email);

        // Phone profile elements
        phone_logout_button = view.findViewById(R.id.phone_logout_button);
        phone_phone_number = view.findViewById(R.id.phone_phone_number);
        phone_profile = view.findViewById(R.id.phone_profile);

        getUserProfile(view);

        return view;
    }

    public void showDialog(Activity activity) {
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

                    add_phone_number.setText(getString(R.string.edit_phone_number));
                    Toast.makeText(view.getContext(), getString(R.string.add_phone_success), Toast.LENGTH_SHORT).show();

                    dialog.dismiss();

                    google_phone.setText(phone_input);

                    phone_card.setVisibility(View.VISIBLE);
                    phone_title.setVisibility(View.VISIBLE);
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
                    showDialog(getActivity());
                }
            });

            Picasso.get().load(profile.getPhotoUrl()).into(google_profile_photo);

            google_name.setText(profile.getDisplayName());
            db_ref.child("users").child(currentUser.getUid()).child("phone_number").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!String.valueOf(snapshot.getValue()).equals("null")) {
                        add_phone_number.setText(getString(R.string.edit_phone_number));
                        google_phone.setText(snapshot.getValue().toString());
                        google_phone.setVisibility(View.VISIBLE);
                    } else {
                        view.findViewById(R.id.card_google_phone).setVisibility(View.GONE);
                        view.findViewById(R.id.google_phone_title).setVisibility(View.GONE);
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
        } else if (currentUser.getProviderData().get(1).getProviderId().equals("phone")) {
            phone_profile.setVisibility(View.VISIBLE);
            phone_phone_number.setText(currentUser.getPhoneNumber());
            phone_logout_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();

                    startActivity(new Intent(getContext(), AuthActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            });

            progressBar.setVisibility(View.GONE);
        }
    }
}