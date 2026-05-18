package com.example.farmedu_ermin.models;

import java.io.Serializable;

public class ZacinskoBilje implements Serializable {

    private String naslov;
    private int slika;
    private String opis;
    private String vrijemeSadnje;
    private String zalijevanje;
    private String sunce;
    private String temperatura;
    private String tipTla;
    private String prinos;
    private String vrijemeBerbe;

    public ZacinskoBilje(String naslov, int slika, String opis,
                         String vrijemeSadnje, String zalijevanje,
                         String sunce, String temperatura,
                         String tipTla, String prinos, String vrijemeBerbe) {

        this.naslov = naslov;
        this.slika = slika;
        this.opis = opis;
        this.vrijemeSadnje = vrijemeSadnje;
        this.zalijevanje = zalijevanje;
        this.sunce = sunce;
        this.temperatura = temperatura;
        this.tipTla = tipTla;
        this.prinos = prinos;
        this.vrijemeBerbe = vrijemeBerbe;
    }

    public String getNaslov() { return naslov; }
    public int getSlika() { return slika; }
    public String getOpis() { return opis; }
    public String getVrijemeSadnje() { return vrijemeSadnje; }
    public String getZalijevanje() { return zalijevanje; }
    public String getSunce() { return sunce; }
    public String getTemperatura() { return temperatura; }
    public String getTipTla() { return tipTla; }
    public String getPrinos() { return prinos; }
    public String getVrijemeBerbe() { return vrijemeBerbe; }
}