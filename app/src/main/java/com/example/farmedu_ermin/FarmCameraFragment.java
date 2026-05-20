package com.example.farmedu_ermin;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.adapters.CameraAdapter;
import com.example.farmedu_ermin.models.CameraModel;

import java.util.ArrayList;
import java.util.List;

public class FarmCameraFragment extends Fragment {

    private RecyclerView recyclerCameras;

    public FarmCameraFragment() {
        super(R.layout.fragment_farm_camera);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {

        super.onViewCreated(view, savedInstanceState);

        // =========================================
        // INIT
        // =========================================

        recyclerCameras =
                view.findViewById(R.id.recyclerCameras);

        // =========================================
        // GRID 2 KOLONE
        // =========================================

        recyclerCameras.setLayoutManager(
                new GridLayoutManager(
                        requireContext(),
                        2
                )
        );

        // =========================================
        // DATA
        // =========================================

        List<CameraModel> list =
                new ArrayList<>();

        list.add(
                new CameraModel(
                        "Štala 1",
                        "Mliječne krave",
                        R.drawable.live
                )
        );

        list.add(
                new CameraModel(
                        "Tor 2",
                        "Ovce",
                        R.drawable.ovcelive
                )
        );

        list.add(
                new CameraModel(
                        "Pilići 1",
                        "Živina",
                        R.drawable.kokosilive
                )
        );

        list.add(
                new CameraModel(
                        "Polje 1",
                        "Kukuruz",
                        R.drawable.block
                )
        );

        // =========================================
        // ADAPTER
        // =========================================

        CameraAdapter adapter =
                new CameraAdapter(list);

        recyclerCameras.setAdapter(adapter);
    }
}