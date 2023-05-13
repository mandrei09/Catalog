package Utilitare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;

public interface DBFunctii extends General{
    static Locatie fromDBLocatie(int id){
        Locatie l = null;
        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();
        try
        {
            String sql = "SELECT * FROM LOCATIE WHERE ID=" + id ;
            ResultSet rs= conn.createStatement().executeQuery(sql);
            while(rs.next())
            {
                l=new Locatie(
                        rs.getString("tara"),
                        rs.getString("judet"),
                        rs.getString("oras"),
                        rs.getString("strada"),
                        rs.getInt("numar"),
                        rs.getString("cod_postal"));
            }

        }
        catch (SQLException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
        AuditService.logAction(Operatii.READ,Clase.LOCATIE);
        return l;
    }

    static void updateDBLocatie(int ID) throws SQLException {
        Scanner sc = new Scanner(System.in);
        int optiune;

        System.out.println("-------------------------------------");
//        System.out.print("VA ROG INTRODUCETI ID-UL LOCATIEI PENTRU CARE DORITI SA EFECTUATI OPERATII DE TIP UPDATE IN DB: ");
//        int ID; ID=sc.nextInt();

        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();

        if(existaInDB(conn,ID,"locatie")>0)
        {
            String[] coloane = {"tara", "judet", "oras", "strada", "numar", "cod_postal"};
            System.out.println("-------------------------------------");

            while (true)
            {
                System.out.println("VA ROG INTRODUCETI NUMARUL CORESPUNZATOR COLOANEI PE CARE DORITI SA O MODIFICATI: ");
                System.out.println("1. Tara.");
                System.out.println("2. Judetul.");
                System.out.println("3. Orasul.");
                System.out.println("4. Strada.");
                System.out.println("5. Numar.");
                System.out.println("6. Cod Postal.");

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
                if(optiune>0 && optiune<7)
                    break;
                else
                    System.out.println("\nOPTIUNE INVALIDA, VA ROG SELECTATI UNA DINTRE CELE DE MAI JOS.\n");
            }

            String sql = "update locatie set " + coloane[optiune-1] + " = ? where ID=?;";
            PreparedStatement st = conn.prepareStatement(sql);

            System.out.print("VA ROG INTRODUCETI VALOAREA NOUA PENTRU COLOANA SELECTATA: ");
            if(optiune==5){
                int numarNou=sc.nextInt();
                st.setInt(1,numarNou);
            }
            else{
                sc.nextLine();
                String valoareNoua=sc.nextLine();
                st.setString(1,valoareNoua);
            }
            st.setInt(2,ID);
            st.executeUpdate();
            System.out.println("DATELE AU FOST ACTUALIZATE CU SUCCES!");
            AuditService.logAction(Operatii.UPDATE,Clase.LOCATIE);
        }
        else
            System.out.println("NU EXISTA IN DB LOCATIE CU ID-UL SPECIFICAT");
    }

    static int existaInDB(Connection conn, int id,String table) throws SQLException {
        String query = "SELECT COUNT(ID) FROM " + table + " WHERE ID= " + id;
        ResultSet rs = conn.createStatement().executeQuery(query);
        rs.next();
        return rs.getInt(1);
    }

    static int selecteazaLocatie(Connection conn,String CNP,String table) throws SQLException{
        String query = "SELECT resedinta FROM " + table + " WHERE CNP= " + CNP;
        ResultSet rs = conn.createStatement().executeQuery(query);
        rs.next();
        return rs.getInt(1);
    }

    static int existaCNP(Connection conn,String CNP,String table) throws SQLException{
        String query = "SELECT count(id) FROM " + table + " WHERE CNP= " + CNP;
        ResultSet rs = conn.createStatement().executeQuery(query);
        rs.next();
        return rs.getInt(1);
    }

    static int returnCNP(Connection conn,String CNP,String table) throws SQLException{
        String query = "SELECT id FROM " + table + " WHERE CNP= " + CNP;
        ResultSet rs = conn.createStatement().executeQuery(query);
        rs.next();
        return rs.getInt(1);
    }

    static void updateVarstaDB(Connection conn,String table, DataNastere d,String CNP) throws SQLException{
        String sql = "update "+ table +" set varsta = ? where CNP=?;";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1,General.calculVarsta(d));
        st.setString(2,CNP);
        st.executeUpdate();
    }

    static DataNastere toDataNastere(java.sql.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int zi = calendar.get(Calendar.DAY_OF_MONTH);
        int luna = calendar.get(Calendar.MONTH) + 1; // adding 1 because January is 0
        int an = calendar.get(Calendar.YEAR);
        return new DataNastere(zi, luna, an);
    }
}
