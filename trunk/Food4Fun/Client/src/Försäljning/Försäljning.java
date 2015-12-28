package F�rs�ljning;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * F�rs�ljning lagrar de olika k�pen, och har metoder som l�ter klienten:
 *      - ber�kna den totala f�rs�ljningen eller den totala f�rs�ljningen f�r en period.
 *      - l�gga till ett K�p
 *      - nollst�lla f�rs�ljningen (= tar bort alla lagrade K�p)
 *      (- skriva ut hur m�nga "k�p" som �r registrerade.)
 */

public class F�rs�ljning implements Serializable {
    private List<K�p> f�rs�ljning;

    public F�rs�ljning() {
        f�rs�ljning = new ArrayList<K�p>();
    }

    public void l�ggTillK�p(K�p k�p) {
        f�rs�ljning.add(k�p);
    }

    public List<K�p> getF�rs�ljinig() {
        return f�rs�ljning;
    }

    public double totalF�rs�ljning() {
        double summa = 0.0;

        for (K�p k�p : f�rs�ljning)
            summa += k�p.ber�knaSumma();

        return summa;
    }

    /* Samma som ovan fast ber�knar den totala f�rs�ljningen inom ett visst intervall */

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
