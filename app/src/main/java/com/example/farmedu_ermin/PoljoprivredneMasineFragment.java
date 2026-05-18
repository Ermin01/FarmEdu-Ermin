package com.example.farmedu_ermin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farmedu_ermin.adapters.MasineAdapter;
import com.example.farmedu_ermin.models.Poljoprivrednemasine;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class PoljoprivredneMasineFragment extends Fragment {

    RecyclerView recyclerMasine;
    ArrayList<Poljoprivrednemasine> lista;
    MasineAdapter adapter;

    public PoljoprivredneMasineFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_poljoprivredne_masine, container, false);

        // 🔙 BACK BUTTON → Mehanizacijaialati
        view.findViewById(R.id.btnBack).setOnClickListener(v ->

                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_left,
                                R.anim.slide_out_right,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                        )
                        .replace(R.id.fragmentContainer, new Mehanizacijaialati())
                        .commit()
        );

        recyclerMasine = view.findViewById(R.id.recyclerMasine);

        lista = new ArrayList<>();

        // 🔥 UČITAVANJE IZ RAW
        String json = loadJSONFromRaw();

        if (json == null) {

            return view;
        }

        try {

            JSONArray niz = new JSONArray(json);

            for (int i = 0; i < niz.length(); i++) {

                JSONObject obj = niz.getJSONObject(i);

                String nazivSlike = obj.getString("slika");

                int resId = getResources().getIdentifier(
                        nazivSlike,
                        "drawable",
                        requireContext().getPackageName()
                );

                // 🔥 fallback ako nema slike
                if (resId == 0) {

                    resId = R.drawable.bosanski_brdski_konj;
                }

                lista.add(new Poljoprivrednemasine(
                        obj.getString("naslov"),
                        resId,
                        obj.getString("opis")
                ));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        adapter = new MasineAdapter(getContext(), lista);

        recyclerMasine.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerMasine.setAdapter(adapter);

        return view;
    }

    // ✅ RAW JSON
    private String loadJSONFromRaw() {

        try {

            InputStream is = getResources().openRawResource(R.raw.poljoprivrednemasine);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            return new String(buffer, "UTF-8");

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
}