package com.example.farmedu_ermin.adapters;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.CreateNoteFragment;
import com.example.farmedu_ermin.R;
import com.example.farmedu_ermin.models.NoteModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NoteAdapter
        extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private final ArrayList<NoteModel> noteList;

    public NoteAdapter(ArrayList<NoteModel> noteList) {

        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_note,
                        parent,
                        false
                );

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull NoteViewHolder holder,
            int position) {

        NoteModel note = noteList.get(position);

        // TITLE

        holder.txtTitle.setText(
                note.getTitle()
        );

        // CONTENT

        holder.txtContent.setText(
                note.getContent()
        );

        // DATE

        String date = new SimpleDateFormat(
                "dd MMM yyyy",
                Locale.getDefault()
        ).format(
                new Date(note.getTimestamp())
        );

        holder.txtDate.setText(date);

        // PINNED UI

        if (note.isPinned()) {

            holder.txtPinned.setVisibility(View.VISIBLE);

        } else {

            holder.txtPinned.setVisibility(View.GONE);
        }

        // NOTE COLOR

        try {

            String color = note.getNoteColor();

            if(color == null || color.isEmpty()) {

                color = "#F7F4EE";
            }

            holder.cardNote.setCardBackgroundColor(
                    Color.parseColor(color)
            );

        } catch (Exception e) {

            holder.cardNote.setCardBackgroundColor(
                    Color.parseColor("#F7F4EE")
            );
        }

        // LOCKED BORDER

        if(note.isLocked()) {

            holder.cardNote.setCardElevation(10f);

        } else {

            holder.cardNote.setCardElevation(2f);
        }

        // OPEN NOTE

        holder.cardNote.setOnClickListener(v -> {

            // LOCK CHECK

            if(note.isLocked()) {

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(
                                holder.itemView.getContext()
                        );

                builder.setTitle("Unesi PIN");

                final EditText input =
                        new EditText(
                                holder.itemView.getContext()
                        );

                input.setHint("PIN");

                input.setInputType(
                        InputType.TYPE_CLASS_NUMBER |
                                InputType.TYPE_NUMBER_VARIATION_PASSWORD
                );

                builder.setView(input);

                builder.setPositiveButton(
                        "Otvori",
                        (dialog, which) -> {

                            String enteredPin =
                                    input.getText()
                                            .toString()
                                            .trim();

                            if(enteredPin.equals(
                                    note.getPinCode()
                            )) {

                                openNote(
                                        holder,
                                        note,
                                        position
                                );

                            } else {

                                Toast.makeText(
                                        holder.itemView.getContext(),
                                        "Pogrešan PIN",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        });

                builder.setNegativeButton(
                        "Cancel",
                        null
                );

                builder.show();

                return;
            }

            openNote(holder, note, position);
        });

        // MENU

        holder.btnMore.setOnClickListener(v -> {

            PopupMenu popupMenu =
                    new PopupMenu(
                            holder.itemView.getContext(),
                            holder.btnMore
                    );

            popupMenu.getMenu().add("Edit");
            popupMenu.getMenu().add("Pin");
            popupMenu.getMenu().add("Delete");
            popupMenu.getMenu().add("Share");
            popupMenu.getMenu().add("Lock");

            popupMenu.setOnMenuItemClickListener(item -> {

                String action =
                        item.getTitle().toString();

                // EDIT

                if(action.equals("Edit")) {

                    openNote(holder, note, position);
                }

                // PIN

                else if(action.equals("Pin")) {

                    boolean newPinState =
                            !note.isPinned();

                    note.setPinned(newPinState);

                    updatePinnedStatus(
                            note,
                            newPinState
                    );

                    notifyItemChanged(position);

                    Toast.makeText(
                            holder.itemView.getContext(),
                            "Pin promijenjen",
                            Toast.LENGTH_SHORT
                    ).show();
                }

                // DELETE

                else if(action.equals("Delete")) {

                    String uid =
                            FirebaseAuth.getInstance()
                                    .getCurrentUser()
                                    .getUid();

                    FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(uid)
                            .collection("notes")
                            .document(note.getNoteId())
                            .delete()

                            .addOnSuccessListener(unused -> {

                                noteList.remove(position);

                                notifyItemRemoved(position);

                                notifyItemRangeChanged(
                                        position,
                                        noteList.size()
                                );

                                Toast.makeText(
                                        holder.itemView.getContext(),
                                        "Bilješka obrisana",
                                        Toast.LENGTH_SHORT
                                ).show();
                            })

                            .addOnFailureListener(e -> {

                                Toast.makeText(
                                        holder.itemView.getContext(),
                                        "Greška pri brisanju",
                                        Toast.LENGTH_SHORT
                                ).show();
                            });
                }

                // SHARE

                else if(action.equals("Share")) {

                    Intent shareIntent =
                            new Intent(
                                    Intent.ACTION_SEND
                            );

                    shareIntent.setType("text/plain");

                    shareIntent.putExtra(
                            Intent.EXTRA_SUBJECT,
                            note.getTitle()
                    );

                    shareIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            note.getTitle()
                                    + "\n\n"
                                    + note.getContent()
                    );

                    holder.itemView.getContext()
                            .startActivity(
                                    Intent.createChooser(
                                            shareIntent,
                                            "Share Note"
                                    )
                            );
                }

                // LOCK

                else if(action.equals("Lock")) {

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(
                                    holder.itemView.getContext()
                            );

                    builder.setTitle("Zaključaj Note");

                    final EditText input =
                            new EditText(
                                    holder.itemView.getContext()
                            );

                    input.setHint("Unesi PIN");

                    input.setInputType(
                            InputType.TYPE_CLASS_NUMBER |
                                    InputType.TYPE_NUMBER_VARIATION_PASSWORD
                    );

                    builder.setView(input);

                    builder.setPositiveButton(
                            "Zaključaj",
                            (dialog, which) -> {

                                String pin =
                                        input.getText()
                                                .toString()
                                                .trim();

                                if(!pin.isEmpty()) {

                                    note.setLocked(true);

                                    note.setPinCode(pin);

                                    updateLockStatus(
                                            note,
                                            pin
                                    );

                                    notifyItemChanged(position);

                                    Toast.makeText(
                                            holder.itemView.getContext(),
                                            "Note zaključan",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            });

                    builder.setNegativeButton(
                            "Cancel",
                            null
                    );

                    builder.show();
                }

                return true;
            });

            popupMenu.show();
        });
    }

    // OPEN NOTE

    private void openNote(
            NoteViewHolder holder,
            NoteModel note,
            int position) {

        Bundle bundle = new Bundle();

        bundle.putString(
                "noteId",
                note.getNoteId()
        );

        bundle.putString(
                "title",
                note.getTitle()
        );

        bundle.putString(
                "content",
                note.getContent()
        );

        bundle.putBoolean(
                "pinned",
                note.isPinned()
        );

        bundle.putBoolean(
                "locked",
                note.isLocked()
        );

        bundle.putString(
                "pinCode",
                note.getPinCode()
        );

        bundle.putString(
                "noteColor",
                note.getNoteColor()
        );

        CreateNoteFragment fragment =
                new CreateNoteFragment();

        fragment.setArguments(bundle);

        FragmentActivity activity =
                (FragmentActivity)
                        holder.itemView.getContext();

        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragmentContainer,
                        fragment
                )
                .addToBackStack(null)
                .commit();
    }

    // UPDATE PINNED

    private void updatePinnedStatus(
            NoteModel note,
            boolean pinned) {

        String uid =
                FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("notes")
                .document(note.getNoteId())
                .update("pinned", pinned);
    }

    // UPDATE LOCK

    private void updateLockStatus(
            NoteModel note,
            String pin) {

        String uid =
                FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("notes")
                .document(note.getNoteId())
                .update(
                        "locked", true,
                        "pinCode", pin
                );
    }

    @Override
    public int getItemCount() {

        return noteList.size();
    }

    public static class NoteViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtContent;
        TextView txtDate;
        TextView txtPinned;

        ImageView btnMore;

        CardView cardNote;

        public NoteViewHolder(
                @NonNull View itemView) {

            super(itemView);

            txtTitle =
                    itemView.findViewById(R.id.txtTitle);

            txtContent =
                    itemView.findViewById(R.id.txtContent);

            txtDate =
                    itemView.findViewById(R.id.txtDate);

            txtPinned =
                    itemView.findViewById(R.id.txtPinned);

            btnMore =
                    itemView.findViewById(R.id.btnMore);

            cardNote =
                    itemView.findViewById(R.id.cardNote);
        }
    }
}