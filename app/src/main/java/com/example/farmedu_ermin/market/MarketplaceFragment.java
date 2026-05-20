package com.example.farmedu_ermin.market;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farmedu_ermin.R;

public class MarketplaceFragment extends Fragment {

    public MarketplaceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_marketplace,
                container,
                false
        );
    }
}