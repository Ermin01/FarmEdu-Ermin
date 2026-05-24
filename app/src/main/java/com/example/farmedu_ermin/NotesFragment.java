package com.example.farmedu_ermin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmedu_ermin.adapters.NoteAdapter;
import com.example.farmedu_ermin.chat.NotesStorage;
import com.example.farmedu_ermin.models.NoteModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotesFragment extends Fragment {

    private RecyclerView notesRecycler;

    private NoteAdapter adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_notes,
                container,
                false
        );

        notesRecycler =
                view.findViewById(R.id.notesRecycler);

        FloatingActionButton fabAdd =
                view.findViewById(R.id.fabAdd);

        adapter = new NoteAdapter(
                NotesStorage.notesList
        );

        notesRecycler.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        notesRecycler.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()

                    .replace(
                            R.id.fragmentContainer,
                            new CreateNoteFragment()
                    )

                    .addToBackStack(null)
                    .commit();
        });

        loadNotes();

        return view;
    }

    private void loadNotes() {

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            return;
        }

        String uid =
                FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("notes")
                .get()

                .addOnSuccessListener(queryDocumentSnapshots -> {

                    NotesStorage.notesList.clear();

                    for (var doc : queryDocumentSnapshots) {

                        NoteModel note =
                                doc.toObject(NoteModel.class);

                        if(note != null){

                            NotesStorage.notesList.add(note);
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onResume() {

        super.onResume();

        loadNotes();
    }
}