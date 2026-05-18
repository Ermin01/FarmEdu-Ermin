package com.example.farmedu_ermin.utils;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.*;
import android.widget.*;

import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.models.Voce;

public class VoceDetaljiFragment extends Fragment {

    public VoceDetaljiFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_voce_detalji, container, false);

        // 🔙 BACK BUTTON
        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        Voce v = (Voce) getArguments().getSerializable("voce");

        if (v != null) {

            TextView tvNaslov = view.findViewById(R.id.tvNaslov);
            TextView tvOpis = view.findViewById(R.id.tvOpis);

            TextView tvSunce = view.findViewById(R.id.tvSunce);
            TextView tvVoda = view.findViewById(R.id.tvVoda);
            TextView tvTemperatura = view.findViewById(R.id.tvTemperatura);
            TextView tvTlo = view.findViewById(R.id.tvTlo);
            TextView tvPrinos = view.findViewById(R.id.tvPrinos);
            TextView tvBerba = view.findViewById(R.id.tvBerba);

            ImageView img = view.findViewById(R.id.imgDetalji);

            // 🔥 NASLOV + OPIS
            tvNaslov.setText(getStringByName(v.getNaslov()));
            tvOpis.setText(getStringByName(v.getOpis()));

            // 🔥 OSTALO
            tvSunce.setText("☀️ " + getString(R.string.sunce) + ": " + getStringByName(v.getSunce()));
            tvVoda.setText("💧 " + getString(R.string.zalijevanje) + ": " + getStringByName(v.getZalijevanje()));
            tvTemperatura.setText("🌡️ " + getString(R.string.temperatura) + ": " + getStringByName(v.getTemperatura()));
            tvTlo.setText("🌱 " + getString(R.string.tip_tla) + ": " + getStringByName(v.getTipTla()));
            tvPrinos.setText("📦 " + getString(R.string.prinos) + ": " + getStringByName(v.getPrinos()));
            tvBerba.setText("🧺 " + getString(R.string.berba) + ": " + getStringByName(v.getVrijemeBerbe()));

            // 🔥 SLIKA
            img.setImageResource(v.getSlika());
        }

        return view;
    }

    // 🔥 HELPER (ključ → string)
    private String getStringByName(String key) {
        int resId = getResources().getIdentifier(key, "string", requireContext().getPackageName());
        return resId != 0 ? getString(resId) : key;
    }
}