package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import application.Question;
import application.Answer;

public class QuestionDAO {
    public static void insertQuestion(Question question, int userId) {
        String sql = "INSERT INTO questions(id, text, user_id, resolved_answer_id) VALUES(?, ?, ?, NULL)";
        try (Connection conn = DatabaseHelper.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, question.getId());
            pstmt.setString(2, question.getText());
            pstmt.setInt(3, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }

    public static List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions";
        try (Connection conn = DatabaseHelper.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                questions.add(new Question(rs.getInt("id"), rs.getString("text")));
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return questions;
    }

    public static List<Question> getUnresolvedQuestionsWithAnswers() {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE resolved_answer_id IS NULL";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int questionId = rs.getInt("id");
                Question question = new Question(questionId, rs.getString("text"));
                List<Answer> answers = AnswerDAO.getAnswersByQuestionId(questionId);
                for (Answer a : answers) {
                    question.addAnswer(a);
                }
                questions.add(question);
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return questions;
    }

    public static boolean markAsResolved(int questionId, int answerId, int userId) {
        String checkOwnership = "SELECT user_id FROM questions WHERE id = ?";
        String updateQuery = "UPDATE questions SET resolved_answer_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmtCheck = conn.prepareStatement(checkOwnership);
             PreparedStatement pstmtUpdate = conn.prepareStatement(updateQuery)) {
            
            pstmtCheck.setInt(1, questionId);
            try (ResultSet rs = pstmtCheck.executeQuery()) {
                if (rs.next() && rs.getInt("user_id") == userId) {
                    pstmtUpdate.setInt(1, answerId);
                    pstmtUpdate.setInt(2, questionId);
                    pstmtUpdate.executeUpdate();
                    return true;
                } else {
                    System.out.println("You are not authorized to mark this answer as accepted.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
        return false;
    }

    public static List<Question> searchSimilarQuestions(String queryText) {
        List<Question> similarQuestions = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE text LIKE ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + queryText + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    similarQuestions.add(new Question(rs.getInt("id"), rs.getString("text")));
                }
            }
        } catch (SQLException e) {
            System.out.println("Search failed: " + e.getMessage());
        }
        return similarQuestions;
    }
}
