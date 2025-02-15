package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import application.Question;
import application.Answer;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:hw2.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
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
    
    public static List<Answer> getAnswersByQuestionId(int questionId) {
        List<Answer> answers = new ArrayList<>();
        String sql = "SELECT id, text FROM answers WHERE question_id = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, questionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    answers.add(new Answer(rs.getInt("id"), rs.getString("text")));
                }
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }

        return answers;
    }



    // Create Tables (Run on Startup)
    public static void createTables() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS questions (" +
                         "id INTEGER PRIMARY KEY, " +
                         "text TEXT NOT NULL, " +
                         "user_id INTEGER DEFAULT NULL, " +  //Make user_id optional
                         "resolved_answer_id INTEGER)");

            stmt.execute("CREATE TABLE IF NOT EXISTS answers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +  //Use AUTOINCREMENT
                    "text TEXT NOT NULL, " +
                    "question_id INTEGER, " +
                    "FOREIGN KEY(question_id) REFERENCES questions(id))");


            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }


    // Insert Question
    public static boolean insertQuestion(Question question, int userId) {
        if (questionExists(question.getText())) {
            return false;
        }

        String sql = "INSERT INTO questions(id, text, user_id, resolved_answer_id) VALUES(?, ?, ?, NULL)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, question.getId());
            pstmt.setString(2, question.getText());
            pstmt.setInt(3, userId); // Ensure user_id is set
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }


    // Check if Question Exists
    public static boolean questionExists(String text) {
        String sql = "SELECT id FROM questions WHERE text = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, text.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Check failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean questionExistsById(int id) {
        String sql = "SELECT id FROM questions WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Check failed: " + e.getMessage());
            return false;
        }
    }

    // Update Question
    public static void updateQuestion(int id, String newText) {
        String sql = "UPDATE questions SET text = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newText);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    // Remove Question
    public static boolean removeQuestion(int id) {
        String sql = "DELETE FROM questions WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
            return false;
        }
    }

    // Insert Answer
    public static boolean insertAnswer(Answer answer, int questionId) {
        String sql = "INSERT INTO answers(text, question_id) VALUES(?, ?)"; // Removed `id`
        
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, answer.getText());
            pstmt.setInt(2, questionId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }




    // Remove Answer
    public static boolean removeAnswer(int answerId) {
        String sql = "DELETE FROM answers WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, answerId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
            return false;
        }
    }

    // Check if Answer Exists
    public static boolean answerExistsById(int answerId) {
        String sql = "SELECT id FROM answers WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, answerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Check failed: " + e.getMessage());
            return false;
        }
    }

    // Accept Answer
    public static void markAnswerAsAccepted(int answerId) {
        String sql = "UPDATE questions SET resolved_answer_id = ? WHERE id = " +
                "(SELECT question_id FROM answers WHERE id = ?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, answerId);
            pstmt.setInt(2, answerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }
    
 // Get Unresolved Questions (No Accepted Answer)
    public static List<Question> getUnresolvedQuestions() {
        List<Question> unresolvedQuestions = new ArrayList<>();
        String sql = "SELECT id, text FROM questions WHERE resolved_answer_id IS NULL";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                unresolvedQuestions.add(new Question(rs.getInt("id"), rs.getString("text")));
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }

        return unresolvedQuestions;
    }

    // Get Resolved Questions (With an Accepted Answer)
    public static List<Question> getResolvedQuestions() {
        List<Question> resolvedQuestions = new ArrayList<>();
        String sql = "SELECT id, text FROM questions WHERE resolved_answer_id IS NOT NULL";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                resolvedQuestions.add(new Question(rs.getInt("id"), rs.getString("text")));
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }

        return resolvedQuestions;
    }

    // Search for Questions
    public static List<Question> searchQuestions(String keyword) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT id, text FROM questions WHERE LOWER(text) LIKE ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword.toLowerCase() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    questions.add(new Question(rs.getInt("id"), rs.getString("text")));
                }
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }

        return questions;
    }

    // Get All Questions
    public static List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT id, text FROM questions";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                questions.add(new Question(rs.getInt("id"), rs.getString("text")));
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }

        return questions;
    }

    public static void main(String[] args) {
        createTables();  // Ensure tables are created when the program runs
    }
}
