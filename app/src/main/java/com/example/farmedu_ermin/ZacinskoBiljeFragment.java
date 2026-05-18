package com.example.farmedu_ermin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.*;
import android.widget.Toast;

import com.example.farmedu_ermin.adapters.ZacinskoBiljeAdapter;
import com.example.farmedu_ermin.models.ZacinskoBilje;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class ZacinskoBiljeFragment extends Fragment {

    RecyclerView recycler;
    ArrayList<ZacinskoBilje> lista;
    ZacinskoBiljeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zacinsko_bilje, container, false);

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

        recycler = view.findViewById(R.id.recyclerZacinsko);

        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        lista = new ArrayList<>();

        loadData();

        adapter = new ZacinskoBiljeAdapter(getContext(), lista);

        recycler.setAdapter(adapter);

        return view;
    }

    private void loadData() {

        try {

            InputStream is = getResources().openRawResource(R.raw.zacinskobilje);

            byte[] buffer = new byte[is.available()];

            is.read(buffer);

            is.close();

            String json = new String(buffer, "UTF-8");

            JSONArray niz = new JSONArray(json);

            for (int i = 0; i < niz.length(); i++) {

                JSONObject obj = niz.getJSONObject(i);

                String imeSlike = obj.getString("slika");

                int slika = getResources().getIdentifier(
                        imeSlike,
                        "drawable",
                        requireContext().getPackageName()
                );

                // fallback slika
                if (slika == 0) {

                    slika = R.drawable.placeholder;
                }

                lista.add(new ZacinskoBilje(
                        obj.getString("naslov"),
                        slika,
                        obj.getString("opis"),
                        obj.getString("vrijeme_sadnje"),
                        obj.getString("zalijevanje"),
                        obj.getString("sunce"),
                        obj.getString("temperatura"),
                        obj.getString("tip_tla"),
                        obj.getString("prinos"),
                        obj.getString("vrijeme_berbe")
                ));
            }

        } catch (Exception e) {

            e.printStackTrace();

            Toast.makeText(getContext(), "Greška JSON!", Toast.LENGTH_LONG).show();
        }
    }
}