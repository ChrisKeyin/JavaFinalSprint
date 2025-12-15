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
 * Service layer for user-related operations such as registration,
 * login, listing users, and deleting users.
 * <p>
 * This class hides the details of password hashing and DAO usage
 * from the rest of the application.
 */
public class UserService {

    private final UserDAO userDAO;
    private static final Logger LOGGER = LoggerUtil.getLogger();

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user with the given data.
     * <p>
     * This method checks whether the username is already taken, hashes
     * the plain-text password using BCrypt, and then persists the user
     * via the DAO.
     *
     * @param username      desired username
     * @param plainPassword password in plain text
     * @param email         email address
     * @param phone         phone number
     * @param address       physical address
     * @param role          chosen {@link UserRole}
     * @return the created {@link User} or {@code null} if registration failed
     */
    public User registerUser(String username,
                             String plainPassword,
                             String email,
                             String phone,
                             String address,
                             UserRole role) {

        // Check if username is already taken
        User existing = userDAO.findByUsername(username);
        if (existing != null) {
            LOGGER.warning("Registration failed: username already exists (" + username + ")");
            return null;
        }

        // Hash password
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));

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

        User created = userDAO.createUser(user);
        if (created != null) {
            LOGGER.info("User registered successfully: " + username + " (" + role + ")");
        } else {
            LOGGER.warning("User registration failed at DAO layer for username: " + username);
        }
        return created;
    }

    /**
     * Attempts to log a user in by verifying the given password
     * against the stored BCrypt hash.
     *
     * @param username      the username
     * @param plainPassword the plain-text password to check
     * @return the logged-in {@link User}, or {@code null} if credentials are invalid
     */
    public User login(String username, String plainPassword) {
        User user = userDAO.findByUsername(username);

        // If user not found OR password is wrong, log generic failure
        if (user == null || !BCrypt.checkpw(plainPassword, user.getPasswordHash())) {
            LOGGER.warning("Login failed: invalid credentials for username: " + username);
            return null;
        }

        LOGGER.info("User logged in successfully: " + username + " (" + user.getRole() + ")");
        return user;
    }

    /**
     * Returns all users in the system.
     *
     * @return list of users
     */
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    /**
     * Returns all users that have a specific role.
     *
     * @param role the {@link UserRole} to filter by
     * @return list of users with that role
     */
    public List<User> getUsersByRole(UserRole role) {
        return userDAO.findByRole(role);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId ID of the user to delete
     * @return {@code true} if the user was deleted; {@code false} otherwise
     */
    public boolean deleteUser(int userId) {
        return userDAO.deleteUserById(userId);
    }
}
