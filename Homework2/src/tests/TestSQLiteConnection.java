package tests;
import java.sql.Connection;
import java.sql.DriverManager;

public class TestSQLiteConnection {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:hw2.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Connected to SQLite successfully!");
            }
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}
