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
 * Data Access Object (DAO) for managing user records in the database.
 * Provides methods to create, retrieve, and delete user accounts.
 * Supports polymorphic user types (Admin, Trainer, Member).
 */
public class UserDAO {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    /**
     * Creates a new user in the database.
     * Inserts user details including hashed password and role information.
     * Retrieves and sets the auto-generated user ID.
     * 
     * @param user the User object containing user details
     * @return the created User object with the generated ID, or null if creation fails
     */
    public User createUser(User user) {
        String sql = "INSERT INTO users (username, password_hash, email, phone_number, address, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set the prepared statement parameters
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getRole().name());

            // Execute the insert and check if rows were affected
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            // Retrieve the auto-generated ID and set it on the user object
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
     * Queries the database for a single user with the specified username.
     * 
     * @param username the username to search for
     * @return the User object if found, or null if not found or on error
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            // Map the result to a User object if found
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
     * Retrieves all users from the database.
     * Results are ordered by user_id.
     * 
     * @return a list of all User objects, or empty list if none found or on error
     */
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY user_id";
        List<User> users = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Map each row from the result set to a User object
            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all users", e);
        }

        return users;
    }

    /**
     * Retrieves all users with a specific role.
     * Filters users by their role (Admin, Trainer, or Member).
     * Results are ordered by user_id.
     * 
     * @param role the UserRole to filter by
     * @return a list of User objects with the specified role, or empty list if none found
     */
    public List<User> findByRole(UserRole role) {
        String sql = "SELECT * FROM users WHERE role = ? ORDER BY user_id";
        List<User> users = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role.name());
            // Map each row from the result set to a User object
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
     * Deletes a user from the database by their ID.
     * 
     * @param userId the ID of the user to delete
     * @return true if the user was successfully deleted, false otherwise
     */
    public boolean deleteUserById(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            // Execute the delete and check if any rows were affected
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
     * Helper method to map a database result set row to a User object.
     * Creates polymorphic User objects (Admin, Trainer, or Member) based on role.
     * 
     * @param rs the ResultSet positioned at the row to map
     * @return a User object (Admin, Trainer, or Member) based on the role column
     * @throws SQLException if there's an error accessing the result set
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

        // Create the appropriate user subclass based on role
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
