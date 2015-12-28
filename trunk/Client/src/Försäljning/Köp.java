package Försäljning;

import Rabatter.Rabatt;

import Produkter.*;

import Rabatter.Kampanjer;

import Rabatter.RabattRegister;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class Köp implements Serializable {
    private List<Produktrad> produkter;
    private List<String> rabatter;
    private Date datum;
    private RabattRegister rabattRegister;

    public Köp(RabattRegister rabattRegister) {
        produkter = new ArrayList<Produktrad>();
        rabatter = new ArrayList<String>();
        datum = new Date();
        this.rabattRegister = rabattRegister;
    }

    public Produktrad getProduktrad(int index) {
        return produkter.get(index);
    }

    public double beräknaSumma() {
        return (summaUtanRabatt() - beräknaRabatter());
    }

    public double summaUtanRabatt() {
        double summa = 0.0;

        for (Produktrad rad : produkter)
            summa += rad.getProduktPris() * rad.getMängd();
        return summa;
    }

    public int getAntalProduktrader() {
        return produkter.size();
    }

    public Date getDatum() {
        return datum;
    }

    /**
     * @return true/false om det inte finns några varor.
     */
    public boolean ärTom() {
        return produkter.isEmpty();
    }

    public boolean köptProdukt(Produkt produkt) {
        for (Produktrad elem : produkter)
            if (elem.sammaProdukt(produkt))
                return true;
        return false;
    }

    private boolean använtRabatt(String rabattkod) {
        for (String elem : rabatter)
            if (elem.equals(rabattkod))
                return true;

        return false;
    }

    public int antalAvProdukt(Produkt produkt) {
        for (Produktrad elem : produkter)
            if (elem.sammaProdukt(produkt))
                return elem.getMängd();
        return 0;
    }
   

    public String skrivKvitto() {
        StringBuilder kvitto = new StringBuilder();
        double rabatt = beräknaRabatter(), summa = beräknaSumma();
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");

        kvitto.append(String.format("%62s", ("DATUM: " + date.format(datum) + "\n\n")));
        kvitto.append("VAROR\n" +
                String.format("%-6s %-30s %7s %7s %9s \n", "EAN-kod", "Beskrivning", "à pris", "Mängd", "pris"));

        // varor.
        for (Produktrad elem : produkter)
            kvitto.append(elem.kvittoRad());

        // rabatter
        if (rabatter.size() > 0) {
            kvitto.append(String.format("%66s", ("SUMMA: " + String.format("%.2f", summa + rabatt) + "kr\n\n")));

            kvitto.append("RABATTER\n");
            for (String elem : rabatter) {
                kvitto.append(rabattRegister.skrivUtRabatt(elem));
                kvitto.append(String.format("%66s",
                                            ("-" + String.format("%.2f", rabattRegister.rabattPåKöp(this, elem)) +
                                             "kr\n")));
            }

            kvitto.append(String.format("%66s", ("RABATT: -" + String.format("%.2f", rabatt) + "kr\n")));
        }

        kvitto.append(String.format("\n%66s", ("ATT BETALA:" + String.format("%.2f", summa) + "kr\n")));
        return kvitto.toString();
    }

    public Iterator getProduktrader() {
        return produkter.iterator();
    }

    public boolean utgångetDatum(Produkt produkt) {
        if (produkt instanceof Färskvaror)
            if (((Färskvaror)produkt).getDate().before(datum))
                return true;
        return false;
    }

    /**
     * Om produkten finns med i en produktrad uppdateras bara mängden (den gamla + den nya)
     * Är mängden lika med 0 tas produktraden med varan bort.
     * @param mängd
     * @param produkt
     */
    public void läggTillVara(int mängd, Produkt produkt) {

        if (!(this.köptProdukt(produkt))) {
            if (produkt instanceof Färskvaror) {
                Produktrad produktrad = new Produktrad(produkt, mängd);
                produkter.add(produktrad);
            } else {
                Produktrad produktrad = new Produktrad(produkt, mängd);
                produkter.add(produktrad);
            }
        } else {
            for (Produktrad rad : produkter) {
                if (rad.sammaProdukt(produkt)) {
                    rad.setMängd((rad.getMängd()) + mängd);

                    if (rad.getMängd() == 0)
                        taBortVara(produkt);
                    return;
                }
            }
        }

    }

    public boolean läggTillRabatt(String rabattkod) {
        if (rabattRegister.finnsRabatt(rabattkod) && !använtRabatt(rabattkod) &&
            rabattRegister.rabattPåKöp(this, rabattkod) > 0) {
            rabatter.add(rabattkod);
            return true;
        } else
            return false;
    }

    private double beräknaRabatter() {
        double summa = 0.0;

        for (String elem : rabatter)
            summa += rabattRegister.rabattPåKöp(this, elem);

        return summa;
    }

    public void taBortVara(Produkt produkt) {
        int i = 0;
        while (!(produkter.get(i).sammaProdukt(produkt)) && (i < produkter.size()))
            i++;

        if (i < produkter.size())
            produkter.remove(i);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Produktrad elem : produkter)
            stringBuilder.append(elem);

        return stringBuilder.toString();
    }
}
