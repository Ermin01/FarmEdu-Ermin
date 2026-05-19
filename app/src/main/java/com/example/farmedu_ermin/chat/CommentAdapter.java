package com.example.farmedu_ermin.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;

import java.util.List;

public class CommentAdapter
        extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private final List<CommentModel> commentList;

    public CommentAdapter(List<CommentModel> commentList) {

        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_comment,
                        parent,
                        false
                );

        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CommentHolder holder,
            int position
    ) {

        CommentModel comment =
                commentList.get(position);

        holder.txtName.setText(
                comment.getUserName()
        );

        holder.txtComment.setText(
                comment.getComment()
        );

        if (comment.getAvatar() != 0) {

            holder.imgUser.setImageResource(
                    comment.getAvatar()
            );

        } else {

            holder.imgUser.setImageResource(
                    R.drawable.user
            );
        }
    }

    @Override
    public int getItemCount() {

        return commentList.size();
    }

    static class CommentHolder
            extends RecyclerView.ViewHolder {

        ImageView imgUser;

        TextView txtName;
        TextView txtComment;

        public CommentHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            imgUser =
                    itemView.findViewById(R.id.imgUser);

            txtName =
                    itemView.findViewById(R.id.txtName);

            txtComment =
                    itemView.findViewById(R.id.txtComment);
        }
    }
}