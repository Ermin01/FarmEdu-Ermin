package com.example.farmedu_ermin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class FavoriteManager {

    private static final String PREF = "favorites_pref";
    private static final String KEY = "favorites";

    public static void addFavorite(Context context, String id) {
        Set<String> set = getFavorites(context);
        set.add(id);
        save(context, set);
    }

    public static void removeFavorite(Context context, String id) {
        Set<String> set = getFavorites(context);
        set.remove(id);
        save(context, set);
    }

    public static Set<String> getFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return new HashSet<>(prefs.getStringSet(KEY, new HashSet<>()));
    }

    private static void save(Context context, Set<String> set) {
        SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        prefs.edit().putStringSet(KEY, set).apply();
    }

    public static boolean isFavorite(Context context, String id) {
        return getFavorites(context).contains(id);
    }
}