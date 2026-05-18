package com.example.farmedu_ermin.utils;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.*;
import android.widget.*;

import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.models.Povrce;

public class PovrceDetaljiFragment extends Fragment {

    public PovrceDetaljiFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_povrce_detalji, container, false);

        // 🔙 BACK
        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        Povrce p = (Povrce) getArguments().getSerializable("povrce");

        if (p != null) {

            TextView tvNaslov = view.findViewById(R.id.tvNaslov);
            TextView tvOpis = view.findViewById(R.id.tvOpis);
            TextView tvSunce = view.findViewById(R.id.tvSunce);
            TextView tvVoda = view.findViewById(R.id.tvVoda);
            TextView tvSadnja = view.findViewById(R.id.tvSadnja);

            ImageView img = view.findViewById(R.id.imgDetalji);

            // 🔥 koristi KEY iz JSON
            tvNaslov.setText(getStringByName(p.getNaslov()));
            tvOpis.setText(getStringByName(p.getOpis()));

            tvSunce.setText("☀️ " + getString(R.string.sunce) + ": "
                    + getStringByName(p.getSunce()));

            tvVoda.setText("💧 " + getString(R.string.zalijevanje) + ": "
                    + getStringByName(p.getZalijevanje()));

            tvSadnja.setText("🌱 " + getString(R.string.sadnja) + ": "
                    + getStringByName(p.getVrijemeSadnje()));

            img.setImageResource(p.getSlika());
        }

        return view;
    }

    // 🔥 helper
    private String getStringByName(String key) {
        int resId = getResources().getIdentifier(key, "string", requireContext().getPackageName());
        return resId != 0 ? getString(resId) : key;
    }
}