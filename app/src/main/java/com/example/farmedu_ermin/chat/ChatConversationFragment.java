package com.example.farmedu_ermin.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChatConversationFragment extends Fragment {

    private BottomNavigationView bottomNav;

    private ImageView imgUser;

    private TextView txtName;
    private TextView txtStatus;

    private AppCompatEditText etMessage;

    private ImageView btnSend;
    private ImageView btnEmoji;
    private ImageView btnAttach;
    private ImageView btnCall;
    private ImageView btnMore;

    private RecyclerView recyclerMessages;

    private ArrayList<Message> messageList;

    private MessageAdapter adapter;

    private FirebaseFirestore db;

    private String currentUid;
    private String receiverUid;

    public ChatConversationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        return inflater.inflate(
                R.layout.fragment_chat_conversation,
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

        // =====================================
        // INIT
        // =====================================

        imgUser =
                view.findViewById(R.id.imgUser);

        txtName =
                view.findViewById(R.id.txtName);

        txtStatus =
                view.findViewById(R.id.txtStatus);

        etMessage =
                view.findViewById(R.id.etMessage);

        btnSend =
                view.findViewById(R.id.btnSend);

        btnEmoji =
                view.findViewById(R.id.btnEmoji);

        btnAttach =
                view.findViewById(R.id.btnAttach);

        btnCall =
                view.findViewById(R.id.btnCall);

        btnMore =
                view.findViewById(R.id.btnMore);

        recyclerMessages =
                view.findViewById(R.id.recyclerMessages);

        // =====================================
        // FIREBASE
        // =====================================

        db =
                FirebaseFirestore.getInstance();

        currentUid =
                FirebaseAuth.getInstance()
                        .getUid();

        if (currentUid == null) {

            Toast.makeText(
                    requireContext(),
                    "Niste prijavljeni",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        // =====================================
        // RECYCLER
        // =====================================

        messageList =
                new ArrayList<>();

        adapter =
                new MessageAdapter(messageList);

        LinearLayoutManager manager =
                new LinearLayoutManager(
                        requireContext()
                );

        manager.setStackFromEnd(true);

        recyclerMessages.setLayoutManager(manager);

        recyclerMessages.setAdapter(adapter);

        // =====================================
        // GET ARGUMENTS
        // =====================================

        Bundle args = getArguments();

        if (args != null) {

            String name =
                    args.getString(
                            "name",
                            "Korisnik"
                    );

            receiverUid =
                    args.getString("uid");

            int avatar =
                    args.getInt(
                            "avatar",
                            R.drawable.user
                    );

            txtName.setText(name);

            try {

                imgUser.setImageResource(avatar);

            } catch (Exception e) {

                imgUser.setImageResource(
                        R.drawable.user
                );
            }
        }

        // =====================================
        // CHECK RECEIVER
        // =====================================

        if (receiverUid == null
                || receiverUid.isEmpty()) {

            Toast.makeText(
                    requireContext(),
                    "Greška: korisnik nije pronađen",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        // =====================================
        // LOAD USER STATUS
        // =====================================

        loadUserStatus();

        // =====================================
        // EMOJI BUTTON
        // =====================================

        btnEmoji.setOnClickListener(v -> {

            try {

                if (etMessage == null) {
                    return;
                }

                String currentText = "";

                if (etMessage.getText() != null) {

                    currentText =
                            etMessage.getText()
                                    .toString();
                }

                currentText =
                        currentText + " 😊";

                etMessage.setText(currentText);

                etMessage.setSelection(
                        currentText.length()
                );

            } catch (Exception e) {

                Log.e(
                        "EMOJI_ERROR",
                        e.getMessage()
                );

                Toast.makeText(
                        requireContext(),
                        "Emoji greška",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // =====================================
        // ATTACH BUTTON
        // =====================================

        btnAttach.setOnClickListener(v -> {

            Toast.makeText(
                    requireContext(),
                    "📎 Dodavanje slike uskoro",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // =====================================
        // CALL BUTTON
        // =====================================

        btnCall.setOnClickListener(v -> {

            Toast.makeText(
                    requireContext(),
                    "📞 Pozivanje korisnika...",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // =====================================
        // SETTINGS BUTTON
        // =====================================

        btnMore.setOnClickListener(v -> {

            Toast.makeText(
                    requireContext(),
                    "⚙️ Opcije razgovora",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // =====================================
        // SEND MESSAGE
        // =====================================

        btnSend.setOnClickListener(v -> {

            try {

                String text = "";

                if (etMessage.getText() != null) {

                    text =
                            etMessage.getText()
                                    .toString()
                                    .trim();
                }

                if (TextUtils.isEmpty(text)) {
                    return;
                }

                btnSend.setEnabled(false);

                Message message =
                        new Message(
                                currentUid,
                                receiverUid,
                                text,
                                System.currentTimeMillis()
                        );

                db.collection("chats")
                        .add(message)

                        .addOnSuccessListener(unused -> {

                            etMessage.setText("");

                            btnSend.setEnabled(true);

                            recyclerMessages.post(() -> {

                                recyclerMessages.smoothScrollToPosition(
                                        Math.max(
                                                adapter.getItemCount() - 1,
                                                0
                                        )
                                );
                            });
                        })

                        .addOnFailureListener(e -> {

                            btnSend.setEnabled(true);

                            Toast.makeText(
                                    requireContext(),
                                    "Greška: " + e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();

                            Log.e(
                                    "FIREBASE_SEND",
                                    e.getMessage()
                            );
                        });

            } catch (Exception e) {

                btnSend.setEnabled(true);

                Log.e(
                        "SEND_ERROR",
                        e.getMessage()
                );
            }
        });

        // =====================================
        // LOAD MESSAGES
        // =====================================

        db.collection("chats")
                .orderBy("timestamp")
                .addSnapshotListener((value, error) -> {

                    if (error != null) {

                        Log.e(
                                "CHAT_LOAD",
                                error.getMessage()
                        );

                        return;
                    }

                    if (value == null) {
                        return;
                    }

                    messageList.clear();

                    for (DocumentSnapshot doc
                            : value.getDocuments()) {

                        try {

                            Message message =
                                    doc.toObject(
                                            Message.class
                                    );

                            if (message == null) {
                                continue;
                            }

                            if (message.getSenderId() == null
                                    || message.getReceiverId() == null) {

                                continue;
                            }

                            boolean sentByMe =

                                    message.getSenderId()
                                            .equals(currentUid)

                                            &&

                                            message.getReceiverId()
                                                    .equals(receiverUid);

                            boolean receivedByMe =

                                    message.getSenderId()
                                            .equals(receiverUid)

                                            &&

                                            message.getReceiverId()
                                                    .equals(currentUid);

                            if (sentByMe || receivedByMe) {

                                messageList.add(message);
                            }

                        } catch (Exception e) {

                            Log.e(
                                    "MESSAGE_PARSE",
                                    e.getMessage()
                            );
                        }
                    }

                    adapter.notifyDataSetChanged();

                    recyclerMessages.post(() -> {

                        if (adapter.getItemCount() > 0) {

                            recyclerMessages.smoothScrollToPosition(
                                    adapter.getItemCount() - 1
                            );
                        }
                    });
                });

        // =====================================
        // HIDE BOTTOM NAV
        // =====================================

        bottomNav =
                requireActivity()
                        .findViewById(R.id.bottomNav);

        if (bottomNav != null) {

            bottomNav.setVisibility(View.GONE);
        }

        // =====================================
        // BACK BUTTON
        // =====================================

        View btnBack =
                view.findViewById(R.id.btnBack);

        if (btnBack != null) {

            btnBack.setOnClickListener(v ->

                    requireActivity()
                            .getSupportFragmentManager()
                            .popBackStack()
            );
        }
    }

    // =====================================
    // LOAD USER STATUS
    // =====================================

    private void loadUserStatus() {

        if (receiverUid == null) {
            return;
        }

        db.collection("users")
                .document(receiverUid)
                .addSnapshotListener((value, error) -> {

                    if (error != null
                            || value == null
                            || !value.exists()) {

                        return;
                    }

                    Boolean online =
                            value.getBoolean("online");

                    Long lastSeen =
                            value.getLong("lastSeen");

                    if (online != null && online) {

                        txtStatus.setText(
                                "🟢 Aktivan sada"
                        );

                        txtStatus.setTextColor(
                                Color.parseColor("#43A047")
                        );

                    } else {

                        String timeText =
                                "⚪ Nedavno aktivan";

                        if (lastSeen != null) {

                            long diff =
                                    System.currentTimeMillis()
                                            - lastSeen;

                            long minutes =
                                    diff / (1000 * 60);

                            if (minutes < 1) {

                                timeText =
                                        "⚪ Aktivan upravo";

                            } else if (minutes < 60) {

                                timeText =
                                        "⚪ Aktivan prije "
                                                + minutes
                                                + " min";

                            } else if (minutes < 24 * 60) {

                                long hours =
                                        minutes / 60;

                                timeText =
                                        "⚪ Aktivan prije "
                                                + hours
                                                + " h";

                            } else {

                                long days =
                                        minutes / (60 * 24);

                                timeText =
                                        "⚪ Aktivan prije "
                                                + days
                                                + " dana";
                            }
                        }

                        txtStatus.setText(timeText);

                        txtStatus.setTextColor(
                                Color.parseColor("#9E9E9E")
                        );
                    }
                });
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        if (bottomNav != null) {

            bottomNav.setVisibility(
                    View.VISIBLE
            );
        }
    }
}