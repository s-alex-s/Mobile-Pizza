package com.example.mobilepizza.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.mobilepizza.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    Button logout_button;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        logout_button = view.findViewById(R.id.logout_button);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser.getProviderData().get(1).getProviderId().equals("google.com")) {
                        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(
                                GoogleSignInOptions.DEFAULT_SIGN_IN).build();
                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getContext(), signInOptions);
                        googleSignInClient.signOut();
                }
                mAuth.signOut();
            }
        });

        return view;
    }
}