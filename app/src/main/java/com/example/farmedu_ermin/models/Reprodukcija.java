package com.example.farmedu_ermin.models;

public class Reprodukcija {

    private String polnaZrelost;
    private String gestacija;
    private String brojMladunaca;
    private String interval;
    private String sezonaParenja;

    public Reprodukcija(String polnaZrelost, String gestacija,
                        String brojMladunaca, String interval,
                        String sezonaParenja) {

        this.polnaZrelost = polnaZrelost;
        this.gestacija = gestacija;
        this.brojMladunaca = brojMladunaca;
        this.interval = interval;
        this.sezonaParenja = sezonaParenja;
    }
}