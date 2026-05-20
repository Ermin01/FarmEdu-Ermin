package com.example.farmedu_ermin.utils;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.farmedu_ermin.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordUser extends Fragment {

    // ====================================================
    // VIEWS
    // ====================================================

    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private EditText etConfirmPassword;

    private LinearLayout btnSavePassword;

    // ====================================================
    // FIREBASE
    // ====================================================

    private FirebaseAuth auth;
    private FirebaseUser user;

    // ====================================================
    // CONSTRUCTOR
    // ====================================================

    public ForgotPasswordUser() {
        // Required empty public constructor
    }

    // ====================================================
    // ON CREATE VIEW
    // ====================================================

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_forgot_password_user,
                container,
                false
        );

        // ====================================================
        // INIT
        // ====================================================

        etCurrentPassword =
                view.findViewById(R.id.etCurrentPassword);

        etNewPassword =
                view.findViewById(R.id.etNewPassword);

        etConfirmPassword =
                view.findViewById(R.id.etConfirmPassword);

        btnSavePassword =
                view.findViewById(R.id.btnSavePassword);

        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();

        // ====================================================
        // BUTTON CLICK
        // ====================================================

        btnSavePassword.setOnClickListener(v -> {

            changePassword();

        });

        return view;
    }

    // ====================================================
    // CHANGE PASSWORD
    // ====================================================

    private void changePassword() {

        String currentPassword =
                etCurrentPassword.getText()
                        .toString()
                        .trim();

        String newPassword =
                etNewPassword.getText()
                        .toString()
                        .trim();

        String confirmPassword =
                etConfirmPassword.getText()
                        .toString()
                        .trim();

        // ====================================================
        // VALIDATION
        // ====================================================

        if (TextUtils.isEmpty(currentPassword)) {

            etCurrentPassword.setError(
                    "Unesite trenutnu lozinku"
            );

            return;
        }

        if (TextUtils.isEmpty(newPassword)) {

            etNewPassword.setError(
                    "Unesite novu lozinku"
            );

            return;
        }

        if (newPassword.length() < 6) {

            etNewPassword.setError(
                    "Lozinka mora imati najmanje 6 karaktera"
            );

            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {

            etConfirmPassword.setError(
                    "Potvrdite novu lozinku"
            );

            return;
        }

        if (!newPassword.equals(confirmPassword)) {

            etConfirmPassword.setError(
                    "Lozinke se ne poklapaju"
            );

            return;
        }

        if (user == null) {

            Toast.makeText(
                    getContext(),
                    "Korisnik nije prijavljen",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // ====================================================
        // GET EMAIL
        // ====================================================

        String email = user.getEmail();

        if (email == null || email.isEmpty()) {

            Toast.makeText(
                    getContext(),
                    "Email nije pronađen",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // ====================================================
        // DISABLE BUTTON
        // ====================================================

        btnSavePassword.setEnabled(false);

        // ====================================================
        // REAUTHENTICATE
        // ====================================================

        AuthCredential credential =
                EmailAuthProvider.getCredential(
                        email,
                        currentPassword
                );

        user.reauthenticate(credential)

                .addOnSuccessListener(unused -> {

                    // ====================================================
                    // UPDATE PASSWORD
                    // ====================================================

                    user.updatePassword(newPassword)

                            .addOnSuccessListener(unused1 -> {

                                btnSavePassword.setEnabled(true);

                                Toast.makeText(
                                        getContext(),
                                        "Lozinka uspješno promijenjena",
                                        Toast.LENGTH_LONG
                                ).show();

                                // CLEAR FIELDS

                                etCurrentPassword.setText("");

                                etNewPassword.setText("");

                                etConfirmPassword.setText("");

                            })

                            .addOnFailureListener(e -> {

                                btnSavePassword.setEnabled(true);

                                Toast.makeText(
                                        getContext(),
                                        e.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();

                            });

                })

                .addOnFailureListener(e -> {

                    btnSavePassword.setEnabled(true);

                    Toast.makeText(
                            getContext(),
                            "Trenutna lozinka nije tačna",
                            Toast.LENGTH_LONG
                    ).show();

                });
    }
}