package com.example.farmedu_ermin;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Poljoprivreda extends Fragment {

    public Poljoprivreda() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_poljoprivreda, container, false);

        // 🔙 BACK -> HOME
        view.findViewById(R.id.btnBack).setOnClickListener(v ->

                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_left,
                                R.anim.slide_out_right,
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                        )
                        .replace(R.id.fragmentContainer, new Home())
                        .commit()
        );

        // 🌱 OSNOVE
        view.findViewById(R.id.cardOsnove).setOnClickListener(v ->
                openFragment(v, new LekcijeListFragment())
        );

        // 🌦 VRIJEME
        view.findViewById(R.id.cardVrijeme).setOnClickListener(v ->
                openFragment(v, new VrijemeFragment())
        );

        // 🚜 ALATI
        view.findViewById(R.id.cardAlati).setOnClickListener(v ->
                openFragment(v, new Mehanizacijaialati())
        );

        // 💡 SAVJETI
        view.findViewById(R.id.cardSavjet).setOnClickListener(v ->
                openFragment(v, new Savjetiipraksa())
        );

        // 🌿 UZGOJ BILJAKA
        view.findViewById(R.id.cardUzgojbiljaka).setOnClickListener(v ->
                openFragment(v, new Uzgojbiljaka())
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