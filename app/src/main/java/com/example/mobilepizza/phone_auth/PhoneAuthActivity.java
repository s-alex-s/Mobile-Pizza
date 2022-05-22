package com.example.mobilepizza.phone_auth;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilepizza.MainActivity;
import com.example.mobilepizza.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    Button button, code_button;
    CountryCodePicker codePicker;
    EditText phone_number_text, code_text;
    LinearLayout phone_view, code_view;
    ImageView before;
    String phone_number;
    TextView resend_button, code_desc;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.mobilepizza.R.layout.activity_phone_auth);

        mAuth = FirebaseAuth.getInstance();
        mAuth.useAppLanguage();

        button = findViewById(R.id.phone_button);
        code_button = findViewById(R.id.code_button);
        code_text = findViewById(R.id.editTextCode);
        code_desc = findViewById(R.id.code_desc);
        codePicker = findViewById(R.id.code_picker);
        phone_number_text = findViewById(R.id.editTextPhone);
        phone_view = findViewById(R.id.phone_view);
        code_view = findViewById(R.id.code_view);
        resend_button = findViewById(R.id.resend_b);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);

        before = findViewById(R.id.before_button);
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        resend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(phone_number)
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(PhoneAuthActivity.this)
                                .setCallbacks(mCallbacks)
                                .setForceResendingToken(mResendToken)
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d("TAG", "onVerificationCompleted:" + phoneAuthCredential);

                Toast.makeText(PhoneAuthActivity.this, getString(R.string.auto_conf), Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w("TAG", "onVerificationFailed", e);

                progressDialog.dismiss();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.e("TAG", "Invalid request");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.e("TAG", "The SMS quota for the project has been exceeded");
                }
                Toast.makeText(PhoneAuthActivity.this, getString(R.string.verif_error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                Log.d("TAG", "onCodeSent:" + s);

                mVerificationId = s;
                mResendToken = forceResendingToken;

                phone_view.setVisibility(View.GONE);
                code_view.setVisibility(View.VISIBLE);

                progressDialog.dismiss();
                Toast.makeText(PhoneAuthActivity.this, getString(R.string.sent_code), Toast.LENGTH_SHORT).show();
            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (phone_number_text.length() == 10) {
                    progressDialog.setMessage(getString(R.string.verifying_phone));
                    progressDialog.show();

                    phone_number = codePicker.getSelectedCountryCodeWithPlus() +
                            phone_number_text.getText().toString();

                    code_desc.setText(getString(R.string.code_description) + " " + phone_number);

                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber(phone_number)
                                    .setTimeout(60L, TimeUnit.SECONDS)
                                    .setActivity(PhoneAuthActivity.this)
                                    .setCallbacks(mCallbacks)
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                } else {
                    phone_number_text.setError(getString(R.string.number_error));
                }
            }
        });

        code_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (code_text.getText().toString().length() == 6) {
                    progressDialog.setMessage(getString(R.string.verifying_code));
                    progressDialog.show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code_text.getText().toString());

                    signInWithPhoneAuthCredential(credential);
                } else {
                    code_text.setError(getString(R.string.code_incorrect));
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithCredential:success");
                            progressDialog.dismiss();

                            startActivity(new Intent(PhoneAuthActivity.this, MainActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        } else {
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                progressDialog.dismiss();

                                Toast.makeText(PhoneAuthActivity.this,
                                        getString(R.string.code_error), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}