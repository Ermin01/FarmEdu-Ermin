package com.example.farmedu_ermin.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {

    private RecyclerView recyclerPosts;
    private ImageView btnAddPost;

    private PostAdapter adapter;
    private final List<PostModel> postList = new ArrayList<>();

    private FirebaseFirestore db;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {

        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        recyclerPosts = view.findViewById(R.id.recyclerPosts);
        btnAddPost = view.findViewById(R.id.accelerate);

        recyclerPosts.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        adapter = new PostAdapter(postList);
        recyclerPosts.setAdapter(adapter);

        loadPosts();

        btnAddPost.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new NovaObjavaFragment())
                        .addToBackStack(null)
                        .commit()
        );
    }

    private void loadPosts() {

        db.collection("posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    postList.clear();

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {

                        PostModel post = doc.toObject(PostModel.class);

                        if (post != null) {

                            // 🔥 ID FIX (OBAVEZNO)
                            post.setPostId(doc.getId());

                            // 🔥 IMAGE FIX (OBAVEZNO - sigurnost)
                            String imageUri = doc.getString("imageUri");
                            if (imageUri != null) {
                                post.setImageUri(imageUri);
                            }

                            postList.add(0, post);
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPosts();
    }
}