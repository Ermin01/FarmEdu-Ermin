package com.example.farmedu_ermin.models;

import java.io.Serializable;

public class Povrce implements Serializable {

    private String naslov;
    private int slika;
    private String opis;
    private String vrijemeSadnje;
    private String zalijevanje;
    private String sunce;

    // 🔥 KONSTRUKTOR
    public Povrce(String naslov, int slika, String opis,
                  String vrijemeSadnje, String zalijevanje, String sunce) {
        this.naslov = naslov;
        this.slika = slika;
        this.opis = opis;
        this.vrijemeSadnje = vrijemeSadnje;
        this.zalijevanje = zalijevanje;
        this.sunce = sunce;
    }

    // ✅ GETTERI
    public String getNaslov() {
        return naslov;
    }

    public int getSlika() {
        return slika;
    }

    public String getOpis() {
        return opis;
    }

    public String getVrijemeSadnje() {
        return vrijemeSadnje;
    }

    public String getZalijevanje() {
        return zalijevanje;
    }

    public String getSunce() {
        return sunce;
    }
}