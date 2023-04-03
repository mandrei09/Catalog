/*Un tip de elev

Persoana(DataNastere,Locatie)
    Elev(Locatie,Materie)
        ElevScoalaGenerala
*/
package Persoane;

import Utilitare.DataNastere;
import Utilitare.Locatie;
import Utilitare.Materie;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ElevScoalaGenerala extends Elev {

    //Date membre
    private double notaEN=-1.0;
    private double notaAdmitereLiceu=-1.0;

    //Definim metoda mentionata in clasa Elev.
    @Override
    double calculMedieAnuala(int clasa) {
        double suma=0;
        for(int i=0;i<note[clasa-1].length;i++)
            suma+=calculMedie(note[clasa-1][i]);
        return suma/note.length;
    }

    //Media de admitere la liceu - 20% din media 5-8 + 80% din media EN
    double calculMedieAdmitere(){
        if (notaEN!=-1.0)
            return ((medii[4]+medii[5]+medii[6]+medii[7])/4.0)*0.2 + 0.8*notaEN;
        else
            return -1.0;
    }

    //Constructori
    public ElevScoalaGenerala(){
        super();
        Scanner sc = new Scanner(System.in);

        while(true){

            while(true){
                try{
                    System.out.print("\nClasa: "); this.clasa=sc.nextInt(); sc.nextLine();
                    break;
                }
                catch (InputMismatchException e) {
                    System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                    sc.next();
                }
            }

            if (clasa>0 && clasa <9)
                break;
            else
                System.out.print("\n\t!!!Elevul trebuie sa fie intr-o clasa de la 1 la 8!!!\n");
        }


        String input;
        while(true)
            if (clasa==8) {
                System.out.print("[yes/no] S-a obtinut nota la EN?: ");
                input = (sc.nextLine()).toLowerCase();
                if (input.equals("yes")) {

                    while(true){
                        try{
                            System.out.print("Nota la EN: ");
                            this.notaEN = sc.nextInt();
                            sc.nextLine();
                            break;
                        }
                        catch (InputMismatchException e) {
                            System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                            sc.next();
                        }
                    }

                    break;
                }
                else
                    if (input.equals("no"))
                        break;
                    else
                        System.out.print("\n\t!!!Input eronat!!!\n\n");
            }
            else
                break;

        materii=new Materie[clasa][21];
        note=new int[clasa][21][11];
        medii=new double[clasa];
        int numarMaterii,numarNote;
        for(int i=0;i<clasa;i++){

            while(true){
                try{
                    System.out.print("Dati numarul de materii din clasa a " + (i+1) + "-a: ");
                    numarMaterii=sc.nextInt(); sc.nextLine();
                    break;
                }
                catch (InputMismatchException e) {
                    System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                    sc.next();
                }
            }


            if(numarMaterii!=0)
                System.out.println("Dati materiile din clasa a " + (i+1) + "-a: ");
            for(int j=0;j<numarMaterii;j++){
                System.out.print("\t");
                materii[i][j]=new Materie();
            }
            for(int j=0;j<numarMaterii;j++) {

                while(true){
                    try{
                        System.out.print("Dati numarul de note de la " + materii[i][j].getDenumire() + ": ");
                        numarNote=sc.nextInt();
                        break;
                    }
                    catch (InputMismatchException e) {
                        System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                        sc.next();
                    }
                }

                if(numarNote!=0)
                    System.out.print("Dati notele de la " + materii[i][j].getDenumire() + ": ");
                for (int k = 0; k <numarNote; k++)
                    while(true){
                        try{
                            note[i][j][k] = sc.nextInt();
                            break;
                        }
                        catch (InputMismatchException e) {
                            System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                            sc.next();
                        }
                    }
            }
        }
        medii[clasa-1]=calculMedieAnuala(clasa);
        notaAdmitereLiceu=calculMedieAdmitere();
    }

    public ElevScoalaGenerala(String nume, String prenume, char initialaTata, DataNastere dataNastere,
                              Locatie resedinta, String CNP, String numarTelefonMobil,
                              String numeScoala,
                              int clasa, double notaEN,
                              double notaAdmitereLiceu, Materie [][] materii, int [][][] note, double[] medii){
        super(nume,prenume,initialaTata,dataNastere,resedinta,CNP,numarTelefonMobil,numeScoala,clasa);
        if (this.clasa==8)
            this.notaEN=notaEN;
        this.notaAdmitereLiceu=notaAdmitereLiceu;
        this.materii=materii;
        this.note=note;
        this.medii=medii;
    }

    //Returneaza lista materiilor dintr-o anumita clasa specificata in parametru
    public StringBuilder listaMaterii(int clasa){
        if(clasa>=1 && clasa<=8) {
            StringBuilder lista = new StringBuilder();
            if (clasa != this.clasa)
                lista.append("Elevul " + nume + " " + prenume +" a avut in clasa a " + clasa + "-a urmatoarele materii: ");
            else
                lista.append("Elevul " + nume + " " + prenume +" are in clasa a " + clasa + "-a urmatoarele materii: ");
            int i = 0;
            while (i< materii[clasa-1].length && materii[clasa - 1][i]!= null) {
                lista.append(materii[clasa - 1][i++].getDenumire());
                lista.append(" ");
            }
            return lista;
        }
        else
            return new StringBuilder("\t\n!!!Trebuie furnizata o clasa intre 1-8!!!\n");
    }

    @Override
    public String toString(){
        if(notaEN==-1.0)
            return "\nScoala generala" + super.toString()
                    + "Clasa: " + clasa
                    + "\nElevul nu a dat inca E.N.\n"
                    + "\nElevul nu are inca media de intrare la liceu.\n";
        else
            return "\nScoala generala" + super.toString()
                    + "Clasa: " + clasa
                    + "\nNota la E.N.: " + notaEN
                    + "\nMedia de admitere la liceu: " + notaAdmitereLiceu + "\n";

    }

    public static void main(String[] args) {
    }
}
