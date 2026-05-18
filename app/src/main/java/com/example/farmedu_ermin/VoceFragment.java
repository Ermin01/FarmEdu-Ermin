package com.example.farmedu_ermin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.*;

import com.example.farmedu_ermin.adapters.VoceAdapter;
import com.example.farmedu_ermin.models.Voce;

import org.json.*;

import java.io.InputStream;
import java.util.ArrayList;

public class VoceFragment extends Fragment {

    RecyclerView recycler;
    ArrayList<Voce> lista;

    public VoceFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_voce, container, false);

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

        recycler = view.findViewById(R.id.recyclerVoce);

        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        lista = new ArrayList<>();

        loadData();

        recycler.setAdapter(new VoceAdapter(lista));

        return view;
    }

    private void loadData() {

        try {

            InputStream is = getResources().openRawResource(R.raw.voce);

            byte[] buffer = new byte[is.available()];

            is.read(buffer);

            is.close();

            String json = new String(buffer, "UTF-8");

            JSONArray niz = new JSONArray(json);

            for (int i = 0; i < niz.length(); i++) {

                JSONObject obj = niz.getJSONObject(i);

                int resId = getResources().getIdentifier(
                        obj.getString("slika"),
                        "drawable",
                        requireContext().getPackageName()
                );

                lista.add(new Voce(
                        obj.getString("naslov"),
                        resId,
                        obj.getString("opis"),
                        obj.getString("vrijeme_sadnje"),
                        obj.getString("vrijeme_berbe"),
                        obj.getString("zalijevanje"),
                        obj.getString("sunce"),
                        obj.getString("temperatura"),
                        obj.getString("tip_tla"),
                        obj.getString("prinos")
                ));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}