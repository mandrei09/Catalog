/*
Clasa din care se fac apelurile sistemului

    Main -> Service -> Catalog(
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

import java.sql.SQLOutput;
import java.util.*;
import Persoane.*;
import Utilitare.*;

abstract public class Main implements Functii{
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Service service = new Service();
        int optiune;
        boolean ok=true;

        while(ok){
            System.out.println("-------------------------------------");
            System.out.println("VA ROG SELECTATI OPTIUNEA DIN MENIUL DE MAI JOS PRIN INTRODUCEREA NUMARULUI CORESPUNZATOR IN CONSOLA:");
            System.out.println("PROIECT CATALOG V1:");
            System.out.println("\t0.  Crearea unor cataloage predefinite pentru testarea functiilor de sistem.");
            System.out.println("\t1.  Crearea unui nou catalog cu date citite de la tastatura.");
            System.out.println("\t2.  Afisarea datelor dintr-un anumit catalog.");
            System.out.println("\t3.  Afisarea tutoror cataloagelor existente in sistem.");
            System.out.println("\t4.  Eliminarea unui catalog din lista.");
            System.out.println("\t5.  Adaugarea unei persoane intr-un anumit catalog.");
            System.out.println("\t6.  Eliminarea unei persoane dintr-un anumit catalog.");
            System.out.println("\t7.  Afisarea carnetului pentru un anumit elev.");
            System.out.println("\t8.  Afisarea listei cursurilor unei persoane.");
            System.out.println("\t9.  Sortarea cataloagelor crescator / descrescator dupa numarul de persoane.");
            System.out.println("\t10. Afisarea tuturor studentilor restanti.");
            System.out.println("PROIECT CATALOG V2");
            System.out.println("\t11. Afisarea datelor din DB ale unui elev/student dupa CNP.");
            System.out.println("\t12. Actualizarea datelor din DB ale unui elev/student dupa CNP.");
            System.out.println("\t13. Eliminarea unui elev/student din DB dupa CNP.");
            System.out.println("\t14. Parasirea aplicatiei.");
            System.out.println("-------------------------------------");

            while(true){
                try{
                    System.out.print("Optiune: ") ; optiune = sc.nextInt();
                    break;
                }
                catch (InputMismatchException e) {
                    System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                    sc.next();
                }
            }

            switch (optiune)
            {
                case 0 ->
                {
                    System.out.println("\tAti selectat : Crearea unor cataloage predefinite pentru testarea functiilor de sistem.");
                    System.out.println("\nS-AU DEFINIT 2 CATALOAGE:\n\t1)PENTRU ELEVI DE SCOALA GENERALA.\n\t2)PENTRU STUDENTI LA FMI - INFORMATICA.");
                    service.creareCataloagePredefinite();;
                    break;
                }

                case 1 ->
                {
                    System.out.println("Ati selectat : Crearea unui nou catalog cu date citite de la tastatura.");
                    service.creareCatalog();
                    break;
                }

                case 2 ->
                {
                    if(service.getCataloageLength()!=0){
                        int op;
                        System.out.println("\tAti selectat : Afisarea datelor dintr-un anumit catalog.");
                        while(true){

                            while(true){
                                try{
                                    System.out.println("Va rog introduceti un numar de la 0 la " + (service.getCataloageLength()-1) + ": ");
                                    op = sc.nextInt();
                                    break;
                                }
                                catch (InputMismatchException e) {
                                    System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                                    sc.next();
                                }
                            }

                            if (op>-1 && op<service.getCataloageLength()){
                                service.afisareDateCatalog(op);
                                break;
                            }
                            else
                                System.out.println("\nOPTIUNE INVALIDA!\n");
                        }
                    }
                    else
                        System.out.println("\nNU EXISTA NICIUN CATALOG MEMORAT IN SISTEM\n");
                }

                case 3 ->
                {
                    if(service.getCataloageLength()!=0) {
                        System.out.println("Ati selectat Afisarea tutoror cataloagelor existente in sistem.");
                        service.afisareDateToateCataloagele();
                    }
                    else
                        System.out.println("\nNU EXISTA NICIUN CATALOG MEMORAT IN SISTEM\n");
                }

                case 4 ->
                {
                    if(service.getCataloageLength()!=0) {
                        int op;
                        System.out.println("\tAti selectat : Eliminarea unui catalog din lista.");
                        while(true){

                            while(true){
                                try{
                                    System.out.print("Va rog introduceti un numar de la 0 la " + (service.getCataloageLength()-1) + ": ");
                                    op = sc.nextInt();
                                    break;
                                }
                                catch (InputMismatchException e) {
                                    System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                                    sc.next();
                                }
                            }

                            if (op>-1 && op<service.getCataloageLength()){
                                service.eliminareCatalog(op);
                                break;
                            }
                            else
                                System.out.println("\nOPTIUNE INVALIDA!\n");
                        }
                    }
                    else
                        System.out.println("\nNU EXISTA NICIUN CATALOG MEMORAT IN SISTEM\n");
                }

                case 5 ->
                {
                    if(service.getCataloageLength()!=0) {
                        int op;
                        System.out.println("\tAti selectat : Adaugarea unei persoane intr-un anumit catalog.");
                        while(true){

                            while(true){
                                try{
                                    System.out.println("Va rog introduceti un numar de la 0 la " + (service.getCataloageLength()-1) + ": ");
                                    op = sc.nextInt();
                                    break;
                                }
                                catch (InputMismatchException e) {
                                    System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                                    sc.next();
                                }
                            }

                            if (op>-1 && op<service.getCataloageLength()){
                                service.adaugarePersoanaInCatalog(op);
                                break;
                            }
                            else
                                System.out.println("\nOPTIUNE INVALIDA!\n");
                        }
                    }
                    else
                        System.out.println("\nNU EXISTA NICIUN CATALOG MEMORAT IN SISTEM\n");
                }

                case 6 ->
                {
                    if(service.getCataloageLength()!=0) {
                        int op=-1; String CNP; boolean ok6;
                        System.out.println("\tAti selectat : Eliminarea unei persoane dintr-un anumit catalog.");

                        while (true) {
                            ok6=false;
                            try {
                                System.out.print("Va rog introduceti un numar de la 0 la " + (service.getCataloageLength()-1) + ": ");
                                op = sc.nextInt(); sc.nextLine();
                                ok6=true;
                            }
                            catch (InputMismatchException e) {
                                System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                                sc.next();
                            }

                            if(ok6)
                                if ((op<0 || op>=service.getCataloageLength())) {
                                    System.out.println("\n\tOPTIUNE INVALIDA!\n");                                }
                                else
                                    break;
                        }

                        while(true){
                            System.out.print("Va rog introduceti CNP-ul persoanei pe care doriti sa o stergeti din catalog: ");
                            CNP = sc.nextLine();
                            if(Functii.validatorCNP(CNP)==1){
                                service.eliminarePersoanaDinCatalog(op,CNP);
                                break;
                            }
                            else
                                System.out.println("\n\t!!!Structura CNP-ului este eronata!!!\n");
                        }
                    }
                    else
                        System.out.println("\nNU EXISTA NICIUN CATALOG MEMORAT IN SISTEM\n");
                }

                case 7 ->
                {
                    if(service.getCataloageLength()!=0) {
                        String CNP;
                        System.out.println("\tAti selectat : Afisarea carnetului pentru un anumit elev.");
                        while(true){
                            System.out.print("Va rog introduceti clasa persoanei pentru care faceti solicitarea: ");
                            sc.nextLine(); CNP = sc.nextLine();
                            if(Functii.validatorCNP(CNP)==1){
                                service.afisareCarnetElev(CNP);
                                break;
                            }
                            else
                                System.out.println("\n\t!!!Structura CNP-ului este eronata!!!\n");
                        }
                    }
                    else
                        System.out.println("\nNU EXISTA NICIUN CATALOG MEMORAT IN SISTEM\n");
                }

                case 8 ->
                {
                    if(service.getCataloageLength()!=0) {
                        int clasa; String CNP;
                        System.out.println("\tAti selectat : Afisarea listei cursurilor unei persoane.");
                        while(true){

                            while(true){
                                try{
                                    System.out.print("Va rog introduceti clasa persoanei pentru care faceti solicitarea: ");
                                    clasa = sc.nextInt();
                                    break;
                                }
                                catch (InputMismatchException e) {
                                    System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                                    sc.next();
                                }
                            }

                            System.out.print("Va rog introduceti CNP-ul persoanei pe care doriti sa o stergeti din catalog: ");
                            sc.nextLine(); CNP = sc.nextLine();
                            if(Functii.validatorCNP(CNP)==1){
                                service.afisareListaMaterii(CNP,clasa);
                                break;
                            }
                            else
                                System.out.print("\n\t!!!Structura CNP-ului este eronata!!!\n");
                        }
                    }
                    else
                        System.out.println("\nNU EXISTA NICIUN CATALOG MEMORAT IN SISTEM\n");
                }

                case 9 ->
                {
                    if(service.getCataloageLength()!=0) {
                        System.out.println("\tAti selectat : Sortarea cataloagelor crescator / descrescator dupa numarul de persoane.");
                        int op;
                        while(true){

                            while(true){
                                try{
                                    System.out.println("Cum doriti sa sortati?\n\t(1) - Crescator \n\t(2) - Descrescator");
                                    System.out.print("[1/2] Optiune: ");
                                    op = sc.nextInt();
                                    break;
                                }
                                catch (InputMismatchException e) {
                                    System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                                    sc.next();
                                }
                            }

                            if (op==1 || op == 2){
                                service.sortareCataloage(op);
                                break;
                            }
                            else
                                System.out.println("\nOPTIUNE INVALIDA!\n");
                        }
                    }
                    else
                        System.out.println("\nNU EXISTA NICIUN CATALOG MEMORAT IN SISTEM\n");
                }

                case 10 ->
                {
                    if(service.getCataloageLength()!=0) {
                        System.out.println("\tAti selectat : Afisarea tuturor studentilor restanti.");
                        service.studentiRestanti();
                    }
                    else
                        System.out.println("\nNU EXISTA NICIUN CATALOG MEMORAT IN SISTEM\n");
                }

                case 11 ->
                {
                    service.mainFromDB();
                }

                case 12 ->
                {
                    service.mainUpdateDB();
                }

                case 13 ->
                {
                    service.mainDeleteDB();
                }

                case 14 ->
                {
                    System.out.println("\nMultumesc pentru ca mi-ati folosit programul, o zi buna! :)");
                    ok=false;
                    break;
                }
                default -> System.out.println("\nOPTIUNE INVALIDA, VA ROG SELECTATI UNA DINTRE CELE DE MAI JOS.\n");
            }
        }
    }
}