/*Clasa de baza pentru Student

Persoana(DataNastere,Locatie)
    Elev(Locatie,Materie)
        ElevScoalaGenerala
        ElevLiceu
    StudentLaFMI(Locatie,Materie)

*/

package Persoane;

import Utilitare.Functii;
import Utilitare.DataNastere;
import Utilitare.Locatie;
import Utilitare.Materie;

import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class StudentLaFMI extends Persoana implements Functii {

    //Date membre
    protected final String numeUniversitate = "Universitatea din Bucuresti"; ;
    protected final String numeFacultate = "Facultatea de Matematica si Informatica";
    protected final Locatie locatieFacultate = new Locatie("Romania","Bucuresti","Bucuresti",
            "Soseaua Panduri",90,"050663");
    protected int anStudiu;
    protected int seria;
    protected int grupa;
    protected Materie[][] cursuri;
    protected int[][] note;
    protected double[] medii;
    double medieAdmitere;
    boolean olimpic;

    //Constructori
    public StudentLaFMI(){
        super();
        Scanner sc = new Scanner(System.in);
        String input;
        while(true){
            System.out.print("[yes/no] Este studentul olimpic?: ");
            input = (sc.nextLine()).toLowerCase();
            if (input.equals("yes")) {
                olimpic=true;
                this.medieAdmitere=10.0;
                break;
            }
            else
                if (input.equals("no")) {
                    olimpic=false;
                    while(true){

                        while(true){
                            try{
                                System.out.print("\tMedia de admitere: ");
                                this.medieAdmitere=sc.nextDouble();
                                break;
                            }
                            catch (InputMismatchException e) {
                                System.out.print("\n\t!!!Media trebuie introdusa ca in exemplul urmator: 9,5 !!!\n\n");
                                sc.next();
                            }
                        }

                        if(this.medieAdmitere<0.0 || this.medieAdmitere>10.0)
                            System.out.print("\n\t!!!Media trebuie sa fie intre valoarile 0,0 si 10,0!!!\n\n");
                        else
                            break;
                    }
                    break;

                }
                else
                    System.out.print("\n\t!!!Input eronat!!!\n\n");
        }
    }

    public StudentLaFMI(String nume, String prenume, char initialaTata, DataNastere dataNastere,
                        Locatie resedinta, String CNP, String numarTelefonMobil, double medieAdmitere, boolean olimpic,
                        int anStudiu) {
        super(nume, prenume, initialaTata, dataNastere, resedinta, CNP, numarTelefonMobil);
        this.olimpic=olimpic;
        if(!this.olimpic)
            this.medieAdmitere=medieAdmitere;
        else
            this.medieAdmitere=10.0;
        this.anStudiu=anStudiu;
    }

    //Getters

    public int getAnStudiu() {
        return anStudiu;
    }

    public void setAnStudiu(int anStudiu) {
        this.anStudiu = anStudiu;
    }

    public Materie[][] getCursuri() {
        return cursuri;
    }

    public int[][] getNote() {
        return note;
    }

    @Override
    public String toString(){
        String o;
        if(olimpic)
            o = "Olimpic!";
        else
            o = "Nu este olimpic!";

        return "\nStudentul " + super.toString()
                + "Universitate: " + numeUniversitate
                + ".\nFacultatea: " + numeFacultate
                + ".\n\tDe la locatia: \n" +locatieFacultate.toString()
                + "Media de admitere: " + medieAdmitere + ".\n" + o +"\n"
                + "Anul de studii: " + anStudiu + ".\n";

        }

    double calculMedieAnuala(int anStudiu) {
        int sumaNote=0,sumaPonderi=0;
        int i=0;
        while(i<note[anStudiu-1].length && cursuri[anStudiu - 1][i]!=null) {
            sumaNote += note[anStudiu - 1][i] * cursuri[anStudiu - 1][i].getPondere();
            sumaPonderi += cursuri[anStudiu - 1][i].getPondere();
            i++;
        }
        return sumaNote*1.0/sumaPonderi;
    }

    public static void main(String[] args) {
    }
}
