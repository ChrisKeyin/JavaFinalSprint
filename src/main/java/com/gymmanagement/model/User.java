package com.gymmanagement.model;

/**
 * Represents a user in the gym management system.
 * This is the base class for all user types (Admin, Trainer, Member).
 * Stores user authentication and profile information.
 */
public class User {
    // User identification and authentication
    private int userId;
    private String username;
    private String passwordHash;  // store only the hash, never plain text!
    
    // User profile information
    private String email;
    private String phoneNumber;
    private String address;
    
    // User role and permissions
    private UserRole role;

    /**
     * Default constructor for User.
     * Creates an empty user instance.
     */
    public User() {
    }

    /**
     * Constructor for User with all properties.
     * 
     * @param userId the unique identifier for the user
     * @param username the login username (must be unique)
     * @param passwordHash the bcrypt hashed password
     * @param email the user's email address
     * @param phoneNumber the user's phone number
     * @param address the user's physical address
     * @param role the user's role (Admin, Trainer, or Member)
     */
    public User(int userId, String username, String passwordHash,
                String email, String phoneNumber, String address, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }

    // Getters and Setters

    /**
     * Gets the unique user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password hash.
     * Note: This returns the hash, not the actual password.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the password hash.
     * Ensure the password is hashed before setting.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Gets the user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the user's phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the user's address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the user's address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the user's role.
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Sets the user's role.
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * Returns a string representation of the User object.
     * Includes all user details in a readable format.
     * Note: Does not include the password hash for security.
     */
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", role=" + role +
                '}';
    }
}
