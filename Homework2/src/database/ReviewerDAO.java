package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewerDAO {
    public static void incrementReviewCount(int reviewerId) {
        String sql = "UPDATE reviewers SET review_count = review_count + 1 WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reviewerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    public static List<String> getTopReviewers() {
        List<String> reviewers = new ArrayList<>();
        String sql = "SELECT name FROM reviewers ORDER BY review_count DESC LIMIT 5";
        
        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reviewers.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return reviewers;
    }
}