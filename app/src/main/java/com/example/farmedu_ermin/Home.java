package com.example.farmedu_ermin;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.farmedu_ermin.chat.ChatFragment;
import com.example.farmedu_ermin.chat.ZahtjevPrijateljstvoFragment;
import com.example.farmedu_ermin.market.MarketplaceFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

public class Home extends Fragment {

    private ViewTreeObserver.OnScrollChangedListener scrollListener;
    private ListenerRegistration badgeListener;

    private TextView tvWelcome;
    private TextView txtAiTip;
    private TextView btnReadMore;

    private TextView txtAiTitle;
    private TextView txtAiCategory;
    private TextView txtAiConfidence;

    private ImageView aiAvatar;

    private LinearLayout loadingOverlay;

    private LinearLayout btnVoice;
    private LinearLayout btnRefreshAi;
    private LinearLayout aiTipCard;

    private TextView txtPogledajSve;

    private LinearLayout articlesContainer;
    private LinearLayout dotsContainer;

    private HorizontalScrollView horizontalArticles;

    private TextToSpeech textToSpeech;
    private boolean isSpeaking = false;

    private TextView txtVoice;
    private ImageView imgVoice;

    private JSONArray tipsArray;
    private JSONArray articlesArray;

    private String currentDescription = "";
    private String currentVoiceText = "";

    private ImageView profileImage;

    // WEATHER
    private TextView txtTemperature;
    private TextView txtCondition;
    private TextView txtHumidity;
    private TextView txtWind;
    private TextView txtLocation;
    private ImageView imgCountry;
    private ImageView weatherIcon;

    private TextView txtInboxBadge;

    public Home() {
    }

    // =========================
    // WEATHER RECEIVER
    // =========================

    private final BroadcastReceiver weatherReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    if (!isAdded()) return;

