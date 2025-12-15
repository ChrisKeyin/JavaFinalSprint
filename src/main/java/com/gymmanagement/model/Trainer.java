package com.gymmanagement.model;

/**
 * Represents a trainer user of the system.
 * <p>
 * Trainers can manage workout classes and purchase memberships for themselves.
 * This class enforces the TRAINER role.
 */
public class Trainer extends User {

    public Trainer() {
        super();
        setRole(UserRole.TRAINER);
    }

    public Trainer(int userId, String username, String passwordHash,
                   String email, String phoneNumber, String address) {
        super(userId, username, passwordHash, email, phoneNumber, address, UserRole.TRAINER);
    }
}
