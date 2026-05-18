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

public class AlatiOpremaFragment extends Fragment {

    RecyclerView recycler;
    ArrayList<Poljoprivrednemasine> lista;
    MasineAdapter adapter;

    public AlatiOpremaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alati_oprema, container, false);

        // 🔙 BACK BUTTON → Poljoprivreda
        view.findViewById(R.id.btnBack).setOnClickListener(v ->

                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_left,
                                R.anim.slide_out_right,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                        )
                        .replace(R.id.fragmentContainer, new Poljoprivreda())
                        .commit()
        );

        recycler = view.findViewById(R.id.recyclerAlati);

        lista = new ArrayList<>();

        String json = loadJSONFromRaw();

        if (json == null) return view;

        try {

            JSONArray niz = new JSONArray(json);

            for (int i = 0; i < niz.length(); i++) {

                JSONObject obj = niz.getJSONObject(i);

                int resId = getResources().getIdentifier(
                        obj.getString("slika"),
                        "drawable",
                        requireContext().getPackageName()
                );

                if (resId == 0) {

                    resId = R.drawable.alatiioprema;
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

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        recycler.setAdapter(adapter);

        return view;
    }

    private String loadJSONFromRaw() {

        try {

            InputStream is = getResources().openRawResource(R.raw.alati_oprema);

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