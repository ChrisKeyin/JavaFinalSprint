package com.gymmanagement.model;

/**
 * Represents an administrative user of the system.
 * <p>
 * Admins can manage other users, view revenue, and manage gym merchandise.
 * This class does not add new fields beyond {@link User} but enforces the ADMIN role.
 */
public class Admin extends User {

    public Admin() {
        super();
        setRole(UserRole.ADMIN);
    }

    public Admin(int userId, String username, String passwordHash,
                 String email, String phoneNumber, String address) {
        super(userId, username, passwordHash, email, phoneNumber, address, UserRole.ADMIN);
    }
}
