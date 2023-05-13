package Utilitare;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {
    private static JDBC instance;
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/proiect pao";
    private String username = "root";
    private String password = "";

    private JDBC() {
        try
        {
            connection = DriverManager.getConnection(url, username, password);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static synchronized JDBC getInstance() {
        if (instance == null) {
            instance = new JDBC();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
