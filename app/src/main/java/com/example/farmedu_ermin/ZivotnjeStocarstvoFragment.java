package com.example.farmedu_ermin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ZivotnjeStocarstvoFragment extends Fragment {


    public ZivotnjeStocarstvoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zivotnje_stocarstvo, container, false);

        // 🔙 BACK
        view.findViewById(R.id.btnBack).setOnClickListener(v -> {

            v.animate()
                    .scaleX(0.90f)
                    .scaleY(0.90f)
                    .setDuration(80)
                    .withEndAction(() -> {

                        v.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(80)
                                .start();

                        requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(
                                        R.anim.slide_in_left,
                                        R.anim.slide_out_right
                                )
                                .replace(R.id.fragmentContainer, new Home())
                                .commit();

                    })
                    .start();

        });

        view.findViewById(R.id.cardOsnovestoca).setOnClickListener(v ->
                openFragment(v, new OsnovestocarstvaFragment())
        );

        view.findViewById(R.id.cardIshrana).setOnClickListener(v ->
                openFragment(v, new IshranaZivotinjaFragment())
        );

        view.findViewById(R.id.cardBolesti).setOnClickListener(v ->
                openFragment(v, new BolestiPrevencijaFragment())
        );

        view.findViewById(R.id.cardSmjestaj).setOnClickListener(v ->
                openFragment(v, new SmjestajopremaFragment())
        );

        view.findViewById(R.id.cardProizvodnja).setOnClickListener(v ->
                openFragment(v, new ProizvodnjapreradaFragment())
        );








        // 🐄 VRSTE ŽIVOTINJA (🔴 OVO TI FALI)
        view.findViewById(R.id.cardZivotinje).setOnClickListener(v ->

                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                        )
                        .replace(R.id.fragmentContainer, new Zivotinje())
                        .addToBackStack(null)
                        .commit()
        );

        return view;
    }

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