package com.example.farmedu_ermin.models;


import java.util.List;

public class LekcijaModel {
    public String naslov;
    public String opis;
    public List<Sekcija> sekcije;

    public static class Sekcija {
        public String tip;
        public String naslov;
        public String sadrzaj;
        public List<String> stavke;
    }
}
