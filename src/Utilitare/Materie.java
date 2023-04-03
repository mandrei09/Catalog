//Clasa pentru a definii obiecte de tip materie, corespunzatoare atat claselor de tip
//elev, cat si celor de tip student.
package Utilitare;

import java.util.Scanner;

public class Materie {
    private String denumire;
    private int pondere = 1; //toate materiile de pana la facultate au aceeasi pondere

    //Constructori
    public Materie(){
        Scanner sc = new Scanner(System.in);
        System.out.print("\tDenumirea materiei: "); this.denumire=sc.nextLine();
    }

    public Materie(String denumire, int pondere) {
        this.denumire = denumire;
        this.pondere = pondere;
    }

    //Getters + setteres
    public Materie(String denumire){
        this.denumire=denumire;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setPondere(int pondere) {
        this.pondere = pondere;
    }

    public double getPondere() {
        return pondere;
    }

    @Override
    public String toString(){
        return "\n\tMateria " + denumire + " cu ponderea " + pondere + ".\n";
    }

    public static void main(String[] args){
    }
}
