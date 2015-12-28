package food4fun;

import Försäljning.*;

import Produkter.*;

import java.text.*;

import java.util.*;

import Rabatter.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

/**
 * Menyn är huvudmenyn som ansvarar för alla val som användaren gör.
 * Menyn hanterar även all Input och Output i systemet.
 */
public class Meny implements Serializable {
    private ProduktRegister produktRegister;
    private Försäljning försäljning;
    private RabattRegister rabattRegister;

    /**
     * Programmets main metod som ser till att skapa ett kassasystem.
     */
    public static void main(String[] args) {
        Meny meny = new Meny(new Kassasystem());
    }

    public Meny(Kassasystem kassaregister) {
        kassaregister = laddaKassaRegister(kassaregister);

        produktRegister = kassaregister.getProduktRegister();
        försäljning = kassaregister.getFörsäljning();
        rabattRegister = kassaregister.getRabattRegister();

        showMenu();
        sparaKassaregistret(kassaregister);
    }

    private final int REGISTRERA_KÖP = 1, BERÄKNA = 2, NOLLSTÄLL = 3, ÄNDRA_PRIS = 4, REGISTRERA_PRODUKT = 5, TABORT =
        6, SPARA = 9, SKRIVUT = 7, RABATTMENY = 8, QUIT = -1;
    private final String MENU = "\n\t * * * MENY * * *\n" +
        REGISTRERA_KÖP + ". Registrera nytt köp \n" +
        "\nFÖRSÄLJNING\n " + BERÄKNA + ". Beräkna total försäljning för period \n " + NOLLSTÄLL +
        ". Nollställ total försäljning \n" +
        "\nPRODUKTER\n " + ÄNDRA_PRIS + ". Ändra pris på produkt \n " + REGISTRERA_PRODUKT +
        ". Registrera ny produkt \n " + TABORT + ". Ta bort en produkt \n " + SKRIVUT +
        ". Skriver ut Produkt Registret\n\n" +
        RABATTMENY + ". Hantera Rabatter\n\n" +
        SPARA + ". Spara och avsluta \n";

    private void showMenu() {
        int choice = 0;

        while (choice != QUIT) {
            println(MENU);
            choice = getInteger();
            switch (choice) {
            case REGISTRERA_KÖP:
                köpProdukter(försäljning);
                break;
            case BERÄKNA:
                beräknaKöpFörPeriod();
                break;
            case NOLLSTÄLL:
                försäljning.nollställFörsäljning();
                println("\nFörsäljning nollställd.");
                break;
            case ÄNDRA_PRIS:
                redigeraPris();
                break;
            case REGISTRERA_PRODUKT:
                registreraProdukt();
                break;
            case TABORT:
                taBortProdukt();
                break;
            case SPARA:
                choice = QUIT;
            case SKRIVUT:
                skrivUtProduktRegistret();
                break;
            case RABATTMENY:
                rabattMeny();
                break;
            }
        }
    }
    
    /** Produkt Metoder */
    
    /**
     * köpProdukter skapar en instans av köp för att sedan lägga till produkter som klienten vill
     * köpa. Så länge produkten existerar samt att bäst före datumet inte har gått ut så registreras produkten.
     * @param försäljning
     */
    private void köpProdukter(Försäljning försäljning) {
        Köp köp = new Köp(rabattRegister);
        String rabatt = "";
        final int PRODUKT_REG = -2;
        int ean = PRODUKT_REG;
        final String inputMsg = "\nSkriv in EAN nummret:\n(" + QUIT + " = avsluta, \n" +
            PRODUKT_REG + " = skriva ut produktregistret)";

        println(" * * * REGISTRERA NYTT KÖP * * *\n");
        while (ean != QUIT) {
            println(inputMsg);
            ean = getInteger();
            switch (ean) {
            case QUIT:
                break;
            case PRODUKT_REG:
                skrivUtProduktRegistret();
                break;
            default:
                if (produktRegister.checkEAN(ean)) {
                    if (!köp.utgångetDatum(produktRegister.getProdukt(ean))) {
                        println("Skriv in antal");
                        köp.läggTillVara(getInteger(), produktRegister.getProdukt(ean));
                        println(köp.skrivKvitto());
                    } else {
                        System.out.println("Produktens bäst före datum har gått ut.\n" +
                                "Produkterna registreras inte");
                    }
                } else {
                    println("Produkt med EAN nummer \"" + ean + "\" existerar inte.");
                }
                break;
            }
        }
        if (!köp.ärTom()) {
            while (true) {
                println("\nSkriv in rabattkod (skriv \"" + QUIT + "\" för att avsluta)");
                rabatt = getLine();
                if (rabatt.equals("-1"))
                    break;

                if (rabattRegister.finnsRabatt(rabatt)) {
                    if (köp.läggTillRabatt(rabatt)) {
                        println(köp.skrivKvitto());
                    } else
                        println("Det gick inte att lägga till \"" + rabatt +
                                "\".\nAntingen är köpet inte kvalificerat för rabatten eller så är den redan använd.");
                } else {
                    println("En rabatt med koden \"" + rabatt + "\" existerar inte.");
                }
            }
        }

        //sparar köpet om man köpt några varor.
        if (!köp.ärTom()) {
            println("\t\tKVITTO\n" +
                    köp.skrivKvitto());
            försäljning.läggTillKöp(köp);
        }
    }

