package com.example.farmedu_ermin.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {

    @SerializedName("name")
    public String name;

    @SerializedName("main")
    public Main main;

    @SerializedName("wind")
    public Wind wind;

    @SerializedName("weather")
    public List<Weather> weather;

    @SerializedName("sys")
    public Sys sys;

    // =========================
    // SYS
    // =========================
    public static class Sys {

        @SerializedName("country")
        public String country;

        @SerializedName("sunrise")
        public long sunrise;

        @SerializedName("sunset")
        public long sunset;
    }

    // =========================
    // MAIN
    // =========================
    public static class Main {

        @SerializedName("temp")
        public float temp;

        @SerializedName("feels_like")
        public float feelsLike;

        @SerializedName("temp_min")
        public float tempMin;

        @SerializedName("temp_max")
        public float tempMax;

        @SerializedName("humidity")
        public int humidity;

        @SerializedName("pressure")
        public int pressure;
    }

    // =========================
    // WIND
    // =========================
    public static class Wind {

        @SerializedName("speed")
        public float speed;
    }

    // =========================
    // WEATHER
    // =========================
    public static class Weather {

        @SerializedName("main")
        public String main;

        @SerializedName("description")
        public String description;

        @SerializedName("icon")
        public String icon;
    }
}