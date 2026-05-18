package com.example.farmedu_ermin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.farmedu_ermin.chat.ChatFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = FirebaseFirestore.getInstance();

        BottomNavigationView bottomNav =
                findViewById(R.id.bottomNav);

        // =====================================
        // DEFAULT TAB
        // =====================================

        if (savedInstanceState == null) {

            openTab(new Home());
        }

        // =====================================
        // BOTTOM NAVIGATION
        // =====================================

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.itHome) {

                openTab(new Home());
                return true;

            } else if (id == R.id.poljoprivreda) {

                openTab(new Poljoprivreda());
                return true;

            } else if (id == R.id.zivotnje) {

                openTab(new ZivotnjeStocarstvoFragment());
                return true;

            } else if (id == R.id.chat) {

                openTab(new ChatFragment());
                return true;

            } else if (id == R.id.itOpcije) {

                openTab(new Opcije());
                return true;
            }

            return false;
        });
    }

    // =====================================
    // ONLINE STATUS
    // =====================================

    @Override
    protected void onStart() {

        super.onStart();

        updateOnlineStatus(true);
    }

    @Override
    protected void onResume() {

        super.onResume();

        updateOnlineStatus(true);
    }

    @Override
    protected void onPause() {

        super.onPause();

        updateOnlineStatus(false);
    }

    @Override
    protected void onStop() {

        super.onStop();

        updateOnlineStatus(false);
    }

    // =====================================
    // UPDATE USER STATUS
    // =====================================

    private void updateOnlineStatus(boolean online) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        String uid =
                FirebaseAuth.getInstance()
                        .getUid();

        if (uid == null) {
            return;
        }

        db.collection("users")
                .document(uid)
                .update(
                        "online", online,
                        "lastSeen", System.currentTimeMillis()
                );
    }

    // =====================================
    // OPEN TAB
    // =====================================

    private void openTab(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}