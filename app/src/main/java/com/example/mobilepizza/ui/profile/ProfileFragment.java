package com.example.mobilepizza.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.example.mobilepizza.AuthActivity;
import com.example.mobilepizza.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    // Google profile elements
    ConstraintLayout google_profile;
    ImageView google_profile_photo;
    TextView google_name, google_phone;
    Button google_logout_button;

    // Phone profile elements
    ConstraintLayout phone_profile;
    ImageView phone_profile_photo;
    TextView phone_name, phone_phone_number;
    Button phone_logout_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser.getProviderData().get(1).getProviderId().equals("google.com")) {
            google_profile = view.findViewById(R.id.google_profile);
            google_profile.setVisibility(View.VISIBLE);

            google_profile_photo = view.findViewById(R.id.google_profile_photo);

            Picasso.get().load(currentUser.getPhotoUrl()).into(google_profile_photo);

            google_name = view.findViewById(R.id.google_name);
            google_name.setText(currentUser.getDisplayName());

            google_phone = view.findViewById(R.id.google_phone);
            if (!Objects.equals(currentUser.getPhoneNumber(), "")) {
                google_phone.setText(currentUser.getPhoneNumber());
            } else {
                view.findViewById(R.id.card_google_phone).setVisibility(View.GONE);
                view.findViewById(R.id.google_phone_title).setVisibility(View.GONE);
            }

            google_logout_button = view.findViewById(R.id.google_logout_button);
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
            phone_profile = view.findViewById(R.id.phone_profile);
            phone_profile.setVisibility(View.VISIBLE);

            phone_phone_number = view.findViewById(R.id.phone_phone_number);
            phone_phone_number.setText(currentUser.getPhoneNumber());

            phone_logout_button = view.findViewById(R.id.phone_logout_button);
            phone_logout_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();

                    startActivity(new Intent(getContext(), AuthActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            });
        }

        return view;
    }
}