package com.example.farmedu_ermin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.List;

public class ProfileFragment extends Fragment {

    private EditText tvAbout;

    private TextView tvName;
    private TextView tvCity;

    private TextView txtFriendsCount;
    private TextView txtPostsCount;
    private TextView txtCommentsCount;

    private ImageView btnBack;
    private ImageView btnSettings;

    private ImageView imgProfile;

    private LinearLayout btnEditProfile;
    private LinearLayout btnLogout;

    private FlexboxLayout interestsLayout;
    private LinearLayout btnBrisiProfil;


    private FirebaseFirestore db;

    private String uid;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(
            View view,
            @Nullable Bundle savedInstanceState
    ) {

        super.onViewCreated(view, savedInstanceState);

        // ====================================================
        // INIT
        // ====================================================

        tvName =
                view.findViewById(R.id.tvName);

        tvCity =
                view.findViewById(R.id.tvCity);

        tvAbout =
                view.findViewById(R.id.tvAbout);

        txtFriendsCount =
                view.findViewById(R.id.txtFriendsCount);

        txtPostsCount =
                view.findViewById(R.id.txtPostsCount);

        txtCommentsCount =
                view.findViewById(R.id.txtCommentsCount);

        btnBack =
                view.findViewById(R.id.btnBack);

        btnSettings =
                view.findViewById(R.id.btnSettings);

        btnEditProfile =
                view.findViewById(R.id.btnEditProfile);

        btnLogout =
                view.findViewById(R.id.btnLogout);

        imgProfile =
                view.findViewById(R.id.imgProfile);

        interestsLayout =
                view.findViewById(R.id.interestsLayout);

        btnBrisiProfil =
                view.findViewById(R.id.btnBrisiprofil);

        db =
                FirebaseFirestore.getInstance();

        FirebaseUser user =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        // ====================================================
        // USER NULL
        // ====================================================

        if (user == null) {

            Intent intent =
                    new Intent(
                            requireActivity(),
                            MainActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            requireActivity().finish();

            return;
        }

        uid = user.getUid();

        // ====================================================
        // DEFAULT VALUES
        // ====================================================

        tvName.setText("Korisnik");

        tvCity.setText("Dodajte svoju lokaciju");

        tvAbout.setText("");

        tvAbout.setHint("Napišite nešto o sebi");

        txtFriendsCount.setText("0");

        txtPostsCount.setText("0");

        txtCommentsCount.setText("0");

        imgProfile.setImageResource(
                R.drawable.user
        );

        // ====================================================
        // LOAD DATA
        // ====================================================

        loadProfile();

        loadFriendsCount();

        loadPostsCount();

        loadCommentsCount();

        // ====================================================
        // EDIT PROFILE
        // ====================================================

        btnEditProfile.setOnClickListener(v ->

                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                        )
                        .replace(
                                R.id.fragmentContainer,
                                new EditProfileFragment()
                        )
                        .addToBackStack(null)
                        .commit()

        );

        // ====================================================
// DELETE PROFILE
// ====================================================

        btnBrisiProfil.setOnClickListener(v -> {

            new android.app.AlertDialog.Builder(requireContext())

                    .setTitle("Obriši profil")

                    .setMessage(
                            "Da li ste sigurni da želite obrisati profil? Ova radnja se ne može poništiti."
                    )

                    .setPositiveButton("Obriši",
                            (dialog, which) -> {

                                FirebaseUser currentUser =
                                        FirebaseAuth.getInstance()
                                                .getCurrentUser();

                                if (currentUser == null) {

                                    Toast.makeText(
                                            getContext(),
                                            "Greška",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    return;
                                }

                                String userId =
                                        currentUser.getUid();

                                // ====================================================
                                // DELETE FIRESTORE USER
                                // ====================================================

                                db.collection("users")
                                        .document(userId)
                                        .delete()

                                        .addOnSuccessListener(unused -> {

                                            // ====================================================
                                            // DELETE AUTH ACCOUNT
                                            // ====================================================

                                            currentUser.delete()

                                                    .addOnSuccessListener(unused1 -> {

                                                        Toast.makeText(
                                                                getContext(),
                                                                "Profil obrisan",
                                                                Toast.LENGTH_LONG
                                                        ).show();

                                                        Intent intent =
                                                                new Intent(
                                                                        requireActivity(),
                                                                        MainActivity.class
                                                                );

                                                        intent.setFlags(
                                                                Intent.FLAG_ACTIVITY_NEW_TASK
                                                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                        );

                                                        startActivity(intent);

                                                        requireActivity().finish();
                                                    })

                                                    .addOnFailureListener(e -> {

                                                        Toast.makeText(
                                                                getContext(),
                                                                "Greška pri brisanju naloga",
                                                                Toast.LENGTH_LONG
                                                        ).show();
                                                    });

                                        })

                                        .addOnFailureListener(e -> {

                                            Toast.makeText(
                                                    getContext(),
                                                    "Greška pri brisanju podataka",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        });

                            })

                    .setNegativeButton("Otkaži", null)

                    .show();

        });

        // ====================================================
        // LOGOUT
        // ====================================================

        btnLogout.setOnClickListener(v -> {

            FirebaseAuth.getInstance()
                    .signOut();

            Intent intent =
                    new Intent(
                            getActivity(),
                            MainActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            requireActivity().finish();
        });

        // ====================================================
        // BACK
        // ====================================================

        btnBack.setOnClickListener(v ->

                requireActivity()
                        .onBackPressed()

        );

        // ====================================================
        // SETTINGS
        // ====================================================

        btnSettings.setOnClickListener(v ->

                Toast.makeText(
                        getContext(),
                        "Postavke",
                        Toast.LENGTH_SHORT
                ).show()

        );
    }

    // ====================================================
    // LOAD PROFILE
    // ====================================================

    private void loadProfile() {

        db.collection("users")
                .document(uid)
                .get(Source.CACHE)

                .addOnSuccessListener(cacheDoc -> {

                    if (cacheDoc != null
                            && cacheDoc.exists()) {

                        updateUI(cacheDoc);
                    }

                    // ====================================================
                    // SERVER REFRESH
                    // ====================================================

                    db.collection("users")
                            .document(uid)
                            .get(Source.SERVER)

                            .addOnSuccessListener(serverDoc -> {

                                if (serverDoc != null
                                        && serverDoc.exists()) {

                                    updateUI(serverDoc);
                                }
                            });
                });
    }

    // ====================================================
    // LOAD FRIENDS COUNT
    // ====================================================

    private void loadFriendsCount() {

        db.collection("users")
                .document(uid)
                .collection("partners")

                .get()

                .addOnSuccessListener(queryDocumentSnapshots -> {

                    int count =
                            queryDocumentSnapshots.size();

                    txtFriendsCount.setText(
                            String.valueOf(count)
                    );
                })

                .addOnFailureListener(e -> {

                    txtFriendsCount.setText("0");
                });
    }

    // ====================================================
    // LOAD POSTS COUNT
    // ====================================================

    private void loadPostsCount() {

        db.collection("posts")
                .whereEqualTo("userId", uid)
                .get()

                .addOnSuccessListener(queryDocumentSnapshots -> {

                    int count =
                            queryDocumentSnapshots.size();

                    txtPostsCount.setText(
                            String.valueOf(count)
                    );
                })

                .addOnFailureListener(e -> {

                    txtPostsCount.setText("0");
                });
    }

    // ====================================================
    // LOAD COMMENTS COUNT
    // ====================================================

    private void loadCommentsCount() {

        db.collection("posts")
                .get()

                .addOnSuccessListener(posts -> {

                    if (posts.isEmpty()) {

                        txtCommentsCount.setText("0");

                        return;
                    }

                    final int[] totalComments = {0};

                    final int totalPosts =
                            posts.size();

                    final int[] processed = {0};

                    for (DocumentSnapshot post :
                            posts.getDocuments()) {

                        db.collection("posts")
                                .document(post.getId())
                                .collection("comments")
                                .whereEqualTo(
                                        "userId",
                                        uid
                                )
                                .get()

                                .addOnSuccessListener(comments -> {

                                    totalComments[0] +=
                                            comments.size();

                                    processed[0]++;

                                    if (processed[0]
                                            >= totalPosts) {

                                        txtCommentsCount.setText(
                                                String.valueOf(
                                                        totalComments[0]
                                                )
                                        );
                                    }
                                })

                                .addOnFailureListener(e -> {

                                    processed[0]++;

                                    if (processed[0]
                                            >= totalPosts) {

                                        txtCommentsCount.setText(
                                                String.valueOf(
                                                        totalComments[0]
                                                )
                                        );
                                    }
                                });
                    }
                })

                .addOnFailureListener(e -> {

                    txtCommentsCount.setText("0");
                });
    }

    // ====================================================
    // UPDATE UI
    // ====================================================

    @SuppressWarnings("unchecked")
    private void updateUI(DocumentSnapshot doc) {

        String name =
                doc.getString("name");

        String city =
                doc.getString("city");

        String about =
                doc.getString("about");

        Long avatar =
                doc.getLong("avatar");

        List<String> interests =
                (List<String>) doc.get("interests");

        // ====================================================
        // NAME
        // ====================================================

        if (name != null
                && !name.trim().isEmpty()) {

            tvName.setText(name);

        } else {

            tvName.setText("Korisnik");
        }

        // ====================================================
        // CITY
        // ====================================================

        if (city != null
                && !city.trim().isEmpty()) {

            tvCity.setText(city);

        } else {

            tvCity.setText(
                    "Dodajte svoju lokaciju"
            );
        }

        // ====================================================
        // ABOUT
        // ====================================================

        if (about != null
                && !about.trim().isEmpty()) {

            tvAbout.setText(about);

        } else {

            tvAbout.setText("");

            tvAbout.setHint(
                    "Napišite nešto o sebi"
            );
        }

        // ====================================================
        // AVATAR
        // ====================================================

        if (avatar != null) {

            try {

                imgProfile.setImageResource(
                        avatar.intValue()
                );

            } catch (Exception e) {

                imgProfile.setImageResource(
                        R.drawable.user
                );
            }

        } else {

            imgProfile.setImageResource(
                    R.drawable.user
            );
        }

        // ====================================================
        // INTERESTS
        // ====================================================

        interestsLayout.removeAllViews();

        if (interests != null
                && !interests.isEmpty()) {

            for (String interest : interests) {

                TextView chip =
                        new TextView(requireContext());

                chip.setText("🌱 " + interest);

                chip.setTextColor(
                        Color.parseColor("#D7FF3F")
                );

                chip.setTextSize(13);

                chip.setPadding(
                        30,
                        16,
                        30,
                        16
                );

                chip.setBackgroundResource(
                        R.drawable.bg_calendar_active_day
                );

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                params.setMargins(
                        0,
                        0,
                        18,
                        18
                );

                chip.setLayoutParams(params);

                interestsLayout.addView(chip);
            }
        }
    }
}