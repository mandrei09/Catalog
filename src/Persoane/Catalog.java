/*
Clasa generica, un catalog este o grupare de mai multe persoane de acelasi fel
    Acestea sunt retinute intr-o structura de date de tip map.
    Persoanele sunt indexate dupa CNP.

    Catalog(
        Persoana(DataNastere,Locatie)
            Elev(Locatie,Materie)
                ElevScoalaGenerala
                ElevLiceu
            StudentlaFMI(Locatie,Materie)
                StudentlaFMI_Info
                StudentlaFMI_CTI
                StudentLaFMI_Matematica
    )
*/

package Persoane;

import Persoane.*;
import Utilitare.DBFunctii;
import Utilitare.JDBC;

import java.sql.SQLException;
import java.util.*;

public class Catalog<Tip> {
    private Map<String,Tip> lista = new HashMap<String,Tip>();
//    private int clasa;
    private int numarPersoane;

    private List<Integer> DBIndex = new ArrayList();

    //Constructori
    public Catalog(Class<Tip> tipClasa){
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                System.out.print("Dati numarul de persoane pe care le va contine catalogul: ");
                this.numarPersoane=sc.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                sc.next();
            }
        }

        System.out.print("Introduceti date in catalog: \n");

        //Catalogul contine mai multe persoane de acelasi tip
        //El memoreaza tipul in functie de prima persoana introdusa.

        for(int i=0;i<numarPersoane;i++){

            Tip obiectCurent;
            try {
                obiectCurent = tipClasa.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
//            if(i==0){
//                if(obiectCurent instanceof Elev) {
//                    if(obiectCurent instanceof ElevScoalaGenerala){
//                        while(true){
//                            while (true) {
//                                try {
//                                    System.out.print("\n[1 - 8] Introduceti clasa pentru care creati catalogul: ");
//                                    this.clasa=sc.nextInt(); sc.nextLine();
//                                    break;
//                                } catch (InputMismatchException e) {
//                                    System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
//                                    sc.next();
//                                }
//                            }
//                            if (clasa>0 && clasa <9)
//                                break;
//                            else
//                                System.out.print("\n\t!!!Elevul trebuie sa fie intr-o clasa de la 1 la 8!!!\n");
//                        }
//                    }
//                    else
//                        if(obiectCurent instanceof ElevLiceu){
//                            while(true){
//                                while (true) {
//                                    try {
//                                        System.out.print("\n[9 - 12] Introduceti clasa pentru care creati catalogul: ");
//                                        this.clasa=sc.nextInt(); sc.nextLine();
//                                        break;
//                                    } catch (InputMismatchException e) {
//                                        System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
//                                        sc.next();
//                                    }
//                                }
//                                if (clasa>8 && clasa <13)
//                                    break;
//                                else
//                                    System.out.print("\n\t!!!Elevul trebuie sa fie intr-o clasa de la 9 la 12!!!\n");
//                            }
//                        }
//                }
//                else
//                    if(obiectCurent instanceof StudentLaFMI){
//                        if(obiectCurent instanceof StudentLaFMI_Info || obiectCurent instanceof Persoane.StudentLaFMI_Matematica){
//                            while(true){
//                                while (true) {
//                                    try {
//                                        System.out.print("\nAn studiu: "); this.clasa=sc.nextInt(); sc.nextLine();
//                                        break;
//                                    } catch (InputMismatchException e) {
//                                        System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
//                                        sc.next();
//                                    }
//                                }
//                                if (clasa>0 && clasa <4)
//                                    break;
//                                else
//                                    System.out.print("\n\t!!!La acesta specializare se fac 3 ani!!!\n");
//                            }
//                        }
//                        else
//                            if(obiectCurent instanceof Persoane.StudentLaFMI_CTI){
//                                while(true){
//                                    while (true) {
//                                        try {
//                                            System.out.print("\nAn studiu: "); this.clasa=sc.nextInt(); sc.nextLine();
//                                            break;
//                                        } catch (InputMismatchException e) {
//                                            System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
//                                            sc.next();
//                                        }
//                                    }
//                                    if (clasa>0 && clasa <5)
//                                        break;
//                                    else
//                                        System.out.print("\n\t!!!La acesta specializare se fac 4 ani!!!\n");
//                                }
//                            }
//                    }
//            }
//            //Se adauga in lista de persoane obiectul (CNP Obiect - Obiect)
//            else
//                if(obiectCurent instanceof Elev)
//                    ((Elev) obiectCurent).setClasa(this.clasa);
//                else
//                    if(obiectCurent instanceof StudentLaFMI)
//                        ((StudentLaFMI) obiectCurent).setAnStudiu(this.clasa);
            String table="";
            if (obiectCurent instanceof ElevScoalaGenerala)
                table="esg";
            else
                if(obiectCurent instanceof ElevLiceu)
                    table="el";
                else
                    if(obiectCurent instanceof StudentLaFMI_Info)
                        table="info";
                    else
                        if(obiectCurent instanceof StudentLaFMI_Matematica)
                            table="matematica";
                        else
                            if(obiectCurent instanceof StudentLaFMI_CTI)
                                table="cti";

            try {
                DBIndex.add(DBFunctii.returnCNP(JDBC.getInstance().getConnection(),((Persoana)obiectCurent).getCNP(),table));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            lista.put(((Persoana) obiectCurent).getCNP(),obiectCurent);
        }
    }

    public Catalog(Map<String,Tip> lista) {
        this.lista = lista;
        this.numarPersoane = lista.size();

        Iterator<Map.Entry<String, Tip>> iterator = lista.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Tip> entry = iterator.next();

            Tip value = entry.getValue();

            String table="";
            if (value instanceof ElevScoalaGenerala)
                table="esg";
                else
                    if(value instanceof ElevLiceu)
                        table="el";
                    else
                        if(value instanceof StudentLaFMI_Info)
                            table="info";
                        else
                            if(value instanceof StudentLaFMI_Matematica)
                                table="matematica";
                            else
                                if(value instanceof StudentLaFMI_CTI)
                                    table="cti";

            try {
                DBIndex.add(DBFunctii.returnCNP(JDBC.getInstance().getConnection(),((Persoana)value).getCNP(),table));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
//        this.clasa = clasa;
    }

    //Getters

    public Map<String,Tip> getLista() {
        return lista;
    }

    public int getNumarPersoane() {
        return numarPersoane;
    }

    public List<Integer> getDBIndex(){return DBIndex;}

    //Functie care elimina o persoana din catalogul curent.
    public void eliminarePersoana(String CNP){
        if(!lista.containsKey(CNP))
            System.out.print("\n\t!!!Nu exista in catalog o persoana cu CNP-ul specificat!!!\n");
        else
            lista.remove(CNP);
    }



    public static void main(String[] args) {
//        Catalog<ElevScoalaGenerala> c = new Catalog<ElevScoalaGenerala>(ElevScoalaGenerala.class);
    }
}
