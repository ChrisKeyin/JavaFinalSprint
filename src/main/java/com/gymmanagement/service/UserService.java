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

public class UserService {

    private final UserDAO userDAO;
    private static final Logger LOGGER = LoggerUtil.getLogger();

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User registerUser(String username,
                             String plainPassword,
                             String email,
                             String phone,
                             String address,
                             UserRole role) {

        User existing = userDAO.findByUsername(username);
        if (existing != null) {
            LOGGER.warning("Registration failed: username already exists (" + username + ")");
            return null;
        }

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

    public User login(String username, String plainPassword) {
        User user = userDAO.findByUsername(username);

        if (user == null || !BCrypt.checkpw(plainPassword, user.getPasswordHash())) {
            LOGGER.warning("Login failed: invalid credentials for username: " + username);
            return null;
        }

        LOGGER.info("User logged in successfully: " + username + " (" + user.getRole() + ")");
        return user;
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public List<User> getUsersByRole(UserRole role) {
        return userDAO.findByRole(role);
    }

    public boolean deleteUser(int userId) {
        return userDAO.deleteUserById(userId);
    }
}
