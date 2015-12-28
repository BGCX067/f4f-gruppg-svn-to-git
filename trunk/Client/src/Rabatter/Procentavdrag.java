package Rabatter;

import F�rs�ljning.K�p;

import java.io.Serializable;

import java.util.Date;


public class Procentavdrag extends Rabatt implements Serializable {
    private double procent;

    public Procentavdrag(String beskrivning, String rabattkod, double procent, Date afd) {
        super(afd, beskrivning, rabattkod);
        this.procent = procent;
    }

    public double ber�knaRabatt(K�p k�p) {
        double summa = 0.0;

        if (this.�rGiltig())
            summa = k�p.summaUtanRabatt() * procent;

        return summa;
    }

    public String toString() {
        return (super.toString() + (procent * 100) + "% rabatt.\n");
    }
}
