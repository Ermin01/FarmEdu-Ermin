package com.example.farmedu_ermin.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter
        extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private ArrayList<ChatUser> list;

    public ChatAdapter(ArrayList<ChatUser> list) {

        this.list = list;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_chat,
                                parent,
                                false
                        );

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ChatViewHolder holder,
            int position
    ) {

        ChatUser user =
                list.get(position);

        if (user == null) {
            return;
        }

        // =====================================
        // NAME
        // =====================================

        holder.txtName.setText(
                user.getName()
        );

        // =====================================
        // AVATAR
        // =====================================

        try {

            holder.imgUser.setImageResource(
                    user.getAvatar()
            );

        } catch (Exception e) {

            holder.imgUser.setImageResource(
                    R.drawable.user
            );
        }

        // =====================================
        // DEFAULT VALUES
        // =====================================

        holder.txtLastMessage.setText(
                "Započni razgovor..."
        );

        holder.txtTime.setText("");

        holder.txtUnread.setVisibility(
                View.GONE
        );

        holder.imgSeen.setVisibility(
                View.GONE
        );

        // =====================================
        // LOAD LAST MESSAGE
        // =====================================

        FirebaseFirestore.getInstance()
                .collection("chats")
                .orderBy(
                        "timestamp",
                        Query.Direction.DESCENDING
                )
                .get()

                .addOnSuccessListener(query -> {

                    for (DocumentSnapshot doc
                            : query.getDocuments()) {

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

                            String currentUid =
                                    FirebaseAuth.getInstance()
                                            .getUid();

                            if (currentUid == null) {
                                return;
                            }

                            boolean sentByMe =

                                    message.getSenderId()
                                            .equals(currentUid)

                                            &&

                                            message.getReceiverId()
                                                    .equals(
                                                            user.getUid()
                                                    );

                            boolean receivedByMe =

                                    message.getSenderId()
                                            .equals(
                                                    user.getUid()
                                            )

                                            &&

                                            message.getReceiverId()
                                                    .equals(
                                                            currentUid
                                                    );

                            if (!sentByMe
                                    && !receivedByMe) {

                                continue;
                            }

                            // =====================================
                            // LAST MESSAGE
                            // =====================================

                            String lastMessage =
                                    message.getMessage();

                            if (lastMessage == null
                                    || lastMessage.isEmpty()) {

                                lastMessage =
                                        "Pošaljana poruka";
                            }

                            holder.txtLastMessage.setText(
                                    lastMessage
                            );

                            // =====================================
                            // TIME
                            // =====================================

                            try {

                                SimpleDateFormat sdf =
                                        new SimpleDateFormat(
                                                "HH:mm",
                                                Locale.getDefault()
                                        );

                                String time =
                                        sdf.format(
                                                new Date(
                                                        message.getTimestamp()
                                                )
                                        );

                                holder.txtTime.setText(
                                        time
                                );

                            } catch (Exception e) {

                                holder.txtTime.setText("");
                            }

                            // =====================================
                            // SEEN ICON
                            // =====================================

                            if (sentByMe) {

                                holder.imgSeen.setVisibility(
                                        View.VISIBLE
                                );

                            } else {

                                holder.imgSeen.setVisibility(
                                        View.GONE
                                );
                            }

                            // =====================================
                            // UNREAD BADGE
                            // =====================================

                            if (receivedByMe) {

                                holder.txtUnread.setVisibility(
                                        View.VISIBLE
                                );

                                holder.txtUnread.setText(
                                        "1"
                                );

                            } else {

                                holder.txtUnread.setVisibility(
                                        View.GONE
                                );
                            }

                            break;

                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                });

        // =====================================
        // OPEN CHAT
        // =====================================

        holder.itemView.setOnClickListener(v -> {

            try {

                ChatConversationFragment fragment =
                        new ChatConversationFragment();

                Bundle bundle =
                        new Bundle();

                bundle.putString(
                        "uid",
                        user.getUid()
                );

                bundle.putString(
                        "name",
                        user.getName()
                );

                bundle.putInt(
                        "avatar",
                        user.getAvatar()
                );

                fragment.setArguments(bundle);

                FragmentActivity activity =
                        (FragmentActivity)
                                v.getContext();

                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.fragmentContainer,
                                fragment
                        )
                        .addToBackStack(null)
                        .commit();

            } catch (Exception e) {

                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    // =====================================
    // VIEW HOLDER
    // =====================================

    static class ChatViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgUser;

        ImageView imgSeen;

        TextView txtName;

        TextView txtLastMessage;

        TextView txtTime;

        TextView txtUnread;

        public ChatViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            imgUser =
                    itemView.findViewById(
                            R.id.imgUser
                    );

            imgSeen =
                    itemView.findViewById(
                            R.id.imgSeen
                    );

            txtName =
                    itemView.findViewById(
                            R.id.txtName
                    );

            txtLastMessage =
                    itemView.findViewById(
                            R.id.txtLastMessage
                    );

            txtTime =
                    itemView.findViewById(
                            R.id.txtTime
                    );

            txtUnread =
                    itemView.findViewById(
                            R.id.txtUnread
                    );
        }
    }
}