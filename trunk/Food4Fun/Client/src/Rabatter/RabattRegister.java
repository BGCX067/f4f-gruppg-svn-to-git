package Rabatter;


import F�rs�ljning.K�p;

import java.io.Serializable;

import java.util.*;

/*
 * RabattRegister �r precis vad det l�ter som, ett register som lagrar de olika
 * rabatterna.
 * 
 * Den tillhandah�ller metoder f�r att l�gga till/ta bort rabatter, skriva ut en rabatt,
 * ber�kna rabatten p� ett k�p och kolla om en rabattkod �r giltig (dvs om det finns en
 * rabatt lagrad i rabattregistret med en viss rabattkod.)
 */

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

    @Override
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

    public double rabattP�K�p(K�p k�p, String rabattkod) {
        if (rabatter.containsKey(rabattkod))
            return rabatter.get(rabattkod).ber�knaRabatt(k�p);
        else
            return 0;
    }
}
