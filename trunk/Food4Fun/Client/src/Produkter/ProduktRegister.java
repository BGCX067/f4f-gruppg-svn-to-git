package Produkter;

import java.io.Serializable;

import java.util.*;

public class ProduktRegister implements Serializable {
    List<Produkt> produkter;

    public ProduktRegister() {
        produkter = new ArrayList<Produkt>();
    }

    public void addProdukt(Produkt produkt) {
        produkter.add(produkt);
    }

    public void taBortProdukt(int ean) {
        for (int index = 0; index < produkter.size(); index++) {
            if (ean == produkter.get(index).getEaNo())
                System.out.println("Raderar produkt: " + produkter.remove(index));
        }
    }

    public void skrivUtProdukter() {
        Iterator iter = produkter.iterator();
        System.out.printf("%-6s %-30s %7s \t%s\n", "EAN-kod", "Beskrivning", "à pris", "bäst före datum");
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }

    public Produkt getProdukt(int ean) {
        for (int i = 0; i < produkter.size(); i++) {
            if (ean == produkter.get(i).getEaNo()) {
                return ((Produkt)produkter.get(i));
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
