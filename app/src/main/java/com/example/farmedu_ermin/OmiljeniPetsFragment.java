package com.example.farmedu_ermin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.example.farmedu_ermin.models.Zivotinja;
import com.example.farmedu_ermin.utils.FavoriteManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OmiljeniPetsFragment extends Fragment {

    private TextView txtCount;
    private LinearLayout container;

    public OmiljeniPetsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup containerView,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_omiljeni_pets,
                containerView,
                false
        );

        txtCount = view.findViewById(R.id.txtFavoritesCount);

        container = view.findViewById(R.id.favoritesContainer);

        loadFavorites();

        return view;
    }

    // 🔥 UCITAJ SVE ZIVOTINJE IZ JSON
    private List<Zivotinja> loadAllPets() {

        List<Zivotinja> lista = new ArrayList<>();

        try {

            InputStream is =
                    getResources().openRawResource(R.raw.zivotinje);

            byte[] buffer = new byte[is.available()];

            is.read(buffer);

            is.close();

            String json = new String(buffer, "UTF-8");

            JSONArray mainArray = new JSONArray(json);

            // 🔥 GLAVNE KATEGORIJE
            for (int i = 0; i < mainArray.length(); i++) {

                JSONObject categoryObj =
                        mainArray.getJSONObject(i);

                // 🔥 LISTA ZIVOTINJA
                JSONArray animalsArray =
                        categoryObj.getJSONArray("lista");

                for (int j = 0; j < animalsArray.length(); j++) {

                    JSONObject animalObj =
                            animalsArray.getJSONObject(j);

                    String ime =
                            animalObj.getString("ime");

                    String slikaIme =
                            animalObj.getString("slika");

                    int resId = getResources()
                            .getIdentifier(
                                    slikaIme,
                                    "drawable",
                                    requireContext().getPackageName()
                            );

                    Zivotinja z = new Zivotinja(
                            ime,
                            resId,
                            ime
                    );

                    lista.add(z);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            Toast.makeText(
                    getContext(),
                    "Greška JSON",
                    Toast.LENGTH_LONG
            ).show();
        }

        return lista;
    }

    // 🔥 LOAD FAVORITES
    private void loadFavorites() {

        container.removeAllViews();

        SharedPreferences prefs = requireContext()
                .getSharedPreferences(
                        "favorites_pref",
                        Context.MODE_PRIVATE
                );

        Set<String> favs = new HashSet<>(
                prefs.getStringSet(
                        "favorites",
                        new HashSet<>()
                )
        );

        // 🔥 COUNT
        txtCount.setText(String.valueOf(favs.size()));

        LayoutInflater inflater =
                LayoutInflater.from(getContext());

        // 🔥 EMPTY STATE
        if (favs.isEmpty()) {

            TextView empty = new TextView(getContext());

            empty.setText("Nema omiljenih životinja 🐾");

            empty.setTextSize(16f);

            empty.setPadding(20, 40, 20, 20);

            empty.setGravity(Gravity.CENTER);

            container.addView(empty);

            return;
        }

        // 🔥 UCITAJ SVE IZ JSON
        List<Zivotinja> sveZivotinje =
                loadAllPets();

        // 🔥 RENDER FAVORITA
        for (String favoriteName : favs) {

            Zivotinja found = null;

            // 🔥 PRONADJI ZIVOTINJU
            for (Zivotinja z : sveZivotinje) {

                if (z.getIme()
                        .trim()
                        .equalsIgnoreCase(
                                favoriteName.trim()
                        )) {

                    found = z;
                    break;
                }
            }

            // 🔥 AKO NIJE PRONADJENA
            if (found == null) {

                TextView error = new TextView(getContext());

                error.setText(
                        "Nije pronađeno: " + favoriteName
                );

                error.setPadding(20,20,20,20);

                container.addView(error);

                continue;
            }

            // 🔥 CREATE ITEM
            View item = inflater.inflate(
                    R.layout.item_favorite_pet,
                    container,
                    false
            );

            TextView name =
                    item.findViewById(R.id.txtName);

            TextView desc =
                    item.findViewById(R.id.txtDesc);

            ImageView img =
                    item.findViewById(R.id.imgPet);

            ImageView delete =
                    item.findViewById(R.id.btnDelete);

            // 🔥 DATA
            name.setText(found.getIme());

            desc.setText("Omiljeni ljubimac 🐾");

            // 🔥 IMAGE
            if (found.getSlika() != 0) {

                img.setImageResource(
                        found.getSlika()
                );

            } else {

                img.setImageResource(
                        R.drawable.ic_launcher_foreground
                );
            }

            // 🔥 DELETE
            delete.setOnClickListener(v -> {

                FavoriteManager.removeFavorite(
                        requireContext(),
                        favoriteName
                );

                Toast.makeText(
                        getContext(),
                        "Uklonjeno iz favorita",
                        Toast.LENGTH_SHORT
                ).show();

                loadFavorites();
            });

            // 🔥 DODAJ ITEM
            container.addView(item);
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        loadFavorites();
    }
}