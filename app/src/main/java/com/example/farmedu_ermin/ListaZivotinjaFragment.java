package com.example.farmedu_ermin;

import android.os.Bundle;
import android.view.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.adapters.ZivotinjeAdapter;
import com.example.farmedu_ermin.models.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ListaZivotinjaFragment extends Fragment {

    RecyclerView recyclerView;
    List<Zivotinja> lista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lista_zivotinja, container, false);

        recyclerView = view.findViewById(R.id.recyclerZivotinje);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        lista = new ArrayList<>();

        String kategorija = getArguments().getString("kategorija");

        try {
            InputStream is = getResources().openRawResource(R.raw.zivotinje);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JSONArray array = new JSONArray(json);

            for (int i = 0; i < array.length(); i++) {

                JSONObject obj = array.getJSONObject(i);

                String imeKategorije = obj.getString("ime");

                if (!imeKategorije.equals(kategorija)) continue;

                JSONArray listaZivotinja = obj.getJSONArray("lista");

                for (int j = 0; j < listaZivotinja.length(); j++) {

                    JSONObject zObj = listaZivotinja.getJSONObject(j);

                    String ime = zObj.getString("ime");
                    String slikaIme = zObj.getString("slika");

                    int resId = getResources().getIdentifier(
                            slikaIme,
                            "drawable",
                            requireContext().getPackageName()
                    );

                    // 🔥 OSNOVNE INFO
                    JSONObject o = zObj.getJSONObject("osnovneInfo");
                    OsnovneInfo osnovne = new OsnovneInfo(
                            o.getString("zivotniVijek"),
                            o.getString("visina"),
                            o.getString("tezina"),
                            o.getString("porijeklo"),
                            o.getString("brzina"),
                            o.getString("tipDlake"),
                            o.getString("snaga")
                    );

                    // 🔥 PONAŠANJE
                    JSONObject p = zObj.getJSONObject("ponasanje");
                    Ponasanje ponasanje = new Ponasanje(
                            p.getString("temperament"),
                            p.getString("inteligencija"),
                            p.getString("socijalnoPonasanje"),
                            p.getString("odnosPremaLjudima"),
                            p.getString("odnosPremaDjeci"),
                            p.getString("odnosPremaZivotinjama"),
                            p.getString("nivoEnergije"),
                            p.getString("lakocaTreniranja"),
                            p.getString("instinkti")
                    );

                    // 🔥 ZDRAVLJE
                    JSONObject z = zObj.getJSONObject("zdravlje");
                    Zdravlje zdravlje = new Zdravlje(
                            z.getString("prosjecnaStarost"),
                            z.getString("bolesti"),
                            z.getString("genetika"),
                            z.getString("otpornost"),
                            z.getString("osjetljivost")
                    );

                    // 🔥 ISHRANA
                    JSONObject iObj = zObj.getJSONObject("ishrana");
                    Ishrana ishrana = new Ishrana(
                            iObj.getString("tip"),
                            iObj.getString("dnevniUnos"),
                            iObj.getString("vrstaHrane"),
                            iObj.getString("dijeta"),
                            iObj.getString("alergije"),
                            iObj.getString("voda")
                    );

                    // 🔥 AKTIVNOST
                    JSONObject a = zObj.getJSONObject("aktivnost");
                    Aktivnost aktivnost = new Aktivnost(
                            a.getString("kretanje"),
                            a.getString("vrsta"),
                            a.getString("trening"),
                            a.getString("radneSposobnosti"),
                            a.getString("sport")
                    );

                    // 🔥 FINAL OBJEKAT
                    Zivotinja zivotinja = new Zivotinja(
                            ime,
                            resId,
                            imeKategorije,
                            osnovne,
                            ponasanje,
                            zdravlje,
                            null,
                            null,
                            ishrana,
                            aktivnost,
                            null,
                            null
                    );

                    lista.add(zivotinja);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView.setAdapter(new ZivotinjeAdapter(lista));

        return view;
    }
}