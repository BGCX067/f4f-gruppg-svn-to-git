package food4fun;

import F�rs�ljning.*;

import Produkter.*;

import java.text.*;

import java.util.*;

import Rabatter.*;

import java.io.Serializable;


public class Meny implements Serializable {
    ProduktRegister produktRegister;
    F�rs�ljning f�rs�ljning;
    RabattRegister kampanjer;

    public static void main(String[] args) {
        Filhantering fil = new Filhantering();
        Kassasystem kassaregister;

        //Om kassaregister.dat existerar s� laddas kassaregistret in.
        if (fil.tomFil()) {
            System.out.println("Laddar kassaregister");
            kassaregister = fil.laddaObjektFil();
        } else {
            System.out.println("Tom fil");
            kassaregister = new Kassasystem();
        }

        Meny meny = new Meny(kassaregister);
        meny.showMenu();
        System.out.println("Sparar kassaregister");
        //Sparar hela kassaregistret till kassaregister.dat
        fil.sparaObjektFil(kassaregister);
    }

    public Meny(Kassasystem kassaregister) {
        produktRegister = kassaregister.getProduktRegister();
        f�rs�ljning = kassaregister.getF�rs�ljning();
        kampanjer = kassaregister.getKampanjer();
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

    public void showMenu() {
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
                produktRegister.skrivUtProdukter();
                break;
            case RABATTMENY:
                rabattMeny();
                break;
            }
        }
    }

    public void ber�knaK�pF�rPeriod() {
        println("TOTAL F�RS�LJNING F�R PERIOD:\n");
        println("Total f�rs�ljning: " +
                f�rs�ljning.totalF�rs�ljning(skrivDatum("startdatum "), skrivDatum("slutdatum ")) + "\n");
    }

    private void k�pProdukter(F�rs�ljning f�rs�ljning) {
        K�p k�p = new K�p(kampanjer);
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
                produktRegister.skrivUtProdukter();
                break;
            default:
                if (produktRegister.checkEAN(ean)) {
                    println("Skriv in antal");
                    k�p.l�ggTillVara(getInteger(), produktRegister.getProdukt(ean));
                    println(k�p.skrivKvitto());
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

                if (kampanjer.finnsRabatt(rabatt)) {
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

    private void registreraProdukt() {
        final int PRODUKT = 1, F�RSKVARA = 2;
        println("REGISTRERA PRODUKT\n1: Registrera Produkt\n2: Registrera F�rskvara\n" +
                QUIT + " Avbryt\n");

        int choice = getInteger();

        while (choice != QUIT) {
            if (choice == PRODUKT) {
                int eanNummer;

                println("Skriv in EAN Nummer");
                int ean = getInteger();
                if (!produktRegister.checkEAN(ean)) {
                    eanNummer = ean;
                } else {
                    println("Produkt med samma EAN nummer finns redan\n" +
                            "Avbryter registrering");
                    break;
                }
                println("Skriv in Produkt Beskrivning");
                String beskrivning = getLine();
                println("Skriv in pris p� produkten");
                double pris = getDouble();

                produktRegister.addProdukt(new Produkt(eanNummer, beskrivning, pris));
                break;
            } else if (choice == F�RSKVARA) {
                int eanNummer;
                println("Skriv in EAN Nummer");
                int ean = getInteger();

                if (!produktRegister.checkEAN(ean)) {
                    eanNummer = ean;
                } else {
                    println("Produkt med samma EAN nummer finns redan\n" +
                            "Avbryter registrering");
                    break;
                }
                println("Skriv in Produkt Beskrivning");
                String beskrivning = getLine();
                println("Skriv in pris p� produkten");
                double pris = getDouble();
                println("Skriv in b�st f�re datum p� produkten, YYYY-MM-DD");
                Date b�stF�reDatum = getDatum(getLine());

                produktRegister.addProdukt(new F�rskvaror(eanNummer, beskrivning, pris, b�stF�reDatum));
                break;
            } else {
                println("Fel val, f�rs�k igen");
                choice = getInteger();
            }
        }
    }

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

    public int getInteger() {
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

    public int getInteger(String msg) {
        System.out.println(msg);
        return getInteger();
    }

    public void println(String str) {
        System.out.println(str);
    }

    public String getLine() {
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }

    public String getLine(String msg) {
        System.out.print(msg);
        return getLine();
    }

    public double getDouble() {
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

    public double getDouble(String msg) {
        System.out.print(msg);
        return getDouble();
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

    public void rabattMeny() {
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
                kampanjer.taBortRabatt(getLine("Skriv in koden f�r rabatten du vill ta bort: "));
                break;
            case SKRIV_UT:
                println(kampanjer.skrivUtRabatt(getLine("Skriv in koden f�r rabatten du vill skriva ut: ")));
                break;
            case SKRIV_UT_ALLA:
                println(kampanjer + "");
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
                kampanjer.nyRabatt(new K�pXF�rY(getLine(INPUT_BESK), getLine(INPUT_KOD),
                                                getProdukt("Produktens EAN-kod: "), getInteger("Antal X:"),
                                                getInteger("Antal Y: "), skrivDatum(INPUT_DATUM)));
                return;
            case XF�RYOMZ:
                kampanjer.nyRabatt(new K�pXf�rYOmZ(getLine(INPUT_BESK), getLine(INPUT_KOD),
                                                   getProdukt("Produkt X EAN-kod: "),
                                                   getProdukt("Produkt Zs EAN-kod: "),
                                                   getDouble("Rabattpris f�r Produkt X: "), skrivDatum(INPUT_DATUM)));
                return;
            case XOMY:
                kampanjer.nyRabatt(new XRabattOmY(getLine(INPUT_BESK), getLine(INPUT_KOD),
                                                  getProdukt("Produktens EAN-kod: "),
                                                  getDouble("Produktens rabatterade pris: "),
                                                  getDouble("Minsta summa att handla f�r: "),
                                                  skrivDatum(INPUT_DATUM)));
                return;
            case XPROCENT:
                kampanjer.nyRabatt(new Procentavdrag(getLine(INPUT_BESK), getLine(INPUT_KOD),
                                                     getDouble("Rabatt i procent (ex 5% rabatt = 0,05): "),
                                                     skrivDatum(INPUT_DATUM)));
                return;
            default:
                System.out.println("Ogiltigt val.");
                break;
            }
        }
    }

    private Date skrivDatum(String msg) {
        System.out.print(msg);
        return getDatum(getLine());
    }

    private Date getDatum(String datum) {
        final String DEFAULT_DATE = "1970-01-01";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return format.parse(datum);
        } catch (ParseException e) {
            System.out.println("Exception" + e);
            return getDatum(DEFAULT_DATE);
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
}
