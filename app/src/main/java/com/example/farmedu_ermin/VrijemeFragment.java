package com.example.farmedu_ermin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.farmedu_ermin.models.ForecastResponse;
import com.example.farmedu_ermin.models.WeatherApi;
import com.example.farmedu_ermin.models.WeatherResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VrijemeFragment extends Fragment {

    private static final String API_KEY =
            "814ab437dfc2fbbb570ddacff892a6af";

    private static final String PREFS_NAME =
            "WeatherPrefs";

    private static final String KEY_CITY =
            "selected_city";

    private static final int LOCATION_REQUEST = 1001;

    private TextView grad, datum, temp, opis;

    private TextView tvUv, tvFeels;

    private ImageView icon, btnBack, imgCountry;

    private EditText etSearchCity;

    private LinearLayout forecastContainer;

    private FrameLayout loadingLayout;

    private WeatherApi api;

    public VrijemeFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_vrijeme,
                container,
                false
        );

        initRetrofit();

        // TEXT
        grad = view.findViewById(R.id.tvGrad);
        datum = view.findViewById(R.id.tvDatum);
        temp = view.findViewById(R.id.tvTemp);
        opis = view.findViewById(R.id.tvOpis);

        tvUv = view.findViewById(R.id.tvUv);
        tvFeels = view.findViewById(R.id.tvFeels);

        // IMAGE
        icon = view.findViewById(R.id.weatherIcon);
        btnBack = view.findViewById(R.id.btnBack);
        imgCountry = view.findViewById(R.id.imgCountry);

        // SEARCH
        etSearchCity =
                view.findViewById(R.id.etSearchCity);

        // CRNA SLOVA U SEARCH
        etSearchCity.setTextColor(Color.BLACK);
        etSearchCity.setHintTextColor(Color.parseColor("#80000000"));

        // FORECAST
        forecastContainer =
                view.findViewById(R.id.forecastContainer);

        // LOADING
        loadingLayout =
                view.findViewById(R.id.loadingLayout);

        showLoading(true);

        // BACK
        btnBack.setOnClickListener(v -> {

            if (getActivity() != null) {

                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack();
            }
        });

        // SEARCH
        etSearchCity.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE) {

                String city =
                        etSearchCity.getText()
                                .toString()
                                .trim();

                if (!city.isEmpty()) {

                    showLoading(true);

                    saveCity(city);

                    getWeatherByCity(city);

                    etSearchCity.setText("");

                    etSearchCity.clearFocus();
                }

                return true;
            }

            return false;
        });

        // LOAD SAVED CITY
        String savedCity = getSavedCity();

        if (savedCity != null
                && !savedCity.isEmpty()) {

            getWeatherByCity(savedCity);

        } else {

            checkLocationPermission();
        }

        return view;
    }

    // ====================================
    // LOADING
    // ====================================

    private void showLoading(boolean show) {

        if (loadingLayout == null) return;

        if (show) {

            loadingLayout.setVisibility(View.VISIBLE);
            loadingLayout.setAlpha(1f);

        } else {

            loadingLayout.animate()
                    .alpha(0f)
                    .setDuration(250)
                    .withEndAction(() -> {

                        if (loadingLayout != null) {

                            loadingLayout.setVisibility(View.GONE);
                            loadingLayout.setAlpha(1f);
                        }
                    })
                    .start();
        }
    }

    // ====================================
    // RETROFIT
    // ====================================

    private void initRetrofit() {

        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl("https://api.openweathermap.org/")
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        )
                        .build();

        api = retrofit.create(WeatherApi.class);
    }

    // ====================================
    // LOCATION
    // ====================================

    private void checkLocationPermission() {

        if (!isAdded()) return;

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    LOCATION_REQUEST
            );

        } else {

            getWeatherByLocation();
        }
    }

    private void getWeatherByLocation() {

        try {

            if (!isAdded()) return;

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            LocationManager lm =
                    (LocationManager)
                            requireContext()
                                    .getSystemService(
                                            Context.LOCATION_SERVICE
                                    );

            if (lm == null) {

                getWeatherByCity("Lukavac");
                return;
            }

            Location loc =
                    lm.getLastKnownLocation(
                            LocationManager.GPS_PROVIDER
                    );

            if (loc == null) {

                loc = lm.getLastKnownLocation(
                        LocationManager.NETWORK_PROVIDER
                );
            }

            if (loc == null) {

                loc = lm.getLastKnownLocation(
                        LocationManager.PASSIVE_PROVIDER
                );
            }

            if (loc != null) {

                callApiByCoords(
                        loc.getLatitude(),
                        loc.getLongitude()
                );

            } else {

                getWeatherByCity("Lukavac");
            }

        } catch (Exception e) {

            e.printStackTrace();

            getWeatherByCity("Lukavac");
        }
    }

    // ====================================
    // WEATHER CITY
    // ====================================

    private void getWeatherByCity(String city) {

        Call<WeatherResponse> call =
                api.getWeather(
                        city,
                        API_KEY,
                        "metric",
                        "bs"
                );

        call.enqueue(new Callback<WeatherResponse>() {

            @Override
            public void onResponse(
                    Call<WeatherResponse> call,
                    Response<WeatherResponse> response
            ) {

                if (!isAdded()) return;

                showLoading(false);

                if (response.isSuccessful()
                        && response.body() != null) {

                    WeatherResponse data =
                            response.body();

                    saveCity(data.name);

                    updateWeatherUI(data);

                    getForecast(data.name);

                } else {

                    if (getContext() == null) return;

                    Toast.makeText(
                            getContext(),
                            "Grad nije pronađen",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<WeatherResponse> call,
                    Throwable t
            ) {

                if (!isAdded()) return;

                showLoading(false);

                if (getContext() == null) return;

                Toast.makeText(
                        getContext(),
                        "Greška API konekcije",
                        Toast.LENGTH_SHORT
                ).show();

                t.printStackTrace();
            }
        });
    }

    // ====================================
    // WEATHER GPS
    // ====================================

    private void callApiByCoords(
            double lat,
            double lon
    ) {

        Call<WeatherResponse> call =
                api.getWeatherByCoords(
                        lat,
                        lon,
                        API_KEY,
                        "metric",
                        "bs"
                );

        call.enqueue(new Callback<WeatherResponse>() {

            @Override
            public void onResponse(
                    Call<WeatherResponse> call,
                    Response<WeatherResponse> response
            ) {

                if (!isAdded()) return;

                showLoading(false);

                if (response.isSuccessful()
                        && response.body() != null) {

                    WeatherResponse data =
                            response.body();

                    updateWeatherUI(data);

                    getForecastByCoords(lat, lon);

                } else {

                    getWeatherByCity("Lukavac");
                }
            }

            @Override
            public void onFailure(
                    Call<WeatherResponse> call,
                    Throwable t
            ) {

                if (!isAdded()) return;

                showLoading(false);

                getWeatherByCity("Lukavac");

                t.printStackTrace();
            }
        });
    }

    // ====================================
    // UPDATE UI
    // ====================================

    private void updateWeatherUI(
            WeatherResponse data
    ) {

        try {

            if (!isAdded()) return;

            String locationName = "Nepoznata lokacija";

            if (data.name != null
                    && !data.name.isEmpty()) {

                locationName = data.name;
            }

            grad.setText(locationName);

            temp.setText(
                    Math.round(data.main.temp)
                            + "°C"
            );

            String weatherDesc = "Sunčano";
            String weatherIcon = "01d";

            if (data.weather != null
                    && data.weather.size() > 0) {

                weatherDesc =
                        data.weather.get(0).description;

                weatherIcon =
                        data.weather.get(0).icon;
            }

            if (weatherDesc != null
                    && !weatherDesc.isEmpty()) {

                opis.setText(
                        weatherDesc.substring(0, 1)
                                .toUpperCase()
                                + weatherDesc.substring(1)
                );

            } else {

                opis.setText("Sunčano");
            }

            String weatherUrl =
                    "https://openweathermap.org/img/wn/"
                            + weatherIcon
                            + "@4x.png";

            Glide.with(this)
                    .load(weatherUrl)
                    .placeholder(R.drawable.sunny_light)
                    .error(R.drawable.sunny_light)
                    .diskCacheStrategy(
                            DiskCacheStrategy.ALL
                    )
                    .into(icon);

            // HUMIDITY
            tvUv.setText(
                    data.main.humidity + "%"
            );

            // FEELS LIKE
            tvFeels.setText(
                    "Feels "
                            + Math.round(data.main.feelsLike)
                            + "°C"
            );

            // COUNTRY
            String countryCode = "ba";

            if (data.sys != null
                    && data.sys.country != null
                    && !data.sys.country.isEmpty()) {

                countryCode =
                        data.sys.country.toLowerCase();
            }

            String flagUrl =
                    "https://flagcdn.com/w80/"
                            + countryCode
                            + ".png";

            Glide.with(this)
                    .load(flagUrl)
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .diskCacheStrategy(
                            DiskCacheStrategy.ALL
                    )
                    .into(imgCountry);

            Locale locale =
                    new Locale(
                            "",
                            countryCode.toUpperCase()
                    );

            String countryName =
                    locale.getDisplayCountry(
                            Locale.getDefault()
                    );

            if (countryName == null
                    || countryName.isEmpty()) {

                countryName =
                        "Nepoznata država";
            }

            String today =
                    new SimpleDateFormat(
                            "dd MMM yyyy",
                            Locale.getDefault()
                    ).format(new Date());

            datum.setText(
                    countryName + " • " + today
            );

            // SAVE HOME WEATHER
            SharedPreferences prefs =
                    requireContext()
                            .getSharedPreferences(
                                    "HOME_WEATHER",
                                    Context.MODE_PRIVATE
                            );

            prefs.edit()

                    .putString(
                            "temp",
                            Math.round(data.main.temp)
                                    + "°C"
                    )

                    .putString(
                            "desc",
                            weatherDesc
                    )

                    .putString(
                            "humidity",
                            data.main.humidity + "%"
                    )

                    .putString(
                            "wind",
                            Math.round(data.wind.speed)
                                    + " km/h"
                    )

                    .putString(
                            "city",
                            locationName
                    )

                    .putString(
                            "country",
                            countryName
                    )

                    .putString(
                            "countryCode",
                            countryCode
                    )

                    .putString(
                            "icon",
                            weatherIcon
                    )

                    .apply();

            Intent intent =
                    new Intent(
                            "UPDATE_HOME_WEATHER"
                    );

            requireContext()
                    .sendBroadcast(intent);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ====================================
    // FORECAST
    // ====================================

    private void getForecast(String city) {

        Call<ForecastResponse> call =
                api.getForecast(
                        city,
                        API_KEY,
                        "metric",
                        "bs"
                );

        loadForecast(call);
    }

    private void getForecastByCoords(
            double lat,
            double lon
    ) {

        Call<ForecastResponse> call =
                api.getForecastByCoords(
                        lat,
                        lon,
                        API_KEY,
                        "metric",
                        "bs"
                );

        loadForecast(call);
    }

    private void loadForecast(
            Call<ForecastResponse> call
    ) {

        call.enqueue(new Callback<ForecastResponse>() {

            @Override
            public void onResponse(
                    Call<ForecastResponse> call,
                    Response<ForecastResponse> response
            ) {

                if (!isAdded()) return;

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().list != null) {

                    forecastContainer.removeAllViews();

                    for (int i = 0; i < 7; i++) {

                        int index = i * 8;

                        if (index >= response.body().list.size()) {
                            break;
                        }

                        ForecastResponse.ForecastItem item =
                                response.body().list.get(index);

                        View card =
                                LayoutInflater.from(getContext())
                                        .inflate(
                                                R.layout.item_forecast,
                                                forecastContainer,
                                                false
                                        );

                        TextView day =
                                card.findViewById(R.id.tvDay);

                        TextView desc =
                                card.findViewById(R.id.tvDesc);

                        TextView forecastTemp =
                                card.findViewById(
                                        R.id.tvForecastTemp
                                );

                        ImageView forecastIcon =
                                card.findViewById(
                                        R.id.ivForecast
                                );

                        try {

                            SimpleDateFormat input =
                                    new SimpleDateFormat(
                                            "yyyy-MM-dd HH:mm:ss",
                                            Locale.getDefault()
                                    );

                            SimpleDateFormat output =
                                    new SimpleDateFormat(
                                            "EEEE",
                                            new Locale("bs")
                                    );

                            Date d = input.parse(item.dtTxt);

                            if (d != null) {

                                String dan =
                                        output.format(d);

                                day.setText(
                                        dan.substring(0, 1)
                                                .toUpperCase()
                                                + dan.substring(1)
                                );
                            }

                        } catch (Exception e) {

                            day.setText("Dan");
                        }

                        if (item.weather != null
                                && item.weather.size() > 0) {

                            String descText =
                                    item.weather.get(0)
                                            .description;

                            if (descText != null
                                    && !descText.isEmpty()) {

                                desc.setText(
                                        descText.substring(0, 1)
                                                .toUpperCase()
                                                + descText.substring(1)
                                );

                            } else {

                                desc.setText("Vrijeme");
                            }

                            String iconCode =
                                    item.weather.get(0).icon;

                            String iconUrl =
                                    "https://openweathermap.org/img/wn/"
                                            + iconCode
                                            + "@4x.png";

                            Glide.with(VrijemeFragment.this)
                                    .load(iconUrl)
                                    .placeholder(R.drawable.sunny_light)
                                    .error(R.drawable.sunny_light)
                                    .diskCacheStrategy(
                                            DiskCacheStrategy.ALL
                                    )
                                    .into(forecastIcon);
                        }

                        forecastTemp.setText(
                                Math.round(item.main.temp)
                                        + "°C"
                        );

                        forecastContainer.addView(card);
                    }
                }
            }

            @Override
            public void onFailure(
                    Call<ForecastResponse> call,
                    Throwable t
            ) {

                t.printStackTrace();

                if (getContext() == null) return;

                Toast.makeText(
                        getContext(),
                        "Forecast greška",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    // ====================================
    // SAVE CITY
    // ====================================

    private void saveCity(String city) {

        SharedPreferences prefs =
                requireContext()
                        .getSharedPreferences(
                                PREFS_NAME,
                                Context.MODE_PRIVATE
                        );

        prefs.edit()
                .putString(KEY_CITY, city)
                .apply();
    }

    private String getSavedCity() {

        SharedPreferences prefs =
                requireContext()
                        .getSharedPreferences(
                                PREFS_NAME,
                                Context.MODE_PRIVATE
                        );

        return prefs.getString(
                KEY_CITY,
                null
        );
    }

    // ====================================
    // PERMISSION
    // ====================================

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == LOCATION_REQUEST) {

            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {

                getWeatherByLocation();

            } else {

                getWeatherByCity("Lukavac");
            }
        }
    }

    // ====================================
    // DESTROY
    // ====================================

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        forecastContainer = null;
        loadingLayout = null;
        api = null;
    }
}