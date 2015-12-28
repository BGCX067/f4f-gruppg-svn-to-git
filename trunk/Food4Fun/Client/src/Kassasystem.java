package food4fun;

import Produkter.*;

import Försäljning.*;

import Rabatter.RabattRegister;

import java.io.*;

public class Kassasystem implements Serializable {

    ProduktRegister produktRegister;
    Försäljning försäljning;
    RabattRegister kampanjer;

    public Kassasystem() {
        Filhantering fil = new Filhantering();
        produktRegister = new ProduktRegister();
        försäljning = new Försäljning();
        kampanjer = new RabattRegister();

        fil.läsInFil("Produkter.txt", produktRegister);
        fil.läsInFil("Färskvaror.txt", produktRegister);

    }

    public ProduktRegister getProduktRegister() {
        return produktRegister;
    }

    public Försäljning getFörsäljning() {
        return försäljning;
    }

    public RabattRegister getKampanjer() {
        return kampanjer;
    }
}
