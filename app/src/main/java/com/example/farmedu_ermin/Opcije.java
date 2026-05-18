package com.example.farmedu_ermin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class Opcije extends Fragment {

    private Switch switchObavijesti;

    private SharedPreferences prefs;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view =
                inflater.inflate(
                        R.layout.fragment_opcije,
                        container,
                        false
                );

        // PREFS

        prefs = requireActivity()
                .getSharedPreferences(
                        "FarmEduSettings",
                        Context.MODE_PRIVATE
                );

        // INIT

        LinearLayout btnJezik =
                view.findViewById(R.id.btnJezik);

        LinearLayout btnOApp =
                view.findViewById(R.id.btnOApp);

        LinearLayout btnOcijena =
                view.findViewById(R.id.btnOcijena);

        LinearLayout btnPodrska =
                view.findViewById(R.id.btnPodrska);

        LinearLayout btnSigurnostkopija =
                view.findViewById(R.id.btnSigurnostkopija);

        LinearLayout btnprivatnost =
                view.findViewById(R.id.btnprivatnost);

        LinearLayout btnProfilkorisnika =
                view.findViewById(R.id.btnProfilkorisnika);

        LinearLayout btnPets =
                view.findViewById(R.id.btnPets);

        switchObavijesti =
                view.findViewById(R.id.switchObavijesti);

        // 🔥 LOAD SAVED STATE

        boolean notificationsEnabled =
                prefs.getBoolean(
                        "notifications_enabled",
                        true
                );

        switchObavijesti.setChecked(
                notificationsEnabled
        );

        // 🔥 SWITCH LISTENER

        switchObavijesti.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    prefs.edit()
                            .putBoolean(
                                    "notifications_enabled",
                                    isChecked
                            )
                            .apply();

                    if (isChecked) {

                        Toast.makeText(
                                getContext(),
                                "Obavijesti uključene",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        Toast.makeText(
                                getContext(),
                                "Obavijesti isključene",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        // CLICK EVENTS

        btnJezik.setOnClickListener(v ->
                showLanguageDialog()
        );

        btnOApp.setOnClickListener(v ->
                openOAppFragment()
        );

        btnOcijena.setOnClickListener(v ->
                openOcijeniAplikaciju()
        );

        btnPodrska.setOnClickListener(v ->
                openPodrskaAplikaciju()
        );

        btnSigurnostkopija.setOnClickListener(v ->
                openSigurnostikopija()
        );

        btnprivatnost.setOnClickListener(v ->
                openPrivatnost()
        );

        btnProfilkorisnika.setOnClickListener(v ->
                openProfilKOrisnika()
        );

        btnPets.setOnClickListener(v ->
                openPets()
        );

        return view;
    }

    // 🔥 CENTRAL NAVIGATION

    private void openFragment(Fragment fragment) {

        if (!isAdded()) return;

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragmentContainer,
                        fragment
                )
                .commit();
    }

    private void openOAppFragment() {

        openFragment(
                new FragmentOAplikaciji()
        );
    }

    private void openOcijeniAplikaciju() {

        openFragment(
                new OcjeniAplikacijuFragment()
        );
    }

    private void openPodrskaAplikaciju() {

        openFragment(
                new PomoiciPodrskaFragment()
        );
    }

    private void openSigurnostikopija() {

        openFragment(
                new SigurnostiKopijaFragment()
        );
    }

    private void openPrivatnost() {

        openFragment(
                new PrivatnostFragment()
        );
    }

    private void openProfilKOrisnika() {

        openFragment(
                new ProfileFragment()
        );
    }

    private void openPets() {

        openFragment(
                new OmiljeniPetsFragment()
        );
    }

    // 🌍 LANGUAGE

    private void showLanguageDialog() {

        String[] jezici = {

                getString(R.string.bosanski_jezik),
                getString(R.string.engleski_jezik),
                getString(R.string.njemacki_jezik)
        };

        new AlertDialog.Builder(
                requireContext()
        )
                .setTitle(
                        getString(
                                R.string.odaberi_jezik
                        )
                )
                .setItems(
                        jezici,
                        (dialog, which) -> {

                            if (which == 0) {

                                setLocale("bs");

                            } else if (which == 1) {

                                setLocale("en");

                            } else {

                                setLocale("de");
                            }
                        }
                )
                .show();
    }

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