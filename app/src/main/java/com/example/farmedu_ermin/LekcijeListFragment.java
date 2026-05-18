package com.example.farmedu_ermin;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LekcijeListFragment extends Fragment {

    public LekcijeListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lekcije_list_activity, container, false);

        // 🔙 BACK -> POLJOPRIVREDA
        view.findViewById(R.id.btnBack).setOnClickListener(v ->

                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_left,
                                R.anim.slide_out_right,
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                        )
                        .replace(R.id.fragmentContainer, new Poljoprivreda())
                        .commit()
        );

        // 📚 LEKCIJE
        view.findViewById(R.id.lekcija1).setOnClickListener(v ->
                openFragment(v, new Lekcija1Fragment())
        );

        view.findViewById(R.id.lekcija2).setOnClickListener(v ->
                openFragment(v, new Lekcija2Fragment())
        );

        view.findViewById(R.id.lekcija3).setOnClickListener(v ->
                openFragment(v, new Lekcija3Fragment())
        );

        // 🧠 KVIZ
        view.findViewById(R.id.kviz).setOnClickListener(v ->
                openFragment(v, new KvizActivity())
        );

        return view;
    }

    // 🔥 UNIVERSALNA METODA
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