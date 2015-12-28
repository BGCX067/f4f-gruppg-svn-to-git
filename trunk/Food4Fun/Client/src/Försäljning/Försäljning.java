package Försäljning;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Försäljning lagrar de olika köpen, och har metoder som låter klienten:
 *      - beräkna den totala försäljningen eller den totala försäljningen för en period.
 *      - lägga till ett Köp
 *      - nollställa försäljningen (= tar bort alla lagrade Köp)
 *      (- skriva ut hur många "köp" som är registrerade.)
 */

public class Försäljning implements Serializable {
    private List<Köp> försäljning;

    public Försäljning() {
        försäljning = new ArrayList<Köp>();
    }

    public void läggTillKöp(Köp köp) {
        försäljning.add(köp);
    }

    public List<Köp> getFörsäljinig() {
        return försäljning;
    }

    public double totalFörsäljning() {
        double summa = 0.0;

        for (Köp köp : försäljning)
            summa += köp.beräknaSumma();

        return summa;
    }

    /* Samma som ovan fast beräknar den totala försäljningen inom ett visst intervall */

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
