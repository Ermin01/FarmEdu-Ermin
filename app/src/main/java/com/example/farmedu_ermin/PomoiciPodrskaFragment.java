package com.example.farmedu_ermin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PomoiciPodrskaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pomoici_podrska, container, false);

        ImageView btnBack = view.findViewById(R.id.btnBack);

        LinearLayout btnEmail = view.findViewById(R.id.btnEmail);
        LinearLayout btnWeb = view.findViewById(R.id.btnWeb);

        // BACK
        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        // EMAIL
        btnEmail.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("ermin:support@farmedu-ermin.com"));

            startActivity(Intent.createChooser(intent, "Pošalji email"));
        });

        // WEB
        btnWeb.setOnClickListener(v -> {

            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com")
            );

            startActivity(intent);
        });

        return view;
    }
}