package com.example.farmedu_ermin;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class LoginFragment extends Fragment {

    private EditText etEmail, etPassword;

    private Button btnLogin;

    private TextView tvGoRegister;

    private ImageView btnBack;
    private ImageView btnLanguage;

    private FirebaseAuth mAuth;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_login,
                container,
                false
        );

        etEmail =
                view.findViewById(R.id.etEmail);

        etPassword =
                view.findViewById(R.id.etPassword);

        btnLogin =
                view.findViewById(R.id.btnLogin);

        tvGoRegister =
                view.findViewById(R.id.tvGoRegister);

        btnBack =
                view.findViewById(R.id.btnBack);

        btnLanguage =
                view.findViewById(R.id.btnLanguage);

        mAuth = FirebaseAuth.getInstance();

        // 🔙 BACK

        btnBack.setOnClickListener(v -> {

            if (requireActivity()
                    .getSupportFragmentManager()
                    .getBackStackEntryCount() > 0) {

                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack();

            } else {

                requireActivity().finish();
            }
        });

        // 🌍 LANGUAGE

        btnLanguage.setOnClickListener(v ->
                showLanguageDialog()
        );

        // 🔐 LOGIN

        btnLogin.setOnClickListener(v ->
                loginUser()
        );

        // 🔑 FORGOT PASSWORD

        TextView tvForgotPassword =
                view.findViewById(R.id.tvForgotPassword);

        tvForgotPassword.setOnClickListener(v -> {

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new ForgotPassword())
                    .addToBackStack(null)
                    .commit();

        });

        // 🔁 REGISTER

        tvGoRegister.setOnClickListener(v -> {

            if (getActivity() instanceof MainActivity) {

                ((MainActivity) requireActivity())
                        .openRegisterFragment();
            }
        });

        return view;
    }

    // 🔐 LOGIN

    private void loginUser() {

        String email =
                etEmail.getText()
                        .toString()
                        .trim();

        String password =
                etPassword.getText()
                        .toString()
                        .trim();

        // EMAIL VALIDACIJA

        if (TextUtils.isEmpty(email)) {

            etEmail.setError("Unesi email");

            etEmail.requestFocus();

            return;
        }

        // PASSWORD VALIDACIJA

        if (TextUtils.isEmpty(password)) {

            etPassword.setError("Unesi lozinku");

            etPassword.requestFocus();

            return;
        }




        // LOADING

        btnLogin.setEnabled(false);

        btnLogin.setText("Prijava...");

        mAuth.signInWithEmailAndPassword(
                        email,
                        password
                )
                .addOnCompleteListener(task -> {

                    if (!isAdded()) return;

                    btnLogin.setEnabled(true);

                    btnLogin.setText("Prijavi se");

                    // ✅ LOGIN USPJEŠAN

                    if (task.isSuccessful()) {

                        Toast.makeText(
                                requireContext(),
                                "Login uspješan ✅",
                                Toast.LENGTH_SHORT
                        ).show();

                        Intent intent =
                                new Intent(
                                        requireActivity(),
                                        DashboardActivity.class
                                );

                        intent.addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        );

                        startActivity(intent);

                        requireActivity().finish();
                    }

                    // ❌ GREŠKA

                    else {

                        String error =
                                "Greška pri prijavi";

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

    // 🌍 SET LANGUAGE

    private void setLocale(String lang) {

        Locale locale =
                new Locale(lang);

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