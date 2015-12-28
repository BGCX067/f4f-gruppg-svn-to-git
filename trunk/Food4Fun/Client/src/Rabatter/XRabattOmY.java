package Rabatter;

import F�rs�ljning.K�p;

import Produkter.Produkt;

import Produkter.ProduktRegister;

import java.io.Serializable;

import java.util.Date;


public class XRabattOmY extends ProduktRabatt implements Serializable {
    private final int INDEX_PRODUKT = 0;
    private double prisX;
    private double summaY;
    
    public XRabattOmY(String beskrivning, String rabattkod, Produkt produktX, double prisX, double summaY, Date afd){
        super(afd, beskrivning, rabattkod);
        addProdukt(produktX);
        this.prisX = prisX;
        this.summaY = summaY;
    }

    public double ber�knaRabatt(K�p k�p) {
        double rabatt = 0.0;
        
        if( this.�rGiltig()){
            if(k�p.summaUtanRabatt() >= summaY && k�p.k�ptProdukt( getProdukt(INDEX_PRODUKT) ))
                rabatt = getProdukt(INDEX_PRODUKT).getPris() - prisX;
        }
        
        return rabatt;
    }
    
    public String toString() {
        return (super.toString() +
                ("K�p "+ getProdukt(INDEX_PRODUKT).getBeskrivning() + " f�r "+ prisX + "kr om du handlar f�r " + summaY +"kr.\n") );
    }
}