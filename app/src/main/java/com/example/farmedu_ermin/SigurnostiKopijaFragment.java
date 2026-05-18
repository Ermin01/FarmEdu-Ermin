package com.example.farmedu_ermin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SigurnostiKopijaFragment extends Fragment {

    private FirebaseFirestore db;

    private String uid;

    private TextView tvLastBackup;
    private TextView tvStatus;

    private Switch switchAutoBackup;

    private LinearLayout btnBackupNow;
    private LinearLayout btnRestore;

    private SharedPreferences prefs;

    public SigurnostiKopijaFragment() {}

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_sigurnosti_kopija,
                container,
                false
        );

        ImageView btnBack =
                view.findViewById(R.id.btnBack);

        tvLastBackup =
                view.findViewById(R.id.tvLastBackup);

        tvStatus =
                view.findViewById(R.id.tvStatus);

        switchAutoBackup =
                view.findViewById(R.id.switchAutoBackup);

        btnBackupNow =
                view.findViewById(R.id.btnBackupNow);

        btnRestore =
                view.findViewById(R.id.btnRestore);

        db = FirebaseFirestore.getInstance();

        uid = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid();

        prefs = requireActivity()
                .getSharedPreferences(
                        "BackupPrefs",
                        requireContext().MODE_PRIVATE
                );

        // LOAD SWITCH STATE

        boolean autoBackup =
                prefs.getBoolean(
                        "auto_backup",
                        false
                );

        switchAutoBackup.setChecked(autoBackup);

        updateStatus(autoBackup);

        // LAST BACKUP

        String lastBackup =
                prefs.getString(
                        "last_backup",
                        "Nikad"
                );

        tvLastBackup.setText(
                "Zadnji backup: " + lastBackup
        );

        // BACK

        btnBack.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()
        );

        // SWITCH

        switchAutoBackup.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    prefs.edit()
                            .putBoolean(
                                    "auto_backup",
                                    isChecked
                            )
                            .apply();

                    updateStatus(isChecked);

                    Toast.makeText(
                            getContext(),
                            isChecked
                                    ? "Auto backup uključen"
                                    : "Auto backup isključen",
                            Toast.LENGTH_SHORT
                    ).show();
                });

        // BACKUP NOW

        btnBackupNow.setOnClickListener(v ->
                makeBackup()
        );

        // RESTORE

        btnRestore.setOnClickListener(v ->
                restoreBackup()
        );

        return view;
    }

    // UPDATE STATUS

    private void updateStatus(boolean active) {

        if (active) {

            tvStatus.setText("Aktivan");

        } else {

            tvStatus.setText("Neaktivan");
        }
    }

    // MAKE BACKUP

    private void makeBackup() {

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (!documentSnapshot.exists()) {

                        Toast.makeText(
                                getContext(),
                                "Nema podataka za backup",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    Map<String, Object> backup =
                            new HashMap<>();

                    backup.put(
                            "name",
                            documentSnapshot.getString("name")
                    );

                    backup.put(
                            "city",
                            documentSnapshot.getString("city")
                    );

                    backup.put(
                            "about",
                            documentSnapshot.getString("about")
                    );

                    backup.put(
                            "avatar",
                            documentSnapshot.getLong("avatar")
                    );

                    db.collection("backups")
                            .document(uid)
                            .set(backup)
                            .addOnSuccessListener(unused -> {

                                String date =
                                        new SimpleDateFormat(
                                                "dd.MM.yyyy HH:mm",
                                                Locale.getDefault()
                                        ).format(new Date());

                                prefs.edit()
                                        .putString(
                                                "last_backup",
                                                date
                                        )
                                        .apply();

                                tvLastBackup.setText(
                                        "Zadnji backup: " + date
                                );

                                Toast.makeText(
                                        getContext(),
                                        "Backup uspješno sačuvan",
                                        Toast.LENGTH_SHORT
                                ).show();

                            });
                });
    }

    // RESTORE BACKUP

    private void restoreBackup() {

        db.collection("backups")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (!documentSnapshot.exists()) {

                        Toast.makeText(
                                getContext(),
                                "Backup nije pronađen",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    db.collection("users")
                            .document(uid)
                            .set(documentSnapshot.getData())
                            .addOnSuccessListener(unused ->

                                    Toast.makeText(
                                            getContext(),
                                            "Podaci vraćeni",
                                            Toast.LENGTH_SHORT
                                    ).show()
                            );
                });
    }
}