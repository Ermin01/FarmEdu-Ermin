package com.example.farmedu_ermin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.adapters.ZivotinjeAdapter;
import com.example.farmedu_ermin.models.Zivotinja;
import com.example.farmedu_ermin.utils.GridSpacingItemDecoration;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Zivotinje extends Fragment {

    private RecyclerView recyclerView;
    private ZivotinjeAdapter adapter;

    private List<Zivotinja> lista = new ArrayList<>();
    private List<Zivotinja> filteredList = new ArrayList<>();

    private EditText etSearch;
    private ImageButton btnFilter;

    private String currentSearch = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zivotinje, container, false);

        recyclerView = view.findViewById(R.id.recyclerZivotinje);
        etSearch = view.findViewById(R.id.etSearch);
        btnFilter = view.findViewById(R.id.btnFilter);

        setupRecycler();
        loadData();
        setupSearch();
        setupFilter();

        return view;
    }

    // PREMIUM GRID
    private void setupRecycler() {

        GridLayoutManager manager =
                new GridLayoutManager(getContext(), 2);

        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setPadding(6, 6, 6, 140);

        int spacing = dpToPx(4);

        recyclerView.addItemDecoration(
                new GridSpacingItemDecoration(2, spacing, true)
        );
    }

    // LOAD JSON
    private void loadData() {

        try {

            InputStream is =
                    getResources().openRawResource(R.raw.zivotinje);

            byte[] buffer = new byte[is.available()];

            is.read(buffer);

            is.close();

            String json = new String(buffer, "UTF-8");

            JSONArray array = new JSONArray(json);

            lista.clear();

            for (int i = 0; i < array.length(); i++) {

                JSONObject obj = array.getJSONObject(i);

                String ime = obj.getString("ime");

                String slikaIme = obj.getString("slika");

                int resId = getResources().getIdentifier(
                        slikaIme,
                        "drawable",
                        requireContext().getPackageName()
                );

                lista.add(new Zivotinja(
                        ime,
                        resId,
                        ime
                ));
            }

            filteredList.clear();

            filteredList.addAll(lista);

            adapter = new ZivotinjeAdapter(filteredList);

            recyclerView.setAdapter(adapter);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // SEARCH
    private void setupSearch() {

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {

                currentSearch = s.toString();

                filterAndKeepSort();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // FILTER BUTTON
    private void setupFilter() {

        btnFilter.setOnClickListener(v -> showSortBottomSheet());
    }

    // FILTER
    private void filterAndKeepSort() {

        filteredList.clear();

        for (Zivotinja z : lista) {

            if (z.getIme()
                    .toLowerCase()
                    .contains(currentSearch.toLowerCase())) {

                filteredList.add(z);
            }
        }

        adapter.notifyDataSetChanged();
    }

    // BOTTOM SHEET
    private void showSortBottomSheet() {

        View sheetView = LayoutInflater
                .from(getContext())
                .inflate(R.layout.bottom_sheet_sort, null);

        BottomSheetDialog dialog =
                new BottomSheetDialog(requireContext());

        dialog.setContentView(sheetView);

        sheetView.findViewById(R.id.sortAZ).setOnClickListener(v -> {

            Collections.sort(filteredList,
                    (a, b) -> a.getIme()
                            .compareToIgnoreCase(b.getIme()));

            adapter.notifyDataSetChanged();

            dialog.dismiss();
        });

        sheetView.findViewById(R.id.sortZA).setOnClickListener(v -> {

            Collections.sort(filteredList,
                    (a, b) -> b.getIme()
                            .compareToIgnoreCase(a.getIme()));

            adapter.notifyDataSetChanged();

            dialog.dismiss();
        });

        sheetView.findViewById(R.id.sortReset).setOnClickListener(v -> {

            filterAndKeepSort();

            dialog.dismiss();
        });

        dialog.show();
    }

    // DP TO PX
    private int dpToPx(int dp) {

        float density =
                getResources().getDisplayMetrics().density;

        return Math.round(dp * density);
    }
}