package com.example.farmedu_ermin.models;

public class Vakcinacija {

    private String obavezne;
    private String preporucene;
    private String zadnjaVakcinacija;
    private String plan;
    private String paraziti;
    private String pregledi;
    private String sterilizacija;
    private String terapije;

    public Vakcinacija(String obavezne, String preporucene,
                       String zadnjaVakcinacija, String plan,
                       String paraziti, String pregledi,
                       String sterilizacija, String terapije) {

        this.obavezne = obavezne;
        this.preporucene = preporucene;
        this.zadnjaVakcinacija = zadnjaVakcinacija;
        this.plan = plan;
        this.paraziti = paraziti;
        this.pregledi = pregledi;
        this.sterilizacija = sterilizacija;
        this.terapije = terapije;
    }
}