package com.example.farmedu_ermin.utils;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.*;
import android.widget.*;

import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.models.Zitarica;

public class ZitariceDetaljiFragment extends Fragment {

    public ZitariceDetaljiFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zitarice_detalji, container, false);

        ImageView img = view.findViewById(R.id.imgDetalji);
        TextView naslov = view.findViewById(R.id.tvNaslov);
        TextView opis = view.findViewById(R.id.tvOpis);
        TextView sunce = view.findViewById(R.id.tvSunce);
        TextView voda = view.findViewById(R.id.tvVoda);
        TextView temp = view.findViewById(R.id.tvTemperatura);
        TextView tlo = view.findViewById(R.id.tvTlo);
        TextView prinos = view.findViewById(R.id.tvPrinos);
        TextView berba = view.findViewById(R.id.tvBerba);

        // 🔥 SIGURNO UZIMANJE ARGUMENATA
        Bundle args = getArguments();
        if (args == null) return view;

        Zitarica z = (Zitarica) args.getSerializable("zitarica");
        if (z == null) return view;

        // 🔥 PODACI
        naslov.setText(getStringByName(z.getNaslov()));
        opis.setText(getStringByName(z.getOpis()));

        sunce.setText("☀️ " + getString(R.string.sunce) + ": " + getStringByName(z.getSunce()));
        voda.setText("💧 " + getString(R.string.zalijevanje) + ": " + getStringByName(z.getZalijevanje()));
        temp.setText("🌡 " + getString(R.string.temperatura) + ": " + getStringByName(z.getTemperatura()));
        tlo.setText("🌱 " + getString(R.string.tip_tla) + ": " + getStringByName(z.getTipTla()));
        prinos.setText("📊 " + getString(R.string.prinos) + ": " + getStringByName(z.getPrinos()));
        berba.setText("🌾 " + getString(R.string.berba) + ": " + getStringByName(z.getVrijemeBerbe()));

        img.setImageResource(z.getSlika());

        return view;
    }

    private String getStringByName(String key) {
        int resId = requireContext()
                .getResources()
                .getIdentifier(key, "string", requireContext().getPackageName());

        return resId != 0 ? getString(resId) : key.replace("_", " ");
    }
}