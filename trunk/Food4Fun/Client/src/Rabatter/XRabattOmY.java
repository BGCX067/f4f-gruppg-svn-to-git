package Rabatter;

import Försäljning.Köp;

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

    public double beräknaRabatt(Köp köp) {
        double rabatt = 0.0;
        
        if( this.ärGiltig()){
            if(köp.summaUtanRabatt() >= summaY && köp.köptProdukt( getProdukt(INDEX_PRODUKT) ))
                rabatt = getProdukt(INDEX_PRODUKT).getPris() - prisX;
        }
        
        return rabatt;
    }
    
    public String toString() {
        return (super.toString() +
                ("Köp "+ getProdukt(INDEX_PRODUKT).getBeskrivning() + " för "+ prisX + "kr om du handlar för " + summaY +"kr.\n") );
    }
}