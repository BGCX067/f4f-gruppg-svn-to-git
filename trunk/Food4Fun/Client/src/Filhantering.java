package food4fun;

import Produkter.*;

import java.io.*;

import java.text.*;

import java.util.*;

public class Filhantering {
    private final String REGISTER = "kassaregister.dat";

    public Filhantering() {
    }

    public void läsInFil(String fileName, ProduktRegister produkter) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(fileName));
            scanner.useDelimiter("\n");
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
        } catch (FileNotFoundException e) {
            System.out.println("Exception " + e);
        } catch (IOException e) {
            System.out.println("Exception " + e);
        }
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

    public void sparaObjektFil(Kassasystem kassaregister) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(REGISTER));
            out.writeObject(kassaregister);
            out.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public Kassasystem laddaObjektFil() {
        Kassasystem kassaregister = new Kassasystem();
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(REGISTER));
            kassaregister = (Kassasystem)in.readObject();
            in.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
        }
        return kassaregister;
    }

    public boolean tomFil() {
        return new File(REGISTER).exists();
    }
}
