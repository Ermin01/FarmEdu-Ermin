package com.example.farmedu_ermin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.adapters.ObjectAdapter;
import com.example.farmedu_ermin.models.ObjectModel;

import java.util.ArrayList;
import java.util.List;

public class SmjestajopremaFragment extends Fragment {

    RecyclerView rvContent;

    public SmjestajopremaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_smjetajioprema, container, false);

        rvContent = view.findViewById(R.id.rvContent);

        List<ObjectModel> objects = new ArrayList<>();

        objects.add(new ObjectModel(
                getString(R.string.fragment_smjestaj_stale),
                getString(R.string.fragment_smjestaj_stale_desc),
                R.drawable.stalastala));

        objects.add(new ObjectModel(
                getString(R.string.fragment_smjestaj_nadstresnice),
                getString(R.string.fragment_smjestaj_nadstresnice_desc),
                R.drawable.stalastala));

        objects.add(new ObjectModel(
                getString(R.string.fragment_smjestaj_boksovi),
                getString(R.string.fragment_smjestaj_boksovi_desc),
                R.drawable.boksovizastoku));

        objects.add(new ObjectModel(
                getString(R.string.fragment_smjestaj_kokosinjci),
                getString(R.string.fragment_smjestaj_kokosinjci_desc),
                R.drawable.kokosikucie));

        ObjectAdapter objectAdapter = new ObjectAdapter(objects);

        rvContent.setLayoutManager(
                new LinearLayoutManager(
                        getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        );

        rvContent.setAdapter(objectAdapter);

        // 🔙 BACK BUTTON
        view.findViewById(R.id.btnBack).setOnClickListener(v ->

                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_left,
                                R.anim.slide_out_right,
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                        )
                        .replace(R.id.fragmentContainer, new ZivotnjeStocarstvoFragment())
                        .commit()
        );

        return view;
    }
}