    /**
     * registreraProdukt() tar hand om at lägga till flera produkter i produkt registret.
     * Ber klienten att skriva in EAN nummer, Beskrivning, Pris och eventuellt ett bäst före datum.
     * Avbryter metoden om man försöker att registrera en produkt med samma EAN nummer som en produkt som redan
     * existerar.
     */
    private void registreraProdukt() {
        println("REGISTRERA PRODUKT\n" +
                QUIT + " Avbryt\n");
        boolean loop = true;
        int eanNummer;
        while (loop) {
            println("Skriv in EAN Nummer");
            eanNummer = getInteger();

            if (eanNummer == QUIT)
                loop = false;
            else if (!produktRegister.checkEAN(eanNummer)) {
                String beskrivning = setBeskrivning();
                Double pris = setPris();
                Date bästFöreDatum = setBästFöreDatum();
                if (bästFöreDatum != null) {
                    println("Registrerar ny färskvara");
                    produktRegister.addProdukt(new Färskvaror(eanNummer, beskrivning, pris, bästFöreDatum));
                } else {
                    println("Registrerar ny Produkt");
                    produktRegister.addProdukt(new Produkt(eanNummer, beskrivning, pris));
                }
            } else {
                println("Produkt med samma EAN nummer finns redan\nAvbryter registrering");
                loop = false;
            }
        }
    }


    /**
     * redigeraPris() låter användaren redigera ett pris på en specifik produkt
     * genom att ange ett EAN nummer som matchar med ett EAN nummer i produktregistret.
     * Om en matchning sker så ändras priset på den produkten till ett som klienten anger.
     * Annars får klienten ange ett nytt EAN nummer som matchar eller avsluta metoden.
     */
    private void redigeraPris() {
        println("REDIGERA PRIS\nSkriv in EAN nummret till produkten du vill ändra.\n" +
                "Avbryt genom att skriva in -1 ");
        int ean = getInteger();

        while (ean != QUIT) {
            if (produktRegister.checkEAN(ean)) {
                println("Skriv in nytt pris på\n" +
                        produktRegister.getProdukt(ean));
                produktRegister.getProdukt(ean).setPris(getDouble());
                break;
            } else {
                println("Produkt EAN nummer :" + ean + " finns inte\n" +
                        "Försök igen. Avbryt med -1");
                ean = getInteger();
            }
        }
    }
    
    private void skrivUtProduktRegistret() {
        Iterator iter = produktRegister.getProduktRegister();
        System.out.printf("%-6s %-30s %7s \t%s\n", "EAN-kod", "Beskrivning", "à pris", "bäst före datum");
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }

    public void beräknaKöpFörPeriod() {
        println("TOTAL FÖRSÄLJNING FÖR PERIOD:\n");
        println("Total försäljning: " +
                försäljning.totalFörsäljning(skrivDatum("startdatum "), skrivDatum("slutdatum ")) + "\n");
    }

    private Produkt getProdukt(String msg) {
        int ean;
        while (true) {
            println(msg);
            ean = getInteger();
            if (produktRegister.checkEAN(ean))
                return produktRegister.getProdukt(ean);
            else
                println("\tEn produkt med EAN " + ean + " finns inte i registret.");
        }
    }

