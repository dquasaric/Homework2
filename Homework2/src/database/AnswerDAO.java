package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import application.Answer;

public class AnswerDAO {
    public static void insertAnswer(Answer answer, int questionId) {
        String sql = "INSERT INTO answers(id, text, question_id) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, answer.getId());
            pstmt.setString(2, answer.getText());
            pstmt.setInt(3, questionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }

    public static List<Answer> getAnswersByQuestionId(int questionId) {
        List<Answer> answers = new ArrayList<>();
        String sql = "SELECT * FROM answers WHERE question_id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
}
