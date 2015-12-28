package Rabatter;

import F�rs�ljning.K�p;

import Produkter.Produkt;

import java.io.Serializable;

import java.util.Date;


public class K�pXf�rYOmZ extends ProduktRabatt implements Serializable {
    final int INDEX_PRODUKTX = 0,
              INDEX_PRODUKTZ = 1;
    private double prisY;

    public K�pXf�rYOmZ(String beskrivning, String rabattkod, Produkt produktX, Produkt produktZ, double prisY,
                       Date afd) {
        super(afd, beskrivning, rabattkod);
        addProdukt(produktX);
        addProdukt(produktZ);
        this.prisY = prisY;
    }

    public double ber�knaRabatt(K�p k�p) {
        double rabatt = 0.0;
        
        if( this.�rGiltig() ){
            if(k�p.k�ptProdukt( getProdukt(INDEX_PRODUKTZ) ) && k�p.k�ptProdukt(getProdukt(INDEX_PRODUKTX)))
                rabatt = k�p.antalAvProdukt(getProdukt(INDEX_PRODUKTX)) * (getProdukt(INDEX_PRODUKTX).getPris() - prisY);
        }
        
        return rabatt;
    }

    public String toString() {
        return (super.toString() +
                ("K�p "+ getProdukt(INDEX_PRODUKTX).getBeskrivning() + " f�r "+ prisY + "kr om du �ven k�per " +
                 getProdukt(INDEX_PRODUKTZ).getBeskrivning() +"\n") );
    }
}