    private void taBortProdukt() {
        println("TA BORT PRODUKT\nSkriv in EAN nummret till produkten du vill radera.\n" +
                "Avbryt genom att skriva in -1 ");
        int ean = getInteger();

        while (ean != QUIT) {
            if (produktRegister.checkEAN(ean)) {
                produktRegister.taBortProdukt(ean);
                break;
            } else {
                println("Produkt EAN nummer :" + ean + " finns inte\n" +
                        "Försök igen. Avbryt med -1");
                ean = getInteger();
            }
        }
    }

    /** Rabatt Metoder */

    private void rabattMeny() {
        int choice;
        final int LÄGG_TILL = 1, TA_BORT = 2, SKRIV_UT = 3, SKRIV_UT_ALLA = 4;
        final String MENU = "HANTERA RABATTER \n" +
            "  " + LÄGG_TILL + " Lägg till rabatt\n" +
            "  " + TA_BORT + " Ta bort rabatt\n" +
            "  " + SKRIV_UT + " Skriv ut en rabatt\n" +
            "  " + SKRIV_UT_ALLA + " Skriv ut alla rabatter i registret\n" +
            "   " + QUIT + " tillbaka";

        while (true) {
            choice = getInteger(MENU);
            switch (choice) {
            case QUIT:
                return;
            case LÄGG_TILL:
                läggTillRabatt();
                break;
            case TA_BORT:
                rabattRegister.taBortRabatt(getLine("Skriv in koden för rabatten du vill ta bort: "));
                break;
            case SKRIV_UT:
                println(rabattRegister.skrivUtRabatt(getLine("Skriv in koden för rabatten du vill skriva ut: ")));
                break;
            case SKRIV_UT_ALLA:
                println(rabattRegister + "");
                break;
            default:
                break;
            }
        }
    }

    private void läggTillRabatt() {
        int choice;
        final int XFÖRY = 1, XFÖRYOMZ = 2, XOMY = 3, XPROCENT = 4;

        final String INPUT_DATUM = "Skriv in \"Används före datum\" (yyyy-MM-dd): ";
        final String INPUT_KOD = "Skriv in rabattkod: ";
        final String INPUT_BESK = "Skriv in rabattens beskrivning: ";

        final String MENU = "Lägg till rabatt \n" +
            "  " + XFÖRY + ". Köp Xst av en produkt för priset av Y\n" +
            "  " + XFÖRYOMZ + ". Köp produkt X för Ykr om man även köper produkt Z\n" +
            "  " + XOMY + ". Spara Xkr på en produkt om man handlar för minst Ykr\n" +
            "  " + XPROCENT + ". X procent i rabatt på ett köp\n" +
            "   " + QUIT + ". tillbaka";

        while (true) {
            choice = getInteger(MENU);
            switch (choice) {
            case QUIT:
                return;
            case XFÖRY:
                rabattRegister.nyRabatt(new KöpXFörY(getLine(INPUT_BESK), getLine(INPUT_KOD),
                                                     getProdukt("Produktens EAN-kod: "), getInteger("Antal X:"),
                                                     getInteger("Antal Y: "), skrivDatum(INPUT_DATUM)));
                return;
            case XFÖRYOMZ:
                rabattRegister.nyRabatt(new KöpXförYOmZ(getLine(INPUT_BESK), getLine(INPUT_KOD),
                                                        getProdukt("Produkt X EAN-kod: "),
                                                        getProdukt("Produkt Zs EAN-kod: "),
                                                        getDouble("Rabattpris för Produkt X: "),
                                                        skrivDatum(INPUT_DATUM)));
                return;
            case XOMY:
                rabattRegister.nyRabatt(new XRabattOmY(getLine(INPUT_BESK), getLine(INPUT_KOD),
                                                       getProdukt("Produktens EAN-kod: "),
                                                       getDouble("Produktens rabatterade pris: "),
                                                       getDouble("Minsta summa att handla för: "),
                                                       skrivDatum(INPUT_DATUM)));
                return;
            case XPROCENT:
                rabattRegister.nyRabatt(new Procentavdrag(getLine(INPUT_BESK), getLine(INPUT_KOD),
                                                          getDouble("Rabatt i procent (ex 5% rabatt = 0,05): "),
                                                          skrivDatum(INPUT_DATUM)));
                return;
            default:
                System.out.println("Ogiltigt val.");
                break;
            }
        }
    }


