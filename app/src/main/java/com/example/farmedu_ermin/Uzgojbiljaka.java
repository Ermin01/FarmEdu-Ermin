package com.example.farmedu_ermin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Uzgojbiljaka extends Fragment {

    public Uzgojbiljaka() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_uzgojbiljaka, container, false);

        // 🔙 BACK BUTTON → Poljoprivreda
        view.findViewById(R.id.btnBack).setOnClickListener(v ->

                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_left,
                                R.anim.slide_out_right,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                        )
                        .replace(R.id.fragmentContainer, new Poljoprivreda())
                        .commit()
        );

        // 🌱 POVRĆE
        view.findViewById(R.id.cardPovrce).setOnClickListener(v ->
                openFragment(v, new PovrceFragment())
        );

        // 🌾 ŽITARICE
        view.findViewById(R.id.cardZitarice).setOnClickListener(v ->
                openFragment(v, new ZitariceFragment())
        );

        // 🍎 VOĆE
        view.findViewById(R.id.cardVoce).setOnClickListener(v ->
                openFragment(v, new VoceFragment())
        );

        // 🌿 ZAČINSKO BILJE
        view.findViewById(R.id.cardBilje).setOnClickListener(v ->
                openFragment(v, new ZacinskoBiljeFragment())
        );

        // 💡 SAVJETI
        view.findViewById(R.id.cardSavjeti).setOnClickListener(v ->
                openFragment(v, new SavjetiFragment())
        );

        return view;
    }

    // 🔥 JEDNA METODA ZA SVE
    private void openFragment(View v, Fragment fragment) {

        // CLICK animacija
        v.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction(() -> {

                    v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start();

                    // SLIDE animacija
                    getParentFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left,
                                    R.anim.slide_in_left,
                                    R.anim.slide_out_right
                            )
                            .replace(R.id.fragmentContainer, fragment)
                            .addToBackStack(null)
                            .commit();

                })
                .start();
    }
}