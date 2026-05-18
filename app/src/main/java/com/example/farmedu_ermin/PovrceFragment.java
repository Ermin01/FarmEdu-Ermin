package com.example.farmedu_ermin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farmedu_ermin.adapters.PovrceAdapter;
import com.example.farmedu_ermin.models.Povrce;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class PovrceFragment extends Fragment {

    RecyclerView recyclerPovrce;
    ArrayList<Povrce> lista;
    PovrceAdapter adapter;

    public PovrceFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_povrce, container, false);

        // 🔙 BACK BUTTON → UzgojBiljakaFragment
        view.findViewById(R.id.btnBack).setOnClickListener(v ->

                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_left,
                                R.anim.slide_out_right,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                        )
                        .replace(R.id.fragmentContainer, new Uzgojbiljaka())
                        .commit()
        );

        recyclerPovrce = view.findViewById(R.id.recyclerPovrce);

        lista = new ArrayList<>();

        loadData(); // 🔥 učitaj JSON

        // 🔥 GRID 2 kolone
        recyclerPovrce.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new PovrceAdapter(lista);

        recyclerPovrce.setAdapter(adapter);

        return view;
    }

    private void loadData() {

        try {

            InputStream is = getResources().openRawResource(R.raw.povrce);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            String json = new String(buffer, "UTF-8");

            JSONArray niz = new JSONArray(json);

            for (int i = 0; i < niz.length(); i++) {

                JSONObject obj = niz.getJSONObject(i);

                String nazivSlike = obj.getString("slika");

                int resId = getResources().getIdentifier(
                        nazivSlike,
                        "drawable",
                        requireContext().getPackageName()
                );

                lista.add(new Povrce(
                        obj.getString("naslov"),
                        resId,
                        obj.getString("opis"),
                        obj.optString("vrijeme_sadnje"),
                        obj.optString("zalijevanje"),
                        obj.optString("sunce")
                ));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}