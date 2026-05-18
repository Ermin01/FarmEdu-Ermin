package com.example.farmedu_ermin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farmedu_ermin.adapters.ZitariceAdapter;
import com.example.farmedu_ermin.models.Zitarica;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class ZitariceFragment extends Fragment {

    RecyclerView recycler;
    ArrayList<Zitarica> lista;
    ZitariceAdapter adapter;

    public ZitariceFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zitarice, container, false);

        // 🔙 BACK BUTTON
        view.findViewById(R.id.btnBack).setOnClickListener(v -> {

            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_left,
                            R.anim.slide_out_right,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                    )
                    .replace(R.id.fragmentContainer, new Uzgojbiljaka())
                    .commit();

        });

        recycler = view.findViewById(R.id.recyclerPovrce);
        lista = new ArrayList<>();

        loadData();

        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new ZitariceAdapter(getContext(), lista);

        recycler.setAdapter(adapter);

        return view;
    }

    private void loadData() {

        try {

            InputStream is = getResources().openRawResource(R.raw.zitarice);

            byte[] buffer = new byte[is.available()];

            is.read(buffer);

            is.close();

            String json = new String(buffer, "UTF-8");

            JSONArray niz = new JSONArray(json);

            for (int i = 0; i < niz.length(); i++) {

                JSONObject obj = niz.getJSONObject(i);

                int resId = getResources().getIdentifier(
                        obj.optString("slika"),
                        "drawable",
                        requireContext().getPackageName()
                );

                lista.add(new Zitarica(
                        obj.optString("naslov"),
                        resId,
                        obj.optString("opis"),
                        obj.optString("vrijeme_sadnje"),
                        obj.optString("vrijeme_berbe"),
                        obj.optString("zalijevanje"),
                        obj.optString("sunce"),
                        obj.optString("temperatura"),
                        obj.optString("tip_tla"),
                        obj.optString("prinos")
                ));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}