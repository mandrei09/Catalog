//Clasa de baza pentru elev
/*
Persoana(DataNastere,Locatie)
    Elev(Locatie,Materie)
 */
package Persoane;
import Utilitare.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public abstract class Elev extends Persoana implements Functii,DBFunctii{

    //Date membre
    protected String numeScoala;

    protected int clasa;
    protected Materie[][] materii;
    protected int[][][] note;

//    protected double[] medii;

    //Constructori


    public Elev(){
        super();
        Scanner sc = new Scanner(System.in);
        System.out.print("Scoala: "); this.numeScoala= sc.nextLine();
    }

    public Elev(String nume, String prenume, char initialaTata, DataNastere dataNastere,
                Locatie resedinta, String CNP, String numarTelefonMobil,
                String numeScoala,int clasa){
        super(nume,prenume,initialaTata,dataNastere,resedinta,CNP,numarTelefonMobil);
        this.clasa=clasa;
        this.numeScoala=numeScoala;
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
        if(m!=null)
            try
            {
                String sql = "INSERT INTO materii_" + table +" (ElevID,MaterieID) VALUES (?,?)";
                PreparedStatement statement = conn.prepareStatement(sql);

                statement.setInt(1, idElev(conn,table));
                statement.setInt(2, m.idMaterie(conn,an));


                int rowsInserted = statement.executeUpdate();
//                if (rowsInserted > 0) {
//                    System.out.println("A new row was inserted successfully!");
//                }
            }
            catch (SQLException e) {
                System.out.println("Error connecting to the database: " + e.getMessage());
            }
    }

    public void intoDBMaterii_Note(String table,int[][] numarNote,int dif){

        int c = clasa-dif;

        //Introducerea materiilor in baza de date.
        for(int i=0;i<c;i++)
            for(int j=0;j<materii[i].length;j++)
                intoDBMaterie(materii[i][j],i+dif+1,table);

        //Introducerea notelor in baza de date.
        for(int i=0;i<c;i++)
            for(int j=0;j<numarNote[i].length;j++)
                for(int k=0;k<numarNote[i][j];k++)
                    intoDBNota(materii[i][j],i+dif+1,note[i][j][k],table);
    }

    protected void intoDBNota(Materie m,int an,int nota,String table){
        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();
        if(m!=null)
            try
            {
                String sql = "INSERT INTO note_"+ table +" (ElevID,MaterieID,Nota) VALUES (?,?,?)";
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

    protected static Materie[][] fromDBMaterii(String table,int ID,int clasa,int dif){
        Scanner sc = new Scanner(System.in);
        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();
        String sql = "select m1.denumire, m1.an from materie m1, materii_" + table + " m2 where m1.id=m2.MaterieID and m2.ElevID=" + ID + " order by m1.id";

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

    protected static int[][][] fromDBNote(String table,int ID,int clasa,int dif){
        Scanner sc = new Scanner(System.in);
        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();
        String sql = "select n.nota,n.MaterieID,m.an from note_" + table + " n, materie m where n.MaterieID=m.id and n.elevID=" +ID+ " order by MaterieID";

        clasa-=dif;

        int[][][] note=new int[clasa][21][11];
        int notaCurenta,idMaterieAnterioara=-1000,anCurent,idMaterieCurenta,numarMaterie=-1;

        int[][] indici = new int[clasa][21];

        try
        {
            ResultSet rs= conn.createStatement().executeQuery(sql);
            while(rs.next()){
                notaCurenta=rs.getInt("nota");
                anCurent=rs.getInt("an");
                idMaterieCurenta=rs.getInt("MaterieID");
                //Daca materia nu s-a schimbat, doar adaugam note
                if(idMaterieAnterioara==idMaterieCurenta)
                    note[anCurent-1][numarMaterie][indici[anCurent-1][numarMaterie]++]=notaCurenta;
                else //Daca materia s-a schimbat, crestem contorul
                    note[anCurent-1][++numarMaterie][indici[anCurent-1][numarMaterie]++]=notaCurenta;
                idMaterieAnterioara=idMaterieCurenta;
            }
        }
        catch (SQLException e) {throw new RuntimeException(e);}
        return note;
    }

    public static void deleteDB(String table) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------------------------");
        System.out.print("VA ROG INTRODUCETI CNP-UL ELEVULUI PE CARE DORITI SA-L STERGETI DIN DB: ");
        String CNP ; CNP = sc.nextLine();

        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();

        if(existaInDB(conn,CNP,table)>0)
        {
            String sql = "delete from " + table + " where CNP=?;";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1,CNP);
            st.executeUpdate();
            System.out.println("ELEVUL A FOST STERS CU SUCCES!");
            if(table.equals("esg"))
                AuditService.logAction(Operatii.DELETE,Clase.ELEV_SCOALA_GENERALA);
            else
                AuditService.logAction(Operatii.DELETE,Clase.ELEV_LICEU);
            System.out.println("-------------------------------------");
        }
        else
            System.out.println("NU EXISTA IN DB ELEV CU CNP-UL SPECIFICAT");
    }

    //Getters
    public int getClasa() {
        return clasa;
    }

    public void setClasa(int clasa) {
        this.clasa = clasa;
    }

    public Materie[][] getMaterii() {
        return materii;
    }

    public int[][][] getNote() {
        return note;
    }

    @Override
    public String toString(){
        return "\nElevul " + super.toString()
                + "Scoala: " + numeScoala + "\n" ;
    }
    //Functie care calculeaza media la o singură materie
    double calculMedie(int[] note) {
        int suma=0;
        for (int j : note) suma += j;
        return suma*1.0/note.length;
    }

    //Functie care calculeaza media anuala
    abstract double calculMedieAnuala(int clasa);

    //Functie care calculeaza media de admitere la FMI.
    abstract double calculMedieAdmitere();
    public static void main(String[] args) {

    }

}
