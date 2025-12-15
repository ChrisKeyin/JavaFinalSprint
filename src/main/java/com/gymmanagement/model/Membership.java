package com.gymmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a membership subscription purchased by a gym member.
 * Stores information about membership type, cost, duration, and the associated member.
 */
public class Membership {

    // Membership properties
    private int membershipId;
    private String membershipType;
    private String membershipDescription;
    private BigDecimal membershipCost;
    private int memberId;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Default constructor for Membership.
     * Creates an empty membership instance.
     */
    public Membership() {
    }

    /**
     * Constructor for Membership with all properties.
     * 
     * @param membershipId the unique identifier for the membership
     * @param membershipType the type of membership (e.g., Monthly, Annual)
     * @param membershipDescription a description of the membership benefits
     * @param membershipCost the cost of the membership
     * @param memberId the ID of the member who purchased this membership
     * @param startDate the date when the membership begins
     * @param endDate the date when the membership expires (can be null for active memberships)
     */
    public Membership(int membershipId, String membershipType, String membershipDescription,
                      BigDecimal membershipCost, int memberId,
                      LocalDate startDate, LocalDate endDate) {
        this.membershipId = membershipId;
        this.membershipType = membershipType;
        this.membershipDescription = membershipDescription;
        this.membershipCost = membershipCost;
        this.memberId = memberId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters

    /**
     * Gets the unique membership ID.
     */
    public int getMembershipId() {
        return membershipId;
    }

    /**
     * Sets the membership ID.
     */
    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    /**
     * Gets the type of membership.
     */
    public String getMembershipType() {
        return membershipType;
    }

    /**
     * Sets the type of membership.
     */
    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    /**
     * Gets the description of membership benefits.
     */
    public String getMembershipDescription() {
        return membershipDescription;
    }

    /**
     * Sets the description of membership benefits.
     */
    public void setMembershipDescription(String membershipDescription) {
        this.membershipDescription = membershipDescription;
    }

    /**
     * Gets the cost of the membership.
     */
    public BigDecimal getMembershipCost() {
        return membershipCost;
    }

    /**
     * Sets the cost of the membership.
     */
    public void setMembershipCost(BigDecimal membershipCost) {
        this.membershipCost = membershipCost;
    }

    /**
     * Gets the ID of the member who owns this membership.
     */
    public int getMemberId() {
        return memberId;
    }

    /**
     * Sets the ID of the member who owns this membership.
     */
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    /**
     * Gets the start date of the membership.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the membership.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the membership.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the membership.
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns a string representation of the Membership object.
     * Includes all membership details in a readable format.
     */
    @Override
    public String toString() {
        return "Membership{" +
                "membershipId=" + membershipId +
                ", membershipType='" + membershipType + '\'' +
                ", membershipDescription='" + membershipDescription + '\'' +
                ", membershipCost=" + membershipCost +
                ", memberId=" + memberId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
