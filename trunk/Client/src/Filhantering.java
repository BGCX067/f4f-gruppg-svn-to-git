package food4fun;

import Produkter.*;

import java.io.*;

import java.text.*;

import java.util.*;

/**
 * Filhanterings klassen står för all filhantering.
 * Har metoder som läser in .txt filer och sparar det i ett produkt register.
 * Det finns äver metoder som läser in ett kassasystem objekt samt sparar ett
 * kassasystem objekt.
 *
 */
public class Filhantering {
    private final String REGISTER = "kassaregister.dat";

    public Filhantering() {
    }

    public void läsInFil(String fileName, ProduktRegister produkter) throws FileNotFoundException, IOException {
        Scanner scanner = new Scanner(new File(fileName)).useDelimiter("\n");
        scanner.nextLine();
        while (scanner.hasNext()) {
            String[] tmp = scanner.nextLine().split("\t");
            if (tmp.length < 4) {
                // Lägger till en Produkt om tmp är mindre än 3, dvs. utan datum.
                produkter.addProdukt(new Produkt(Integer.parseInt(tmp[0]), tmp[1],
                                                 Double.parseDouble(tmp[2].replace(",", "."))));
            } else {
                // Lägger till en Färskvara om tmp längden är större än 3.
                produkter.addProdukt(new Färskvaror(Integer.parseInt(tmp[0]), tmp[1],
                                                    Double.parseDouble(tmp[2].replace(",", ".")),
                                                    parseStringToDate(tmp[3])));
            }
        }
        scanner.close(); // Stänger dokumentet.
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

    public Date parseStringToDate(String bästFöreDatum) {
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return (formatter.parse(bästFöreDatum)); // Datumet
        } catch (ParseException x) {
            System.out.println("Exception" + x);
        }
        return (null);
    }

    public boolean tomFil() {
        return new File(REGISTER).exists();
    }
}
