package com.example.farmedu_ermin;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.*;
import android.widget.*;
import android.content.res.ColorStateList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class KvizActivity extends Fragment {

    TextView tvPitanje, tvRezultat, tvProgress;
    Button b1, b2, b3;
    ImageView btnBack;

    JSONArray pitanja;
    int index = 0;
    int score = 0;

    public KvizActivity() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_kviz_activity, container, false);

        tvPitanje = view.findViewById(R.id.tvPitanje);
        tvRezultat = view.findViewById(R.id.tvRezultat);
        tvProgress = view.findViewById(R.id.tvProgress);

        b1 = view.findViewById(R.id.odgovor1);
        b2 = view.findViewById(R.id.odgovor2);
        b3 = view.findViewById(R.id.odgovor3);

        btnBack = view.findViewById(R.id.btnBack);

        // 🔙 BACK
        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        ucitajJson();

        b1.setOnClickListener(v -> provjeriOdgovor(0));
        b2.setOnClickListener(v -> provjeriOdgovor(1));
        b3.setOnClickListener(v -> provjeriOdgovor(2));

        return view;
    }

    private void ucitajJson() {
        try {
            InputStream is = getResources().openRawResource(R.raw.kviz);

            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");

            JSONObject obj = new JSONObject(json);
            pitanja = obj.getJSONArray("pitanja");

            prikaziPitanje();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Greška pri učitavanju kviza", Toast.LENGTH_LONG).show();
        }
    }

    private void prikaziPitanje() {
        try {
            if (index >= pitanja.length()) {

                tvPitanje.setText("🎉 Kviz završen!");
                tvRezultat.setText("Rezultat: " + score + "/" + pitanja.length());

                b1.setVisibility(View.GONE);
                b2.setVisibility(View.GONE);
                b3.setVisibility(View.GONE);

                tvProgress.setText("");
                return;
            }

            resetDugmad();

            JSONObject p = pitanja.getJSONObject(index);

            tvPitanje.setText(p.getString("pitanje"));

            JSONArray odg = p.getJSONArray("odgovori");

            b1.setText(odg.getString(0));
            b2.setText(odg.getString(1));
            b3.setText(odg.getString(2));

            tvProgress.setText("Pitanje " + (index + 1) + "/" + pitanja.length());

            b1.setEnabled(true);
            b2.setEnabled(true);
            b3.setEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void provjeriOdgovor(int izbor) {
        try {
            JSONObject p = pitanja.getJSONObject(index);
            int tacan = p.getInt("tacan");

            Button kliknuto = (izbor == 0) ? b1 : (izbor == 1) ? b2 : b3;

            b1.setEnabled(false);
            b2.setEnabled(false);
            b3.setEnabled(false);

            if (izbor == tacan) {
                score++;
                kliknuto.setBackgroundTintList(ColorStateList.valueOf(0xFF4CAF50));
            } else {
                kliknuto.setBackgroundTintList(ColorStateList.valueOf(0xFFF44336));

                Button tacno = (tacan == 0) ? b1 : (tacan == 1) ? b2 : b3;
                tacno.setBackgroundTintList(ColorStateList.valueOf(0xFF4CAF50));
            }

            kliknuto.postDelayed(() -> {
                index++;
                prikaziPitanje();
            }, 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetDugmad() {
        int defaultColor = 0xFFE8F5E9;

        b1.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        b2.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        b3.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
    }
}