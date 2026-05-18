package com.example.farmedu_ermin;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfileFragment extends Fragment {

    private EditText etName;
    private EditText etCity;
    private EditText etAbout;

    private LinearLayout btnSave;

    private ImageView btnCountryPicker;
    private ImageView imgProfile;
    private ImageView btnPickImage;

    // COVER
    private ImageView imgCover;
    private ImageView btnChangeCover;

    private FlexboxLayout interestsContainer;

    private FirebaseFirestore db;

    private String uid;

    private boolean isLoading = false;

    // =========================================
    // AVATAR
    // =========================================

    private int selectedAvatar = R.drawable.user;

    // =========================================
    // COVER
    // =========================================

    private int selectedCover = R.drawable.farm_baner;

    private final List<String> selectedInterests =
            new ArrayList<>();

    // =========================================
    // INTEREST LIST
    // =========================================

    private final List<String> allInterests =
            Arrays.asList(

                    "🌾 Poljoprivreda",
                    "🚜 Mehanizacija",
                    "🌱 Organska proizvodnja",
                    "🐄 Stočarstvo",
                    "🍏 Voćarstvo",
                    "🐝 Pčelarstvo",
                    "💧 Navodnjavanje",
                    "🥕 Povrtlarstvo",
                    "🍇 Vinogradarstvo",
                    "🏡 Plastenici",
                    "🚁 Dronovi",
                    "📡 Digitalna farma",
                    "🏕 Agroturizam",
                    "🐟 Ribarstvo",
                    "🌿 Sadnice",
                    "💰 EU Fondovi",
                    "♻️ Održiva proizvodnja",
                    "🐓 Peradarstvo",
                    "🐑 Ovčarstvo",
                    "🐐 Kozarstvo",
                    "🌽 Kukuruz",
                    "🌾 Pšenica",
                    "🧑‍🌾 Farmer",
                    "🚛 Transport robe",
                    "🥛 Mljekarstvo",
                    "🧀 Sirarstvo",
                    "🌻 Suncokret",
                    "🪴 Rasadnici",
                    "🌳 Šumarstvo",
                    "🌼 Cvjećarstvo"
            );

    public EditProfileFragment() {

        super(R.layout.fragment_edit_profile);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {

        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.etName);

        etCity = view.findViewById(R.id.etCity);

        etAbout = view.findViewById(R.id.etAbout);

        btnSave = view.findViewById(R.id.btnSave);

        btnCountryPicker =
                view.findViewById(R.id.btnCountryPicker);

        imgProfile =
                view.findViewById(R.id.imgProfile);

        btnPickImage =
                view.findViewById(R.id.btnPickImage);

        // COVER

        imgCover =
                view.findViewById(R.id.imgCover);

        btnChangeCover =
                view.findViewById(R.id.btnChangeCover);

        interestsContainer =
                view.findViewById(R.id.interestsContainer);

        db = FirebaseFirestore.getInstance();

        FirebaseUser user =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        if (user == null) {

            Toast.makeText(
                    getContext(),
                    "User nije prijavljen",
                    Toast.LENGTH_SHORT
            ).show();

            requireActivity().finish();

            return;
        }

        uid = user.getUid();

        // LOAD USER

        loadUser();

        // LOCATION PICKER

        btnCountryPicker.setOnClickListener(v ->
                showCountryPicker()
        );

        // SAVE

        btnSave.setOnClickListener(v ->
                saveProfile()
        );

        // AVATAR PICKER

        btnPickImage.setOnClickListener(v ->
                showAvatarPicker()
        );

        imgProfile.setOnClickListener(v ->
                showAvatarPicker()
        );

        // COVER PICKER

        btnChangeCover.setOnClickListener(v ->
                showCoverPicker()
        );

        imgCover.setOnClickListener(v ->
                showCoverPicker()
        );
    }

    // =========================================
    // REFRESH CHIPS
    // =========================================

    private void refreshInterestChips() {

        interestsContainer.removeAllViews();

        for (String interest : selectedInterests) {

            interestsContainer.addView(
                    createSelectedChip(interest)
            );
        }

        for (String interest : allInterests) {

            if (selectedInterests.contains(interest))
                continue;

            interestsContainer.addView(
                    createNormalChip(interest)
            );
        }
    }

    // =========================================
    // NORMAL CHIP
    // =========================================

    private TextView createNormalChip(
            String interest
    ) {

        TextView chip =
                new TextView(requireContext());

        chip.setText(interest);

        chip.setTextColor(Color.WHITE);

        chip.setTextSize(14);

        chip.setTypeface(
                null,
                Typeface.BOLD
        );

        chip.setPadding(
                40,
                24,
                40,
                24
        );

        chip.setBackgroundResource(
                R.drawable.bg_premium_search
        );

        FlexboxLayout.LayoutParams params =
                new FlexboxLayout.LayoutParams(
                        FlexboxLayout.LayoutParams.WRAP_CONTENT,
                        FlexboxLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                0,
                20,
                20
        );

        chip.setLayoutParams(params);

        chip.setOnClickListener(v -> {

            if (selectedInterests.size() >= 10) {

                Toast.makeText(
                        requireContext(),
                        "Možeš odabrati maksimalno 10 interesa",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            selectedInterests.add(interest);

            refreshInterestChips();
        });

        return chip;
    }

    // =========================================
    // SELECTED CHIP
    // =========================================

    private LinearLayout createSelectedChip(
            String interest
    ) {

        LinearLayout layout =
                new LinearLayout(requireContext());

        layout.setOrientation(
                LinearLayout.HORIZONTAL
        );

        layout.setGravity(Gravity.CENTER_VERTICAL);

        layout.setPadding(
                40,
                20,
                30,
                20
        );

        layout.setBackgroundResource(
                R.drawable.active_bg
        );

        FlexboxLayout.LayoutParams params =
                new FlexboxLayout.LayoutParams(
                        FlexboxLayout.LayoutParams.WRAP_CONTENT,
                        FlexboxLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                0,
                20,
                20
        );

        layout.setLayoutParams(params);

        TextView txt =
                new TextView(requireContext());

        txt.setText(interest);

        txt.setTextColor(
                Color.parseColor("#07141B")
        );

        txt.setTypeface(
                null,
                Typeface.BOLD
        );

        txt.setTextSize(14);

        TextView btnRemove =
                new TextView(requireContext());

        btnRemove.setText("  ✕");

        btnRemove.setTextColor(
                Color.parseColor("#07141B")
        );

        btnRemove.setTypeface(
                null,
                Typeface.BOLD
        );

        btnRemove.setTextSize(16);

        btnRemove.setOnClickListener(v -> {

            selectedInterests.remove(interest);

            refreshInterestChips();
        });

        layout.addView(txt);

        layout.addView(btnRemove);

        return layout;
    }

    // =========================================
    // LOAD USER
    // =========================================

    private void loadUser() {

        db.collection("users")
                .document(uid)
                .get()

                .addOnSuccessListener(doc -> {

                    if (!doc.exists())
                        return;

                    // NAME

                    String name =
                            doc.getString("name");

                    if (name != null) {

                        etName.setText(name);
                    }

                    // CITY

                    String city =
                            doc.getString("city");

                    if (city != null) {

                        etCity.setText(city);
                    }

                    // ABOUT

                    String about =
                            doc.getString("about");

                    if (about != null) {

                        etAbout.setText(about);
                    }

                    // AVATAR

                    Long avatar =
                            doc.getLong("avatar");

                    if (avatar != null) {

                        selectedAvatar =
                                avatar.intValue();

                        imgProfile.setImageResource(
                                selectedAvatar
                        );
                    }

                    // COVER

                    Long cover =
                            doc.getLong("cover");

                    if (cover != null) {

                        selectedCover =
                                cover.intValue();

                        imgCover.setImageResource(
                                selectedCover
                        );
                    }

                    // INTERESTS

                    List<String> savedInterests =
                            (List<String>) doc.get("interests");

                    if (savedInterests != null) {

                        selectedInterests.clear();

                        selectedInterests.addAll(
                                savedInterests
                        );
                    }

                    refreshInterestChips();
                });
    }

    // =========================================
    // LOCATION PICKER
    // =========================================

    private void showCountryPicker() {

        final EditText input =
                new EditText(requireContext());

        input.setHint(
                "Unesi grad, selo ili državu"
        );

        input.setPadding(
                40,
                30,
                40,
                30
        );

        new AlertDialog.Builder(requireContext())

                .setTitle("Lokacija korisnika")

                .setMessage(
                        "Unesi svoju lokaciju"
                )

                .setView(input)

                .setPositiveButton(
                        "Sačuvaj",
                        (dialog, which) -> {

                            String location =
                                    input.getText()
                                            .toString()
                                            .trim();

                            if (!location.isEmpty()) {

                                etCity.setText(location);

                            } else {

                                Toast.makeText(
                                        requireContext(),
                                        "Unesi lokaciju",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        })

                .setNegativeButton(
                        "Otkaži",
                        null
                )

                .show();
    }

    // =========================================
    // AVATAR PICKER
    // =========================================

    private void showAvatarPicker() {

        String[] avatarNames = {

                "Farmerka",
                "Veterinar",
                "Veterinar muškarac",
                "Agronom",
                "Agronom žena",
                "Tehničar",
                "Traktorista",
                "Dostavljač"
        };

        int[] avatarImages = {

                R.drawable.farmerkaavatar,
                R.drawable.veterinaravatar,
                R.drawable.veterinaravatarm,
                R.drawable.agronomfarmavatar,
                R.drawable.agronomfarmavatarzensko,
                R.drawable.tehnicaravatar,
                R.drawable.traktorista,
                R.drawable.dostavljac
        };

        AlertDialog.Builder builder =
                new AlertDialog.Builder(
                        requireContext()
                );

        builder.setTitle("Izaberi avatar");

        builder.setItems(
                avatarNames,
                (dialog, which) -> {

                    selectedAvatar =
                            avatarImages[which];

                    imgProfile.setImageResource(
                            selectedAvatar
                    );
                }
        );

        builder.show();
    }

    // =========================================
    // COVER PICKER
    // =========================================

    private void showCoverPicker() {

        String[] coverNames = {

                "🌾 Farma",
                "🌿 Livada",
                "🍎 Voćnjak",
                "🐄 Stočarstvo"
        };

        int[] coverImages = {

                R.drawable.farm_baner,
                R.drawable.farm_baner_livada,
                R.drawable.farm_baner_vocnjak,
                R.drawable.farm_baner_zivotnja
        };

        AlertDialog.Builder builder =
                new AlertDialog.Builder(
                        requireContext()
                );

        builder.setTitle("Izaberi naslovnu fotografiju");

        builder.setItems(
                coverNames,
                (dialog, which) -> {

                    selectedCover =
                            coverImages[which];

                    imgCover.setImageResource(
                            selectedCover
                    );
                }
        );

        builder.show();
    }

    // =========================================
    // SAVE PROFILE
    // =========================================

    private void saveProfile() {

        if (isLoading)
            return;

        String name =
                etName.getText()
                        .toString()
                        .trim();

        String city =
                etCity.getText()
                        .toString()
                        .trim();

        String about =
                etAbout.getText()
                        .toString()
                        .trim();

        if (name.isEmpty()) {

            etName.setError(
                    "Unesi ime"
            );

            return;
        }

        if (city.isEmpty()) {

            etCity.setError(
                    "Unesi lokaciju"
            );

            return;
        }

        isLoading = true;

        btnSave.setEnabled(false);

        Map<String, Object> data =
                new HashMap<>();

        data.put("name", name);

        data.put("city", city);

        data.put("about", about);

        data.put("avatar", selectedAvatar);

        // COVER SAVE

        data.put("cover", selectedCover);

        data.put(
                "interests",
                selectedInterests
        );

        db.collection("users")
                .document(uid)
                .set(
                        data,
                        SetOptions.merge()
                )

                .addOnSuccessListener(unused -> {

                    isLoading = false;

                    btnSave.setEnabled(true);

                    Toast.makeText(
                            getContext(),
                            "Profil sačuvan ✅",
                            Toast.LENGTH_SHORT
                    ).show();

                    requireActivity()
                            .getSupportFragmentManager()
                            .popBackStack();
                })

                .addOnFailureListener(e -> {

                    isLoading = false;

                    btnSave.setEnabled(true);

                    Toast.makeText(
                            getContext(),
                            "Greška: " + e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }
}