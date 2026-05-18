package com.example.farmedu_ermin.utils;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.*;
import android.widget.*;

import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.models.ZacinskoBilje;

public class ZacinskoDetaljiFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zacinsko_detalji, container, false);

        ZacinskoBilje z = (ZacinskoBilje) getArguments().getSerializable("biljka");

        if (z != null) {

            ((TextView)view.findViewById(R.id.tvNaslov))
                    .setText(getStringByName(z.getNaslov()));

            ((TextView)view.findViewById(R.id.tvOpis))
                    .setText(getStringByName(z.getOpis()));

            ((TextView)view.findViewById(R.id.tvSunce))
                    .setText("☀️ " + getString(R.string.sunce) + ": " + getStringByName(z.getSunce()));

            ((TextView)view.findViewById(R.id.tvVoda))
                    .setText("💧 " + getString(R.string.zalijevanje) + ": " + getStringByName(z.getZalijevanje()));

            ((TextView)view.findViewById(R.id.tvTemperatura))
                    .setText("🌡 " + getString(R.string.temperatura) + ": " + getStringByName(z.getTemperatura()));

            ((TextView)view.findViewById(R.id.tvTlo))
                    .setText("🌱 " + getString(R.string.tip_tla) + ": " + getStringByName(z.getTipTla()));

            ((TextView)view.findViewById(R.id.tvPrinos))
                    .setText("📦 " + getString(R.string.prinos) + ": " + getStringByName(z.getPrinos()));

            ((TextView)view.findViewById(R.id.tvBerba))
                    .setText("🧺 " + getString(R.string.berba) + ": " + getStringByName(z.getVrijemeBerbe()));

            ((ImageView)view.findViewById(R.id.imgDetalji))
                    .setImageResource(z.getSlika());
        }

        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }

    private String getStringByName(String key) {
        int resId = getResources().getIdentifier(key, "string", requireContext().getPackageName());
        return resId != 0 ? getString(resId) : key;
    }
}