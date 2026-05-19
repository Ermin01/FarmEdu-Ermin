package com.example.farmedu_ermin.chat;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class NovaObjavaFragment extends Fragment {

    private RecyclerView recyclerPhotos;

    private EditText etDescription;

    private TextView txtCounter;
    private TextView txtName;

    private ImageView imgUser;
    private ImageView btnBack;

    private LinearLayout btnObjavi;

    private TextView chipStocarstvo;
    private TextView chipVocnjak;
    private TextView chipMasine;

    private String selectedCategory = "Stočarstvo";

    private FirebaseFirestore db;

    private PhotoAdapter photoAdapter;

    private final List<Uri> photoList = new ArrayList<>();

    // =========================================================
    // IMAGE PICKER
    // =========================================================

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {

                        if (uri == null) return;

                        // MAX 3 SLIKE
                        if (photoList.size() >= 3) {

                            Toast.makeText(
                                    requireContext(),
                                    "Možeš dodati maksimalno 3 slike",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        photoList.add(uri);

                        photoAdapter.notifyDataSetChanged();
                    });

    // =========================================================
    // ON CREATE VIEW
    // =========================================================

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        return inflater.inflate(
                R.layout.fragment_nova_objava,
                container,
                false
        );
    }

    // =========================================================
    // ON VIEW CREATED
    // =========================================================

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {

        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        // =====================================================
        // INIT
        // =====================================================

        recyclerPhotos = view.findViewById(R.id.recyclerPhotos);

        etDescription = view.findViewById(R.id.etDescription);

        txtCounter = view.findViewById(R.id.txtCounter);

        txtName = view.findViewById(R.id.txtName);

        imgUser = view.findViewById(R.id.imgUser);

        btnObjavi = view.findViewById(R.id.btnObjavi);

        btnBack = view.findViewById(R.id.btnBack);

        chipStocarstvo = view.findViewById(R.id.chipStocarstvo);

        chipVocnjak = view.findViewById(R.id.chipVocnjak);

        chipMasine = view.findViewById(R.id.chipMasine);

        // =====================================================
        // RECYCLER
        // =====================================================

        recyclerPhotos.setLayoutManager(
                new LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        );

        photoAdapter = new PhotoAdapter(
                photoList,
                this::openGallery
        );

        recyclerPhotos.setAdapter(photoAdapter);

        // =====================================================
        // CATEGORY UI
        // =====================================================

        updateCategoryUI();

        chipStocarstvo.setOnClickListener(v -> {

            selectedCategory = "Stočarstvo";

            updateCategoryUI();
        });

        chipVocnjak.setOnClickListener(v -> {

            selectedCategory = "Voćnjak";

            updateCategoryUI();
        });

        chipMasine.setOnClickListener(v -> {

            selectedCategory = "Mašine";

            updateCategoryUI();
        });

        // =====================================================
        // BACK
        // =====================================================

        btnBack.setOnClickListener(v ->

                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.fragmentContainer,
                                new FeedFragment()
                        )
                        .commit()

        );

        // =====================================================
        // COUNTER
        // =====================================================

        etDescription.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after
            ) {}

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count
            ) {

                txtCounter.setText(
                        s.length() + "/500"
                );
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // =====================================================
        // LOAD USER
        // =====================================================

        loadUser();

        // =====================================================
        // PUBLISH
        // =====================================================

        btnObjavi.setOnClickListener(v ->

                publishPost()

        );
    }

    // =========================================================
    // PUBLISH POST
    // =========================================================

    private void publishPost() {

        String desc =
                etDescription
                        .getText()
                        .toString()
                        .trim();

        if (desc.isEmpty()) {

            Toast.makeText(
                    requireContext(),
                    "Unesi opis objave",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        FirebaseUser user =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        if (user == null) return;

        btnObjavi.setEnabled(false);

        // =====================================================
        // LOAD USER DATA
        // =====================================================

        db.collection("users")
                .document(user.getUid())
                .get()

                .addOnSuccessListener(doc -> {

                    String name =
                            doc.getString("name");

                    if (name == null || name.isEmpty()) {

                        name = "Korisnik";
                    }

                    int avatar = R.drawable.user;

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

                    } catch (Exception ignored) {}

                    // =================================================
                    // SAVE IMAGES
                    // =================================================

                    List<String> savedImages =
                            new ArrayList<>();

                    for (Uri uri : photoList) {

                        String path =
                                saveImageToInternalStorage(uri);

                        if (!path.isEmpty()) {

                            savedImages.add(path);
                        }
                    }

                    // =================================================
                    // CREATE POST
                    // =================================================

                    PostModel post =
                            new PostModel();

                    // USER UID
                    post.setUserId(user.getUid());

                    // BASIC
                    post.setName(name);

                    post.setTime("Upravo sada");

                    post.setDescription(
                            "📌 "
                                    + selectedCategory
                                    + "\n\n"
                                    + desc
                    );

                    post.setAvatar(avatar);

                    // STATS
                    post.setLikes(0);

                    post.setComments(0);

                    post.setLiked(false);

                    post.setLikedUsers(
                            new ArrayList<>()
                    );

                    // =================================================
                    // OLD IMAGE SUPPORT
                    // =================================================

                    if (!savedImages.isEmpty()) {

                        post.setImageUri(
                                savedImages.get(0)
                        );
                    }

                    // =================================================
                    // MULTIPLE IMAGES
                    // =================================================

                    post.setImageUrls(savedImages);

                    // =================================================
                    // SAVE FIREBASE
                    // =================================================

                    db.collection("posts")
                            .add(post)

                            .addOnSuccessListener(ref -> {

                                ref.update(
                                        "postId",
                                        ref.getId()
                                );

                                Toast.makeText(
                                        requireContext(),
                                        "Objava objavljena",
                                        Toast.LENGTH_SHORT
                                ).show();

                                requireActivity()
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(
                                                R.id.fragmentContainer,
                                                new FeedFragment()
                                        )
                                        .commit();
                            })

                            .addOnFailureListener(e -> {

                                btnObjavi.setEnabled(true);

                                Toast.makeText(
                                        requireContext(),
                                        "Greška pri objavi",
                                        Toast.LENGTH_SHORT
                                ).show();
                            });
                });
    }

    // =========================================================
    // SAVE IMAGE
    // =========================================================

    private String saveImageToInternalStorage(Uri uri) {

        try {

            InputStream input =
                    requireContext()
                            .getContentResolver()
                            .openInputStream(uri);

            if (input == null) return "";

            File file =
                    new File(
                            requireContext().getFilesDir(),
                            "img_"
                                    + System.currentTimeMillis()
                                    + ".jpg"
                    );

            OutputStream out =
                    new FileOutputStream(file);

            byte[] buffer =
                    new byte[1024];

            int len;

            while ((len = input.read(buffer)) > 0) {

                out.write(buffer, 0, len);
            }

            out.flush();

            out.close();

            input.close();

            return file.getAbsolutePath();

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }
    }

    // =========================================================
    // LOAD USER
    // =========================================================

    private void loadUser() {

        FirebaseUser user =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        if (user == null) return;

        db.collection("users")
                .document(user.getUid())
                .get()

                .addOnSuccessListener(doc -> {

                    // =================================================
                    // NAME
                    // =================================================

                    String name =
                            doc.getString("name");

                    txtName.setText(
                            name != null
                                    ? name
                                    : "Korisnik"
                    );

                    // =================================================
                    // AVATAR
                    // =================================================

                    try {

                        Object avatarObj =
                                doc.get("avatar");

                        if (avatarObj instanceof Long) {

                            int resId =
                                    ((Long) avatarObj)
                                            .intValue();

                            imgUser.setImageResource(
                                    resId
                            );

                        } else if (avatarObj instanceof Integer) {

                            imgUser.setImageResource(
                                    (Integer) avatarObj
                            );

                        } else {

                            imgUser.setImageResource(
                                    R.drawable.user
                            );
                        }

                    } catch (Exception e) {

                        imgUser.setImageResource(
                                R.drawable.user
                        );
                    }
                });
    }

    // =========================================================
    // OPEN GALLERY
    // =========================================================

    private void openGallery() {

        pickImageLauncher.launch("image/*");
    }

    // =========================================================
    // CATEGORY UI
    // =========================================================

    private void updateCategoryUI() {

        reset();

        if (selectedCategory.equals("Stočarstvo")) {

            activate(chipStocarstvo);

        } else if (selectedCategory.equals("Voćnjak")) {

            activate(chipVocnjak);

        } else {

            activate(chipMasine);
        }
    }

    private void reset() {

        deactivate(chipStocarstvo);

        deactivate(chipVocnjak);

        deactivate(chipMasine);
    }

    private void activate(TextView v) {

        v.setBackgroundResource(
                R.drawable.bg_green_button
        );

        v.setTextColor(Color.WHITE);
    }

    private void deactivate(TextView v) {

        v.setBackgroundResource(
                R.drawable.bg_search_modern
        );

        v.setTextColor(
                Color.parseColor("#111111")
        );
    }
}