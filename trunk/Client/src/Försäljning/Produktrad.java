package Försäljning;

import Produkter.Produkt;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

public class Produktrad implements Serializable {
    private Produkt produkt;
    private int mängd;
    private Date dagensDatum;

    public Produktrad(Produkt produkt, int mängd) {
        this.produkt = produkt;
        this.mängd = mängd;
        this.dagensDatum = new Date();
    }

    private String getDagensDatum() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return (dateFormat.format(dagensDatum));

    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    public void setMängd(int mängd) {
        this.mängd = mängd;
    }

    public int getMängd() {
        return mängd;
    }

    public double getProduktPris() {
        return produkt.getPris();
    }

    public String kvittoRad() {
        return String.format("%6d  %-30s %7.2f %7d %9.2f \n", produkt.getEaNo(), produkt.getBeskrivning(),
                             produkt.getPris(), mängd, (produkt.getPris() * mängd));
    }

    @Override
    public String toString() {
        return String.format("%6d  %-30s %7.2f %7d %9.2f \n", produkt.getEaNo(), produkt.getBeskrivning(), produkt.getPris(),
                             mängd, (produkt.getPris() * mängd));
    }
    /*public String toString() {
        return String.format("%-6s %-30s %7s %7s %9s \n%6d  %-30s %7.2f %7d %9.2f \n", "EAN-kod", "Beskrivning",
                             "à pris", "Mängd", "pris", produkt.getEaNo(), produkt.getBeskrivning(), produkt.getPris(),
                             mängd, (produkt.getPris() * mängd));
    }//*/

    public boolean sammaProdukt(Produkt p) {
        return (this.produkt == p);
    }

    public boolean equals(Object o) {
        if (o instanceof Produktrad) {
            return (this.produkt == ((Produktrad)o).produkt);
        } else
            return false;
    }
}
