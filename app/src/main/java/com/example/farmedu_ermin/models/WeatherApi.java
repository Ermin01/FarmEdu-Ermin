package com.example.farmedu_ermin.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    // TRENUTNO VRIJEME PO GRADU
    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeather(

            @Query("q") String city,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("lang") String lang
    );

    // TRENUTNO VRIJEME PO GPS
    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeatherByCoords(

            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("lang") String lang
    );

    // FORECAST PO GRADU
    @GET("data/2.5/forecast")
    Call<ForecastResponse> getForecast(

            @Query("q") String city,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("lang") String lang
    );

    // FORECAST PO GPS
    @GET("data/2.5/forecast")
    Call<ForecastResponse> getForecastByCoords(

            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("lang") String lang
    );
}