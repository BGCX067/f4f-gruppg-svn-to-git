package Rabatter;

import Försäljning.Köp;

import java.io.Serializable;

import java.util.Date;


public class Procentavdrag extends Rabatt implements Serializable {
    private double procent;

    public Procentavdrag(String beskrivning, String rabattkod, double procent, Date afd) {
        super(afd, beskrivning, rabattkod);
        this.procent = procent;
    }

    public double beräknaRabatt(Köp köp) {
        double summa = 0.0;

        if (this.ärGiltig())
            summa = köp.summaUtanRabatt() * procent;

        return summa;
    }

    public String toString() {
        return (super.toString() + (procent * 100) + "% rabatt.\n");
    }
}
