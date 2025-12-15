package com.gymmanagement.service;

import com.gymmanagement.dao.UserDAO;
import com.gymmanagement.model.Admin;
import com.gymmanagement.model.Member;
import com.gymmanagement.model.Trainer;
import com.gymmanagement.model.User;
import com.gymmanagement.model.UserRole;
import com.gymmanagement.util.LoggerUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.logging.Logger;

/**
 * Service class for managing user authentication and account operations.
 * Acts as a facade between the application and the data access layer (DAO).
 * Provides business logic for user registration, login, and account management.
 * Handles password hashing and verification using BCrypt.
 */
public class UserService {

    private final UserDAO userDAO;
    private static final Logger LOGGER = LoggerUtil.getLogger();

    /**
     * Default constructor that initializes the UserDAO.
     */
    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Constructor that accepts a UserDAO instance (useful for dependency injection and testing).
     * 
     * @param userDAO the DAO instance for user database operations
     */
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user account in the system.
     * Validates that the username is unique, hashes the password, and creates the appropriate user type.
     * 
     * @param username the desired username (must be unique)
     * @param plainPassword the plain text password (will be hashed using BCrypt)
     * @param email the user's email address
     * @param phone the user's phone number
     * @param address the user's physical address
     * @param role the user's role (Admin, Trainer, or Member)
     * @return the created User object with the generated ID, or null if registration fails
     */
    public User registerUser(String username,
                             String plainPassword,
                             String email,
                             String phone,
                             String address,
                             UserRole role) {

        // Check if username already exists (must be unique)
        User existing = userDAO.findByUsername(username);
        if (existing != null) {
            LOGGER.warning("Registration failed: username already exists (" + username + ")");
            return null;
        }

        // Hash the password using BCrypt with 12 rounds
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));

        // Create the appropriate user subclass based on role
        User user;
        switch (role) {
            case ADMIN:
                user = new Admin(0, username, hashed, email, phone, address);
                break;
            case TRAINER:
                user = new Trainer(0, username, hashed, email, phone, address);
                break;
            case MEMBER:
            default:
                user = new Member(0, username, hashed, email, phone, address);
                break;
        }

        // Delegate to DAO to persist the user
        User created = userDAO.createUser(user);
        // Log the result of the operation
        if (created != null) {
            LOGGER.info("User registered successfully: " + username + " (" + role + ")");
        } else {
            LOGGER.warning("User registration failed at DAO layer for username: " + username);
        }
        return created;
    }

    /**
     * Authenticates a user by verifying their credentials.
     * Looks up the user by username and validates the password using BCrypt.
     * 
     * @param username the username to authenticate
     * @param plainPassword the plain text password to verify
     * @return the authenticated User object, or null if authentication fails
     */
    public User login(String username, String plainPassword) {
        // Look up the user by username
        User user = userDAO.findByUsername(username);

        // Verify user exists and password matches the stored hash
        if (user == null || !BCrypt.checkpw(plainPassword, user.getPasswordHash())) {
            LOGGER.warning("Login failed: invalid credentials for username: " + username);
            return null;
        }

        LOGGER.info("User logged in successfully: " + username + " (" + user.getRole() + ")");
        return user;
    }

    /**
     * Retrieves all users from the system.
     * 
     * @return a list of all User objects, or empty list if none found
     */
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    /**
     * Retrieves all users with a specific role.
     * 
     * @param role the UserRole to filter by
     * @return a list of User objects with the specified role, or empty list if none found
     */
    public List<User> getUsersByRole(UserRole role) {
        return userDAO.findByRole(role);
    }

    /**
     * Deletes a user account from the system.
     * 
     * @param userId the ID of the user to delete
     * @return true if the user was successfully deleted, false otherwise
     */
    public boolean deleteUser(int userId) {
        return userDAO.deleteUserById(userId);
    }
}
