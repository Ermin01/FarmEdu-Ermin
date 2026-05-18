package com.example.farmedu_ermin;

import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class FragmentOAplikaciji extends Fragment {

    public FragmentOAplikaciji() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_o_aplikaciji, container, false);


        ImageView btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }
}