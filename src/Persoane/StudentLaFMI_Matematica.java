/*Un tip de student

Persoana(DataNastere,Locatie)
    Elev(Locatie,Materie)
        ElevScoalaGenerala
        ElevLiceu
    StudentlaFMI(Locatie,Materie)
        StudentlaFMI_Info
        StudentlaFMI_CTI
        StudentLaFMI_Matematica

*/
package Persoane;

import Utilitare.DataNastere;
import Utilitare.Locatie;
import Utilitare.Materie;

import java.util.InputMismatchException;
import java.util.Scanner;

class StudentLaFMI_Matematica extends StudentLaFMI {
    protected static final String specializare = "Matematica";
    public StudentLaFMI_Matematica(){
        super();
        Scanner sc = new Scanner(System.in);

        while(true){

            while(true){
                try{
                    System.out.print("\nAn studiu: "); this.anStudiu=sc.nextInt(); sc.nextLine();
                    break;
                }
                catch (InputMismatchException e) {
                    System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                    sc.next();
                }
            }

            if (anStudiu>0 && anStudiu <4)
                break;
            else
                System.out.print("\n\t!!!La specializarea matematica se fac 3 ani!!!\n");
        }

        final int[][] grupe = {{101,102,103,104},
                {201,202,203,204},
                {301,302,303,304}};

        this.seria=anStudiu*10;

        final String grupeDinSerie = "[" + (grupe[anStudiu-1][0]) + ","
                + (grupe[anStudiu-1][1]) + ","
                + (grupe[anStudiu-1][2]) + ","
                + (grupe[anStudiu-1][3]) +"]";

        while(true){

            while(true){
                try{
                    System.out.print("\n" + grupeDinSerie +" Dati grupa: ");
                    grupa = sc.nextInt();
                    break;
                }
                catch (InputMismatchException e) {
                    System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                    sc.next();
                }
            }

            if(grupa == grupe[anStudiu-1][0]
                    || grupa==grupe[anStudiu-1][1]
                    || grupa==grupe[anStudiu-1][2]
                    || grupa==grupe[anStudiu-1][3])
                break;
            else
                System.out.print("\n\t!!!Selectati una dintre " + grupeDinSerie + "\n");
        }

        cursuri=new Materie[anStudiu][21];
        note=new int[anStudiu][21];
//        medii=new double[anStudiu];
        int numarMaterii;
        for(int i=0;i<anStudiu;i++){

            while(true){
                try{
                    System.out.print("Dati numarul de materii din anul " + (i+1) + ": ");
                    numarMaterii=sc.nextInt(); sc.nextLine();
                    break;
                }
                catch (InputMismatchException e) {
                    System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                    sc.next();
                }
            }

            if(numarMaterii!=0)
                System.out.println("Dati materiile din anul " + (i+1));
            for(int j=0;j<numarMaterii;j++){
                cursuri[i][j]=new Materie(i+1);

                while(true){
                    try{
                        System.out.print("\tNumarul de credite al materiei: "); cursuri[i][j].setPondere(sc.nextInt());
                        break;
                    }
                    catch (InputMismatchException e) {
                        System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                        sc.next();
                    }
                }

            }
            for(int j=0;j<numarMaterii;j++) {

                while(true){
                    try{
                        System.out.print("Dati nota de la " + cursuri[i][j].getDenumire() + ": ");
                        note[i][j] = sc.nextInt();
                        break;
                    }
                    catch (InputMismatchException e) {
                        System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                        sc.next();
                    }
                }

            }
        }
//        medii[anStudiu-1]=calculMedieAnuala(anStudiu);
        intoDB("matematica");
        intoDBCursuri_Note("matematica");
        System.out.println("STUDENTUL A FOST ADAUGAT IN DB!");
    }

    public static void fromDBMatematica(){
        fromDB("matematica");
    }

    public StudentLaFMI_Matematica(String nume, String prenume, char initialaTata, DataNastere dataNastere,
                             Locatie resedinta, String CNP, String numarTelefonMobil,double medieAdmitere,
                             boolean olimpic, int anStudiu,int seria,int grupa,Materie[][] cursuri, int[][] note) {
        super(nume, prenume, initialaTata, dataNastere, resedinta, CNP, numarTelefonMobil,medieAdmitere,olimpic,anStudiu);
        this.cursuri=cursuri;
        this.note=note;
//        this.medii=medii;
        this.seria=seria;
        this.grupa=grupa;
    }

    @Override
    public String toString() {
        return "\nSpecializarea: " + specializare + super.toString()
                + "Seria: " + seria + ". Grupa: " + grupa + ".\n";
    }

    public StringBuilder listaMaterii(int anStudiu){
        if(anStudiu>=1 && anStudiu<=3){
            StringBuilder lista = new StringBuilder();
            if(anStudiu!=this.anStudiu)
                lista.append("Studentul " + nume + " " + prenume +" a avut in anul " + anStudiu + " urmatoarele materii: ");
            else
                lista.append("Studentul " + nume + " " + prenume +" are in anul " + anStudiu + " urmatoarele materii: ");
            int i=0;
            while(i<cursuri[anStudiu-1].length && cursuri[anStudiu-1][i]!=null) {
                lista.append(cursuri[anStudiu-1][i++].getDenumire());
                lista.append(" ");
            }
            return lista;
        }
        else
            return new StringBuilder("\t\n!!!Trebuie furnizata un an intre 1 si 3!!!\n");
    }

    public static void main(String[] args) {
    }
}
