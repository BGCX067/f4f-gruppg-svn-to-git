package F�rs�ljning;

import Rabatter.Rabatt;

import Produkter.*;

import Rabatter.RabattRegister;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/*
 * K�p representerar ett k�ptillf�lle, med ett datum, ett antal
 * produktrader och ett antal rabatter.
 * 
 * Den har metoder som ber�knar summan, ber�knar summan utan att ta med rabatterna i
 * ber�kningen och ber�knar den totalla rabatten p� k�pet.
 * 
 * Man l�gga till/ta bort en produktrad, l�gga till en rabatt och f� ut
 * en formatterad str�ng f�r kvittot eller en str�ng f�r raden f�r en viss produkt.
 * 
 * Det g�r att kolla om man redan anv�nt en rabatt eller om det redan finns en
 * produktrad med en viss produkt med i k�pet.
 */

public class K�p implements Serializable {
    private List<Produktrad> produkter;
    private List<String> rabatter;
    private Date datum;
    private RabattRegister rabattRegister;

    public K�p(RabattRegister rabattRegister) {
        produkter = new ArrayList<Produktrad>();
        rabatter = new ArrayList<String>();
        datum = new Date();
        this.rabattRegister = rabattRegister;
    }

    public Produktrad getProduktrad(int index) {
        return produkter.get(index);
    }

    public double ber�knaSumma() {
        return (summaUtanRabatt() - ber�knaRabatter());
    }

    public double summaUtanRabatt() {
        double summa = 0.0;

        for (Produktrad rad : produkter)
            summa += rad.getProduktPris() * rad.getM�ngd();
        return summa;
    }

    public int getAntalProduktrader() {
        return produkter.size();
    }

    public Date getDatum() {
        return datum;
    }

    /**
     * @return true/false om det inte finns n�gra varor.
     */
    public boolean �rTom() {
        return produkter.isEmpty();
    }

    public boolean k�ptProdukt(Produkt produkt) {
        for (Produktrad elem : produkter)
            if (elem.sammaProdukt(produkt))
                return true;
        return false;
    }

    public boolean anv�ntRabatt(String rabattkod) {
        for (String elem : rabatter)
            if (elem.equals(rabattkod))
                return true;

        return false;
    }

    public int antalAvProdukt(Produkt produkt) {
        for (Produktrad elem : produkter)
            if (elem.sammaProdukt(produkt))
                return elem.getM�ngd();
        return 0;
    }
    // inte helt 100% att den ska ligga h�r.
    public String skrivKvitto() {
        StringBuilder kvitto = new StringBuilder();
        double rabatt =  ber�knaRabatter(), summa = ber�knaSumma();
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");

        kvitto.append(String.format( "%62s", ("DATUM: " + date.format(datum) +"\n\n") ));
        kvitto.append("VAROR\n" +
                      String.format("%-6s %-30s %7s %7s %9s \n", "EAN-kod", "Beskrivning", "� pris", "M�ngd", "pris"));

        // varor.
        for(Produktrad elem : produkter)
            kvitto.append( elem.kvittoRad() );

        // rabatter
        if(rabatter.size() > 0){
            kvitto.append(String.format("%66s",("SUMMA: " + String.format("%.2f", summa+rabatt) + "kr\n\n")));

            kvitto.append("RABATTER\n");
            for(String elem : rabatter){
                kvitto.append(rabattRegister.skrivUtRabatt(elem));
                kvitto.append( String.format("%66s", ("-" + String.format( "%.2f", rabattRegister.rabattP�K�p(this, elem) ) + "kr\n") ));
            }

            kvitto.append(String.format( "%66s",("RABATT: -" + String.format("%.2f" ,rabatt) + "kr\n") ));
        }

        kvitto.append(String.format( "\n%66s", ("ATT BETALA:" + String.format("%.2f", summa) + "kr\n") ));
        return kvitto.toString();
    }
    

    public String h�mtaProduktrad(Produkt produkt) {
        int i = 0;
        while (!(produkter.get(i).equals(produkt)) && (i < produkter.size()))
            i++;

        if (i < produkter.size())
            return produkter.get(i) + "";

        else {
            System.out.println("Ingen " + produkt + "finns registrerad i k�pet.\n");
            return "";
        }
    }

    //ska funka lika bra med toString.
    public Iterator getProduktrader() {
        return produkter.iterator();
    }
    /*public void skrivUtProduktrader() {
        Iterator iter = produkter.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }*/
    public boolean utg�ngetDatum(Produkt produkt) {
        if (produkt instanceof F�rskvaror)
            if (((F�rskvaror)produkt).getDate().before(datum)) 
                return true;
        return false; 
    }
    
    /*
     * Om produkten finns med i en produktrad uppdateras bara m�ngden (den gamla + den nya)
     * �r m�ngden lika med 0 tas produktraden med varan bort.
     */
    public void l�ggTillVara(int m�ngd, Produkt produkt) {
        Date datum = new Date();
        if (!(this.k�ptProdukt(produkt))) {
            if (produkt instanceof F�rskvaror) {
                /*if (((F�rskvaror)produkt).getDate().before(datum)) {
                    System.out.println("Produktens b�st f�re datum har g�tt ut.\n" +
                            "Produkterna registreras inte");
                } else {//*/
                    Produktrad produktrad = new Produktrad(produkt, m�ngd);
                    produkter.add(produktrad);
                //}
            } else {
                Produktrad produktrad = new Produktrad(produkt, m�ngd);
                produkter.add(produktrad);
            }
        } else {
            for (Produktrad rad : produkter) {
                if (rad.sammaProdukt(produkt)) {
                    rad.setM�ngd((rad.getM�ngd()) + m�ngd);

                    if (rad.getM�ngd() == 0)
                        taBortVara(produkt);
                    return;
                }
            }
        }

    }

    public boolean l�ggTillRabatt(String rabattkod) {
        if (rabattRegister.finnsRabatt(rabattkod) && !anv�ntRabatt(rabattkod) &&
            rabattRegister.rabattP�K�p(this, rabattkod) > 0) {
            rabatter.add(rabattkod);
            return true;
        } else
            return false;
    }

    private double ber�knaRabatter() {
        double summa = 0.0;

        for (String elem : rabatter)
            summa += rabattRegister.rabattP�K�p(this, elem);

        return summa;
    }

    public void taBortVara(Produkt produkt) {
        int i = 0;
        while (!(produkter.get(i).sammaProdukt(produkt)) && (i < produkter.size()))
            i++;

        if (i < produkter.size())
            produkter.remove(i);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Produktrad elem : produkter)
            stringBuilder.append(elem);

        return stringBuilder.toString();
    }
}
