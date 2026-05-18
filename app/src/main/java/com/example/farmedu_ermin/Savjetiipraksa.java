package com.example.farmedu_ermin;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.*;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Random;

public class Savjetiipraksa extends Fragment {

    String json;

    public Savjetiipraksa() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_savjetiipraksa, container, false);

        TextView naslov = view.findViewById(R.id.tvSavjetNaslov);
        TextView tekst = view.findViewById(R.id.tvSavjetTekst);
        Button novi = view.findViewById(R.id.btnNoviSavjet);
        ImageView btnBack = view.findViewById(R.id.btnBack);

        // 🔙 BACK
        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        try {
            InputStream is = getResources().openRawResource(R.raw.savjeti);

            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

            // prvi savjet
            prikaziRandomSavjet(naslov, tekst);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔄 NOVI SAVJET
        novi.setOnClickListener(v -> prikaziRandomSavjet(naslov, tekst));

        return view;
    }

    private void prikaziRandomSavjet(TextView naslov, TextView tekst) {
        try {
            JSONArray array = new JSONArray(json);
            Random rand = new Random();
            int index = rand.nextInt(array.length());

            JSONObject obj = array.getJSONObject(index);

            // 🔥 KLJUČNO — PREVOD
            naslov.setText(getStringByName(obj.getString("naslov")));
            tekst.setText(getStringByName(obj.getString("tekst")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 metoda za string resource
    private String getStringByName(String key) {
        int resId = getResources().getIdentifier(
                key,
                "string",
                requireContext().getPackageName()
        );

        return resId != 0 ? getString(resId) : key;
    }
}