package com.gymmanagement.model;

/**
 * Represents a Trainer user in the gym management system.
 * Extends the User class with the TRAINER role.
 * Trainers can create and manage workout classes, and purchase memberships.
 */
public class Trainer extends User {

    /**
     * Default constructor for Trainer.
     * Creates a Trainer instance with no specific user data.
     * The role is automatically set to UserRole.TRAINER.
     */
    public Trainer() {
        super();
        setRole(UserRole.TRAINER);
    }

    /**
     * Constructor for Trainer with complete user details.
     * 
     * @param userId the unique identifier for the trainer
     * @param username the login username
     * @param passwordHash the hashed password
     * @param email the email address
     * @param phoneNumber the phone number
     * @param address the physical address
     */
    public Trainer(int userId, String username, String passwordHash,
                   String email, String phoneNumber, String address) {
        super(userId, username, passwordHash, email, phoneNumber, address, UserRole.TRAINER);
    }
}
