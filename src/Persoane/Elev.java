//Clasa de baza pentru elev
/*
Persoana(DataNastere,Locatie)
    Elev(Locatie,Materie)
 */
package Persoane;
import Utilitare.Functii;
import Utilitare.DataNastere;
import Utilitare.Locatie;
import Utilitare.Materie;

import java.util.Scanner;
public abstract class Elev extends Persoana implements Functii {

    //Date membre
    protected String numeScoala;

    protected int clasa;
    protected Materie[][] materii;
    protected int[][][] note;

    protected double[] medii;

    //Constructori

//    public Persoane.Elev() {
//    }

    public Elev(){
        super();
        Scanner sc = new Scanner(System.in);
        System.out.print("Scoala: "); this.numeScoala= sc.nextLine();
    }

    public Elev(String nume, String prenume, char initialaTata, DataNastere dataNastere,
                Locatie resedinta, String CNP, String numarTelefonMobil,
                String numeScoala,int clasa){
        super(nume,prenume,initialaTata,dataNastere,resedinta,CNP,numarTelefonMobil);
        this.clasa=clasa;
        this.numeScoala=numeScoala;
    }

    //Getters
    public int getClasa() {
        return clasa;
    }

    public void setClasa(int clasa) {
        this.clasa = clasa;
    }

    public Materie[][] getMaterii() {
        return materii;
    }

    public int[][][] getNote() {
        return note;
    }

    @Override
    public String toString(){
        return "\nElevul " + super.toString()
                + "Scoala: " + numeScoala + "\n" ;
    }
    //Functie care calculeaza media la o singură materie
    double calculMedie(int[] note) {
        int suma=0;
        for (int j : note) suma += j;
        return suma*1.0/note.length;
    }

    //Functie care calculeaza media anuala
    abstract double calculMedieAnuala(int clasa);

    //Functie care calculeaza media de admitere la FMI.
    abstract double calculMedieAdmitere();
    public static void main(String[] args) {

    }

}
