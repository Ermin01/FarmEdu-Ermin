package com.example.farmedu_ermin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private EditText etName, etEmail, etPassword, etConfirm;

    private Button btnRegister;

    private ImageView btnBack;
    private ImageView btnLanguage;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public RegisterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_register,
                container,
                false
        );

        // INIT
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirm = view.findViewById(R.id.etConfirm);

        btnRegister = view.findViewById(R.id.btnRegister);

        btnBack = view.findViewById(R.id.btnBack);
        btnLanguage = view.findViewById(R.id.btnLanguage);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // 🔙 BACK
        btnBack.setOnClickListener(v -> {

            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();

        });

        // 🌍 LANGUAGE
        btnLanguage.setOnClickListener(v ->
                showLanguageDialog()
        );

        // 📝 REGISTER
        btnRegister.setOnClickListener(v ->
                registerUser()
        );

        return view;
    }

    private void registerUser() {

        String name =
                etName.getText()
                        .toString()
                        .trim();

        String email =
                etEmail.getText()
                        .toString()
                        .trim();

        String password =
                etPassword.getText()
                        .toString()
                        .trim();

        String confirm =
                etConfirm.getText()
                        .toString()
                        .trim();

        // ✅ NAME
        if (TextUtils.isEmpty(name)) {

            etName.setError("Unesi ime");
            etName.requestFocus();

            return;
        }

        // ✅ EMAIL
        if (TextUtils.isEmpty(email)) {

            etEmail.setError("Unesi email");
            etEmail.requestFocus();

            return;
        }

        // ✅ PASSWORD
        if (TextUtils.isEmpty(password)) {

            etPassword.setError("Unesi lozinku");
            etPassword.requestFocus();

            return;
        }

        // ✅ MIN PASSWORD
        if (password.length() < 6) {

            etPassword.setError(
                    "Lozinka mora imati min 6 karaktera"
            );

            etPassword.requestFocus();

            return;
        }

        // ✅ CONFIRM
        if (!password.equals(confirm)) {

            etConfirm.setError(
                    "Lozinke se ne poklapaju"
            );

            etConfirm.requestFocus();

            return;
        }

        // 🔒 LOADING
        btnRegister.setEnabled(false);
        btnRegister.setText("Registracija...");

        // 🔥 FIREBASE REGISTER
        mAuth.createUserWithEmailAndPassword(
                        email,
                        password
                )
                .addOnCompleteListener(task -> {

                    if (!isAdded()) return;

                    btnRegister.setEnabled(true);
                    btnRegister.setText("Registruj se");

                    // ✅ SUCCESS
                    if (task.isSuccessful()) {

                        FirebaseUser user =
                                mAuth.getCurrentUser();

                        if (user == null) return;

                        // 🔥 UPDATE FIREBASE NAME
                        UserProfileChangeRequest updates =
                                new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();

                        user.updateProfile(updates);

                        // 🔥 FIRESTORE USER DATA
                        Map<String, Object> userData =
                                new HashMap<>();

                        userData.put("name", name);
                        userData.put("email", email);
                        userData.put("city", "");
                        userData.put("about", "");
                        userData.put("avatar", R.drawable.user);

                        db.collection("users")
                                .document(user.getUid())
                                .set(userData)
                                .addOnSuccessListener(unused -> {

                                    Toast.makeText(
                                            requireContext(),
                                            "Registracija uspješna ✅",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    // 🔓 LOGOUT
                                    mAuth.signOut();

                                    // 🔁 OPEN LOGIN
                                    if (getActivity() instanceof MainActivity) {

                                        ((MainActivity) requireActivity())
                                                .openLoginFragment();
                                    }

                                })
                                .addOnFailureListener(e -> {

                                    Toast.makeText(
                                            requireContext(),
                                            "Firestore greška",
                                            Toast.LENGTH_LONG
                                    ).show();
                                });

                    }

                    // ❌ ERROR
                    else {

                        String error =
                                "Greška pri registraciji";

                        if (task.getException() != null) {

                            error =
                                    task.getException()
                                            .getMessage();
                        }

                        Toast.makeText(
                                requireContext(),
                                error,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    // 🌍 LANGUAGE DIALOG
    private void showLanguageDialog() {

        String[] jezici = {
                "Bosanski",
                "English",
                "Deutsch"
        };

        new AlertDialog.Builder(requireContext())
                .setTitle("Odaberi jezik")
                .setItems(jezici, (dialog, which) -> {

                    if (which == 0) {

                        setLocale("bs");

                    } else if (which == 1) {

                        setLocale("en");

                    } else {

                        setLocale("de");
                    }

                }).show();
    }

    // 🌍 SET LOCALE
    private void setLocale(String lang) {

        Locale locale = new Locale(lang);

        Locale.setDefault(locale);

        Configuration config =
                new Configuration();

        config.setLocale(locale);

        requireActivity()
                .getResources()
                .updateConfiguration(
                        config,
                        requireActivity()
                                .getResources()
                                .getDisplayMetrics()
                );

        SharedPreferences prefs =
                requireActivity()
                        .getSharedPreferences(
                                "Settings",
                                Context.MODE_PRIVATE
                        );

        prefs.edit()
                .putString("lang", lang)
                .apply();

        requireActivity().recreate();
    }
}