package com.example.farmedu_ermin.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CommentsFragment extends Fragment {

    private RecyclerView recyclerComments;

    private EditText etComment;

    private LinearLayout btnSend;

    private ImageView btnBack;
    private ImageView imgMyAvatar;

    private final List<CommentModel> commentList =
            new ArrayList<>();

    private CommentAdapter adapter;

    private String postId;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        return inflater.inflate(
                R.layout.fragment_comments,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {

        recyclerComments =
                view.findViewById(R.id.recyclerComments);

        etComment =
                view.findViewById(R.id.etComment);

        btnSend =
                view.findViewById(R.id.btnSend);

        btnBack =
                view.findViewById(R.id.btnBack);

        imgMyAvatar =
                view.findViewById(R.id.imgMyAvatar);

        adapter =
                new CommentAdapter(commentList);

        recyclerComments.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        recyclerComments.setAdapter(adapter);

        // =====================================================
        // BACK
        // =====================================================

        btnBack.setOnClickListener(v -> {

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.fragmentContainer,
                            new FeedFragment()
                    )
                    .commit();
        });

        // =====================================================
        // POST ID
        // =====================================================

        if (getArguments() != null) {

            postId =
                    getArguments().getString("postId");
        }

        // =====================================================
        // LOAD USER AVATAR
        // =====================================================

        loadMyAvatar();

        // =====================================================
        // LOAD COMMENTS
        // =====================================================

        loadComments();

        // =====================================================
        // SEND
        // =====================================================

        btnSend.setOnClickListener(v ->
                sendComment()
        );
    }

    // =====================================================
    // LOAD MY AVATAR
    // =====================================================

    private void loadMyAvatar() {

        FirebaseUser user =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        if (user == null) return;

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(doc -> {

                    int avatar =
                            R.drawable.user;

                    try {

                        Object avatarObj =
                                doc.get("avatar");

                        if (avatarObj instanceof Long) {

                            avatar =
                                    ((Long) avatarObj)
                                            .intValue();

                        } else if (avatarObj instanceof Integer) {

                            avatar =
                                    (Integer) avatarObj;
                        }

                    } catch (Exception ignored) {
                    }

                    imgMyAvatar.setImageResource(avatar);
                });
    }

    // =====================================================
    // SEND COMMENT
    // =====================================================

    private void sendComment() {

        String text =
                etComment.getText()
                        .toString()
                        .trim();

        if (text.isEmpty()) {

            Toast.makeText(
                    requireContext(),
                    "Napiši komentar",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        FirebaseUser user =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        if (user == null) return;

        btnSend.setEnabled(false);

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(doc -> {

                    String name =
                            doc.getString("name");

                    if (name == null || name.isEmpty()) {

                        name = "Korisnik";
                    }

                    int avatar =
                            R.drawable.user;

                    try {

                        Object avatarObj =
                                doc.get("avatar");

                        if (avatarObj instanceof Long) {

                            avatar =
                                    ((Long) avatarObj)
                                            .intValue();

                        } else if (avatarObj instanceof Integer) {

                            avatar =
                                    (Integer) avatarObj;
                        }

                    } catch (Exception ignored) {
                    }

                    CommentModel comment =
                            new CommentModel();

                    comment.setComment(text);

                    comment.setUserId(
                            user.getUid()
                    );

                    comment.setUserName(name);

                    comment.setAvatar(avatar);

                    comment.setTime(
                            System.currentTimeMillis()
                    );

                    FirebaseFirestore.getInstance()
                            .collection("posts")
                            .document(postId)
                            .collection("comments")
                            .add(comment)
                            .addOnSuccessListener(ref -> {

                                // UPDATE COMMENTS COUNT

                                FirebaseFirestore.getInstance()
                                        .collection("posts")
                                        .document(postId)
                                        .get()
                                        .addOnSuccessListener(postDoc -> {

                                            Long current =
                                                    postDoc.getLong(
                                                            "comments"
                                                    );

                                            int count =
                                                    current != null
                                                            ? current.intValue()
                                                            : 0;

                                            FirebaseFirestore.getInstance()
                                                    .collection("posts")
                                                    .document(postId)
                                                    .update(
                                                            "comments",
                                                            count + 1
                                                    );
                                        });

                                etComment.setText("");

                                btnSend.setEnabled(true);

                                // 🔥 ODMAH DODAJ U LISTU

                                commentList.add(comment);

                                adapter.notifyItemInserted(
                                        commentList.size() - 1
                                );

                                recyclerComments.scrollToPosition(
                                        commentList.size() - 1
                                );
                            })
                            .addOnFailureListener(e -> {

                                btnSend.setEnabled(true);

                                Toast.makeText(
                                        requireContext(),
                                        "Greška",
                                        Toast.LENGTH_SHORT
                                ).show();
                            });
                });
    }

    // =====================================================
    // LOAD COMMENTS
    // =====================================================

    private void loadComments() {

        FirebaseFirestore.getInstance()
                .collection("posts")
                .document(postId)
                .collection("comments")
                .orderBy("time")
                .get()
                .addOnSuccessListener(query -> {

                    commentList.clear();

                    for (DocumentSnapshot doc :
                            query.getDocuments()) {

                        CommentModel comment =
                                doc.toObject(
                                        CommentModel.class
                                );

                        if (comment != null) {

                            commentList.add(comment);
                        }
                    }

                    adapter.notifyDataSetChanged();

                    if (!commentList.isEmpty()) {

                        recyclerComments.scrollToPosition(
                                commentList.size() - 1
                        );
                    }
                });
    }
}