    /** Fil metoder */

    /**
     * laddaKassaRegistret tar in ett kassaregister och kollar sedan om det existerar en
     * kassaregister.dat. Om den existerar så laddas det sparade kassa objektet in, om den inte existerar så laddas
     * produkt och färskvaror in från separata .txt filer och tilldelas produktregistret.
     * Om inte .txt filerna existerar så avbrys programmets körning.
     * @param kassaregister
     * @return
     */
    private Kassasystem laddaKassaRegister(Kassasystem kassaregister) {
        Filhantering fil = new Filhantering();
        final String PRODUKTER = "C:\\Users\\Mathias\\Documents\\JDeveloper\\Produkter.txt";
        final String FÄRSKVAROR = "C:\\Users\\Mathias\\Documents\\JDeveloper\\Färskvaror.txt";
        
        if (fil.tomFil()) {
            try {
                println("Laddar kassaregister");
                return kassaregister = fil.laddaObjektFil();
            } catch (IOException e) {
                println("I/O-Fel vid inläsning av objektet\n" + 
                "Avbryter programmet");
                System.exit(0);
            } catch (ClassNotFoundException e) {
                println("Fel med klasser vid inläsning av filen\n" + 
                "Avbryter programmet");
                System.exit(0);
            }
        } else {
            try {
                println("Laddar från .txt filer");
                fil.läsInFil(PRODUKTER, kassaregister.getProduktRegister());
                fil.läsInFil(FÄRSKVAROR, kassaregister.getProduktRegister());
            } catch (FileNotFoundException e) {
                println("Hittar inte filen\nAvbryter programmet");
                System.exit(0);
            } catch (IOException e) {
                println("I/O-Fel vid inläsning av filen\n" + 
                "Avbryter programmet");
                System.exit(0);
            }
        }
        return kassaregister;
    }

    /**
     * sparaKassaregistret() tar in ett kassaregister objektet och sparar det till kassaregister.dat
     * @param kassaregister
     */
    private void sparaKassaregistret(Kassasystem kassaregister) {
        Filhantering fil = new Filhantering();
        try {
            fil.sparaObjektFil(kassaregister);
            println("Sparar kassaregister och avslutar Programmet");
            System.exit(0);
        } catch (IOException e) {
            println("I/O-Fel när filen skulle sparas\nKan ej spara kassaregistret");
        }
    }


    /** Input metoder */

    private String setBeskrivning() {
        println("Skriv in Produkt Beskrivning");
        return getLine();
    }

    private Double setPris() {
        println("Skriv in pris på produkten");
        return getDouble();
    }

    private Date setBästFöreDatum() {
        println("Skriv in bäst före datum på produkten, YYYY-MM-DD.\nTryck <enter> om du inte vill ange bäst före datum");
        return getDatum(getLine());
    }

    private int getInteger() {
        int integer = 0;
        Scanner scan = new Scanner(System.in);
        boolean loop = true;
        while (loop) {
            try {
                integer = scan.nextInt();
                loop = false;
            } catch (InputMismatchException e) {
                System.out.println("\tAnge ett heltal.");
                scan.next();
                loop = true;
            }
        }
        return integer;
    }

    private int getInteger(String msg) {
        System.out.println(msg);
        return getInteger();
    }

    private void println(String str) {
        System.out.println(str);
    }

    private String getLine() {
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }

    private String getLine(String msg) {
        System.out.print(msg);
        return getLine();
    }

    private double getDouble() {
        double d = 0.0;
        Scanner scan = new Scanner(System.in);
        boolean loop = true;
        while (loop) {
            try {
                d = scan.nextDouble();
                loop = false;
            } catch (InputMismatchException e) {
                System.out.println("\tAnge ett tal (ex 0,25 eller 33).");
                scan.next();
                loop = true;
            }
        }
        return d;
    }

    private double getDouble(String msg) {
        System.out.print(msg);
        return getDouble();
    }

    private Date skrivDatum(String msg) {
        System.out.print(msg);
        return getDatum(getLine());
    }

    private Date getDatum(String datum) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(datum);
        } catch (ParseException e) {
            return null;
        }
    }
}
