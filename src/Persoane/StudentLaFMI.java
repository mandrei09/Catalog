/*Clasa de baza pentru Student

Persoana(DataNastere,Locatie)
    Elev(Locatie,Materie)
        ElevScoalaGenerala
        ElevLiceu
    StudentLaFMI(Locatie,Materie)

*/

package Persoane;

import Utilitare.*;

import java.sql.*;
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
//    protected double[] medii;
    double medieAdmitere;
    boolean olimpic;

    protected void intoDB(String table) {
        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();
        try {
            String sql = "INSERT INTO " + table + " (nume, prenume, initiala_tata,data_nastere,varsta,resedinta,CNP,numar_telefon_mobil,medie_admitere,olimpic,an_studiu,seria,grupa) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, this.nume);
            statement.setString(2, this.prenume);
            statement.setString(3, "" + this.initialaTata);
            statement.setDate(4, (Date) new java.sql.Date(this.dataNastere.getDate().getTime()));
            statement.setInt(5,this.varsta);

            int locationID = this.resedinta.intoDB();
            statement.setInt(6, locationID);

            statement.setString(7, this.CNP);
            statement.setString(8, this.numarTelefonMobil);
            statement.setDouble(9, this.medieAdmitere);
            statement.setBoolean(10, this.olimpic);
            statement.setInt(11, this.anStudiu);
            statement.setInt(12, this.seria);
            statement.setInt(13, this.grupa);

            AuditService.logAction(Operatii.CREATE,Clase.ELEV_SCOALA_GENERALA);

            int rowsInserted = statement.executeUpdate();
//            if (rowsInserted > 0) {
//                System.out.println("A new row was inserted successfully!");
//            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public int idElev(Connection conn,String table) throws SQLException {
        String query = "SELECT MAX(ID) from " + table;
        ResultSet rs = conn.createStatement().executeQuery(query);
        rs.next();
        return rs.getInt(1);
    }

    protected void intoDBMaterie(Materie m,int an,String table){
        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();
        int idMaterie=-1;
        if(m!=null){
            try
            {
                String sql = "INSERT INTO materii_" + table +" (StudentID,CursID) VALUES (?,?)";
                PreparedStatement statement = conn.prepareStatement(sql);

                statement.setInt(1, idElev(conn,table));
                idMaterie=m.idMaterie(conn,an);
                statement.setInt(2, idMaterie);

                int rowsInserted = statement.executeUpdate();
//                if (rowsInserted > 0) {
//                    System.out.println("A new row was inserted successfully!");
//                }
            }
            catch (SQLException e) {
                System.out.println("Error connecting to the database: " + e.getMessage());
            }

            try
            {
                String sql = "UPDATE materie SET Pondere = (?) WHERE ID=" + idMaterie;
                PreparedStatement statement = conn.prepareStatement(sql);

                statement.setInt(1,m.getPondere());

                int rowsInserted = statement.executeUpdate();
//                if (rowsInserted > 0) {
//                    System.out.println("A new row was inserted successfully!");
//                }
            }
            catch (SQLException e) {
                System.out.println("Error connecting to the database: " + e.getMessage());
            }
        }
    }

    protected void intoDBNota(Materie m,int an,int nota,String table){
        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();
        if(m!=null)
            try
            {
                String sql = "INSERT INTO note_"+ table +" (StudentID,CursID,Nota) VALUES (?,?,?)";
                PreparedStatement statement = conn.prepareStatement(sql);

                statement.setInt(1, idElev(conn,table));
                statement.setInt(2, m.idMaterie(conn,an));
                statement.setInt(3, nota);

                int rowsInserted = statement.executeUpdate();
//                if (rowsInserted > 0) {
//                    System.out.println("Nota A new row was inserted successfully!");
//                }
            }
            catch (SQLException e) {
                System.out.println("Error connecting to the database: " + e.getMessage());
            }
    }

    public void intoDBCursuri_Note(String table){

        //Introducerea cursurilor in baza de date.
        for(int i=0;i<anStudiu;i++)
            for(int j=0;j<cursuri[i].length;j++)
                intoDBMaterie(cursuri[i][j],i+1,table);

        //Introducerea notelor in baza de date.
        for(int i=0;i<anStudiu;i++)
            for(int j=0;j<cursuri[i].length;j++)
                intoDBNota(cursuri[i][j],i+1,note[i][j],table);
    }

    protected static Materie[][] fromDBMaterii(String table,int ID,int clasa,int dif){
        Scanner sc = new Scanner(System.in);
        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();
        String sql = "select m1.denumire, m1.an from materie m1, materii_" + table + " m2 where m1.id=m2.CursID and m2.StudentID=" + ID + " order by m1.id";

        clasa-=dif;

        Materie[][] materii=new Materie[clasa][21];
        int anCurent; String denumireCurenta;

        int[] indici = new int[clasa];

        try
        {
            ResultSet rs= conn.createStatement().executeQuery(sql);
            while(rs.next()){
                anCurent=rs.getInt("an");
                denumireCurenta=rs.getString("denumire");
                materii[anCurent-1][indici[anCurent-1]++]=new Materie(denumireCurenta,1,anCurent);
            }
        }
        catch (SQLException e) {throw new RuntimeException(e);}
        return materii;
    }

    protected static int[][] fromDBNote(String table,int ID,int anStudiu){
        Scanner sc = new Scanner(System.in);
        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();
        String sql = "select n.nota,n.CursID,m.an from note_" + table + " n, materie m where n.CursID=m.id and n.StudentID=" +ID+ " order by CursID";

        int[][] note=new int[anStudiu][21];
        int notaCurenta,anCurent;

        int[] indici = new int[anStudiu];

        try
        {
            ResultSet rs= conn.createStatement().executeQuery(sql);
            while(rs.next()){
                anCurent=rs.getInt("an");
                notaCurenta=rs.getInt("nota");
                note[anCurent-1][indici[anCurent-1]++]=notaCurenta;
            }
        }
        catch (SQLException e) {throw new RuntimeException(e);}
        return note;
    }

    protected static StudentLaFMI fromDB(String table){
        Scanner sc = new Scanner(System.in);

        System.out.println("-------------------------------------");
        System.out.print("VA ROG INTRODUCETI CNP-UL STUDENTULUI PENTRU CARE DORITI SA EFECTUATI OPERATII DE TIP READ DIN DB: ");
        String CNP ; CNP = sc.nextLine();
        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();
        int ID=0,anStudiu=0;

        try
        {
            boolean ok=false;
            String sql = " SELECT * FROM " + table + " WHERE CNP= " + CNP;
            ResultSet rs= conn.createStatement().executeQuery(sql);

            while(rs.next()) {
                if(ok==false){
                    System.out.println("\nSE VOR AFISA DATELE STUDENTULUI: ");
                    ok=true;
                }

                anStudiu=rs.getInt("an_studiu");
                ID=rs.getInt("ID");

                switch (table) {
                    case "info" -> {
                        AuditService.logAction(Operatii.READ,Clase.FMI_INFO);
                        return new
                                StudentLaFMI_Info(rs.getString("nume"),
                                rs.getString("prenume"),
                                rs.getString("initiala_tata").charAt(0),
                                DBFunctii.toDataNastere(rs.getDate("data_nastere")),
                                DBFunctii.fromDBLocatie(rs.getInt("resedinta")),
                                CNP,
                                rs.getString("numar_telefon_mobil"),
                                rs.getDouble("medie_admitere"),
                                rs.getBoolean("olimpic"),
                                rs.getInt("an_studiu"),
                                rs.getInt("seria"),
                                rs.getInt("grupa"),
                                fromDBMaterii(table, ID, anStudiu, 0),
                                fromDBNote(table, ID, anStudiu));
                    }
                    case "matematica" -> {
                        AuditService.logAction(Operatii.READ,Clase.FMI_MATEMATICA);
                        return new
                                StudentLaFMI_Matematica(rs.getString("nume"),
                                rs.getString("prenume"),
                                rs.getString("initiala_tata").charAt(0),
                                DBFunctii.toDataNastere(rs.getDate("data_nastere")),
                                DBFunctii.fromDBLocatie(rs.getInt("resedinta")),
                                CNP,
                                rs.getString("numar_telefon_mobil"),
                                rs.getDouble("medie_admitere"),
                                rs.getBoolean("olimpic"),
                                rs.getInt("an_studiu"),
                                rs.getInt("seria"),
                                rs.getInt("grupa"),
                                fromDBMaterii(table, ID, anStudiu, 0),
                                fromDBNote(table, ID, anStudiu));
                    }
                    case "cti" -> {
                        AuditService.logAction(Operatii.READ,Clase.FMI_CTI);
                        return new
                                StudentLaFMI_CTI(rs.getString("nume"),
                                rs.getString("prenume"),
                                rs.getString("initiala_tata").charAt(0),
                                DBFunctii.toDataNastere(rs.getDate("data_nastere")),
                                DBFunctii.fromDBLocatie(rs.getInt("resedinta")),
                                CNP,
                                rs.getString("numar_telefon_mobil"),
                                rs.getDouble("medie_admitere"),
                                rs.getBoolean("olimpic"),
                                rs.getInt("an_studiu"),
                                rs.getInt("seria"),
                                rs.getInt("grupa"),
                                fromDBMaterii(table, ID, anStudiu, 0),
                                fromDBNote(table, ID, anStudiu));
                    }
                }
            }
            if(ok==false)
                System.out.println("Nu exista o persoana in DB cu CNP-ul specificat!");
        }
        catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
        return null;
    }

    public static void updateDB(String table) throws SQLException {
        Scanner sc = new Scanner(System.in);
        int optiune;

        System.out.println("-------------------------------------");
        System.out.print("VA ROG INTRODUCETI CNP-UL STUDENTULUI PENTRU CARE DORITI SA EFECTUATI OPERATII DE TIP UPDATE IN DB: ");
        String CNP ; CNP = sc.nextLine();

        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();

        if(existaInDB(conn,CNP,table)>0)
        {
            String[] coloane = {"nume", "prenume", "initiala_tata", "data_nastere","", "resedinta", "CNP",
                    "numar_telefon_mobil", "medie_admitere", "olimpic", "an_studiu", "seria", "grupa"};

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
                System.out.println("9.  Medie admitere.");
                System.out.println("10. Olimpic.");
                System.out.println("11. An studiu.");
                System.out.println("12. Seria.");
                System.out.println("13. Grupa.");

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
                if(optiune>0 && optiune<14)
                    break;
                else
                    System.out.println("\nOPTIUNE INVALIDA, VA ROG SELECTATI UNA DINTRE CELE DE MAI JOS.\n");
            }

            if(optiune==5){
                int idLocatie = DBFunctii.selecteazaLocatie(conn,CNP,table);
                DBFunctii.updateDBLocatie(idLocatie);
            }
            else
            {
                String sql = "update " + table +" set " + coloane[optiune-1] + " = ? where CNP=?;";
                PreparedStatement st = conn.prepareStatement(sql);
                if(optiune>10 || optiune==6){
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
                    if(optiune==10){
                        String input;
                        sc.nextLine();
                        while(true){
                            System.out.print("[yes/no] Este studentul olimpic?: ");
                            input = (sc.nextLine()).toLowerCase();
                            if (input.equals("yes")) {st.setBoolean(1,true); break;}
                            else
                            if (input.equals("no"))
                            {st.setBoolean(1,false); break;}
                            else
                                System.out.print("\n\t!!!INPUT ERONAT!!!\n\n");
                        }
                    }
                    else
                        if(optiune==9) {
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
                                DBFunctii.updateVarstaDB(conn,table,d,CNP);
                            }
                            else
                                if(optiune==7){
                                    System.out.print("VA ROG INTRODUCETI VALOAREA NOUA PENTRU COLOANA SELECTATA: ");
                                    sc.nextLine();
                                    String valoareNoua=sc.nextLine();
                                    if(DBFunctii.existaCNP(conn,valoareNoua,table)==0){
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
                if(table.equals("info"))
                    AuditService.logAction(Operatii.UPDATE,Clase.FMI_INFO);
                else
                    if(table.equals("matematica"))
                        AuditService.logAction(Operatii.UPDATE,Clase.FMI_MATEMATICA);
                    else
                        AuditService.logAction(Operatii.UPDATE,Clase.FMI_CTI);
                System.out.println("DATELE AU FOST ACTUALIZATE CU SUCCES!");
            }
        }
        else
            System.out.println("NU EXISTA IN DB STUDENT CU CNP-UL SPECIFICAT");
    }

    public static void deleteDB(String table) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------------------------");
        System.out.print("VA ROG INTRODUCETI CNP-UL STUDENTULUI PE CARE DORITI SA-L STERGETI DIN DB: ");
        String CNP ; CNP = sc.nextLine();

        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();

        if(existaInDB(conn,CNP,table)>0)
        {
            String sql = "delete from " + table + " where CNP=?;";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1,CNP);
            st.executeUpdate();

            if(table.equals("info"))
                AuditService.logAction(Operatii.DELETE,Clase.FMI_INFO);
            else
            if(table.equals("matematica"))
                AuditService.logAction(Operatii.DELETE,Clase.FMI_MATEMATICA);
            else
                AuditService.logAction(Operatii.DELETE,Clase.FMI_CTI);

            System.out.println("STUDENTUL A FOST STERS CU SUCCES!");
            System.out.println("-------------------------------------");
        }
        else
            System.out.println("NU EXISTA IN DB STUDENT CU CNP-UL SPECIFICAT");
    }
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
