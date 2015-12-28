package food4fun;

import Produkter.*;

import Försäljning.*;

import Rabatter.RabattRegister;

import java.io.*;

/**
 * Kassasystemet skapar alla register där sedan all data lagras.
 * Innehåller metoder som hämtar varje register med en get<register namn> metod.
 */
public class Kassasystem implements Serializable {
    private ProduktRegister produktRegister;
    private Försäljning försäljning;
    private RabattRegister rabattRegister;

    public Kassasystem() {
        produktRegister = new ProduktRegister();
        försäljning = new Försäljning();
        rabattRegister = new RabattRegister();
    }

    public ProduktRegister getProduktRegister() {
        return produktRegister;
    }

    public Försäljning getFörsäljning() {
        return försäljning;
    }

    public RabattRegister getRabattRegister() {
        return rabattRegister;
    }
}
