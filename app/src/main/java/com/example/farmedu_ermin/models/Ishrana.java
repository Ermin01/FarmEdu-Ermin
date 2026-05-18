package com.example.farmedu_ermin.models;

public class Ishrana {

    private String tip;
    private String dnevniUnos;
    private String vrstaHrane;
    private String dijeta;
    private String alergije;
    private String voda;

    public Ishrana(String tip, String dnevniUnos, String vrstaHrane,
                   String dijeta, String alergije, String voda) {

        this.tip = tip;
        this.dnevniUnos = dnevniUnos;
        this.vrstaHrane = vrstaHrane;
        this.dijeta = dijeta;
        this.alergije = alergije;
        this.voda = voda;
    }


    public String getTip() { return tip; } // 🔥
    public String getDnevniUnos() { return dnevniUnos; } // 🔥
    public String getVrstaHrane() { return vrstaHrane; }
    public String getDijeta() { return dijeta; }
    public String getAlergije() { return alergije; }
    public String getVoda() { return voda; }
}