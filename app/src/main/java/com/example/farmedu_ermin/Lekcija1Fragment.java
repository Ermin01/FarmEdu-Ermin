package com.example.farmedu_ermin;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.*;
import android.widget.*;

import org.json.*;

import java.io.InputStream;

public class Lekcija1Fragment extends Fragment {

    public Lekcija1Fragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lekcija1, container, false);

        // 🔙 BACK -> LEKCIJE LIST
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

        // ⚠️ VAŽNO: koristi contentLayout
        LinearLayout layout = view.findViewById(R.id.contentLayout);

        try {
            InputStream is = getResources().openRawResource(R.raw.lekcija1);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer);

            JSONObject obj = new JSONObject(json);

            // 🔥 osnovni podaci
            naslov.setText(obj.getString("naslov"));
            opis.setText(obj.getString("opis"));

            // 🔥 sekcije
            JSONArray arr = obj.getJSONArray("sekcije");

            for (int i = 0; i < arr.length(); i++) {

                JSONObject item = arr.getJSONObject(i);

                String tip = item.getString("tip");
                String naslovSekcijeText = item.getString("naslov");

                // 📦 CARD
                LinearLayout card = new LinearLayout(getContext());
                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(24, 24, 24, 24);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(0, 16, 0, 0);

                card.setLayoutParams(params);

                card.setBackgroundResource(R.drawable.card_bg);
                card.setElevation(6f);

                // 🟢 NASLOV
                TextView naslovSekcije = new TextView(getContext());

                naslovSekcije.setText(naslovSekcijeText);
                naslovSekcije.setTextSize(18);
                naslovSekcije.setTextColor(0xFF2E7D32);

                card.addView(naslovSekcije);

                // 🔥 TEKST
                if (tip.equals("tekst")) {

                    TextView tekst = new TextView(getContext());

                    tekst.setText(item.getString("sadrzaj"));
                    tekst.setTextSize(15);
                    tekst.setTextColor(0xFF444444);
                    tekst.setPadding(0, 8, 0, 0);

                    card.addView(tekst);

                }
                // 🔥 LISTA
                else if (tip.equals("lista")) {

                    JSONArray lista = item.getJSONArray("stavke");

                    for (int j = 0; j < lista.length(); j++) {

                        TextView bullet = new TextView(getContext());

                        bullet.setText("• " + lista.getString(j));
                        bullet.setTextSize(15);
                        bullet.setTextColor(0xFF444444);
                        bullet.setPadding(0, 6, 0, 0);

                        card.addView(bullet);
                    }

                }
                // 🔥 HIGHLIGHT
                else if (tip.equals("highlight")) {

                    card.setBackgroundColor(0xFFE8F5E9);

                    TextView tekst = new TextView(getContext());

                    tekst.setText(item.getString("sadrzaj"));
                    tekst.setTextSize(15);
                    tekst.setTextColor(0xFF2E7D32);
                    tekst.setPadding(0, 8, 0, 0);

                    card.addView(tekst);
                }

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