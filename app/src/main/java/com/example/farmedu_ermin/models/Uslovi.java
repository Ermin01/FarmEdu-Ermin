package com.example.farmedu_ermin.models;

public class Uslovi {

    private String prostor;
    private String minimalni;
    private String temperatura;
    private String potrebaProstora;
    private String sigurnost;

    public Uslovi(String prostor, String minimalni,
                  String temperatura, String potrebaProstora, String sigurnost) {

        this.prostor = prostor;
        this.minimalni = minimalni;
        this.temperatura = temperatura;
        this.potrebaProstora = potrebaProstora;
        this.sigurnost = sigurnost;
    }
}