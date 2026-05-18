package com.example.farmedu_ermin;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.farmedu_ermin.adapters.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class PopularniClanciFragment extends Fragment {

    private LinearLayout articlesContainer;
    private JSONArray articlesArray;

    public PopularniClanciFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_popularni_clanci,
                container,
                false
        );

        articlesContainer =
                view.findViewById(R.id.articlesContainer);

        loadArticles();

        displayArticles();

        return view;
    }

    // ====================================================
    // LOAD JSON BASED ON SAVED LANGUAGE
    // ====================================================

    private void loadArticles() {

        try {

            // 🔥 UZIMA JEZIK KOJI JE KORISNIK IZABRAO
            String language =
                    LocaleHelper.getLanguage(
                            requireContext()
                    );

            int rawRes;

            // ====================================================
            // LANGUAGE FIX
            // ====================================================

            if (language.equalsIgnoreCase("en")) {

                rawRes =
                        R.raw.popularni_clanci_en;

            } else if (language.equalsIgnoreCase("de")) {

                rawRes =
                        R.raw.popularni_clanci_de;

            } else {

                // ✅ BOSANSKI DEFAULT
                rawRes =
                        R.raw.popularni_clanci;
            }

            InputStream is =
                    getResources()
                            .openRawResource(rawRes);

            int size =
                    is.available();

            byte[] buffer =
                    new byte[size];

            is.read(buffer);

            is.close();

            String json =
                    new String(
                            buffer,
                            StandardCharsets.UTF_8
                    );

            articlesArray =
                    new JSONArray(json);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ====================================================
    // DISPLAY ARTICLES
    // ====================================================

    private void displayArticles() {

        try {

            if (articlesArray == null) return;

            articlesContainer.removeAllViews();

            for (int i = 0;
                 i < articlesArray.length();
                 i++) {

                JSONObject object =
                        articlesArray.getJSONObject(i);

                String id =
                        object.optString("id");

                String title =
                        object.optString("title");

                String description =
                        object.optString("description");

                String category =
                        object.optString("category");

                String time =
                        object.optString("time");

                String image =
                        object.optString("image");

                // ====================================================
                // TAGS
                // ====================================================

                JSONArray tagsArray =
                        object.optJSONArray("tags");

                String[] tags =
                        new String[
                                tagsArray != null
                                        ? tagsArray.length()
                                        : 0
                                ];

                if (tagsArray != null) {

                    for (int j = 0;
                         j < tagsArray.length();
                         j++) {

                        tags[j] =
                                tagsArray.getString(j);
                    }
                }

                // ====================================================
                // IMAGE
                // ====================================================

                int imageRes =
                        getResources().getIdentifier(
                                image,
                                "drawable",
                                requireContext()
                                        .getPackageName()
                        );

                if (imageRes == 0) {

                    imageRes =
                            R.drawable.user;
                }

                // ====================================================
                // CARD
                // ====================================================

                CardView card =
                        new CardView(requireContext());

                LinearLayout.LayoutParams cardParams =
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                dpToPx(170)
                        );

                cardParams.setMargins(
                        dpToPx(18),
                        dpToPx(14),
                        dpToPx(18),
                        0
                );

                card.setLayoutParams(cardParams);

                card.setRadius(dpToPx(22));

                card.setCardElevation(dpToPx(3));

                card.setCardBackgroundColor(
                        0xFFFFFFFF
                );

                // ====================================================
                // ROOT
                // ====================================================

                LinearLayout root =
                        new LinearLayout(requireContext());

                root.setOrientation(
                        LinearLayout.HORIZONTAL
                );

                root.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        )
                );

                // ====================================================
                // IMAGE VIEW
                // ====================================================

                ImageView img =
                        new ImageView(requireContext());

                LinearLayout.LayoutParams imgParams =
                        new LinearLayout.LayoutParams(
                                dpToPx(140),
                                ViewGroup.LayoutParams.MATCH_PARENT
                        );

                img.setLayoutParams(imgParams);

                img.setScaleType(
                        ImageView.ScaleType.CENTER_CROP
                );

                img.setImageResource(imageRes);

                // ====================================================
                // CONTENT
                // ====================================================

                LinearLayout content =
                        new LinearLayout(requireContext());

                LinearLayout.LayoutParams contentParams =
                        new LinearLayout.LayoutParams(
                                0,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                1f
                        );

                content.setLayoutParams(contentParams);

                content.setOrientation(
                        LinearLayout.VERTICAL
                );

                content.setPadding(
                        dpToPx(16),
                        dpToPx(16),
                        dpToPx(16),
                        dpToPx(16)
                );

                // ====================================================
                // CATEGORY BOX
                // ====================================================

                LinearLayout categoryBox =
                        new LinearLayout(requireContext());

                categoryBox.setOrientation(
                        LinearLayout.HORIZONTAL
                );

                categoryBox.setBackgroundColor(
                        0xFFEEF5EB
                );

                categoryBox.setPadding(
                        dpToPx(12),
                        dpToPx(8),
                        dpToPx(12),
                        dpToPx(8)
                );

                // ====================================================
                // DOT
                // ====================================================

                View dot =
                        new View(requireContext());

                LinearLayout.LayoutParams dotParams =
                        new LinearLayout.LayoutParams(
                                dpToPx(10),
                                dpToPx(10)
                        );

                dotParams.rightMargin =
                        dpToPx(6);

                dot.setLayoutParams(dotParams);

                dot.setBackgroundResource(
                        R.drawable.dot_active
                );

                // ====================================================
                // CATEGORY TEXT
                // ====================================================

                TextView tvCategory =
                        new TextView(requireContext());

                tvCategory.setText(category);

                tvCategory.setTextSize(12);

                tvCategory.setTypeface(
                        null,
                        Typeface.BOLD
                );

                tvCategory.setTextColor(
                        0xFF4E9A3E
                );

                categoryBox.addView(dot);
                categoryBox.addView(tvCategory);

                // ====================================================
                // TITLE
                // ====================================================

                TextView tvTitle =
                        new TextView(requireContext());

                tvTitle.setText(title);

                tvTitle.setTextSize(20);

                tvTitle.setTypeface(
                        null,
                        Typeface.BOLD
                );

                tvTitle.setTextColor(
                        0xFF111111
                );

                tvTitle.setMaxLines(2);

                LinearLayout.LayoutParams titleParams =
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                titleParams.topMargin =
                        dpToPx(14);

                tvTitle.setLayoutParams(titleParams);

                // ====================================================
                // DESCRIPTION
                // ====================================================

                TextView tvDesc =
                        new TextView(requireContext());

                tvDesc.setText(description);

                tvDesc.setTextSize(14);

                tvDesc.setTextColor(
                        0xFF6B6B6B
                );

                tvDesc.setMaxLines(2);

                LinearLayout.LayoutParams descParams =
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                descParams.topMargin =
                        dpToPx(8);

                tvDesc.setLayoutParams(descParams);

                // ====================================================
                // TIME
                // ====================================================

                TextView tvTime =
                        new TextView(requireContext());

                tvTime.setText(time);

                tvTime.setTextSize(14);

                tvTime.setTypeface(
                        null,
                        Typeface.BOLD
                );

                tvTime.setTextColor(
                        0xFF4E9A3E
                );

                LinearLayout.LayoutParams timeParams =
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                timeParams.topMargin =
                        dpToPx(18);

                tvTime.setLayoutParams(timeParams);

                // ====================================================
                // ADD VIEWS
                // ====================================================

                content.addView(categoryBox);
                content.addView(tvTitle);
                content.addView(tvDesc);
                content.addView(tvTime);

                root.addView(img);
                root.addView(content);

                card.addView(root);

                // ====================================================
                // OPEN ARTICLE
                // ====================================================

                card.setOnClickListener(v -> {

                    Bundle bundle =
                            new Bundle();

                    bundle.putString("id", id);

                    bundle.putString(
                            "title",
                            title
                    );

                    bundle.putString(
                            "category",
                            category
                    );

                    bundle.putString(
                            "description",
                            description
                    );

                    bundle.putString(
                            "time",
                            time
                    );

                    bundle.putString(
                            "image",
                            image
                    );

                    bundle.putStringArray(
                            "tags",
                            tags
                    );

                    DetaljiClankaFragment fragment =
                            new DetaljiClankaFragment();

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

                articlesContainer.addView(card);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ====================================================
    // DP TO PX
    // ====================================================

    private int dpToPx(int dp) {

        float density =
                getResources()
                        .getDisplayMetrics()
                        .density;

        return Math.round(dp * density);
    }
}