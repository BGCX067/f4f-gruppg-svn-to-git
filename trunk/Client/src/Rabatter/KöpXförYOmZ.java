package Rabatter;

import Försäljning.Köp;

import Produkter.Produkt;

import java.io.Serializable;

import java.util.Date;


public class KöpXförYOmZ extends ProduktRabatt implements Serializable {
    final int INDEX_PRODUKTX = 0,
              INDEX_PRODUKTZ = 1;
    private double prisY;

    public KöpXförYOmZ(String beskrivning, String rabattkod, Produkt produktX, Produkt produktZ, double prisY,
                       Date afd) {
        super(afd, beskrivning, rabattkod);
        addProdukt(produktX);
        addProdukt(produktZ);
        this.prisY = prisY;
    }

    public double beräknaRabatt(Köp köp) {
        double rabatt = 0.0;
        
        if( this.ärGiltig() ){
            if(köp.köptProdukt( getProdukt(INDEX_PRODUKTZ) ) && köp.köptProdukt(getProdukt(INDEX_PRODUKTX)))
                rabatt = köp.antalAvProdukt(getProdukt(INDEX_PRODUKTX)) * (getProdukt(INDEX_PRODUKTX).getPris() - prisY);
        }
        
        return rabatt;
    }

    public String toString() {
        return (super.toString() +
                ("Köp "+ getProdukt(INDEX_PRODUKTX).getBeskrivning() + " för "+ prisY + "kr om du även köper " +
                 getProdukt(INDEX_PRODUKTZ).getBeskrivning() +"\n") );
    }
}
