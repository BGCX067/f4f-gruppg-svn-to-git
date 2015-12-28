package Produkter;

import java.text.SimpleDateFormat;

import java.util.Date;

public class F�rskvaror extends Produkt {
    private Date b�stF�reDatum;

    public F�rskvaror(int ean, String beskrinving, double pris, Date b�stF�reDatum) {
        super(ean, beskrinving, pris);
        this.b�stF�reDatum = b�stF�reDatum;
    }

    public void setDate(Date b�stF�reDatum) {
        this.b�stF�reDatum = b�stF�reDatum;
    }

    public Date getDate() {
        return b�stF�reDatum;
    }

    @Override
    public String toString() {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        return (super.toString() + "\t" + date.format(b�stF�reDatum));
    }
}

