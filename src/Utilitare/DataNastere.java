//Clasa creata pentru a instantia obiecte care retin data nasterii unor persoane
package Utilitare;
import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.util.Scanner;

public final class DataNastere {
    //Date membre
    private int zi,luna,an;

    public DataNastere(){

    }

    //Constructori
    public DataNastere(Scanner sc) {
        sc = new Scanner(System.in);
        boolean ok;
        while (true) {
            ok = false;
            try {
                System.out.print("\tZiua: ");
                this.zi = sc.nextInt();
                ok = true;
            } catch (InputMismatchException e) {
                System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                sc.next();
            }

            if (ok)
                if ((0 > this.zi || this.zi > 31)) {
                    System.out.print("\n\t!!!Ziua trebuie sa fie un numar intreg intre 1 si 31!!!\n");
                } else break;
        }

        while (true) {
            ok = false;
            try {
                System.out.print("\tLuna: ");
                this.luna = sc.nextInt();
                ok = true;
            } catch (InputMismatchException e) {
                System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                sc.next();
            }

            if (ok)
                if (0 > this.luna || this.luna > 12) {
                    System.out.print("\n\t!!!Luna trebuie sa fie un numar intreg intre 1 si 12!!!\n\n");
                } else
                    break;
        }

        String dataSiOra = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        int anCurent = Integer.parseInt(dataSiOra.substring(0, 4));

        while (true) {
            ok = false;
            try {
                System.out.print("\tAnul: ");
                this.an = sc.nextInt();
                ok = true;
            } catch (InputMismatchException e) {
                System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                sc.next();
            }

            if (ok)
                if (this.an > anCurent) {
                    System.out.print("\n\t!!!Anul nu poate fi mai mare decat " + anCurent + "!!!\n\n");
                } else
                    break;
        }
    }
    public DataNastere(int zi,int luna,int an){
        this.zi=zi;
        this.luna=luna;
        this.an=an;
    }
    public Date getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String dateStr = String.format("%02d.%02d.%04d", this.zi, this.luna, this.an);

        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Error parsing date: " + dateStr);
            return null;
        }
    }

    public DataNastere(DataNastere n){
        this.zi=n.zi;
        this.luna=n.luna;
        this.an=n.an;
    }
    public int getZi() {
        return zi;
    }

    public int getLuna() {
        return luna;
    }

    public int getAn() {
        return an;
    }

    @Override
    public String toString() {
        return "\t" + zi + "/" + luna + "/" + an + "\n";
    }

    public static void main(String[] args) {

    }
}
