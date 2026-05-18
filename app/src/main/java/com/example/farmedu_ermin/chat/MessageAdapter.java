package com.example.farmedu_ermin.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Message> list;

    private final int TYPE_SENT = 1;

    private final int TYPE_RECEIVED = 2;

    public MessageAdapter(ArrayList<Message> list) {

        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {

        try {

            Message message =
                    list.get(position);

            if (message == null) {
                return TYPE_RECEIVED;
            }

            String currentUid =
                    FirebaseAuth.getInstance()
                            .getUid();

            if (currentUid == null) {
                return TYPE_RECEIVED;
            }

            String senderId =
                    message.getSenderId();

            if (senderId == null) {
                return TYPE_RECEIVED;
            }

            if (senderId.equals(currentUid)) {

                return TYPE_SENT;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        if (viewType == TYPE_SENT) {

            View view =
                    LayoutInflater.from(parent.getContext())
                            .inflate(
                                    R.layout.item_message_sent,
                                    parent,
                                    false
                            );

            return new SentHolder(view);
        }

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_message_received,
                                parent,
                                false
                        );

        return new ReceivedHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            int position
    ) {

        try {

            Message message =
                    list.get(position);

            if (message == null) {
                return;
            }

            String text =
                    message.getMessage();

            if (text == null) {
                text = "";
            }

            String time =
                    new SimpleDateFormat(
                            "HH:mm",
                            Locale.getDefault()
                    ).format(
                            new Date(
                                    message.getTimestamp()
                            )
                    );

            // =====================================
            // SENT
            // =====================================

            if (holder instanceof SentHolder) {

                SentHolder sentHolder =
                        (SentHolder) holder;

                sentHolder.txtMessage.setText(text);

                sentHolder.txtTopTime.setText(time);

                sentHolder.txtMessageTime.setText(time);
            }

            // =====================================
            // RECEIVED
            // =====================================

            else if (holder instanceof ReceivedHolder) {

                ReceivedHolder receivedHolder =
                        (ReceivedHolder) holder;

                receivedHolder.txtMessage.setText(text);

                receivedHolder.txtTopTime.setText(time);

                receivedHolder.txtMessageTime.setText(time);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        if (list == null) {
            return 0;
        }

        return list.size();
    }

    // =====================================
    // SENT HOLDER
    // =====================================

    static class SentHolder
            extends RecyclerView.ViewHolder {

        TextView txtMessage;

        TextView txtTopTime;

        TextView txtMessageTime;

        public SentHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtMessage =
                    itemView.findViewById(
                            R.id.txtMessage
                    );

            txtTopTime =
                    itemView.findViewById(
                            R.id.txtTopTime
                    );

            txtMessageTime =
                    itemView.findViewById(
                            R.id.txtMessageTime
                    );
        }
    }

    // =====================================
    // RECEIVED HOLDER
    // =====================================

    static class ReceivedHolder
            extends RecyclerView.ViewHolder {

        TextView txtMessage;

        TextView txtTopTime;

        TextView txtMessageTime;

        public ReceivedHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtMessage =
                    itemView.findViewById(
                            R.id.txtMessage
                    );

            txtTopTime =
                    itemView.findViewById(
                            R.id.txtTopTime
                    );

            txtMessageTime =
                    itemView.findViewById(
                            R.id.txtMessageTime
                    );
        }
    }
}