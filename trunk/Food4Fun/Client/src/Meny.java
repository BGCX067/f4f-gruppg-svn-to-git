package food4fun;

import Försäljning.*;

import Produkter.*;

import java.text.*;

import java.util.*;

import Rabatter.*;

import java.io.Serializable;


public class Meny implements Serializable {
    ProduktRegister produktRegister;
    Försäljning försäljning;
    RabattRegister kampanjer;

    public static void main(String[] args) {
        Filhantering fil = new Filhantering();
        Kassasystem kassaregister;

        //Om kassaregister.dat existerar så laddas kassaregistret in.
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
        försäljning = kassaregister.getFörsäljning();
        kampanjer = kassaregister.getKampanjer();
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

    public void showMenu() {
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
                produktRegister.skrivUtProdukter();
                break;
            case RABATTMENY:
                rabattMeny();
                break;
            }
        }
    }

    public void beräknaKöpFörPeriod() {
        println("TOTAL FÖRSÄLJNING FÖR PERIOD:\n");
        println("Total försäljning: " +
                försäljning.totalFörsäljning(skrivDatum("startdatum "), skrivDatum("slutdatum ")) + "\n");
    }

    private void köpProdukter(Försäljning försäljning) {
        Köp köp = new Köp(kampanjer);
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
                produktRegister.skrivUtProdukter();
                break;
            default:
                if (produktRegister.checkEAN(ean)) {
                    println("Skriv in antal");
                    köp.läggTillVara(getInteger(), produktRegister.getProdukt(ean));
                    println(köp.skrivKvitto());
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

                if (kampanjer.finnsRabatt(rabatt)) {
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

    private void registreraProdukt() {
        final int PRODUKT = 1, FÄRSKVARA = 2;
        println("REGISTRERA PRODUKT\n1: Registrera Produkt\n2: Registrera Färskvara\n" +
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
                println("Skriv in pris på produkten");
                double pris = getDouble();

                produktRegister.addProdukt(new Produkt(eanNummer, beskrivning, pris));
                break;
            } else if (choice == FÄRSKVARA) {
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
                println("Skriv in pris på produkten");
                double pris = getDouble();
                println("Skriv in bäst före datum på produkten, YYYY-MM-DD");
                Date bästFöreDatum = getDatum(getLine());

                produktRegister.addProdukt(new Färskvaror(eanNummer, beskrivning, pris, bästFöreDatum));
                break;
            } else {
                println("Fel val, försök igen");
                choice = getInteger();
            }
        }
    }

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
                kampanjer.taBortRabatt(getLine("Skriv in koden för rabatten du vill ta bort: "));
                break;
            case SKRIV_UT:
                println(kampanjer.skrivUtRabatt(getLine("Skriv in koden för rabatten du vill skriva ut: ")));
                break;
            case SKRIV_UT_ALLA:
                println(kampanjer + "");
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
                kampanjer.nyRabatt(new KöpXFörY(getLine(INPUT_BESK), getLine(INPUT_KOD),
                                                getProdukt("Produktens EAN-kod: "), getInteger("Antal X:"),
                                                getInteger("Antal Y: "), skrivDatum(INPUT_DATUM)));
                return;
            case XFÖRYOMZ:
                kampanjer.nyRabatt(new KöpXförYOmZ(getLine(INPUT_BESK), getLine(INPUT_KOD),
                                                   getProdukt("Produkt X EAN-kod: "),
                                                   getProdukt("Produkt Zs EAN-kod: "),
                                                   getDouble("Rabattpris för Produkt X: "), skrivDatum(INPUT_DATUM)));
                return;
            case XOMY:
                kampanjer.nyRabatt(new XRabattOmY(getLine(INPUT_BESK), getLine(INPUT_KOD),
                                                  getProdukt("Produktens EAN-kod: "),
                                                  getDouble("Produktens rabatterade pris: "),
                                                  getDouble("Minsta summa att handla för: "),
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
                        "Försök igen. Avbryt med -1");
                ean = getInteger();
            }
        }

    }
}
