package com.example.farmedu_ermin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OcjeniAplikacijuFragment extends Fragment {

    private ImageView btnBack;
    private RatingBar ratingBar;
    private EditText etKomentar;
    private LinearLayout btnSubmit;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public OcjeniAplikacijuFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ocjeni_aplikaciju, container, false);

        btnBack = view.findViewById(R.id.btnBack);
        ratingBar = view.findViewById(R.id.ratingBar);
        etKomentar = view.findViewById(R.id.etKomentar);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // 🔙 BACK
        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        // ⭐ SUBMIT RATING
        btnSubmit.setOnClickListener(v -> submitRating());

        return view;
    }

    // ================= SUBMIT RATING =================
    private void submitRating() {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(getContext(),
                    "Moraš biti logovan da ocijeniš aplikaciju!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        float rating = ratingBar.getRating();
        String komentar = etKomentar.getText().toString().trim();

        if (rating == 0) {
            Toast.makeText(getContext(),
                    "Izaberi ocjenu!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(komentar)) {
            etKomentar.setError("Unesi komentar");
            return;
        }

        String userId = user.getUid();

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("rating", rating);
        data.put("comment", komentar);
        data.put("timestamp", System.currentTimeMillis());

        db.collection("app_ratings")
                .document(userId) // 1 user = 1 rating (možeš promijeniti ako želiš više)
                .set(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(),
                            "Hvala na ocjeni! ✅",
                            Toast.LENGTH_SHORT).show();

                    ratingBar.setRating(0);
                    etKomentar.setText("");
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(),
                                "Greška: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
    }
}