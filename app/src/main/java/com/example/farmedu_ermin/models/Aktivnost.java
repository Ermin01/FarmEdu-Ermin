package com.example.farmedu_ermin.models;

public class Aktivnost {

    private String kretanje;
    private String vrsta;
    private String trening;
    private String radneSposobnosti;
    private String sport;

    public Aktivnost(String kretanje, String vrsta,
                     String trening, String radneSposobnosti, String sport) {

        this.kretanje = kretanje;
        this.vrsta = vrsta;
        this.trening = trening;
        this.radneSposobnosti = radneSposobnosti;
        this.sport = sport;
    }
    public String getKretanje() { return kretanje; } // 🔥
    public String getVrsta() { return vrsta; }
    public String getTrening() { return trening; } // 🔥
    public String getRadneSposobnosti() { return radneSposobnosti; }
    public String getSport() { return sport; }
}