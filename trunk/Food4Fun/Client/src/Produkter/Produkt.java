package Produkter;

import java.io.Serializable;

public class Produkt implements Serializable {
    private String beskrinving = "";
    private int eanNummer;
    private double pris;

    public Produkt(int eanNummer, String beskrinving, double pris) {
        this.eanNummer = eanNummer;
        this.beskrinving = beskrinving;
        this.pris = pris;
    }

    public void setBeskrivning(String besk) {
        beskrinving = besk;
    }

    public String getBeskrivning() {
        return beskrinving;
    }

    public void setEaNo(int eanNummer) {
        this.eanNummer = eanNummer;
    }

    public int getEaNo() {
        return eanNummer;
    }

    public void setPris(double pris) {
        this.pris = pris;
    }

    public double getPris() {
        return pris;
    }

    public String toString() {
        return String.format("%6d  %-30s %7.2f", eanNummer, beskrinving, pris);
    }
}
