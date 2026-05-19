package com.example.farmedu_ermin.chat;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.farmedu_ermin.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditPostFragment extends Fragment {

    private EditText etDescription;

    private ImageView img1;
    private ImageView img2;
    private ImageView img3;

    private ImageView remove1;
    private ImageView remove2;
    private ImageView remove3;

    private LinearLayout btnSave;
    private LinearLayout btnDelete;

    private FirebaseFirestore db;

    private String postId;

    // =====================================================
    // STARE SLIKE
    // =====================================================

    private final List<String> imagePaths =
            new ArrayList<>();

    // =====================================================
    // NOVE SLIKE
    // =====================================================

    private Uri newUri1;
    private Uri newUri2;
    private Uri newUri3;

    // =====================================================
    // PICKER INDEX
    // =====================================================

    private int currentPickerIndex = 0;

    private ActivityResultLauncher<String> picker;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        return inflater.inflate(
                R.layout.fragment_edit_post,
                container,
                false
        );
    }

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

        etDescription =
                view.findViewById(R.id.etDescription);

        img1 =
                view.findViewById(R.id.img1);

        img2 =
                view.findViewById(R.id.img2);

        img3 =
                view.findViewById(R.id.img3);

        remove1 =
                view.findViewById(R.id.remove1);

        remove2 =
                view.findViewById(R.id.remove2);

        remove3 =
                view.findViewById(R.id.remove3);

        btnSave =
                view.findViewById(R.id.btnSave);

        btnDelete =
                view.findViewById(R.id.btnDelete);

        // =====================================================
        // IMAGE PICKER
        // =====================================================

        picker = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {

                    if (uri == null) return;

                    if (currentPickerIndex == 0) {

                        newUri1 = uri;

                    } else if (currentPickerIndex == 1) {

                        newUri2 = uri;

                    } else {

                        newUri3 = uri;
                    }

                    updateImagesUI();
                });

        // =====================================================
        // LOAD DATA
        // =====================================================

        if (getArguments() != null) {

            postId =
                    getArguments().getString("postId");

            etDescription.setText(
                    getArguments().getString(
                            "description",
                            ""
                    )
            );

            ArrayList<String> images =
                    getArguments().getStringArrayList(
                            "imageUrls"
                    );

            if (images != null) {

                imagePaths.addAll(images);
            }
        }

        // =====================================================
        // UI REFRESH
        // =====================================================

        updateImagesUI();

        // =====================================================
        // IMAGE CLICK
        // =====================================================

        img1.setOnClickListener(v -> {

            currentPickerIndex = 0;

            picker.launch("image/*");
        });

        img2.setOnClickListener(v -> {

            currentPickerIndex = 1;

            picker.launch("image/*");
        });

        img3.setOnClickListener(v -> {

            currentPickerIndex = 2;

            picker.launch("image/*");
        });

        // =====================================================
        // REMOVE IMAGE
        // =====================================================

        remove1.setOnClickListener(v ->
                removeImage(0)
        );

        remove2.setOnClickListener(v ->
                removeImage(1)
        );

        remove3.setOnClickListener(v ->
                removeImage(2)
        );

        // =====================================================
        // SAVE
        // =====================================================

        btnSave.setOnClickListener(v -> {

            String desc =
                    etDescription.getText()
                            .toString()
                            .trim();

            if (desc.isEmpty()) {

                Toast.makeText(
                        getContext(),
                        "Unesi opis objave",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            List<String> finalImages =
                    new ArrayList<>();

            // =====================================================
            // IMAGE 1
            // =====================================================

            if (newUri1 != null) {

                finalImages.add(
                        saveImageToInternalStorage(newUri1)
                );

            } else if (
                    imagePaths.size() > 0
                            && imagePaths.get(0) != null
                            && !imagePaths.get(0).isEmpty()
            ) {

                finalImages.add(
                        imagePaths.get(0)
                );
            }

            // =====================================================
            // IMAGE 2
            // =====================================================

            if (newUri2 != null) {

                finalImages.add(
                        saveImageToInternalStorage(newUri2)
                );

            } else if (
                    imagePaths.size() > 1
                            && imagePaths.get(1) != null
                            && !imagePaths.get(1).isEmpty()
            ) {

                finalImages.add(
                        imagePaths.get(1)
                );
            }

            // =====================================================
            // IMAGE 3
            // =====================================================

            if (newUri3 != null) {

                finalImages.add(
                        saveImageToInternalStorage(newUri3)
                );

            } else if (
                    imagePaths.size() > 2
                            && imagePaths.get(2) != null
                            && !imagePaths.get(2).isEmpty()
            ) {

                finalImages.add(
                        imagePaths.get(2)
                );
            }

            // =====================================================
            // UPDATE MAP
            // =====================================================

            Map<String, Object> map =
                    new HashMap<>();

            map.put("description", desc);

            map.put("imageUrls", finalImages);

            // OLD SUPPORT

            if (!finalImages.isEmpty()) {

                map.put(
                        "imageUri",
                        finalImages.get(0)
                );

            } else {

                map.put("imageUri", "");
            }

            // =====================================================
            // FIRESTORE UPDATE
            // =====================================================

            db.collection("posts")
                    .document(postId)
                    .update(map)
                    .addOnSuccessListener(unused -> {

                        Toast.makeText(
                                getContext(),
                                "Objava uređena",
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
                    });
        });

        // =====================================================
        // DELETE POST
        // =====================================================

        btnDelete.setOnClickListener(v -> {

            db.collection("posts")
                    .document(postId)
                    .delete()
                    .addOnSuccessListener(unused -> {

                        Toast.makeText(
                                getContext(),
                                "Objava obrisana",
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
                    });
        });
    }

    // =====================================================
    // UPDATE ALL UI
    // =====================================================

    private void updateImagesUI() {

        updateSingleImage(
                img1,
                remove1,
                0,
                newUri1
        );

        updateSingleImage(
                img2,
                remove2,
                1,
                newUri2
        );

        updateSingleImage(
                img3,
                remove3,
                2,
                newUri3
        );
    }

    // =====================================================
    // UPDATE SINGLE IMAGE
    // =====================================================

    private void updateSingleImage(
            ImageView imageView,
            ImageView removeBtn,
            int index,
            Uri newUri
    ) {

        // =====================================================
        // NEW URI
        // =====================================================

        if (newUri != null) {

            Glide.with(requireContext())
                    .load(newUri)
                    .centerCrop()
                    .into(imageView);

            removeBtn.setVisibility(View.VISIBLE);

            return;
        }

        // =====================================================
        // OLD IMAGE
        // =====================================================

        if (imagePaths.size() > index) {

            String path =
                    imagePaths.get(index);

            if (path != null && !path.isEmpty()) {

                File file =
                        new File(path);

                if (file.exists()) {

                    Glide.with(requireContext())
                            .load(file)
                            .centerCrop()
                            .into(imageView);

                    removeBtn.setVisibility(
                            View.VISIBLE
                    );

                    return;
                }
            }
        }

        // =====================================================
        // DEFAULT
        // =====================================================

        imageView.setImageResource(
                R.drawable.dodajfotku
        );

        removeBtn.setVisibility(View.GONE);
    }

    // =====================================================
    // REMOVE IMAGE
    // =====================================================

    private void removeImage(int index) {

        // CLEAR NEW URI

        if (index == 0) {

            newUri1 = null;

        } else if (index == 1) {

            newUri2 = null;

        } else {

            newUri3 = null;
        }

        // CLEAR OLD IMAGE

        if (imagePaths.size() > index) {

            imagePaths.set(index, "");
        }

        updateImagesUI();
    }

    // =====================================================
    // SAVE IMAGE INTERNAL STORAGE
    // =====================================================

    private String saveImageToInternalStorage(
            Uri uri
    ) {

        try {

            InputStream in =
                    requireContext()
                            .getContentResolver()
                            .openInputStream(uri);

            File file =
                    new File(
                            requireContext().getFilesDir(),
                            "edit_"
                                    + System.currentTimeMillis()
                                    + ".jpg"
                    );

            OutputStream out =
                    new FileOutputStream(file);

            byte[] buffer =
                    new byte[1024];

            int len;

            while ((len = in.read(buffer)) > 0) {

                out.write(buffer, 0, len);
            }

            out.close();

            in.close();

            return file.getAbsolutePath();

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }
    }
}