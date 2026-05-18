package com.example.farmedu_ermin.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.farmedu_ermin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ZahtjevPrijateljstvoFragment extends Fragment {

    private LinearLayout containerRequests;
    private TextView txtEmpty;

    private FirebaseFirestore db;

    private ListenerRegistration requestListener;
    private ListenerRegistration notificationListener;

    private final Set<String> loadedRequestIds =
            new HashSet<>();

    public ZahtjevPrijateljstvoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_zahtjev_prijateljstvo,
                container,
                false
        );

        db = FirebaseFirestore.getInstance();

        containerRequests =
                view.findViewById(R.id.containerRequests);

        txtEmpty =
                view.findViewById(R.id.txtEmpty);

        View btnBack =
                view.findViewById(R.id.btnBack);

        if (btnBack != null) {

            btnBack.setOnClickListener(v -> {

                if (getParentFragmentManager()
                        .getBackStackEntryCount() > 0) {

                    getParentFragmentManager()
                            .popBackStack();
                }
            });
        }

        listenForRequests();

        listenForAcceptedNotifications();

        return view;
    }

    // ====================================================
    // LOAD AVATAR
    // ====================================================

    private void loadAvatar(
            ImageView imageView,
            Object avatarValue
    ) {

        try {

            if (avatarValue instanceof String) {

                String avatarName =
                        (String) avatarValue;

                int avatarRes =
                        getResources().getIdentifier(
                                avatarName,
                                "drawable",
                                requireContext().getPackageName()
                        );

                if (avatarRes != 0) {

                    imageView.setImageResource(
                            avatarRes
                    );

                    return;
                }
            }

            if (avatarValue instanceof Long) {

                imageView.setImageResource(
                        ((Long) avatarValue).intValue()
                );

                return;
            }

            if (avatarValue instanceof Integer) {

                imageView.setImageResource(
                        (Integer) avatarValue
                );

                return;
            }

            imageView.setImageResource(
                    R.drawable.user
            );

        } catch (Exception e) {

            e.printStackTrace();

            imageView.setImageResource(
                    R.drawable.user
            );
        }
    }

    // ====================================================
    // LISTEN REQUESTS
    // ====================================================

    private void listenForRequests() {

        FirebaseUser currentUser =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        if (currentUser == null)
            return;

        String currentUid =
                currentUser.getUid();

        requestListener =
                db.collection("chat_requests")

                        .whereEqualTo(
                                "receiverUid",
                                currentUid
                        )

                        .whereEqualTo(
                                "status",
                                "pending"
                        )

                        .addSnapshotListener((value, error) -> {

                            if (!isAdded()
                                    || getContext() == null) {

                                return;
                            }

                            if (error != null) {

                                Log.e(
                                        "FIREBASE",
                                        "REQUEST LISTENER ERROR",
                                        error
                                );

                                return;
                            }

                            containerRequests.removeAllViews();

                            loadedRequestIds.clear();

                            boolean hasRequests =
                                    value != null
                                            && !value.isEmpty();

                            if (hasRequests) {

                                for (DocumentSnapshot doc :
                                        value.getDocuments()) {

                                    String requestId =
                                            doc.getId();

                                    String senderUid =
                                            doc.getString(
                                                    "senderUid"
                                            );

                                    if (senderUid == null
                                            || senderUid.isEmpty()) {

                                        continue;
                                    }

                                    loadUserCard(
                                            senderUid,
                                            requestId,
                                            currentUid
                                    );
                                }
                            }

                            loadAcceptedNotificationsOnce(
                                    currentUid
                            );
                        });
    }

    // ====================================================
    // LISTEN NOTIFICATIONS
    // ====================================================

    private void listenForAcceptedNotifications() {

        FirebaseUser currentUser =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        if (currentUser == null)
            return;

        String currentUid =
                currentUser.getUid();

        notificationListener =
                db.collection("notifications")

                        .whereEqualTo(
                                "receiverUid",
                                currentUid
                        )

                        .whereEqualTo(
                                "type",
                                "partner_accept"
                        )

                        .addSnapshotListener((value, error) -> {

                            if (!isAdded()
                                    || getContext() == null) {

                                return;
                            }

                            if (error != null) {

                                Log.e(
                                        "FIREBASE",
                                        "NOTIFICATION ERROR",
                                        error
                                );

                                return;
                            }

                            loadAcceptedNotificationsOnce(
                                    currentUid
                            );
                        });
    }

    // ====================================================
    // LOAD NOTIFICATIONS
    // ====================================================

    private void loadAcceptedNotificationsOnce(
            String currentUid
    ) {

        db.collection("notifications")

                .whereEqualTo(
                        "receiverUid",
                        currentUid
                )

                .whereEqualTo(
                        "type",
                        "partner_accept"
                )

                .get()

                .addOnSuccessListener(value -> {

                    if (!isAdded()
                            || getContext() == null) {

                        return;
                    }

                    boolean hasAnything =
                            containerRequests.getChildCount() > 0;

                    for (DocumentSnapshot doc :
                            value.getDocuments()) {

                        String docId =
                                doc.getId();

                        View existing =
                                containerRequests.findViewWithTag(
                                        docId
                                );

                        if (existing != null) {
                            continue;
                        }

                        String partnerName =
                                doc.getString(
                                        "partnerName"
                                );

                        String partnerUid =
                                doc.getString(
                                        "partnerUid"
                                );

                        if (partnerName == null
                                || partnerName.isEmpty()) {

                            partnerName =
                                    "FarmEdu korisnik";
                        }

                        View card =
                                LayoutInflater.from(
                                                getContext()
                                        )

                                        .inflate(
                                                R.layout.item_notification_card,
                                                containerRequests,
                                                false
                                        );

                        card.setTag(docId);

                        ImageView imgAvatar =
                                card.findViewById(
                                        R.id.imgAvatar
                                );

                        TextView txtMessage =
                                card.findViewById(
                                        R.id.txtMessage
                                );

                        View dotUnread =
                                card.findViewById(
                                        R.id.dotUnread
                                );

                        txtMessage.setText(
                                partnerName +
                                        " je prihvatio/la vaš zahtjev za partnerstvo."
                        );

                        // ====================================================
                        // LOAD AKTUELNI AVATAR IZ USERS
                        // ====================================================

                        if (partnerUid != null) {

                            db.collection("users")
                                    .document(partnerUid)
                                    .get()

                                    .addOnSuccessListener(userDoc -> {

                                        if (!userDoc.exists())
                                            return;

                                        Object newAvatar =
                                                userDoc.get("avatar");

                                        loadAvatar(
                                                imgAvatar,
                                                newAvatar
                                        );
                                    });

                        } else {

                            imgAvatar.setImageResource(
                                    R.drawable.user
                            );
                        }

                        if (dotUnread != null) {

                            dotUnread.setVisibility(
                                    View.VISIBLE
                            );
                        }

                        containerRequests.addView(card);

                        hasAnything = true;
                    }

                    if (txtEmpty != null) {

                        if (hasAnything) {

                            txtEmpty.setVisibility(
                                    View.GONE
                            );

                        } else {

                            txtEmpty.setVisibility(
                                    View.VISIBLE
                            );
                        }
                    }
                });
    }

    // ====================================================
    // USER CARD
    // ====================================================

    private void loadUserCard(
            String senderUid,
            String requestId,
            String currentUid
    ) {

        if (loadedRequestIds.contains(requestId)) {
            return;
        }

        loadedRequestIds.add(requestId);

        db.collection("users")
                .document(senderUid)
                .get()

                .addOnSuccessListener(doc -> {

                    if (!isAdded()
                            || getContext() == null) {

                        return;
                    }

                    if (!doc.exists())
                        return;

                    View card =
                            LayoutInflater.from(
                                            getContext()
                                    )

                                    .inflate(
                                            R.layout.item_request_card,
                                            containerRequests,
                                            false
                                    );

                    ImageView imgUser =
                            card.findViewById(
                                    R.id.imgUser
                            );

                    TextView txtName =
                            card.findViewById(
                                    R.id.txtName
                            );

                    TextView txtCity =
                            card.findViewById(
                                    R.id.txtCity
                            );

                    TextView txtStatus =
                            card.findViewById(
                                    R.id.txtStatus
                            );

                    LinearLayout btnAccept =
                            card.findViewById(
                                    R.id.btnAccept
                            );

                    LinearLayout btnReject =
                            card.findViewById(
                                    R.id.btnReject
                            );

                    String name =
                            doc.getString("name");

                    String city =
                            doc.getString("city");

                    Object avatarValue =
                            doc.get("avatar");

                    if (name == null
                            || name.isEmpty()) {

                        name =
                                "FarmEdu korisnik";
                    }

                    if (city == null
                            || city.isEmpty()) {

                        city = "BiH";
                    }

                    txtName.setText(name);

                    txtCity.setText(
                            "📍 " + city
                    );

                    txtStatus.setText(
                            name +
                                    " vam je poslao/la zahtjev za partnerstvo."
                    );

                    loadAvatar(
                            imgUser,
                            avatarValue
                    );

                    btnAccept.setOnClickListener(v -> {

                        btnAccept.setEnabled(false);

                        btnReject.setEnabled(false);

                        acceptRequest(
                                requestId,
                                senderUid,
                                currentUid,
                                card
                        );
                    });

                    btnReject.setOnClickListener(v -> {

                        btnAccept.setEnabled(false);

                        btnReject.setEnabled(false);

                        rejectRequest(
                                requestId,
                                card
                        );
                    });

                    containerRequests.addView(card);

                    txtEmpty.setVisibility(
                            View.GONE
                    );
                });
    }

    // ====================================================
    // ACCEPT REQUEST
    // ====================================================

    private void acceptRequest(
            String requestId,
            String senderUid,
            String currentUid,
            View card
    ) {

        db.collection("chat_requests")
                .document(requestId)

                .update(
                        "status",
                        "accepted"
                )

                .addOnSuccessListener(unused -> {

                    containerRequests.removeView(card);

                    loadedRequestIds.remove(requestId);

                    createPartnership(
                            currentUid,
                            senderUid
                    );

                    db.collection("users")
                            .document(currentUid)
                            .get()

                            .addOnSuccessListener(userDoc -> {

                                String accepterName =
                                        userDoc.getString(
                                                "name"
                                        );

                                if (accepterName == null
                                        || accepterName.isEmpty()) {

                                    accepterName =
                                            "FarmEdu korisnik";
                                }

                                Map<String, Object> notification =
                                        new HashMap<>();

                                notification.put(
                                        "receiverUid",
                                        senderUid
                                );

                                notification.put(
                                        "type",
                                        "partner_accept"
                                );

                                notification.put(
                                        "message",
                                        accepterName +
                                                " je prihvatio/la vaš zahtjev."
                                );

                                notification.put(
                                        "partnerName",
                                        accepterName
                                );

                                notification.put(
                                        "partnerUid",
                                        currentUid
                                );

                                notification.put(
                                        "seen",
                                        false
                                );

                                notification.put(
                                        "timestamp",
                                        System.currentTimeMillis()
                                );

                                db.collection("notifications")
                                        .add(notification);

                                Toast.makeText(
                                        requireContext(),
                                        "✅ Zahtjev prihvaćen!",
                                        Toast.LENGTH_SHORT
                                ).show();
                            });

                    if (containerRequests.getChildCount() == 0) {

                        txtEmpty.setVisibility(
                                View.VISIBLE
                        );
                    }
                });
    }

    // ====================================================
    // CREATE PARTNERSHIP
    // ====================================================

    private void createPartnership(
            String uid1,
            String uid2
    ) {

        Map<String, Object> p1 =
                new HashMap<>();

        p1.put("partnerUid", uid2);

        p1.put(
                "timestamp",
                System.currentTimeMillis()
        );

        db.collection("users")
                .document(uid1)
                .collection("partners")
                .document(uid2)
                .set(p1);

        Map<String, Object> p2 =
                new HashMap<>();

        p2.put("partnerUid", uid1);

        p2.put(
                "timestamp",
                System.currentTimeMillis()
        );

        db.collection("users")
                .document(uid2)
                .collection("partners")
                .document(uid1)
                .set(p2);
    }

    // ====================================================
    // REJECT REQUEST
    // ====================================================

    private void rejectRequest(
            String requestId,
            View card
    ) {

        db.collection("chat_requests")
                .document(requestId)

                .update(
                        "status",
                        "rejected"
                )

                .addOnSuccessListener(unused -> {

                    containerRequests.removeView(card);

                    loadedRequestIds.remove(requestId);

                    Toast.makeText(
                            requireContext(),
                            "Zahtjev odbijen.",
                            Toast.LENGTH_SHORT
                    ).show();

                    if (containerRequests.getChildCount() == 0) {

                        txtEmpty.setVisibility(
                                View.VISIBLE
                        );
                    }
                });
    }

    // ====================================================
    // DESTROY
    // ====================================================

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        if (requestListener != null) {

            requestListener.remove();

            requestListener = null;
        }

        if (notificationListener != null) {

            notificationListener.remove();

            notificationListener = null;
        }
    }
}