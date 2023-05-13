/*Un tip de elev

Persoana(DataNastere,Locatie)
    Elev(Locatie,Materie)
        ElevScoalaGenerala
        ElevLiceu

*/
package Persoane;

import Utilitare.*;

import java.sql.*;
import java.util.Scanner;
import java.util.InputMismatchException;
public class ElevLiceu extends Elev {

    //Date membre
    private double medieBAC =-1.0;
    private double notaInfo = -1.0, notaMatematica = -1.0;
    private boolean olimpic;
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
            notaAdmitereFMI=10.0;
        else
            notaAdmitereFMI=0.2*medieBAC+
                0.8*Math.min(10,(0.5*notaMatematica+0.5*notaInfo));
        return notaAdmitereFMI;
    }

    double calculMedieBac(){
        return (notaInfo+notaMatematica)/2.0;
    }

    private void intoDB(){
        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();
        try
        {
            String sql = "INSERT INTO el (nume, prenume, initiala_tata,data_nastere,varsta,resedinta,CNP,numar_telefon_mobil,nume_scoala,clasa,medie_BAC,nota_matematica,nota_info,olimpic,nota_admitere_FMI) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            statement.setDouble(11, this.medieBAC);
            statement.setDouble(12, this.notaMatematica);
            statement.setDouble(13, this.notaInfo);
            statement.setBoolean(14, this.olimpic);
            statement.setDouble(15, this.notaAdmitereFMI);

            int rowsInserted = statement.executeUpdate();
            AuditService.logAction(Operatii.CREATE,Clase.ELEV_LICEU);
//            if (rowsInserted > 0) {
//                System.out.println("ElevLiceu A new row was inserted successfully!");
//            }
        }
        catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public static ElevLiceu fromDB(){
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
            String sql = "SELECT * FROM el WHERE CNP=" + CNP;
            ResultSet rs= conn.createStatement().executeQuery(sql);

            while(rs.next()) {
                if(ok==false){
                    System.out.println("\nSE VOR AFISA DATELE ELEVULUI: ");
                    ok=true;
                }

                clasa=rs.getInt("clasa");
                ID=rs.getInt("ID");

                AuditService.logAction(Operatii.READ,Clase.ELEV_LICEU);

                return new ElevLiceu(rs.getString("nume"),rs.getString("prenume"),
                        rs.getString("initiala_tata").charAt(0),DBFunctii.toDataNastere(rs.getDate("data_nastere")),
                        DBFunctii.fromDBLocatie(rs.getInt("resedinta")),CNP,rs.getString("numar_telefon_mobil"),
                        rs.getString("nume_scoala"),clasa,rs.getDouble("medie_BAC"),rs.getDouble("nota_matematica"),
                        rs.getDouble("nota_info"),rs.getBoolean("olimpic"),rs.getDouble("nota_admitere_FMI"),
                        fromDBMaterii("el",ID,clasa,8),fromDBNote("el",ID,clasa,8));

            }
            if(ok==false)
                System.out.println("Nu exista o persoana in DB cu CNP-ul specificat!");
        }
        catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
        return null;
    }

    public static void updateDB() throws SQLException {
        Scanner sc = new Scanner(System.in);
        int optiune;

        System.out.println("-------------------------------------");
        System.out.print("VA ROG INTRODUCETI CNP-UL ELEVULUI PENTRU CARE DORITI SA EFECTUATI OPERATII DE TIP UPDATE IN DB: ");
        String CNP ; CNP = sc.nextLine();

        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();

        if(existaInDB(conn,CNP,"el")>0)
        {
            String[] coloane = {"nume", "prenume", "initiala_tata", "data_nastere","", "resedinta", "CNP",
                    "numar_telefon_mobil", "nume_scoala", "clasa", "medie_BAC", "nota_matematica",
                    "nota_info", "olimpic", "nota_admitere_FMI"};

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
                System.out.println("11. Medie BAC.");
                System.out.println("12. Nota Matematica.");
                System.out.println("13. Nota Informatica.");
                System.out.println("14. Olimpic.");
                System.out.println("15. Nota Admitere FMI.");

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
                if(optiune>0 && optiune<16)
                    break;
                else
                    System.out.println("\nOPTIUNE INVALIDA, VA ROG SELECTATI UNA DINTRE CELE DE MAI JOS.\n");
                }

                if(optiune==5){
                    int idLocatie = DBFunctii.selecteazaLocatie(conn,CNP,"el");
                    DBFunctii.updateDBLocatie(idLocatie);
                }
                else
                {
                    String sql = "update el set " + coloane[optiune-1] + " = ? where CNP=?;";
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
                        if(optiune==14){
                            String input;
                            sc.nextLine();
                            while(true){
                                System.out.print("[yes/no] Este elevul olimpic?: ");
                                input = (sc.nextLine()).toLowerCase();
                                if (input.equals("yes")) {st.setBoolean(1,true); break;}
                                else
                                    if (input.equals("no"))
                                        {st.setBoolean(1,false); break;}
                                    else
                                        System.out.print("\n\t!!!Input eronat!!!\n\n");
                            }
                        }
                        else
                            if(optiune>10 && optiune<14 || optiune==15) {
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
                                    DBFunctii.updateVarstaDB(conn,"el",d,CNP);
                                }
                                else
                                    if(optiune==7){
                                        System.out.print("VA ROG INTRODUCETI VALOAREA NOUA PENTRU COLOANA SELECTATA: ");
                                        sc.nextLine();
                                        String valoareNoua=sc.nextLine();
                                        if(DBFunctii.existaCNP(conn,valoareNoua,"el")==0){
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
                    AuditService.logAction(Operatii.UPDATE,Clase.ELEV_LICEU);
                    System.out.println("DATELE AU FOST ACTUALIZATE CU SUCCES!");
                }
        }

        else
            System.out.println("NU EXISTA IN DB ELEV CU CNP-UL SPECIFICAT");
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

        materii=new Materie[clasa-8][21];
        note=new int[clasa-8][21][11];
//        medii=new double[clasa-8];

        int numarMaterii;
        int[][]numarNote = new int[clasa][21];
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
                materii[i][j]=new Materie(i+9);
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

//        medii[clasa-9]=calculMedieAnuala(clasa);
        medieBAC=calculMedieBac();
        notaAdmitereFMI=calculMedieAdmitere();

        intoDB();
        intoDBMaterii_Note("el",numarNote,8);
        System.out.println("ELEVUL A FOST ADAUGAT IN DB!");
    }

    public ElevLiceu(String nume, String prenume, char initialaTata, DataNastere dataNastere,
                     Locatie resedinta, String CNP, String numarTelefonMobil,
                     String numeScoala,
                     int clasa, double medieBAC, double notaMatematica, double notaInfo,
                     boolean olimpic, double notaAdmitereFMI, Materie [][] materii, int [][][] note){
        super(nume,prenume,initialaTata,dataNastere,resedinta,CNP,numarTelefonMobil,numeScoala,clasa);
        this.medieBAC = medieBAC;
        this.notaInfo=notaInfo;
        this.notaMatematica=notaMatematica;
        this.notaAdmitereFMI=notaAdmitereFMI;
        this.olimpic=olimpic;
        this.materii=materii;
        this.note=note;
//        this.medii=medii;
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
        ElevLiceu e = new ElevLiceu();
        System.out.println(fromDB());
    }

}
