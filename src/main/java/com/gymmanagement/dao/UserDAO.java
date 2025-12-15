package com.gymmanagement.dao;

import com.gymmanagement.model.Admin;
import com.gymmanagement.model.Member;
import com.gymmanagement.model.Trainer;
import com.gymmanagement.model.User;
import com.gymmanagement.model.UserRole;
import com.gymmanagement.util.DBConnection;
import com.gymmanagement.util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) for managing {@link User} entities in the database.
 * <p>
 * This class provides methods for creating, querying, and deleting users.
 */
public class UserDAO {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    /**
     * Inserts a new user into the database.
     *
     * @param user the {@link User} to create
     * @return the created user with generated ID, or {@code null} if creation failed
     */
    public User createUser(User user) {
        String sql = "INSERT INTO users (username, password_hash, email, phone_number, address, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getRole().name());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    user.setUserId(generatedId);
                }
            }

            LOGGER.info("Created user: " + user.getUsername() + " with role " + user.getRole());
            return user;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating user in the database", e);
            return null;
        }
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return the matching {@link User}, or {@code null} if none exists
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by username: " + username, e);
        }
        return null;
    }

    /**
     * Retrieves all users in the system, ordered by their ID.
     *
     * @return list of all users
     */
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY user_id";
        List<User> users = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all users", e);
        }

        return users;
    }

    /**
     * Returns all users that have a specific role.
     *
     * @param role the {@link UserRole} to filter by
     * @return list of users with the given role
     */
    public List<User> findByRole(UserRole role) {
        String sql = "SELECT * FROM users WHERE role = ? ORDER BY user_id";
        List<User> users = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapRowToUser(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving users by role: " + role, e);
        }

        return users;
    }

    /**
     * Deletes a user from the database based on their ID.
     *
     * @param userId the ID of the user to delete
     * @return {@code true} if a user was deleted; {@code false} otherwise
     */
    public boolean deleteUserById(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                LOGGER.info("Deleted user with id: " + userId);
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user with id: " + userId, e);
        }
        return false;
    }

    /**
     * Maps a single result set row to an appropriate {@link User} subclass
     * based on the value of the {@code role} column.
     *
     * @param rs result set positioned at a user row
     * @return an instance of {@link Admin}, {@link Trainer}, or {@link Member}
     * @throws SQLException if an error occurs while reading from the result set
     */
    private User mapRowToUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String username = rs.getString("username");
        String passwordHash = rs.getString("password_hash");
        String email = rs.getString("email");
        String phone = rs.getString("phone_number");
        String address = rs.getString("address");
        String roleStr = rs.getString("role");

        UserRole role = UserRole.valueOf(roleStr.toUpperCase());

        switch (role) {
            case ADMIN:
                return new Admin(id, username, passwordHash, email, phone, address);
            case TRAINER:
                return new Trainer(id, username, passwordHash, email, phone, address);
            case MEMBER:
            default:
                return new Member(id, username, passwordHash, email, phone, address);
        }
    }
}
