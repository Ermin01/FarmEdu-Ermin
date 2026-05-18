package com.example.farmedu_ermin.chat;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.*;
import java.util.*;

public class NovaObjavaFragment extends Fragment {

    private RecyclerView recyclerPhotos;
    private EditText etDescription;
    private TextView txtCounter, txtName;
    private ImageView imgUser;
    private LinearLayout btnObjavi;

    private TextView chipStocarstvo, chipVocnjak, chipMasine;

    private String selectedCategory = "Stočarstvo";

    private FirebaseFirestore db;

    private PhotoAdapter photoAdapter;
    private final List<Uri> photoList = new ArrayList<>();

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null && photoList.size() < 3) {
                            photoList.add(uri);
                            photoAdapter.notifyDataSetChanged();
                        }
                    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_nova_objava, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        recyclerPhotos = view.findViewById(R.id.recyclerPhotos);
        etDescription = view.findViewById(R.id.etDescription);
        txtCounter = view.findViewById(R.id.txtCounter);
        txtName = view.findViewById(R.id.txtName);
        imgUser = view.findViewById(R.id.imgUser);
        btnObjavi = view.findViewById(R.id.btnObjavi);

        chipStocarstvo = view.findViewById(R.id.chipStocarstvo);
        chipVocnjak = view.findViewById(R.id.chipVocnjak);
        chipMasine = view.findViewById(R.id.chipMasine);

        recyclerPhotos.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL, false));

        photoAdapter = new PhotoAdapter(photoList, this::openGallery);
        recyclerPhotos.setAdapter(photoAdapter);

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

        etDescription.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int c, int a) {}
            public void onTextChanged(CharSequence s, int start, int b, int c) {
                txtCounter.setText(s.length() + "/500");
            }
            public void afterTextChanged(Editable s) {}
        });

        loadUser();

        btnObjavi.setOnClickListener(v -> publishPost());
    }

    private void publishPost() {

        String desc = etDescription.getText().toString().trim();

        if (desc.isEmpty()) {
            Toast.makeText(requireContext(), "Unesi opis", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        db.collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(doc -> {

                    String name = doc.getString("name");
                    if (name == null) name = "Korisnik";

                    Long avatarLong = doc.getLong("avatar");
                    int avatar = avatarLong != null ? avatarLong.intValue() : R.drawable.user;

                    String imagePath = "";

                    if (!photoList.isEmpty()) {
                        imagePath = saveImageToInternalStorage(photoList.get(0));
                    }

                    PostModel post = new PostModel();
                    post.setName(name);
                    post.setTime("Upravo sada");
                    post.setDescription("📌 " + selectedCategory + "\n\n" + desc);
                    post.setAvatar(avatar);
                    post.setImageUri(imagePath);
                    post.setLikes(0);
                    post.setComments(0);

                    db.collection("posts")
                            .add(post)
                            .addOnSuccessListener(r -> {
                                Toast.makeText(requireContext(), "Objava objavljena", Toast.LENGTH_SHORT).show();

                                requireActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragmentContainer, new FeedFragment())
                                        .commit();
                            });
                });
    }

    private String saveImageToInternalStorage(Uri uri) {
        try {
            InputStream input = requireContext().getContentResolver().openInputStream(uri);

            File file = new File(requireContext().getFilesDir(),
                    "img_" + System.currentTimeMillis() + ".jpg");

            OutputStream out = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len;

            while ((len = input.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            out.close();
            input.close();

            return file.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void loadUser() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        db.collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(doc -> {

                    // NAME
                    String name = doc.getString("name");
                    txtName.setText(name != null ? name : "Korisnik");

                    // AVATAR FIX 🔥
                    try {

                        Object avatarObj = doc.get("avatar");

                        if (avatarObj instanceof Long) {

                            int resId = ((Long) avatarObj).intValue();
                            imgUser.setImageResource(resId);

                        } else if (avatarObj instanceof Integer) {

                            imgUser.setImageResource((Integer) avatarObj);

                        } else {

                            imgUser.setImageResource(R.drawable.user);
                        }

                    } catch (Exception e) {

                        imgUser.setImageResource(R.drawable.user);
                    }
                });
    }

    private void openGallery() {
        pickImageLauncher.launch("image/*");
    }

    private void updateCategoryUI() {
        reset();
        if (selectedCategory.equals("Stočarstvo")) activate(chipStocarstvo);
        else if (selectedCategory.equals("Voćnjak")) activate(chipVocnjak);
        else activate(chipMasine);
    }

    private void reset() {
        deactivate(chipStocarstvo);
        deactivate(chipVocnjak);
        deactivate(chipMasine);
    }

    private void activate(TextView v) {
        v.setBackgroundResource(R.drawable.bg_green_button);
        v.setTextColor(Color.WHITE);
    }

    private void deactivate(TextView v) {
        v.setBackgroundResource(R.drawable.bg_search_modern);
        v.setTextColor(Color.parseColor("#111111"));
    }
}