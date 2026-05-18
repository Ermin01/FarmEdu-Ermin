package com.example.farmedu_ermin.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private ImageView btnAdd;

    private RecyclerView recyclerChats;

    private ArrayList<ChatUser> chatList;

    private ChatAdapter adapter;

    private FirebaseFirestore db;

    public ChatFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_chat,
                container,
                false
        );

        btnAdd =
                view.findViewById(R.id.btnAdd);

        recyclerChats =
                view.findViewById(R.id.recyclerChats);

        db = FirebaseFirestore.getInstance();

        chatList = new ArrayList<>();

        adapter =
                new ChatAdapter(chatList);

        recyclerChats.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );

        recyclerChats.setAdapter(adapter);


        ImageView btnObjave =
                view.findViewById(R.id.btnObjave);

        btnObjave.setOnClickListener(v -> {

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
                            new FeedFragment()
                    )
                    .addToBackStack(null)
                    .commit();
        });

        btnAdd.setOnClickListener(v -> {

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
                            new DodajKorisnikaChatFragment()
                    )
                    .addToBackStack(null)
                    .commit();
        });

        loadPartners();

        return view;
    }



    // =====================================
    // LOAD PARTNERS
    // =====================================

    private void loadPartners() {

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

        String currentUid =
                currentUser.getUid();

        db.collection("users")
                .document(currentUid)
                .collection("partners")

                .get()

                .addOnSuccessListener(query -> {

                    if (!isAdded()
                            || getContext() == null) {

                        return;
                    }

                    chatList.clear();

                    adapter.notifyDataSetChanged();

                    if (query == null
                            || query.isEmpty()) {

                        Log.d(
                                "CHAT_FRAGMENT",
                                "NEMA PARTNERA"
                        );

                        return;
                    }

                    for (DocumentSnapshot document :
                            query.getDocuments()) {

                        // =====================================
                        // PARTNER UID
                        // =====================================

                        String partnerUid =
                                document.getString(
                                        "partnerUid"
                                );

                        // FALLBACK
                        if (partnerUid == null
                                || partnerUid.isEmpty()) {

                            partnerUid =
                                    document.getId();
                        }

                        if (partnerUid == null
                                || partnerUid.isEmpty()) {

                            continue;
                        }

                        loadUserData(
                                partnerUid
                        );
                    }
                })



                .addOnFailureListener(e -> {

                    if (!isAdded())
                        return;

                    Toast.makeText(
                            requireContext(),
                            "Greška pri učitavanju partnera",
                            Toast.LENGTH_LONG
                    ).show();

                    Log.e(
                            "CHAT_FRAGMENT",
                            e.getMessage()
                    );
                });
    }

    // =====================================
    // LOAD USER DATA
    // =====================================

    private void loadUserData(String uid) {

        db.collection("users")
                .document(uid)
                .get()

                .addOnSuccessListener(doc -> {

                    if (!isAdded()
                            || getContext() == null) {

                        return;
                    }

                    if (!doc.exists()) {
                        return;
                    }

                    // =====================================
                    // NAME
                    // =====================================

                    String name =
                            doc.getString("name");

                    if (name == null
                            || name.isEmpty()) {

                        name = "Korisnik";
                    }

                    // =====================================
                    // AVATAR
                    // =====================================

                    int avatarRes =
                            R.drawable.user;

                    try {

                        Object avatarValue =
                                doc.get("avatar");

                        // LONG AVATAR

                        if (avatarValue instanceof Long) {

                            avatarRes =
                                    ((Long) avatarValue)
                                            .intValue();
                        }

                        // STRING AVATAR

                        else if (avatarValue instanceof String) {

                            String avatarName =
                                    (String) avatarValue;

                            int drawableRes =
                                    getResources()
                                            .getIdentifier(
                                                    avatarName,
                                                    "drawable",
                                                    requireContext()
                                                            .getPackageName()
                                            );

                            if (drawableRes != 0) {

                                avatarRes =
                                        drawableRes;
                            }
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                        avatarRes =
                                R.drawable.user;
                    }

                    // =====================================
                    // ADD USER
                    // =====================================

                    chatList.add(
                            new ChatUser(
                                    uid,
                                    name,
                                    avatarRes
                            )
                    );

                    adapter.notifyItemInserted(
                            chatList.size() - 1
                    );
                })

                .addOnFailureListener(e ->

                        Log.e(
                                "CHAT_USER_LOAD",
                                e.getMessage()
                        )
                );
    }
}