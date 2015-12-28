package Försäljning;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Försäljnings klassen ansvarar för att lagra alla köp som genomförs.
 * Försäljnings klassen har även metoder som räknar ut den totala försäljnings summan,
 * exempelvis mellan två specifika datum. Den har även en metod för att nollställa alla köp.
 */
public class Försäljning implements Serializable {
    private List<Köp> försäljning;

    public Försäljning() {
        försäljning = new ArrayList<Köp>();
    }

    public void läggTillKöp(Köp köp) {
        försäljning.add(köp);
    }

    public double totalFörsäljning() {
        double summa = 0.0;

        for (Köp köp : försäljning)
            summa += köp.beräknaSumma();

        return summa;
    }

    /**
     * Beräknar ut och returnerar den totala försäljnings summan mellan ett visst datum intervall.
     * @param startdatum
     * @param slutdatum
     * @return summa
     */
    public double totalFörsäljning(Date startdatum, Date slutdatum) {
        double summa = 0.0;
        for (Köp köp : försäljning) {
            if (0 >= startdatum.compareTo(köp.getDatum()) && 0 <= slutdatum.compareTo(köp.getDatum()))
                summa += köp.beräknaSumma();
        }
        return summa;
    }

    public void nollställFörsäljning() {
        försäljning.clear();
    }

    public String toString() {
        return "Antal köp lagrade: " + försäljning.size();
    }
}
