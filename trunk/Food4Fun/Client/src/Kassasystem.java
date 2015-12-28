package food4fun;

import Produkter.*;

import F�rs�ljning.*;

import Rabatter.RabattRegister;

import java.io.*;

public class Kassasystem implements Serializable {

    ProduktRegister produktRegister;
    F�rs�ljning f�rs�ljning;
    RabattRegister kampanjer;

    public Kassasystem() {
        Filhantering fil = new Filhantering();
        produktRegister = new ProduktRegister();
        f�rs�ljning = new F�rs�ljning();
        kampanjer = new RabattRegister();

        fil.l�sInFil("Produkter.txt", produktRegister);
        fil.l�sInFil("F�rskvaror.txt", produktRegister);

    }

    public ProduktRegister getProduktRegister() {
        return produktRegister;
    }

    public F�rs�ljning getF�rs�ljning() {
        return f�rs�ljning;
    }

    public RabattRegister getKampanjer() {
        return kampanjer;
    }
}
