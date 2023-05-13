/*Un tip de elev

Persoana(DataNastere,Locatie)
    Elev(Locatie,Materie)
        ElevScoalaGenerala
*/
package Persoane;

import Utilitare.*;

import javax.lang.model.element.ElementVisitor;
import java.sql.*;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ElevScoalaGenerala extends Elev{

    //Date membre
    private double notaEN=-1.0;
    private double notaAdmitereLiceu=-1.0;

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
            return ((calculMedieAnuala(5)+calculMedieAnuala(6)+calculMedieAnuala(7)+calculMedieAnuala(8))/4.0)*0.2 + 0.8*notaEN;
        else
            return -1.0;
    }

    //Create
    public void intoDB(){
        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();
        try
        {
            String sql = "INSERT INTO esg (nume, prenume, initiala_tata,data_nastere,varsta,resedinta,CNP,numar_telefon_mobil,nume_scoala,clasa,nota_EN,nota_admitere_liceu) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, this.nume);
            statement.setString(2,this.prenume);
            statement.setString(3, "" + this.initialaTata);
            statement.setDate(4, (Date) new java.sql.Date(this.dataNastere.getDate().getTime()));
            statement.setInt(5,this.varsta);

            int locationID = this.resedinta.intoDB();
            statement.setInt(6, locationID);

            statement.setString(7, this.CNP);
            statement.setString(8, this.numarTelefonMobil);
            statement.setString(9, this.numeScoala);
            statement.setInt(10, this.clasa);
            statement.setDouble(11, this.notaEN);
            statement.setDouble(12, this.notaAdmitereLiceu);

            int rowsInserted = statement.executeUpdate();
//            if (rowsInserted > 0) {
//                System.out.println("ElevScoalaGenerala A new row was inserted successfully!");
//            }
            AuditService.logAction(Operatii.CREATE,Clase.ELEV_SCOALA_GENERALA);
        }
        catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    //Read
    public static ElevScoalaGenerala fromDB(){
        Scanner sc = new Scanner(System.in);

        System.out.println("-------------------------------------");
        System.out.print("VA ROG INTRODUCETI CNP-UL ELEVULUI PENTRU CARE DORITI SA EFECTUATI OPERATII DE TIP READ DIN DB: ");
        String CNP ; CNP = sc.nextLine();
        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();
        int ID=0,clasa=0;
        try
        {
            boolean ok=false;
            String sql = "SELECT * FROM esg WHERE CNP=" + CNP;
            ResultSet rs= conn.createStatement().executeQuery(sql);

            while(rs.next()) {
                if(ok==false){
                    System.out.println("\nSE VOR AFISA DATELE ELEVULUI: ");
                    ok=true;
                }

                clasa=rs.getInt("clasa");
                ID=rs.getInt("ID");
                AuditService.logAction(Operatii.READ,Clase.ELEV_SCOALA_GENERALA);
                return new ElevScoalaGenerala(rs.getString("nume"),rs.getString("prenume"),
                        rs.getString("initiala_tata").charAt(0),DBFunctii.toDataNastere(rs.getDate("data_nastere")),
                        DBFunctii.fromDBLocatie(rs.getInt("resedinta")),CNP,rs.getString("numar_telefon_mobil"),
                        rs.getString("nume_scoala"),clasa,rs.getDouble("nota_EN"),rs.getDouble("nota_admitere_liceu"),
                        fromDBMaterii("esg",ID,clasa,0),fromDBNote("esg",ID,clasa,0));
            }
            if(ok==false)
                System.out.println("Nu exista o persoana in DB cu CNP-ul specificat!");
        }
        catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
        return null;
    }

    //Update
    public static void updateDB() throws SQLException {
        Scanner sc = new Scanner(System.in);
        int optiune;

        System.out.println("-------------------------------------");
        System.out.print("VA ROG INTRODUCETI CNP-UL ELEVULUI PENTRU CARE DORITI SA EFECTUATI OPERATII DE TIP UPDATE IN DB: ");
        String CNP ; CNP = sc.nextLine();

        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();

        if(existaInDB(conn,CNP,"esg")>0)
        {
            String[] coloane = {"nume", "prenume", "initiala_tata", "data_nastere","", "resedinta", "CNP",
                    "numar_telefon_mobil", "nume_scoala", "clasa", "nota_EN", "nota_admitere_liceu"};

            System.out.println("-------------------------------------");

            while (true)
            {
                System.out.println("VA ROG INTRODUCETI NUMARUL CORESPUNZATOR COLOANEI PE CARE DORITI SA O MODIFICATI: ");
                System.out.println("1.  Nume.");
                System.out.println("2.  Prenume.");
                System.out.println("3.  Initiala Tatalui.");
                System.out.println("4.  Data Nastere.");
                System.out.println("5.  Resedinta actuala.");
                System.out.println("6.  Schimbarea resedintei.");
                System.out.println("7.  CNP");
                System.out.println("8.  Numar De Telefon.");
                System.out.println("9.  Nume Scoala.");
                System.out.println("10. Clasa.");
                System.out.println("11. Nota E.N.");
                System.out.println("12. Nota Admitere Liceu.");

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
                if(optiune>0 && optiune<13)
                    break;
                else
                    System.out.println("\nOPTIUNE INVALIDA, VA ROG SELECTATI UNA DINTRE CELE DE MAI JOS.\n");
            }

            if(optiune==5){
                int idLocatie = DBFunctii.selecteazaLocatie(conn,CNP,"esg");
                DBFunctii.updateDBLocatie(idLocatie);
            }
            else
            {
                String sql = "update esg set " + coloane[optiune-1] + " = ? where CNP=?;";
                PreparedStatement st = conn.prepareStatement(sql);
                if(optiune==10 || optiune==6){
                    int numarNou;
                    while(true){
                        try{
                            System.out.print("VA ROG INTRODUCETI VALOAREA NOUA PENTRU COLOANA SELECTATA: ");
                            numarNou=sc.nextInt();
                            break;
                        }
                        catch (InputMismatchException e) {
                            System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                            sc.next();
                        }
                    }
                    if(optiune==6)
                        while(true)
                        {
                            if(DBFunctii.existaInDB(conn,numarNou,"locatie")>0)
                            {
                                st.setInt(1,numarNou);
                                break;
                            }
                            else
                                System.out.println("\nNU EXISTA IN DB LOCATIE CU ID-UL SPECIFICAT\n");
                            try{
                                System.out.print("VA ROG INTRODUCETI VALOAREA NOUA PENTRU COLOANA SELECTATA: ");
                                numarNou=sc.nextInt();
                                break;
                            }
                            catch (InputMismatchException e) {
                                System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                                sc.next();
                            }
                        }
                    else
                        st.setInt(1,numarNou);
                }
                else
                    if(optiune==11 || optiune==12) {
                        double numarNou;
                        while(true){
                            try{
                                System.out.print("VA ROG INTRODUCETI VALOAREA NOUA PENTRU COLOANA SELECTATA: ");
                                numarNou=sc.nextDouble();
                                break;
                            }
                            catch (InputMismatchException e) {
                                System.out.print("\n\t!!!Trebuie sa introduceti un numar real, de forma 9,81!!!\n\n");
                                sc.next();
                            }
                        }

                        st.setDouble(1,numarNou);
                    }
                    else
                        if(optiune==4) {
                            DataNastere d = new DataNastere(sc);
                            st.setDate(1, (Date) new java.sql.Date(d.getDate().getTime()));
                            DBFunctii.updateVarstaDB(conn,"esg",d,CNP);
                        }
                        else
                            if(optiune==7){
                                sc.nextLine();
                                String valoareNoua=sc.nextLine();
                                if(DBFunctii.existaCNP(conn,valoareNoua,"esg")==0){
                                    st.setString(1,valoareNoua);
                                }
                                else
                                    System.out.println("EXISTA IN DB DEJA UN ELEV CU CNP-UL SPECIFICAT");
                            }
                            else {
                                System.out.print("VA ROG INTRODUCETI VALOAREA NOUA PENTRU COLOANA SELECTATA: ");
                                sc.nextLine();
                                String valoareNoua=sc.nextLine();
                                st.setString(1,valoareNoua);
                            }
                st.setString(2,CNP);
                st.executeUpdate();
                AuditService.logAction(Operatii.UPDATE,Clase.ELEV_SCOALA_GENERALA);
                System.out.println("DATELE AU FOST ACTUALIZATE CU SUCCES!");
            }
        }

        else
            System.out.println("NU EXISTA IN DB ELEV CU CNP-UL SPECIFICAT");
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
//        medii=new double[clasa];

        int numarMaterii;
        int[][]numarNote = new int[clasa][21];
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
                materii[i][j]=new Materie(i+1);
            }

            for(int j=0;j<numarMaterii;j++) {

                while(true){
                    try{
                        System.out.print("Dati numarul de note de la " + materii[i][j].getDenumire() + ": ");
                        numarNote[i][j]=sc.nextInt();
                        break;
                    }
                    catch (InputMismatchException e) {
                        System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                        sc.next();
                    }
                }

                if(numarNote[i][j]!=0)
                    System.out.print("Dati notele de la " + materii[i][j].getDenumire() + ": ");
                for (int k = 0; k <numarNote[i][j]; k++)
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

//        medii[clasa-1]=calculMedieAnuala(clasa);
        notaAdmitereLiceu=calculMedieAdmitere();

        //Create
        intoDB();
        intoDBMaterii_Note("esg",numarNote,0);
        System.out.println("ELEVUL A FOST ADAUGAT IN DB!");
    }

    public ElevScoalaGenerala(String nume, String prenume, char initialaTata, DataNastere dataNastere,
                              Locatie resedinta, String CNP, String numarTelefonMobil,
                              String numeScoala,
                              int clasa, double notaEN,
                              double notaAdmitereLiceu, Materie [][] materii, int [][][] note){
        super(nume,prenume,initialaTata,dataNastere,resedinta,CNP,numarTelefonMobil,numeScoala,clasa);
        if (this.clasa==8)
            this.notaEN=notaEN;
        this.notaAdmitereLiceu=notaAdmitereLiceu;
        this.materii=materii;
        this.note=note;
//        this.medii=medii;
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
