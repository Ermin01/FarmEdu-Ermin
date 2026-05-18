package com.example.farmedu_ermin;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.farmedu_ermin.models.SavedArticle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetaljiClankaFragment extends Fragment {

    private TextView articleTitle;
    private TextView articleCategory;
    private TextView articleDescription;
    private TextView articleTime;

    private ImageView btnShare;
    private ImageView articleImage;
    private ImageView btnBack;
    private ImageView btnBookmark;

    private LinearLayout btnVoice;
    private ImageView voiceIcon;

    private LinearLayout tagsContainer;

    private TextToSpeech textToSpeech;

    private ProgressBar readingProgress;

    private boolean isSpeaking = false;

    public DetaljiClankaFragment() {
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_detalji_clanka,
                container,
                false
        );

        // =====================================
        // INIT
        // =====================================

        articleTitle =
                view.findViewById(R.id.articleTitle);

        articleCategory =
                view.findViewById(R.id.articleCategory);

        articleDescription =
                view.findViewById(R.id.articleDescription);

        articleTime =
                view.findViewById(R.id.articleTime);

        articleImage =
                view.findViewById(R.id.articleImage);

        btnVoice =
                view.findViewById(R.id.btnVoice);

        voiceIcon =
                view.findViewById(R.id.voiceIcon);

        btnBack =
                view.findViewById(R.id.btnBack);

        btnBookmark =
                view.findViewById(R.id.btnBookmark);

        btnShare =
                view.findViewById(R.id.btnShare);

        tagsContainer =
                view.findViewById(R.id.tagsContainer);

        readingProgress =
                view.findViewById(R.id.readingProgress);

        ScrollView articleScroll =
                view.findViewById(R.id.articleScroll);

        // =====================================
        // SCROLL PROGRESS
        // =====================================

        articleScroll
                .getViewTreeObserver()
                .addOnScrollChangedListener(() -> {

                    View content =
                            articleScroll.getChildAt(0);

                    if (content != null) {

                        int scrollY =
                                articleScroll.getScrollY();

                        int contentHeight =
                                content.getHeight();

                        int scrollViewHeight =
                                articleScroll.getHeight();

                        int totalScrollable =
                                contentHeight - scrollViewHeight;

                        if (totalScrollable > 0) {

                            int progress =
                                    (int) (
                                            ((float) scrollY
                                                    / totalScrollable)
                                                    * 100
                                    );

                            readingProgress.setProgress(progress);

                        } else {

                            readingProgress.setProgress(0);
                        }
                    }
                });

        // =====================================
        // GET DATA
        // =====================================

        Bundle args = getArguments();

        final String title =
                args != null
                        ? args.getString("title", "")
                        : "";

        final String category =
                args != null
                        ? args.getString("category", "")
                        : "";

        final String description =
                args != null
                        ? args.getString("description", "")
                        : "";

        final String time =
                args != null
                        ? args.getString("time", "")
                        : "";

        final String image =
                args != null
                        ? args.getString("image", "")
                        : "";

        final String id =
                args != null
                        ? args.getString("id", "")
                        : "";

        final String[] tags =
                args != null
                        ? args.getStringArray("tags")
                        : null;

        // =====================================
        // SET DATA
        // =====================================

        articleTitle.setText(title);
        articleCategory.setText(category);
        articleDescription.setText(description);
        articleTime.setText(time);

        // =====================================
        // IMAGE
        // =====================================

        int imageRes =
                getResources().getIdentifier(
                        image,
                        "drawable",
                        requireContext().getPackageName()
                );

        if (imageRes != 0) {

            articleImage.setImageResource(imageRes);
        }

        // =====================================
        // TAGS
        // =====================================

        tagsContainer.removeAllViews();

        if (tags != null && tags.length > 0) {

            for (String tag : tags) {

                TextView tagView =
                        new TextView(requireContext());

                tagView.setText("#" + tag);

                tagView.setTextSize(14);

                tagView.setTextColor(
                        getResources().getColor(R.color.orange)
                );

                tagView.setBackgroundResource(
                        R.drawable.tag_bg_green
                );

                tagView.setPadding(
                        32,
                        16,
                        32,
                        16
                );

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                params.setMarginEnd(16);

                tagView.setLayoutParams(params);

                tagsContainer.addView(tagView);
            }
        }

        // =====================================
        // CHECK BOOKMARK
        // =====================================

        SharedPreferences prefs =
                requireContext().getSharedPreferences(
                        "BOOKMARKS",
                        Context.MODE_PRIVATE
                );

        Gson gson = new Gson();

        String json =
                prefs.getString(
                        "saved_articles",
                        null
                );

        Type type =
                new TypeToken<ArrayList<SavedArticle>>() {
                }.getType();

        List<SavedArticle> list =
                (json != null)
                        ? gson.fromJson(json, type)
                        : new ArrayList<>();

        boolean alreadySaved = false;

        for (SavedArticle a : list) {

            if (a.id.equals(id)) {

                alreadySaved = true;
                break;
            }
        }

        if (alreadySaved) {

            btnBookmark.setImageResource(
                    R.drawable.bookmark_filll
            );

        } else {

            btnBookmark.setImageResource(
                    R.drawable.bookmark
            );
        }

        // =====================================
        // BACK
        // =====================================

        btnBack.setOnClickListener(v ->

                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()

        );

        // =====================================
        // SHARE
        // =====================================

        btnShare.setOnClickListener(v -> {

            String shareText =
                    "🌱 "
                            + title
                            + "\n\n"
                            + description
                            + "\n\nFarmEdu 🚜";

            Intent intent =
                    new Intent(Intent.ACTION_SEND);

            intent.setType("text/plain");

            intent.putExtra(
                    Intent.EXTRA_TEXT,
                    shareText
            );

            startActivity(
                    Intent.createChooser(
                            intent,
                            "Podijeli"
                    )
            );
        });

        // =====================================
        // BOOKMARK
        // =====================================

        btnBookmark.setOnClickListener(v -> {

            SharedPreferences sharedPrefs =
                    requireContext().getSharedPreferences(
                            "BOOKMARKS",
                            Context.MODE_PRIVATE
                    );

            Gson gson1 = new Gson();

            String savedJson =
                    sharedPrefs.getString(
                            "saved_articles",
                            null
                    );

            Type savedType =
                    new TypeToken<ArrayList<SavedArticle>>() {
                    }.getType();

            List<SavedArticle> savedList =
                    (savedJson != null)
                            ? gson1.fromJson(savedJson, savedType)
                            : new ArrayList<>();

            boolean exists = false;

            SavedArticle toRemove = null;

            for (SavedArticle a : savedList) {

                if (a.id.equals(id)) {

                    exists = true;
                    toRemove = a;
                    break;
                }
            }

            if (toRemove != null) {

                savedList.remove(toRemove);
            }

            if (exists) {

                btnBookmark.setImageResource(
                        R.drawable.bookmark
                );

                Toast.makeText(
                        requireContext(),
                        "Uklonjeno iz favorita",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                savedList.add(
                        new SavedArticle(
                                id,
                                title,
                                category,
                                description,
                                time,
                                image
                        )
                );

                btnBookmark.setImageResource(
                        R.drawable.bookmark_filll
                );

                Toast.makeText(
                        requireContext(),
                        "Članak spremljen",
                        Toast.LENGTH_SHORT
                ).show();
            }

            sharedPrefs.edit()
                    .putString(
                            "saved_articles",
                            gson1.toJson(savedList)
                    )
                    .apply();

            ObjectAnimator.ofFloat(
                    btnBookmark,
                    "scaleX",
                    1f,
                    1.2f,
                    1f
            ).setDuration(200).start();

            ObjectAnimator.ofFloat(
                    btnBookmark,
                    "scaleY",
                    1f,
                    1.2f,
                    1f
            ).setDuration(200).start();
        });

        // =====================================
        // TTS LANGUAGE
        // =====================================

        String language =
                Locale.getDefault().getLanguage();

        Locale ttsLocale;

        switch (language) {

            case "de":
                ttsLocale = Locale.GERMAN;
                break;

            case "en":
                ttsLocale = Locale.ENGLISH;
                break;

            default:
                ttsLocale = new Locale("bs", "BA");
                break;
        }

        // =====================================
        // TEXT TO SPEECH
        // =====================================

        textToSpeech =
                new TextToSpeech(
                        requireContext(),
                        status -> {

                            if (status != TextToSpeech.ERROR) {

                                textToSpeech.setLanguage(ttsLocale);

                                textToSpeech.setSpeechRate(0.9f);

                                textToSpeech.setOnUtteranceProgressListener(
                                        new UtteranceProgressListener() {

                                            @Override
                                            public void onStart(String utteranceId) {
                                            }

                                            @Override
                                            public void onDone(String utteranceId) {

                                                requireActivity().runOnUiThread(() -> {

                                                    voiceIcon.setImageResource(
                                                            android.R.drawable.ic_btn_speak_now
                                                    );

                                                    isSpeaking = false;
                                                });
                                            }

                                            @Override
                                            public void onError(String utteranceId) {
                                            }
                                        }
                                );
                            }
                        }
                );

        // =====================================
        // VOICE BUTTON
        // =====================================

        btnVoice.setOnClickListener(v -> {

            if (isSpeaking) {

                textToSpeech.stop();

                voiceIcon.setImageResource(
                        android.R.drawable.ic_btn_speak_now
                );

                isSpeaking = false;

            } else {

                String text =
                        title
                                + ". "
                                + description;

                textToSpeech.speak(
                        text,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "ARTICLE_TTS"
                );

                voiceIcon.setImageResource(
                        android.R.drawable.ic_media_pause
                );

                isSpeaking = true;
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {

        if (textToSpeech != null) {

            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        super.onDestroy();
    }
}