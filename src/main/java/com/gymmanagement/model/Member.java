package com.gymmanagement.model;

public class Member extends User {

    public Member() {
        super();
        setRole(UserRole.MEMBER);
    }

    public Member(int userId, String username, String passwordHash,
                  String email, String phoneNumber, String address) {
        super(userId, username, passwordHash, email, phoneNumber, address, UserRole.MEMBER);
    }
}
