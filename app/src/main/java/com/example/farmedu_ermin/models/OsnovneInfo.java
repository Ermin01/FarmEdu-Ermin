package com.example.farmedu_ermin.models;

public class OsnovneInfo {

    private String zivotniVijek;
    private String visina;
    private String tezina;
    private String porijeklo;
    private String brzina;
    private String tipDlake;
    private String snaga;

    public OsnovneInfo(String zivotniVijek, String visina, String tezina,
                       String porijeklo, String brzina, String tipDlake, String snaga) {
        this.zivotniVijek = zivotniVijek;
        this.visina = visina;
        this.tezina = tezina;
        this.porijeklo = porijeklo;
        this.brzina = brzina;
        this.tipDlake = tipDlake;
        this.snaga = snaga;
    }

    public String getZivotniVijek() { return zivotniVijek; }
    public String getVisina() { return visina; }
    public String getTezina() { return tezina; }
    public String getPorijeklo() { return porijeklo; }
    public String getBrzina() { return brzina; }
    public String getTipDlake() { return tipDlake; }
    public String getSnaga() { return snaga; }
}