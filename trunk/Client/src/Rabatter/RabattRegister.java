package Rabatter;


import Försäljning.Köp;

import java.io.Serializable;

import java.util.*;

public class RabattRegister implements Serializable {
    private HashMap<String, Rabatt> rabatter;

    public RabattRegister() {
        rabatter = new HashMap<String, Rabatt>();
    }

    public boolean finnsRabatt(String rabattKod) {
        return rabatter.containsKey(rabattKod);
    }

    public void taBortRabatt(String rabattKod) {
        rabatter.remove(rabattKod);
    }

    public void nyRabatt(Rabatt rabatt) {
        rabatter.put(rabatt.getRabattkod(), rabatt);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Collection<Rabatt> rabatt = rabatter.values();

        for (Rabatt elem : rabatt)
            stringBuilder.append(elem + "\n");

        return stringBuilder.toString();
    }

    public String skrivUtRabatt(String rabattKod) {
        if (rabatter.containsKey(rabattKod))
            return "" + rabatter.get(rabattKod);
        else
            return ("Det finns ingen rabatt med kod \"" + rabattKod + "\" sparad i systemet.");
    }

    public double rabattPåKöp(Köp köp, String rabattkod) {
        if (rabatter.containsKey(rabattkod))
            return rabatter.get(rabattkod).beräknaRabatt(köp);
        else
            return 0;
    }
}
