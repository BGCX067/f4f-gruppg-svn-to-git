package Rabatter;

import Försäljning.Köp;

import Försäljning.Produktrad;

import Produkter.Produkt;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;

/*
 * En abstrakt klass för rabatter som har något att göra med olika produkter.
 */

abstract class ProduktRabatt extends Rabatt implements Serializable {
    private ArrayList<Produkt> produkter;
    
    protected ProduktRabatt(Date afd, String beskrivning, String rabattkod) {
        super(afd, beskrivning, rabattkod);
        produkter = new ArrayList<Produkt>(2);
    }
    
    protected void addProdukt(Produkt p){
            produkter.add(p);
    }
    
    protected Produkt getProdukt(int index){
        if(index < produkter.size()) 
            return produkter.get(index);
        else return null;
    }
    
    protected boolean removeProdukt(int index){
        if(index < produkter.size()){ 
            produkter.remove(index);
            return true;
        } else return false;
    }
    
    public abstract double beräknaRabatt(Köp köp);
}