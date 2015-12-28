package Produkter;

import java.io.Serializable;

import java.util.*;
/*
 * ProduktRegistret sk�ter hanteringen av alla produkter.
 * Produkt registret har metoder som till�ter clienten att l�gga till en produkt,
 * ta bort en produkt, h�mta en produkt, leta efter en specifik produkt med samma EAN Nummer,
 * �ven returnera hela Listan med produkter.
 */
public class ProduktRegister implements Serializable {
    private List<Produkt> produkter;

    public ProduktRegister() {
        produkter = new ArrayList<Produkt>();
    }

    public void addProdukt(Produkt produkt) {
        produkter.add(produkt);
    }

    public Iterator getProduktRegister() {
        return produkter.iterator();
    }

    public void taBortProdukt(int ean) {
        for (int index = 0; index < produkter.size(); index++) {
            if (ean == produkter.get(index).getEaNo())
                produkter.remove(index);
        }
    }

    public Produkt getProdukt(int ean) {
        for (int i = 0; i < produkter.size(); i++) {
            if (ean == produkter.get(i).getEaNo()) {
                return (produkter.get(i));
            }
        }
        return null;
    }

    public boolean checkEAN(int ean) {
        for (int index = 0; index < produkter.size(); index++) {
            if (ean == produkter.get(index).getEaNo())
                return true;
        }
        return false;
    }
}
