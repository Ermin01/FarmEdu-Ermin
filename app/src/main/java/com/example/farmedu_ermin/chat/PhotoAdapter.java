package com.example.farmedu_ermin.chat;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;

import java.util.List;

public class PhotoAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // =========================================
    // VIEW TYPES
    // =========================================

    private static final int TYPE_PHOTO = 0;

    private static final int TYPE_ADD = 1;

    // =========================================
    // DATA
    // =========================================

    private final List<Uri> photoList;

    private final OnAddClickListener listener;

    // =========================================
    // INTERFACE
    // =========================================

    public interface OnAddClickListener {

        void onAddClick();
    }

    // =========================================
    // CONSTRUCTOR
    // =========================================

    public PhotoAdapter(
            List<Uri> photoList,
            OnAddClickListener listener
    ) {

        this.photoList = photoList;

        this.listener = listener;
    }

    // =========================================
    // ITEM TYPE
    // =========================================

    @Override
    public int getItemViewType(int position) {

        if (position == photoList.size()
                && photoList.size() < 3) {

            return TYPE_ADD;
        }

        return TYPE_PHOTO;
    }

    // =========================================
    // ITEM COUNT
    // =========================================

    @Override
    public int getItemCount() {

        if (photoList.size() >= 3) {

            return photoList.size();
        }

        return photoList.size() + 1;
    }

    // =========================================
    // CREATE HOLDER
    // =========================================

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        LayoutInflater inflater =
                LayoutInflater.from(
                        parent.getContext()
                );

        // =====================================
        // ADD BUTTON
        // =====================================

        if (viewType == TYPE_ADD) {

            View view =
                    inflater.inflate(
                            R.layout.item_add_photo,
                            parent,
                            false
                    );

            return new AddViewHolder(view);
        }

        // =====================================
        // PHOTO ITEM
        // =====================================

        View view =
                inflater.inflate(
                        R.layout.item_photo,
                        parent,
                        false
                );

        return new PhotoViewHolder(view);
    }

    // =========================================
    // BIND HOLDER
    // =========================================

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            int position
    ) {

        // =====================================
        // ADD BUTTON
        // =====================================

        if (holder instanceof AddViewHolder) {

            holder.itemView.setOnClickListener(v -> {

                if (listener != null) {

                    listener.onAddClick();
                }
            });

            return;
        }

        // =====================================
        // PHOTO HOLDER
        // =====================================

        PhotoViewHolder photoHolder =
                (PhotoViewHolder) holder;

        Uri photoUri =
                photoList.get(position);

        // =====================================
        // IMAGE
        // =====================================

        photoHolder.imgPhoto.setImageURI(
                photoUri
        );

        // =====================================
        // REMOVE PHOTO
        // =====================================

        photoHolder.btnRemove
                .setOnClickListener(v -> {

                    int adapterPosition =
                            photoHolder.getAdapterPosition();

                    if (adapterPosition
                            != RecyclerView.NO_POSITION) {

                        photoList.remove(adapterPosition);

                        notifyItemRemoved(adapterPosition);

                        notifyItemRangeChanged(
                                adapterPosition,
                                getItemCount()
                        );
                    }
                });
    }

    // =========================================
    // PHOTO VIEW HOLDER
    // =========================================

    static class PhotoViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgPhoto;

        ImageView btnRemove;

        public PhotoViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            imgPhoto =
                    itemView.findViewById(
                            R.id.imgPhoto
                    );

            btnRemove =
                    itemView.findViewById(
                            R.id.btnRemove
                    );
        }
    }

    // =========================================
    // ADD VIEW HOLDER
    // =========================================

    static class AddViewHolder
            extends RecyclerView.ViewHolder {

        public AddViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);
        }
    }
}