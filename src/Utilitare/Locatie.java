//Clasa creata pentru a instantia obiecte de tip locatie
package Utilitare;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Locatie implements DBFunctii{
    //Date membre
    private int numar;
    private String strada;
    private String codPostal;
    private String oras,judet, tara;

    public int idLocatie(Connection conn) throws SQLException {
        String query = "SELECT MAX(id) FROM locatie WHERE "
                + "LOWER(tara) = LOWER('" + this.tara + "') AND "
                + "LOWER(judet) = LOWER('" + this.judet + "') AND "
                + "LOWER(oras) = LOWER('" + this.oras + "') AND "
                + "LOWER(strada) = LOWER('" + this.strada + "') AND "
                + "numar = " + this.numar + " AND "
                + "cod_postal = '" + this.codPostal + "'";

        ResultSet rs = conn.createStatement().executeQuery(query);
        rs.next();
        return rs.getInt(1);
    }

    public int intoDB(){
        JDBC dbManager = JDBC.getInstance();
        Connection conn = dbManager.getConnection();
        try
        {
            String sql = "INSERT INTO locatie (tara, judet, oras,strada,numar,cod_postal) VALUES (?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, this.tara);
            statement.setString(2,this.judet);
            statement.setString(3, this.oras);
            statement.setString(4, this.strada);
            statement.setInt(5, this.numar);
            statement.setString(6, this.codPostal);

            //Verificam daca in DB exita o locatie identica cu cea pe care dorim sa o inseram.
            String query = "SELECT COUNT(id) FROM locatie WHERE "
                    + "LOWER(tara) = LOWER('" + this.tara + "') AND "
                    + "LOWER(judet) = LOWER('" + this.judet + "') AND "
                    + "LOWER(oras) = LOWER('" + this.oras + "') AND "
                    + "LOWER(strada) = LOWER('" + this.strada + "') AND "
                    + "numar = " + this.numar + " AND "
                    + "cod_postal = '" + this.codPostal + "'";

            ResultSet rs = conn.createStatement().executeQuery(query);
            rs.next();
            int rowsReturned=rs.getInt(1);
            if(rowsReturned==0) {
                int rowsInserted = statement.executeUpdate();
//                if (rowsInserted > 0) {
//                    System.out.println("Locatie S-a adăugat o noua înregistrare in DB!");
//                }
            }
            AuditService.logAction(Operatii.CREATE,Clase.LOCATIE);
            return idLocatie(conn);
        }
        catch (SQLException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
        return 0;
    }

    //Constructori

    public Locatie(){

    }

    public Locatie(Scanner sc) {
        sc = new Scanner(System.in);
        while(true) {
            System.out.print("\n\tCod Postal: ");
            this.codPostal = sc.nextLine();
            if (!validatorCodPostal(codPostal))
                System.out.print("\n\t!!!Codul postal trebuie sa fie format din 6 cifre!!!\n");
            else
                break;
        }
        System.out.print("\tTara: "); this.tara=sc.nextLine();
        System.out.print("\tJudetul: "); this.judet=sc.nextLine();
        System.out.print("\tOrasul: "); this.oras=sc.nextLine();
        System.out.print("\tStrada: "); this.strada= sc.nextLine();

        while(true){
            try{
                System.out.print("\tNumar: "); this.numar= sc.nextInt();
                break;
            }
            catch (InputMismatchException e) {
                System.out.print("\n\t!!!Trebuie sa introduceti un numar intreg!!!\n\n");
                sc.next();
            }
        }
        intoDB();
    }

    //Codul postal este format din 6 caractere, toate cifre.
    boolean validatorCodPostal(String codPostal) {
        return codPostal.matches("[0-9]{6}");
    }

    //Constructori
    public Locatie(String tara,String judet,String oras,String strada,int numar,String codPostal) {
        this.numar = numar;
        this.strada = strada;
        if (validatorCodPostal(codPostal))
            this.codPostal = codPostal;
        else
            this.codPostal = "NULL";
        this.judet = judet;
        this.tara = tara;
        this.oras=oras;
    }

    public Locatie(Locatie l){
        this.numar = l.numar;
        this.strada = l.strada;
        if (validatorCodPostal(l.codPostal))
            this.codPostal = l.codPostal;
        else
            this.codPostal = "NULL";
        this.judet = l.judet;
        this.tara = l.tara;
        this.oras= l.oras;
    }

    @Override
    public String toString() {
        return "("+codPostal+") - " + tara + ", judetul " + judet + ", " + oras +
            ", pe strada " + strada + ", la numarul " + numar + ".";}

    public static void main(String[] args) throws SQLException {
    }
}
