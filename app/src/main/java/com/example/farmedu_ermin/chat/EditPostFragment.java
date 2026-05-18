package com.example.farmedu_ermin.chat;

import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.farmedu_ermin.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.*;
import java.util.*;

public class EditPostFragment extends Fragment {

    private EditText etDescription;
    private ImageView imgPreview;
    private LinearLayout btnSave, btnDelete, btnChangePhoto;

    private FirebaseFirestore db;

    private String postId;
    private String oldImagePath;
    private Uri newImageUri;

    private ActivityResultLauncher<String> picker;

    @Override
    public View onCreateView(@NonNull LayoutInflater i, ViewGroup c, Bundle b) {
        return i.inflate(R.layout.fragment_edit_post, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle b) {

        db = FirebaseFirestore.getInstance();

        etDescription = v.findViewById(R.id.etDescription);
        imgPreview = v.findViewById(R.id.imgPreview);
        btnSave = v.findViewById(R.id.btnSave);
        btnDelete = v.findViewById(R.id.btnDelete);
        btnChangePhoto = v.findViewById(R.id.btnChangePhoto);

        picker = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri == null) return;

                    newImageUri = uri;

                    // 🔥 FORCE refresh
                    Glide.with(requireContext())
                            .clear(imgPreview);

                    Glide.with(requireContext())
                            .load(uri)
                            .centerCrop()
                            .into(imgPreview);
                });

        if (getArguments() != null) {

            postId = getArguments().getString("postId");
            oldImagePath = getArguments().getString("imageUri");

            etDescription.setText(getArguments().getString("description"));

            loadImage(oldImagePath);
        }

        btnChangePhoto.setOnClickListener(v1 -> picker.launch("image/*"));

        btnSave.setOnClickListener(v1 -> {

            String desc = etDescription.getText().toString().trim();
            if (desc.isEmpty()) return;

            String finalPath = oldImagePath;

            if (newImageUri != null) {
                finalPath = saveImageToInternalStorage(newImageUri);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("description", desc);
            map.put("imageUri", finalPath);

            db.collection("posts")
                    .document(postId)
                    .update(map)
                    .addOnSuccessListener(r -> {

                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();

                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentContainer, new FeedFragment())
                                .commit();
                    });
        });

        btnDelete.setOnClickListener(v1 -> {

            db.collection("posts")
                    .document(postId)
                    .delete()
                    .addOnSuccessListener(r -> requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new FeedFragment())
                            .commit());
        });
    }

    private void loadImage(String path) {

        if (path == null || path.isEmpty()) {
            imgPreview.setImageResource(R.drawable.user);
            return;
        }

        try {
            File file = new File(path);

            if (file.exists()) {
                Glide.with(requireContext())
                        .load(file)
                        .centerCrop()
                        .into(imgPreview);
            } else {
                imgPreview.setImageResource(R.drawable.user);
            }

        } catch (Exception e) {
            imgPreview.setImageResource(R.drawable.user);
        }
    }

    private String saveImageToInternalStorage(Uri uri) {

        try {
            InputStream in = requireContext().getContentResolver().openInputStream(uri);

            File file = new File(requireContext().getFilesDir(),
                    "edit_" + System.currentTimeMillis() + ".jpg");

            OutputStream out = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len;

            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            out.close();
            in.close();

            return file.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return oldImagePath;
        }
    }
}