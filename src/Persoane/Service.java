/*
Clasa generica, aceasta contine operatiile sistemului


    Service -> Catalog(
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

import Utilitare.*;

import java.sql.SQLException;
import java.util.*;

public class Service
{
    private List<Persoane.Catalog> cataloage = new ArrayList<Persoane.Catalog>();
    public Service(){

    }

    public int getCataloageLength(){
        return cataloage.size();
    }

    //0. Crearea unor cataloage predefinite pentru testarea functiilor de sistem.
    public void creareCataloagePredefinite(){

        //Catalog 1

        DataNastere dataNastere1 = new DataNastere(9,12,2002);
        DataNastere dataNastere2 = new DataNastere(10,4,2005);

        Locatie locatie1 = new Locatie("Romania","Prahova","Brebu","Preot Vasile Nicolau",23,"107101");
        Locatie locatie2 = new Locatie("Romania","Galati","Galati","Morii",105,"142101");
        Locatie locatie3 = new Locatie("Romania","Prahova","Bobolia","Principala",23,"123456");

        Materie materie1 = new Materie("Romana"); materie1.intoDB(1); materie1.intoDB(2);
        Materie materie2 = new Materie("Matematica"); materie2.intoDB(1); materie2.intoDB(2);
        Materie materie3 = new Materie("Chimie"); materie3.intoDB(2);
        Materie[][] materiiElevScoalaGenerala= {{materie1,materie2},{materie1,materie2,materie3}};

        int[][][] noteElevScoalaGenerala ={{{10,10,8},{10,9}},
                {{10,6},{8,9},{5,6}}};
        int[][] numarNote={{3,2},{2,2,2}};

        ElevScoalaGenerala elevScoalaGenerala1 = new ElevScoalaGenerala("Mihai","Andrei",'C',dataNastere1,locatie1,
                "5021209295918","0726858494","Gimnaziala Matei Basarab",2,-1.0,-1.0,
                materiiElevScoalaGenerala,noteElevScoalaGenerala);

        elevScoalaGenerala1.intoDB();
        elevScoalaGenerala1.intoDBMaterii_Note("esg",numarNote,0);

        ElevScoalaGenerala elevScoalaGenerala2 = new ElevScoalaGenerala("Oprea","Leonard",'C',dataNastere2,locatie2,
                "5021209295919","0726858493","Gimnaziala Bobolia",2,-1.0,-1.0,
                materiiElevScoalaGenerala,noteElevScoalaGenerala);
        elevScoalaGenerala2.intoDB();
        elevScoalaGenerala2.intoDBMaterii_Note("esg",numarNote,0);

        Map<String,ElevScoalaGenerala> listaElevi1 = new HashMap<String,ElevScoalaGenerala>();
        listaElevi1.put("5021209295918",elevScoalaGenerala1);
        listaElevi1.put("5021209295919",elevScoalaGenerala2);

        Catalog<ElevScoalaGenerala> catalog1 = new Catalog<ElevScoalaGenerala>(listaElevi1);

        //Catalog 2

        Materie[][] materiiStudentLaInfo = new Materie[2][3];
        materiiStudentLaInfo[0][0]=new Materie("flp",5,1); materiiStudentLaInfo[0][0].intoDB(1);
        materiiStudentLaInfo[0][1]=new Materie("rc",2,1); materiiStudentLaInfo[0][1].intoDB(1);
        materiiStudentLaInfo[0][2]=new Materie("aa",3,1); materiiStudentLaInfo[0][2].intoDB(1);
        materiiStudentLaInfo[1][0]=new Materie("pao",5,2); materiiStudentLaInfo[1][0].intoDB(2);
        materiiStudentLaInfo[1][1]=new Materie("af",3,2); materiiStudentLaInfo[1][1].intoDB(2);
        materiiStudentLaInfo[1][2]=new Materie("mds",5,2); materiiStudentLaInfo[1][2].intoDB(2);

        int[][] n = {{5,6,7},{3,9,10}};
        StudentLaFMI_Info student1 = new StudentLaFMI_Info
                ("Oprea","Leonard",'C',dataNastere1,locatie3,"5021209295917","0726358494",10.0,true,1,14,141,materiiStudentLaInfo,n);
        StudentLaFMI_Info student2 = new StudentLaFMI_Info
                ("Cobeanu","Stefania",'F',dataNastere2,locatie1,"6021202295919","0726858394",9.0,false,1,15,152,materiiStudentLaInfo,n);
        student1.intoDB("info"); student2.intoDB("info");
        student1.intoDBCursuri_Note("info"); student2.intoDBCursuri_Note("info");

        Map<String,StudentLaFMI_Info> lista = new HashMap<String,StudentLaFMI_Info>();
        lista.put(student1.getCNP(),student1);
        lista.put(student2.getCNP(),student2);

        Catalog<StudentLaFMI_Info> catalog2 = new Catalog<StudentLaFMI_Info>(lista);

        //Adaugare cataloage create la lista.
        cataloage.add(catalog1); cataloage.add(catalog2);
    }

    //1. Crearea unui nou catalog cu date citite de la tastatura
    public void creareCatalog(){
        Scanner sc = new Scanner(System.in);
        int optiune;
        boolean ok=true;
        while (ok)
        {
            System.out.println("-------------------------------------");
            System.out.println("Ce fel de catalog vreti sa creati?");
            System.out.println("\t1.Pentru Elev scoala generala.");
            System.out.println("\t2.Pentru Elev liceu.");
            System.out.println("\t3.Pentru Student FMI la informatica.");
            System.out.println("\t4.Pentru Student FMI la matematica.");
            System.out.println("\t5.Pentru Student FMI la CTI.");
            System.out.println("\t\nPENTRU INTOARCERE LA PASUL PRECEDENT, APASATI TASTA 6");
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
                case 1 ->
                {
                    Persoane.Catalog<ElevScoalaGenerala> catalog = new Persoane.Catalog<>(ElevScoalaGenerala.class);
                    cataloage.add(catalog);
                    System.out.println("\n\tCATALOG ADAUGAT!");
                    ok=false;
                }
                case 2 ->
                {
                    Persoane.Catalog<ElevLiceu> catalog = new Persoane.Catalog<>(ElevLiceu.class);
                    cataloage.add(catalog);
                    System.out.println("\n\tCATALOG ADAUGAT!");
                    ok=false;
                }
                case 3 ->
                {
                    Persoane.Catalog<StudentLaFMI_Info> catalog = new Persoane.Catalog<>(StudentLaFMI_Info.class);
                    cataloage.add(catalog);
                    System.out.println("\n\tCATALOG ADAUGAT!");
                    ok=false;
                }
                case 4 ->
                {
                    Persoane.Catalog<Persoane.StudentLaFMI_Matematica> catalog = new Persoane.Catalog<>(Persoane.StudentLaFMI_Matematica.class);
                    cataloage.add(catalog);
                    System.out.println("\n\tCATALOG ADAUGAT!");
                    ok=false;
                }
                case 5 ->
                {
                    Persoane.Catalog<Persoane.StudentLaFMI_CTI> catalog = new Persoane.Catalog<>(Persoane.StudentLaFMI_CTI.class);
                    cataloage.add(catalog);
                    System.out.println("\n\tCATALOG ADAUGAT!");
                    ok=false;
                }
                case 6 ->
                {
                    ok=false;
                }
                default -> System.out.println("\nOPTIUNE INVALIDA, VA ROG SELECTATI UNA DINTRE CELE DE MAI JOS.\n");
            }
        }
    }

    //2. Afisarea datelor dintr-un anumit catalog.
    public void afisareDateCatalog(int numarCatalog){
        System.out.println("-------------------------------------");
        System.out.println("Index catalog: " + (numarCatalog));
        cataloage.get(numarCatalog).getLista().forEach((cheie, valoare) -> System.out.println(valoare));
        System.out.println("-------------------------------------");
    }

    //3. Afisarea tutoror cataloagelor existente in sistem.
    public void afisareDateToateCataloagele(){
        System.out.println("\n-------------------------------------");
        System.out.println("SE VOR AFISA DATELOR TUTUROR CATALOAGELOR!");
        for(int i=0;i<cataloage.size();i++){
            afisareDateCatalog(i);
        }
        System.out.println("-------------------------------------\n");
    }

    //4. Eliminarea unui catalog din lista.
    public void eliminareCatalog(int numarCatalog){
        System.out.println("\n-------------------------------------");
        System.out.println("SE VA ELIMINA DIN LISTA CATALOGUL CU INDEXUL " + numarCatalog + " !");
        //Eliminarea persoanelor din DB
        String table="";
        if(cataloage.get(numarCatalog).getLista().values().iterator().next() instanceof ElevScoalaGenerala) {
            table="esg";
        }
        else
            if(cataloage.get(numarCatalog).getLista().values().iterator().next() instanceof ElevLiceu){
                table="el";
            }
            else
                if(cataloage.get(numarCatalog).getLista().values().iterator().next() instanceof StudentLaFMI_Info){
                    table="info";
                }
                else
                    if(cataloage.get(numarCatalog).getLista().values().iterator().next() instanceof Persoane.StudentLaFMI_CTI){
                        table="cti";
                    }
                    else
                        if(cataloage.get(numarCatalog).getLista().values().iterator().next() instanceof Persoane.StudentLaFMI_Matematica){
                            table="matematica";
                        }
        List<Integer> listaID = cataloage.get(numarCatalog).getDBIndex();
        for(int i=0;i<listaID.size();i++) {
            try {
                Persoana.deleteID_DB(table,listaID.get(i));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        cataloage.remove(numarCatalog);

        System.out.println("-------------------------------------\n");
    }

    //5. Adaugarea unei persoane intr-un anumit catalog.
    public void adaugarePersoanaInCatalog(int numarCatalog){
        Persoana p;
        //Ne decidem care este tipul  persoanei pe care dorim sa o aduagam in functie de celelalte persoane
        if(cataloage.get(numarCatalog)
                .getLista().values().iterator().next() instanceof ElevScoalaGenerala) {
            p = new ElevScoalaGenerala();
            cataloage.get(numarCatalog).getLista().put(p.getCNP(), p);
        }
        else
            if(cataloage.get(numarCatalog)
                .getLista().values().iterator().next() instanceof ElevLiceu){
                p = new ElevScoalaGenerala();
                cataloage.get(numarCatalog).getLista().put(p.getCNP(),p);
            }
            else
                if(cataloage.get(numarCatalog)
                    .getLista().values().iterator().next() instanceof StudentLaFMI_Info){
                    p = new StudentLaFMI_Info();
                    cataloage.get(numarCatalog).getLista().put(p.getCNP(),p);
                }
                else
                    if(cataloage.get(numarCatalog)
                            .getLista().values().iterator().next() instanceof Persoane.StudentLaFMI_CTI){
                        p = new Persoane.StudentLaFMI_CTI();
                        cataloage.get(numarCatalog).getLista().put(p.getCNP(),p);
                    }
                    else
                        if(cataloage.get(numarCatalog)
                                .getLista().values().iterator().next() instanceof Persoane.StudentLaFMI_Matematica){
                            p = new Persoane.StudentLaFMI_Matematica();
                            cataloage.get(numarCatalog).getLista().put(p.getCNP(),p);
                        }
    }

    //6. Eliminarea unei persoane dintr-un anumit catalog.
    public void eliminarePersoanaDinCatalog(int numarCatalog,String CNP){
        System.out.println("\n-------------------------------------");
        System.out.println("SE VA ELIMINA DIN CATALOGUL CU INDEXUL " + numarCatalog
                + " persoana cu CNP-ul " + CNP + "." );
        System.out.println("DATELE PERSOANEI SUNT:");
        System.out.println(cataloage.get(numarCatalog).getLista().get(CNP));

        String table="";
        if(cataloage.get(numarCatalog).getLista().values().iterator().next() instanceof ElevScoalaGenerala) {
            table="esg";
        }
        else
            if(cataloage.get(numarCatalog).getLista().values().iterator().next() instanceof ElevLiceu){
                table="el";
            }
            else
                if(cataloage.get(numarCatalog).getLista().values().iterator().next() instanceof StudentLaFMI_Info){
                    table="info";
                }
                else
                    if(cataloage.get(numarCatalog).getLista().values().iterator().next() instanceof Persoane.StudentLaFMI_CTI){
                        table="cti";
                    }
                    else
                        if(cataloage.get(numarCatalog).getLista().values().iterator().next() instanceof Persoane.StudentLaFMI_Matematica){
                            table="matematica";
                        }
        try {
            Persoana.deleteID_CNP(table,CNP);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cataloage.get(numarCatalog).eliminarePersoana(CNP);
        System.out.println("-------------------------------------\n");
    }

    //7. Afisarea carnetului pentru un anumit elev.
    public void afisareCarnetElev(String CNP){
        System.out.println("\n-------------------------------------");
        System.out.println("SE VA AFISA CARNETUL ELEVULUI!");
        //Intai cautam elevul in cataloage.
        int ok=0;
        for(int i=0;i<cataloage.size();i++)
            if(cataloage.get(i).getLista().containsKey(CNP)){
                Object obiectCurent = cataloage.get(i).getLista().get(CNP);
                System.out.println(obiectCurent);
//                System.out.println(1);
                if(obiectCurent instanceof StudentLaFMI){
                    ((StudentLaFMI) obiectCurent)
                            .carnet(((StudentLaFMI) obiectCurent).getAnStudiu()
                            ,((StudentLaFMI) obiectCurent).getCursuri()
                                    ,((StudentLaFMI) obiectCurent).getNote());
                }
                else
                    if(obiectCurent instanceof Elev){
                        ((Elev) obiectCurent)
                                .carnet(((Elev) obiectCurent).getClasa()
                                        ,((Elev) obiectCurent).getMaterii()
                                ,((Elev) obiectCurent).getNote());
                    }
                ok=1;
                break;
            }
        if(ok==0)
            System.out.print("\n\t!!!In catalog nu este nimeni cu CNP-ul specificat!!!\n");
        System.out.println("\n-------------------------------------\n");
    }

    //8.Afisarea listei cursurilor unei persoane.
    public void afisareListaMaterii(String CNP,int clasa){
        System.out.println("\n-------------------------------------");
        System.out.println("SE VA AFISA LISTA MATERIILOR!");
        boolean ok=false;
        for(int i=0;i<cataloage.size();i++)
            if(cataloage.get(i).getLista().containsKey(CNP)){
                Object obiectCurent = cataloage.get(i).getLista().get(CNP);
                if(obiectCurent instanceof Elev)
                    if(clasa > ((Elev) obiectCurent).getClasa())
                    {
                        System.out.print("\n\t!!!Clasa introdusa este gresita!!!\n");
                        break;
                    }
                    else
                        if(obiectCurent instanceof ElevScoalaGenerala){
                            System.out.println(((ElevScoalaGenerala) obiectCurent).listaMaterii(clasa));
                            ok=true;
                            break;
                        }
                        else
                            if(obiectCurent instanceof ElevLiceu){
                                System.out.println(((ElevLiceu) obiectCurent).listaMaterii(clasa));
                                ok=true;
                                break;
                            }
                            else;
                else
                    if (obiectCurent instanceof StudentLaFMI)
                        if(clasa > ((StudentLaFMI) obiectCurent).getAnStudiu())
                        {
                            System.out.print("\n\t!!!Clasa introdusa este gresita!!!\n");
                            break;
                        }
                        else
                            if(obiectCurent instanceof StudentLaFMI_Info){
                                System.out.println(((StudentLaFMI_Info) obiectCurent).listaMaterii(clasa));
                                ok=true;
                                break;
                            }

                            else
                                if(obiectCurent instanceof Persoane.StudentLaFMI_Matematica){
                                    System.out.println(((Persoane.StudentLaFMI_Matematica) obiectCurent).listaMaterii(clasa));
                                    ok=true;
                                    break;
                                }
                                else
                                    if (obiectCurent instanceof Persoane.StudentLaFMI_CTI) {
                                        System.out.println(((Persoane.StudentLaFMI_CTI) obiectCurent).listaMaterii(clasa));
                                        ok = true;
                                        break;
                                    }
            }
        if(!ok)
            System.out.print("\n\t!!!In catalog nu este nimeni cu CNP-ul specificat!!!\n");
        System.out.println("-------------------------------------\n");
    }

    //9. Sortarea cataloagelor crescator / descrescator dupa numarul de persoane.
    public void sortareCataloage(int optiune){
        System.out.println("\n-------------------------------------");
        if(optiune==1) {
            System.out.println("SE VOR SORTA CATALOAGELE CRESCATOR DUPA NUMARUL DE PERSOANE!");
            Collections.sort(cataloage, new Utilitare.CatalogComparator(true));
        }
        else
            if(optiune==2) {
                System.out.println("SE VOR SORTA CATALOAGELE DESCRESCATOR DUPA NUMARUL DE PERSOANE!");
                Collections.sort(cataloage, new Utilitare.CatalogComparator(false));
            }
            else
                System.out.print("\n\t!!!Optiune eronata, sortarea a esuat!!!\n");
        System.out.println("-------------------------------------\n");
    }

    //10. Afisarea tuturor studentilor restanti.
    public void studentiRestanti(){
        System.out.println("\n-------------------------------------");
        System.out.println("SE VOR AFISA STUDENTII RESTANTI!");
        int numar=0;
        for(int i=0;i<cataloage.size();i++)
            //Verificam tipul catalogului - ne intereseaza doar cataloagele care contin studenti
            if(cataloage.get(i).getLista().values().iterator().next() instanceof StudentLaFMI)
                //Parcurgem vectorul notelor si cautam materiile pentru care nota<5
                cataloage.get(i).getLista().forEach((cheie, valoare)
                        -> {
                    if(valoare instanceof StudentLaFMI){
                        Materie[][] materii = ((StudentLaFMI) valoare).getCursuri();
                        int[][] note = ((StudentLaFMI) valoare).getNote();
                        for(int j=0;j<note.length;j++)
                            for(int k=0;k<note[j].length;k++)
                                if(note[j][k]<5){;
                                    System.out.println("(" + ((StudentLaFMI) valoare).getCNP() + ") Studentul " +
                                            ((StudentLaFMI) valoare).getNume()
                                            + " " + ((StudentLaFMI) valoare).getPrenume()
                                            + " este restantant la:" + materii[j][k]);
                                }
                    }
                });
        System.out.println("-------------------------------------\n");
    }

    //11. Afisarea datelor din DB ale unui elev/student dupa CNP.
    public Clase mainFromDB(){
        Scanner sc = new Scanner(System.in);
        int optiune;
        boolean ok=true;
        while (ok)
        {
            System.out.println("-------------------------------------");
            System.out.println("Ce ocupatie are aceasta persoana?");
            System.out.println("\t1. Elev - Scoala Generala.");
            System.out.println("\t2. Elev - Liceu.");
            System.out.println("\t3. Student FMI - Informatica.");
            System.out.println("\t4. Student FMI - Matematica.");
            System.out.println("\t5. Student FMI - CTI.");
            System.out.println("\t\nPENTRU INTOARCERE LA PASUL PRECEDENT, APASATI TASTA 6");
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
                case 1 ->
                {
                    System.out.println(ElevScoalaGenerala.fromDB());
                    ok=false; return Clase.ELEV_SCOALA_GENERALA;
                }
                case 2 ->
                {
                    System.out.println(ElevLiceu.fromDB());
                    ok=false; return Clase.ELEV_LICEU;
                }
                case 3 ->
                {
                    System.out.println(StudentLaFMI.fromDB("info"));
                    ok=false; return Clase.FMI_INFO;
                }
                case 4 ->
                {
                    System.out.println(StudentLaFMI.fromDB("matematica"));
                    ok=false; return Clase.FMI_MATEMATICA;
                }
                case 5 ->
                {
                    System.out.println(StudentLaFMI.fromDB("cti"));
                    ok=false; return Clase.FMI_CTI;
                }
                case 6 ->
                {
                    ok=false;
                }
                default -> System.out.println("\nOPTIUNE INVALIDA, VA ROG SELECTATI UNA DINTRE CELE DE MAI JOS.\n");
            }
        }
        return null;
    }

    //12. Actualizarea datelor din DB ale unui elev/student dupa CNP.
    public Clase mainUpdateDB(){
        Scanner sc = new Scanner(System.in);
        int optiune;
        boolean ok=true;
        while (ok)
        {
            System.out.println("-------------------------------------");
            System.out.println("Ce ocupatie are aceasta persoana?");
            System.out.println("\t1. Elev - Scoala Generala.");
            System.out.println("\t2. Elev - Liceu.");
            System.out.println("\t3. Student FMI - Informatica.");
            System.out.println("\t4. Student FMI - Matematica.");
            System.out.println("\t5. Student FMI - CTI.");
            System.out.println("\t\nPENTRU INTOARCERE LA PASUL PRECEDENT, APASATI TASTA 6");
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
                case 1 ->
                {
                    try {
                        ElevScoalaGenerala.updateDB();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    ok=false; return Clase.ELEV_SCOALA_GENERALA;
                }
                case 2 ->
                {
                    try {
                        ElevLiceu.updateDB();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    ok=false; return Clase.ELEV_LICEU;
                }
                case 3 ->
                {
                    try {
                        StudentLaFMI.updateDB("info");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    ok=false; return Clase.FMI_INFO;
                }
                case 4 ->
                {
                    try {
                        StudentLaFMI.updateDB("matematica");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    ok=false; return Clase.FMI_MATEMATICA;
                }
                case 5 ->
                {
                    try {
                        StudentLaFMI.updateDB("cti");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    ok=false; return Clase.FMI_CTI;
                }
                case 6 ->
                {
                    ok=false;
                }
                default -> System.out.println("\nOPTIUNE INVALIDA, VA ROG SELECTATI UNA DINTRE CELE DE MAI JOS.\n");
            }


        }
        return null;
    }

    public Clase mainDeleteDB(){
        Scanner sc = new Scanner(System.in);
        int optiune;
        boolean ok=true;
        while (ok)
        {
            System.out.println("-------------------------------------");
            System.out.println("Ce ocupatie are aceasta persoana?");
            System.out.println("\t1. Elev - Scoala Generala.");
            System.out.println("\t2. Elev - Liceu.");
            System.out.println("\t3. Student FMI - Informatica.");
            System.out.println("\t4. Student FMI - Matematica.");
            System.out.println("\t5. Student FMI - CTI.");
            System.out.println("\t\nPENTRU INTOARCERE LA PASUL PRECEDENT, APASATI TASTA 6");
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
                case 1 ->
                {
                    try {
                        Elev.deleteDB("esg");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    ok=false; return Clase.ELEV_SCOALA_GENERALA;
                }
                case 2 ->
                {
                    try {
                        Elev.deleteDB("el");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    ok=false; return Clase.ELEV_LICEU;
                }
                case 3 ->
                {
                    try {
                        StudentLaFMI.deleteDB("info");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    ok=false; return Clase.FMI_INFO;
                }
                case 4 ->
                {
                    try {
                        StudentLaFMI.deleteDB("matematica");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    ok=false; return Clase.FMI_MATEMATICA;
                }
                case 5 ->
                {
                    try {
                        StudentLaFMI.deleteDB("cti");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    ok=false; return Clase.FMI_CTI;
                }
                case 6 ->
                {
                    ok=false;
                }
                default -> System.out.println("\nOPTIUNE INVALIDA, VA ROG SELECTATI UNA DINTRE CELE DE MAI JOS.\n");
            }
        }
        return null;
    }

    public static void main(String[] args) {
    }
}

