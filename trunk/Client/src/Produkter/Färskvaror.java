package Produkter;

import java.text.SimpleDateFormat;

import java.util.Date;
/*
 * Objektet F�rskvaror �rver metoder ifr�n Produkt.
 * L�gger till attributet b�st f�re datum p� produkten.
 * F�rskvaror inneh�ller metoder som kan s�tta ett b�st f�re datum
 * samt h�mta ett b�st f�re datum.
 */
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

