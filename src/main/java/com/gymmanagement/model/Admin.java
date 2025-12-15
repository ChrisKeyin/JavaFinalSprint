package com.gymmanagement.model;

/**
 * Represents an Admin user in the gym management system.
 * Extends the User class with the ADMIN role.
 * Admins have full system access including user management, membership tracking, and merchandise management.
 */
public class Admin extends User {

    /**
     * Default constructor for Admin.
     * Creates an Admin instance with no specific user data.
     * The role is automatically set to UserRole.ADMIN.
     */
    public Admin() {
        super();
        setRole(UserRole.ADMIN);
    }

    /**
     * Constructor for Admin with complete user details.
     * 
     * @param userId the unique identifier for the admin
     * @param username the login username
     * @param passwordHash the hashed password
     * @param email the email address
     * @param phoneNumber the phone number
     * @param address the physical address
     */
    public Admin(int userId, String username, String passwordHash,
                 String email, String phoneNumber, String address) {
        super(userId, username, passwordHash, email, phoneNumber, address, UserRole.ADMIN);
    }
}
