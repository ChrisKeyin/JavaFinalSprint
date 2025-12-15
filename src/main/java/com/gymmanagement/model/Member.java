package com.gymmanagement.model;

/**
 * Represents a Member user in the gym management system.
 * Extends the User class with the MEMBER role.
 * Members can browse workout classes, purchase memberships, and view their expenses.
 */
public class Member extends User {

    /**
     * Default constructor for Member.
     * Creates a Member instance with no specific user data.
     * The role is automatically set to UserRole.MEMBER.
     */
    public Member() {
        super();
        setRole(UserRole.MEMBER);
    }

    /**
     * Constructor for Member with complete user details.
     * 
     * @param userId the unique identifier for the member
     * @param username the login username
     * @param passwordHash the hashed password
     * @param email the email address
     * @param phoneNumber the phone number
     * @param address the physical address
     */
    public Member(int userId, String username, String passwordHash,
                  String email, String phoneNumber, String address) {
        super(userId, username, passwordHash, email, phoneNumber, address, UserRole.MEMBER);
    }
}
