package food4fun;

import Produkter.*;

import java.io.*;

import java.text.*;

import java.util.*;

/**
 * Filhanterings klassen st�r f�r all filhantering.
 * Har metoder som l�ser in .txt filer och sparar det i ett produkt register.
 * Det finns �ver metoder som l�ser in ett kassasystem objekt samt sparar ett
 * kassasystem objekt.
 *
 */
public class Filhantering {
    private final String REGISTER = "kassaregister.dat";

    public Filhantering() {
    }

    public void l�sInFil(String fileName, ProduktRegister produkter) throws FileNotFoundException, IOException {
        Scanner scanner = new Scanner(new File(fileName)).useDelimiter("\n");
        scanner.nextLine();
        while (scanner.hasNext()) {
            String[] tmp = scanner.nextLine().split("\t");
            if (tmp.length < 4) {
                // L�gger till en Produkt om tmp �r mindre �n 3, dvs. utan datum.
                produkter.addProdukt(new Produkt(Integer.parseInt(tmp[0]), tmp[1],
                                                 Double.parseDouble(tmp[2].replace(",", "."))));
            } else {
                // L�gger till en F�rskvara om tmp l�ngden �r st�rre �n 3.
                produkter.addProdukt(new F�rskvaror(Integer.parseInt(tmp[0]), tmp[1],
                                                    Double.parseDouble(tmp[2].replace(",", ".")),
                                                    parseStringToDate(tmp[3])));
            }
        }
        scanner.close(); // St�nger dokumentet.
    }

    public void sparaObjektFil(Kassasystem kassaregister) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(REGISTER));
        out.writeObject(kassaregister);
        out.close();
    }

    public Kassasystem laddaObjektFil() throws IOException, ClassNotFoundException {
        Kassasystem kassaregister = new Kassasystem();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream(REGISTER));
        kassaregister = (Kassasystem)in.readObject();
        in.close();
        return kassaregister;
    }

    public Date parseStringToDate(String b�stF�reDatum) {
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return (formatter.parse(b�stF�reDatum)); // Datumet
        } catch (ParseException x) {
            System.out.println("Exception" + x);
        }
        return (null);
    }

    public boolean tomFil() {
        return new File(REGISTER).exists();
    }
}
