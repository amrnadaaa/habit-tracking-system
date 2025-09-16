package com.myapp.service;

import java.sql.*;

public class HabitService {

        private Connection connection;

    public HabitService(Connection connection) {
        this.connection = connection;
    }

        public boolean addHabit(String habitName, String email) throws SQLException {

        String findUserQuery = "SELECT id FROM user WHERE email = ?";
        try (PreparedStatement userStmt = connection.prepareStatement(findUserQuery)) {
            userStmt.setString(1, email);
            ResultSet userRs = userStmt.executeQuery();

            if (!userRs.next()) {
                System.out.println(" User not found with email: " + email);
                return false;
            }

            int userId = userRs.getInt("id");


            String checkHabitQuery = "SELECT id FROM habit WHERE id = ? AND name = ?";
            try (PreparedStatement habitStmt = connection.prepareStatement(checkHabitQuery)) {
                habitStmt.setInt(1, userId);
                habitStmt.setString(2, habitName);
                ResultSet habitRs = habitStmt.executeQuery();

                if (habitRs.next()) {
                    System.out.println(" Habit already exists: " + habitName);
                    return false;
                }
            }


            String insertHabitQuery = "INSERT INTO habit (id, name) VALUES (?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertHabitQuery)) {
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, habitName);
                insertStmt.executeUpdate();
                System.out.println(" Habit added: " + habitName + " for user " + email);
            }
        }
            return false;
        }
        public void ViewHapits(String email)
        {

            try {

                String findUserQuery = "SELECT id FROM user WHERE email = ?";
                try (PreparedStatement userStmt = connection.prepareStatement(findUserQuery)) {
                    userStmt.setString(1, email);
                    ResultSet userRs = userStmt.executeQuery();

                    if (!userRs.next()) {
                        System.out.println(" No user found with email: " + email);
                        return;
                    }

                    int userId = userRs.getInt("id");


                    String sql = "SELECT habit.name, report.content, report.created_at " +
                            "FROM user_habit " +
                            "JOIN habit ON user_habit.habit_id = habit.id " +
                            "LEFT JOIN report ON user_habit.report_id = report.id " +
                            "WHERE user_habit.user_id = ?";

                    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setInt(1, userId);
                        ResultSet rs = stmt.executeQuery();

                        System.out.println(" Habits for user: " + email);
                        while (rs.next()) {
                            String habitName = rs.getString("name");
                            String reportContent = rs.getString("content");
                            Timestamp createdAt = rs.getTimestamp("created_at");

                            System.out.println("- Habit: " + habitName);
                            if (reportContent != null) {
                                System.out.println("   Report: " + reportContent);
                                System.out.println("   Date: " + createdAt);
                            } else {
                                System.out.println("   No report yet.");
                            }
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    public void deleteHabit(String email, String habitName) {
        try {
            String findUserQuery = "SELECT id FROM user WHERE email = ?";
            try (PreparedStatement userStmt = connection.prepareStatement(findUserQuery)) {
                userStmt.setString(1, email);
                ResultSet userRs = userStmt.executeQuery();

                if (!userRs.next()) {
                    System.out.println(" No user found with email: " + email);
                    return;
                }

                int userId = userRs.getInt("id");

                String findHabitQuery = "SELECT id FROM habit WHERE name = ?";
                try (PreparedStatement habitStmt = connection.prepareStatement(findHabitQuery)) {
                    habitStmt.setString(1, habitName);
                    ResultSet habitRs = habitStmt.executeQuery();

                    if (!habitRs.next()) {
                        System.out.println(" No habit found with name: " + habitName);
                        return;
                    }

                    int habitId = habitRs.getInt("id");

                    String deleteQuery = "DELETE FROM user_habit WHERE user_id = ? AND habit_id = ?";
                    try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                        deleteStmt.setInt(1, userId);
                        deleteStmt.setInt(2, habitId);

                        int rowsAffected = deleteStmt.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println(" Habit '" + habitName + "' deleted for user " + email);
                        } else {
                            System.out.println(" Habit not linked to user " + email);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
