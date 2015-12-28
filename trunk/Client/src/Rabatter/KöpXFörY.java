package Rabatter;

import F�rs�ljning.K�p;

import Produkter.Produkt;

import java.io.Serializable;

import java.util.Date;


public class K�pXF�rY extends ProduktRabatt implements Serializable {
    final int INDEX_PRODUKT = 0;
    private int antalX;
    private int antalY;

    public K�pXF�rY(String beskrivning, String rabattkod, Produkt produktX, int antalX, int antalY, Date afd) {
        super(afd, beskrivning, rabattkod);
        addProdukt(produktX);
        this.antalX = antalX;
        this.antalY = antalY;
    }

    public double ber�knaRabatt(K�p k�p) {
        double rabatt = 0.0;

        if (this.�rGiltig()) {
            if (k�p.k�ptProdukt(getProdukt(INDEX_PRODUKT)) &&
                k�p.antalAvProdukt(getProdukt(INDEX_PRODUKT)) >= antalX) {
                rabatt = ((int)k�p.antalAvProdukt(getProdukt(INDEX_PRODUKT)) / antalX) * (antalX - antalY);
                rabatt *= getProdukt(INDEX_PRODUKT).getPris();
            }
        }

        return rabatt;
    }

    public String toString() {
        return (super.toString() +
                (antalX + getProdukt(INDEX_PRODUKT).getBeskrivning() + " f�r priset av " + antalY + "\n"));
    }
}
