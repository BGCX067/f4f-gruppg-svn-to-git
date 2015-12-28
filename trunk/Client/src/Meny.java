package food4fun;

import F�rs�ljning.*;

import Produkter.*;

import java.text.*;

import java.util.*;

import Rabatter.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

/**
 * Menyn �r huvudmenyn som ansvarar f�r alla val som anv�ndaren g�r.
 * Menyn hanterar �ven all Input och Output i systemet.
 */
public class Meny implements Serializable {
    private ProduktRegister produktRegister;
    private F�rs�ljning f�rs�ljning;
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
        f�rs�ljning = kassaregister.getF�rs�ljning();
        rabattRegister = kassaregister.getRabattRegister();

        showMenu();
        sparaKassaregistret(kassaregister);
    }

    private final int REGISTRERA_K�P = 1, BER�KNA = 2, NOLLST�LL = 3, �NDRA_PRIS = 4, REGISTRERA_PRODUKT = 5, TABORT =
        6, SPARA = 9, SKRIVUT = 7, RABATTMENY = 8, QUIT = -1;
    private final String MENU = "\n\t * * * MENY * * *\n" +
        REGISTRERA_K�P + ". Registrera nytt k�p \n" +
        "\nF�RS�LJNING\n " + BER�KNA + ". Ber�kna total f�rs�ljning f�r period \n " + NOLLST�LL +
        ". Nollst�ll total f�rs�ljning \n" +
        "\nPRODUKTER\n " + �NDRA_PRIS + ". �ndra pris p� produkt \n " + REGISTRERA_PRODUKT +
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
            case REGISTRERA_K�P:
                k�pProdukter(f�rs�ljning);
                break;
            case BER�KNA:
                ber�knaK�pF�rPeriod();
                break;
            case NOLLST�LL:
                f�rs�ljning.nollst�llF�rs�ljning();
                println("\nF�rs�ljning nollst�lld.");
                break;
            case �NDRA_PRIS:
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
     * k�pProdukter skapar en instans av k�p f�r att sedan l�gga till produkter som klienten vill
     * k�pa. S� l�nge produkten existerar samt att b�st f�re datumet inte har g�tt ut s� registreras produkten.
     * @param f�rs�ljning
     */
    private void k�pProdukter(F�rs�ljning f�rs�ljning) {
        K�p k�p = new K�p(rabattRegister);
        String rabatt = "";
        final int PRODUKT_REG = -2;
        int ean = PRODUKT_REG;
        final String inputMsg = "\nSkriv in EAN nummret:\n(" + QUIT + " = avsluta, \n" +
            PRODUKT_REG + " = skriva ut produktregistret)";

        println(" * * * REGISTRERA NYTT K�P * * *\n");
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
                    if (!k�p.utg�ngetDatum(produktRegister.getProdukt(ean))) {
                        println("Skriv in antal");
                        k�p.l�ggTillVara(getInteger(), produktRegister.getProdukt(ean));
                        println(k�p.skrivKvitto());
                    } else {
                        System.out.println("Produktens b�st f�re datum har g�tt ut.\n" +
                                "Produkterna registreras inte");
                    }
                } else {
                    println("Produkt med EAN nummer \"" + ean + "\" existerar inte.");
                }
                break;
            }
        }
        if (!k�p.�rTom()) {
            while (true) {
                println("\nSkriv in rabattkod (skriv \"" + QUIT + "\" f�r att avsluta)");
                rabatt = getLine();
                if (rabatt.equals("-1"))
                    break;

                if (rabattRegister.finnsRabatt(rabatt)) {
                    if (k�p.l�ggTillRabatt(rabatt)) {
                        println(k�p.skrivKvitto());
                    } else
                        println("Det gick inte att l�gga till \"" + rabatt +
                                "\".\nAntingen �r k�pet inte kvalificerat f�r rabatten eller s� �r den redan anv�nd.");
                } else {
                    println("En rabatt med koden \"" + rabatt + "\" existerar inte.");
                }
            }
        }

        //sparar k�pet om man k�pt n�gra varor.
        if (!k�p.�rTom()) {
            println("\t\tKVITTO\n" +
                    k�p.skrivKvitto());
            f�rs�ljning.l�ggTillK�p(k�p);
        }
    }

    /**
     * registreraProdukt() tar hand om at l�gga till flera produkter i produkt registret.
     * Ber klienten att skriva in EAN nummer, Beskrivning, Pris och eventuellt ett b�st f�re datum.
     * Avbryter metoden om man f�rs�ker att registrera en produkt med samma EAN nummer som en produkt som redan
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
                Date b�stF�reDatum = setB�stF�reDatum();
                if (b�stF�reDatum != null) {
                    println("Registrerar ny f�rskvara");
                    produktRegister.addProdukt(new F�rskvaror(eanNummer, beskrivning, pris, b�stF�reDatum));
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
     * redigeraPris() l�ter anv�ndaren redigera ett pris p� en specifik produkt
     * genom att ange ett EAN nummer som matchar med ett EAN nummer i produktregistret.
     * Om en matchning sker s� �ndras priset p� den produkten till ett som klienten anger.
     * Annars f�r klienten ange ett nytt EAN nummer som matchar eller avsluta metoden.
     */
    private void redigeraPris() {
        println("REDIGERA PRIS\nSkriv in EAN nummret till produkten du vill �ndra.\n" +
                "Avbryt genom att skriva in -1 ");
        int ean = getInteger();

        while (ean != QUIT) {
            if (produktRegister.checkEAN(ean)) {
                println("Skriv in nytt pris p�\n" +
                        produktRegister.getProdukt(ean));
                produktRegister.getProdukt(ean).setPris(getDouble());
                break;
            } else {
                println("Produkt EAN nummer :" + ean + " finns inte\n" +
                        "F�rs�k igen. Avbryt med -1");
                ean = getInteger();
            }
        }
    }
    
    private void skrivUtProduktRegistret() {
        Iterator iter = produktRegister.getProduktRegister();
        System.out.printf("%-6s %-30s %7s \t%s\n", "EAN-kod", "Beskrivning", "� pris", "b�st f�re datum");
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }

    public void ber�knaK�pF�rPeriod() {
        println("TOTAL F�RS�LJNING F�R PERIOD:\n");
        println("Total f�rs�ljning: " +
                f�rs�ljning.totalF�rs�ljning(skrivDatum("startdatum "), skrivDatum("slutdatum ")) + "\n");
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
                        "F�rs�k igen. Avbryt med -1");
                ean = getInteger();
            }
        }
    }

    /** Rabatt Metoder */

    private void rabattMeny() {
        int choice;
        final int L�GG_TILL = 1, TA_BORT = 2, SKRIV_UT = 3, SKRIV_UT_ALLA = 4;
        final String MENU = "HANTERA RABATTER \n" +
            "  " + L�GG_TILL + " L�gg till rabatt\n" +
            "  " + TA_BORT + " Ta bort rabatt\n" +
            "  " + SKRIV_UT + " Skriv ut en rabatt\n" +
            "  " + SKRIV_UT_ALLA + " Skriv ut alla rabatter i registret\n" +
            "   " + QUIT + " tillbaka";

        while (true) {
            choice = getInteger(MENU);
            switch (choice) {
            case QUIT:
                return;
            case L�GG_TILL:
                l�ggTillRabatt();
                break;
            case TA_BORT:
                rabattRegister.taBortRabatt(getLine("Skriv in koden f�r rabatten du vill ta bort: "));
                break;
            case SKRIV_UT:
                println(rabattRegister.skrivUtRabatt(getLine("Skriv in koden f�r rabatten du vill skriva ut: ")));
                break;
            case SKRIV_UT_ALLA:
                println(rabattRegister + "");
                break;
            default:
                break;
            }
        }
    }

    private void l�ggTillRabatt() {
        int choice;
        final int XF�RY = 1, XF�RYOMZ = 2, XOMY = 3, XPROCENT = 4;

        final String INPUT_DATUM = "Skriv in \"Anv�nds f�re datum\" (yyyy-MM-dd): ";
        final String INPUT_KOD = "Skriv in rabattkod: ";
        final String INPUT_BESK = "Skriv in rabattens beskrivning: ";

        final String MENU = "L�gg till rabatt \n" +
            "  " + XF�RY + ". K�p Xst av en produkt f�r priset av Y\n" +
            "  " + XF�RYOMZ + ". K�p produkt X f�r Ykr om man �ven k�per produkt Z\n" +
            "  " + XOMY + ". Spara Xkr p� en produkt om man handlar f�r minst Ykr\n" +
            "  " + XPROCENT + ". X procent i rabatt p� ett k�p\n" +
            "   " + QUIT + ". tillbaka";

        while (true) {
            choice = getInteger(MENU);
            switch (choice) {
            case QUIT:
                return;
            case XF�RY:
                rabattRegister.nyRabatt(new K�pXF�rY(getLine(INPUT_BESK), getLine(INPUT_KOD),
                                                     getProdukt("Produktens EAN-kod: "), getInteger("Antal X:"),
                                                     getInteger("Antal Y: "), skrivDatum(INPUT_DATUM)));
                return;
            case XF�RYOMZ:
                rabattRegister.nyRabatt(new K�pXf�rYOmZ(getLine(INPUT_BESK), getLine(INPUT_KOD),
                                                        getProdukt("Produkt X EAN-kod: "),
                                                        getProdukt("Produkt Zs EAN-kod: "),
                                                        getDouble("Rabattpris f�r Produkt X: "),
                                                        skrivDatum(INPUT_DATUM)));
                return;
            case XOMY:
                rabattRegister.nyRabatt(new XRabattOmY(getLine(INPUT_BESK), getLine(INPUT_KOD),
                                                       getProdukt("Produktens EAN-kod: "),
                                                       getDouble("Produktens rabatterade pris: "),
                                                       getDouble("Minsta summa att handla f�r: "),
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
     * kassaregister.dat. Om den existerar s� laddas det sparade kassa objektet in, om den inte existerar s� laddas
     * produkt och f�rskvaror in fr�n separata .txt filer och tilldelas produktregistret.
     * Om inte .txt filerna existerar s� avbrys programmets k�rning.
     * @param kassaregister
     * @return
     */
    private Kassasystem laddaKassaRegister(Kassasystem kassaregister) {
        Filhantering fil = new Filhantering();
        final String PRODUKTER = "C:\\Users\\Mathias\\Documents\\JDeveloper\\Produkter.txt";
        final String F�RSKVAROR = "C:\\Users\\Mathias\\Documents\\JDeveloper\\F�rskvaror.txt";
        
        if (fil.tomFil()) {
            try {
                println("Laddar kassaregister");
                return kassaregister = fil.laddaObjektFil();
            } catch (IOException e) {
                println("I/O-Fel vid inl�sning av objektet\n" + 
                "Avbryter programmet");
                System.exit(0);
            } catch (ClassNotFoundException e) {
                println("Fel med klasser vid inl�sning av filen\n" + 
                "Avbryter programmet");
                System.exit(0);
            }
        } else {
            try {
                println("Laddar fr�n .txt filer");
                fil.l�sInFil(PRODUKTER, kassaregister.getProduktRegister());
                fil.l�sInFil(F�RSKVAROR, kassaregister.getProduktRegister());
            } catch (FileNotFoundException e) {
                println("Hittar inte filen\nAvbryter programmet");
                System.exit(0);
            } catch (IOException e) {
                println("I/O-Fel vid inl�sning av filen\n" + 
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
            println("I/O-Fel n�r filen skulle sparas\nKan ej spara kassaregistret");
        }
    }


    /** Input metoder */

    private String setBeskrivning() {
        println("Skriv in Produkt Beskrivning");
        return getLine();
    }

    private Double setPris() {
        println("Skriv in pris p� produkten");
        return getDouble();
    }

    private Date setB�stF�reDatum() {
        println("Skriv in b�st f�re datum p� produkten, YYYY-MM-DD.\nTryck <enter> om du inte vill ange b�st f�re datum");
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
