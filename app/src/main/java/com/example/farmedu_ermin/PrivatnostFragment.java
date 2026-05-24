package com.example.farmedu_ermin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PrivatnostFragment extends Fragment {

    private Switch switchLokacija, switchAnalitika, switchNotif;
    private LinearLayout btnClearData, btnPolicy;

    private SharedPreferences preferences;

    public PrivatnostFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_privatnost, container, false);

        // BACK BUTTON
        ImageView btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        // INIT
        switchLokacija = view.findViewById(R.id.switchLokacija);
        switchAnalitika = view.findViewById(R.id.switchAnalitika);
        switchNotif = view.findViewById(R.id.switchNotif);

        btnClearData = view.findViewById(R.id.btnClearData);
        btnPolicy = view.findViewById(R.id.btnPolicy);

        // SHARED PREFERENCES
        preferences = requireActivity()
                .getSharedPreferences("PrivatnostSettings", Context.MODE_PRIVATE);

        // LOAD SAVED STATES
        switchLokacija.setChecked(
                preferences.getBoolean("lokacija", false)
        );

        switchAnalitika.setChecked(
                preferences.getBoolean("analitika", false)
        );

        switchNotif.setChecked(
                preferences.getBoolean("notif", false)
        );

        // =========================================
        // LOKACIJA
        // =========================================

        switchLokacija.setOnCheckedChangeListener((buttonView, isChecked) -> {

            preferences.edit()
                    .putBoolean("lokacija", isChecked)
                    .apply();

            if (isChecked) {

                Toast.makeText(
                        getContext(),
                        "Pristup lokaciji uključen",
                        Toast.LENGTH_SHORT
                ).show();

                showAlert(
                        "Lokacija aktivirana",
                        "Aplikacija sada može koristiti GPS za lokalne informacije i farm monitoring."
                );

            } else {

                Toast.makeText(
                        getContext(),
                        "Pristup lokaciji isključen",
                        Toast.LENGTH_SHORT
                ).show();

                showAlert(
                        "Lokacija isključena",
                        "GPS pristup je deaktiviran. Neke funkcije možda neće raditi pravilno."
                );
            }
        });

        // =========================================
        // ANALITIKA
        // =========================================

        switchAnalitika.setOnCheckedChangeListener((buttonView, isChecked) -> {

            preferences.edit()
                    .putBoolean("analitika", isChecked)
                    .apply();

            if (isChecked) {

                Toast.makeText(
                        getContext(),
                        "Analitika uključena",
                        Toast.LENGTH_SHORT
                ).show();

                showAlert(
                        "Analitika aktivirana",
                        "Pomažete poboljšanju aplikacije slanjem anonimnih podataka korištenja."
                );

            } else {

                Toast.makeText(
                        getContext(),
                        "Analitika isključena",
                        Toast.LENGTH_SHORT
                ).show();

                showAlert(
                        "Analitika isključena",
                        "Anonimno praćenje aktivnosti je deaktivirano."
                );
            }
        });

        // =========================================
        // PERSONALIZIRANE OBAVIJESTI
        // =========================================

        switchNotif.setOnCheckedChangeListener((buttonView, isChecked) -> {

            preferences.edit()
                    .putBoolean("notif", isChecked)
                    .apply();

            if (isChecked) {

                Toast.makeText(
                        getContext(),
                        "Personalizirane obavijesti uključene",
                        Toast.LENGTH_SHORT
                ).show();

                showAlert(
                        "Pametne preporuke uključene",
                        "Dobijat ćete prilagođene savjete, upozorenja i preporuke."
                );

            } else {

                Toast.makeText(
                        getContext(),
                        "Personalizirane obavijesti isključene",
                        Toast.LENGTH_SHORT
                ).show();

                showAlert(
                        "Obavijesti isključene",
                        "Više nećete primati personalizirane preporuke."
                );
            }
        });

        // =========================================
        // OBRISI PODATKE
        // =========================================

        btnClearData.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(requireContext());

            builder.setTitle("Obrisati podatke?");
            builder.setMessage(
                    "Da li ste sigurni da želite obrisati sve spremljene postavke i lokalne podatke aplikacije?"
            );

            builder.setPositiveButton("Obriši", (dialog, which) -> {

                preferences.edit().clear().apply();

                switchLokacija.setChecked(false);
                switchAnalitika.setChecked(false);
                switchNotif.setChecked(false);

                Toast.makeText(
                        getContext(),
                        "Svi podaci su obrisani",
                        Toast.LENGTH_LONG
                ).show();

                showAlert(
                        "Podaci obrisani",
                        "Sve privatne postavke i lokalni podaci su uspješno uklonjeni."
                );

            });

            builder.setNegativeButton("Odustani", null);

            builder.show();

        });

        // =========================================
        // PRIVACY POLICY
        // =========================================

        btnPolicy.setOnClickListener(v -> {

            showAlert(
                    "Pravila privatnosti",
                    "Vaši podaci su zaštićeni i koriste se isključivo za funkcionalnosti aplikacije. Nikada ne dijelimo privatne informacije bez vaše dozvole."
            );

        });

        return view;
    }

    // =========================================
    // CUSTOM ALERT
    // =========================================

    private void showAlert(String title, String message) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(requireContext());

        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("U redu", null);

        builder.show();
    }
}