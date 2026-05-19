package com.example.farmedu_ermin.chat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farmedu_ermin.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PostAdapter
        extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<PostModel> postList;

    private final FirebaseFirestore db =
            FirebaseFirestore.getInstance();

    public PostAdapter(List<PostModel> postList) {

        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PostViewHolder holder,
            int position
    ) {

        PostModel post = postList.get(position);

        Context context = holder.itemView.getContext();

        // =====================================================
        // TEXT
        // =====================================================

        holder.txtName.setText(post.getName());

        holder.txtTime.setText(post.getTime());

        holder.txtDescription.setText(
                post.getDescription()
        );

        // =====================================================
        // AVATAR
        // =====================================================

        if (post.getAvatar() != 0) {

            holder.imgUser.setImageResource(
                    post.getAvatar()
            );

        } else {

            holder.imgUser.setImageResource(
                    R.drawable.user
            );
        }

        // =====================================================
        // RESET
        // =====================================================

        holder.layoutImages.setVisibility(View.GONE);

        holder.imgPost1.setVisibility(View.GONE);

        holder.imgPost2.setVisibility(View.GONE);

        holder.imgPost3.setVisibility(View.GONE);

        holder.rightImagesContainer.setVisibility(View.GONE);

        CardView cardPost2 =
                holder.itemView.findViewById(R.id.cardPost2);

        CardView cardPost3 =
                holder.itemView.findViewById(R.id.cardPost3);

        cardPost3.setVisibility(View.VISIBLE);

        // =====================================================
        // LOAD IMAGES
        // =====================================================

        List<String> imageList = post.getImageUrls();

        if (imageList == null) {

            imageList = new ArrayList<>();
        }

        // OLD SUPPORT

        if (imageList.isEmpty()
                && post.getImageUri() != null
                && !post.getImageUri().isEmpty()) {

            imageList.add(post.getImageUri());
        }

        final List<String> images = imageList;

        int count = images.size();

        // =====================================================
        // DEFAULT HEIGHT
        // =====================================================

        holder.layoutImages.getLayoutParams().height =
                dpToPx(context, 300);

        // =====================================================
        // 2 IMAGES = LOWER HEIGHT
        // =====================================================

        if (count == 2) {

            holder.layoutImages.getLayoutParams().height =
                    dpToPx(context, 210);
        }

        // =====================================================
        // IMAGE CLICK LISTENERS
        // =====================================================

        holder.imgPost1.setOnClickListener(v -> {

            if (images.size() > 0) {

                showImageViewer(
                        context,
                        images.get(0)
                );
            }
        });

        holder.imgPost2.setOnClickListener(v -> {

            if (images.size() > 1) {

                showImageViewer(
                        context,
                        images.get(1)
                );
            }
        });

        holder.imgPost3.setOnClickListener(v -> {

            if (images.size() > 2) {

                showImageViewer(
                        context,
                        images.get(2)
                );
            }
        });

        // =====================================================
        // 1 IMAGE = FULL WIDTH
        // =====================================================

        if (count == 1) {

            holder.layoutImages.setVisibility(View.VISIBLE);

            holder.imgPost1.setVisibility(View.VISIBLE);

            holder.rightImagesContainer.setVisibility(View.GONE);

            LinearLayout.LayoutParams leftParams =
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );

            holder.leftCard.setLayoutParams(leftParams);

            loadImage(
                    context,
                    images.get(0),
                    holder.imgPost1
            );
        }

        // =====================================================
        // 2 IMAGES
        // =====================================================

        else if (count == 2) {

            holder.layoutImages.setVisibility(View.VISIBLE);

            holder.imgPost1.setVisibility(View.VISIBLE);

            holder.imgPost2.setVisibility(View.VISIBLE);

            holder.imgPost3.setVisibility(View.GONE);

            holder.rightImagesContainer.setVisibility(View.VISIBLE);

            // LEFT

            LinearLayout.LayoutParams leftParams =
                    new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1f
                    );

            leftParams.setMarginEnd(5);

            holder.leftCard.setLayoutParams(leftParams);

            // RIGHT

            LinearLayout.LayoutParams rightParams =
                    new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1f
                    );

            holder.rightImagesContainer.setLayoutParams(
                    rightParams
            );

            // SECOND IMAGE

            LinearLayout.LayoutParams card2Params =
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );

            card2Params.setMarginStart(5);

            cardPost2.setLayoutParams(card2Params);

            cardPost3.setVisibility(View.GONE);

            loadImage(
                    context,
                    images.get(0),
                    holder.imgPost1
            );

            loadImage(
                    context,
                    images.get(1),
                    holder.imgPost2
            );
        }

        // =====================================================
        // 3 IMAGES
        // =====================================================

        else if (count >= 3) {

            holder.layoutImages.setVisibility(View.VISIBLE);

            holder.imgPost1.setVisibility(View.VISIBLE);

            holder.imgPost2.setVisibility(View.VISIBLE);

            holder.imgPost3.setVisibility(View.VISIBLE);

            holder.rightImagesContainer.setVisibility(View.VISIBLE);

            // LEFT

            LinearLayout.LayoutParams leftParams =
                    new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1f
                    );

            leftParams.setMarginEnd(5);

            holder.leftCard.setLayoutParams(leftParams);

            // RIGHT

            LinearLayout.LayoutParams rightParams =
                    new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1f
                    );

            holder.rightImagesContainer.setLayoutParams(
                    rightParams
            );

            loadImage(
                    context,
                    images.get(0),
                    holder.imgPost1
            );

            loadImage(
                    context,
                    images.get(1),
                    holder.imgPost2
            );

            loadImage(
                    context,
                    images.get(2),
                    holder.imgPost3
            );
        }

        // =====================================================
        // LIKES
        // =====================================================

        holder.txtLikes.setText(
                String.valueOf(post.getLikes())
        );

        holder.txtComments.setText(
                String.valueOf(post.getComments())
        );

        if (post.getLikedUsers() == null) {

            post.setLikedUsers(new ArrayList<>());
        }

        FirebaseUser currentUser =
                FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {

            String uid = currentUser.getUid();

            boolean liked =
                    post.getLikedUsers().contains(uid);

            if (liked) {

                holder.imgLike.setColorFilter(
                        Color.parseColor("#43A047")
                );

                holder.layoutLike.setBackgroundResource(
                        R.drawable.bg_green_button
                );

                holder.txtLikes.setTextColor(Color.WHITE);

            } else {

                holder.imgLike.setColorFilter(
                        Color.parseColor("#777777")
                );

                holder.layoutLike.setBackgroundResource(
                        R.drawable.bg_search_modern
                );

                holder.txtLikes.setTextColor(
                        Color.parseColor("#111111")
                );
            }

            holder.layoutLike.setOnClickListener(v -> {

                List<String> likedUsers =
                        post.getLikedUsers();

                if (likedUsers.contains(uid)) {

                    likedUsers.remove(uid);

                    post.setLikes(
                            Math.max(
                                    0,
                                    post.getLikes() - 1
                            )
                    );

                } else {

                    likedUsers.add(uid);

                    post.setLikes(
                            post.getLikes() + 1
                    );
                }

                notifyItemChanged(
                        holder.getAdapterPosition()
                );

                db.collection("posts")
                        .document(post.getPostId())
                        .update(
                                "likes",
                                post.getLikes(),

                                "likedUsers",
                                likedUsers
                        );
            });
        }

        // =====================================================
        // COMMENT
        // =====================================================

        holder.layoutComment.setOnClickListener(v -> {

            Bundle bundle = new Bundle();

            bundle.putString(
                    "postId",
                    post.getPostId()
            );

            CommentsFragment fragment =
                    new CommentsFragment();

            fragment.setArguments(bundle);

            androidx.fragment.app.FragmentActivity activity =
                    (androidx.fragment.app.FragmentActivity) context;

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.fragmentContainer,
                            fragment
                    )
                    .addToBackStack(null)
                    .commit();
        });

        // =====================================================
        // SHARE
        // =====================================================

        holder.layoutShare.setOnClickListener(v ->

                Toast.makeText(
                        context,
                        "Share uskoro 🚀",
                        Toast.LENGTH_SHORT
                ).show()
        );

        // =====================================================
        // MENU
        // =====================================================

        // =====================================================
