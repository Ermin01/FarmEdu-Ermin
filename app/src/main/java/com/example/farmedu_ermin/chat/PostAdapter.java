package com.example.farmedu_ermin.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farmedu_ermin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<PostModel> postList;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PostAdapter(List<PostModel> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        PostModel post = postList.get(position);
        Context context = holder.itemView.getContext();

        holder.txtName.setText(post.getName());
        holder.txtTime.setText(post.getTime());
        holder.txtDescription.setText(post.getDescription());

        // ================= AVATAR =================
        if (post.getAvatar() != 0) {
            holder.imgUser.setImageResource(post.getAvatar());
        } else {
            holder.imgUser.setImageResource(R.drawable.user);
        }

        // ================= LOCAL IMAGE FIX =================
        String imagePath = post.getImageUri();

        if (imagePath != null && !imagePath.isEmpty()) {

            File file = new File(imagePath);

            if (file.exists()) {

                holder.imgPost.setVisibility(View.VISIBLE);

                Glide.with(context)
                        .load(file)   // 🔥 KLJUČNI FIX
                        .centerCrop()
                        .placeholder(R.drawable.farm_baner_zivotnja)
                        .error(R.drawable.farm_baner_zivotnja)
                        .into(holder.imgPost);

            } else {
                holder.imgPost.setVisibility(View.GONE);
            }

        } else {
            holder.imgPost.setVisibility(View.GONE);
        }

        // ================= LIKES =================
        holder.txtLikes.setText(String.valueOf(post.getLikes()));
        holder.txtComments.setText(String.valueOf(post.getComments()));

        if (post.getLikedUsers() == null) {
            post.setLikedUsers(new ArrayList<>());
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {

            String uid = currentUser.getUid();

            boolean liked = post.getLikedUsers().contains(uid);

            holder.imgLike.setColorFilter(
                    liked ? Color.parseColor("#43A047")
                            : Color.parseColor("#777777")
            );

            holder.layoutLike.setOnClickListener(v -> {

                List<String> likedUsers = post.getLikedUsers();

                if (likedUsers.contains(uid)) {
                    likedUsers.remove(uid);
                    post.setLikes(Math.max(0, post.getLikes() - 1));
                } else {
                    likedUsers.add(uid);
                    post.setLikes(post.getLikes() + 1);
                }

                notifyItemChanged(holder.getAdapterPosition());

                db.collection("posts")
                        .document(post.getPostId())
                        .update(
                                "likes", post.getLikes(),
                                "likedUsers", likedUsers
                        );
            });
        }

        // ================= COMMENT =================
        holder.layoutComment.setOnClickListener(v ->
                Toast.makeText(context, "Komentari uskoro 💬", Toast.LENGTH_SHORT).show()
        );

        // ================= SHARE =================
        holder.imgShare.setOnClickListener(v ->
                Toast.makeText(context, "Share uskoro 🚀", Toast.LENGTH_SHORT).show()
        );

        // ================= MORE MENU =================
        holder.btnMore.setOnClickListener(v -> {

            PopupMenu menu = new PopupMenu(context, holder.btnMore);
            menu.getMenu().add("✏️ Uredi objavu");
            menu.getMenu().add("🗑️ Obriši objavu");
            menu.getMenu().add("🙈 Sakrij objavu");
            menu.getMenu().add("🚨 Prijavi objavu");

            menu.setOnMenuItemClickListener(item -> {

                String t = item.getTitle().toString();

                if (t.contains("Uredi")) {

                    EditPostFragment fragment = new EditPostFragment();

                    Bundle b = new Bundle();
                    b.putString("postId", post.getPostId());
                    b.putString("description", post.getDescription());
                    b.putString("imageUri", post.getImageUri());

                    fragment.setArguments(b);

                    ((FragmentActivity) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, fragment)
                            .addToBackStack(null)
                            .commit();

                    return true;
                }

                if (t.contains("Obriši")) {

                    new AlertDialog.Builder(context)
                            .setTitle("Brisanje objave")
                            .setMessage("Da li želiš obrisati objavu?")
                            .setPositiveButton("Obriši", (d, w) -> {

                                db.collection("posts")
                                        .document(post.getPostId())
                                        .delete();

                                int pos = holder.getAdapterPosition();

                                if (pos != RecyclerView.NO_POSITION) {
                                    postList.remove(pos);
                                    notifyItemRemoved(pos);
                                }

                                Toast.makeText(context, "Obrisano", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Otkaži", null)
                            .show();

                    return true;
                }

                if (t.contains("Sakrij")) {
                    Toast.makeText(context, "Sakriveno", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (t.contains("Prijavi")) {
                    Toast.makeText(context, "Prijavljeno", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            });

            menu.show();
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUser, imgPost, imgLike, imgShare, btnMore;
        TextView txtName, txtTime, txtDescription, txtLikes, txtComments;
        LinearLayout layoutLike, layoutComment;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUser = itemView.findViewById(R.id.imgUser);
            imgPost = itemView.findViewById(R.id.imgPost);
            imgLike = itemView.findViewById(R.id.imgLike);
            imgShare = itemView.findViewById(R.id.imgShare);
            btnMore = itemView.findViewById(R.id.btnMore);

            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtLikes = itemView.findViewById(R.id.txtLikes);
            txtComments = itemView.findViewById(R.id.txtComments);

            layoutLike = itemView.findViewById(R.id.layoutLike);
            layoutComment = itemView.findViewById(R.id.layoutComment);
        }
    }
}