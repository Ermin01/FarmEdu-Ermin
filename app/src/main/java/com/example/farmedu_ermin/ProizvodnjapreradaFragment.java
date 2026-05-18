package com.example.farmedu_ermin;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProizvodnjapreradaFragment extends Fragment {

    public ProizvodnjapreradaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_proizvodnjaprerada, container, false);

        // 🔙 BACK BUTTON
        View btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v ->
                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_left,
                                R.anim.slide_out_right,
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                        )
                        .replace(R.id.fragmentContainer, new ZivotnjeStocarstvoFragment())
                        .commit()
        );

        // 📦 CARDS
        View cardMlijeko = view.findViewById(R.id.cardMlijeko);
        View cardMeso = view.findViewById(R.id.cardMeso);
        View cardJaja = view.findViewById(R.id.cardJaja);
        View cardVuna = view.findViewById(R.id.cardVuna);

        cardMlijeko.setOnClickListener(v ->
                openFragment(v, new MlijekoFragment())
        );

        cardMeso.setOnClickListener(v ->
                openFragment(v, new MesoFragment())
        );

        cardJaja.setOnClickListener(v ->
                openFragment(v, new JajaFragment())
        );

        cardVuna.setOnClickListener(v ->
                openFragment(v, new VunaFragment())
        );

        return view;
    }

    // 🔥 PREMIUM NAVIGATION
    private void openFragment(View v, Fragment fragment) {

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