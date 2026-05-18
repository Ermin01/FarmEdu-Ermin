package com.example.farmedu_ermin.models;

public class Poljoprivrednemasine {

    private String naslov;
    private int slika;
    private String opis;

    public Poljoprivrednemasine(String naslov, int slika, String opis) {
        this.naslov = naslov;
        this.slika = slika;
        this.opis = opis;
    }

    public String getNaslov() {
        return naslov;
    }

    public int getSlika() {
        return slika;
    }

    public String getOpis() {
        return opis;
    }
}