// MENU
// =====================================================

        holder.btnMore.setOnClickListener(v -> {

            PopupMenu menu =
                    new PopupMenu(context, holder.btnMore);

            menu.getMenu().add("✏️ Uredi objavu");

            menu.getMenu().add("🗑️ Obriši objavu");

            menu.setOnMenuItemClickListener(item -> {

                String title =
                        item.getTitle().toString();

                // =====================================================
                // EDIT POST
                // =====================================================

                if (title.contains("Uredi")) {

                    Bundle bundle =
                            new Bundle();

                    bundle.putString(
                            "postId",
                            post.getPostId()
                    );

                    bundle.putString(
                            "description",
                            post.getDescription()
                    );

                    // 🔥 POŠALJI SVE SLIKE
                    bundle.putStringArrayList(
                            "imageUrls",
                            new ArrayList<>(images)
                    );

                    EditPostFragment fragment =
                            new EditPostFragment();

                    fragment.setArguments(bundle);

                    androidx.fragment.app.FragmentActivity activity =
                            (androidx.fragment.app.FragmentActivity) context;

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.fragmentContainer,
                                    fragment
                            )
                            .addToBackStack(null)
                            .commit();

                    return true;
                }

                // =====================================================
                // DELETE POST
                // =====================================================

                if (title.contains("Obriši")) {

                    new AlertDialog.Builder(context)

                            .setTitle("Brisanje objave")

                            .setMessage(
                                    "Da li želiš obrisati objavu?"
                            )

                            .setPositiveButton(
                                    "Obriši",
                                    (d, w) -> {

                                        db.collection("posts")
                                                .document(post.getPostId())
                                                .delete();

                                        int pos =
                                                holder.getAdapterPosition();

                                        if (pos != RecyclerView.NO_POSITION) {

                                            postList.remove(pos);

                                            notifyItemRemoved(pos);
                                        }

                                        Toast.makeText(
                                                context,
                                                "Obrisano",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    })

                            .setNegativeButton(
                                    "Otkaži",
                                    null
                            )

                            .show();

                    return true;
                }

                return true;
            });

            menu.show();
        });
    }

    // =====================================================
    // IMAGE VIEWER
    // =====================================================

    private void showImageViewer(
            Context context,
            String imagePath
    ) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(
                        context,
                        android.R.style.Theme_Black_NoTitleBar_Fullscreen
                );

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.dialog_image_viewer,
                        null
                );

        PhotoView photoView =
                view.findViewById(R.id.photoView);

        ImageView btnClose =
                view.findViewById(R.id.btnClose);

        // =====================================================
        // LOAD IMAGE
        // =====================================================

        Glide.with(context)
                .load(new File(imagePath))
                .placeholder(R.drawable.farm_baner_zivotnja)
                .error(R.drawable.farm_baner_zivotnja)
                .into(photoView);

        // =====================================================
        // DIALOG
        // =====================================================

        AlertDialog dialog =
                builder
                        .setView(view)
                        .create();

        // =====================================================
        // CLOSE
        // =====================================================

        btnClose.setOnClickListener(v ->
                dialog.dismiss()
        );

        // =====================================================
        // SHOW
        // =====================================================

        dialog.show();

        // =====================================================
        // PREMIUM DIM
        // =====================================================

        if (dialog.getWindow() != null) {

            dialog.getWindow().setDimAmount(0.9f);
        }
    }
    // =====================================================
    // LOAD IMAGE
    // =====================================================

    private void loadImage(
            Context context,
            String path,
            ImageView imageView
    ) {

        if (path == null || path.isEmpty()) {

            imageView.setImageResource(
                    R.drawable.farm_baner_zivotnja
            );

            return;
        }

        Glide.with(context)
                .load(new File(path))
                .centerCrop()
                .placeholder(R.drawable.farm_baner_zivotnja)
                .error(R.drawable.farm_baner_zivotnja)
                .into(imageView);
    }

    // =====================================================
    // DP TO PX
    // =====================================================

    private int dpToPx(Context context, int dp) {

        return (int) (
                dp *
                        context.getResources()
                                .getDisplayMetrics()
                                .density
        );
    }

    @Override
    public int getItemCount() {

        return postList.size();
    }

    // =====================================================
    // HOLDER
    // =====================================================

    static class PostViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgUser;

        ImageView imgPost1;
        ImageView imgPost2;
        ImageView imgPost3;

        ImageView imgLike;

        ImageView btnMore;

        TextView txtName;
        TextView txtTime;

        TextView txtDescription;

        TextView txtLikes;
        TextView txtComments;

        LinearLayout layoutLike;

        LinearLayout layoutComment;

        LinearLayout layoutShare;

        LinearLayout layoutImages;

        LinearLayout rightImagesContainer;

        CardView leftCard;

        public PostViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            imgUser =
                    itemView.findViewById(R.id.imgUser);

            imgPost1 =
                    itemView.findViewById(R.id.imgPost1);

            imgPost2 =
                    itemView.findViewById(R.id.imgPost2);

            imgPost3 =
                    itemView.findViewById(R.id.imgPost3);

            imgLike =
                    itemView.findViewById(R.id.imgLike);

            btnMore =
                    itemView.findViewById(R.id.btnMore);

            txtName =
                    itemView.findViewById(R.id.txtName);

            txtTime =
                    itemView.findViewById(R.id.txtTime);

            txtDescription =
                    itemView.findViewById(R.id.txtDescription);

            txtLikes =
                    itemView.findViewById(R.id.txtLikes);

            txtComments =
                    itemView.findViewById(R.id.txtComments);

            layoutLike =
                    itemView.findViewById(R.id.layoutLike);

            layoutComment =
                    itemView.findViewById(R.id.layoutComment);

            layoutShare =
                    itemView.findViewById(R.id.layoutShare);

            layoutImages =
                    itemView.findViewById(R.id.layoutImages);

            rightImagesContainer =
                    itemView.findViewById(R.id.rightImagesContainer);

            leftCard =
                    itemView.findViewById(R.id.leftCard);
        }
    }
}