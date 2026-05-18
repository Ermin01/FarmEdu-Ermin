package com.example.farmedu_ermin;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.farmedu_ermin.models.TaskModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TaskStorage {

    private static final String PREF_NAME = "tasks";

    private static final String KEY = "task_list";

    public static ArrayList<TaskModel> getTasks(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );

        String json =
                prefs.getString(KEY, "");

        Gson gson = new Gson();

        Type type =
                new TypeToken<ArrayList<TaskModel>>() {
                }.getType();

        if (json.isEmpty()) {

            return new ArrayList<>();
        }

        return gson.fromJson(json, type);
    }

    public static void saveTasks(
            Context context,
            ArrayList<TaskModel> tasks
    ) {

        SharedPreferences prefs =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );

        Gson gson = new Gson();

        String json =
                gson.toJson(tasks);

        prefs.edit()
                .putString(KEY, json)
                .apply();
    }
}