package sample.utils.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection databaseLink;
    public static Connection getConnection() {
        try{
            databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/e-shop", "root", "01020300");
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
        return databaseLink;
    }
}
