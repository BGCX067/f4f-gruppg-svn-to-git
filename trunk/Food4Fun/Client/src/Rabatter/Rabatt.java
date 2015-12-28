package Rabatter;

import F�rs�ljning.K�p;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

/*
 * Rabatt �r en abstrakt klass f�r rabatterna, som det �r t�nkt alla rabatter
 * ska �rva fr�n.
 * Den inneh�ller allt som rabatterna har gemensamt (beskrivning, rabattkod och
 * anv�nds-f�re-datum) med get och set-metoder f�r alla attributen f�rutom rabattkod,
 * som bara har en get-metod.
 * 
 * Den deklarerar ocks� en abstrakt klass, ber�knaRabatt(k�p),
 * som alla rabatterna ska implementera.
 */

public abstract class Rabatt implements Serializable {
    private String beskrivning;
    private Date afd;           // anv�nds-f�re-datum
    private String rabattkod;

    protected Rabatt(Date afd, String beskrivning, String rabattkod) {
        this.rabattkod = rabattkod;
        this.beskrivning = beskrivning;
        this.afd = afd;
    }

    // skickar tillbaka rabattavdraget.

    public abstract double ber�knaRabatt(K�p k�p);

    public void setAFD(Date afd) {
        this.afd = afd;
    }

    public boolean �rGiltig() {
        return (new Date().getTime() <= afd.getTime());
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public String getAFD() {
        DateFormat afd = new SimpleDateFormat("yyyy-MM-dd");
        return afd.format(this.afd);
    }

    public String getRabattkod() {
        return rabattkod;
    }

    public String toString() {
        return ("Kod: \"" + rabattkod + "\". " + beskrivning + ". AFD: " + getAFD() + "\n");
    }
}
