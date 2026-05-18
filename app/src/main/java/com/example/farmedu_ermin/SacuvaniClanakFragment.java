package com.example.farmedu_ermin;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.farmedu_ermin.models.SavedArticle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class SacuvaniClanakFragment extends Fragment {

    private LinearLayout savedContainer;

    private ImageView btnBack;
    private ImageView btnSettings;
    private ImageView btnTrash;

    private TextView emptyState;

    private boolean selectionMode = false;

    private final HashSet<View> selectedCards =
            new HashSet<>();

    private List<SavedArticle> articleList =
            new ArrayList<>();

    private JSONArray localizedArticles;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle b
    ) {

        View v = inflater.inflate(
                R.layout.fragment_sacuvani_clanak,
                container,
                false
        );

        savedContainer =
                v.findViewById(R.id.savedContainer);

        btnBack =
                v.findViewById(R.id.btnBack);

        btnSettings =
                v.findViewById(R.id.btnSettings);

        btnTrash =
                v.findViewById(R.id.btnTrash);

        emptyState =
                v.findViewById(R.id.emptyState);

        btnTrash.setVisibility(View.GONE);

        loadLocalizedArticles();

        // ================= BACK =================

        btnBack.setOnClickListener(v1 -> {

            if (selectionMode) {

                exitSelection();
                return;
            }

            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });

        // ================= SETTINGS =================

        btnSettings.setOnClickListener(
                this::openMenu
        );

        btnTrash.setOnClickListener(v12 ->
                deleteSelected()
        );

        loadSavedArticles();

        return v;
    }

    // ====================================================
    // LOAD LANGUAGE ARTICLES
    // ====================================================

    private void loadLocalizedArticles() {

        try {

            String lang =
                    Locale.getDefault()
                            .getLanguage();

            int rawRes;

            if (lang.equals("en")) {

                rawRes =
                        R.raw.popularni_clanci_en;

            } else if (lang.equals("de")) {

                rawRes =
                        R.raw.popularni_clanci_de;

            } else {

                rawRes =
                        R.raw.popularni_clanci;
            }

            InputStream is =
                    getResources()
                            .openRawResource(rawRes);

            int size =
                    is.available();

            byte[] buffer =
                    new byte[size];

            is.read(buffer);

            is.close();

            String json =
                    new String(
                            buffer,
                            StandardCharsets.UTF_8
                    );

            localizedArticles =
                    new JSONArray(json);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ====================================================
    // MENU
    // ====================================================

    private void openMenu(View v) {

        PopupMenu menu =
                new PopupMenu(requireContext(), v);

        menu.getMenu().add("Obriši sve");

        menu.setOnMenuItemClickListener(item -> {

            if (item.getTitle()
                    .toString()
                    .equals("Obriši sve")) {

                deleteAll();
            }

            return true;
        });

        menu.show();
    }

    // ====================================================
    // DELETE ALL
    // ====================================================

    private void deleteAll() {

        new AlertDialog.Builder(requireContext())

                .setTitle("Obriši sve")

                .setMessage(
                        "Da li želiš obrisati sve članke?"
                )

                .setPositiveButton(
                        "Da",
                        (d, w) -> {

                            articleList.clear();

                            saveToStorage();

                            savedContainer.removeAllViews();

                            selectedCards.clear();

                            selectionMode = false;

                            btnTrash.setVisibility(
                                    View.GONE
                            );

                            emptyState.setVisibility(
                                    View.VISIBLE
                            );
                        }
                )

                .setNegativeButton(
                        "Ne",
                        null
                )

                .show();
    }

    // ====================================================
    // DELETE SELECTED
    // ====================================================

    private void deleteSelected() {

        if (selectedCards.isEmpty()) return;

        new AlertDialog.Builder(requireContext())

                .setTitle("Brisanje")

                .setMessage(
                        "Obrisati selektovane?"
                )

                .setPositiveButton(
                        "Da",
                        (d, w) -> {

                            for (View v : selectedCards) {

                                SavedArticle a =
                                        (SavedArticle) v.getTag();

                                articleList.remove(a);
                            }

                            saveToStorage();

                            selectedCards.clear();

                            exitSelection();

                            loadSavedArticles();
                        }
                )

                .setNegativeButton(
                        "Ne",
                        null
                )

                .show();
    }

    // ====================================================
    // LOAD SAVED ARTICLES
    // ====================================================

    private void loadSavedArticles() {

        SharedPreferences prefs =
                requireContext()
                        .getSharedPreferences(
                                "BOOKMARKS",
                                Context.MODE_PRIVATE
                        );

        String json =
                prefs.getString(
                        "saved_articles",
                        null
                );

        Gson gson =
                new Gson();

        Type type =
                new TypeToken<ArrayList<SavedArticle>>() {
                }.getType();

        articleList =
                json != null
                        ? gson.fromJson(json, type)
                        : new ArrayList<>();

        savedContainer.removeAllViews();

        emptyState.setVisibility(
                articleList.isEmpty()
                        ? View.VISIBLE
                        : View.GONE
        );

        for (SavedArticle a : articleList) {

            JSONObject localized =
                    findLocalizedArticle(a.id);

            if (localized == null) continue;

            try {

                String titleText =
                        localized.getString("title");

                String categoryText =
                        localized.getString("category");

                String timeText =
                        localized.getString("time");

                String descriptionText =
                        localized.getString("description");

                String imageName =
                        localized.getString("image");

                View card =
                        LayoutInflater.from(getContext())
                                .inflate(
                                        R.layout.item_saved_article,
                                        savedContainer,
                                        false
                                );

                TextView title =
                        card.findViewById(R.id.tvTitle);

                TextView category =
                        card.findViewById(R.id.tvCategory);

                TextView time =
                        card.findViewById(R.id.tvTime);

                ImageView img =
                        card.findViewById(R.id.imgArticle);

                title.setText(titleText);

                category.setText(categoryText);

                time.setText(timeText);

                int res =
                        getResources().getIdentifier(
                                imageName,
                                "drawable",
                                requireContext()
                                        .getPackageName()
                        );

                if (res != 0) {

                    img.setImageResource(res);

                } else {

                    img.setImageResource(
                            R.drawable.user
                    );
                }

                card.setTag(a);

                // ================= LONG CLICK =================

                card.setOnLongClickListener(v -> {

                    selectionMode = true;

                    toggleSelect(card);

                    return true;
                });

                // ================= CLICK =================

                card.setOnClickListener(v -> {

                    if (selectionMode) {

                        toggleSelect(card);

                        return;
                    }

                    Bundle b =
                            new Bundle();

                    b.putString(
                            "id",
                            a.id
                    );

                    b.putString(
                            "title",
                            titleText
                    );

                    b.putString(
                            "category",
                            categoryText
                    );

                    b.putString(
                            "description",
                            descriptionText
                    );

                    b.putString(
                            "time",
                            timeText
                    );

                    b.putString(
                            "image",
                            imageName
                    );

                    DetaljiClankaFragment f =
                            new DetaljiClankaFragment();

                    f.setArguments(b);

                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.fragmentContainer,
                                    f
                            )
                            .addToBackStack(null)
                            .commit();
                });

                savedContainer.addView(card);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    // ====================================================
    // FIND LOCALIZED ARTICLE
    // ====================================================

    private JSONObject findLocalizedArticle(
            String id
    ) {

        try {

            if (localizedArticles == null)
                return null;

            for (int i = 0;
                 i < localizedArticles.length();
                 i++) {

                JSONObject obj =
                        localizedArticles
                                .getJSONObject(i);

                if (obj.getString("id")
                        .equals(id)) {

                    return obj;
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    // ====================================================
    // SELECT
    // ====================================================

    private void toggleSelect(View v) {

        CardView card =
                (CardView) v;

        if (selectedCards.contains(v)) {

            selectedCards.remove(v);

            card.setCardBackgroundColor(
                    Color.WHITE
            );

        } else {

            selectedCards.add(v);

            card.setCardBackgroundColor(
                    0xFFE5E5E5
            );
        }

        btnTrash.setVisibility(
                selectedCards.isEmpty()
                        ? View.GONE
                        : View.VISIBLE
        );
    }

    // ====================================================
    // EXIT SELECTION
    // ====================================================

    private void exitSelection() {

        selectionMode = false;

        selectedCards.clear();

        btnTrash.setVisibility(View.GONE);

        loadSavedArticles();
    }

    // ====================================================
    // STORAGE
    // ====================================================

    private void saveToStorage() {

        SharedPreferences prefs =
                requireContext()
                        .getSharedPreferences(
                                "BOOKMARKS",
                                Context.MODE_PRIVATE
                        );

        Gson gson =
                new Gson();

        prefs.edit()

                .putString(
                        "saved_articles",
                        gson.toJson(articleList)
                )

                .apply();
    }
}