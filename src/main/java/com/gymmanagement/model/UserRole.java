package com.gymmanagement.model;

/**
 * Enumeration of user roles in the gym management system.
 * Defines the three types of users and their permission levels.
 */
public enum UserRole {
    /**
     * Admin role - Full system access.
     * Permissions: View all users, delete users, manage memberships, manage merchandise.
     */
    ADMIN,
    
    /**
     * Trainer role - Moderate system access.
     * Permissions: Create/update/delete own workout classes, purchase memberships, view merchandise.
     */
    TRAINER,
    
    /**
     * Member role - Limited system access.
     * Permissions: Browse workout classes, purchase memberships, view own membership expenses, view merchandise.
     */
    MEMBER
}
