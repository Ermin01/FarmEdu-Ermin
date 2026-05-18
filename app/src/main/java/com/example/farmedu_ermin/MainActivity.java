package com.example.farmedu_ermin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.farmedu_ermin.adapters.LocaleHelper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button btnStart, btnRegister;
    private View fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 🔥 Firebase init
        FirebaseApp.initializeApp(this);

        // 🌍 Language
        LocaleHelper.setLocale(this, LocaleHelper.getLanguage(this));

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.buttonstartaplikacija);
        btnRegister = findViewById(R.id.buttonRegister);
        fragmentContainer = findViewById(R.id.fragmentContainer);

        // ✅ AKO JE USER VEĆ LOGOVAN → preskoči login
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
            return;
        }

        // 🚀 START → LOGIN (FIX)
        btnStart.setOnClickListener(v -> openLoginFragment());

        // 📝 REGISTER
        btnRegister.setOnClickListener(v -> openRegisterFragment());

        // 🔙 BACK HANDLER
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                    getSupportFragmentManager().popBackStack();

                    btnStart.setVisibility(View.VISIBLE);
                    btnRegister.setVisibility(View.VISIBLE);
                    fragmentContainer.setVisibility(View.GONE);

                } else {
                    finish();
                }
            }
        });

        // 📱 EDGE FIX
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bgImage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // 🔥 OPEN REGISTER (PUBLIC da radi iz fragmenta)
    public void openRegisterFragment() {

        Fragment fragment = new RegisterFragment();

        btnStart.setVisibility(View.GONE);
        btnRegister.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.VISIBLE);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                )
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    // 🔐 OPEN LOGIN (PUBLIC)
    public void openLoginFragment() {

        Fragment fragment = new LoginFragment();

        btnStart.setVisibility(View.GONE);
        btnRegister.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.VISIBLE);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                )
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    // 🌍 LANGUAGE
    @Override
    protected void attachBaseContext(Context newBase) {
        String lang = LocaleHelper.getLanguage(newBase);
        super.attachBaseContext(newBase);
        LocaleHelper.setLocale(newBase, lang);
    }
}