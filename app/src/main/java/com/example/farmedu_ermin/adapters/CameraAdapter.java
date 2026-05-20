package com.example.farmedu_ermin.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.camera.CameraLiveActivity;
import com.example.farmedu_ermin.models.CameraModel;

import java.util.List;

public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.CameraViewHolder> {

    private final List<CameraModel> list;

    public CameraAdapter(List<CameraModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CameraViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_camera,
                                parent,
                                false
                        );

        return new CameraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CameraViewHolder holder,
            int position
    ) {

        CameraModel model = list.get(position);

        holder.imgCamera.setImageResource(
                model.getImage()
        );

        holder.txtTitle.setText(
                model.getTitle()
        );

        holder.txtSubtitle.setText(
                model.getSubtitle()
        );

        // CLICK

        holder.cardView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    v.getContext(),
                    CameraLiveActivity.class
            );

            intent.putExtra(
                    "title",
                    model.getTitle()
            );

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class CameraViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgCamera;

        TextView txtTitle;
        TextView txtSubtitle;

        CardView cardView;

        public CameraViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            imgCamera =
                    itemView.findViewById(R.id.imgCamera);

            txtTitle =
                    itemView.findViewById(R.id.txtTitle);

            txtSubtitle =
                    itemView.findViewById(R.id.txtSubtitle);

            cardView =
                    itemView.findViewById(R.id.cardCamera);
        }
    }
}