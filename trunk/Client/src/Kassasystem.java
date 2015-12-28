package food4fun;

import Produkter.*;

import F�rs�ljning.*;

import Rabatter.RabattRegister;

import java.io.*;

/**
 * Kassasystemet skapar alla register d�r sedan all data lagras.
 * Inneh�ller metoder som h�mtar varje register med en get<register namn> metod.
 */
public class Kassasystem implements Serializable {
    private ProduktRegister produktRegister;
    private F�rs�ljning f�rs�ljning;
    private RabattRegister rabattRegister;

    public Kassasystem() {
        produktRegister = new ProduktRegister();
        f�rs�ljning = new F�rs�ljning();
        rabattRegister = new RabattRegister();
    }

    public ProduktRegister getProduktRegister() {
        return produktRegister;
    }

    public F�rs�ljning getF�rs�ljning() {
        return f�rs�ljning;
    }

    public RabattRegister getRabattRegister() {
        return rabattRegister;
    }
}
