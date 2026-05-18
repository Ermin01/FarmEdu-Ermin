package com.example.farmedu_ermin.chat;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.farmedu_ermin.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PocetniProfilUseraFragment extends Fragment {

    private ImageView imgUser;
    private ImageView imgBanner;
    private ImageView btnBack;
    private ImageView btnMore;

    private TextView txtName;
    private TextView txtProfession;
    private TextView txtAbout;

    private TextView txtChats;
    private TextView txtFriends;
    private TextView txtPosts;

    private LinearLayout btnSendRequest;
    private TextView btnRequestText;

    private FlexboxLayout interestsLayout;
    private LinearLayout layoutNoInterests;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private String profileUid = "";

    private boolean isRequestLoading = false;

    public PocetniProfilUseraFragment() {
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_pocetni_profil_usera,
                container,
                false
        );

        // =========================
        // INIT
        // =========================

        imgUser = view.findViewById(R.id.imgUser);

        imgBanner = view.findViewById(R.id.imgBanner);

        btnBack = view.findViewById(R.id.btnBack);

        btnMore = view.findViewById(R.id.btnMore);

        txtName = view.findViewById(R.id.txtName);

        txtProfession = view.findViewById(R.id.txtProfession);

        txtAbout = view.findViewById(R.id.txtAbout);

        txtChats = view.findViewById(R.id.txtChats);

        txtFriends = view.findViewById(R.id.txtFriends);

        txtPosts = view.findViewById(R.id.txtPosts);

        btnSendRequest = view.findViewById(R.id.btnSendRequest);

        btnRequestText = view.findViewById(R.id.txtRequestButton);

        interestsLayout =
                view.findViewById(R.id.interestsLayout);

        layoutNoInterests =
                view.findViewById(R.id.layoutNoInterests);

        db = FirebaseFirestore.getInstance();

        currentUser =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        // =========================
        // BACK BUTTON
        // =========================

        btnBack.setOnClickListener(v -> {

            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });

        // =========================
        // MORE OPTIONS
        // =========================

        btnMore.setOnClickListener(v -> {

            showProfileOptionsSheet();
        });

        // =========================
        // BUTTON CLICK
        // =========================

        btnSendRequest.setOnClickListener(v -> {

            if (isRequestLoading) {
                return;
            }

            String currentText =
                    btnRequestText.getText()
                            .toString();

            if (currentText.equals("🤝 Partneri")) {

                Toast.makeText(
                        requireContext(),
                        "Već ste partneri",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (currentText.equals("Zahtjev poslan")) {

                cancelRequest();

            } else {

                sendRequest();
            }
        });

        // =========================
        // LOAD PROFILE
        // =========================

        loadUserProfile();

        return view;
    }

    // =========================================
    // LOAD PROFILE
    // =========================================

    private void loadUserProfile() {

        if (getArguments() == null) {

            Toast.makeText(
                    requireContext(),
                    "Greška profila",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        profileUid =
                getArguments()
                        .getString("uid");

        if (profileUid == null ||
                profileUid.isEmpty()) {

            Toast.makeText(
                    requireContext(),
                    "UID ne postoji",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        db.collection("users")
                .document(profileUid)
                .get()

                .addOnSuccessListener(doc -> {

                    if (!doc.exists()) {

                        Toast.makeText(
                                requireContext(),
                                "Korisnik ne postoji",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    loadBasicInfo(doc);

                    loadInterests(doc);

                    loadStats();

                    checkPartnershipStatus();

                })

                .addOnFailureListener(e -> {

                    Toast.makeText(
                            requireContext(),
                            "Greška učitavanja",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }

    // =========================================
    // LOAD BASIC INFO
    // =========================================

    // =========================================
// LOAD BASIC INFO
// FIXED VERSION
// =========================================

    private void loadBasicInfo(DocumentSnapshot doc) {

        String name = doc.getString("name");

        if (name == null || name.trim().isEmpty()) {

            name = "FarmEdu korisnik";
        }

        txtName.setText(name);

        // =========================================
        // CITY
        // =========================================

        String city = doc.getString("city");

        if (city == null || city.trim().isEmpty()) {

            txtProfession.setVisibility(View.GONE);

        } else {

            txtProfession.setVisibility(View.VISIBLE);

            txtProfession.setText("📍 " + city);
        }

        // =========================================
        // ABOUT
        // =========================================

        String about = doc.getString("about");

        if (about == null || about.trim().isEmpty()) {

            about = "Korisnik još nije dodao opis.";
        }

        txtAbout.setText(about);

        // =========================================
        // AVATAR FIX
        // =========================================

        try {

            Object avatarObj = doc.get("avatar");

            if (avatarObj != null) {

                int avatarRes;

                if (avatarObj instanceof Long) {

                    avatarRes =
                            ((Long) avatarObj).intValue();

                } else if (avatarObj instanceof Integer) {

                    avatarRes =
                            (Integer) avatarObj;

                } else {

                    avatarRes = R.drawable.user;
                }

                imgUser.setImageResource(avatarRes);

            } else {

                imgUser.setImageResource(
                        R.drawable.user
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            imgUser.setImageResource(
                    R.drawable.user
            );
        }

        // =========================================
        // COVER FIX
        // =========================================

        try {

            // OVDJE JE BIO PROBLEM:
            // U EditProfileFragment spremaš "cover"
            // a ovdje si čitao "banner"

            Object coverObj = doc.get("cover");

            if (coverObj != null) {

                int coverRes;

                if (coverObj instanceof Long) {

                    coverRes =
                            ((Long) coverObj).intValue();

                } else if (coverObj instanceof Integer) {

                    coverRes =
                            (Integer) coverObj;

                } else {

                    coverRes =
                            R.drawable.farm_baner;
                }

                imgBanner.setImageResource(
                        coverRes
                );

            } else {

                imgBanner.setImageResource(
                        R.drawable.farm_baner
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            imgBanner.setImageResource(
                    R.drawable.farm_baner
            );
        }
    }
    // =========================================
    // LOAD INTERESTS
    // =========================================

    private void loadInterests(DocumentSnapshot doc) {

        interestsLayout.removeAllViews();

        List<String> interests =
                (List<String>) doc.get("interests");

        if (interests == null ||
                interests.isEmpty()) {

            layoutNoInterests.setVisibility(View.VISIBLE);

            interestsLayout.setVisibility(View.GONE);

            return;
        }

        layoutNoInterests.setVisibility(View.GONE);

        interestsLayout.setVisibility(View.VISIBLE);

        for (String interest : interests) {

            addInterestTag(interest);
        }
    }

    // =========================================
    // ADD INTEREST TAG
    // =========================================

    private void addInterestTag(String text) {

        TextView tag =
                new TextView(requireContext());

        tag.setText(text);

        tag.setTextSize(14);

        tag.setTextColor(
                Color.parseColor("#1D7A3E")
        );

        tag.setPadding(40, 20, 40, 20);

        tag.setGravity(Gravity.CENTER);

        tag.setTypeface(
                null,
                Typeface.BOLD
        );

        GradientDrawable drawable =
                new GradientDrawable();

        drawable.setColor(
                Color.parseColor("#F1FAF3")
        );

        drawable.setCornerRadius(100);

        drawable.setStroke(
                2,
                Color.parseColor("#D8F0DD")
        );

        tag.setBackground(drawable);

        FlexboxLayout.LayoutParams params =
                new FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                0,
                24,
                24
        );

        tag.setLayoutParams(params);

        interestsLayout.addView(tag);
    }

    // =========================================
    // LOAD STATS
    // =========================================

    private void loadStats() {

        db.collection("users")
                .document(profileUid)
                .collection("partners")
                .get()

                .addOnSuccessListener(query -> {

                    if (!isAdded()) return;

                    txtFriends.setText(
                            String.valueOf(query.size())
                    );
                })

                .addOnFailureListener(e -> {

                    txtFriends.setText("0");
                });

        db.collection("posts")
                .whereEqualTo(
                        "uid",
                        profileUid
                )
                .get()

                .addOnSuccessListener(query -> {

                    if (!isAdded()) return;

                    txtPosts.setText(
                            String.valueOf(query.size())
                    );
                })

                .addOnFailureListener(e -> {

                    txtPosts.setText("0");
                });

        db.collection("chat_requests")
                .whereEqualTo(
                        "status",
                        "accepted"
                )
                .get()

                .addOnSuccessListener(query -> {

                    if (!isAdded()) return;

                    int count = 0;

                    for (DocumentSnapshot d :
                            query.getDocuments()) {

                        String sender =
                                d.getString("senderUid");

                        String receiver =
                                d.getString("receiverUid");

                        if (sender == null ||
                                receiver == null) {

                            continue;
                        }

                        if (sender.equals(profileUid)
                                || receiver.equals(profileUid)) {

                            count++;
                        }
                    }

                    txtChats.setText(
                            String.valueOf(count)
                    );
                })

                .addOnFailureListener(e -> {

                    txtChats.setText("0");
                });
    }

    // =========================================
    // PROFILE OPTIONS
    // =========================================

    private void showProfileOptionsSheet() {

        BottomSheetDialog dialog =
                new BottomSheetDialog(
                        requireContext()
                );

        View sheetView =
                getLayoutInflater().inflate(
                        R.layout.bottom_sheet_profile_options,
                        null
                );

        LinearLayout btnDeletePartner =
                sheetView.findViewById(
                        R.id.btnDeletePartner
                );

        LinearLayout btnReport =
                sheetView.findViewById(
                        R.id.btnReport
                );

        LinearLayout btnBlock =
                sheetView.findViewById(
                        R.id.btnBlock
                );

        LinearLayout btnClose =
                sheetView.findViewById(
                        R.id.btnClose
                );

        btnDeletePartner.setOnClickListener(v -> {

            dialog.dismiss();

            showDeletePartnerDialog();
        });

        btnReport.setOnClickListener(v -> {

            dialog.dismiss();

            Toast.makeText(
                    requireContext(),
                    "Korisnik prijavljen ⚠️",
                    Toast.LENGTH_SHORT
            ).show();
        });

        btnBlock.setOnClickListener(v -> {

            dialog.dismiss();

            Toast.makeText(
                    requireContext(),
                    "Korisnik blokiran 🚫",
                    Toast.LENGTH_SHORT
            ).show();
        });

        btnClose.setOnClickListener(v -> {

            dialog.dismiss();
        });

        dialog.setContentView(sheetView);

        dialog.show();
    }

    // =========================================
    // DELETE DIALOG
    // =========================================

    private void showDeletePartnerDialog() {

        new AlertDialog.Builder(requireContext())

                .setTitle("Obriši partnera")

                .setMessage(
                        "Da li sigurno želiš obrisati partnera?"
                )

                .setPositiveButton(
                        "Obriši",
                        (dialog, which) -> {

                            Toast.makeText(
                                    requireContext(),
                                    "Partner obrisan",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                )

                .setNegativeButton(
                        "Otkaži",
                        null
                )

                .show();
    }

    // =========================================
    // CHECK PARTNERSHIP
    // =========================================

    private void checkPartnershipStatus() {

        if (currentUser == null) return;

        db.collection("users")
                .document(currentUser.getUid())
                .collection("partners")
                .document(profileUid)
                .get()

                .addOnSuccessListener(doc -> {

                    if (!isAdded()) return;

                    if (doc.exists()) {

                        btnSendRequest.setEnabled(false);

                        btnSendRequest.setAlpha(0.9f);

                        btnSendRequest.setBackgroundResource(
                                R.drawable.accept_button_bg
                        );

                        btnRequestText.setText(
                                "🤝 Partneri"
                        );

                    } else {

                        checkExistingRequest();
                    }
                });
    }

    // =========================================
    // CHECK REQUEST
    // =========================================

    private void checkExistingRequest() {

        if (currentUser == null) return;

        db.collection("chat_requests")
                .whereEqualTo(
                        "senderUid",
                        currentUser.getUid()
                )
                .whereEqualTo(
                        "receiverUid",
                        profileUid
                )
                .whereEqualTo(
                        "status",
                        "pending"
                )
                .get()

                .addOnSuccessListener(query -> {

                    if (!isAdded()) return;

                    if (!query.isEmpty()) {

                        btnSendRequest.setBackgroundResource(
                                R.drawable.accept_button_bg
                        );

                        btnRequestText.setText(
                                "Zahtjev poslan"
                        );

                    } else {

                        btnSendRequest.setBackgroundResource(
                                R.drawable.bg_green_button
                        );

                        btnRequestText.setText(
                                "Pošalji zahtjev za chat"
                        );
                    }
                });
    }

    // =========================================
    // SEND REQUEST
    // =========================================

    private void sendRequest() {

        if (currentUser == null) return;

        isRequestLoading = true;

        btnSendRequest.setEnabled(false);

        Map<String, Object> request =
                new HashMap<>();

        request.put(
                "senderUid",
                currentUser.getUid()
        );

        request.put(
                "receiverUid",
                profileUid
        );

        request.put(
                "status",
                "pending"
        );

        request.put(
                "timestamp",
                System.currentTimeMillis()
        );

        db.collection("chat_requests")
                .add(request)

                .addOnSuccessListener(unused -> {

                    if (!isAdded()) return;

                    isRequestLoading = false;

                    btnSendRequest.setEnabled(true);

                    btnSendRequest.setBackgroundResource(
                            R.drawable.accept_button_bg
                    );

                    btnRequestText.setText(
                            "Zahtjev poslan"
                    );

                    Toast.makeText(
                            requireContext(),
                            "Zahtjev poslan ✅",
                            Toast.LENGTH_SHORT
                    ).show();
                })

                .addOnFailureListener(e -> {

                    isRequestLoading = false;

                    btnSendRequest.setEnabled(true);

                    Toast.makeText(
                            requireContext(),
                            "Greška slanja zahtjeva",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }

    // =========================================
    // CANCEL REQUEST
    // =========================================

    private void cancelRequest() {

        if (currentUser == null) return;

        isRequestLoading = true;

        btnSendRequest.setEnabled(false);

        db.collection("chat_requests")
                .whereEqualTo(
                        "senderUid",
                        currentUser.getUid()
                )
                .whereEqualTo(
                        "receiverUid",
                        profileUid
                )
                .whereEqualTo(
                        "status",
                        "pending"
                )
                .get()

                .addOnSuccessListener(query -> {

                    for (DocumentSnapshot doc :
                            query.getDocuments()) {

                        db.collection("chat_requests")
                                .document(doc.getId())
                                .delete();
                    }

                    if (!isAdded()) return;

                    isRequestLoading = false;

                    btnSendRequest.setEnabled(true);

                    btnSendRequest.setBackgroundResource(
                            R.drawable.bg_green_button
                    );

                    btnRequestText.setText(
                            "Pošalji zahtjev za chat"
                    );

                    Toast.makeText(
                            requireContext(),
                            "Zahtjev poništen",
                            Toast.LENGTH_SHORT
                    ).show();
                })

                .addOnFailureListener(e -> {

                    isRequestLoading = false;

                    btnSendRequest.setEnabled(true);

                    Toast.makeText(
                            requireContext(),
                            "Greška poništavanja",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }
}