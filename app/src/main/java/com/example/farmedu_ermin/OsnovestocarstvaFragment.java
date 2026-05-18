package com.example.farmedu_ermin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OsnovestocarstvaFragment extends Fragment {

    public OsnovestocarstvaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_osnovestocarstva,
                container,
                false
        );

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
                        .replace(
                                R.id.fragmentContainer,
                                new ZivotnjeStocarstvoFragment()
                        )
                        .commit()
        );

        return view;
    }
}