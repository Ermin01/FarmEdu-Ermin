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

    private ImageView btnBack;
    private ImageView btnSettings;

    private ImageView imgProfile;

    private LinearLayout btnEditProfile;
    private LinearLayout btnLogout;

    private FlexboxLayout interestsLayout;

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

        imgProfile.setImageResource(
                R.drawable.user
        );

        // ====================================================
        // LOAD DATA
        // ====================================================

        loadProfile();

        loadFriendsCount();

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
    // UPDATE UI
    // ====================================================

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
                && !name.isEmpty()) {

            tvName.setText(name);

        } else {

            tvName.setText("Korisnik");
        }

        // ====================================================
        // CITY
        // ====================================================

        if (city != null
                && !city.isEmpty()) {

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
                && !about.isEmpty()) {

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

            imgProfile.setImageResource(
                    avatar.intValue()
            );

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