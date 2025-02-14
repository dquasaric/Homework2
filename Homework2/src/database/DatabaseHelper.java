package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:hw2.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC"); // Ensure SQLite driver is loaded
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        }
    }

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

    public static void createTables() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            // Drop old table if exists
            stmt.execute("DROP TABLE IF EXISTS questions;");
            stmt.execute("DROP TABLE IF EXISTS answers;");

            // Create new tables
            String createQuestionsTable = "CREATE TABLE questions ("
                    + "id INTEGER PRIMARY KEY, "
                    + "text TEXT NOT NULL, "
                    + "user_id INTEGER NOT NULL, "
                    + "resolved_answer_id INTEGER, "
                    + "FOREIGN KEY(resolved_answer_id) REFERENCES answers(id))";

            String createAnswersTable = "CREATE TABLE answers ("
                    + "id INTEGER PRIMARY KEY, "
                    + "text TEXT NOT NULL, "
                    + "question_id INTEGER NOT NULL, "
                    + "FOREIGN KEY(question_id) REFERENCES questions(id))";

            stmt.execute(createQuestionsTable);
            stmt.execute(createAnswersTable);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    public static void clearDatabase() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM questions");
            stmt.executeUpdate("DELETE FROM answers");
            System.out.println("Database cleared successfully.");
        } catch (SQLException e) {
            System.out.println("Clear database failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        createTables();  // Ensure tables are created when the program runs
    }
}
