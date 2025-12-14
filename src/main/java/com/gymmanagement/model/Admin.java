package com.gymmanagement.model;

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
