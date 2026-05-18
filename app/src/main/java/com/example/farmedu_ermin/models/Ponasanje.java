package com.example.farmedu_ermin.models;

public class Ponasanje {

    private String temperament;
    private String inteligencija;
    private String socijalnoPonasanje;
    private String odnosPremaLjudima;
    private String odnosPremaDjeci;
    private String odnosPremaZivotinjama;
    private String nivoEnergije;
    private String lakocaTreniranja;
    private String instinkti;

    public Ponasanje(String temperament, String inteligencija, String socijalnoPonasanje,
                     String odnosPremaLjudima, String odnosPremaDjeci,
                     String odnosPremaZivotinjama, String nivoEnergije,
                     String lakocaTreniranja, String instinkti) {

        this.temperament = temperament;
        this.inteligencija = inteligencija;
        this.socijalnoPonasanje = socijalnoPonasanje;
        this.odnosPremaLjudima = odnosPremaLjudima;
        this.odnosPremaDjeci = odnosPremaDjeci;
        this.odnosPremaZivotinjama = odnosPremaZivotinjama;
        this.nivoEnergije = nivoEnergije;
        this.lakocaTreniranja = lakocaTreniranja;
        this.instinkti = instinkti;
    }

    public String getTemperament() { return temperament; }
    public String getInteligencija() { return inteligencija; }
    public String getSocijalnoPonasanje() { return socijalnoPonasanje; }
    public String getOdnosPremaLjudima() { return odnosPremaLjudima; }
    public String getOdnosPremaDjeci() { return odnosPremaDjeci; }
    public String getOdnosPremaZivotinjama() { return odnosPremaZivotinjama; }
    public String getNivoEnergije() { return nivoEnergije; } // 🔥 OVO TI FALI
    public String getLakocaTreniranja() { return lakocaTreniranja; }
    public String getInstinkti() { return instinkti; }
}