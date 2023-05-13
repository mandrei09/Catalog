//Clasa pentru a definii obiecte de tip materie, corespunzatoare atat claselor de tip
//elev, cat si celor de tip student.
package Utilitare;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Materie {
    private String denumire;
    private int pondere = 1; //toate materiile de pana la facultate au aceeasi pondere

    public int idMaterie(Connection conn,int an) throws SQLException {
        String query = "SELECT MAX(id) FROM materie " +
                "WHERE lower(denumire) = lower('" + this.denumire + "') AND " +
//                "pondere = " + this.pondere + " AND " +
                "an = " + an + ";";

        ResultSet rs = conn.createStatement().executeQuery(query);
        rs.next();
        return rs.getInt(1);
    }

    public void intoDB(int an) {
        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();
        try {
            String sql = "INSERT INTO materie (denumire,pondere,an) VALUES (?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, this.denumire);
            statement.setInt(2, this.pondere);
            statement.setInt(3, an);

            String query = "SELECT COUNT(id) FROM materie " +
                    "WHERE lower(denumire) = lower('" + this.denumire + "') AND " +
                    "pondere = " + this.pondere + " AND " +
                    "an = " + an + ";";

            ResultSet rs = conn.createStatement().executeQuery(query);
            rs.next();
            int rowsReturned=rs.getInt(1);
            if(rowsReturned==0) {
                int rowsInserted = statement.executeUpdate();
//                if (rowsInserted > 0) {
//                    System.out.println("Materie A new row was inserted successfully!");
//                }
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
        AuditService.logAction(Operatii.CREATE,Clase.MATERIE);
    }

    //Constructori
    public Materie(int an){
        Scanner sc = new Scanner(System.in);
        System.out.print("\tDenumirea materiei: "); this.denumire=sc.nextLine();
        intoDB(an);
    }

    public Materie(String denumire, int pondere,int an) {
        this.denumire = denumire;
        this.pondere = pondere;
    }

    //Getters + setters
    public Materie(String denumire){
        this.denumire=denumire;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setPondere(int pondere) {
        this.pondere = pondere;
    }

    public int getPondere() {
        return pondere;
    }

    @Override
    public String toString(){
        return "\n\tMateria " + denumire + " cu ponderea " + pondere + ".\n";
    }

    public static void main(String[] args){
    }
}
