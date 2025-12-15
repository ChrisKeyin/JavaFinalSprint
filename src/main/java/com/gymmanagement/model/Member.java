package com.gymmanagement.model;

/**
 * Represents a gym member.
 * <p>
 * Members can browse workout classes, purchase memberships,
 * and view their total membership expenses.
 * This class enforces the MEMBER role.
 */
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
