package com.example.farmedu_ermin;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends Fragment {

    private EditText etResetEmail;

    private Button btnSendReset;

    private ImageView btnBack;

    private ImageView loadingSpinner;

    private FirebaseAuth mAuth;

    public ForgotPassword() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_forgot_password,
                container,
                false
        );

        etResetEmail =
                view.findViewById(R.id.etResetEmail);

        btnSendReset =
                view.findViewById(R.id.btnSendReset);

        btnBack =
                view.findViewById(R.id.btnBack);

        loadingSpinner =
                view.findViewById(R.id.loadingSpinner);

        mAuth = FirebaseAuth.getInstance();

        // 🔙 BACK

        btnBack.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()
        );

        // 🔐 RESET PASSWORD

        btnSendReset.setOnClickListener(v ->
                resetPassword()
        );

        return view;
    }

    private void resetPassword() {

        String email =
                etResetEmail.getText()
                        .toString()
                        .trim();

        // ✅ VALIDACIJA

        if (TextUtils.isEmpty(email)) {

            etResetEmail.setError(
                    "Unesite email adresu"
            );

            etResetEmail.requestFocus();

            return;
        }

        // 🌪️ SHOW SPINNER

        loadingSpinner.setVisibility(View.VISIBLE);

        loadingSpinner.animate()
                .rotationBy(1440f)
                .setDuration(2000)
                .setInterpolator(new LinearInterpolator())
                .start();

        // 🔒 DISABLE BUTTON

        btnSendReset.setEnabled(false);

        btnSendReset.setText("Šaljemo link...");

        btnSendReset.animate()
                .alpha(0.7f)
                .setDuration(300)
                .start();

        // ⏳ PREMIUM DELAY

        new Handler().postDelayed(() -> {

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {

                        if (!isAdded()) return;

                        loadingSpinner.clearAnimation();
                        loadingSpinner.setVisibility(View.GONE);

                        btnSendReset.setEnabled(true);
                        btnSendReset.setText("Pošalji link");

                        if (task.isSuccessful()) {

                            Toast.makeText(
                                    requireContext(),
                                    "RESET EMAIL POSLAN ✅",
                                    Toast.LENGTH_LONG
                            ).show();

                        } else {

                            Exception e = task.getException();

                            Toast.makeText(
                                    requireContext(),
                                    "ERROR: " + e,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });

        }, 2500);
    }
}