/*Un tip de elev

Persoana(DataNastere,Locatie)
    Elev(Locatie,Materie)
        ElevScoalaGenerala
        ElevLiceu

*/
package Persoane;

import Utilitare.DataNastere;
import Utilitare.Locatie;
import Utilitare.Materie;

import java.util.Scanner;
import java.util.InputMismatchException;
public class ElevLiceu extends Elev {

    //Date membre
    private double medieBAC =-1.0;
    private double notaInfo = -1.0, notaMatematica = -1.0;
    private final boolean olimpic;
    private double notaAdmitereFMI=-1.0;

    //Media se calculeaza la fel ca in scoala generala, dar difera indexarea.
    @Override
    double calculMedieAnuala(int clasa) {
        double suma=0;
        for(int i=0;i<note[clasa-9].length;i++)
            suma+=calculMedie(note[clasa-9][i]);
        return suma/note.length;
    }

    //Formula mediei de admitere la FMI (cand admiterea a fost consurs de dosare).
    double calculMedieAdmitere(){
        if (olimpic)
            medieBAC=10.0;
        else
            medieBAC=0.2*medieBAC+
                0.8*Math.min(10,(0.5*notaMatematica+0.5*notaInfo));
        return medieBAC;
    }

    //Constructori
    public ElevLiceu(){
        super();
        Scanner sc = new Scanner(System.in);

        while(true){

            while (true) {
                try {
                    System.out.print("\nClasa: "); this.clasa=sc.nextInt(); sc.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                    sc.next();
                }
            }

            if (clasa>8 && clasa <13)
                break;
            else
                System.out.print("\n\t!!!Elevul trebuie sa fie intr-o clasa de la 9 la 12!!!\n");
        }

        String input;
        while(true)
            if (clasa==12) {
                System.out.print("[yes/no] S-a obtinut nota la BAC?: ");
                input = (sc.nextLine()).toLowerCase();
                if (input.equals("yes")) {

                    while(true){
                        try{
                            System.out.print("Nota la BAC: ");
                            this.medieBAC = sc.nextInt();
                            break;
                        }
                        catch (InputMismatchException e) {
                            System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                            sc.next();
                        }
                    }

                    sc.nextLine();

                    while(true){
                        try{
                            System.out.print("Nota la matematica: ");
                            this.notaMatematica = sc.nextInt();
                            break;
                        }
                        catch (InputMismatchException e) {
                            System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                            sc.next();
                        }
                    }

                    sc.nextLine();

                    while(true){
                        try{
                            System.out.print("Nota la informatica: ");
                            this.notaInfo = sc.nextInt();
                            break;
                        }
                        catch (InputMismatchException e) {
                            System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                            sc.next();
                        }
                    }

                    sc.nextLine();
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

        while(true){
            System.out.print("[yes/no] Este elevul olimpic?: ");
            input = (sc.nextLine()).toLowerCase();
            if (input.equals("yes")) {olimpic=true; break;}
            else
                if (input.equals("no"))
                    {olimpic=false; break;}
                else
                    System.out.print("\n\t!!!Input eronat!!!\n\n");
        }

        materii=new Materie[clasa-8][21];
        note=new int[clasa-8][21][11];
        medii=new double[clasa-8];
        int numarMaterii,numarNote;
        for(int i=0;i<clasa-8;i++){

            while(true){
                try{
                    System.out.print("Dati numarul de materii din clasa a " + (i+9) + "-a: ");
                    numarMaterii=sc.nextInt(); sc.nextLine();
                    break;
                }
                catch (InputMismatchException e) {
                    System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                    sc.next();
                }
            }

            if(numarMaterii!=0)
                System.out.println("Dati materiile din clasa a " + (i+9) + "-a: ");
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
        medii[clasa-9]=calculMedieAnuala(clasa);
        notaAdmitereFMI=calculMedieAdmitere();
    }

    public ElevLiceu(String nume, String prenume, char initialaTata, DataNastere dataNastere,
                     Locatie resedinta, String CNP, String numarTelefonMobil,
                     String numeScoala,
                     int clasa, double medieBAC, double notaMatematica, double notaInfo,
                     boolean olimpic, double notaAdmitereFMI, Materie [][] materii, int [][][] note, double[] medii){
        super(nume,prenume,initialaTata,dataNastere,resedinta,CNP,numarTelefonMobil,numeScoala,clasa);
        if(this.clasa!=12){
            this.medieBAC = medieBAC;
            this.notaInfo=notaInfo;
            this.notaMatematica=notaMatematica;
            this.notaAdmitereFMI=notaAdmitereFMI;
        }

        this.olimpic=olimpic;
        this.materii=materii;
        this.note=note;
        this.medii=medii;
    }

    //Returneaza lista materiilor dintr-o anumita clasa specificata in parametru
    public StringBuilder listaMaterii(int clasa){
        if(clasa>=9 && clasa<=12){
            StringBuilder lista = new StringBuilder();
            if(clasa!=this.clasa)
                lista.append("Elevul " + nume + " " + prenume +" a avut in clasa a " + clasa + "-a urmatoarele materii: ");
            else
                lista.append("Elevul " + nume + " " + prenume +" are in clasa a " + clasa + "-a urmatoarele materii: ");
            int i=0;
            while(i< materii[clasa-9].length &&  materii[clasa-9][i]!=null) {
                lista.append(materii[clasa - 9][i++].getDenumire());
                lista.append(" ");
            }
            return lista;
        }
        else
            return new StringBuilder("\t\n!!!Trebuie furnizata o clasa intre 9-12!!!\n");
    }

    public String toString(){
        if(medieBAC == -1.0)
            return "\nLiceu" + super.toString()
                    + "Clasa: " + clasa
                    + "\nElevul nu a dat inca BAC-ul\n"
                    + "\nElevul nu are inca media de intrare la facultate.\n";
        else
            return "\nLiceu" + super.toString()
                    + "Clasa: " + clasa
                    + "\nNota la BAC.: " + medieBAC
                    + "\nMedia de admitere la facultate: " + notaAdmitereFMI + "\n";

    }

    public static void main(String[] args) {

    }
}
