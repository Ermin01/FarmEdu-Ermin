package com.example.farmedu_ermin.models;

import java.io.Serializable;

public class Zivotinja implements Serializable {

    private String ime;
    private int slika;
    private String kategorija;

    private OsnovneInfo osnovneInfo;
    private Ponasanje ponasanje;
    private Zdravlje zdravlje;
    private Vakcinacija vakcinacija;
    private Reprodukcija reprodukcija;
    private Ishrana ishrana;
    private Aktivnost aktivnost;
    private Njega njega;
    private Uslovi uslovi;

    // 🔥 PRAZAN KONSTRUKTOR (VAŽNO za JSON)
    public Zivotinja() {}

    // 🔥 FULL konstruktor
    public Zivotinja(String ime, int slika, String kategorija,
                     OsnovneInfo osnovneInfo,
                     Ponasanje ponasanje,
                     Zdravlje zdravlje,
                     Vakcinacija vakcinacija,
                     Reprodukcija reprodukcija,
                     Ishrana ishrana,
                     Aktivnost aktivnost,
                     Njega njega,
                     Uslovi uslovi) {

        this.ime = ime;
        this.slika = slika;
        this.kategorija = kategorija;
        this.osnovneInfo = osnovneInfo;
        this.ponasanje = ponasanje;
        this.zdravlje = zdravlje;
        this.vakcinacija = vakcinacija;
        this.reprodukcija = reprodukcija;
        this.ishrana = ishrana;
        this.aktivnost = aktivnost;
        this.njega = njega;
        this.uslovi = uslovi;
    }

    // ✅ jednostavni konstruktor
    public Zivotinja(String ime, int slika, String kategorija) {
        this.ime = ime;
        this.slika = slika;
        this.kategorija = kategorija;
    }

    // 🔹 GETTERI
    public String getIme() { return ime; }
    public int getSlika() { return slika; }
    public String getKategorija() { return kategorija; }

    public OsnovneInfo getOsnovneInfo() { return osnovneInfo; }
    public Ponasanje getPonasanje() { return ponasanje; }
    public Zdravlje getZdravlje() { return zdravlje; }
    public Vakcinacija getVakcinacija() { return vakcinacija; }
    public Reprodukcija getReprodukcija() { return reprodukcija; }
    public Ishrana getIshrana() { return ishrana; }
    public Aktivnost getAktivnost() { return aktivnost; }
    public Njega getNjega() { return njega; }
    public Uslovi getUslovi() { return uslovi; }

    // 🔹 SETTERI (BITNI!)
    public void setIme(String ime) { this.ime = ime; }
    public void setSlika(int slika) { this.slika = slika; }
    public void setKategorija(String kategorija) { this.kategorija = kategorija; }

    public void setOsnovneInfo(OsnovneInfo osnovneInfo) { this.osnovneInfo = osnovneInfo; }
    public void setPonasanje(Ponasanje ponasanje) { this.ponasanje = ponasanje; }
    public void setZdravlje(Zdravlje zdravlje) { this.zdravlje = zdravlje; }
    public void setVakcinacija(Vakcinacija vakcinacija) { this.vakcinacija = vakcinacija; }
    public void setReprodukcija(Reprodukcija reprodukcija) { this.reprodukcija = reprodukcija; }
    public void setIshrana(Ishrana ishrana) { this.ishrana = ishrana; }
    public void setAktivnost(Aktivnost aktivnost) { this.aktivnost = aktivnost; }
    public void setNjega(Njega njega) { this.njega = njega; }
    public void setUslovi(Uslovi uslovi) { this.uslovi = uslovi; }
}