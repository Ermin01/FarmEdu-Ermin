package com.example.farmedu_ermin.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.farmedu_ermin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

public class NotifikacijeFragment extends Fragment {

    private LinearLayout containerNotif;
    private TextView txtEmpty;

    private FirebaseFirestore db;
    private ListenerRegistration notifListener;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_notifikacije,
                container,
                false
        );

        db = FirebaseFirestore.getInstance();

        containerNotif = view.findViewById(R.id.containerNotif);
        txtEmpty = view.findViewById(R.id.txtEmpty);

        View btnBack = view.findViewById(R.id.btnBack);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                    getParentFragmentManager().popBackStack();
                }
            });
        }

        listenNotifications();

        return view;
    }

    private void listenNotifications() {

        FirebaseUser user =
                FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) return;

        notifListener = db.collection("notifications")
                .whereEqualTo("receiverUid", user.getUid())
                .whereEqualTo("type", "partner_accept")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {

                    if (!isAdded() || getContext() == null) return;

                    if (error != null) {
                        Log.e("NOTIF", "ERROR", error);
                        return;
                    }

                    containerNotif.removeAllViews();

                    if (value == null || value.isEmpty()) {

                        txtEmpty.setVisibility(View.VISIBLE);

                        return;
                    }

                    txtEmpty.setVisibility(View.GONE);

                    for (DocumentSnapshot doc : value.getDocuments()) {

                        String msg = doc.getString("message");

                        Long avatar =
                                doc.getLong("avatar");

                        Boolean seen =
                                doc.getBoolean("seen");

                        View card =
                                LayoutInflater.from(getContext())
                                        .inflate(
                                                R.layout.item_notification_card,
                                                containerNotif,
                                                false
                                        );

                        ImageView img =
                                card.findViewById(R.id.imgAvatar);

                        TextView txt =
                                card.findViewById(R.id.txtMessage);

                        View dot =
                                card.findViewById(R.id.dotUnread);

                        txt.setText(msg);

                        try {

                            if (avatar != null) {

                                img.setImageResource(
                                        avatar.intValue()
                                );

                            } else {

                                img.setImageResource(R.drawable.user);
                            }

                        } catch (Exception e) {

                            img.setImageResource(R.drawable.user);
                        }

                        if (seen != null && seen) {

                            dot.setVisibility(View.GONE);

                        } else {

                            dot.setVisibility(View.VISIBLE);

                            doc.getReference()
                                    .update("seen", true);
                        }

                        containerNotif.addView(card);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (notifListener != null) {
            notifListener.remove();
            notifListener = null;
        }
    }
}