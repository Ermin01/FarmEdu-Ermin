package com.example.farmedu_ermin.models;

public class Zdravlje {

    private String prosjecnaStarost;
    private String bolesti;
    private String genetika;
    private String otpornost;
    private String osjetljivost;

    public Zdravlje(String prosjecnaStarost, String bolesti,
                    String genetika, String otpornost, String osjetljivost) {

        this.prosjecnaStarost = prosjecnaStarost;
        this.bolesti = bolesti;
        this.genetika = genetika;
        this.otpornost = otpornost;
        this.osjetljivost = osjetljivost;
    }

    public String getProsjecnaStarost() { return prosjecnaStarost; } // 🔥
    public String getBolesti() { return bolesti; } // 🔥
    public String getGenetika() { return genetika; }
    public String getOtpornost() { return otpornost; }
    public String getOsjetljivost() { return osjetljivost; }
}