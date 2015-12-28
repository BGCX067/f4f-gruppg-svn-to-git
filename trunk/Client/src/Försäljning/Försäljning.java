package F�rs�ljning;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * F�rs�ljnings klassen ansvarar f�r att lagra alla k�p som genomf�rs.
 * F�rs�ljnings klassen har �ven metoder som r�knar ut den totala f�rs�ljnings summan,
 * exempelvis mellan tv� specifika datum. Den har �ven en metod f�r att nollst�lla alla k�p.
 */
public class F�rs�ljning implements Serializable {
    private List<K�p> f�rs�ljning;

    public F�rs�ljning() {
        f�rs�ljning = new ArrayList<K�p>();
    }

    public void l�ggTillK�p(K�p k�p) {
        f�rs�ljning.add(k�p);
    }

    public double totalF�rs�ljning() {
        double summa = 0.0;

        for (K�p k�p : f�rs�ljning)
            summa += k�p.ber�knaSumma();

        return summa;
    }

    /**
     * Ber�knar ut och returnerar den totala f�rs�ljnings summan mellan ett visst datum intervall.
     * @param startdatum
     * @param slutdatum
     * @return summa
     */
    public double totalF�rs�ljning(Date startdatum, Date slutdatum) {
        double summa = 0.0;
        for (K�p k�p : f�rs�ljning) {
            if (0 >= startdatum.compareTo(k�p.getDatum()) && 0 <= slutdatum.compareTo(k�p.getDatum()))
                summa += k�p.ber�knaSumma();
        }
        return summa;
    }

    public void nollst�llF�rs�ljning() {
        f�rs�ljning.clear();
    }

    public String toString() {
        return "Antal k�p lagrade: " + f�rs�ljning.size();
    }
}
