package com.example.farmedu_ermin;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Mehanizacijaialati extends Fragment {

    public Mehanizacijaialati() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mehanizacijaialati, container, false);

        ImageView btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        // 🚜 MAŠINE
        view.findViewById(R.id.cardMasine).setOnClickListener(v ->
                openFragment(v, new PoljoprivredneMasineFragment())
        );

        // 🧰 ALATI
        view.findViewById(R.id.cardAlati).setOnClickListener(v ->
                openFragment(v, new AlatiOpremaFragment())
        );

        // 🔧 SERVIS
        view.findViewById(R.id.cardServis).setOnClickListener(v ->
                openFragment(v, new Odrzavanjeiservis())
        );

        // 🛑 SIGURNOST
        view.findViewById(R.id.cardSigurnost).setOnClickListener(v ->
                openFragment(v, new Cardsigurnostpriradu())
        );

        // ⛽ GORIVO
        view.findViewById(R.id.cardGorivo).setOnClickListener(v ->
                openFragment(v, new GorivoFragment())
        );

        return view;
    }

    // 🔥 JEDNA METODA ZA SVE (ANIMACIJA + NAVIGACIJA)
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
                    requireActivity()
                            .getSupportFragmentManager()
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