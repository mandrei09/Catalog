//Clasa creata pentru a instantia obiecte de tip locatie
package Utilitare;
//care retin date despre locatia unor persoane.
import java.util.InputMismatchException;
import java.util.Scanner;
public class Locatie {
    //Date membre
    private int numar;
    private String strada;
    private String codPostal;
    private String oras,judet, tara;

    //Constructori
    public Locatie() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.print("\n\tCod Postal:");
            this.codPostal = sc.nextLine();
            if (!validatorCodPostal(codPostal))
                System.out.print("\n\t!!!Codul postal trebuie sa fie format din 6 cifre!!!\n");
            else
                break;
        }
        System.out.print("\tTara:"); this.tara=sc.nextLine();
        System.out.print("\tJudetul:"); this.judet=sc.nextLine();
        System.out.print("\tOrasul:"); this.oras=sc.nextLine();
        System.out.print("\tStrada:"); this.strada= sc.nextLine();

        while(true){
            try{
                System.out.print("\tNumar:"); this.numar= sc.nextInt();
                break;
            }
            catch (InputMismatchException e) {
                System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                sc.next();
            }
        }

    }

    //Codul postal este format din 6 caractere, toate cifre.
    boolean validatorCodPostal(String codPostal) {
        return codPostal.matches("[0-9]{6}");
    }

    //Constructori
    public Locatie(String tara,String judet,String oras,String strada,int numar,String codPostal) {
        this.numar = numar;
        this.strada = strada;
        if (validatorCodPostal(codPostal))
            this.codPostal = codPostal;
        else
            this.codPostal = "NULL";
        this.judet = judet;
        this.tara = tara;
        this.oras=oras;
    }

    public Locatie(Locatie l){
        this.numar = l.numar;
        this.strada = l.strada;
        if (validatorCodPostal(l.codPostal))
            this.codPostal = l.codPostal;
        else
            this.codPostal = "NULL";
        this.judet = l.judet;
        this.tara = l.tara;
        this.oras= l.oras;
    }

    @Override
    public String toString() {
        return "\t("+codPostal+") - " + tara + ", judetul " + judet + ", " + oras +
            ", pe strada " + strada + ", la numarul " + numar + ".\n";}

    public static void main(String[] args) {
    }
}
