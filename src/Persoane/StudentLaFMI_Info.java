/*Un tip de student

Persoana(DataNastere,Locatie)
    Elev(Locatie,Materie)
        ElevScoalaGenerala
        ElevLiceu
    StudentlaFMI(Locatie,Materie)
        StudentlaFMI_Info

*/
package Persoane;

import Utilitare.DataNastere;
import Utilitare.Locatie;
import Utilitare.Materie;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class StudentLaFMI_Info extends StudentLaFMI {
    protected static final String specializare = "Informatica";
    public StudentLaFMI_Info(){
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
                System.out.print("\n\t!!!La specializarea informatica se fac 3 ani!!!\n");
        }

        //Determinarea grupei si seriei.

        final int[][] grupe = {{131,132,133,134,141,142,143,144,151,152,153,154},
                               {231,232,233,234,241,242,243,244,251,252,253,254},
                               {331,332,333,334,341,342,343,344,351,352,353,354}};

        final int[] Serii = {grupe[anStudiu-1][0]/10,grupe[anStudiu-1][4]/10,grupe[anStudiu-1][8]/10};
        final String serii = "[" + Serii[0] + "," + Serii[1] + "," + Serii[2] +"]";

        while(true){

            while(true){
                try{
                    System.out.print("\n" + serii +" Dati seria: ");
                    seria = sc.nextInt();
                    break;
                }
                catch (InputMismatchException e) {
                    System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                    sc.next();
                }
            }

            if(seria==Serii[0] || seria==Serii[1] || seria==Serii[2])
                break;
            else
                System.out.print("\n\t!!!Selectati una dintre " + serii + "\n\n");
        }

        final int startingIndex;
        if (seria%10==3)
            startingIndex=0;
        else if (seria%10==4) {
            startingIndex=4;
        }
        else
            startingIndex=8;

        final String grupeDinSerie = "[" + (grupe[anStudiu-1][startingIndex]) + ","
                + (grupe[anStudiu-1][startingIndex+1]) + ","
                + (grupe[anStudiu-1][startingIndex+2]) + ","
                + (grupe[anStudiu-1][startingIndex+3]) +"]";

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

            if(grupa == grupe[anStudiu-1][startingIndex]
                    || grupa==grupe[anStudiu-1][startingIndex+1]
                    || grupa==grupe[anStudiu-1][startingIndex+2]
                    || grupa==grupe[anStudiu-1][startingIndex+3])
                break;
            else
                System.out.print("\n\t!!!Selectati una dintre " + grupeDinSerie + "\n\n");
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
                        System.out.print("\tNumarul de credite al materiei: ");
                        cursuri[i][j].setPondere(sc.nextInt());
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
        intoDB("info");
        intoDBCursuri_Note("info");
        System.out.println("STUDENTUL A FOST ADAUGAT IN DB!");
    }
    public static void fromDBInfo(){
        fromDB("info");
    }

    public StudentLaFMI_Info(String nume, String prenume, char initialaTata, DataNastere dataNastere,
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
