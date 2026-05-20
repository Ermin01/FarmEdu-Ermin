package com.example.farmedu_ermin.camera;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.farmedu_ermin.R;

public class CameraLiveActivity extends AppCompatActivity {

    private ImageView imgLive;
    private ImageView btnBack;

    private TextView txtTitle;
    private TextView txtStatus;

    private ProgressBar progressBar;

    private LinearLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_live);



        imgLive = findViewById(R.id.imgLive);

        btnBack = findViewById(R.id.btnBack);

        txtTitle = findViewById(R.id.txtTitle);

        txtStatus = findViewById(R.id.txtStatus);

        progressBar = findViewById(R.id.progressBar);

        loadingLayout = findViewById(R.id.loadingLayout);



        String title =
                getIntent().getStringExtra("title");

        txtTitle.setText(title);



        btnBack.setOnClickListener(v -> finish());


        loadingLayout.setVisibility(View.VISIBLE);



        new Handler().postDelayed(() -> {

            // sakrij loading krug

            progressBar.setVisibility(View.GONE);

            // promijeni text

            txtStatus.setText(
                    "Slaba vam je konekcija.\nPokušajte kasnije."
            );

        }, 3000);
    }
}