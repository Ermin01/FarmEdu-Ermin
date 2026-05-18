package com.example.farmedu_ermin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class Lekcija3Fragment extends Fragment {

    public Lekcija3Fragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lekcija3, container, false);

        // 🔙 BACK dugme
        view.findViewById(R.id.btnBack).setOnClickListener(v ->

                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_left,
                                R.anim.slide_out_right,
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                        )
                        .replace(R.id.fragmentContainer, new LekcijeListFragment())
                        .commit()
        );

        TextView naslov = view.findViewById(R.id.naslov);
        TextView opis = view.findViewById(R.id.opis);
        LinearLayout layout = view.findViewById(R.id.contentLayout);

        try {

            InputStream is = getResources().openRawResource(R.raw.lekcija3);

            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");

            JSONObject obj = new JSONObject(json);

            naslov.setText(obj.getString("naslov"));
            opis.setText(obj.getString("opis"));

            JSONArray arr = obj.getJSONArray("sadrzaj");

            for (int i = 0; i < arr.length(); i++) {

                JSONObject item = arr.getJSONObject(i);

                String n = item.getString("naslov");
                String t = item.getString("tekst");
                String ikona = item.optString("ikona", "🌱");

                // 📦 CARD
                LinearLayout card = new LinearLayout(getContext());
                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(32, 32, 32, 32);
                card.setBackgroundResource(R.drawable.card_bg);

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                params.setMargins(16, 16, 16, 0);

                card.setLayoutParams(params);

                // 🌱 NASLOV + IKONA
                TextView t1 = new TextView(getContext());
                t1.setText(ikona + "  " + n);
                t1.setTextSize(18);
                t1.setTextColor(0xFF2E7D32);
                t1.setPadding(0, 0, 0, 10);

                // 📄 TEKST
                TextView t2 = new TextView(getContext());
                t2.setText(t);
                t2.setTextSize(15);
                t2.setTextColor(0xFF444444);

                card.addView(t1);
                card.addView(t2);

                layout.addView(card);
            }

        } catch (Exception e) {

            e.printStackTrace();

            Toast.makeText(
                    getContext(),
                    "Greška JSON",
                    Toast.LENGTH_LONG
            ).show();
        }

        return view;
    }
}