package F�rs�ljning;

import Produkter.Produkt;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

public class Produktrad implements Serializable {
    private Produkt produkt;
    private int m�ngd;
    private Date dagensDatum;

    public Produktrad(Produkt produkt, int m�ngd) {
        this.produkt = produkt;
        this.m�ngd = m�ngd;
        this.dagensDatum = new Date();
    }

    private String getDagensDatum() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return (dateFormat.format(dagensDatum));

    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    public void setM�ngd(int m�ngd) {
        this.m�ngd = m�ngd;
    }

    public int getM�ngd() {
        return m�ngd;
    }

    public double getProduktPris() {
        return produkt.getPris();
    }

    public String kvittoRad() {
        return String.format("%6d  %-30s %7.2f %7d %9.2f \n", produkt.getEaNo(), produkt.getBeskrivning(),
                             produkt.getPris(), m�ngd, (produkt.getPris() * m�ngd));
    }

    @Override
    public String toString() {
        return String.format("%6d  %-30s %7.2f %7d %9.2f \n", produkt.getEaNo(), produkt.getBeskrivning(), produkt.getPris(),
                             m�ngd, (produkt.getPris() * m�ngd));
    }
    /*public String toString() {
        return String.format("%-6s %-30s %7s %7s %9s \n%6d  %-30s %7.2f %7d %9.2f \n", "EAN-kod", "Beskrivning",
                             "� pris", "M�ngd", "pris", produkt.getEaNo(), produkt.getBeskrivning(), produkt.getPris(),
                             m�ngd, (produkt.getPris() * m�ngd));
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
