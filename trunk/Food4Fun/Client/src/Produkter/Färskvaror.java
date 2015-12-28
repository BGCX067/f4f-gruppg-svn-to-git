package Produkter;

import java.text.SimpleDateFormat;

import java.util.Date;

public class Färskvaror extends Produkt {
    private Date bästFöreDatum;

    public Färskvaror(int ean, String beskrinving, double pris, Date bästFöreDatum) {
        super(ean, beskrinving, pris);
        this.bästFöreDatum = bästFöreDatum;
    }

    public void setDate(Date bästFöreDatum) {
        this.bästFöreDatum = bästFöreDatum;
    }

    public Date getDate() {
        return bästFöreDatum;
    }

    @Override
    public String toString() {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        return (super.toString() + "\t" + date.format(bästFöreDatum));
    }
}

