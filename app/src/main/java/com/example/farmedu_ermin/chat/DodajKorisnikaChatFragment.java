package com.example.farmedu_ermin.chat;

import android.os.Bundle;
import android.os.Handler;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.farmedu_ermin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DodajKorisnikaChatFragment extends Fragment {

    private LinearLayout usersContainer;
    private EditText etSearchUser;
    private ImageView btnBack;

    private FirebaseFirestore db;

    private final Set<String> sentRequests =
            new HashSet<>();

    private final Set<String> partners =
            new HashSet<>();

    private final Handler handler =
            new Handler();

    public DodajKorisnikaChatFragment() {
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_dodaj_korisnika_chat,
                container,
                false
        );

        usersContainer =
                view.findViewById(R.id.usersContainer);

        etSearchUser =
                view.findViewById(R.id.etSearchUser);

        btnBack =
                view.findViewById(R.id.btnBack);

        db = FirebaseFirestore.getInstance();

        // =====================================
        // BACK
        // =====================================

        btnBack.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()
        );

        // =====================================
        // SEARCH
        // =====================================

        etSearchUser.addTextChangedListener(
                new TextWatcher() {

                    Runnable searchRunnable;

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        handler.removeCallbacksAndMessages(null);

                        searchRunnable = () ->
                                loadUsers(
                                        s.toString()
                                                .trim()
                                                .toLowerCase()
                                );

                        handler.postDelayed(
                                searchRunnable,
                                300
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                });

        loadUsers("");

        return view;
    }

    // =========================================
    // LOAD USERS
    // =========================================

    private void loadUsers(String searchText) {

        FirebaseUser currentUser =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        if (currentUser == null) {

            Toast.makeText(
                    requireContext(),
                    "Niste prijavljeni",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        usersContainer.removeAllViews();

        // =====================================
        // LOAD SENT REQUESTS
        // =====================================

        db.collection("chat_requests")
                .whereEqualTo(
                        "senderUid",
                        currentUser.getUid()
                )
                .whereEqualTo(
                        "status",
                        "pending"
                )
                .get()

                .addOnSuccessListener(requests -> {

                    sentRequests.clear();

                    for (DocumentSnapshot req :
                            requests.getDocuments()) {

                        String receiverUid =
                                req.getString("receiverUid");

                        if (receiverUid != null) {

                            sentRequests.add(receiverUid);
                        }
                    }

                    // =====================================
                    // LOAD PARTNERS
                    // =====================================

                    db.collection("users")
                            .document(currentUser.getUid())
                            .collection("partners")
                            .get()

                            .addOnSuccessListener(partnerQuery -> {

                                partners.clear();

                                for (DocumentSnapshot partner :
                                        partnerQuery.getDocuments()) {

                                    partners.add(
                                            partner.getId()
                                    );
                                }

                                loadAllUsers(
                                        currentUser,
                                        searchText
                                );
                            })

                            .addOnFailureListener(e ->
                                    loadAllUsers(
                                            currentUser,
                                            searchText
                                    )
                            );
                })

                .addOnFailureListener(e ->

                        Toast.makeText(
                                requireContext(),
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()
                );
    }

    // =========================================
    // LOAD ALL USERS
    // =========================================

    private void loadAllUsers(
            FirebaseUser currentUser,
            String searchText
    ) {

        db.collection("users")
                .get()

                .addOnSuccessListener(query -> {

                    usersContainer.removeAllViews();

                    if (query.isEmpty()) {

                        TextView txt =
                                new TextView(requireContext());

                        txt.setText("Nema korisnika");

                        txt.setTextSize(18);

                        txt.setPadding(
                                40,
                                40,
                                40,
                                40
                        );

                        usersContainer.addView(txt);

                        return;
                    }

                    for (DocumentSnapshot doc :
                            query.getDocuments()) {

                        try {

                            String uid =
                                    doc.getId();

                            // =====================================
                            // SKIP CURRENT USER
                            // =====================================

                            if (uid.equals(
                                    currentUser.getUid()
                            )) {

                                continue;
                            }

                            // =====================================
                            // NAME
                            // =====================================

                            String name =
                                    doc.getString("name");

                            if (name == null
                                    || name.trim().isEmpty()) {

                                name =
                                        "FarmEdu korisnik";
                            }

                            // =====================================
                            // SEARCH FILTER
                            // =====================================

                            if (!searchText.isEmpty()
                                    &&
                                    !name.toLowerCase()
                                            .contains(searchText)) {

                                continue;
                            }

                            // =====================================
                            // CITY
                            // =====================================

                            String city =
                                    doc.getString("city");

                            if (city == null
                                    || city.trim().isEmpty()) {

                                city =
                                        "Bosna i Hercegovina";
                            }

                            // =====================================
                            // AVATAR
                            // =====================================

                            int avatar =
                                    R.drawable.user;

                            try {

                                Long avatarLong =
                                        doc.getLong("avatar");

                                if (avatarLong != null) {

                                    avatar =
                                            avatarLong.intValue();
                                }

                            } catch (Exception ignored) {
                            }

                            // =====================================
                            // ADD CARD
                            // =====================================

                            addUserCard(
                                    uid,
                                    name,
                                    city,
                                    avatar
                            );

                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                })

                .addOnFailureListener(e ->

                        Toast.makeText(
                                requireContext(),
                                "Greška users kolekcije",
                                Toast.LENGTH_LONG
                        ).show()
                );
    }

    // =========================================
    // ADD USER CARD
    // =========================================

    private void addUserCard(
            String uid,
            String name,
            String city,
            int avatar
    ) {

        View card =
                LayoutInflater.from(requireContext())
                        .inflate(
                                R.layout.item_user_chat,
                                usersContainer,
                                false
                        );

        ImageView imgUser =
                card.findViewById(R.id.imgUser);

        TextView txtName =
                card.findViewById(R.id.txtName);

        TextView txtUsername =
                card.findViewById(R.id.txtUsername);

        LinearLayout btnAdd =
                card.findViewById(R.id.btnAdd);

        TextView btnText =
                (TextView) btnAdd.getChildAt(0);

        // =====================================
        // SET DATA
        // =====================================

        txtName.setText(name);

        txtUsername.setText(
                "📍 " + city
        );

        try {

            imgUser.setImageResource(avatar);

        } catch (Exception e) {

            imgUser.setImageResource(
                    R.drawable.user
            );
        }

        // =====================================
        // BUTTON STATUS
        // =====================================

        updateButtonState(
                uid,
                btnText
        );

        FirebaseUser currentUser =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        // =====================================
        // BUTTON CLICK
        // =====================================

        btnAdd.setOnClickListener(v -> {

            if (currentUser == null)
                return;

            String stanje =
                    btnText.getText().toString();

            // =====================================
            // PARTNERS
            // =====================================

            if (stanje.equals("🤝 Partneri")) {

                Toast.makeText(
                        requireContext(),
                        "Već ste partneri",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // =====================================
            // CANCEL REQUEST
            // =====================================

            if (stanje.equals("Poslano")) {

                btnAdd.setEnabled(false);

                db.collection("chat_requests")
                        .whereEqualTo(
                                "senderUid",
                                currentUser.getUid()
                        )
                        .whereEqualTo(
                                "receiverUid",
                                uid
                        )
                        .whereEqualTo(
                                "status",
                                "pending"
                        )
                        .get()

                        .addOnSuccessListener(queryDocumentSnapshots -> {

                            for (DocumentSnapshot doc :
                                    queryDocumentSnapshots.getDocuments()) {

                                db.collection("chat_requests")
                                        .document(doc.getId())
                                        .delete();
                            }

                            sentRequests.remove(uid);

                            btnText.setText("Dodaj");

                            btnAdd.setEnabled(true);

                            Toast.makeText(
                                    requireContext(),
                                    "Zahtjev poništen",
                                    Toast.LENGTH_SHORT
                            ).show();
                        })

                        .addOnFailureListener(e -> {

                            btnAdd.setEnabled(true);

                            Toast.makeText(
                                    requireContext(),
                                    "Greška poništavanja",
                                    Toast.LENGTH_SHORT
                            ).show();
                        });

                return;
            }

            // =====================================
            // SEND REQUEST
            // =====================================

            btnAdd.setEnabled(false);

            Map<String, Object> request =
                    new HashMap<>();

            request.put(
                    "senderUid",
                    currentUser.getUid()
            );

            request.put(
                    "receiverUid",
                    uid
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

                    .addOnSuccessListener(documentReference -> {

                        sentRequests.add(uid);

                        btnText.setText("Poslano");

                        btnAdd.setEnabled(true);

                        Toast.makeText(
                                requireContext(),
                                "Zahtjev poslan ✅",
                                Toast.LENGTH_SHORT
                        ).show();
                    })

                    .addOnFailureListener(e -> {

                        btnAdd.setEnabled(true);

                        Toast.makeText(
                                requireContext(),
                                "Greška slanja",
                                Toast.LENGTH_SHORT
                        ).show();
                    });
        });

        // =====================================
        // OPEN PROFILE
        // =====================================

        card.setOnClickListener(v -> {

            Bundle bundle =
                    new Bundle();

            bundle.putString(
                    "uid",
                    uid
            );

            PocetniProfilUseraFragment fragment =
                    new PocetniProfilUseraFragment();

            fragment.setArguments(bundle);

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
                            fragment
                    )
                    .addToBackStack(null)
                    .commit();
        });

        // =====================================
        // ADD VIEW
        // =====================================

        usersContainer.addView(card);
    }

    // =========================================
    // UPDATE BUTTON STATE
    // =========================================

    private void updateButtonState(
            String uid,
            TextView btnText
    ) {

        if (partners.contains(uid)) {

            btnText.setText("🤝 Partneri");

        } else if (sentRequests.contains(uid)) {

            btnText.setText("Poslano");

        } else {

            btnText.setText("Dodaj");
        }
    }
}