                    loadHomeWeather();
                }
            };

    // =========================
    // ON RESUME
    // =========================

    @Override
    public void onResume() {
        super.onResume();

        try {

            IntentFilter filter =
                    new IntentFilter("UPDATE_HOME_WEATHER");

            ContextCompat.registerReceiver(
                    requireContext(),
                    weatherReceiver,
                    filter,
                    ContextCompat.RECEIVER_NOT_EXPORTED
            );

        } catch (Exception ignored) {
        }

        loadHomeWeather();
    }

    // =========================
    // ON PAUSE
    // =========================

    @Override
    public void onPause() {
        super.onPause();

        try {
            requireContext().unregisterReceiver(weatherReceiver);
        } catch (Exception ignored) {
        }
    }

    // =========================
    // DESTROY VIEW
    // =========================

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (badgeListener != null) {
            badgeListener.remove();
            badgeListener = null;
        }

        try {

            if (horizontalArticles != null
                    && scrollListener != null) {

                horizontalArticles
                        .getViewTreeObserver()
                        .removeOnScrollChangedListener(scrollListener);
            }

        } catch (Exception ignored) {
        }

        horizontalArticles = null;
        articlesContainer = null;
        dotsContainer = null;
        articlesArray = null;
        tipsArray = null;
    }

    // =========================
    // CREATE VIEW
    // =========================

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view =
                inflater.inflate(
                        R.layout.fragment_home,
                        container,
                        false
                );

        // WEATHER
        txtTemperature = view.findViewById(R.id.txtTemperature);
        txtCondition = view.findViewById(R.id.txtCondition);
        txtHumidity = view.findViewById(R.id.txtHumidity);
        txtWind = view.findViewById(R.id.txtWind);
        txtLocation = view.findViewById(R.id.txtLocation);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        imgCountry = view.findViewById(R.id.imgCountry);

        // PROFILE
        profileImage = view.findViewById(R.id.profileImage);

        // AI
        tvWelcome = view.findViewById(R.id.tvWelcome);

        txtAiTip = view.findViewById(R.id.txtAiTip);
        btnReadMore = view.findViewById(R.id.btnReadMore);

        txtAiTitle = view.findViewById(R.id.txtAiTitle);
        txtAiCategory = view.findViewById(R.id.txtAiCategory);
        txtAiConfidence = view.findViewById(R.id.txtAiConfidence);

        aiAvatar = view.findViewById(R.id.aiAvatar);

        btnVoice = view.findViewById(R.id.btnVoice);
        txtVoice = view.findViewById(R.id.txtVoice);
        imgVoice = view.findViewById(R.id.imgVoice);

        btnRefreshAi = view.findViewById(R.id.btnRefreshAi);

        aiTipCard = view.findViewById(R.id.aiTipCard);

        txtPogledajSve = view.findViewById(R.id.txtPogledajSve);

        // ARTICLES
        articlesContainer = view.findViewById(R.id.articlesContainer);
        dotsContainer = view.findViewById(R.id.dotsContainer);
        horizontalArticles = view.findViewById(R.id.horizontalArticles);
        profileImage = view.findViewById(R.id.profileImage);

        txtInboxBadge = view.findViewById(R.id.txtInboxBadge);
        loadingOverlay = view.findViewById(R.id.loadingOverlay);

        setupWelcome();
        showLoading();
        loadJsonData();
        loadArticles();

        displayArticles();
        setupDots();
        setupScrollListener();

        loadRandomTip();

        startTipAnimation();
        startAvatarGlow();

        setupVoiceAI();

        loadProfileAvatar();
        loadHomeWeather();

        setupClicks(view);
        loadInboxBadge();

        view.postDelayed(() -> {

            if (isAdded()) {
                hideLoading();
            }

        }, 2200);

        return view;
    }

    private void loadInboxBadge() {

        FirebaseUser currentUser =
                FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {

            if (txtInboxBadge != null) {
                txtInboxBadge.animate().cancel();
            }

            return;
        }

        String currentUid = currentUser.getUid();

        // reset prije listenera
        txtInboxBadge.clearAnimation();
        txtInboxBadge.setVisibility(View.GONE);
        txtInboxBadge.setAlpha(0f);
        txtInboxBadge.setScaleX(0.7f);
        txtInboxBadge.setScaleY(0.7f);

        if (badgeListener != null) {
            badgeListener.remove();
            badgeListener = null;
        }

        badgeListener = FirebaseFirestore.getInstance()
                .collection("chat_requests")
                .whereEqualTo("receiverUid", currentUid)
                .whereEqualTo("status", "pending")
                .addSnapshotListener((value, error) -> {

                    if (!isAdded()
                            || getActivity() == null
                            || txtInboxBadge == null) {
                        return;
                    }

                    if (error != null) {

                        Log.e(
                                "FIREBASE_BADGE",
                                "BADGE ERROR",
                                error
                        );

                        txtInboxBadge.setVisibility(View.GONE);
                        return;
                    }

                    if (value == null || value.isEmpty()) {

                        txtInboxBadge.animate().cancel();

                        txtInboxBadge.animate()
                                .alpha(0f)
                                .scaleX(0.7f)
                                .scaleY(0.7f)
                                .setDuration(120)
                                .withEndAction(() -> {
                                    if (txtInboxBadge != null) {
                                        txtInboxBadge.setVisibility(View.GONE);
                                    }
                                })
                                .start();

                        return;
                    }

                    int count = value.size();

                    txtInboxBadge.setText(
                            count > 99
                                    ? "99+"
                                    : String.valueOf(count)
                    );

                    // prikazi smooth
                    if (txtInboxBadge.getVisibility() != View.VISIBLE) {

                        txtInboxBadge.setVisibility(View.VISIBLE);

                        txtInboxBadge.animate().cancel();

                        txtInboxBadge.animate()
                                .alpha(1f)
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(220)
                                .start();
                    }
                });
    }



    private void setupWelcome() {

        FirebaseUser user =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        if (user == null) {

            tvWelcome.setText(
                    getString(R.string.welcome_default)
            );

            return;
        }

        String name = user.getDisplayName();

        if (name != null && !name.trim().isEmpty()) {

            tvWelcome.setText(
                    getString(
                            R.string.welcome_user,
                            name
                    )
            );

        } else {

            tvWelcome.setText(
                    getString(R.string.welcome_default)
            );
        }

        // smooth fade
        tvWelcome.setAlpha(0f);

        tvWelcome.animate()
                .alpha(1f)
                .setDuration(350)
                .start();
    }

    // =========================
    // ALL CLICKS
    // =========================

    private void setupClicks(View view) {

        // VOICE

        btnVoice.setOnClickListener(v -> {

            if (textToSpeech == null) return;

            if (isSpeaking) {

                textToSpeech.stop();

                isSpeaking = false;

                txtVoice.setText(" Poslušaj");

                imgVoice.setImageResource(R.drawable.volume);

                Toast.makeText(
                        requireContext(),
                        "AI glas zaustavljen",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                String speakText =
                        currentVoiceText.isEmpty()
                                ? txtAiTip.getText().toString()
                                : currentVoiceText;

                textToSpeech.speak(
                        speakText,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "AI_VOICE"
                );

                isSpeaking = true;

                txtVoice.setText(" Zaustavi");

                imgVoice.setImageResource(
                        android.R.drawable.ic_media_pause
                );

                Toast.makeText(
                        requireContext(),
                        "AI čita savjet...",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // REFRESH AI

        btnRefreshAi.setOnClickListener(v -> {

            loadRandomTip();

            startTipAnimation();

            Toast.makeText(
                    requireContext(),
                    "Novi AI savjet učitan",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // READ MORE

        btnReadMore.setOnClickListener(v -> {

            String message =
                    txtAiTitle.getText().toString()
                            + "\n\n"
                            + currentDescription;

            Toast.makeText(
                    requireContext(),
                    message,
                    Toast.LENGTH_LONG
            ).show();
        });

        // AI CARD

        aiTipCard.setOnClickListener(v ->
                Toast.makeText(
                        requireContext(),
                        currentDescription,
                        Toast.LENGTH_LONG
                ).show()
        );

//click inbox
        FrameLayout btnInbox =
                view.findViewById(R.id.btnInbox);

        btnInbox.setOnClickListener(v -> {

            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                    )
                    .replace(
                            R.id.fragmentContainer,
                            new ZahtjevPrijateljstvoFragment()
                    )
                    .addToBackStack(null)
                    .commit();

        });

        // ARTICLES

        txtPogledajSve.setOnClickListener(v ->
                openFragment(new PopularniClanciFragment())
        );

        // PROFILE

        profileImage.setOnClickListener(v -> showProfileMenu());

        // CARDS

        view.findViewById(R.id.cardSavjeti)
                .setOnClickListener(v ->
                        openFragment(
                                new SavjetiStrucnjakaFragment()
                        )
                );

        view.findViewById(R.id.kameralive)
                .setOnClickListener(v ->
                        openFragment(
                                new FarmCameraFragment()
                        )
                );

        view.findViewById(R.id.martkshop)
                .setOnClickListener(v ->
                        openFragment(
                                new MarketplaceFragment()
                        )
                );







        view.findViewById(R.id.cardPlaner)
                .setOnClickListener(v ->
                        openFragment(
                                new PlanerdanaFragment()
                        )
                );
    }

    // =========================
    // PROFILE MENU
    // =========================

    private void showProfileMenu() {

        PopupMenu popupMenu =
                new PopupMenu(requireContext(), profileImage);

        popupMenu.getMenuInflater().inflate(
                R.menu.profile_menu,
                popupMenu.getMenu()
        );

        popupMenu.setOnMenuItemClickListener(item -> {

            int id = item.getItemId();

            if (id == R.id.menu_profile) {

                openFragment(new ProfileFragment());
                return true;
            }

            else if (id == R.id.menu_settings) {

                openFragment(new Opcije());
                return true;
            }

            else if (id == R.id.menu_fav_animals) {

                openFragment(new OmiljeniPetsFragment());
                return true;
            }

//            else if (id == R.id.chat) {
//
//                openFragment(new ChatFragment());
//                return true;
//            }

            else if (id == R.id.menu_fav_articles) {

                openFragment(new SacuvaniClanakFragment());
                return true;
            }

            FirebaseAuth.getInstance().signOut();

            Toast.makeText(
                    getContext(),
                    "Uspješno ste odjavljeni",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent =
                    new Intent(
                            requireActivity(),
                            MainActivity.class
                    );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            requireActivity().finish();

            return true;
        });

        popupMenu.show();
    }

    // =========================
    // WEATHER AUTO UPDATE FIX
    // =========================
// =========================
// WEATHER AUTO UPDATE FIX
// =========================

    private void loadHomeWeather() {

        if (!isAdded()) return;

        try {

            SharedPreferences prefs =
                    requireContext().getSharedPreferences(
                            "HOME_WEATHER",
                            Context.MODE_PRIVATE
                    );

            String temperature =
                    prefs.getString("temp", "--°C");

            String desc =
                    prefs.getString("desc", "Sunčano");

            String humidity =
                    prefs.getString("humidity", "--%");

            String wind =
                    prefs.getString("wind", "-- km/h");

            String city =
                    prefs.getString("city", "Lukavac");

            String country =
                    prefs.getString(
                            "country",
                            "Bosna i Hercegovina"
                    );

            String countryCode =
                    prefs.getString("countryCode", "ba");

            String iconCode =
                    prefs.getString("icon", "01d");

            // =========================
            // TEXT
            // =========================

            txtTemperature.setText(temperature);

            if (desc == null || desc.isEmpty()) {
                desc = "Sunčano";
            }

            desc =
                    desc.substring(0, 1).toUpperCase()
                            + desc.substring(1);

            String lower = desc.toLowerCase();

            if (lower.contains("kiša")
                    || lower.contains("rain")) {

                txtCondition.setText("🌧 " + desc);

            } else if (lower.contains("obla")
                    || lower.contains("cloud")) {

                txtCondition.setText("☁ " + desc);

            } else if (lower.contains("snijeg")
                    || lower.contains("snow")) {

                txtCondition.setText("❄ " + desc);

            } else if (lower.contains("oluja")
                    || lower.contains("storm")) {

                txtCondition.setText("⛈ " + desc);

            } else {

                txtCondition.setText("☀ " + desc);
            }

            txtHumidity.setText(
                    "Vlažnost " + humidity
            );

            txtWind.setText(
                    "Vjetar " + wind
            );

            txtLocation.setText(
                    city + ", " + country
            );

            // =========================
            // COUNTRY FLAG FIX
            // =========================

            if (countryCode == null
                    || countryCode.isEmpty()) {

                countryCode = "ba";
            }

            countryCode = countryCode.toLowerCase().trim();

            // FLAGSAPICOM
            String flagUrl =
                    "https://flagsapi.com/"
                            + countryCode.toUpperCase()
                            + "/flat/64.png";

            Glide.with(requireContext())
                    .load(flagUrl)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.active_bg)
                    .error(R.drawable.active_bg)
                    .into(imgCountry);

            // =========================
            // WEATHER ICON
            // =========================

            String weatherUrl =
                    "https://openweathermap.org/img/wn/"
                            + iconCode
                            + "@4x.png";

            Glide.with(requireContext())
                    .load(weatherUrl)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.sunny_light)
                    .error(R.drawable.sunny_light)
                    .into(weatherIcon);

        } catch (Exception e) {

            e.printStackTrace();

            txtTemperature.setText("--°C");
            txtCondition.setText("Weather error");
            txtHumidity.setText("Vlažnost --%");
            txtWind.setText("Vjetar -- km/h");
            txtLocation.setText("Lukavac");
        }
    }

    // =========================
    // PROFILE AVATAR
    // =========================

    private void loadProfileAvatar() {

        FirebaseUser user =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        if (user == null) {

            profileImage.setImageResource(
                    R.drawable.user
            );

            return;
        }

        String uid = user.getUid();

        SharedPreferences prefs =
                requireContext().getSharedPreferences(
                        "ProfilePrefs",
                        Context.MODE_PRIVATE
                );

        String avatarKey = "avatar_" + uid;

        int savedAvatar =
                prefs.getInt(
                        avatarKey,
                        R.drawable.user
                );

        profileImage.setImageResource(savedAvatar);

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (!isAdded()) return;

                    if (documentSnapshot.exists()) {

                        Long avatar =
                                documentSnapshot.getLong("avatar");

                        if (avatar != null) {

                            int avatarRes =
                                    avatar.intValue();

                            prefs.edit()
                                    .putInt(
                                            avatarKey,
                                            avatarRes
                                    )
                                    .apply();

                            profileImage.setImageResource(
                                    avatarRes
                            );
                        }
                    }
                });
    }

    // =========================
    // JSON
    // =========================

    private void loadJsonData() {

        try {

            SharedPreferences prefs =
                    requireActivity()
                            .getSharedPreferences(
                                    "Settings",
                                    Context.MODE_PRIVATE
                            );

            String lang =
                    prefs.getString("lang", "bs");

            int rawFile;

            switch (lang) {

                case "de":
                    rawFile = R.raw.ai_tips_de;
                    break;

                case "en":
                    rawFile = R.raw.ai_tips_en;
                    break;

                default:
                    rawFile = R.raw.ai_tips;
                    break;
            }

            InputStream is =
                    getResources()
                            .openRawResource(rawFile);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            String json =
                    new String(
                            buffer,
                            StandardCharsets.UTF_8
                    );

            tipsArray = new JSONArray(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // ARTICLES
    // =========================

    private void loadArticles() {

        try {

            SharedPreferences prefs =
                    requireActivity()
                            .getSharedPreferences(
                                    "Settings",
                                    Context.MODE_PRIVATE
                            );

            String lang =
                    prefs.getString("lang", "bs");

            int rawFile;

            switch (lang) {

                case "de":
                    rawFile = R.raw.popularni_clanci_de;
                    break;

                case "en":
                    rawFile = R.raw.popularni_clanci_en;
                    break;

                default:
                    rawFile = R.raw.popularni_clanci;
                    break;
            }

            InputStream is =
                    getResources()
                            .openRawResource(rawFile);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            String json =
                    new String(
                            buffer,
                            StandardCharsets.UTF_8
                    );

            articlesArray = new JSONArray(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // DISPLAY ARTICLES
    // =========================

    private void displayArticles() {

        try {

            articlesContainer.removeAllViews();

            if (articlesArray == null
                    || articlesArray.length() == 0) {
                return;
            }

            for (int i = 0;
                 i < articlesArray.length();
                 i++) {

                JSONObject object =
                        articlesArray.getJSONObject(i);

                String title =
                        object.getString("title");

                String desc =
                        object.getString("description");

                String category =
                        object.getString("category");

                String reading =
                        object.getString("time");

                String image =
                        object.getString("image");

                int imageRes =
                        getResources().getIdentifier(
                                image,
                                "drawable",
                                requireContext()
                                        .getPackageName()
                        );

                CardView card =
                        new CardView(requireContext());

                LinearLayout.LayoutParams cardParams =
                        new LinearLayout.LayoutParams(
                                dpToPx(175),
                                dpToPx(260)
                        );

                cardParams.setMarginEnd(dpToPx(12));

                card.setLayoutParams(cardParams);

                card.setRadius(dpToPx(18));

                card.setCardElevation(dpToPx(4));

                LinearLayout root =
                        new LinearLayout(requireContext());

                root.setOrientation(
                        LinearLayout.VERTICAL
                );

                root.setBackgroundColor(0xFFFFFFFF);

                FrameLayout frame =
                        new FrameLayout(requireContext());

                frame.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                dpToPx(140)
                        )
                );

                ImageView img =
                        new ImageView(requireContext());

                img.setLayoutParams(
                        new FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        )
                );

                img.setScaleType(
                        ImageView.ScaleType.CENTER_CROP
                );

                img.setImageResource(imageRes);

                frame.addView(img);

                TextView badge =
                        new TextView(requireContext());

                FrameLayout.LayoutParams badgeParams =
                        new FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                dpToPx(28)
                        );

                badgeParams.setMargins(
                        dpToPx(10),
                        dpToPx(10),
                        0,
                        0
                );

                badge.setLayoutParams(badgeParams);

                badge.setText(" " + category + " ");

                badge.setTextColor(0xFFFFFFFF);

                badge.setTextSize(10);

                badge.setPadding(
                        dpToPx(10),
                        0,
                        dpToPx(10),
                        0
                );

                badge.setGravity(Gravity.CENTER);

                badge.setBackgroundColor(0xCC6FAF45);

                frame.addView(badge);

                LinearLayout content =
                        new LinearLayout(requireContext());

                content.setOrientation(
                        LinearLayout.VERTICAL
                );

                content.setPadding(
                        dpToPx(12),
                        dpToPx(12),
                        dpToPx(12),
                        dpToPx(12)
                );

                TextView tvTitle =
                        new TextView(requireContext());

                tvTitle.setText(title);

                tvTitle.setTextSize(17);

                tvTitle.setTextColor(0xFF222222);

                tvTitle.setMaxLines(2);

                tvTitle.setTypeface(
                        null,
                        Typeface.BOLD
                );

                TextView tvDesc =
                        new TextView(requireContext());

                tvDesc.setText(desc);

                tvDesc.setTextSize(12);

                tvDesc.setTextColor(0xFF777777);

                TextView tvRead =
                        new TextView(requireContext());

                tvRead.setText(reading);

                tvRead.setTextColor(0xFF6FAF45);

                tvRead.setTextSize(11);

                content.addView(tvTitle);
                content.addView(tvDesc);
                content.addView(tvRead);

                root.addView(frame);
                root.addView(content);

                card.addView(root);

                articlesContainer.addView(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // DOTS
    // =========================

    private void setupDots() {

        dotsContainer.removeAllViews();

        if (articlesArray == null) return;

        int totalPages =
                (int) Math.ceil(
                        articlesArray.length() / 2.0
                );

        for (int i = 0; i < totalPages; i++) {

            View dot = new View(requireContext());

            int size =
                    (i == 0)
                            ? dpToPx(10)
                            : dpToPx(8);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            size,
                            size
                    );

            params.setMargins(
                    dpToPx(4),
                    dpToPx(4),
                    dpToPx(4),
                    dpToPx(4)
            );

            dot.setLayoutParams(params);

            if (i == 0) {

                dot.setBackgroundResource(
                        R.drawable.dot_active
                );

            } else {

                dot.setBackgroundResource(
                        R.drawable.dot_inactive
                );
            }

            dotsContainer.addView(dot);
        }
    }

    // =========================
    // SCROLL
    // =========================

    private void setupScrollListener() {

        scrollListener = () -> {

            if (!isAdded()
                    || horizontalArticles == null
                    || articlesArray == null) {
                return;
            }

            int scrollX =
                    horizontalArticles.getScrollX();

            int cardWidth = dpToPx(187);

            int pageWidth = cardWidth * 2;

            int position = scrollX / pageWidth;

            updateDots(position);
        };

        horizontalArticles
                .getViewTreeObserver()
                .addOnScrollChangedListener(scrollListener);

        horizontalArticles.setOnTouchListener(
                (v, event) -> {

                    if (event.getAction()
                            == MotionEvent.ACTION_UP) {

                        v.postDelayed(() -> {

                            int scrollX =
                                    horizontalArticles.getScrollX();

                            int cardWidth =
                                    dpToPx(187);

                            int pageWidth =
                                    cardWidth * 2;

                            int position =
                                    Math.round(
                                            (float) scrollX
                                                    / pageWidth
                                    );

                            horizontalArticles.smoothScrollTo(
                                    position * pageWidth,
                                    0
                            );

                            updateDots(position);

                        }, 80);
                    }

                    return false;
                });
    }

    private void updateDots(int activePosition) {

        for (int i = 0;
             i < dotsContainer.getChildCount();
             i++) {

            View dot =
                    dotsContainer.getChildAt(i);

            LinearLayout.LayoutParams params =
                    (LinearLayout.LayoutParams)
                            dot.getLayoutParams();

            if (i == activePosition) {

                params.width = dpToPx(10);
                params.height = dpToPx(10);

                dot.setBackgroundResource(
                        R.drawable.dot_active
                );

            } else {

                params.width = dpToPx(8);
                params.height = dpToPx(8);

                dot.setBackgroundResource(
                        R.drawable.dot_inactive
                );
            }

            dot.setLayoutParams(params);
        }
    }

    // =========================
    // OPEN FRAGMENT
    // =========================

    private void openFragment(Fragment fragment) {

        if (getActivity() == null) return;

        getParentFragmentManager()
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
    }

    // =========================
    // RANDOM TIP
    // =========================

    private void loadRandomTip() {

        try {

            if (tipsArray == null
                    || tipsArray.length() == 0) {

                txtAiTip.setText(
                        getString(
                                R.string.ai_tip_not_available
                        )
                );

                return;
            }

            Random random = new Random();

            JSONObject object =
                    tipsArray.getJSONObject(
                            random.nextInt(
                                    tipsArray.length()
                            )
                    );

            String title =
                    object.getString("title");

            String tip =
                    object.getString("tip");

            String description =
                    object.getString("description");

            String category =
                    object.getString("category");

            String priority =
                    object.getString("priority");

            String voice =
                    object.getString("voice");

            String emoji =
                    object.getString("emoji");

            currentDescription = description;
            currentVoiceText = voice;

            txtAiTitle.setText(
                    emoji + " " + title
            );

            txtAiTip.setText(tip);

            txtAiCategory.setText(category);

            if (priority.equals("high")) {

                txtAiConfidence.setText(
                        getString(
                                R.string.ai_confidence_high
                        )
                );

            } else if (priority.equals("medium")) {

                txtAiConfidence.setText(
                        getString(
                                R.string.ai_confidence_medium
                        )
                );

            } else {

                txtAiConfidence.setText(
                        getString(
                                R.string.ai_confidence_low
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // ANIMATION
    // =========================

    private void startTipAnimation() {

        AlphaAnimation fadeIn =
                new AlphaAnimation(0f, 1f);

        fadeIn.setDuration(900);

        txtAiTip.startAnimation(fadeIn);
    }

    private void startAvatarGlow() {

        ObjectAnimator scaleX =
                ObjectAnimator.ofFloat(
                        aiAvatar,
                        "scaleX",
                        1f,
                        1.08f,
                        1f
                );

        scaleX.setDuration(1800);

        scaleX.setRepeatCount(
                ObjectAnimator.INFINITE
        );

        ObjectAnimator scaleY =
                ObjectAnimator.ofFloat(
                        aiAvatar,
                        "scaleY",
                        1f,
                        1.08f,
                        1f
                );

        scaleY.setDuration(1800);

        scaleY.setRepeatCount(
                ObjectAnimator.INFINITE
        );

        scaleX.start();
        scaleY.start();
    }

    // =========================
    // VOICE AI
    // =========================

    private void setupVoiceAI() {

        SharedPreferences prefs =
                requireActivity()
                        .getSharedPreferences(
                                "Settings",
                                Context.MODE_PRIVATE
                        );

        String lang =
                prefs.getString("lang", "bs");

        Locale locale;

        switch (lang) {

            case "de":
                locale = Locale.GERMAN;
                break;

            case "en":
                locale = Locale.ENGLISH;
                break;

            default:
                locale = new Locale("bs");
                break;
        }

        textToSpeech =
                new TextToSpeech(
                        requireContext(),
                        status -> {

                            if (status != TextToSpeech.ERROR) {

                                textToSpeech.setLanguage(locale);

                                textToSpeech.setSpeechRate(0.9f);

                                textToSpeech.setOnUtteranceProgressListener(
                                        new android.speech.tts.UtteranceProgressListener() {

                                            @Override
                                            public void onStart(String utteranceId) {
                                            }

                                            @Override
                                            public void onDone(String utteranceId) {

                                                if (getActivity() == null)
                                                    return;

                                                getActivity().runOnUiThread(() -> {

                                                    isSpeaking = false;

                                                    txtVoice.setText(" Poslušaj");

                                                    imgVoice.setImageResource(
                                                            R.drawable.volume
                                                    );
                                                });
                                            }

                                            @Override
                                            public void onError(String utteranceId) {
                                            }
                                        });
                            }
                        });
    }

    // =========================
    // DP
    // =========================

    private int dpToPx(int dp) {

        float density =
                getResources()
                        .getDisplayMetrics()
                        .density;

        return Math.round(dp * density);
    }

    // =========================
    // DESTROY
    // =========================

    private void showLoading() {

        if (loadingOverlay == null) return;

        loadingOverlay.setAlpha(0f);
        loadingOverlay.setVisibility(View.VISIBLE);

        loadingOverlay.animate()
                .alpha(1f)
                .setDuration(200)
                .start();
    }

    private void hideLoading() {

        if (loadingOverlay == null) return;

        loadingOverlay.animate()
                .alpha(0f)
                .setDuration(250)
                .withEndAction(() ->
                        loadingOverlay.setVisibility(View.GONE))
                .start();
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