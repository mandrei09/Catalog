//Librarie pentru a prelua data curenta din sistem.
package Persoane;
import Utilitare.Functii;
import Utilitare.DataNastere;
import Utilitare.Locatie;
import java.text.SimpleDateFormat;
import java.util.Scanner;

//Clasa de baza
public abstract class Persoana implements Functii {

    //Date membre
    protected String nume,prenume;
    protected char initialaTata;
    protected int varsta;
    protected DataNastere dataNastere;
    protected Locatie resedinta;
    protected String CNP,numarTelefonMobil;

    //Regex pentru a verifica daca numarul de telefon mobil incepe cu 07,
    //iar in rest sunt 8 caractere, toate cifre.
    boolean validatorNumarTelefon(String numarTelefon){
        return numarTelefon.matches("(07)[0-9]{8}");
    }

    //Functie care calculeaza varsta unei persoane.
    int calculVarsta() {
        String dataSiOra = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        int varsta=Integer.parseInt(dataSiOra.substring(0,4))-dataNastere.getAn();
        if(Integer.parseInt(dataSiOra.substring(5,7))<dataNastere.getLuna())
            varsta--;
        else
        if(Integer.parseInt(dataSiOra.substring(5,7))==dataNastere.getLuna())
            if(Integer.parseInt(dataSiOra.substring(8,10))<dataNastere.getZi())
                varsta--;
        return varsta;
    }

    //Contructori

//    public Persoane.Persoana() {
//    }

    public Persoana(){
        Scanner sc = new Scanner(System.in);
        System.out.print("\nNume: "); this.nume= sc.nextLine();
        System.out.print("Prenume: "); this.prenume=sc.nextLine();
        System.out.print("Initiala tatalui: "); this.initialaTata=sc.next().charAt(0);
        System.out.print("Data nasterii: "); this.dataNastere=new DataNastere();
        this.varsta=calculVarsta();
        System.out.print("Locatie: "); this.resedinta=new Locatie(); sc.nextLine();
        while(true){
            System.out.print("CNP: "); this.CNP= sc.nextLine();
            if(Functii.validatorCNP(CNP)!=1)
                System.out.println("\n\t!!!Structura CNP-ului este eronata!!!\n");
            else
                break;
        }
        while(true){
            System.out.print("Numar de telefon: "); this.numarTelefonMobil= sc.nextLine();
            if(!validatorNumarTelefon(numarTelefonMobil))
                System.out.println("\n\t!!!Structura numarului de telefon este eronata!!!\n");
            else
                break;
        }
        System.out.println();
    }


    public Persoana(String nume, String prenume,char initialaTata,DataNastere dataNastere,Locatie resedinta,String CNP,String numarTelefonMobil){
        this.nume=nume;
        this.prenume=prenume;
        this.initialaTata=initialaTata;
        this.dataNastere=new DataNastere(dataNastere);
        this.resedinta=new Locatie(resedinta);
        if(Functii.validatorCNP(CNP)==1)
            this.CNP=CNP;
        else
            this.CNP="NULL";
        this.varsta=calculVarsta();
        if(validatorNumarTelefon(numarTelefonMobil))
            this.numarTelefonMobil=numarTelefonMobil;
        else
            this.numarTelefonMobil="NULL";
    }

    public String getCNP() {
        return CNP;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    @Override
    public String toString() {
        return "\t" + nume + " " + initialaTata + ". " + prenume
                + "\nData nastere:" + dataNastere.toString()
                + "CNP: " + CNP + "\nResedinta:\n " + resedinta.toString()
                + "Varsta: " + varsta + "\nNumar de telefon: " + numarTelefonMobil + ".\n";
    }
    public static void main(String[] args) {
    }

}