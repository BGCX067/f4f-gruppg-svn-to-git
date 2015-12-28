package Rabatter;

import Försäljning.Köp;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

public abstract class Rabatt implements Serializable {
    private String beskrivning;
    private Date afd;
    private String rabattkod;

    protected Rabatt(Date afd, String beskrivning, String rabattkod) {
        this.rabattkod = rabattkod;
        this.beskrivning = beskrivning;
        this.afd = afd;
    }

    // skickar tillbaka rabattavdraget.

    public abstract double beräknaRabatt(Köp köp);

    public void setAFD(Date afd) {
        this.afd = afd;
    }

    public boolean ärGiltig() {
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
