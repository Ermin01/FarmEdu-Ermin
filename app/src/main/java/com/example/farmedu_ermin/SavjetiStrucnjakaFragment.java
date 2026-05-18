package com.example.farmedu_ermin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

public class SavjetiStrucnjakaFragment extends Fragment {

    // FILTER BUTTONS
    private LinearLayout filterSve;
    private LinearLayout cardFarmer;
    private LinearLayout cardVeterinar;
    private LinearLayout cardAgronomi;

    // CARDS
    private View cardFarmeri;
    private View cardVeterinari;
    private View cardAgronomiList;
    private View cardTehnicar;

    public SavjetiStrucnjakaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_savjeti_strucnjaka,
                container,
                false
        );
//cardPrehrana
        // FILTERS
        filterSve = view.findViewById(R.id.filterSve);
        cardFarmer = view.findViewById(R.id.cardFarmer);
        cardVeterinar = view.findViewById(R.id.cardVeterinar);
        cardAgronomi = view.findViewById(R.id.cardAgronomi);
//„Inženjer prehrambene tehnologije”
        // LIST CARDS
        cardFarmeri = view.findViewById(R.id.cardFarmeri);
        cardVeterinari = view.findViewById(R.id.cardVeterinari);
        cardAgronomiList = view.findViewById(R.id.cardAgronomiList);
        cardTehnicar = view.findViewById(R.id.cardTehnicar);

        // =========================
        // SVE
        // =========================
        filterSve.setOnClickListener(v -> {

            cardFarmeri.setVisibility(View.VISIBLE);
            cardVeterinari.setVisibility(View.VISIBLE);
            cardAgronomiList.setVisibility(View.VISIBLE);
            cardTehnicar.setVisibility(View.VISIBLE);

        });

        // =========================
        // FARMERI
        // =========================
        cardFarmer.setOnClickListener(v -> {

            cardFarmeri.setVisibility(View.VISIBLE);

            cardVeterinari.setVisibility(View.GONE);
            cardAgronomiList.setVisibility(View.GONE);
            cardTehnicar.setVisibility(View.GONE);

        });

        // =========================
        // VETERINARI
        // =========================
        cardVeterinar.setOnClickListener(v -> {

            cardVeterinari.setVisibility(View.VISIBLE);

            cardFarmeri.setVisibility(View.GONE);
            cardAgronomiList.setVisibility(View.GONE);
            cardTehnicar.setVisibility(View.GONE);

        });

        // =========================
        // AGRONOMI
        // =========================
        cardAgronomi.setOnClickListener(v -> {

            cardAgronomiList.setVisibility(View.VISIBLE);
            cardTehnicar.setVisibility(View.VISIBLE);

            cardFarmeri.setVisibility(View.GONE);
            cardVeterinari.setVisibility(View.GONE);

        });

        return view;
    }
}