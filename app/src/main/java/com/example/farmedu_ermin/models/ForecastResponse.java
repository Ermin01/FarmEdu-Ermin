package com.example.farmedu_ermin.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastResponse {

    @SerializedName("list")
    public List<ForecastItem> list;

    // =====================================
    // FORECAST ITEM
    // =====================================
    public static class ForecastItem {

        @SerializedName("main")
        public Main main;

        @SerializedName("weather")
        public List<Weather> weather;

        @SerializedName("dt_txt")
        public String dtTxt;
    }

    // =====================================
    // MAIN
    // =====================================
    public static class Main {

        @SerializedName("temp")
        public float temp;

        @SerializedName("temp_min")
        public float tempMin;

        @SerializedName("temp_max")
        public float tempMax;

        @SerializedName("humidity")
        public int humidity;
    }

    // =====================================
    // WEATHER
    // =====================================
    public static class Weather {

        @SerializedName("main")
        public String main;

        @SerializedName("description")
        public String description;

        @SerializedName("icon")
        public String icon;
    }
}