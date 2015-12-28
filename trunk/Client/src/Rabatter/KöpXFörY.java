package Rabatter;

import Försäljning.Köp;

import Produkter.Produkt;

import java.io.Serializable;

import java.util.Date;


public class KöpXFörY extends ProduktRabatt implements Serializable {
    final int INDEX_PRODUKT = 0;
    private int antalX;
    private int antalY;

    public KöpXFörY(String beskrivning, String rabattkod, Produkt produktX, int antalX, int antalY, Date afd) {
        super(afd, beskrivning, rabattkod);
        addProdukt(produktX);
        this.antalX = antalX;
        this.antalY = antalY;
    }

    public double beräknaRabatt(Köp köp) {
        double rabatt = 0.0;

        if (this.ärGiltig()) {
            if (köp.köptProdukt(getProdukt(INDEX_PRODUKT)) &&
                köp.antalAvProdukt(getProdukt(INDEX_PRODUKT)) >= antalX) {
                rabatt = ((int)köp.antalAvProdukt(getProdukt(INDEX_PRODUKT)) / antalX) * (antalX - antalY);
                rabatt *= getProdukt(INDEX_PRODUKT).getPris();
            }
        }

        return rabatt;
    }

    public String toString() {
        return (super.toString() +
                (antalX + getProdukt(INDEX_PRODUKT).getBeskrivning() + " för priset av " + antalY + "\n"));
    }
}
