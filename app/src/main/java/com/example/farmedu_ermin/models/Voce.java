package com.example.farmedu_ermin.models;

import java.io.Serializable;

public class Voce implements Serializable {

    private String naslov;
    private int slika;
    private String opis;
    private String vrijemeSadnje;
    private String vrijemeBerbe;
    private String zalijevanje;
    private String sunce;
    private String temperatura;
    private String tipTla;
    private String prinos;

    public Voce(String naslov, int slika, String opis,
                String vrijemeSadnje, String vrijemeBerbe,
                String zalijevanje, String sunce,
                String temperatura, String tipTla, String prinos) {

        this.naslov = naslov;
        this.slika = slika;
        this.opis = opis;
        this.vrijemeSadnje = vrijemeSadnje;
        this.vrijemeBerbe = vrijemeBerbe;
        this.zalijevanje = zalijevanje;
        this.sunce = sunce;
        this.temperatura = temperatura;
        this.tipTla = tipTla;
        this.prinos = prinos;
    }

    public String getNaslov() { return naslov; }
    public int getSlika() { return slika; }
    public String getOpis() { return opis; }
    public String getVrijemeSadnje() { return vrijemeSadnje; }
    public String getVrijemeBerbe() { return vrijemeBerbe; }
    public String getZalijevanje() { return zalijevanje; }
    public String getSunce() { return sunce; }
    public String getTemperatura() { return temperatura; }
    public String getTipTla() { return tipTla; }
    public String getPrinos() { return prinos; }
}