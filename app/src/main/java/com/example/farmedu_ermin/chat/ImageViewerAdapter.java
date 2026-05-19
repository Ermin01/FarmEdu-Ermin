package com.example.farmedu_ermin.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farmedu_ermin.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.List;

public class ImageViewerAdapter
        extends RecyclerView.Adapter<ImageViewerAdapter.ImageHolder> {

    private final Context context;
    private final List<String> images;

    public ImageViewerAdapter(Context context, List<String> images) {

        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_fullscreen_image, parent, false);

        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ImageHolder holder,
            int position
    ) {

        Glide.with(context)
                .load(new File(images.get(position)))
                .into(holder.photoView);
    }

    @Override
    public int getItemCount() {

        return images.size();
    }

    static class ImageHolder extends RecyclerView.ViewHolder {

        PhotoView photoView;

        public ImageHolder(@NonNull View itemView) {

            super(itemView);

            photoView =
                    itemView.findViewById(R.id.photoView);
        }
    }
}